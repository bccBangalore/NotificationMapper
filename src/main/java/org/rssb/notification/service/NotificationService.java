package org.rssb.notification.service;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.core.InvalidReferenceException;
import freemarker.template.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rssb.notification.model.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;


@Service
public class NotificationService {

    @Value("${smsNotificationUrl}")
    String smsNotificationUrl;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * This method resolves placeholders in template & call SmsNotification MS
     * intended to trigger SMS to end user
     *
     * @param notificationRequest
     */
    public void processNotification(String notificationRequest) {
        Configuration configuration = prepareConfiguration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());

        JSONObject notificationObj = new JSONObject(notificationRequest);
        try {
        notificationObj.put("callBackStatus", "initiated");
        String templateBody = notificationObj.optString("template");


            JSONArray notificationArray = notificationObj.getJSONArray("sewadars");

            for (int i = 0; i < notificationArray.length(); i++) {
                JSONObject sewadarObj = (JSONObject) notificationArray.get(i);

                String resultingMessage =
                        resolveTemplate(configuration, templateBody, sewadarObj);

                logger.info("Resulting Mapped Msg content to User:" + '\n' + resultingMessage);
                if (sewadarObj.optString("status").equalsIgnoreCase("failure")
                        || sewadarObj.optString("status").equalsIgnoreCase("")) {
                    String smsResponse = callSmsNotification(resultingMessage, sewadarObj.getString("mobileNo"));
                    updateSewadarObject(smsResponse, sewadarObj, notificationObj);

                }
            }
            logger.info("Notification Object Updated:" + notificationObj.toString());

            processCallback(notificationObj);


        } catch (Exception e) {
            logger.error("Exception in Notification Service");
            throw new RuntimeException();
        }


    }

    private void processCallback(JSONObject notificationObj) {
        String status = notificationObj.getString("callBackStatus");
        int retryChances = notificationObj.getInt("numberOfRetries");
        if (status.equalsIgnoreCase("initiated")) {
            notificationObj.put("callBackStatus", "processed");
            logger.info("If cond'n processing callback:" + notificationObj.toString());


            // trigger callback
        } else if (status.equalsIgnoreCase("unprocessed")
                && retryChances > 0) {
            scheduleRetry(notificationObj, retryChances);

        } else {
            notificationObj.put("callBackStatus", "processed");
            logger.info("Else cond'n processing callback:" + notificationObj.toString());

        }
    }

    /**
     * This method implements retry mechanism to send message
     *
     * @param notificationObj
     * @param retryChances
     */
    private void scheduleRetry(JSONObject notificationObj, int retryChances) {

        notificationObj.put("numberOfRetries", --retryChances);
        int delay = notificationObj.getInt("retryDelay");
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                processNotification(notificationObj.toString());

            }
        }, Date.from(LocalDateTime.now().plusSeconds(delay)
                .atZone(ZoneId.systemDefault()).toInstant()));

        logger.info("Retry Scheduled:" + retryChances);

    }

    /**
     * This method updates Sewadar Object with staus & detail of message
     *
     * @param smsResponse
     * @param sewadarObj
     * @param notificationObj
     */

    private void updateSewadarObject(String smsResponse, JSONObject sewadarObj, JSONObject notificationObj) {
        JSONArray sendDetails = sewadarObj.getJSONArray("sendDetail");
        sendDetails.put(smsResponse);

        if (smsResponse.contains("Messages has been sent")) {
            sewadarObj.put("status", "sucess");

            //       updateNotificationRequest(notificationObj);
        } else {
            sewadarObj.put("status", "failure");
            notificationObj.put("callBackStatus", "unprocessed");

            //    updateNotificationRequest(notificationObj);
        }
        sewadarObj.put("retryProcesssed",sewadarObj.optInt("retryProcesssed")+1);
    }

    /**
     * This method resolves template placeholders with values of notification Object
     *
     * @param configuration
     * @param templateBody
     * @param sewadarObj
     */
    private String resolveTemplate(Configuration configuration, String templateBody, JSONObject sewadarObj) throws InvalidReferenceException {

        StringWriter stringWriter = new StringWriter();
        try {
            ObjectMapper notificationMapper = new ObjectMapper();

            Map<String, Object> params = notificationMapper.readValue(sewadarObj.toString(),
                    new TypeReference<Map<String, Object>>() {
                    });

            Template notificationTemplate = new Template("SewaTemplate", templateBody, configuration);

            notificationTemplate.process(params, stringWriter);
        } catch (InvalidReferenceException e) {
            sewadarObj.put("status", "Invalid");
            sewadarObj.put("sendDetail", "Mandatory attribute is missing:" + e.getBlamedExpressionString());
            logger.info(e.getBlamedExpressionString());
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }


    private String callSmsNotification(String resultingMessage, String mobileNo) {
        logger.info("Enter-callSmsNotification");
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setMobileNo(mobileNo);
        smsRequest.setMessage(resultingMessage);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> smsResponse = restTemplate.postForEntity(smsNotificationUrl, smsRequest, String.class);
        return smsResponse.toString();
//        return "Sms was not sent succesfully";
    }

    private Configuration prepareConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        return configuration;
    }


}

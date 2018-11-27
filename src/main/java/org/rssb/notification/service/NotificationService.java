package org.rssb.notification.service;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rssb.notification.domain.NotificationTracker;
import org.rssb.notification.model.SmsRequest;
import org.rssb.notification.repository.NotificationTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


@Service
public class NotificationService {

    @Value("${smsNotificationUrl}")
    String smsNotificationUrl;

    @Autowired
    NotificationTrackerRepository notificationTrackerRepository;

    Logger logger = LoggerFactory.getLogger(NotificationService.class);


    /**
     * This method resolves placeholders in template & call SmsNotification MS
     * intended to trigger SMS to end user
     *
     * @param notificationRequest
     */
    public void processNotification(String notificationRequest) {

        Configuration configuration = prepareConfiguration();
        configuration.setClassForTemplateLoading(NotificationService.class, "/templates");
        try {
            JSONArray notificationArray = new JSONArray(notificationRequest);

            for (int i = 0; i < notificationArray.length(); i++) {
                JSONObject notificationObject = notificationArray.getJSONObject(i);
                Map<String, Object> params = new HashMap<>();
                params.put("sewadar_name", notificationObject.optString("sewadar_name"));
                params.put("attendance_days", notificationObject.optString("attendance_days"));
                params.put("jatha_venue", notificationObject.optString("jatha_venue"));
                params.put("sewa_department", notificationObject.optString("sewa_department"));
                params.put("sewa_startDate", notificationObject.optString("sewa_startDate"));
                params.put("sewa_endDate", notificationObject.optString("sewa_endDate"));

                String templateName = notificationObject.getString("templateId") + ".ftl";
                logger.info("Template Name:" + templateName);
                Template template = configuration.getTemplate(templateName);
                // write the freemarker output to a StringWriter
                StringWriter stringWriter = new StringWriter();
                template.process(params, stringWriter);

                // get the String from the StringWriter
                String resultingMessage = stringWriter.toString();
                logger.info("Resulting Message to User:" + resultingMessage);
                callSmsNotification(resultingMessage, notificationObject.getString("mobileNo"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }


    /**
     * This method resolves placeholders in template & call SmsNotification MS
     * intended to trigger SMS to end user
     *
     * @param notificationRequest
     */
    public void processMappedNotification(String notificationRequest) {
        Configuration configuration = prepareConfiguration();
        configuration.setClassForTemplateLoading(NotificationService.class, "/templates");
        JSONObject notificationObj = new JSONObject(notificationRequest);
        String template = notificationObj.optString("templateId");
        try {
            Class<?> classType = Class.forName("org.rssb.notification.model." + template);

            ObjectMapper notificationMapper = new ObjectMapper();
            JSONArray notificationArray = notificationObj.getJSONArray("sewadars");

            for (int i = 0; i < notificationArray.length(); i++) {

               Object obj = notificationMapper.readValue(notificationArray.get(i).toString(), classType);
                logger.info("Mapped Obj" + obj.toString());

                Map<String, Object> params = notificationMapper.convertValue(obj, Map.class);

                Template notificationTemplate = configuration.getTemplate(template + ".ftl");
                // write the freemarker output to a StringWriter
                StringWriter stringWriter = new StringWriter();
                notificationTemplate.process(params, stringWriter);

                // get the String from the StringWriter
                String resultingMessage = stringWriter.toString();
                logger.info("Resulting Sms content to User:" + '\n'+ resultingMessage);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }

    /**
     * This method resolves placeholders in template & call SmsNotification MS
     * intended to trigger SMS to end user
     * @param notificationRequest
     */
    public void processDynamicMappedNotification(String notificationRequest) {
        Configuration configuration = prepareConfiguration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());

        JSONObject notificationObj = new JSONObject(notificationRequest);
        String templateBody = notificationObj.optString("template");
        try {

            ObjectMapper notificationMapper = new ObjectMapper();
            JSONArray notificationArray = notificationObj.getJSONArray("sewadars");

            for (int i = 0; i < notificationArray.length(); i++) {

                Map<String, Object> params = notificationMapper.readValue(notificationArray.get(i).toString(),
                        new TypeReference<Map<String,Object>>(){});

                Template notificationTemplate=new Template("SewaTemplate",templateBody,configuration);
                StringWriter stringWriter = new StringWriter();
                notificationTemplate.process(params, stringWriter);

                // get the String from the StringWriter
                String resultingMessage = stringWriter.toString();
                logger.info("Resulting Mapped Msg content to User:" + '\n'+ resultingMessage);
            }


        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }


    private void callSmsNotification(String resultingMessage, String mobileNo) {
        logger.info("Enter-callSmsNotification");
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setMobileNo(mobileNo);
        smsRequest.setMessage(resultingMessage);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(smsNotificationUrl, smsRequest, String.class);
    }

    private Configuration prepareConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        return configuration;
    }

    /**
     * This method saves notificationRequest in database
     * @param notificationRequest
     */
    public void saveNotificationRequest(String notificationRequest)
    {

        NotificationTracker notificationTracker=new NotificationTracker();

        notificationTracker.setNotificationPayload(notificationRequest);

        notificationTrackerRepository.save(notificationTracker);
    }
}

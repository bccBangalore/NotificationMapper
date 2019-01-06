package org.rssb.notification.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.core.InvalidReferenceException;
import freemarker.template.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rssb.notification.sms.SmsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static List<SmsEntity> resolveNotificationObject(JSONObject notificationObj) {
        Configuration configuration = prepareConfiguration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        List<SmsEntity> smsEntityList = new ArrayList<>();

        String templateBody = notificationObj.optString("template");
        JSONArray notificationArray = notificationObj.getJSONArray("sewadars");

        for (int i = 0; i < notificationArray.length(); i++) {
            JSONObject sewadarObj = (JSONObject) notificationArray.get(i);
            try {
                String resultingMessage =
                        Utils.resolveTemplate(configuration, templateBody, sewadarObj);

                logger.info("Resulting Mapped Msg content to User:" + '\n' + resultingMessage);
                smsEntityList.add(new SmsEntity(resultingMessage, sewadarObj));
            } catch (InvalidReferenceException e) {
                throw new RuntimeException("Exception while resolving notification object", e);
            }
        }
        return smsEntityList;
    }

    private static Configuration prepareConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        return configuration;
    }

    /**
     * This method updates Sewadar Object with staus & detail of message
     *
     * @param smsResponse
     * @param sewadarObj
     */
    public static void updateSewadarObject(String smsResponse, JSONObject sewadarObj) {
        JSONArray sendDetails = sewadarObj.getJSONArray("sendDetail");
        sendDetails.put(smsResponse);

        if (smsResponse.contains("Messages has been sent")) {
            sewadarObj.put("status", "success");
            //       updateNotificationRequest(notificationObj);
        } else {
            sewadarObj.put("status", "failure");
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
    public static String resolveTemplate(Configuration configuration, String templateBody, JSONObject sewadarObj) throws InvalidReferenceException {

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
}

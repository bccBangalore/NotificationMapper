package org.rssb.notification.service;


import org.json.JSONObject;
import org.rssb.notification.sms.SmsEntity;
import org.rssb.notification.sms.SmsServiceGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Service
public class NotificationService {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private SmsServiceGateway smsServiceGateway;

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Resolves placeholders in template & call smsServiceGateway MS
     * intended to trigger SMS to end user
     *
     * @param notificationRequest
     */
    public void processNotification(String notificationRequest) {
        JSONObject notificationObj = new JSONObject(notificationRequest);
        notificationObj.put("callBackStatus", "initiated");
        List<SmsEntity> smsEntityList = Utils.resolveNotificationObject(notificationObj);
        List<String> responses = smsServiceGateway.sendSms(smsEntityList);

        boolean failurePresent = responses.stream().anyMatch(response -> !response.contains("Messages has been sent"));
        if (failurePresent) {
            notificationObj.put("callBackStatus", "unprocessed");
            logger.info("Notification Object Updated:" + notificationObj.toString());
        }

        processCallback(notificationObj);
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
        taskScheduler.schedule(() -> processNotification(notificationObj.toString()),
                Date.from(LocalDateTime.now().plusSeconds(delay)
                        .atZone(ZoneId.systemDefault()).toInstant()));

        logger.info("Retry Scheduled:" + retryChances);
    }
}

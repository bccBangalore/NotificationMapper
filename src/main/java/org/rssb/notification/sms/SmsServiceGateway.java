package org.rssb.notification.sms;

import org.json.JSONObject;
import org.rssb.notification.model.SmsRequest;
import org.rssb.notification.service.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps the implementation of the SmsService
 */
public final class SmsServiceGateway {

    @Value("${smsNotificationUrl}")
    String smsNotificationUrl;

    Logger logger = LoggerFactory.getLogger(SmsServiceGateway.class);

    /**
     * Send the sms request to the smsService in the correct format and returns a list of responses
     *
     * @param smsEntityList contains the {@link SmsEntity}(s) to send the smss to
     * @return list of responses for every smsEntity
     */
    public List<String> sendSms(List<SmsEntity> smsEntityList) {
        return smsEntityList.stream()
                .filter((response) -> {
                    JSONObject sewadarObj = response.getSewadarObj();
                    return sewadarObj.optString("status").equalsIgnoreCase("failure")
                            || sewadarObj.optString("status").equalsIgnoreCase("");
                })
                .map((smsEntity) -> {
                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setMobileNo(smsEntity.getSewadarObj().getString("mobileNo"));
                    smsRequest.setMessage(smsEntity.getSmsBody());
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> smsResponse = restTemplate.postForEntity(smsNotificationUrl, smsRequest, String.class);
                    Utils.updateSewadarObject(smsResponse.toString(), smsEntity.getSewadarObj());
                    return smsResponse.toString();
                })
                .collect(Collectors.toList());
    }
}

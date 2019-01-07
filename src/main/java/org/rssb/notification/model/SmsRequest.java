package org.rssb.notification.model;

/**
 * SmsRequest body
 */
public class SmsRequest {

    private String mobileNo;
    private String message;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

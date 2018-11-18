package org.rssb.notification.model;

/**
 * The type Sms request.
 */
public class SmsRequest {

    private String mobileNo;
    private String message;

    /**
     * Gets mobile no.
     *
     * @return the mobile no
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets mobile no.
     *
     * @param mobileNo the mobile no
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

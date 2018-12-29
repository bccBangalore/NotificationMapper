package org.rssb.notification.sms;

import org.json.JSONObject;
import org.springframework.lang.NonNull;

public final class SmsEntity {
    private final String smsBody;
    private final JSONObject sewadarObj;

    public SmsEntity(@NonNull  String smsBody, @NonNull JSONObject sewadarObj) {
        this.smsBody = smsBody;
        this.sewadarObj = sewadarObj;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public JSONObject getSewadarObj() {
        return sewadarObj;
    }
}

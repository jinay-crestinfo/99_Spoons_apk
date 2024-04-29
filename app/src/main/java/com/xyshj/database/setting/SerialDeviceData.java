package com.xyshj.database.setting;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SerialDeviceData {
    public long baudRate;
    public String manufactor;
    public String masterVersionNumber;
    public String serialPortAddress;
    public int accessPosition = -1;
    public int protocolVersionNumber = -1;
    public int enable = -1;

    public String toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("accessPosition", this.accessPosition);
            jSONObject.put("manufactor", this.manufactor);
            jSONObject.put("protocolVersionNumber", this.protocolVersionNumber);
            jSONObject.put("serialPortAddress", this.serialPortAddress);
            jSONObject.put("baudRate", this.baudRate);
            jSONObject.put("masterVersionNumber", this.masterVersionNumber);
            jSONObject.put("enable", this.enable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public void formJson(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.accessPosition = jSONObject.optInt("accessPosition");
            this.manufactor = jSONObject.optString("manufactor");
            this.protocolVersionNumber = jSONObject.optInt("protocolVersionNumber");
            this.serialPortAddress = jSONObject.optString("serialPortAddress");
            this.baudRate = jSONObject.optLong("baudRate");
            this.masterVersionNumber = jSONObject.optString("masterVersionNumber");
            this.enable = jSONObject.optInt("enable");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

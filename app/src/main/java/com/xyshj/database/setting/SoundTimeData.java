package com.xyshj.database.setting;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SoundTimeData {
    public String endTime;
    public int soundValue;
    public String startTime;

    public String toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startTime", this.startTime);
            jSONObject.put("endTime", this.endTime);
            jSONObject.put("soundValue", this.soundValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public void formJson(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.startTime = jSONObject.optString("startTime");
            this.endTime = jSONObject.optString("endTime");
            this.soundValue = jSONObject.optInt("soundValue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

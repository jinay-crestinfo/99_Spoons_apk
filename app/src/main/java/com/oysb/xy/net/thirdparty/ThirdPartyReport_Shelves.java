package com.oysb.xy.net.thirdparty;

//import com.iflytek.speech.VoiceWakeuperAidl;

import com.oysb.utils.Loger;

import org.slf4j.Marker;

import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class ThirdPartyReport_Shelves extends ThirdPartyReport {
    public ThirdPartyReport_Shelves() {
        this.FunCode = "1001";
        this.requestMaxCount = 3;
    }

    public void setParams(String str, String str2, String str3, List<HashMap<String, Object>> list) {
        this.PackNO = str;
        this.MachineSN = str2;
        this.jgh = str3;
        StringBuilder sb = new StringBuilder();
        for (HashMap<String, Object> hashMap : list) {
            sb.append(Integer.parseInt(hashMap.get("hdh").toString()) + Marker.ANY_MARKER);
            sb.append(hashMap.get("zt").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("spbm").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdjg").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdcl").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdrl").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("lay").toString());
        }
        this.info = sb.toString();
        this.info = this.info.substring(0, this.info.length() - 1);
        Loger.writeLog("APP", "上报货道参数：" + this.info);
    }
}

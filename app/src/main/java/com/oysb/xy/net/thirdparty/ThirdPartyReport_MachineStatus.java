package com.oysb.xy.net.thirdparty;

//import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_MachineStatus extends ThirdPartyReport {
    public ThirdPartyReport_MachineStatus() {
        this.FunCode = "1002";
    }

    public void setParams(String str, String str2, String str3, Map<Integer, Integer> map) {
        this.PackNO = str;
        this.MachineSN = str2;
        this.SoftVer = str3;
        StringBuilder sb = new StringBuilder();
        for (Integer num : map.keySet()) {
            sb.append(num + Marker.ANY_MARKER + map.get(num));
        }
        sb.substring(0, sb.length() - 1);
        this.info = sb.toString();
        Loger.writeLog("APP", "上报机器状态参数：" + this.info);
    }
}

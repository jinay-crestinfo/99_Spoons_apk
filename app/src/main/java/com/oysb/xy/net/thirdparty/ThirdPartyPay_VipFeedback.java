package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyPay_VipFeedback extends ThirdPartyReport {
    public ThirdPartyPay_VipFeedback() {
        this.FunCode = "5005";
        this.repeatDelay = 3000;
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, String str3, String str4, int i, String str5, int i2) {
        this.MachineSN = str;
        this.PackNO = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(str3 + Marker.ANY_MARKER);
        sb.append(str4 + Marker.ANY_MARKER);
        sb.append(i + Marker.ANY_MARKER);
        sb.append(str5 + Marker.ANY_MARKER);
        sb.append(i2);
        this.info = sb.toString();
    }
}

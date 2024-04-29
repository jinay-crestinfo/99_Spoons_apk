package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyPay_QRcodeFeedback extends ThirdPartyReport {
    public ThirdPartyPay_QRcodeFeedback() {
        this.FunCode = "5003";
        this.repeatDelay = 5000;
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, String str3, int i, String str4, int i2) {
        this.MachineSN = str;
        this.PackNO = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(str3 + Marker.ANY_MARKER);
        sb.append(i + Marker.ANY_MARKER);
        sb.append(str4 + Marker.ANY_MARKER);
        sb.append(i2);
        this.info = sb.toString();
    }
}

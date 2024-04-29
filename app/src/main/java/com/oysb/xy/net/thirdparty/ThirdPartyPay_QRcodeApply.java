package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyPay_QRcodeApply extends ThirdPartyReport {
    public ThirdPartyPay_QRcodeApply() {
        this.FunCode = "5001";
        this.repeatDelay = 5000;
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, int i, int i2, String str3, int i3) {
        this.MachineSN = str;
        this.PackNO = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        sb.append(i2 + Marker.ANY_MARKER);
        sb.append(str3 + Marker.ANY_MARKER);
        sb.append(i3 + Marker.ANY_MARKER);
        this.info = sb.toString();
    }
}

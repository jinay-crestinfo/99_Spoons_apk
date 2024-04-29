package com.oysb.xy.net.thirdparty;

/* loaded from: classes2.dex */
public class ThirdPartyPay_QRcodeQuery extends ThirdPartyReport {
    public ThirdPartyPay_QRcodeQuery() {
        this.FunCode = "5002";
        this.repeatDelay = 4000;
        this.requestMaxCount = 20;
    }

    public void setParam(String str, String str2, String str3) {
        this.MachineSN = str;
        this.PackNO = str2;
        this.info = str3;
    }
}

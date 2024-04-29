package com.oysb.xy.net.thirdparty;

/* loaded from: classes2.dex */
public class ThirdPartyPay_PwApply2 extends ThirdPartyReport {
    public ThirdPartyPay_PwApply2() {
        this.FunCode = "5008";
        this.repeatDelay = 3000;
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, String str3) {
        this.MachineSN = str;
        this.PackNO = str2;
        this.info = str3;
    }
}

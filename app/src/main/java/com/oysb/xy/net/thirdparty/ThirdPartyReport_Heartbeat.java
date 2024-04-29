package com.oysb.xy.net.thirdparty;

/* loaded from: classes2.dex */
public class ThirdPartyReport_Heartbeat extends ThirdPartyReport {
    public ThirdPartyReport_Heartbeat() {
        this.FunCode = "3006";
    }

    public void setParam(String str, String str2) {
        this.MachineSN = str;
        this.PackNO = str2;
        this.requestMaxCount = 1;
    }
}

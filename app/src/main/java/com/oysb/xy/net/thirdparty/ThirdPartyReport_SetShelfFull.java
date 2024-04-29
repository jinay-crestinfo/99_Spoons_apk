package com.oysb.xy.net.thirdparty;

/* loaded from: classes2.dex */
public class ThirdPartyReport_SetShelfFull extends ThirdPartyReport {
    public ThirdPartyReport_SetShelfFull() {
        this.FunCode = "2005";
    }

    public void setParams(String str, String str2) {
        this.PackNO = str;
        this.MachineSN = str2;
        this.info = "1*" + System.currentTimeMillis();
    }
}

package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_DoorStatus extends ThirdPartyReport {
    public ThirdPartyReport_DoorStatus() {
        this.FunCode = "3001";
    }

    public void setParams(String str, String str2, int i, int i2) {
        this.PackNO = str;
        this.MachineSN = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        sb.append(i2 + Marker.ANY_MARKER);
        sb.append(System.currentTimeMillis());
        this.info = sb.toString();
    }
}
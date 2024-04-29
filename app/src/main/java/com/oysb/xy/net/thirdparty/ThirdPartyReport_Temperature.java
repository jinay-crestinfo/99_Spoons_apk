package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_Temperature extends ThirdPartyReport {
    public ThirdPartyReport_Temperature() {
        this.FunCode = "3003";
    }

    public void setParams(String str, String str2, int i, int i2, int i3) {
        this.PackNO = str;
        this.MachineSN = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        sb.append(i2 + Marker.ANY_MARKER);
        StringBuilder sb2 = new StringBuilder();
        if (i2 != 0) {
            i3 = 0;
        }
        sb2.append(i3);
        sb2.append(Marker.ANY_MARKER);
        sb.append(sb2.toString());
        sb.append(System.currentTimeMillis());
        this.info = sb.toString();
    }
}

package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_SetShelDropCheck extends ThirdPartyReport {
    public ThirdPartyReport_SetShelDropCheck() {
        this.FunCode = "2006";
    }

    public void setParams(String str, String str2, int i, int i2, int i3) {
        this.PackNO = str;
        this.MachineSN = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        StringBuilder sb2 = new StringBuilder();
        if (i == 3) {
            i2 = 0;
        }
        sb2.append(i2);
        sb2.append(Marker.ANY_MARKER);
        sb.append(sb2.toString());
        sb.append(i3 + Marker.ANY_MARKER);
        sb.append(System.currentTimeMillis());
        this.info = sb.toString();
    }
}

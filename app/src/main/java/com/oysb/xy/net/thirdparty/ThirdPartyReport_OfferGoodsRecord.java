package com.oysb.xy.net.thirdparty;

import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_OfferGoodsRecord extends ThirdPartyReport {
    public ThirdPartyReport_OfferGoodsRecord() {
        this.FunCode = "4002";
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, int i, int i2, int i3, String str3, int i4, int i5, int i6, String str4) {
        this.MachineSN = str;
        this.PackNO = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        sb.append(i2 + Marker.ANY_MARKER);
        sb.append(i3 + Marker.ANY_MARKER);
        sb.append(str3 + Marker.ANY_MARKER);
        sb.append(i4 + Marker.ANY_MARKER);
        sb.append(i5 + Marker.ANY_MARKER);
        sb.append(i6 + Marker.ANY_MARKER);
        sb.append(str4 + Marker.ANY_MARKER);
        sb.append(System.currentTimeMillis());
        this.info = sb.toString();
    }
}

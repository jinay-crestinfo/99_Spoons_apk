package com.oysb.xy.net.thirdparty;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThirdPartyReport_MoneyBalances extends ThirdPartyReport {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public ThirdPartyReport_MoneyBalances() {
        this.FunCode = "4001";
        this.requestMaxCount = 3;
    }

    public void setParam(String str, String str2, int i, int i2, int i3, int i4, int i5) {
        this.MachineSN = str;
        this.PackNO = str2;
        StringBuilder sb = new StringBuilder();
        sb.append(i + Marker.ANY_MARKER);
        sb.append(i2 + Marker.ANY_MARKER);
        sb.append(i3 + Marker.ANY_MARKER);
        sb.append(i4 + Marker.ANY_MARKER);
        sb.append(i5 + Marker.ANY_MARKER);
        sb.append(this.sdf.format(new Date()));
        this.info = sb.toString();
    }
}

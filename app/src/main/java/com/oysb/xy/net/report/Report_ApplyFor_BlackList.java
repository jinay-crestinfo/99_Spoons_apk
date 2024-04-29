package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public class Report_ApplyFor_BlackList extends Report {
    private static final long serialVersionUID = 1;

    public Report_ApplyFor_BlackList() {
        this.dataType = (byte) 16;
        this.bizType = (byte) 68;
        this.lostAble = true;
    }

    public void setParams(String str) {
        String format = String.format("%010d", Long.valueOf(Long.parseLong(str)));
        this.data = new byte[5];
        for (int i = 0; i < 5; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("0x");
            int i2 = i * 2;
            sb.append(format.substring(i2, i2 + 2));
            this.data[i] = (byte) (ObjectHelper.string2Short(sb.toString()) + 17);
        }
    }
}

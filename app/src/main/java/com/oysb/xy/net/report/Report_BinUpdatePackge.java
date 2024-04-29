package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public class Report_BinUpdatePackge extends Report {
    private static final long serialVersionUID = 1;

    public Report_BinUpdatePackge() {
        this.dataType = (byte) 34;
        this.bizType = (byte) 113;
        this.lostAble = true;
    }

    public void setParams(int i, String str, String str2) {
        byte[] bytes = str2.getBytes();
        this.data = new byte[bytes.length + 6];
        ObjectHelper.updateBytes(this.data, i, 0, 2);
        ObjectHelper.updateBytes(this.data, str.getBytes(), 0, 2, 4);
        ObjectHelper.updateBytes(this.data, bytes, 0, 6, bytes.length);
    }
}

package com.shj.biz.tradrecord;

import java.io.Serializable;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class RecordData implements Serializable {
    public HashMap<Integer, TypeRecordData> TypeRecordDataMap;
    public String date;
    public String exp;
    public int frequency;
    public long money;
}

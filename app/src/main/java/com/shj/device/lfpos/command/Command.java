package com.shj.device.lfpos.command;

import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public class Command {
    public static byte ACK = 6;
    public static byte ETX = 3;
    public static byte NAK = 21;
    public static byte STX = 2;
    protected byte[] CONT;
    private byte TYPE;
    protected byte[] data;
    private Object obj;
    private long time;
    private String tradType;
    public static byte[] FS = {ClosedCaptionCtrl.MISC_CHAN_2};
    public static String strFS = new String(new byte[]{ClosedCaptionCtrl.MISC_CHAN_2});
    public static int dataOffset = 11;
    private boolean inited = false;
    private byte PATH = 3;
    int ID = 0;
    private int sendRepeatCount = 0;
    private long expiredTime = 60000;

    public void doCommand() {
    }

    public boolean init(byte[] bArr) {
        this.data = bArr;
        byte[] bArr2 = new byte[ObjectHelper.intFromBytes(bArr, 1, 2) - 8];
        this.CONT = bArr2;
        ObjectHelper.updateBytes(bArr2, bArr, dataOffset, 0, bArr2.length);
        this.TYPE = (byte) ObjectHelper.intFromBytes(bArr, 4, 1);
        this.ID = ObjectHelper.intFromBytes(bArr, 5, 6);
        return true;
    }

    private static byte[] createCmd(byte b, byte b2, byte[] bArr, byte[] bArr2) {
        int length = bArr2.length + 13;
        byte[] bArr3 = new byte[length];
        ObjectHelper.updateBytes(bArr3, (int) STX, 0, 1);
        int i = length - 2;
        ObjectHelper.updateBytes(bArr3, (int) ETX, i, 1);
        ObjectHelper.updateBytes(bArr3, length - 5, 1, 2);
        ObjectHelper.updateBytes(bArr3, (int) b, 3, 1);
        ObjectHelper.updateBytes(bArr3, (int) b2, 4, 1);
        ObjectHelper.updateBytes(bArr3, bArr, 5, 6);
        ObjectHelper.updateBytes(bArr3, bArr2, 11, bArr2.length);
        ObjectHelper.updateBytes(bArr3, (int) ObjectHelper.computerXor(bArr3, 1, i), length - 1, 1);
        return bArr3;
    }

    public boolean isValid() {
        return System.currentTimeMillis() - this.time < this.expiredTime;
    }

    public byte[] getRawCommand() {
        byte[] createCmd = createCmd(this.PATH, this.TYPE, ObjectHelper.int2Bytes16_2(getID(), 6), this.CONT);
        this.data = createCmd;
        return createCmd;
    }

    public void setID(int i) {
        this.ID = i;
    }

    public int getID() {
        return this.ID;
    }

    public boolean isInited() {
        return this.inited;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public long getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(long j) {
        this.expiredTime = j;
    }

    public int getSendRepeatCount() {
        return this.sendRepeatCount;
    }

    public void addSendRepeatCount() {
        this.sendRepeatCount++;
    }

    public void setPath(byte b) {
        this.PATH = b;
    }

    public void setType(byte b) {
        this.TYPE = b;
    }

    public String getTradType() {
        return this.tradType;
    }

    public void setTradType(String str) {
        this.tradType = str;
    }

    public void setParams(String... strArr) {
        String str = "";
        for (String str2 : strArr) {
            str = str + str2 + strFS;
        }
        this.CONT = str.getBytes();
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

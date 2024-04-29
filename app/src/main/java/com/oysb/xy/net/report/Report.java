package com.oysb.xy.net.report;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.xy.i.ReportListener;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import kotlin.UByte;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.spi.Configurator;

/* loaded from: classes2.dex */
public abstract class Report implements Serializable {
    private static final long serialVersionUID = -5578603567833947735L;
    byte[] data;
    transient ReportListener listener;
    transient Object obj;
    Date pushTime;
    private String retCode;
    private String retMsg;
    Date sendTime;
    ReportState state;
    Date updateTime;
    byte dataType = 0;
    byte serialNumber = 0;
    byte bizType = 0;
    byte check = 0;
    boolean needAck = true;
    boolean lostAble = false;
    private boolean doNotStoreInDb = false;
    boolean mastReConnectOnTimeOut = true;
    int needResendCount = 1;
    int reSendCount = 0;
    byte[] rawData = null;
    private AckResult result = null;
    private String SN = UUID.randomUUID().toString().replace("-", "");

    public boolean shuldReConnectOnTimeOut() {
        return this.mastReConnectOnTimeOut;
    }

    public void setLostAble(boolean z) {
        this.lostAble = z;
    }

    public byte getDataType() {
        return this.dataType;
    }

    public byte getBizType() {
        return this.bizType;
    }

    public synchronized byte[] getRawData() {
        byte[] bArr = this.rawData;
        if (bArr != null) {
            return bArr;
        }
        updateSerialNumber();
        byte[] bArr2 = this.data;
        int i = bArr2.length > 250 ? 2 : 1;
        byte b = this.dataType;
        if (b != 25 && i > 1) {
            b = (byte) (b + 208);
        }
        int i2 = i + 4;
        int length = bArr2.length + i2;
        byte[] bArr3 = new byte[length];
        int i3 = 0;
        ObjectHelper.updateBytes(bArr3, (int) b, 0, 1);
        ObjectHelper.updateBytes(bArr3, (int) this.serialNumber, 1, 1);
        ObjectHelper.updateBytes(bArr3, i2 + this.data.length, 2, i);
        int i4 = 2 + i;
        ObjectHelper.updateBytes(bArr3, (int) this.bizType, i4, 1);
        byte[] bArr4 = this.data;
        ObjectHelper.updateBytes(bArr3, bArr4, i4 + 1, bArr4.length);
        this.check = (byte) 0;
        while (true) {
            int i5 = length - 1;
            if (i3 < i5) {
                this.check = (byte) (this.check + ((short) (bArr3[i3] & UByte.MAX_VALUE)));
                i3++;
            } else {
                bArr3[i5] = this.check;
                this.rawData = bArr3;
                return bArr3;
            }
        }
    }

    public void updateByRawData(byte[] bArr) {
        this.serialNumber = (byte) ObjectHelper.intFromBytes(bArr, 1, 1);
        if (ObjectHelper.intFromBytes(bArr, 2, 1) == bArr.length) {
            byte[] bArr2 = new byte[bArr.length - 5];
            this.data = bArr2;
            System.arraycopy(bArr, 4, bArr2, 0, bArr2.length);
        } else {
            byte[] bArr3 = new byte[bArr.length - 6];
            this.data = bArr3;
            System.arraycopy(bArr, 5, bArr3, 0, bArr3.length);
        }
    }

    public void setReportListener(ReportListener reportListener) {
        this.listener = reportListener;
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    public Object getObject() {
        return this.obj;
    }

    public boolean needAck() {
        return this.needAck;
    }

    public boolean lostAble() {
        return this.lostAble;
    }

    public boolean acceptAck(byte[] bArr) {
        try {
            if (ObjectHelper.intFromBytes(bArr, 1, 1) != ((short) (this.serialNumber & UByte.MAX_VALUE))) {
                throw new Exception("ACK错误");
            }
            AckResult parseDataFromAck = parseDataFromAck(this, bArr);
            setResult(parseDataFromAck);
            setRetCode(parseDataFromAck.getCode().toString());
            setRetMsg(parseDataFromAck.getData());
            return parseDataFromAck.getCode().getIndex() == 0;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return false;
        }
    }

    public void onSuccess() {
        ReportListener reportListener = this.listener;
        if (reportListener != null) {
            reportListener.onReportResult(this);
        }
    }

    public void onFailed() {
        ReportListener reportListener = this.listener;
        if (reportListener != null) {
            reportListener.onReportResult(this);
        }
    }

    public void setSN(String str) {
        this.SN = str;
    }

    public void addResendCount() {
        this.reSendCount++;
    }

    public void setNeedResendCount(int i) {
        this.needResendCount = i;
    }

    public void cancelReport() {
        this.reSendCount = this.needResendCount;
    }

    public boolean needResend() {
        return this.reSendCount < this.needResendCount;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date date) {
        this.sendTime = date;
    }

    public int getReportCode() {
        return ((short) (this.dataType & UByte.MAX_VALUE)) * ((short) (this.bizType & UByte.MAX_VALUE));
    }

    public ReportState getState() {
        return this.state;
    }

    public void setState(ReportState reportState) {
        this.state = reportState;
    }

    public String getSN() {
        return this.SN;
    }

    public synchronized void updateSerialNumber() {
        if (this.serialNumber != 0) {
            return;
        }
        this.serialNumber = (byte) (getNextSerial().intValue() % 256);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Integer getNextSerial() {
        /*
            r5 = this;
            byte r0 = r5.dataType
            r1 = 255(0xff, float:3.57E-43)
            if (r0 == r1) goto L16
            r1 = 31
            if (r0 == r1) goto L16
            r1 = 16
            if (r0 == r1) goto L16
            r1 = 23
            if (r0 != r1) goto L13
            goto L16
        L13:
            java.lang.String r0 = "report_g_serial_1"
            goto L18
        L16:
            java.lang.String r0 = "report_g_serial_2"
        L18:
            r1 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            com.oysb.utils.cache.ACache r3 = com.oysb.utils.cache.CacheHelper.getFileCache()     // Catch: java.lang.Exception -> L3c
            java.lang.Object r3 = r3.getAsObject(r0)     // Catch: java.lang.Exception -> L3c
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Exception -> L3c
            if (r3 != 0) goto L31
            r2 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Exception -> L2f
            goto L32
        L2f:
            r2 = move-exception
            goto L40
        L31:
            r2 = r3
        L32:
            int r3 = r2.intValue()     // Catch: java.lang.Exception -> L3c
            int r3 = r3 + r1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Exception -> L3c
            goto L44
        L3c:
            r3 = move-exception
            r4 = r3
            r3 = r2
            r2 = r4
        L40:
            com.oysb.utils.Loger.safe_inner_exception_catch(r2)
            r2 = r3
        L44:
            int r3 = r2.intValue()
            int r3 = r3 % 256
            if (r3 != 0) goto L55
            int r2 = r2.intValue()
            int r2 = r2 + r1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
        L55:
            com.oysb.utils.cache.ACache r1 = com.oysb.utils.cache.CacheHelper.getFileCache()
            r1.put(r0, r2)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.xy.net.report.Report.getNextSerial():java.lang.Integer");
    }

    public int getSerialNumber() {
        updateSerialNumber();
        return this.serialNumber & UByte.MAX_VALUE;
    }

    public String getRetMsg() {
        return this.retMsg;
    }

    public void setRetMsg(String str) {
        this.retMsg = str;
    }

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String str) {
        this.retCode = str;
    }

    public Date getAppendTime() {
        return this.pushTime;
    }

    public void setAppendTime(Date date) {
        this.pushTime = date;
    }

    public static int parseSerialNumberFromAck(byte[] bArr) {
        return ObjectHelper.intFromBytes(bArr, 1, 1);
    }

    public static ReportAckCode parseAckCode(byte[] bArr) {
        int i;
        byte[] bArr2 = new byte[1];
        System.arraycopy(bArr, 3, bArr2, 0, 1);
        try {
            i = Integer.parseInt(new String(bArr2));
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            i = -1;
        }
        return ReportAckCode.int2Enum(i);
    }

    public static String parseAckData(byte[] bArr) {
        try {
            int length = bArr.length - 4;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 3, bArr2, 0, length);
            return new String(bArr2);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return "";
        }
    }

    public static AckResult parseDataFromAck(Report report, byte[] bArr) {
        AckResult ackResult = new AckResult();
        try {
            byte b = report.dataType;
            if (b == -1) {
                ackResult.setCode(ReportAckCode.int2Enum(0));
            } else if (b == 31) {
                if (bArr.length > 5) {
                    ackResult.setCode(ReportAckCode.int2Enum(0));
                    int length = bArr.length - 4;
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 3, bArr2, 0, length);
                    ackResult.setData(String.format("%02d", Integer.valueOf(bArr2[0] - 1)) + String.format("%02d", Integer.valueOf(bArr2[1] - 1)) + String.format("%02d", Integer.valueOf(bArr2[2] - 1)) + String.format("%02d", Integer.valueOf(bArr2[3] - 1)) + String.format("%02d", Integer.valueOf(bArr2[4] - 1)) + String.format("%02d", Integer.valueOf(bArr2[5] - 1)));
                } else {
                    ackResult.setCode(parseAckCode(bArr));
                }
            } else {
                ackResult.setCode(parseAckCode(bArr));
                ackResult.setData(parseAckData(bArr));
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            ackResult.setCode(ReportAckCode.int2Enum(1));
        }
        return ackResult;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date date) {
        this.updateTime = date;
    }

    public static byte[] createTimeBytes(Date date) {
        if (date.getYear() == 1970) {
            date = (Date) CacheHelper.getFileCache().getAsObject("LASTCONNECTTIME");
        }
        String format = DateUtil.format(date, "yyMMddHHmmss");
        return new byte[]{(byte) (Short.parseShort(format.substring(0, 2)) + 1), (byte) (Short.parseShort(format.substring(2, 4)) + 1), (byte) (Short.parseShort(format.substring(4, 6)) + 1), (byte) (Short.parseShort(format.substring(6, 8)) + 1), (byte) (Short.parseShort(format.substring(8, 10)) + 1), (byte) (Short.parseShort(format.substring(10, 12)) + 1)};
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ObjectHelper.hex2String(this.dataType));
        sb.append(StringUtils.SPACE);
        sb.append(ObjectHelper.hex2String(this.bizType));
        sb.append(" data:");
        byte[] bArr = this.data;
        sb.append(bArr != null ? new String(bArr) : Configurator.NULL);
        return sb.toString();
    }

    public AckResult getResult() {
        return this.result;
    }

    public void setResult(AckResult ackResult) {
        this.result = ackResult;
    }

    public boolean doNotStoreInDb() {
        return this.doNotStoreInDb;
    }

    public void setDoNotStoreInDb(boolean z) {
        this.doNotStoreInDb = z;
    }
}

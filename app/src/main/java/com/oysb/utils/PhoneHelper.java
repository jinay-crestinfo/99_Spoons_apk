package com.oysb.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class PhoneHelper {
    private static final int UNSUPPORTED = -1;
    int uid;
    private WeakReference<Context> wkContext;
    String phone = "-";
    String imei = "-";
    String imsi = "-";
    String iccid = "-";
    String simName = "-";
    long lastSumTraffic = 0;
    long curMonthTraffic = 0;
    long curDayTraffic = 0;

    public String getIccid() {
        String str = this.iccid;
        if (str == null || str.length() <= 2) {
            return AndroidSystem.getSystemSerial();
        }
        return this.iccid;
    }

    public String getSimName() {
        return this.simName;
    }

    public String getPhone() {
        String str = this.phone;
        if (str == null || str.length() <= 2) {
            this.phone = getImsi();
        }
        String str2 = this.phone;
        if (str2 == null || str2.length() <= 2) {
            this.phone = getImei();
        }
        return this.phone;
    }

    public String getImei() {
        return this.imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public long getCacheData(String str) {
        try {
            return ((Long) CacheHelper.getFileCache().getAsObject(str)).longValue();
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return 0L;
        }
    }

    public void init(Context context) {
        Loger.writeLog("PHONE", "正在初始化电话卡模块");
        this.wkContext = new WeakReference<>(context);
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            return;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String line1Number = telephonyManager.getLine1Number();
            this.phone = line1Number;
            if (line1Number == null) {
                this.phone = "-";
            }
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            this.iccid = simSerialNumber;
            if (simSerialNumber == null) {
                this.iccid = "-";
            }
            String deviceId = telephonyManager.getDeviceId();
            this.imei = deviceId;
            if (deviceId == null || deviceId.equals("-")) {
                this.imei = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id");
            }
            if (this.imei == null) {
                this.imei = "-";
            }
            String subscriberId = telephonyManager.getSubscriberId();
            this.imsi = subscriberId;
            if (subscriberId == null) {
                this.imsi = "-";
            }
            String simOperatorName = telephonyManager.getSimOperatorName();
            this.simName = simOperatorName;
            if (simOperatorName == null) {
                this.simName = "-";
            }
            Loger.writeLog("PHONE", "phone:" + this.phone + " imei:" + this.imei + "imsi:" + this.imsi + " iccid:" + this.iccid);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            e.printStackTrace();
        }
        try {
            this.uid = context.getPackageManager().getApplicationInfo(context.getPackageName(), 1).uid;
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
        }
        try {
            this.lastSumTraffic = (((Long) CacheHelper.getFileCache().getAsObject("xy_lastSumTraffic")).longValue() - getRcvTraffic()) - getSndTraffic();
        } catch (Exception e3) {
            Loger.safe_inner_exception_catch(e3);
            this.lastSumTraffic = 0L;
        }
    }

    public void adjustTrafficInfo(long j) {
        String str;
        String str2 = "";
        try {
            str = CacheHelper.getFileCache().getAsString("PHONE_phone");
        } catch (Exception unused) {
            str = "";
        }
        String str3 = "-";
        if (str == null || str.length() == 0) {
            str = "-";
        }
        try {
            str2 = CacheHelper.getFileCache().getAsString("PHONE_imsi");
        } catch (Exception unused2) {
        }
        if (str2 != null && str2.length() != 0) {
            str3 = str2;
        }
        if (this.phone.equalsIgnoreCase(str) && this.imsi.equalsIgnoreCase(str3)) {
            return;
        }
        CacheHelper.getFileCache().put("PHONE_imsi", this.imsi);
        CacheHelper.getFileCache().put("PHONE_phone", this.phone);
        CacheHelper.getFileCache().put("xy_curMonthStartTraffic", Long.valueOf(j));
        CacheHelper.getFileCache().put("xy_curDayStartTraffic", Long.valueOf(j));
    }

    public long updateTrafficInfo() {
        long j;
        long rcvTraffic = getRcvTraffic();
        long sndTraffic = getSndTraffic();
        long j2 = -1;
        if (rcvTraffic != -1 && sndTraffic != -1) {
            j2 = this.lastSumTraffic + (rcvTraffic == -1 ? 0L : rcvTraffic) + (sndTraffic == -1 ? 0L : sndTraffic);
        }
        adjustTrafficInfo(j2);
        CacheHelper.getFileCache().put("xy_lastSumTraffic", Long.valueOf(j2));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.YMD);
        String format = simpleDateFormat.format(new Date());
        long cacheData = getCacheData("xy_curDayStartTraffic");
        long cacheData2 = getCacheData("xy_curMonthStartTraffic");
        this.curMonthTraffic = j2 - getCacheData("xy_curMonthStartTraffic");
        long cacheData3 = j2 - getCacheData("xy_curDayStartTraffic");
        this.curDayTraffic = cacheData3;
        if (cacheData <= 0 || cacheData3 <= 0 || CacheHelper.getFileCache().getAsString("xy_trafficDay") == null || !CacheHelper.getFileCache().getAsString("xy_trafficDay").equals(format)) {
            CacheHelper.getFileCache().put("xy_trafficDay", format);
            CacheHelper.getFileCache().put("xy_curDayStartTraffic", Long.valueOf(j2));
            this.curDayTraffic = j2 - getCacheData("xy_curDayStartTraffic");
            cacheData = j2;
        }
        String substring = format.substring(0, 6);
        long j3 = j2;
        if (cacheData2 <= 0 || this.curMonthTraffic <= 0 || CacheHelper.getFileCache().getAsString("xy_trafficMonth") == null || !CacheHelper.getFileCache().getAsString("xy_trafficMonth").equals(substring)) {
            CacheHelper.getFileCache().put("xy_trafficMonth", substring);
            CacheHelper.getFileCache().put("xy_curMonthStartTraffic", Long.valueOf(j3));
            j = j3;
            this.curMonthTraffic = j;
            this.curMonthTraffic = j - getCacheData("xy_curMonthStartTraffic");
        } else {
            j = j3;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(simpleDateFormat.format(new Date()));
        sb.append(" curDay:");
        double d = this.curDayTraffic;
        Double.isNaN(d);
        sb.append(String.format("%.2f (r%d s%d)", Double.valueOf(d / 1048576.0d), Long.valueOf(rcvTraffic - getCacheData("xy_curDayStartRcvTraffic")), Long.valueOf(sndTraffic - getCacheData("xy_curDayStartSndTraffic"))));
        sb.append(UsbFile.separator);
        double d2 = cacheData;
        Double.isNaN(d2);
        sb.append(String.format("%.2f", Double.valueOf(d2 / 1048576.0d)));
        sb.append(" curMonth:");
        double d3 = this.curMonthTraffic;
        Double.isNaN(d3);
        sb.append(String.format("%.2f", Double.valueOf(d3 / 1048576.0d)));
        sb.append(UsbFile.separator);
        double d4 = cacheData2;
        Double.isNaN(d4);
        sb.append(String.format("%.2f", Double.valueOf(d4 / 1048576.0d)));
        Loger.writeLog("UI", sb.toString());
        CacheHelper.getFileCache().put("xy_curDayStartRcvTraffic", Long.valueOf(rcvTraffic));
        CacheHelper.getFileCache().put("xy_curDayStartSndTraffic", Long.valueOf(sndTraffic));
        return j;
    }

    public long getCurMonthTraffic() {
        return this.curMonthTraffic;
    }

    public long getCurDayTraffic() {
        return this.curDayTraffic;
    }

    public long getRcvTraffic() {
        long uidRxBytes = TrafficStats.getUidRxBytes(this.uid);
        if (uidRxBytes != -1) {
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile("/proc/uid_stat/" + this.uid + "/tcp_rcv", "r");
                String line = randomAccessFile.readLine();
                if (line != null) {
                    uidRxBytes = Long.parseLong(line);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return uidRxBytes;
    }


    public long getSndTraffic() {
        long uidTxBytes = TrafficStats.getUidTxBytes(this.uid);
        if (uidTxBytes != -1) {
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile("/proc/uid_stat/" + this.uid + "/tcp_snd", "r");
                String line = randomAccessFile.readLine();
                if (line != null) {
                    uidTxBytes = Long.parseLong(line);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            } finally {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return uidTxBytes;
    }


    public long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    public long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }
}

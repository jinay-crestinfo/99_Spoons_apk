package com.oysb.utils;

import android.os.SystemClock;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class SystemDateTimeHelper {
    static final String TAG = "SystemDateTime";

    public static void setDateTime(int i, int i2, int i3, int i4, int i5) {
        try {
            requestPermission();
            Calendar calendar = Calendar.getInstance();
            calendar.set(1, i);
            calendar.set(2, i2 - 1);
            calendar.set(5, i3);
            calendar.set(11, i4);
            calendar.set(12, i5);
            long timeInMillis = calendar.getTimeInMillis();
            if (timeInMillis / 1000 < 2147483647L) {
                SystemClock.setCurrentTimeMillis(timeInMillis);
            }
            if (Calendar.getInstance().getTimeInMillis() - timeInMillis <= 1000) {
            } else {
                throw new IOException("failed to set Date.");
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static void setDate(int i, int i2, int i3) throws IOException, InterruptedException {
        requestPermission();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, i);
        calendar.set(2, i2);
        calendar.set(5, i3);
        long timeInMillis = calendar.getTimeInMillis();
        if (timeInMillis / 1000 < 2147483647L) {
            SystemClock.setCurrentTimeMillis(timeInMillis);
        }
        if (Calendar.getInstance().getTimeInMillis() - timeInMillis > 1000) {
            throw new IOException("failed to set Date.");
        }
    }

    public static void setTime(int i, int i2) throws IOException, InterruptedException {
        requestPermission();
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, i);
        calendar.set(12, i2);
        long timeInMillis = calendar.getTimeInMillis();
        if (timeInMillis / 1000 < 2147483647L) {
            SystemClock.setCurrentTimeMillis(timeInMillis);
        }
        if (Calendar.getInstance().getTimeInMillis() - timeInMillis > 1000) {
            throw new IOException("failed to set Time.");
        }
    }

    static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    static Process createSuProcess() throws IOException {
        File file = new File("/system/xbin/ru");
        if (file.exists()) {
            return Runtime.getRuntime().exec(file.getAbsolutePath());
        }
        return Runtime.getRuntime().exec("su");
    }

    static Process createSuProcess(String command) throws IOException {
        Process suProcess = createSuProcess();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(suProcess.getOutputStream());
            dataOutputStream.writeBytes(command + StringUtils.LF);
            dataOutputStream.writeBytes("exit $?\n");
            dataOutputStream.close();
            return suProcess;
        } catch (IOException e) {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (suProcess != null) {
                suProcess.destroy();
            }
            throw e;
        }
    }

}

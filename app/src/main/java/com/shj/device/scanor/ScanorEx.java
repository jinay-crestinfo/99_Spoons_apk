package com.shj.device.scanor;

import android.serialport.SerialDevice;
import com.oysb.utils.Loger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
public class ScanorEx extends SerialDevice {
    private static ScanorEx scanor = new ScanorEx();
    private ScanorListener listener;
    private String preFix = "";

    /* loaded from: classes2.dex */
    public interface ScanorListener {
        void onMessage(String str);
    }

    public static ScanorEx get() {
        return scanor;
    }

    @Override // android.serialport.SerialDevice
    public void onMessage(byte[] bArr) {
        String trim = new String(bArr).trim();
        if (trim.endsWith(SocketClient.NETASCII_EOL)) {
            trim = trim.substring(0, trim.length() - 2);
        }
        if (trim.endsWith("\n\r")) {
            trim = trim.substring(0, trim.length() - 2);
        }
        if (trim.endsWith(StringUtils.LF)) {
            trim = trim.substring(0, trim.length() - 1);
        }
        String trim2 = trim.trim();
        if (trim2.length() == 0) {
            return;
        }
        try {
            this.listener.onMessage(this.preFix + trim2);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SCANOR", e);
        }
    }

    @Override // android.serialport.SerialDevice
    public void onConnectStateChanged(boolean z) {
        if (z) {
            this.listener.onMessage("connectState:true");
        } else {
            this.listener.onMessage("connectState:false");
        }
    }

    public ScanorListener getScanorListener() {
        return this.listener;
    }

    public void setScanorListener(ScanorListener scanorListener) {
        this.listener = scanorListener;
    }

    public String getPreFix() {
        return this.preFix;
    }

    public void setPreFix(String str) {
        this.preFix = str;
    }
}

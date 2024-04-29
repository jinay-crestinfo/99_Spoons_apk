package com.shj.biz.paper;

import android.app.Activity;
import com.ftdi.j2xx.FT_Device;
import device.itl.sspcoms.BarCodeReader;
import device.itl.sspcoms.DeviceEvent;
import device.itl.sspcoms.DeviceEventListener;
import device.itl.sspcoms.DeviceFileUpdateListener;
import device.itl.sspcoms.DevicePayoutEventListener;
import device.itl.sspcoms.DeviceSetupListener;
import device.itl.sspcoms.ItlCurrency;
import device.itl.sspcoms.PayoutRoute;
import device.itl.sspcoms.SSPComsConfig;
import device.itl.sspcoms.SSPDevice;
import device.itl.sspcoms.SSPPayoutEvent;
import device.itl.sspcoms.SSPSystem;
import device.itl.sspcoms.SSPUpdate;

/* loaded from: classes2.dex */
public class ITLDeviceCom extends Thread implements DeviceSetupListener, DeviceEventListener, DeviceFileUpdateListener, DevicePayoutEventListener {
    static final int READBUF_SIZE = 256;
    static final int WRITEBUF_SIZE = 4096;
    static PaperResultListener currentResultListener = null;
    private static boolean isrunning = false;
    private static SSPSystem ssp;
    private Activity context;
    private FT_Device ftDev = null;
    byte[] rbuf = new byte[256];
    byte[] wbuf = new byte[4096];
    int mReadSize = 0;
    private SSPDevice sspDevice = null;

    /* loaded from: classes2.dex */
    public interface PaperResultListener {
        void DeviceDisconnected(SSPDevice sSPDevice);

        void DisplayEvents(DeviceEvent deviceEvent);

        void DisplayPayoutEvents(SSPPayoutEvent sSPPayoutEvent);

        void DisplaySetUp(SSPDevice sSPDevice);

        void SetConfig(int i, byte b, byte b2, byte b3, byte b4);

        void UpdateFileDownload(SSPUpdate sSPUpdate);
    }

    public ITLDeviceCom(Activity activity, PaperResultListener paperResultListener) {
        this.context = activity;
        currentResultListener = paperResultListener;
        SSPSystem sSPSystem = new SSPSystem();
        ssp = sSPSystem;
        sSPSystem.setOnDeviceSetupListener(this);
        ssp.setOnEventUpdateListener(this);
        ssp.setOnDeviceFileUpdateListener(this);
        ssp.setOnPayoutEventListener(this);
    }

    public void setup(FT_Device fT_Device, int i, boolean z, boolean z2, long j) {
        this.ftDev = fT_Device;
        ssp.SetAddress(i);
        ssp.EscrowMode(z);
        ssp.SetESSPMode(z2, j);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        ssp.Run();
        isrunning = true;
        while (isrunning) {
            synchronized (this.ftDev) {
                int GetNewData = ssp.GetNewData(this.wbuf);
                if (GetNewData > 0) {
                    if (ssp.GetDownloadState() != SSPSystem.DownloadSetupState.active) {
                        this.ftDev.purge((byte) 1);
                    }
                    this.ftDev.write(this.wbuf, GetNewData);
                    ssp.SetComsBufferWritten(true);
                }
            }
            synchronized (this.ftDev) {
                int queueStatus = this.ftDev.getQueueStatus();
                if (queueStatus > 0) {
                    this.mReadSize = queueStatus;
                    if (queueStatus > 256) {
                        this.mReadSize = 256;
                    }
                    ssp.ProcessResponse(this.rbuf, this.ftDev.read(this.rbuf, this.mReadSize));
                }
            }
            SSPComsConfig GetComsConfig = ssp.GetComsConfig();
            if (GetComsConfig.configUpdate == SSPComsConfig.ComsConfigChangeState.ccNewConfig) {
                GetComsConfig.configUpdate = SSPComsConfig.ComsConfigChangeState.ccUpdating;
                this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.1
                    final /* synthetic */ SSPComsConfig val$cfg;

                    AnonymousClass1(SSPComsConfig GetComsConfig2) {
                        GetComsConfig = GetComsConfig2;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (ITLDeviceCom.currentResultListener != null) {
                            ITLDeviceCom.currentResultListener.SetConfig(GetComsConfig.baud, GetComsConfig.dataBits, GetComsConfig.stopBits, GetComsConfig.parity, GetComsConfig.flowControl);
                        }
                    }
                });
                GetComsConfig2.configUpdate = SSPComsConfig.ComsConfigChangeState.ccUpdated;
            }
            try {
                sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ SSPComsConfig val$cfg;

        AnonymousClass1(SSPComsConfig GetComsConfig2) {
            GetComsConfig = GetComsConfig2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.SetConfig(GetComsConfig.baud, GetComsConfig.dataBits, GetComsConfig.stopBits, GetComsConfig.parity, GetComsConfig.flowControl);
            }
        }
    }

    @Override // device.itl.sspcoms.DeviceSetupListener
    public void OnNewDeviceSetup(SSPDevice sSPDevice) {
        this.sspDevice = sSPDevice;
        this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.2
            final /* synthetic */ SSPDevice val$dev;

            AnonymousClass2(SSPDevice sSPDevice2) {
                sSPDevice = sSPDevice2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ITLDeviceCom.currentResultListener != null) {
                    ITLDeviceCom.currentResultListener.DisplaySetUp(sSPDevice);
                }
            }
        });
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ SSPDevice val$dev;

        AnonymousClass2(SSPDevice sSPDevice2) {
            sSPDevice = sSPDevice2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.DisplaySetUp(sSPDevice);
            }
        }
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements Runnable {
        final /* synthetic */ SSPDevice val$dev;

        AnonymousClass3(SSPDevice sSPDevice) {
            sSPDevice = sSPDevice;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.DeviceDisconnected(sSPDevice);
            }
        }
    }

    @Override // device.itl.sspcoms.DeviceSetupListener
    public void OnDeviceDisconnect(SSPDevice sSPDevice) {
        this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.3
            final /* synthetic */ SSPDevice val$dev;

            AnonymousClass3(SSPDevice sSPDevice2) {
                sSPDevice = sSPDevice2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ITLDeviceCom.currentResultListener != null) {
                    ITLDeviceCom.currentResultListener.DeviceDisconnected(sSPDevice);
                }
            }
        });
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements Runnable {
        final /* synthetic */ DeviceEvent val$ev;

        AnonymousClass4(DeviceEvent deviceEvent) {
            deviceEvent = deviceEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.DisplayEvents(deviceEvent);
            }
        }
    }

    @Override // device.itl.sspcoms.DeviceEventListener
    public void OnDeviceEvent(DeviceEvent deviceEvent) {
        this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.4
            final /* synthetic */ DeviceEvent val$ev;

            AnonymousClass4(DeviceEvent deviceEvent2) {
                deviceEvent = deviceEvent2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ITLDeviceCom.currentResultListener != null) {
                    ITLDeviceCom.currentResultListener.DisplayEvents(deviceEvent);
                }
            }
        });
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements Runnable {
        final /* synthetic */ SSPPayoutEvent val$ev;

        AnonymousClass5(SSPPayoutEvent sSPPayoutEvent) {
            sSPPayoutEvent = sSPPayoutEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.DisplayPayoutEvents(sSPPayoutEvent);
            }
        }
    }

    @Override // device.itl.sspcoms.DevicePayoutEventListener
    public void OnNewPayoutEvent(SSPPayoutEvent sSPPayoutEvent) {
        this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.5
            final /* synthetic */ SSPPayoutEvent val$ev;

            AnonymousClass5(SSPPayoutEvent sSPPayoutEvent2) {
                sSPPayoutEvent = sSPPayoutEvent2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ITLDeviceCom.currentResultListener != null) {
                    ITLDeviceCom.currentResultListener.DisplayPayoutEvents(sSPPayoutEvent);
                }
            }
        });
    }

    /* renamed from: com.shj.biz.paper.ITLDeviceCom$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements Runnable {
        final /* synthetic */ SSPUpdate val$sspUpdate;

        AnonymousClass6(SSPUpdate sSPUpdate) {
            sSPUpdate = sSPUpdate;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ITLDeviceCom.currentResultListener != null) {
                ITLDeviceCom.currentResultListener.UpdateFileDownload(sSPUpdate);
            }
        }
    }

    @Override // device.itl.sspcoms.DeviceFileUpdateListener
    public void OnFileUpdateStatus(SSPUpdate sSPUpdate) {
        this.context.runOnUiThread(new Runnable() { // from class: com.shj.biz.paper.ITLDeviceCom.6
            final /* synthetic */ SSPUpdate val$sspUpdate;

            AnonymousClass6(SSPUpdate sSPUpdate2) {
                sSPUpdate = sSPUpdate2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ITLDeviceCom.currentResultListener != null) {
                    ITLDeviceCom.currentResultListener.UpdateFileDownload(sSPUpdate);
                }
            }
        });
    }

    public void Stop() {
        ssp.Close();
        isrunning = false;
    }

    boolean SetSSPDownload(SSPUpdate sSPUpdate) {
        return ssp.SetDownload(sSPUpdate);
    }

    public void SetEscrowMode(boolean z) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.EscrowMode(z);
        }
    }

    public void SetDeviceEnable(boolean z) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            if (z) {
                sSPSystem.EnableDevice();
            } else {
                sSPSystem.DisableDevice();
            }
        }
    }

    public void SetEscrowAction(SSPSystem.BillAction billAction) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.SetBillEscrowAction(billAction);
        }
    }

    public void SetBarcocdeConfig(BarCodeReader barCodeReader) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.SetBarCodeConfiguration(barCodeReader);
        }
    }

    int GetDeviceCode() {
        if (ssp != null) {
            return this.sspDevice.headerType.getValue();
        }
        return -1;
    }

    public void SetPayoutRoute(ItlCurrency itlCurrency, PayoutRoute payoutRoute) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.SetPayoutRoute(itlCurrency, payoutRoute);
        }
    }

    public void PayoutAmount(ItlCurrency itlCurrency) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.PayoutAmount(itlCurrency);
        }
    }

    void FloatAmount(ItlCurrency itlCurrency, ItlCurrency itlCurrency2) {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.FloatAmount(itlCurrency, itlCurrency2);
        }
    }

    public void EmptyPayout() {
        SSPSystem sSPSystem = ssp;
        if (sSPSystem != null) {
            sSPSystem.EmptyPayout();
        }
    }
}

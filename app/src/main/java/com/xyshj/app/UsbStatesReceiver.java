package com.xyshj.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.oysb.app.R;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class UsbStatesReceiver extends BroadcastReceiver {
    public static final int EVENT_USB_IN = 1000;
    public static final int EVENT_USB_OUT = 2000;
    Context context;
    private Handler handler = new Handler() { // from class: com.xyshj.app.UsbStatesReceiver.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1000) {
                ArrayList arrayList = new ArrayList();
                String str = "/storage/external_storage/sda/xy";
                Loger.writeLog("UI", "/storage/external_storage/sda/xy");
                if (!new File("/storage/external_storage/sda/xy").exists()) {
                    str = "/storage/external_storage/sda0/xy";
                    Loger.writeLog("UI", "/storage/external_storage/sda0/xy");
                } else if (SDFileUtils.getFiles("/storage/external_storage/sda/xy", "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                if (!new File(str).exists()) {
                    str = "/storage/external_storage/sda1/xy";
                    Loger.writeLog("UI", "/storage/external_storage/sda1/xy");
                } else if (SDFileUtils.getFiles(str, "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                if (!new File(str).exists()) {
                    Loger.writeLog("UI", "/storage/external_storage/sda2/xy");
                } else if (SDFileUtils.getFiles(str, "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                File file = null;
                if (arrayList.size() > 0) {
                    UsbStatesReceiver.this.context.getPackageManager();
                    Iterator it = arrayList.iterator();
                    if (it.hasNext()) {
                        file = (File) it.next();
                    }
                }
                File file2 = file;
                if (file2 != null) {
                    ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_upanupdate, "shjapp.apk", file2.getName()), 0, ShjAppHelper.getString(R.string.lab_updatenow), ShjAppHelper.getString(R.string.button_cancel), file2, new ShjAppHelper.OnItemClickListener() { // from class: com.xyshj.app.UsbStatesReceiver.1.1
                        C00771() {
                        }

                        @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
                        public void onItemClick(Object obj, int i2) {
                            Uri fromFile = Uri.fromFile((File) obj);
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
                            intent.addFlags(268435456);
                            UsbStatesReceiver.this.context.startActivity(intent);
                        }
                    });
                } else {
                    ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_udisk_inserted), 0, ShjAppHelper.getString(R.string.lab_open_udisk), ShjAppHelper.getString(R.string.button_tosetting), "", new ShjAppHelper.OnItemClickListener() { // from class: com.xyshj.app.UsbStatesReceiver.1.2
                        AnonymousClass2() {
                        }

                        @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
                        public void onItemClick(Object obj, int i2) {
                            if (i2 == 0) {
                                PackageManager packageManager = ShjAppHelper.getMainActivity().getPackageManager();
                                try {
                                    PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
                                    if (packageInfo != null) {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
                                    } else {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                                    }
                                    return;
                                } catch (Exception e) {
                                    Loger.safe_inner_exception_catch(e);
                                    try {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                                        return;
                                    } catch (Exception unused) {
                                        return;
                                    }
                                }
                            }
                            ShjAppHelper.enterSetting(ShjAppHelper.getMainActivity(), false);
                        }
                    });
                }
            } else if (i == 2000) {
                ShjAppHelper.setListenerNull();
                ShjAppHelper.cancelMessage(false);
            }
            super.handleMessage(message);
        }

        /* renamed from: com.xyshj.app.UsbStatesReceiver$1$1 */
        /* loaded from: classes2.dex */
        class C00771 implements ShjAppHelper.OnItemClickListener {
            C00771() {
            }

            @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                Uri fromFile = Uri.fromFile((File) obj);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
                intent.addFlags(268435456);
                UsbStatesReceiver.this.context.startActivity(intent);
            }
        }

        /* renamed from: com.xyshj.app.UsbStatesReceiver$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements ShjAppHelper.OnItemClickListener {
            AnonymousClass2() {
            }

            @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                if (i2 == 0) {
                    PackageManager packageManager = ShjAppHelper.getMainActivity().getPackageManager();
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
                        if (packageInfo != null) {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
                        } else {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                        }
                        return;
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        try {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    }
                }
                ShjAppHelper.enterSetting(ShjAppHelper.getMainActivity(), false);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.app.UsbStatesReceiver$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1000) {
                ArrayList arrayList = new ArrayList();
                String str = "/storage/external_storage/sda/xy";
                Loger.writeLog("UI", "/storage/external_storage/sda/xy");
                if (!new File("/storage/external_storage/sda/xy").exists()) {
                    str = "/storage/external_storage/sda0/xy";
                    Loger.writeLog("UI", "/storage/external_storage/sda0/xy");
                } else if (SDFileUtils.getFiles("/storage/external_storage/sda/xy", "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                if (!new File(str).exists()) {
                    str = "/storage/external_storage/sda1/xy";
                    Loger.writeLog("UI", "/storage/external_storage/sda1/xy");
                } else if (SDFileUtils.getFiles(str, "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                if (!new File(str).exists()) {
                    Loger.writeLog("UI", "/storage/external_storage/sda2/xy");
                } else if (SDFileUtils.getFiles(str, "apk") != null) {
                    arrayList.addAll(arrayList);
                }
                File file = null;
                if (arrayList.size() > 0) {
                    UsbStatesReceiver.this.context.getPackageManager();
                    Iterator it = arrayList.iterator();
                    if (it.hasNext()) {
                        file = (File) it.next();
                    }
                }
                File file2 = file;
                if (file2 != null) {
                    ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_upanupdate, "shjapp.apk", file2.getName()), 0, ShjAppHelper.getString(R.string.lab_updatenow), ShjAppHelper.getString(R.string.button_cancel), file2, new ShjAppHelper.OnItemClickListener() { // from class: com.xyshj.app.UsbStatesReceiver.1.1
                        C00771() {
                        }

                        @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
                        public void onItemClick(Object obj, int i2) {
                            Uri fromFile = Uri.fromFile((File) obj);
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
                            intent.addFlags(268435456);
                            UsbStatesReceiver.this.context.startActivity(intent);
                        }
                    });
                } else {
                    ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_udisk_inserted), 0, ShjAppHelper.getString(R.string.lab_open_udisk), ShjAppHelper.getString(R.string.button_tosetting), "", new ShjAppHelper.OnItemClickListener() { // from class: com.xyshj.app.UsbStatesReceiver.1.2
                        AnonymousClass2() {
                        }

                        @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
                        public void onItemClick(Object obj, int i2) {
                            if (i2 == 0) {
                                PackageManager packageManager = ShjAppHelper.getMainActivity().getPackageManager();
                                try {
                                    PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
                                    if (packageInfo != null) {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
                                    } else {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                                    }
                                    return;
                                } catch (Exception e) {
                                    Loger.safe_inner_exception_catch(e);
                                    try {
                                        ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                                        return;
                                    } catch (Exception unused) {
                                        return;
                                    }
                                }
                            }
                            ShjAppHelper.enterSetting(ShjAppHelper.getMainActivity(), false);
                        }
                    });
                }
            } else if (i == 2000) {
                ShjAppHelper.setListenerNull();
                ShjAppHelper.cancelMessage(false);
            }
            super.handleMessage(message);
        }

        /* renamed from: com.xyshj.app.UsbStatesReceiver$1$1 */
        /* loaded from: classes2.dex */
        class C00771 implements ShjAppHelper.OnItemClickListener {
            C00771() {
            }

            @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                Uri fromFile = Uri.fromFile((File) obj);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
                intent.addFlags(268435456);
                UsbStatesReceiver.this.context.startActivity(intent);
            }
        }

        /* renamed from: com.xyshj.app.UsbStatesReceiver$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements ShjAppHelper.OnItemClickListener {
            AnonymousClass2() {
            }

            @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                if (i2 == 0) {
                    PackageManager packageManager = ShjAppHelper.getMainActivity().getPackageManager();
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
                        if (packageInfo != null) {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
                        } else {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                        }
                        return;
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        try {
                            ShjAppHelper.getMainActivity().startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    }
                }
                ShjAppHelper.enterSetting(ShjAppHelper.getMainActivity(), false);
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED") || intent.getAction().equals("android.intent.action.MEDIA_CHECKING")) {
            this.handler.sendEmptyMessage(1000);
            intent.getDataString();
        } else if (intent.getAction().equals("android.intent.action.MEDIA_EJECT")) {
            this.handler.sendEmptyMessage(2000);
        }
    }
}

package com.shj.setting.generator;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Event.FindPeopleInEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.OnCommandAnswerListener;
import com.shj.ShelfInfo;
import com.shj.ShelfType;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.DataFileDeleteDialog;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.R;
import com.shj.setting.Utils.SalesUtils;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.Utils.UsbFileUtil;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.ClickExecItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class ClickExecItemNote extends SettingNote {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private int cabinetNumber;
    private ClickExecItemView clickExecItemView;
    private MutilTextTipDialog findPelpleInMutilTextTipDialog;
    private Handler handler;
    private LoadingDialog loadingDialog;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public ClickExecItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.handler = new Handler() { // from class: com.shj.setting.generator.ClickExecItemNote.11
            AnonymousClass11() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    ToastUitl.showLong(ClickExecItemNote.this.context, R.string.backups_success_tip);
                    if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    ClickExecItemNote.this.loadingDialog.dismiss();
                    return;
                }
                if (message.what == 1) {
                    ToastUitl.showLong(ClickExecItemNote.this.context, R.string.copy_complete);
                    if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    ClickExecItemNote.this.loadingDialog.dismiss();
                    return;
                }
                if (message.what == 2) {
                    ToastUitl.showShort(ClickExecItemNote.this.context, R.string.not_have_u_disk);
                    if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    ClickExecItemNote.this.loadingDialog.dismiss();
                }
            }
        };
    }

    private String getInstruction() {
        int i = this.settingType;
        if (i == 134 || i == 355) {
            return this.context.getResources().getString(R.string.full_delivery);
        }
        if (i != 162 && i != 163) {
            switch (i) {
                case 158:
                case 159:
                case 160:
                    break;
                default:
                    return "";
            }
        }
        return getSettingName();
    }

    private String getExecButtonName() {
        int i = this.settingType;
        if (i != 162 && i != 163) {
            if (i != 282) {
                switch (i) {
                    case 158:
                    case 159:
                        return this.context.getResources().getString(R.string.diagnosis);
                    case 160:
                        break;
                    default:
                        return getSettingName();
                }
            } else {
                return getSettingName() + this.context.getResources().getString(R.string.lancher_settings_tip);
            }
        }
        return this.context.getResources().getString(R.string.cargo_clearance);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        ClickExecItemView clickExecItemView = new ClickExecItemView(this.context, getInstruction(), getExecButtonName());
        this.clickExecItemView = clickExecItemView;
        clickExecItemView.setEventListener(this.eventListener);
        this.clickExecItemView.setAlwaysNotDisplaySaveButton();
        setExecItemListener();
        return this.clickExecItemView;
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            ClickExecItemNote.this.exec();
        }
    }

    private void setExecItemListener() {
        this.clickExecItemView.setExecClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.ClickExecItemNote.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                ClickExecItemNote.this.exec();
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:136:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00f7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void exec() {
        /*
            Method dump skipped, instructions count: 1186
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.generator.ClickExecItemNote.exec():void");
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            EventBus.getDefault().post(new GetMenuDateEvent(70, SalesUtils.getSalesDescribe(bArr)));
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.normal);
            } else {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.error);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.normal);
            } else {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.error);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            ClickExecItemNote.this.loadingDialog.dismiss();
            if (z) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.export_success);
            } else {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.export_fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass6() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            ClickExecItemNote.this.loadingDialog.dismiss();
            if (z) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.import_success);
            } else {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.import_fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.open_success);
                return;
            }
            List<Integer> shelves = Shj.getShelves();
            if (shelves != null) {
                Iterator<Integer> it = shelves.iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                    if (shelfInfo != null && shelfInfo.getShelfType() == ShelfType.Box) {
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand.TestShelf(false, 1, intValue);
                        Shj.getInstance(ClickExecItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand, null);
                    }
                }
            }
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            ToastUitl.showCompeleteTip(ClickExecItemNote.this.context, z, ClickExecItemNote.this.getSettingName());
        }
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements Runnable {
        final /* synthetic */ boolean val$isNeedCopy2Udisk;
        final /* synthetic */ String val$savePath;

        AnonymousClass9(String str, boolean z) {
            str = str;
            z = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            List<String> backupsShjApp2USBDrive = SDFileUtils.backupsShjApp2USBDrive(ClickExecItemNote.this.context, str);
            if (z) {
                UsbFile redUDiskDevsList = ClickExecItemNote.this.redUDiskDevsList();
                if (redUDiskDevsList == null) {
                    ClickExecItemNote.this.handler.sendEmptyMessage(2);
                    return;
                }
                Iterator<String> it = backupsShjApp2USBDrive.iterator();
                while (it.hasNext()) {
                    File file = new File(it.next());
                    if (file.exists()) {
                        UsbFileUtil.saveSDFile2OTG(file, redUDiskDevsList);
                        file.delete();
                    }
                }
                ClickExecItemNote.this.handler.sendEmptyMessage(1);
                return;
            }
            ClickExecItemNote.this.handler.sendEmptyMessage(0);
        }
    }

    private void startBackupsShjApp(String str, boolean z) {
        new Thread(new Runnable() { // from class: com.shj.setting.generator.ClickExecItemNote.9
            final /* synthetic */ boolean val$isNeedCopy2Udisk;
            final /* synthetic */ String val$savePath;

            AnonymousClass9(String str2, boolean z2) {
                str = str2;
                z = z2;
            }

            @Override // java.lang.Runnable
            public void run() {
                List<String> backupsShjApp2USBDrive = SDFileUtils.backupsShjApp2USBDrive(ClickExecItemNote.this.context, str);
                if (z) {
                    UsbFile redUDiskDevsList = ClickExecItemNote.this.redUDiskDevsList();
                    if (redUDiskDevsList == null) {
                        ClickExecItemNote.this.handler.sendEmptyMessage(2);
                        return;
                    }
                    Iterator<String> it = backupsShjApp2USBDrive.iterator();
                    while (it.hasNext()) {
                        File file = new File(it.next());
                        if (file.exists()) {
                            UsbFileUtil.saveSDFile2OTG(file, redUDiskDevsList);
                            file.delete();
                        }
                    }
                    ClickExecItemNote.this.handler.sendEmptyMessage(1);
                    return;
                }
                ClickExecItemNote.this.handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void startCopyLogFile(String str, String str2, boolean z) {
        String str3 = str + this.context.getString(R.string.log);
        File file = new File(str3);
        if (!file.exists()) {
            file.mkdirs();
            file.setWritable(true, false);
            file.setReadable(true, false);
            file.setExecutable(true, false);
        }
        new Thread(new Runnable() { // from class: com.shj.setting.generator.ClickExecItemNote.10
            final /* synthetic */ boolean val$isNeedCopy2Udisk;
            final /* synthetic */ String val$machineId;
            final /* synthetic */ String val$tempPath;

            AnonymousClass10(String str32, String str22, boolean z2) {
                str3 = str32;
                str2 = str22;
                z = z2;
            }

            @Override // java.lang.Runnable
            public void run() {
                String copyLogFile = ClickExecItemNote.this.copyLogFile(str3, str2);
                if (z) {
                    UsbFile redUDiskDevsList = ClickExecItemNote.this.redUDiskDevsList();
                    if (redUDiskDevsList == null) {
                        ClickExecItemNote.this.handler.sendEmptyMessage(2);
                        return;
                    }
                    File file2 = new File(copyLogFile);
                    if (file2.exists()) {
                        UsbFileUtil.saveSDFile2OTG(file2, redUDiskDevsList);
                        file2.delete();
                    }
                    ClickExecItemNote.this.handler.sendEmptyMessage(1);
                    return;
                }
                ClickExecItemNote.this.handler.sendEmptyMessage(1);
            }
        }).start();
    }

    /* renamed from: com.shj.setting.generator.ClickExecItemNote$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements Runnable {
        final /* synthetic */ boolean val$isNeedCopy2Udisk;
        final /* synthetic */ String val$machineId;
        final /* synthetic */ String val$tempPath;

        AnonymousClass10(String str32, String str22, boolean z2) {
            str3 = str32;
            str2 = str22;
            z = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            String copyLogFile = ClickExecItemNote.this.copyLogFile(str3, str2);
            if (z) {
                UsbFile redUDiskDevsList = ClickExecItemNote.this.redUDiskDevsList();
                if (redUDiskDevsList == null) {
                    ClickExecItemNote.this.handler.sendEmptyMessage(2);
                    return;
                }
                File file2 = new File(copyLogFile);
                if (file2.exists()) {
                    UsbFileUtil.saveSDFile2OTG(file2, redUDiskDevsList);
                    file2.delete();
                }
                ClickExecItemNote.this.handler.sendEmptyMessage(1);
                return;
            }
            ClickExecItemNote.this.handler.sendEmptyMessage(1);
        }
    }

    public UsbFile redUDiskDevsList() {
        UsbManager usbManager = (UsbManager) this.context.getSystemService(UsbFileUtil.DEFAULT_BIN_DIR);
        UsbMassStorageDevice[] massStorageDevices = UsbMassStorageDevice.getMassStorageDevices(this.context);
        PendingIntent broadcast = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        UsbFile usbFile = null;
        for (UsbMassStorageDevice usbMassStorageDevice : massStorageDevices) {
            if (usbManager.hasPermission(usbMassStorageDevice.getUsbDevice())) {
                usbFile = readDevice(usbMassStorageDevice);
            } else {
                usbManager.requestPermission(usbMassStorageDevice.getUsbDevice(), broadcast);
            }
        }
        return usbFile;
    }

    private UsbFile readDevice(UsbMassStorageDevice usbMassStorageDevice) {
        try {
            usbMassStorageDevice.init();
            FileSystem fileSystem = usbMassStorageDevice.getPartitions().get(0).getFileSystem();
            fileSystem.getVolumeLabel();
            Log.e("Capacity: ", fileSystem.getCapacity() + "");
            Log.e("Occupied Space: ", fileSystem.getOccupiedSpace() + "");
            Log.e("Free Space: ", fileSystem.getFreeSpace() + "");
            Log.e("Chunk size: ", fileSystem.getChunkSize() + "");
            return fileSystem.getRootDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveText2UDisk(String str, UsbFile usbFile) {
        File saveFile = UsbFileUtil.getSaveFile("test/test" + File.separator + UsbFileUtil.DEFAULT_BIN_DIR, "te");
        try {
            FileWriter fileWriter = new FileWriter(saveFile);
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (usbFile != null) {
            UsbFileUtil.saveSDFile2OTG(saveFile, usbFile);
        }
    }

    public String copyLogFile(String str, String str2) {
        copyLogFileSingleDirect(str, "/crash");
        copyLogFileSingleDirect(str, "/logcat");
        copyLogFileSingleDirect(str, "/Logcat");
        copyLogFileSingleDirect(str, "/logfile");
        copyLogFileSingleDirect(str, "/xyShj/log");
        String str3 = str + "_" + str2 + "_" + getDate() + ".zip";
        try {
            SDFileUtils.ZipFolder(str, str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    private String getDate() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(System.currentTimeMillis()));
    }

    private void copyLogFileSingleDirect(String str, String str2) {
        String str3 = SDFileUtils.SDCardRoot + str2;
        String str4 = str + str2;
        deleteFile(str4);
        File file = new File(str4);
        file.mkdirs();
        file.setWritable(true, false);
        file.setReadable(true, false);
        file.setExecutable(true, false);
        File file2 = new File(str3);
        if (file2.exists()) {
            for (File file3 : file2.listFiles()) {
                SDFileUtils.CopySdcardFile(file3.getAbsolutePath(), str4 + UsbFile.separator + file3.getName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.ClickExecItemNote$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 extends Handler {
        AnonymousClass11() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                ToastUitl.showLong(ClickExecItemNote.this.context, R.string.backups_success_tip);
                if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                    return;
                }
                ClickExecItemNote.this.loadingDialog.dismiss();
                return;
            }
            if (message.what == 1) {
                ToastUitl.showLong(ClickExecItemNote.this.context, R.string.copy_complete);
                if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                    return;
                }
                ClickExecItemNote.this.loadingDialog.dismiss();
                return;
            }
            if (message.what == 2) {
                ToastUitl.showShort(ClickExecItemNote.this.context, R.string.not_have_u_disk);
                if (ClickExecItemNote.this.loadingDialog == null || !ClickExecItemNote.this.loadingDialog.isShowing()) {
                    return;
                }
                ClickExecItemNote.this.loadingDialog.dismiss();
            }
        }
    }

    private void restoreFactorySettings() {
        new DataFileDeleteDialog(this.context).show();
    }

    private void deleteFile(String str) {
        try {
            SDFileUtils.safeDeleteFile(new File(str));
        } catch (Exception unused) {
        }
    }

    private void enterFileManagement() {
        PackageManager packageManager = this.context.getPackageManager();
        try {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
                if (packageInfo != null) {
                    this.context.startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
                } else {
                    this.context.startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
                }
            } catch (PackageManager.NameNotFoundException unused) {
                this.context.startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.android.rk", 1).packageName));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ToastUitl.showShort(this.context, this.context.getString(R.string.not_installed));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.clickExecItemView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
        EventBus.getDefault().register(this);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        super.onDetached();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        MutilTextTipDialog mutilTextTipDialog;
        if ((baseEvent instanceof FindPeopleInEvent) && (mutilTextTipDialog = this.findPelpleInMutilTextTipDialog) != null && mutilTextTipDialog.isShowing()) {
            this.findPelpleInMutilTextTipDialog.addTextShow("感应到人体", true);
        }
    }
}

package com.shj.setting.helper;

import android.content.Context;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class LowerMachineProgramUpdate {
    private static int count;

    static /* synthetic */ int access$008() {
        int i = count;
        count = i + 1;
        return i;
    }

    public static void programUpdateMainControl(Context context, String str, int i, int i2) {
        programUpdate(context, str, 0, i, i2);
    }

    public static void programUpdateCargoDrive(Context context, String str, int i, int i2) {
        programUpdate(context, str, 1, i, i2);
    }

    public static void programUpdate(Context context, String str, int i, int i2, int i3) {
        if (new File(str).exists()) {
            try {
                byte[] readFile = readFile(str);
                if (readFile != null) {
                    int crc16 = crc16(readFile);
                    Loger.writeLog("UI", "programUpdate crc =" + crc16);
                    updateStart(context, str, i2, i3, crc16, i);
                } else {
                    Loger.writeLog("UI", "programUpdate bytesData = null");
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        ToastUitl.showShort(context, R.string.file_not_exists);
    }

    private static void updateStart(Context context, String str, int i, int i2, int i3, int i4) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.programUpdateStart(i, i2, i3, i4);
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.helper.LowerMachineProgramUpdate.1
            final /* synthetic */ Context val$context;
            final /* synthetic */ String val$filePath;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1(Context context2, String str2) {
                context = context2;
                str = str2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (z) {
                    LowerMachineProgramUpdate.ReadFileByBytes(context, str);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.helper.LowerMachineProgramUpdate$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$filePath;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(Context context2, String str2) {
            context = context2;
            str = str2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                LowerMachineProgramUpdate.ReadFileByBytes(context, str);
            }
        }
    }

    public static void sendData(Context context, List<byte[]> list, int i) {
        Loger.writeLog("UI", "send data index=" + i);
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.programUpdateSendData(i, list.get(i));
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.helper.LowerMachineProgramUpdate.2
            final /* synthetic */ Context val$context;
            final /* synthetic */ int val$index;
            final /* synthetic */ List val$sendDataList;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass2(int i2, List list2, Context context2) {
                i = i2;
                list = list2;
                context = context2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    if (LowerMachineProgramUpdate.count < 5) {
                        LowerMachineProgramUpdate.access$008();
                        Loger.writeLog("UI", "send data fail count=" + LowerMachineProgramUpdate.count);
                        LowerMachineProgramUpdate.sendData(context, list, i);
                        return;
                    }
                    return;
                }
                if (i + 1 < list.size()) {
                    int unused = LowerMachineProgramUpdate.count = 0;
                    LowerMachineProgramUpdate.sendData(context, list, i + 1);
                } else {
                    LowerMachineProgramUpdate.updateEnd(context);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.helper.LowerMachineProgramUpdate$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$index;
        final /* synthetic */ List val$sendDataList;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2(int i2, List list2, Context context2) {
            i = i2;
            list = list2;
            context = context2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                if (LowerMachineProgramUpdate.count < 5) {
                    LowerMachineProgramUpdate.access$008();
                    Loger.writeLog("UI", "send data fail count=" + LowerMachineProgramUpdate.count);
                    LowerMachineProgramUpdate.sendData(context, list, i);
                    return;
                }
                return;
            }
            if (i + 1 < list.size()) {
                int unused = LowerMachineProgramUpdate.count = 0;
                LowerMachineProgramUpdate.sendData(context, list, i + 1);
            } else {
                LowerMachineProgramUpdate.updateEnd(context);
            }
        }
    }

    public static void updateEnd(Context context) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.programUpdateEnd();
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.helper.LowerMachineProgramUpdate.3
            final /* synthetic */ Context val$context;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass3(Context context2) {
                context = context2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                Loger.writeLog("UI", "send data end state=" + z);
                ToastUitl.showLong(context, R.string.update_complete);
            }
        });
    }

    /* renamed from: com.shj.setting.helper.LowerMachineProgramUpdate$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3(Context context2) {
            context = context2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            Loger.writeLog("UI", "send data end state=" + z);
            ToastUitl.showLong(context, R.string.update_complete);
        }
    }

    public static byte[] readFile(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final int crc16(byte[] bArr) {
        if (bArr != null) {
            return crc16(bArr, 0, bArr.length);
        }
        return 0;
    }

    public static final int crc16(byte[] bArr, int i, int i2) {
        int i3;
        byte[] bArr2 = new byte[i2];
        while (i < i2) {
            bArr2[i] = bArr[i];
            i++;
        }
        int i4 = 65535;
        for (int i5 = 0; i5 < i2; i5++) {
            if (bArr2[i5] < 0) {
                i3 = bArr2[i5] + 256;
            } else {
                i3 = bArr2[i5];
            }
            i4 ^= i3;
            for (int i6 = 8; i6 != 0; i6--) {
                i4 = (i4 & 1) != 0 ? (i4 >> 1) ^ 40961 : i4 >> 1;
            }
        }
        return i4 & Integer.MAX_VALUE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v13, types: [java.lang.String] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x006f -> B:16:0x0072). Please report as a decompilation issue!!! */
    public static void ReadFileByBytes(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        File file = new File(str);
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;
        FileInputStream fileInputStream3 = null;
        fileInputStream = null;
        try {
        } catch (IOException e) {
            e.printStackTrace();
            fileInputStream = fileInputStream;
        }
        try {
            try {
                FileInputStream fileInputStream4 = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[192];
                    while (true) {
                        int read = fileInputStream4.read(bArr);
                        if (-1 == read) {
                            break;
                        }
                        if (read < 192) {
                            arrayList.add(ObjectHelper.bytesFromBytes(bArr, 0, read));
                        } else {
                            arrayList.add(bArr);
                        }
                        bArr = new byte[192];
                    }
                    count = 0;
                    ?? r7 = "UI";
                    Loger.writeLog("UI", "send data size=" + arrayList.size());
                    sendData(context, arrayList, 0);
                    fileInputStream4.close();
                    fileInputStream = r7;
                } catch (FileNotFoundException e2) {
                    e = e2;
                    fileInputStream2 = fileInputStream4;
                    e.printStackTrace();
                    fileInputStream = fileInputStream2;
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                        fileInputStream = fileInputStream2;
                    }
                } catch (IOException e3) {
                    e = e3;
                    fileInputStream3 = fileInputStream4;
                    e.printStackTrace();
                    fileInputStream = fileInputStream3;
                    if (fileInputStream3 != null) {
                        fileInputStream3.close();
                        fileInputStream = fileInputStream3;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream4;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                e = e5;
            } catch (IOException e6) {
                e = e6;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}

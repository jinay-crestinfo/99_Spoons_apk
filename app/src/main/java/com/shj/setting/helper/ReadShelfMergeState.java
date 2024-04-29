package com.shj.setting.helper;

import android.content.Context;
import android.text.TextUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.SettingActivity;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class ReadShelfMergeState {
    public static final int ERROR_TYPE_MERGE_STATE_ERROR = 1;
    public static final int ERROR_TYPE_NOT_READ_DATA = 0;
    private ReadCompleteListener readCompleteListener;
    private HashMap<Integer, Integer> shelftMergeStateHashMap = new HashMap<>();
    private boolean isAlreadRead = false;

    /* loaded from: classes2.dex */
    public interface ReadCompleteListener {
        void complete(HashMap<Integer, Integer> hashMap);

        void error(int i, String str);
    }

    public void readMergeState(Context context, ReadCompleteListener readCompleteListener) {
        this.readCompleteListener = readCompleteListener;
        if (this.isAlreadRead) {
            if (readCompleteListener != null) {
                readCompleteListener.complete(this.shelftMergeStateHashMap);
            }
        } else {
            List<Integer> list = SettingActivity.getBasicMachineInfo().cabinetList;
            this.shelftMergeStateHashMap.clear();
            readShelfMergeState(context, list, 0);
        }
    }

    public void readShelfMergeState(Context context, List<Integer> list, int i) {
        if (i >= list.size()) {
            ReadCompleteListener readCompleteListener = this.readCompleteListener;
            if (readCompleteListener != null) {
                readCompleteListener.complete(this.shelftMergeStateHashMap);
            }
            this.isAlreadRead = true;
            return;
        }
        int intValue = list.get(i).intValue();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.shelfMergeStateQuery(intValue);
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.helper.ReadShelfMergeState.1
            final /* synthetic */ Context val$context;
            final /* synthetic */ int val$index;
            final /* synthetic */ List val$list;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1(Context context2, List list2, int i2) {
                context = context2;
                list = list2;
                i = i2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length <= 0) {
                    if (ReadShelfMergeState.this.readCompleteListener != null) {
                        ReadShelfMergeState.this.readCompleteListener.error(0, "读取货道联动状态失败");
                    }
                    Loger.writeLog("SHJ", "读取货道联动状态失败");
                } else {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 2);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 2);
                    int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 4, 1);
                    Loger.writeLog("SET", "读取货道联动状态 起始货道=" + intFromBytes + "结束货道=" + intFromBytes2 + "货道个数=" + intFromBytes3);
                    String str = null;
                    String str2 = "";
                    int i2 = 0;
                    int i3 = -1;
                    for (int i4 = 0; i4 < intFromBytes3; i4++) {
                        int intFromBytes4 = ObjectHelper.intFromBytes(bArr, i4 + 5, 1);
                        int i5 = intFromBytes + i4;
                        ReadShelfMergeState.this.shelftMergeStateHashMap.put(Integer.valueOf(i5), Integer.valueOf(intFromBytes4));
                        if (i2 != 0) {
                            if (intFromBytes4 != 1) {
                                if (str != null) {
                                    str2 = str2 + str;
                                    str = null;
                                }
                                str2 = str2 + "货道" + i3 + "状态" + intFromBytes4;
                                if (i2 == 1) {
                                    str2 = str2 + StringUtils.LF;
                                }
                            }
                            i2--;
                        } else if (intFromBytes4 > 1) {
                            i2 = intFromBytes4 - 1;
                            str = "货道:" + i5 + "状态:" + intFromBytes4;
                            i3 = i5;
                        }
                    }
                    if (!TextUtils.isEmpty(str2) && ReadShelfMergeState.this.readCompleteListener != null) {
                        ReadShelfMergeState.this.readCompleteListener.error(1, str2);
                    }
                }
                ReadShelfMergeState.this.readShelfMergeState(context, list, i + 1);
            }
        });
    }

    /* renamed from: com.shj.setting.helper.ReadShelfMergeState$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$index;
        final /* synthetic */ List val$list;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass1(Context context2, List list2, int i2) {
            context = context2;
            list = list2;
            i = i2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                if (ReadShelfMergeState.this.readCompleteListener != null) {
                    ReadShelfMergeState.this.readCompleteListener.error(0, "读取货道联动状态失败");
                }
                Loger.writeLog("SHJ", "读取货道联动状态失败");
            } else {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 2);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 2);
                int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 4, 1);
                Loger.writeLog("SET", "读取货道联动状态 起始货道=" + intFromBytes + "结束货道=" + intFromBytes2 + "货道个数=" + intFromBytes3);
                String str = null;
                String str2 = "";
                int i2 = 0;
                int i3 = -1;
                for (int i4 = 0; i4 < intFromBytes3; i4++) {
                    int intFromBytes4 = ObjectHelper.intFromBytes(bArr, i4 + 5, 1);
                    int i5 = intFromBytes + i4;
                    ReadShelfMergeState.this.shelftMergeStateHashMap.put(Integer.valueOf(i5), Integer.valueOf(intFromBytes4));
                    if (i2 != 0) {
                        if (intFromBytes4 != 1) {
                            if (str != null) {
                                str2 = str2 + str;
                                str = null;
                            }
                            str2 = str2 + "货道" + i3 + "状态" + intFromBytes4;
                            if (i2 == 1) {
                                str2 = str2 + StringUtils.LF;
                            }
                        }
                        i2--;
                    } else if (intFromBytes4 > 1) {
                        i2 = intFromBytes4 - 1;
                        str = "货道:" + i5 + "状态:" + intFromBytes4;
                        i3 = i5;
                    }
                }
                if (!TextUtils.isEmpty(str2) && ReadShelfMergeState.this.readCompleteListener != null) {
                    ReadShelfMergeState.this.readCompleteListener.error(1, str2);
                }
            }
            ReadShelfMergeState.this.readShelfMergeState(context, list, i + 1);
        }
    }
}

package com.shj.setting.helper;

import android.content.Context;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.SettingActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class ReadLightInspectionData {
    private ReadLightCompeleteListener readLightCompeleteListener;
    private HashMap<Integer, List<Integer>> lightDataHashMap = new HashMap<>();
    private boolean isAlreadRead = false;

    /* loaded from: classes2.dex */
    public interface ReadLightCompeleteListener {
        void complete(HashMap<Integer, List<Integer>> hashMap);

        void error(String str);
    }

    public void readLightData(Context context, ReadLightCompeleteListener readLightCompeleteListener) {
        this.readLightCompeleteListener = readLightCompeleteListener;
        if (this.isAlreadRead) {
            if (readLightCompeleteListener != null) {
                readLightCompeleteListener.complete(this.lightDataHashMap);
            }
        } else {
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
            this.lightDataHashMap.clear();
            readLayerLightInspectionData(context, list, 0);
        }
    }

    public void readLayerLightInspectionData(Context context, List<Integer> list, int i) {
        if (i >= list.size()) {
            Loger.writeLog("SET", "lightDataHashMap size=" + this.lightDataHashMap.size());
            ReadLightCompeleteListener readLightCompeleteListener = this.readLightCompeleteListener;
            if (readLightCompeleteListener != null) {
                readLightCompeleteListener.complete(this.lightDataHashMap);
            }
            this.isAlreadRead = true;
            return;
        }
        int intValue = list.get(i).intValue();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.readLightInspectionStatus(intValue);
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.helper.ReadLightInspectionData.1
            final /* synthetic */ Context val$context;
            final /* synthetic */ int val$index;
            final /* synthetic */ List val$layerList;

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
                    if (ReadLightInspectionData.this.readLightCompeleteListener != null) {
                        ReadLightInspectionData.this.readLightCompeleteListener.error(i + "层光检数据读取错误");
                    }
                    ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
                    return;
                }
                if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                    ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                byte[] bytesFromBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
                int startShelfNo = CalculatedInventory.getStartShelfNo(intFromBytes);
                for (int i2 = 0; i2 < bytesFromBytes.length; i2++) {
                    byte b = bytesFromBytes[i2];
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < intFromBytes2; i3++) {
                        arrayList.add(Integer.valueOf((b >> i3) & 1));
                    }
                    ReadLightInspectionData.this.lightDataHashMap.put(Integer.valueOf(startShelfNo + i2), arrayList);
                }
                ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
            }
        });
    }

    /* renamed from: com.shj.setting.helper.ReadLightInspectionData$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$index;
        final /* synthetic */ List val$layerList;

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
                if (ReadLightInspectionData.this.readLightCompeleteListener != null) {
                    ReadLightInspectionData.this.readLightCompeleteListener.error(i + "层光检数据读取错误");
                }
                ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
            byte[] bytesFromBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
            int startShelfNo = CalculatedInventory.getStartShelfNo(intFromBytes);
            for (int i2 = 0; i2 < bytesFromBytes.length; i2++) {
                byte b = bytesFromBytes[i2];
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < intFromBytes2; i3++) {
                    arrayList.add(Integer.valueOf((b >> i3) & 1));
                }
                ReadLightInspectionData.this.lightDataHashMap.put(Integer.valueOf(startShelfNo + i2), arrayList);
            }
            ReadLightInspectionData.this.readLayerLightInspectionData(context, list, i + 1);
        }
    }
}

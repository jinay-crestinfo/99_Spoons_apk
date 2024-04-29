package com.shj.setting.helper;

import android.content.Context;
import com.oysb.utils.Loger;
import com.shj.Shj;
import com.shj.setting.bean.CommodityInfo;
import com.shj.setting.bean.ShelfCommodityInfo;
import com.shj.setting.helper.CommodityInfoObtain;
import com.shj.setting.helper.ReadLightInspectionData;
import com.shj.setting.helper.ReadShelfMergeState;
import com.xyshj.database.setting.AppSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ReportCalculatedInventory {
    private static HashMap<Integer, Integer> shelftMergeStateHashMap = new HashMap<>();
    private static HashMap<String, ShelfCommodityInfo> shelfCommodityInfoHashMap = new HashMap<>();
    private static HashMap<Integer, List<Integer>> lightDataHashMap = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.ReportCalculatedInventory$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ReadShelfMergeState.ReadCompleteListener {
        final /* synthetic */ Context val$context;

        AnonymousClass1(Context context) {
            context = context;
        }

        @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
        public void complete(HashMap<Integer, Integer> hashMap) {
            HashMap unused = ReportCalculatedInventory.shelftMergeStateHashMap = hashMap;
            Loger.writeLog("SET", "readShelfMergeState success");
            String machineId = AppSetting.getMachineId(context, null);
            new CommodityInfoObtain().getGoodsInfo(context, machineId, new CommodityInfoObtain.GetInfoCompleteListener() { // from class: com.shj.setting.helper.ReportCalculatedInventory.1.1
                final /* synthetic */ String val$machineId;

                C00741(String machineId2) {
                    machineId = machineId2;
                }

                @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                public void complete(HashMap<String, ShelfCommodityInfo> hashMap2, HashMap<String, CommodityInfo> hashMap3) {
                    HashMap unused2 = ReportCalculatedInventory.shelfCommodityInfoHashMap = hashMap2;
                    Loger.writeLog("SET", "commodityInfoObtain success");
                    ReportCalculatedInventory.readLightData(context, machineId);
                }

                @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                public void error(String str) {
                    Loger.writeLog("SET", str);
                }
            });
        }

        /* renamed from: com.shj.setting.helper.ReportCalculatedInventory$1$1 */
        /* loaded from: classes2.dex */
        class C00741 implements CommodityInfoObtain.GetInfoCompleteListener {
            final /* synthetic */ String val$machineId;

            C00741(String machineId2) {
                machineId = machineId2;
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void complete(HashMap<String, ShelfCommodityInfo> hashMap2, HashMap<String, CommodityInfo> hashMap3) {
                HashMap unused2 = ReportCalculatedInventory.shelfCommodityInfoHashMap = hashMap2;
                Loger.writeLog("SET", "commodityInfoObtain success");
                ReportCalculatedInventory.readLightData(context, machineId);
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void error(String str) {
                Loger.writeLog("SET", str);
            }
        }

        @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
        public void error(int i, String str) {
            Loger.writeLog("SET", "errorInfo");
        }
    }

    public static void report(Context context) {
        new ReadShelfMergeState().readMergeState(context, new ReadShelfMergeState.ReadCompleteListener() { // from class: com.shj.setting.helper.ReportCalculatedInventory.1
            final /* synthetic */ Context val$context;

            AnonymousClass1(Context context2) {
                context = context2;
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void complete(HashMap<Integer, Integer> hashMap) {
                HashMap unused = ReportCalculatedInventory.shelftMergeStateHashMap = hashMap;
                Loger.writeLog("SET", "readShelfMergeState success");
                String machineId2 = AppSetting.getMachineId(context, null);
                new CommodityInfoObtain().getGoodsInfo(context, machineId2, new CommodityInfoObtain.GetInfoCompleteListener() { // from class: com.shj.setting.helper.ReportCalculatedInventory.1.1
                    final /* synthetic */ String val$machineId;

                    C00741(String machineId22) {
                        machineId = machineId22;
                    }

                    @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                    public void complete(HashMap<String, ShelfCommodityInfo> hashMap2, HashMap<String, CommodityInfo> hashMap3) {
                        HashMap unused2 = ReportCalculatedInventory.shelfCommodityInfoHashMap = hashMap2;
                        Loger.writeLog("SET", "commodityInfoObtain success");
                        ReportCalculatedInventory.readLightData(context, machineId);
                    }

                    @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                    public void error(String str) {
                        Loger.writeLog("SET", str);
                    }
                });
            }

            /* renamed from: com.shj.setting.helper.ReportCalculatedInventory$1$1 */
            /* loaded from: classes2.dex */
            class C00741 implements CommodityInfoObtain.GetInfoCompleteListener {
                final /* synthetic */ String val$machineId;

                C00741(String machineId22) {
                    machineId = machineId22;
                }

                @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                public void complete(HashMap<String, ShelfCommodityInfo> hashMap2, HashMap<String, CommodityInfo> hashMap3) {
                    HashMap unused2 = ReportCalculatedInventory.shelfCommodityInfoHashMap = hashMap2;
                    Loger.writeLog("SET", "commodityInfoObtain success");
                    ReportCalculatedInventory.readLightData(context, machineId);
                }

                @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
                public void error(String str) {
                    Loger.writeLog("SET", str);
                }
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void error(int i, String str) {
                Loger.writeLog("SET", "errorInfo");
            }
        });
    }

    /* renamed from: com.shj.setting.helper.ReportCalculatedInventory$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ReadLightInspectionData.ReadLightCompeleteListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$machineId;

        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
        public void error(String str) {
        }

        AnonymousClass2(Context context, String str) {
            context = context;
            str = str;
        }

        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
        public void complete(HashMap<Integer, List<Integer>> hashMap) {
            HashMap unused = ReportCalculatedInventory.lightDataHashMap = hashMap;
            Loger.writeLog("SET", "readLightInspectionData success");
            ReportCalculatedInventory.stockCount(context, str, CalculatedInventory.getFlag());
        }
    }

    public static void readLightData(Context context, String str) {
        new ReadLightInspectionData().readLightData(context, new ReadLightInspectionData.ReadLightCompeleteListener() { // from class: com.shj.setting.helper.ReportCalculatedInventory.2
            final /* synthetic */ Context val$context;
            final /* synthetic */ String val$machineId;

            @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
            public void error(String str2) {
            }

            AnonymousClass2(Context context2, String str2) {
                context = context2;
                str = str2;
            }

            @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
            public void complete(HashMap<Integer, List<Integer>> hashMap) {
                HashMap unused = ReportCalculatedInventory.lightDataHashMap = hashMap;
                Loger.writeLog("SET", "readLightInspectionData success");
                ReportCalculatedInventory.stockCount(context, str, CalculatedInventory.getFlag());
            }
        });
    }

    public static void stockCount(Context context, String str, String str2) {
        int i;
        CalculatedInventory calculatedInventory = new CalculatedInventory();
        List<Integer> shelves = Shj.getShelves();
        HashMap hashMap = new HashMap();
        hashMap.put("jqbh", str);
        hashMap.put("lx", 0);
        hashMap.put("bz", str2);
        hashMap.put("sbsj", CalculatedInventory.getTime());
        ArrayList arrayList = new ArrayList();
        Loger.writeLog("SET", "stockCount shelvesList.size=" + shelves.size());
        Iterator<Integer> it = shelves.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            List<Integer> list = CalculatedInventory.getlightData(shelftMergeStateHashMap, lightDataHashMap, intValue);
            if (list != null && list.size() > 0) {
                Loger.writeLog("SET", "lightData.size=" + list.size());
                ShelfCommodityInfo shelfCommodityInfo = shelfCommodityInfoHashMap.get(String.format("%03d", Integer.valueOf(intValue)));
                int i2 = shelfCommodityInfo != null ? shelfCommodityInfo.length : 0;
                Loger.writeLog("SET", "goodsLength=" + i2);
                if (i2 == 0) {
                    i = -1;
                } else if (list.size() == 6) {
                    i = calculatedInventory.getGoodsCount(context, intValue, CalculatedInventory.shlelvsLength_6_list, Integer.valueOf(i2).intValue() * 10, list);
                } else {
                    i = list.size() == 8 ? calculatedInventory.getGoodsCount(context, intValue, CalculatedInventory.shlelvsLength_8_list, Integer.valueOf(i2).intValue() * 10, list) : -4;
                }
                Loger.writeLog("SET", "货道=" + intValue + " 库存=" + i);
                arrayList.add(CalculatedInventory.getJsonString(intValue, i));
            } else {
                Loger.writeLog("SET", "lightData == null");
                arrayList.add(CalculatedInventory.getJsonString(intValue, -3));
            }
        }
        hashMap.put("hdbhs", arrayList);
        JSONObject jSONObject = new JSONObject(hashMap);
        Loger.writeLog("SET", "report shelf info=" + jSONObject.toString());
        CalculatedInventory.uploadShelfCountInfo(context, jSONObject, false);
    }
}

package com.shj.setting.helper;

import android.content.Context;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.Utils.ToastUitl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CalculatedInventory {
    public static final int GOODS_LENGTH_ISERROR = -2;
    public static final int GOODS_LENGTH_ISZERO = -1;
    public static final int LIGHT_COUNT_NOT_SUPORT = -4;
    public static final int NOT_GET_LIGHT_DATA = -3;
    public static final int OTHER_ERROR = -5;
    public static final int SHELF_LENGTH = 5052;
    public static int[] shlelvsLength_6_list = {672, 730, 730, 730, 730, 730, 730};
    public static int[] shlelvsLength_8_list = {522, 550, 550, 550, 550, 550, 550, 550, 550};
    public HashMap<String, List<OverShelfData>> overShelfDataHashMap = new HashMap<>();

    public static String getErrorInfo(int i) {
        return i != -5 ? i != -4 ? i != -3 ? i != -2 ? i != -1 ? "" : "商品长度没有设置" : "商品长度设置错误" : "未获取到光检数据" : "光检个数不支持" : "未知错误";
    }

    public int getGoodsCount(Context context, int i, int[] iArr, int i2, List<Integer> list) {
        boolean z;
        if (list == null) {
            return -3;
        }
        int size = list.size();
        if (!isGoodsLengthCanUser(i2, iArr)) {
            return -2;
        }
        String str = String.valueOf(i2) + String.valueOf(size);
        List<OverShelfData> list2 = this.overShelfDataHashMap.get(str);
        if (list2 == null) {
            list2 = getOverShelfDataList(iArr, i2, size);
            this.overShelfDataHashMap.put(str, list2);
        }
        Iterator<OverShelfData> it = list2.iterator();
        int i3 = 0;
        while (it.hasNext()) {
            Iterator<Integer> it2 = it.next().lightIndexList.iterator();
            while (true) {
                z = true;
                if (!it2.hasNext()) {
                    z = false;
                    break;
                }
                if (list.get(it2.next().intValue()).intValue() == 1) {
                    break;
                }
            }
            if (!z) {
                break;
            }
            i3++;
        }
        return i3;
    }

    private boolean isGoodsLengthCanUser(int i, int[] iArr) {
        for (int i2 : iArr) {
            if (i <= i2) {
                return false;
            }
        }
        return true;
    }

    private List<OverShelfData> getOverShelfDataList(int[] iArr, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        int shelfAllLength = (getShelfAllLength(iArr) / i) + 1;
        int i3 = 0;
        while (i3 < shelfAllLength) {
            OverShelfData overShelfData = new OverShelfData();
            overShelfData.goodsIndex = i3;
            overShelfData.lightIndexList = new ArrayList();
            overShelfData.startPosition = i3 * i;
            i3++;
            overShelfData.endPosition = i3 * i;
            arrayList.add(overShelfData);
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            i4 += iArr[i5];
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    OverShelfData overShelfData2 = (OverShelfData) it.next();
                    if (isIn(i4, overShelfData2)) {
                        overShelfData2.lightIndexList.add(Integer.valueOf(i5));
                        break;
                    }
                }
            }
        }
        return arrayList;
    }

    private int getShelfAllLength(int[] iArr) {
        int i = 0;
        for (int i2 : iArr) {
            i += i2;
        }
        return i;
    }

    private boolean isIn(int i, OverShelfData overShelfData) {
        return i > overShelfData.startPosition && i < overShelfData.endPosition;
    }

    public static int getStartShelfNo(int i) {
        return i <= 6 ? (i * 33) + 1 : (i < 7 || i > 13) ? ((i / 7) * 300) + ((i % 7) * 33) + 1 : ((i - 7) * 33) + 1 + 300;
    }

    public static List<String> getAllLayerNo(HashMap<Integer, List<Integer>> hashMap) {
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            Iterator<Integer> it2 = hashMap.get(it.next()).iterator();
            while (it2.hasNext()) {
                arrayList.add(String.valueOf(it2.next().intValue()));
            }
        }
        return arrayList;
    }

    public static String getFlag() {
        return UUID.randomUUID().toString();
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static JSONObject getJsonString(int i, int i2) {
        HashMap hashMap = new HashMap();
        hashMap.put("hdh", String.format("%03d", Integer.valueOf(i)));
        hashMap.put("sl", Integer.valueOf(i2));
        return new JSONObject(hashMap);
    }

    public static void uploadShelfCountInfo(Context context, JSONObject jSONObject, boolean z) {
        RequestItem requestItem = new RequestItem(NetAddress.getSaveMachineStockInfo(), jSONObject, "POST");
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.CalculatedInventory.1
            final /* synthetic */ Context val$context;
            final /* synthetic */ boolean val$isShowTip;

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z2) {
            }

            AnonymousClass1(boolean z2, Context context2) {
                z = z2;
                context = context2;
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                int optInt;
                try {
                    JSONObject jSONObject2 = new JSONObject(str);
                    if (!"H0000".equals(jSONObject2.optString("code")) || (optInt = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).optInt("nub")) <= 0) {
                        return true;
                    }
                    if (z) {
                        ToastUitl.showShort(context, "上传盘点数据成功");
                    }
                    Loger.writeLog("NET", "上传计算库存成功，个数=" + optInt);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return true;
                }
            }
        });
        RequestHelper.request(requestItem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.CalculatedInventory$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ boolean val$isShowTip;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass1(boolean z2, Context context2) {
            z = z2;
            context = context2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            int optInt;
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                if (!"H0000".equals(jSONObject2.optString("code")) || (optInt = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).optInt("nub")) <= 0) {
                    return true;
                }
                if (z) {
                    ToastUitl.showShort(context, "上传盘点数据成功");
                }
                Loger.writeLog("NET", "上传计算库存成功，个数=" + optInt);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        }
    }

    public static List<Integer> getlightData(HashMap<Integer, Integer> hashMap, HashMap<Integer, List<Integer>> hashMap2, int i) {
        int i2;
        if (hashMap.containsKey(Integer.valueOf(i))) {
            int intValue = hashMap.get(Integer.valueOf(i)).intValue();
            if (intValue == 1) {
                return hashMap2.get(Integer.valueOf(i));
            }
            if (intValue <= 1) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int i3 = 0;
            for (int i4 = 0; i4 < intValue; i4++) {
                List<Integer> list = hashMap2.get(Integer.valueOf(i + i4));
                if (list != null) {
                    int size = list.size();
                    if (i3 == 0 || i3 == size) {
                        arrayList.add(list);
                        i3 = size;
                    }
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (int i5 = 0; i5 < i3; i5++) {
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        i2 = 0;
                        break;
                    }
                    List list2 = (List) it.next();
                    if (((Integer) list2.get(i5)).intValue() != 0) {
                        i2 = ((Integer) list2.get(i5)).intValue();
                        break;
                    }
                }
                arrayList2.add(Integer.valueOf(i2));
            }
            return null;
        }
        return hashMap2.get(Integer.valueOf(i));
    }
}

package com.shj.setting.helper;

import android.content.Context;
import android.text.TextUtils;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.SettingActivity;
import com.shj.setting.bean.CommodityInfo;
import com.shj.setting.bean.ShelfCommodityInfo;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CommodityInfoObtain {
    private GetInfoCompleteListener getInfoCompleteListener;
    private HashMap<String, CommodityInfo> commodityInfoHashMap = new HashMap<>();
    private HashMap<String, ShelfCommodityInfo> shelfCommodityInfoHashMap = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface GetInfoCompleteListener {
        void complete(HashMap<String, ShelfCommodityInfo> hashMap, HashMap<String, CommodityInfo> hashMap2);

        void error(String str);
    }

    public void getGoodsInfo(Context context, String str, GetInfoCompleteListener getInfoCompleteListener) {
        this.getInfoCompleteListener = getInfoCompleteListener;
        String machineKcslUrl = NetAddress.getMachineKcslUrl();
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            RequestItem requestItem = new RequestItem(machineKcslUrl, jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.CommodityInfoObtain.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                    if (CommodityInfoObtain.this.getInfoCompleteListener != null) {
                        CommodityInfoObtain.this.getInfoCompleteListener.error("获取商品信息失败");
                    }
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    CommodityInfoObtain.this.commodityInfoHashMap.clear();
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (!jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                            return true;
                        }
                        JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                            JSONObject jSONObject3 = optJSONArray.getJSONObject(i2);
                            ShelfCommodityInfo shelfCommodityInfo = new ShelfCommodityInfo();
                            shelfCommodityInfo.strShelf = jSONObject3.optString("hdbh");
                            shelfCommodityInfo.shelf = Integer.valueOf(shelfCommodityInfo.strShelf).intValue();
                            shelfCommodityInfo.stockCount = jSONObject3.optInt("kcsl");
                            shelfCommodityInfo.barCode = jSONObject3.optString("sptxm");
                            shelfCommodityInfo.code = jSONObject3.optString("spbh");
                            shelfCommodityInfo.length = jSONObject3.optInt("spcd");
                            if (SettingActivity.isDebug) {
                                shelfCommodityInfo.barCode = "zkbar" + i2;
                                shelfCommodityInfo.length = 200;
                                if (shelfCommodityInfo.shelf == 6 || shelfCommodityInfo.shelf == 16 || shelfCommodityInfo.shelf == 26) {
                                    Loger.writeLog("SET", shelfCommodityInfo.strShelf + "barCode=" + shelfCommodityInfo.barCode);
                                }
                            }
                            CommodityInfoObtain.this.shelfCommodityInfoHashMap.put(shelfCommodityInfo.strShelf, shelfCommodityInfo);
                            if (!TextUtils.isEmpty(shelfCommodityInfo.barCode) && CommodityInfoObtain.this.commodityInfoHashMap.get(shelfCommodityInfo.barCode) == null) {
                                CommodityInfo commodityInfo = new CommodityInfo();
                                commodityInfo.barCode = shelfCommodityInfo.barCode;
                                commodityInfo.code = shelfCommodityInfo.code;
                                commodityInfo.length = shelfCommodityInfo.length;
                                CommodityInfoObtain.this.commodityInfoHashMap.put(commodityInfo.barCode, commodityInfo);
                            }
                        }
                        Loger.writeLog("SET", "commodityInfoHashMap.size=" + CommodityInfoObtain.this.commodityInfoHashMap.size());
                        if (CommodityInfoObtain.this.getInfoCompleteListener == null) {
                            return true;
                        }
                        CommodityInfoObtain.this.getInfoCompleteListener.complete(CommodityInfoObtain.this.shelfCommodityInfoHashMap, CommodityInfoObtain.this.commodityInfoHashMap);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.CommodityInfoObtain$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            if (CommodityInfoObtain.this.getInfoCompleteListener != null) {
                CommodityInfoObtain.this.getInfoCompleteListener.error("获取商品信息失败");
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            CommodityInfoObtain.this.commodityInfoHashMap.clear();
            try {
                JSONObject jSONObject2 = new JSONObject(str2);
                if (!jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                    return true;
                }
                JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                    JSONObject jSONObject3 = optJSONArray.getJSONObject(i2);
                    ShelfCommodityInfo shelfCommodityInfo = new ShelfCommodityInfo();
                    shelfCommodityInfo.strShelf = jSONObject3.optString("hdbh");
                    shelfCommodityInfo.shelf = Integer.valueOf(shelfCommodityInfo.strShelf).intValue();
                    shelfCommodityInfo.stockCount = jSONObject3.optInt("kcsl");
                    shelfCommodityInfo.barCode = jSONObject3.optString("sptxm");
                    shelfCommodityInfo.code = jSONObject3.optString("spbh");
                    shelfCommodityInfo.length = jSONObject3.optInt("spcd");
                    if (SettingActivity.isDebug) {
                        shelfCommodityInfo.barCode = "zkbar" + i2;
                        shelfCommodityInfo.length = 200;
                        if (shelfCommodityInfo.shelf == 6 || shelfCommodityInfo.shelf == 16 || shelfCommodityInfo.shelf == 26) {
                            Loger.writeLog("SET", shelfCommodityInfo.strShelf + "barCode=" + shelfCommodityInfo.barCode);
                        }
                    }
                    CommodityInfoObtain.this.shelfCommodityInfoHashMap.put(shelfCommodityInfo.strShelf, shelfCommodityInfo);
                    if (!TextUtils.isEmpty(shelfCommodityInfo.barCode) && CommodityInfoObtain.this.commodityInfoHashMap.get(shelfCommodityInfo.barCode) == null) {
                        CommodityInfo commodityInfo = new CommodityInfo();
                        commodityInfo.barCode = shelfCommodityInfo.barCode;
                        commodityInfo.code = shelfCommodityInfo.code;
                        commodityInfo.length = shelfCommodityInfo.length;
                        CommodityInfoObtain.this.commodityInfoHashMap.put(commodityInfo.barCode, commodityInfo);
                    }
                }
                Loger.writeLog("SET", "commodityInfoHashMap.size=" + CommodityInfoObtain.this.commodityInfoHashMap.size());
                if (CommodityInfoObtain.this.getInfoCompleteListener == null) {
                    return true;
                }
                CommodityInfoObtain.this.getInfoCompleteListener.complete(CommodityInfoObtain.this.shelfCommodityInfoHashMap, CommodityInfoObtain.this.commodityInfoHashMap);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        }
    }
}

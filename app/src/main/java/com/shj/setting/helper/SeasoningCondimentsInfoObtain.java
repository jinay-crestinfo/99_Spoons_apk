package com.shj.setting.helper;

import android.content.Context;
import android.text.TextUtils;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.bean.SeasoningCondimentsInfo;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SeasoningCondimentsInfoObtain {
    private GetInfoCompleteListener getInfoCompleteListener;
    private List<SeasoningCondimentsInfo> seasoningCondimentsInfoList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface GetInfoCompleteListener {
        void complete(List<SeasoningCondimentsInfo> list);

        void error(String str);
    }

    public void getGoodsInfo(Context context, String str, GetInfoCompleteListener getInfoCompleteListener) {
        this.getInfoCompleteListener = getInfoCompleteListener;
        String machineDispatchListQueryUrl = NetAddress.getMachineDispatchListQueryUrl();
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            RequestItem requestItem = new RequestItem(machineDispatchListQueryUrl, jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.SeasoningCondimentsInfoObtain.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                    if (SeasoningCondimentsInfoObtain.this.getInfoCompleteListener != null) {
                        SeasoningCondimentsInfoObtain.this.getInfoCompleteListener.error("获取调货信息失败");
                    }
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.clear();
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (!jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                            return true;
                        }
                        JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                            JSONObject jSONObject3 = optJSONArray.getJSONObject(i2);
                            SeasoningCondimentsInfo seasoningCondimentsInfo = new SeasoningCondimentsInfo();
                            seasoningCondimentsInfo.strShelf = jSONObject3.optString("hdbh");
                            seasoningCondimentsInfo.shelf = Integer.valueOf(seasoningCondimentsInfo.strShelf).intValue();
                            seasoningCondimentsInfo.goodsCode = jSONObject3.optString("spbh");
                            seasoningCondimentsInfo.targetReplenishedCount = jSONObject3.optInt("dbsl");
                            seasoningCondimentsInfo.targetCount = jSONObject3.optInt("dbhkc");
                            seasoningCondimentsInfo.goodsBatch = jSONObject3.optString("scpc");
                            seasoningCondimentsInfo.barCode = jSONObject3.optString("sptxm");
                            seasoningCondimentsInfo.price = jSONObject3.optString("spdj");
                            SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.add(seasoningCondimentsInfo);
                        }
                        Loger.writeLog("SET", "seasoningCondimentsInfoHashMap.size=" + SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.size());
                        if (SeasoningCondimentsInfoObtain.this.getInfoCompleteListener == null) {
                            return true;
                        }
                        SeasoningCondimentsInfoObtain.this.getInfoCompleteListener.complete(SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList);
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
    /* renamed from: com.shj.setting.helper.SeasoningCondimentsInfoObtain$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            if (SeasoningCondimentsInfoObtain.this.getInfoCompleteListener != null) {
                SeasoningCondimentsInfoObtain.this.getInfoCompleteListener.error("获取调货信息失败");
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.clear();
            try {
                JSONObject jSONObject2 = new JSONObject(str2);
                if (!jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                    return true;
                }
                JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                    JSONObject jSONObject3 = optJSONArray.getJSONObject(i2);
                    SeasoningCondimentsInfo seasoningCondimentsInfo = new SeasoningCondimentsInfo();
                    seasoningCondimentsInfo.strShelf = jSONObject3.optString("hdbh");
                    seasoningCondimentsInfo.shelf = Integer.valueOf(seasoningCondimentsInfo.strShelf).intValue();
                    seasoningCondimentsInfo.goodsCode = jSONObject3.optString("spbh");
                    seasoningCondimentsInfo.targetReplenishedCount = jSONObject3.optInt("dbsl");
                    seasoningCondimentsInfo.targetCount = jSONObject3.optInt("dbhkc");
                    seasoningCondimentsInfo.goodsBatch = jSONObject3.optString("scpc");
                    seasoningCondimentsInfo.barCode = jSONObject3.optString("sptxm");
                    seasoningCondimentsInfo.price = jSONObject3.optString("spdj");
                    SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.add(seasoningCondimentsInfo);
                }
                Loger.writeLog("SET", "seasoningCondimentsInfoHashMap.size=" + SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList.size());
                if (SeasoningCondimentsInfoObtain.this.getInfoCompleteListener == null) {
                    return true;
                }
                SeasoningCondimentsInfoObtain.this.getInfoCompleteListener.complete(SeasoningCondimentsInfoObtain.this.seasoningCondimentsInfoList);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        }
    }
}

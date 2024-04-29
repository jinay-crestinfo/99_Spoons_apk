package com.shj.setting.helper;

import android.text.TextUtils;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.Shj;
import com.shj.setting.NetAddress.NetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UploadOperationRecord {
    public static void upload(String str, String str2, String str3) {
        String operationInfoUrl = NetAddress.getOperationInfoUrl();
        String machineId = Shj.getMachineId();
        try {
            if (TextUtils.isEmpty(machineId)) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", machineId);
            jSONObject.put("czry", str);
            jSONObject.put("cz", str2);
            jSONObject.put("xq", str3);
            jSONObject.put("sbsj", getTime());
            RequestItem requestItem = new RequestItem(operationInfoUrl, jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.UploadOperationRecord.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
                    return true;
                }

                AnonymousClass1() {
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.UploadOperationRecord$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
            return true;
        }

        AnonymousClass1() {
        }
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}

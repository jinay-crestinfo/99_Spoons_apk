package com.oysb.xy.net.thirdparty;

//import com.iflytek.cloud.SpeechUtility;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public abstract class ThirdPartyReport {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    private static String thirdPartyUrl = "http://nuvending.twdor.com/sandbox/";
    String FunCode;
    String MachineSN;
    String PackNO;
    String SoftVer;
    String info;
    String jgh;
    OnRequestListenter l;
    int requestMaxCount = 3;
    int repeatDelay = 5000;

    public static void setThirdPartyUrl(String str) {
        thirdPartyUrl = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyRequestItem extends RequestItem {
        public MyRequestItem(String str, RequestParams requestParams, String str2) {
            super(str, requestParams, str2);
        }

        @Override // com.oysb.utils.http.RequestItem
        public long getRepeatDelay() {
            return ThirdPartyReport.this.repeatDelay;
        }
    }

    private String encodeURIComponent(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public void request(OnRequestListenter onRequestListener) {
        try {
            this.l = onRequestListener;
            RequestParams requestParams = new RequestParams();
            requestParams.put("FunCode", this.FunCode);
            requestParams.put("PackNO", this.PackNO);
            requestParams.put("MachineSN", this.MachineSN);
            requestParams.put("TimeStamp", sdf.format(new Date()));

            if (this.FunCode.equals("1001")) {
                if (this.jgh != null) {
                    requestParams.put("jgh", this.jgh);
                }
            } else if (this.FunCode.equals("1002") && this.SoftVer != null) {
                requestParams.put("SoftVer", this.SoftVer);
            }

            if (this.info != null) {
                requestParams.put(Loger.InfoType_Info, this.info);
            }

            MyRequestItem myRequestItem = new MyRequestItem(thirdPartyUrl, requestParams, HttpGet.METHOD_NAME);
            myRequestItem.setRequestMaxCount(this.requestMaxCount);
            myRequestItem.setLostAble(true);
            myRequestItem.setRepeatDelay(this.repeatDelay);
            myRequestItem.setKey(this.PackNO);
            myRequestItem.setApplicationType("application/txt_NoEncode");

            myRequestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() {
                @Override
                public void onRequestFinished(RequestItem requestItem, boolean success) {
                    // Not implemented
                }

                @Override
                public void onFailure(RequestItem requestItem, int statusCode, String response, Throwable throwable) {
                    Loger.writeLog("REQUEST", statusCode + StringUtils.SPACE + response);
                    if (ThirdPartyReport.this.l != null) {
                        ThirdPartyReport.this.l.onFail(response);
                    }
                }

                @Override
                public boolean onSuccess(RequestItem requestItem, int statusCode, String response) {
                    try {
                        String cleanedResponse = response.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                        JSONObject jsonObject = new JSONObject(cleanedResponse);

                        if (jsonObject.getString("status").equals("0")) {
                            if (ThirdPartyReport.this.l != null) {
                                ThirdPartyReport.this.l.onSuccess(cleanedResponse);
                            }
                            return !ThirdPartyReport.this.FunCode.equals("5002");
                        }

                        if (ThirdPartyReport.this.l != null) {
                            ThirdPartyReport.this.l.onFail(cleanedResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            RequestHelper.request(myRequestItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.xy.net.thirdparty.ThirdPartyReport$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem, int i, String str4, Throwable th) {
            Loger.writeLog("REQUEST", i + StringUtils.SPACE + str4);
            if (ThirdPartyReport.this.l != null) {
                ThirdPartyReport.this.l.onFail(str4);
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem, int i, String str4) {
            try {
                String cleanedResponse = str4.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                JSONObject jsonObject = new JSONObject(cleanedResponse);

                if (jsonObject.getString("status").equals("0")) {
                    if (ThirdPartyReport.this.l != null) {
                        ThirdPartyReport.this.l.onSuccess(cleanedResponse);
                    }
                    return !ThirdPartyReport.this.FunCode.equals("5002");
                }

                if (ThirdPartyReport.this.l != null) {
                    ThirdPartyReport.this.l.onFail(cleanedResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }
}

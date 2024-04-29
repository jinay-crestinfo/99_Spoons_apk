package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.downloader.Error;
import com.downloader.Progress;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.PRDownloaderTool;
import com.oysb.utils.activity.ActivityHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.Utils.Constant;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.helper.AppManageHelper;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.machine.BuildConfig;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SoftManageDialog extends Dialog {
    private static Context mContext;
    private static MyAdapter myAdapter;
    private Button bt_ok;
    private Button bt_system_setting;
    private int count;
    private LayoutInflater inflater;
    private boolean isChinese;
    private ListView listView;
    private TextView tv_instruction;
    private static final String appfolder = SDFileUtils.SDCardRoot + "xyShj/download";
    private static List<AppInfo> appInfoList = new ArrayList();
    private static final String[][] packages = {new String[]{BuildConfig.APPLICATION_ID, "售货机程序", ""}, new String[]{Zoloz.SMILE2PAY_PACKAGE, "支付宝刷脸程序", "zfbslcx"}, new String[]{"com.alipay.iot.service", "支付宝刷脸服务程序", "zfbslfwcx"}, new String[]{"com.alipay.iot.master", "支付宝授权程序", "zfbsqcx"}, new String[]{"com.tencent.wxpayface", "微信刷脸程序", "wxslcx"}, new String[]{"com.estrongs.android.pop", "Es文件浏览器", "eswjllq"}, new String[]{"com.juphoon.cloud.xinyuan", "网络电话", "wldh"}, new String[]{"com.zkteco.android.xyliveface", "中控身份识别程序", "zkxfsbcx"}, new String[]{Constant.safeAppName, "安全启动程序", "aqqdcx"}, new String[]{"com.bjw.ComAssistant", "系统串口调试工具", "xtcktsgz"}, new String[]{"com.licheedev.serialtool", "串口调试工具", "cktsgz"}, new String[]{"com.oysb.floattestapp", "售货机日志查看工具", "shjrzckgj"}, new String[]{"com.google.android.tts", "谷歌文字转语音引擎", "ggwjzyyyc"}, new String[]{"com.baidu.duersdk.opensdk", "百度度秘文字转语音引擎", "bddmwjzyyyc"}, new String[]{"com.iflytek.speechcloud", "科大讯飞文字转语音引擎", "kdxfwjzyyyc"}, new String[]{"com.keanbin.pinyinime", "兴元拼音输入法", "xypxsrf"}};

    /* loaded from: classes2.dex */
    public static class AppInfo {
        public String appName;
        public String appType;
        public String appVersion;
        public int appVersionCode;
        public String appVersionName;
        public boolean autoInstall;
        public Progress downloadProgress;
        public String downloadUrl;
        public String higherEditionsAppPath;
        public int index;
        public boolean isAllShowInfo;
        public boolean isInstalled;
        public String packageName;
    }

    static /* synthetic */ int access$708(SoftManageDialog softManageDialog) {
        int i = softManageDialog.count;
        softManageDialog.count = i + 1;
        return i;
    }

    public SoftManageDialog(Context context) {
        super(context, R.style.loading_style);
        this.isChinese = true;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_soft_manage);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_system_setting = (Button) findViewById(R.id.bt_system_setting);
        TextView textView = (TextView) findViewById(R.id.tv_instruction);
        this.tv_instruction = textView;
        textView.setText(Html.fromHtml(mContext.getResources().getString(R.string.soft_manage_instuction).replaceAll(StringUtils.LF, "<br>")));
        File file = new File(appfolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        setCanceledOnTouchOutside(false);
        setListener();
        MyAdapter myAdapter2 = new MyAdapter(mContext);
        myAdapter = myAdapter2;
        this.listView.setAdapter((ListAdapter) myAdapter2);
        this.isChinese = "zh".equalsIgnoreCase(CommonTool.getLanguage(mContext));
        getNetAppInfo();
    }

    /* renamed from: com.shj.setting.Dialog.SoftManageDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SoftManageDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SoftManageDialog.this.dismiss();
            }
        });
        this.bt_system_setting.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SoftManageDialog.mContext.startActivity(new Intent("android.settings.SETTINGS"));
                SoftManageDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.SoftManageDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SoftManageDialog.mContext.startActivity(new Intent("android.settings.SETTINGS"));
            SoftManageDialog.this.dismiss();
        }
    }

    public void getNetAppInfo() {
        try {
            Runtime.getRuntime().exec("chmod 777 " + appfolder);
        } catch (Exception unused) {
        }
        String querySoftwareInfo = NetAddress.getQuerySoftwareInfo();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", AppSetting.getMachineId(mContext, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestItem requestItem = new RequestItem(querySoftwareInfo, jSONObject, "POST");
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.3
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                SoftManageDialog.appInfoList.clear();
                try {
                    JSONObject jSONObject2 = new JSONObject(str);
                    if ("H0000".equals(jSONObject2.optString("code"))) {
                        JSONArray optJSONArray = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                            JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                            AppInfo appInfo = new AppInfo();
                            appInfo.packageName = optJSONObject.optString("rjbm");
                            if (SoftManageDialog.this.isChinese) {
                                appInfo.appName = optJSONObject.optString("rjmc");
                            } else {
                                appInfo.appName = appInfo.packageName;
                            }
                            appInfo.appType = optJSONObject.optString("applx");
                            appInfo.isAllShowInfo = true;
                            PackageInfo packageInfo = SetUtils.getPackageInfo(SoftManageDialog.mContext, appInfo.packageName);
                            if (packageInfo == null) {
                                appInfo.isInstalled = false;
                                appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(SoftManageDialog.mContext, SoftManageDialog.appfolder, appInfo.packageName, "0", 0, false);
                            } else {
                                appInfo.isInstalled = true;
                                appInfo.appVersionName = packageInfo.versionName;
                                appInfo.appVersionCode = packageInfo.versionCode;
                                appInfo.appVersion = packageInfo.versionName + "." + packageInfo.versionCode;
                                appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(SoftManageDialog.mContext, SoftManageDialog.appfolder, appInfo.packageName, packageInfo.versionName, packageInfo.versionCode, false);
                            }
                            SoftManageDialog.appInfoList.add(appInfo);
                            if (SoftManageDialog.myAdapter != null) {
                                SoftManageDialog.myAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                return true;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.Dialog.SoftManageDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            SoftManageDialog.appInfoList.clear();
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                if ("H0000".equals(jSONObject2.optString("code"))) {
                    JSONArray optJSONArray = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                        AppInfo appInfo = new AppInfo();
                        appInfo.packageName = optJSONObject.optString("rjbm");
                        if (SoftManageDialog.this.isChinese) {
                            appInfo.appName = optJSONObject.optString("rjmc");
                        } else {
                            appInfo.appName = appInfo.packageName;
                        }
                        appInfo.appType = optJSONObject.optString("applx");
                        appInfo.isAllShowInfo = true;
                        PackageInfo packageInfo = SetUtils.getPackageInfo(SoftManageDialog.mContext, appInfo.packageName);
                        if (packageInfo == null) {
                            appInfo.isInstalled = false;
                            appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(SoftManageDialog.mContext, SoftManageDialog.appfolder, appInfo.packageName, "0", 0, false);
                        } else {
                            appInfo.isInstalled = true;
                            appInfo.appVersionName = packageInfo.versionName;
                            appInfo.appVersionCode = packageInfo.versionCode;
                            appInfo.appVersion = packageInfo.versionName + "." + packageInfo.versionCode;
                            appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(SoftManageDialog.mContext, SoftManageDialog.appfolder, appInfo.packageName, packageInfo.versionName, packageInfo.versionCode, false);
                        }
                        SoftManageDialog.appInfoList.add(appInfo);
                        if (SoftManageDialog.myAdapter != null) {
                            SoftManageDialog.myAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            return true;
        }
    }

    public void getAppInfo() {
        appInfoList.clear();
        try {
            Runtime.getRuntime().exec("chmod 777 " + appfolder);
        } catch (Exception unused) {
        }
        int i = 0;
        for (String[] strArr : packages) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = strArr[0];
            appInfo.appName = strArr[1];
            appInfo.appType = strArr[2];
            if (i >= 9) {
                appInfo.isAllShowInfo = true;
            }
            PackageInfo packageInfo = SetUtils.getPackageInfo(mContext, appInfo.packageName);
            if (packageInfo == null) {
                appInfo.isInstalled = false;
                appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(mContext, appfolder, appInfo.packageName, "0", 0, false);
            } else {
                appInfo.isInstalled = true;
                appInfo.appVersionName = packageInfo.versionName;
                appInfo.appVersionCode = packageInfo.versionCode;
                appInfo.appVersion = packageInfo.versionName + "." + packageInfo.versionCode;
                appInfo.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(mContext, appfolder, appInfo.packageName, packageInfo.versionName, packageInfo.versionCode, false);
            }
            appInfoList.add(appInfo);
            i++;
        }
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return SoftManageDialog.appInfoList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = SoftManageDialog.this.inflater.inflate(R.layout.layout_soft_manage_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
                viewHolder.tv_app_version = (TextView) view.findViewById(R.id.tv_app_version);
                viewHolder.tv_isinstalled = (TextView) view.findViewById(R.id.tv_isinstalled);
                viewHolder.bt_check_update = (Button) view.findViewById(R.id.bt_check_update);
                viewHolder.bt_install = (Button) view.findViewById(R.id.bt_install);
                viewHolder.bt_uninstall = (Button) view.findViewById(R.id.bt_uninstall);
                viewHolder.bt_delete = (Button) view.findViewById(R.id.bt_delete);
                viewHolder.bt_download = (Button) view.findViewById(R.id.bt_download);
                viewHolder.seekbar = (SeekBar) view.findViewById(R.id.seekbar);
                viewHolder.tv_progress = (TextView) view.findViewById(R.id.tv_progress);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            AppInfo appInfo = (AppInfo) SoftManageDialog.appInfoList.get(i);
            appInfo.index = i;
            viewHolder.tv_app_name.setText(appInfo.appName);
            if (appInfo.isInstalled) {
                viewHolder.tv_isinstalled.setText(SoftManageDialog.mContext.getString(R.string.installed));
                viewHolder.tv_isinstalled.setTextColor(SoftManageDialog.mContext.getResources().getColor(R.color.color_text));
                viewHolder.tv_app_version.setText(((AppInfo) SoftManageDialog.appInfoList.get(i)).appVersion);
                viewHolder.bt_check_update.setText(R.string.check_update);
                viewHolder.bt_uninstall.setEnabled(true);
                viewHolder.bt_uninstall.setTextColor(-1);
            } else {
                viewHolder.tv_isinstalled.setText(SoftManageDialog.mContext.getString(R.string.not_installed));
                viewHolder.tv_isinstalled.setTextColor(-6710887);
                viewHolder.bt_check_update.setText(R.string.get_download_url);
                viewHolder.bt_uninstall.setEnabled(false);
                viewHolder.bt_uninstall.setTextColor(SoftManageDialog.mContext.getResources().getColor(R.color.color_text));
            }
            if (appInfo.higherEditionsAppPath != null) {
                viewHolder.bt_install.setEnabled(true);
                viewHolder.bt_install.setTextColor(-1);
                viewHolder.bt_delete.setEnabled(true);
                viewHolder.bt_delete.setTextColor(-1);
                if (appInfo.autoInstall) {
                    viewHolder.bt_install.performClick();
                    appInfo.autoInstall = false;
                }
            } else {
                viewHolder.bt_install.setEnabled(false);
                viewHolder.bt_install.setTextColor(SoftManageDialog.mContext.getResources().getColor(R.color.color_text));
                viewHolder.bt_delete.setEnabled(false);
                viewHolder.bt_delete.setTextColor(SoftManageDialog.mContext.getResources().getColor(R.color.color_text));
            }
            if (appInfo.downloadUrl != null) {
                viewHolder.bt_download.setEnabled(true);
                viewHolder.bt_download.setTextColor(-1);
            } else {
                viewHolder.bt_download.setEnabled(false);
                viewHolder.bt_download.setTextColor(SoftManageDialog.mContext.getResources().getColor(R.color.color_text));
            }
            if (appInfo.downloadProgress != null) {
                viewHolder.seekbar.setVisibility(0);
                viewHolder.tv_progress.setVisibility(0);
                viewHolder.seekbar.setProgress((int) ((appInfo.downloadProgress.currentBytes * 100) / appInfo.downloadProgress.totalBytes));
                float f = ((float) appInfo.downloadProgress.currentBytes) / 1048576.0f;
                float f2 = ((float) appInfo.downloadProgress.totalBytes) / 1048576.0f;
                DecimalFormat decimalFormat = new DecimalFormat("##.00");
                viewHolder.tv_progress.setText(decimalFormat.format(f) + "M/" + decimalFormat.format(f2) + "M");
            } else {
                viewHolder.seekbar.setVisibility(8);
                viewHolder.tv_progress.setVisibility(8);
            }
            viewHolder.bt_install.setTag(appInfo);
            viewHolder.bt_install.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    AppInfo appInfo2 = (AppInfo) view2.getTag();
                    if (appInfo2.higherEditionsAppPath != null) {
                        SoftManageDialog.this.installApk(SoftManageDialog.mContext, appInfo2.higherEditionsAppPath);
                        appInfo2.higherEditionsAppPath = null;
                        MyAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            viewHolder.bt_uninstall.setTag(appInfo);
            viewHolder.bt_uninstall.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.2
                AnonymousClass2() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    AppInfo appInfo2 = (AppInfo) view2.getTag();
                    TipDialog tipDialog = new TipDialog(SoftManageDialog.mContext, 0, SoftManageDialog.mContext.getString(R.string.uninstall) + appInfo2.appName, SoftManageDialog.mContext.getString(R.string.determine), SoftManageDialog.mContext.getString(R.string.cancel));
                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.2.1
                        final /* synthetic */ AppInfo val$appInfo;

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        AnonymousClass1(AppInfo appInfo22) {
                            appInfo2 = appInfo22;
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                            SoftManageDialog.mContext.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appInfo2.packageName, null)));
                        }
                    });
                    tipDialog.show();
                }

                /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$2$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements TipDialog.TipDialogListener {
                    final /* synthetic */ AppInfo val$appInfo;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(AppInfo appInfo22) {
                        appInfo2 = appInfo22;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        SoftManageDialog.mContext.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appInfo2.packageName, null)));
                    }
                }
            });
            viewHolder.bt_delete.setTag(appInfo);
            viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.3
                AnonymousClass3() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    AppInfo appInfo2 = (AppInfo) view2.getTag();
                    TipDialog tipDialog = new TipDialog(SoftManageDialog.mContext, 0, SoftManageDialog.mContext.getString(R.string.delete) + appInfo2.higherEditionsAppPath, SoftManageDialog.mContext.getString(R.string.determine), SoftManageDialog.mContext.getString(R.string.cancel));
                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.3.1
                        final /* synthetic */ AppInfo val$appInfo;

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        AnonymousClass1(AppInfo appInfo22) {
                            appInfo2 = appInfo22;
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                            if (appInfo2.higherEditionsAppPath == null || !new File(appInfo2.higherEditionsAppPath).delete()) {
                                return;
                            }
                            appInfo2.higherEditionsAppPath = null;
                            MyAdapter.this.notifyDataSetChanged();
                        }
                    });
                    tipDialog.show();
                }

                /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$3$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements TipDialog.TipDialogListener {
                    final /* synthetic */ AppInfo val$appInfo;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(AppInfo appInfo22) {
                        appInfo2 = appInfo22;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        if (appInfo2.higherEditionsAppPath == null || !new File(appInfo2.higherEditionsAppPath).delete()) {
                            return;
                        }
                        appInfo2.higherEditionsAppPath = null;
                        MyAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            viewHolder.bt_check_update.setTag(appInfo);
            viewHolder.bt_check_update.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.4
                AnonymousClass4() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    AppInfo appInfo2 = (AppInfo) view2.getTag();
                    LoadingDialog loadingDialog = new LoadingDialog(SoftManageDialog.mContext, R.string.lab_waiting);
                    loadingDialog.show();
                    AppManageHelper.getAppDownloadUrl(SoftManageDialog.mContext, appInfo2.appType, appInfo2.packageName, appInfo2.appVersionName, appInfo2.appVersionCode, new AppManageHelper.GetDownloadUrlListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.4.1
                        final /* synthetic */ AppInfo val$appInfo;
                        final /* synthetic */ LoadingDialog val$loadingDialog;

                        AnonymousClass1(LoadingDialog loadingDialog2, AppInfo appInfo22) {
                            loadingDialog = loadingDialog2;
                            appInfo2 = appInfo22;
                        }

                        @Override // com.shj.setting.helper.AppManageHelper.GetDownloadUrlListener
                        public void getDownloadUrl(String str) {
                            loadingDialog.dismiss();
                            appInfo2.downloadUrl = str;
                            MyAdapter.this.notifyDataSetChanged();
                            if (str == null) {
                                ToastUitl.showShort(SoftManageDialog.mContext, R.string.not_find_new_apk);
                            }
                        }
                    });
                }

                /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$4$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements AppManageHelper.GetDownloadUrlListener {
                    final /* synthetic */ AppInfo val$appInfo;
                    final /* synthetic */ LoadingDialog val$loadingDialog;

                    AnonymousClass1(LoadingDialog loadingDialog2, AppInfo appInfo22) {
                        loadingDialog = loadingDialog2;
                        appInfo2 = appInfo22;
                    }

                    @Override // com.shj.setting.helper.AppManageHelper.GetDownloadUrlListener
                    public void getDownloadUrl(String str) {
                        loadingDialog.dismiss();
                        appInfo2.downloadUrl = str;
                        MyAdapter.this.notifyDataSetChanged();
                        if (str == null) {
                            ToastUitl.showShort(SoftManageDialog.mContext, R.string.not_find_new_apk);
                        }
                    }
                }
            });
            viewHolder.bt_download.setTag(appInfo);
            viewHolder.bt_download.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.5
                AnonymousClass5() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    String fileName;
                    AppInfo appInfo2 = (AppInfo) view2.getTag();
                    String str = appInfo2.downloadUrl;
                    if (str != null) {
                        if (str.contains("&fn=") && str.contains(".apk&")) {
                            int indexOf = str.indexOf("&fn=") + 4;
                            fileName = str.substring(indexOf, str.indexOf(".apk&", indexOf) + 4);
                        } else {
                            fileName = SDFileUtils.getFileName(str);
                        }
                        PRDownloaderTool.addImmediatelyDownloadTask(appInfo2.downloadUrl, SoftManageDialog.appfolder, fileName, new PRDownloaderTool.OnProgressConditionListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.5.1
                            final /* synthetic */ AppInfo val$appInfo;

                            AnonymousClass1(AppInfo appInfo22) {
                                appInfo2 = appInfo22;
                            }

                            @Override // com.oysb.utils.PRDownloaderTool.OnProgressConditionListener
                            public void onProgress(Progress progress) {
                                if (SoftManageDialog.this.count % 100 == 0) {
                                    appInfo2.downloadProgress = progress;
                                    MyAdapter.this.notifyDataSetChanged();
                                }
                                SoftManageDialog.access$708(SoftManageDialog.this);
                            }
                        }, new PRDownloaderTool.OnDownloadConditionListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.5.2
                            final /* synthetic */ AppInfo val$appInfo;

                            AnonymousClass2(AppInfo appInfo22) {
                                appInfo2 = appInfo22;
                            }

                            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                            public void onDownloadComplete(String str2) {
                                appInfo2.higherEditionsAppPath = str2;
                                appInfo2.downloadProgress = null;
                                appInfo2.autoInstall = true;
                                MyAdapter.this.notifyDataSetChanged();
                                ToastUitl.showShort(SoftManageDialog.mContext, R.string.lab_downloadsuccess);
                            }

                            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                            public void onError(Error error) {
                                ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_error);
                            }

                            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                            public void downloadFileExists() {
                                ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_file_Exists);
                            }
                        });
                        view2.setEnabled(false);
                        appInfo22.downloadUrl = null;
                    }
                }

                /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$5$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements PRDownloaderTool.OnProgressConditionListener {
                    final /* synthetic */ AppInfo val$appInfo;

                    AnonymousClass1(AppInfo appInfo22) {
                        appInfo2 = appInfo22;
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnProgressConditionListener
                    public void onProgress(Progress progress) {
                        if (SoftManageDialog.this.count % 100 == 0) {
                            appInfo2.downloadProgress = progress;
                            MyAdapter.this.notifyDataSetChanged();
                        }
                        SoftManageDialog.access$708(SoftManageDialog.this);
                    }
                }

                /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$5$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements PRDownloaderTool.OnDownloadConditionListener {
                    final /* synthetic */ AppInfo val$appInfo;

                    AnonymousClass2(AppInfo appInfo22) {
                        appInfo2 = appInfo22;
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void onDownloadComplete(String str2) {
                        appInfo2.higherEditionsAppPath = str2;
                        appInfo2.downloadProgress = null;
                        appInfo2.autoInstall = true;
                        MyAdapter.this.notifyDataSetChanged();
                        ToastUitl.showShort(SoftManageDialog.mContext, R.string.lab_downloadsuccess);
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void onError(Error error) {
                        ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_error);
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void downloadFileExists() {
                        ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_file_Exists);
                    }
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AppInfo appInfo2 = (AppInfo) view2.getTag();
                if (appInfo2.higherEditionsAppPath != null) {
                    SoftManageDialog.this.installApk(SoftManageDialog.mContext, appInfo2.higherEditionsAppPath);
                    appInfo2.higherEditionsAppPath = null;
                    MyAdapter.this.notifyDataSetChanged();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AppInfo appInfo22 = (AppInfo) view2.getTag();
                TipDialog tipDialog = new TipDialog(SoftManageDialog.mContext, 0, SoftManageDialog.mContext.getString(R.string.uninstall) + appInfo22.appName, SoftManageDialog.mContext.getString(R.string.determine), SoftManageDialog.mContext.getString(R.string.cancel));
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.2.1
                    final /* synthetic */ AppInfo val$appInfo;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(AppInfo appInfo222) {
                        appInfo2 = appInfo222;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        SoftManageDialog.mContext.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appInfo2.packageName, null)));
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TipDialog.TipDialogListener {
                final /* synthetic */ AppInfo val$appInfo;

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1(AppInfo appInfo222) {
                    appInfo2 = appInfo222;
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    SoftManageDialog.mContext.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appInfo2.packageName, null)));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$3 */
        /* loaded from: classes2.dex */
        public class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AppInfo appInfo22 = (AppInfo) view2.getTag();
                TipDialog tipDialog = new TipDialog(SoftManageDialog.mContext, 0, SoftManageDialog.mContext.getString(R.string.delete) + appInfo22.higherEditionsAppPath, SoftManageDialog.mContext.getString(R.string.determine), SoftManageDialog.mContext.getString(R.string.cancel));
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.3.1
                    final /* synthetic */ AppInfo val$appInfo;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(AppInfo appInfo222) {
                        appInfo2 = appInfo222;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        if (appInfo2.higherEditionsAppPath == null || !new File(appInfo2.higherEditionsAppPath).delete()) {
                            return;
                        }
                        appInfo2.higherEditionsAppPath = null;
                        MyAdapter.this.notifyDataSetChanged();
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TipDialog.TipDialogListener {
                final /* synthetic */ AppInfo val$appInfo;

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1(AppInfo appInfo222) {
                    appInfo2 = appInfo222;
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    if (appInfo2.higherEditionsAppPath == null || !new File(appInfo2.higherEditionsAppPath).delete()) {
                        return;
                    }
                    appInfo2.higherEditionsAppPath = null;
                    MyAdapter.this.notifyDataSetChanged();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$4 */
        /* loaded from: classes2.dex */
        public class AnonymousClass4 implements View.OnClickListener {
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AppInfo appInfo22 = (AppInfo) view2.getTag();
                LoadingDialog loadingDialog2 = new LoadingDialog(SoftManageDialog.mContext, R.string.lab_waiting);
                loadingDialog2.show();
                AppManageHelper.getAppDownloadUrl(SoftManageDialog.mContext, appInfo22.appType, appInfo22.packageName, appInfo22.appVersionName, appInfo22.appVersionCode, new AppManageHelper.GetDownloadUrlListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.4.1
                    final /* synthetic */ AppInfo val$appInfo;
                    final /* synthetic */ LoadingDialog val$loadingDialog;

                    AnonymousClass1(LoadingDialog loadingDialog22, AppInfo appInfo222) {
                        loadingDialog = loadingDialog22;
                        appInfo2 = appInfo222;
                    }

                    @Override // com.shj.setting.helper.AppManageHelper.GetDownloadUrlListener
                    public void getDownloadUrl(String str) {
                        loadingDialog.dismiss();
                        appInfo2.downloadUrl = str;
                        MyAdapter.this.notifyDataSetChanged();
                        if (str == null) {
                            ToastUitl.showShort(SoftManageDialog.mContext, R.string.not_find_new_apk);
                        }
                    }
                });
            }

            /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$4$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements AppManageHelper.GetDownloadUrlListener {
                final /* synthetic */ AppInfo val$appInfo;
                final /* synthetic */ LoadingDialog val$loadingDialog;

                AnonymousClass1(LoadingDialog loadingDialog22, AppInfo appInfo222) {
                    loadingDialog = loadingDialog22;
                    appInfo2 = appInfo222;
                }

                @Override // com.shj.setting.helper.AppManageHelper.GetDownloadUrlListener
                public void getDownloadUrl(String str) {
                    loadingDialog.dismiss();
                    appInfo2.downloadUrl = str;
                    MyAdapter.this.notifyDataSetChanged();
                    if (str == null) {
                        ToastUitl.showShort(SoftManageDialog.mContext, R.string.not_find_new_apk);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$5 */
        /* loaded from: classes2.dex */
        public class AnonymousClass5 implements View.OnClickListener {
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String fileName;
                AppInfo appInfo22 = (AppInfo) view2.getTag();
                String str = appInfo22.downloadUrl;
                if (str != null) {
                    if (str.contains("&fn=") && str.contains(".apk&")) {
                        int indexOf = str.indexOf("&fn=") + 4;
                        fileName = str.substring(indexOf, str.indexOf(".apk&", indexOf) + 4);
                    } else {
                        fileName = SDFileUtils.getFileName(str);
                    }
                    PRDownloaderTool.addImmediatelyDownloadTask(appInfo22.downloadUrl, SoftManageDialog.appfolder, fileName, new PRDownloaderTool.OnProgressConditionListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.5.1
                        final /* synthetic */ AppInfo val$appInfo;

                        AnonymousClass1(AppInfo appInfo222) {
                            appInfo2 = appInfo222;
                        }

                        @Override // com.oysb.utils.PRDownloaderTool.OnProgressConditionListener
                        public void onProgress(Progress progress) {
                            if (SoftManageDialog.this.count % 100 == 0) {
                                appInfo2.downloadProgress = progress;
                                MyAdapter.this.notifyDataSetChanged();
                            }
                            SoftManageDialog.access$708(SoftManageDialog.this);
                        }
                    }, new PRDownloaderTool.OnDownloadConditionListener() { // from class: com.shj.setting.Dialog.SoftManageDialog.MyAdapter.5.2
                        final /* synthetic */ AppInfo val$appInfo;

                        AnonymousClass2(AppInfo appInfo222) {
                            appInfo2 = appInfo222;
                        }

                        @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                        public void onDownloadComplete(String str2) {
                            appInfo2.higherEditionsAppPath = str2;
                            appInfo2.downloadProgress = null;
                            appInfo2.autoInstall = true;
                            MyAdapter.this.notifyDataSetChanged();
                            ToastUitl.showShort(SoftManageDialog.mContext, R.string.lab_downloadsuccess);
                        }

                        @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                        public void onError(Error error) {
                            ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_error);
                        }

                        @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                        public void downloadFileExists() {
                            ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_file_Exists);
                        }
                    });
                    view2.setEnabled(false);
                    appInfo222.downloadUrl = null;
                }
            }

            /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$5$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements PRDownloaderTool.OnProgressConditionListener {
                final /* synthetic */ AppInfo val$appInfo;

                AnonymousClass1(AppInfo appInfo222) {
                    appInfo2 = appInfo222;
                }

                @Override // com.oysb.utils.PRDownloaderTool.OnProgressConditionListener
                public void onProgress(Progress progress) {
                    if (SoftManageDialog.this.count % 100 == 0) {
                        appInfo2.downloadProgress = progress;
                        MyAdapter.this.notifyDataSetChanged();
                    }
                    SoftManageDialog.access$708(SoftManageDialog.this);
                }
            }

            /* renamed from: com.shj.setting.Dialog.SoftManageDialog$MyAdapter$5$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements PRDownloaderTool.OnDownloadConditionListener {
                final /* synthetic */ AppInfo val$appInfo;

                AnonymousClass2(AppInfo appInfo222) {
                    appInfo2 = appInfo222;
                }

                @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                public void onDownloadComplete(String str2) {
                    appInfo2.higherEditionsAppPath = str2;
                    appInfo2.downloadProgress = null;
                    appInfo2.autoInstall = true;
                    MyAdapter.this.notifyDataSetChanged();
                    ToastUitl.showShort(SoftManageDialog.mContext, R.string.lab_downloadsuccess);
                }

                @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                public void onError(Error error) {
                    ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_error);
                }

                @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                public void downloadFileExists() {
                    ToastUitl.showShort(SoftManageDialog.mContext, R.string.download_file_Exists);
                }
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public Button bt_check_update;
            public Button bt_delete;
            public Button bt_download;
            public Button bt_install;
            public Button bt_uninstall;
            public SeekBar seekbar;
            public TextView tv_app_name;
            public TextView tv_app_version;
            public TextView tv_isinstalled;
            public TextView tv_progress;

            public ViewHolder() {
            }
        }
    }

    public void installApk(Context context, String str) {
        AppUpdateHelper.silentInstall(str, null);
    }

    /* loaded from: classes2.dex */
    public static class BootReceiver extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String dataString;
            String dataString2;
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") && (dataString2 = intent.getDataString()) != null) {
                Iterator it = SoftManageDialog.appInfoList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    AppInfo appInfo = (AppInfo) it.next();
                    if (dataString2.equalsIgnoreCase("package:" + appInfo.packageName)) {
                        appInfo.isInstalled = true;
                        PackageInfo packageInfo = SetUtils.getPackageInfo(context, appInfo.packageName);
                        if (packageInfo != null) {
                            appInfo.appVersion = packageInfo.versionName + "." + packageInfo.versionCode;
                        }
                    }
                }
                if (SoftManageDialog.myAdapter != null) {
                    SoftManageDialog.myAdapter.notifyDataSetChanged();
                }
                if ("package:com.xyshj.safeapp".equalsIgnoreCase(dataString2)) {
                    new TipDialog(SoftManageDialog.mContext, 60, R.string.safe_app_tip, R.string.button_ok, 0, true).show();
                } else if ("package:com.google.android.tts".equalsIgnoreCase(dataString2) || "package:com.baidu.duersdk.opensdk".equalsIgnoreCase(dataString2) || "package:com.iflytek.speechcloud".equalsIgnoreCase(dataString2)) {
                    new TipDialog(SoftManageDialog.mContext, 60, R.string.tts_app_tip, R.string.button_ok, 0, true).show();
                } else if (!ActivityHelper.isForeground(context)) {
                    ToastUitl.showShort(context, context.getString(R.string.install) + context.getString(R.string.success));
                }
            }
            if (!intent.getAction().equals("android.intent.action.PACKAGE_REMOVED") || (dataString = intent.getDataString()) == null) {
                return;
            }
            String asString = CacheHelper.getFileCache().getAsString("needReinstallPackagePath");
            if (TextUtils.isEmpty(asString)) {
                Iterator it2 = SoftManageDialog.appInfoList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    AppInfo appInfo2 = (AppInfo) it2.next();
                    if (dataString.equalsIgnoreCase("package:" + appInfo2.packageName)) {
                        appInfo2.isInstalled = false;
                        appInfo2.appVersion = "";
                        appInfo2.appVersionName = null;
                        appInfo2.appVersionCode = 0;
                        appInfo2.higherEditionsAppPath = AppUpdateHelper.get2UpdateApk(context, SoftManageDialog.appfolder, appInfo2.packageName, "0", 0, false);
                        appInfo2.isAllShowInfo = true;
                        break;
                    }
                }
                if (SoftManageDialog.myAdapter != null) {
                    SoftManageDialog.myAdapter.notifyDataSetChanged();
                }
            } else {
                CacheHelper.getFileCache().remove("needReinstallPackagePath");
                Loger.writeLog("SHJ", "安装" + asString);
                AppUpdateHelper.silentInstall(asString, null);
            }
            if (ActivityHelper.isForeground(context)) {
                return;
            }
            ToastUitl.showShort(context, context.getString(R.string.uninstall) + context.getString(R.string.success));
        }
    }
}

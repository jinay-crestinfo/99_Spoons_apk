package com.shj.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.mjdev.libaums.fs.UsbFile;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.activity.ActivityHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.OnCommandAnswerListener;
import com.shj.ShelfInfo;
import com.shj.ShelfType;
import com.shj.Shj;
import com.shj.biz.ReportManager;
import com.shj.biz.ShjManager;
import com.shj.biz.paper.PaperMoney;
import com.shj.biz.yg.YGDBHelper;
import com.shj.command.CommandManager;
import com.shj.command.Command_Up_BatchEnd;
import com.shj.command.Command_Up_BatchStart;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.commandV2.MenuCommandType;
import com.shj.device.Machine;
import com.shj.service.ShjVMCSerialClientManager;
import com.shj.setting.Dialog.DetachableShowListener;
import com.shj.setting.Dialog.FaultClearDialog;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.LoginDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.Dialog.OneKeySetDialog;
import com.shj.setting.Dialog.PicTextTipDialog;
import com.shj.setting.Dialog.SalesDialog;
import com.shj.setting.Dialog.ScrollTipDialog;
import com.shj.setting.Dialog.SelectEnabledDialog;
import com.shj.setting.Dialog.SetShelfInfoDialog;
import com.shj.setting.Dialog.ShelfTestDialog;
import com.shj.setting.Dialog.SyncBackstageDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.SearchSettingItemAdapter;
import com.shj.setting.Utils.HomeListener;
import com.shj.setting.Utils.PinYinSearch;
import com.shj.setting.Utils.ReinstallUtils;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.Utils.UsbFileUtil;
import com.shj.setting.bean.BoxLaunchTestData;
import com.shj.setting.event.BatchEndEvent;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.event.HideKeyBoradEvent;
import com.shj.setting.event.SelectMainContolFileEvent;
import com.shj.setting.event.SelectPicEvent;
import com.shj.setting.event.SelectShelfDrivingFileEvent;
import com.shj.setting.event.SetMenuEvent;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.event.ShowShelfErrorTipEvent;
import com.shj.setting.event.UpdataGoodsInfoUIEvent;
import com.shj.setting.generator.Generator;
import com.shj.setting.helper.GoodsImagesHelper;
import com.shj.setting.helper.ShjHelper;
import com.shj.setting.mainSettingItem.AbsMainSettingItem;
import com.shj.setting.mainSettingItem.AppSettingItem;
import com.shj.setting.mainSettingItem.CalibrationHeatingSettingItem;
import com.shj.setting.mainSettingItem.FaultDiagnosisSettingItem2;
import com.shj.setting.mainSettingItem.GoodwayManagementSettingItem;
import com.shj.setting.mainSettingItem.LiftSystemSettingItem2;
import com.shj.setting.mainSettingItem.LogLookSettingItem;
import com.shj.setting.mainSettingItem.MechanismParametersItem;
import com.shj.setting.mainSettingItem.PaySystemSettingItem;
import com.shj.setting.mainSettingItem.ReplenishmentSettingItem;
import com.shj.setting.mainSettingItem.SalesStatisticsSettingItem;
import com.shj.setting.mainSettingItem.SearchSettingItem;
import com.shj.setting.mainSettingItem.SystemSetupSettingItem2;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SerialDeviceData;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public class SettingActivity extends Activity implements View.OnClickListener {
    private static final int ADD_VIEW = 0;
    public static final int CHECKCOMMONDCANEXE = 8;
    public static final int DISMISS_LOADING_DIALOG = 10;
    public static final int DISSMISS_LOADING_DIALOG = 7;
    public static final int DOWNLOADGOODSPICTURE = 5;
    private static final int EXITAPP = 1;
    private static final String IS_SHOW_TIP_ITEM = "is_show_tip_item";
    public static final String KEY_SN_TIME = "checkWeixinSnTime";
    private static final int MODE_DATA = 1;
    private static final int MODE_DOBUSINESS = 0;
    private static final int MODE_SETTING = 2;
    public static final int RESTARTAPP = 4;
    public static final int SHELVESBYNET_END = 3;
    public static final int SHELVESBYNET_SETTING = 2;
    private static final String SHOW_COMMAND_ITEM = "show_command_item";
    public static final int SHOW_SHELF_LOADING_DIALOG = 9;
    public static final int UPDATESHELVES = 6;
    private static final String USER_SAVE_INFO = "user_save_info";
    private static BasicMachineInfo basicMachineInfo = null;
    public static boolean isDebug = false;
    public static final String machineid_and_merchant_number_filename = "machineid_and_merchant_number.txt";
    public static SearchSettingItem searchSettingItem;
    private static String userName;
    private List<AbsMainSettingItem> absMainSettingItemList;
    private String appType;
    private Button bt_clear_fault;
    private Button bt_full_goods;
    private Button bt_help;
    private Button bt_hide_child;
    private Button bt_hide_input;
    private Button bt_look_loginfo;
    private Button bt_one_key_open;
    private Button bt_reboot;
    private Button bt_sync_backstage;
    private Button bt_track_test;
    private long clickTime;
    private EditText et_search;
    private GridView grid_shelvs;
    private boolean isEventBusRigister;
    private boolean isSelfStartShj;
    private ImageView iv_qrcode;
    private LinearLayout ll_cabinet;
    private LinearLayout ll_content;
    private LinearLayout ll_dobusiness;
    private LinearLayout ll_layer_num;
    private LinearLayout ll_menu;
    private LoadingDialog loadingDialog;
    private ListView lv_main_item;
    private ListView lv_search;
    private ListView lv_show_info;
    private UserSettingDao mUserSettingDao;
    private String machineid;
    private MainItemAdapter mainItemAdapter;
    private MutilTextTipDialog mutilTextTipDialog;
    private RelativeLayout rl_data;
    private RelativeLayout rl_old_setting;
    private ScrollTipDialog scrollTipDialog;
    private SearchSettingItemAdapter searchSettingItemAdapter;
    private LoadingDialog shelfLoadingDialog;
    private ShelvsAdapter shelvsAdapter;
    private ShowInfoAdapter showInfoAdapter;
    private List<ShowInfoData> showInfoDataList;
    private SharedPreferences sp;
    private String symbol;
    private TextView tv_data;
    private TextView tv_dobusiness;
    private TextView tv_look_help;
    private TextView tv_machineid;
    private TextView tv_maincontrol_version;
    private TextView tv_setting;
    private List<SearchSettingItemAdapter.SearchAdapterData> searchDataList = new ArrayList();
    private int currentMainIndex = -1;
    private long clickQrCodeTime = 0;
    private int clickQrCodeCount = 0;
    private boolean isShowCommandItem = false;
    private int type = 0;
    private int mode = -1;
    private int currentSelectCabinet = 0;
    private List<Button> cabinetButtonList = new ArrayList();
    private List<Button> layerButtonList = new ArrayList();
    private Handler handler = new Handler() { // from class: com.shj.setting.SettingActivity.27
        AnonymousClass27() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    AbsMainSettingItem.MainSettingView view = ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(SettingActivity.this.currentMainIndex)).getView();
                    if (view != null) {
                        if (view.menuView != null) {
                            SettingActivity.this.ll_menu.addView(view.menuView);
                            SettingActivity.this.ll_menu.setVisibility(0);
                        }
                        SettingActivity.this.ll_content.addView(view.contentView);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams.height = 300;
                        SettingActivity.this.ll_content.addView(new View(SettingActivity.this), layoutParams);
                        SettingActivity.this.loadingDialog.dismiss();
                        return;
                    }
                    return;
                case 1:
                    SettingActivity.this.exitSetting();
                    return;
                case 2:
                    ShjHelper.setShelvesByNet_setting(SettingActivity.this);
                    return;
                case 3:
                    SettingActivity settingActivity = SettingActivity.this;
                    ShjHelper.setShelvesByNet_end(settingActivity, settingActivity.mUserSettingDao, SettingActivity.this.handler);
                    return;
                case 4:
                    SettingActivity.this.restartApp();
                    return;
                case 5:
                    SettingActivity.this.downloadgoodspicture();
                    return;
                case 6:
                    SettingActivity.this.updateShelves();
                    SettingActivity.this.getCabinetName();
                    if (SettingActivity.this.shelfLoadingDialog != null) {
                        SettingActivity.this.shelfLoadingDialog.dismiss();
                    }
                    if (SettingActivity.this.mode == 0) {
                        SettingActivity.this.showDobusinessView();
                        return;
                    }
                    return;
                case 7:
                    if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                        SettingActivity.this.loadingDialog.dismiss();
                    }
                    ToastUitl.showShort(SettingActivity.this, R.string.copy_complete);
                    return;
                case 8:
                    SettingActivity.this.showContent();
                    return;
                case 9:
                    if (SettingActivity.this.shelfLoadingDialog == null) {
                        SettingActivity.this.shelfLoadingDialog = new LoadingDialog(SettingActivity.this);
                    }
                    if (SettingActivity.this.shelfLoadingDialog.isShowing()) {
                        return;
                    }
                    SettingActivity.this.shelfLoadingDialog.show();
                    SettingActivity.this.shelfLoadingDialog.setCanceledOnTouchOutside(false);
                    return;
                case 10:
                    if (SettingActivity.this.loadingDialog != null) {
                        SettingActivity.this.loadingDialog.dismiss();
                    }
                    if (SettingActivity.this.shelvsAdapter != null) {
                        SettingActivity.this.shelvsAdapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface AllocationGoodsInfoListener {
        void haveAllocation(boolean z);
    }

    /* loaded from: classes2.dex */
    public static class BasicMachineInfo {
        public List<Integer> cabinetList;
        public List<String> cabinetNumberList;
        public List<Integer> layerNumberList;
        public HashMap<Integer, List<Integer>> layerNumberMap;
        public HashMap<Integer, List<Integer>> shelvesLayerMap;
        public HashMap<Integer, List<Integer>> shelvesMap;
    }

    /* loaded from: classes2.dex */
    public static class ShowInfoData {
        public static final int TYPE_CABINET_SHIDU = 3;
        public static final int TYPE_CABINET_WENDU = 2;
        public static final int TYPE_ENVIRONMENT_SHIDU = 1;
        public static final int TYPE_ENVIRONMENT_WENDU = 0;
        public static final int TYPE_OTHER = 4;
        public static final int TYPE_PHONE = 5;
        public String name;
        public int type;
        public String value;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFormat(-3);
        getWindow().setFlags(1024, 1024);
        UserSettingDao userSettingDao = new UserSettingDao(this);
        this.mUserSettingDao = userSettingDao;
        Generator.init(this, userSettingDao);
        this.appType = AppSetting.getAppType(this, this.mUserSettingDao);
        this.symbol = AppSetting.getMonetarySymbol(this, null);
        setDebug();
        Loger.writeLog("SET", "进入售货机设置");
        SetUtils.getAllAppSetttingValue(this);
        ShjManager.setActivityContext(this);
        setContentView(R.layout.layout_setting);
        checkVersion();
        checkAppVersion();
        checkWeixinSn();
        checkFreeSpace();
        listenerHome();
        this.sp = getSharedPreferences(USER_SAVE_INFO, 0);
        this.machineid = AppSetting.getMachineId(this, this.mUserSettingDao);
        userName = getIntent().getStringExtra(YGDBHelper.COLUM_USERNAME);
        Loger.writeLog("SET", "userName=" + userName);
        this.type = getIntent().getIntExtra(IjkMediaMeta.IJKM_KEY_TYPE, 0);
        if (getIntent().getBooleanExtra("showLoginDialog", true)) {
            showLoginDialog();
        } else {
            init();
        }
        TTSTest.init(this);
        ReportManager.init(this.appType);
        if (this.appType.equalsIgnoreCase("XY-MOOD")) {
            PaperMoney.findPapermoneyMachine(this);
        } else {
            AppSetting.saveSettingEnabled(this, 332, false, this.mUserSettingDao);
        }
        if (this.appType.equalsIgnoreCase("XY-TR")) {
            AppSetting.saveSettingEnabled(this, SettingType.LIGHTING_CONTROL_TR, true, this.mUserSettingDao);
        } else {
            AppSetting.saveSettingEnabled(this, SettingType.LIGHTING_CONTROL_TR, false, this.mUserSettingDao);
        }
    }

    private void setDebug() {
        if (isDebug) {
            SetUtils.setDebug(this, this.mUserSettingDao);
        }
    }

    private void checkVersion() {
        try {
            if (ActivityHelper.isFacePayExist(this)) {
                String str = Build.DISPLAY;
                if (str.startsWith("F4932QA")) {
                    String substring = str.substring(str.length() - 8);
                    Log.i("checkVersion", "date=" + substring);
                    if (Long.parseLong(substring) < 20191115) {
                        new TipDialog(this, 0, R.string.system_version_lower, R.string.button_ok, 0).show();
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    private void checkWeixinSn() {
        if (ActivityHelper.isWxFacePayExist(this) && AppSetting.getFacepayType(this, this.mUserSettingDao) == 1 && !SetUtils.isRegularSn()) {
            String asString = CacheHelper.getFileCache().getAsString("checkWeixinSnTime");
            long currentTimeMillis = System.currentTimeMillis();
            if (TextUtils.isEmpty(asString)) {
                CacheHelper.getFileCache().put("checkWeixinSnTime", String.valueOf(currentTimeMillis));
                showCheckWeixinSn(true, 30);
                return;
            }
            int longValue = (int) ((currentTimeMillis - Long.valueOf(asString).longValue()) / 86400000);
            if (longValue >= 30) {
                showCheckWeixinSn(false, 0);
            } else {
                showCheckWeixinSn(true, 30 - longValue);
            }
        }
    }

    private void checkFreeSpace() {
        if (AndroidSystem.getFreeSpace() < 0.5d) {
            new PicTextTipDialog(this, R.drawable.nospace_help, R.string.out_of_disk_space_tip).show();
        }
    }

    private void showCheckWeixinSn(boolean z, int i) {
        if (z) {
            TipDialog tipDialog = new TipDialog(this, 0, String.format(getString(R.string.not_regular_sn_tip), String.valueOf(i)), getString(R.string.update_face_sn), getString(R.string.button_cancel));
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.1
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    SettingActivity.this.startActivity(new Intent("android.intent.action.sn.write"));
                }
            });
            tipDialog.show();
        } else {
            TipDialog tipDialog2 = new TipDialog(this, 0, getString(R.string.not_regular_sn_not_use_tip), getString(R.string.update_face_sn), "");
            tipDialog2.setCanBack(false);
            tipDialog2.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.2
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    SettingActivity.this.startActivity(new Intent("android.intent.action.sn.write"));
                }
            });
            tipDialog2.show();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            SettingActivity.this.startActivity(new Intent("android.intent.action.sn.write"));
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            SettingActivity.this.startActivity(new Intent("android.intent.action.sn.write"));
        }
    }

    private void checkAppVersion() {
        String packageName = getPackageName();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            String str = AppUpdateHelper.get2UpdateApk(this, SDFileUtils.SDCardRoot + "xyShj/update", packageName, packageInfo.versionName, packageInfo.versionCode, false);
            if (str != null) {
                TipDialog tipDialog = new TipDialog(this, 0, R.string.install_newversion_tip, R.string.silence_install, R.string.commond_install, R.string.button_cancel, false);
                setLayoutParams(this, tipDialog);
                tipDialog.setMessage(getResources().getString(R.string.install_newversion_tip) + str);
                tipDialog.setTipDialogListenerEx(new TipDialog.TipDialogListenerEx() { // from class: com.shj.setting.SettingActivity.3
                    final /* synthetic */ String val$updateApp;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
                    public void buttonClick_03() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
                    public void timeEnd() {
                    }

                    AnonymousClass3(String str2) {
                        str = str2;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
                    public void buttonClick_01() {
                        AppUpdateHelper.silentInstall(str, null);
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
                    public void buttonClick_02() {
                        ReinstallUtils.installApk(SettingActivity.this, str);
                    }
                });
                tipDialog.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements TipDialog.TipDialogListenerEx {
        final /* synthetic */ String val$updateApp;

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_03() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void timeEnd() {
        }

        AnonymousClass3(String str2) {
            str = str2;
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_01() {
            AppUpdateHelper.silentInstall(str, null);
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_02() {
            ReinstallUtils.installApk(SettingActivity.this, str);
        }
    }

    private void setLayoutParams(Context context, Dialog dialog) {
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = getResources().getDimensionPixelSize(R.dimen.x800);
        dialog.getWindow().setAttributes(attributes);
    }

    /* renamed from: com.shj.setting.SettingActivity$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Runnable {
        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            do {
                try {
                    Thread.sleep(1000L);
                    if (Shj.isHaveShelvesInfo()) {
                        SettingActivity.this.handler.sendEmptyMessage(9);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!Shj.isResetFinished());
            SettingActivity.this.handler.sendEmptyMessage(6);
        }
    }

    public void init() {
        if (!shjIsStart()) {
            new Thread(new Runnable() { // from class: com.shj.setting.SettingActivity.4
                AnonymousClass4() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    do {
                        try {
                            Thread.sleep(1000L);
                            if (Shj.isHaveShelvesInfo()) {
                                SettingActivity.this.handler.sendEmptyMessage(9);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (!Shj.isResetFinished());
                    SettingActivity.this.handler.sendEmptyMessage(6);
                }
            }).start();
        }
        queryBasicMachineInfo();
        boolean z = this.sp.getBoolean(SHOW_COMMAND_ITEM, false);
        this.isShowCommandItem = z;
        if (z) {
            showContent();
        } else {
            checkCommondCanExe();
            this.handler.sendEmptyMessageDelayed(8, 1500L);
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements HomeListener.KeyFun {
        AnonymousClass5() {
        }

        @Override // com.shj.setting.Utils.HomeListener.KeyFun
        public void home() {
            Loger.writeLog("SET", "click home");
            SettingActivity.this.exitSetting();
        }

        @Override // com.shj.setting.Utils.HomeListener.KeyFun
        public void recent() {
            Loger.writeLog("SET", "click recet");
            SettingActivity.this.exitSetting();
        }

        @Override // com.shj.setting.Utils.HomeListener.KeyFun
        public void longHome() {
            Loger.writeLog("SET", "long click home");
            SettingActivity.this.exitSetting();
        }
    }

    private void listenerHome() {
        HomeListener homeListener = new HomeListener(this);
        homeListener.setInterface(new HomeListener.KeyFun() { // from class: com.shj.setting.SettingActivity.5
            AnonymousClass5() {
            }

            @Override // com.shj.setting.Utils.HomeListener.KeyFun
            public void home() {
                Loger.writeLog("SET", "click home");
                SettingActivity.this.exitSetting();
            }

            @Override // com.shj.setting.Utils.HomeListener.KeyFun
            public void recent() {
                Loger.writeLog("SET", "click recet");
                SettingActivity.this.exitSetting();
            }

            @Override // com.shj.setting.Utils.HomeListener.KeyFun
            public void longHome() {
                Loger.writeLog("SET", "long click home");
                SettingActivity.this.exitSetting();
            }
        });
        homeListener.startListen();
    }

    /* renamed from: com.shj.setting.SettingActivity$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements DialogInterface.OnDismissListener {
        final /* synthetic */ LoginDialog val$loginDialog;

        AnonymousClass6(LoginDialog loginDialog) {
            loginDialog = loginDialog;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            if (loginDialog.isPasswordRight()) {
                SettingActivity.this.init();
            } else {
                SettingActivity.this.onBackPressed();
            }
        }
    }

    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.SettingActivity.6
            final /* synthetic */ LoginDialog val$loginDialog;

            AnonymousClass6(LoginDialog loginDialog2) {
                loginDialog = loginDialog2;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (loginDialog.isPasswordRight()) {
                    SettingActivity.this.init();
                } else {
                    SettingActivity.this.onBackPressed();
                }
            }
        });
        loginDialog2.setButtonListener(new LoginDialog.ButtonListener() { // from class: com.shj.setting.SettingActivity.7
            AnonymousClass7() {
            }

            @Override // com.shj.setting.Dialog.LoginDialog.ButtonListener
            public void updatePassword() {
                SettingActivity settingActivity = SettingActivity.this;
                settingActivity.showResetLoginPwdView(settingActivity, false);
            }
        });
        loginDialog2.show();
    }

    /* renamed from: com.shj.setting.SettingActivity$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements LoginDialog.ButtonListener {
        AnonymousClass7() {
        }

        @Override // com.shj.setting.Dialog.LoginDialog.ButtonListener
        public void updatePassword() {
            SettingActivity settingActivity = SettingActivity.this;
            settingActivity.showResetLoginPwdView(settingActivity, false);
        }
    }

    private void checkCommondCanExe() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setLight(false, 0, 0);
        Shj.getInstance(this);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.SettingActivity.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass8() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length != 2) {
                    return;
                }
                SettingActivity.this.isShowCommandItem = true;
                SettingActivity.this.sp.edit().putBoolean(SettingActivity.SHOW_COMMAND_ITEM, true).commit();
                SettingActivity.this.handler.removeMessages(8);
                SettingActivity.this.handler.sendEmptyMessage(8);
            }
        });
    }

    /* renamed from: com.shj.setting.SettingActivity$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length != 2) {
                return;
            }
            SettingActivity.this.isShowCommandItem = true;
            SettingActivity.this.sp.edit().putBoolean(SettingActivity.SHOW_COMMAND_ITEM, true).commit();
            SettingActivity.this.handler.removeMessages(8);
            SettingActivity.this.handler.sendEmptyMessage(8);
        }
    }

    public void showContent() {
        this.currentMainIndex = -1;
        findView();
        setListener();
        this.tv_dobusiness.performClick();
        showInfo();
        setMainControlVersion();
        if (this.isEventBusRigister) {
            return;
        }
        this.isEventBusRigister = true;
        EventBus.getDefault().register(this);
    }

    private boolean shjIsStart() {
        Shj.getInstance(this);
        Shj.setShjListener(new SettingShjListener());
        if (ShjManager.isShjStarted() || ShjVMCSerialClientManager.getProcessor().isRunning()) {
            return true;
        }
        if (AppSetting.getDataSaveLocation(this, this.mUserSettingDao) == 1) {
            Shj.setStoreGoodsInfoInVMC(false);
        }
        SerialDeviceData mainControl = AppSetting.getMainControl(this, this.mUserSettingDao);
        ShjManager.initShjManager(this, SDFileUtils.SDCardRoot + "xyshj/cache.data");
        Loger.setSendBroadcast(true);
        if (mainControl != null) {
            if (mainControl.protocolVersionNumber != 0 && mainControl.protocolVersionNumber != -1) {
                Shj.setVersion(mainControl.protocolVersionNumber);
            }
            if (mainControl.serialPortAddress != null) {
                Shj.setComPath(mainControl.serialPortAddress);
            }
            if (mainControl.baudRate != 0) {
                Shj.setComBaudrate(mainControl.baudRate);
            }
        }
        Shj.initShj(this);
        ShjManager.reset();
        this.isSelfStartShj = true;
        Loger.writeLog("SET", "进入售货机设置");
        return false;
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        ShelvsAdapter shelvsAdapter;
        String str;
        if (baseEvent instanceof SettingTypeEvent) {
            SettingTypeEvent settingTypeEvent = (SettingTypeEvent) baseEvent;
            int settingType = settingTypeEvent.getSettingType();
            if (settingType == 102) {
                showMachinId();
                return;
            }
            if (settingType == 254) {
                MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this);
                this.mutilTextTipDialog = mutilTextTipDialog;
                mutilTextTipDialog.show();
                this.mutilTextTipDialog.addTextShow(getString(R.string.test_start));
                BoxLaunchTestData boxLaunchTestData = (BoxLaunchTestData) settingTypeEvent.getData();
                boxLaunchLoopTest(boxLaunchTestData.start, boxLaunchTestData.end, boxLaunchTestData.times, boxLaunchTestData.isOutGoods, 1);
                return;
            }
            if (settingType == 264) {
                downloadMaterialFile();
                return;
            }
            if (settingType == 269) {
                Iterator<ShowInfoData> it = this.showInfoDataList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ShowInfoData next = it.next();
                    if (next.type == 5) {
                        next.value = AppSetting.getKeFuPhone(this, this.mUserSettingDao);
                        break;
                    }
                }
                this.showInfoAdapter.notifyDataSetChanged();
                return;
            }
            if (settingType == 288) {
                downloadInstructionsPictures();
                return;
            }
            if (settingType != 301) {
                switch (settingType) {
                    case 127:
                        downloadgoodspicture();
                        return;
                    case 128:
                        getAllocationGoodsInfo(this, this.machineid, new AllocationGoodsInfoListener() { // from class: com.shj.setting.SettingActivity.9
                            AnonymousClass9() {
                            }

                            @Override // com.shj.setting.SettingActivity.AllocationGoodsInfoListener
                            public void haveAllocation(boolean z) {
                                if (!z) {
                                    SettingActivity settingActivity = SettingActivity.this;
                                    ShjHelper.downLoadGoodsInfo(settingActivity, true, settingActivity.mUserSettingDao);
                                } else {
                                    ToastUitl.showLong(SettingActivity.this, "查询到调拔信息，请先扫码补货，并保存");
                                }
                            }
                        });
                        return;
                    case SettingType.COMMODITY_ONE_BUTTON_SETUP /* 129 */:
                        getAllocationGoodsInfo(this, this.machineid, new AllocationGoodsInfoListener() { // from class: com.shj.setting.SettingActivity.10
                            AnonymousClass10() {
                            }

                            @Override // com.shj.setting.SettingActivity.AllocationGoodsInfoListener
                            public void haveAllocation(boolean z) {
                                if (!z) {
                                    SettingActivity settingActivity = SettingActivity.this;
                                    ShjHelper.oneButtonSetup(settingActivity, settingActivity.mUserSettingDao, SettingActivity.this.handler);
                                } else {
                                    ToastUitl.showLong(SettingActivity.this, "查询到调拔信息，请先扫码补货，并保存");
                                }
                            }
                        });
                        return;
                    default:
                        return;
                }
            }
            downLoadGoodsDetailInfo();
            return;
        }
        if (baseEvent instanceof SetMenuEvent) {
            SetMenuEvent setMenuEvent = (SetMenuEvent) baseEvent;
            String settingMenuTip = getSettingMenuTip(setMenuEvent.menuCommandType);
            if (TextUtils.isEmpty(settingMenuTip)) {
                return;
            }
            if (setMenuEvent.isSuccess) {
                str = settingMenuTip + getString(R.string.success);
            } else {
                str = settingMenuTip + getString(R.string.fail);
            }
            String string = getString(R.string.setting);
            if (!str.startsWith(string)) {
                str = string + str;
            }
            ToastUitl.showLong(this, str);
            return;
        }
        if (baseEvent instanceof GetMenuDateEvent) {
            GetMenuDateEvent getMenuDateEvent = (GetMenuDateEvent) baseEvent;
            int i = getMenuDateEvent.menuCommandType;
            if (i < 67 || i > 71) {
                return;
            }
            new SalesDialog(this, (List) getMenuDateEvent.data).show();
            return;
        }
        if (baseEvent instanceof ShowShelfErrorTipEvent) {
            showShelvesErrorTip(this.scrollTipDialog);
            return;
        }
        if (baseEvent instanceof BatchEndEvent) {
            this.handler.sendEmptyMessage(10);
            return;
        }
        if (baseEvent instanceof HideKeyBoradEvent) {
            hideInput();
        } else {
            if (!(baseEvent instanceof UpdataGoodsInfoUIEvent) || (shelvsAdapter = this.shelvsAdapter) == null) {
                return;
            }
            shelvsAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 implements AllocationGoodsInfoListener {
        AnonymousClass9() {
        }

        @Override // com.shj.setting.SettingActivity.AllocationGoodsInfoListener
        public void haveAllocation(boolean z) {
            if (!z) {
                SettingActivity settingActivity = SettingActivity.this;
                ShjHelper.downLoadGoodsInfo(settingActivity, true, settingActivity.mUserSettingDao);
            } else {
                ToastUitl.showLong(SettingActivity.this, "查询到调拔信息，请先扫码补货，并保存");
            }
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$10 */
    /* loaded from: classes2.dex */
    class AnonymousClass10 implements AllocationGoodsInfoListener {
        AnonymousClass10() {
        }

        @Override // com.shj.setting.SettingActivity.AllocationGoodsInfoListener
        public void haveAllocation(boolean z) {
            if (!z) {
                SettingActivity settingActivity = SettingActivity.this;
                ShjHelper.oneButtonSetup(settingActivity, settingActivity.mUserSettingDao, SettingActivity.this.handler);
            } else {
                ToastUitl.showLong(SettingActivity.this, "查询到调拔信息，请先扫码补货，并保存");
            }
        }
    }

    public void getAllocationGoodsInfo(Context context, String str, AllocationGoodsInfoListener allocationGoodsInfoListener) {
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
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.SettingActivity.11
                final /* synthetic */ AllocationGoodsInfoListener val$allocationGoodsInfoListener;

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass11(AllocationGoodsInfoListener allocationGoodsInfoListener2) {
                    allocationGoodsInfoListener = allocationGoodsInfoListener2;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                    AllocationGoodsInfoListener allocationGoodsInfoListener2 = allocationGoodsInfoListener;
                    if (allocationGoodsInfoListener2 != null) {
                        allocationGoodsInfoListener2.haveAllocation(false);
                    }
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                            JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                            if (allocationGoodsInfoListener != null) {
                                if (optJSONArray != null && optJSONArray.length() != 0) {
                                    allocationGoodsInfoListener.haveAllocation(true);
                                }
                                allocationGoodsInfoListener.haveAllocation(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ AllocationGoodsInfoListener val$allocationGoodsInfoListener;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass11(AllocationGoodsInfoListener allocationGoodsInfoListener2) {
            allocationGoodsInfoListener = allocationGoodsInfoListener2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            AllocationGoodsInfoListener allocationGoodsInfoListener2 = allocationGoodsInfoListener;
            if (allocationGoodsInfoListener2 != null) {
                allocationGoodsInfoListener2.haveAllocation(false);
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            try {
                JSONObject jSONObject2 = new JSONObject(str2);
                if (jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                    JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    if (allocationGoodsInfoListener != null) {
                        if (optJSONArray != null && optJSONArray.length() != 0) {
                            allocationGoodsInfoListener.haveAllocation(true);
                        }
                        allocationGoodsInfoListener.haveAllocation(false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public void boxLaunchLoopTest(int i, int i2, int i3, boolean z, int i4) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestHFJ(i, i2, z);
        Shj.getInstance(this);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.SettingActivity.12
            final /* synthetic */ int val$end;
            final /* synthetic */ boolean val$isOutGoods;
            final /* synthetic */ int val$showTimes;
            final /* synthetic */ int val$start;
            final /* synthetic */ int val$times;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
            }

            AnonymousClass12(int i42, int i32, int i5, int i22, boolean z2) {
                i4 = i42;
                i3 = i32;
                i = i5;
                i2 = i22;
                z = z2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    String[] stringArray = SettingActivity.this.getResources().getStringArray(R.array.lunch_box_test_result);
                    if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                        String str = stringArray[intFromBytes];
                        if (SettingActivity.this.mutilTextTipDialog != null) {
                            String format = String.format(SettingActivity.this.getString(R.string.test_count), "" + i4);
                            SettingActivity.this.mutilTextTipDialog.addTextShow(format + str);
                        }
                    }
                }
                int i5 = i3;
                if (i5 > 1) {
                    SettingActivity.this.boxLaunchLoopTest(i, i2, i5 - 1, z, i4 + 1);
                } else if (SettingActivity.this.mutilTextTipDialog != null) {
                    SettingActivity.this.mutilTextTipDialog.addTextShow(SettingActivity.this.getString(R.string.test_complete));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.SettingActivity$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements OnCommandAnswerListener {
        final /* synthetic */ int val$end;
        final /* synthetic */ boolean val$isOutGoods;
        final /* synthetic */ int val$showTimes;
        final /* synthetic */ int val$start;
        final /* synthetic */ int val$times;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass12(int i42, int i32, int i5, int i22, boolean z2) {
            i4 = i42;
            i3 = i32;
            i = i5;
            i2 = i22;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String[] stringArray = SettingActivity.this.getResources().getStringArray(R.array.lunch_box_test_result);
                if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                    String str = stringArray[intFromBytes];
                    if (SettingActivity.this.mutilTextTipDialog != null) {
                        String format = String.format(SettingActivity.this.getString(R.string.test_count), "" + i4);
                        SettingActivity.this.mutilTextTipDialog.addTextShow(format + str);
                    }
                }
            }
            int i5 = i3;
            if (i5 > 1) {
                SettingActivity.this.boxLaunchLoopTest(i, i2, i5 - 1, z, i4 + 1);
            } else if (SettingActivity.this.mutilTextTipDialog != null) {
                SettingActivity.this.mutilTextTipDialog.addTextShow(SettingActivity.this.getString(R.string.test_complete));
            }
        }
    }

    public void downloadgoodspicture() {
        new GoodsImagesHelper(this).downLoadGoodsImages(true, this.machineid, false);
    }

    private void downloadInstructionsPictures() {
        if (AppSetting.isSettingEnabled(this, SettingType.DOWNLOAD_INSTRUCTIONS_PICTURES, this.mUserSettingDao)) {
            new GoodsImagesHelper(this).downLoadGoodsSmImages(true, this.machineid);
        }
    }

    private void downLoadGoodsDetailInfo() {
        if (AppSetting.isSettingEnabled(this, SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES, this.mUserSettingDao)) {
            new GoodsImagesHelper(this).requestGoodsXqImages(true, this.machineid, "");
        }
    }

    private void downloadMaterialFile() {
        new GoodsImagesHelper(this).downLoadFodderFileDatas(true, AppSetting.getMachineId(this, this.mUserSettingDao));
    }

    private String getSettingMenuTip(int i) {
        if (i == MenuCommandType.TYPE_5CENTS) {
            return getString(R.string.coins_finve_cents);
        }
        if (i == MenuCommandType.TYPE_ONE_YUAN) {
            return getString(R.string.coins_one_yuan);
        }
        if (i == MenuCommandType.TYPE_SWALLOWING_MONEY_TIME) {
            return getString(R.string.swallowing_money_time);
        }
        if (i == MenuCommandType.TYPE_DISPOSAL_OF_SURPLUS_AMOUNT) {
            return getString(R.string.disposal_of_surplus_amount);
        }
        return null;
    }

    private void findView() {
        this.tv_dobusiness = (TextView) findViewById(R.id.tv_dobusiness);
        this.grid_shelvs = (GridView) findViewById(R.id.grid_shelvs);
        this.tv_data = (TextView) findViewById(R.id.tv_data);
        this.tv_setting = (TextView) findViewById(R.id.tv_setting);
        this.ll_cabinet = (LinearLayout) findViewById(R.id.ll_cabinet);
        this.ll_layer_num = (LinearLayout) findViewById(R.id.ll_layer_num);
        this.bt_clear_fault = (Button) findViewById(R.id.bt_clear_fault);
        this.bt_one_key_open = (Button) findViewById(R.id.bt_one_key_open);
        this.bt_track_test = (Button) findViewById(R.id.bt_track_test);
        this.bt_full_goods = (Button) findViewById(R.id.bt_full_goods);
        this.bt_sync_backstage = (Button) findViewById(R.id.bt_sync_backstage);
        this.rl_old_setting = (RelativeLayout) findViewById(R.id.rl_old_setting);
        this.ll_dobusiness = (LinearLayout) findViewById(R.id.ll_dobusiness);
        this.rl_data = (RelativeLayout) findViewById(R.id.rl_data);
        this.iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        this.tv_machineid = (TextView) findViewById(R.id.tv_machineid);
        this.tv_maincontrol_version = (TextView) findViewById(R.id.tv_maincontrol_version);
        this.tv_look_help = (TextView) findViewById(R.id.tv_look_help);
        this.lv_show_info = (ListView) findViewById(R.id.lv_show_info);
        this.lv_main_item = (ListView) findViewById(R.id.lv_main_item);
        this.bt_help = (Button) findViewById(R.id.bt_help);
        this.bt_hide_input = (Button) findViewById(R.id.bt_hide_input);
        this.bt_reboot = (Button) findViewById(R.id.bt_reboot);
        this.bt_hide_child = (Button) findViewById(R.id.bt_hide_child);
        this.bt_look_loginfo = (Button) findViewById(R.id.bt_look_loginfo);
        this.ll_content = (LinearLayout) findViewById(R.id.ll_content);
        this.ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.lv_search = (ListView) findViewById(R.id.lv_search);
        if (AppSetting.getHideHelpQrcode(this, this.mUserSettingDao)) {
            this.iv_qrcode.setVisibility(4);
            this.tv_look_help.setVisibility(4);
        } else {
            this.iv_qrcode.setVisibility(0);
            this.tv_look_help.setVisibility(0);
        }
    }

    private void setListener() {
        this.tv_dobusiness.setOnClickListener(this);
        this.tv_data.setOnClickListener(this);
        this.tv_setting.setOnClickListener(this);
        this.bt_clear_fault.setOnClickListener(this);
        this.bt_one_key_open.setOnClickListener(this);
        this.bt_track_test.setOnClickListener(this);
        this.bt_full_goods.setOnClickListener(this);
        this.bt_sync_backstage.setOnClickListener(this);
        this.tv_machineid.setOnClickListener(this);
        this.bt_hide_child.setOnClickListener(this);
        this.bt_reboot.setOnClickListener(this);
        this.bt_look_loginfo.setOnClickListener(this);
        this.iv_qrcode.setOnClickListener(this);
        this.bt_help.setOnClickListener(this);
        this.bt_hide_input.setOnClickListener(this);
        this.lv_main_item.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.SettingActivity.13
            AnonymousClass13() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (SettingActivity.this.currentMainIndex == -1 || Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) >= 1500) {
                    SettingActivity.this.clickTime = System.currentTimeMillis();
                    if (SettingActivity.this.currentMainIndex == i) {
                        return;
                    }
                    SettingActivity.this.currentMainIndex = i;
                    Iterator it = SettingActivity.this.absMainSettingItemList.iterator();
                    while (it.hasNext()) {
                        ((AbsMainSettingItem) it.next()).setSelect(false);
                    }
                    if (i < SettingActivity.this.absMainSettingItemList.size()) {
                        ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).setSelect(true);
                        if (SettingActivity.this.absMainSettingItemList.get(i) instanceof LogLookSettingItem) {
                            Loger.flush();
                        }
                        Loger.writeLog("SET", "选择了：" + ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).getName());
                        TTSTest.Speak(((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).getName());
                        SettingActivity.this.mainItemAdapter.notifyDataSetChanged();
                        SettingActivity.this.ll_content.removeAllViews();
                        SettingActivity.this.ll_menu.removeAllViews();
                        SettingActivity.this.ll_menu.setVisibility(8);
                        SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this);
                        DetachableShowListener wrap = DetachableShowListener.wrap(new DialogInterface.OnShowListener() { // from class: com.shj.setting.SettingActivity.13.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnShowListener
                            public void onShow(DialogInterface dialogInterface) {
                                SettingActivity.this.handler.sendEmptyMessage(0);
                            }
                        });
                        SettingActivity.this.loadingDialog.setOnShowListener(wrap);
                        wrap.clearOnDetach(SettingActivity.this.loadingDialog);
                        SettingActivity.this.loadingDialog.show();
                    }
                }
            }

            /* renamed from: com.shj.setting.SettingActivity$13$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements DialogInterface.OnShowListener {
                AnonymousClass1() {
                }

                @Override // android.content.DialogInterface.OnShowListener
                public void onShow(DialogInterface dialogInterface) {
                    SettingActivity.this.handler.sendEmptyMessage(0);
                }
            }
        });
        this.iv_qrcode.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.SettingActivity.14
            AnonymousClass14() {
            }

            /* renamed from: com.shj.setting.SettingActivity$14$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    AppSetting.saveHideHelpQrcode(SettingActivity.this, true, SettingActivity.this.mUserSettingDao);
                    SettingActivity.this.iv_qrcode.setVisibility(4);
                    SettingActivity.this.tv_look_help.setVisibility(4);
                }
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                TipDialog tipDialog = new TipDialog(SettingActivity.this, 0, R.string.hide_help_qrcode_tip, R.string.button_ok, R.string.button_cancel);
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.14.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        AppSetting.saveHideHelpQrcode(SettingActivity.this, true, SettingActivity.this.mUserSettingDao);
                        SettingActivity.this.iv_qrcode.setVisibility(4);
                        SettingActivity.this.tv_look_help.setVisibility(4);
                    }
                });
                tipDialog.show();
                return false;
            }
        });
        this.et_search.addTextChangedListener(new TextWatcher() { // from class: com.shj.setting.SettingActivity.15
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass15() {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String charSequence2 = charSequence.toString();
                SettingActivity settingActivity = SettingActivity.this;
                PinYinSearch.getSearchSettingItemData(settingActivity, charSequence2, settingActivity.searchDataList, SettingActivity.this.mUserSettingDao, false);
                if (SettingActivity.this.searchDataList.size() > 0) {
                    SettingActivity.this.lv_search.setVisibility(0);
                    SettingActivity.this.searchSettingItemAdapter.notifyDataSetChanged();
                } else {
                    SettingActivity.this.lv_search.setVisibility(8);
                }
            }
        });
        this.lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.SettingActivity.16
            AnonymousClass16() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i < SettingActivity.this.searchDataList.size()) {
                    SearchSettingItemAdapter.SearchAdapterData searchAdapterData = (SearchSettingItemAdapter.SearchAdapterData) SettingActivity.this.searchDataList.get(i);
                    Iterator it = SettingActivity.this.absMainSettingItemList.iterator();
                    while (it.hasNext()) {
                        ((AbsMainSettingItem) it.next()).setSelect(false);
                    }
                    SettingActivity.this.currentMainIndex = -1;
                    SettingActivity.this.mainItemAdapter.notifyDataSetChanged();
                    SettingActivity.this.ll_content.removeAllViews();
                    SettingActivity.this.ll_menu.removeAllViews();
                    SettingActivity.this.ll_menu.setVisibility(8);
                    SettingActivity settingActivity = SettingActivity.this;
                    SettingActivity.searchSettingItem = new SearchSettingItem(settingActivity, settingActivity.mUserSettingDao, true);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(Integer.valueOf(searchAdapterData.settingType));
                    SettingActivity.searchSettingItem.setSettingTypeList(arrayList);
                    AbsMainSettingItem.MainSettingView view2 = SettingActivity.searchSettingItem.getView();
                    if (view2 != null) {
                        if (view2.menuView != null) {
                            SettingActivity.this.ll_menu.addView(view2.menuView);
                            SettingActivity.this.ll_menu.setVisibility(0);
                        }
                        SettingActivity.this.ll_content.addView(view2.contentView);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams.height = 300;
                        SettingActivity.this.ll_content.addView(new View(SettingActivity.this), layoutParams);
                    }
                    SettingActivity.this.lv_search.setVisibility(8);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.SettingActivity$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements AdapterView.OnItemClickListener {
        AnonymousClass13() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (SettingActivity.this.currentMainIndex == -1 || Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) >= 1500) {
                SettingActivity.this.clickTime = System.currentTimeMillis();
                if (SettingActivity.this.currentMainIndex == i) {
                    return;
                }
                SettingActivity.this.currentMainIndex = i;
                Iterator it = SettingActivity.this.absMainSettingItemList.iterator();
                while (it.hasNext()) {
                    ((AbsMainSettingItem) it.next()).setSelect(false);
                }
                if (i < SettingActivity.this.absMainSettingItemList.size()) {
                    ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).setSelect(true);
                    if (SettingActivity.this.absMainSettingItemList.get(i) instanceof LogLookSettingItem) {
                        Loger.flush();
                    }
                    Loger.writeLog("SET", "选择了：" + ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).getName());
                    TTSTest.Speak(((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(i)).getName());
                    SettingActivity.this.mainItemAdapter.notifyDataSetChanged();
                    SettingActivity.this.ll_content.removeAllViews();
                    SettingActivity.this.ll_menu.removeAllViews();
                    SettingActivity.this.ll_menu.setVisibility(8);
                    SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this);
                    DetachableShowListener wrap = DetachableShowListener.wrap(new DialogInterface.OnShowListener() { // from class: com.shj.setting.SettingActivity.13.1
                        AnonymousClass1() {
                        }

                        @Override // android.content.DialogInterface.OnShowListener
                        public void onShow(DialogInterface dialogInterface) {
                            SettingActivity.this.handler.sendEmptyMessage(0);
                        }
                    });
                    SettingActivity.this.loadingDialog.setOnShowListener(wrap);
                    wrap.clearOnDetach(SettingActivity.this.loadingDialog);
                    SettingActivity.this.loadingDialog.show();
                }
            }
        }

        /* renamed from: com.shj.setting.SettingActivity$13$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnShowListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                SettingActivity.this.handler.sendEmptyMessage(0);
            }
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements View.OnLongClickListener {
        AnonymousClass14() {
        }

        /* renamed from: com.shj.setting.SettingActivity$14$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                AppSetting.saveHideHelpQrcode(SettingActivity.this, true, SettingActivity.this.mUserSettingDao);
                SettingActivity.this.iv_qrcode.setVisibility(4);
                SettingActivity.this.tv_look_help.setVisibility(4);
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            TipDialog tipDialog = new TipDialog(SettingActivity.this, 0, R.string.hide_help_qrcode_tip, R.string.button_ok, R.string.button_cancel);
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.14.1
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    AppSetting.saveHideHelpQrcode(SettingActivity.this, true, SettingActivity.this.mUserSettingDao);
                    SettingActivity.this.iv_qrcode.setVisibility(4);
                    SettingActivity.this.tv_look_help.setVisibility(4);
                }
            });
            tipDialog.show();
            return false;
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass15() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String charSequence2 = charSequence.toString();
            SettingActivity settingActivity = SettingActivity.this;
            PinYinSearch.getSearchSettingItemData(settingActivity, charSequence2, settingActivity.searchDataList, SettingActivity.this.mUserSettingDao, false);
            if (SettingActivity.this.searchDataList.size() > 0) {
                SettingActivity.this.lv_search.setVisibility(0);
                SettingActivity.this.searchSettingItemAdapter.notifyDataSetChanged();
            } else {
                SettingActivity.this.lv_search.setVisibility(8);
            }
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements AdapterView.OnItemClickListener {
        AnonymousClass16() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (i < SettingActivity.this.searchDataList.size()) {
                SearchSettingItemAdapter.SearchAdapterData searchAdapterData = (SearchSettingItemAdapter.SearchAdapterData) SettingActivity.this.searchDataList.get(i);
                Iterator it = SettingActivity.this.absMainSettingItemList.iterator();
                while (it.hasNext()) {
                    ((AbsMainSettingItem) it.next()).setSelect(false);
                }
                SettingActivity.this.currentMainIndex = -1;
                SettingActivity.this.mainItemAdapter.notifyDataSetChanged();
                SettingActivity.this.ll_content.removeAllViews();
                SettingActivity.this.ll_menu.removeAllViews();
                SettingActivity.this.ll_menu.setVisibility(8);
                SettingActivity settingActivity = SettingActivity.this;
                SettingActivity.searchSettingItem = new SearchSettingItem(settingActivity, settingActivity.mUserSettingDao, true);
                ArrayList arrayList = new ArrayList();
                arrayList.add(Integer.valueOf(searchAdapterData.settingType));
                SettingActivity.searchSettingItem.setSettingTypeList(arrayList);
                AbsMainSettingItem.MainSettingView view2 = SettingActivity.searchSettingItem.getView();
                if (view2 != null) {
                    if (view2.menuView != null) {
                        SettingActivity.this.ll_menu.addView(view2.menuView);
                        SettingActivity.this.ll_menu.setVisibility(0);
                    }
                    SettingActivity.this.ll_content.addView(view2.contentView);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams.height = 300;
                    SettingActivity.this.ll_content.addView(new View(SettingActivity.this), layoutParams);
                }
                SettingActivity.this.lv_search.setVisibility(8);
            }
        }
    }

    public void initView() {
        this.absMainSettingItemList = createMainItemData();
        MainItemAdapter mainItemAdapter = new MainItemAdapter(this, this.absMainSettingItemList, this.sp.getBoolean(IS_SHOW_TIP_ITEM, false), this.mUserSettingDao, this.isShowCommandItem);
        this.mainItemAdapter = mainItemAdapter;
        this.lv_main_item.setAdapter((ListAdapter) mainItemAdapter);
        setListViewBasedOnChildren(this.lv_main_item, true);
        this.lv_main_item.performItemClick(null, 0, 0L);
        SearchSettingItemAdapter searchSettingItemAdapter = new SearchSettingItemAdapter(this, this.searchDataList);
        this.searchSettingItemAdapter = searchSettingItemAdapter;
        this.lv_search.setAdapter((ListAdapter) searchSettingItemAdapter);
    }

    private void showInfo() {
        showMachinId();
        this.showInfoDataList = getShowInfoData();
        ShowInfoAdapter showInfoAdapter = new ShowInfoAdapter(this, this.showInfoDataList);
        this.showInfoAdapter = showInfoAdapter;
        this.lv_show_info.setAdapter((ListAdapter) showInfoAdapter);
    }

    private List<ShowInfoData> getShowInfoData() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < basicMachineInfo.cabinetNumberList.size(); i++) {
            Machine machine = Shj.getMachine(basicMachineInfo.cabinetList.get(i).intValue(), false);
            int intValue = machine != null ? machine.getTemperature().intValue() : 0;
            ShowInfoData showInfoData = new ShowInfoData();
            showInfoData.name = basicMachineInfo.cabinetNumberList.get(i) + getString(R.string.temperature) + ":";
            StringBuilder sb = new StringBuilder();
            sb.append(intValue);
            sb.append("℃");
            showInfoData.value = sb.toString();
            showInfoData.type = 2;
            arrayList.add(showInfoData);
        }
        ShowInfoData showInfoData2 = new ShowInfoData();
        showInfoData2.name = getResources().getString(R.string.after_sale_telephone) + ":";
        showInfoData2.value = AppSetting.getKeFuPhone(this, this.mUserSettingDao);
        showInfoData2.type = 5;
        arrayList.add(showInfoData2);
        return arrayList;
    }

    private void showMachinId() {
        this.tv_machineid.setText(getString(R.string.lab_machineid) + ":" + this.machineid);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view instanceof Button) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
        }
        int id = view.getId();
        if (id == R.id.bt_hide_child) {
            showOrHideChild();
            return;
        }
        if (id == R.id.bt_reboot) {
            SetUtils.forceStopWxSmile(this);
            reboot();
            return;
        }
        if (id == R.id.iv_qrcode || id == R.id.tv_machineid) {
            showSettingEnabledDialog();
            return;
        }
        if (id == R.id.bt_look_loginfo) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.oysb.floattestapp", "com.oysb.floattestapp.FxService");
                startService(intent);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUitl.showShort(this, R.string.pls_install_log_tool);
                return;
            }
        }
        if (id == R.id.bt_help) {
            ShjHelper.showHelpDialog(this);
            return;
        }
        if (id == R.id.bt_hide_input) {
            hideInput();
            return;
        }
        if (id == R.id.tv_dobusiness) {
            if (this.mode == 0) {
                return;
            }
            this.mode = 0;
            changeSettingModeState((TextView) view);
            this.rl_old_setting.setVisibility(8);
            this.ll_dobusiness.setVisibility(0);
            this.rl_data.setVisibility(8);
            showDobusinessView();
            return;
        }
        if (id == R.id.tv_data) {
            if (this.mode == 1) {
                return;
            }
            this.mode = 1;
            changeSettingModeState((TextView) view);
            this.rl_old_setting.setVisibility(8);
            this.ll_dobusiness.setVisibility(8);
            this.rl_data.setVisibility(0);
            showDataView();
            return;
        }
        if (id == R.id.tv_setting) {
            if (this.mode == 2) {
                return;
            }
            this.mode = 2;
            changeSettingModeState((TextView) view);
            this.rl_old_setting.setVisibility(0);
            this.ll_dobusiness.setVisibility(8);
            this.rl_data.setVisibility(8);
            initView();
            return;
        }
        if (id == R.id.bt_clear_fault) {
            new FaultClearDialog(this).show();
            return;
        }
        if (id == R.id.bt_one_key_open) {
            oneKeyOpen(this, this.currentSelectCabinet);
            return;
        }
        if (id == R.id.bt_full_goods) {
            new OneKeySetDialog(this, getBasicMachineInfo().cabinetList).show();
        } else if (id == R.id.bt_track_test) {
            new ShelfTestDialog(this).show();
        } else if (id == R.id.bt_sync_backstage) {
            new SyncBackstageDialog(this).show();
        }
    }

    private void oneKeyOpen(Context context, int i) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setOpenGridMachineGrid(i, -1);
        Shj.getInstance(context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.SettingActivity.17
            final /* synthetic */ Context val$context;
            final /* synthetic */ int val$jgh;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass17(Context context2, int i2) {
                context = context2;
                i = i2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (z) {
                    ToastUitl.showShort(context, R.string.open_success);
                    return;
                }
                HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesMap;
                if (hashMap != null) {
                    List<Integer> list = hashMap.get(Integer.valueOf(i));
                    Collections.sort(list);
                    if (list != null) {
                        CommandManager.appendSendCommand(new Command_Up_BatchStart());
                        Iterator<Integer> it = list.iterator();
                        while (it.hasNext()) {
                            int intValue = it.next().intValue();
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand2.TestShelf(false, 1, intValue);
                            Shj.getInstance(context);
                            Shj.postSetCommand(commandV2_Up_SetCommand2, null);
                        }
                        CommandManager.appendSendCommand(new Command_Up_BatchEnd());
                    }
                }
            }
        });
    }

    /* renamed from: com.shj.setting.SettingActivity$17 */
    /* loaded from: classes2.dex */
    public class AnonymousClass17 implements OnCommandAnswerListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$jgh;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass17(Context context2, int i2) {
            context = context2;
            i = i2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(context, R.string.open_success);
                return;
            }
            HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesMap;
            if (hashMap != null) {
                List<Integer> list = hashMap.get(Integer.valueOf(i));
                Collections.sort(list);
                if (list != null) {
                    CommandManager.appendSendCommand(new Command_Up_BatchStart());
                    Iterator<Integer> it = list.iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand2.TestShelf(false, 1, intValue);
                        Shj.getInstance(context);
                        Shj.postSetCommand(commandV2_Up_SetCommand2, null);
                    }
                    CommandManager.appendSendCommand(new Command_Up_BatchEnd());
                }
            }
        }
    }

    public void showDobusinessView() {
        boolean z;
        this.ll_cabinet.removeAllViews();
        this.cabinetButtonList.clear();
        List<Integer> list = basicMachineInfo.cabinetList;
        LinearLayout linearLayout = null;
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Loger.writeLog("SET", "cabinetList " + i2 + ":" + list.get(i2));
            if (i % 5 == 0) {
                linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                linearLayout.setOrientation(0);
                this.ll_cabinet.addView(linearLayout);
            }
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_button_item, (ViewGroup) null);
            Button button = (Button) inflate.findViewById(R.id.bt_menu);
            this.cabinetButtonList.add(button);
            int intValue = list.get(i2).intValue();
            button.setTag(Integer.valueOf(intValue));
            List<Integer> list2 = basicMachineInfo.shelvesMap.get(Integer.valueOf(intValue));
            if (list2 != null && list2.size() > 0) {
                for (int i3 = 0; i3 < list2.size(); i3++) {
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(list2.get(i3).intValue()));
                    if (shelfInfo != null && shelfInfo.getShelfType() == ShelfType.Box) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (z) {
                button.setText(basicMachineInfo.cabinetNumberList.get(i2) + getString(R.string.box));
            } else {
                button.setText(basicMachineInfo.cabinetNumberList.get(i2));
            }
            button.setTag(R.id.tag_first, Boolean.valueOf(z));
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SettingActivity.18
                AnonymousClass18() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) < 1000) {
                        return;
                    }
                    SettingActivity.this.clickTime = System.currentTimeMillis();
                    SettingActivity.this.currentSelectCabinet = ((Integer) view.getTag()).intValue();
                    Iterator it = SettingActivity.this.cabinetButtonList.iterator();
                    while (it.hasNext()) {
                        ((Button) it.next()).setSelected(false);
                    }
                    view.setSelected(true);
                    boolean booleanValue = ((Boolean) view.getTag(R.id.tag_first)).booleanValue();
                    SettingActivity settingActivity = SettingActivity.this;
                    settingActivity.showGoodsInfo(settingActivity.currentSelectCabinet, booleanValue);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = 5;
            layoutParams.topMargin = 3;
            layoutParams.bottomMargin = 3;
            linearLayout.addView(inflate, layoutParams);
            i++;
        }
        this.cabinetButtonList.get(0).performClick();
    }

    /* renamed from: com.shj.setting.SettingActivity$18 */
    /* loaded from: classes2.dex */
    public class AnonymousClass18 implements View.OnClickListener {
        AnonymousClass18() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) < 1000) {
                return;
            }
            SettingActivity.this.clickTime = System.currentTimeMillis();
            SettingActivity.this.currentSelectCabinet = ((Integer) view.getTag()).intValue();
            Iterator it = SettingActivity.this.cabinetButtonList.iterator();
            while (it.hasNext()) {
                ((Button) it.next()).setSelected(false);
            }
            view.setSelected(true);
            boolean booleanValue = ((Boolean) view.getTag(R.id.tag_first)).booleanValue();
            SettingActivity settingActivity = SettingActivity.this;
            settingActivity.showGoodsInfo(settingActivity.currentSelectCabinet, booleanValue);
        }
    }

    public void showGoodsInfo(int i, boolean z) {
        List<Integer> list = basicMachineInfo.shelvesMap.get(Integer.valueOf(i));
        if (list == null) {
            return;
        }
        if (list.size() > 80) {
            this.ll_layer_num.setVisibility(0);
            addLayerNumButton(i);
        } else {
            this.ll_layer_num.setVisibility(8);
            ShelvsAdapter shelvsAdapter = this.shelvsAdapter;
            if (shelvsAdapter == null) {
                ShelvsAdapter shelvsAdapter2 = new ShelvsAdapter(this, list);
                this.shelvsAdapter = shelvsAdapter2;
                this.grid_shelvs.setAdapter((ListAdapter) shelvsAdapter2);
            } else {
                shelvsAdapter.setShelfDatas(list);
                this.shelvsAdapter.notifyDataSetChanged();
            }
        }
        if (z) {
            this.bt_one_key_open.setVisibility(0);
        } else {
            this.bt_one_key_open.setVisibility(8);
        }
    }

    private void addLayerNumButton(int i) {
        this.layerButtonList.clear();
        this.ll_layer_num.removeAllViews();
        List<Integer> list = basicMachineInfo.layerNumberMap.get(Integer.valueOf(i));
        if (list != null) {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                View inflate = LayoutInflater.from(this).inflate(R.layout.layout_button_layer_item, (ViewGroup) null);
                Button button = (Button) inflate.findViewById(R.id.bt_menu);
                button.setText(intValue + getString(R.string.layer));
                button.setTag(Integer.valueOf(intValue));
                this.layerButtonList.add(button);
                button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SettingActivity.19
                    AnonymousClass19() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) < 500) {
                            return;
                        }
                        SettingActivity.this.clickTime = System.currentTimeMillis();
                        int intValue2 = ((Integer) view.getTag()).intValue();
                        Iterator it2 = SettingActivity.this.layerButtonList.iterator();
                        while (it2.hasNext()) {
                            ((Button) it2.next()).setBackgroundResource(R.drawable.selector_button_layer_blue_bank);
                        }
                        view.setBackgroundResource(R.drawable.selector_button_layer_blue_bank_press);
                        List<Integer> list2 = SettingActivity.basicMachineInfo.shelvesLayerMap.get(Integer.valueOf(intValue2));
                        if (list2 != null) {
                            if (SettingActivity.this.shelvsAdapter != null) {
                                SettingActivity.this.shelvsAdapter.setShelfDatas(list2);
                                SettingActivity.this.shelvsAdapter.notifyDataSetChanged();
                            } else {
                                SettingActivity settingActivity = SettingActivity.this;
                                SettingActivity settingActivity2 = SettingActivity.this;
                                settingActivity.shelvsAdapter = new ShelvsAdapter(settingActivity2, list2);
                                SettingActivity.this.grid_shelvs.setAdapter((ListAdapter) SettingActivity.this.shelvsAdapter);
                            }
                        }
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                layoutParams.leftMargin = 5;
                layoutParams.topMargin = 3;
                layoutParams.bottomMargin = 3;
                this.ll_layer_num.addView(inflate, layoutParams);
            }
            this.clickTime = 0L;
            this.layerButtonList.get(0).performClick();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$19 */
    /* loaded from: classes2.dex */
    public class AnonymousClass19 implements View.OnClickListener {
        AnonymousClass19() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - SettingActivity.this.clickTime) < 500) {
                return;
            }
            SettingActivity.this.clickTime = System.currentTimeMillis();
            int intValue2 = ((Integer) view.getTag()).intValue();
            Iterator it2 = SettingActivity.this.layerButtonList.iterator();
            while (it2.hasNext()) {
                ((Button) it2.next()).setBackgroundResource(R.drawable.selector_button_layer_blue_bank);
            }
            view.setBackgroundResource(R.drawable.selector_button_layer_blue_bank_press);
            List<Integer> list2 = SettingActivity.basicMachineInfo.shelvesLayerMap.get(Integer.valueOf(intValue2));
            if (list2 != null) {
                if (SettingActivity.this.shelvsAdapter != null) {
                    SettingActivity.this.shelvsAdapter.setShelfDatas(list2);
                    SettingActivity.this.shelvsAdapter.notifyDataSetChanged();
                } else {
                    SettingActivity settingActivity = SettingActivity.this;
                    SettingActivity settingActivity2 = SettingActivity.this;
                    settingActivity.shelvsAdapter = new ShelvsAdapter(settingActivity2, list2);
                    SettingActivity.this.grid_shelvs.setAdapter((ListAdapter) SettingActivity.this.shelvsAdapter);
                }
            }
        }
    }

    private void showDataView() {
        AbsMainSettingItem.MainSettingView view = new SalesStatisticsSettingItem(this, this.mUserSettingDao, this.isShowCommandItem).getView();
        if (view != null) {
            if (view.menuView != null) {
                this.ll_menu.addView(view.menuView);
                this.ll_menu.setVisibility(0);
            }
            this.rl_data.removeAllViews();
            this.rl_data.addView(view.contentView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.height = 300;
            this.ll_content.addView(new View(this), layoutParams);
        }
    }

    private void changeSettingModeState(TextView textView) {
        this.tv_dobusiness.setTextColor(getResources().getColor(R.color.setting_black));
        this.tv_data.setTextColor(getResources().getColor(R.color.setting_black));
        this.tv_setting.setTextColor(getResources().getColor(R.color.setting_black));
        textView.setTextColor(getResources().getColor(R.color.setting_mode_select));
        int i = this.mode;
        if (i == 0) {
            setDrawableTop(this.tv_dobusiness, R.drawable.dobusiness_press);
            setDrawableTop(this.tv_data, R.drawable.data_cope);
            setDrawableTop(this.tv_setting, R.drawable.setting_all);
        } else if (i == 1) {
            setDrawableTop(this.tv_dobusiness, R.drawable.dobusiness);
            setDrawableTop(this.tv_data, R.drawable.data_cope_press);
            setDrawableTop(this.tv_setting, R.drawable.setting_all);
        } else if (i == 2) {
            setDrawableTop(this.tv_dobusiness, R.drawable.dobusiness);
            setDrawableTop(this.tv_data, R.drawable.data_cope);
            setDrawableTop(this.tv_setting, R.drawable.setting_all_press);
        }
    }

    private void setDrawableTop(TextView textView, int i) {
        Drawable drawable = getResources().getDrawable(i);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.x48);
        drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    private void showSettingEnabledDialog() {
        if (this.mode == 2) {
            if (Math.abs(System.currentTimeMillis() - this.clickQrCodeTime) > 500) {
                this.clickQrCodeCount = 0;
            } else {
                this.clickQrCodeCount++;
            }
            this.clickQrCodeTime = System.currentTimeMillis();
            if (this.clickQrCodeCount == 5) {
                SelectEnabledDialog selectEnabledDialog = new SelectEnabledDialog(this, this.mUserSettingDao);
                selectEnabledDialog.setSelectEnabledDialogListener(new SelectEnabledDialog.SelectEnabledDialogListener() { // from class: com.shj.setting.SettingActivity.20
                    AnonymousClass20() {
                    }

                    @Override // com.shj.setting.Dialog.SelectEnabledDialog.SelectEnabledDialogListener
                    public void buttonClick_OK() {
                        SettingActivity.this.currentMainIndex = -1;
                        SettingActivity.this.initView();
                    }
                });
                selectEnabledDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.SettingActivity.21
                    AnonymousClass21() {
                    }

                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialogInterface) {
                        ((InputMethodManager) SettingActivity.this.getSystemService("input_method")).toggleSoftInput(0, 2);
                    }
                });
                selectEnabledDialog.show();
            }
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$20 */
    /* loaded from: classes2.dex */
    public class AnonymousClass20 implements SelectEnabledDialog.SelectEnabledDialogListener {
        AnonymousClass20() {
        }

        @Override // com.shj.setting.Dialog.SelectEnabledDialog.SelectEnabledDialogListener
        public void buttonClick_OK() {
            SettingActivity.this.currentMainIndex = -1;
            SettingActivity.this.initView();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$21 */
    /* loaded from: classes2.dex */
    public class AnonymousClass21 implements DialogInterface.OnShowListener {
        AnonymousClass21() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            ((InputMethodManager) SettingActivity.this.getSystemService("input_method")).toggleSoftInput(0, 2);
        }
    }

    public void reboot() {
        if (!ShjManager.isShjStarted()) {
            Loger.writeLog("SET", "退出售货机设置");
            Loger.writeLog("SET", "退出时打印售货机信息 --开始");
            SetUtils.writeShelfInfoLog();
            Loger.writeLog("SET", "退出时打印售货机信息 --结束");
            finish();
            if (this.isSelfStartShj) {
                Loger.writeLog("APP", "app 重启" + Log.getStackTraceString(new Throwable()));
                Loger.flush();
                sendFinishSelfBroadCast();
                System.exit(0);
                return;
            }
            return;
        }
        String asString = CacheHelper.getFileCache().getAsString("passWord");
        if (asString == null || asString.length() < 6 || asString.equals("888888") || asString.equals("123456") || asString.equals("654321")) {
            TipDialog tipDialog = new TipDialog(this, 0, getString(R.string.lab_pleast2eidtpwd), getString(R.string.button_modifynow), "");
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.22
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass22() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    SettingActivity settingActivity = SettingActivity.this;
                    settingActivity.showResetLoginPwdView(settingActivity, true);
                }
            });
            tipDialog.show();
        } else {
            TipDialog tipDialog2 = new TipDialog(this, 0, getString(R.string.lab_restartaftersetting), getString(R.string.button_restartnow), getString(R.string.button_cancel));
            tipDialog2.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.SettingActivity.23
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass23() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    SettingActivity.this.restartApp();
                }
            });
            tipDialog2.show();
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$22 */
    /* loaded from: classes2.dex */
    public class AnonymousClass22 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass22() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            SettingActivity settingActivity = SettingActivity.this;
            settingActivity.showResetLoginPwdView(settingActivity, true);
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$23 */
    /* loaded from: classes2.dex */
    public class AnonymousClass23 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass23() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            SettingActivity.this.restartApp();
        }
    }

    public void restartApp() {
        try {
            sendBroadcast(new Intent("XY_APP_SERVICE_RESTART"));
            Loger.writeLog("APP", "XY_APP_SERVICE_RESTART-----start");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Loger.writeLog("SET", "退出售货机设置");
        SetUtils.getAllAppSetttingValue(this);
        Loger.writeLog("SET", "退出时打印售货机信息 --开始");
        SetUtils.writeShelfInfoLog();
        Loger.writeLog("SET", "退出时打印售货机信息 --结束");
        finish();
        Loger.writeLog("APP", "app 重启" + Log.getStackTraceString(new Throwable()));
        sendFinishSelfBroadCast();
        Loger.flush();
        System.exit(0);
    }

    public void exitSetting() {
        Loger.writeLog("SET", "退出售货机设置");
        SetUtils.getAllAppSetttingValue(this);
        Loger.writeLog("SET", "退出时打印售货机信息 --开始");
        SetUtils.writeShelfInfoLog();
        Loger.writeLog("SET", "退出时打印售货机信息 --结束");
        finish();
        Loger.writeLog("APP", "app 重启" + Log.getStackTraceString(new Throwable()));
        sendFinishSelfBroadCast();
        Loger.flush();
        System.exit(0);
    }

    public void showResetLoginPwdView(Context context, boolean z) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.lab_modifypwd));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.leftMargin = 40;
        layoutParams.rightMargin = 40;
        EditText editText = new EditText(context);
        editText.setHint(getString(R.string.lab_please2enterpwd));
        editText.setLayoutParams(layoutParams);
        editText.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        linearLayout.addView(editText);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = 20;
        layoutParams2.leftMargin = 40;
        layoutParams2.rightMargin = 40;
        EditText editText2 = new EditText(context);
        editText2.setHint(getString(R.string.lab_please2enternewpwd));
        editText2.setLayoutParams(layoutParams2);
        editText2.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        linearLayout.addView(editText2);
        EditText editText3 = new EditText(context);
        editText3.setHint(getString(R.string.lab_please2confirmpwd));
        editText3.setLayoutParams(layoutParams2);
        editText3.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        linearLayout.addView(editText3);
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, 44));
        textView.setTextColor(SupportMenu.CATEGORY_MASK);
        textView.setLayoutParams(layoutParams2);
        linearLayout.addView(textView);
        AnonymousClass24 anonymousClass24 = new TextWatcher() { // from class: com.shj.setting.SettingActivity.24
            final /* synthetic */ TextView val$info;

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass24(TextView textView2) {
                textView = textView2;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                textView.setText("");
            }
        };
        editText.addTextChangedListener(anonymousClass24);
        editText2.addTextChangedListener(anonymousClass24);
        editText3.addTextChangedListener(anonymousClass24);
        builder.setView(linearLayout);
        builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() { // from class: com.shj.setting.SettingActivity.25
            AnonymousClass25() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        });
        builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() { // from class: com.shj.setting.SettingActivity.26
            final /* synthetic */ Context val$context;
            final /* synthetic */ EditText val$editText;
            final /* synthetic */ EditText val$editText1;
            final /* synthetic */ EditText val$editText2;
            final /* synthetic */ TextView val$info;
            final /* synthetic */ boolean val$needReboot;

            AnonymousClass26(EditText editText4, EditText editText22, EditText editText32, TextView textView2, Context context2, boolean z2) {
                editText = editText4;
                editText2 = editText22;
                editText3 = editText32;
                textView = textView2;
                context = context2;
                z = z2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean z2;
                try {
                    String asString = CacheHelper.getFileCache().getAsString("passWord");
                    if (asString == null || asString.length() == 0) {
                        asString = "888888";
                    }
                    String obj = editText.getText().toString();
                    String obj2 = editText2.getText().toString();
                    String obj3 = editText3.getText().toString();
                    if (asString.equals(obj)) {
                        z2 = true;
                    } else {
                        textView.setText(SettingActivity.this.getString(R.string.lab_please2enterpwd));
                        z2 = false;
                    }
                    if (obj2.equals("888888") || obj2.equals("123456") || obj2.equals("654321")) {
                        textView.setText(SettingActivity.this.getString(R.string.lab_pwdtoosimple));
                        z2 = false;
                    }
                    if (!obj2.equals(obj3)) {
                        textView.setText(SettingActivity.this.getString(R.string.lab_pwdunconfirmed));
                        z2 = false;
                    }
                    if (!z2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, false);
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    }
                    CacheHelper.getFileCache().put("passWord", obj2);
                    try {
                        Field declaredField2 = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField2.setAccessible(true);
                        declaredField2.set(dialogInterface, true);
                    } catch (Exception unused2) {
                    }
                    ToastUitl.showLong(context, R.string.lab_pwdreseted);
                    if (z) {
                        SettingActivity.this.reboot();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.show();
    }

    /* renamed from: com.shj.setting.SettingActivity$24 */
    /* loaded from: classes2.dex */
    public class AnonymousClass24 implements TextWatcher {
        final /* synthetic */ TextView val$info;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass24(TextView textView2) {
            textView = textView2;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            textView.setText("");
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$25 */
    /* loaded from: classes2.dex */
    public class AnonymousClass25 implements DialogInterface.OnClickListener {
        AnonymousClass25() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            try {
                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                declaredField.setAccessible(true);
                declaredField.set(dialogInterface, true);
            } catch (Exception unused) {
            }
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$26 */
    /* loaded from: classes2.dex */
    public class AnonymousClass26 implements DialogInterface.OnClickListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ EditText val$editText1;
        final /* synthetic */ EditText val$editText2;
        final /* synthetic */ TextView val$info;
        final /* synthetic */ boolean val$needReboot;

        AnonymousClass26(EditText editText4, EditText editText22, EditText editText32, TextView textView2, Context context2, boolean z2) {
            editText = editText4;
            editText2 = editText22;
            editText3 = editText32;
            textView = textView2;
            context = context2;
            z = z2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            boolean z2;
            try {
                String asString = CacheHelper.getFileCache().getAsString("passWord");
                if (asString == null || asString.length() == 0) {
                    asString = "888888";
                }
                String obj = editText.getText().toString();
                String obj2 = editText2.getText().toString();
                String obj3 = editText3.getText().toString();
                if (asString.equals(obj)) {
                    z2 = true;
                } else {
                    textView.setText(SettingActivity.this.getString(R.string.lab_please2enterpwd));
                    z2 = false;
                }
                if (obj2.equals("888888") || obj2.equals("123456") || obj2.equals("654321")) {
                    textView.setText(SettingActivity.this.getString(R.string.lab_pwdtoosimple));
                    z2 = false;
                }
                if (!obj2.equals(obj3)) {
                    textView.setText(SettingActivity.this.getString(R.string.lab_pwdunconfirmed));
                    z2 = false;
                }
                if (!z2) {
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, false);
                        return;
                    } catch (Exception unused) {
                        return;
                    }
                }
                CacheHelper.getFileCache().put("passWord", obj2);
                try {
                    Field declaredField2 = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField2.setAccessible(true);
                    declaredField2.set(dialogInterface, true);
                } catch (Exception unused2) {
                }
                ToastUitl.showLong(context, R.string.lab_pwdreseted);
                if (z) {
                    SettingActivity.this.reboot();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showOrHideChild() {
        boolean z;
        List<AbsMainSettingItem> list = this.absMainSettingItemList;
        if (list == null || list.size() <= 0) {
            return;
        }
        if (this.mainItemAdapter.isShowChild()) {
            z = false;
            this.bt_hide_child.setText(R.string.show_child);
        } else {
            this.bt_hide_child.setText(R.string.hide_child);
            z = true;
        }
        this.sp.edit().putBoolean(IS_SHOW_TIP_ITEM, z).commit();
        this.mainItemAdapter.setShowChild(z);
        this.mainItemAdapter.notifyDataSetChanged();
        this.lv_main_item.invalidate();
        setListViewBasedOnChildren(this.lv_main_item, true);
    }

    public static void setListViewBasedOnChildren(ListView listView, boolean z) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < adapter.getCount(); i3++) {
            View view = adapter.getView(i3, null, listView);
            view.measure(0, 0);
            i += view.getMeasuredHeight();
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth > i2) {
                i2 = measuredWidth;
            }
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = i + (listView.getDividerHeight() * (adapter.getCount() - 1));
        if (z) {
            layoutParams.width = i2;
        }
        listView.setLayoutParams(layoutParams);
    }

    public List<AbsMainSettingItem> createMainItemData() {
        ArrayList arrayList = new ArrayList();
        int i = this.type;
        if (i == 0) {
            ReplenishmentSettingItem replenishmentSettingItem = new ReplenishmentSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, replenishmentSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(replenishmentSettingItem);
            }
            GoodwayManagementSettingItem goodwayManagementSettingItem = new GoodwayManagementSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, goodwayManagementSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(goodwayManagementSettingItem);
            }
            SystemSetupSettingItem2 systemSetupSettingItem2 = new SystemSetupSettingItem2(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, systemSetupSettingItem2, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(systemSetupSettingItem2);
            }
            PaySystemSettingItem paySystemSettingItem = new PaySystemSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, paySystemSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(paySystemSettingItem);
            }
            CalibrationHeatingSettingItem calibrationHeatingSettingItem = new CalibrationHeatingSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, calibrationHeatingSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(calibrationHeatingSettingItem);
            }
            FaultDiagnosisSettingItem2 faultDiagnosisSettingItem2 = new FaultDiagnosisSettingItem2(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, faultDiagnosisSettingItem2, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(faultDiagnosisSettingItem2);
            }
            LiftSystemSettingItem2 liftSystemSettingItem2 = new LiftSystemSettingItem2(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, liftSystemSettingItem2, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(liftSystemSettingItem2);
            }
            AppSettingItem appSettingItem = new AppSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, appSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(appSettingItem);
            }
            if ("XY-XJP".equalsIgnoreCase(this.appType)) {
                MechanismParametersItem mechanismParametersItem = new MechanismParametersItem(this, this.mUserSettingDao, this.isShowCommandItem);
                if (SetUtils.isNeeShowMainSettingItem(this, mechanismParametersItem, this.isShowCommandItem, this.mUserSettingDao)) {
                    arrayList.add(mechanismParametersItem);
                }
            }
            if (CommonTool.getLanguage(this).equalsIgnoreCase("zh")) {
                LogLookSettingItem logLookSettingItem = new LogLookSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
                if (SetUtils.isNeeShowMainSettingItem(this, logLookSettingItem, this.isShowCommandItem, this.mUserSettingDao)) {
                    arrayList.add(logLookSettingItem);
                }
            }
        } else if (i == 1) {
            ReplenishmentSettingItem replenishmentSettingItem2 = new ReplenishmentSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, replenishmentSettingItem2, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(replenishmentSettingItem2);
            }
            GoodwayManagementSettingItem goodwayManagementSettingItem2 = new GoodwayManagementSettingItem(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, goodwayManagementSettingItem2, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(goodwayManagementSettingItem2);
            }
            SystemSetupSettingItem2 systemSetupSettingItem22 = new SystemSetupSettingItem2(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, systemSetupSettingItem22, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(systemSetupSettingItem22);
            }
            FaultDiagnosisSettingItem2 faultDiagnosisSettingItem22 = new FaultDiagnosisSettingItem2(this, this.mUserSettingDao, this.isShowCommandItem);
            if (SetUtils.isNeeShowMainSettingItem(this, faultDiagnosisSettingItem22, this.isShowCommandItem, this.mUserSettingDao)) {
                arrayList.add(faultDiagnosisSettingItem22);
            }
        }
        return arrayList;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        String path;
        super.onActivityResult(i, i2, intent);
        if (i2 != -1 || i == 255) {
            return;
        }
        Uri data = intent.getData();
        if ("file".equalsIgnoreCase(data.getScheme())) {
            path = data.getPath();
        } else {
            path = getPath(this, data);
        }
        Loger.writeLog("SHJ", "copy path = " + path);
        if (i == 108 || i == 348) {
            EventBus.getDefault().post(new SelectPicEvent(path));
            return;
        }
        if (i == 311) {
            EventBus.getDefault().post(new SelectMainContolFileEvent(path));
            return;
        }
        if (i == 312) {
            EventBus.getDefault().post(new SelectShelfDrivingFileEvent(path));
            return;
        }
        if (i == 250) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null && loadingDialog.isShowing()) {
                this.loadingDialog.dismiss();
            }
            LoadingDialog loadingDialog2 = new LoadingDialog(this, R.string.on_copying);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            UsbFileUtil.copeAdFile(this.handler, path, true);
            return;
        }
        if (i == 251) {
            LoadingDialog loadingDialog3 = this.loadingDialog;
            if (loadingDialog3 != null && loadingDialog3.isShowing()) {
                this.loadingDialog.dismiss();
            }
            LoadingDialog loadingDialog4 = new LoadingDialog(this, R.string.on_copying);
            this.loadingDialog = loadingDialog4;
            loadingDialog4.show();
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            UsbFileUtil.copeAdFile(this.handler, path, false);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor query = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (query == null || !query.moveToFirst()) {
            return null;
        }
        String string = query.getString(query.getColumnIndexOrThrow("_data"));
        query.close();
        return string;
    }

    public String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                String str = split[0];
                if ("primary".equalsIgnoreCase(str)) {
                    return Environment.getExternalStorageDirectory() + UsbFile.separator + split[1];
                }
                return "/mnt/media_rw/" + str + UsbFile.separator + split[1];
            }
            if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            }
            if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String str2 = split2[0];
                if ("image".equals(str2)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str2)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str2)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor;
        Cursor cursor2 = null;
        String str2 = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data", "_data", "mime_type", "_data"}, null, null, null);
        } catch (SecurityException unused) {
            cursor = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            try {
                cursor.moveToFirst();
                String string = cursor.getString(0);
                if (cursor == null) {
                    return string;
                }
                cursor.close();
                return string;
            } catch (Throwable th2) {
                th = th2;
                cursor2 = cursor;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
        } catch (SecurityException unused2) {
            if (uri.toString().contains("/storage/emulated/0")) {
                str2 = "/storage/emulated/0" + uri.toString().split("/storage/emulated/0")[1];
            } else if (uri.toString().contains("/mnt/usb_storage")) {
                str2 = "/mnt/usb_storage" + uri.toString().split("mnt/usb_storage")[1];
            }
            if (cursor != null) {
                cursor.close();
            }
            return str2;
        }
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        getResources().getDisplayMetrics();
        SetUtils.execShellCmd("wm overscan 0,0,0,0");
        SetUtils.execShellCmd("settings put system show_navigation_bar true");
        Intent intent = new Intent("com.mobilepower.terminal.message");
        intent.putExtra(IjkMediaMeta.IJKM_KEY_TYPE, 17);
        sendBroadcast(new Intent("android.intent.action.showbar"));
        sendBroadcast(intent);
        SetUtils.checkNetConnect(this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        UserSettingDao userSettingDao = this.mUserSettingDao;
        if (userSettingDao != null) {
            userSettingDao.close();
        }
        EventBus.getDefault().unregister(this);
    }

    /* loaded from: classes2.dex */
    public static class ShowInfoAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<ShowInfoData> showInfoDataList;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public ShowInfoAdapter(Context context, List<ShowInfoData> list) {
            this.context = context;
            this.showInfoDataList = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<ShowInfoData> list = this.showInfoDataList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = this.inflater.inflate(R.layout.layout_showinfo_item, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_name);
            TextView textView2 = (TextView) inflate.findViewById(R.id.tv_value);
            this.showInfoDataList.size();
            ShowInfoData showInfoData = this.showInfoDataList.get(i);
            textView.setText(showInfoData.name);
            textView2.setText(showInfoData.value);
            return inflate;
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$27 */
    /* loaded from: classes2.dex */
    class AnonymousClass27 extends Handler {
        AnonymousClass27() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    AbsMainSettingItem.MainSettingView view = ((AbsMainSettingItem) SettingActivity.this.absMainSettingItemList.get(SettingActivity.this.currentMainIndex)).getView();
                    if (view != null) {
                        if (view.menuView != null) {
                            SettingActivity.this.ll_menu.addView(view.menuView);
                            SettingActivity.this.ll_menu.setVisibility(0);
                        }
                        SettingActivity.this.ll_content.addView(view.contentView);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams.height = 300;
                        SettingActivity.this.ll_content.addView(new View(SettingActivity.this), layoutParams);
                        SettingActivity.this.loadingDialog.dismiss();
                        return;
                    }
                    return;
                case 1:
                    SettingActivity.this.exitSetting();
                    return;
                case 2:
                    ShjHelper.setShelvesByNet_setting(SettingActivity.this);
                    return;
                case 3:
                    SettingActivity settingActivity = SettingActivity.this;
                    ShjHelper.setShelvesByNet_end(settingActivity, settingActivity.mUserSettingDao, SettingActivity.this.handler);
                    return;
                case 4:
                    SettingActivity.this.restartApp();
                    return;
                case 5:
                    SettingActivity.this.downloadgoodspicture();
                    return;
                case 6:
                    SettingActivity.this.updateShelves();
                    SettingActivity.this.getCabinetName();
                    if (SettingActivity.this.shelfLoadingDialog != null) {
                        SettingActivity.this.shelfLoadingDialog.dismiss();
                    }
                    if (SettingActivity.this.mode == 0) {
                        SettingActivity.this.showDobusinessView();
                        return;
                    }
                    return;
                case 7:
                    if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                        SettingActivity.this.loadingDialog.dismiss();
                    }
                    ToastUitl.showShort(SettingActivity.this, R.string.copy_complete);
                    return;
                case 8:
                    SettingActivity.this.showContent();
                    return;
                case 9:
                    if (SettingActivity.this.shelfLoadingDialog == null) {
                        SettingActivity.this.shelfLoadingDialog = new LoadingDialog(SettingActivity.this);
                    }
                    if (SettingActivity.this.shelfLoadingDialog.isShowing()) {
                        return;
                    }
                    SettingActivity.this.shelfLoadingDialog.show();
                    SettingActivity.this.shelfLoadingDialog.setCanceledOnTouchOutside(false);
                    return;
                case 10:
                    if (SettingActivity.this.loadingDialog != null) {
                        SettingActivity.this.loadingDialog.dismiss();
                    }
                    if (SettingActivity.this.shelvsAdapter != null) {
                        SettingActivity.this.shelvsAdapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void hideInput() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
    }

    private void queryBasicMachineInfo() {
        Loger.writeLog("SET", "queryBasicMachineInfo");
        basicMachineInfo = new BasicMachineInfo();
        updateShelves();
        getCabinetName();
    }

    public void getCabinetName() {
        basicMachineInfo.cabinetNumberList = new ArrayList();
        basicMachineInfo.cabinetNumberList.add(getString(R.string.host));
        String string = getString(R.string.auxiliary_engine);
        for (int i = 1; i < basicMachineInfo.cabinetList.size(); i++) {
            basicMachineInfo.cabinetNumberList.add(string + basicMachineInfo.cabinetList.get(i));
        }
    }

    private static void queryBasicMachineInfoEx() {
        basicMachineInfo = new BasicMachineInfo();
        updateShelvesEx();
    }

    private static void updateShelvesEx() {
        crateBasicMachineInfo(Shj.getShelves());
    }

    public void updateShelves() {
        Loger.writeLog("SET", "进入时打印售货机信息 --开始");
        List<Integer> shelves = Shj.getShelves();
        Collections.sort(shelves);
        Iterator<Integer> it = shelves.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (shelfInfo != null) {
                Loger.writeLog("SET", "货道=" + intValue + "价格=" + String.valueOf(shelfInfo.getPrice().intValue() / 100.0f) + "库存=" + shelfInfo.getGoodsCount() + "编码=" + shelfInfo.getGoodsCode());
            } else {
                Loger.writeLog("SET", "货道=" + intValue + "shelfInfo=null");
            }
        }
        Loger.writeLog("SET", "进入时打印售货机信息 --结束");
        crateBasicMachineInfo(shelves);
        setMainControlVersion();
    }

    private static void crateBasicMachineInfo(List<Integer> list) {
        basicMachineInfo.cabinetList = new ArrayList();
        basicMachineInfo.shelvesMap = new HashMap<>();
        basicMachineInfo.layerNumberMap = new HashMap<>();
        basicMachineInfo.layerNumberList = new ArrayList();
        basicMachineInfo.shelvesLayerMap = new HashMap<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int intValue = list.get(i).intValue();
            int jghByShelf = Shj.getJghByShelf(intValue);
            if (!basicMachineInfo.cabinetList.contains(Integer.valueOf(jghByShelf))) {
                basicMachineInfo.cabinetList.add(Integer.valueOf(jghByShelf));
            }
            List<Integer> list2 = basicMachineInfo.shelvesMap.get(Integer.valueOf(jghByShelf));
            if (list2 == null) {
                list2 = new ArrayList<>();
                basicMachineInfo.shelvesMap.put(Integer.valueOf(jghByShelf), list2);
            }
            list2.add(Integer.valueOf(intValue));
            int layerByShelf = Shj.getLayerByShelf(intValue);
            List<Integer> list3 = basicMachineInfo.layerNumberMap.get(Integer.valueOf(jghByShelf));
            if (list3 == null) {
                list3 = new ArrayList<>();
                basicMachineInfo.layerNumberMap.put(Integer.valueOf(jghByShelf), list3);
            }
            if (!list3.contains(Integer.valueOf(layerByShelf))) {
                list3.add(Integer.valueOf(layerByShelf));
            }
            List<Integer> list4 = basicMachineInfo.shelvesLayerMap.get(Integer.valueOf(layerByShelf));
            if (list4 == null) {
                list4 = new ArrayList<>();
                basicMachineInfo.shelvesLayerMap.put(Integer.valueOf(layerByShelf), list4);
            }
            list4.add(Integer.valueOf(intValue));
            if (!basicMachineInfo.layerNumberList.contains(Integer.valueOf(layerByShelf))) {
                basicMachineInfo.layerNumberList.add(Integer.valueOf(layerByShelf));
            }
        }
        if (!basicMachineInfo.cabinetList.contains(0)) {
            basicMachineInfo.cabinetList.add(0);
        }
        Collections.sort(basicMachineInfo.cabinetList);
    }

    private void setMainControlVersion() {
        if (this.tv_maincontrol_version != null) {
            String machineBoardVersion = Shj.getMachineBoardVersion();
            if (TextUtils.isEmpty(machineBoardVersion)) {
                machineBoardVersion = "0203";
            }
            this.tv_maincontrol_version.setText(getString(R.string.main_control_equipment) + getString(R.string.version_number) + ":" + machineBoardVersion);
        }
    }

    public static BasicMachineInfo getBasicMachineInfo() {
        if (basicMachineInfo == null) {
            queryBasicMachineInfoEx();
        }
        return basicMachineInfo;
    }

    public static String getUserName() {
        return userName;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        exitSetting();
    }

    private void sendFinishSelfBroadCast() {
        sendBroadcast(new Intent("xy_setting_will_finish"));
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ConstructorVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r12v12 ??, still in use, count: 2, list:
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) from 0x012c: INVOKE 
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog)
          (r0v37 ?? I:com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener)
         VIRTUAL call: com.shj.setting.Dialog.ScrollTipDialog.setTipDialogListener(com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener):void A[MD:(com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener):void (m)]
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) from 0x012f: INVOKE (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) VIRTUAL call: com.shj.setting.Dialog.ScrollTipDialog.show():void A[MD:():void (c)] (LINE:2490)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
        	at jadx.core.utils.InsnRemover.perform(InsnRemover.java:72)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:54)
        	at jadx.core.dex.visitors.ConstructorVisitor.visit(ConstructorVisitor.java:34)
        */
    private void showShelvesErrorTip(
    /*  JADX ERROR: JadxRuntimeException in pass: ConstructorVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r12v12 ??, still in use, count: 2, list:
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) from 0x012c: INVOKE 
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog)
          (r0v37 ?? I:com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener)
         VIRTUAL call: com.shj.setting.Dialog.ScrollTipDialog.setTipDialogListener(com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener):void A[MD:(com.shj.setting.Dialog.ScrollTipDialog$TipDialogListener):void (m)]
          (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) from 0x012f: INVOKE (r12v12 ?? I:com.shj.setting.Dialog.ScrollTipDialog) VIRTUAL call: com.shj.setting.Dialog.ScrollTipDialog.show():void A[MD:():void (c)] (LINE:2490)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
        	at jadx.core.utils.InsnRemover.perform(InsnRemover.java:72)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:54)
        */
    /*  JADX ERROR: Method generation error
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r25v0 ??
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:237)
        	at jadx.core.codegen.MethodGen.addMethodArguments(MethodGen.java:223)
        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:168)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:401)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:261)
        */

    /* renamed from: com.shj.setting.SettingActivity$28 */
    /* loaded from: classes2.dex */
    public class AnonymousClass28 implements ScrollTipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_01() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_03() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass28() {
        }
    }

    /* renamed from: com.shj.setting.SettingActivity$29 */
    /* loaded from: classes2.dex */
    public class AnonymousClass29 implements ScrollTipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_01() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void buttonClick_03() {
        }

        @Override // com.shj.setting.Dialog.ScrollTipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass29() {
        }
    }

    public static void setDeBugMode() {
        isDebug = true;
    }

    /* loaded from: classes2.dex */
    public class ShelvsAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<Integer> shelfDatas;
        private String stock;
        private String strPrice;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        /* synthetic */ ShelvsAdapter(SettingActivity settingActivity, Context context, List list, AnonymousClass1 anonymousClass1) {
            this(context, list);
        }

        private ShelvsAdapter(Context context, List<Integer> list) {
            this.context = context;
            this.shelfDatas = list;
            this.inflater = LayoutInflater.from(context);
            this.stock = context.getResources().getString(R.string.lab_stock) + ":";
            this.strPrice = context.getResources().getString(R.string.lab_price) + ":";
        }

        public void setShelfDatas(List<Integer> list) {
            this.shelfDatas = list;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<Integer> list = this.shelfDatas;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_shelvs_item, (ViewGroup) null);
                viewHold = new ViewHold();
                viewHold.iv_goods_image = (ImageView) view.findViewById(R.id.iv_goods_image);
                viewHold.tv_shelf = (TextView) view.findViewById(R.id.tv_shelf);
                viewHold.tv_state = (TextView) view.findViewById(R.id.tv_state);
                viewHold.tv_light_state = (TextView) view.findViewById(R.id.tv_light_state);
                viewHold.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHold.tv_price = (TextView) view.findViewById(R.id.tv_price);
                viewHold.tv_count = (TextView) view.findViewById(R.id.tv_count);
                view.setTag(viewHold);
            } else {
                viewHold = (ViewHold) view.getTag();
            }
            int intValue = this.shelfDatas.get(i).intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (shelfInfo != null) {
                viewHold.iv_goods_image.setImageBitmap(ShjManager.getGoodsManager().getGoodsImage(shelfInfo.getGoodsCode(), true));
                viewHold.tv_name.setText(shelfInfo.getGoodsName());
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                decimalFormatSymbols.setDecimalSeparator(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                double intValue2 = shelfInfo.getPrice().intValue();
                Double.isNaN(intValue2);
                String format = decimalFormat.format(intValue2 / 100.0d);
                viewHold.tv_price.setText(this.strPrice + SettingActivity.this.symbol + format);
                viewHold.tv_shelf.setText(String.format("%03d", Integer.valueOf(intValue)));
                viewHold.tv_count.setText(this.stock + shelfInfo.getGoodsCount());
                if (shelfInfo.getStatus().intValue() == 0) {
                    viewHold.tv_state.setTextColor(this.context.getResources().getColor(R.color.setting_blue));
                } else {
                    viewHold.tv_state.setTextColor(this.context.getResources().getColor(R.color.red));
                }
                String str = this.context.getResources().getString(R.string.drop_inspection) + ":";
                if (shelfInfo.getGdjc().intValue() == 1) {
                    viewHold.tv_light_state.setText(str + this.context.getResources().getString(R.string.setting_open));
                    viewHold.tv_light_state.setTextColor(this.context.getResources().getColor(R.color.setting_blue));
                } else {
                    viewHold.tv_light_state.setText(str + this.context.getResources().getString(R.string.setting_close));
                    viewHold.tv_light_state.setTextColor(this.context.getResources().getColor(R.color.red));
                }
                if (shelfInfo.getGoodsCount().intValue() == 0) {
                    shelfInfo.setStatus(2);
                }
                viewHold.tv_state.setText(shelfInfo.getStatusInfo());
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SettingActivity.ShelvsAdapter.1
                final /* synthetic */ int val$shelf;

                AnonymousClass1(int intValue3) {
                    intValue = intValue3;
                }

                /* renamed from: com.shj.setting.SettingActivity$ShelvsAdapter$1$1 */
                /* loaded from: classes2.dex */
                class C00581 implements SetShelfInfoDialog.GoodsInfoSettingListering {
                    C00581() {
                    }

                    @Override // com.shj.setting.Dialog.SetShelfInfoDialog.GoodsInfoSettingListering
                    public void setInfo(int i, String str, int i2, int i3, int i4) {
                        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
                        if (shelfInfo != null) {
                            if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                                SettingActivity.this.loadingDialog.dismiss();
                            }
                            SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this, R.string.saveing);
                            SettingActivity.this.loadingDialog.show();
                            ShjManager.batchStart();
                            if (i4 == 0) {
                                ShjManager.setShelfGoodsCode(i, str);
                                ShjManager.setShelfGoodsPrice(i, i2);
                                ShjManager.setShelfGoodsCount(i, i3);
                            } else if (i4 == 1) {
                                int intValue = shelfInfo.getLayer().intValue() + 1000;
                                ShjManager.setShelfGoodsCode(intValue, str);
                                ShjManager.setShelfGoodsPrice(intValue, i2);
                                ShjManager.setShelfGoodsCount(intValue, i3);
                            } else if (i4 == 2) {
                                ShjManager.setShelfGoodsCode(0, str);
                                ShjManager.setShelfGoodsPrice(0, i2);
                                ShjManager.setShelfGoodsCount(0, i3);
                            }
                            ShjManager.batchEnd();
                        }
                    }
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    SetShelfInfoDialog setShelfInfoDialog = new SetShelfInfoDialog(SettingActivity.this, intValue, SettingActivity.this.symbol);
                    setShelfInfoDialog.setGoodsInfoSettingListering(new SetShelfInfoDialog.GoodsInfoSettingListering() { // from class: com.shj.setting.SettingActivity.ShelvsAdapter.1.1
                        C00581() {
                        }

                        @Override // com.shj.setting.Dialog.SetShelfInfoDialog.GoodsInfoSettingListering
                        public void setInfo(int i2, String str2, int i22, int i3, int i4) {
                            ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(i2));
                            if (shelfInfo2 != null) {
                                if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                                    SettingActivity.this.loadingDialog.dismiss();
                                }
                                SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this, R.string.saveing);
                                SettingActivity.this.loadingDialog.show();
                                ShjManager.batchStart();
                                if (i4 == 0) {
                                    ShjManager.setShelfGoodsCode(i2, str2);
                                    ShjManager.setShelfGoodsPrice(i2, i22);
                                    ShjManager.setShelfGoodsCount(i2, i3);
                                } else if (i4 == 1) {
                                    int intValue3 = shelfInfo2.getLayer().intValue() + 1000;
                                    ShjManager.setShelfGoodsCode(intValue3, str2);
                                    ShjManager.setShelfGoodsPrice(intValue3, i22);
                                    ShjManager.setShelfGoodsCount(intValue3, i3);
                                } else if (i4 == 2) {
                                    ShjManager.setShelfGoodsCode(0, str2);
                                    ShjManager.setShelfGoodsPrice(0, i22);
                                    ShjManager.setShelfGoodsCount(0, i3);
                                }
                                ShjManager.batchEnd();
                            }
                        }
                    });
                    setShelfInfoDialog.show();
                }
            });
            return view;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.SettingActivity$ShelvsAdapter$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements View.OnClickListener {
            final /* synthetic */ int val$shelf;

            AnonymousClass1(int intValue3) {
                intValue = intValue3;
            }

            /* renamed from: com.shj.setting.SettingActivity$ShelvsAdapter$1$1 */
            /* loaded from: classes2.dex */
            class C00581 implements SetShelfInfoDialog.GoodsInfoSettingListering {
                C00581() {
                }

                @Override // com.shj.setting.Dialog.SetShelfInfoDialog.GoodsInfoSettingListering
                public void setInfo(int i2, String str2, int i22, int i3, int i4) {
                    ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(i2));
                    if (shelfInfo2 != null) {
                        if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                            SettingActivity.this.loadingDialog.dismiss();
                        }
                        SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this, R.string.saveing);
                        SettingActivity.this.loadingDialog.show();
                        ShjManager.batchStart();
                        if (i4 == 0) {
                            ShjManager.setShelfGoodsCode(i2, str2);
                            ShjManager.setShelfGoodsPrice(i2, i22);
                            ShjManager.setShelfGoodsCount(i2, i3);
                        } else if (i4 == 1) {
                            int intValue3 = shelfInfo2.getLayer().intValue() + 1000;
                            ShjManager.setShelfGoodsCode(intValue3, str2);
                            ShjManager.setShelfGoodsPrice(intValue3, i22);
                            ShjManager.setShelfGoodsCount(intValue3, i3);
                        } else if (i4 == 2) {
                            ShjManager.setShelfGoodsCode(0, str2);
                            ShjManager.setShelfGoodsPrice(0, i22);
                            ShjManager.setShelfGoodsCount(0, i3);
                        }
                        ShjManager.batchEnd();
                    }
                }
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetShelfInfoDialog setShelfInfoDialog = new SetShelfInfoDialog(SettingActivity.this, intValue, SettingActivity.this.symbol);
                setShelfInfoDialog.setGoodsInfoSettingListering(new SetShelfInfoDialog.GoodsInfoSettingListering() { // from class: com.shj.setting.SettingActivity.ShelvsAdapter.1.1
                    C00581() {
                    }

                    @Override // com.shj.setting.Dialog.SetShelfInfoDialog.GoodsInfoSettingListering
                    public void setInfo(int i2, String str2, int i22, int i3, int i4) {
                        ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(i2));
                        if (shelfInfo2 != null) {
                            if (SettingActivity.this.loadingDialog != null && SettingActivity.this.loadingDialog.isShowing()) {
                                SettingActivity.this.loadingDialog.dismiss();
                            }
                            SettingActivity.this.loadingDialog = new LoadingDialog(SettingActivity.this, R.string.saveing);
                            SettingActivity.this.loadingDialog.show();
                            ShjManager.batchStart();
                            if (i4 == 0) {
                                ShjManager.setShelfGoodsCode(i2, str2);
                                ShjManager.setShelfGoodsPrice(i2, i22);
                                ShjManager.setShelfGoodsCount(i2, i3);
                            } else if (i4 == 1) {
                                int intValue3 = shelfInfo2.getLayer().intValue() + 1000;
                                ShjManager.setShelfGoodsCode(intValue3, str2);
                                ShjManager.setShelfGoodsPrice(intValue3, i22);
                                ShjManager.setShelfGoodsCount(intValue3, i3);
                            } else if (i4 == 2) {
                                ShjManager.setShelfGoodsCode(0, str2);
                                ShjManager.setShelfGoodsPrice(0, i22);
                                ShjManager.setShelfGoodsCount(0, i3);
                            }
                            ShjManager.batchEnd();
                        }
                    }
                });
                setShelfInfoDialog.show();
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHold {
            public ImageView iv_goods_image;
            public TextView tv_count;
            public TextView tv_light_state;
            public TextView tv_name;
            public TextView tv_price;
            public TextView tv_shelf;
            public TextView tv_state;

            public ViewHold() {
            }
        }
    }
}

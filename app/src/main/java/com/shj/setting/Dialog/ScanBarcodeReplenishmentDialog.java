package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mjdev.libaums.fs.UsbFile;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.video.TTSManager;
import com.shj.OnCommandAnswerListener;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.Goods;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.device.scanor.Scanor;
import com.shj.setting.Dialog.EditTextInputDialog;
import com.shj.setting.Dialog.SelectBatchNumberDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.bean.CommodityInfo;
import com.shj.setting.bean.SeasoningCondimentsInfo;
import com.shj.setting.bean.ShelfCommodityInfo;
import com.shj.setting.helper.CalculatedInventory;
import com.shj.setting.helper.CommodityInfoObtain;
import com.shj.setting.helper.ReadLightInspectionData;
import com.shj.setting.helper.ReadShelfMergeState;
import com.shj.setting.helper.SeasoningCondimentsInfoObtain;
import com.shj.setting.helper.UploadOperationRecord;
import com.xyshj.database.setting.AppSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ScanBarcodeReplenishmentDialog extends Dialog {
    private Button bt_calculate_auto;
    private Button bt_close;
    private Button bt_qrcode_input;
    private Button bt_regetdata;
    private Button bt_upload;
    private HashMap<String, CommodityInfo> commodityInfoHashMap;
    private Context context;
    private HashMap<Integer, List<ReplenishData>> dataHashMap;
    private List<ReplenishData> dataList;
    private Handler handler;
    private HashMap<Integer, Boolean> isAllDataRightHashMap;
    private boolean isGetSeasoningCondimentsInfoCompelete;
    private boolean isGetShelfCommodityInfoCompelete;
    private boolean isStartScanor;
    private boolean isUploadReplenishmentSuccess;
    private List<Button> layerButtonList;
    private HashMap<Integer, List<Integer>> lightDataHashMap;
    private LinearLayout ll_item_name;
    private LinearLayout ll_layer_no;
    private LoadingDialog loadingDialog;
    private ListView lv_scan_shelf_tip;
    private String machineId;
    private MyAdapter myAdapter;
    private LoadingDialog scanLoadingDialog;
    private long scanTime;
    private List<SeasoningCondimentsInfo> seasoningCondimentsInfoList;
    private SelectBatchNumberDialog selectBatchNumberDialog;
    private HashMap<String, ShelfCommodityInfo> shelfCommodityInfoHashMap;
    private HashMap<Integer, Integer> shelftMergeStateHashMap;
    private TextView tv_scan_state;

    /* loaded from: classes2.dex */
    public static class ReplenishData {
        public String goodsBatch;
        public int goodsLength;
        public String goodsName;
        public boolean isCountChange;
        public boolean isCurrentScanItem;
        public boolean isGoodsChange;
        public boolean isShowGoodsChangeTip;
        public int localCount;
        public String newGoodsCode;
        public int price;
        public int replenishCount;
        public int shelf;
        public int state;
        public String strShelf;
        public int targetCount;
        public int targetReplenishCount;
        public String goodsCode = "-1";
        public int newPrice = -1;
    }

    public ScanBarcodeReplenishmentDialog(Context context) {
        super(context, R.style.ad_style);
        this.dataList = new ArrayList();
        this.dataHashMap = new HashMap<>();
        this.lightDataHashMap = new HashMap<>();
        this.shelftMergeStateHashMap = new HashMap<>();
        this.seasoningCondimentsInfoList = new ArrayList();
        this.commodityInfoHashMap = new HashMap<>();
        this.shelfCommodityInfoHashMap = new HashMap<>();
        this.layerButtonList = new ArrayList();
        this.isAllDataRightHashMap = new HashMap<>();
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.6
            AnonymousClass6() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText((String) message.obj);
                    TTSManager.addText((String) message.obj);
                } else if (message.what == 1) {
                    ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText((String) message.obj);
                    TTSManager.addText((String) message.obj);
                } else if (message.what == 2) {
                    String str = (String) message.obj;
                    if ("connectState:true".equals(str)) {
                        ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText("扫码头连接正常,请扫码");
                    } else if (!"connectState:false".equals(str)) {
                        ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                        ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                    } else {
                        TTSManager.addText("扫码头已断开连接");
                        ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText("扫码头已断开连接");
                    }
                }
                if (ScanBarcodeReplenishmentDialog.this.scanLoadingDialog != null) {
                    ScanBarcodeReplenishmentDialog.this.scanLoadingDialog.dismiss();
                }
            }
        };
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_scan_barcode_replenishment);
        setCanceledOnTouchOutside(false);
        this.machineId = AppSetting.getMachineId(this.context, null);
        findView();
        setListener();
        getAllShelfInfo();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.lv_scan_shelf_tip.setAdapter((ListAdapter) myAdapter);
        addLayerButton();
        addItemName();
        startScanQrcode();
        getNetServiceData();
    }

    public void getNetServiceData() {
        showLoadingDialog(R.string.loading);
        new CommodityInfoObtain().getGoodsInfo(this.context, this.machineId, new CommodityInfoObtain.GetInfoCompleteListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void complete(HashMap<String, ShelfCommodityInfo> hashMap, HashMap<String, CommodityInfo> hashMap2) {
                ScanBarcodeReplenishmentDialog.this.shelfCommodityInfoHashMap = hashMap;
                ScanBarcodeReplenishmentDialog.this.commodityInfoHashMap = hashMap2;
                ScanBarcodeReplenishmentDialog.this.isGetShelfCommodityInfoCompelete = true;
                if (ScanBarcodeReplenishmentDialog.this.isGetSeasoningCondimentsInfoCompelete) {
                    ScanBarcodeReplenishmentDialog.this.setReplenishData();
                    ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
                }
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void error(String str) {
                ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }
        });
        new SeasoningCondimentsInfoObtain().getGoodsInfo(this.context, this.machineId, new SeasoningCondimentsInfoObtain.GetInfoCompleteListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.2
            AnonymousClass2() {
            }

            @Override // com.shj.setting.helper.SeasoningCondimentsInfoObtain.GetInfoCompleteListener
            public void complete(List<SeasoningCondimentsInfo> list) {
                ScanBarcodeReplenishmentDialog.this.seasoningCondimentsInfoList = list;
                ScanBarcodeReplenishmentDialog.this.isGetSeasoningCondimentsInfoCompelete = true;
                if (ScanBarcodeReplenishmentDialog.this.isGetShelfCommodityInfoCompelete) {
                    ScanBarcodeReplenishmentDialog.this.setReplenishData();
                    ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
                }
            }

            @Override // com.shj.setting.helper.SeasoningCondimentsInfoObtain.GetInfoCompleteListener
            public void error(String str) {
                ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements CommodityInfoObtain.GetInfoCompleteListener {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
        public void complete(HashMap<String, ShelfCommodityInfo> hashMap, HashMap<String, CommodityInfo> hashMap2) {
            ScanBarcodeReplenishmentDialog.this.shelfCommodityInfoHashMap = hashMap;
            ScanBarcodeReplenishmentDialog.this.commodityInfoHashMap = hashMap2;
            ScanBarcodeReplenishmentDialog.this.isGetShelfCommodityInfoCompelete = true;
            if (ScanBarcodeReplenishmentDialog.this.isGetSeasoningCondimentsInfoCompelete) {
                ScanBarcodeReplenishmentDialog.this.setReplenishData();
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }
        }

        @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
        public void error(String str) {
            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements SeasoningCondimentsInfoObtain.GetInfoCompleteListener {
        AnonymousClass2() {
        }

        @Override // com.shj.setting.helper.SeasoningCondimentsInfoObtain.GetInfoCompleteListener
        public void complete(List<SeasoningCondimentsInfo> list) {
            ScanBarcodeReplenishmentDialog.this.seasoningCondimentsInfoList = list;
            ScanBarcodeReplenishmentDialog.this.isGetSeasoningCondimentsInfoCompelete = true;
            if (ScanBarcodeReplenishmentDialog.this.isGetShelfCommodityInfoCompelete) {
                ScanBarcodeReplenishmentDialog.this.setReplenishData();
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }
        }

        @Override // com.shj.setting.helper.SeasoningCondimentsInfoObtain.GetInfoCompleteListener
        public void error(String str) {
            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
        }
    }

    private void showLoadingDialog(int i) {
        closeLoadingDialog();
        LoadingDialog loadingDialog = new LoadingDialog(this.context, i);
        this.loadingDialog = loadingDialog;
        loadingDialog.show();
    }

    public void closeLoadingDialog() {
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return;
        }
        this.loadingDialog.dismiss();
    }

    public void setReplenishData() {
        Iterator<Integer> it = this.dataHashMap.keySet().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            for (ReplenishData replenishData : this.dataHashMap.get(Integer.valueOf(intValue))) {
                SeasoningCondimentsInfo findSeasoningCondimentsInfo = findSeasoningCondimentsInfo(replenishData.shelf);
                if (findSeasoningCondimentsInfo != null) {
                    replenishData.targetReplenishCount = findSeasoningCondimentsInfo.targetReplenishedCount;
                    replenishData.targetCount = findSeasoningCondimentsInfo.targetCount;
                    if (!replenishData.goodsCode.equalsIgnoreCase(findSeasoningCondimentsInfo.goodsCode)) {
                        if (replenishData.localCount != 0) {
                            replenishData.isCountChange = true;
                        }
                        replenishData.newGoodsCode = findSeasoningCondimentsInfo.goodsCode;
                        replenishData.newPrice = Integer.valueOf(findSeasoningCondimentsInfo.price).intValue();
                        replenishData.localCount = 0;
                    }
                } else {
                    replenishData.targetCount = replenishData.localCount;
                }
                ShelfCommodityInfo shelfCommodityInfo = this.shelfCommodityInfoHashMap.get(replenishData.strShelf);
                if (shelfCommodityInfo != null) {
                    replenishData.goodsLength = shelfCommodityInfo.length;
                }
            }
            checkLayerData(intValue);
        }
        this.myAdapter.notifyDataSetChanged();
    }

    public void clearReplenishData() {
        Iterator<Integer> it = this.dataHashMap.keySet().iterator();
        while (it.hasNext()) {
            for (ReplenishData replenishData : this.dataHashMap.get(Integer.valueOf(it.next().intValue()))) {
                replenishData.replenishCount = 0;
                replenishData.targetReplenishCount = 0;
                replenishData.targetCount = 0;
                replenishData.goodsLength = 0;
            }
        }
        this.myAdapter.notifyDataSetChanged();
        this.isAllDataRightHashMap.clear();
        Iterator<Button> it2 = this.layerButtonList.iterator();
        while (it2.hasNext()) {
            it2.next().setBackgroundResource(R.drawable.selector_soft_manage_button);
        }
    }

    private SeasoningCondimentsInfo findSeasoningCondimentsInfo(int i) {
        for (SeasoningCondimentsInfo seasoningCondimentsInfo : this.seasoningCondimentsInfoList) {
            if (seasoningCondimentsInfo.shelf == i) {
                return seasoningCondimentsInfo;
            }
        }
        return null;
    }

    private void addItemName() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_scan_replennishment_item_name, (ViewGroup) null);
        inflate.setBackgroundColor(-986625);
        this.ll_item_name.addView(inflate);
    }

    private void addLayerButton() {
        List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
        int dimensionPixelOffset = this.context.getResources().getDimensionPixelOffset(R.dimen.x125);
        int dimensionPixelOffset2 = this.context.getResources().getDimensionPixelOffset(R.dimen.y55);
        int color = this.context.getResources().getColor(R.color.setting_white);
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R.dimen.text_small);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = 10;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = 10;
        int size = list.size();
        int size2 = size % 7 != 0 ? (list.size() / ((size / 7) + 1)) + 1 : 7;
        String string = this.context.getString(R.string.layer);
        LinearLayout linearLayout = null;
        for (int i = 0; i < size; i++) {
            if (i % size2 == 0) {
                linearLayout = new LinearLayout(this.context);
                this.ll_layer_no.addView(linearLayout, layoutParams);
            }
            Button button = new Button(this.context);
            button.setText(String.valueOf(list.get(i)) + string);
            button.setTag(list.get(i));
            button.setWidth(dimensionPixelOffset);
            button.setHeight(dimensionPixelOffset2);
            button.setBackgroundResource(R.drawable.selector_soft_manage_button);
            button.setTextColor(color);
            button.setTextSize(dimensionPixelSize);
            button.setGravity(17);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.3
                AnonymousClass3() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
                    scanBarcodeReplenishmentDialog.dataList = (List) scanBarcodeReplenishmentDialog.dataHashMap.get(Integer.valueOf(intValue));
                    ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                    ScanBarcodeReplenishmentDialog.this.setLayerButtonState(intValue);
                }
            });
            linearLayout.addView(button, layoutParams2);
            this.layerButtonList.add(button);
        }
        this.layerButtonList.get(0).performClick();
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int intValue = ((Integer) view.getTag()).intValue();
            ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
            scanBarcodeReplenishmentDialog.dataList = (List) scanBarcodeReplenishmentDialog.dataHashMap.get(Integer.valueOf(intValue));
            ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
            ScanBarcodeReplenishmentDialog.this.setLayerButtonState(intValue);
        }
    }

    private void getAllShelfInfo() {
        List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
        HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesLayerMap;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            List<Integer> list2 = hashMap.get(Integer.valueOf(intValue));
            ArrayList arrayList = new ArrayList();
            this.dataHashMap.put(Integer.valueOf(intValue), arrayList);
            Iterator<Integer> it2 = list2.iterator();
            while (it2.hasNext()) {
                int intValue2 = it2.next().intValue();
                ReplenishData replenishData = new ReplenishData();
                replenishData.shelf = intValue2;
                replenishData.strShelf = String.format("%03d", Integer.valueOf(intValue2));
                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue2));
                if (shelfInfo != null) {
                    replenishData.localCount = shelfInfo.getGoodsCount().intValue();
                    replenishData.goodsBatch = shelfInfo.getGoodsbatchnumber();
                    Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
                    if (goodsByCode != null) {
                        replenishData.goodsName = goodsByCode.getName();
                        replenishData.goodsCode = goodsByCode.getCode();
                        replenishData.price = goodsByCode.getPrice();
                    }
                }
                arrayList.add(replenishData);
            }
        }
    }

    public void setLayerButtonState(int i) {
        for (Button button : this.layerButtonList) {
            if (i == ((Integer) button.getTag()).intValue()) {
                button.setSelected(true);
            } else {
                button.setSelected(false);
            }
        }
    }

    private void checkLayerData(int i) {
        boolean z;
        List<ReplenishData> list = this.dataHashMap.get(Integer.valueOf(i));
        if (list != null) {
            for (ReplenishData replenishData : list) {
                if (replenishData.replenishCount != replenishData.targetReplenishCount || replenishData.localCount != replenishData.targetCount) {
                    z = false;
                    break;
                }
            }
            z = true;
            if (z) {
                for (Button button : this.layerButtonList) {
                    if (i == ((Integer) button.getTag()).intValue()) {
                        button.setBackgroundResource(R.drawable.selector_button_green2);
                        this.isAllDataRightHashMap.put(Integer.valueOf(i), true);
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Scanor.ScanorListener {
        AnonymousClass4() {
        }

        @Override // com.shj.device.scanor.Scanor.ScanorListener
        public void onMessage(String str) {
            Loger.writeLog("SCANOR", str);
            if (str.equalsIgnoreCase("noScanor") || str.contains("没有找到扫码头")) {
                ScanBarcodeReplenishmentDialog.this.isStartScanor = false;
                Message message = new Message();
                message.what = 0;
                message.obj = "未找到扫码头";
                ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message);
                return;
            }
            if (str.equalsIgnoreCase("connected") || str.contains("已找到扫码头") || "isRunning".equals(str)) {
                Message message2 = new Message();
                message2.what = 1;
                message2.obj = "已找到扫码头，请扫码";
                ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message2);
                ScanBarcodeReplenishmentDialog.this.isStartScanor = true;
                return;
            }
            Message message3 = new Message();
            message3.what = 2;
            message3.obj = str;
            ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message3);
        }
    }

    private void startScanQrcode() {
        if (this.isStartScanor) {
            return;
        }
        Scanor.setScanorListener(new Scanor.ScanorListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.4
            AnonymousClass4() {
            }

            @Override // com.shj.device.scanor.Scanor.ScanorListener
            public void onMessage(String str) {
                Loger.writeLog("SCANOR", str);
                if (str.equalsIgnoreCase("noScanor") || str.contains("没有找到扫码头")) {
                    ScanBarcodeReplenishmentDialog.this.isStartScanor = false;
                    Message message = new Message();
                    message.what = 0;
                    message.obj = "未找到扫码头";
                    ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message);
                    return;
                }
                if (str.equalsIgnoreCase("connected") || str.contains("已找到扫码头") || "isRunning".equals(str)) {
                    Message message2 = new Message();
                    message2.what = 1;
                    message2.obj = "已找到扫码头，请扫码";
                    ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message2);
                    ScanBarcodeReplenishmentDialog.this.isStartScanor = true;
                    return;
                }
                Message message3 = new Message();
                message3.what = 2;
                message3.obj = str;
                ScanBarcodeReplenishmentDialog.this.handler.sendMessage(message3);
            }
        });
        LoadingDialog loadingDialog = new LoadingDialog(this.context, this.context.getString(R.string.searching));
        this.scanLoadingDialog = loadingDialog;
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.5
            AnonymousClass5() {
            }

            /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$5$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    Scanor.start(ScanBarcodeReplenishmentDialog.this.context, false);
                }
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                new Thread(new Runnable() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.5.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Scanor.start(ScanBarcodeReplenishmentDialog.this.context, false);
                    }
                }).start();
            }
        });
        this.scanLoadingDialog.show();
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements DialogInterface.OnShowListener {
        AnonymousClass5() {
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$5$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Scanor.start(ScanBarcodeReplenishmentDialog.this.context, false);
            }
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            new Thread(new Runnable() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.5.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    Scanor.start(ScanBarcodeReplenishmentDialog.this.context, false);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends Handler {
        AnonymousClass6() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText((String) message.obj);
                TTSManager.addText((String) message.obj);
            } else if (message.what == 1) {
                ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText((String) message.obj);
                TTSManager.addText((String) message.obj);
            } else if (message.what == 2) {
                String str = (String) message.obj;
                if ("connectState:true".equals(str)) {
                    ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText("扫码头连接正常,请扫码");
                } else if (!"connectState:false".equals(str)) {
                    ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                    ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                } else {
                    TTSManager.addText("扫码头已断开连接");
                    ScanBarcodeReplenishmentDialog.this.tv_scan_state.setText("扫码头已断开连接");
                }
            }
            if (ScanBarcodeReplenishmentDialog.this.scanLoadingDialog != null) {
                ScanBarcodeReplenishmentDialog.this.scanLoadingDialog.dismiss();
            }
        }
    }

    public void getReplenishData(String str) {
        if (System.currentTimeMillis() - this.scanTime < 1000) {
            return;
        }
        this.scanTime = System.currentTimeMillis();
        SelectBatchNumberDialog selectBatchNumberDialog = this.selectBatchNumberDialog;
        if (selectBatchNumberDialog == null || !selectBatchNumberDialog.isShowing()) {
            CommodityInfo commodityInfo = this.commodityInfoHashMap.get(str);
            String str2 = null;
            if (commodityInfo == null) {
                Iterator<SeasoningCondimentsInfo> it = this.seasoningCondimentsInfoList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SeasoningCondimentsInfo next = it.next();
                    if (str.equals(next.barCode)) {
                        str2 = next.goodsCode;
                        break;
                    }
                }
            } else {
                str2 = commodityInfo.code;
            }
            if (TextUtils.isEmpty(str2)) {
                TTSManager.addText("商品条码未入库");
                ToastUitl.showShort(this.context, "商品条码未入库");
            } else {
                getBatchInfo(this.machineId, str2);
            }
        }
    }

    private void getBatchInfo(String str, String str2) {
        String drugsBatchUrl = NetAddress.getDrugsBatchUrl();
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            jSONObject.put("goodsCode", str2);
            RequestItem requestItem = new RequestItem(drugsBatchUrl, jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.7
                final /* synthetic */ String val$goodsCode;

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass7(String str22) {
                    str2 = str22;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
                    ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "获取批号信息失败");
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
                    try {
                        JSONObject jSONObject2 = new JSONObject(str3);
                        if (jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                            JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                            ArrayList arrayList = new ArrayList();
                            Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                                String optString = optJSONArray.getJSONObject(i2).optString("scpc");
                                if (ScanBarcodeReplenishmentDialog.this.isReplenishmentBatch(optString)) {
                                    arrayList.add(optString);
                                }
                            }
                            if (arrayList.size() == 0) {
                                ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "未获取到批号信息");
                            } else if (arrayList.size() == 1) {
                                ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, (String) arrayList.get(0));
                            } else {
                                ScanBarcodeReplenishmentDialog.this.selectBatchNumberDialog = new SelectBatchNumberDialog(ScanBarcodeReplenishmentDialog.this.context, arrayList, new SelectBatchNumberDialog.SelectListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.7.1
                                    AnonymousClass1() {
                                    }

                                    @Override // com.shj.setting.Dialog.SelectBatchNumberDialog.SelectListener
                                    public void select(int i3, String str4) {
                                        ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, str4);
                                    }
                                });
                                ScanBarcodeReplenishmentDialog.this.selectBatchNumberDialog.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$7$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements SelectBatchNumberDialog.SelectListener {
                    AnonymousClass1() {
                    }

                    @Override // com.shj.setting.Dialog.SelectBatchNumberDialog.SelectListener
                    public void select(int i3, String str4) {
                        ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, str4);
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ String val$goodsCode;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass7(String str22) {
            str2 = str22;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "获取批号信息失败");
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
            try {
                JSONObject jSONObject2 = new JSONObject(str3);
                if (jSONObject2.getString("code").equalsIgnoreCase("H0000")) {
                    JSONArray optJSONArray = jSONObject2.optJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    ArrayList arrayList = new ArrayList();
                    Loger.writeLog("SET", "classItems size=" + optJSONArray.length());
                    for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                        String optString = optJSONArray.getJSONObject(i2).optString("scpc");
                        if (ScanBarcodeReplenishmentDialog.this.isReplenishmentBatch(optString)) {
                            arrayList.add(optString);
                        }
                    }
                    if (arrayList.size() == 0) {
                        ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "未获取到批号信息");
                    } else if (arrayList.size() == 1) {
                        ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, (String) arrayList.get(0));
                    } else {
                        ScanBarcodeReplenishmentDialog.this.selectBatchNumberDialog = new SelectBatchNumberDialog(ScanBarcodeReplenishmentDialog.this.context, arrayList, new SelectBatchNumberDialog.SelectListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.7.1
                            AnonymousClass1() {
                            }

                            @Override // com.shj.setting.Dialog.SelectBatchNumberDialog.SelectListener
                            public void select(int i3, String str4) {
                                ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, str4);
                            }
                        });
                        ScanBarcodeReplenishmentDialog.this.selectBatchNumberDialog.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$7$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements SelectBatchNumberDialog.SelectListener {
            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.SelectBatchNumberDialog.SelectListener
            public void select(int i3, String str4) {
                ScanBarcodeReplenishmentDialog.this.getBarcodeMatchShelf(str2, str4);
            }
        }
    }

    public boolean isReplenishmentBatch(String str) {
        Iterator<SeasoningCondimentsInfo> it = this.seasoningCondimentsInfoList.iterator();
        while (it.hasNext()) {
            if (str.equalsIgnoreCase(it.next().goodsBatch)) {
                return true;
            }
        }
        return false;
    }

    public void getBarcodeMatchShelf(String str, String str2) {
        int i;
        Loger.writeLog("SET", "getBarcodeMatchShelf goodsCode=" + str + "batch=" + str2);
        Iterator<SeasoningCondimentsInfo> it = this.seasoningCondimentsInfoList.iterator();
        boolean z = false;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            }
            SeasoningCondimentsInfo next = it.next();
            Loger.writeLog("SET", "temp goodsCode=" + next.goodsCode + "batch=" + next.goodsBatch);
            if (next.goodsCode.equals(str) && next.goodsBatch.equals(str2)) {
                if (isCanAdd(next.shelf)) {
                    i = next.shelf;
                    z = false;
                    break;
                }
                z = true;
            }
        }
        if (z) {
            TTSManager.addText("此商品已补满");
            ToastUitl.showShort(this.context, "此商品已补满");
            return;
        }
        if (i == -1) {
            showNoFindBatchGoodsTipDialog("未找到此商品调拔记录, 请核对后台商品调拔数据，如果后台更改了数据，请点击下方重新获取后台数据按扭。");
            return;
        }
        int layerByShelf = Shj.getLayerByShelf(i);
        List<ReplenishData> list = this.dataHashMap.get(Integer.valueOf(layerByShelf));
        this.dataList = list;
        ReplenishData replenishData = null;
        Iterator<ReplenishData> it2 = list.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            ReplenishData next2 = it2.next();
            if (next2.shelf == i) {
                next2.replenishCount++;
                next2.localCount++;
                next2.isCountChange = true;
                next2.goodsBatch = str2;
                if (next2.isGoodsChange && !next2.isShowGoodsChangeTip) {
                    next2.isShowGoodsChangeTip = true;
                    showShelfGoodsChangeTip(next2.strShelf);
                }
                replenishData = next2;
            }
        }
        if (replenishData != null) {
            this.dataList.remove(replenishData);
            replenishData.isCurrentScanItem = true;
            Iterator<ReplenishData> it3 = this.dataList.iterator();
            while (it3.hasNext()) {
                it3.next().isCurrentScanItem = false;
            }
            this.dataList.add(0, replenishData);
        }
        this.myAdapter.notifyDataSetChanged();
        setLayerButtonState(layerByShelf);
        checkLayerData(layerByShelf);
        sendComodAndShowTip(i);
    }

    private void showNoFindBatchGoodsTipDialog(String str) {
        Context context = this.context;
        new TipDialog(context, str, context.getString(R.string.button_ok)).show();
    }

    private void showShelfGoodsChangeTip(String str) {
        new TipDialog(this.context, "请移除" + str + "货道原来商品", this.context.getString(R.string.button_ok)).show();
    }

    private boolean isCanAdd(int i) {
        Iterator<ReplenishData> it = this.dataHashMap.get(Integer.valueOf(Shj.getLayerByShelf(i))).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ReplenishData next = it.next();
            if (next.shelf == i) {
                if (next.replenishCount < next.targetReplenishCount) {
                    return true;
                }
            }
        }
        return false;
    }

    private String sendComodAndShowTip(int i) {
        int jghByShelf = Shj.getJghByShelf(i);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i));
        sendCommod(arrayList);
        StringBuilder sb = new StringBuilder();
        sb.append(jghByShelf + 1);
        sb.append("号柜");
        sb.append(String.format("%03d", Integer.valueOf(i)));
        sb.append("货道");
        String sb2 = sb.toString();
        TTSManager.addText(sb2);
        TTSManager.addText(sb2);
        String str = "";
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            str = i2 != 0 ? str + VoiceWakeuperAidl.PARAMS_SEPARATE + arrayList.get(i2) : str + arrayList.get(i2);
        }
        return this.context.getResources().getString(R.string.lab_shelf) + "[" + str + "]";
    }

    private void sendCommod(List<Integer> list) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.shelfLightControl(list);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass8() {
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass8() {
        }
    }

    private void findView() {
        this.ll_layer_no = (LinearLayout) findViewById(R.id.ll_layer_no);
        this.ll_item_name = (LinearLayout) findViewById(R.id.ll_item_name);
        this.tv_scan_state = (TextView) findViewById(R.id.tv_scan_state);
        this.lv_scan_shelf_tip = (ListView) findViewById(R.id.lv_scan_shelf_tip);
        this.bt_close = (Button) findViewById(R.id.bt_cancel);
        this.bt_calculate_auto = (Button) findViewById(R.id.bt_calculate_auto);
        this.bt_upload = (Button) findViewById(R.id.bt_upload);
        this.bt_regetdata = (Button) findViewById(R.id.bt_regetdata);
        this.bt_qrcode_input = (Button) findViewById(R.id.bt_qrcode_input);
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ScanBarcodeReplenishmentDialog.this.isUploadReplenishmentSuccess) {
                ScanBarcodeReplenishmentDialog.this.dismiss();
                return;
            }
            TipDialog tipDialog = new TipDialog(ScanBarcodeReplenishmentDialog.this.context, 0, "没有保存补货信息，确定关闭吗", ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_cancel), ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok));
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.9.1
                final /* synthetic */ TipDialog val$tipDialog;

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1(TipDialog tipDialog2) {
                    tipDialog = tipDialog2;
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    tipDialog.dismiss();
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                    tipDialog.dismiss();
                    ScanBarcodeReplenishmentDialog.this.dismiss();
                }
            });
            tipDialog2.show();
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$9$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            final /* synthetic */ TipDialog val$tipDialog;

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass1(TipDialog tipDialog2) {
                tipDialog = tipDialog2;
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                tipDialog.dismiss();
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
                tipDialog.dismiss();
                ScanBarcodeReplenishmentDialog.this.dismiss();
            }
        }
    }

    private void setListener() {
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.9
            AnonymousClass9() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ScanBarcodeReplenishmentDialog.this.isUploadReplenishmentSuccess) {
                    ScanBarcodeReplenishmentDialog.this.dismiss();
                    return;
                }
                TipDialog tipDialog2 = new TipDialog(ScanBarcodeReplenishmentDialog.this.context, 0, "没有保存补货信息，确定关闭吗", ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_cancel), ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok));
                tipDialog2.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.9.1
                    final /* synthetic */ TipDialog val$tipDialog;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(TipDialog tipDialog22) {
                        tipDialog = tipDialog22;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        tipDialog.dismiss();
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                        tipDialog.dismiss();
                        ScanBarcodeReplenishmentDialog.this.dismiss();
                    }
                });
                tipDialog22.show();
            }

            /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$9$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TipDialog.TipDialogListener {
                final /* synthetic */ TipDialog val$tipDialog;

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1(TipDialog tipDialog22) {
                    tipDialog = tipDialog22;
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    tipDialog.dismiss();
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                    tipDialog.dismiss();
                    ScanBarcodeReplenishmentDialog.this.dismiss();
                }
            }
        });
        this.bt_calculate_auto.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.10
            AnonymousClass10() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScanBarcodeReplenishmentDialog.this.calulateInventory();
            }
        });
        this.bt_upload.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.11
            AnonymousClass11() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
                ArrayList arrayList = new ArrayList();
                Iterator<Integer> it = list.iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    if (ScanBarcodeReplenishmentDialog.this.isAllDataRightHashMap.get(Integer.valueOf(intValue)) == null || !((Boolean) ScanBarcodeReplenishmentDialog.this.isAllDataRightHashMap.get(Integer.valueOf(intValue))).booleanValue()) {
                        arrayList.add(Integer.valueOf(intValue));
                    }
                }
                if (arrayList.size() > 0) {
                    String str = "还有";
                    for (int i = 0; i < arrayList.size(); i++) {
                        str = i == 0 ? str + arrayList.get(i) : str + "," + arrayList.get(i);
                    }
                    TipDialog tipDialog = new TipDialog(ScanBarcodeReplenishmentDialog.this.context, 0, str + "层补货数据不对，确定保存吗？", ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_cancel), ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok));
                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.11.1
                        final /* synthetic */ TipDialog val$tipDialog;

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        AnonymousClass1(TipDialog tipDialog2) {
                            tipDialog = tipDialog2;
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                            tipDialog.dismiss();
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                            tipDialog.dismiss();
                            ScanBarcodeReplenishmentDialog.this.uploadReplenishmentRecord(ScanBarcodeReplenishmentDialog.this.machineId, SettingActivity.getUserName());
                        }
                    });
                    tipDialog2.show();
                    return;
                }
                ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
                scanBarcodeReplenishmentDialog.uploadReplenishmentRecord(scanBarcodeReplenishmentDialog.machineId, SettingActivity.getUserName());
            }

            /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$11$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TipDialog.TipDialogListener {
                final /* synthetic */ TipDialog val$tipDialog;

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1(TipDialog tipDialog2) {
                    tipDialog = tipDialog2;
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    tipDialog.dismiss();
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                    tipDialog.dismiss();
                    ScanBarcodeReplenishmentDialog.this.uploadReplenishmentRecord(ScanBarcodeReplenishmentDialog.this.machineId, SettingActivity.getUserName());
                }
            }
        });
        this.bt_regetdata.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.12
            AnonymousClass12() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScanBarcodeReplenishmentDialog.this.clearReplenishData();
                ScanBarcodeReplenishmentDialog.this.getNetServiceData();
            }
        });
        this.bt_qrcode_input.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.13
            AnonymousClass13() {
            }

            /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$13$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements EditTextInputDialog.OkClickListener {
                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.EditTextInputDialog.OkClickListener
                public void onClick(String str) {
                    ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                    ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                }
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new EditTextInputDialog(ScanBarcodeReplenishmentDialog.this.context, new EditTextInputDialog.OkClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.13.1
                    AnonymousClass1() {
                    }

                    @Override // com.shj.setting.Dialog.EditTextInputDialog.OkClickListener
                    public void onClick(String str) {
                        ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                        ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements View.OnClickListener {
        AnonymousClass10() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScanBarcodeReplenishmentDialog.this.calulateInventory();
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements View.OnClickListener {
        AnonymousClass11() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
            ArrayList arrayList = new ArrayList();
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                if (ScanBarcodeReplenishmentDialog.this.isAllDataRightHashMap.get(Integer.valueOf(intValue)) == null || !((Boolean) ScanBarcodeReplenishmentDialog.this.isAllDataRightHashMap.get(Integer.valueOf(intValue))).booleanValue()) {
                    arrayList.add(Integer.valueOf(intValue));
                }
            }
            if (arrayList.size() > 0) {
                String str = "还有";
                for (int i = 0; i < arrayList.size(); i++) {
                    str = i == 0 ? str + arrayList.get(i) : str + "," + arrayList.get(i);
                }
                TipDialog tipDialog2 = new TipDialog(ScanBarcodeReplenishmentDialog.this.context, 0, str + "层补货数据不对，确定保存吗？", ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_cancel), ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok));
                tipDialog2.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.11.1
                    final /* synthetic */ TipDialog val$tipDialog;

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass1(TipDialog tipDialog22) {
                        tipDialog = tipDialog22;
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        tipDialog.dismiss();
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                        tipDialog.dismiss();
                        ScanBarcodeReplenishmentDialog.this.uploadReplenishmentRecord(ScanBarcodeReplenishmentDialog.this.machineId, SettingActivity.getUserName());
                    }
                });
                tipDialog22.show();
                return;
            }
            ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
            scanBarcodeReplenishmentDialog.uploadReplenishmentRecord(scanBarcodeReplenishmentDialog.machineId, SettingActivity.getUserName());
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$11$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            final /* synthetic */ TipDialog val$tipDialog;

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass1(TipDialog tipDialog22) {
                tipDialog = tipDialog22;
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                tipDialog.dismiss();
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
                tipDialog.dismiss();
                ScanBarcodeReplenishmentDialog.this.uploadReplenishmentRecord(ScanBarcodeReplenishmentDialog.this.machineId, SettingActivity.getUserName());
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements View.OnClickListener {
        AnonymousClass12() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScanBarcodeReplenishmentDialog.this.clearReplenishData();
            ScanBarcodeReplenishmentDialog.this.getNetServiceData();
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements View.OnClickListener {
        AnonymousClass13() {
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$13$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements EditTextInputDialog.OkClickListener {
            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.EditTextInputDialog.OkClickListener
            public void onClick(String str) {
                ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            new EditTextInputDialog(ScanBarcodeReplenishmentDialog.this.context, new EditTextInputDialog.OkClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.13.1
                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.EditTextInputDialog.OkClickListener
                public void onClick(String str) {
                    ScanBarcodeReplenishmentDialog.this.getReplenishData(str);
                    ScanBarcodeReplenishmentDialog.this.myAdapter.notifyDataSetChanged();
                }
            }).show();
        }
    }

    public void uploadReplenishmentRecord(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        showLoadingDialog(R.string.saveing);
        UploadOperationRecord.upload(SettingActivity.getUserName(), "扫码补货", "上传补货记录");
        String uploadReplenishmentRecordUrl = NetAddress.getUploadReplenishmentRecordUrl();
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("machineCode", this.machineId);
            hashMap.put("userId", str2);
            ArrayList arrayList = new ArrayList();
            Iterator<Integer> it = this.dataHashMap.keySet().iterator();
            while (it.hasNext()) {
                for (ReplenishData replenishData : this.dataHashMap.get(Integer.valueOf(it.next().intValue()))) {
                    if (replenishData.isCountChange) {
                        ShjManager.setShelfGoodsCount(replenishData.shelf, replenishData.localCount);
                    }
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(replenishData.shelf));
                    if (shelfInfo != null && !TextUtils.isEmpty(replenishData.goodsBatch)) {
                        shelfInfo.setGoodsbatchnumber(replenishData.goodsBatch);
                    }
                    if (!replenishData.newGoodsCode.equalsIgnoreCase("-1") && !replenishData.goodsCode.equalsIgnoreCase(replenishData.newGoodsCode)) {
                        ShjManager.setShelfGoodsCode(replenishData.shelf, replenishData.newGoodsCode);
                    }
                    if (replenishData.newPrice != -1 && replenishData.price != replenishData.newPrice) {
                        ShjManager.setShelfGoodsPrice(replenishData.shelf, replenishData.newPrice);
                    }
                    String str3 = replenishData.goodsCode;
                    if (!replenishData.newGoodsCode.equalsIgnoreCase("-1")) {
                        str3 = replenishData.newGoodsCode;
                    }
                    int i = replenishData.price;
                    if (i != -1) {
                        i = replenishData.newPrice;
                    }
                    arrayList.add(getJsonString(replenishData.strShelf, replenishData.localCount, str3, replenishData.goodsBatch, String.valueOf(i)));
                }
            }
            hashMap.put("hdList", arrayList);
            RequestItem requestItem = new RequestItem(uploadReplenishmentRecordUrl, new JSONObject(hashMap), "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.14
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass14() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i2, String str4, Throwable th) {
                    ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息失败");
                    ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i2, String str4) {
                    try {
                        if (new JSONObject(str4).getString("code").equalsIgnoreCase("H0000")) {
                            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息成功");
                            ScanBarcodeReplenishmentDialog.this.isUploadReplenishmentSuccess = true;
                        } else {
                            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
                    return true;
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass14() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str4, Throwable th) {
            ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息失败");
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str4) {
            try {
                if (new JSONObject(str4).getString("code").equalsIgnoreCase("H0000")) {
                    ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息成功");
                    ScanBarcodeReplenishmentDialog.this.isUploadReplenishmentSuccess = true;
                } else {
                    ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, "上传补货信息失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            return true;
        }
    }

    public static JSONObject getJsonString(String str, int i, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap();
        hashMap.put("hdbh", str);
        hashMap.put("dbhsjkc", Integer.valueOf(i));
        hashMap.put("spbh", str2);
        hashMap.put("scpc", str3);
        hashMap.put("spjg", str4);
        return new JSONObject(hashMap);
    }

    public void calulateInventory() {
        showLoadingDialog(R.string.calculateing);
        new ReadShelfMergeState().readMergeState(this.context, new ReadShelfMergeState.ReadCompleteListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.15
            AnonymousClass15() {
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void complete(HashMap<Integer, Integer> hashMap) {
                ScanBarcodeReplenishmentDialog.this.shelftMergeStateHashMap = hashMap;
                ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
                scanBarcodeReplenishmentDialog.readLightData(scanBarcodeReplenishmentDialog.context, ScanBarcodeReplenishmentDialog.this.machineId);
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void error(int i, String str) {
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
                if (i == 0) {
                    ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
                } else if (i == 1) {
                    new TipDialog(ScanBarcodeReplenishmentDialog.this.context, str, ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok)).show();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements ReadShelfMergeState.ReadCompleteListener {
        AnonymousClass15() {
        }

        @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
        public void complete(HashMap<Integer, Integer> hashMap) {
            ScanBarcodeReplenishmentDialog.this.shelftMergeStateHashMap = hashMap;
            ScanBarcodeReplenishmentDialog scanBarcodeReplenishmentDialog = ScanBarcodeReplenishmentDialog.this;
            scanBarcodeReplenishmentDialog.readLightData(scanBarcodeReplenishmentDialog.context, ScanBarcodeReplenishmentDialog.this.machineId);
        }

        @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
        public void error(int i, String str) {
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            if (i == 0) {
                ToastUitl.showShort(ScanBarcodeReplenishmentDialog.this.context, str);
            } else if (i == 1) {
                new TipDialog(ScanBarcodeReplenishmentDialog.this.context, str, ScanBarcodeReplenishmentDialog.this.context.getString(R.string.button_ok)).show();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements ReadLightInspectionData.ReadLightCompeleteListener {
        final /* synthetic */ Context val$context;

        AnonymousClass16(Context context) {
            context = context;
        }

        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
        public void complete(HashMap<Integer, List<Integer>> hashMap) {
            ScanBarcodeReplenishmentDialog.this.lightDataHashMap = hashMap;
            ScanBarcodeReplenishmentDialog.this.updataCount();
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
        }

        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
        public void error(String str) {
            ToastUitl.showShort(context, str);
            ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
        }
    }

    public void readLightData(Context context, String str) {
        new ReadLightInspectionData().readLightData(context, new ReadLightInspectionData.ReadLightCompeleteListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.16
            final /* synthetic */ Context val$context;

            AnonymousClass16(Context context2) {
                context = context2;
            }

            @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
            public void complete(HashMap<Integer, List<Integer>> hashMap) {
                ScanBarcodeReplenishmentDialog.this.lightDataHashMap = hashMap;
                ScanBarcodeReplenishmentDialog.this.updataCount();
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }

            @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
            public void error(String str2) {
                ToastUitl.showShort(context, str2);
                ScanBarcodeReplenishmentDialog.this.closeLoadingDialog();
            }
        });
    }

    public void updataCount() {
        int i;
        CalculatedInventory calculatedInventory = new CalculatedInventory();
        Iterator<Integer> it = this.dataHashMap.keySet().iterator();
        while (it.hasNext()) {
            for (ReplenishData replenishData : this.dataHashMap.get(Integer.valueOf(it.next().intValue()))) {
                List<Integer> list = CalculatedInventory.getlightData(this.shelftMergeStateHashMap, this.lightDataHashMap, replenishData.shelf);
                if (list != null && list.size() > 0) {
                    Loger.writeLog("SET", "lightData.size=" + list.size());
                    ShelfCommodityInfo shelfCommodityInfo = this.shelfCommodityInfoHashMap.get(replenishData.strShelf);
                    int i2 = shelfCommodityInfo != null ? shelfCommodityInfo.length : 0;
                    Loger.writeLog("SET", "goodsLength=" + i2);
                    if (i2 == 0) {
                        i = -1;
                    } else if (list.size() == 6) {
                        i = calculatedInventory.getGoodsCount(this.context, replenishData.shelf, CalculatedInventory.shlelvsLength_6_list, Integer.valueOf(i2).intValue() * 10, list);
                    } else {
                        i = list.size() == 8 ? calculatedInventory.getGoodsCount(this.context, replenishData.shelf, CalculatedInventory.shlelvsLength_8_list, Integer.valueOf(i2).intValue() * 10, list) : -4;
                    }
                    Loger.writeLog("SET", "货道=" + replenishData.shelf + " 库存=" + i);
                } else {
                    Loger.writeLog("SET", "lightData == null");
                    i = -3;
                }
                if (i <= 0) {
                    replenishData.localCount = 0;
                    replenishData.state = i;
                } else {
                    replenishData.localCount = i;
                }
            }
        }
        this.myAdapter.notifyDataSetChanged();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return ScanBarcodeReplenishmentDialog.this.dataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_scan_replennishment_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_shelf = (TextView) view.findViewById(R.id.tv_shelf);
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.bt_replenish_reduce = (Button) view.findViewById(R.id.bt_replenish_reduce);
                viewHolder.tv_replenish = (TextView) view.findViewById(R.id.tv_replenish_count);
                viewHolder.bt_replenish_add = (Button) view.findViewById(R.id.bt_replenish_add);
                viewHolder.bt_count_reduce = (Button) view.findViewById(R.id.bt_count_reduce);
                viewHolder.tv_count = (TextView) view.findViewById(R.id.tv_count);
                viewHolder.bt_count_add = (Button) view.findViewById(R.id.bt_count_add);
                viewHolder.tv_error_info = (TextView) view.findViewById(R.id.tv_error_info);
                viewHolder.tv_batch = (TextView) view.findViewById(R.id.tv_batch);
                viewHolder.tv_lenth = (TextView) view.findViewById(R.id.tv_lenth);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            ReplenishData replenishData = (ReplenishData) ScanBarcodeReplenishmentDialog.this.dataList.get(i);
            if (replenishData.replenishCount == replenishData.targetReplenishCount && replenishData.localCount == replenishData.targetCount) {
                view.setBackgroundColor(-16724941);
            } else if (replenishData.isCurrentScanItem) {
                view.setBackgroundColor(-23296);
            } else if (i % 2 == 0) {
                view.setBackgroundColor(-1);
            } else {
                view.setBackgroundColor(-986625);
            }
            viewHolder.tv_shelf.setText(replenishData.strShelf);
            viewHolder.tv_name.setText(replenishData.goodsName);
            if (TextUtils.isEmpty(replenishData.goodsBatch)) {
                viewHolder.tv_batch.setVisibility(8);
            } else {
                viewHolder.tv_batch.setVisibility(0);
                viewHolder.tv_batch.setText(replenishData.goodsBatch);
            }
            viewHolder.tv_lenth.setText(String.valueOf(replenishData.goodsLength));
            viewHolder.tv_replenish.setText(String.valueOf(replenishData.replenishCount) + UsbFile.separator + String.valueOf(replenishData.targetReplenishCount));
            viewHolder.tv_count.setText(String.valueOf(replenishData.localCount) + UsbFile.separator + String.valueOf(replenishData.targetCount));
            if (replenishData.state < 0) {
                viewHolder.tv_error_info.setVisibility(0);
                viewHolder.tv_error_info.setText(CalculatedInventory.getErrorInfo(replenishData.state));
            } else {
                viewHolder.tv_error_info.setVisibility(8);
            }
            viewHolder.bt_replenish_reduce.setTag(replenishData);
            viewHolder.bt_replenish_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (((ReplenishData) view2.getTag()).replenishCount > 0) {
                        r2.replenishCount--;
                        r2.localCount--;
                    }
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.bt_replenish_add.setTag(replenishData);
            viewHolder.bt_replenish_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.MyAdapter.2
                AnonymousClass2() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                    replenishData2.replenishCount++;
                    replenishData2.localCount++;
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.bt_count_reduce.setTag(replenishData);
            viewHolder.bt_count_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.MyAdapter.3
                AnonymousClass3() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                    if (replenishData2.localCount > 0) {
                        replenishData2.localCount--;
                        replenishData2.isCountChange = true;
                    }
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.bt_count_add.setTag(replenishData);
            viewHolder.bt_count_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog.MyAdapter.4
                AnonymousClass4() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                    replenishData2.localCount++;
                    replenishData2.isCountChange = true;
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (((ReplenishData) view2.getTag()).replenishCount > 0) {
                    r2.replenishCount--;
                    r2.localCount--;
                }
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$MyAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                replenishData2.replenishCount++;
                replenishData2.localCount++;
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$MyAdapter$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                if (replenishData2.localCount > 0) {
                    replenishData2.localCount--;
                    replenishData2.isCountChange = true;
                }
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* renamed from: com.shj.setting.Dialog.ScanBarcodeReplenishmentDialog$MyAdapter$4 */
        /* loaded from: classes2.dex */
        class AnonymousClass4 implements View.OnClickListener {
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReplenishData replenishData2 = (ReplenishData) view2.getTag();
                replenishData2.localCount++;
                replenishData2.isCountChange = true;
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public Button bt_count_add;
            public Button bt_count_reduce;
            public Button bt_replenish_add;
            public Button bt_replenish_reduce;
            public TextView tv_batch;
            public TextView tv_count;
            public TextView tv_error_info;
            public TextView tv_lenth;
            public TextView tv_name;
            public TextView tv_replenish;
            public TextView tv_shelf;

            public ViewHolder() {
            }
        }
    }
}

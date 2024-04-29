package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.Goods;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.bean.CommodityInfo;
import com.shj.setting.bean.ShelfCommodityInfo;
import com.shj.setting.helper.CalculatedInventory;
import com.shj.setting.helper.CommodityInfoObtain;
import com.shj.setting.helper.ReadLightInspectionData;
import com.shj.setting.helper.ReadShelfMergeState;
import com.shj.setting.helper.UploadOperationRecord;
import com.xyshj.database.setting.AppSetting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CalculatedInventoryDialog extends Dialog {
    private static final String SHELF_GOODS_LENGTH = "shelf_goods_length";
    private static final String SHELF_LENGTH_6 = "shelf_length_6_";
    private static final String SHELF_LENGTH_8 = "shelf_length_8_";
    private Button bt_close;
    private Button bt_ok;
    private Button bt_regetdata;
    private Button bt_upload;
    private Context context;
    private int currentLayerNumber;
    private HashMap<Integer, List<CheckData>> dataHashMap;
    private List<CheckData> dataList;
    private EditText et_remarks;
    private String flag;
    private HashMap<Integer, String> goodsLengthHashMap;
    private Handler handler;
    private List<Button> layerButtonList;
    private HashMap<Integer, List<Integer>> lightDataHashMap;
    private ListView listView;
    private LinearLayout ll_item_name;
    private LinearLayout ll_machine_no;
    private LoadingDialog loadingDialog;
    private String machineId;
    private MyAdapter myAdapter;
    private ReadLightInspectionData readLightInspectionData;
    private ReadShelfMergeState readShelfMergeState;
    private HashMap<String, ShelfCommodityInfo> shelfCommodityInfoHashMap;
    private HashMap<Integer, Integer> shelftMergeStateHashMap;
    private SharedPreferences sp;
    private HashMap<Integer, Boolean> submitHashMap;
    private String userName;

    /* loaded from: classes2.dex */
    public static class CheckData {
        public int checkCount;
        public String goodsBatch;
        public int goodsLenth;
        public String goodsName;
        public int localCount;
        public int netCount;
        public int shelf;
        public int state;
        public String strShelf;
    }

    public CalculatedInventoryDialog(Context context, String str) {
        super(context, R.style.ad_style);
        this.lightDataHashMap = new HashMap<>();
        this.goodsLengthHashMap = new HashMap<>();
        this.shelftMergeStateHashMap = new HashMap<>();
        this.submitHashMap = new HashMap<>();
        this.shelfCommodityInfoHashMap = new HashMap<>();
        this.layerButtonList = new ArrayList();
        this.currentLayerNumber = 0;
        this.dataList = new ArrayList();
        this.dataHashMap = new HashMap<>();
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.7
            AnonymousClass7() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i;
                CalculatedInventory calculatedInventory = new CalculatedInventory();
                for (CheckData checkData : CalculatedInventoryDialog.this.dataList) {
                    List<Integer> list = CalculatedInventory.getlightData(CalculatedInventoryDialog.this.shelftMergeStateHashMap, CalculatedInventoryDialog.this.lightDataHashMap, checkData.shelf);
                    if (list == null || list.size() <= 0) {
                        CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, -3);
                    } else {
                        String str2 = (String) CalculatedInventoryDialog.this.goodsLengthHashMap.get(Integer.valueOf(checkData.shelf));
                        if (TextUtils.isEmpty(str2)) {
                            CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, -1);
                        } else {
                            if (list.size() == 6) {
                                i = calculatedInventory.getGoodsCount(CalculatedInventoryDialog.this.context, checkData.shelf, CalculatedInventory.shlelvsLength_6_list, Integer.valueOf(str2).intValue() * 10, list);
                            } else if (list.size() == 8) {
                                i = calculatedInventory.getGoodsCount(CalculatedInventoryDialog.this.context, checkData.shelf, CalculatedInventory.shlelvsLength_8_list, Integer.valueOf(str2).intValue() * 10, list);
                            } else {
                                ToastUitl.showShort(CalculatedInventoryDialog.this.context, checkData.strShelf + CalculatedInventoryDialog.this.context.getString(R.string.lab_shelf) + "光检个数暂不支持");
                                i = -4;
                            }
                            Loger.writeLog("SET", "货道=" + checkData.shelf + " 库存=" + i);
                            CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, i);
                        }
                    }
                }
                CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
                if (CalculatedInventoryDialog.this.loadingDialog != null) {
                    CalculatedInventoryDialog.this.loadingDialog.dismiss();
                }
            }
        };
        this.context = context;
        this.userName = str;
        this.sp = context.getSharedPreferences(SHELF_GOODS_LENGTH, 0);
        this.flag = CalculatedInventory.getFlag();
        this.machineId = AppSetting.getMachineId(context, null);
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_calculated_inventory);
        setCanceledOnTouchOutside(false);
        findView();
        setListener();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
        addLayerButton();
        addItemName();
        getShelfCommodityInfo();
    }

    public void showLoadingDialog(int i) {
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

    public void getShelfCommodityInfo() {
        showLoadingDialog(R.string.loading);
        new CommodityInfoObtain().getGoodsInfo(this.context, this.machineId, new CommodityInfoObtain.GetInfoCompleteListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void complete(HashMap<String, ShelfCommodityInfo> hashMap, HashMap<String, CommodityInfo> hashMap2) {
                CalculatedInventoryDialog.this.shelfCommodityInfoHashMap = hashMap;
                CalculatedInventoryDialog.this.setShelfLength();
                CalculatedInventoryDialog.this.closeLoadingDialog();
            }

            @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
            public void error(String str) {
                CalculatedInventoryDialog.this.closeLoadingDialog();
                ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements CommodityInfoObtain.GetInfoCompleteListener {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
        public void complete(HashMap<String, ShelfCommodityInfo> hashMap, HashMap<String, CommodityInfo> hashMap2) {
            CalculatedInventoryDialog.this.shelfCommodityInfoHashMap = hashMap;
            CalculatedInventoryDialog.this.setShelfLength();
            CalculatedInventoryDialog.this.closeLoadingDialog();
        }

        @Override // com.shj.setting.helper.CommodityInfoObtain.GetInfoCompleteListener
        public void error(String str) {
            CalculatedInventoryDialog.this.closeLoadingDialog();
            ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
        }
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
                this.ll_machine_no.addView(linearLayout, layoutParams);
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
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.2
                AnonymousClass2() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CalculatedInventoryDialog.this.currentLayerNumber = ((Integer) view.getTag()).intValue();
                    CalculatedInventoryDialog calculatedInventoryDialog = CalculatedInventoryDialog.this;
                    calculatedInventoryDialog.getGridItemData(calculatedInventoryDialog.currentLayerNumber);
                    CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
                    CalculatedInventoryDialog calculatedInventoryDialog2 = CalculatedInventoryDialog.this;
                    calculatedInventoryDialog2.setLayerButtonState(calculatedInventoryDialog2.currentLayerNumber);
                    CalculatedInventoryDialog.this.setShelfLength();
                }
            });
            linearLayout.addView(button, layoutParams2);
            this.layerButtonList.add(button);
        }
        this.layerButtonList.get(0).performClick();
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CalculatedInventoryDialog.this.currentLayerNumber = ((Integer) view.getTag()).intValue();
            CalculatedInventoryDialog calculatedInventoryDialog = CalculatedInventoryDialog.this;
            calculatedInventoryDialog.getGridItemData(calculatedInventoryDialog.currentLayerNumber);
            CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
            CalculatedInventoryDialog calculatedInventoryDialog2 = CalculatedInventoryDialog.this;
            calculatedInventoryDialog2.setLayerButtonState(calculatedInventoryDialog2.currentLayerNumber);
            CalculatedInventoryDialog.this.setShelfLength();
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

    public void getGridItemData(int i) {
        List<CheckData> list = this.dataHashMap.get(Integer.valueOf(i));
        this.dataList = list;
        if (list == null) {
            this.dataList = new ArrayList();
            this.dataHashMap.put(Integer.valueOf(i), this.dataList);
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(SettingActivity.getBasicMachineInfo().shelvesLayerMap.get(Integer.valueOf(i)));
            Collections.sort(arrayList);
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                CheckData checkData = new CheckData();
                checkData.shelf = ((Integer) arrayList.get(i2)).intValue();
                checkData.strShelf = String.format("%03d", arrayList.get(i2));
                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(checkData.shelf));
                if (shelfInfo != null) {
                    Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
                    if (goodsByCode != null) {
                        checkData.goodsName = goodsByCode.getName();
                    }
                    checkData.localCount = shelfInfo.getGoodsCount().intValue();
                    checkData.goodsBatch = shelfInfo.getGoodsbatchnumber();
                }
                this.dataList.add(checkData);
            }
        }
    }

    private void findView() {
        this.ll_machine_no = (LinearLayout) findViewById(R.id.ll_machine_no);
        this.ll_item_name = (LinearLayout) findViewById(R.id.ll_item_name);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_upload = (Button) findViewById(R.id.bt_upload);
        this.bt_close = (Button) findViewById(R.id.bt_cancel);
        this.bt_regetdata = (Button) findViewById(R.id.bt_regetdata);
        this.et_remarks = (EditText) findViewById(R.id.et_remarks);
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (CheckData checkData : CalculatedInventoryDialog.this.dataList) {
                CalculatedInventoryDialog.this.goodsLengthHashMap.put(Integer.valueOf(checkData.shelf), String.valueOf(checkData.goodsLenth));
            }
            CalculatedInventoryDialog.this.showLoadingDialog(R.string.calculateing);
            if (CalculatedInventoryDialog.this.readShelfMergeState == null) {
                CalculatedInventoryDialog.this.readShelfMergeState = new ReadShelfMergeState();
            }
            CalculatedInventoryDialog.this.readShelfMergeState.readMergeState(CalculatedInventoryDialog.this.context, new ReadShelfMergeState.ReadCompleteListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.3.1
                AnonymousClass1() {
                }

                @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
                public void complete(HashMap<Integer, Integer> hashMap) {
                    CalculatedInventoryDialog.this.shelftMergeStateHashMap = hashMap;
                    if (CalculatedInventoryDialog.this.readLightInspectionData == null) {
                        CalculatedInventoryDialog.this.readLightInspectionData = new ReadLightInspectionData();
                    }
                    CalculatedInventoryDialog.this.readLightInspectionData.readLightData(CalculatedInventoryDialog.this.context, new ReadLightInspectionData.ReadLightCompeleteListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.3.1.1
                        C00531() {
                        }

                        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                        public void complete(HashMap<Integer, List<Integer>> hashMap2) {
                            CalculatedInventoryDialog.this.lightDataHashMap = hashMap2;
                            CalculatedInventoryDialog.this.setStockCount();
                        }

                        @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                        public void error(String str) {
                            ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                        }
                    });
                }

                /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$3$1$1 */
                /* loaded from: classes2.dex */
                class C00531 implements ReadLightInspectionData.ReadLightCompeleteListener {
                    C00531() {
                    }

                    @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                    public void complete(HashMap<Integer, List<Integer>> hashMap2) {
                        CalculatedInventoryDialog.this.lightDataHashMap = hashMap2;
                        CalculatedInventoryDialog.this.setStockCount();
                    }

                    @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                    public void error(String str) {
                        ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                    }
                }

                @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
                public void error(int i, String str) {
                    CalculatedInventoryDialog.this.closeLoadingDialog();
                    if (i == 0) {
                        ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                    } else if (i == 1) {
                        new TipDialog(CalculatedInventoryDialog.this.context, 0, str, CalculatedInventoryDialog.this.context.getString(R.string.button_ok), "").show();
                    }
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$3$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements ReadShelfMergeState.ReadCompleteListener {
            AnonymousClass1() {
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void complete(HashMap<Integer, Integer> hashMap) {
                CalculatedInventoryDialog.this.shelftMergeStateHashMap = hashMap;
                if (CalculatedInventoryDialog.this.readLightInspectionData == null) {
                    CalculatedInventoryDialog.this.readLightInspectionData = new ReadLightInspectionData();
                }
                CalculatedInventoryDialog.this.readLightInspectionData.readLightData(CalculatedInventoryDialog.this.context, new ReadLightInspectionData.ReadLightCompeleteListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.3.1.1
                    C00531() {
                    }

                    @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                    public void complete(HashMap<Integer, List<Integer>> hashMap2) {
                        CalculatedInventoryDialog.this.lightDataHashMap = hashMap2;
                        CalculatedInventoryDialog.this.setStockCount();
                    }

                    @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                    public void error(String str) {
                        ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                    }
                });
            }

            /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$3$1$1 */
            /* loaded from: classes2.dex */
            class C00531 implements ReadLightInspectionData.ReadLightCompeleteListener {
                C00531() {
                }

                @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                public void complete(HashMap<Integer, List<Integer>> hashMap2) {
                    CalculatedInventoryDialog.this.lightDataHashMap = hashMap2;
                    CalculatedInventoryDialog.this.setStockCount();
                }

                @Override // com.shj.setting.helper.ReadLightInspectionData.ReadLightCompeleteListener
                public void error(String str) {
                    ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                }
            }

            @Override // com.shj.setting.helper.ReadShelfMergeState.ReadCompleteListener
            public void error(int i, String str) {
                CalculatedInventoryDialog.this.closeLoadingDialog();
                if (i == 0) {
                    ToastUitl.showShort(CalculatedInventoryDialog.this.context, str);
                } else if (i == 1) {
                    new TipDialog(CalculatedInventoryDialog.this.context, 0, str, CalculatedInventoryDialog.this.context.getString(R.string.button_ok), "").show();
                }
            }
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new AnonymousClass3());
        this.bt_upload.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CalculatedInventoryDialog.this.uploadShelfCount();
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
                ArrayList arrayList = new ArrayList();
                Iterator<Integer> it = list.iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    if (CalculatedInventoryDialog.this.submitHashMap.get(Integer.valueOf(intValue)) == null || !((Boolean) CalculatedInventoryDialog.this.submitHashMap.get(Integer.valueOf(intValue))).booleanValue()) {
                        arrayList.add(Integer.valueOf(intValue));
                    }
                }
                if (arrayList.size() > 0) {
                    String str = "还有";
                    for (int i = 0; i < arrayList.size(); i++) {
                        str = i == 0 ? str + arrayList.get(i) : str + "," + arrayList.get(i);
                    }
                    TipDialog tipDialog = new TipDialog(CalculatedInventoryDialog.this.context, 0, str + "层没有提交,确定关闭吗？", CalculatedInventoryDialog.this.context.getString(R.string.button_cancel), CalculatedInventoryDialog.this.context.getString(R.string.button_ok));
                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.5.1
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
                            CalculatedInventoryDialog.this.dismiss();
                        }
                    });
                    tipDialog2.show();
                    return;
                }
                CalculatedInventoryDialog.this.dismiss();
            }

            /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$5$1 */
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
                    CalculatedInventoryDialog.this.dismiss();
                }
            }
        });
        this.bt_regetdata.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                for (CheckData checkData : CalculatedInventoryDialog.this.dataList) {
                    checkData.goodsLenth = 0;
                    checkData.netCount = 0;
                }
                CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
                CalculatedInventoryDialog.this.getShelfCommodityInfo();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CalculatedInventoryDialog.this.uploadShelfCount();
        }
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
            ArrayList arrayList = new ArrayList();
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                if (CalculatedInventoryDialog.this.submitHashMap.get(Integer.valueOf(intValue)) == null || !((Boolean) CalculatedInventoryDialog.this.submitHashMap.get(Integer.valueOf(intValue))).booleanValue()) {
                    arrayList.add(Integer.valueOf(intValue));
                }
            }
            if (arrayList.size() > 0) {
                String str = "还有";
                for (int i = 0; i < arrayList.size(); i++) {
                    str = i == 0 ? str + arrayList.get(i) : str + "," + arrayList.get(i);
                }
                TipDialog tipDialog2 = new TipDialog(CalculatedInventoryDialog.this.context, 0, str + "层没有提交,确定关闭吗？", CalculatedInventoryDialog.this.context.getString(R.string.button_cancel), CalculatedInventoryDialog.this.context.getString(R.string.button_ok));
                tipDialog2.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.5.1
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
                        CalculatedInventoryDialog.this.dismiss();
                    }
                });
                tipDialog22.show();
                return;
            }
            CalculatedInventoryDialog.this.dismiss();
        }

        /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$5$1 */
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
                CalculatedInventoryDialog.this.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (CheckData checkData : CalculatedInventoryDialog.this.dataList) {
                checkData.goodsLenth = 0;
                checkData.netCount = 0;
            }
            CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
            CalculatedInventoryDialog.this.getShelfCommodityInfo();
        }
    }

    private void addItemName() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_check_count_item_name, (ViewGroup) null);
        inflate.setBackgroundColor(-986625);
        this.ll_item_name.addView(inflate);
    }

    public void setStockCount() {
        if (this.dataList != null && this.lightDataHashMap.size() > 0) {
            this.handler.sendEmptyMessageDelayed(0, 2000L);
        } else {
            closeLoadingDialog();
        }
    }

    public void setShelfLength() {
        if (this.shelfCommodityInfoHashMap.size() > 0) {
            for (CheckData checkData : this.dataList) {
                ShelfCommodityInfo shelfCommodityInfo = this.shelfCommodityInfoHashMap.get(checkData.strShelf);
                if (shelfCommodityInfo != null) {
                    Loger.writeLog("SET", "shelfCommodityInfo.length =" + shelfCommodityInfo.length);
                    checkData.goodsLenth = shelfCommodityInfo.length;
                    checkData.netCount = shelfCommodityInfo.stockCount;
                }
            }
            this.myAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 extends Handler {
        AnonymousClass7() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i;
            CalculatedInventory calculatedInventory = new CalculatedInventory();
            for (CheckData checkData : CalculatedInventoryDialog.this.dataList) {
                List<Integer> list = CalculatedInventory.getlightData(CalculatedInventoryDialog.this.shelftMergeStateHashMap, CalculatedInventoryDialog.this.lightDataHashMap, checkData.shelf);
                if (list == null || list.size() <= 0) {
                    CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, -3);
                } else {
                    String str2 = (String) CalculatedInventoryDialog.this.goodsLengthHashMap.get(Integer.valueOf(checkData.shelf));
                    if (TextUtils.isEmpty(str2)) {
                        CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, -1);
                    } else {
                        if (list.size() == 6) {
                            i = calculatedInventory.getGoodsCount(CalculatedInventoryDialog.this.context, checkData.shelf, CalculatedInventory.shlelvsLength_6_list, Integer.valueOf(str2).intValue() * 10, list);
                        } else if (list.size() == 8) {
                            i = calculatedInventory.getGoodsCount(CalculatedInventoryDialog.this.context, checkData.shelf, CalculatedInventory.shlelvsLength_8_list, Integer.valueOf(str2).intValue() * 10, list);
                        } else {
                            ToastUitl.showShort(CalculatedInventoryDialog.this.context, checkData.strShelf + CalculatedInventoryDialog.this.context.getString(R.string.lab_shelf) + "光检个数暂不支持");
                            i = -4;
                        }
                        Loger.writeLog("SET", "货道=" + checkData.shelf + " 库存=" + i);
                        CalculatedInventoryDialog.this.updataCheckDataList(checkData.shelf, i);
                    }
                }
            }
            CalculatedInventoryDialog.this.myAdapter.notifyDataSetChanged();
            if (CalculatedInventoryDialog.this.loadingDialog != null) {
                CalculatedInventoryDialog.this.loadingDialog.dismiss();
            }
        }
    }

    public void updataCheckDataList(int i, int i2) {
        for (CheckData checkData : this.dataList) {
            if (checkData.shelf == i) {
                if (i2 < 0) {
                    checkData.checkCount = 0;
                    checkData.state = i2;
                } else {
                    checkData.checkCount = i2;
                    checkData.state = 0;
                }
            }
        }
    }

    private void checkLayerData() {
        boolean z;
        for (CheckData checkData : this.dataList) {
            if (checkData.localCount != checkData.netCount || checkData.localCount != checkData.checkCount) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            for (Button button : this.layerButtonList) {
                if (this.currentLayerNumber == ((Integer) button.getTag()).intValue()) {
                    button.setBackgroundResource(R.drawable.selector_button_green2);
                }
            }
        }
    }

    public void uploadShelfCount() {
        ShelfInfo shelfInfo;
        this.submitHashMap.put(Integer.valueOf(this.currentLayerNumber), true);
        HashMap hashMap = new HashMap();
        hashMap.put("jqbh", this.machineId);
        hashMap.put("pdr", this.userName);
        hashMap.put("lx", 1);
        hashMap.put("bz", this.flag);
        hashMap.put("remarks", this.et_remarks.getText().toString());
        hashMap.put("sbsj", CalculatedInventory.getTime());
        ArrayList arrayList = new ArrayList();
        for (CheckData checkData : this.dataList) {
            if (checkData.checkCount >= 0 && (shelfInfo = Shj.getShelfInfo(Integer.valueOf(checkData.shelf))) != null && shelfInfo.getGoodsCount().intValue() != checkData.checkCount) {
                ShjManager.setShelfGoodsCount(checkData.shelf, checkData.checkCount);
            }
            arrayList.add(CalculatedInventory.getJsonString(checkData.shelf, checkData.checkCount));
        }
        hashMap.put("hdbhs", arrayList);
        JSONObject jSONObject = new JSONObject(hashMap);
        Loger.writeLog("SET", "report shelf info=" + jSONObject.toString());
        CalculatedInventory.uploadShelfCountInfo(this.context, jSONObject, true);
        checkLayerData();
        UploadOperationRecord.upload(SettingActivity.getUserName(), "库存盘点", "盘点了第" + this.currentLayerNumber + "层");
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
            return CalculatedInventoryDialog.this.dataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_check_count_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_shelf = (TextView) view.findViewById(R.id.tv_shelf);
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tv_lenth = (TextView) view.findViewById(R.id.tv_lenth);
                viewHolder.tv_local_count = (TextView) view.findViewById(R.id.tv_local_count);
                viewHolder.tv_net_count = (TextView) view.findViewById(R.id.tv_net_count);
                viewHolder.tv_check_count = (TextView) view.findViewById(R.id.tv_check_count);
                viewHolder.bt_reduce = (Button) view.findViewById(R.id.bt_reduce);
                viewHolder.bt_add = (Button) view.findViewById(R.id.bt_add);
                viewHolder.tv_error_info = (TextView) view.findViewById(R.id.tv_error_info);
                viewHolder.tv_batch = (TextView) view.findViewById(R.id.tv_batch);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            CheckData checkData = (CheckData) CalculatedInventoryDialog.this.dataList.get(i);
            if (checkData.localCount == checkData.netCount && checkData.localCount == checkData.checkCount) {
                view.setBackgroundColor(-16724941);
            } else if (i % 2 == 0) {
                view.setBackgroundColor(-1);
            } else {
                view.setBackgroundColor(-986625);
            }
            viewHolder.tv_shelf.setText(checkData.strShelf);
            viewHolder.tv_name.setText(checkData.goodsName);
            if (TextUtils.isEmpty(checkData.goodsBatch)) {
                viewHolder.tv_batch.setVisibility(8);
            } else {
                viewHolder.tv_batch.setVisibility(0);
                viewHolder.tv_batch.setText(checkData.goodsBatch);
            }
            viewHolder.tv_lenth.setText(String.valueOf(checkData.goodsLenth));
            viewHolder.tv_local_count.setText(String.valueOf(checkData.localCount));
            viewHolder.tv_net_count.setText(String.valueOf(checkData.netCount));
            viewHolder.tv_check_count.setText(String.valueOf(checkData.checkCount));
            if (checkData.state == 0 || checkData.checkCount > 0) {
                viewHolder.tv_error_info.setVisibility(8);
            } else {
                viewHolder.tv_error_info.setVisibility(0);
                viewHolder.tv_error_info.setText(CalculatedInventory.getErrorInfo(checkData.state));
            }
            viewHolder.bt_reduce.setTag(checkData);
            viewHolder.bt_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (((CheckData) view2.getTag()).checkCount > 0) {
                        r2.checkCount--;
                    }
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.bt_add.setTag(checkData);
            viewHolder.bt_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CalculatedInventoryDialog.MyAdapter.2
                AnonymousClass2() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    CheckData checkData2 = (CheckData) view2.getTag();
                    if (checkData2.checkCount < 100) {
                        checkData2.checkCount++;
                    }
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (((CheckData) view2.getTag()).checkCount > 0) {
                    r2.checkCount--;
                }
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* renamed from: com.shj.setting.Dialog.CalculatedInventoryDialog$MyAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CheckData checkData2 = (CheckData) view2.getTag();
                if (checkData2.checkCount < 100) {
                    checkData2.checkCount++;
                }
                MyAdapter.this.notifyDataSetChanged();
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public Button bt_add;
            public Button bt_reduce;
            public TextView tv_batch;
            public TextView tv_check_count;
            public TextView tv_error_info;
            public TextView tv_lenth;
            public TextView tv_local_count;
            public TextView tv_name;
            public TextView tv_net_count;
            public TextView tv_shelf;

            public ViewHolder() {
            }
        }
    }
}

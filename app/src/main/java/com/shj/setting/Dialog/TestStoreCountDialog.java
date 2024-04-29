package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.helper.OverShelfData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TestStoreCountDialog extends Dialog {
    private static final int UNIT = 10;
    private Button bt_check_data;
    private Button bt_close;
    private Context context;
    private List<CheckBox> errorRadioButtonList;
    private EditText et_goods_lenth;
    private EditText et_light_count;
    private EditText et_shelf_lenth;
    private int goodsLenth;
    private int lightCount;
    private LinearLayout ll_error;
    private LinearLayout ll_goods;
    private LinearLayout ll_light;
    private List<OverShelfData> overShelfDataList;
    private List<CheckBox> radioButtonList;
    private int shelfLength;
    private TextView tv_line;
    private TextView tv_store_count;

    public TestStoreCountDialog(Context context) {
        super(context, R.style.loading_style);
        this.radioButtonList = new ArrayList();
        this.errorRadioButtonList = new ArrayList();
        this.overShelfDataList = new ArrayList();
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_test_store_count);
        setCanceledOnTouchOutside(false);
        findView();
        setListener();
    }

    private void findView() {
        this.et_shelf_lenth = (EditText) findViewById(R.id.et_shelf_lenth);
        this.et_goods_lenth = (EditText) findViewById(R.id.et_goods_lenth);
        this.et_light_count = (EditText) findViewById(R.id.et_light_count);
        this.bt_check_data = (Button) findViewById(R.id.bt_check_data);
        this.tv_store_count = (TextView) findViewById(R.id.tv_store_count);
        this.tv_line = (TextView) findViewById(R.id.tv_line);
        this.ll_light = (LinearLayout) findViewById(R.id.ll_light);
        this.ll_goods = (LinearLayout) findViewById(R.id.ll_goods);
        this.bt_close = (Button) findViewById(R.id.bt_close);
        this.ll_error = (LinearLayout) findViewById(R.id.ll_error);
    }

    /* renamed from: com.shj.setting.Dialog.TestStoreCountDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = TestStoreCountDialog.this.et_shelf_lenth.getText().toString();
            String obj2 = TestStoreCountDialog.this.et_light_count.getText().toString();
            String obj3 = TestStoreCountDialog.this.et_goods_lenth.getText().toString();
            if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2) || TextUtils.isEmpty(obj3)) {
                ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                return;
            }
            TestStoreCountDialog.this.shelfLength = Integer.valueOf(obj).intValue();
            TestStoreCountDialog.this.lightCount = Integer.valueOf(obj2).intValue();
            TestStoreCountDialog.this.goodsLenth = Integer.valueOf(obj3).intValue();
            if (TestStoreCountDialog.this.shelfLength < 10 || TestStoreCountDialog.this.shelfLength > 800) {
                ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                return;
            }
            if (TestStoreCountDialog.this.shelfLength / (TestStoreCountDialog.this.lightCount + 1) > TestStoreCountDialog.this.goodsLenth) {
                ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                return;
            }
            if (TestStoreCountDialog.this.lightCount < 0 || TestStoreCountDialog.this.lightCount > 8) {
                ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
            }
            TestStoreCountDialog.this.ll_goods.removeAllViews();
            TestStoreCountDialog testStoreCountDialog = TestStoreCountDialog.this;
            testStoreCountDialog.addLine(testStoreCountDialog.shelfLength);
            TestStoreCountDialog testStoreCountDialog2 = TestStoreCountDialog.this;
            testStoreCountDialog2.addLight(testStoreCountDialog2.shelfLength, TestStoreCountDialog.this.lightCount);
            TestStoreCountDialog.this.getOverShelfDataList();
        }
    }

    private void setListener() {
        this.bt_check_data.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TestStoreCountDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = TestStoreCountDialog.this.et_shelf_lenth.getText().toString();
                String obj2 = TestStoreCountDialog.this.et_light_count.getText().toString();
                String obj3 = TestStoreCountDialog.this.et_goods_lenth.getText().toString();
                if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2) || TextUtils.isEmpty(obj3)) {
                    ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                    return;
                }
                TestStoreCountDialog.this.shelfLength = Integer.valueOf(obj).intValue();
                TestStoreCountDialog.this.lightCount = Integer.valueOf(obj2).intValue();
                TestStoreCountDialog.this.goodsLenth = Integer.valueOf(obj3).intValue();
                if (TestStoreCountDialog.this.shelfLength < 10 || TestStoreCountDialog.this.shelfLength > 800) {
                    ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                    return;
                }
                if (TestStoreCountDialog.this.shelfLength / (TestStoreCountDialog.this.lightCount + 1) > TestStoreCountDialog.this.goodsLenth) {
                    ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                    return;
                }
                if (TestStoreCountDialog.this.lightCount < 0 || TestStoreCountDialog.this.lightCount > 8) {
                    ToastUitl.showShort(TestStoreCountDialog.this.context, "输入数据有误");
                }
                TestStoreCountDialog.this.ll_goods.removeAllViews();
                TestStoreCountDialog testStoreCountDialog = TestStoreCountDialog.this;
                testStoreCountDialog.addLine(testStoreCountDialog.shelfLength);
                TestStoreCountDialog testStoreCountDialog2 = TestStoreCountDialog.this;
                testStoreCountDialog2.addLight(testStoreCountDialog2.shelfLength, TestStoreCountDialog.this.lightCount);
                TestStoreCountDialog.this.getOverShelfDataList();
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TestStoreCountDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TestStoreCountDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.TestStoreCountDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TestStoreCountDialog.this.dismiss();
        }
    }

    public void addLine(int i) {
        this.tv_line.setWidth(i);
    }

    public void addLight(int i, int i2) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        int i3 = i / (i2 + 1);
        layoutParams.leftMargin = i3 - 20;
        layoutParams.width = 40;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = i3 - 40;
        layoutParams2.width = 40;
        this.ll_light.removeAllViews();
        this.ll_error.removeAllViews();
        this.radioButtonList.clear();
        this.errorRadioButtonList.clear();
        for (int i4 = 0; i4 < i2; i4++) {
            CheckBox checkBox = new CheckBox(this.context);
            checkBox.setWidth(40);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.TestStoreCountDialog.3
                AnonymousClass3() {
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    TestStoreCountDialog.this.addGoods();
                }
            });
            this.radioButtonList.add(checkBox);
            if (i4 == 0) {
                this.ll_light.addView(checkBox, layoutParams);
            } else {
                this.ll_light.addView(checkBox, layoutParams2);
            }
            CheckBox checkBox2 = new CheckBox(this.context);
            checkBox2.setWidth(40);
            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.TestStoreCountDialog.4
                AnonymousClass4() {
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    TestStoreCountDialog.this.addGoods();
                }
            });
            this.errorRadioButtonList.add(checkBox2);
            if (i4 == 0) {
                this.ll_error.addView(checkBox2, layoutParams);
            } else {
                this.ll_error.addView(checkBox2, layoutParams2);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.TestStoreCountDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements CompoundButton.OnCheckedChangeListener {
        AnonymousClass3() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            TestStoreCountDialog.this.addGoods();
        }
    }

    /* renamed from: com.shj.setting.Dialog.TestStoreCountDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements CompoundButton.OnCheckedChangeListener {
        AnonymousClass4() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            TestStoreCountDialog.this.addGoods();
        }
    }

    public void getOverShelfDataList() {
        this.overShelfDataList.clear();
        int i = (this.shelfLength / this.goodsLenth) + 1;
        int i2 = 0;
        int i3 = 0;
        while (i3 < i) {
            OverShelfData overShelfData = new OverShelfData();
            overShelfData.goodsIndex = i3;
            overShelfData.lightIndexList = new ArrayList();
            overShelfData.startPosition = this.goodsLenth * i3;
            i3++;
            overShelfData.endPosition = this.goodsLenth * i3;
            this.overShelfDataList.add(overShelfData);
        }
        int i4 = this.shelfLength / (this.lightCount + 1);
        while (i2 < this.lightCount) {
            int i5 = i2 + 1;
            int i6 = i5 * i4;
            Iterator<OverShelfData> it = this.overShelfDataList.iterator();
            while (true) {
                if (it.hasNext()) {
                    OverShelfData next = it.next();
                    if (isIn(i6, next)) {
                        next.lightIndexList.add(Integer.valueOf(i2));
                        break;
                    }
                }
            }
            i2 = i5;
        }
    }

    private boolean isIn(int i, OverShelfData overShelfData) {
        return i > overShelfData.startPosition && i < overShelfData.endPosition;
    }

    private int getGoodsCount(int i) {
        boolean z;
        if (this.goodsLenth < (this.shelfLength / (this.lightCount + 1)) * (i + 1)) {
            ToastUitl.showShort(this.context, "光检损坏过多不可用");
            return 0;
        }
        Iterator<OverShelfData> it = this.overShelfDataList.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            Iterator<Integer> it2 = it.next().lightIndexList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    z = false;
                    break;
                }
                int intValue = it2.next().intValue();
                if (!this.errorRadioButtonList.get(intValue).isChecked() && this.radioButtonList.get(intValue).isChecked()) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                break;
            }
            i2++;
        }
        return i2;
    }

    public void addGoods() {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.errorRadioButtonList.size(); i3++) {
            if (this.errorRadioButtonList.get(i3).isChecked()) {
                i2++;
                if (i2 > i) {
                    i = i2;
                }
            } else {
                i2 = 0;
            }
        }
        int goodsCount = getGoodsCount(i);
        this.tv_store_count.setText(String.valueOf(goodsCount));
        this.ll_goods.removeAllViews();
        for (int i4 = 0; i4 < goodsCount; i4++) {
            TextView textView = new TextView(this.context);
            textView.setText(String.valueOf(i4));
            textView.setWidth(this.goodsLenth);
            textView.setTextColor(-1);
            textView.setGravity(17);
            textView.setBackgroundColor(-12303292);
            this.ll_goods.addView(textView);
        }
    }

    private int getHaveErrorLightGoodsCount(int i) {
        int i2 = this.goodsLenth;
        int i3 = (this.shelfLength / (this.lightCount + 1)) * (i + 1);
        if (i2 < i3) {
            ToastUitl.showShort(this.context, "光检损坏过多不可用");
            return 0;
        }
        int i4 = 0;
        for (int i5 = 0; i5 < this.radioButtonList.size(); i5++) {
            CheckBox checkBox = this.radioButtonList.get(i5);
            CheckBox checkBox2 = this.errorRadioButtonList.get(i5);
            if (!checkBox.isChecked() && !checkBox2.isChecked()) {
                break;
            }
            i4++;
        }
        if (i4 <= 0) {
            return 0;
        }
        int i6 = 0;
        for (int i7 = 0; i7 < 20; i7++) {
            i6++;
            if (i4 <= (this.goodsLenth * i6) / (this.shelfLength / (this.lightCount + 1))) {
                break;
            }
        }
        return i6;
    }
}

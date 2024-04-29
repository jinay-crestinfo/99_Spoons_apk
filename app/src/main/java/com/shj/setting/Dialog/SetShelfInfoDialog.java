package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.apache.commons.lang3.ClassUtils;

/* loaded from: classes2.dex */
public class SetShelfInfoDialog extends Dialog {
    private Button bt_add;
    private Button bt_bank_sync;
    private Button bt_close;
    private Button bt_full_goods;
    private Button bt_layer_sync;
    private Button bt_reduce;
    private Button bt_save;
    private Context context;
    private EditText et_goodscode;
    private EditText et_price;
    private GoodsInfoSettingListering goodsInfoSettingListering;
    DownloadGoodsInfoListener ll;
    private int shelf;
    private String symbol;
    private EditText tv_input_count;
    private TextView tv_name;
    private TextView tv_shelf;

    /* loaded from: classes2.dex */
    public interface DownloadGoodsInfoListener {
        void onNeedDownload(String str);
    }

    /* loaded from: classes2.dex */
    public interface GoodsInfoSettingListering {
        public static final int setType_bank = 2;
        public static final int setType_layer = 1;
        public static final int setType_sigle = 0;

        void setInfo(int i, String str, int i2, int i3, int i4);
    }

    public SetShelfInfoDialog(Context context, int i, String str) {
        super(context, R.style.translucent_dialog_style);
        this.context = context;
        this.shelf = i;
        this.symbol = str;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_set_shelf_info_dialog);
        findView();
        setListener();
        showContent();
    }

    private void findView() {
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_shelf = (TextView) findViewById(R.id.tv_shelf);
        this.bt_save = (Button) findViewById(R.id.bt_save);
        this.et_price = (EditText) findViewById(R.id.et_price);
        this.et_goodscode = (EditText) findViewById(R.id.et_goodscode);
        this.bt_add = (Button) findViewById(R.id.bt_add);
        this.bt_reduce = (Button) findViewById(R.id.bt_reduce);
        this.bt_full_goods = (Button) findViewById(R.id.bt_full_goods);
        this.tv_input_count = (EditText) findViewById(R.id.tv_input_count);
        this.bt_close = (Button) findViewById(R.id.bt_close);
        this.bt_layer_sync = (Button) findViewById(R.id.bt_layer_sync);
        this.bt_bank_sync = (Button) findViewById(R.id.bt_bank_sync);
    }

    public void setDownloadGoodsInfoListenere(DownloadGoodsInfoListener downloadGoodsInfoListener) {
        this.ll = downloadGoodsInfoListener;
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SetShelfInfoDialog.this.ll != null) {
                SetShelfInfoDialog.this.ll.onNeedDownload(SetShelfInfoDialog.this.et_goodscode.getText().toString());
            }
        }
    }

    private void setListener() {
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SetShelfInfoDialog.this.ll != null) {
                    SetShelfInfoDialog.this.ll.onNeedDownload(SetShelfInfoDialog.this.et_goodscode.getText().toString());
                }
            }
        });
        this.bt_save.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetShelfInfoDialog.this.setShelfInfo(0);
                SetShelfInfoDialog.this.dismiss();
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetShelfInfoDialog.this.dismiss();
            }
        });
        this.bt_layer_sync.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetShelfInfoDialog.this.setShelfInfo(1);
                SetShelfInfoDialog.this.dismiss();
            }
        });
        this.bt_bank_sync.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetShelfInfoDialog.this.setShelfInfo(2);
                SetShelfInfoDialog.this.dismiss();
            }
        });
        this.bt_full_goods.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(Shj.getShelfInfo(Integer.valueOf(SetShelfInfoDialog.this.shelf)).getCapacity()));
            }
        });
        this.bt_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = SetShelfInfoDialog.this.tv_input_count.getText().toString();
                int intValue = Shj.getShelfInfo(Integer.valueOf(SetShelfInfoDialog.this.shelf)).getCapacity().intValue();
                int intValue2 = Integer.valueOf(obj).intValue();
                if (intValue2 < intValue) {
                    SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(intValue2 + 1));
                } else {
                    ToastUitl.showLong(SetShelfInfoDialog.this.context, R.string.to_full_capacity);
                }
            }
        });
        this.bt_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SetShelfInfoDialog.8
            AnonymousClass8() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int intValue = Integer.valueOf(SetShelfInfoDialog.this.tv_input_count.getText().toString()).intValue();
                if (intValue > 0) {
                    SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(intValue - 1));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SetShelfInfoDialog.this.setShelfInfo(0);
            SetShelfInfoDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SetShelfInfoDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SetShelfInfoDialog.this.setShelfInfo(1);
            SetShelfInfoDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SetShelfInfoDialog.this.setShelfInfo(2);
            SetShelfInfoDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(Shj.getShelfInfo(Integer.valueOf(SetShelfInfoDialog.this.shelf)).getCapacity()));
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = SetShelfInfoDialog.this.tv_input_count.getText().toString();
            int intValue = Shj.getShelfInfo(Integer.valueOf(SetShelfInfoDialog.this.shelf)).getCapacity().intValue();
            int intValue2 = Integer.valueOf(obj).intValue();
            if (intValue2 < intValue) {
                SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(intValue2 + 1));
            } else {
                ToastUitl.showLong(SetShelfInfoDialog.this.context, R.string.to_full_capacity);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SetShelfInfoDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int intValue = Integer.valueOf(SetShelfInfoDialog.this.tv_input_count.getText().toString()).intValue();
            if (intValue > 0) {
                SetShelfInfoDialog.this.tv_input_count.setText(String.valueOf(intValue - 1));
            }
        }
    }

    public void setShelfInfo(int i) {
        String obj = this.et_goodscode.getText().toString();
        String obj2 = this.et_price.getText().toString();
        String obj3 = this.tv_input_count.getText().toString();
        if (!isCorrectInputData(obj, obj2, obj3)) {
            ToastUitl.showShort(this.context, R.string.not_input_tip);
        } else if (this.goodsInfoSettingListering != null) {
            this.goodsInfoSettingListering.setInfo(this.shelf, obj, Math.round(Float.valueOf(obj2).floatValue() * 100.0f), Integer.valueOf(obj3).intValue(), i);
        }
    }

    private boolean isCorrectInputData(String str, String str2, String str3) {
        return (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) ? false : true;
    }

    private void showContent() {
        this.tv_shelf.setText(String.format("%03d", Integer.valueOf(this.shelf)));
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(this.shelf));
        if (shelfInfo != null) {
            this.tv_name.setText(shelfInfo.getGoodsName());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            double intValue = shelfInfo.getPrice().intValue();
            Double.isNaN(intValue);
            String format = decimalFormat.format(intValue / 100.0d);
            this.et_price.setText(format);
            this.et_price.setSelection(format.length());
            this.tv_input_count.setText(String.valueOf(shelfInfo.getGoodsCount()));
            this.et_goodscode.setText(shelfInfo.getGoodsCode());
        }
    }

    public void setGoodsInfoSettingListering(GoodsInfoSettingListering goodsInfoSettingListering) {
        this.goodsInfoSettingListering = goodsInfoSettingListering;
    }
}

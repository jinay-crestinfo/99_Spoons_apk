package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.Goods;
import com.shj.setting.R;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.apache.commons.lang3.ClassUtils;

/* loaded from: classes2.dex */
public class GoodDetailQrcodeDialog extends Dialog {
    private Button button_close;
    private Context context;
    private Goods goods;
    private ImageView iv_goods_image;
    private String symbol;
    private TextView tv_available_stock_value;
    private TextView tv_msg;
    private TextView tv_name;
    private TextView tv_price;

    public GoodDetailQrcodeDialog(Context context, Goods goods, String str) {
        super(context, R.style.translucent_dialog_style);
        this.context = context;
        this.goods = goods;
        this.symbol = str;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_dialog_goods_detail_qrcode);
        this.iv_goods_image = (ImageView) findViewById(R.id.iv_goods_image);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.tv_available_stock_value = (TextView) findViewById(R.id.tv_available_stock_value);
        this.tv_price = (TextView) findViewById(R.id.tv_price);
        this.button_close = (Button) findViewById(R.id.button_close);
        updateDetailInfo(this.goods);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    public void updateDetailInfo(Goods goods) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        double price = goods.getPrice();
        Double.isNaN(price);
        String format = decimalFormat.format(price / 100.0d);
        this.tv_price.setText(this.symbol + format);
        Bitmap goodsImage = ShjManager.getGoodsManager().getGoodsImage(goods.getCode(), true);
        this.tv_available_stock_value.setText(String.valueOf(goods.getCount()));
        this.iv_goods_image.setImageBitmap(goodsImage);
        this.tv_msg.setText(goods.getDescript());
        this.tv_name.setText(goods.getName());
    }

    /* renamed from: com.shj.setting.Dialog.GoodDetailQrcodeDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GoodDetailQrcodeDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodDetailQrcodeDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GoodDetailQrcodeDialog.this.dismiss();
            }
        });
    }
}

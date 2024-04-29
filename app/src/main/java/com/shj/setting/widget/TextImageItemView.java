package com.shj.setting.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.ButtonInfoItemView;

/* loaded from: classes2.dex */
public class TextImageItemView extends AbsItemView {
    private Bitmap bitmap;
    private ButtonInfoItemView.ClickEventListener clickEventListener;
    private ImageView iv_pic;
    private String name;
    private String picPath;
    private TextView tv_name;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return null;
    }

    public TextImageItemView(Context context, String str) {
        super(context);
        this.name = str;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_text_image_item, (ViewGroup) null);
        this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        this.iv_pic = (ImageView) inflate.findViewById(R.id.iv_pic);
        this.tv_name.setText(this.name);
        addContentView(inflate);
    }

    public void setImageClickListener(View.OnClickListener onClickListener) {
        this.iv_pic.setOnClickListener(onClickListener);
    }

    public void setPicPath(String str) {
        this.picPath = str;
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        this.bitmap = decodeFile;
        if (decodeFile != null) {
            this.iv_pic.setImageBitmap(decodeFile);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            this.iv_pic.setImageBitmap(bitmap);
        }
    }

    public String getPicPath() {
        return this.picPath;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
}

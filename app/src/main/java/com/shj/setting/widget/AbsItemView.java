package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AbsItemView extends LinearLayout {
    private static final int TWO_LEVEL_NUM = 4;
    private Button bt_clear;
    private Button bt_query;
    private Button bt_save_setting;
    private long clickTime;
    protected Context context;
    private EventListener eventListener;
    private boolean isAlwaysNotDisplaySaveButton;
    private RelativeLayout ll_bottom;
    private LinearLayout ll_center_content;
    private LinearLayout ll_menu;
    private List<Button> munuButtonList;
    private String name;
    private TextView tv_title;

    /* loaded from: classes2.dex */
    public interface EventListener {
        void clearButtonClick(Button button);

        void onAttachedToWindow();

        void onDetachedFromWindow();

        void queryButtonClick(Button button);

        void saveButtonClick(Button button);
    }

    public abstract View getView();

    public AbsItemView(Context context) {
        super(context);
        this.context = context;
        initBaseView();
        setListener();
    }

    public String getName() {
        return this.name;
    }

    public void setTitle(String str) {
        this.name = str;
        TextView textView = this.tv_title;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setTitleVisibility(int i) {
        this.tv_title.setVisibility(i);
    }

    public void setSaveButtonVisibility(int i) {
        if (this.isAlwaysNotDisplaySaveButton) {
            return;
        }
        this.ll_bottom.setVisibility(i);
    }

    public void setQueryButtonVIsibility(int i) {
        this.bt_query.setVisibility(i);
    }

    public void setQueryButtonText(String str) {
        this.bt_query.setText(str);
    }

    public void setClearButtonVisibility(int i) {
        this.bt_clear.setVisibility(i);
    }

    public void setAlwaysNotDisplaySaveButton() {
        this.isAlwaysNotDisplaySaveButton = true;
        this.ll_bottom.setVisibility(8);
    }

    public void setSaveSettingText(String str) {
        this.bt_save_setting.setText(str);
    }

    public void setClearSettingText(String str) {
        this.bt_clear.setText(str);
    }

    public void initBaseView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_base_item, this);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.ll_bottom = (RelativeLayout) findViewById(R.id.ll_bottom);
        this.ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        this.ll_center_content = (LinearLayout) findViewById(R.id.ll_center_content);
        this.bt_save_setting = (Button) findViewById(R.id.bt_save_setting);
        this.bt_clear = (Button) findViewById(R.id.bt_clear);
        this.bt_query = (Button) findViewById(R.id.bt_query);
    }

    /* renamed from: com.shj.setting.widget.AbsItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                return;
            }
            AbsItemView.this.clickTime = System.currentTimeMillis();
            if (AbsItemView.this.eventListener != null) {
                AbsItemView.this.eventListener.saveButtonClick(AbsItemView.this.bt_save_setting);
            }
        }
    }

    private void setListener() {
        this.bt_save_setting.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.AbsItemView.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                    return;
                }
                AbsItemView.this.clickTime = System.currentTimeMillis();
                if (AbsItemView.this.eventListener != null) {
                    AbsItemView.this.eventListener.saveButtonClick(AbsItemView.this.bt_save_setting);
                }
            }
        });
        this.bt_clear.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.AbsItemView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                    return;
                }
                AbsItemView.this.clickTime = System.currentTimeMillis();
                if (AbsItemView.this.eventListener != null) {
                    AbsItemView.this.eventListener.clearButtonClick(AbsItemView.this.bt_clear);
                }
            }
        });
        this.bt_query.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.AbsItemView.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                    return;
                }
                AbsItemView.this.clickTime = System.currentTimeMillis();
                if (AbsItemView.this.eventListener != null) {
                    AbsItemView.this.eventListener.queryButtonClick(AbsItemView.this.bt_query);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.AbsItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                return;
            }
            AbsItemView.this.clickTime = System.currentTimeMillis();
            if (AbsItemView.this.eventListener != null) {
                AbsItemView.this.eventListener.clearButtonClick(AbsItemView.this.bt_clear);
            }
        }
    }

    /* renamed from: com.shj.setting.widget.AbsItemView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - AbsItemView.this.clickTime) < 1000) {
                return;
            }
            AbsItemView.this.clickTime = System.currentTimeMillis();
            if (AbsItemView.this.eventListener != null) {
                AbsItemView.this.eventListener.queryButtonClick(AbsItemView.this.bt_query);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventListener eventListener = this.eventListener;
        if (eventListener != null) {
            eventListener.onAttachedToWindow();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventListener eventListener = this.eventListener;
        if (eventListener != null) {
            eventListener.onDetachedFromWindow();
        }
    }

    public void addContentView(View view) {
        this.ll_center_content.addView(view);
    }

    public void clearContentView() {
        this.ll_center_content.removeAllViews();
    }

    public ViewGroup getCenterContentView() {
        return this.ll_center_content;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}

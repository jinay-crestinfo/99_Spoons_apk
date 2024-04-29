package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class SystemTimeItemView extends AbsItemView {
    private ClickEventListener clickEventListener;
    private TextView tv_date;
    private TextView tv_date_name;
    private TextView tv_time;
    private TextView tv_time_name;

    /* loaded from: classes2.dex */
    public interface ClickEventListener {
        void dateClick(View view);

        void timeClick(View view);
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public SystemTimeItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_system_time_item, (ViewGroup) null);
        this.tv_date = (TextView) inflate.findViewById(R.id.tv_date);
        this.tv_time = (TextView) inflate.findViewById(R.id.tv_time);
        this.tv_date_name = (TextView) inflate.findViewById(R.id.tv_date_name);
        this.tv_time_name = (TextView) inflate.findViewById(R.id.tv_time_name);
        addContentView(inflate);
        setListener();
    }

    /* renamed from: com.shj.setting.widget.SystemTimeItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SystemTimeItemView.this.clickEventListener != null) {
                SystemTimeItemView.this.clickEventListener.dateClick(view);
            }
        }
    }

    private void setListener() {
        this.tv_date.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.SystemTimeItemView.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SystemTimeItemView.this.clickEventListener != null) {
                    SystemTimeItemView.this.clickEventListener.dateClick(view);
                }
            }
        });
        this.tv_time.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.SystemTimeItemView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SystemTimeItemView.this.clickEventListener != null) {
                    SystemTimeItemView.this.clickEventListener.timeClick(view);
                }
            }
        });
        this.tv_date_name.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.SystemTimeItemView.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SystemTimeItemView.this.clickEventListener != null) {
                    SystemTimeItemView.this.clickEventListener.dateClick(view);
                }
            }
        });
        this.tv_time_name.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.SystemTimeItemView.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SystemTimeItemView.this.clickEventListener != null) {
                    SystemTimeItemView.this.clickEventListener.timeClick(view);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.SystemTimeItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SystemTimeItemView.this.clickEventListener != null) {
                SystemTimeItemView.this.clickEventListener.timeClick(view);
            }
        }
    }

    /* renamed from: com.shj.setting.widget.SystemTimeItemView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SystemTimeItemView.this.clickEventListener != null) {
                SystemTimeItemView.this.clickEventListener.dateClick(view);
            }
        }
    }

    /* renamed from: com.shj.setting.widget.SystemTimeItemView$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SystemTimeItemView.this.clickEventListener != null) {
                SystemTimeItemView.this.clickEventListener.timeClick(view);
            }
        }
    }

    public void setDate(String str) {
        this.tv_date.setText(str);
    }

    public String getDate() {
        return this.tv_date.getText().toString();
    }

    public void setTime(String str) {
        this.tv_time.setText(str);
    }

    public String getTime() {
        return this.tv_time.getText().toString();
    }

    public void setClickEventListener(ClickEventListener clickEventListener) {
        this.clickEventListener = clickEventListener;
    }
}

package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class SeekBarItemView extends AbsItemView {
    private ChangeListener changeListener;
    private String name;
    private SeekBar seekbar;
    private TextView tv_name;
    private String value;

    /* loaded from: classes2.dex */
    public interface ChangeListener {
        void change();
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public SeekBarItemView(Context context, String str) {
        super(context);
        this.name = str;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_seekbar_item, (ViewGroup) null);
        this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.seekbar);
        this.seekbar = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.shj.setting.widget.SeekBarItemView.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            AnonymousClass1() {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
                if (SeekBarItemView.this.changeListener != null) {
                    SeekBarItemView.this.changeListener.change();
                }
            }
        });
        this.tv_name.setText(this.name);
        addContentView(inflate);
    }

    /* renamed from: com.shj.setting.widget.SeekBarItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements SeekBar.OnSeekBarChangeListener {
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar2) {
        }

        AnonymousClass1() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar2) {
            if (SeekBarItemView.this.changeListener != null) {
                SeekBarItemView.this.changeListener.change();
            }
        }
    }

    public void setProgress(int i) {
        this.seekbar.setProgress(i);
    }

    public int getProgress() {
        return this.seekbar.getProgress();
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}

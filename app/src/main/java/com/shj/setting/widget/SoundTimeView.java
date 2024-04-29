package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shj.setting.R;
import com.xyshj.database.setting.SoundTimeData;

/* loaded from: classes2.dex */
public class SoundTimeView extends AbsItemView {
    private String settingName;
    private SoundTimeData soundTimeData;
    private TextView tv_name;
    private TextView tv_sound;
    private TextView tv_time;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public SoundTimeView(Context context, String str, SoundTimeData soundTimeData) {
        super(context);
        this.settingName = str;
        this.soundTimeData = soundTimeData;
        initView();
    }

    public void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_sound_time_item, (ViewGroup) null);
        this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        this.tv_time = (TextView) inflate.findViewById(R.id.tv_time);
        this.tv_sound = (TextView) inflate.findViewById(R.id.tv_sound);
        this.tv_name.setText(this.settingName);
        updataValue(this.soundTimeData);
        addContentView(inflate);
    }

    public void updataValue(SoundTimeData soundTimeData) {
        this.tv_time.setText(soundTimeData.startTime + "-" + soundTimeData.endTime);
        this.tv_sound.setText(String.valueOf(soundTimeData.soundValue));
        this.soundTimeData = soundTimeData;
    }
}

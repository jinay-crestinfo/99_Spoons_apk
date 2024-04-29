package com.shj.setting.view;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oysb.utils.PhoneHelper;
import com.shj.biz.ShjManager;
import com.shj.setting.R;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class TrafficStatsView extends LinearLayout {
    private Context context;

    public TrafficStatsView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_trafficstats, this);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_current_day);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_current_month);
        PhoneHelper phoneHelper = ShjManager.getPhoneHelper();
        phoneHelper.updateTrafficInfo();
        long curDayTraffic = phoneHelper.getCurDayTraffic();
        long curMonthTraffic = phoneHelper.getCurMonthTraffic();
        setTrafficText(curDayTraffic, textView);
        setTrafficText(curMonthTraffic, textView2);
    }

    private void setTrafficText(long j, TextView textView) {
        if (j >= IjkMediaMeta.AV_CH_STEREO_RIGHT) {
            double d = j;
            Double.isNaN(d);
            textView.setText(String.format("%.2f", Double.valueOf(d / 1.073741824E9d)) + "GB");
            return;
        }
        if (j >= PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            double d2 = j;
            Double.isNaN(d2);
            textView.setText(String.format("%.2f", Double.valueOf(d2 / 1048576.0d)) + "MB");
            return;
        }
        if (j >= 1024) {
            double d3 = j;
            Double.isNaN(d3);
            textView.setText(String.format("%.2f", Double.valueOf(d3 / 1024.0d)) + "KB");
            return;
        }
        textView.setText(j + "B");
    }
}

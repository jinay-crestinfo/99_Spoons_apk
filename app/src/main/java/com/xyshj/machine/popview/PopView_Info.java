package com.xyshj.machine.popview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.xyshj.machine.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PopView_Info extends BasePopView implements View.OnClickListener {
    public static final int MSG_CLOSE = 101;
    public static final int MSG_TIMER = 102;
    static Handler mainThread = new Handler(Looper.getMainLooper());
    LinearLayout body;
    int MSG_BASE = 0;
    List<InfoItem> infoItems = new ArrayList();
    HashMap<String, Object> dataMap = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface TimeTickListener {
        boolean closeOnTimeTick(int i, JSONObject jSONObject);

        void onClosed(JSONObject jSONObject);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
    }

    /* loaded from: classes2.dex */
    public static class BaseMap extends HashMap {
        public BaseMap put(String str, Object obj) {
            super.put((BaseMap) str, (String) obj);
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public class InfoItem {
        LinearLayout info;
        View infoBody;
        ImageView info_icon;
        TextView notice;
        ImageView notice_icon;
        TextView time;
        int timer;
        TextView title;
        String uid = "";
        int message_base = 0;
        JSONObject jsonObject = null;

        InfoItem() {
        }

        void init() {
            if (this.infoBody != null) {
                return;
            }
            this.message_base = PopView_Info.this.MSG_BASE + 1000;
            this.infoBody = LayoutInflater.from(PopView_Info.this.getParent().getContext()).inflate(R.layout.popview_info_layout, (ViewGroup) null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.topMargin = 10;
            this.infoBody.setPadding(20, 20, 20, 20);
            this.infoBody.setLayoutParams(layoutParams);
            LinearLayout linearLayout = (LinearLayout) this.infoBody.findViewById(R.id.info);
            this.info = linearLayout;
            linearLayout.setTag(0);
            this.title = (TextView) this.infoBody.findViewById(R.id.title);
            this.notice = (TextView) this.infoBody.findViewById(R.id.notice);
            this.notice_icon = (ImageView) this.infoBody.findViewById(R.id.notice_ico);
            this.time = (TextView) this.infoBody.findViewById(R.id.timer);
        }

        /* JADX WARN: Removed duplicated region for block: B:39:0x02b0 A[Catch: Exception -> 0x052e, TryCatch #0 {Exception -> 0x052e, blocks: (B:6:0x0052, B:8:0x005a, B:10:0x0060, B:11:0x0075, B:13:0x007b, B:14:0x0082, B:16:0x008c, B:17:0x009d, B:19:0x00bd, B:20:0x00c6, B:22:0x00cc, B:24:0x00e5, B:26:0x00ee, B:27:0x00f9, B:30:0x00fe, B:32:0x0123, B:33:0x023a, B:35:0x0248, B:37:0x02aa, B:39:0x02b0, B:41:0x02c8, B:43:0x026b, B:45:0x0274, B:46:0x0296, B:49:0x02a8, B:51:0x015d, B:54:0x018a, B:57:0x019d, B:60:0x01db, B:63:0x020f, B:66:0x0219, B:69:0x0223, B:77:0x02db, B:79:0x0324, B:81:0x033e, B:83:0x035c, B:85:0x0370, B:87:0x0377, B:89:0x037f, B:90:0x0396, B:92:0x039c, B:93:0x03d3, B:95:0x03e4, B:98:0x03ed, B:101:0x03fa, B:104:0x0415, B:106:0x041e, B:107:0x0448, B:110:0x0456, B:113:0x046c, B:116:0x047b, B:119:0x048a, B:121:0x0495, B:123:0x049d, B:124:0x04b0, B:126:0x04b8, B:129:0x04fa, B:131:0x0504, B:132:0x051e, B:134:0x04a5, B:140:0x0428, B:142:0x03c1, B:143:0x0093, B:145:0x0070), top: B:5:0x0052 }] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x02c8 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x0370 A[Catch: Exception -> 0x052e, TryCatch #0 {Exception -> 0x052e, blocks: (B:6:0x0052, B:8:0x005a, B:10:0x0060, B:11:0x0075, B:13:0x007b, B:14:0x0082, B:16:0x008c, B:17:0x009d, B:19:0x00bd, B:20:0x00c6, B:22:0x00cc, B:24:0x00e5, B:26:0x00ee, B:27:0x00f9, B:30:0x00fe, B:32:0x0123, B:33:0x023a, B:35:0x0248, B:37:0x02aa, B:39:0x02b0, B:41:0x02c8, B:43:0x026b, B:45:0x0274, B:46:0x0296, B:49:0x02a8, B:51:0x015d, B:54:0x018a, B:57:0x019d, B:60:0x01db, B:63:0x020f, B:66:0x0219, B:69:0x0223, B:77:0x02db, B:79:0x0324, B:81:0x033e, B:83:0x035c, B:85:0x0370, B:87:0x0377, B:89:0x037f, B:90:0x0396, B:92:0x039c, B:93:0x03d3, B:95:0x03e4, B:98:0x03ed, B:101:0x03fa, B:104:0x0415, B:106:0x041e, B:107:0x0448, B:110:0x0456, B:113:0x046c, B:116:0x047b, B:119:0x048a, B:121:0x0495, B:123:0x049d, B:124:0x04b0, B:126:0x04b8, B:129:0x04fa, B:131:0x0504, B:132:0x051e, B:134:0x04a5, B:140:0x0428, B:142:0x03c1, B:143:0x0093, B:145:0x0070), top: B:5:0x0052 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void updateMessage(org.json.JSONObject r25) {
            /*
                Method dump skipped, instructions count: 1348
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.popview.PopView_Info.InfoItem.updateMessage(org.json.JSONObject):void");
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Info$InfoItem$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PopView_Info.this.handler.sendEmptyMessage(InfoItem.this.message_base + 101);
            }
        }

        void handMessage(Message message) {
            if (message.what - this.message_base > 1000) {
                return;
            }
            try {
                int i = message.what - this.message_base;
                if (i == 101) {
                    PopView_Info.this.infoItems.remove(this);
                    Animation loadAnimation = AnimationUtils.loadAnimation(PopView_Info.this.getParent().getContext(), R.anim.fade_out);
                    loadAnimation.setDuration(300L);
                    loadAnimation.setFillAfter(true);
                    loadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.xyshj.machine.popview.PopView_Info.InfoItem.2
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                        }

                        AnonymousClass2() {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            PopView_Info.this.body.removeView(InfoItem.this.infoBody);
                        }
                    });
                    this.infoBody.startAnimation(loadAnimation);
                    if (PopView_Info.this.dataMap.containsKey(this.jsonObject.getString("uid") + "timeTickListener")) {
                        try {
                            ((TimeTickListener) PopView_Info.this.dataMap.get(this.jsonObject.getString("uid") + "timeTickListener")).onClosed(this.jsonObject);
                        } catch (Exception unused) {
                        }
                    }
                    if (PopView_Info.this.infoItems.size() == 0) {
                        PopView_Info.this.close();
                    }
                } else if (i == 102) {
                    this.timer--;
                    this.time.setText(this.timer + this.infoBody.getContext().getString(R.string.lab_seconds));
                    if (PopView_Info.this.dataMap.containsKey(this.jsonObject.getString("uid") + "timeTickListener")) {
                        try {
                            ((TimeTickListener) PopView_Info.this.dataMap.get(this.jsonObject.getString("uid") + "timeTickListener")).closeOnTimeTick(this.timer, this.jsonObject);
                        } catch (Exception unused2) {
                        }
                    }
                    PopView_Info.this.handler.sendEmptyMessageDelayed(this.message_base + 102, 1000L);
                    if (this.timer <= 0) {
                        PopView_Info.this.handler.sendEmptyMessageDelayed(this.message_base + 101, 1000L);
                    }
                }
            } catch (Exception e) {
                Loger.writeException("SHJ", e);
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Info$InfoItem$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements Animation.AnimationListener {
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            AnonymousClass2() {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                PopView_Info.this.body.removeView(InfoItem.this.infoBody);
            }
        }
    }

    @Override // com.oysb.utils.view.BasePopView, com.oysb.utils.view.BFPopView
    public void close() {
        synchronized (this) {
            if (this.infoItems.size() > 0) {
                return;
            }
            super.close();
        }
    }

    public static void showInfo(Map<String, Object> map) {
        showInfo(map, 0, null);
    }

    public static void closeInfo(String str) {
        try {
            PopView_Info popView_Info = (PopView_Info) BFPopView.findPopView("MainActivity", "PopView_Info");
            for (InfoItem infoItem : popView_Info.infoItems) {
                if (infoItem.uid.equals(str)) {
                    popView_Info.handler.sendEmptyMessageDelayed(infoItem.message_base + 101, 1000L);
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }

    public static void showInfo(Map<String, Object> map, int i, TimeTickListener timeTickListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            for (String str : map.keySet()) {
                jSONObject.put(str, map.get(str));
            }
            mainThread.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Info.1
                final /* synthetic */ JSONObject val$jsonObject;
                final /* synthetic */ TimeTickListener val$l;
                final /* synthetic */ Map val$params;

                AnonymousClass1(JSONObject jSONObject2, TimeTickListener timeTickListener2, Map map2) {
                    jSONObject = jSONObject2;
                    timeTickListener = timeTickListener2;
                    map = map2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_Info popView_Info = (PopView_Info) BFPopView.findPopView("MainActivity", "PopView_Info");
                    if (popView_Info != null && popView_Info.isShowing()) {
                        popView_Info._showInfo(jSONObject);
                    } else {
                        BFPopView.showPopView("MainActivity", "PopView_Info", PopView_Info.class.getName(), jSONObject.toString(), 0);
                        popView_Info = (PopView_Info) BFPopView.findPopView("MainActivity", "PopView_Info");
                    }
                    if (timeTickListener != null) {
                        popView_Info.dataMap.put(map.get("uid") + "timeTickListener", timeTickListener);
                    }
                }
            }, i);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Info$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;
        final /* synthetic */ TimeTickListener val$l;
        final /* synthetic */ Map val$params;

        AnonymousClass1(JSONObject jSONObject2, TimeTickListener timeTickListener2, Map map2) {
            jSONObject = jSONObject2;
            timeTickListener = timeTickListener2;
            map = map2;
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_Info popView_Info = (PopView_Info) BFPopView.findPopView("MainActivity", "PopView_Info");
            if (popView_Info != null && popView_Info.isShowing()) {
                popView_Info._showInfo(jSONObject);
            } else {
                BFPopView.showPopView("MainActivity", "PopView_Info", PopView_Info.class.getName(), jSONObject.toString(), 0);
                popView_Info = (PopView_Info) BFPopView.findPopView("MainActivity", "PopView_Info");
            }
            if (timeTickListener != null) {
                popView_Info.dataMap.put(map.get("uid") + "timeTickListener", timeTickListener);
            }
        }
    }

    public void _showInfo(JSONObject jSONObject) {
        boolean z;
        Iterator<InfoItem> it = this.infoItems.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            InfoItem next = it.next();
            if (next.uid.equals(jSONObject.getString("uid"))) {
                next.updateMessage(jSONObject);
                z = true;
                break;
            }
            continue;
        }
        if (z) {
            return;
        }
        InfoItem infoItem = new InfoItem();
        int i = this.MSG_BASE;
        int i2 = i + i + 1000;
        this.MSG_BASE = i2;
        infoItem.message_base = i2;
        infoItem.init();
        infoItem.infoBody.setVisibility(4);
        this.body.addView(infoItem.infoBody);
        this.infoItems.add(infoItem);
        infoItem.updateMessage(jSONObject);
        Animation loadAnimation = AnimationUtils.loadAnimation(getParent().getContext(), R.anim.fade_in);
        loadAnimation.setDuration(300L);
        loadAnimation.setFillAfter(true);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.xyshj.machine.popview.PopView_Info.2
            final /* synthetic */ InfoItem val$item;

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            AnonymousClass2(InfoItem infoItem2) {
                infoItem = infoItem2;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                infoItem.infoBody.setVisibility(0);
            }
        });
        infoItem2.infoBody.startAnimation(loadAnimation);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Info$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Animation.AnimationListener {
        final /* synthetic */ InfoItem val$item;

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        AnonymousClass2(InfoItem infoItem2) {
            infoItem = infoItem2;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            infoItem.infoBody.setVisibility(0);
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        RelativeLayout relativeLayout = new RelativeLayout(getParent().getContext());
        relativeLayout.setBackgroundColor(0);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        relativeLayout.setPadding(100, 0, 100, 0);
        this.body = new LinearLayout(relativeLayout.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13);
        this.body.setLayoutParams(layoutParams);
        this.body.setOrientation(1);
        relativeLayout.addView(this.body);
        return relativeLayout;
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        isShowing();
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        Iterator<InfoItem> it = this.infoItems.iterator();
        while (it.hasNext()) {
            it.next().handMessage(message);
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        super.onViewWillShow();
        try {
            this.MSG_BASE = 1000;
            this.infoItems.clear();
            this.body.removeAllViews();
            _showInfo(new JSONObject(getArgs().toString()));
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        super.onViewDidClose();
        this.dataMap.clear();
    }
}

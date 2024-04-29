package com.shj.setting.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.baidu.mapapi.UIMsg;
import com.shj.setting.R;
import com.shj.setting.Utils.Base64Util;

/* loaded from: classes2.dex */
public class QrcodeFloatView {
    private static Boolean isShown = false;
    private static QrcodeFloatView qrcodeFloatView;
    private View mView;
    private WindowManager.LayoutParams params;
    private WindowManager wm;
    private int left = 0;
    private int top = 100;
    private int right = 800;
    private int bottom = 1280;
    private boolean isForwordRight = true;
    private boolean isForwordDowm = true;
    Handler handler = new Handler() { // from class: com.shj.setting.view.QrcodeFloatView.2
        AnonymousClass2() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            QrcodeFloatView.this.sport();
            QrcodeFloatView.this.wm.updateViewLayout(QrcodeFloatView.this.mView, QrcodeFloatView.this.params);
            QrcodeFloatView.this.handler.sendEmptyMessageDelayed(0, 10L);
        }
    };

    public static QrcodeFloatView getinstance() {
        if (qrcodeFloatView == null) {
            qrcodeFloatView = new QrcodeFloatView();
        }
        return qrcodeFloatView;
    }

    public void showFloat(Context context, String str) {
        int i;
        if (isShown.booleanValue()) {
            return;
        }
        isShown = true;
        this.top = (int) context.getResources().getDimension(R.dimen.y100);
        this.right = (int) context.getResources().getDimension(R.dimen.x1080);
        this.bottom = ((int) context.getResources().getDimension(R.dimen.y1920)) / 2;
        this.wm = (WindowManager) context.getSystemService("window");
        this.params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT >= 24) {
            i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        } else {
            i = UIMsg.m_AppUI.MSG_APP_VERSION_FORCE;
        }
        this.params.type = i;
        this.params.flags = 131112;
        this.params.format = -3;
        this.params.width = (int) context.getResources().getDimension(R.dimen.x200);
        this.params.height = (int) context.getResources().getDimension(R.dimen.y200);
        this.params.y = (int) context.getResources().getDimension(R.dimen.y100);
        this.params.gravity = 51;
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_qrcode_floatview, (ViewGroup) null);
        this.mView = inflate;
        inflate.setOnTouchListener(new View.OnTouchListener() { // from class: com.shj.setting.view.QrcodeFloatView.1
            float lastX;
            float lastY;
            int oldOffsetX;
            int oldOffsetY;
            int tag = 0;

            AnonymousClass1() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (this.tag == 0) {
                    this.oldOffsetX = QrcodeFloatView.this.params.x;
                    this.oldOffsetY = QrcodeFloatView.this.params.y;
                }
                if (action == 0) {
                    this.lastX = x;
                    this.lastY = y;
                } else if (action == 2) {
                    QrcodeFloatView.this.params.x += ((int) (x - this.lastX)) / 3;
                    QrcodeFloatView.this.params.y += ((int) (y - this.lastY)) / 3;
                    this.tag = 1;
                    if (QrcodeFloatView.this.mView != null) {
                        QrcodeFloatView.this.wm.updateViewLayout(QrcodeFloatView.this.mView, QrcodeFloatView.this.params);
                    }
                } else if (action == 1) {
                    this.tag = 0;
                }
                return true;
            }
        });
        ImageView imageView = (ImageView) this.mView.findViewById(R.id.image);
        Bitmap base64ToBitmap = Base64Util.base64ToBitmap(str);
        if (base64ToBitmap != null) {
            imageView.setImageBitmap(base64ToBitmap);
        }
        this.wm.addView(this.mView, this.params);
        this.handler.sendEmptyMessageDelayed(0, 2000L);
    }

    /* renamed from: com.shj.setting.view.QrcodeFloatView$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements View.OnTouchListener {
        float lastX;
        float lastY;
        int oldOffsetX;
        int oldOffsetY;
        int tag = 0;

        AnonymousClass1() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.tag == 0) {
                this.oldOffsetX = QrcodeFloatView.this.params.x;
                this.oldOffsetY = QrcodeFloatView.this.params.y;
            }
            if (action == 0) {
                this.lastX = x;
                this.lastY = y;
            } else if (action == 2) {
                QrcodeFloatView.this.params.x += ((int) (x - this.lastX)) / 3;
                QrcodeFloatView.this.params.y += ((int) (y - this.lastY)) / 3;
                this.tag = 1;
                if (QrcodeFloatView.this.mView != null) {
                    QrcodeFloatView.this.wm.updateViewLayout(QrcodeFloatView.this.mView, QrcodeFloatView.this.params);
                }
            } else if (action == 1) {
                this.tag = 0;
            }
            return true;
        }
    }

    public void closeFloatBox() {
        View view;
        WindowManager windowManager;
        this.handler.removeMessages(0);
        if (!isShown.booleanValue() || (view = this.mView) == null || (windowManager = this.wm) == null) {
            return;
        }
        try {
            windowManager.removeView(view);
            isShown = false;
            this.mView = null;
            this.wm = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.view.QrcodeFloatView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends Handler {
        AnonymousClass2() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            QrcodeFloatView.this.sport();
            QrcodeFloatView.this.wm.updateViewLayout(QrcodeFloatView.this.mView, QrcodeFloatView.this.params);
            QrcodeFloatView.this.handler.sendEmptyMessageDelayed(0, 10L);
        }
    }

    public void sport() {
        if (this.isForwordRight) {
            if (this.params.x < this.right - this.params.width) {
                this.params.x++;
            } else {
                this.isForwordRight = false;
            }
        } else if (this.params.x > this.left) {
            this.params.x--;
        } else {
            this.isForwordRight = true;
        }
        if (this.isForwordDowm) {
            if (this.params.y < this.bottom - this.params.height) {
                this.params.y++;
                return;
            } else {
                this.isForwordDowm = false;
                return;
            }
        }
        if (this.params.y > this.top) {
            this.params.y--;
        } else {
            this.isForwordDowm = true;
        }
    }
}

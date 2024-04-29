package com.shj.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Utils.CameraUtils;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.xyshj.database.setting.AppSetting;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TakePictureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final int COUNT_DOWN_NUM = 120;
    private static final int MSG_COUNT_DOWN = 1;
    private static final int MSG_DELETEPHONE_NUMBER = 2;
    public static final String PID_VID = "usb:v1BCFp3288";
    public static final String PID_VID_ZFB = "usb:v2BC5p0501";
    private Bitmap bitmap;
    private Button bt_close;
    private Button bt_key_0;
    private Button bt_key_1;
    private Button bt_key_2;
    private Button bt_key_3;
    private Button bt_key_4;
    private Button bt_key_5;
    private Button bt_key_6;
    private Button bt_key_7;
    private Button bt_key_8;
    private Button bt_key_9;
    private Button bt_key_del;
    private Button bt_ok;
    private Button bt_retake_pic;
    private Button bt_take_pic;
    private EditText et_phone;
    private boolean isTakePic;
    private ImageView iv_pic;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private String picturePath;
    private Timer timer;
    private TextView tv_count_down;
    private final int CAMERA_WIDTH = 1024;
    private final int CAMERA_HEIGH = 768;
    private String picDir = SDFileUtils.SDCardRoot + "xyShj/CamaraPic/";
    private int countDown = 120;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() { // from class: com.shj.setting.TakePictureActivity.6
        AnonymousClass6() {
        }

        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            TakePictureActivity.this.bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            TakePictureActivity.this.iv_pic.setImageBitmap(TakePictureActivity.this.bitmap);
            TakePictureActivity.this.mCamera.startPreview();
        }
    };
    private Handler mHandler = new Handler() { // from class: com.shj.setting.TakePictureActivity.9
        AnonymousClass9() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    return;
                }
                TakePictureActivity.this.deletePhoneNumber();
            } else {
                if (TakePictureActivity.this.countDown > 0) {
                    TakePictureActivity.this.tv_count_down.setText(TakePictureActivity.access$910(TakePictureActivity.this) + "s");
                    TakePictureActivity.this.mHandler.removeMessages(1);
                    TakePictureActivity.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
                    return;
                }
                TakePictureActivity.this.finish();
            }
        }
    };

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    static /* synthetic */ int access$910(TakePictureActivity takePictureActivity) {
        int i = takePictureActivity.countDown;
        takePictureActivity.countDown = i - 1;
        return i;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_high_time_meter_takepic_activity);
        findView();
        setListener();
        initSurface(this);
    }

    private void findView() {
        this.tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        this.bt_take_pic = (Button) findViewById(R.id.bt_take_pic);
        this.bt_retake_pic = (Button) findViewById(R.id.bt_retake_pic);
        this.bt_close = (Button) findViewById(R.id.bt_cancel);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.iv_pic = (ImageView) findViewById(R.id.iv_pic);
        this.et_phone = (EditText) findViewById(R.id.et_phone);
        this.mSurfaceView = (SurfaceView) findViewById(R.id.verifySurfaceView);
        Button button = (Button) findViewById(R.id.bt_key_0);
        this.bt_key_0 = button;
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.bt_key_1);
        this.bt_key_1 = button2;
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.bt_key_2);
        this.bt_key_2 = button3;
        button3.setOnClickListener(this);
        Button button4 = (Button) findViewById(R.id.bt_key_3);
        this.bt_key_3 = button4;
        button4.setOnClickListener(this);
        Button button5 = (Button) findViewById(R.id.bt_key_4);
        this.bt_key_4 = button5;
        button5.setOnClickListener(this);
        Button button6 = (Button) findViewById(R.id.bt_key_5);
        this.bt_key_5 = button6;
        button6.setOnClickListener(this);
        Button button7 = (Button) findViewById(R.id.bt_key_6);
        this.bt_key_6 = button7;
        button7.setOnClickListener(this);
        Button button8 = (Button) findViewById(R.id.bt_key_7);
        this.bt_key_7 = button8;
        button8.setOnClickListener(this);
        Button button9 = (Button) findViewById(R.id.bt_key_8);
        this.bt_key_8 = button9;
        button9.setOnClickListener(this);
        Button button10 = (Button) findViewById(R.id.bt_key_9);
        this.bt_key_9 = button10;
        button10.setOnClickListener(this);
        Button button11 = (Button) findViewById(R.id.bt_key_del);
        this.bt_key_del = button11;
        button11.setOnClickListener(this);
    }

    /* renamed from: com.shj.setting.TakePictureActivity$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TakePictureActivity.this.mCamera == null) {
                return;
            }
            TakePictureActivity.this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.shj.setting.TakePictureActivity.1.1
                C00591() {
                }

                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera) {
                    TakePictureActivity.this.picturePath = null;
                    TakePictureActivity.this.isTakePic = true;
                }
            });
        }

        /* renamed from: com.shj.setting.TakePictureActivity$1$1 */
        /* loaded from: classes2.dex */
        class C00591 implements Camera.AutoFocusCallback {
            C00591() {
            }

            @Override // android.hardware.Camera.AutoFocusCallback
            public void onAutoFocus(boolean z, Camera camera) {
                TakePictureActivity.this.picturePath = null;
                TakePictureActivity.this.isTakePic = true;
            }
        }
    }

    private void setListener() {
        this.bt_take_pic.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.TakePictureActivity.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TakePictureActivity.this.mCamera == null) {
                    return;
                }
                TakePictureActivity.this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.shj.setting.TakePictureActivity.1.1
                    C00591() {
                    }

                    @Override // android.hardware.Camera.AutoFocusCallback
                    public void onAutoFocus(boolean z, Camera camera) {
                        TakePictureActivity.this.picturePath = null;
                        TakePictureActivity.this.isTakePic = true;
                    }
                });
            }

            /* renamed from: com.shj.setting.TakePictureActivity$1$1 */
            /* loaded from: classes2.dex */
            class C00591 implements Camera.AutoFocusCallback {
                C00591() {
                }

                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera) {
                    TakePictureActivity.this.picturePath = null;
                    TakePictureActivity.this.isTakePic = true;
                }
            }
        });
        this.bt_retake_pic.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.TakePictureActivity.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TakePictureActivity.this.iv_pic.setVisibility(4);
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.TakePictureActivity.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TakePictureActivity.this.finish();
            }
        });
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.TakePictureActivity.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TakePictureActivity.this.sendResultBroadcast();
            }
        });
        this.bt_key_del.setOnTouchListener(new View.OnTouchListener() { // from class: com.shj.setting.TakePictureActivity.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (TakePictureActivity.this.timer != null) {
                        return false;
                    }
                    TakePictureActivity.this.timer = new Timer();
                    TakePictureActivity.this.timer.schedule(new TimerTask() { // from class: com.shj.setting.TakePictureActivity.5.1
                        AnonymousClass1() {
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            TakePictureActivity.this.mHandler.sendEmptyMessage(2);
                        }
                    }, 0L, 150L);
                } else if (motionEvent.getAction() == 1 && TakePictureActivity.this.timer != null) {
                    TakePictureActivity.this.timer.cancel();
                    TakePictureActivity.this.timer = null;
                }
                return false;
            }

            /* renamed from: com.shj.setting.TakePictureActivity$5$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    TakePictureActivity.this.mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.TakePictureActivity$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TakePictureActivity.this.iv_pic.setVisibility(4);
        }
    }

    /* renamed from: com.shj.setting.TakePictureActivity$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TakePictureActivity.this.finish();
        }
    }

    /* renamed from: com.shj.setting.TakePictureActivity$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TakePictureActivity.this.sendResultBroadcast();
        }
    }

    /* renamed from: com.shj.setting.TakePictureActivity$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnTouchListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (TakePictureActivity.this.timer != null) {
                    return false;
                }
                TakePictureActivity.this.timer = new Timer();
                TakePictureActivity.this.timer.schedule(new TimerTask() { // from class: com.shj.setting.TakePictureActivity.5.1
                    AnonymousClass1() {
                    }

                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        TakePictureActivity.this.mHandler.sendEmptyMessage(2);
                    }
                }, 0L, 150L);
            } else if (motionEvent.getAction() == 1 && TakePictureActivity.this.timer != null) {
                TakePictureActivity.this.timer.cancel();
                TakePictureActivity.this.timer = null;
            }
            return false;
        }

        /* renamed from: com.shj.setting.TakePictureActivity$5$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 extends TimerTask {
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TakePictureActivity.this.mHandler.sendEmptyMessage(2);
            }
        }
    }

    /* renamed from: com.shj.setting.TakePictureActivity$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements Camera.PictureCallback {
        AnonymousClass6() {
        }

        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            TakePictureActivity.this.bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            TakePictureActivity.this.iv_pic.setImageBitmap(TakePictureActivity.this.bitmap);
            TakePictureActivity.this.mCamera.startPreview();
        }
    }

    private void initSurface(Context context) {
        SurfaceHolder holder = this.mSurfaceView.getHolder();
        this.mSurfaceHolder = holder;
        holder.addCallback(this);
        String highTimeMeterPidVid = AppSetting.getHighTimeMeterPidVid(context, null);
        if (!TextUtils.isEmpty(highTimeMeterPidVid)) {
            int cameraIndex = SetUtils.getCameraIndex(highTimeMeterPidVid);
            if (cameraIndex == -1) {
                ToastUitl.showShort(context, "未找到摄像头");
                return;
            } else {
                if (OpenCameraAndSetSurfaceviewSize(cameraIndex)) {
                    return;
                }
                ToastUitl.showShort(context, R.string.camara_can_not_use);
                return;
            }
        }
        ToastUitl.showShort(context, "未设置PID与VID");
    }

    private boolean OpenCameraAndSetSurfaceviewSize(int i) {
        try {
            if (this.mCamera == null) {
                this.mCamera = Camera.open(i);
            }
            try {
                Camera.Parameters parameters = this.mCamera.getParameters();
                parameters.setPreviewSize(1024, 768);
                parameters.setPreviewFormat(17);
                parameters.setAntibanding("auto");
                this.mCamera.setParameters(parameters);
                return true;
            } catch (Exception unused) {
                ToastUitl.showShort(this, "预览尺寸不支持");
                return false;
            }
        } catch (Exception e) {
            Loger.writeLog("UI", "open camera" + e.getMessage());
            return false;
        }
    }

    private void SetAndStartPreview(SurfaceHolder surfaceHolder) {
        try {
            this.mCamera.setPreviewDisplay(surfaceHolder);
            this.mCamera.setPreviewCallback(new VerifyPreview());
            this.mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (this.mCamera != null) {
            SetAndStartPreview(surfaceHolder);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        closeCamera();
    }

    public void closeCamera() {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    /* loaded from: classes2.dex */
    public class VerifyPreview implements Camera.PreviewCallback {
        VerifyPreview() {
        }

        @Override // android.hardware.Camera.PreviewCallback
        public void onPreviewFrame(byte[] bArr, Camera camera) {
            if (TakePictureActivity.this.isTakePic) {
                TakePictureActivity.this.isTakePic = false;
                TakePictureActivity.this.iv_pic.setVisibility(0);
                TakePictureActivity.this.bitmap = CameraUtils.getBitMap(bArr, camera);
                TakePictureActivity.this.iv_pic.setImageBitmap(TakePictureActivity.this.bitmap);
                TakePictureActivity takePictureActivity = TakePictureActivity.this;
                takePictureActivity.saveBitmap(takePictureActivity.bitmap);
            }
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        File file = new File(this.picDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String str = this.picDir + "chufang.jpg";
        File file2 = new File(str);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            file2.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            this.picturePath = str;
            Loger.writeLog("UI", "拍摄图片保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void openLight() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestHFJ_JG(8, 1, 0);
        Shj.getInstance(this);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.TakePictureActivity.7
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass7() {
            }
        });
    }

    /* renamed from: com.shj.setting.TakePictureActivity$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass7() {
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
        openLight();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        closeLight();
    }

    private void closeLight() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestHFJ_JG(8, 2, 0);
        Shj.getInstance(this);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.TakePictureActivity.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass8() {
            }
        });
    }

    /* renamed from: com.shj.setting.TakePictureActivity$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass8() {
        }
    }

    public void sendResultBroadcast() {
        if (this.picturePath == null) {
            ToastUitl.showLong(this, "请先拍摄处方单");
            return;
        }
        String obj = this.et_phone.getText().toString();
        if (TextUtils.isEmpty(obj) || !SetUtils.isPhoneLegal(obj)) {
            ToastUitl.showShort(this, R.string.pls_input_correct_phone_number);
            return;
        }
        String string = getIntent().getExtras().getString("identityCardNumber");
        Intent intent = new Intent("android.intent.action.takepicture.complete");
        Bundle bundle = new Bundle();
        bundle.putString("identityCardNumber", string);
        bundle.putString("phoneNumber", obj);
        bundle.putString("pictureFilePath", this.picturePath);
        Loger.writeLog("UI", "identityCardNumber=" + string);
        Loger.writeLog("UI", "pictureFilePath=" + this.picturePath);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        sendBroadcast(intent);
        finish();
    }

    /* renamed from: com.shj.setting.TakePictureActivity$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 extends Handler {
        AnonymousClass9() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    return;
                }
                TakePictureActivity.this.deletePhoneNumber();
            } else {
                if (TakePictureActivity.this.countDown > 0) {
                    TakePictureActivity.this.tv_count_down.setText(TakePictureActivity.access$910(TakePictureActivity.this) + "s");
                    TakePictureActivity.this.mHandler.removeMessages(1);
                    TakePictureActivity.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
                    return;
                }
                TakePictureActivity.this.finish();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_key_0) {
            inPut(0);
            return;
        }
        if (id == R.id.bt_key_1) {
            inPut(1);
            return;
        }
        if (id == R.id.bt_key_2) {
            inPut(2);
            return;
        }
        if (id == R.id.bt_key_3) {
            inPut(3);
            return;
        }
        if (id == R.id.bt_key_4) {
            inPut(4);
            return;
        }
        if (id == R.id.bt_key_5) {
            inPut(5);
            return;
        }
        if (id == R.id.bt_key_6) {
            inPut(6);
            return;
        }
        if (id == R.id.bt_key_7) {
            inPut(7);
        } else if (id == R.id.bt_key_8) {
            inPut(8);
        } else if (id == R.id.bt_key_9) {
            inPut(9);
        }
    }

    private void inPut(int i) {
        String obj = this.et_phone.getText().toString();
        if (obj == null) {
            this.et_phone.setText(String.valueOf(i));
            return;
        }
        if (obj.length() < 11) {
            this.et_phone.setText(obj + String.valueOf(i));
        }
    }

    public void deletePhoneNumber() {
        String obj = this.et_phone.getText().toString();
        if (!TextUtils.isEmpty(obj)) {
            this.et_phone.setText(obj.substring(0, obj.length() - 1));
            return;
        }
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
    }
}

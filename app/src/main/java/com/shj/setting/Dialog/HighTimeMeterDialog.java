package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.oysb.utils.Loger;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.CameraUtils;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.xyshj.database.setting.AppSetting;
import java.io.IOException;

/* loaded from: classes2.dex */
public class HighTimeMeterDialog extends Dialog implements SurfaceHolder.Callback {
    public static final String PID_VID = "usb:v1BCFp3288";
    public static final String PID_VID_ZFB = "usb:v2BC5p0501";
    private final int CAMERA_HEIGH;
    private final int CAMERA_WIDTH;
    private Button bt_close;
    private Button bt_take_pic;
    private Context context;
    private boolean isTakePic;
    private ImageView iv_pic;
    private Camera mCamera;
    private Camera.PictureCallback mPicture;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public HighTimeMeterDialog(Context context) {
        super(context, R.style.loading_style);
        this.CAMERA_WIDTH = 1024;
        this.CAMERA_HEIGH = 768;
        this.mPicture = new Camera.PictureCallback() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.3
            AnonymousClass3() {
            }

            @Override // android.hardware.Camera.PictureCallback
            public void onPictureTaken(byte[] bArr, Camera camera) {
                HighTimeMeterDialog.this.iv_pic.setImageBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length));
                HighTimeMeterDialog.this.mCamera.startPreview();
            }
        };
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_high_time_meter);
        setCanceledOnTouchOutside(false);
        findView();
        setListener();
        initSurface(this.context);
        openLight();
    }

    private void findView() {
        this.bt_take_pic = (Button) findViewById(R.id.bt_take_pic);
        this.bt_close = (Button) findViewById(R.id.bt_cancel);
        this.iv_pic = (ImageView) findViewById(R.id.iv_pic);
        this.mSurfaceView = (SurfaceView) findViewById(R.id.verifySurfaceView);
    }

    /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (HighTimeMeterDialog.this.mCamera == null) {
                return;
            }
            HighTimeMeterDialog.this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.1.1
                C00551() {
                }

                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera) {
                    HighTimeMeterDialog.this.isTakePic = true;
                }
            });
        }

        /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$1$1 */
        /* loaded from: classes2.dex */
        class C00551 implements Camera.AutoFocusCallback {
            C00551() {
            }

            @Override // android.hardware.Camera.AutoFocusCallback
            public void onAutoFocus(boolean z, Camera camera) {
                HighTimeMeterDialog.this.isTakePic = true;
            }
        }
    }

    private void setListener() {
        this.bt_take_pic.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HighTimeMeterDialog.this.mCamera == null) {
                    return;
                }
                HighTimeMeterDialog.this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.1.1
                    C00551() {
                    }

                    @Override // android.hardware.Camera.AutoFocusCallback
                    public void onAutoFocus(boolean z, Camera camera) {
                        HighTimeMeterDialog.this.isTakePic = true;
                    }
                });
            }

            /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$1$1 */
            /* loaded from: classes2.dex */
            class C00551 implements Camera.AutoFocusCallback {
                C00551() {
                }

                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera) {
                    HighTimeMeterDialog.this.isTakePic = true;
                }
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HighTimeMeterDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            HighTimeMeterDialog.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Camera.PictureCallback {
        AnonymousClass3() {
        }

        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            HighTimeMeterDialog.this.iv_pic.setImageBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length));
            HighTimeMeterDialog.this.mCamera.startPreview();
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
                ToastUitl.showShort(this.context, "预览尺寸不支持");
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

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        closeCamera();
        closeLight();
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
            if (HighTimeMeterDialog.this.isTakePic) {
                HighTimeMeterDialog.this.isTakePic = false;
                HighTimeMeterDialog.this.iv_pic.setImageBitmap(CameraUtils.getBitMap(bArr, camera));
            }
        }
    }

    private void openLight() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestHFJ_JG(8, 1, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.4
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass4() {
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass4() {
        }
    }

    private void closeLight() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestHFJ_JG(8, 2, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.HighTimeMeterDialog.5
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass5() {
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.HighTimeMeterDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass5() {
        }
    }
}

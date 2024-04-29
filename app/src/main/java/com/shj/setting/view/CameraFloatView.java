package com.shj.setting.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import com.baidu.mapapi.UIMsg;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.setting.R;
import com.shj.setting.Utils.CameraUtils;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.machine.monitor.CamaraDataUpload;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: classes2.dex */
public class CameraFloatView implements SurfaceHolder.Callback {
    private static CameraFloatView cameraFloatView;
    private static Boolean isShown = false;
    private static String machinaId;
    private Bitmap mBitmap;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private View mView;
    private int takepicCount;
    private WindowManager wm;
    private final int CAMERA_WIDTH = 640;
    private final int CAMERA_HEIGH = NNTPReply.AUTHENTICATION_REQUIRED;
    private String picDir = SDFileUtils.SDCardRoot + "xyShj/CamaraPic/";
    private long takeTime = 0;
    public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() { // from class: com.shj.setting.view.CameraFloatView.3
        AnonymousClass3() {
        }

        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            String str = CameraFloatView.machinaId + "_" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
            Loger.writeLog("UI", "正在保存拍摄图片...");
            CameraFloatView.this.mBitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            File file = new File(CameraFloatView.this.picDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String str2 = CameraFloatView.this.picDir + str + ".jpg";
            File file2 = new File(str2);
            try {
                file2.createNewFile();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                CameraFloatView.this.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                SDFileUtils.ZipFolder(str2, CameraFloatView.this.picDir + str + ".zip");
                file2.delete();
                Loger.writeLog("UI", "拍摄图片保存成功");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            CameraFloatView.this.mCamera.takePicture(null, null, null);
            if (CameraFloatView.this.mCamera != null) {
                CameraFloatView.this.mCamera.startPreview();
            }
        }
    };

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    static /* synthetic */ int access$210(CameraFloatView cameraFloatView2) {
        int i = cameraFloatView2.takepicCount;
        cameraFloatView2.takepicCount = i - 1;
        return i;
    }

    public static CameraFloatView getinstance() {
        if (cameraFloatView == null) {
            cameraFloatView = new CameraFloatView();
        }
        return cameraFloatView;
    }

    public void showFloatBox(Context context) {
        String machineId = AppSetting.getMachineId(context, null);
        machinaId = machineId;
        if (TextUtils.isEmpty(machineId)) {
            machinaId = "0000000000";
        }
        showFloat(context);
    }

    public void showFloat(Context context) {
        int i;
        deleteFile();
        boolean booleanValue = AppSetting.getCameraAutoTake(context, null).booleanValue();
        if (isShown.booleanValue()) {
            return;
        }
        isShown = true;
        this.wm = (WindowManager) context.getSystemService("window");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT >= 24) {
            i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        } else {
            i = UIMsg.m_AppUI.MSG_APP_VERSION_FORCE;
        }
        layoutParams.type = i;
        layoutParams.flags = 131112;
        layoutParams.format = -3;
        if (booleanValue) {
            layoutParams.width = 1;
            layoutParams.height = 1;
        } else {
            layoutParams.width = (int) context.getResources().getDimension(R.dimen.x640);
            layoutParams.height = (int) context.getResources().getDimension(R.dimen.y480);
        }
        layoutParams.gravity = 51;
        if (booleanValue) {
            this.mView = LayoutInflater.from(context).inflate(R.layout.layout_camerafloatview_small, (ViewGroup) null);
        } else {
            this.mView = LayoutInflater.from(context).inflate(R.layout.layout_camerafloatview, (ViewGroup) null);
        }
        this.mView.setOnTouchListener(new View.OnTouchListener() { // from class: com.shj.setting.view.CameraFloatView.1
            float lastX;
            float lastY;
            int oldOffsetX;
            int oldOffsetY;
            int tag = 0;
            final /* synthetic */ WindowManager.LayoutParams val$params;

            AnonymousClass1(WindowManager.LayoutParams layoutParams2) {
                layoutParams = layoutParams2;
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (this.tag == 0) {
                    this.oldOffsetX = layoutParams.x;
                    this.oldOffsetY = layoutParams.y;
                }
                if (action == 0) {
                    this.lastX = x;
                    this.lastY = y;
                } else if (action == 2) {
                    layoutParams.x += ((int) (x - this.lastX)) / 3;
                    layoutParams.y += ((int) (y - this.lastY)) / 3;
                    this.tag = 1;
                    if (CameraFloatView.this.mView != null) {
                        CameraFloatView.this.wm.updateViewLayout(CameraFloatView.this.mView, layoutParams);
                    }
                } else if (action == 1) {
                    int i2 = layoutParams.x;
                    int i3 = layoutParams.y;
                    if (Math.abs(this.oldOffsetX - i2) <= 20 && Math.abs(this.oldOffsetY - i3) <= 20) {
                        CameraFloatView.this.takePic();
                    } else {
                        this.tag = 0;
                    }
                }
                return true;
            }
        });
        ((Button) this.mView.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.view.CameraFloatView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraFloatView.this.closeFloatBox();
            }
        });
        this.mSurfaceView = (SurfaceView) this.mView.findViewById(R.id.verifySurfaceView);
        initSurface(context);
        this.wm.addView(this.mView, layoutParams2);
    }

    /* renamed from: com.shj.setting.view.CameraFloatView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnTouchListener {
        float lastX;
        float lastY;
        int oldOffsetX;
        int oldOffsetY;
        int tag = 0;
        final /* synthetic */ WindowManager.LayoutParams val$params;

        AnonymousClass1(WindowManager.LayoutParams layoutParams2) {
            layoutParams = layoutParams2;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.tag == 0) {
                this.oldOffsetX = layoutParams.x;
                this.oldOffsetY = layoutParams.y;
            }
            if (action == 0) {
                this.lastX = x;
                this.lastY = y;
            } else if (action == 2) {
                layoutParams.x += ((int) (x - this.lastX)) / 3;
                layoutParams.y += ((int) (y - this.lastY)) / 3;
                this.tag = 1;
                if (CameraFloatView.this.mView != null) {
                    CameraFloatView.this.wm.updateViewLayout(CameraFloatView.this.mView, layoutParams);
                }
            } else if (action == 1) {
                int i2 = layoutParams.x;
                int i3 = layoutParams.y;
                if (Math.abs(this.oldOffsetX - i2) <= 20 && Math.abs(this.oldOffsetY - i3) <= 20) {
                    CameraFloatView.this.takePic();
                } else {
                    this.tag = 0;
                }
            }
            return true;
        }
    }

    /* renamed from: com.shj.setting.view.CameraFloatView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CameraFloatView.this.closeFloatBox();
        }
    }

    public void closeFloatBox() {
        View view;
        WindowManager windowManager;
        if (!isShown.booleanValue() || (view = this.mView) == null || (windowManager = this.wm) == null) {
            return;
        }
        try {
            windowManager.removeView(view);
            isShown = false;
            this.mView = null;
            this.wm = null;
            cameraFloatView = null;
            Camera camera = this.mCamera;
            if (camera != null) {
                camera.setPreviewCallback(null);
                this.mCamera.stopPreview();
                this.mCamera.release();
                this.mCamera = null;
            }
        } catch (Exception e) {
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
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    private void initSurface(Context context) {
        SurfaceHolder holder = this.mSurfaceView.getHolder();
        this.mSurfaceHolder = holder;
        holder.addCallback(this);
        String surveillanceCameraPidVid = AppSetting.getSurveillanceCameraPidVid(context, null);
        if (!TextUtils.isEmpty(surveillanceCameraPidVid)) {
            int cameraIndex = SetUtils.getCameraIndex(surveillanceCameraPidVid);
            if (cameraIndex == -1) {
                ToastUitl.showShort(context, "未找到摄像头");
                return;
            } else {
                if (OpenCameraAndSetSurfaceviewSize(context, cameraIndex)) {
                    return;
                }
                ToastUitl.showShort(context, R.string.camara_can_not_use);
                this.mView.setVisibility(8);
                return;
            }
        }
        ToastUitl.showShort(context, "未设置PID与VID");
    }

    private boolean OpenCameraAndSetSurfaceviewSize(Context context, int i) {
        try {
            if (this.mCamera == null) {
                this.mCamera = Camera.open(i);
            }
            try {
                Camera.Parameters parameters = this.mCamera.getParameters();
                parameters.setPreviewSize(640, NNTPReply.AUTHENTICATION_REQUIRED);
                this.mCamera.setParameters(parameters);
                return true;
            } catch (Exception unused) {
                ToastUitl.showShort(context, "预览尺寸不支持");
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
            Camera.Parameters parameters = this.mCamera.getParameters();
            parameters.setPreviewFormat(17);
            parameters.setPreviewFpsRange(10, 15);
            this.mCamera.setPreviewCallback(new VerifyPreview());
            this.mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes2.dex */
    public class VerifyPreview implements Camera.PreviewCallback {
        VerifyPreview() {
        }

        @Override // android.hardware.Camera.PreviewCallback
        public void onPreviewFrame(byte[] bArr, Camera camera) {
            CamaraDataUpload.upload("192.168.2.121", 6000, bArr, camera);
            if (CameraFloatView.this.takepicCount <= 0) {
                return;
            }
            CameraFloatView.access$210(CameraFloatView.this);
            CameraFloatView.this.mBitmap = CameraUtils.getBitMap(bArr, camera);
            String str = CameraFloatView.machinaId + "_" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
            Loger.writeLog("UI", "正在保存拍摄图片...");
            File file = new File(CameraFloatView.this.picDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String str2 = CameraFloatView.this.picDir + str + ".jpg";
            File file2 = new File(str2);
            try {
                file2.createNewFile();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                CameraFloatView.this.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                SDFileUtils.ZipFolder(str2, CameraFloatView.this.picDir + str + ".zip");
                file2.delete();
                Loger.writeLog("UI", "拍摄图片保存成功");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void takePic() {
        if (this.mCamera == null || System.currentTimeMillis() - this.takeTime < 1000) {
            return;
        }
        this.takeTime = System.currentTimeMillis();
        this.takepicCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.view.CameraFloatView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Camera.PictureCallback {
        AnonymousClass3() {
        }

        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            String str = CameraFloatView.machinaId + "_" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
            Loger.writeLog("UI", "正在保存拍摄图片...");
            CameraFloatView.this.mBitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            File file = new File(CameraFloatView.this.picDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String str2 = CameraFloatView.this.picDir + str + ".jpg";
            File file2 = new File(str2);
            try {
                file2.createNewFile();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                CameraFloatView.this.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                SDFileUtils.ZipFolder(str2, CameraFloatView.this.picDir + str + ".zip");
                file2.delete();
                Loger.writeLog("UI", "拍摄图片保存成功");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            CameraFloatView.this.mCamera.takePicture(null, null, null);
            if (CameraFloatView.this.mCamera != null) {
                CameraFloatView.this.mCamera.startPreview();
            }
        }
    }

    private void deleteFile() {
        File file = new File(this.picDir);
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                if (needDelete(file2.getName())) {
                    file2.delete();
                }
            }
        }
    }

    private boolean needDelete(String str) {
        try {
            String substring = str.substring(11, 21);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(substring);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - date.getTime() > 432000000) {
                return true;
            }
            Log.e("needDelete", "date.getTime(); =====" + date.getTime());
            return false;
        } catch (Exception unused) {
            return true;
        }
    }
}

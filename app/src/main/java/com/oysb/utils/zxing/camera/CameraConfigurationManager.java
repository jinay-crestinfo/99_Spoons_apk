package com.oysb.utils.zxing.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public final class CameraConfigurationManager {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final int DESIRED_SHARPNESS = 30;
    private static final String TAG = "CameraConfigurationManager";
    private static final int TEN_DESIRED_ZOOM = 27;
    private Point cameraResolution;
    private final Context context;
    private int previewFormat;
    private String previewFormatString;
    private Point screenResolution;

    public static int getDesiredSharpness() {
        return 30;
    }

    public CameraConfigurationManager(Context context) {
        this.context = context;
    }

    public void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        this.previewFormat = parameters.getPreviewFormat();
        this.previewFormatString = parameters.get("preview-format");
        String str = TAG;
        Log.d(str, "Default preview format: " + this.previewFormat + '/' + this.previewFormatString);
        Display defaultDisplay = ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay();
        this.screenResolution = new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        Log.d(str, "Screen resolution: " + this.screenResolution);
        this.cameraResolution = getCameraResolution(parameters, this.screenResolution);
        Log.d(str, "Camera resolution: " + this.screenResolution);
    }

    public void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Log.d(TAG, "Setting preview size: " + this.cameraResolution);
        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        setFlash(parameters);
        setZoom(parameters);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
    }

    public Point getCameraResolution() {
        return this.cameraResolution;
    }

    public Point getScreenResolution() {
        return this.screenResolution;
    }

    public int getPreviewFormat() {
        return this.previewFormat;
    }

    public String getPreviewFormatString() {
        return this.previewFormatString;
    }

    private static Point getCameraResolution(Camera.Parameters parameters, Point point) {
        String str = parameters.get("preview-size-values");
        if (str == null) {
            str = parameters.get("preview-size-value");
        }
        Point point2 = null;
        if (str != null) {
            Log.d(TAG, "preview-size-values parameter: " + str);
            point2 = findBestPreviewSizeValue(str, point);
        }
        return point2 == null ? new Point((point.x >> 3) << 3, (point.y >> 3) << 3) : point2;
    }

    private static Point findBestPreviewSizeValue(CharSequence charSequence, Point point) {
        String[] split = COMMA_PATTERN.split(charSequence);
        int length = split.length;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String trim = split[i2].trim();
            int indexOf = trim.indexOf(120);
            if (indexOf < 0) {
                Log.w(TAG, "Bad preview-size: " + trim);
            } else {
                try {
                    int parseInt = Integer.parseInt(trim.substring(0, indexOf));
                    int parseInt2 = Integer.parseInt(trim.substring(indexOf + 1));
                    int abs = Math.abs(parseInt - point.x) + Math.abs(parseInt2 - point.y);
                    if (abs == 0) {
                        i4 = parseInt2;
                        i3 = parseInt;
                        break;
                    }
                    if (abs < i) {
                        i4 = parseInt2;
                        i = abs;
                        i3 = parseInt;
                    }
                } catch (NumberFormatException unused) {
                    Log.w(TAG, "Bad preview-size: " + trim);
                }
            }
            i2++;
        }
        if (i3 <= 0 || i4 <= 0) {
            return null;
        }
        return new Point(i3, i4);
    }

    private static int findBestMotZoomValue(CharSequence charSequence, int i) {
        int i2 = 0;
        for (String str : COMMA_PATTERN.split(charSequence)) {
            try {
                double parseDouble = Double.parseDouble(str.trim());
                int i3 = (int) (10.0d * parseDouble);
                double d = i;
                Double.isNaN(d);
                if (Math.abs(d - parseDouble) < Math.abs(i - i2)) {
                    i2 = i3;
                }
            } catch (NumberFormatException unused) {
                return i;
            }
        }
        return i2;
    }

    private void setFlash(Camera.Parameters parameters) {
        if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) {
            parameters.set("flash-value", 1);
        } else {
            parameters.set("flash-value", 2);
        }
        parameters.set("flash-mode", "off");
    }

    private void setZoom(Camera.Parameters parameters) {
        String str = parameters.get("zoom-supported");
        if (str == null || Boolean.parseBoolean(str)) {
            int i = 27;
            String str2 = parameters.get("max-zoom");
            if (str2 != null) {
                try {
                    int parseDouble = (int) (Double.parseDouble(str2) * 10.0d);
                    if (27 > parseDouble) {
                        i = parseDouble;
                    }
                } catch (NumberFormatException unused) {
                    Log.w(TAG, "Bad max-zoom: " + str2);
                }
            }
            String str3 = parameters.get("taking-picture-zoom-max");
            if (str3 != null) {
                try {
                    int parseInt = Integer.parseInt(str3);
                    if (i > parseInt) {
                        i = parseInt;
                    }
                } catch (NumberFormatException unused2) {
                    Log.w(TAG, "Bad taking-picture-zoom-max: " + str3);
                }
            }
            String str4 = parameters.get("mot-zoom-values");
            if (str4 != null) {
                i = findBestMotZoomValue(str4, i);
            }
            String str5 = parameters.get("mot-zoom-step");
            if (str5 != null) {
                try {
                    int parseDouble2 = (int) (Double.parseDouble(str5.trim()) * 10.0d);
                    if (parseDouble2 > 1) {
                        i -= i % parseDouble2;
                    }
                } catch (NumberFormatException unused3) {
                }
            }
            if (str2 != null || str4 != null) {
                double d = i;
                Double.isNaN(d);
                parameters.set("zoom", String.valueOf(d / 10.0d));
            }
            if (str3 != null) {
                parameters.set("taking-picture-zoom", i);
            }
        }
    }
}

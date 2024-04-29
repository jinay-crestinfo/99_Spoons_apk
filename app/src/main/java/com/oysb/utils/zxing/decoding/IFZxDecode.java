package com.oysb.utils.zxing.decoding;

import android.graphics.Bitmap;
import android.os.Handler;
import com.google.zxing.Result;
import com.oysb.utils.zxing.view.ViewfinderView;

/* loaded from: classes2.dex */
public interface IFZxDecode {
    void drawViewfinder();

    Handler getHandler();

    ViewfinderView getViewfinderView();

    void handleDecode(Result result, Bitmap bitmap);
}

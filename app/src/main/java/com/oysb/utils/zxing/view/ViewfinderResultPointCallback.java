package com.oysb.utils.zxing.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/* loaded from: classes2.dex */
public final class ViewfinderResultPointCallback implements ResultPointCallback {
    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    @Override // com.google.zxing.ResultPointCallback
    public void foundPossibleResultPoint(ResultPoint resultPoint) {
        this.viewfinderView.addPossibleResultPoint(resultPoint);
    }
}

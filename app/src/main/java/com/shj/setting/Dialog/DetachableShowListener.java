package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ViewTreeObserver;

/* loaded from: classes2.dex */
public class DetachableShowListener implements DialogInterface.OnShowListener {
    private DialogInterface.OnShowListener delegateOrNull;

    public static DetachableShowListener wrap(DialogInterface.OnShowListener onShowListener) {
        return new DetachableShowListener(onShowListener);
    }

    private DetachableShowListener(DialogInterface.OnShowListener onShowListener) {
        this.delegateOrNull = onShowListener;
    }

    @Override // android.content.DialogInterface.OnShowListener
    public void onShow(DialogInterface dialogInterface) {
        DialogInterface.OnShowListener onShowListener = this.delegateOrNull;
        if (onShowListener != null) {
            onShowListener.onShow(dialogInterface);
        }
    }

    public void clearOnDetach(Dialog dialog) {
        if (Build.VERSION.SDK_INT >= 18) {
            dialog.getWindow().getDecorView().getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() { // from class: com.shj.setting.Dialog.DetachableShowListener.1
                @Override // android.view.ViewTreeObserver.OnWindowAttachListener
                public void onWindowAttached() {
                }

                AnonymousClass1() {
                }

                @Override // android.view.ViewTreeObserver.OnWindowAttachListener
                public void onWindowDetached() {
                    DetachableShowListener.this.delegateOrNull = null;
                }
            });
        }
    }

    /* renamed from: com.shj.setting.Dialog.DetachableShowListener$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ViewTreeObserver.OnWindowAttachListener {
        @Override // android.view.ViewTreeObserver.OnWindowAttachListener
        public void onWindowAttached() {
        }

        AnonymousClass1() {
        }

        @Override // android.view.ViewTreeObserver.OnWindowAttachListener
        public void onWindowDetached() {
            DetachableShowListener.this.delegateOrNull = null;
        }
    }
}

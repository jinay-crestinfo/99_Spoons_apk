package com.xyshj.machine.app;

import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;

/* loaded from: classes.dex */
public class SysApp extends ShjAppBase {
    public static String getPriceUnit() {
        Object obj = ShjAppHelper.getAppSetting().get("货币符号");
        if (obj != null) {
            return obj.toString();
        }
        return ShjAppHelper.getString(R.string.priceunit);
    }
}

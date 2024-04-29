package com.xyshj.machine.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.xyshj.machine.R;

/* loaded from: classes2.dex */
public class NetConnectTipUtils {
    public static boolean checkNetConnect(Context context) {
        if (isConnected(context)) {
            return true;
        }
        Toast makeText = Toast.makeText(context, R.string.net_available_tip, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
        return false;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

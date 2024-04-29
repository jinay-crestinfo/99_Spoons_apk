package com.shj.setting.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import com.oysb.utils.Loger;
import com.oysb.utils.activity.ActivityHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.file.SDFileUtils;
import java.io.File;

/* loaded from: classes2.dex */
public class ReinstallUtils {
    public static void reinstall(Context context, String str) {
        if (ActivityHelper.checkApkExist(context, str)) {
            String backupsApp = SDFileUtils.backupsApp(context, str, SDFileUtils.SDCardRoot + "xyShj/backUpApp/", true);
            if (backupsApp != null) {
                if (new File(backupsApp).exists()) {
                    CacheHelper.getFileCache().put("needReinstallPackagePath", backupsApp);
                    context.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", str, null)));
                    return;
                }
                return;
            }
            Loger.writeLog("SHJ", str + "备份失败");
            return;
        }
        Loger.writeLog("SHJ", str + "未安装");
    }

    public static void installApk(Context context, String str) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(268435456);
            Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", new File(str));
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
            intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
        } else {
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}

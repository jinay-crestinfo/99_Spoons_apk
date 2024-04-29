package com.shj.biz;

import com.oysb.utils.AndroidSystem;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.io.XyStreamParser;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.Shj;
import com.shj.biz.tools.FileUploader;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ShjPushMessageManager {
    public static void onPushMessage(String str) {
        String str2 = "";
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("machineCode");
            str2 = jSONObject.getString("context");
            if (string.length() > 0) {
                if (!string.equalsIgnoreCase(Shj.getMachineId())) {
                    return;
                }
            }
        } catch (JSONException unused) {
        }
        Loger.writeLog("PUSH", str);
        try {
            if (str.startsWith("STREAM_LOG:")) {
                String[] split = str.split(":");
                Integer valueOf = Integer.valueOf(Integer.parseInt(split[1]));
                if (valueOf.intValue() == 0) {
                    XyStreamParser.clearLogDetailNames();
                }
                if (1 == valueOf.intValue()) {
                    XyStreamParser.addLogDetailNames(split[2]);
                    return;
                }
                return;
            }
            if (str.equals("RESTART") || str2.equals("RESTART")) {
                AndroidSystem.setNeedRestartApp(true, "远程指令:" + str);
                return;
            }
            if (str.equals("REBOOT") || str2.equals("REBOOT")) {
                AndroidSystem.setNeedRebootSystem(true, "远程指令:" + str);
                return;
            }
            if (str.startsWith("lock:")) {
                ShjManager.getGoodsManager().updateGoodsReseveInfosByServerCommand(str.substring(5));
                return;
            }
            if (str.startsWith("cxLog")) {
                FileUploader.copyLog(str);
                return;
            }
            if (str.startsWith("file")) {
                FileUploader.fileOperate(str);
                return;
            }
            if (str.equals("REUPDATE") || str2.equals("REUPDATE")) {
                try {
                    Loger.writeLog("APP;SHJ", CacheHelper.getFileCache().getAsString("清除前 lastUpdateApp").toString());
                } catch (Exception unused2) {
                }
                AppUpdateHelper.clearUpdateCount();
                AppUpdateHelper.clearAppPackages(ShjManager.getAppContext(), SDFileUtils.SDCardRoot + "xyShj/update/");
                ShjManager.getStatusListener().onNeedReUpdateApp("远程指令：" + str);
                Loger.writeLog("APP;SHJ", CacheHelper.getFileCache().getAsString("清除后 lastUpdateApp").toString());
            }
        } catch (Exception unused3) {
        }
    }
}

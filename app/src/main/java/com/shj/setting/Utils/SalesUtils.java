package com.shj.setting.Utils;

import android.text.TextUtils;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class SalesUtils {
    public static List<String> getSalesDescribe(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 18; i += 2) {
            arrayList.add(String.valueOf(ObjectHelper.intFromBytes(bArr, i * 4, 4)));
            double longFromBytes = ObjectHelper.longFromBytes(bArr, (i + 1) * 4, 4);
            Double.isNaN(longFromBytes);
            arrayList.add(String.valueOf(longFromBytes / 100.0d));
        }
        return arrayList;
    }

    public static void updateWinxinFaceStateCount(boolean z) {
        try {
            String format = new SimpleDateFormat(DateUtil.YMD).format(new Date(System.currentTimeMillis()));
            String asString = CacheHelper.getFileCache().getAsString("weixin_face_fail_count");
            if (asString != null) {
                String[] split = asString.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                int length = split.length;
                boolean z2 = false;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    if (format.equals(split[i])) {
                        z2 = true;
                        break;
                    }
                    i++;
                }
                if (!z2) {
                    CacheHelper.getFileCache().put("weixin_face_fail_count", asString + VoiceWakeuperAidl.PARAMS_SEPARATE + format);
                }
            } else {
                CacheHelper.getFileCache().put("weixin_face_fail_count", format);
            }
            int i2 = z ? 10000 : 1;
            String asString2 = CacheHelper.getFileCache().getAsString("weixin_face_fail_count_" + format);
            if (asString2 != null) {
                i2 += Integer.valueOf(asString2).intValue();
            }
            CacheHelper.getFileCache().put("weixin_face_fail_count_" + format, String.valueOf(i2));
        } catch (Exception e) {
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e.getMessage());
        }
    }

    public static void reportWinxinFailCount() {
        try {
            String format = new SimpleDateFormat(DateUtil.YMD).format(new Date(System.currentTimeMillis()));
            String asString = CacheHelper.getFileCache().getAsString("weixin_face_fail_count");
            if (asString != null) {
                String str = "";
                for (String str2 : asString.split(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    if (!format.equals(str2)) {
                        String asString2 = CacheHelper.getFileCache().getAsString("weixin_face_fail_count_" + str2);
                        CacheHelper.getFileCache().remove("weixin_face_fail_count_" + str2);
                        int intValue = Integer.valueOf(asString2).intValue();
                        str = str + str2 + " 刷脸取消次数:" + (intValue % 10000) + "刷脸成功次数:" + (intValue / 10000) + VoiceWakeuperAidl.PARAMS_SEPARATE;
                    } else {
                        CacheHelper.getFileCache().put("weixin_face_fail_count", format);
                    }
                }
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_Serial, "", str);
            }
        } catch (Exception e) {
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e.getMessage());
        }
    }
}

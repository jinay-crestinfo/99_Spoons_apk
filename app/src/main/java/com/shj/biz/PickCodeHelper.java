package com.shj.biz;

import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report_Transfdata;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PickCodeHelper {
    public static final int PICKCODE_TRANSF_CODE = 257;
    static HashMap<String, String> codeMap;

    public static boolean isPickCodeResult(int i) {
        return i == 257;
    }

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        codeMap = hashMap;
        hashMap.put("H0000", "取货验证成功");
        codeMap.put("S0000", "机器交易异常，请联系售后");
        codeMap.put("S0001", "机器正在交易中,请稍后重试");
        codeMap.put("S0002", "取货码无效");
        codeMap.put("S0003", "取货码不能为空，请输入正确的取货码");
        codeMap.put("S0004", "机器交易异常，请联系管理员");
        codeMap.put("S0005", "货道卡货");
        codeMap.put("S0006", "机器离线");
    }

    public static String getResultInfo(String str) {
        try {
            return codeMap.get(str);
        } catch (Exception unused) {
            return str;
        }
    }

    public static void tryPickCode(String str) {
        Report_Transfdata report_Transfdata = new Report_Transfdata();
        report_Transfdata.setParams(257, str);
        report_Transfdata.setLostAble(false);
        NetManager.appendReport(report_Transfdata);
    }
}

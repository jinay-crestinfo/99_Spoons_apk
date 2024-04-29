package com.oysb.xy.net.thirdparty;

import com.oysb.utils.Loger;

/* loaded from: classes2.dex */
public class ThirdPartyReport_MachineSignal extends ThirdPartyReport {
    public ThirdPartyReport_MachineSignal() {
        this.FunCode = "1003";
    }

    public void setParams(String str, String str2, String str3) {
        this.PackNO = str;
        this.MachineSN = str2;
        this.info = str3;
        Loger.writeLog("APP", "上报机器信号位置参数：" + this.info);
    }
}

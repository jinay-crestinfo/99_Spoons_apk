package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0xFE")
/* loaded from: classes2.dex */
public class Command_Down_ReportPosInfo extends Command {
    private String info = null;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.info = "";
        for (int i = 2; i <= 17; i++) {
            this.info += String.valueOf((int) bArr[i]);
        }
        Loger.writeLog("SHJ", "收到下位机上报：机请求显示 0xFE info" + this.info);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onUpdatePosInfo(this.info);
    }
}

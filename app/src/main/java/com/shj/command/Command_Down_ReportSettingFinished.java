package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0x22")
/* loaded from: classes2.dex */
public class Command_Down_ReportSettingFinished extends Command {
    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        Loger.writeLog("SHJ", "收到下位机上报：设置完成  0x22 ok" + init);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onSettingFinished();
    }
}

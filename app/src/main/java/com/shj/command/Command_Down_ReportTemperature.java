package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0x11")
/* loaded from: classes2.dex */
public class Command_Down_ReportTemperature extends Command {
    private int temperature = -1;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.temperature = (bArr[2] * 256) + bArr[3];
        Loger.writeLog("SHJ", "收到下位机上报：温度  0x0D temperature" + this.temperature);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.setTemperature(0, this.temperature);
    }
}

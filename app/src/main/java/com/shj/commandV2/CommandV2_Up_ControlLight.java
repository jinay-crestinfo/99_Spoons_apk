package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_ControlLight extends CommandV2 {
    int jgh = -1;
    int status = 0;

    public CommandV2_Up_ControlLight() {
        setType(Command.CommandType.Command);
        setHead((short) 104);
    }

    public void setParams(int i, int i2) {
        this.jgh = i;
        this.status = i2;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 1, 1);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "灯光控制命令已下发 jgh:" + this.jgh + " status:" + this.status);
    }
}

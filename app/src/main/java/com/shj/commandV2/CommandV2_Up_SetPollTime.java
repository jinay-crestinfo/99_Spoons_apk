package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_SetPollTime extends CommandV2 {
    int passTime;

    public CommandV2_Up_SetPollTime() {
        setType(Command.CommandType.Command);
        setHead((short) 22);
    }

    public void setParams(int i) {
        this.dataV2 = new byte[1];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 1);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SHJ;SET", "上位机设置poll时间:" + this.passTime);
    }
}

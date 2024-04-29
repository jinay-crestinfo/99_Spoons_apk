package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_QueryShjStatus extends CommandV2 {
    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public CommandV2_Up_QueryShjStatus() {
        setType(Command.CommandType.Command);
        setHead((short) 81);
    }

    public void setParams(int i, int i2) {
        this.dataV2 = new byte[6];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
        ObjectHelper.updateBytes(this.dataV2, i2, 2, 4);
    }
}

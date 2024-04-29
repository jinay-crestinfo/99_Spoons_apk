package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_QueryICCardInfo extends CommandV2 {
    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public CommandV2_Up_QueryICCardInfo() {
        setType(Command.CommandType.Command);
        setHead((short) 97);
    }

    public void setParams(boolean z) {
        this.dataV2 = new byte[1];
        ObjectHelper.updateBytes(this.dataV2, z ? 1 : 2, 0, 1);
    }
}

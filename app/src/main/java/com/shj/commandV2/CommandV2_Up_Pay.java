package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Pay extends CommandV2 {
    int money = 0;

    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public CommandV2_Up_Pay() {
        setType(Command.CommandType.Command);
        setHead((short) 100);
    }

    public void setParams(int i) {
        this.money = i;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 4);
    }
}

package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_CheckShelf extends CommandV2 {
    public CommandV2_Up_CheckShelf() {
        setType(Command.CommandType.Command);
        setHead((short) 1);
    }

    public void setParams(int i) {
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.setOfferGoodsDiviceState(0);
    }
}

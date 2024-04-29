package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_queryShelfDoorStatus extends CommandV2 {
    int shelf;

    public CommandV2_Up_queryShelfDoorStatus() {
        setType(Command.CommandType.Command);
        setHead((short) 104);
    }

    public void setParams(int i) {
        this.shelf = i;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "查询货道门状态 shelf: " + this.shelf);
    }
}

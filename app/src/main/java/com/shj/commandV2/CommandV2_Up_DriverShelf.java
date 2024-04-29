package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_DriverShelf extends CommandV2 {
    int shelf = -1;

    public CommandV2_Up_DriverShelf() {
        setType(Command.CommandType.Command);
        setHead((short) 6);
    }

    public void setParams(int i, boolean z, boolean z2) {
        this.shelf = i;
        setParamsV2(i, z, z2, false, 1);
    }

    public void setParamsV2(int i, boolean z, boolean z2, boolean z3, int i2) {
        this.shelf = i;
        this.dataV2 = new byte[6];
        ObjectHelper.updateBytes(this.dataV2, z ? 1 : 0, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, z2 ? 1 : 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
        ObjectHelper.updateBytes(this.dataV2, z3 ? 1 : 0, 4, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 5, 1);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "驱动货道命令已下发 ");
        Shj.setDrivedShelf(this.shelf);
    }
}

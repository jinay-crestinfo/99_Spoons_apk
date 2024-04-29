package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_DriverGoodsShelf extends Command {
    int shelf;

    public Command_Up_DriverGoodsShelf() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 7, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i, boolean z) {
        this.shelf = i;
        ObjectHelper.updateBytes(this.data, i, 2, 2);
        if (z) {
            this.data[4] = 1;
        }
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "驱动货道命令已下发 ");
        Shj.setDrivedShelf(this.shelf);
        super.doCommand();
    }
}

package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SelectGoods extends Command {
    public Command_Up_SelectGoods() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 1, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.set2SelectShelf(ObjectHelper.intFromBytes(this.data, 2, 2));
        super.doCommand();
    }
}

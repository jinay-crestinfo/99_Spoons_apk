package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SetShelfGoodsCount extends Command {
    public Command_Up_SetShelfGoodsCount() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 5, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i, int i2) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
        ObjectHelper.updateBytes(this.data, i2, 4, 1);
    }
}

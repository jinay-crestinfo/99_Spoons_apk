package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SetShelfGoodsCode extends Command {
    public Command_Up_SetShelfGoodsCode() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 49, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i, String str) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
        ObjectHelper.updateBytes(this.data, Integer.parseInt(str), 4, 2);
    }
}

package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_TestGoodsIsValid extends Command {
    public Command_Up_TestGoodsIsValid() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 8, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
    }
}

package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_TestGoodsShelfIsExist extends Command {
    public Command_Up_TestGoodsShelfIsExist() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 32, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setShelf(int i) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
    }
}

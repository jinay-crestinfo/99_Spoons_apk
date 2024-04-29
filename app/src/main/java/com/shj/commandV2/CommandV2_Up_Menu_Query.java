package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Menu_Query extends CommandV2 {
    public CommandV2_Up_Menu_Query(int i) {
        setType(Command.CommandType.Command);
        setHead((short) 112);
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }
}

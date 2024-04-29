package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Swallowing_Money_Time extends CommandV2 {
    public CommandV2_Up_Swallowing_Money_Time() {
        setType(Command.CommandType.Command);
        setHead((short) 112);
    }

    public void setParams(int i) {
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, 34, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }
}

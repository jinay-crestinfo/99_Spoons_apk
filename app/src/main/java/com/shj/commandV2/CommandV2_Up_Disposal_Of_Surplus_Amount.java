package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Disposal_Of_Surplus_Amount extends CommandV2 {
    public static final int MODE_LOOKING_FOR_MONEY = 2;
    public static final int MODE_SWALLOWING_LOOKING = 3;
    public static final int MODE_SWALLOWING_MONEY = 1;

    public CommandV2_Up_Disposal_Of_Surplus_Amount() {
        setType(Command.CommandType.Command);
        setHead((short) 112);
    }

    public void setParams(int i) {
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, 35, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }
}

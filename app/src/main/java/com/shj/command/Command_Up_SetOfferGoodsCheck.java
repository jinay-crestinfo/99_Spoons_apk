package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SetOfferGoodsCheck extends Command {
    public Command_Up_SetOfferGoodsCheck() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 6, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i, boolean z) {
        ObjectHelper.updateBytes(this.data, i, 2, 2);
        if (z) {
            this.data[4] = 1;
        }
    }
}

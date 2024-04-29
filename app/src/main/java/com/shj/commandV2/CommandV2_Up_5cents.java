package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_5cents extends CommandV2 {
    public CommandV2_Up_5cents() {
        setType(Command.CommandType.Command);
        setHead((short) 112);
    }

    public void setParams(boolean z, int i) {
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, 20, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, 20, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }
}

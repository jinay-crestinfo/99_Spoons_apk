package com.shj.command;

import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SetCoinType extends Command {
    public Command_Up_SetCoinType() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, ClosedCaptionCtrl.MISC_CHAN_1, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(int i) {
        ObjectHelper.updateBytes(this.data, i, 2, 1);
    }
}

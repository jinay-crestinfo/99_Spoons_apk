package com.shj.commandV2;

import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_BatchEnd extends CommandV2 {
    public void setParams() {
    }

    public CommandV2_Up_BatchEnd() {
        setType(Command.CommandType.VCMD);
        setVCmd(true);
        init(new byte[]{-6, 19, 0, 0, 0, 0, 0, 0, -1});
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.onBatchEnd();
    }
}

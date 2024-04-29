package com.shj.command;

import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_BatchStart extends Command {
    public Command_Up_BatchStart() {
        setType(Command.CommandType.VCMD);
        setVCmd(true);
        init(new byte[]{-6, 19, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams() {
        this.data = "VCMD".getBytes();
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.onBatchStart();
    }
}

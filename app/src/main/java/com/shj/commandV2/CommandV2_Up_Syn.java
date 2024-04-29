package com.shj.commandV2;

import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Syn extends CommandV2 {
    public void setParams() {
    }

    public CommandV2_Up_Syn() {
        setType(Command.CommandType.Command);
        setHead((short) 49);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.onReset();
    }
}

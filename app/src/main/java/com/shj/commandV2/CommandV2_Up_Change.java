package com.shj.commandV2;

import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Change extends CommandV2 {
    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public void setParams() {
    }

    public CommandV2_Up_Change() {
        setType(Command.CommandType.Command);
        setHead((short) 37);
        setExpiredTime(2000L);
    }
}

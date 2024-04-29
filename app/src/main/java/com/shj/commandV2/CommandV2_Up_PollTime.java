package com.shj.commandV2;

import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_PollTime extends CommandV2 {
    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public CommandV2_Up_PollTime() {
        setType(Command.CommandType.Command);
        setHead((short) 22);
    }

    public void setParams(int i) {
        this.dataV2 = new byte[]{(byte) i};
    }
}

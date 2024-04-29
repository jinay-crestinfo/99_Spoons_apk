package com.shj.commandV2;

import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Empty extends Command {
    Object obj;

    public CommandV2_Up_Empty() {
        setType(Command.CommandType.VCMD);
        setHead((short) 255);
    }

    public Object getObj() {
        return this.obj;
    }

    public void setParams(Object obj) {
        this.obj = obj;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        setFinished();
    }
}

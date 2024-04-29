package com.shj.command;

import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_TopUp extends Command {
    public Command_Up_TopUp() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 18, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(boolean z) {
        if (z) {
            this.data[2] = 1;
        } else {
            this.data[2] = 2;
        }
    }
}

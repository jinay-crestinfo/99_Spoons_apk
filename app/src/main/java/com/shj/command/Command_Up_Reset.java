package com.shj.command;

import com.serotonin.modbus4j.code.FunctionCode;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_Reset extends Command {
    public Command_Up_Reset() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, FunctionCode.WRITE_COILS, 0, 0, 0, 0, 0, 0, -1});
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
    }
}

package com.shj.command;

import com.oysb.utils.anno.XYClass;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x0a")
/* loaded from: classes2.dex */
public class Command_Down_WaitCommand extends Command {
    static byte[] ackBytes = {-6, 10, 0, 0, 0, 0, 0, 0, -1};

    public Command_Down_WaitCommand() {
        setType(Command.CommandType.Wait);
    }

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        return super.init(bArr);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        synchronized (CommandManager.getSendCommandQueue()) {
            CommandManager.getSendCommandQueue().notifyAll();
        }
    }
}

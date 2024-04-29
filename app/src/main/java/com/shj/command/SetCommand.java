package com.shj.command;

/* loaded from: classes2.dex */
public class SetCommand extends Command {
    public static final short HEAD = 10;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        return 10 == bArr[1];
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        if (isInited()) {
            super.doCommand();
        }
    }
}

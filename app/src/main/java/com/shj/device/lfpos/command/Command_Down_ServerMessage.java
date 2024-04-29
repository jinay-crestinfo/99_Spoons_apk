package com.shj.device.lfpos.command;

import com.shj.device.lfpos.LfPos;

/* loaded from: classes2.dex */
public class Command_Down_ServerMessage extends Command {
    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        return super.init(bArr);
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        LfPos.cmd_onServerMessage(getID(), this.CONT);
        super.doCommand();
    }
}

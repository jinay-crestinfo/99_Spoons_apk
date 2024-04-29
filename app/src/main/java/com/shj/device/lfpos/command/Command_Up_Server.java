package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Server extends Command {
    public Command_Up_Server() {
        setType((byte) 0);
        setPath((byte) 6);
    }

    public void setCONT(byte[] bArr) {
        this.CONT = bArr;
    }
}

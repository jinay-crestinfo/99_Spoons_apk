package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Test extends Command {
    public Command_Up_Test() {
        setTradType("99");
        setPath((byte) 1);
        setType((byte) 0);
        this.CONT = getTradType().getBytes();
    }
}

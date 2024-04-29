package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Properties extends Command {
    public Command_Up_Properties() {
        setTradType("57");
        setType((byte) 0);
    }

    public void setParams(String str, String str2) {
        setParams(getTradType(), str, str2);
    }
}

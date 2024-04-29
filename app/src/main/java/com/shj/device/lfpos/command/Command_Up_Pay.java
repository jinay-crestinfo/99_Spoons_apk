package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Pay extends Command {
    public Command_Up_Pay() {
        setTradType("W1");
        setType((byte) 0);
    }

    public void setParams(int i, String str) {
        setParams(getTradType(), String.format("%12d", Integer.valueOf(i)), str);
    }
}

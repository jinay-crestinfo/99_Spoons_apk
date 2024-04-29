package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Sum extends Command {
    public Command_Up_Sum() {
        setTradType("63");
        setType((byte) 0);
    }

    public void setParams(String str, String str2, String str3, String str4) {
        setParams(getTradType(), str, str2, str3, str4);
    }
}

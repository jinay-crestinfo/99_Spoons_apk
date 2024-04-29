package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Pay_Pos extends Command {
    public Command_Up_Pay_Pos() {
        setTradType("W3");
        setType((byte) 0);
    }

    public void setParams(String str, String str2, String str3) {
        setParams(getTradType(), str, str2, str3);
    }
}

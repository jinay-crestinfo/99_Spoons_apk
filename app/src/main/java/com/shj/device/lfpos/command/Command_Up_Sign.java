package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Sign extends Command {
    public Command_Up_Sign() {
        setTradType("51");
        setType((byte) 0);
    }

    public void setParams(String str, String str2, String str3) {
        setParams(getTradType(), str, str2, str3);
    }
}

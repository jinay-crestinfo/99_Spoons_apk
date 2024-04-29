package com.shj.device.lfpos.command;

/* loaded from: classes2.dex */
public class Command_Up_Search extends Command {
    public Command_Up_Search() {
        setTradType("69");
        setType((byte) 0);
    }

    public void setParams(String str, String str2, String str3, String str4, String str5) {
        setParams(getTradType(), str, str2, str3, str4, str5);
    }
}

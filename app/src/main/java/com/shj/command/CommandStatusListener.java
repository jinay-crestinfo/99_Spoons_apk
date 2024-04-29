package com.shj.command;

/* loaded from: classes2.dex */
public interface CommandStatusListener {
    void onCommandError(Command command, CommandError commandError);

    void onCommandFinished(Command command);
}

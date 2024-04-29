package com.shj.command;

import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_ACK extends Command {
    static byte[] ackBytes = {-6, 0, 0, 0, 0, 0, 0, 0, -1};

    public Command_Up_ACK() {
        setType(Command.CommandType.Ack);
        init(ackBytes);
    }
}

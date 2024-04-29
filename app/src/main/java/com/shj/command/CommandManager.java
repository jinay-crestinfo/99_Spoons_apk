package com.shj.command;

import com.oysb.utils.Loger;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Down_CommandAnswer;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class CommandManager {
    private static CommandManager commandManager;
    static Queue<byte[]> receiveCmdQueue = new LinkedList();
    static Queue<Command> sendCmdQueue = new LinkedList();
    static HashMap<Short, Class> hasCommands = new HashMap<>();
    static Command currentCommand = null;
    static Command lastCommand = null;

    public static CommandManager getInstance() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                if (commandManager == null) {
                    commandManager = new CommandManager();
                }
            }
        }
        return commandManager;
    }

    public static void registerCommand(Short sh, String str) {
        try {
            hasCommands.put(sh, Class.forName(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendReceivedRawCommand(byte[] bArr) {
        if (bArr != null) {
            synchronized (receiveCmdQueue) {
                receiveCmdQueue.offer(bArr);
                receiveCmdQueue.notifyAll();
            }
        }
    }

    public static Queue<byte[]> getReceiveCommandQueue() {
        return receiveCmdQueue;
    }

    public static Command parseReceiveCommand(byte[] bArr) {
        Command command;
        try {
            Command command2 = (Command) hasCommands.get(Short.valueOf((short) ((Shj.getVersion() == 2 ? bArr[2] : bArr[1]) & UByte.MAX_VALUE))).newInstance();
            command2.init(bArr);
            if ((command2 instanceof CommandV2_Down_CommandAnswer) && (command = currentCommand) != null) {
                ((CommandV2_Down_CommandAnswer) command2).setSetCommand((CommandV2_Up_SetCommand) command);
            }
            return command2;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Command command3 = currentCommand;
            if (command3 instanceof CommandV2_Up_SetCommand) {
                bArr[5] = command3.getRawCommand()[5];
                bArr[6] = currentCommand.getRawCommand()[6];
                CommandV2_Down_CommandAnswer commandV2_Down_CommandAnswer = new CommandV2_Down_CommandAnswer();
                commandV2_Down_CommandAnswer.setSetCommand((CommandV2_Up_SetCommand) currentCommand);
                commandV2_Down_CommandAnswer.init(bArr);
                return commandV2_Down_CommandAnswer;
            }
            Loger.writeException("COMMAND", e);
            return null;
        }
    }

    public static void appendSendCommand(Command command) {
        if (command != null) {
            synchronized (sendCmdQueue) {
                command.setTime(System.currentTimeMillis());
                sendCmdQueue.offer(command);
                sendCmdQueue.notifyAll();
            }
        }
    }

    public static Queue<Command> getSendCommandQueue() {
        return sendCmdQueue;
    }

    public static Command getCurrentCommand() {
        return currentCommand;
    }

    public static void setCurrentCommand(Command command) {
        Shj.onWriteCommand(command);
        currentCommand = command;
    }

    public static void Ack() {
        appendSendCommand(new Command_Up_ACK());
    }
}

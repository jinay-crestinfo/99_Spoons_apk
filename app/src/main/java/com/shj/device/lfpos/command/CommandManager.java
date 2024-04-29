package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class CommandManager {
    static Queue<byte[]> receiveCmdQueue = new LinkedList();
    static Queue<Command> sendCmdQueue = new LinkedList();
    static HashMap<String, Class> hasCommands = new HashMap<>();
    static Command currentCommand = null;
    static Command lastCommand = null;

    public static void Ack() {
    }

    static {
        registerCommand(Command_Down_Properties.class.getName());
        registerCommand(Command_Down_ACK.class.getName());
        registerCommand(Command_Down_NAK.class.getName());
        registerCommand(Command_Down_ClearLogs.class.getName());
        registerCommand(Command_Down_ClearRecord.class.getName());
        registerCommand(Command_Down_Message.class.getName());
        registerCommand(Command_Down_Pay.class.getName());
        registerCommand(Command_Down_Properties.class.getName());
        registerCommand(Command_Down_Records.class.getName());
        registerCommand(Command_Down_Search.class.getName());
        registerCommand(Command_Down_Sum.class.getName());
    }

    public static void registerCommand(String str) {
        try {
            XYClass xYClass = (XYClass) Class.forName(str).getAnnotation(XYClass.class);
            if (xYClass.KEY().equals("HEAD")) {
                registerCommand(xYClass.VALUE(), str);
            }
        } catch (Exception e) {
            Loger.writeLog("SHJ", "注册命令失败...");
            e.printStackTrace();
        }
    }

    private static void registerCommand(String str, String str2) {
        try {
            hasCommands.put(str, Class.forName(str2));
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
        String str;
        try {
            if (isMessage(bArr)) {
                byte[] bArr2 = new byte[2];
                ObjectHelper.updateBytes(bArr2, bArr, 11, 0, 2);
                str = new String(bArr2, StringUtils.GB2312);
            } else if (isAck(bArr)) {
                str = "06H";
            } else {
                if (!isNak(bArr)) {
                    return null;
                }
                str = "15H";
            }
            try {
                Class<Command_Down_ServerMessage> cls = hasCommands.get(str);
                if (cls == null) {
                    cls = Command_Down_ServerMessage.class;
                }
                Command_Down_ServerMessage newInstance = cls.newInstance();
                newInstance.init(bArr);
                return newInstance;
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                return null;
            }
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
            Loger.writeException("LFPOS", e2);
            return null;
        }
    }

    private static boolean isMessage(byte[] bArr) {
        return (ObjectHelper.intFromBytes(bArr, 11, 1) == 6 || ObjectHelper.intFromBytes(bArr, 12, 1) == 28) ? false : true;
    }

    private static boolean isAck(byte[] bArr) {
        return ObjectHelper.intFromBytes(bArr, 11, 1) == 6 && ObjectHelper.intFromBytes(bArr, 12, 1) == 28;
    }

    private static boolean isNak(byte[] bArr) {
        return ObjectHelper.intFromBytes(bArr, 11, 1) == 21 && ObjectHelper.intFromBytes(bArr, 12, 1) == 28;
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
        currentCommand = command;
    }
}

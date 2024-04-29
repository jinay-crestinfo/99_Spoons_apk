package com.shj.command;

import com.shj.Shj;

/* loaded from: classes2.dex */
public class Command {
    private CommandStatusListener commandStatusListener;
    protected byte[] data;
    protected short dataOffset;
    private short head;
    private short sn;
    private long time;
    private CommandType type;
    private boolean inited = false;
    private short version = 1;
    private int sendRepeatCount = 0;
    private long expiredTime = 30000;
    private boolean needAck = false;
    private boolean isVCmd = false;

    /* loaded from: classes2.dex */
    public enum CommandType {
        Receive,
        Command,
        Wait,
        Ack,
        VCMD
    }

    public void doCommand() {
    }

    public boolean init(byte[] bArr) {
        this.data = bArr;
        return true;
    }

    public boolean isValid() {
        return Shj.isBatchJobRunning() || System.currentTimeMillis() - this.time < this.expiredTime;
    }

    public byte[] getRawCommand() {
        return this.data;
    }

    public boolean isInited() {
        return this.inited;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public long getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(long j) {
        this.expiredTime = j;
    }

    public boolean isNeedAck() {
        return this.needAck;
    }

    public void setNeedAck(boolean z) {
        this.needAck = z;
    }

    public CommandType getType() {
        return this.type;
    }

    public void setType(CommandType commandType) {
        this.type = commandType;
    }

    public CommandStatusListener getCommandStatusListener() {
        return this.commandStatusListener;
    }

    public void setCommandStatusListener(CommandStatusListener commandStatusListener) {
        this.commandStatusListener = commandStatusListener;
    }

    public void setFinished() {
        CommandStatusListener commandStatusListener = this.commandStatusListener;
        if (commandStatusListener != null) {
            commandStatusListener.onCommandFinished(this);
        }
    }

    public void setError(CommandError commandError) {
        CommandStatusListener commandStatusListener = this.commandStatusListener;
        if (commandStatusListener != null) {
            commandStatusListener.onCommandError(this, commandError);
        }
    }

    public short getSn() {
        return this.sn;
    }

    public void setSn(short s) {
        this.sn = s;
    }

    public short getVersion() {
        return this.version;
    }

    public void setVersion(short s) {
        this.version = s;
    }

    public short getHead() {
        return this.head;
    }

    public void setHead(short s) {
        this.head = s;
    }

    public int getSendRepeatCount() {
        return this.sendRepeatCount;
    }

    public void addSendRepeatCount() {
        this.sendRepeatCount++;
    }

    public boolean isVCmd() {
        return this.isVCmd;
    }

    public void setVCmd(boolean z) {
        this.isVCmd = z;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}

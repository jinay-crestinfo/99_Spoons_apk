package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x71")
/* loaded from: classes2.dex */
public class CommandV2_Down_CommandAnswer extends CommandV2 {
    CommandV2_Up_SetCommand setCommand;
    int cmdType = 0;
    boolean isSetCommand = false;
    boolean isOk = false;
    byte[] answers = null;

    public CommandV2_Down_CommandAnswer() {
        setHead((short) 113);
        setType(Command.CommandType.Receive);
    }

    public void setSetCommand(CommandV2_Up_SetCommand commandV2_Up_SetCommand) {
        this.setCommand = commandV2_Up_SetCommand;
    }

    public CommandV2_Up_SetCommand getSetCommand() {
        return this.setCommand;
    }

    public byte[] getAnswers() {
        return this.answers;
    }

    public int getCmdType() {
        return this.cmdType;
    }

    public boolean isSetCommand() {
        return this.isSetCommand;
    }

    public boolean isSetOk() {
        return this.isOk;
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        Loger.writeLog("SHJ;SET", "下位机上报指令执行设置结果:" + ObjectHelper.hex2String(bArr, bArr.length));
        byte[] bArr2 = new byte[bArr.length + (-8)];
        this.answers = bArr2;
        ObjectHelper.updateBytes(bArr2, bArr, 7, 0, bArr2.length);
        boolean init = super.init(bArr);
        this.cmdType = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        boolean z = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 1) == 1;
        this.isSetCommand = z;
        if (z) {
            this.isOk = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 1) == 0;
            Loger.writeLog("SHJ;SET", "下位机上报指令执行设置结果:" + this.isOk);
        } else {
            Loger.writeLog("SHJ;SET", "下位机上报指令执行结果:" + ObjectHelper.hex2String(bArr, bArr.length));
        }
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Loger.writeLog("SHJ", "......:" + this.isOk);
        Shj.onSetCommandAnswer(this);
    }
}

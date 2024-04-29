package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x32")
/* loaded from: classes2.dex */
public class CommandV2_Down_Version extends CommandV2 {
    String version = "0203";

    public CommandV2_Down_Version() {
        setHead((short) 50);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        byte[] bytesFromBytes = ObjectHelper.bytesFromBytes(bArr, this.dataOffset, (bArr.length - 1) - this.dataOffset);
        int length = bytesFromBytes.length;
        for (int length2 = bytesFromBytes.length - 1; length2 >= 0; length2--) {
            length = length2 + 1;
            if (bytesFromBytes[length2] == 0) {
                break;
            }
        }
        this.version = new String(ObjectHelper.bytesFromBytes(bytesFromBytes, 0, length));
        Loger.writeLog("SHJ;SET", "下位机上报机器版本号:" + this.version);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.setMachineBoardVersion(this.version);
    }
}

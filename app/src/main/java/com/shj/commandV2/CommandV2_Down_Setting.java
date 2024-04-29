package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x63")
/* loaded from: classes2.dex */
public class CommandV2_Down_Setting extends CommandV2 {
    public CommandV2_Down_Setting() {
        setHead((short) 99);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        Loger.writeLog("SHJ;SET", "下位机上报设置命令:" + ObjectHelper.hex2String(bArr, bArr.length));
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onDownReportSetCmd();
    }
}

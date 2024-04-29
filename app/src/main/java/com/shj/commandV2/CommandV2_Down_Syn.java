package com.shj.commandV2;

import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x31")
/* loaded from: classes2.dex */
public class CommandV2_Down_Syn extends CommandV2 {
    public CommandV2_Down_Syn() {
        setHead((short) 49);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        Loger.writeLog("SHJ;SET", "下位机上报同步指令:" + ObjectHelper.hex2String(bArr, bArr.length));
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onDownSyn();
        AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_AppStartUp, "", "下位机重启了上报同步指令，将重启上位机软件");
    }
}

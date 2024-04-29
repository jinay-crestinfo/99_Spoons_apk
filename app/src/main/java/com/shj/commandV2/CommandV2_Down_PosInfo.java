package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x24")
/* loaded from: classes2.dex */
public class CommandV2_Down_PosInfo extends CommandV2 {
    public CommandV2_Down_PosInfo() {
        setHead((short) 36);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        return super.init(bArr);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdatePosInfo("");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

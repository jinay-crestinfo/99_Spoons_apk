package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x33")
/* loaded from: classes2.dex */
public class CommandV2_Down_Notice extends CommandV2 {
    private byte[] checkDatas = new byte[0];

    public CommandV2_Down_Notice() {
        setHead((short) 51);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.checkDatas = ObjectHelper.bytesFromBytes(bArr, this.dataOffset, 8);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        byte[] bArr2 = this.checkDatas;
        sb.append(ObjectHelper.hex2String(bArr2, bArr2.length));
        Loger.writeLog("SHJ", sb.toString());
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onVmcNotice(this.checkDatas);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

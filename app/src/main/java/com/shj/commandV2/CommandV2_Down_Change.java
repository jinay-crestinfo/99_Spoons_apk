package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x26")
/* loaded from: classes2.dex */
public class CommandV2_Down_Change extends CommandV2 {
    private int coinMoney = 0;
    private int paperMoney = 0;

    public CommandV2_Down_Change() {
        setHead((short) 38);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.paperMoney = ObjectHelper.intFromBytes(bArr, this.dataOffset, 4);
        this.coinMoney = ObjectHelper.intFromBytes(bArr, this.dataOffset + 4, 4);
        Loger.writeLog("SHJ", "下位机找零：coin:" + this.coinMoney + " paperMoney:" + this.paperMoney);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onChangeFinish(this.coinMoney, this.paperMoney);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

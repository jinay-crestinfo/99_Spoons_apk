package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x23")
/* loaded from: classes2.dex */
public class CommandV2_Down_CurrentMoney extends CommandV2 {
    private int currentMoney = 0;

    public CommandV2_Down_CurrentMoney() {
        setHead((short) 35);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.currentMoney = ObjectHelper.intFromBytes(bArr, this.dataOffset, 4);
        Loger.writeLog("SHJ", "下位机报告余额：currentMoney:" + this.currentMoney);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onResetCurrentMoney(this.currentMoney, false);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

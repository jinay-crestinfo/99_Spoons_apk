package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0x13")
/* loaded from: classes2.dex */
public class Command_Down_ReportPaperMoneyBalance extends Command {
    private int balance = 0;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.balance = ObjectHelper.intFromBytes(bArr, 2, 4);
        Loger.writeLog("SHJ", "收到下位机上报：纸币余额 0x13 balance" + this.balance);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onUpdatePaperMoneyBalance(this.balance);
    }
}

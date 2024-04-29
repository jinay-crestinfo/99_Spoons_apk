package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0x12")
/* loaded from: classes2.dex */
public class Command_Down_ReportCoinBalance extends Command {
    private int coinBalance = 0;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.coinBalance = ObjectHelper.intFromBytes(bArr, 2, 4);
        Loger.writeLog("SHJ", "收到下位机上报：硬币余额 0x12 coinBalance" + this.coinBalance);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onUpdateCoinBalance(this.coinBalance);
    }
}

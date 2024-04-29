package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_CheckOfferDevices extends CommandV2 {
    int shelf = -1;

    public CommandV2_Up_CheckOfferDevices() {
        setType(Command.CommandType.Command);
        setHead((short) 3);
        setExpiredTime(6000L);
    }

    public void setParams(int i) {
        this.shelf = i;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "检查设备状态命令已下发 :" + this.shelf);
    }
}

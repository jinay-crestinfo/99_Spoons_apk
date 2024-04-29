package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x14")
/* loaded from: classes2.dex */
public class CommandV2_Up_SetShelfCapacity extends CommandV2 {
    int capacity;
    int shelf;

    public CommandV2_Up_SetShelfCapacity() {
        setType(Command.CommandType.Command);
        setHead((short) 20);
    }

    public void setParams(int i, int i2) {
        this.shelf = i;
        this.capacity = i2;
        if (isVCmd()) {
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
        ObjectHelper.updateBytes(this.dataV2, i2, 2, 1);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        if (!Shj.isStoreGoodsInfoInVMC()) {
            Shj.onUpdateShelfCapacity(this.shelf, this.capacity);
            Shj.OnGoodsSetCommandResult(this, this.shelf, Integer.valueOf(this.capacity));
        }
        Loger.writeLog("SHJ;SET", "上位机设置货道容量 shelf:" + this.shelf + " capacity:" + this.capacity);
    }
}

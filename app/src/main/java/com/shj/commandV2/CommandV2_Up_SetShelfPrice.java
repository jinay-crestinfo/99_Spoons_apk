package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x12")
/* loaded from: classes2.dex */
public class CommandV2_Up_SetShelfPrice extends CommandV2 {
    int price;
    int shelf;

    public CommandV2_Up_SetShelfPrice() {
        if (Shj.isStoreGoodsInfoInVMC()) {
            setType(Command.CommandType.Command);
        } else {
            setType(Command.CommandType.VCMD);
        }
        setHead((short) 18);
    }

    public void setParams(int i, int i2) {
        this.shelf = i;
        this.price = i2;
        if (isVCmd()) {
            return;
        }
        this.dataV2 = new byte[6];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
        ObjectHelper.updateBytes(this.dataV2, i2, 2, 4);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        if (!Shj.isStoreGoodsInfoInVMC()) {
            ShjDbHelper.saveShelfInfo(this.shelf, -1, -1, this.price, -1, -1, null, null, null, null);
            Shj.onUpdateShelfPrice(Integer.valueOf(this.shelf), Integer.valueOf(this.price));
            Shj.OnGoodsSetCommandResult(this, this.shelf, Integer.valueOf(this.price));
            Loger.writeLog("SHJ;SET", "上位机设置货道价格 OnGoodsSetCommandResult shelf:" + this.shelf + " price:" + this.price);
        }
        Loger.writeLog("SHJ;SET", "上位机设置货道价格 shelf:" + this.shelf + " price:" + this.price);
    }
}

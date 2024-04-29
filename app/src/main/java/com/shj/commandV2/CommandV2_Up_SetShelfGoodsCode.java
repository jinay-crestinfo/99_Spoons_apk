package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x15")
/* loaded from: classes2.dex */
public class CommandV2_Up_SetShelfGoodsCode extends CommandV2 {
    String goodsCode;
    int shelf;

    public CommandV2_Up_SetShelfGoodsCode() {
        if (Shj.isStoreGoodsInfoInVMC()) {
            setType(Command.CommandType.Command);
        } else {
            setType(Command.CommandType.VCMD);
        }
        setHead((short) 21);
    }

    public void setParams(int i, String str) {
        this.shelf = i;
        this.goodsCode = str;
        if (isVCmd()) {
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
        ObjectHelper.updateBytes(this.dataV2, Integer.parseInt(str), 2, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        if (!Shj.isStoreGoodsInfoInVMC()) {
            ShjDbHelper.saveShelfInfo(this.shelf, -1, -1, -1, -1, -1, null, "" + this.goodsCode, null, null);
            Shj.onUpdateShelfGoodsCode(Integer.valueOf(this.shelf), this.goodsCode);
            Shj.OnGoodsSetCommandResult(this, this.shelf, this.goodsCode);
        }
        Loger.writeLog("SHJ;SET", "上位机设置货道容量 shelf:" + this.shelf + " goodsCode:" + this.goodsCode);
    }
}

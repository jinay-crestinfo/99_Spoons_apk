package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x15")
/* loaded from: classes2.dex */
public class CommandV2_Down_UpdateShelfGoodsCode extends CommandV2 {
    private int shelf = 0;
    private String goodsCode = "0";

    public CommandV2_Down_UpdateShelfGoodsCode() {
        setHead((short) 20);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        String str = "000" + ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 2);
        this.goodsCode = str;
        this.goodsCode = str.substring(str.length() - 4);
        Loger.writeLog("SHJ;SET", "下位机上报货道商品编号：shelf:" + this.shelf + " goodsCode:" + this.goodsCode);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            if (Shj.isStoreGoodsInfoInVMC()) {
                Shj.onUpdateShelfGoodsCode(Integer.valueOf(this.shelf), this.goodsCode);
            }
            Shj.OnGoodsSetCommandResult(this, this.shelf, this.goodsCode);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x12")
/* loaded from: classes2.dex */
public class CommandV2_Down_UpdateShelfPrice extends CommandV2 {
    private int shelf = 0;
    private int price = 0;

    public CommandV2_Down_UpdateShelfPrice() {
        setHead((short) 18);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        this.price = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 4);
        Loger.writeLog("SHJ;SET", "下位机上报货道价格：shelf:" + this.shelf + " price:" + this.price);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            if (Shj.isStoreGoodsInfoInVMC()) {
                Shj.onUpdateShelfPrice(Integer.valueOf(this.shelf), Integer.valueOf(this.price));
            }
            Shj.OnGoodsSetCommandResult(this, this.shelf, Integer.valueOf(this.price));
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

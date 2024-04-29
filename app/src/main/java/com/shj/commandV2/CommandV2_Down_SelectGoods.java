package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x05")
/* loaded from: classes2.dex */
public class CommandV2_Down_SelectGoods extends CommandV2 {
    private int shelf = 0;

    public CommandV2_Down_SelectGoods() {
        setHead((short) 5);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        Loger.writeLog("SHJ;SALES", "选货：shelf:" + this.shelf);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            int i = this.shelf;
            if (i > 0) {
                Shj.onSelectGoodsOnShelf(Integer.valueOf(i));
            } else {
                Shj.onDeselectGoodsOnShelf(0);
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

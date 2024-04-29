package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.OfferState;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x04")
/* loaded from: classes2.dex */
public class CommandV2_Down_OfferGoods extends CommandV2 {
    private OfferState state;
    private int shelf = 0;
    private int picker = 1;

    public CommandV2_Down_OfferGoods() {
        setHead((short) 4);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 2);
        if (bArr.length > this.dataOffset + 4) {
            this.picker = ObjectHelper.intFromBytes(bArr, this.dataOffset + 3, 1);
        }
        this.state = OfferState.int2Enum(intFromBytes);
        Loger.writeLog("SHJ;SALES", "出货：shelf:" + this.shelf + " state:" + this.state + " picker:" + this.picker);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onOfferGoods(this.shelf, this.state, this.picker);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

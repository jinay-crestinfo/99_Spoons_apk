package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.ShelfDriverState;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x99")
/* loaded from: classes2.dex */
public class CommandV2_Down_DriverShelf extends CommandV2 {
    private int shelf = 0;
    private ShelfDriverState state;

    public CommandV2_Down_DriverShelf() {
        setHead((short) 10);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 2);
        if (intFromBytes == 1) {
            this.state = ShelfDriverState.OfferSuccess;
        } else if (intFromBytes == 2) {
            this.state = ShelfDriverState.NoEngine;
        } else if (intFromBytes == 3) {
            this.state = ShelfDriverState.Blocked;
        } else if (intFromBytes == 4) {
            this.state = ShelfDriverState.EngineError;
        } else if (intFromBytes == 5) {
            this.state = ShelfDriverState.ShelfCheckFailed;
        }
        Loger.writeLog("SHJ;SALES", "驱动货道：shelf:" + this.shelf + " state:" + this.state);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onDriverShelf(this.shelf, this.state);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

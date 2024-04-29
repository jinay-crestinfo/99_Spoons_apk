package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x69")
/* loaded from: classes2.dex */
public class CommandV2_Down_ShelfDoorStatus extends CommandV2 {
    private int state = 0;
    private int shelf = 0;

    public CommandV2_Down_ShelfDoorStatus() {
        setHead((short) 105);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        this.state = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 1);
        Loger.writeLog("SHJ;SALES", "货道门状态：shelf:" + this.shelf + " state:" + this.state);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onShelfDoorStatusUpdated(this.shelf, this.state);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

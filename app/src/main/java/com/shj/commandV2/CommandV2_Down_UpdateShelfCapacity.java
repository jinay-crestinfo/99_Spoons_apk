package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x14")
/* loaded from: classes2.dex */
public class CommandV2_Down_UpdateShelfCapacity extends CommandV2 {
    private int shelf = 0;
    private int capacity = 0;

    public CommandV2_Down_UpdateShelfCapacity() {
        setHead((short) 20);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        this.capacity = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 1);
        Loger.writeLog("SHJ;SET", "下位机上报货道容量：shelf:" + this.shelf + " capacity:" + this.capacity);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateShelfCapacity(this.shelf, this.capacity);
            Shj.OnGoodsSetCommandResult(this, this.shelf, Integer.valueOf(this.capacity));
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

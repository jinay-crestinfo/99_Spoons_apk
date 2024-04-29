package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x62")
/* loaded from: classes2.dex */
public class CommandV2_Down_ICCardInfo extends CommandV2 {
    private int state = 0;
    private int money = 0;

    public CommandV2_Down_ICCardInfo() {
        setHead((short) 98);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.state = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.money = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 4);
        Loger.writeLog("SHJ", "ICCard余额：" + this.money);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateICCardMoney(this.money, "");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

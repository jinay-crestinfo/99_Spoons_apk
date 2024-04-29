package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_UnSelectGoods extends CommandV2 {
    public CommandV2_Up_UnSelectGoods() {
        setType(Command.CommandType.Command);
        setHead((short) 5);
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, 0, 0, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "取消选货命令已下发");
    }

    @Override // com.shj.commandV2.CommandV2
    public void onAck() {
        Loger.writeLog("SALES", "已取消选货");
        if (Shj.getSelectedShelf() != null) {
            Shj.onDeselectGoodsOnShelf(Shj.getSelectedShelf().getShelf());
        }
    }
}

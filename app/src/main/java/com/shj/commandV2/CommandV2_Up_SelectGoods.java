package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.MoneyType;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.command.CommandManager;

/* loaded from: classes2.dex */
public class CommandV2_Up_SelectGoods extends CommandV2 {
    int shelf = -1;

    public CommandV2_Up_SelectGoods() {
        if (Shj.isStoreGoodsInfoInVMC()) {
            setType(Command.CommandType.Command);
        } else {
            setType(Command.CommandType.VCMD);
        }
        setHead((short) 3);
    }

    public void setParams(int i) {
        this.shelf = i;
        if (isVCmd()) {
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, i, 0, 2);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SALES", "选货命令已下发 :" + this.shelf);
        if (Shj.isStoreGoodsInfoInVMC()) {
            return;
        }
        try {
            int i = this.shelf;
            if (i > 0) {
                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
                if (shelfInfo.isStatusOK()) {
                    Shj.onSelectGoodsOnShelf(Integer.valueOf(this.shelf));
                    if (Shj.getWallet().getCatchMoney().intValue() >= shelfInfo.getPrice().intValue()) {
                        CommandV2_Up_DriverShelf commandV2_Up_DriverShelf = new CommandV2_Up_DriverShelf();
                        commandV2_Up_DriverShelf.setParamsV2(this.shelf, true, false, false, 1);
                        CommandManager.appendSendCommand(commandV2_Up_DriverShelf);
                        int intValue = Shj.getWallet().getCatchMoney().intValue() - shelfInfo.getPrice().intValue();
                        CommandV2_Up_SetMoney commandV2_Up_SetMoney = new CommandV2_Up_SetMoney();
                        commandV2_Up_SetMoney.setParams(MoneyType.EAT, 0, "");
                        CommandManager.appendSendCommand(commandV2_Up_SetMoney);
                        CommandV2_Up_SetMoney commandV2_Up_SetMoney2 = new CommandV2_Up_SetMoney();
                        commandV2_Up_SetMoney2.setParams(MoneyType.Paper, intValue, "");
                        CommandManager.appendSendCommand(commandV2_Up_SetMoney2);
                    }
                } else {
                    Shj.onUpdateShelfState(this.shelf, shelfInfo.getStatus().intValue());
                }
            } else {
                Shj.onDeselectGoodsOnShelf(0);
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

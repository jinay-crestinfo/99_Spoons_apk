package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.CommandError;
import com.shj.device.VMCStatus;

@XYClass(KEY = "HEAD", VALUE = "0x0D")
/* loaded from: classes2.dex */
public class Command_Down_ReportStatus extends Command {
    int money;
    VMCStatus status;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.status = VMCStatus.int2Enum(bArr[2]);
        this.money = ObjectHelper.intFromBytes(bArr, 3, 4);
        Loger.writeLog("SHJ", "收到下位机上报：状态  0x0D status" + this.status + " money" + this.money);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateShjStatus(this.status, this.money);
            if ((CommandManager.currentCommand instanceof Command_Up_ClearShelfBlock) || (CommandManager.currentCommand instanceof Command_Up_DoCharge) || (CommandManager.currentCommand instanceof Command_Up_DriverGoodsShelf) || (CommandManager.currentCommand instanceof Command_Up_EnablePosFindCard) || (CommandManager.currentCommand instanceof Command_Up_UnablePosFindCard) || (CommandManager.currentCommand instanceof Command_Up_SetCoinType) || (CommandManager.currentCommand instanceof Command_Up_SetPaperType) || (CommandManager.currentCommand instanceof Command_Up_SetOfferGoodsCheck) || (CommandManager.currentCommand instanceof Command_Up_TestGoodsIsValid) || (CommandManager.currentCommand instanceof Command_Up_TestGoodsShelfIsExist) || (CommandManager.currentCommand instanceof Command_Up_TopUp) || (CommandManager.currentCommand instanceof Command_Up_UnablePosFindCard)) {
                CommandManager.currentCommand.setFinished();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            if ((CommandManager.currentCommand instanceof Command_Up_SelectGoods) || (CommandManager.currentCommand instanceof Command_Up_UnSelectGoods)) {
                CommandManager.currentCommand.setError(new CommandError(CommandError.CommandErrorType.UpError, e.getMessage()));
            }
        }
    }
}

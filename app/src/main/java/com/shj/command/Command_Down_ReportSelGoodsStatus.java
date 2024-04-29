package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.CommandError;

@XYClass(KEY = "HEAD", VALUE = "0x14")
/* loaded from: classes2.dex */
public class Command_Down_ReportSelGoodsStatus extends Command {
    private int shelf = -1;
    private boolean select = false;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        boolean z = bArr[2] == 1;
        this.select = z;
        if (z) {
            this.shelf = ObjectHelper.intFromBytes(bArr, 3, 2);
        }
        Loger.writeLog("SHJ", "收到下位机上报：下位机选择商品或取消选择商品 0x14 select" + this.select);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            if (this.select) {
                Shj.onSelectGoodsOnShelf(Integer.valueOf(this.shelf));
            } else {
                Shj.onDeselectGoodsOnShelf(Integer.valueOf(this.shelf));
            }
            if ((CommandManager.currentCommand instanceof Command_Up_SelectGoods) || (CommandManager.currentCommand instanceof Command_Up_UnSelectGoods)) {
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

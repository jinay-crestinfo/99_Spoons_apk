package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.CommandError;

@XYClass(KEY = "HEAD", VALUE = "0x0c")
/* loaded from: classes2.dex */
public class Command_Down_ReportShelfGoodsCount extends Command {
    private int shelf = -1;
    private int goodsCount = 0;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, 2, 2);
        this.goodsCount = ObjectHelper.intFromBytes(bArr, 4, 1);
        Loger.writeLog("SHJ", "收到下位机上报：商品存货量  0x0c shelf" + this.shelf + " goodsCount" + this.goodsCount);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateGoodsCount(Integer.valueOf(this.shelf), Integer.valueOf(this.goodsCount));
            if (CommandManager.currentCommand instanceof Command_Up_SetShelfGoodsCount) {
                CommandManager.currentCommand.setFinished();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            if (CommandManager.currentCommand instanceof Command_Up_SetShelfGoodsCount) {
                CommandManager.currentCommand.setError(new CommandError(CommandError.CommandErrorType.UpError, e.getMessage()));
            }
        }
    }
}

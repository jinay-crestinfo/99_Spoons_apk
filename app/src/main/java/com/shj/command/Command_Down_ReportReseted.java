package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.CommandError;

@XYClass(KEY = "HEAD", VALUE = "0x10")
/* loaded from: classes2.dex */
public class Command_Down_ReportReseted extends Command {
    private int money = -1;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.money = ObjectHelper.intFromBytes(bArr, 2, 4);
        Loger.writeLog("SHJ", "收到下位机上报：复位指令 0x10 money" + this.money);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onReset();
            if (CommandManager.currentCommand instanceof Command_Up_Reset) {
                CommandManager.currentCommand.setFinished();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            if (CommandManager.currentCommand instanceof Command_Up_Reset) {
                CommandManager.currentCommand.setError(new CommandError(CommandError.CommandErrorType.UpError, e.getMessage()));
            }
        }
    }
}

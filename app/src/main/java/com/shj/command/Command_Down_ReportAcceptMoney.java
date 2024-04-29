package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.command.CommandError;

@XYClass(KEY = "HEAD", VALUE = "0x0b")
/* loaded from: classes2.dex */
public class Command_Down_ReportAcceptMoney extends Command {
    private MoneyType moneyType;
    private String info = "";
    private int revValue = 0;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.revValue = ObjectHelper.intFromBytes(bArr, 2, 4);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, 6, 1);
        this.info = "";
        switch (intFromBytes) {
            case 0:
                this.moneyType = MoneyType.Paper;
                break;
            case 1:
                this.moneyType = MoneyType.Coin;
                break;
            case 2:
                this.moneyType = MoneyType.ICCard;
                this.info = "NOINFO";
                break;
            case 3:
                this.moneyType = MoneyType.ECard;
                this.info = "NOINFO";
                break;
            case 4:
                this.moneyType = MoneyType.Zfb;
                this.info = "NOINFO";
                break;
            case 5:
                this.moneyType = MoneyType.Weixin;
                this.info = "NOINFO";
                break;
            case 6:
                this.moneyType = MoneyType.PickCode;
                this.info = "NOINFO";
                break;
            case 7:
                this.moneyType = MoneyType.YL;
                this.info = "NOINFO";
                break;
            case 8:
                this.moneyType = MoneyType.EAT;
                break;
            case 9:
                this.moneyType = MoneyType.JD;
                this.info = "NOINFO";
                break;
            default:
                switch (intFromBytes) {
                    case 151:
                        this.moneyType = MoneyType.PaperChange;
                        break;
                    case 152:
                        this.moneyType = MoneyType.CoinChange;
                        break;
                    case 153:
                        this.moneyType = MoneyType.Reset;
                        break;
                }
        }
        Loger.writeLog("SHJ", "收到下位机上报：收款 0x0b moneyType" + this.moneyType + " value" + this.revValue + " info:" + this.info);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onAcceptMoney(this.revValue, this.moneyType, this.info);
            if (CommandManager.currentCommand instanceof Command_Up_SetMoney) {
                CommandManager.currentCommand.setFinished();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            if (CommandManager.currentCommand instanceof Command_Up_SetMoney) {
                CommandManager.currentCommand.setError(new CommandError(CommandError.CommandErrorType.UpError, e.getMessage()));
            }
        }
    }
}

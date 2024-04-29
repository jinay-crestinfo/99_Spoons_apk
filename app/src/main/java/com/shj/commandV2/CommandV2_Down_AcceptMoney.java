package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x21")
/* loaded from: classes2.dex */
public class CommandV2_Down_AcceptMoney extends CommandV2 {
    private MoneyType moneyType;
    private int revValue = 0;
    private String info = "";

    public CommandV2_Down_AcceptMoney() {
        setHead((short) 33);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.revValue = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 4);
        int length = ((bArr.length - 5) - 1) - this.dataOffset;
        if (length > 0) {
            int length2 = ((bArr.length - 5) - 1) - this.dataOffset;
            byte[] bArr2 = new byte[length2];
            System.arraycopy(bArr, this.dataOffset + 5, bArr2, 0, length2);
            this.info = new String(bArr2);
        } else {
            this.info = "NOINFO";
        }
        if (intFromBytes == 21) {
            this.moneyType = MoneyType.PickCode;
        } else if (intFromBytes != 153) {
            switch (intFromBytes) {
                case 1:
                    this.moneyType = MoneyType.Paper;
                    if (length == 0) {
                        this.info = "";
                        break;
                    }
                    break;
                case 2:
                    this.moneyType = MoneyType.Coin;
                    if (length == 0) {
                        this.info = "";
                        break;
                    }
                    break;
                case 3:
                    this.moneyType = MoneyType.ICCard;
                    break;
                case 4:
                    this.moneyType = MoneyType.ECard;
                    break;
                case 5:
                    this.moneyType = MoneyType.Weixin;
                    break;
                case 6:
                    this.moneyType = MoneyType.Zfb;
                    break;
                case 7:
                    this.moneyType = MoneyType.JD;
                    break;
                case 8:
                    this.moneyType = MoneyType.EAT;
                    break;
                case 9:
                    this.moneyType = MoneyType.YL;
                    break;
            }
        } else {
            this.moneyType = MoneyType.Reset;
        }
        Loger.writeLog("SHJ", "收到下位机上报：收款 0x0b moneyType" + this.moneyType + " value" + this.revValue);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onAcceptMoney(this.revValue, this.moneyType, this.info);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

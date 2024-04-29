package com.shj.command;

import com.oysb.utils.ObjectHelper;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class Command_Up_SetMoney extends Command {
    String info = "";

    public Command_Up_SetMoney() {
        setType(Command.CommandType.Command);
        init(new byte[]{-6, 17, 0, 0, 0, 0, 0, 0, -1});
    }

    public void setParams(MoneyType moneyType, int i, String str) {
        this.info = str;
        int i2 = 6;
        switch (AnonymousClass1.$SwitchMap$com$shj$MoneyType[moneyType.ordinal()]) {
            case 1:
                i2 = 1;
                break;
            case 2:
            default:
                i2 = 0;
                break;
            case 3:
                i2 = 5;
                break;
            case 4:
                i2 = 4;
                break;
            case 5:
                i2 = 3;
                break;
            case 6:
                i2 = 2;
                break;
            case 7:
            case 8:
                break;
            case 9:
                i2 = 7;
                break;
            case 10:
                i2 = 8;
                break;
            case 11:
                i2 = 9;
                break;
            case 12:
                i2 = 153;
                break;
        }
        ObjectHelper.updateBytes(this.data, i2, 2, 1);
        ObjectHelper.updateBytes(this.data, i, 3, 4);
    }

    /* renamed from: com.shj.command.Command_Up_SetMoney$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$MoneyType;

        static {
            int[] iArr = new int[MoneyType.values().length];
            $SwitchMap$com$shj$MoneyType = iArr;
            try {
                iArr[MoneyType.Coin.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Paper.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Zfb.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ECard.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ICCard.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin_Share.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.PickCode.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.YL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.EAT.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.JD.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Reset.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.getWallet().setLastAddMoneyInfo(this.info);
        if (ObjectHelper.intFromBytes(this.data, 2, 1) == 153) {
            Shj.onAcceptMoney(0, MoneyType.Reset, "");
        }
    }
}

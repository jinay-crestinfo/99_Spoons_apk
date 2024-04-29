package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.command.Command;
import java.nio.charset.Charset;

/* loaded from: classes2.dex */
public class CommandV2_Up_SetMoney extends CommandV2 {
    String info = "";

    public CommandV2_Up_SetMoney() {
        setType(Command.CommandType.Command);
        setHead((short) 39);
    }

    public void setParams(MoneyType moneyType, int i, String str) {
        int index;
        this.info = str;
        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
        if (AnonymousClass1.$SwitchMap$com$shj$MoneyType[moneyType.ordinal()] == 1) {
            index = MoneyType.PickCode.getIndex();
        } else {
            index = moneyType.getIndex();
        }
        this.dataV2 = new byte[bytes.length + 5];
        ObjectHelper.updateBytes(this.dataV2, index, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 1, 4);
        ObjectHelper.updateBytes(this.dataV2, bytes, 5, bytes.length);
    }

    /* renamed from: com.shj.commandV2.CommandV2_Up_SetMoney$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$MoneyType;

        static {
            int[] iArr = new int[MoneyType.values().length];
            $SwitchMap$com$shj$MoneyType = iArr;
            try {
                iArr[MoneyType.Weixin_Share.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Shj.getWallet().setLastAddMoneyInfo(this.info);
    }
}

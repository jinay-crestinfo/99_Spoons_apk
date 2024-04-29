package com.shj.commandV2;

import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;

@XYClass(KEY = "HEAD", VALUE = "0x29")
/* loaded from: classes2.dex */
public class CommandV2_Down_ICCardPay extends CommandV2 {
    private int type = 0;
    private String card = "";

    public CommandV2_Down_ICCardPay() {
        setHead((short) 41);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.type = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        int length = ((bArr.length - 1) - 1) - this.dataOffset;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, this.dataOffset + 1, bArr2, 0, length);
        String str = new String(bArr2);
        this.card = str;
        if (str.startsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
            this.card = this.card.substring(1);
        }
        Loger.writeLog("SHJ", "会员卡信息：type:" + this.type + " card:" + this.card);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onNeedICCardPay(this.type, this.card);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

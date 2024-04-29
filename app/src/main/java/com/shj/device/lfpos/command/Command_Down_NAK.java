package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;

@XYClass(KEY = "HEAD", VALUE = "15H")
/* loaded from: classes2.dex */
public class Command_Down_NAK extends Command {
    String message;
    String resultCode;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.resultCode = split[1];
            this.message = split[2];
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "NAK tradType:" + getTradType() + "resultCode:" + this.resultCode + " message:" + this.message);
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        LfPos.cmd_onNak(getID(), this.resultCode, this.message, "");
        super.doCommand();
    }
}

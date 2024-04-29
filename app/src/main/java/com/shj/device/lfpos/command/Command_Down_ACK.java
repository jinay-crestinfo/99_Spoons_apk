package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;
import java.util.HashMap;

@XYClass(KEY = "HEAD", VALUE = "06H")
/* loaded from: classes2.dex */
public class Command_Down_ACK extends Command {
    String resultCode = "";
    String message = "";
    String remain = "";

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.resultCode = split[1];
            this.message = split[2];
            if (split.length == 4) {
                this.remain = split[3];
            }
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "ACK tradType:" + getTradType() + "resultCode:" + this.resultCode + " message:" + this.message);
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        HashMap hashMap = new HashMap();
        hashMap.put("ID", Integer.valueOf(getID()));
        hashMap.put("CODE", this.resultCode);
        hashMap.put("MESSAGE", this.message);
        hashMap.put("REMAIN", this.remain);
        LfPos.cmd_onAck(hashMap);
        super.doCommand();
    }
}

package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;
import java.util.HashMap;

@XYClass(KEY = "HEAD", VALUE = "82")
/* loaded from: classes2.dex */
public class Command_Down_ClearRecord extends Command {
    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            setTradType(new String(this.CONT, StringUtils.GB2312).split(new String(FS))[0]);
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_ClearRecord tradType:" + getTradType());
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        HashMap hashMap = new HashMap();
        hashMap.put("ID", Integer.valueOf(getID()));
        hashMap.put("CODE", getTradType());
        hashMap.put("MESSAGE", "");
        hashMap.put("REMAIN", "");
        LfPos.cmd_onAck(hashMap);
        super.doCommand();
    }
}

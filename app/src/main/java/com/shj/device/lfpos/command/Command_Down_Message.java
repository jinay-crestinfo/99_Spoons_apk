package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;
import java.util.HashMap;

@XYClass(KEY = "HEAD", VALUE = "97")
/* loaded from: classes2.dex */
public class Command_Down_Message extends Command {
    String content;
    String type;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.type = split[1];
            this.content = split[2];
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_Message tradType:" + getTradType());
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

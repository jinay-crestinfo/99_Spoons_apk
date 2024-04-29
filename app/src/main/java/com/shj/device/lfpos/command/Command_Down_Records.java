package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.Record;
import java.util.List;

@XYClass(KEY = "HEAD", VALUE = "64")
/* loaded from: classes2.dex */
public class Command_Down_Records extends Command {
    List<Record> records;
    String split;
    String tradCount;
    String version;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            setTradType(new String(this.CONT, StringUtils.GB2312).split(new String(FS))[0]);
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_Records tradType:" + getTradType());
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        super.doCommand();
    }
}

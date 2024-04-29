package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;

@XYClass(KEY = "HEAD", VALUE = "57")
/* loaded from: classes2.dex */
public class Command_Down_Properties extends Command {
    String childVersion;
    String masterVersion;
    String pos_compnay;
    String pos_serial;
    String pos_type;
    String remain;
    String user_en;
    String user_zh;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.pos_compnay = split[1];
            this.pos_type = split[2];
            this.pos_serial = split[3];
            this.user_zh = split[4];
            this.user_en = split[5];
            this.masterVersion = split[6];
            this.childVersion = split[7];
            this.remain = split[8];
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "PROPERTIES pos_compnay:" + this.pos_compnay + " pos_type:" + this.pos_type + " pos_serial:" + this.pos_serial + " user_zh:" + this.user_zh + " user_en:" + this.user_en + " masterVersion:" + this.masterVersion + " childVersion:" + this.childVersion + " remain:" + this.remain);
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        LfPos.cmd_onUpdatePosProperties(getID(), this.pos_compnay, this.pos_type, this.pos_serial, this.user_zh, this.user_en, this.masterVersion, this.childVersion, this.remain, getObj());
        super.doCommand();
    }
}

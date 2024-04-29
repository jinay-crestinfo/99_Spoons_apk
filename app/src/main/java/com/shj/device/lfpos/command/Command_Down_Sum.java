package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;

@XYClass(KEY = "HEAD", VALUE = "63")
/* loaded from: classes2.dex */
public class Command_Down_Sum extends Command {
    String incard_dj_count;
    String incard_dj_money;
    String incard_jj_count;
    String incard_jj_money;
    String name_en;
    String name_zh;
    String outcard_dj_count;
    String outcard_dj_money;
    String outcard_jj_count;
    String outcard_jj_money;
    String posid;
    String remark;
    String tradSn;
    String user;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.user = split[1];
            this.posid = split[2];
            this.name_zh = split[3];
            this.name_en = split[4];
            this.tradSn = split[5];
            this.incard_jj_count = split[6];
            this.incard_jj_money = split[7];
            this.incard_dj_count = split[8];
            this.incard_dj_money = split[9];
            this.outcard_jj_count = split[10];
            this.outcard_jj_money = split[11];
            this.outcard_dj_count = split[12];
            this.outcard_dj_money = split[13];
            this.remark = split[14];
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_Sum tradType:" + getTradType());
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        super.doCommand();
    }
}

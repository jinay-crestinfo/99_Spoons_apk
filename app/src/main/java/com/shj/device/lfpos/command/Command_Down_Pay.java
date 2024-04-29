package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.oysb.utils.zxing.decoding.Intents;
import com.shj.device.lfpos.LfPos;
import java.util.HashMap;

@XYClass(KEY = "HEAD", VALUE = "W1")
/* loaded from: classes2.dex */
public class Command_Down_Pay extends Command {
    String name_en;
    String name_zh;
    String posid;
    String remain;
    String type;
    String user;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            String str = split[1];
            this.type = str;
            this.user = split[2];
            this.posid = split[3];
            this.name_zh = split[4];
            this.name_en = split[5];
            this.remain = split[6];
            if (str.equalsIgnoreCase("03")) {
                byte[] bArr2 = ObjectHelper.splitBytes(bArr, FS[0]).get(6);
                byte b = bArr2[0];
                bArr2[0] = bArr2[3];
                bArr2[3] = b;
                byte b2 = bArr2[1];
                bArr2[1] = bArr2[2];
                bArr2[2] = b2;
                this.remain = ObjectHelper.hex2String(bArr2, bArr2.length).replace(org.apache.commons.lang3.StringUtils.SPACE, "").toLowerCase();
            }
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_Pay tradType:" + getTradType() + " type:" + this.type + " remain:" + this.remain);
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        HashMap hashMap = new HashMap();
        hashMap.put("ID", Integer.valueOf(getID()));
        hashMap.put("CODE", getTradType());
        hashMap.put(Intents.WifiConnect.TYPE, this.type);
        hashMap.put("USER", this.user);
        hashMap.put("POSID", this.posid);
        try {
            hashMap.put("NAME_ZH", this.name_zh);
            hashMap.put("NAME_EN", this.name_en);
            hashMap.put("REMAIN", this.remain);
            hashMap.put("MESSAGE", "");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        LfPos.cmd_onAck(hashMap);
        super.doCommand();
    }
}

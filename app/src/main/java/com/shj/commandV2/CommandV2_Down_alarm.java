package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;
import org.apache.commons.lang3.StringUtils;

@XYClass(KEY = "HEAD", VALUE = "0x53")
/* loaded from: classes2.dex */
public class CommandV2_Down_alarm extends CommandV2 {
    private int alarm_jgh1 = 0;
    private int alarm_jgh2 = 0;
    private int alarm_jgh3 = 0;
    private int alarm_jgh4 = 0;
    private int alarm_jgh5 = 0;
    private int alarm_jgh6 = 0;
    private int alarm_jgh7 = 0;
    private int alarm_jgh8 = 0;

    public CommandV2_Down_alarm() {
        setHead((short) 83);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.alarm_jgh1 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.alarm_jgh2 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        this.alarm_jgh3 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 3);
        this.alarm_jgh4 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 4);
        this.alarm_jgh5 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 5);
        this.alarm_jgh6 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 6);
        this.alarm_jgh7 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 7);
        this.alarm_jgh8 = ObjectHelper.intFromBytes(bArr, this.dataOffset, 8);
        Loger.writeLog("SHJ", "机柜报警 1:" + this.alarm_jgh1 + " 2:" + this.alarm_jgh2 + " 3:" + this.alarm_jgh3 + " 4:" + this.alarm_jgh4 + " 5:" + this.alarm_jgh5 + " 6:" + this.alarm_jgh6 + " 7:" + this.alarm_jgh7 + " 8:" + this.alarm_jgh8 + StringUtils.SPACE);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            int i = this.alarm_jgh1;
            Shj.onAlarm(i, i, i, i, i, i, i, i);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

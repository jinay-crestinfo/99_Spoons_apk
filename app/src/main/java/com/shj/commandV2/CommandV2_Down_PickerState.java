package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;
import java.util.ArrayList;
import java.util.List;

@XYClass(KEY = "HEAD", VALUE = "0x66")
/* loaded from: classes2.dex */
public class CommandV2_Down_PickerState extends CommandV2 {
    List<Integer> wblStatus = new ArrayList();

    public CommandV2_Down_PickerState() {
        setHead((short) 102);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        String str = "";
        for (int i = 0; i < intFromBytes; i++) {
            this.wblStatus.add(Integer.valueOf(ObjectHelper.intFromBytes(bArr, this.dataOffset + i + 1, 1)));
            str = str + this.wblStatus.get(i);
        }
        Loger.writeLog("SHJ", "共有" + intFromBytes + "个微波炉,状太：" + str);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.setPickerStatus(this.wblStatus);
    }
}

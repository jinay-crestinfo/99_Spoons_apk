package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.device.VMCStatus;

@XYClass(KEY = "HEAD", VALUE = "0x67")
/* loaded from: classes2.dex */
public class CommandV2_Down_VMCStatus extends CommandV2 {
    private VMCStatus vmcStatus;
    private int stateMarker = 0;
    private int state = 0;

    public CommandV2_Down_VMCStatus() {
        setHead((short) 103);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.stateMarker = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 1);
        this.state = intFromBytes;
        if (intFromBytes == 33) {
            this.vmcStatus = this.stateMarker == 0 ? VMCStatus.LiftChecking : VMCStatus.Normal;
        }
        Loger.writeLog("SHJ;SALES", "VMC状态：stateMarker:" + this.stateMarker + " state:" + this.state);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateShjStatus(this.vmcStatus);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }
}

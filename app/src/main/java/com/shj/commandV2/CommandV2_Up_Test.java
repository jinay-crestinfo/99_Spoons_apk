package com.shj.commandV2;

import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2_Up_Test extends CommandV2 {
    @Override // com.shj.command.Command
    public void doCommand() {
    }

    public CommandV2_Up_Test() {
        setType(Command.CommandType.Command);
        setHead((short) 52);
    }

    public void setParams(byte[] bArr) {
        byte[] bArr2 = {ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS, ClosedCaptionCtrl.ROLL_UP_CAPTIONS_4_ROWS, -61, -28, 95, 48, ClosedCaptionCtrl.ERASE_DISPLAYED_MEMORY, 43};
        byte b = bArr[0];
        bArr[0] = bArr[4];
        bArr[4] = b;
        byte b2 = bArr[2];
        bArr[2] = bArr[6];
        bArr[6] = b2;
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((255 - bArr[i]) - i);
        }
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = (byte) (bArr[i2] + bArr2[i2]);
        }
        this.dataV2 = bArr;
    }
}

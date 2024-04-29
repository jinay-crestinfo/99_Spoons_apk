package com.shj.commandV2;

import com.oysb.utils.ObjectHelper;
import com.shj.command.Command;

/* loaded from: classes2.dex */
public class CommandV2 extends Command {
    public static final int OFFSET = 5;
    protected byte[] dataV2;
    int serialNumber;

    public void onAck() {
    }

    public CommandV2() {
        this.dataOffset = (short) 5;
        setVersion((short) 2);
    }

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        super.init(bArr);
        if (getType() == Command.CommandType.Receive) {
            setSn((short) ObjectHelper.intFromBytes(bArr, 3, 1));
        }
        return true;
    }

    @Override // com.shj.command.Command
    public byte[] getRawCommand() {
        this.data = createCmd(ObjectHelper.int2Bytes16_2(getHead(), 1), ObjectHelper.int2Bytes16_2(getSerialNumber(), 1), this.dataV2);
        return this.data;
    }

    public synchronized void updateSerialNumber() {
        if (this.serialNumber != 0) {
            return;
        }
        this.serialNumber = (byte) (getNextSerial().intValue() % 256);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Integer getNextSerial() {
        /*
            r6 = this;
            java.lang.String r0 = "commandV2_g_serial_1"
            r1 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            com.oysb.utils.cache.ACache r3 = com.oysb.utils.cache.CacheHelper.getFileCache()     // Catch: java.lang.Exception -> L26
            java.lang.Object r3 = r3.getAsObject(r0)     // Catch: java.lang.Exception -> L26
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Exception -> L26
            if (r3 != 0) goto L1b
            r2 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Exception -> L19
            goto L1c
        L19:
            r2 = move-exception
            goto L2a
        L1b:
            r2 = r3
        L1c:
            int r3 = r2.intValue()     // Catch: java.lang.Exception -> L26
            int r3 = r3 + r1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Exception -> L26
            goto L33
        L26:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
        L2a:
            com.oysb.utils.Loger.safe_inner_exception_catch(r2)
            java.lang.String r4 = "SHJ"
            com.oysb.utils.Loger.writeException(r4, r2)
            r2 = r3
        L33:
            int r3 = r2.intValue()
            int r3 = r3 % 256
            if (r3 != 0) goto L44
            int r2 = r2.intValue()
            int r2 = r2 + r1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
        L44:
            com.oysb.utils.cache.ACache r1 = com.oysb.utils.cache.CacheHelper.getFileCache()
            r1.put(r0, r2)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.commandV2.CommandV2.getNextSerial():java.lang.Integer");
    }

    public int getSerialNumber() {
        updateSerialNumber();
        return this.serialNumber & 255;
    }

    private static byte[] createCmd(byte[]... bArr) {
        int i = 4;
        for (byte[] bArr2 : bArr) {
            if (bArr2 != null) {
                i += bArr2.length;
            }
        }
        byte[] bArr3 = new byte[i];
        ObjectHelper.updateBytes(bArr3, 250, 0, 1);
        ObjectHelper.updateBytes(bArr3, 251, 1, 1);
        int i2 = 2;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            byte[] bArr4 = bArr[i3];
            if (bArr4 != null) {
                if (i3 == 0) {
                    System.arraycopy(bArr4, 0, bArr3, i2, bArr4.length);
                    int length = i2 + bArr4.length;
                    ObjectHelper.updateBytes(bArr3, i - 5, length, 1);
                    i2 = length + 1;
                } else {
                    System.arraycopy(bArr4, 0, bArr3, i2, bArr4.length);
                    i2 += bArr4.length;
                }
            }
        }
        ObjectHelper.updateBytes(bArr3, (int) computerXor(bArr3, 0, i - 1), i2, 1);
        return bArr3;
    }

    private static short computerXor(byte[] bArr, int i, int i2) {
        short s = 0;
        for (int i3 = i; i3 < i + i2; i3++) {
            s = (short) (s ^ bArr[i3]);
        }
        return (short) (s & 255);
    }
}

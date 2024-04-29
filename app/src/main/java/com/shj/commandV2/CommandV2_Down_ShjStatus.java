package com.shj.commandV2;

import android.support.v4.view.InputDeviceCompat;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;
import com.xyshj.database.setting.SettingType;

@XYClass(KEY = "HEAD", VALUE = "0x52")
/* loaded from: classes2.dex */
public class CommandV2_Down_ShjStatus extends CommandV2 {
    private static String lastDataString = "";
    private static long lastLogTime;
    private int paperMachineState = 0;
    private int coinMachineState = 0;
    private int posMachineState = 0;
    private int wkyState = 0;
    private int temperature = 0;
    private int doorState = 0;
    private int paperMoney = 0;
    private int coinMoney = 0;
    private int machineId = 0;
    private int temperature1 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature2 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature3 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature4 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature5 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature6 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature7 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int temperature8 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity1 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity2 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity3 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity4 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity5 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity6 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity7 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int humidity8 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState1 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState2 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState3 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState4 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState5 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState6 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState7 = SettingType.RESTORE_FACTORY_SETTINGS;
    private int doorState8 = SettingType.RESTORE_FACTORY_SETTINGS;

    public CommandV2_Down_ShjStatus() {
        setHead((short) 82);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.paperMachineState = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.coinMachineState = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 1);
        this.posMachineState = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 1);
        this.wkyState = ObjectHelper.intFromBytes(bArr, this.dataOffset + 3, 1);
        int intFromBytes = ObjectHelper.intFromBytes(bArr, this.dataOffset + 4, 1);
        this.temperature = intFromBytes;
        if (intFromBytes > 128) {
            this.temperature = intFromBytes + InputDeviceCompat.SOURCE_ANY;
        }
        this.doorState = ObjectHelper.intFromBytes(bArr, this.dataOffset + 5, 1);
        this.paperMoney = ObjectHelper.intFromBytes(bArr, this.dataOffset + 6, 4);
        this.coinMoney = ObjectHelper.intFromBytes(bArr, this.dataOffset + 10, 4);
        this.machineId = ObjectHelper.intFromBytes(bArr, this.dataOffset + 14, 10);
        if (bArr.length > this.dataOffset + 32) {
            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 24, 1);
            this.temperature1 = intFromBytes2;
            if (intFromBytes2 > 128 && intFromBytes2 != 170 && intFromBytes2 != 161 && intFromBytes2 != 162) {
                this.temperature1 = intFromBytes2 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes3 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 25, 1);
            this.temperature2 = intFromBytes3;
            if (intFromBytes3 > 128 && intFromBytes3 != 170 && intFromBytes3 != 161 && intFromBytes3 != 162) {
                this.temperature2 = intFromBytes3 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes4 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 26, 1);
            this.temperature3 = intFromBytes4;
            if (intFromBytes4 > 128 && intFromBytes4 != 170 && intFromBytes4 != 161 && intFromBytes4 != 162) {
                this.temperature3 = intFromBytes4 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes5 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 27, 1);
            this.temperature4 = intFromBytes5;
            if (intFromBytes5 > 128 && intFromBytes5 != 170 && intFromBytes5 != 161 && intFromBytes5 != 162) {
                this.temperature4 = intFromBytes5 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes6 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 28, 1);
            this.temperature5 = intFromBytes6;
            if (intFromBytes6 > 128 && intFromBytes6 != 170 && intFromBytes6 != 161 && intFromBytes6 != 162) {
                this.temperature5 = intFromBytes6 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes7 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 29, 1);
            this.temperature6 = intFromBytes7;
            if (intFromBytes7 > 128 && intFromBytes7 != 170 && intFromBytes7 != 161 && intFromBytes7 != 162) {
                this.temperature6 = intFromBytes7 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes8 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 30, 1);
            this.temperature7 = intFromBytes8;
            if (intFromBytes8 > 128 && intFromBytes8 != 170 && intFromBytes8 != 161 && intFromBytes8 != 162) {
                this.temperature7 = intFromBytes8 + InputDeviceCompat.SOURCE_ANY;
            }
            int intFromBytes9 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 31, 1);
            this.temperature8 = intFromBytes9;
            if (intFromBytes9 > 128 && intFromBytes9 != 170 && intFromBytes9 != 161 && intFromBytes9 != 162) {
                this.temperature8 = intFromBytes9 + InputDeviceCompat.SOURCE_ANY;
            }
        }
        if (bArr.length > this.dataOffset + 40) {
            this.humidity1 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 32, 1);
            this.humidity2 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 33, 1);
            this.humidity3 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 34, 1);
            this.humidity4 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 35, 1);
            this.humidity5 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 36, 1);
            this.humidity6 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 37, 1);
            this.humidity7 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 38, 1);
            this.humidity8 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 39, 1);
        }
        if (bArr.length > this.dataOffset + 48) {
            this.doorState1 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 40, 1);
            this.doorState2 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 41, 1);
            this.doorState3 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 42, 1);
            this.doorState4 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 43, 1);
            this.doorState5 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 44, 1);
            this.doorState6 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 45, 1);
            this.doorState7 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 46, 1);
            this.doorState8 = ObjectHelper.intFromBytes(bArr, this.dataOffset + 47, 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("机器状态：paperMachineState:" + this.paperMachineState);
        sb.append(" coinMachineState:" + this.coinMachineState);
        sb.append(" posMachineState:" + this.posMachineState);
        sb.append(" wkyState:" + this.wkyState);
        sb.append(" temperature:" + this.temperature);
        sb.append(" doorState:" + this.doorState);
        sb.append(" paperMoney:" + this.paperMoney);
        sb.append(" coinMoney:" + this.coinMoney);
        sb.append(" machineId:" + this.machineId);
        sb.append(" temperature1:" + this.temperature1);
        sb.append(" temperature2:" + this.temperature2);
        sb.append(" temperature3:" + this.temperature3);
        sb.append(" temperature4:" + this.temperature4);
        sb.append(" temperature5:" + this.temperature5);
        sb.append(" temperature6:" + this.temperature6);
        sb.append(" temperature7:" + this.temperature7);
        sb.append(" temperature8:" + this.temperature8);
        sb.append(" humidity1:" + this.humidity1);
        sb.append(" humidity1:" + this.humidity2);
        sb.append(" humidity1:" + this.humidity3);
        sb.append(" humidity1:" + this.humidity4);
        sb.append(" humidity1:" + this.humidity5);
        sb.append(" humidity1:" + this.humidity6);
        sb.append(" humidity1:" + this.humidity7);
        sb.append(" humidity1:" + this.humidity8);
        sb.append(" humidity1:" + this.doorState1);
        sb.append(" humidity1:" + this.doorState2);
        sb.append(" humidity1:" + this.doorState3);
        sb.append(" humidity1:" + this.doorState4);
        sb.append(" humidity1:" + this.doorState5);
        sb.append(" humidity1:" + this.doorState6);
        sb.append(" humidity1:" + this.doorState7);
        sb.append(" humidity1:" + this.doorState8);
        String sb2 = sb.toString();
        if (!sb2.equals(lastDataString)) {
            lastDataString = sb2;
            Loger.writeLog("SHJ", sb2);
        } else if (System.currentTimeMillis() - lastLogTime > 300000) {
            lastLogTime = System.currentTimeMillis();
            Loger.writeLog("SHJ", sb2);
        }
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            Shj.onUpdateShjStatusV2(this.paperMachineState, this.coinMachineState, this.posMachineState, this.wkyState, this.temperature, this.doorState, this.paperMoney, this.coinMoney, this.machineId, this.temperature1, this.temperature2, this.temperature3, this.temperature4, this.temperature5, this.temperature6, this.temperature7, this.temperature8, this.humidity1, this.humidity2, this.humidity3, this.humidity4, this.humidity5, this.humidity6, this.humidity7, this.humidity8, this.doorState1, this.doorState2, this.doorState3, this.doorState4, this.doorState5, this.doorState6, this.doorState7, this.doorState8);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

package com.shj.commandV2;

import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.serotonin.modbus4j.code.ExceptionCode;
import com.serotonin.modbus4j.code.FunctionCode;
import com.shj.command.Command;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class CommandV2_Up_SetCommand extends CommandV2 {
    byte cmdType = 0;
    boolean isSetCommand = false;

    public CommandV2_Up_SetCommand() {
        setType(Command.CommandType.Command);
        setHead((short) 112);
    }

    public int getCmdType() {
        return ObjectHelper.intFromBytes(new byte[]{this.cmdType}, 0, 1);
    }

    public boolean isSetCommand() {
        return this.isSetCommand;
    }

    public void setCoinSystem(boolean z, int i) {
        this.cmdType = (byte) 1;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setShelfModel(boolean z, int i, int i2) {
        this.cmdType = (byte) 2;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setEnginStopYZ(boolean z, int i, int i2) {
        this.cmdType = (byte) 3;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setMergeShelf(boolean z, int i, int i2) {
        this.cmdType = (byte) 4;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 1);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void ClearMergeShelf() {
        this.cmdType = (byte) 5;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void setMergeShelSynRunTime(boolean z, int i, int i2) {
        this.cmdType = (byte) 6;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setNewMergeShelSynRunTime(boolean z, int i, int i2) {
        this.cmdType = (byte) -122;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setEnginDLYZ(boolean z, int i, int i2) {
        this.cmdType = (byte) 7;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setMachineId(boolean z, String str) {
        this.cmdType = (byte) 8;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[12];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, str.getBytes(), 2, 10);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setTime(boolean z, Date date) {
        this.cmdType = (byte) 9;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[9];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, 0, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getYear() % 100, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getMonth() + 1, 4, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getDate(), 5, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getHours(), 6, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getMinutes(), 7, 1);
            ObjectHelper.updateBytes(this.dataV2, date.getSeconds(), 8, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setPrecision(boolean z, int i) {
        this.cmdType = (byte) 16;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setPickDoorCloseTime(boolean z, int i, int i2) {
        this.cmdType = (byte) 17;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setConnectLift(boolean z, int i, boolean z2, boolean z3) {
        this.cmdType = (byte) 18;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 1 : 2, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, z3 ? 1 : 0, 4, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setGuardDoorCloseTime(boolean z, int i, int i2) {
        this.cmdType = (byte) 19;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setCoinCount_05(boolean z, int i) {
        this.cmdType = ClosedCaptionCtrl.MISC_CHAN_1;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setCoinCount_10(boolean z, int i) {
        this.cmdType = (byte) 21;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setLight(boolean z, int i, int i2) {
        this.cmdType = FunctionCode.WRITE_MASK_REGISTER;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setYLICCard(boolean z, boolean z2) {
        this.cmdType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 0 : 2, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setPaperMoney(boolean z, int i) {
        this.cmdType = (byte) 24;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setPaperModel(boolean z, int i) {
        this.cmdType = ClosedCaptionCtrl.MID_ROW_CHAN_2;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setAcceptPaperMinChargeMoney(boolean z, int i) {
        this.cmdType = (byte) 32;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setAutoChargeTime(boolean z, int i) {
        this.cmdType = ClosedCaptionCtrl.BACKSPACE;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setAutoEatMoneyTime(boolean z, int i) {
        this.cmdType = (byte) 34;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setRemainMoneyPro(boolean z, int i) {
        this.cmdType = (byte) 35;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setGoodsDroopCheck(boolean z, int i, boolean z2) {
        this.cmdType = (byte) 36;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 1 : 2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setBeltTime(boolean z, int i, int i2, int i3) {
        this.cmdType = ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[8];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
            ObjectHelper.updateBytes(this.dataV2, i3, 6, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setBlock41(boolean z, int i, boolean z2) {
        this.cmdType = ClosedCaptionCtrl.ROLL_UP_CAPTIONS_3_ROWS;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, !z2 ? 1 : 0, 4, 1);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setcontinueSaleAfterBlock(boolean z, int i, boolean z2) {
        this.cmdType = ClosedCaptionCtrl.ROLL_UP_CAPTIONS_4_ROWS;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 2 : 1, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setWkY(boolean z, int i, int i2, int i3) {
        this.cmdType = (byte) 40;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 4, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setYSJ(boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.cmdType = ClosedCaptionCtrl.RESUME_DIRECT_CAPTIONING;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[9];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 4, 1);
            ObjectHelper.updateBytes(this.dataV2, i4, 5, 1);
            ObjectHelper.updateBytes(this.dataV2, i5, 6, 1);
            ObjectHelper.updateBytes(this.dataV2, i6, 7, 1);
            ObjectHelper.updateBytes(this.dataV2, i7, 8, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setClearSales(boolean z, String str) {
        this.cmdType = (byte) 48;
        this.isSetCommand = z;
        this.dataV2 = new byte[8];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, str.getBytes(), 2, 6);
    }

    public void setResetPassword(boolean z, String str, String str2) {
        this.cmdType = (byte) 49;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[14];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, str.getBytes(), 2, 6);
            ObjectHelper.updateBytes(this.dataV2, str2.getBytes(), 8, 6);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setClearBlocks(boolean z) {
        this.cmdType = (byte) 50;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        } else {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        }
    }

    public void setClearEnginError(boolean z) {
        this.cmdType = (byte) 51;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        } else {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        }
    }

    public void setClearLiftError(boolean z) {
        this.cmdType = (byte) 52;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        } else {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        }
    }

    public void setClearBlock41s(boolean z) {
        this.cmdType = (byte) 53;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        } else {
            this.dataV2 = new byte[2];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        }
    }

    public void setWKYState(boolean z, int i) {
        this.cmdType = (byte) 54;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setWKYSets(boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.cmdType = (byte) 55;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[11];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 4, 1);
            ObjectHelper.updateBytes(this.dataV2, i4, 5, 1);
            ObjectHelper.updateBytes(this.dataV2, i5, 6, 1);
            ObjectHelper.updateBytes(this.dataV2, i6, 7, 1);
            ObjectHelper.updateBytes(this.dataV2, i7, 8, 1);
            ObjectHelper.updateBytes(this.dataV2, i8, 9, 1);
            ObjectHelper.updateBytes(this.dataV2, i9, 10, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void TestShelf(boolean z, int i, int i2) {
        this.cmdType = (byte) 56;
        this.isSetCommand = z;
        this.dataV2 = new byte[5];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
    }

    public void TestChargeCoin(boolean z, int i, int i2, int i3) {
        this.cmdType = (byte) 57;
        this.isSetCommand = z;
        this.dataV2 = new byte[6];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
        ObjectHelper.updateBytes(this.dataV2, i3, 5, 1);
    }

    public void queryCoinBalance() {
        this.cmdType = (byte) 64;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setShelfCount() {
        this.cmdType = (byte) 65;
        this.isSetCommand = false;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setShelfInfo(boolean z, int i) {
        this.cmdType = (byte) 66;
        this.isSetCommand = z;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setSales_Day(boolean z, int i, int i2, int i3) {
        this.cmdType = (byte) 67;
        this.isSetCommand = z;
        this.dataV2 = new byte[6];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i / 100, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i % 100, 3, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 4, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 5, 1);
    }

    public void setSales_Month(boolean z, int i, int i2) {
        this.cmdType = (byte) 68;
        this.isSetCommand = z;
        this.dataV2 = new byte[5];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i / 100, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i % 100, 3, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 4, 1);
    }

    public void setSales_Year(boolean z, int i) {
        this.cmdType = (byte) 69;
        this.isSetCommand = z;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i / 100, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i % 100, 3, 1);
    }

    public void setSales_All(boolean z, int i) {
        this.cmdType = (byte) 70;
        this.isSetCommand = z;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setSales_Shelf(boolean z, int i) {
        this.cmdType = (byte) 71;
        this.isSetCommand = z;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setAutoAdjust_OfferGoodsCheck_frequency(boolean z, int i) {
        this.cmdType = (byte) 72;
        this.isSetCommand = z;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setOfferGoodsCheck_Sensitivity(boolean z, int i, int i2) {
        this.cmdType = (byte) 73;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void TestOfferGoodsCheck(boolean z, int i, int i2) {
        this.cmdType = (byte) 80;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setSetLiftPos(boolean z, int i, int i2, int i3) {
        this.cmdType = (byte) 81;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 4, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
    }

    public void setSetLiftSpeed(boolean z, int i, int i2) {
        this.cmdType = (byte) 82;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setSetLiftSensitivity(boolean z, int i, int i2) {
        this.cmdType = (byte) 83;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void TestLift(int i, int i2) {
        this.cmdType = (byte) 84;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
    }

    public void setWBLPos(boolean z, int i, int i2, int i3, int i4) {
        this.cmdType = (byte) 85;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[8];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
            ObjectHelper.updateBytes(this.dataV2, i3, 6, 1);
            ObjectHelper.updateBytes(this.dataV2, i4, 7, 1);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i4, 3, 1);
    }

    public void setHFJ_HZ_spacing(int i, int i2, int i3) {
        this.cmdType = (byte) 86;
        this.isSetCommand = true;
        this.dataV2 = new byte[7];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
        ObjectHelper.updateBytes(this.dataV2, i3, 5, 2);
    }

    public void TestHFJ_JG(int i, int i2, int i3) {
        this.cmdType = (byte) 87;
        this.dataV2 = new byte[5];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 4, 1);
    }

    public void TestHFJ(int i, int i2, boolean z) {
        this.cmdType = (byte) 88;
        this.dataV2 = new byte[7];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
        ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
        ObjectHelper.updateBytes(this.dataV2, !z ? 1 : 0, 6, 1);
    }

    public void changePaperMoney(boolean z, int i) {
        this.cmdType = (byte) 89;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void connectWKY(boolean z, boolean z2) {
        this.cmdType = (byte) 96;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, !z2 ? 1 : 0, 2, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void testPaperMoneyMachiine() {
        this.cmdType = (byte) 97;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void testCoinMachiine() {
        this.cmdType = (byte) 98;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void setOneKeyFullGoods() {
        this.cmdType = (byte) 99;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void setWBLHeatTime(boolean z, int i, int i2) {
        this.cmdType = (byte) 100;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i, 4, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setWBLHeat(boolean z, boolean z2) {
        this.cmdType = (byte) 101;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 1 : 0, 2, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setOpenGridMachineGrid(int i, int i2) {
        this.isSetCommand = true;
        if (i2 == -1) {
            this.cmdType = (byte) -124;
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.cmdType = (byte) -123;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
    }

    public void setBoxAutoPosition(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        this.cmdType = (byte) 102;
        this.isSetCommand = true;
        this.dataV2 = new byte[12];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 4, 1);
        ObjectHelper.updateBytes(this.dataV2, i4, 5, 1);
        ObjectHelper.updateBytes(this.dataV2, i5, 6, 1);
        ObjectHelper.updateBytes(this.dataV2, i6, 7, 1);
        ObjectHelper.updateBytes(this.dataV2, i7, 8, 1);
        ObjectHelper.updateBytes(this.dataV2, i8, 9, 1);
        ObjectHelper.updateBytes(this.dataV2, i9, 10, 1);
        ObjectHelper.updateBytes(this.dataV2, i10, 11, 1);
    }

    public void setBoxPosition(boolean z, int i, int i2) {
        this.cmdType = (byte) 103;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setBoxSpeed(boolean z, int i, int i2) {
        this.cmdType = (byte) 104;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 2, 1);
    }

    public void setBoxLmd(boolean z, int i, int i2) {
        this.cmdType = (byte) 105;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 2, 1);
    }

    public void exportSysData() {
        this.cmdType = (byte) 112;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void importSysData() {
        this.cmdType = (byte) 113;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void testHfjOfferGoods(int i, int i2) {
        this.cmdType = (byte) 114;
        this.isSetCommand = true;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
    }

    public void queryQgqState(int i) {
        this.cmdType = (byte) 115;
        this.isSetCommand = false;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void queryQgqStateNew(int i) {
        this.cmdType = (byte) 120;
        this.isSetCommand = false;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void oneKeySetFullGoodsByJgh(int i) {
        this.cmdType = (byte) 121;
        this.isSetCommand = true;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setHumidifier(boolean z, int i, int i2, int i3) {
        this.cmdType = Byte.MIN_VALUE;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 2, 1);
    }

    public void setfaultTemperatureProbe(boolean z, int i) {
        this.cmdType = (byte) -127;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[3];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setElectromagneticLockTime(boolean z, int i, int i2) {
        this.cmdType = (byte) -126;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 2);
            return;
        }
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
    }

    public void setFindPeoperSensitivity(boolean z, int i, int i2) {
        this.cmdType = (byte) -121;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setFindPeoperDistance(boolean z, int i, int i2) {
        this.cmdType = (byte) -120;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void readLightInspectionStatus(int i) {
        this.cmdType = (byte) -111;
        this.isSetCommand = false;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void setOpenelEctricLock(boolean z, String str) {
        this.cmdType = (byte) -112;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[8];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, str.getBytes(), 2, 6);
        } else {
            this.dataV2 = new byte[8];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, str.getBytes(), 2, 6);
        }
        Loger.writeLog("SHJ;SET", "下位机上报指令执行设置结果:" + ObjectHelper.hex2String(this.dataV2, this.dataV2.length));
    }

    public void programUpdateStart(int i, int i2, int i3, int i4) {
        this.cmdType = (byte) -103;
        this.isSetCommand = true;
        this.dataV2 = new byte[9];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 2, 2);
        ObjectHelper.updateBytes(this.dataV2, i, 4, 1);
        ObjectHelper.updateBytes(this.dataV2, i2, 5, 1);
        ObjectHelper.updateBytes(this.dataV2, i3, 6, 2);
        ObjectHelper.updateBytes(this.dataV2, i4, 8, 1);
    }

    public void programUpdateSendData(int i, byte[] bArr) {
        this.cmdType = (byte) -103;
        this.isSetCommand = true;
        int length = bArr.length;
        this.dataV2 = new byte[length + 6];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, length, 2, 2);
        ObjectHelper.updateBytes(this.dataV2, i, 4, 2);
        ObjectHelper.updateBytes(this.dataV2, bArr, 6, length);
    }

    public void programUpdateEnd() {
        this.cmdType = (byte) -103;
        this.isSetCommand = true;
        this.dataV2 = new byte[4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, 255, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, 255, 3, 1);
    }

    public void shelfDrvingVersionQuery(int i) {
        this.cmdType = (byte) -110;
        this.isSetCommand = false;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void shelfMergeStateQuery(int i) {
        this.cmdType = (byte) -109;
        this.isSetCommand = false;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void shelfLightControl(List<Integer> list) {
        this.cmdType = (byte) -108;
        this.isSetCommand = true;
        int size = list.size();
        this.dataV2 = new byte[(size * 2) + 4];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 2, 1);
        ObjectHelper.updateBytes(this.dataV2, size, 3, 1);
        for (int i = 0; i < size; i++) {
            ObjectHelper.updateBytes(this.dataV2, list.get(i).intValue(), (i * 2) + 4, 2);
        }
    }

    public void setTopLight(boolean z, int i, int i2) {
        this.cmdType = (byte) -107;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 1);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setLightBoxRollingInterval(boolean z, int i) {
        this.cmdType = (byte) -106;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void rebootMachine() {
        this.cmdType = (byte) -105;
        this.isSetCommand = true;
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
    }

    public void setDisinfectantTankControl(boolean z, int i) {
        this.cmdType = (byte) -96;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setWaterInletSolenoidValveControl(boolean z, int i) {
        this.cmdType = (byte) -95;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[4];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 2);
            return;
        }
        this.dataV2 = new byte[2];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
    }

    public void setMixingTankControl(boolean z, int i, int i2) {
        this.cmdType = (byte) -94;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[5];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 3, 2);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void temperatureControlShopping(boolean z, int i, boolean z2, int i2, int i3) {
        this.cmdType = (byte) 10;
        this.isSetCommand = z;
        if (z) {
            this.dataV2 = new byte[6];
            ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
            ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
            ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
            ObjectHelper.updateBytes(this.dataV2, z2 ? 1 : 0, 3, 1);
            ObjectHelper.updateBytes(this.dataV2, i2, 4, 1);
            ObjectHelper.updateBytes(this.dataV2, i3, 5, 1);
            return;
        }
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 0, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    public void temperatureControlShoppingClear(int i) {
        this.cmdType = ExceptionCode.GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND;
        this.isSetCommand = true;
        this.dataV2 = new byte[3];
        ObjectHelper.updateBytes(this.dataV2, (int) this.cmdType, 0, 1);
        ObjectHelper.updateBytes(this.dataV2, 1, 1, 1);
        ObjectHelper.updateBytes(this.dataV2, i, 2, 1);
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        Loger.writeLog("SHJ;SET", "上位机设置 :" + ObjectHelper.hex2String(this.dataV2, this.dataV2.length));
    }
}

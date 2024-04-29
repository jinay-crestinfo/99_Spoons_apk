package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.command.CommandManager;

@XYClass(KEY = "HEAD", VALUE = "0x02")
/* loaded from: classes2.dex */
public class CommandV2_Down_ShelfStatus extends CommandV2 {
    private int state = 0;
    private int shelf = 0;

    public CommandV2_Down_ShelfStatus() {
        setHead((short) 2);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.state = ObjectHelper.intFromBytes(bArr, this.dataOffset, 1);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset + 1, 2);
        int i = this.state;
        if (i == 1) {
            this.state = 0;
        } else if (i == 2) {
            this.state = 2;
        } else if (i == 3 || i == 4) {
            this.state = 4;
        }
        Loger.writeLog("SHJ;SALES", "货道状态：shelf:" + this.shelf + " state:" + this.state);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        int i;
        super.doCommand();
        try {
            int i2 = this.state;
            if (i2 != 10) {
                Shj.setOfferGoodsDiviceState(i2);
            }
            if (Shj.isStoreGoodsInfoInVMC()) {
                int i3 = this.state;
                if (i3 < 5 || i3 == 10) {
                    Shj.onUpdateShelfState(this.shelf, i3);
                }
            } else {
                int i4 = this.state;
                if ((i4 != 2 && i4 < 5) || i4 == 10) {
                    Shj.onUpdateShelfState(this.shelf, i4);
                }
            }
            Command currentCommand = CommandManager.getCurrentCommand();
            if (currentCommand != null) {
                if (((currentCommand instanceof CommandV2_Up_SelectGoods) || (currentCommand instanceof CommandV2_Up_UnSelectGoods)) && (i = this.shelf) > 0 && this.state == 0) {
                    Shj.onSelectGoodsOnShelf(Integer.valueOf(i));
                }
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

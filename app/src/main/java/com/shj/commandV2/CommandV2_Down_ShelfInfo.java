package com.shj.commandV2;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.command.Command;
import com.tencent.wxpayface.WxfacePayCommonCode;

@XYClass(KEY = "HEAD", VALUE = "0x11")
/* loaded from: classes2.dex */
public class CommandV2_Down_ShelfInfo extends CommandV2 {
    private int shelf = 0;
    private int price = 0;
    private int count = 0;
    private int capacity = 0;
    private String goodsCode = "0";
    private int status = 0;
    private int gdjc = -1;
    private int jgh = -1;
    private int layer = -1;

    public CommandV2_Down_ShelfInfo() {
        setHead((short) 17);
        setType(Command.CommandType.Receive);
    }

    @Override // com.shj.commandV2.CommandV2, com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        this.shelf = ObjectHelper.intFromBytes(bArr, this.dataOffset, 2);
        this.price = ObjectHelper.intFromBytes(bArr, this.dataOffset + 2, 4);
        this.count = ObjectHelper.intFromBytes(bArr, this.dataOffset + 6, 1);
        this.capacity = ObjectHelper.intFromBytes(bArr, this.dataOffset + 7, 1);
        String str = "" + ObjectHelper.intFromBytes(bArr, this.dataOffset + 8, 2);
        this.goodsCode = str;
        if (str.length() < 4) {
            String str2 = "0000" + this.goodsCode;
            this.goodsCode = str2;
            this.goodsCode = str2.substring(str2.length() - 4);
        }
        this.status = ObjectHelper.intFromBytes(bArr, this.dataOffset + 10, 1);
        if (bArr.length > this.dataOffset + 12) {
            this.gdjc = ObjectHelper.intFromBytes(bArr, this.dataOffset + 11, 1);
        }
        if (bArr.length > this.dataOffset + 13) {
            this.jgh = ObjectHelper.intFromBytes(bArr, this.dataOffset + 12, 1);
        }
        if (bArr.length > this.dataOffset + 14) {
            this.layer = ObjectHelper.intFromBytes(bArr, this.dataOffset + 13, 1);
        }
        int i = this.status;
        if (i == 1 || i == 3) {
            this.status = 3;
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "货道状态：shelf:" + this.shelf + " state:" + this.status);
        } else if (i == 4) {
            this.status = 4;
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "货道状态：shelf:" + this.shelf + " state:" + this.status);
        }
        Loger.writeLog("SHJ;SET", "下位机上报货道信息：shelf:" + this.shelf + " price:" + this.price + " count:" + this.count + " capacity:" + this.capacity + " goodsCode:" + this.goodsCode + " state:" + this.status + " jgh:" + this.jgh + " laywer:" + this.layer);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        try {
            if (Shj.isStoreGoodsInfoInVMC()) {
                Shj.onUpdateShelfInfo(this.shelf, this.price, this.count, this.capacity, this.goodsCode, this.status, this.gdjc, this.jgh, this.layer);
                return;
            }
            if (!ShjDbHelper.hashShelf(this.shelf)) {
                ShjDbHelper.saveShelfInfo(this.shelf, this.layer, this.status, this.price, this.capacity, this.count, "", "" + this.goodsCode, "", "");
            }
            Shj.onUpdateShelfInfo(this.shelf, -1, this.count, this.capacity, "", this.status, this.gdjc, this.jgh, this.layer);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}

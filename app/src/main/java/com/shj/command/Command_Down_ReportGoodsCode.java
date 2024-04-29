package com.shj.command;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.anno.XYClass;
import com.shj.Shj;

@XYClass(KEY = "HEAD", VALUE = "0x21")
/* loaded from: classes2.dex */
public class Command_Down_ReportGoodsCode extends Command {
    private String goodsCode = "-1";
    private int shelf = 0;

    @Override // com.shj.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        String str = "" + ObjectHelper.intFromBytes(bArr, 4, 2);
        this.goodsCode = str;
        if (str.length() < 4) {
            String str2 = "0000" + this.goodsCode;
            this.goodsCode = str2;
            this.goodsCode = str2.substring(str2.length() - 4);
        }
        this.shelf = ObjectHelper.intFromBytes(bArr, 2, 2);
        Loger.writeLog("SHJ", "收到下位机上报：货道对应的商品编码 0x21 shelf" + this.shelf + " goodsCode" + this.goodsCode);
        return init;
    }

    @Override // com.shj.command.Command
    public void doCommand() {
        super.doCommand();
        Shj.onUpdateShelfGoodsCode(Integer.valueOf(this.shelf), this.goodsCode);
    }
}

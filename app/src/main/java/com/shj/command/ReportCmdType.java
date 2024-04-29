package com.shj.command;

import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public enum ReportCmdType {
    R_FA_0A("FA_0A VMC 查询命令", ObjectHelper.intFromShorts(new short[]{250, 10}, 0, 2)),
    R_FA_0B("FA_0B VMC 报告收到钱币", ObjectHelper.intFromShorts(new short[]{250, 11}, 0, 2)),
    R_FA_0F("FA_0F VMC 报告商品价格", ObjectHelper.intFromShorts(new short[]{250, 15}, 0, 2)),
    R_FA_0C("FA_0C VMC 报告商品存货量", ObjectHelper.intFromShorts(new short[]{250, 12}, 0, 2)),
    R_FA_21("FA_21 VMC 报告货道对应的商品编码", ObjectHelper.intFromShorts(new short[]{250, 33}, 0, 2)),
    R_FA_10("FA_10 VMC 报告已复位", ObjectHelper.intFromShorts(new short[]{250, 16}, 0, 2)),
    R_FA_12("FA_12 VMC 报告硬币余额", ObjectHelper.intFromShorts(new short[]{250, 18}, 0, 2)),
    R_FA_13("FA_13 VMC 报告纸币余额", ObjectHelper.intFromShorts(new short[]{250, 19}, 0, 2)),
    R_FA_0D("FA_0D VMC 报告状态", ObjectHelper.intFromShorts(new short[]{250, 13}, 0, 2)),
    R_FA_FE("FA_FE POS 请求显示", ObjectHelper.intFromShorts(new short[]{250, 254}, 0, 2)),
    R_FA_14("FA_14 下位机选择商品或取消选择商品", ObjectHelper.intFromShorts(new short[]{250, 20}, 0, 2)),
    R_FAFB_0x21("FAFB_0x21 VMC 收钱通知 ", ObjectHelper.intFromShorts(new short[]{250, 251, 33}, 0, 3)),
    R_FAFB_0x23("FAFB_0x23 VMC 报告当前金额", ObjectHelper.intFromShorts(new short[]{250, 251, 35}, 0, 3)),
    R_FAFB_0x24("FAFB_0x24 POS 请求显示", ObjectHelper.intFromShorts(new short[]{250, 251, 36}, 0, 3)),
    R_FAFB_0x26("FAFB_0x26 找零的金额", ObjectHelper.intFromShorts(new short[]{250, 251, 38}, 0, 3)),
    R_FAFB_0x11("FAFB_0x11 VMC 报告货道的价格，库存，容量，货道商品编号", ObjectHelper.intFromShorts(new short[]{250, 251, 17}, 0, 3)),
    R_FAFB_0x12("FAFB_0x12 设置货道的价格", ObjectHelper.intFromShorts(new short[]{250, 251, 18}, 0, 3)),
    R_FAFB_0x13("FAFB_0x13 设置货道的库存", ObjectHelper.intFromShorts(new short[]{250, 251, 19}, 0, 3)),
    R_FAFB_0x14("FAFB_0x14 设置货道的容量", ObjectHelper.intFromShorts(new short[]{250, 251, 20}, 0, 3)),
    R_FAFB_0x15("FAFB_0x15 设置货道商品编号", ObjectHelper.intFromShorts(new short[]{250, 251, 21}, 0, 3)),
    R_FAFB_0x02("FAFB_0x02 货道状态", ObjectHelper.intFromShorts(new short[]{250, 251, 2}, 0, 3)),
    R_FAFB_0x04("FAFB_0x04 VMC 出货状态提示 ", ObjectHelper.intFromShorts(new short[]{250, 251, 4}, 0, 3)),
    R_FAFB_0x05("FAFB_0x05 下位机选择(取消选择)货道", ObjectHelper.intFromShorts(new short[]{250, 251, 5}, 0, 3)),
    R_FAFB_0x52("FAFB_0x52 获取状态信息 ", ObjectHelper.intFromShorts(new short[]{250, 251, 82}, 0, 3)),
    R_FAFB_0x31("FAFB_0x31 请求信息同步 ", ObjectHelper.intFromShorts(new short[]{250, 251, 49}, 0, 3));

    private int index;
    private String name;

    ReportCmdType(String str, int i) {
        setName(str);
        setIndex(i);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public static ReportCmdType int2Enum(int i) {
        ReportCmdType reportCmdType = R_FA_0A;
        for (ReportCmdType reportCmdType2 : values()) {
            if (reportCmdType2.getIndex() == i) {
                return reportCmdType2;
            }
        }
        return reportCmdType;
    }
}

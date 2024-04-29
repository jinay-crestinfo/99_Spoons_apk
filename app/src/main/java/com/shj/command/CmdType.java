package com.shj.command;

import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public enum CmdType {
    FA_20("FA_20  上位机查询货道是否存在", ObjectHelper.intFromShorts(new short[]{250, 32}, 0, 2)),
    FA_07("FA_07 上位机驱动货道", ObjectHelper.intFromShorts(new short[]{250, 7}, 0, 2)),
    FA_00("FA_00 上位机应答操作", ObjectHelper.intFromShorts(new short[]{250, 0}, 0, 2)),
    FA_04("FA_04 上位机设置商品价格", ObjectHelper.intFromShorts(new short[]{250, 4}, 0, 2)),
    FA_05("FA_05 上位机设置商品存货量", ObjectHelper.intFromShorts(new short[]{250, 5}, 0, 2)),
    FA_06("FA_06 上位机设置出货检测", ObjectHelper.intFromShorts(new short[]{250, 6}, 0, 2)),
    FA_08("FA_08 上位机查询商品是否有效", ObjectHelper.intFromShorts(new short[]{250, 8}, 0, 2)),
    FA_01("FA_01 上位机选货购买", ObjectHelper.intFromShorts(new short[]{250, 1}, 0, 2)),
    FA_10("FA_10 上位取消选货", ObjectHelper.intFromShorts(new short[]{250, 16}, 0, 2)),
    FA_09("FA_09 上位机启用 POS 寻卡", ObjectHelper.intFromShorts(new short[]{250, 9}, 0, 2)),
    FA_03("FA_03 上位机请求找零", ObjectHelper.intFromShorts(new short[]{250, 3}, 0, 2)),
    FA_0A("FA_0A 上位机禁止 POS 寻卡", ObjectHelper.intFromShorts(new short[]{250, 10}, 0, 2)),
    FA_13("FA_13 上位请求设置纸币接收面额", ObjectHelper.intFromShorts(new short[]{250, 19}, 0, 2)),
    FA_14("FA_14 上位请求设置硬币接收面额", ObjectHelper.intFromShorts(new short[]{250, 20}, 0, 2)),
    FA_12("FA_12 上位请求进入或退出充值模式", ObjectHelper.intFromShorts(new short[]{250, 18}, 0, 2)),
    FA_02("FA_02 设置货道容量", ObjectHelper.intFromShorts(new short[]{250, 2}, 0, 2)),
    FA_0e("FA_0e 清除卡货故障", ObjectHelper.intFromShorts(new short[]{250, 14}, 0, 2)),
    FA_0f("FA_0f 上位机复位", ObjectHelper.intFromShorts(new short[]{250, 15}, 0, 2)),
    FA_11("FA_11 上位机收到了钱币或设置 VMC 金额", ObjectHelper.intFromShorts(new short[]{250, 17}, 0, 2)),
    FAFB_0x25("FAFB_0x25 上位机找零请求", ObjectHelper.intFromShorts(new short[]{250, 251, 37}, 0, 3)),
    FAFB_0x27("FAFB_0x27 上位机收到钱币", ObjectHelper.intFromShorts(new short[]{250, 251, 39}, 0, 3)),
    FAFB_0x12("FAFB_0x12 设置货道的价格", ObjectHelper.intFromShorts(new short[]{250, 251, 18}, 0, 3)),
    FAFB_0x13("FAFB_0x13 设置货道的库存 ", ObjectHelper.intFromShorts(new short[]{250, 251, 19}, 0, 3)),
    FAFB_0x14("FAFB_0x14 设置货道的容量", ObjectHelper.intFromShorts(new short[]{250, 251, 20}, 0, 3)),
    FAFB_0x15("FAFB_0x15 设置货道商品编号 ", ObjectHelper.intFromShorts(new short[]{250, 251, 21}, 0, 3)),
    FAFB_0x16("FAFB_0x16 上位机设置 VMC 查询(POLL)访问间隔", ObjectHelper.intFromShorts(new short[]{250, 251, 22}, 0, 3)),
    FAFB_0x01("FAFB_0x01 上位机查询某个货道正常", ObjectHelper.intFromShorts(new short[]{250, 251, 1}, 0, 3)),
    FAFB_0x03("FAFB_0x03 上位机选择购买", ObjectHelper.intFromShorts(new short[]{250, 251, 1}, 0, 3)),
    FAFB_0x05("FAFB_0x05 下位机选择(取消选择)货道或者", ObjectHelper.intFromShorts(new short[]{250, 251, 5}, 0, 3)),
    FAFB_0x51("FAFB_0x51 上位机请求获取状态信息", ObjectHelper.intFromShorts(new short[]{250, 251, 81}, 0, 3)),
    FAFB_0x31("FAFB_0x31 请求信息同步", ObjectHelper.intFromShorts(new short[]{250, 251, 49}, 0, 3));

    private int index;
    private String name;

    CmdType(String str, int i) {
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

    public static CmdType int2Enum(int i) {
        CmdType cmdType = FA_20;
        for (CmdType cmdType2 : values()) {
            if (cmdType2.getIndex() == i) {
                return cmdType2;
            }
        }
        return cmdType;
    }
}

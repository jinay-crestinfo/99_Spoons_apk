package com.shj;

/* loaded from: classes2.dex */
public enum OfferState {
    Offering("正在出货", 1),
    OfferSuccess("出货成功", 2),
    Blocked("卡货", 3),
    EngineError("电机未正常停止", 4),
    CargoLaneInvalid("货道无效", 5),
    NoEngine("电机不存在", 6),
    LiftError("升降机故障", 7),
    LiftUping("升降机正在上升", 16),
    LiftDowning("升降机正在下降", 17),
    LiftUpError("升降机上升错误", 18),
    LiftDownError("升降机下降错误", 19),
    WBLClosingFrontDoor("正在关闭微波炉前门", 20),
    NoWBLCloseFrontDoorError("微波炉前门关闭错误", 21),
    WBLOpeiningBackDoor("正在打开微波炉后门", 22),
    NoWBLOpenBackDoorError("微波炉后门打开错误", 23),
    Push2WBL("正在将盒饭推入微波炉", 24),
    WBLClosingBackDoor("正在关闭微波炉后门", 25),
    NoWBLCloseBackDoorError("微波炉后门关闭错误", 32),
    WBLNoGoods("微波炉内没检测到盒饭", 33),
    WBLRunning("盒饭正在加热", 34),
    WBLTimer("盒饭加热剩余时间", 35),
    WBLWaitting2PickGoods("请取出盒饭", 36),
    CGResetError("撑杆回位错误", 37),
    WBLOpeningFrontDoor("正在打开微波炉前门", 38),
    WBLOpenFrontDoorError("打开微波炉前门出错", 39),
    LiftCGPushError("升降台撑杆推出错误", 40),
    LiftInWBLError("升降台进微波炉错误", 41),
    LiftOutWBLError("升降台出微波炉错误", 48),
    WBLInnerTGPushError("微波炉内推杆推出错误", 49),
    WBLInnerTGBackError("微波炉内推杆收回错误", 50),
    BeltCheckError("皮带自检错误", 10),
    ShelfNoGoods("货道缺货", 252),
    ShelfOfferGoodsPaused("货道暂停售卖", 253),
    OfferGoodsPaused("暂停出货", 254),
    BusinessStoped("出货终止", 255),
    Unkonw("未定义", 1000);

    private int index;
    private String name;

    OfferState(String str, int i) {
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

    public static OfferState int2Enum(int i) {
        OfferState offerState = Unkonw;
        OfferState[] values = values();
        int length = values.length;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            OfferState offerState2 = values[i2];
            if (offerState2.getIndex() == i) {
                z = true;
                offerState = offerState2;
                break;
            }
            i2++;
        }
        if (!z) {
            offerState.setIndex(i);
        }
        return offerState;
    }
}

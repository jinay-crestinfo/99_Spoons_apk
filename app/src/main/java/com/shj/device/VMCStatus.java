package com.shj.device;

import android.content.Context;
import com.oysb.utils.CommonTool;

/* loaded from: classes2.dex */
public enum VMCStatus {
    OfferGoodsSuccess("出货成功", 0),
    DoChargeSuccess("找零成功", 1),
    GoodsLack("商品缺货", 2),
    GoodsInvalidate("商品无效", 3),
    DoChargeFailed("找零失败", 4),
    GoodsBlocked("卡货", 5),
    GoodsValidate("商品有效", 6),
    OfferingGoods("正在出货", 7),
    NoEnging("电机不存在", 8),
    GearStopError("齿轮盒停止位置不对", 9),
    EngingRunning("电机正常", 10),
    CommunicationsError("通讯错误", 11),
    CardPaySuccess("银联卡扣钱成功", 12),
    CardPayFailed("银联卡扣钱失败", 13),
    PosSinging("POS  正在签到", 14),
    PosSingSuccess("POS  签到成功", 15),
    DoorIsOpen("门打开", 16),
    DoorIsClose("门关闭", 17),
    Charging("正在找零", 18),
    ClearGoodsBlockSuccess("卡货清除成功", 19),
    ClearGoodsBLockFailed("卡货清除失败", 20),
    LiftChecking("升降机正在自检", 33),
    VMCChecking("下位机正在自检", 34),
    Normal("正常", 254);

    private int index;
    private String name;

    VMCStatus(String str, int i) {
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

    public static VMCStatus int2Enum(int i) {
        VMCStatus vMCStatus = CardPayFailed;
        for (VMCStatus vMCStatus2 : values()) {
            if (vMCStatus2.getIndex() == i) {
                return vMCStatus2;
            }
        }
        return vMCStatus;
    }

    /* renamed from: com.shj.device.VMCStatus$1 */
    /* loaded from: classes2.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$device$VMCStatus;

        static {
            int[] iArr = new int[VMCStatus.values().length];
            $SwitchMap$com$shj$device$VMCStatus = iArr;
            try {
                iArr[VMCStatus.Normal.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.LiftChecking.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.VMCChecking.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public String getVMCStateInfo(Context context) {
        String name;
        try {
            String language = CommonTool.getLanguage(context);
            int i = AnonymousClass1.$SwitchMap$com$shj$device$VMCStatus[ordinal()];
            if (i == 1) {
                name = language.equalsIgnoreCase("en") ? "Normal" : getName();
            } else if (i == 2) {
                name = language.equalsIgnoreCase("en") ? "LiftChecking" : getName();
            } else {
                if (i != 3) {
                    return "";
                }
                name = language.equalsIgnoreCase("en") ? "VMCChecking" : getName();
            }
            return name;
        } catch (Exception unused) {
            return "";
        }
    }
}

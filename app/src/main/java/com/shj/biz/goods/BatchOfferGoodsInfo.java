package com.shj.biz.goods;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderPayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class BatchOfferGoodsInfo {
    private int curOfferingIdx;
    private int money;
    private MoneyType moneyType;
    private String remark;
    private boolean checkGoodsCount = true;
    private boolean checkShelfStatue = true;
    private int state = 0;
    private boolean shouldSetMoney = true;
    private boolean canOfferNextShelfOnBlock = true;
    private OrderArgs args = new OrderArgs();
    private boolean offerByShelf = true;
    private boolean isNextOfferMoneySetted = false;
    private int maxOfferPerGoodsTime = -1;
    private int offeredCount = 0;
    private int successCount = 0;
    private int count = 0;
    private long lastSelectGoodsTime = 0;
    private ShelfInfo offeringShelf = null;
    private boolean paused = false;
    private boolean canceled = false;
    private ArrayList<HashMap<String, Integer>> shelfs = new ArrayList<>();
    private HashMap<Integer, Integer> mapShelfCount = new HashMap<>();
    private HashMap<String, Integer> mapGoodsCount = null;

    public void reset() {
        this.curOfferingIdx = 0;
    }

    public void cancelOfferJob() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public List<Integer> getOfferedShelves() {
        ArrayList arrayList = new ArrayList();
        Iterator<HashMap<String, Integer>> it = this.shelfs.iterator();
        while (it.hasNext()) {
            HashMap<String, Integer> next = it.next();
            if (!arrayList.contains(next.get(ShjDbHelper.COLUM_shelf))) {
                arrayList.add(next.get(ShjDbHelper.COLUM_shelf));
            }
        }
        return arrayList;
    }

    public int get2OfferCount() {
        return this.count;
    }

    public int getNextOfferShelf() {
        int i = this.curOfferingIdx;
        this.curOfferingIdx = i + 1;
        if (i < this.shelfs.size()) {
            return this.shelfs.get(i).get(ShjDbHelper.COLUM_shelf).intValue();
        }
        return -1;
    }

    public int getCurOfferingIdx() {
        return this.curOfferingIdx;
    }

    public int getCurOfferingShelf() {
        if (this.curOfferingIdx < this.shelfs.size()) {
            return this.shelfs.get(this.curOfferingIdx).get(ShjDbHelper.COLUM_shelf).intValue();
        }
        return -1;
    }

    public boolean isBatchOfferFinished() {
        ArrayList<HashMap<String, Integer>> arrayList = this.shelfs;
        return arrayList == null || this.offeredCount >= arrayList.size() || this.curOfferingIdx > this.shelfs.size();
    }

    public void addGoodsItem(String str) {
        this.offerByShelf = false;
        if (this.mapGoodsCount == null) {
            this.mapGoodsCount = new HashMap<>();
        }
        if (this.mapGoodsCount.containsKey(str)) {
            HashMap<String, Integer> hashMap = this.mapGoodsCount;
            hashMap.put(str, Integer.valueOf(hashMap.get(str).intValue() + 1));
        } else {
            this.mapGoodsCount.put(str, 1);
        }
        GoodsManager goodsManager = ShjManager.getGoodsManager();
        OrderArgs orderArgs = this.args;
        String str2 = "";
        if (orderArgs != null) {
            str2 = orderArgs.getNextGoodsbatchnumber("" + str);
        }
        addShelfItem(goodsManager.getNextGoodsShelf(str, str2, this.mapGoodsCount.get(str).intValue() - 1));
    }

    public void addShelfItem(int i) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(ShjDbHelper.COLUM_shelf, Integer.valueOf(i));
        hashMap.put(SpeechUtility.TAG_RESOURCE_RESULT, 0);
        getShelfs().add(hashMap);
        if (getMapShelfCount().containsKey(Integer.valueOf(i))) {
            getMapShelfCount().put(Integer.valueOf(i), Integer.valueOf(getMapShelfCount().get(Integer.valueOf(i)).intValue() + 1));
        } else {
            getMapShelfCount().put(Integer.valueOf(i), 1);
        }
        this.count++;
    }

    public OrderArgs getOrderArgs() {
        return this.args;
    }

    public void setOrderArgs(OrderArgs orderArgs) {
        this.args = orderArgs;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int i) {
        this.money = i;
    }

    public MoneyType getMoneyType() {
        return this.moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    public int getOfferedCount() {
        return this.offeredCount;
    }

    public void setOfferedCount(int i) {
        this.offeredCount = i;
    }

    public ArrayList<HashMap<String, Integer>> getShelfs() {
        return this.shelfs;
    }

    public void setShelfs(ArrayList<HashMap<String, Integer>> arrayList) {
        this.shelfs = arrayList;
    }

    public HashMap<Integer, Integer> getMapShelfCount() {
        return this.mapShelfCount;
    }

    public void setMapShelfCount(HashMap<Integer, Integer> hashMap) {
        this.mapShelfCount = hashMap;
    }

    public int getOfferFailedCount() {
        Iterator<HashMap<String, Integer>> it = getShelfs().iterator();
        int i = 0;
        while (it.hasNext()) {
            HashMap<String, Integer> next = it.next();
            int intValue = next.get(ShjDbHelper.COLUM_shelf).intValue();
            int intValue2 = next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue();
            Shj.getShelfInfo(Integer.valueOf(intValue));
            if (intValue2 != 1) {
                i++;
            }
        }
        return i;
    }

    public String getOfferResult_ycch() {
        Iterator<HashMap<String, Integer>> it = getShelfs().iterator();
        String str = "";
        while (it.hasNext()) {
            HashMap<String, Integer> next = it.next();
            int intValue = next.get(ShjDbHelper.COLUM_shelf).intValue();
            int intValue2 = next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (intValue2 == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(VoiceWakeuperAidl.PARAMS_SEPARATE);
                sb.append(this.offerByShelf ? String.format("%03d", Integer.valueOf(intValue)) : String.format("%04d", shelfInfo.getGoodsCode()));
                sb.append(":");
                sb.append(0);
                str = sb.toString();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(VoiceWakeuperAidl.PARAMS_SEPARATE);
                sb2.append(this.offerByShelf ? String.format("%03d", Integer.valueOf(intValue)) : String.format("%04d", shelfInfo.getGoodsCode()));
                sb2.append(":");
                sb2.append(ShjManager.offerDeviceErrorState2ServerState(OfferState.int2Enum(intValue2)));
                str = sb2.toString();
            }
        }
        return str.substring(1);
    }

    public String getOfferJSONResult(Order order) {
        double d;
        double d2;
        Iterator<HashMap<String, Integer>> it = getShelfs().iterator();
        String str = "";
        int i = 0;
        while (it.hasNext()) {
            HashMap<String, Integer> next = it.next();
            int intValue = next.get(ShjDbHelper.COLUM_shelf).intValue();
            int intValue2 = next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (intValue2 == 1) {
                str = str + ",{\"id\":\"" + shelfInfo.getGoodsCode() + "\",\"status\":\"T\"}";
            } else {
                str = str + ",{\"id\":\"" + shelfInfo.getGoodsCode() + "\",\"status\":\"F\"}";
                int intValue3 = shelfInfo.getPrice().intValue();
                try {
                    String arg = order.getArgs().getArg("ZK_" + shelfInfo.getGoodsCode());
                    int parseInt = Integer.parseInt(order.getArgs().getArg("ZK_" + shelfInfo.getGoodsCode() + "_COUNT"));
                    if (arg.length() > 0) {
                        JSONObject jSONObject = new JSONObject(arg);
                        switch (AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[order.getPayType().ordinal()]) {
                            case 1:
                                d = jSONObject.getDouble("wxzkjg") * 100.0d;
                                d2 = parseInt;
                                Double.isNaN(d2);
                                break;
                            case 2:
                                d = jSONObject.getDouble("alipayzkjg") * 100.0d;
                                d2 = parseInt;
                                Double.isNaN(d2);
                                break;
                            case 3:
                                d = jSONObject.getDouble("cardzkjg") * 100.0d;
                                d2 = parseInt;
                                Double.isNaN(d2);
                                break;
                            case 4:
                            case 5:
                            case 6:
                                d = jSONObject.getDouble("unionzkjg") * 100.0d;
                                d2 = parseInt;
                                Double.isNaN(d2);
                                break;
                            default:
                                intValue3 = shelfInfo.getPrice().intValue();
                                break;
                        }
                        intValue3 = (int) (d / d2);
                    }
                } catch (Exception unused) {
                }
                i += intValue3;
            }
        }
        return "{\"refund_fee\":" + i + ",\"goods\":[" + str.substring(1) + "]}";
    }

    /* renamed from: com.shj.biz.goods.BatchOfferGoodsInfo$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$biz$order$OrderPayType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.WEIXIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ZFB.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ICCard.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL6.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YLJH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public boolean isShouldSetMoney() {
        return this.shouldSetMoney;
    }

    public void setShouldSetMoney(boolean z) {
        this.shouldSetMoney = z;
    }

    public String getRemark() {
        String str = this.remark;
        return str == null ? "" : str;
    }

    public void setRemark(String str) {
        this.remark = str;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public void setSuccessCount(int i) {
        this.successCount = i;
    }

    public ShelfInfo getOfferingShelf() {
        return this.offeringShelf;
    }

    public void setOfferingShelf(ShelfInfo shelfInfo) {
        this.offeringShelf = shelfInfo;
        this.isNextOfferMoneySetted = false;
        this.lastSelectGoodsTime = 0L;
    }

    public boolean isCanOfferNextShelfOnBlock() {
        return this.canOfferNextShelfOnBlock;
    }

    public void setCanOfferNextShelfOnBlock(boolean z) {
        this.canOfferNextShelfOnBlock = z;
    }

    public boolean isNextOfferMoneySetted() {
        return this.isNextOfferMoneySetted;
    }

    public void setNextOfferMoneySetted(boolean z) {
        this.isNextOfferMoneySetted = z;
    }

    public long getLastSelectGoodsTime() {
        return this.lastSelectGoodsTime;
    }

    public void setLastSelectGoodsTime(long j) {
        this.lastSelectGoodsTime = j;
    }

    public int getMaxOfferPerGoodsTime() {
        return this.maxOfferPerGoodsTime;
    }

    public void setMaxOfferPerGoodsTime(int i) {
        this.maxOfferPerGoodsTime = i;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean z) {
        this.paused = z;
    }

    public boolean checkGoodsCount() {
        return this.checkGoodsCount;
    }

    public void setCheckGoodsCount(boolean z) {
        this.checkGoodsCount = z;
    }

    public boolean checkShelfStatue() {
        return this.checkShelfStatue;
    }

    public void setCheckShelfStatue(boolean z) {
        this.checkShelfStatue = z;
    }
}

package com.shj.biz;

import android.text.TextUtils;
import com.oysb.utils.http.RequestHelper;
import com.shj.biz.ReportManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderPayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class ThirdPartyOrderManager {
    private static List<String> currentKey = new ArrayList();
    private static int orderOutTime = 80;
    static HashMap<String, Order> args = new HashMap<>();

    public static void packNumToOrder(String str, Order order) {
        args.put(str, order);
    }

    public static void removeOrder(String str) {
        args.remove(str);
    }

    public static Order packNumFromOrder(String str) {
        if (args.containsKey(str)) {
            return args.get(str);
        }
        return null;
    }

    public static void creatOrder(OrderPayType orderPayType, int i, String str, int i2, String str2, ReportManager.SendMessageListener sendMessageListener) {
        Order order = new Order();
        order.setShelf(i);
        order.setGoodsCode(str);
        order.setPayType(orderPayType);
        order.setPrice(i2);
        int i3 = AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[order.getPayType().ordinal()];
        if (i3 == 1) {
            ReportManager.requestPwApply(str2, order, sendMessageListener);
        } else if (i3 == 2) {
            ReportManager.requestVipApply(str2, order, sendMessageListener);
        } else {
            ReportManager.requestQrcode(order, sendMessageListener);
        }
    }

    /* renamed from: com.shj.biz.ThirdPartyOrderManager$1 */
    /* loaded from: classes2.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$biz$order$OrderPayType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.PickCode.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ICCard.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static void cancelOrder(int i) {
        cancelRequest();
        for (Order order : args.values()) {
            if (order.getPayType() == OrderPayType.ZFB || order.getPayType() == OrderPayType.WEIXIN) {
                if (!TextUtils.isEmpty(order.getPayId())) {
                    ReportManager.requestQrcodeFeedBack(order, i);
                }
            }
        }
        clearOrder();
    }

    private static void cancelRequest() {
        List<String> currentKey2 = getCurrentKey();
        for (int i = 0; i < currentKey2.size(); i++) {
            if (!TextUtils.isEmpty(currentKey2.get(i))) {
                RequestHelper.cancelRequestItem(currentKey2.get(i));
            }
        }
        getCurrentKey().clear();
    }

    public static void orderEnd(Order order, int i) {
        cancelRequest();
        int i2 = AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[order.getPayType().ordinal()];
        if (i2 == 1) {
            ReportManager.requestPwFeedBack(order, i);
        } else if (i2 == 2) {
            ReportManager.requestVipFeedBack(order, i);
        } else {
            ReportManager.requestQrcodeFeedBack(order, i);
        }
        clearOrder();
    }

    private static void clearOrder() {
        args.clear();
    }

    public static List<String> getCurrentKey() {
        return currentKey;
    }

    public static void setCurrentKey(String str) {
        if (currentKey.contains(str)) {
            return;
        }
        currentKey.add(str);
    }

    public static int getOrderOutTime() {
        return orderOutTime;
    }

    public static void setOrderOutTime(int i) {
        orderOutTime = i;
    }
}

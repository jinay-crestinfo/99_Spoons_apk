package com.xyshj.machine.listener;

import android.content.Intent;
import android.os.Bundle;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.app.R;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.video.TTSManager;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.goods.GoodsStatusListener;
import com.shj.biz.order.Order;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;

/* loaded from: classes2.dex */
public class MyGoodsStatusListener implements GoodsStatusListener {
    public static final String ACTION_BATCH_OFFER_GOODS_FINISHED = "ACTION_BATCH_OFFER_GOODS_FINISHED";
    public static final String ACTION_GOODS_DESELECT = "ACTION_GOODS_DESELECT";
    public static final String ACTION_GOODS_LIST_CHANGED = "ACTION_GOODS_LIST_CHANGED";
    public static final String ACTION_GOODS_OFFER_GOODS_BLOCKED = "ACTION_GOODS_OFFER_GOODS_BLOCKED";
    public static final String ACTION_GOODS_OFFER_GOODS_FAILED = "ACTION_GOODS_OFFER_GOODS_FAILED";
    public static final String ACTION_GOODS_OFFER_GOODS_NONE = "ACTION_GOODS_OFFER_GOODS_NONE";
    public static final String ACTION_GOODS_OFFER_GOODS_START = "ACTION_GOODS_OFFER_GOODS_START";
    public static final String ACTION_GOODS_OFFER_GOODS_STATUS = "ACTION_GOODS_OFFER_GOODS_STATUS";
    public static final String ACTION_GOODS_OFFER_GOODS_SUCCESS = "ACTION_GOODS_OFFER_GOODS_SUCCESS";
    public static final String ACTION_GOODS_RELOADED = "ACTION_GOODS_RELOADED";
    public static final String ACTION_GOODS_SELECT = "ACTION_GOODS_SELECT";
    public static final String ACTION_GOODS_SYNCHRONISM_FINISHED = "ACTION_GOODS_SYNCHRONISM_FINISHED";
    public static final String ACTION_GOODS_UPDATE = "ACTION_GOODS_UPDATE";
    public static final String ACTION_GOODS_UPDATE_COUNT = "ACTION_GOODS_UPDATE_COUNT";
    public static final String ACTION_GOODS_UPDATE_PRICE = "ACTION_GOODS_UPDATE_PRICE";
    public static final String ACTION_GOODS_UPDATE_SHELFCOUNT = "ACTION_GOODS_UPDATE_SHELFCOUNT";
    public static final String ACTION_GOODS_UPDATE_SHELFGOODS = "ACTION_GOODS_UPDATE_SHELFGOODS";
    public static final String ACTION_GOODS_UPDATE_SHELSTATUS = "ACTION_GOODS_UPDATE_SHELSTATUS";

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onGoodsReloaded() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_GOODS_RELOADED));
        Loger.writeLog("Broadcast", ACTION_GOODS_RELOADED);
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onSelectGoods(String str) {
        ShjAppBase.sysModel.setSelectedGoods(ShjManager.getGoodsManager().getGoodsByCode(str));
        Intent intent = new Intent(ACTION_GOODS_SELECT);
        Bundle bundle = new Bundle();
        bundle.putString("goodsCode", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_SELECT " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onDeselectGoods(String str) {
        Intent intent = new Intent(ACTION_GOODS_DESELECT);
        Bundle bundle = new Bundle();
        bundle.putString("goodsCode", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_DESELECT " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateGoodsCount(String str, int i) {
        Intent intent = new Intent(ACTION_GOODS_UPDATE_COUNT);
        Bundle bundle = new Bundle();
        bundle.putString("goodsCode", str);
        bundle.putInt("count", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_UPDATE_COUNT " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateShelfGoodsCount(Integer num, int i) {
        Intent intent = new Intent(ACTION_GOODS_UPDATE_SHELFCOUNT);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, num.intValue());
        bundle.putInt("count", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_UPDATE_SHELFCOUNT " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateShelfGoodsStatus(int i, int i2) {
        Intent intent = new Intent(ACTION_GOODS_UPDATE_SHELSTATUS);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        bundle.putInt("state", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_UPDATE_SHELSTATUS " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateGoodsPrice(String str, Integer num) {
        Intent intent = new Intent(ACTION_GOODS_UPDATE_PRICE);
        Bundle bundle = new Bundle();
        bundle.putString("goodsCode", str);
        bundle.putInt(ShjDbHelper.COLUM_price, num.intValue());
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_UPDATE_PRICE " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateShelfGoods(Integer num, String str) {
        Intent intent = new Intent(ACTION_GOODS_UPDATE_SHELFGOODS);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, num.intValue());
        bundle.putString("goodsCode", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_UPDATE_SHELFGOODS " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods(int i, String str, int i2) {
        Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_START);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        bundle.putString("goodsCode", str);
        bundle.putInt("picker", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_OFFER_GOODS_START " + bundle.toString());
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_dispensing));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods_Blocked(int i, String str, int i2) {
        try {
            ShjManager.getGoodsManager().getGoodsByCode(str);
            ShjManager.getOrderManager().getTradSn();
            Loger.writeLog("UI", "orderArgs:" + ShjManager.getOrderManager().getResentOrder(2, null).getArgs());
            Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_BLOCKED);
            Bundle bundle = new Bundle();
            bundle.putInt(ShjDbHelper.COLUM_shelf, i);
            bundle.putString("goodsCode", str);
            bundle.putInt("picker", i2);
            intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
            ShjAppBase.sysApp.sendBroadcast(intent);
            Loger.writeLog("Broadcast", "ACTION_GOODS_OFFER_GOODS_BLOCKED " + bundle.toString());
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_offerblocked));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods_None(int i, String str, int i2) {
        ShjManager.getGoodsManager().getGoodsByCode(str);
        ShjManager.getOrderManager().getTradSn();
        Loger.writeLog("UI", "orderArgs:" + ShjManager.getOrderManager().getResentOrder(2, null).getArgs());
        Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_NONE);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        bundle.putString("goodsCode", str);
        bundle.putInt("picker", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_OFFER_GOODS_NONE " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods_Failed(int i, String str, int i2) {
        Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_FAILED);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        bundle.putString("goodsCode", str);
        bundle.putInt("picker", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_GOODS_OFFER_GOODS_FAILED " + bundle.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods_Success(int i, String str, int i2) {
        try {
            ShjManager.getGoodsManager().getGoodsByCode(str);
            ShjManager.getOrderManager().getTradSn();
            Loger.writeLog("UI", "orderArgs:" + ShjManager.getOrderManager().getResentOrder(2, null).getArgs());
            Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_SUCCESS);
            Bundle bundle = new Bundle();
            bundle.putInt(ShjDbHelper.COLUM_shelf, i);
            bundle.putString("goodsCode", str);
            bundle.putInt("picker", i2);
            intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
            ShjAppBase.sysApp.sendBroadcast(intent);
            Loger.writeLog("Broadcast", "ACTION_GOODS_OFFER_GOODS_SUCCESS " + bundle.toString());
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_offersuccess));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onGoodsListChanged() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_GOODS_LIST_CHANGED));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onGoodsUpdated() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_GOODS_UPDATE));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onUpdateShelfDoorState(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("货道:");
        sb.append(i);
        sb.append(" 门状态:");
        sb.append(i2 == 1 ? "开" : "关");
        Loger.writeLog("NET", sb.toString());
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onGoodsSynchronismFinished() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_GOODS_SYNCHRONISM_FINISHED));
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onBatchOfferGoods_Finished(BatchOfferGoodsInfo batchOfferGoodsInfo, String str, int i) {
        try {
            Order resentOrder = ShjManager.getOrderManager().getResentOrder(2, null);
            Loger.writeLog("SHJ", "order price:" + resentOrder.getSumPrice() + " orderID:" + resentOrder.getUid());
            int intValue = Shj.getWallet().getCatchMoney().intValue() - resentOrder.getSumPrice();
            if (intValue < 0) {
                intValue = 0;
            }
            Shj.onResetCurrentMoney(intValue, false);
        } catch (Exception unused) {
        }
        Intent intent = new Intent(ACTION_BATCH_OFFER_GOODS_FINISHED);
        Bundle bundle = new Bundle();
        bundle.putString("content", str);
        bundle.putInt("picker", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("SALES", "batchOfferGoodsFinished:" + str);
    }

    @Override // com.shj.biz.goods.GoodsStatusListener
    public void onOfferingGoods_State(int i, int i2, String str, int i3) {
        Intent intent = new Intent(ACTION_GOODS_OFFER_GOODS_STATUS);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        bundle.putInt("state", i2);
        if (isEnglish(str)) {
            bundle.putString("message", str);
        } else {
            bundle.putString("message", getOfferGoodsDiviceStateInfo(str));
        }
        bundle.putInt("picker", i3);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("SALES", "onOfferingGoods shelf:" + i + " state:" + i2 + " msg:" + str);
    }

    public static boolean isEnglish(String str) {
        return str.matches("^[a-zA-Z]*");
    }

    public static String getOfferGoodsDiviceStateInfo(String str) {
        String str2 = str;
        String str3 = "电机不存在";
        try {
            String language = CommonTool.getLanguage(ShjAppBase.sysApp);
            if (str2.equals("货道正常")) {
                str2 = language.equalsIgnoreCase("en") ? "Selection normal" : ShjAppHelper.getString(R.string.cargo_way_normal);
            } else if (str2.equals("正在出货")) {
                str2 = language.equalsIgnoreCase("en") ? "Is the shipment" : "正在出货";
            } else if (str2.equals("出货成功")) {
                str2 = language.equalsIgnoreCase("en") ? "Successful delivery" : "出货成功";
            } else if (str2.equals("卡货")) {
                str2 = language.equalsIgnoreCase("en") ? "Card goods" : "卡货";
            } else if (str2.equals("电机未正常停止")) {
                str2 = language.equalsIgnoreCase("en") ? "Motor does not stop normally" : "电机未正常停止";
            } else if (str2.equals("货道无效")) {
                str2 = language.equalsIgnoreCase("en") ? "Aisle is invalid" : "货道无效";
            } else if (str2.equals("电机不存在")) {
                if (language.equalsIgnoreCase("en")) {
                    str2 = "Motor does not exist";
                }
                str2 = str3;
            } else if (str2.equals("升降机故障")) {
                str2 = language.equalsIgnoreCase("en") ? "Lift fault" : "升降机故障";
            } else if (str2.equals("升降机正在上升")) {
                str2 = language.equalsIgnoreCase("en") ? "The lift is going up" : "升降机正在上升";
            } else if (str2.equals("升降机正在下降")) {
                str2 = language.equalsIgnoreCase("en") ? "Dispensing" : "升降机正在下降";
            } else if (str2.equals("升降机上升错误")) {
                str2 = language.equalsIgnoreCase("en") ? "Lift lift error" : "升降机上升错误";
            } else if (str2.equals("升降机下降错误")) {
                str2 = language.equalsIgnoreCase("en") ? "Lift descending error" : "升降机下降错误";
            } else if (str2.equals("正在关闭微波炉前门")) {
                str2 = language.equalsIgnoreCase("en") ? "Closing the microwave front door" : "正在关闭微波炉前门";
            } else {
                str3 = "微波炉前门关闭错误";
                if (str2.equals(str3)) {
                    if (language.equalsIgnoreCase("en")) {
                        str2 = "Microwave front door closed wrong";
                    }
                    str2 = str3;
                } else {
                    str3 = "正在打开微波炉后门";
                    if (str2.equals(str3)) {
                        if (language.equalsIgnoreCase("en")) {
                            str2 = "Opening the back door of the microwave";
                        }
                        str2 = str3;
                    } else {
                        str3 = "微波炉后门打开错误";
                        if (str2.equals(str3)) {
                            if (language.equalsIgnoreCase("en")) {
                                str2 = "Microwave backdoor opened wrong";
                            }
                            str2 = str3;
                        } else {
                            str3 = "正在将盒饭推入微波炉";
                            if (str2.equals(str3)) {
                                if (language.equalsIgnoreCase("en")) {
                                    str2 = "Pushing lunch boxes into the microwave";
                                }
                                str2 = str3;
                            } else {
                                str3 = "正在关闭微波炉后门";
                                if (str2.equals(str3)) {
                                    if (language.equalsIgnoreCase("en")) {
                                        str2 = "Closing the back door of the microwave";
                                    }
                                    str2 = str3;
                                } else {
                                    str3 = "微波炉后门关闭错误";
                                    if (str2.equals(str3)) {
                                        if (language.equalsIgnoreCase("en")) {
                                            str2 = "Microwave door closing error";
                                        }
                                        str2 = str3;
                                    } else {
                                        str3 = "微波炉内没检测到盒饭";
                                        if (str2.equals(str3)) {
                                            if (language.equalsIgnoreCase("en")) {
                                                str2 = "No box lunch detected in microwave";
                                            }
                                            str2 = str3;
                                        } else {
                                            str3 = "盒饭正在加热";
                                            if (str2.equals(str3)) {
                                                if (language.equalsIgnoreCase("en")) {
                                                    str2 = "The box lunch is heating up";
                                                }
                                                str2 = str3;
                                            } else {
                                                str3 = "盒饭加热剩余时间";
                                                if (str2.equals(str3)) {
                                                    if (language.equalsIgnoreCase("en")) {
                                                        str2 = "Box lunch heating remaining time";
                                                    }
                                                    str2 = str3;
                                                } else if (str2.equals("请取出商品")) {
                                                    str2 = language.equalsIgnoreCase("en") ? " Pls take out the goods" : "请取出商品";
                                                } else if (str2.equals("撑杆回位错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Pole return error" : "撑杆回位错误";
                                                } else if (str2.equals("正在打开微波炉前门")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Opening the microwave front door" : "正在打开微波炉前门";
                                                } else if (str2.equals("打开微波炉前门出错")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Error opening microwave front door" : "打开微波炉前门出错";
                                                } else if (str2.equals("升降台撑杆推出错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Lifting platform strut out error" : "升降台撑杆推出错误";
                                                } else if (str2.equals("升降台进微波炉错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Lift table into microwave oven error" : "升降台进微波炉错误";
                                                } else if (str2.equals("升降台出微波炉错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Lift table out of microwave oven error" : "升降台出微波炉错误";
                                                } else if (str2.equals("微波炉内推杆推出错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Push rod push error in microwave oven" : "微波炉内推杆推出错误";
                                                } else if (str2.equals("微波炉内推杆收回错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Microwave push rod retraction error" : "微波炉内推杆收回错误";
                                                } else if (str2.equals("皮带自检错误")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Belt self-checking error" : "皮带自检错误";
                                                } else if (str2.equals("暂停出货")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "suspension of shipment" : "暂停出货";
                                                } else if (str2.equals("出货终止")) {
                                                    str2 = language.equalsIgnoreCase("en") ? "Dispensing failed" : "出货终止";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

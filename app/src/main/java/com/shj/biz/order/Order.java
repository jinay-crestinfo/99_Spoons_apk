package com.shj.biz.order;

import com.oysb.utils.Loger;
import com.shj.Shj;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class Order implements Serializable {
    private static final long serialVersionUID = 1;
    private static int xh;
    private Date creatTime;
    private String facePayId;
    private Date fetchTime;
    private String goodsCode;
    private int goodsCount;
    private String goodsName;
    private long lastOfferTime;
    private OrderArgs orderArgs;
    private int orderId;
    private String payId;
    private Date payTime;
    private OrderPayType payType;
    private List<OrderPayType> payTypes;
    private int price;
    private int shelf;
    private Date submitTime;
    private int sumPrice;
    private int tradId;
    private String uid;
    private String goodsbatchnumber = "";
    private int payPrice = -1;
    private int status = 0;

    public Order() {
        this.uid = "";
        this.uid = nextUid();
        Loger.writeLog("SHJ", "create Order:" + this.uid);
        setCreatTime(new Date());
        setStatus(0);
    }

    public long getLastOfferTime() {
        return this.lastOfferTime;
    }

    public void setLastOfferTime(long j) {
        this.lastOfferTime = j;
    }

    public static String nextUid() {
        int i = xh + 1;
        xh = i;
        if (i > 999999) {
            xh = 1;
        }
        return Shj.getMachineId() + String.format("%s00%06d", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), Integer.valueOf(xh));
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int i) {
        this.orderId = i;
    }

    public String getGoodsCode() {
        if (!this.goodsCode.equals("-1") && !this.goodsCode.equals("0") && this.goodsCode.length() < 4 && Shj.isStoreGoodsInfoInVMC()) {
            String str = "0000" + this.goodsCode;
            this.goodsCode = str;
            this.goodsCode = str.substring(str.length() - 4);
        }
        return this.goodsCode;
    }

    public void setGoodsCode(String str) {
        if (!str.equals("-1") && !str.equals("0") && str.length() < 4 && Shj.isStoreGoodsInfoInVMC()) {
            String str2 = "0000" + str;
            str = str2.substring(str2.length() - 4);
        }
        this.goodsCode = str;
    }

    public int getGoodsCount() {
        return this.goodsCount;
    }

    public void setGoodsCount(int i) {
        this.goodsCount = i;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int i) {
        this.price = i;
    }

    public int getSumPrice() {
        return this.sumPrice;
    }

    public void setSumPrice(int i) {
        this.sumPrice = i;
    }

    public Date getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(Date date) {
        this.creatTime = date;
    }

    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date date) {
        this.payTime = date;
    }

    public OrderPayType getPayType() {
        return this.payType;
    }

    public void setPayType(OrderPayType orderPayType) {
        this.payType = orderPayType;
    }

    public Date getFetchTime() {
        return this.fetchTime;
    }

    public void setFetchTime(Date date) {
        this.fetchTime = date;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public Date getSubmitTime() {
        return this.submitTime;
    }

    public void setSubmitTime(Date date) {
        this.submitTime = date;
    }

    public int getShelf() {
        return this.shelf;
    }

    public void setShelf(int i) {
        this.shelf = i;
    }

    public String getPayId() {
        return this.payId;
    }

    public void setPayId(String str) {
        Loger.writeLog("SALES", "setPayId payId=" + str);
        this.payId = str;
    }

    public int getTradId() {
        return this.tradId;
    }

    public void setTradId(int i) {
        this.tradId = i;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String str) {
        this.goodsName = str;
    }

    public OrderArgs getArgs() {
        return this.orderArgs;
    }

    public void setArgs(OrderArgs orderArgs) {
        this.orderArgs = orderArgs;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public List<OrderPayType> getPayTypes() {
        return this.payTypes;
    }

    public void setPayTypes(List<OrderPayType> list) {
        this.payTypes = list;
    }

    public int getPayPrice() {
        if (this.payPrice == -1) {
            this.payPrice = this.price;
        }
        return this.payPrice;
    }

    public void setPayPrice(int i) {
        this.payPrice = i;
    }

    public String getFacePayId() {
        return this.facePayId;
    }

    public void setFacePayId(String str) {
        this.facePayId = str;
    }

    public String getGoodsbatchnumber() {
        return this.goodsbatchnumber;
    }

    public void setGoodsbatchnumber(String str) {
        this.goodsbatchnumber = str;
    }
}

package com.xyshj.machine.model;

import android.graphics.Bitmap;
import android.widget.Toast;
import com.oysb.app.R;
import com.oysb.utils.Loger;
import com.oysb.utils.XyHashMap;
import com.shj.biz.goods.Goods;
import com.xyshj.app.ShjAppHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SysModel implements Serializable {
    public static final String UI_TYPE_BQL_EN = "UI_TYPE_BQL_EN";
    public static final String UI_TYPE_NORMA = "UI_TYPE_NORMA";
    private static final long serialVersionUID = -4308023953457991015L;
    private JSONObject avFileInfo;
    private int bonusPool;
    private int fzje;
    private JSONArray lotteryProb;
    private JSONObject phoneList;
    private PhoneListener phoneListener;
    private JSONArray qrCodes;
    private int rzje;
    private Goods selectedGoods;
    private boolean virtualMode = false;
    private boolean needRestart = false;
    private int goodsCountPerLayer = 7;
    private boolean isBackFromSmFreeView = false;
    private HashMap<Object, Bitmap> mapImages = new HashMap<>();
    private int winSpbh = 0;
    private int shopCartMaxGoodsCount = 3;
    private boolean showMdm = false;
    private boolean hasMemberFragment = false;
    private boolean isShopCar = false;
    private boolean isDetailAddShopCar = false;
    private String uiType = "";
    private List<ShopCarData> shopCarDataList = new ArrayList();
    private XyHashMap<Goods, Integer> map = new XyHashMap<>();
    private boolean appFullStarted = false;
    private String hlData = "";

    /* loaded from: classes2.dex */
    public interface PhoneListener {
        void getData(JSONObject jSONObject);
    }

    public List<ShopCarData> getShopCarDataList() {
        return this.shopCarDataList;
    }

    public String getUiType() {
        return this.uiType;
    }

    public void setUiType(String str) {
        this.uiType = str;
    }

    public int getShopCarAllCount() {
        Iterator<ShopCarData> it = this.shopCarDataList.iterator();
        int i = 0;
        while (it.hasNext()) {
            i += it.next().count;
        }
        return i;
    }

    public int getShopCarAllPrice() {
        int i = 0;
        for (ShopCarData shopCarData : this.shopCarDataList) {
            i += shopCarData.goods.getPrice() * shopCarData.count;
        }
        return i;
    }

    public boolean deleteGoods2ShopCar(int i) {
        if (i >= this.shopCarDataList.size()) {
            return false;
        }
        ShopCarData shopCarData = this.shopCarDataList.get(i);
        if (shopCarData.count <= 0) {
            return false;
        }
        shopCarData.count--;
        return true;
    }

    public boolean addGoods2ShopCar(int i) {
        if (i < this.shopCarDataList.size()) {
            ShopCarData shopCarData = this.shopCarDataList.get(i);
            if (shopCarData.count < shopCarData.goods.getCount()) {
                shopCarData.count++;
                return true;
            }
            Toast makeText = Toast.makeText(ShjAppHelper.getMainActivity(), shopCarData.goods.getName() + ShjAppHelper.getMainActivity().getString(R.string.not_more_goods), 0);
            makeText.setGravity(17, 0, 0);
            makeText.show();
        }
        return false;
    }

    public boolean addGoods2ShopCar(Goods goods) {
        for (ShopCarData shopCarData : this.shopCarDataList) {
            if (shopCarData.goods.getCode() == goods.getCode()) {
                if (shopCarData.count < goods.getCount()) {
                    shopCarData.count++;
                    return true;
                }
                Toast makeText = Toast.makeText(ShjAppHelper.getMainActivity(), goods.getName() + ShjAppHelper.getMainActivity().getString(R.string.not_more_goods), 0);
                makeText.setGravity(17, 0, 0);
                makeText.show();
                return false;
            }
        }
        ShopCarData shopCarData2 = new ShopCarData();
        shopCarData2.goods = goods;
        shopCarData2.count = 1;
        this.shopCarDataList.add(shopCarData2);
        return true;
    }

    public void clearShopCarDataList() {
        this.shopCarDataList.clear();
    }

    public XyHashMap<Goods, Integer> getMap() {
        return this.map;
    }

    public void setMap(XyHashMap<Goods, Integer> xyHashMap) {
        this.map = xyHashMap;
    }

    public boolean isShopCar() {
        return this.isShopCar;
    }

    public void setShopCar(boolean z) {
        this.isShopCar = z;
    }

    public boolean isDetailAddShopCar() {
        return this.isDetailAddShopCar;
    }

    public void setDetailAddShopCar(boolean z) {
        this.isDetailAddShopCar = z;
    }

    public void setShowMdm(boolean z) {
        this.showMdm = z;
    }

    public boolean canShowMdm() {
        return this.showMdm;
    }

    public void showMdm(boolean z) {
        this.showMdm = z;
    }

    public int getRzje() {
        return this.rzje;
    }

    public void setRzje(int i) {
        this.rzje = i;
    }

    public int getFzje() {
        return this.fzje;
    }

    public void setFzje(int i) {
        this.fzje = i;
    }

    public int getBonusPool() {
        return this.bonusPool;
    }

    public void setBonusPool(int i) {
        this.bonusPool = i;
    }

    public int getWinSpbh() {
        return this.winSpbh;
    }

    public void setWinSpbh(int i) {
        this.winSpbh = i;
    }

    public int getGoodsCountPerLayer() {
        return this.goodsCountPerLayer;
    }

    public void setGoodsCountPerLayer(int i) {
        this.goodsCountPerLayer = i;
    }

    public Goods getSelectedGoods() {
        return this.selectedGoods;
    }

    public void setSelectedGoods(Goods goods) {
        this.selectedGoods = goods;
    }

    public Bitmap getBitmap(Object obj) {
        if (this.mapImages.containsKey(obj)) {
            return this.mapImages.get(obj);
        }
        return null;
    }

    public void removeBitmap(Object obj) {
        if (this.mapImages.containsKey(obj)) {
            this.mapImages.remove(obj);
        }
    }

    public void pubBitmap(Object obj, Bitmap bitmap) {
        try {
            Bitmap bitmap2 = getBitmap(obj);
            if (bitmap2 != null && !bitmap2.isRecycled()) {
                Loger.writeLog("BITMAP", "recycle:" + bitmap2.toString());
                bitmap2.recycle();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        this.mapImages.put(obj, bitmap);
    }

    public void setVirtualMode(boolean z) {
        this.virtualMode = z;
    }

    public boolean isNeedRestart() {
        return this.needRestart;
    }

    public void setNeedRestart(boolean z) {
        this.needRestart = z;
    }

    public boolean isAppFullStarted() {
        return this.appFullStarted;
    }

    public void setAppFullStarted(boolean z) {
        this.appFullStarted = z;
    }

    public JSONArray getQrCodes() {
        return this.qrCodes;
    }

    public void setQrCodes(JSONArray jSONArray) {
        this.qrCodes = jSONArray;
    }

    public JSONObject getAvFileInfo() {
        return this.avFileInfo;
    }

    public void setAvFileInfo(JSONObject jSONObject) {
        this.avFileInfo = jSONObject;
    }

    public int getShopCartMaxGoodsCount() {
        return this.shopCartMaxGoodsCount;
    }

    public void setShopCartMaxGoodsCount(int i) {
        this.shopCartMaxGoodsCount = i;
    }

    public boolean hasMemberFragment() {
        return this.hasMemberFragment;
    }

    public void setHasMemberFragment(boolean z) {
        this.hasMemberFragment = z;
    }

    public JSONObject getPhoneList() {
        return this.phoneList;
    }

    public void setPhoneList(JSONObject jSONObject) {
        this.phoneList = jSONObject;
        PhoneListener phoneListener = this.phoneListener;
        if (phoneListener != null) {
            phoneListener.getData(jSONObject);
        }
    }

    public void setPhoneListener(PhoneListener phoneListener) {
        this.phoneListener = phoneListener;
    }
}

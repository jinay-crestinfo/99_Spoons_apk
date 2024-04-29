package com.shj.biz.goods;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.github.mjdev.libaums.fs.UsbFile;
//import com.iflytek.cloud.SpeechEvent;
//import com.iflytek.speech.VoiceWakeuperAidl;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.BitmapCache;
import com.oysb.utils.dialog.StepProgressDialog;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.image.DownloadImagesListener;
import com.oysb.utils.image.ImageUtils;
import com.oysb.utils.image.NetImageItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.OnUpdateGoodsInfoListener;
import com.shj.biz.ShjManager;
import com.shj.setting.NetAddress.NetAddress;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import com.xyshj.database.setting.SettingType;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class GoodsManager {
    private static final int MSG_GOODS_UPDATED = 2000;
    String imageFolder = SDFileUtils.SDCardRoot + "/xyshj/images";
    Bitmap defaultGoodsImage = null;
    Bitmap defaultGoodsMarkImage = null;
    private HashMap<String, Goods> goodsMap = new HashMap<>();
    private HashMap<String, List<Goods>> keyGoodsMap = new HashMap<>();
    private HashMap<String, List<Goods>> classificationGoodsMap = new HashMap<>();
    private List<GoodsClassificationInfo> goodsClassificationInfoList = new ArrayList();
    private List<Goods> lateastUpdatedGoods = new ArrayList();
    private boolean hasGoodsCodeChanged = false;
    private int hcode_jsonGoodInfos = 0;
    private Handler handler = new Handler() { // from class: com.shj.biz.goods.GoodsManager.1


        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            super.dispatchMessage(message);
            try {
                if (message.what == 2000 && Shj.isResetFinished()) {
                    ShjManager.getGoodsStatusListener().onGoodsUpdated();
                }
            } catch (Exception unused) {
            }
        }
    };
    List<ShelfInfo> tempShelves = null;
    String xspbh = "";

    private void printGoodsMap(String str) {
    }

    public void _onResetShjGoods(List<String> list) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.goods.GoodsManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            super.dispatchMessage(message);
            try {
                if (message.what == 2000 && Shj.isResetFinished()) {
                    ShjManager.getGoodsStatusListener().onGoodsUpdated();
                }
            } catch (Exception unused) {
            }
        }
    }

    public void _onReset() {
        this.keyGoodsMap.clear();
    }

    public void reLoadGoods() {
        Goods goods;
        Loger.writeLog("SALES", "正在加载配货单***");
        new ArrayList();
        String str = SDFileUtils.SDCardRoot + "xyShj/goodsNet.json";
        if (new File(str).exists()) {
            try {
                String readFile = SDFileUtils.readFile(str);
                if (this.hcode_jsonGoodInfos == readFile.hashCode()) {
                    return;
                }
                this.hcode_jsonGoodInfos = readFile.hashCode();
                JSONObject jSONObject = new JSONObject(readFile);
                JSONArray jSONArray = jSONObject.getJSONArray("goods");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject2 = (JSONObject) jSONArray.get(i);
                    if (this.goodsMap.containsKey(jSONObject2.getString("code"))) {
                        goods = this.goodsMap.get(jSONObject2.getString("code"));
                    } else {
                        goods = new Goods();
                    }
                    goods.setCode("" + jSONObject2.getString("code"));
                    goods.setName(jSONObject2.getString("name"));
                    if ("1".equalsIgnoreCase(jSONObject2.optString("sfyzbs"))) {
                        goods.setNeedIdentityCheck(true);
                    }
                    if (jSONObject2.has("descript")) {
                        goods.setDescript(jSONObject2.getString("descript"));
                    }
                    if (jSONObject2.has("hyspsj")) {
                        goods.setDiscountPrice(jSONObject2.getInt("hyspsj"));
                    }
                    if (jSONObject2.has("keys")) {
                        goods.setKeys(jSONObject2.getString("keys").split(","));
                    } else {
                        goods.setKeys(new String[]{""});
                    }
                    if (jSONObject2.has("bqid")) {
                        goods.setClassification(jSONObject2.getString("bqid").split(""));
                    }
                    this.goodsMap.put(goods.getCode(), goods);
                    try {
                        this.goodsMap.put(Integer.parseInt(goods.getCode()) + "", goods);
                    } catch (Exception unused) {
                    }
                    updateKeyGoodsMap(goods.getCode());
                }
                this.goodsClassificationInfoList.clear();
                JSONArray optJSONArray = jSONObject.optJSONArray("classification");
                if (optJSONArray != null) {
                    for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                        JSONObject jSONObject3 = (JSONObject) optJSONArray.get(i2);
                        GoodsClassificationInfo goodsClassificationInfo = new GoodsClassificationInfo();
                        goodsClassificationInfo.id = jSONObject3.optString("bqid");
                        goodsClassificationInfo.name = jSONObject3.optString("bqmc");
                        goodsClassificationInfo.image = jSONObject3.optString("bqimg");
                        goodsClassificationInfo.priority = jSONObject3.optInt("bqyxj");
                        goodsClassificationInfo.iconName = goodsClassificationInfo.id + goodsClassificationInfo.image;
                        this.goodsClassificationInfoList.add(goodsClassificationInfo);
                    }
                }
                Collections.sort(this.goodsClassificationInfoList, new Comparator<GoodsClassificationInfo>() { // from class: com.shj.biz.goods.GoodsManager.2


                    @Override // java.util.Comparator
                    public int compare(GoodsClassificationInfo goodsClassificationInfo2, GoodsClassificationInfo goodsClassificationInfo3) {
                        return Integer.valueOf(goodsClassificationInfo2.priority).compareTo(Integer.valueOf(goodsClassificationInfo3.priority));
                    }
                });
            } catch (Exception unused2) {
            }
        }
        if (Shj.isResetFinished()) {
            ShjManager.getGoodsStatusListener().onGoodsReloaded();
        }
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Comparator<GoodsClassificationInfo> {
        AnonymousClass2() {
        }

        @Override // java.util.Comparator
        public int compare(GoodsClassificationInfo goodsClassificationInfo2, GoodsClassificationInfo goodsClassificationInfo3) {
            return Integer.valueOf(goodsClassificationInfo2.priority).compareTo(Integer.valueOf(goodsClassificationInfo3.priority));
        }
    }

    public List<GoodsClassificationInfo> getGoodsClassificationInfoList() {
        return this.goodsClassificationInfoList;
    }

    private void updateGoods(String str, String str2) {
        if (str.equals("-1")) {
            return;
        }
        printGoodsMap("updateGoods " + str);
        if (this.goodsMap.containsKey(str)) {
            return;
        }
        Goods goods = new Goods();
        goods.setCode(str);
        goods.setCount(0);
        goods.setDescript("");
        goods.setGroupName(str2);
        goods.setName(Shj.isStoreGoodsInfoInVMC() ? str : "");
        goods.setPrice(0);
        goods.setKeys(new String[]{""});
        this.goodsMap.put(str, goods);
        updateKeyGoodsMap(str);
        printGoodsMap("updateGoods  updateKeyGoodsMap " + str);
    }

    private void updateKeyGoodsMap(String str) {
        try {
            Goods goods = this.goodsMap.get(str);
            if (Shj.isResetFinished() || !goods.isKeyGoodsMapUpdated()) {
                if (Shj.getShelfInfosByGoodsCode(str).size() > 0) {
                    for (String str2 : this.keyGoodsMap.keySet()) {
                        if (goods.hasKey(str2)) {
                            if (!this.keyGoodsMap.get(str2).contains(goods)) {
                                this.keyGoodsMap.get(str2).add(goods);
                            }
                        } else if (this.keyGoodsMap.get(str2).contains(goods)) {
                            this.keyGoodsMap.get(str2).remove(goods);
                        }
                    }
                    for (String str3 : goods.getKeys()) {
                        if (!this.keyGoodsMap.containsKey(str3)) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(goods);
                            this.keyGoodsMap.put(str3, arrayList);
                        }
                    }
                    goods.setKeyGoodsMapUpdated(true);
                    return;
                }
                Iterator<String> it = this.keyGoodsMap.keySet().iterator();
                while (it.hasNext()) {
                    this.keyGoodsMap.get(it.next()).remove(goods);
                }
            }
        } catch (Exception unused) {
        }
    }

    private void updateKeyGoodsMap(int i, String str, String str2, String str3) {
        Goods goods;
        if (this.goodsMap.containsKey(str)) {
            printGoodsMap("updateKeyGoodsMap start");
            Goods goods2 = this.goodsMap.get(str);
            goods2.setGroupName(str3);
            if (!str2.equalsIgnoreCase(str) && !str2.equals("-1")) {
                updateKeyGoodsMap(str2);
                Goods goods3 = this.goodsMap.get(str2);
                if (goods3 != null) {
                    for (String str4 : goods3.getClassification()) {
                        if (this.classificationGoodsMap.containsKey(str4) && this.classificationGoodsMap.get(str4).contains(goods3)) {
                            this.classificationGoodsMap.get(str4).remove(goods3);
                        }
                    }
                }
            }
            updateKeyGoodsMap(goods2.getCode());
            for (String str5 : goods2.getClassification()) {
                if (this.classificationGoodsMap.containsKey(str5)) {
                    if (!this.classificationGoodsMap.get(str5).contains(goods2)) {
                        this.classificationGoodsMap.get(str5).add(goods2);
                    }
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(goods2);
                    this.classificationGoodsMap.put(str5, arrayList);
                }
            }
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            goods2.setCount(Shj.getGoodsCount(goods2.getCode()));
            if (shelfInfo.getPrice() != null) {
                goods2.setPrice(shelfInfo.getPrice().intValue());
            }
            if (!str2.equals("-1") && Shj.getShelfInfosByGoodsCode(str2).size() > 0 && (goods = this.goodsMap.get(str2)) != null) {
                goods.setCount(Shj.getGoodsCount(goods.getCode()));
            }
            printGoodsMap("updateKeyGoodsMap end");
        }
    }

    public List<String> getGoodsKeys() {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = this.keyGoodsMap.keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    public List<Goods> getGoodsByKey(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        try {
            for (Goods goods : this.keyGoodsMap.get(str)) {
                if (str2.contains(goods.getGroupName() + VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    arrayList.add(goods);
                }
            }
        } catch (Exception unused) {
        }
        printGoodsMap("getGoodsByKey     key:" + str);
        return arrayList;
    }

    public List<Goods> getGoodsByClassification(List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            Iterator<String> it = this.classificationGoodsMap.keySet().iterator();
            while (it.hasNext()) {
                arrayList.addAll(this.classificationGoodsMap.get(it.next()));
            }
        } else {
            Iterator<String> it2 = list.iterator();
            while (it2.hasNext()) {
                arrayList.addAll(this.classificationGoodsMap.get(it2.next()));
            }
        }
        HashSet hashSet = new HashSet(arrayList);
        arrayList.clear();
        arrayList.addAll(hashSet);
        return arrayList;
    }

    public List<Goods> getGoodsByClassification(String str) {
        return this.classificationGoodsMap.get(str);
    }

    public List<Goods> getAllGoods() {
        ArrayList arrayList = new ArrayList();
        try {
            for (Goods goods : this.goodsMap.values()) {
                if (goods.getPrice() != -1) {
                    arrayList.add(goods);
                }
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public boolean hasGoodsCodeChanged() {
        List<Goods> list;
        return this.hasGoodsCodeChanged || (list = this.lateastUpdatedGoods) == null || list.size() == 0;
    }

    public void addLateastGoods(Goods goods) {
        if (this.lateastUpdatedGoods == null) {
            this.lateastUpdatedGoods = new ArrayList();
        }
        if (!this.lateastUpdatedGoods.contains(goods)) {
            this.lateastUpdatedGoods.add(goods);
        }
        this.handler.removeMessages(2000);
        if (Shj.isBatchJobRunning()) {
            return;
        }
        this.handler.sendEmptyMessageDelayed(2000, 1000L);
    }

    public List<Goods> getLateastUpdatedGoods(boolean z) {
        if (!z) {
            return this.lateastUpdatedGoods;
        }
        this.hasGoodsCodeChanged = false;
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.lateastUpdatedGoods);
        this.lateastUpdatedGoods.clear();
        return arrayList;
    }

    public List<Goods> getLateastUpdatedGoodsInSubset(boolean z, List<Goods> list) {
        ArrayList arrayList = new ArrayList(list);
        arrayList.retainAll(this.lateastUpdatedGoods);
        if (z) {
            this.lateastUpdatedGoods.removeAll(list);
            if (this.lateastUpdatedGoods.size() == 0) {
                this.hasGoodsCodeChanged = false;
            }
        }
        return arrayList;
    }

    public void setGoodsImageFolder(String str) {
        this.imageFolder = str;
    }

    public String getGoodsImageFolder() {
        return this.imageFolder;
    }

    public void setDefaultGoodsImage(Bitmap bitmap) {
        this.defaultGoodsImage = bitmap;
    }

    public void setDefaultGoodsMarkImage(Bitmap bitmap) {
        this.defaultGoodsMarkImage = bitmap;
    }

    public String getGoodsVideo(String str) {
        String _getGoodsVideo = _getGoodsVideo(str, "mov");
        if (_getGoodsVideo.length() == 0) {
            _getGoodsVideo = _getGoodsVideo(str, "mp4");
        }
        if (_getGoodsVideo.length() == 0) {
            _getGoodsVideo = _getGoodsVideo(str, "avi");
        }
        if (_getGoodsVideo.length() == 0) {
            _getGoodsVideo = _getGoodsVideo(str, "3gp");
        }
        if (_getGoodsVideo.length() == 0) {
            _getGoodsVideo = _getGoodsVideo(str, "flv");
        }
        return _getGoodsVideo.length() == 0 ? _getGoodsVideo(str, "rmvb") : _getGoodsVideo;
    }

    private String _getGoodsVideo(String str, String str2) {
        try {
            File file = new File(this.imageFolder + UsbFile.separator + str + "." + str2);
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00000" + str + "." + str2);
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0000" + str + "." + str2);
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/000" + str + "." + str2);
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00" + str + "." + str2);
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0" + str + "." + str2);
            }
            return !file.exists() ? "" : file.getAbsolutePath();
        } catch (Exception unused) {
            return "";
        }
    }

    public String getGoodsImagePath(String str) {
        try {
            File file = new File(this.imageFolder + UsbFile.separator + str + ".png");
            if (!file.exists()) {
                file = new File(this.imageFolder + UsbFile.separator + str + ".jpg");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00000" + str + ".png");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00000" + str + ".jpg");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0000" + str + ".png");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0000" + str + ".jpg");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/000" + str + ".png");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/000" + str + ".jpg");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00" + str + ".png");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/00" + str + ".jpg");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0" + str + ".png");
            }
            if (!file.exists()) {
                file = new File(this.imageFolder + "/0" + str + ".jpg");
            }
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public Bitmap getGoodsImage(String str, boolean z) {
        Bitmap bitmap = null;
        try {
            String str2 = "Goods|" + str + "|" + z;
            bitmap = BitmapCache.getBitmapFromMemCache(str2);
            if (bitmap == null) {
                bitmap = ImageUtils.getBitmap(getGoodsImagePath(str), 0, z ? SettingType.LIGHT_BOX_ROLLING_INTERVAL : 160);
                if (bitmap != null) {
                    BitmapCache.addBitmapToMemoryCache(str2, bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.defaultGoodsImage == null) {
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "defaultGoodsImage is null");
        }
        return bitmap == null ? this.defaultGoodsImage : bitmap;
    }

    public Bitmap getGoodsImage(String str, int i, int i2) {
        Bitmap bitmap;
        try {
            String str2 = "Goods|" + str + "|" + i + "|" + i2;
            bitmap = BitmapCache.getBitmapFromMemCache(str2);
            if (bitmap == null) {
                try {
                    bitmap = ImageUtils.getBitmap(getGoodsImagePath(str), i, i2);
                    if (bitmap != null) {
                        BitmapCache.addBitmapToMemoryCache(str2, bitmap);
                    }
                } catch (Exception unused) {
                }
            }
        } catch (Exception unused2) {
            bitmap = null;
        }
        if (this.defaultGoodsImage == null) {
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "defaultGoodsImage is null");
        }
        if (bitmap == null) {
            Loger.writeLog("SHJ", "goodsCode:" + str + " 图片不存在");
            try {
                downLoadGoodsImages(str, false, null);
            } catch (Exception unused3) {
            }
        }
        return bitmap == null ? this.defaultGoodsImage : bitmap;
    }

    public Bitmap getGoodsMarkerImage(String str) {
        Goods goodsByCode;
        Bitmap bitmap = null;
        try {
            goodsByCode = getGoodsByCode(str);
        } catch (Exception unused) {
        }
        if (goodsByCode.getMarkImage().equalsIgnoreCase("default")) {
            return this.defaultGoodsMarkImage;
        }
        String str2 = "GoodsMarker|" + goodsByCode.getMarkImage();
        bitmap = BitmapCache.getBitmapFromMemCache(str2);
        if (bitmap == null) {
            bitmap = ImageUtils.getBitmap(this.imageFolder + "/cx/" + goodsByCode.getMarkImage(), 0, 100);
            if (bitmap != null) {
                BitmapCache.addBitmapToMemoryCache(str2, bitmap);
            }
        }
        return bitmap;
    }

    public int getGoodsCount(String str) {
        return Shj.getGoodsCount(str);
    }

    public Goods getGoodsByCode(String str) {
        try {
            Goods goods = this.goodsMap.get(str);
            if (goods.getPrice() == -1) {
                return null;
            }
            return goods;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isGoodsOnSale(String str) {
        HashMap<Integer, ShelfInfo> shelfInfosByGoodsCode = Shj.getShelfInfosByGoodsCode(str);
        if (shelfInfosByGoodsCode == null) {
            return false;
        }
        Iterator<ShelfInfo> it = shelfInfosByGoodsCode.values().iterator();
        while (it.hasNext()) {
            if (it.next().isStatusOK()) {
                return true;
            }
        }
        return false;
    }

    public int getFirstGoodsShelf(String str) {
        int nextGoodsShelf;
        if (isGoodsOnSale(str) && (nextGoodsShelf = getNextGoodsShelf(str)) != -1) {
            return nextGoodsShelf;
        }
        HashMap<Integer, ShelfInfo> shelfInfosByGoodsCode = Shj.getShelfInfosByGoodsCode(str);
        if (shelfInfosByGoodsCode == null) {
            return -1;
        }
        Iterator<ShelfInfo> it = shelfInfosByGoodsCode.values().iterator();
        if (it.hasNext()) {
            return it.next().getShelf().intValue();
        }
        return -1;
    }

    public int getNextGoodsShelf(String str, String str2, int i) {
        boolean z;
        if (Shj.getSelectedShelf() != null && Shj.getSelectedShelf().getGoodsCode() == str) {
            return Shj.getSelectedShelf().getShelf().intValue();
        }
        HashMap<Integer, ShelfInfo> shelfInfosByGoodsCode = Shj.getShelfInfosByGoodsCode(str, str2);
        int i2 = -1;
        if (shelfInfosByGoodsCode == null) {
            return -1;
        }
        if (i == 0) {
            if (this.tempShelves == null) {
                this.tempShelves = new ArrayList();
            }
            this.tempShelves.clear();
            for (ShelfInfo shelfInfo : shelfInfosByGoodsCode.values()) {
                shelfInfo.setReady2OfferCount(0);
                this.tempShelves.add(shelfInfo);
            }
            Collections.sort(this.tempShelves, new Comparator<ShelfInfo>() { // from class: com.shj.biz.goods.GoodsManager.3
                AnonymousClass3() {
                }

                @Override // java.util.Comparator
                public int compare(ShelfInfo shelfInfo2, ShelfInfo shelfInfo3) {
                    if (shelfInfo2.getGoodsCount().intValue() > shelfInfo3.getGoodsCount().intValue()) {
                        return -1;
                    }
                    return shelfInfo2.getGoodsCount().intValue() < shelfInfo3.getGoodsCount().intValue() ? 1 : 0;
                }
            });
        }
        for (ShelfInfo shelfInfo2 : this.tempShelves) {
            if (shelfInfo2.isStatusOK() && shelfInfo2.getGoodsCount().intValue() > 0) {
                int ready2OfferCount = shelfInfo2.getReady2OfferCount();
                if (shelfInfo2.getGoodsCount().intValue() - ready2OfferCount > 0) {
                    i2 = shelfInfo2.getShelf().intValue();
                    shelfInfo2.setReady2OfferCount(ready2OfferCount + 1);
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    break;
                }
            }
        }
        return i2;
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Comparator<ShelfInfo> {
        AnonymousClass3() {
        }

        @Override // java.util.Comparator
        public int compare(ShelfInfo shelfInfo2, ShelfInfo shelfInfo3) {
            if (shelfInfo2.getGoodsCount().intValue() > shelfInfo3.getGoodsCount().intValue()) {
                return -1;
            }
            return shelfInfo2.getGoodsCount().intValue() < shelfInfo3.getGoodsCount().intValue() ? 1 : 0;
        }
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements Comparator<ShelfInfo> {
        AnonymousClass4() {
        }

        @Override // java.util.Comparator
        public int compare(ShelfInfo shelfInfo, ShelfInfo shelfInfo2) {
            if (shelfInfo.getGoodsCount().intValue() > shelfInfo2.getGoodsCount().intValue()) {
                return 1;
            }
            return shelfInfo.getGoodsCount().intValue() < shelfInfo2.getGoodsCount().intValue() ? -1 : 0;
        }
    }

    public int getNextGoodsShelf(String str) {
        return getNextGoodsShelf(str, null, 0);
    }

    public void _onUpdateGoodsCount(String str, int i) {
        if (!this.goodsMap.containsKey(str)) {
            Loger.writeLog("SHJ", "goods.json中未配置此商品{" + str + ")");
            updateGoods(str, "");
        }
        this.goodsMap.get(str).setCount(i);
        addLateastGoods(this.goodsMap.get(str));
        if (Shj.isResetFinished()) {
            ShjManager.getGoodsStatusListener().onUpdateGoodsCount(str, i);
        }
    }

    public void _onUpdateGoodsPrice(Integer num, String str, Integer num2) {
        if (!this.goodsMap.containsKey(str)) {
            Loger.writeLog("SHJ", "goods.json中未配置此商品{" + str + "," + num2 + ")");
            updateGoods(str, "");
        }
        Goods goods = this.goodsMap.get(str);
        if (goods.getPrice() == num2.intValue()) {
            return;
        }
        goods.setPrice(num2.intValue());
        addLateastGoods(this.goodsMap.get(str));
        if (Shj.isResetFinished()) {
            ShjManager.getGoodsStatusListener().onUpdateGoodsPrice(str, num2);
        }
    }

    public void _onUpdateShelfGoods(Integer num, String str, String str2, String str3) {
        if (!this.goodsMap.containsKey(str)) {
            updateGoods(str, str3);
        }
        if (!str.equalsIgnoreCase(str2)) {
            updateKeyGoodsMap(num.intValue(), str, str2, str3);
            try {
                if (Shj.getShelfInfo(num).getGoodsName().isEmpty()) {
                    Shj.getShelfInfo(num).setGoodsName(this.goodsMap.get(str).getName());
                }
            } catch (Exception unused) {
            }
        }
        if (Shj.isResetFinished()) {
            this.hasGoodsCodeChanged = true;
            addLateastGoods(getGoodsByCode(str));
            ShjManager.getGoodsStatusListener().onUpdateShelfGoods(num, str);
            ShjManager.getGoodsManager().downLoadGoodsImages(str, false, null);
        }
    }

    public void _onOfferingGoods(int i) {
        ShelfInfo selectedShelf = Shj.getSelectedShelf();
        ShjManager.getGoodsStatusListener().onOfferingGoods(selectedShelf.getShelf().intValue(), selectedShelf.getGoodsCode(), i);
        if (!ShjManager.getOrderManager().hasOrder() || ShjManager.isBatchOfferingGoods()) {
            return;
        }
        Loger.writeLog("SALES", "正在取消原有支付方式");
        ShjManager.getOrderManager().cancelPay();
    }

    public void _onOfferGoodsSuccess(int i) {
        try {
            ShelfInfo selectedShelf = Shj.getSelectedShelf();
            ShjManager.getGoodsStatusListener().onOfferingGoods_Success(selectedShelf.getShelf().intValue(), selectedShelf.getGoodsCode(), i);
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public void _onOfferGoodsBlocked(int i) {
        try {
            ShelfInfo selectedShelf = Shj.getSelectedShelf();
            ShjManager.getGoodsStatusListener().onOfferingGoods_Blocked(selectedShelf.getShelf().intValue(), selectedShelf.getGoodsCode(), i);
        } catch (Exception unused) {
        }
    }

    public void _onOfferGoodsNone(int i) {
        ShelfInfo selectedShelf = Shj.getSelectedShelf();
        ShjManager.getGoodsStatusListener().onOfferingGoods_None(selectedShelf.getShelf().intValue(), selectedShelf.getGoodsCode(), i);
    }

    public void _onSelectGoodsOnShelf(Integer num) {
        ShelfInfo shelfInfo = Shj.getShelfInfo(num);
        if (shelfInfo == null || ShjManager.isBatchOfferingGoods()) {
            return;
        }
        ShjManager.getGoodsStatusListener().onSelectGoods(shelfInfo.getGoodsCode());
    }

    public void _onDeselectGoodsOnShelf(Integer num) {
        ShelfInfo shelfInfo = Shj.getShelfInfo(num);
        if (shelfInfo != null) {
            ShjManager.getGoodsStatusListener().onDeselectGoods(shelfInfo.getGoodsCode());
        }
    }

    public void _onUpdateShelfGoodsState(int i, int i2) {
        if (Shj.isResetFinished()) {
            ShjManager.getGoodsStatusListener().onUpdateShelfGoodsStatus(i, i2);
            if (Shj.getShelfInfo(Integer.valueOf(i)) != null) {
                addLateastGoods(getGoodsByCode(Shj.getShelfInfo(Integer.valueOf(i)).getGoodsCode()));
            }
        }
    }

    public void _checkNeedDownloadGoodsInfo() {
        Loger.writeLog("SHJ", "检查是否有商品信息和图片需要更新");
        try {
            List<Goods> allGoods = getAllGoods();
            ArrayList arrayList = new ArrayList();
            for (Goods goods : allGoods) {
                if (getGoodsImagePath(goods.getCode()) == null || goods.getName() == null || goods.getName().length() == 0 || goods.getName().equals(goods.getCode())) {
                    Loger.writeLog("SHJ", "商品编号：" + goods.getCode() + " 信息需更新");
                    arrayList.add(goods.getCode());
                }
            }
            if (!arrayList.isEmpty()) {
                Loger.writeLog("SHJ", "准备更新商品信息和图片");
                ShjManager.getGoodsManager().downLoadGoodsImages(null, false, null);
            } else {
                Loger.writeLog("SHJ", "商品信息及图片已是最新");
            }
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public void updateGoodsReseveInfosByServerCommand(String str) {
        try {
            for (String str2 : str.split(UsbFile.separator)) {
                try {
                    String[] split = str2.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                    Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(split[0]);
                    goodsByCode.setReserveCount(Integer.parseInt(split[1]));
                    ShjManager.getGoodsManager().addLateastGoods(goodsByCode);
                } catch (Exception unused) {
                }
            }
        } catch (Exception unused2) {
        }
    }

    public void startUpdateGoodsIsOnSale() {
        String queryHdqsxxUrl = NetAddress.queryHdqsxxUrl();
        if (queryHdqsxxUrl == null || queryHdqsxxUrl.length() == 0) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", Shj.getMachineId());
        } catch (Exception unused) {
        }
        RequestItem requestItem = new RequestItem(queryHdqsxxUrl, jSONObject, "POST");
        requestItem.setRequestMaxCount(Integer.MAX_VALUE);
        requestItem.setLostAble(true);
        requestItem.setRepeatDelay(1800000);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.goods.GoodsManager.5
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass5() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                Loger.writeLog("REQUEST", "获取货道停售信息失败:" + i + StringUtils.SPACE + str);
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取货道停售信息失败，shjty/getHdTsxx：onFailure");
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                try {
                    JSONObject jSONObject2 = new JSONObject(str.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
                    if (!RequestHelper.isNewRequestResult(jSONObject2, "startUpdateGoodsIsOnSale")) {
                        Loger.writeLog("REQUEST", "货道停售信息没有变化");
                        return false;
                    }
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject2, "startUpdateGoodsIsOnSale");
                    if (!updateVersionedRequestResult.has("code") || !updateVersionedRequestResult.getString("code").equals("H0000")) {
                        return false;
                    }
                    JSONArray jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    List<Goods> allGoods = ShjManager.getGoodsManager().getAllGoods();
                    ArrayList arrayList = new ArrayList();
                    for (Goods goods : allGoods) {
                        for (ShelfInfo shelfInfo : Shj.getShelfInfosByGoodsCode(goods.getCode()).values()) {
                            if (shelfInfo.getDatas("pickcode_only") != null) {
                                if (((Boolean) shelfInfo.getDatas("pickcode_only")).booleanValue() && !arrayList.contains(goods)) {
                                    arrayList.add(goods);
                                }
                                shelfInfo.setDatas("pickcode_only", false);
                                shelfInfo.setDatas("isClock", false);
                                shelfInfo.setPickOnly(false);
                                shelfInfo.setStopSaleByServer(false);
                            }
                        }
                    }
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        try {
                            JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                            int i3 = jSONObject3.getInt("hdbh");
                            ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(i3));
                            if (shelfInfo2 != null) {
                                boolean z = jSONObject3.getBoolean("sfshd");
                                boolean z2 = jSONObject3.getBoolean("jzcqh");
                                if (z || z2) {
                                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_Order, "", "仅取货:" + z + " 已锁定:" + z2);
                                }
                                Loger.writeLog("REQUEST", "货道停售信息更新shelf:" + i3 + "商品:" + shelfInfo2.getGoodsName() + "(" + shelfInfo2.getGoodsCode() + ") 取货专用:" + z + " 已锁定:" + z2);
                                shelfInfo2.setDatas("pickcode_only", Boolean.valueOf(z));
                                shelfInfo2.setPickOnly(z);
                                shelfInfo2.setDatas("isClock", Boolean.valueOf(z2));
                                shelfInfo2.setStopSaleByServer(z2);
                                if (z || z2) {
                                    ShjManager.getGoodsManager().addLateastGoods(ShjManager.getGoodsManager().getGoodsByCode(shelfInfo2.getGoodsCode()));
                                }
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                    }
                    Loger.writeLog("REQUEST", "货道停售信息更新，通知更新界面");
                    return false;
                } catch (Exception e) {
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "解新货道停售信息出错 shjty/getHdTsxx：onSuccess,error:" + e.getLocalizedMessage());
                    return false;
                }
            }
        });
        RequestHelper.clearVersionedRequestResult("startUpdateGoodsIsOnSale");
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass5() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            Loger.writeLog("REQUEST", "获取货道停售信息失败:" + i + StringUtils.SPACE + str);
            AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取货道停售信息失败，shjty/getHdTsxx：onFailure");
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            try {
                JSONObject jSONObject2 = new JSONObject(str.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
                if (!RequestHelper.isNewRequestResult(jSONObject2, "startUpdateGoodsIsOnSale")) {
                    Loger.writeLog("REQUEST", "货道停售信息没有变化");
                    return false;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject2, "startUpdateGoodsIsOnSale");
                if (!updateVersionedRequestResult.has("code") || !updateVersionedRequestResult.getString("code").equals("H0000")) {
                    return false;
                }
                JSONArray jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                List<Goods> allGoods = ShjManager.getGoodsManager().getAllGoods();
                ArrayList arrayList = new ArrayList();
                for (Goods goods : allGoods) {
                    for (ShelfInfo shelfInfo : Shj.getShelfInfosByGoodsCode(goods.getCode()).values()) {
                        if (shelfInfo.getDatas("pickcode_only") != null) {
                            if (((Boolean) shelfInfo.getDatas("pickcode_only")).booleanValue() && !arrayList.contains(goods)) {
                                arrayList.add(goods);
                            }
                            shelfInfo.setDatas("pickcode_only", false);
                            shelfInfo.setDatas("isClock", false);
                            shelfInfo.setPickOnly(false);
                            shelfInfo.setStopSaleByServer(false);
                        }
                    }
                }
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    try {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                        int i3 = jSONObject3.getInt("hdbh");
                        ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(i3));
                        if (shelfInfo2 != null) {
                            boolean z = jSONObject3.getBoolean("sfshd");
                            boolean z2 = jSONObject3.getBoolean("jzcqh");
                            if (z || z2) {
                                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_Order, "", "仅取货:" + z + " 已锁定:" + z2);
                            }
                            Loger.writeLog("REQUEST", "货道停售信息更新shelf:" + i3 + "商品:" + shelfInfo2.getGoodsName() + "(" + shelfInfo2.getGoodsCode() + ") 取货专用:" + z + " 已锁定:" + z2);
                            shelfInfo2.setDatas("pickcode_only", Boolean.valueOf(z));
                            shelfInfo2.setPickOnly(z);
                            shelfInfo2.setDatas("isClock", Boolean.valueOf(z2));
                            shelfInfo2.setStopSaleByServer(z2);
                            if (z || z2) {
                                ShjManager.getGoodsManager().addLateastGoods(ShjManager.getGoodsManager().getGoodsByCode(shelfInfo2.getGoodsCode()));
                            }
                        }
                    } catch (Exception unused2) {
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                }
                Loger.writeLog("REQUEST", "货道停售信息更新，通知更新界面");
                return false;
            } catch (Exception e) {
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "解新货道停售信息出错 shjty/getHdTsxx：onSuccess,error:" + e.getLocalizedMessage());
                return false;
            }
        }
    }

    public void startUpdateGoodsReserveInfosTask() {
        String orderGoodsByMachineIdUrl = NetAddress.getOrderGoodsByMachineIdUrl();
        if (orderGoodsByMachineIdUrl == null || orderGoodsByMachineIdUrl.length() == 0) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", Shj.getMachineId());
        } catch (Exception unused) {
        }
        RequestItem requestItem = new RequestItem(orderGoodsByMachineIdUrl, jSONObject, "POST");
        requestItem.setRequestMaxCount(Integer.MAX_VALUE);
        requestItem.setLostAble(true);
        requestItem.setRepeatDelay(3600000);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.goods.GoodsManager.6
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass6() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                Loger.writeLog("REQUEST", "获取机器商品预定信息失败:" + i + StringUtils.SPACE + str + "errorMesage = " + (th != null ? th.getMessage() : ""));
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取机器商品预定信息失败，orderGoods/getOrderGoodsByMachineId：onFailure");
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                JSONObject jSONObject2;
                try {
                    jSONObject2 = new JSONObject(str.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
                } catch (Exception e) {
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "处理机器商品预定信息失败，orderGoods/getOrderGoodsByMachineId：onSuccess,error:" + e.getLocalizedMessage());
                }
                if (!RequestHelper.isNewRequestResult(jSONObject2, "queryLockedGoodsInfo")) {
                    Loger.writeLog("REQUEST", "商品预定信息没有变化");
                    return false;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject2, "queryLockedGoodsInfo");
                if (updateVersionedRequestResult.has("code") && updateVersionedRequestResult.getString("code").equals("H0000")) {
                    JSONArray jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    List<Goods> allGoods = ShjManager.getGoodsManager().getAllGoods();
                    ArrayList arrayList = new ArrayList();
                    for (Goods goods : allGoods) {
                        Loger.writeLog("REQUEST", "商品预定更新goods:" + goods.getName());
                        if (goods.getReserveCount() > 0) {
                            arrayList.add(goods);
                        }
                        goods.setReserveCount(0);
                    }
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        try {
                            JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                            Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(jSONObject3.getString("spbh"));
                            if (goodsByCode != null) {
                                Loger.writeLog("REQUEST", "商品预定更新goods:" + goodsByCode.getName());
                                goodsByCode.setReserveCount(jSONObject3.getInt("ydsl"));
                                ShjManager.getGoodsManager().addLateastGoods(goodsByCode);
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                    }
                    Loger.writeLog("REQUEST", "商品预定更新，通知更新界面");
                }
                return false;
            }
        });
        RequestHelper.clearVersionedRequestResult("queryLockedGoodsInfo");
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass6() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            Loger.writeLog("REQUEST", "获取机器商品预定信息失败:" + i + StringUtils.SPACE + str + "errorMesage = " + (th != null ? th.getMessage() : ""));
            AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取机器商品预定信息失败，orderGoods/getOrderGoodsByMachineId：onFailure");
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            JSONObject jSONObject2;
            try {
                jSONObject2 = new JSONObject(str.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
            } catch (Exception e) {
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "处理机器商品预定信息失败，orderGoods/getOrderGoodsByMachineId：onSuccess,error:" + e.getLocalizedMessage());
            }
            if (!RequestHelper.isNewRequestResult(jSONObject2, "queryLockedGoodsInfo")) {
                Loger.writeLog("REQUEST", "商品预定信息没有变化");
                return false;
            }
            JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject2, "queryLockedGoodsInfo");
            if (updateVersionedRequestResult.has("code") && updateVersionedRequestResult.getString("code").equals("H0000")) {
                JSONArray jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                List<Goods> allGoods = ShjManager.getGoodsManager().getAllGoods();
                ArrayList arrayList = new ArrayList();
                for (Goods goods : allGoods) {
                    Loger.writeLog("REQUEST", "商品预定更新goods:" + goods.getName());
                    if (goods.getReserveCount() > 0) {
                        arrayList.add(goods);
                    }
                    goods.setReserveCount(0);
                }
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    try {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                        Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(jSONObject3.getString("spbh"));
                        if (goodsByCode != null) {
                            Loger.writeLog("REQUEST", "商品预定更新goods:" + goodsByCode.getName());
                            goodsByCode.setReserveCount(jSONObject3.getInt("ydsl"));
                            ShjManager.getGoodsManager().addLateastGoods(goodsByCode);
                        }
                    } catch (Exception unused2) {
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                }
                Loger.writeLog("REQUEST", "商品预定更新，通知更新界面");
            }
            return false;
        }
    }

    public void startUpdateGoodsInfo(OnUpdateGoodsInfoListener onUpdateGoodsInfoListener) {
        String str = NetAddress.getAppBaseUrl() + "/service-goods/marketing/newQueryGoodsMarketing";
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", Shj.getMachineId());
        } catch (Exception unused) {
        }
        RequestItem requestItem = new RequestItem(str, jSONObject, "POST");
        requestItem.setRequestMaxCount(Integer.MAX_VALUE);
        requestItem.setLostAble(true);
        requestItem.setRepeatDelay(300000);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.goods.GoodsManager.7
            final /* synthetic */ OnUpdateGoodsInfoListener val$listener;

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass7(OnUpdateGoodsInfoListener onUpdateGoodsInfoListener2) {
                onUpdateGoodsInfoListener = onUpdateGoodsInfoListener2;
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                Loger.writeLog("REQUEST", "获取机器促销信息失败:" + i + StringUtils.SPACE + str2);
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取机器促销信息失败，marketing/newQueryGoodsMarketing：onFailure");
                if (onUpdateGoodsInfoListener == null || requestItem2.getRepeat() != 1) {
                    return;
                }
                onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished((List) requestItem2.getObj());
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                String str3;
                String str4;
                String str5;
                JSONObject jSONObject2;
                Goods goodsByCode;
                String str6 = "hyzx";
                String str7 = "";
                try {
                    JSONObject jSONObject3 = new JSONObject(str2.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
                    if (!RequestHelper.isNewRequestResult(jSONObject3, "queryGoodsMarketing")) {
                        Loger.writeLog("REQUEST", "优惠没有变化");
                        return false;
                    }
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject3, "queryGoodsMarketing");
                    try {
                        if (updateVersionedRequestResult.has("code") && updateVersionedRequestResult.getString("code").equals("H0000")) {
                            JSONArray jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray("yhxqList");
                            for (Goods goods : ShjManager.getGoodsManager().getAllGoods()) {
                                goods.clearActivityDescript();
                                goods.setMarkImage("");
                                goods.setDiscountPrice(0);
                            }
                            int i2 = 0;
                            while (i2 < jSONArray.length()) {
                                try {
                                    jSONObject2 = jSONArray.getJSONObject(i2);
                                    goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(jSONObject2.getString("spbh"));
                                } catch (Exception unused2) {
                                }
                                if (goodsByCode != null) {
                                    goodsByCode.setMarkImage(jSONObject2.getString("yxlx"));
                                    if (jSONObject2.has(str6) && !jSONObject2.getString(str6).isEmpty()) {
                                        goodsByCode.setMarkImage(jSONObject2.getString(str6));
                                    }
                                    if (jSONObject2.getString("yxlx").equalsIgnoreCase("商品折扣") && jSONObject2.getDouble("yhje") > 0.0d) {
                                        str4 = str6;
                                        str5 = str7;
                                        double price = goodsByCode.getPrice();
                                        try {
                                            double d = jSONObject2.getDouble("yhje");
                                            Double.isNaN(price);
                                            goodsByCode.setDiscountPrice((int) (price * d));
                                        } catch (Exception unused3) {
                                        }
                                    } else {
                                        str4 = str6;
                                        str5 = str7;
                                        if (jSONObject2.getString("yxlx").equalsIgnoreCase("特价") && jSONObject2.getDouble("yhje") > 0.0d) {
                                            goodsByCode.setDiscountPrice((int) (jSONObject2.getDouble("yhje") * 100.0d));
                                        } else if (jSONObject2.getString("yxlx").equalsIgnoreCase("优惠码") && jSONObject2.getDouble("yhje") > 0.0d) {
                                            double price2 = goodsByCode.getPrice();
                                            double d2 = jSONObject2.getDouble("yhje") * 100.0d;
                                            Double.isNaN(price2);
                                            goodsByCode.setDiscountPrice((int) (price2 - d2));
                                        } else if (jSONObject2.getString("yxlx").equalsIgnoreCase("满减") && jSONObject2.getDouble("mjszje") > 0.0d && jSONObject2.getDouble("mjyhje") > 0.0d) {
                                            goodsByCode.setFullcut_fullPrice((int) (jSONObject2.getDouble("mjszje") * 100.0d));
                                            goodsByCode.setFullcut_cutPrice((int) (jSONObject2.getDouble("mjyhje") * 100.0d));
                                        }
                                    }
                                    if (jSONObject2.has("bz") && !jSONObject2.isNull("bz")) {
                                        goodsByCode.addActivityDescript(jSONObject2.getString("bz"));
                                    }
                                    GoodsManager.this.addLateastGoods(goodsByCode);
                                    i2++;
                                    str7 = str5;
                                    str6 = str4;
                                }
                                str4 = str6;
                                str5 = str7;
                                i2++;
                                str7 = str5;
                                str6 = str4;
                            }
                            str3 = str7;
                            Loger.writeLog("REQUEST", "优惠更新，通知更新界面");
                            if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                                if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                                    onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                                }
                            } else {
                                ShjManager.getGoodsStatusListener().onGoodsUpdated();
                            }
                        } else {
                            str3 = "";
                            if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                                onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                            }
                        }
                        return false;
                    } catch (Exception e) {
                        e = e;
                        AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, str3, "处理机器促销信息失败，marketing/newQueryGoodsMarketing：onSuccess,error:" + e.getLocalizedMessage());
                        if (onUpdateGoodsInfoListener == null || requestItem2.getRepeat() != 1) {
                            return false;
                        }
                        onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                        return false;
                    }
                } catch (Exception e2) {
                    e = e2;
                    str3 = "";
                }
            }
        });
        RequestHelper.clearVersionedRequestResult("queryGoodsMarketing");
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ OnUpdateGoodsInfoListener val$listener;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass7(OnUpdateGoodsInfoListener onUpdateGoodsInfoListener2) {
            onUpdateGoodsInfoListener = onUpdateGoodsInfoListener2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            Loger.writeLog("REQUEST", "获取机器促销信息失败:" + i + StringUtils.SPACE + str2);
            AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, "", "获取机器促销信息失败，marketing/newQueryGoodsMarketing：onFailure");
            if (onUpdateGoodsInfoListener == null || requestItem2.getRepeat() != 1) {
                return;
            }
            onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished((List) requestItem2.getObj());
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            String str3;
            String str4;
            String str5;
            JSONObject jSONObject2;
            Goods goodsByCode;
            String str6 = "hyzx";
            String str7 = "";
            try {
                JSONObject jSONObject3 = new JSONObject(str2.replace("\"[", "[").replace("]\"", "]").replace("\\", ""));
                if (!RequestHelper.isNewRequestResult(jSONObject3, "queryGoodsMarketing")) {
                    Loger.writeLog("REQUEST", "优惠没有变化");
                    return false;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject3, "queryGoodsMarketing");
                try {
                    if (updateVersionedRequestResult.has("code") && updateVersionedRequestResult.getString("code").equals("H0000")) {
                        JSONArray jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray("yhxqList");
                        for (Goods goods : ShjManager.getGoodsManager().getAllGoods()) {
                            goods.clearActivityDescript();
                            goods.setMarkImage("");
                            goods.setDiscountPrice(0);
                        }
                        int i2 = 0;
                        while (i2 < jSONArray.length()) {
                            try {
                                jSONObject2 = jSONArray.getJSONObject(i2);
                                goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(jSONObject2.getString("spbh"));
                            } catch (Exception unused2) {
                            }
                            if (goodsByCode != null) {
                                goodsByCode.setMarkImage(jSONObject2.getString("yxlx"));
                                if (jSONObject2.has(str6) && !jSONObject2.getString(str6).isEmpty()) {
                                    goodsByCode.setMarkImage(jSONObject2.getString(str6));
                                }
                                if (jSONObject2.getString("yxlx").equalsIgnoreCase("商品折扣") && jSONObject2.getDouble("yhje") > 0.0d) {
                                    str4 = str6;
                                    str5 = str7;
                                    double price = goodsByCode.getPrice();
                                    try {
                                        double d = jSONObject2.getDouble("yhje");
                                        Double.isNaN(price);
                                        goodsByCode.setDiscountPrice((int) (price * d));
                                    } catch (Exception unused3) {
                                    }
                                } else {
                                    str4 = str6;
                                    str5 = str7;
                                    if (jSONObject2.getString("yxlx").equalsIgnoreCase("特价") && jSONObject2.getDouble("yhje") > 0.0d) {
                                        goodsByCode.setDiscountPrice((int) (jSONObject2.getDouble("yhje") * 100.0d));
                                    } else if (jSONObject2.getString("yxlx").equalsIgnoreCase("优惠码") && jSONObject2.getDouble("yhje") > 0.0d) {
                                        double price2 = goodsByCode.getPrice();
                                        double d2 = jSONObject2.getDouble("yhje") * 100.0d;
                                        Double.isNaN(price2);
                                        goodsByCode.setDiscountPrice((int) (price2 - d2));
                                    } else if (jSONObject2.getString("yxlx").equalsIgnoreCase("满减") && jSONObject2.getDouble("mjszje") > 0.0d && jSONObject2.getDouble("mjyhje") > 0.0d) {
                                        goodsByCode.setFullcut_fullPrice((int) (jSONObject2.getDouble("mjszje") * 100.0d));
                                        goodsByCode.setFullcut_cutPrice((int) (jSONObject2.getDouble("mjyhje") * 100.0d));
                                    }
                                }
                                if (jSONObject2.has("bz") && !jSONObject2.isNull("bz")) {
                                    goodsByCode.addActivityDescript(jSONObject2.getString("bz"));
                                }
                                GoodsManager.this.addLateastGoods(goodsByCode);
                                i2++;
                                str7 = str5;
                                str6 = str4;
                            }
                            str4 = str6;
                            str5 = str7;
                            i2++;
                            str7 = str5;
                            str6 = str4;
                        }
                        str3 = str7;
                        Loger.writeLog("REQUEST", "优惠更新，通知更新界面");
                        if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                            if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                                onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                            }
                        } else {
                            ShjManager.getGoodsStatusListener().onGoodsUpdated();
                        }
                    } else {
                        str3 = "";
                        if (onUpdateGoodsInfoListener != null && requestItem2.getRepeat() == 1) {
                            onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                        }
                    }
                    return false;
                } catch (Exception e) {
                    e = e;
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_HttpRequest, str3, "处理机器促销信息失败，marketing/newQueryGoodsMarketing：onSuccess,error:" + e.getLocalizedMessage());
                    if (onUpdateGoodsInfoListener == null || requestItem2.getRepeat() != 1) {
                        return false;
                    }
                    onUpdateGoodsInfoListener.OnUpdateGoodsInfoFinished(null);
                    return false;
                }
            } catch (Exception e2) {
                e = e2;
                str3 = "";
            }
        }
    }

    public void updateGoodsInfo() {
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        RequestParams requestParams = new RequestParams();
        requestParams.put("machineCode", Shj.getMachineId());
        RequestItem requestItem = new RequestItem(goodsInfoUrl, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.goods.GoodsManager.8
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass8() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                try {
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood");
                    if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
                        JSONArray jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        updateVersionedRequestResult = new JSONObject();
                        updateVersionedRequestResult.put("goods", jSONArray);
                    }
                    SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", updateVersionedRequestResult.toString(), false);
                    return true;
                } catch (Exception unused) {
                    return true;
                }
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$8 */
    /* loaded from: classes2.dex */
    class AnonymousClass8 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass8() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            try {
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood");
                if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
                    JSONArray jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    updateVersionedRequestResult = new JSONObject();
                    updateVersionedRequestResult.put("goods", jSONArray);
                }
                SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", updateVersionedRequestResult.toString(), false);
                return true;
            } catch (Exception unused) {
                return true;
            }
        }
    }

    public void downLoadGoodsImages(String str, boolean z, GoodsImagesDownloadListener goodsImagesDownloadListener) {
        String str2 = "0000" + str;
        this.xspbh = str2;
        this.xspbh = str2.substring(str2.length() - 4);
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        RequestParams requestParams = new RequestParams();
        requestParams.put("machineCode", Shj.getMachineId());
        if (str != null && str.length() > 0) {
            requestParams.put("spbh", this.xspbh);
        }
        requestParams.put("queryType", 0);
        RequestItem requestItem = new RequestItem(goodsInfoUrl, requestParams, HttpGet.METHOD_NAME);
        requestItem.setObj(goodsImagesDownloadListener);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.goods.GoodsManager.9
            final /* synthetic */ boolean val$abs;
            final /* synthetic */ GoodsImagesDownloadListener val$l;
            final /* synthetic */ String val$spbh;

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z2) {
            }

            AnonymousClass9(GoodsImagesDownloadListener goodsImagesDownloadListener2, boolean z2, String str3) {
                goodsImagesDownloadListener = goodsImagesDownloadListener2;
                z = z2;
                str = str3;
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
                try {
                    goodsImagesDownloadListener.onTaskFinished(0, 0, str3);
                } catch (Exception unused) {
                }
            }

            /* JADX WARN: Code restructure failed: missing block: B:117:0x02a0, code lost:
            
                if (r2 != null) goto L390;
             */
            /* JADX WARN: Removed duplicated region for block: B:118:0x029b A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:126:0x0293 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:132:? A[Catch: Exception -> 0x0332, SYNTHETIC, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x0332, blocks: (B:3:0x000c, B:6:0x0039, B:7:0x004e, B:19:0x00a8, B:20:0x00ad, B:23:0x00f3, B:27:0x0115, B:29:0x011b, B:31:0x0125, B:33:0x012b, B:35:0x0137, B:36:0x0148, B:38:0x0168, B:41:0x0173, B:43:0x0179, B:47:0x0188, B:51:0x018d, B:52:0x0197, B:56:0x01e4, B:57:0x019e, B:59:0x01a2, B:61:0x01a8, B:63:0x01d6, B:67:0x01dd, B:69:0x01e1, B:75:0x0213, B:77:0x0244, B:78:0x0248, B:80:0x024e, B:85:0x0259, B:99:0x02a3, B:100:0x02a9, B:102:0x02af, B:104:0x02c9, B:106:0x02dc, B:109:0x0328, B:128:0x0296, B:148:0x01fc, B:150:0x0202, B:154:0x0210, B:155:0x020d, B:161:0x0331, B:187:0x0096, B:205:0x004a, B:22:0x00c3, B:162:0x00e5), top: B:2:0x000c, inners: #0 }] */
            /* JADX WARN: Removed duplicated region for block: B:133:0x028c A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:146:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:147:0x01f5  */
            /* JADX WARN: Removed duplicated region for block: B:173:0x00a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:177:0x009b A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:19:0x00a8 A[Catch: Exception -> 0x0332, TRY_ENTER, TryCatch #1 {Exception -> 0x0332, blocks: (B:3:0x000c, B:6:0x0039, B:7:0x004e, B:19:0x00a8, B:20:0x00ad, B:23:0x00f3, B:27:0x0115, B:29:0x011b, B:31:0x0125, B:33:0x012b, B:35:0x0137, B:36:0x0148, B:38:0x0168, B:41:0x0173, B:43:0x0179, B:47:0x0188, B:51:0x018d, B:52:0x0197, B:56:0x01e4, B:57:0x019e, B:59:0x01a2, B:61:0x01a8, B:63:0x01d6, B:67:0x01dd, B:69:0x01e1, B:75:0x0213, B:77:0x0244, B:78:0x0248, B:80:0x024e, B:85:0x0259, B:99:0x02a3, B:100:0x02a9, B:102:0x02af, B:104:0x02c9, B:106:0x02dc, B:109:0x0328, B:128:0x0296, B:148:0x01fc, B:150:0x0202, B:154:0x0210, B:155:0x020d, B:161:0x0331, B:187:0x0096, B:205:0x004a, B:22:0x00c3, B:162:0x00e5), top: B:2:0x000c, inners: #0 }] */
            /* JADX WARN: Removed duplicated region for block: B:26:0x0114  */
            /* JADX WARN: Removed duplicated region for block: B:77:0x0244 A[Catch: Exception -> 0x0332, TryCatch #1 {Exception -> 0x0332, blocks: (B:3:0x000c, B:6:0x0039, B:7:0x004e, B:19:0x00a8, B:20:0x00ad, B:23:0x00f3, B:27:0x0115, B:29:0x011b, B:31:0x0125, B:33:0x012b, B:35:0x0137, B:36:0x0148, B:38:0x0168, B:41:0x0173, B:43:0x0179, B:47:0x0188, B:51:0x018d, B:52:0x0197, B:56:0x01e4, B:57:0x019e, B:59:0x01a2, B:61:0x01a8, B:63:0x01d6, B:67:0x01dd, B:69:0x01e1, B:75:0x0213, B:77:0x0244, B:78:0x0248, B:80:0x024e, B:85:0x0259, B:99:0x02a3, B:100:0x02a9, B:102:0x02af, B:104:0x02c9, B:106:0x02dc, B:109:0x0328, B:128:0x0296, B:148:0x01fc, B:150:0x0202, B:154:0x0210, B:155:0x020d, B:161:0x0331, B:187:0x0096, B:205:0x004a, B:22:0x00c3, B:162:0x00e5), top: B:2:0x000c, inners: #0 }] */
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public boolean onSuccess(com.oysb.utils.http.RequestItem r23, int r24, java.lang.String r25) {
                /*
                    Method dump skipped, instructions count: 826
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.goods.GoodsManager.AnonymousClass9.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
            }

            /* renamed from: com.shj.biz.goods.GoodsManager$9$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements DownloadImagesListener {
                AnonymousClass1() {
                }

                @Override // com.oysb.utils.image.DownloadImagesListener
                public void onDownloadImage(NetImageItem netImageItem, int i, int i2) {
                    Loger.writeLog("SHJ", "下载商品图片:" + netImageItem.getUrl());
                    StepProgressDialog.update(i, i2 + 1, "", false);
                }

                @Override // com.oysb.utils.image.DownloadImagesListener
                public void onStartDownloadImages(int i) {
                    StepProgressDialog.showProgressDlg("");
                }

                @Override // com.oysb.utils.image.DownloadImagesListener
                public void onDownloadImagesFinished(int i, int i2) {
                    StepProgressDialog.closeProgressDlg(3000);
                    Loger.writeLog("SHJ", "商品信息和图片已更新完成");
                    try {
                        goodsImagesDownloadListener.onTaskFinished(i, i2, null);
                    } catch (Exception unused) {
                    }
                    if (str != null && str.length() > 0) {
                        GoodsManager.this.addLateastGoods(GoodsManager.this.getGoodsByCode(str));
                    } else {
                        ShjManager.getGoodsManager().reLoadGoods();
                    }
                }
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.goods.GoodsManager$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$abs;
        final /* synthetic */ GoodsImagesDownloadListener val$l;
        final /* synthetic */ String val$spbh;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass9(GoodsImagesDownloadListener goodsImagesDownloadListener2, boolean z2, String str3) {
            goodsImagesDownloadListener = goodsImagesDownloadListener2;
            z = z2;
            str = str3;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
            try {
                goodsImagesDownloadListener.onTaskFinished(0, 0, str3);
            } catch (Exception unused) {
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem, int i, String str) {
            /*  JADX ERROR: Method code generation error
                java.lang.NullPointerException
                */
            /*
                Method dump skipped, instructions count: 826
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.goods.GoodsManager.AnonymousClass9.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
        }

        /* renamed from: com.shj.biz.goods.GoodsManager$9$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DownloadImagesListener {
            AnonymousClass1() {
            }

            @Override // com.oysb.utils.image.DownloadImagesListener
            public void onDownloadImage(NetImageItem netImageItem, int i, int i2) {
                Loger.writeLog("SHJ", "下载商品图片:" + netImageItem.getUrl());
                StepProgressDialog.update(i, i2 + 1, "", false);
            }

            @Override // com.oysb.utils.image.DownloadImagesListener
            public void onStartDownloadImages(int i) {
                StepProgressDialog.showProgressDlg("");
            }

            @Override // com.oysb.utils.image.DownloadImagesListener
            public void onDownloadImagesFinished(int i, int i2) {
                StepProgressDialog.closeProgressDlg(3000);
                Loger.writeLog("SHJ", "商品信息和图片已更新完成");
                try {
                    goodsImagesDownloadListener.onTaskFinished(i, i2, null);
                } catch (Exception unused) {
                }
                if (str != null && str.length() > 0) {
                    GoodsManager.this.addLateastGoods(GoodsManager.this.getGoodsByCode(str));
                } else {
                    ShjManager.getGoodsManager().reLoadGoods();
                }
            }
        }
    }

    public void onGoodsSynchronismFinished() {
        ShjManager.getGoodsStatusListener().onGoodsSynchronismFinished();
    }
}

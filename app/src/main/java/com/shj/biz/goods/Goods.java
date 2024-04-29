package com.shj.biz.goods;

import android.text.TextUtils;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.ObjectHelper;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class Goods {
    private String advice;
    private String batch;
    private String[] classification;
    private String factory;
    private String function;
    private boolean isNew;
    private String[] keys;
    private boolean needIdentityCheck;
    private String phoneNumber;
    private Object selectShelf;
    private String smsimg;
    private String spc;
    private String groupName = "";
    private String code = "0";
    private String codeEx = "";
    private String name = "";
    private String image = "";
    private String markImage = "";
    private int count = 0;
    private int reserveCount = 0;
    private int price = 0;
    private String descript = "";
    private int discountPrice = 0;
    private int fullcut_fullPrice = 0;
    private int fullcut_cutPrice = 0;
    private String activityDescript = "";
    private String productive = "";
    private HashMap<String, String> effectives = new HashMap<>();
    private String className = "";
    private boolean keyGoodsMapUpdated = false;
    private boolean isPrescription = false;

    public boolean isKeyGoodsMapUpdated() {
        return this.keyGoodsMapUpdated;
    }

    public void setKeyGoodsMapUpdated(boolean z) {
        this.keyGoodsMapUpdated = z;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String str) {
        this.image = str;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int i) {
        this.price = i;
    }

    public String getDescript() {
        String str = this.descript;
        if (str == null || str.length() == 0) {
            this.descript = "";
        }
        return this.descript;
    }

    public void setDescript(String str) {
        this.descript = str;
    }

    public String[] getKeys() {
        String[] strArr = this.keys;
        if (strArr == null || strArr.length == 0) {
            this.keys = new String[]{""};
        }
        return this.keys;
    }

    public void setKeys(String[] strArr) {
        if (strArr != this.keys && ObjectHelper.ary2String(strArr) != ObjectHelper.ary2String(this.keys)) {
            setKeyGoodsMapUpdated(false);
        }
        this.keys = strArr;
    }

    public boolean hasKey(String str) {
        String[] strArr = this.keys;
        if (strArr == null) {
            return false;
        }
        for (String str2 : strArr) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public String getMarkImage() {
        return this.markImage;
    }

    public void setMarkImage(String str) {
        this.markImage = str;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setNew(boolean z) {
        this.isNew = z;
    }

    public String getCodeEx() {
        String str = this.codeEx;
        return str == null ? "" : str;
    }

    public void setCodeEx(String str) {
        this.codeEx = str;
    }

    public String getFactory() {
        return this.factory;
    }

    public void setFactory(String str) {
        this.factory = str;
    }

    public String getSpc() {
        String str = this.spc;
        if (str == null || str.length() == 0) {
            this.spc = "无";
        }
        return this.spc;
    }

    public void setSpc(String str) {
        this.spc = str;
    }

    public String getBatch() {
        return this.batch;
    }

    public void setBatch(String str) {
        this.batch = str;
    }

    public String getEffective(String str) {
        return this.effectives.containsKey(str) ? this.effectives.get(str) : "";
    }

    public void setEffective(String str, String str2) {
        this.effectives.put(str, str2);
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String str) {
        this.function = str;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public String getSmsimg() {
        return this.smsimg;
    }

    public void setSmsimg(String str) {
        this.smsimg = str;
    }

    public Object getSelectShelf() {
        return this.selectShelf;
    }

    public void setSelectShelf(Object obj) {
        this.selectShelf = obj;
    }

    public String[] getClassification() {
        String[] strArr = this.classification;
        if (strArr == null || strArr.length == 0) {
            this.classification = new String[]{""};
        }
        return this.classification;
    }

    public void setClassification(String[] strArr) {
        this.classification = strArr;
    }

    public String getClassName() {
        if (this.className == null) {
            this.className = "";
        }
        return this.className;
    }

    public void setClassName(String str) {
        this.className = str;
    }

    public int getDiscountPrice() {
        int i = this.discountPrice;
        return i == 0 ? this.price : i;
    }

    public void setDiscountPrice(int i) {
        if (i <= 0) {
            i = 0;
        }
        this.discountPrice = i;
    }

    public String getProductive() {
        return this.productive;
    }

    public void setProductive(String str) {
        this.productive = str;
    }

    public void setNeedIdentityCheck(boolean z) {
        this.needIdentityCheck = z;
    }

    public boolean isNeedIdentityCheck() {
        return this.needIdentityCheck;
    }

    public String getActivityDescript() {
        if (this.activityDescript == null) {
            this.activityDescript = "";
        }
        return this.activityDescript;
    }

    public void addActivityDescript(String str) {
        this.activityDescript += str + VoiceWakeuperAidl.PARAMS_SEPARATE;
    }

    public void clearActivityDescript() {
        this.activityDescript = "";
    }

    public int getFullcut_fullPrice() {
        return this.fullcut_fullPrice;
    }

    public void setFullcut_fullPrice(int i) {
        this.fullcut_fullPrice = i;
    }

    public int getFullcut_cutPrice() {
        return this.fullcut_cutPrice;
    }

    public void setFullcut_cutPrice(int i) {
        this.fullcut_cutPrice = i;
    }

    public int getReserveCount() {
        return this.reserveCount;
    }

    public void setReserveCount(int i) {
        this.reserveCount = i;
    }

    public boolean isPrescription() {
        return this.isPrescription;
    }

    public void setPrescription(boolean z) {
        this.isPrescription = z;
    }

    public String getAdvice() {
        return TextUtils.isEmpty(this.advice) ? "无" : this.advice;
    }

    public void setAdvice(String str) {
        this.advice = str;
    }
}

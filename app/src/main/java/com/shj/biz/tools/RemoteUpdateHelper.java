package com.shj.biz.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import com.downloader.Error;
import com.github.mjdev.libaums.fs.UsbFile;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechUtility;
import com.loopj.android.http.HttpGet;
import com.oysb.utils.Loger;
import com.oysb.utils.PRDownloaderTool;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.image.ImageUtils;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.xy.net.thirdparty.OnRequestListenter;
import com.oysb.xy.net.thirdparty.ThirdPartyUpdates_QueryAdLists;
import com.oysb.xy.net.thirdparty.ThirdPartyUpdates_QueryPayTypes;
import com.oysb.xy.net.thirdparty.ThirdPartyUpdates_QueryShelfPriceLists;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ReportManager;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.Goods;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.tools.DownloadService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class RemoteUpdateHelper {
    private String packNo;
    private static HashMap<Integer, PaymentOptionDetail> podmap = new HashMap<>();
    public static String payLogoPath = SDFileUtils.SDCardRoot + "xyshj/logo/";

    public RemoteUpdateHelper(String str) {
        this.packNo = str;
    }

    public static PaymentOptionDetail getPaymentOptionDetail(int i) {
        if (podmap.containsKey(Integer.valueOf(i))) {
            return podmap.get(Integer.valueOf(i));
        }
        return null;
    }

    public static void clearPaymentOptionDetail() {
        podmap.clear();
    }

    public static HashMap<Integer, PaymentOptionDetail> getPaymentOptionDetail() {
        return podmap;
    }

    public static void setPaymentOptionDetail(int i, PaymentOptionDetail paymentOptionDetail) {
        podmap.put(Integer.valueOf(i), paymentOptionDetail);
    }

    public void doUpdateAdImage() {
        ThirdPartyUpdates_QueryAdLists thirdPartyUpdates_QueryAdLists = new ThirdPartyUpdates_QueryAdLists();
        thirdPartyUpdates_QueryAdLists.setParam(Shj.getMachineId(), this.packNo);
        thirdPartyUpdates_QueryAdLists.request(new OnRequestListenter() { // from class: com.shj.biz.tools.RemoteUpdateHelper.1
            AnonymousClass1() {
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onFail(String str) {
                ReportManager.sendHeartBeat();
                Loger.writeLog("APP", "查询广告更新列表失败：" + str);
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onSucces(String str) {
                JSONArray jSONArray;
                Loger.writeLog("APP", "查询广告更新列表成功：" + str);
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    jSONObject.remove("PackNo");
                    if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdateAdImage")) {
                        ReportManager.sendHeartBeat();
                        Loger.writeLog("REQUEST", "广告更新没有变化");
                        return;
                    }
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdateAdImage");
                    String string = updateVersionedRequestResult.getString("status");
                    updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                        File file = new File(SDFileUtils.SDCardRoot + "xyShj/avFiles");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            if (jSONObject2.has(IjkMediaMeta.IJKM_KEY_TYPE)) {
                                String string2 = jSONObject2.getString(IjkMediaMeta.IJKM_KEY_TYPE);
                                String string3 = jSONObject2.getString("image");
                                String lowerCase = string3.substring(string3.lastIndexOf(UsbFile.separator) + 1).trim().toLowerCase();
                                arrayList2.add(lowerCase);
                                File file2 = new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + string2);
                                if (!file2.exists()) {
                                    file2.mkdirs();
                                } else {
                                    if (!new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + string2 + UsbFile.separator + lowerCase).exists()) {
                                        arrayList.add(jSONObject2);
                                    }
                                }
                            }
                        }
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            for (File file3 : new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + jSONArray.getJSONObject(i2).getString(IjkMediaMeta.IJKM_KEY_TYPE)).listFiles()) {
                                if (!arrayList2.contains(file3.getName())) {
                                    SDFileUtils.safeDeleteFile(file3);
                                }
                            }
                        }
                        if (arrayList.size() > 0) {
                            SaveImageRunnable saveImageRunnable = new SaveImageRunnable();
                            saveImageRunnable.items = arrayList;
                            new Thread(saveImageRunnable).start();
                            return;
                        }
                    }
                    ReportManager.sendHeartBeat();
                } catch (JSONException e) {
                    ReportManager.sendHeartBeat();
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnRequestListenter {
        AnonymousClass1() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            ReportManager.sendHeartBeat();
            Loger.writeLog("APP", "查询广告更新列表失败：" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            JSONArray jSONArray;
            Loger.writeLog("APP", "查询广告更新列表成功：" + str);
            try {
                JSONObject jSONObject = new JSONObject(str);
                jSONObject.remove("PackNo");
                if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdateAdImage")) {
                    ReportManager.sendHeartBeat();
                    Loger.writeLog("REQUEST", "广告更新没有变化");
                    return;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdateAdImage");
                String string = updateVersionedRequestResult.getString("status");
                updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                    File file = new File(SDFileUtils.SDCardRoot + "xyShj/avFiles");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        if (jSONObject2.has(IjkMediaMeta.IJKM_KEY_TYPE)) {
                            String string2 = jSONObject2.getString(IjkMediaMeta.IJKM_KEY_TYPE);
                            String string3 = jSONObject2.getString("image");
                            String lowerCase = string3.substring(string3.lastIndexOf(UsbFile.separator) + 1).trim().toLowerCase();
                            arrayList2.add(lowerCase);
                            File file2 = new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + string2);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            } else {
                                if (!new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + string2 + UsbFile.separator + lowerCase).exists()) {
                                    arrayList.add(jSONObject2);
                                }
                            }
                        }
                    }
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        for (File file3 : new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + jSONArray.getJSONObject(i2).getString(IjkMediaMeta.IJKM_KEY_TYPE)).listFiles()) {
                            if (!arrayList2.contains(file3.getName())) {
                                SDFileUtils.safeDeleteFile(file3);
                            }
                        }
                    }
                    if (arrayList.size() > 0) {
                        SaveImageRunnable saveImageRunnable = new SaveImageRunnable();
                        saveImageRunnable.items = arrayList;
                        new Thread(saveImageRunnable).start();
                        return;
                    }
                }
                ReportManager.sendHeartBeat();
            } catch (JSONException e) {
                ReportManager.sendHeartBeat();
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SaveImageRunnable implements Runnable {
        int idx;
        List<JSONObject> items;

        private SaveImageRunnable() {
            this.idx = 0;
        }

        /* synthetic */ SaveImageRunnable(RemoteUpdateHelper remoteUpdateHelper, AnonymousClass1 anonymousClass1) {
            this();
        }

        /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$SaveImageRunnable$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements PRDownloaderTool.OnDownloadConditionListener {
            AnonymousClass1() {
            }

            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
            public void downloadFileExists() {
                SaveImageRunnable.this.idx++;
                if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                    ReportManager.sendHeartBeat();
                }
            }

            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
            public void onDownloadComplete(String str) {
                SaveImageRunnable.this.idx++;
                if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                    ReportManager.sendHeartBeat();
                }
            }

            @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
            public void onError(Error error) {
                SaveImageRunnable.this.idx++;
                if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                    ReportManager.sendHeartBeat();
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                new PRDownloaderTool.OnDownloadConditionListener() { // from class: com.shj.biz.tools.RemoteUpdateHelper.SaveImageRunnable.1
                    AnonymousClass1() {
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void downloadFileExists() {
                        SaveImageRunnable.this.idx++;
                        if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                            ReportManager.sendHeartBeat();
                        }
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void onDownloadComplete(String str) {
                        SaveImageRunnable.this.idx++;
                        if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                            ReportManager.sendHeartBeat();
                        }
                    }

                    @Override // com.oysb.utils.PRDownloaderTool.OnDownloadConditionListener
                    public void onError(Error error) {
                        SaveImageRunnable.this.idx++;
                        if (SaveImageRunnable.this.idx == SaveImageRunnable.this.items.size()) {
                            ReportManager.sendHeartBeat();
                        }
                    }
                };
                for (int i = 0; i < this.items.size(); i++) {
                    JSONObject jSONObject = this.items.get(i);
                    String string = jSONObject.getString("image");
                    Loger.writeLog("REQUEST", "download:" + string);
                    PRDownloaderTool.addSerialDownloadTask(string, SDFileUtils.SDCardRoot + "xyShj/avFiles/" + jSONObject.getString(IjkMediaMeta.IJKM_KEY_TYPE), string.substring(string.lastIndexOf(UsbFile.separator) + 1).trim().toLowerCase(), null, null);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v14, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r8v7, types: [java.io.ByteArrayOutputStream] */
    public boolean saveImage(String str, String str2, String str3) {
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        BufferedOutputStream bufferedOutputStream = null;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            try {
                httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                inputStream = httpURLConnection.getInputStream();
                try {
                } catch (Exception e2) {
                    e = e2;
                    str = 0;
                } catch (Throwable th) {
                    th = th;
                    str = 0;
                }
            } catch (Exception e3) {
                e = e3;
                str = 0;
                inputStream = null;
            } catch (Throwable th2) {
                th = th2;
                str = 0;
                inputStream = null;
            }
            if (httpURLConnection.getResponseCode() != 200) {
                if (inputStream != null) {
                    inputStream.close();
                }
                return false;
            }
            str = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    str.write(bArr, 0, read);
                }
                byte[] byteArray = str.toByteArray();
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new File(SDFileUtils.SDCardRoot + "xyShj/avFiles/" + str2 + UsbFile.separator + str3)));
                try {
                    decodeByteArray.compress(Bitmap.CompressFormat.PNG, 80, bufferedOutputStream2);
                    bufferedOutputStream2.flush();
                    try {
                        bufferedOutputStream2.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    try {
                        str.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        }
                    }
                    return true;
                } catch (Exception e7) {
                    bufferedOutputStream = bufferedOutputStream2;
                    e = e7;
                    e.printStackTrace();
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e8) {
                            e8.printStackTrace();
                        }
                    }
                    if (str != 0) {
                        try {
                            str.close();
                        } catch (Exception e9) {
                            e9.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return false;
                } catch (Throwable th3) {
                    bufferedOutputStream = bufferedOutputStream2;
                    th = th3;
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e10) {
                            e10.printStackTrace();
                        }
                    }
                    if (str != 0) {
                        try {
                            str.close();
                        } catch (Exception e11) {
                            e11.printStackTrace();
                        }
                    }
                    if (inputStream == null) {
                        throw th;
                    }
                    try {
                        inputStream.close();
                        throw th;
                    } catch (Exception e12) {
                        e12.printStackTrace();
                        throw th;
                    }
                }
            } catch (Exception e13) {
                e = e13;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public void doUpdateShelfPrice() {
        ThirdPartyUpdates_QueryShelfPriceLists thirdPartyUpdates_QueryShelfPriceLists = new ThirdPartyUpdates_QueryShelfPriceLists();
        thirdPartyUpdates_QueryShelfPriceLists.setParam(Shj.getMachineId(), this.packNo);
        thirdPartyUpdates_QueryShelfPriceLists.request(new OnRequestListenter() { // from class: com.shj.biz.tools.RemoteUpdateHelper.2
            AnonymousClass2() {
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onFail(String str) {
                ReportManager.sendHeartBeat();
                Loger.writeLog("APP", "查询商品更新列表失败：" + str);
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onSucces(String str) {
                JSONArray jSONArray;
                Loger.writeLog("APP", "查询商品更新列表成功：" + str);
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    jSONObject.remove("PackNo");
                    if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdateShelfPrice")) {
                        ReportManager.sendHeartBeat();
                        Loger.writeLog("REQUEST", "商品更新没有变化");
                        return;
                    }
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdateShelfPrice");
                    String string = updateVersionedRequestResult.getString("status");
                    updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                        JSONObject jSONObject2 = new JSONObject();
                        String str2 = SDFileUtils.SDCardRoot + "xyShj/goodsNet.json";
                        if (new File(str2).exists()) {
                            try {
                                jSONObject2 = new JSONObject(SDFileUtils.readFile(str2));
                            } catch (JSONException unused) {
                            }
                        }
                        JSONArray jSONArray2 = new JSONArray();
                        if (jSONObject2.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                            jSONArray2 = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        }
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                            int i2 = jSONObject3.getInt("slct");
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i2));
                            if (jSONObject3.has("code")) {
                                String string2 = jSONObject3.getString("code");
                                if (shelfInfo != null && !shelfInfo.getGoodsCode().equals(string2)) {
                                    ShjManager.setShelfGoodsCode(i2, string2);
                                }
                            }
                            if (jSONObject3.has(ShjDbHelper.COLUM_price)) {
                                int i3 = jSONObject3.getInt(ShjDbHelper.COLUM_price);
                                if (shelfInfo != null && shelfInfo.getPrice().intValue() != i3) {
                                    ShjManager.setShelfGoodsPrice(i2, i3);
                                }
                            }
                            if (jSONObject3.has("name")) {
                                Goods goods = null;
                                String string3 = jSONObject3.getString("name");
                                if (jSONObject3.has("code")) {
                                    goods = ShjManager.getGoodsManager().getGoodsByCode(jSONObject3.getString("code"));
                                } else if (shelfInfo != null) {
                                    goods = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
                                }
                                if (goods != null && !goods.getName().equals(string3)) {
                                    goods.setName(string3);
                                    if (!arrayList.contains(goods)) {
                                        arrayList.add(goods);
                                    }
                                }
                            }
                            jSONObject3.remove("slct");
                            for (int i4 = 0; i4 < jSONArray2.length(); i4++) {
                                if (Integer.parseInt(jSONArray2.getJSONObject(i4).getString("code")) == Integer.parseInt(jSONObject3.getString("code"))) {
                                    jSONArray2.remove(i4);
                                }
                            }
                            jSONArray2.put(jSONObject3);
                        }
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                        }
                        if (jSONArray2.length() > 0) {
                            jSONObject2.put("goods", jSONArray2);
                            SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", jSONObject2.toString(), false);
                            return;
                        }
                        return;
                    }
                    ReportManager.sendHeartBeat();
                } catch (JSONException e) {
                    ReportManager.sendHeartBeat();
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnRequestListenter {
        AnonymousClass2() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            ReportManager.sendHeartBeat();
            Loger.writeLog("APP", "查询商品更新列表失败：" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            JSONArray jSONArray;
            Loger.writeLog("APP", "查询商品更新列表成功：" + str);
            try {
                JSONObject jSONObject = new JSONObject(str);
                jSONObject.remove("PackNo");
                if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdateShelfPrice")) {
                    ReportManager.sendHeartBeat();
                    Loger.writeLog("REQUEST", "商品更新没有变化");
                    return;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdateShelfPrice");
                String string = updateVersionedRequestResult.getString("status");
                updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                    JSONObject jSONObject2 = new JSONObject();
                    String str2 = SDFileUtils.SDCardRoot + "xyShj/goodsNet.json";
                    if (new File(str2).exists()) {
                        try {
                            jSONObject2 = new JSONObject(SDFileUtils.readFile(str2));
                        } catch (JSONException unused) {
                        }
                    }
                    JSONArray jSONArray2 = new JSONArray();
                    if (jSONObject2.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                        jSONArray2 = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    }
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                        int i2 = jSONObject3.getInt("slct");
                        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i2));
                        if (jSONObject3.has("code")) {
                            String string2 = jSONObject3.getString("code");
                            if (shelfInfo != null && !shelfInfo.getGoodsCode().equals(string2)) {
                                ShjManager.setShelfGoodsCode(i2, string2);
                            }
                        }
                        if (jSONObject3.has(ShjDbHelper.COLUM_price)) {
                            int i3 = jSONObject3.getInt(ShjDbHelper.COLUM_price);
                            if (shelfInfo != null && shelfInfo.getPrice().intValue() != i3) {
                                ShjManager.setShelfGoodsPrice(i2, i3);
                            }
                        }
                        if (jSONObject3.has("name")) {
                            Goods goods = null;
                            String string3 = jSONObject3.getString("name");
                            if (jSONObject3.has("code")) {
                                goods = ShjManager.getGoodsManager().getGoodsByCode(jSONObject3.getString("code"));
                            } else if (shelfInfo != null) {
                                goods = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
                            }
                            if (goods != null && !goods.getName().equals(string3)) {
                                goods.setName(string3);
                                if (!arrayList.contains(goods)) {
                                    arrayList.add(goods);
                                }
                            }
                        }
                        jSONObject3.remove("slct");
                        for (int i4 = 0; i4 < jSONArray2.length(); i4++) {
                            if (Integer.parseInt(jSONArray2.getJSONObject(i4).getString("code")) == Integer.parseInt(jSONObject3.getString("code"))) {
                                jSONArray2.remove(i4);
                            }
                        }
                        jSONArray2.put(jSONObject3);
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ShjManager.getGoodsManager().addLateastGoods((Goods) it.next());
                    }
                    if (jSONArray2.length() > 0) {
                        jSONObject2.put("goods", jSONArray2);
                        SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", jSONObject2.toString(), false);
                        return;
                    }
                    return;
                }
                ReportManager.sendHeartBeat();
            } catch (JSONException e) {
                ReportManager.sendHeartBeat();
                e.printStackTrace();
            }
        }
    }

    public void doUpdatePayTypes() {
        ThirdPartyUpdates_QueryPayTypes thirdPartyUpdates_QueryPayTypes = new ThirdPartyUpdates_QueryPayTypes();
        thirdPartyUpdates_QueryPayTypes.setParam(Shj.getMachineId(), this.packNo);
        thirdPartyUpdates_QueryPayTypes.request(new OnRequestListenter() { // from class: com.shj.biz.tools.RemoteUpdateHelper.3
            AnonymousClass3() {
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onFail(String str) {
                ReportManager.sendHeartBeat();
                Loger.writeLog("APP", "查询支付方式失败：" + str);
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onSucces(String str) {
                JSONArray jSONArray;
                Loger.writeLog("APP", "查询支付方式成功：" + str);
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    jSONObject.remove("PackNo");
                    if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdatePayTypes")) {
                        ReportManager.sendHeartBeat();
                        Loger.writeLog("APP", "支付方式更新没有变化");
                        return;
                    }
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdatePayTypes");
                    String string = updateVersionedRequestResult.getString("status");
                    updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                        HashMap hashMap = new HashMap();
                        RemoteUpdateHelper.clearPaymentOptionDetail();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            OrderPayType.int2Enum(jSONObject2.getInt(IjkMediaMeta.IJKM_KEY_TYPE)).setName(jSONObject2.getString("name"));
                            PaymentOptionDetail paymentOptionDetail = new PaymentOptionDetail();
                            paymentOptionDetail.setType(jSONObject2.getInt(IjkMediaMeta.IJKM_KEY_TYPE));
                            paymentOptionDetail.setName(jSONObject2.getString("name"));
                            paymentOptionDetail.setLogoURL(jSONObject2.getString("logo"));
                            hashMap.put(jSONObject2.getString("name"), paymentOptionDetail);
                        }
                        Loger.writeLog("APP", hashMap.toString());
                        if (hashMap.size() > 0) {
                            new DownloadService(RemoteUpdateHelper.payLogoPath, hashMap, new DownloadService.DownloadStateListener() { // from class: com.shj.biz.tools.RemoteUpdateHelper.3.1
                                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                                public void onFailed() {
                                }

                                AnonymousClass1() {
                                }

                                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                                public void onitemFinish(PaymentOptionDetail paymentOptionDetail2) {
                                    String str2 = RemoteUpdateHelper.payLogoPath + paymentOptionDetail2.getName() + ".png";
                                    paymentOptionDetail2.getName().equalsIgnoreCase("gopay");
                                    paymentOptionDetail2.setIcon(new BitmapDrawable(ImageUtils.getBitmap(str2, 70, 50)));
                                    RemoteUpdateHelper.setPaymentOptionDetail(paymentOptionDetail2.getType(), paymentOptionDetail2);
                                }

                                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                                public void onFinish() {
                                    ReportManager.sendHeartBeat();
                                    Shj.isResetFinished();
                                }
                            }).startDownload();
                            return;
                        }
                    }
                    ReportManager.sendHeartBeat();
                } catch (JSONException e) {
                    ReportManager.sendHeartBeat();
                    e.printStackTrace();
                }
            }

            /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements DownloadService.DownloadStateListener {
                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                public void onFailed() {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                public void onitemFinish(PaymentOptionDetail paymentOptionDetail2) {
                    String str2 = RemoteUpdateHelper.payLogoPath + paymentOptionDetail2.getName() + ".png";
                    paymentOptionDetail2.getName().equalsIgnoreCase("gopay");
                    paymentOptionDetail2.setIcon(new BitmapDrawable(ImageUtils.getBitmap(str2, 70, 50)));
                    RemoteUpdateHelper.setPaymentOptionDetail(paymentOptionDetail2.getType(), paymentOptionDetail2);
                }

                @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                public void onFinish() {
                    ReportManager.sendHeartBeat();
                    Shj.isResetFinished();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnRequestListenter {
        AnonymousClass3() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            ReportManager.sendHeartBeat();
            Loger.writeLog("APP", "查询支付方式失败：" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            JSONArray jSONArray;
            Loger.writeLog("APP", "查询支付方式成功：" + str);
            try {
                JSONObject jSONObject = new JSONObject(str);
                jSONObject.remove("PackNo");
                if (!RequestHelper.isNewRequestResult(jSONObject, "doUpdatePayTypes")) {
                    ReportManager.sendHeartBeat();
                    Loger.writeLog("APP", "支付方式更新没有变化");
                    return;
                }
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(jSONObject, "doUpdatePayTypes");
                String string = updateVersionedRequestResult.getString("status");
                updateVersionedRequestResult.getString(NotificationCompat.CATEGORY_MESSAGE);
                if (string.startsWith("0") && (jSONArray = updateVersionedRequestResult.getJSONArray(SpeechUtility.TAG_RESOURCE_RESULT)) != null && jSONArray.length() > 0) {
                    HashMap hashMap = new HashMap();
                    RemoteUpdateHelper.clearPaymentOptionDetail();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        OrderPayType.int2Enum(jSONObject2.getInt(IjkMediaMeta.IJKM_KEY_TYPE)).setName(jSONObject2.getString("name"));
                        PaymentOptionDetail paymentOptionDetail = new PaymentOptionDetail();
                        paymentOptionDetail.setType(jSONObject2.getInt(IjkMediaMeta.IJKM_KEY_TYPE));
                        paymentOptionDetail.setName(jSONObject2.getString("name"));
                        paymentOptionDetail.setLogoURL(jSONObject2.getString("logo"));
                        hashMap.put(jSONObject2.getString("name"), paymentOptionDetail);
                    }
                    Loger.writeLog("APP", hashMap.toString());
                    if (hashMap.size() > 0) {
                        new DownloadService(RemoteUpdateHelper.payLogoPath, hashMap, new DownloadService.DownloadStateListener() { // from class: com.shj.biz.tools.RemoteUpdateHelper.3.1
                            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                            public void onFailed() {
                            }

                            AnonymousClass1() {
                            }

                            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                            public void onitemFinish(PaymentOptionDetail paymentOptionDetail2) {
                                String str2 = RemoteUpdateHelper.payLogoPath + paymentOptionDetail2.getName() + ".png";
                                paymentOptionDetail2.getName().equalsIgnoreCase("gopay");
                                paymentOptionDetail2.setIcon(new BitmapDrawable(ImageUtils.getBitmap(str2, 70, 50)));
                                RemoteUpdateHelper.setPaymentOptionDetail(paymentOptionDetail2.getType(), paymentOptionDetail2);
                            }

                            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
                            public void onFinish() {
                                ReportManager.sendHeartBeat();
                                Shj.isResetFinished();
                            }
                        }).startDownload();
                        return;
                    }
                }
                ReportManager.sendHeartBeat();
            } catch (JSONException e) {
                ReportManager.sendHeartBeat();
                e.printStackTrace();
            }
        }

        /* renamed from: com.shj.biz.tools.RemoteUpdateHelper$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DownloadService.DownloadStateListener {
            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
            public void onFailed() {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
            public void onitemFinish(PaymentOptionDetail paymentOptionDetail2) {
                String str2 = RemoteUpdateHelper.payLogoPath + paymentOptionDetail2.getName() + ".png";
                paymentOptionDetail2.getName().equalsIgnoreCase("gopay");
                paymentOptionDetail2.setIcon(new BitmapDrawable(ImageUtils.getBitmap(str2, 70, 50)));
                RemoteUpdateHelper.setPaymentOptionDetail(paymentOptionDetail2.getType(), paymentOptionDetail2);
            }

            @Override // com.shj.biz.tools.DownloadService.DownloadStateListener
            public void onFinish() {
                ReportManager.sendHeartBeat();
                Shj.isResetFinished();
            }
        }
    }
}

package com.shj.setting.Utils;

import android.content.Context;
import android.text.TextUtils;
import com.alipay.api.AlipayConstants;
import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.CommonTool;
import com.shj.setting.Dialog.SelectEnabledDialog;
import com.shj.setting.SearchSettingItemAdapter;
import com.shj.setting.generator.Generator;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class PinYinSearch {
    static final int GB_SP_DIFF = 160;
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    public static void getSearchSettingItemData(Context context, String str, List<SearchSettingItemAdapter.SearchAdapterData> list, UserSettingDao userSettingDao, boolean z) {
        list.clear();
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String lowerCase = str.toLowerCase();
        List<Integer> needShowSettingItem = SelectEnabledDialog.getNeedShowSettingItem();
        if (!z) {
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_CASH));
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_AGGREGATE_CODE));
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_WEIXIN));
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_ZFB));
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
            needShowSettingItem.remove(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
            needShowSettingItem.remove((Object) 211);
            needShowSettingItem.remove((Object) 212);
            needShowSettingItem.remove((Object) 213);
            needShowSettingItem.add(105);
        }
        boolean z2 = CommonTool.getLanguage(context).equals("en") || CommonTool.getLanguage(context).equals("fr");
        Iterator<Integer> it = needShowSettingItem.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            String settingName = SettingTypeName.getSettingName(context, intValue);
            if (z || AppSetting.isSettingEnabled(context, intValue, userSettingDao)) {
                if (!z2) {
                    settingName = getPinyinFirstLetter(settingName);
                }
                if (settingName.contains(lowerCase)) {
                    SearchSettingItemAdapter.SearchAdapterData searchAdapterData = new SearchSettingItemAdapter.SearchAdapterData();
                    searchAdapterData.settingType = intValue;
                    searchAdapterData.name = SettingTypeName.getSettingName(context, searchAdapterData.settingType);
                    String parentName = Generator.getParentName(searchAdapterData.settingType);
                    if (!TextUtils.isEmpty(parentName)) {
                        searchAdapterData.name = parentName + UsbFile.separator + searchAdapterData.name;
                    }
                    list.add(searchAdapterData);
                }
            }
        }
    }

    public static String getPinyinFirstLetter(String str) {
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            for (char c : str.toCharArray()) {
                str2 = str2 + getFirstLetter(c);
            }
        }
        return str2;
    }

    public static Character getFirstLetter(char c) {
        try {
            byte[] bytes = String.valueOf(c).getBytes(AlipayConstants.CHARSET_GBK);
            if (bytes[0] >= 128 || bytes[0] <= 0) {
                return Character.valueOf(convert(bytes));
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    static char convert(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] - 160);
        }
        int i2 = (bArr[0] * 100) + bArr[1];
        for (int i3 = 0; i3 < 23; i3++) {
            int[] iArr = secPosValueList;
            if (i2 >= iArr[i3] && i2 < iArr[i3 + 1]) {
                return firstLetter[i3];
            }
        }
        return '-';
    }
}

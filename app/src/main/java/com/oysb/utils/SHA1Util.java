package com.oysb.utils;


/* loaded from: classes2.dex */
public class SHA1Util {
    private static final String b64pad = "=";
    private static final int chrsz = 8;
    private static final boolean hexcase = false;

    private static int bit_rol(int i, int i2) {
        return (i >>> (32 - i2)) | (i << i2);
    }

    private static int rol(int i, int i2) {
        return (i >>> (32 - i2)) | (i << i2);
    }

    private static int safe_add(int i, int i2) {
        int i3 = (i & 65535) + (i2 & 65535);
        return ((((i >> 16) + (i2 >> 16)) + (i3 >> 16)) << 16) | (i3 & 65535);
    }

    private static int sha1_ft(int i, int i2, int i3, int i4) {
        if (i < 20) {
            return (i2 & i3) | ((i2 ^ (-1)) & i4);
        }
        if (i >= 40 && i < 60) {
            return (i2 & i3) | (i2 & i4) | (i3 & i4);
        }
        return (i2 ^ i3) ^ i4;
    }

    private static int sha1_kt(int i) {
        if (i < 20) {
            return 1518500249;
        }
        if (i < 40) {
            return 1859775393;
        }
        return i < 60 ? -1894007588 : -899497514;
    }

    public static String hex_sha1(String str) {
        if (str == null) {
            str = "";
        }
        return binb2hex(core_sha1(str2binb(str), str.length() * 8));
    }

    public static String b64_hmac_sha1(String str, String str2) {
        return binb2b64(core_hmac_sha1(str, str2));
    }

    public static String b64_sha1(String str) {
        if (str == null) {
            str = "";
        }
        return binb2b64(core_sha1(str2binb(str), str.length() * 8));
    }

    private static String binb2b64(int[] iArr) {
        int[] strechbinarray = strechbinarray(iArr, iArr.length * 4);
        String str = "";
        for (int i = 0; i < strechbinarray.length * 4; i += 3) {
            int i2 = i + 1;
            int i3 = (((strechbinarray[i >> 2] >> ((3 - (i % 4)) * 8)) & 255) << 16) | (((strechbinarray[i2 >> 2] >> ((3 - (i2 % 4)) * 8)) & 255) << 8);
            int i4 = i + 2;
            int i5 = i3 | ((strechbinarray[i4 >> 2] >> ((3 - (i4 % 4)) * 8)) & 255);
            for (int i6 = 0; i6 < 4; i6++) {
                str = (i * 8) + (i6 * 6) > strechbinarray.length * 32 ? str + b64pad : str + "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((i5 >> ((3 - i6) * 6)) & 63);
            }
        }
        return cleanb64str(str);
    }

    private static String binb2hex(int[] iArr) {
        String str = "";
        for (int i = 0; i < iArr.length * 4; i++) {
            int i2 = i >> 2;
            int i3 = (3 - (i % 4)) * 8;
            str = str + new Character("0123456789abcdef".charAt((iArr[i2] >> (i3 + 4)) & 15)).toString() + new Character("0123456789abcdef".charAt((iArr[i2] >> i3) & 15)).toString();
        }
        return str;
    }

    private static String binb2str(int[] iArr) {
        String str = "";
        for (int i = 0; i < iArr.length * 32; i += 8) {
            str = str + ((char) ((iArr[i >> 5] >>> (24 - (i % 32))) & 255));
        }
        return str;
    }

    private static String cleanb64str(String str) {
        String str2 = "";
        if (str == null) {
            str = "";
        }
        int length = str.length();
        if (length <= 1) {
            return str;
        }
        int i = length - 1;
        char charAt = str.charAt(i);
        while (i >= 0 && str.charAt(i) == charAt) {
            str2 = str2 + str.charAt(i);
            i--;
        }
        return str.substring(0, str.indexOf(str2));
    }

    private static int[] complete216(int[] iArr) {
        if (iArr.length >= 16) {
            return iArr;
        }
        int length = 16 - iArr.length;
        int[] iArr2 = new int[length];
        for (int i = 0; i < length; i++) {
            iArr2[i] = 0;
        }
        return concat(iArr, iArr2);
    }

    private static int[] concat(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[iArr.length + iArr2.length];
        for (int i = 0; i < iArr.length + iArr2.length; i++) {
            if (i < iArr.length) {
                iArr3[i] = iArr[i];
            } else {
                iArr3[i] = iArr2[i - iArr.length];
            }
        }
        return iArr3;
    }

    private static int[] core_hmac_sha1(String str, String str2) {
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        int[] complete216 = complete216(str2binb(str));
        if (complete216.length > 16) {
            complete216 = core_sha1(complete216, str.length() * 8);
        }
        int[] iArr = new int[16];
        int[] iArr2 = new int[16];
        for (int i = 0; i < 16; i++) {
            iArr[i] = 0;
            iArr2[i] = 0;
        }
        for (int i2 = 0; i2 < 16; i2++) {
            iArr[i2] = complete216[i2] ^ 909522486;
            iArr2[i2] = complete216[i2] ^ 1549556828;
        }
        return core_sha1(concat(iArr2, core_sha1(concat(iArr, str2binb(str2)), (str2.length() * 8) + 512)), 672);
    }

    private static int[] core_sha1(int[] iArr, int i) {
        int i2 = i >> 5;
        int[] strechbinarray = strechbinarray(iArr, i2);
        strechbinarray[i2] = strechbinarray[i2] | (128 << (24 - (i % 32)));
        int i3 = (((i + 64) >> 9) << 4) + 15;
        int[] strechbinarray2 = strechbinarray(strechbinarray, i3);
        strechbinarray2[i3] = i;
        int i4 = 80;
        int[] iArr2 = new int[80];
        int i5 = 1732584193;
        int i6 = -271733879;
        int i7 = -1732584194;
        int i8 = 271733878;
        int i9 = -1009589776;
        int i10 = 0;
        while (true) {
            int i11 = 1;
            int i12 = 5;
            if (i10 >= strechbinarray2.length) {
                return new int[]{i5, i6, i7, i8, i9};
            }
            int i13 = i5;
            int i14 = i6;
            int i15 = i7;
            int i16 = i8;
            int i17 = i9;
            int i18 = 0;
            while (i18 < i4) {
                if (i18 < 16) {
                    iArr2[i18] = strechbinarray2[i10 + i18];
                } else {
                    iArr2[i18] = rol(((iArr2[i18 - 3] ^ iArr2[i18 - 8]) ^ iArr2[i18 - 14]) ^ iArr2[i18 - 16], i11);
                }
                int safe_add = safe_add(safe_add(rol(i13, i12), sha1_ft(i18, i14, i15, i16)), safe_add(safe_add(i17, iArr2[i18]), sha1_kt(i18)));
                int rol = rol(i14, 30);
                i18++;
                i17 = i16;
                i14 = i13;
                i12 = 5;
                i13 = safe_add;
                i16 = i15;
                i15 = rol;
                i4 = 80;
                i11 = 1;
            }
            i5 = safe_add(i13, i5);
            i6 = safe_add(i14, i6);
            i7 = safe_add(i15, i7);
            i8 = safe_add(i16, i8);
            i9 = safe_add(i17, i9);
            i10 += 16;
            i4 = 80;
        }
    }

//    private static void dotest() {
//        System.out.println("hex_sha1(" + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + hex_sha1(SpeechEvent.KEY_EVENT_RECORD_DATA));
//        System.out.println("b64_sha1(" + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + b64_sha1(SpeechEvent.KEY_EVENT_RECORD_DATA));
//        System.out.println("str_sha1(" + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + str_sha1(SpeechEvent.KEY_EVENT_RECORD_DATA));
//        System.out.println("hex_hmac_sha1(key," + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + hex_hmac_sha1("key", SpeechEvent.KEY_EVENT_RECORD_DATA));
//        System.out.println("b64_hmac_sha1(key," + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + b64_hmac_sha1("key", SpeechEvent.KEY_EVENT_RECORD_DATA));
//        System.out.println("str_hmac_sha1(key," + SpeechEvent.KEY_EVENT_RECORD_DATA + ")=" + str_hmac_sha1("key", SpeechEvent.KEY_EVENT_RECORD_DATA));
//    }

    public static String hex_hmac_sha1(String str, String str2) {
        return binb2hex(core_hmac_sha1(str, str2));
    }

    private static boolean sha1_vm_test() {
        return hex_sha1("abc").equals("a9993e364706816aba3e25717850c26c9cd0d89d");
    }

    public static String str_hmac_sha1(String str, String str2) {
        return binb2str(core_hmac_sha1(str, str2));
    }

    public static String str_sha1(String str) {
        if (str == null) {
            str = "";
        }
        return binb2str(core_sha1(str2binb(str), str.length() * 8));
    }

    private static int[] str2binb(String str) {
        if (str == null) {
            str = "";
        }
        int length = str.length() * 8;
        int[] iArr = new int[length];
        for (int i = 0; i < str.length() * 8; i += 8) {
            int i2 = i >> 5;
            iArr[i2] = iArr[i2] | ((str.charAt(i / 8) & 255) << (24 - (i % 32)));
        }
        int i3 = 0;
        int i4 = 0;
        while (i3 < length && iArr[i3] != 0) {
            i3++;
            i4++;
        }
        int[] iArr2 = new int[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            iArr2[i5] = iArr[i5];
        }
        return iArr2;
    }

    private static int[] strechbinarray(int[] iArr, int i) {
        int length = iArr.length;
        int i2 = i + 1;
        if (length >= i2) {
            return iArr;
        }
        int[] iArr2 = new int[i2];
        for (int i3 = 0; i3 < i; i3++) {
            iArr2[i3] = 0;
        }
        for (int i4 = 0; i4 < length; i4++) {
            iArr2[i4] = iArr[i4];
        }
        return iArr2;
    }

    public static void main(String[] strArr) {
        System.out.println("admin的SHA1的值为：" + hex_sha1("admin") + ",length=" + hex_sha1("admin").length());
    }
}

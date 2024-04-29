package com.oysb.utils;

import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

/* loaded from: classes2.dex */
public final class MD5 {
    private static final String ALGORITHM = "MD5";
    private static final String LOG_TAG = "MD5";
    private static MessageDigest sDigest;

    static {
        try {
            sDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "Get MD5 Digest failed.");
            throw new UnsupportedDigestAlgorithmException("MD5", e);
        }
    }

    private MD5() {
    }

    public static final String encode(String str) {
        try {
            return Utility.hexString(sDigest.digest(str.getBytes()));
        } catch (Exception unused) {
            return str;
        }
    }
}

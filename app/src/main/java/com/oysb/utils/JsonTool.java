package com.oysb.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/* loaded from: classes2.dex */
public class JsonTool {
    public static <T> T parseJsonAllGson(String str, Class<T> cls) {
        return (T) new Gson().fromJson(str, (Class) cls);
    }

    /* renamed from: com.oysb.utils.JsonTool$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1<T> extends TypeToken<List<T>> {
        AnonymousClass1() {
        }
    }

    public static <T> List<T> parseJsonArrayGson(String str, Class<T> cls) {
        return (List) new Gson().fromJson(str, new TypeToken<List<T>>() { // from class: com.oysb.utils.JsonTool.1
        }.getType());
    }
}

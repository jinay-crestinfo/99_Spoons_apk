package com.oysb.utils;

import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class XyHashMap<K, V> extends HashMap<K, V> {
    ArrayList<K> keyList = new ArrayList<>();

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        if (!this.keyList.contains(k)) {
            this.keyList.add(k);
        }
        return (V) super.put(k, v);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        if (this.keyList.contains(obj)) {
            this.keyList.remove(obj);
        }
        return (V) super.remove(obj);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        this.keyList.clear();
        super.clear();
    }

    @Override // java.util.HashMap, java.util.AbstractMap
    public Object clone() {
        XyHashMap xyHashMap = (XyHashMap) super.clone();
        xyHashMap.keyList.addAll(this.keyList);
        return xyHashMap;
    }

    public ArrayList<K> getKeyList() {
        return this.keyList;
    }
}

package com.xyshj.database.setting.basedao;

import java.util.List;

/* loaded from: classes2.dex */
public interface IBaseDao<M> {
    Integer delete(M m);

    Long insert(M m);

    List<M> query(M m);

    List<M> query(M m, String str);

    List<M> query(M m, String str, Integer num, Integer num2);

    Integer update(M m, M m2);
}

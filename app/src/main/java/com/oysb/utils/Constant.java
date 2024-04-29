package com.oysb.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class Constant {
    public static int TYPE_BACKUPS = 8;
    public static int TYPE_REPORT = 4;
    public static int TYPE_UPGRADE = 2;
    public static List<ConfigurePackageInfo> needPackageNames = new ArrayList();

    public static List<ConfigurePackageInfo> getNeedPackageNames() {
        return needPackageNames;
    }

    public static void addPackageInfo(ConfigurePackageInfo configurePackageInfo) {
        needPackageNames.add(configurePackageInfo);
    }

    public static void updataPackageInfo(ConfigurePackageInfo configurePackageInfo) {
        boolean z;
        Iterator<ConfigurePackageInfo> it = needPackageNames.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            ConfigurePackageInfo next = it.next();
            if (next.packageName.equals(configurePackageInfo.packageName)) {
                next.type = configurePackageInfo.type;
                next.appName = configurePackageInfo.appName;
                z = true;
                break;
            }
        }
        if (z) {
            return;
        }
        needPackageNames.add(configurePackageInfo);
    }

    /* loaded from: classes2.dex */
    public static class ConfigurePackageInfo {
        public String appName;
        public String packageName;
        public int type;

        public ConfigurePackageInfo(String str, String str2, int i) {
            this.packageName = str;
            this.appName = str2;
            this.type = i;
        }
    }
}

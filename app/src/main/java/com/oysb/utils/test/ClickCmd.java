package com.oysb.utils.test;

import com.xyshj.database.setting.SettingType;

/* loaded from: classes2.dex */
public class ClickCmd extends Command {
    public ClickCmd(int i, int i2) {
        int x = Shell.getX(i);
        int y = Shell.getY(i2);
        addCommandItem("/dev/input/event" + Shell.aplf, 3, 0, x);
        addCommandItem("/dev/input/event" + Shell.aplf, 3, 1, y);
        addCommandItem("/dev/input/event" + Shell.aplf, 1, SettingType.NEW_LINKAGE_SYNCHRONIZATION_LAYER, 1);
        addCommandItem("/dev/input/event" + Shell.aplf, 0, 0, 0);
        addCommandItem("/dev/input/event" + Shell.aplf, 1, SettingType.NEW_LINKAGE_SYNCHRONIZATION_LAYER, 0);
        addCommandItem("/dev/input/event" + Shell.aplf, 0, 0, 0);
    }
}

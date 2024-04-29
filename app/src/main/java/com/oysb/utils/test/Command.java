package com.oysb.utils.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class Command {
    OnCommandResultListener resultListener;
    protected List<CommandItem> items = new ArrayList();
    private int wait = 0;

    public String toString() {
        return "ClickCmd";
    }

    public List<CommandItem> getCommandItems() {
        return this.items;
    }

    public void addCommandItem(String str, int i, int i2, int i3) {
        this.items.add(new CommandItem("sendevent " + str + StringUtils.SPACE + i + StringUtils.SPACE + i2 + StringUtils.SPACE + i3, 0L));
    }

    public void addCommandItem(String str, int i, int i2, int i3, int i4) {
        this.items.add(new CommandItem("sendevent " + str + StringUtils.SPACE + i + StringUtils.SPACE + i2 + StringUtils.SPACE + i3, i4));
    }

    public void setResultLisener(OnCommandResultListener onCommandResultListener) {
        this.resultListener = onCommandResultListener;
    }

    public OnCommandResultListener getResultLisener() {
        return this.resultListener;
    }

    public int getWait() {
        return this.wait;
    }

    public void setWait(int i) {
        this.wait = i;
    }
}

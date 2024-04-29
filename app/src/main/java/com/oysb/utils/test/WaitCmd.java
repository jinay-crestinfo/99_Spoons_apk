package com.oysb.utils.test;

/* loaded from: classes2.dex */
public class WaitCmd extends Command {
    public WaitCmd(int i) {
        addCommandItem("/dev/input/event" + Shell.aplf, 0, 0, 0, i);
    }
}

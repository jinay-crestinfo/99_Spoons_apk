package com.oysb.utils.test;

/* loaded from: classes2.dex */
public class TestCmd extends Command {
    public TestCmd(int i) {
        this.items.add(new CommandItem("getevent -p", 0L));
        this.items.add(new CommandItem("sendevent /dev/input/event0 1 158 1", 0L));
        this.items.add(new CommandItem("sendevent /dev/input/event0 1 158 0", 0L));
        this.items.add(new CommandItem("input keyevent 3", 0L));
        this.items.add(new CommandItem("input text  'helloworld!' ", 0L));
        this.items.add(new CommandItem("input tap 168 252", 0L));
    }
}

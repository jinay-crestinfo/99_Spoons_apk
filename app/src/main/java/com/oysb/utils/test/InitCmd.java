package com.oysb.utils.test;

import com.oysb.utils.Loger;

/* loaded from: classes2.dex */
public class InitCmd extends Command {
    public InitCmd() {
        setWait(5000);
        this.items.add(new CommandItem("getevent -p", 0L));
        setResultLisener(new OnCommandResultListener() { // from class: com.oysb.utils.test.InitCmd.1

            @Override // com.oysb.utils.test.OnCommandResultListener
            public void onResult(String str) {
                Loger.writeLog("TEST", str);
                if (str != null) {
                    try {
                        int indexOf = str.indexOf("ABS");
                        String substring = str.substring(indexOf, str.indexOf("input", indexOf));
                        int lastIndexOf = str.lastIndexOf("/event", indexOf);
                        Shell.setAplf(Integer.parseInt(str.substring(lastIndexOf + 6, lastIndexOf + 7)));
                        int indexOf2 = substring.indexOf("max", substring.indexOf("0035"));
                        String substring2 = substring.substring(indexOf2 + 4, substring.indexOf(",", indexOf2));
                        substring2.trim();
                        int indexOf3 = substring.indexOf("max", substring.indexOf("0036"));
                        String substring3 = substring.substring(indexOf3 + 4, substring.indexOf(",", indexOf3));
                        substring3.trim();
                        Shell.SCREEN_0035MAX = Integer.parseInt(substring2);
                        Shell.SCREEN_0036MAX = Integer.parseInt(substring3);
                        Shell.setReady(true);
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.test.InitCmd$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandResultListener {
        AnonymousClass1() {
        }

        @Override // com.oysb.utils.test.OnCommandResultListener
        public void onResult(String str) {
            Loger.writeLog("TEST", str);
            if (str != null) {
                try {
                    int indexOf = str.indexOf("ABS");
                    String substring = str.substring(indexOf, str.indexOf("input", indexOf));
                    int lastIndexOf = str.lastIndexOf("/event", indexOf);
                    Shell.setAplf(Integer.parseInt(str.substring(lastIndexOf + 6, lastIndexOf + 7)));
                    int indexOf2 = substring.indexOf("max", substring.indexOf("0035"));
                    String substring2 = substring.substring(indexOf2 + 4, substring.indexOf(",", indexOf2));
                    substring2.trim();
                    int indexOf3 = substring.indexOf("max", substring.indexOf("0036"));
                    String substring3 = substring.substring(indexOf3 + 4, substring.indexOf(",", indexOf3));
                    substring3.trim();
                    Shell.SCREEN_0035MAX = Integer.parseInt(substring2);
                    Shell.SCREEN_0036MAX = Integer.parseInt(substring3);
                    Shell.setReady(true);
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    e.printStackTrace();
                }
            }
        }
    }
}

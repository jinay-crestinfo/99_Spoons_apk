package com.oysb.utils.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class TextHelper {
    WeakReference<View> wkView;

    /* loaded from: classes2.dex */
    public enum ValueCheckType {
        Name,
        Password,
        Email,
        Phone,
        Card_sfz,
        None
    }

    public TextHelper(View view) {
        setContentView(view);
    }

    public void setContentView(View view) {
        this.wkView = new WeakReference<>(view);
    }

    private View findViewById(int i) {
        try {
            return this.wkView.get().findViewById(i);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", e);
            return null;
        }
    }

    public void setText(TextView textView, String str, String str2) {
        if (str != null) {
            try {
                if (str.isEmpty()) {
                }
                textView.setText(str);
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                return;
            }
        }
        str = str2;
        textView.setText(str);
    }

    public void setText(TextView textView, Object obj, String str, String str2) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(obj == null ? str2 : null);
            r0 = sb.toString();
        } else if (obj != null) {
            r0 = String.format(str, obj);
        }
        setText(textView, r0, str2);
    }

    public void setText(int i, String str, String str2) {
        try {
            setText((TextView) findViewById(i), str, str2);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public void setText(int i, Object obj, String str, String str2) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(obj == null ? str2 : null);
            r0 = sb.toString();
        } else if (obj != null) {
            r0 = String.format(str, obj);
        }
        setText(i, r0, str2);
    }

    public void setEditText(EditText editText, String str, String str2, boolean z) {
        if (str != null) {
            try {
                if (str.isEmpty()) {
                }
                editText.setText(str);
                editText.setSelected(z);
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                return;
            }
        }
        str = str2;
        editText.setText(str);
        editText.setSelected(z);
    }

    public void setEditText(EditText editText, Object obj, String str, String str2, boolean z) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(obj == null ? str2 : null);
            r0 = sb.toString();
        } else if (obj != null) {
            r0 = String.format(str, obj);
        }
        setEditText(editText, r0, str2, z);
    }

    public void setEditText(int i, String str, String str2, boolean z) {
        try {
            setEditText((EditText) findViewById(i), str, str2, z);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public void setEditText(int i, Object obj, String str, String str2, boolean z) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(obj == null ? str2 : null);
            r0 = sb.toString();
        } else if (obj != null) {
            r0 = String.format(str, obj);
        }
        setEditText(i, r0, str2, z);
    }

    public String getText(View view, ValueCheckType valueCheckType, String str) {
        String obj;
        if (view == null) {
            return str;
        }
        if (!(view instanceof TextView) ? !(!(view instanceof EditText) || (obj = ((EditText) view).getText().toString()) == null) : (obj = ((TextView) view).getText().toString()) != null) {
            str = obj;
        }
        int i = AnonymousClass1.$SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType[valueCheckType.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        if (str != null && !str.isEmpty()) {
                            CommonTool.isMobileNum(str);
                        }
                    } else if (i == 5 && str != null && !str.isEmpty()) {
                        CommonTool.vId(str);
                    }
                } else if (str != null && !str.isEmpty()) {
                    CommonTool.vEmail(str);
                }
            } else if (str != null) {
                str.isEmpty();
            }
        } else if (str != null) {
            str.isEmpty();
        }
        return str;
    }

    /* renamed from: com.oysb.utils.activity.TextHelper$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType;

        static {
            int[] iArr = new int[ValueCheckType.values().length];
            $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType = iArr;
            try {
                iArr[ValueCheckType.Name.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType[ValueCheckType.Password.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType[ValueCheckType.Email.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType[ValueCheckType.Phone.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$oysb$utils$activity$TextHelper$ValueCheckType[ValueCheckType.Card_sfz.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public String getText(View view, String str) {
        return getText(view, ValueCheckType.None, str);
    }

    public String getText(int i, ValueCheckType valueCheckType, String str) {
        return getText(findViewById(i), valueCheckType, str);
    }

    public String getText(int i, String str) {
        return getText(i, ValueCheckType.None, str);
    }
}

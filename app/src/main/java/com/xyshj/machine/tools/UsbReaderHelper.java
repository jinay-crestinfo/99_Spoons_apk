package com.xyshj.machine.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BFPopView;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayType;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;

/* loaded from: classes2.dex */
public class UsbReaderHelper {
    public static final String ACTION_USB_CARD_READ_TEXT = "ACTION_USB_CARD_READ_TEXT";
    public static final int MSG_USB_CARD_READ_TEXT = 6002;
    private static Handler handler = new Handler() { // from class: com.xyshj.machine.tools.UsbReaderHelper.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BFPopView findPopView;
            BFPopView findPopView2;
            int indexOf;
            super.handleMessage(message);
            try {
                if (message.what != 6002) {
                    return;
                }
                String obj = message.obj.toString();
                UsbReaderHelper.usbReadHelperEditText.setText("");
                Loger.writeLog("UI;SHJ", "USB Card Reader Text:" + obj);
                boolean z = false;
                if (obj.length() > 3 && (indexOf = obj.indexOf(obj.substring(obj.length() - 3)) + 3) < obj.length()) {
                    String substring = obj.substring(0, indexOf);
                    if (substring.equalsIgnoreCase(obj.substring(obj.length() - substring.length()))) {
                        obj = substring;
                    }
                }
                if (ShjManager.isQueryingICCardInfo()) {
                    Shj.onNeedICCardPay(2, obj);
                    return;
                }
                Intent intent = new Intent(UsbReaderHelper.ACTION_USB_CARD_READ_TEXT);
                Bundle bundle = new Bundle();
                bundle.putString("card", obj);
                intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
                ShjAppBase.sysApp.sendBroadcast(intent);
                Loger.writeLog("UI;SHJ", "通知支付 ACTION_USB_CARD_READ_TEXT");
                BFPopView findPopView3 = BFPopView.findPopView("MainFragment", "GoodsDetailPopView");
                if (findPopView3 != null && findPopView3.isShowing()) {
                    Shj.onNeedICCardPay(1, obj);
                    z = true;
                }
                if (!z && (findPopView2 = BFPopView.findPopView("MainFragment", "PayPopView")) != null && findPopView2.isShowing()) {
                    Shj.onNeedICCardPay(1, obj);
                    z = true;
                }
                if (z || (findPopView = BFPopView.findPopView("MainActivity", "PopView_PaySummary_st")) == null || !findPopView.isShowing()) {
                    return;
                }
                Loger.writeLog("UI;SHJ", "PopView_PaySummary_st 当前是打开状态，可以发起支付");
                Shj.onNeedICCardPay(1, obj);
            } catch (Exception unused) {
            }
        }
    };
    static EditText usbReadHelperEditText;

    /* renamed from: com.xyshj.machine.tools.UsbReaderHelper$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BFPopView findPopView;
            BFPopView findPopView2;
            int indexOf;
            super.handleMessage(message);
            try {
                if (message.what != 6002) {
                    return;
                }
                String obj = message.obj.toString();
                UsbReaderHelper.usbReadHelperEditText.setText("");
                Loger.writeLog("UI;SHJ", "USB Card Reader Text:" + obj);
                boolean z = false;
                if (obj.length() > 3 && (indexOf = obj.indexOf(obj.substring(obj.length() - 3)) + 3) < obj.length()) {
                    String substring = obj.substring(0, indexOf);
                    if (substring.equalsIgnoreCase(obj.substring(obj.length() - substring.length()))) {
                        obj = substring;
                    }
                }
                if (ShjManager.isQueryingICCardInfo()) {
                    Shj.onNeedICCardPay(2, obj);
                    return;
                }
                Intent intent = new Intent(UsbReaderHelper.ACTION_USB_CARD_READ_TEXT);
                Bundle bundle = new Bundle();
                bundle.putString("card", obj);
                intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
                ShjAppBase.sysApp.sendBroadcast(intent);
                Loger.writeLog("UI;SHJ", "通知支付 ACTION_USB_CARD_READ_TEXT");
                BFPopView findPopView3 = BFPopView.findPopView("MainFragment", "GoodsDetailPopView");
                if (findPopView3 != null && findPopView3.isShowing()) {
                    Shj.onNeedICCardPay(1, obj);
                    z = true;
                }
                if (!z && (findPopView2 = BFPopView.findPopView("MainFragment", "PayPopView")) != null && findPopView2.isShowing()) {
                    Shj.onNeedICCardPay(1, obj);
                    z = true;
                }
                if (z || (findPopView = BFPopView.findPopView("MainActivity", "PopView_PaySummary_st")) == null || !findPopView.isShowing()) {
                    return;
                }
                Loger.writeLog("UI;SHJ", "PopView_PaySummary_st 当前是打开状态，可以发起支付");
                Shj.onNeedICCardPay(1, obj);
            } catch (Exception unused) {
            }
        }
    }

    public static void tryReadUsbICCardInfo(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("SDKVERSION : ");
        sb.append(Build.VERSION.SDK_INT >= 21);
        sb.append(" hasICCard:");
        sb.append(ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard));
        Loger.writeLog("SHJ", sb.toString());
        if (Build.VERSION.SDK_INT >= 21) {
            if (ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard) || ShjAppHelper.getScanPay()) {
                endableUsbReadHelperEditText(Boolean.valueOf(z));
            }
        }
    }

    public static void attachUseReadEditText(Activity activity) {
        try {
            EditText editText = new EditText(activity);
            usbReadHelperEditText = editText;
            editText.setEnabled(false);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(100, -2);
            usbReadHelperEditText.setLeft(-200);
            usbReadHelperEditText.setLayoutParams(layoutParams);
            usbReadHelperEditText.setFocusableInTouchMode(false);
            if (Build.VERSION.SDK_INT >= 21) {
                usbReadHelperEditText.setShowSoftInputOnFocus(false);
            }
            ((ViewGroup) activity.findViewById(1000)).addView(usbReadHelperEditText);
            usbReadHelperEditText.addTextChangedListener(new TextWatcher() { // from class: com.xyshj.machine.tools.UsbReaderHelper.2
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                AnonymousClass2() {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    UsbReaderHelper.handler.removeMessages(UsbReaderHelper.MSG_USB_CARD_READ_TEXT);
                    String obj = UsbReaderHelper.usbReadHelperEditText.getText().toString();
                    if (obj.length() > 0) {
                        Message obtain = Message.obtain();
                        obtain.what = UsbReaderHelper.MSG_USB_CARD_READ_TEXT;
                        obtain.obj = obj;
                        UsbReaderHelper.handler.sendMessageDelayed(obtain, 500L);
                    }
                }
            });
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.tools.UsbReaderHelper$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass2() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            UsbReaderHelper.handler.removeMessages(UsbReaderHelper.MSG_USB_CARD_READ_TEXT);
            String obj = UsbReaderHelper.usbReadHelperEditText.getText().toString();
            if (obj.length() > 0) {
                Message obtain = Message.obtain();
                obtain.what = UsbReaderHelper.MSG_USB_CARD_READ_TEXT;
                obtain.obj = obj;
                UsbReaderHelper.handler.sendMessageDelayed(obtain, 500L);
            }
        }
    }

    public static void attachUseReadEditText(EditText editText) {
        try {
            usbReadHelperEditText = editText;
            editText.setEnabled(false);
            usbReadHelperEditText.setFocusableInTouchMode(false);
            if (Build.VERSION.SDK_INT >= 21) {
                usbReadHelperEditText.setShowSoftInputOnFocus(false);
            }
            usbReadHelperEditText.addTextChangedListener(new TextWatcher() { // from class: com.xyshj.machine.tools.UsbReaderHelper.3
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                AnonymousClass3() {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    UsbReaderHelper.handler.removeMessages(UsbReaderHelper.MSG_USB_CARD_READ_TEXT);
                    String obj = UsbReaderHelper.usbReadHelperEditText.getText().toString();
                    if (obj.length() > 0) {
                        Message obtain = Message.obtain();
                        obtain.what = UsbReaderHelper.MSG_USB_CARD_READ_TEXT;
                        obtain.obj = obj;
                        UsbReaderHelper.handler.sendMessageDelayed(obtain, 500L);
                    }
                }
            });
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.tools.UsbReaderHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass3() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            UsbReaderHelper.handler.removeMessages(UsbReaderHelper.MSG_USB_CARD_READ_TEXT);
            String obj = UsbReaderHelper.usbReadHelperEditText.getText().toString();
            if (obj.length() > 0) {
                Message obtain = Message.obtain();
                obtain.what = UsbReaderHelper.MSG_USB_CARD_READ_TEXT;
                obtain.obj = obj;
                UsbReaderHelper.handler.sendMessageDelayed(obtain, 500L);
            }
        }
    }

    public static void endableUsbReadHelperEditText(Boolean bool) {
        Loger.writeLog("SHJ", "usb read enable:" + bool);
        try {
            EditText editText = usbReadHelperEditText;
            if (editText == null) {
                return;
            }
            editText.setText("");
            usbReadHelperEditText.setEnabled(bool.booleanValue());
            if (bool.booleanValue()) {
                usbReadHelperEditText.setFocusable(true);
                usbReadHelperEditText.requestFocus();
            }
        } catch (Exception unused) {
        }
    }
}

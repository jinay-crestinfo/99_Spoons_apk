package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
//import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class Report_OnlinePay_Apply extends Report {
    private static final long serialVersionUID = 1;
    int payType;
    int type;

    public Report_OnlinePay_Apply() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 48;
        this.lostAble = true;
        this.reSendCount = 0;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(int i, int i2, String str, String str2, String str3, String str4, int i3, String str5, String str6) {
        this.type = i;
        this.payType = i2;
        if (i == 1) {
//            this.data = ("" + i + VoiceWakeuperAidl.PARAMS_SEPARATE + i2 + VoiceWakeuperAidl.PARAMS_SEPARATE + str + VoiceWakeuperAidl.PARAMS_SEPARATE + str2 + VoiceWakeuperAidl.PARAMS_SEPARATE + str3 + VoiceWakeuperAidl.PARAMS_SEPARATE + str4 + VoiceWakeuperAidl.PARAMS_SEPARATE + i3 + VoiceWakeuperAidl.PARAMS_SEPARATE + str5 + VoiceWakeuperAidl.PARAMS_SEPARATE + str6 + "").getBytes();
            this.lostAble = true;
            this.mastReConnectOnTimeOut = false;
            return;
        }
        if (i == 2) {
//            this.data = ("" + i + VoiceWakeuperAidl.PARAMS_SEPARATE + i2 + VoiceWakeuperAidl.PARAMS_SEPARATE + str5 + VoiceWakeuperAidl.PARAMS_SEPARATE + str6 + VoiceWakeuperAidl.PARAMS_SEPARATE + str2 + VoiceWakeuperAidl.PARAMS_SEPARATE).getBytes();
            this.lostAble = true;
            this.mastReConnectOnTimeOut = false;
            return;
        }
        if (i == 3) {
//            this.data = ("" + i + VoiceWakeuperAidl.PARAMS_SEPARATE + i2 + VoiceWakeuperAidl.PARAMS_SEPARATE + str5 + VoiceWakeuperAidl.PARAMS_SEPARATE + str + VoiceWakeuperAidl.PARAMS_SEPARATE + str6 + VoiceWakeuperAidl.PARAMS_SEPARATE).getBytes();
            this.lostAble = false;
            this.mastReConnectOnTimeOut = true;
        }
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        if (ObjectHelper.intFromBytes(bArr, 1, 1) != ((short) (this.serialNumber & UByte.MAX_VALUE))) {
            return false;
        }
        int length = bArr.length - ((bArr[0] & UByte.MAX_VALUE) == 29 ? 4 : 5);
        byte[] bArr2 = new byte[length];
        ObjectHelper.updateBytes(bArr2, bArr, (bArr[0] & UByte.MAX_VALUE) == 29 ? 3 : 4, 0, length);
        String str = new String(bArr2, "UTF-8");
        Loger.writeLog("NET", "Report_OnlinePay_Apply 结果:" + str);
        NetManager.getOnlinePayApplyResultListener().onPayResult(this.type, this.payType, str);
        return true;
    }
}

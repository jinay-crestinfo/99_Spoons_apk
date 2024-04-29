package com.oysb.xy.net.report;
//
//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
//import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class Report_OnlinePay_OnlineCard_v2 extends Report {
    private static final long serialVersionUID = 1;
    String type = "1";

    public Report_OnlinePay_OnlineCard_v2() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 64;
        this.lostAble = true;
        this.reSendCount = 2;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(int i, String str, int i2, String str2, String str3, String str4, int i3, String str5, String str6) {
        this.type = i2 + "";
//        this.data = (i2 + ";NA;" + str2 + VoiceWakeuperAidl.PARAMS_SEPARATE + str3 + VoiceWakeuperAidl.PARAMS_SEPARATE + str4 + VoiceWakeuperAidl.PARAMS_SEPARATE + i3 + VoiceWakeuperAidl.PARAMS_SEPARATE + str5 + VoiceWakeuperAidl.PARAMS_SEPARATE + i + VoiceWakeuperAidl.PARAMS_SEPARATE + str + VoiceWakeuperAidl.PARAMS_SEPARATE + str6 + VoiceWakeuperAidl.PARAMS_SEPARATE).getBytes();
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
            if (ObjectHelper.intFromBytes(bArr, 1, 1) != ((short) (this.serialNumber & UByte.MAX_VALUE))) {
                return false;
            }
            boolean acceptAck = super.acceptAck(bArr);
            Loger.writeLog("NET", "Report_OnlinePay_OnlineCard_v2 " + ObjectHelper.hex2String(bArr, bArr.length));
            Loger.writeLog("NET", "reCode:" + getRetCode() + " MSG:" + getRetMsg());
            NetManager.getOnlineCardPayResultListener().onPayResult(this.type, acceptAck, getRetMsg());
            return true;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return false;
        }
    }
}

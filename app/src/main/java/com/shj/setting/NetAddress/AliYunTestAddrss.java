package com.shj.setting.NetAddress;

/* loaded from: classes2.dex */
public class AliYunTestAddrss extends DefaultAddrss {
    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getAppBaseUrl() {
        return "http://gateway.xyvend.cn:8090";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getDebugServerPort() {
        return "7088";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getJQConnectUrl() {
        return "http://gateway.xyvend.cn:8081/mGatewayService/authenticate";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQueryAppUrl() {
        return "http://app.xyvend.cn:9092/machinepush/appDetails/queryAppUrl";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getSPImgUrl() {
        return "http://wc.xyvend.cn:8086/spImg/";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getWebBaseUrl() {
        return "http://gateway.xyvend.cn:8090";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getWinxinAuthInfoUrl() {
        return "http://www.xyzfpt.com/api/wx_xy_fws/getWxpayFaceAuthInfo.php";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getWinxinFacepayUrl() {
        return "http://www.xyzfpt.com/api/wx_xy_fws/facepay.php";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getZfbPayInitUrl() {
        return getAppBaseUrl() + "/service-pay/pay/smile/init";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getZfbPayUrl() {
        return getAppBaseUrl() + "/service-pay/pay/smile/pay";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getFwsZfbPayInitUrl() {
        return getAppBaseUrl() + "/service-pay/pay/fws/smile/init";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getFwsZfbPayUrl() {
        return getAppBaseUrl() + "/service-pay/pay/fws/smile/pay";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getGoodsInfoUrl() {
        return getAppBaseUrl() + "/service-app/shjty/getMachineAllGood";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getPrintTemplateUrl() {
        return getAppBaseUrl() + "/service-machine/jqxppz/printTemplate";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getKFPhoneUrl() {
        return getAppBaseUrl() + "/service-app/shjapp/contactlists";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQuerySoftwareInfo() {
        return getAppBaseUrl() + "/service-machine/soft/querySoftwareInfoByMachineCode";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getShoppingReportResultUrl() {
        return getAppBaseUrl() + "/service-app/shjapp/reportResult";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getDoConfigInfoUrl() {
        return getAppBaseUrl() + "/service-machine/djpz/doConfigInfo";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQueryPayResult() {
        return getAppBaseUrl() + "/service-pay/pay/notify/tlt?out_trade_no";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getSinoKangGoodsQueryUrl() {
        return getAppBaseUrl() + "/service-goods/sinoKangapp/sinoKangGoodsQuery";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getJudgeMachineUrl() {
        return getAppBaseUrl() + "/service-machine/machine/judgeMachine";
    }
}

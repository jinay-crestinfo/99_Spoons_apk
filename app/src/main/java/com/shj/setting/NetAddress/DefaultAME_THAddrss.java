package com.shj.setting.NetAddress;

/* loaded from: classes2.dex */
public class DefaultAME_THAddrss extends DefaultAddrss {
    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getAppBaseUrl() {
        return "http://39.108.150.226:8090";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getDebugServerPort() {
        return "7088";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getGoodsInfoUrl() {
        return "http://203.170.193.181/api/machine/getMachineAllGood";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getJQConnectUrl() {
        return "http://39.108.150.226:8085/mGatewayService/authenticate";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getJudgeMachineUrl() {
        return "";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getKFPhoneUrl() {
        return "http://203.170.193.181/api/machine/queryServicelnfo";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQueryAppUrl() {
        return "http://app.xyvend.cn:9092/machinepush/appDetails/queryAppUrl";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQuerySoftwareInfo() {
        return "http://www.xynetweb.cn:8090/service-machine/soft/querySoftwareInfoByMachineCode";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getReportCashUrl() {
        return "http://203.170.193.181/api/machine/reportCash";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getReportErrorUrl() {
        return "http://203.170.193.181/api/machine/reportError";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getSPImgUrl() {
        return "http://39.108.150.226:8086/spImg/";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getSinoKangGoodsQueryUrl() {
        return "http://39.108.150.226:8090/service-goods/sinoKangapp/sinoKangGoodsQuery";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getWebBaseUrl() {
        return "http://39.108.150.226:8090";
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
    public String payWithCodeUrl() {
        return "http://203.170.193.181/api/machine/payWithCode";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String queryDiscountsUrl() {
        return "http://203.170.193.181/api/machine/queryDiscounts";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String queryQrcodeResultUrl() {
        return "http://203.170.193.181/api/machine/queryQrcodeResult";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String queryQrcodeUrl() {
        return "http://203.170.193.181/api/machine/queryQrcode";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String queryVoucherUsableUrl() {
        return "http://203.170.193.181/api/machine/queryVoucherUsable";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String reportMachineStatusUrl() {
        return "http://203.170.193.181/api/machine/reportMachineStatus";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String reportOfferResultUrl() {
        return "http://203.170.193.181/api/machine/reportOfferResult";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String synServerUrl() {
        return "http://203.170.193.181/api/machine/synServer";
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
    public String getPrintTemplateUrl() {
        return getAppBaseUrl() + "/service-machine/jqxppz/printTemplate";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getShoppingReportResultUrl() {
        return getAppBaseUrl() + "/service-app/shjapp/reportResult";
    }
}

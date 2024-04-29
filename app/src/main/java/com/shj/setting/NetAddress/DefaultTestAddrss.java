package com.shj.setting.NetAddress;

/* loaded from: classes2.dex */
public class DefaultTestAddrss extends DefaultAddrss {
    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getAppBaseUrl() {
        return "http://42.193.149.63:8090";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQueryPayResult() {
        return "http://175.6.72.188:8090/service-pay/pay/notify/tlt?out_trade_no";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getSPImgUrl() {
        return "http://42.193.149.63:8086/spImg/";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getWebBaseUrl() {
        return "http://42.193.149.63:8090";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getJQConnectUrl() {
        return getAppBaseUrl() + "/mmygw-1/mGatewayService/authenticate";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getQuerySoftwareInfo() {
        return getAppBaseUrl() + "/service-machine/soft/querySoftwareInfoByMachineCode";
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
    public String getDoConfigInfoUrl() {
        return getAppBaseUrl() + "/service-machine/djpz/doConfigInfo";
    }

    @Override // com.shj.setting.NetAddress.DefaultAddrss, com.shj.setting.NetAddress.IAddress
    public String getGoodsInfoUrl() {
        return getAppBaseUrl() + "/service-app/shjty/getMachineAllGood";
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

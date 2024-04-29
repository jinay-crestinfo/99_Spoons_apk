package com.shj.setting.NetAddress;

/* loaded from: classes2.dex */
public class NetAddress {
    public static IAddress iAddress = new DefaultAMEAddrss();

    public static void setIAddress(IAddress iAddress2) {
        iAddress = iAddress2;
    }

    public static IAddress getiAddress() {
        return iAddress;
    }

    public static String getAppBaseUrl() {
        return iAddress.getAppBaseUrl();
    }

    public static String getWebBaseUrl() {
        return iAddress.getWebBaseUrl();
    }

    public static String getZfbPayInitUrl() {
        return iAddress.getZfbPayInitUrl();
    }

    public static String getZfbPayUrl() {
        return iAddress.getZfbPayUrl();
    }

    public static String getFwsZfbPayInitUrl() {
        return iAddress.getFwsZfbPayInitUrl();
    }

    public static String getFwsZfbPayUrl() {
        return iAddress.getFwsZfbPayUrl();
    }

    public static String getWinxinAuthInfoUrl() {
        return iAddress.getWinxinAuthInfoUrl();
    }

    public static String getWinxinFacepayUrl() {
        return iAddress.getWinxinFacepayUrl();
    }

    public static String getGoodsInfoUrl() {
        return iAddress.getGoodsInfoUrl();
    }

    public static String getDebugServerPort() {
        return iAddress.getDebugServerPort();
    }

    public static String getJQConnectUrl() {
        return iAddress.getJQConnectUrl();
    }

    public static String getPrintTemplateUrl() {
        return iAddress.getPrintTemplateUrl();
    }

    public static String getKFPhoneUrl() {
        return iAddress.getKFPhoneUrl();
    }

    public static String getQueryAppUrl() {
        return iAddress.getQueryAppUrl();
    }

    public static String getQuerySoftwareInfo() {
        return iAddress.getQuerySoftwareInfo();
    }

    public static String getShoppingReportResultUrl() {
        return iAddress.getShoppingReportResultUrl();
    }

    public static String getSPImgUrl() {
        return iAddress.getSPImgUrl();
    }

    public static String getDoConfigInfoUrl() {
        return iAddress.getDoConfigInfoUrl();
    }

    public static String getQueryPayResult() {
        return iAddress.getQueryPayResult();
    }

    public static String getSinoKangGoodsQueryUrl() {
        return iAddress.getSinoKangGoodsQueryUrl();
    }

    public static String getSaveMachineStockInfo() {
        return iAddress.getAppBaseUrl() + "/service-machine/shjgl/saveMachineStockInfo";
    }

    public static String getBindMachineUrl() {
        return iAddress.getAppBaseUrl() + "/service-api/api/bindMachine";
    }

    public static String getOperationInfoUrl() {
        return iAddress.getAppBaseUrl() + "/service-machine/shjgl/saveOperationInfo";
    }

    public static String getMachineKcslUrl() {
        return iAddress.getAppBaseUrl() + "/service-goods/sinoKangapp/getMachineKcsl";
    }

    public static String getMachineDispatchListQueryUrl() {
        return iAddress.getAppBaseUrl() + "/service-goods/sinoKangapp/machineDispatchListQuery";
    }

    public static String getUploadReplenishmentRecordUrl() {
        return iAddress.getAppBaseUrl() + "/service-goods/sinoKangapp/uploadReplenishmentRecord";
    }

    public static String getDrugsBatchUrl() {
        return iAddress.getAppBaseUrl() + "/service-goods/sinoKangapp/getDrugsBatch";
    }

    public static String getJudgeMachineUrl() {
        return iAddress.getJudgeMachineUrl();
    }

    public static String getQueryAdformat() {
        return iAddress.getAppBaseUrl() + "/service-job/ad/queryAdformat";
    }

    public static String getWcgjAppUrl() {
        return iAddress.getWcgjAppUrl();
    }

    public static String queryVoucherUsableUrl() {
        return iAddress.queryVoucherUsableUrl();
    }

    public static String queryDiscountsUrl() {
        return iAddress.queryDiscountsUrl();
    }

    public static String reportOfferResultUrl() {
        return iAddress.reportOfferResultUrl();
    }

    public static String reportMachineStatusUrl() {
        return iAddress.reportMachineStatusUrl();
    }

    public static String payWithCodeUrl() {
        return iAddress.payWithCodeUrl();
    }

    public static String queryQrcodeUrl() {
        return iAddress.queryQrcodeUrl();
    }

    public static String queryQrcodeResultUrl() {
        return iAddress.queryQrcodeResultUrl();
    }

    public static String synServerUrl() {
        return iAddress.synServerUrl();
    }

    public static String queryHdqsxxUrl() {
        return iAddress.queryHdtsxxUrl();
    }

    public static String getOrderGoodsByMachineIdUrl() {
        return iAddress.getOrderGoodsByMachineIdUrl();
    }

    public static String getReportErrorUrl() {
        return iAddress.getReportErrorUrl();
    }

    public static String getReportCashUrl() {
        return iAddress.getReportCashUrl();
    }
}

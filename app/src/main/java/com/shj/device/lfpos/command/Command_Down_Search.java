package com.shj.device.lfpos.command;

import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import com.oysb.utils.anno.XYClass;
import com.shj.device.lfpos.LfPos;
import org.slf4j.Marker;

@XYClass(KEY = "HEAD", VALUE = "69")
/* loaded from: classes2.dex */
public class Command_Down_Search extends Command {
    String aid;
    String atc;
    String bkyzs;
    String card;
    String cardDate;
    String ckh;
    String ckryzjg;
    String czy;
    String czyh;
    String dzqmxx;
    String fkhbsm;
    String fkhyysj;
    String gjxykgsdm;
    String ickjyzs;
    String kckrje;
    String kpxlh;
    String kth;
    String money;
    String name_en;
    String name_zh;
    String posbsm;
    String posid;
    String qsrq;
    String remark;
    String sdhbsm;
    String sftjjy;
    String sqm;
    String srms;
    String sumMoney;
    String sylsh;
    String tac;
    String tdxxy;
    String totalMoney;
    String tradPzh;
    String tradSn;
    String tradTime;
    String tsi;
    String tvr;
    String type;
    String user;
    String xfMoney;
    String yjylx;
    String yybq;
    String yyjhtz;
    String yyssmc;
    String zdxl;

    @Override // com.shj.device.lfpos.command.Command
    public boolean init(byte[] bArr) {
        boolean init = super.init(bArr);
        try {
            String[] split = new String(this.CONT, StringUtils.GB2312).split(new String(FS));
            setTradType(split[0]);
            this.user = split[1];
            this.posid = split[2];
            this.name_zh = split[3];
            this.name_en = split[4];
            this.sdhbsm = split[5];
            this.fkhbsm = split[6];
            this.posbsm = split[7];
            this.card = split[8];
            this.czy = split[9];
            this.yjylx = split[10];
            this.cardDate = split[11];
            this.tradSn = split[12];
            this.tradPzh = split[13];
            this.tradTime = split[14];
            this.sqm = split[15];
            this.ckh = split[16];
            this.money = split[17];
            this.xfMoney = split[18];
            this.sumMoney = split[19];
            this.totalMoney = split[20];
            this.gjxykgsdm = split[21];
            this.ickjyzs = split[22];
            this.tvr = split[23];
            this.tsi = split[24];
            this.aid = split[25];
            this.atc = split[26];
            this.yybq = split[27];
            this.yyssmc = split[28];
            this.tac = split[29];
            this.kckrje = split[30];
            this.sftjjy = split[31];
            this.srms = split[32];
            this.remark = split[33];
            this.tdxxy = split[34];
            this.kth = split[35];
            this.czyh = split[36];
            this.sylsh = split[37];
            this.qsrq = split[38];
            this.bkyzs = split[39];
            this.yyjhtz = split[40];
            this.zdxl = split[41];
            this.fkhyysj = split[42];
            this.kpxlh = split[43];
            this.ckryzjg = split[44];
            this.dzqmxx = split[45];
        } catch (Exception unused) {
        }
        Loger.writeLog("LFPOS", "Command_Down_Search tradType:" + getTradType() + " card:" + this.card);
        return init;
    }

    @Override // com.shj.device.lfpos.command.Command
    public void doCommand() {
        LfPos.cmd_onSearchedTradInfo(this.card.replace(Marker.ANY_MARKER, "#"));
        super.doCommand();
    }
}

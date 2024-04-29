package com.shj;

import com.oysb.utils.CommonTool;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class ShelfInfo {
    private Integer shelf;
    private ShelfType shelfType;
    private String groupName = "";
    private String goodsCode = "0";
    private String goodsName = "";
    private String goodsImage = "";
    private String goodsbatchnumber = "";
    private Integer goodsCount = 0;
    private String goodcount_settime = "";
    private int lastGoodsSetCount = 0;
    private Integer capacity = 10;
    private Integer price = 0;
    private String goodprice_settime = "";
    private Integer status = 0;
    private Integer doorStatus = 2;
    private Integer gdjc = -1;
    private Integer jgh = -1;
    private Integer layer = -1;
    private boolean isPickOnly = false;
    private boolean isStopSaleByServer = false;
    private HashMap<String, Object> datas = new HashMap<>();
    private int ready2OfferCount = 0;

    public ShelfType getShelfType() {
        return this.shelfType;
    }

    public void setShelfType(ShelfType shelfType) {
        this.shelfType = shelfType;
    }

    public String getGoodsCode() {
        if (!this.goodsCode.equals("-1") && !this.goodsCode.equals("0") && this.goodsCode.length() < 4 && Shj.isStoreGoodsInfoInVMC()) {
            String str = "0000" + this.goodsCode;
            this.goodsCode = str;
            this.goodsCode = str.substring(str.length() - 4);
        }
        return this.goodsCode;
    }

    public void setGoodsCode(String str) {
        if (!str.equals("-1") && !str.equals("0") && str.length() < 4 && Shj.isStoreGoodsInfoInVMC()) {
            String str2 = "0000" + str;
            str = str2.substring(str2.length() - 4);
        }
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_goodscode, "" + str);
        }
        this.goodsCode = str;
    }

    public Integer getGoodsCount() {
        return this.goodsCount;
    }

    public void setGoodsCount(Integer num) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_cur_count, "" + num);
        }
        this.goodsCount = num;
        if (num.intValue() > 0 && this.status.intValue() == 2) {
            this.status = 0;
        } else {
            if (num.intValue() > 0 || this.status.intValue() != 0) {
                return;
            }
            this.status = 2;
        }
    }

    public Integer getShelf() {
        return this.shelf;
    }

    public void setShelf(Integer num) {
        this.shelf = num;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Integer num) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_max_count, "" + num);
        }
        this.capacity = num;
        if (1 == num.intValue()) {
            this.shelfType = ShelfType.Box;
        }
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer num) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_price, "" + num);
        }
        this.price = num;
    }

    public boolean isStatusOK() {
        return this.status.intValue() == 0 || this.status.intValue() == 5;
    }

    public boolean isTempCheckErrorButNeedStopOfferGoods() {
        return this.status.intValue() == 2 || this.status.intValue() == 4;
    }

    public Integer getStatus() {
        return this.status;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0016, code lost:
    
        if (r1 != 4) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Integer getStatus2Server() {
        /*
            r5 = this;
            java.lang.Integer r0 = r5.status
            int r0 = r0.intValue()
            java.lang.Integer r1 = r5.status
            int r1 = r1.intValue()
            r2 = 2
            r3 = 1
            if (r1 == r3) goto L1d
            if (r1 == r2) goto L1b
            r4 = 3
            if (r1 == r4) goto L19
            r3 = 4
            if (r1 == r3) goto L1d
            goto L1e
        L19:
            r0 = 1
            goto L1e
        L1b:
            r0 = 0
            goto L1e
        L1d:
            r0 = 2
        L1e:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.ShelfInfo.getStatus2Server():java.lang.Integer");
    }

    public Integer getStatus2OfferResult() {
        int intValue = this.status.intValue();
        int intValue2 = this.status.intValue();
        if (intValue2 == 1) {
            intValue = OfferState.EngineError.getIndex();
        } else if (intValue2 == 2) {
            intValue = OfferState.ShelfNoGoods.getIndex();
        } else if (intValue2 == 3) {
            intValue = OfferState.Blocked.getIndex();
        } else if (intValue2 == 4) {
            intValue = OfferState.ShelfOfferGoodsPaused.getIndex();
        } else if (intValue2 == 5) {
            intValue = OfferState.WBLWaitting2PickGoods.getIndex();
        }
        return Integer.valueOf(intValue);
    }

    public void setStatus(Integer num) {
        this.status = num;
    }

    public String getStatusInfo() {
        String str = "";
        try {
            String language = CommonTool.getLanguage(Shj.getContext());
            int intValue = this.status.intValue();
            String str2 = "błąd autokontrolny windy";
            String str3 = "Elevator self-check error";
            switch (intValue) {
                case 0:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        if (!language.equalsIgnoreCase("es")) {
                                            str = "货道正常";
                                            break;
                                        } else {
                                            str = "CARRIL NORMAL";
                                            break;
                                        }
                                    } else {
                                        str = "컬럼 정상";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "W Normie";
                                break;
                            }
                        } else {
                            str = "Sélection disponible";
                            break;
                        }
                    } else {
                        str = "Selection normal";
                        break;
                    }
                case 1:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道故障";
                                        break;
                                    } else {
                                        str = "컬럼 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Błąd wyboru";
                                break;
                            }
                        } else {
                            str = "Erreur de sélection";
                            break;
                        }
                    } else {
                        str = "selection error";
                        break;
                    }
                case 2:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        if (!language.equalsIgnoreCase("es")) {
                                            str = "货道缺货";
                                            break;
                                        } else {
                                            break;
                                        }
                                    } else {
                                        str = "컬럼 재고부족";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Brak produktu";
                                break;
                            }
                        } else {
                            str = "Sélection en ruputre de stock";
                            break;
                        }
                    } else {
                        str = "Selection out of stock";
                        break;
                    }
                case 3:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道卡货";
                                        break;
                                    } else {
                                        str = "컬럼 끼임";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Wydanie Zablokowane";
                                break;
                            }
                        } else {
                            str = "Sélection coincée";
                            break;
                        }
                    } else {
                        str = "Selection jamed";
                        break;
                    }
                case 4:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道暂停使用";
                                        break;
                                    } else {
                                        str = "컬럼 사용 일시정지 ";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Wybór zawieszony";
                                break;
                            }
                        } else {
                            str = "Produits non encore récupérés";
                            break;
                        }
                    } else {
                        str = "Selection pause to use";
                        break;
                    }
                case 5:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "取货口商品未取出";
                                        break;
                                    } else {
                                        str = "상품이 아직 인출하지 않았습니다.";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Odbierz zakup";
                                break;
                            }
                        } else {
                            str = "Trappe de récupération ouverte";
                            break;
                        }
                    } else {
                        str = "Goods in pick up";
                        break;
                    }
                case 6:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "取货口门没关上";
                                        break;
                                    } else {
                                        str = "문이 안 닫혔습니다.";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "drzwi odbioru nie zamknięte";
                                break;
                            }
                        } else {
                            str = "Erreur de fermeture porte réceptacle";
                            break;
                        }
                    } else {
                        str = "Pick up door didn't close";
                        break;
                    }
                case 7:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "升降机故障";
                                        break;
                                    } else {
                                        str = "엘리베이터 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = str2;
                                break;
                            }
                        } else {
                            str = "Erreur ascenseur";
                            break;
                        }
                    } else {
                        str = str3;
                        break;
                    }
                case 8:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "升降机自检错误";
                                        break;
                                    } else {
                                        str = "엘리베이터 자기진단- 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = str2;
                                break;
                            }
                        } else {
                            str = "Erreur Auto-test ascenseur";
                            break;
                        }
                    } else {
                        str = str3;
                        break;
                    }
                case 9:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "前门关闭错误";
                                        break;
                                    } else {
                                        str = "앞 도어 닫힘 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Błąd zamknięcia drzwi przednich";
                                break;
                            }
                        } else {
                            str = "Erreur fermeture de porte";
                            break;
                        }
                    } else {
                        str = "Front door close error";
                        break;
                    }
                default:
                    str2 = "Błąd zamknięcia drzwi tylnych";
                    str3 = "Backdoor close error";
                    switch (intValue) {
                        case 16:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "后门打开错误";
                                                break;
                                            } else {
                                                str = "뒤 문 닫힘 오류";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = str2;
                                        break;
                                    }
                                } else {
                                    str = "Erreur ouverture porte sas intérieur";
                                    break;
                                }
                            } else {
                                str = str3;
                                break;
                            }
                        case 17:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "后门关闭错误";
                                                break;
                                            }
                                            str = "뒤 문 닫힘 오류";
                                            break;
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    }
                                    str = str2;
                                    break;
                                } else {
                                    str = "Erreur fermeture porte sas intérieur";
                                    break;
                                }
                            }
                            str = str3;
                            break;
                        case 18:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "没检测到盒饭";
                                                break;
                                            } else {
                                                str = "음식 감지 안됨 ";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Nie wykryto posiłku";
                                        break;
                                    }
                                } else {
                                    str = "Aucun plat détecté";
                                    break;
                                }
                            } else {
                                str = "No meal detecting";
                                break;
                            }
                        case 19:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "盒饭正在加热";
                                                break;
                                            } else {
                                                str = "음식 가열 중";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Podgrzewam Posiłek";
                                        break;
                                    }
                                } else {
                                    str = "Réchauffement en cours";
                                    break;
                                }
                            } else {
                                str = "Heating the meal";
                                break;
                            }
                        case 20:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "前门打开错误";
                                                break;
                                            } else {
                                                str = "앞 문 열림 오류";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Błąd otwarcia drzwi przednich";
                                        break;
                                    }
                                } else {
                                    str = "Erreur ouverture de porte";
                                    break;
                                }
                            } else {
                                str = "Front Door Open error";
                                break;
                            }
                        case 21:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "请取出微波炉内盒饭";
                                                break;
                                            } else {
                                                str = "전자레인지에서  음식 반출";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Wyciągnij wszelkie metalowe objekty z mikrofalówki";
                                        break;
                                    }
                                } else {
                                    str = "Sortir le plat du micro onde";
                                    break;
                                }
                            } else {
                                str = "Take out meal from Microwave Oven";
                                break;
                            }
                        case 22:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "撑杆回位错误";
                                                break;
                                            } else {
                                                str = "버팀대 위치 오류";
                                                break;
                                            }
                                        }
                                        str = "Normal";
                                        break;
                                    } else {
                                        str = "Błąd pozycji";
                                        break;
                                    }
                                } else {
                                    str = "Erreur  électro piston";
                                    break;
                                }
                            } else {
                                str = "Brace position error";
                                break;
                            }
                    }
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public Object getDatas(String str) {
        if (this.datas.containsKey(str)) {
            return this.datas.get(str);
        }
        return null;
    }

    public void setDatas(String str, Object obj) {
        this.datas.put(str, obj);
    }

    public Integer getGdjc() {
        return this.gdjc;
    }

    public void setGdjc(Integer num) {
        this.gdjc = num;
    }

    public Integer getDoorStatus() {
        return this.doorStatus;
    }

    public void setDoorStatus(Integer num) {
        this.doorStatus = num;
    }

    public Integer getJgh() {
        return this.jgh;
    }

    public void setJgh(Integer num) {
        this.jgh = num;
    }

    public Integer getLayer() {
        return this.layer;
    }

    public void setLayer(Integer num) {
        this.layer = num;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        if (Shj.getShelfInfo(this.shelf) != null && Shj.isResetFinished() && !this.goodsCode.equalsIgnoreCase(str)) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_goods_name, str);
        }
        this.goodsName = str;
    }

    public String getGoodsImage() {
        return this.goodsImage;
    }

    public void setGoodsImage(String str) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_goods_image, str);
        }
        this.goodsImage = str;
    }

    public boolean isPickOnly() {
        return this.isPickOnly;
    }

    public void setPickOnly(boolean z) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_pickonly, z ? "TRUE" : "FALSE");
        }
        this.isPickOnly = z;
    }

    public String getGoodsbatchnumber() {
        return this.goodsbatchnumber;
    }

    public void setGoodsbatchnumber(String str) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_goodsbatchnumber, str);
        }
        this.goodsbatchnumber = str;
    }

    public String getGoodcount_settime() {
        return this.goodcount_settime;
    }

    public void setGoodcount_settime(String str) {
        this.goodcount_settime = str;
    }

    public int getLastGoodsSetCount() {
        return this.lastGoodsSetCount;
    }

    public void setLastGoodsSetCount(int i) {
        this.lastGoodsSetCount = i;
    }

    public String getGoodprice_settime() {
        return this.goodprice_settime;
    }

    public void setGoodprice_settime(String str) {
        this.goodprice_settime = str;
    }

    public boolean isStopSaleByServer() {
        return this.isStopSaleByServer;
    }

    public void setStopSaleByServer(boolean z) {
        if (Shj.isResetFinished()) {
            ShjDbHelper.updateShelfInfo(this.shelf.intValue(), ShjDbHelper.COLUM_stopsale, z ? "TRUE" : "FALSE");
        }
        this.isStopSaleByServer = z;
    }

    public int getReady2OfferCount() {
        return this.ready2OfferCount;
    }

    public void setReady2OfferCount(int i) {
        this.ready2OfferCount = i;
    }
}

package com.basoft.eorder.domain.model;

import com.basoft.eorder.domain.model.topic.StoreTopic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description: 店铺
 * @Date Created in 下午3:03 2018/12/4
 **/
public class Store {
    public static final int IS_SELFSERVICE_BAN = 0;

    public static final int IS_SELFSERVICE_ALLOW = 1;

    @JsonSerialize
    private Long id;

    private String name;

    private ContactInfo contact;

    private CompanyInfo companyInfo;

    //描述
    private String description;

    //描述中文
    private String descriptionChn;

    private User manager;

    private int status;

    private String merchantId;

    private String merchantNm;

    private String gatewayPw;

    private String paymentMethod;

    private String currency;

    private String transidType;

    private String appid;

    private String mchId;

    private String subAppid;

    private String subMchid;

    private String apiKey;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private int storeType;

    //是否支持自助点餐 1-支持 0-不支持
    private int isSelfservice;

    //是否支持外送 1-支持 0-不支持
    private int isDelivery;

    //是否启用自助点餐 1-启用 0-禁用
    private int selfserviceUseyn;

    //是否启用外送 1-启用 0-禁用
    private int deliveryUseyn;

    //是否设置为下单并进行支付类商店 1-下单并支付 0-下单不支付
    private int isPaySet;

    //门店是否正在营业 1-营业中 0-打烊
    private int isOpening;

    //门店是否分时间段营业 1-分段营业 0-不分段营业
    private int isSegmented;

    //早段起始时间，格式为：时分，如09:00
    private String morningSt;

    //早段结束时间，格式为：时分，如09:00
    private String morningEt;

    //中段起始时间，格式为：时分，如09:00
    private String noonSt;

    //中段结束时间，格式为：时分，如09:00
    private String noonEt;

    //晚段起始时间，格式为：时分，如09:00
    private String eveningSt;

    //晚段结束时间，格式为：时分，如09:00
    private String eveningEt;

    private String afternoonSt;

    private String afternoonEt;

    //夜宵段起始时间，格式为：时分，如09:00。（备用字段）
    private String midnightSt;

    //夜宵段结束时间，格式为：时分，如09:00。（备用字段）
    private String midnightEt;

    //收费类型 1-按营业额的百分比 2-按月
    private int chargeType;

    //按营业额百分比收费的百分率。取值范围为0-100
    private double chargeRate;

    //按月收费的收费金额。韩币单位
    private double chargeFee;

    //按月收费类商店的桌子上限数量
    private int tableCount;

    private int isJoin;//是否加盟店

    private int chargeRatePriceOn;//酒店结算按照费率还是按照价格

    private List<StoreExtend> storeExtendList;

    private List<StoreAttach> storeAttachList;

    private List<StoreTopic> topicList;

    private String cashSettleType;//交易金结算类型，取值：PG，BA。默认值PG

    private int prodPriceType;//酒店房价格类型。 1-独立平台价格，商户月结算按商户费率  2-价格分离（到手价和平台价分离

    private String stBankName;//交易金结算-BA结算时，商户接收交易金银行名称

    private String stBankAcc;//交易金结算-BA结算时，商户接收交易金银行卡号

    private String stBankAccName;//交易金结算-BA结算时，商户接收交易金银行卡账户名称

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public ContactInfo contact() {
        return contact;
    }

    public CompanyInfo companyInfo() {
        return companyInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionChn() {
        return descriptionChn;
    }

    public int getStatus() {
        return status;
    }

    public User manager() {
        return this.manager;
    }

    public String merchantId() {
        return this.merchantId;
    }

    public String merchantNm() {
        return this.merchantNm;
    }

    public String gatewayPw() {
        return this.gatewayPw;
    }

    public String paymentMethod() {
        return this.paymentMethod;
    }

    public String currency() {
        return this.currency;
    }

    public String transidType() {
        return this.transidType;
    }

    public String appid() {
        return this.appid;
    }

    public String mchId() {
        return this.mchId;
    }

    public String subAppid() {
        return this.subAppid;
    }

    public String subMchid() {
        return this.subMchid;
    }

    public String apiKey() {
        return this.apiKey;
    }

    public BigDecimal longitude() {
        return this.longitude;
    }

    public BigDecimal latitude() {
        return this.latitude;
    }

    public int storeType() {
        return this.storeType;
    }

    public int isSelfservice() {
        return isSelfservice;
    }

    public int isDelivery() {
        return isDelivery;
    }

    public int selfserviceUseyn() {
        return selfserviceUseyn;
    }

    public int deliveryUseyn() {
        return deliveryUseyn;
    }

    public int isPaySet() {
        return isPaySet;
    }

    public int isOpening() {
        return isOpening;
    }

    public int isSegmented() {
        return isSegmented;
    }

    public String morningSt() {
        return morningSt;
    }

    public String morningEt() {
        return morningEt;
    }

    public String noonSt() {
        return noonSt;
    }

    public String noonEt() {
        return noonEt;
    }

    public String eveningSt() {
        return eveningSt;
    }

    public String eveningEt() {
        return eveningEt;
    }

    public String afternoonSt() {
        return afternoonSt;
    }

    public String afternoonEt() {
        return afternoonEt;
    }

    public String midnightSt() {
        return midnightSt;
    }

    public String midnightEt() {
        return midnightEt;
    }

    public int chargeType() {
        return chargeType;
    }

    public double chargeRate() {
        return chargeRate;
    }

    public double chargeFee() {
        return chargeFee;
    }

    public int tableCount() {
        return tableCount;
    }

    public List<StoreExtend> storeExtendList(){
        return storeExtendList;
    }

    public List<StoreAttach> storeAttachList(){
        return storeAttachList;
    }

    public List<StoreTopic> topicList(){
        return topicList;
    }

    public String cashSettleType() {
        return cashSettleType;
    }

    public int prodPriceType() {
        return prodPriceType;
    }

    public String stBankName() {
        return stBankName;
    }

    public String stBankAcc() {
        return stBankAcc;
    }

    public String stBankAccName() {
        return stBankAccName;
    }

    public int isJoin(){
        return isJoin;
    }

    public int chargeRatePriceOn(){
        return chargeRatePriceOn;
    }

    // 额外加三个setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }
    // 额外加三个setter

    public enum Status {
        NORMAL(0), OPEN(1), CLOSED(2), DELETED(3);
        private int code;

        Status(int code) {
            this.code = code;
        }
        /*private Status(int code){
            code = code;
        }*/

        public int code() {
            return this.code;
        }

        public static Store.Status valueOf(Integer value) {
            for (Store.Status e : Store.Status.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    public static Builder newBuild(Store store) {
        return new Builder()
                .id(store.id)
                .name(store.name)
                .detailAddr(store.contact.getDetailAddr())
                .detailAddrChn(store.contact.getDetailAddrChn())
                .description(store.description)
                .descriptionChn(store.descriptionChn)
                .logoUrl(store.contact.getLogoUrl())
                .manager(store.manager)
                .bizScope(store.companyInfo.getScope())
                .ceoName(store.companyInfo.getCeo())
                .mobile(store.contact.getMobile())
                .email(store.contact.getEmail())
                .city(store.contact.getCity())
                .merchantId(store.merchantId)
                .merchantNm(store.merchantNm)
                .gatewayPw(store.gatewayPw)
                .paymentMethod(store.paymentMethod)
                .currency(store.currency)
                .transidType(store.transidType)
                .appid(store.appid)
                .mchId(store.mchId)
                .subAppid(store.subAppid)
                .subMchid(store.subMchid)
                .apiKey(store.apiKey)
                .latitude(store.latitude)
                .longitude(store.longitude)
                .storeType(store.storeType);
    }

    public static final class Builder {
        private Long id;

        private String name;

        private String city;

        private String email;

        private String mobile;

        private String detailAddr;

        private String detailAddrChn;

        private String shopHour;

        private String logoUrl;

        private Long managerId;

        private int status;

        private int storeType;

        // WeChat Pay API & Alliex API param
        private String merchantId;

        private String merchantNm;

        private String gatewayPw;

        private String paymentMethod;

        private String currency;

        private String transidType;

        private String appid;

        private String mchId;

        private String subAppid;

        private String subMchid;

        private String apiKey;

        private BigDecimal longitude;

        private BigDecimal latitude;

        //private ContactInfo contact;
        //private CompanyInfo companyInfo;

        //法人
        private String ceo;

        //经营范围
        private String bizScope;

        //描述
        private String description;

        //中文描述
        private String descriptionChn;

        private User manager;

        //是否支持自助点餐 1-支持 0-不支持
        private int isSelfservice;

        //是否支持外送 1-支持 0-不支持
        private int isDelivery;

        //是否启用自助点餐 1-启用 0-禁用
        private int selfserviceUseyn;

        //是否启用外送 1-启用 0-禁用
        private int deliveryUseyn;

        //是否设置为下单并进行支付类商店 1-下单并支付 0-下单不支付
        private int isPaySet;

        //门店是否正在营业 1-营业中 0-打烊
        private int isOpening;

        //门店是否分时间段营业 1-分段营业 0-不分段营业
        private int isSegmented;

        //早段起始时间，格式为：时分，如09:00
        private String morningSt;

        //早段结束时间，格式为：时分，如09:00
        private String morningEt;

        //中段起始时间，格式为：时分，如09:00
        private String noonSt;

        //中段结束时间，格式为：时分，如09:00
        private String noonEt;

        //晚段起始时间，格式为：时分，如09:00
        private String eveningSt;

        //晚段结束时间，格式为：时分，如09:00
        private String eveningEt;

        private String afternoonSt;

        private String afternoonEt;

        //夜宵段起始时间，格式为：时分，如09:00。（备用字段）
        private String midnightSt;

        //夜宵段结束时间，格式为：时分，如09:00。（备用字段）
        private String midnightEt;

        //收费类型 1-按营业额的百分比 2-按月
        private int chargeType;

        //按营业额百分比收费的百分率。取值范围为0-100
        private double chargeRate;

        //按月收费的收费金额。韩币单位
        private double chargeFee;

        private int chargeRatePriceOn; //酒店结算按照费率还是按照价格

        //按月收费类商店的桌子上限数量
        private int tableCount;

        private List<StoreExtend> storeExtendList;

        private List<StoreAttach> storeAttachList;

        private List<StoreTopic> topicList;

        private String cashSettleType;//交易金结算类型，取值：PG，BA。默认值PG

        private int prodPriceType;//酒店房价格类型。 1-独立平台价格，商户月结算按商户费率  2-价格分离（到手价和平台价分离

        private String stBankName;//交易金结算-BA结算时，商户接收交易金银行名称

        private String stBankAcc;//交易金结算-BA结算时，商户接收交易金银行卡号

        private String stBankAccName;

        private int isJoin;



        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder ceoName(String ceo) {
            this.ceo = ceo;
            return this;
        }

        public Builder manager(User manager) {
            this.manager = manager;
            return this;
        }

        public Builder bizScope(String scope) {
            this.bizScope = scope;
            return this;
        }

        public Builder detailAddr(String detailAddr) {
            this.detailAddr = detailAddr;
            return this;
        }

        public Builder detailAddrChn(String detailAddrChn) {
            this.detailAddrChn = detailAddrChn;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder descriptionChn(String descriptionChn) {
            this.descriptionChn = descriptionChn;
            return this;
        }

        public Builder shopHour(String shopHour) {
            this.shopHour = shopHour;
            return this;
        }

        public Builder managerId(Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder storeType(int storeType) {
            this.storeType = storeType;
            return this;
        }

        public Builder merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder merchantNm(String merchantNm) {
            this.merchantNm = merchantNm;
            return this;
        }

        public Builder gatewayPw(String gatewayPw) {
            this.gatewayPw = gatewayPw;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder transidType(String transidType) {
            this.transidType = transidType;
            return this;
        }

        public Builder appid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder mchId(String mchId) {
            this.mchId = mchId;
            return this;
        }

        public Builder subAppid(String subAppid) {
            this.subAppid = subAppid;
            return this;
        }

        public Builder subMchid(String subMchid) {
            this.subMchid = subMchid;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder longitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder isSelfservice(int isSelfservice) {
            this.isSelfservice = isSelfservice;
            return this;
        }

        public Builder isDelivery(int isDelivery) {
            this.isDelivery = isDelivery;
            return this;
        }

        public Builder selfserviceUseyn(int selfserviceUseyn) {
            this.selfserviceUseyn = selfserviceUseyn;
            return this;
        }

        public Builder deliveryUseyn(int deliveryUseyn) {
            this.deliveryUseyn = deliveryUseyn;
            return this;
        }

        public Builder isPaySet(int isPaySet) {
            this.isPaySet = isPaySet;
            return this;
        }

        public Builder isOpening(int isOpening) {
            this.isOpening = isOpening;
            return this;
        }

        public Builder isSegmented(int isSegmented) {
            this.isSegmented = isSegmented;
            return this;
        }

        public Builder morningSt(String morningSt) {
            this.morningSt = morningSt;
            return this;
        }

        public Builder morningEt(String morningEt) {
            this.morningEt = morningEt;
            return this;
        }

        public Builder noonSt(String noonSt) {
            this.noonSt = noonSt;
            return this;
        }

        public Builder noonEt(String noonEt) {
            this.noonEt = noonEt;
            return this;
        }

        public Builder eveningSt(String eveningSt) {
            this.eveningSt = eveningSt;
            return this;
        }

        public Builder eveningEt(String eveningEt) {
            this.eveningEt = eveningEt;
            return this;
        }

        public Builder afternoonSt(String afternoonSt) {
            this.afternoonSt = afternoonSt;
            return this;
        }

        public Builder afternoonEt(String afternoonEt) {
            this.afternoonEt = afternoonEt;
            return this;
        }

        public Builder midnightSt(String midnightSt) {
            this.midnightSt = midnightSt;
            return this;
        }

        public Builder midnightEt(String midnightEt) {
            this.midnightEt = midnightEt;
            return this;
        }

        public Builder chargeType(int chargeType) {
            this.chargeType = chargeType;
            return this;
        }

        public Builder chargeRate(double chargeRate) {
            this.chargeRate = chargeRate;
            return this;
        }

        public Builder chargeFee(double chargeFee) {
            this.chargeFee = chargeFee;
            return this;
        }

        public Builder tableCount(int tableCount) {
            this.tableCount = tableCount;
            return this;
        }

        public Builder cashSettleType(String cashSettleType) {
            this.cashSettleType = cashSettleType;
            return this;
        }

        public Builder prodPriceType(int prodPriceType) {
            this.prodPriceType = prodPriceType;
            return this;
        }

        public Builder stBankName(String stBankName) {
            this.stBankName = stBankName;
            return this;
        }

        public Builder stBankAcc(String stBankAcc) {
            this.stBankAcc = stBankAcc;
            return this;
        }

        public Builder stBankAccName(String stBankAccName) {
            this.stBankAccName = stBankAccName;
            return this;
        }

        public Builder isJoin(int isJoin) {
            this.isJoin = isJoin;
            return this;
        }

        public Builder chargeRatePriceOn(int chargeRatePriceOn) {
            this.chargeRatePriceOn = chargeRatePriceOn;
            return this;
        }




        public Builder storeExtendList(List<StoreExtend> storeExtendList){
            if(storeExtendList!=null) {
                storeExtendList.stream().map(s -> {
                    s.setStoreId(this.id);
                    return s;
                }).collect(Collectors.toList());
            }
            this.storeExtendList = storeExtendList;
            return this;
        }

        public Builder storeAttachList(List<StoreAttach> attachList) {
            if(attachList!=null) {
                attachList.stream().map(s -> {
                    s.setStoreId(this.id);
                    return s;
                }).collect(Collectors.toList());
            }
            this.storeAttachList = attachList;
            return this;
        }

        public Builder topicList(List<StoreTopic> topicList) {
            if(topicList!=null) {
                topicList.stream().map(s -> {
                    s.setStoreId(this.id);
                    return s;
                }).collect(Collectors.toList());
            }
            this.topicList = topicList;
            return this;
        }

        public Store build() {
            Store store = new Store();
            store.id = this.id;
            store.name = this.name;
            store.companyInfo = new CompanyInfo(this.ceo, this.bizScope, this.shopHour);
            store.contact = new ContactInfo(this.mobile, this.email, this.city, this.detailAddr, this.detailAddrChn, this.logoUrl);
            store.manager = this.manager;
            store.status = this.status;
            store.merchantId = this.merchantId;
            store.merchantNm = this.merchantNm;
            store.gatewayPw = this.gatewayPw;
            store.paymentMethod = this.paymentMethod;
            store.currency = this.currency;
            store.transidType = this.transidType;
            store.appid = this.appid;
            store.mchId = this.mchId;
            store.subAppid = this.subAppid;
            store.subMchid = this.subMchid;
            store.apiKey = this.apiKey;
            store.longitude = this.longitude;
            store.latitude = this.latitude;
            store.description = this.description;
            store.descriptionChn = this.descriptionChn;
            store.storeType = this.storeType;
            store.isSelfservice = this.isSelfservice;
            store.isDelivery = this.isDelivery;
            store.selfserviceUseyn = this.selfserviceUseyn;
            store.deliveryUseyn = this.deliveryUseyn;
            store.isPaySet = this.isPaySet;
            store.isOpening = this.isOpening;
            store.isSegmented = this.isSegmented;
            store.morningSt = this.morningSt;
            store.morningEt = this.morningEt;
            store.noonSt = this.noonSt;
            store.noonEt = this.noonEt;
            store.eveningSt = this.eveningSt;
            store.eveningEt = this.eveningEt;
            store.afternoonSt = this.afternoonSt;
            store.afternoonEt = this.afternoonEt;
            store.midnightSt = this.midnightSt;
            store.midnightEt = this.midnightEt;
            store.chargeType = this.chargeType;
            store.chargeRate = this.chargeRate;
            store.chargeFee = this.chargeFee;
            store.tableCount = this.tableCount;
            store.storeExtendList = this.storeExtendList;
            store.storeAttachList = this.storeAttachList;
            store.topicList = this.topicList;
            store.cashSettleType = this.cashSettleType;
            store.prodPriceType = this.prodPriceType;
            store.stBankName = this.stBankName;
            store.stBankAcc = this.stBankAcc;
            store.stBankAccName = this.stBankAccName;
            store.isJoin = this.isJoin;
            store.chargeRatePriceOn = this.chargeRatePriceOn;
            //收费类型 1-按营业额的百分比 2-按月
            return store;
        }
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact=" + contact +
                ", companyInfo=" + companyInfo +
                ", description='" + description + '\'' +
                ", descriptionChn='" + descriptionChn + '\'' +
                ", manager=" + manager +
                ", status=" + status +
                ", merchantId='" + merchantId + '\'' +
                ", merchantNm='" + merchantNm + '\'' +
                ", gatewayPw='" + gatewayPw + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", currency='" + currency + '\'' +
                ", transidType='" + transidType + '\'' +
                ", appid='" + appid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", subAppid='" + subAppid + '\'' +
                ", subMchid='" + subMchid + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", storeType=" + storeType +
                ", isSelfservice=" + isSelfservice +
                ", isDelivery=" + isDelivery +
                ", selfserviceUseyn=" + selfserviceUseyn +
                ", deliveryUseyn=" + deliveryUseyn +
                ", isPaySet=" + isPaySet +
                ", isOpening=" + isOpening +
                ", isSegmented=" + isSegmented +
                ", morningSt='" + morningSt + '\'' +
                ", morningEt='" + morningEt + '\'' +
                ", noonSt='" + noonSt + '\'' +
                ", noonEt='" + noonEt + '\'' +
                ", eveningSt='" + eveningSt + '\'' +
                ", eveningEt='" + eveningEt + '\'' +
                ", afternoonSt='" + afternoonSt + '\'' +
                ", afternoonEt='" + afternoonEt + '\'' +
                ", midnightSt='" + midnightSt + '\'' +
                ", midnightEt='" + midnightEt + '\'' +
                ", chargeType=" + chargeType +
                ", chargeRate=" + chargeRate +
                ", chargeFee=" + chargeFee +
                ", tableCount=" + tableCount +
                ",storeTopicList="+topicList+
                ",storeExtendList="+storeExtendList+
                ",storeAttachList="+storeAttachList+
                ",isJoin="+isJoin+
                ",chargeRatePriceOn="+chargeRatePriceOn+
                '}';
    }
}

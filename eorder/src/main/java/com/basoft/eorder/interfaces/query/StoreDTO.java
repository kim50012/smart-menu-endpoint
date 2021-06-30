package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.interfaces.query.topic.StoreTopicDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author woonill
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: StoreDTO
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-13 16:15
 * @Version: 1.0
 */
public class StoreDTO {
    private Long id;
    private String name;
    private String city;
    private String areaNm;
    private String detailAddr;  //详细地址
    private String detailAddrChn;  //中文详细地址
    private String description; //描述
    private String descriptionChn; //描述中文
    private String shopHour;  //营业时间
    private String email;
    private String mobile;
    private String ceoName;
    private String bizScope;
    private String created;
    private Long managerId;
    private String managerPhone;
    private String managerAccount;
    private String managerEmail;
    private String managerName;
    private String merchantId;//加盟店Id
    private String merchantNm;
    private String gatewayPw;
    private String transidType;
    private int status;
    private String logoUrl;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private int disdance = -1;
    private int storeType;
    private int isSelfservice;//是否支持自助点餐 1-支持 0-不支持
    private int isDelivery;//是否支持外送 1-支持 0-不支持
    private int selfserviceUseyn;//是否启用自助点餐 1-启用 0-禁用
    private int deliveryUseyn;//是否启用外送 1-启用 0-禁用
    private int isPaySet; //是否设置为下单并进行支付类商店 1-下单并支付 0-下单不支付
    private int isOpening; //门店是否正在营业 1-营业中 0-打烊
    private int isSegmented; //门店是否分时间段营业 1-分段营业 0-不分段营业
    private String morningSt; //早段起始时间，格式为：时分，如09:00
    private String morningEt; //早段结束时间，格式为：时分，如09:00
    private String noonSt; //中段起始时间，格式为：时分，如09:00
    private String noonEt; //中段结束时间，格式为：时分，如09:00
    private String eveningSt; //晚段起始时间，格式为：时分，如09:00
    private String eveningEt; //晚段结束时间，格式为：时分，如09:00
    private String afternoonSt;//下午茶起始时间，格式为：时分，如09:00。（备用字段）
    private String afternoonEt;//下午茶结束时间，格式为：时分，如09:00。（备用字段）
    private String midnightSt; //夜宵段起始时间，格式为：时分，如09:00。（备用字段）
    private String midnightEt; //夜宵段结束时间，格式为：时分，如09:00。（备用字段）
    //收费类型 1-按营业额的百分比 2-按月
    private int chargeType;
    private int nextChargeType;

    private int retailType;
    
    //按营业额百分比收费的百分率。取值范围为0-100
    private double chargeRate;
    private double nextChargeRate;

    //按月收费的收费金额。韩币单位
    private double chargeFee;
    private double nextChargeFee;

    //按月收费类商店的桌子上限数量
    private int tableCount;

    private List<Category> categoryList;

    // 20190806 门店的业务标签ID组合串，格式为：tagid1,tagid2,tagid3
    private String categorysAllString;

    // 20190807 门店商品最低价格（人民币）
    private BigDecimal minPriceChn;

    // 20190807 门店商品最低价格（韩币）
    private BigDecimal minPriceKor;

    // 20190814 门店商品活动最低价格（人民币）
    private BigDecimal minDiscountPriceKor;

    // 20190814 门店商品活动最低价格（韩币）
    private BigDecimal minDiscountPriceChn;

    // 标签列表-Admin CMS管理
    private List<Category> categoryTagList;

    // 标签列表-Manager CMS管理
    private List<Category> categoryManagerTagList;

    private List<StoreTableDTO> storeTableList;

    private List<StoreExtendDTO> storeExtendList;

    private List<StoreAttachDTO> attachList;

    private List<StoreTopicDTO> topicList;

    private String cashSettleType;//交易金结算类型，取值：PG，BA。默认值PG

    private int prodPriceType;//酒店房价格类型。 1-独立平台价格，商户月结算按商户费率  2-价格分离（到手价和平台价分离

    private String stBankName;//交易金结算-BA结算时，商户接收交易金银行名称

    private String stBankAcc;//交易金结算-BA结算时，商户接收交易金银行卡号

    private String stBankAccName;//交易金结算-BA结算时，商户接收交易金银行卡账户名称

    private int isJoin;

    private int chargeRatePriceOn; //酒店结算按照费率还是按照价格

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getDetailAddrChn() {
        return detailAddrChn;
    }

    public void setDetailAddrChn(String detailAddrChn) {
        this.detailAddrChn = detailAddrChn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionChn() {
        return descriptionChn;
    }

    public void setDescriptionChn(String descriptionChn) {
        this.descriptionChn = descriptionChn;
    }

    public String getShopHour() {
        return shopHour;
    }

    public void setShopHour(String shopHour) {
        this.shopHour = shopHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getBizScope() {
        return bizScope;
    }

    public void setBizScope(String bizScope) {
        this.bizScope = bizScope;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<StoreTableDTO> getStoreTableList() {
        return storeTableList;
    }

    public void setStoreTableList(List<StoreTableDTO> storeTableList) {
        this.storeTableList = storeTableList;
    }

    public List<StoreTopicDTO> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<StoreTopicDTO> topicList) {
        this.topicList = topicList;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getManagerAccount() {
        return managerAccount;
    }

    public void setManagerAccount(String managerAccount) {
        this.managerAccount = managerAccount;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public int getDisdance() {
        return disdance;
    }

    public void setDisdance(int disdance) {
        this.disdance = disdance;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantNm() {
        return merchantNm;
    }

    public void setMerchantNm(String merchantNm) {
        this.merchantNm = merchantNm;
    }

    public String getGatewayPw() {
        return gatewayPw;
    }

    public void setGatewayPw(String gatewayPw) {
        this.gatewayPw = gatewayPw;
    }

    public String getTransidType() {
        return transidType;
    }

    public void setTransidType(String transidType) {
        this.transidType = transidType;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategorysAllString() {
        return categorysAllString;
    }

    public void setCategorysAllString(String categorysAllString) {
        this.categorysAllString = categorysAllString;
    }

    public BigDecimal getMinPriceChn() {
        return minPriceChn;
    }

    public void setMinPriceChn(BigDecimal minPriceChn) {
        this.minPriceChn = minPriceChn;
    }

    public BigDecimal getMinPriceKor() {
        return minPriceKor;
    }

    public void setMinPriceKor(BigDecimal minPriceKor) {
        this.minPriceKor = minPriceKor;
    }

    public BigDecimal getMinDiscountPriceKor() {
        return minDiscountPriceKor;
    }

    public void setMinDiscountPriceKor(BigDecimal minDiscountPriceKor) {
        this.minDiscountPriceKor = minDiscountPriceKor;
    }

    public BigDecimal getMinDiscountPriceChn() {
        return minDiscountPriceChn;
    }

    public void setMinDiscountPriceChn(BigDecimal minDiscountPriceChn) {
        this.minDiscountPriceChn = minDiscountPriceChn;
    }

    public List<Category> getCategoryTagList() {
        return categoryTagList;
    }

    public void setCategoryTagList(List<Category> categoryTagList) {
        this.categoryTagList = categoryTagList;
    }

    public List<Category> getCategoryManagerTagList() {
        return categoryManagerTagList;
    }

    public void setCategoryManagerTagList(List<Category> categoryManagerTagList) {
        this.categoryManagerTagList = categoryManagerTagList;
    }

    public int getIsSelfservice() {
        return isSelfservice;
    }

    public void setIsSelfservice(int isSelfservice) {
        this.isSelfservice = isSelfservice;
    }

    public int getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(int isDelivery) {
        this.isDelivery = isDelivery;
    }

    public int getSelfserviceUseyn() {
        return selfserviceUseyn;
    }

    public void setSelfserviceUseyn(int selfserviceUseyn) {
        this.selfserviceUseyn = selfserviceUseyn;
    }

    public int getDeliveryUseyn() {
        return deliveryUseyn;
    }

    public void setDeliveryUseyn(int deliveryUseyn) {
        this.deliveryUseyn = deliveryUseyn;
    }

    public int getIsPaySet() {
        return isPaySet;
    }

    public void setIsPaySet(int isPaySet) {
        this.isPaySet = isPaySet;
    }

    public int getIsOpening() {
        return isOpening;
    }

    public void setIsOpening(int isOpening) {
        this.isOpening = isOpening;
    }

    public int getIsSegmented() {
        return isSegmented;
    }

    public void setIsSegmented(int isSegmented) {
        this.isSegmented = isSegmented;
    }

    public String getMorningSt() {
        return morningSt;
    }

    public void setMorningSt(String morningSt) {
        this.morningSt = morningSt;
    }

    public String getMorningEt() {
        return morningEt;
    }

    public void setMorningEt(String morningEt) {
        this.morningEt = morningEt;
    }

    public String getNoonSt() {
        return noonSt;
    }

    public void setNoonSt(String noonSt) {
        this.noonSt = noonSt;
    }

    public String getNoonEt() {
        return noonEt;
    }

    public void setNoonEt(String noonEt) {
        this.noonEt = noonEt;
    }

    public String getEveningSt() {
        return eveningSt;
    }

    public void setEveningSt(String eveningSt) {
        this.eveningSt = eveningSt;
    }

    public String getEveningEt() {
        return eveningEt;
    }

    public void setEveningEt(String eveningEt) {
        this.eveningEt = eveningEt;
    }

    public String getAfternoonSt() {
        return afternoonSt;
    }

    public void setAfternoonSt(String afternoonSt) {
        this.afternoonSt = afternoonSt;
    }

    public String getAfternoonEt() {
        return afternoonEt;
    }

    public void setAfternoonEt(String afternoonEt) {
        this.afternoonEt = afternoonEt;
    }

    public String getMidnightSt() {
        return midnightSt;
    }

    public void setMidnightSt(String midnightSt) {
        this.midnightSt = midnightSt;
    }

    public String getMidnightEt() {
        return midnightEt;
    }

    public void setMidnightEt(String midnightEt) {
        this.midnightEt = midnightEt;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(int chargeFee) {
        this.chargeFee = chargeFee;
    }

    public int getNextChargeType() {
        return nextChargeType;
    }

    public void setNextChargeType(int nextChargeType) {
        this.nextChargeType = nextChargeType;
    }

    public double getNextChargeRate() {
        return nextChargeRate;
    }

    public void setNextChargeRate(double nextChargeRate) {
        this.nextChargeRate = nextChargeRate;
    }

    public double getNextChargeFee() {
        return nextChargeFee;
    }

    public void setNextChargeFee(double nextChargeFee) {
        this.nextChargeFee = nextChargeFee;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public List<StoreExtendDTO> getStoreExtendList() {
        return storeExtendList;
    }

    public void setStoreExtendList(List<StoreExtendDTO> storeExtendList) {
        this.storeExtendList = storeExtendList;
    }

    public List<StoreAttachDTO> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<StoreAttachDTO> attachList) {
        this.attachList = attachList;
    }

    public String getCashSettleType() {
        return cashSettleType;
    }

    public void setCashSettleType(String cashSettleType) {
        this.cashSettleType = cashSettleType;
    }

    public int getProdPriceType() {
        return prodPriceType;
    }

    public void setProdPriceType(int prodPriceType) {
        this.prodPriceType = prodPriceType;
    }

    public String getStBankName() {
        return stBankName;
    }

    public void setStBankName(String stBankName) {
        this.stBankName = stBankName;
    }

    public String getStBankAcc() {
        return stBankAcc;
    }

    public void setStBankAcc(String stBankAcc) {
        this.stBankAcc = stBankAcc;
    }

    public String getStBankAccName() {
        return stBankAccName;
    }

    public void setStBankAccName(String stBankAccName) {
        this.stBankAccName = stBankAccName;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public int getChargeRatePriceOn() {
        return chargeRatePriceOn;
    }

    public void setChargeRatePriceOn(int chargeRatePriceOn) {
        this.chargeRatePriceOn = chargeRatePriceOn;
    }

    public int getRetailType() {
        return retailType;
    }

    public void setRetailType(int retailType) {
        this.retailType = retailType;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.*;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.domain.model.topic.StoreTopic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SaveStore implements Command {
    public static final String NAME = "saveStore";

    private Long id;
    private String name;

    private String merchantId;

    private String city;
    private String email;
    private String phone;
    private String logoUrl;
    private String detailAddress;
    private String detailAddrChn;  //中文详细地址
    private String description; //描述
    private String descriptionChn; //描述中文
    private String shopHour;  //营业时间
    private String ceo;//法人
    private String scope;//经营范围
    private Long managerId;
    // 0-新建未上架new  1-上架状态open  2-下架forbidden  3-关闭delete（所有终端不可用）  4-欠费停服务（Manager CMS可用）
    private int status;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private List<Long> categoryIds;//店铺的商品分类
    private List<Long> categoryTagIds;// 店铺标签-Admin CMS管理
    private List<Long> categoryManagerTagIds;// 店铺标签-Manager CMS管理

    private String merchantNm;
    private String gatewayPw;
    private String transidType;

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

    //收费类型 1-按营业额百分比 2-按营业额百分比或最小服务费 3-按月
    private int chargeType;

    //按营业额百分比收费的百分率。取值范围为0-100
    private double chargeRate;

    // 第一种情况：当收费类型为2，此字段存储最小服务费；第二种情况，当收费类型为3，此字段存储按月收费的收费金额。韩币单位
    private double chargeFee;

    //按月收费类商店的桌子上限数量
    private int tableCount;

    private List<StoreExtend> storeExtendList;

    private List<StoreAttach> attachList;

    private List<StoreTopic> topicList;

    private String cashSettleType;//交易金结算类型，取值：PG，BA。默认值PG

    private int prodPriceType;//酒店房价格类型。 1-独立平台价格，商户月结算按商户费率  2-价格分离（到手价和平台价分离

    private String stBankName;//交易金结算-BA结算时，商户接收交易金银行名称

    private String stBankAcc;//交易金结算-BA结算时，商户接收交易金银行卡号

    private String stBankAccName;//交易金结算-BA结算时，商户接收交易金银行卡账户名称

    private int isJoin;

    private int chargeRatePriceOn;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getCategoryTagIds() {
        return categoryTagIds;
    }

    public void setCategoryTagIds(List<Long> categoryTagIds) {
        this.categoryTagIds = categoryTagIds;
    }

    public List<Long> getCategoryManagerTagIds() {
        return categoryManagerTagIds;
    }

    public void setCategoryManagerTagIds(List<Long> categoryManagerTagIds) {
        this.categoryManagerTagIds = categoryManagerTagIds;
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

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
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

    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public List<StoreExtend> getStoreExtendList() {
        return storeExtendList;
    }

    public void setStoreExtendList(List<StoreExtend> storeExtendList) {
        this.storeExtendList = storeExtendList;
    }

    public List<StoreAttach> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<StoreAttach> attachList) {
        this.attachList = attachList;
    }

    public List<StoreTopic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<StoreTopic> topicList) {
        this.topicList = topicList;
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

    public Store build(Store store, User user, Long id) {
        validate(user, id);
        return new Store.Builder()
                .id(id)
                .name(this.name)
                .city(this.city)
                .detailAddr(this.detailAddress)
                .detailAddrChn(this.detailAddrChn)
                .description(this.description)
                .descriptionChn(this.descriptionChn)
                .shopHour(this.shopHour)
                .email(this.email)
                .mobile(this.phone)
                .ceoName(this.ceo)
                .logoUrl(this.logoUrl)
                .bizScope(this.scope)
                .manager(user)
                .merchantId(this.merchantId)
                .status(this.status)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .merchantNm(this.merchantNm)
                .gatewayPw(this.gatewayPw)
                .transidType(this.transidType)
                .storeType(this.storeType)
                .isSelfservice(this.isSelfservice)
                .isDelivery(this.isDelivery)
                .selfserviceUseyn(this.selfserviceUseyn)
                .deliveryUseyn(this.deliveryUseyn)
                .isPaySet(this.isPaySet)
                //.isOpening(this.storeType == 1 ? store.isOpening() : this.isOpening)
                .isOpening(this.isOpening)

                /*.isSegmented(store.isSegmented())
                .morningSt(store.morningSt())
                .morningEt(store.morningEt())
                .noonSt(store.noonSt())
                .noonEt(store.noonEt())
                .eveningSt(store.eveningSt())
                .eveningEt(store.eveningEt())
                .afternoonSt(store.afternoonSt())
                .afternoonEt(store.afternoonEt())
                .midnightSt(store.midnightSt())
                .midnightEt(store.midnightEt())*/

                .isSegmented(this.isSegmented)
                .morningSt(this.morningSt)
                .morningEt(this.morningEt)
                .noonSt(this.noonSt)
                .noonEt(this.noonEt)
                .eveningSt(this.eveningSt)
                .eveningEt(this.eveningEt)
                .afternoonSt(this.afternoonSt)
                .afternoonEt(this.afternoonEt)
                .midnightSt(this.midnightSt)
                .midnightEt(this.midnightEt)

                .chargeType(this.chargeType)
                .chargeRate(this.chargeRate)
                .chargeFee(this.chargeFee)
                .tableCount(this.tableCount)
                .storeExtendList(this.storeExtendList)
                .storeAttachList(this.attachList)
                .topicList(this.topicList)
                .cashSettleType(this.cashSettleType)
                .prodPriceType(this.prodPriceType)
                .stBankName(this.stBankName)
                .stBankAcc(this.stBankAcc)
                .stBankAccName(this.stBankAccName)
                .isJoin(this.isJoin)
                .chargeRatePriceOn(this.chargeRatePriceOn)
                // 修改时店铺状态保持原有状态，新建时店铺状态取int默认值0
                .status(store.getStatus())
                .build();
    }

    private void validate(User user, Long nid) {
        if (user == null) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (nid == null) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
    }

    public List<StoreCategory> buildStoreCategoryList(Store store) {
        /*if (this.categoryIds != null && this.categoryIds.size() > 0) {
            return this.categoryIds.stream()
                    .map(new Function<Long, StoreCategory>() {
                        @Override
                        public StoreCategory apply(Long aLong) {
                            return new StoreCategory(aLong, store.id());
                        }
                    }).collect(Collectors.toList());
        }
        return null;*/

        // 店铺商品分类的目录
        List<StoreCategory> storeCategoryList = new ArrayList<StoreCategory>();
        if (this.categoryIds != null && this.categoryIds.size() > 0) {
            storeCategoryList = this.categoryIds.stream()
                    .map(new Function<Long, StoreCategory>() {
                        @Override
                        public StoreCategory apply(Long aLong) {
                            return new StoreCategory(aLong, store.id(),
                                    CommonConstants.CATEGORY_FUNCTION_TYPE_PRODUCT,
                                    CommonConstants.CATEGORY_MANAGE_TYPE_ADMIN);
                        }
                    }).collect(Collectors.toList());
        }

        // 店铺标签的目录-Admin CMS管理
        List<StoreCategory> storeCategoryTagList = null;
        if (this.categoryTagIds != null && this.categoryTagIds.size() > 0) {
            storeCategoryTagList = this.categoryTagIds.stream()
                    .map(new Function<Long, StoreCategory>() {
                        @Override
                        public StoreCategory apply(Long aLong) {
                            return new StoreCategory(aLong, store.id(),
                                    CommonConstants.CATEGORY_FUNCTION_TYPE_TAG,
                                    CommonConstants.CATEGORY_MANAGE_TYPE_ADMIN);
                        }
                    }).collect(Collectors.toList());
        }

        // 店铺标签的目录-Manager CMS管理
        List<StoreCategory> storeCategoryManagerTagList = null;
        if (this.categoryManagerTagIds != null && this.categoryManagerTagIds.size() > 0) {
            storeCategoryManagerTagList = this.categoryManagerTagIds.stream()
                    .map(new Function<Long, StoreCategory>() {
                        @Override
                        public StoreCategory apply(Long aLong) {
                            return new StoreCategory(aLong, store.id(),
                                    CommonConstants.CATEGORY_FUNCTION_TYPE_TAG,
                                    CommonConstants.CATEGORY_MANAGE_TYPE_MANAGER);
                        }
                    }).collect(Collectors.toList());
        }

        // 合并
        if(storeCategoryTagList != null && storeCategoryTagList.size() > 0){
            storeCategoryList.addAll(storeCategoryTagList);
        }
        if(storeCategoryManagerTagList != null && storeCategoryManagerTagList.size() > 0){
            storeCategoryList.addAll(storeCategoryManagerTagList);
        }

        return storeCategoryList;
    }

    public StoreChargeInfoRecord buildStoreChargeInfoRecord(Store store, SaveStore saveStore) {
        StoreChargeInfoRecord storeChargeInfoRecord = StoreChargeInfoRecord.newBuild(store, saveStore).build();
        return storeChargeInfoRecord;
    }
}
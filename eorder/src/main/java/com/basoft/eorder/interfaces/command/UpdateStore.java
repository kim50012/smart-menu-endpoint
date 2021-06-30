package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpdateStore implements Command {
    public static final String NAME = "updateStore";

    private Long id;
    private String name;
    private String city;
    private String detailAddr;
    private String detailAddrChn;
    private String description;
    private String descriptionChn;
    private String shopHour;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private String mobile;
    private String logoUrl;

    private String managerName;
    private String managerPhone;
    private String managerEmail;

    private String merchantId;
    private String merchantNm;
    private String gatewayPw;
    private String transidType;

    private int isSelfservice;//是否支持自助点餐 1-支持 0-不支持

    private int isDelivery;//是否支持外送 1-支持 0-不支持

    private int selfserviceUseyn;//是否启用自助点餐 1-启用 0-禁用

    private int deliveryUseyn;//是否启用外送 1-启用 0-禁用

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

    private List<Long> categoryIds;//店铺的商品分类

    private List<Long> categoryTagIds;// 店铺标签-Admin CMS管理

    private List<Long> categoryManagerTagIds;// 店铺标签-Manager CMS管理

    private List<StoreExtend> storeExtendList;

    private List<StoreAttach> attachList;

    private String cashSettleType;

    private int isJoin;



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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
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

    public String getCashSettleType() {
        return cashSettleType;
    }

    public void setCashSettleType(String cashSettleType) {
        this.cashSettleType = cashSettleType;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public Store build(Store store) {
        return Store.newBuild(store)
                .city(this.city)
                .detailAddr(this.detailAddr)
                .detailAddrChn(this.detailAddrChn)
                .description(this.description)
                .descriptionChn(this.descriptionChn)
                .shopHour(this.shopHour)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .mobile(this.mobile)
                .logoUrl(this.logoUrl)
                .merchantId(this.merchantId)
                .merchantNm(this.merchantNm)
                .gatewayPw(this.gatewayPw)
                .transidType(this.transidType)
                .isSelfservice(this.isSelfservice)
                .isDelivery(this.isDelivery)
                .selfserviceUseyn(this.selfserviceUseyn)
                .deliveryUseyn(this.deliveryUseyn)
                .isPaySet(store.isPaySet())
                .isOpening(this.isOpening)
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
                .chargeType(store.chargeType())
                .chargeRate(store.chargeRate())
                .chargeFee(store.chargeFee())
                .tableCount(store.tableCount())
                .storeExtendList(this.storeExtendList)
                .storeAttachList(this.attachList)
                .cashSettleType(this.cashSettleType)
                .isJoin(this.isJoin)
                .build();
    }

    User buildManager(User manager) {
        return User.newUser(manager)
                .email(this.managerEmail)
                .name(this.managerName)
                .mobile(this.managerPhone)
                .build();
    }

    public List<StoreCategory> buildStoreCategoryList(Store store) {
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
}

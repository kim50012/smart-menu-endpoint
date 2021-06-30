package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:56 2019/2/11
 **/
public class UserOrderDTO {
    private String openId;

    private String nickName;

    private String headImgUrl;

    private String storeName;
    
    private String custNo;

    private int qty;

    private BigDecimal sumAmount;

    private BigDecimal agtFee;

    private String laterTrainDate;

    private String laterStore;  //最近交易店铺

    private String subscribeTime;

    private String bindTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public BigDecimal getAgtFee() {
        return agtFee;
    }

    public void setAgtFee(BigDecimal agtFee) {
        this.agtFee = agtFee;
    }

    public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

    public String getLaterTrainDate() {
        return laterTrainDate;
    }

    public void setLaterTrainDate(String laterTrainDate) {
        this.laterTrainDate = laterTrainDate;
    }

    public String getLaterStore() {
        return laterStore;
    }

    public void setLaterStore(String laterStore) {
        this.laterStore = laterStore;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getBindTime() {
        return bindTime;
    }

    public void setBindTime(String bindTime) {
        this.bindTime = bindTime;
    }
}

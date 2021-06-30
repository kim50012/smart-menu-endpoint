package com.basoft.eorder.interfaces.query.baSettle;

import lombok.Data;

import java.util.Date;

@Data
public class StoreSettlementDayBaDTO {
	//columns START
	private Long sid;  //结算编号
	private Long storeId;  //所属商户ID
	private String settleDate;  //结算年月日
	private Integer settleType;  //结算收费类型 1-按营业额百分比 2-按营业额百分比或最小服务费 3-按月
	private Integer settleRate;  //结算按营业额百分比收费的百分率。取值范围为0-100
	private Integer settleFee;  //第一种情况：当收费类型为2，此字段存储最小服务费；第二种情况，当收费类型为3，此字段存储按月收费的收费金额。韩币单位
	private Integer orderCount;  //结算订单数量
	private Long settleSum;  //结算总金额
	private Long sumAmount;  //结算总金额
	private Long amount;
	private String pgDate;  //PG结算日期
	private Long pgSum;  //PG结算总金额
	private Long pgFee;  //PG结算服务费
	private String closingMonths;  //平台结算月
	private String plDate;  //平台结算日期
	private Long plMinFee;  //平台最小服务费
	private Long plServiceFee;  //平台使用服务费
	private Long plFinalFee;  //平台结算服务费
	private Long serviceFeeSum;  //总服务费
	private Long profitSum;  //总结算金额
	private Long cashSettleSum;  //BA交易金结算总金额，即BA需要支付给商户的交易金（结算总金额-PG结算服务费-平台结算服务费）
	private Date createTime;  //结算时间
	private String createUser;  //结算人
	private Date updateTime;  //修改时间
	private String updateUser;  //修改人
	//columns END

}


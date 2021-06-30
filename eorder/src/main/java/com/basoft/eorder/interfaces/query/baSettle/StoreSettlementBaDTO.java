package com.basoft.eorder.interfaces.query.baSettle;

import lombok.Data;

import java.util.Date;

@Data
public class StoreSettlementBaDTO {
	//columns START
	private Long sid;  //结算编号
	private Long storeId;  //所属商户ID
	private String settleYearMonth;  //结算年月
	private Integer settleYear;  //结算年
	private Integer settleMonth;  //结算月
	private String startDt;  //结算起始日期
	private String endDt;  //结算起始日期
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
	private String plDate;  //平台结算日期
	private Long plMinFee;  //平台最小服务费
	private Long plServiceFee;  //平台使用服务费
	private Long plFinalFee;  //平台结算服务费
	private Long serviceFeeSum;//平台服务费加pg服务费
	private Long cashSettleSum;  //BA交易金结算总金额，即BA需要支付给商户的交易金（结算总金额-PG结算服务费-平台结算服务费）
	private String closingMonths;//结算月
	private String stBankName;//交易金结算-BA结算时，商户接收交易金银行名称
	private String stBankAcc;//交易金结算-BA结算时，商户接收交易金银行卡号
	private String stBankAccName;//交易金结算-BA结算时，商户接收交易金银行卡账户名称
	private int cloStatus;//0:等待支付,1:支付完成,2:删除
	private Date createTime;  //结算时间
	private String createUser;  //结算人
	private Date updateTime;  //修改时间
	private String updateUser;  //修改人

	private String storeName;
	private String storeType;
	private String city;
	private String areaNm;
	//columns END

}


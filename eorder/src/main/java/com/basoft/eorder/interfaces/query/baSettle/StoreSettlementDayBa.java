package com.basoft.eorder.interfaces.query.baSettle;

import lombok.Data;

import java.util.Date;

@Data
public class StoreSettlementDayBa {

	//columns START
	private Long sid;  //结算编号

	private Long storeId;  //所属商户ID

	private Date settleDate;  //结算年月日

	private Integer settleType;  //结算收费类型 1-按营业额百分比 2-按营业额百分比或最小服务费 3-按月

	private Integer settleRate;  //结算按营业额百分比收费的百分率。取值范围为0-100

	private Integer settleFee;  //第一种情况：当收费类型为2，此字段存储最小服务费；第二种情况，当收费类型为3，此字段存储按月收费的收费金额。韩币单位

	private Integer orderCount;  //结算订单数量

	private Long settleSum;  //结算总金额

	private String pgDate;  //PG结算日期

	private Long pgSum;  //PG结算总金额

	private Long pgServiceFee;  //PG结算服务费

	private String plDate;  //平台结算日期

	private Long plMinFee;  //平台最小服务费

	private Long plServiceFee;  //平台使用服务费

	private Long plFinalFee;  //平台结算服务费

	private Long cashSettleSum;  //BA交易金结算总金额，即BA需要支付给商户的交易金（结算总金额-PG结算服务费-平台结算服务费）

	private Date createTime;  //结算时间

	private String createUser;  //结算人

	private Date updateTime;  //修改时间

	private String updateUser;  //修改人
	//columns END

	public static final class Builder {
		//columns START
		private Long sid;  //结算编号

		private Long storeId;  //所属商户ID

		private Date settleDate;  //结算年月日

		private Integer settleType;  //结算收费类型 1-按营业额百分比 2-按营业额百分比或最小服务费 3-按月

		private Integer settleRate;  //结算按营业额百分比收费的百分率。取值范围为0-100

		private Integer settleFee;  //第一种情况：当收费类型为2，此字段存储最小服务费；第二种情况，当收费类型为3，此字段存储按月收费的收费金额。韩币单位

		private Integer orderCount;  //结算订单数量

		private Long settleSum;  //结算总金额

		private String pgDate;  //PG结算日期

		private Long pgSum;  //PG结算总金额

		private Long pgServiceFee;  //PG结算服务费

		private String plDate;  //平台结算日期

		private Long plMinFee;  //平台最小服务费

		private Long plServiceFee;  //平台使用服务费

		private Long plFinalFee;  //平台结算服务费

		private Long cashSettleSum;  //BA交易金结算总金额，即BA需要支付给商户的交易金（结算总金额-PG结算服务费-平台结算服务费）

		private Date createTime;  //结算时间

		private String createUser;  //结算人

		private Date updateTime;  //修改时间

		private String updateUser;  //修改人

		//columns END
		public Builder sid(Long sid) {
			this.sid = sid;
			return this;
		}

		public Builder storeId(Long storeId) {
			this.storeId = storeId;
			return this;
		}

		public Builder settleDate(Date settleDate) {
			this.settleDate = settleDate;
			return this;
		}

		public Builder settleType(Integer settleType) {
			this.settleType = settleType;
			return this;
		}

		public Builder settleRate(Integer settleRate) {
			this.settleRate = settleRate;
			return this;
		}

		public Builder settleFee(Integer settleFee) {
			this.settleFee = settleFee;
			return this;
		}

		public Builder orderCount(Integer orderCount) {
			this.orderCount = orderCount;
			return this;
		}

		public Builder settleSum(Long settleSum) {
			this.settleSum = settleSum;
			return this;
		}

		public Builder pgDate(String pgDate) {
			this.pgDate = pgDate;
			return this;
		}

		public Builder pgSum(Long pgSum) {
			this.pgSum = pgSum;
			return this;
		}

		public Builder pgServiceFee(Long pgServiceFee) {
			this.pgServiceFee = pgServiceFee;
			return this;
		}

		public Builder plDate(String plDate) {
			this.plDate = plDate;
			return this;
		}

		public Builder plMinFee(Long plMinFee) {
			this.plMinFee = plMinFee;
			return this;
		}

		public Builder plServiceFee(Long plServiceFee) {
			this.plServiceFee = plServiceFee;
			return this;
		}

		public Builder plFinalFee(Long plFinalFee) {
			this.plFinalFee = plFinalFee;
			return this;
		}

		public Builder cashSettleSum(Long cashSettleSum) {
			this.cashSettleSum = cashSettleSum;
			return this;
		}

		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder createUser(String createUser) {
			this.createUser = createUser;
			return this;
		}

		public Builder updateTime(Date updateTime) {
			this.updateTime = updateTime;
			return this;
		}

		public Builder updateUser(String updateUser) {
			this.updateUser = updateUser;
			return this;
		}

		public StoreSettlementDayBa build() {
			StoreSettlementDayBa storeSettlementDayBa = new StoreSettlementDayBa();
			storeSettlementDayBa.sid = this.sid;
			storeSettlementDayBa.storeId = this.storeId;
			storeSettlementDayBa.settleDate = this.settleDate;
			storeSettlementDayBa.settleType = this.settleType;
			storeSettlementDayBa.settleRate = this.settleRate;
			storeSettlementDayBa.settleFee = this.settleFee;
			storeSettlementDayBa.orderCount = this.orderCount;
			storeSettlementDayBa.settleSum = this.settleSum;
			storeSettlementDayBa.pgDate = this.pgDate;
			storeSettlementDayBa.pgSum = this.pgSum;
			storeSettlementDayBa.pgServiceFee = this.pgServiceFee;
			storeSettlementDayBa.plDate = this.plDate;
			storeSettlementDayBa.plMinFee = this.plMinFee;
			storeSettlementDayBa.plServiceFee = this.plServiceFee;
			storeSettlementDayBa.plFinalFee = this.plFinalFee;
			storeSettlementDayBa.cashSettleSum = this.cashSettleSum;
			storeSettlementDayBa.createTime = this.createTime;
			storeSettlementDayBa.createUser = this.createUser;
			storeSettlementDayBa.updateTime = this.updateTime;
			storeSettlementDayBa.updateUser = this.updateUser;
			return storeSettlementDayBa;
		}
	}
}


package com.basoft.eorder.interfaces.query.baSettle;

import lombok.Data;

import java.util.Date;

@Data
public class StoreSettlementBa {
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

		public Builder settleYearMonth(String settleYearMonth) {
			this.settleYearMonth = settleYearMonth;
			return this;
		}

		public Builder settleYear(Integer settleYear) {
			this.settleYear = settleYear;
			return this;
		}

		public Builder settleMonth(Integer settleMonth) {
			this.settleMonth = settleMonth;
			return this;
		}

		public Builder startDt(String startDt) {
			this.startDt = startDt;
			return this;
		}

		public Builder endDt(String endDt) {
			this.endDt = endDt;
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

		public Builder createTime(java.util.Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder createUser(String createUser) {
			this.createUser = createUser;
			return this;
		}

		public Builder updateTime(java.util.Date updateTime) {
			this.updateTime = updateTime;
			return this;
		}

		public Builder updateUser(String updateUser) {
			this.updateUser = updateUser;
			return this;
		}

		public StoreSettlementBa build() {
			StoreSettlementBa storeSettlementBa = new StoreSettlementBa();
			storeSettlementBa.sid = this.sid;
			storeSettlementBa.storeId = this.storeId;
			storeSettlementBa.settleYearMonth = this.settleYearMonth;
			storeSettlementBa.settleYear = this.settleYear;
			storeSettlementBa.settleMonth = this.settleMonth;
			storeSettlementBa.startDt = this.startDt;
			storeSettlementBa.endDt = this.endDt;
			storeSettlementBa.settleType = this.settleType;
			storeSettlementBa.settleRate = this.settleRate;
			storeSettlementBa.settleFee = this.settleFee;
			storeSettlementBa.orderCount = this.orderCount;
			storeSettlementBa.settleSum = this.settleSum;
			storeSettlementBa.pgDate = this.pgDate;
			storeSettlementBa.pgSum = this.pgSum;
			storeSettlementBa.pgServiceFee = this.pgServiceFee;
			storeSettlementBa.plDate = this.plDate;
			storeSettlementBa.plMinFee = this.plMinFee;
			storeSettlementBa.plServiceFee = this.plServiceFee;
			storeSettlementBa.plFinalFee = this.plFinalFee;
			storeSettlementBa.cashSettleSum = this.cashSettleSum;
			storeSettlementBa.createTime = this.createTime;
			storeSettlementBa.createUser = this.createUser;
			storeSettlementBa.updateTime = this.updateTime;
			storeSettlementBa.updateUser = this.updateUser;
			return storeSettlementBa;
		}
	}
}


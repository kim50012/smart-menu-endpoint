package com.basoft.eorder.interfaces.query.inventory.hotel;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class InventoryHotelDTO implements Comparable<InventoryHotelDTO>{
	//columns START
	private Long invId;  //invId  db_column: INV_ID

	private Long storeId;  //酒店ID  db_column: STORE_ID

	private Long prodId;  //酒店库存对应的产品  db_column: PROD_ID

	private String productNmKor;  //产品名称韩文

	private String productNmChn;  //产品名称中文

	private Long prodSkuId;  //酒店库存对应的产品SKU  db_column: PROD_SKU_ID

	private String skuNmKor;  //产品名称韩文

	private String skuNmChn;  //产品名称中文

	private String price;

	private String disPrice;

	private String priceSettle;

	private String disPriceSettle;

	private String priceDefault;

	private String priceSettleDefault;//默认到手价

	private Integer invYear;  //库存年  db_column: INV_YEAR

	private Integer invMonth;  //库存月  db_column: INV_MONTH

	private Integer invDay;  //库存日  db_column: INV_DAY

	@ExcelColumn(valueKor = "invDate", col = 1)
	private String invDate;  //库存日期  db_column: INV_DATE

	private String isOpening;  //当前日期是否开放该房间，默认值为1 1-开放 0-关闭  db_column: IS_OPENING

	@ExcelColumn(valueKor = "invTotal", col = 2)
	private String invTotal;  //当前日期的库存总量，可修改，设定值要大于已订购数量。  db_column: INV_TOTAL

	@ExcelColumn(valueKor = "invUsed", col = 3)
	private String invUsed;  //当前日期的被订购总量，小于等于库存总量。  db_column: INV_USED

	@ExcelColumn(valueKor = "invBalance", col = 4)
	private String invBalance;  //当前日期的库存余量。由于库存总量可以随时被修改，所以此字段无参考意义  db_column: INV_BALANCE

	private Date createTime;  //创建时间생성시간  db_column: CREATE_TIME

	private String createUser;  //创建人  db_column: CREATE_USER

	private Date updateTime;  //修改时间수정시간  db_column: UPDATE_TIME

	private String updateUser;  //修改人  db_column: UPDATE_USER

	public static final class FailMsg{
		private String productNmKor;  //产品名称韩文
		private String productNmChn;  //产品名称中文
		private String erroMsgCode;
		private String invDate;
		private String invUsed;

		public String getProductNmKor() {
			return productNmKor;
		}

		public void setProductNmKor(String productNmKor) {
			this.productNmKor = productNmKor;
		}

		public String getProductNmChn() {
			return productNmChn;
		}

		public void setProductNmChn(String productNmChn) {
			this.productNmChn = productNmChn;
		}

		public String getErroMsgCode() {
			return erroMsgCode;
		}

		public void setErroMsgCode(String erroMsgCode) {
			this.erroMsgCode = erroMsgCode;
		}

		public String getInvDate() {
			return invDate;
		}

		public void setInvDate(String invDate) {
			this.invDate = invDate;
		}

		public String getInvUsed() {
			return invUsed;
		}

		public void setInvUsed(String invUsed) {
			this.invUsed = invUsed;
		}
	}

	public static final class TopPrice {
		private Long minPrice;
		private Long maxPrice;
	}
	public static final class InvDateAndInv {
		private Long prodId;
		private BigDecimal price;//价格
		private BigDecimal disPrice;//折扣价格
		private BigDecimal priceSettle;
		private BigDecimal disPriceSettle;
		private String invTotal;
		private String invUsed;
		private String invBalance;
		private String invDate;
		private String isOpening;

		public Long getProdId() {
			return prodId;
		}

		public void setProdId(Long prodId) {
			this.prodId = prodId;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public BigDecimal getDisPrice() {
			return disPrice;
		}

		public void setDisPrice(BigDecimal disPrice) {
			this.disPrice = disPrice;
		}

		public BigDecimal getPriceSettle() {
			return priceSettle;
		}

		public void setPriceSettle(BigDecimal priceSettle) {
			this.priceSettle = priceSettle;
		}

		public BigDecimal getDisPriceSettle() {
			return disPriceSettle;
		}

		public void setDisPriceSettle(BigDecimal disPriceSettle) {
			this.disPriceSettle = disPriceSettle;
		}

		public String getInvTotal() {
			return invTotal;
		}

		public void setInvTotal(String invTotal) {
			this.invTotal = invTotal;
		}

		public String getInvUsed() {
			return invUsed;
		}

		public void setInvUsed(String invUsed) {
			this.invUsed = invUsed;
		}

		public String getInvBalance() {
			return invBalance;
		}

		public void setInvBalance(String invBalance) {
			this.invBalance = invBalance;
		}

		public String getInvDate() {
			return invDate;
		}

		public void setInvDate(String invDate) {
			this.invDate = invDate;
		}

		public String getIsOpening() {
			return isOpening;
		}

		public void setIsOpening(String isOpening) {
			this.isOpening = isOpening;
		}
	}

	private List<InvDateAndInv> invDateAndTotalList = new LinkedList<>();

	private List<String> invDateList = new LinkedList<>();

	//columns END



	/**
	 * 重写比较方法
	 *
	 * @param inventoryHotel
	 * @return
	 */
	@Override
	public int compareTo(InventoryHotelDTO inventoryHotel) {
		Integer currentInventory = Integer.valueOf(this.getInvBalance());
		Integer inputInventory = Integer.valueOf(inventoryHotel.getInvBalance());

		// 升序
		return currentInventory.compareTo(inputInventory);

		// 降序
		//return inputInventory.compareTo(currentInventory);
	}

	@Override
	public String toString() {
		return "InventoryHotelDTO{" +
				"storeId=" + storeId +
				", prodId=" + prodId +
				", productNmKor='" + productNmKor + '\'' +
				", productNmChn='" + productNmChn + '\'' +
				", prodSkuId=" + prodSkuId +
				", skuNmKor='" + skuNmKor + '\'' +
				", skuNmChn='" + skuNmChn + '\'' +
				", price='" + price + '\'' +
				", disPrice='" + disPrice + '\'' +
				", priceDefault='" + priceDefault + '\'' +
				", invYear=" + invYear +
				", invMonth=" + invMonth +
				", invDay=" + invDay +
				", invDate='" + invDate + '\'' +
				", isOpening='" + isOpening + '\'' +
				", invTotal='" + invTotal + '\'' +
				", invUsed='" + invUsed + '\'' +
				", invBalance='" + invBalance + '\'' +
				'}';
	}
}


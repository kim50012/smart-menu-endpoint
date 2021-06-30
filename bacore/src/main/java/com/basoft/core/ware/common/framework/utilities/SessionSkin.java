package com.basoft.core.ware.common.framework.utilities;

public class SessionSkin implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Integer shopId;
	private Integer skinTypeShop;
	private Integer skinTypeWeb;
	private Integer pageIdShopMain;
	private Integer pageIdShopList;
	private Integer pageIdShopDetail;
	private Integer pageIdShopHome;
	private String shopLogoImgUrl;
	private String shopName;
	private String shopQRImgUrl;
	private String shopWechatId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getPageIdShopMain() {
		return pageIdShopMain;
	}

	public void setPageIdShopMain(Integer pageIdShopMain) {
		this.pageIdShopMain = pageIdShopMain;
	}

	public Integer getSkinTypeWeb() {
		return skinTypeWeb;
	}

	public void setSkinTypeWeb(Integer skinTypeWeb) {
		this.skinTypeWeb = skinTypeWeb;
	}

	public Integer getSkinTypeShop() {
		return skinTypeShop;
	}

	public void setSkinTypeShop(Integer skinTypeShop) {
		this.skinTypeShop = skinTypeShop;
	}

	public Integer getPageIdShopDetail() {
		return pageIdShopDetail;
	}

	public void setPageIdShopDetail(Integer pageIdShopDetail) {
		this.pageIdShopDetail = pageIdShopDetail;
	}

	public Integer getPageIdShopList() {
		return pageIdShopList;
	}

	public void setPageIdShopList(Integer pageIdShopList) {
		this.pageIdShopList = pageIdShopList;
	}

	public Integer getPageIdShopHome() {
		return pageIdShopHome;
	}

	public void setPageIdShopHome(Integer pageIdShopHome) {
		this.pageIdShopHome = pageIdShopHome;
	}

	public String getShopLogoImgUrl() {
		return shopLogoImgUrl;
	}

	public void setShopLogoImgUrl(String shopLogoImgUrl) {
		this.shopLogoImgUrl = shopLogoImgUrl;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopQRImgUrl() {
		return shopQRImgUrl;
	}

	public void setShopQRImgUrl(String shopQRImgUrl) {
		this.shopQRImgUrl = shopQRImgUrl;
	}

	public String getShopWechatId() {
		return shopWechatId;
	}

	public void setShopWechatId(String shopWechatId) {
		this.shopWechatId = shopWechatId;
	}

	@Override
	public String toString() {
		return "SessionSkin [shopId=" + shopId + ", skinTypeShop=" + skinTypeShop + ", skinTypeWeb=" + skinTypeWeb
				+ ", pageIdShopMain=" + pageIdShopMain + ", pageIdShopList=" + pageIdShopList + ", pageIdShopDetail="
				+ pageIdShopDetail + ", pageIdShopHome=" + pageIdShopHome + ", shopLogoImgUrl=" + shopLogoImgUrl
				+ ", shopName=" + shopName + ", shopQRImgUrl=" + shopQRImgUrl + ", shopWechatId=" + shopWechatId + "]";
	}
}
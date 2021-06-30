package com.basoft.service.entity.shop;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.util.Date;

public class Shop {
	@JsonSerialize(using = ToStringSerializer.class )
    private Long shopId;

    private String shopNm;

    private Integer gCorpId;

    private Integer mktId;

    private Byte mktIsMainshop;

    private String wxOpenidS;

    private String wxIdS;

    private String wxNickName;

    private Byte wxType;

    private String wxAppId;

    private String wxAppSecret;

    private String wxApiUrl;

    private String wxApiToken;

    private Byte wxIsUseQuikFollow;

    private String corpNm;

    private String corpLicenseNo;

    private String legalPersonNm;

    private String legalPersonIdcard;

    private String contactNm;

    private String contactMobileNo;

    private String contactEmail;

    private String contactQq;

    private String contactHeadimg;

    private Integer adrProvinceId;

    private Integer adrCityId;

    private Integer adrDistrictId;

    private String adrDetail;

    private String adrZip;

    private String adrTel;

    private Integer scId;

    private Integer mainGcId;

    private Integer sgId;

    private Byte stsId;

    private String stsDesc;

    private Date openedDt;

    private Date closedDt;

    private Byte authTypeId;

    private Byte authIsSucc;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long authImgCert1;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long authImgCert2;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long authImgCert3;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long authImgCert4;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long authImgCert5;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long imgLogoPic;
    
    // 扩展
    private String imgLogoPicFullURL;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long imgLogoLabel;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long imgBanner;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long imgWatermark;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long img2dBarcode;

    private String descPageCaption;

    private String seoTag;

    private Short sumSalesCnt;

    private Short sumSalesQty;

    private BigDecimal sumSalesAmt;

    private Short sumCollectedQty;

    private Byte avgPraiseRate;

    private Byte avgGoodsDescScore;

    private Byte avgServiceScore;

    private Byte avgDeliveryRate;

    private String createdUserId;

    private Date modifiedDt;

    private Date createdDt;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopNm() {
        return shopNm;
    }

    public void setShopNm(String shopNm) {
        this.shopNm = shopNm == null ? null : shopNm.trim();
    }

    public Integer getgCorpId() {
        return gCorpId;
    }

    public void setgCorpId(Integer gCorpId) {
        this.gCorpId = gCorpId;
    }

    public Integer getMktId() {
        return mktId;
    }

    public void setMktId(Integer mktId) {
        this.mktId = mktId;
    }

    public Byte getMktIsMainshop() {
        return mktIsMainshop;
    }

    public void setMktIsMainshop(Byte mktIsMainshop) {
        this.mktIsMainshop = mktIsMainshop;
    }

    public String getWxOpenidS() {
        return wxOpenidS;
    }

    public void setWxOpenidS(String wxOpenidS) {
        this.wxOpenidS = wxOpenidS == null ? null : wxOpenidS.trim();
    }

    public String getWxIdS() {
        return wxIdS;
    }

    public void setWxIdS(String wxIdS) {
        this.wxIdS = wxIdS == null ? null : wxIdS.trim();
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName == null ? null : wxNickName.trim();
    }

    public Byte getWxType() {
        return wxType;
    }

    public void setWxType(Byte wxType) {
        this.wxType = wxType;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId == null ? null : wxAppId.trim();
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret == null ? null : wxAppSecret.trim();
    }

    public String getWxApiUrl() {
        return wxApiUrl;
    }

    public void setWxApiUrl(String wxApiUrl) {
        this.wxApiUrl = wxApiUrl == null ? null : wxApiUrl.trim();
    }

    public String getWxApiToken() {
        return wxApiToken;
    }

    public void setWxApiToken(String wxApiToken) {
        this.wxApiToken = wxApiToken == null ? null : wxApiToken.trim();
    }

    public Byte getWxIsUseQuikFollow() {
        return wxIsUseQuikFollow;
    }

    public void setWxIsUseQuikFollow(Byte wxIsUseQuikFollow) {
        this.wxIsUseQuikFollow = wxIsUseQuikFollow;
    }

    public String getCorpNm() {
        return corpNm;
    }

    public void setCorpNm(String corpNm) {
        this.corpNm = corpNm == null ? null : corpNm.trim();
    }

    public String getCorpLicenseNo() {
        return corpLicenseNo;
    }

    public void setCorpLicenseNo(String corpLicenseNo) {
        this.corpLicenseNo = corpLicenseNo == null ? null : corpLicenseNo.trim();
    }

    public String getLegalPersonNm() {
        return legalPersonNm;
    }

    public void setLegalPersonNm(String legalPersonNm) {
        this.legalPersonNm = legalPersonNm == null ? null : legalPersonNm.trim();
    }

    public String getLegalPersonIdcard() {
        return legalPersonIdcard;
    }

    public void setLegalPersonIdcard(String legalPersonIdcard) {
        this.legalPersonIdcard = legalPersonIdcard == null ? null : legalPersonIdcard.trim();
    }

    public String getContactNm() {
        return contactNm;
    }

    public void setContactNm(String contactNm) {
        this.contactNm = contactNm == null ? null : contactNm.trim();
    }

    public String getContactMobileNo() {
        return contactMobileNo;
    }

    public void setContactMobileNo(String contactMobileNo) {
        this.contactMobileNo = contactMobileNo == null ? null : contactMobileNo.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public String getContactQq() {
        return contactQq;
    }

    public void setContactQq(String contactQq) {
        this.contactQq = contactQq == null ? null : contactQq.trim();
    }

    public String getContactHeadimg() {
        return contactHeadimg;
    }

    public void setContactHeadimg(String contactHeadimg) {
        this.contactHeadimg = contactHeadimg == null ? null : contactHeadimg.trim();
    }

    public Integer getAdrProvinceId() {
        return adrProvinceId;
    }

    public void setAdrProvinceId(Integer adrProvinceId) {
        this.adrProvinceId = adrProvinceId;
    }

    public Integer getAdrCityId() {
        return adrCityId;
    }

    public void setAdrCityId(Integer adrCityId) {
        this.adrCityId = adrCityId;
    }

    public Integer getAdrDistrictId() {
        return adrDistrictId;
    }

    public void setAdrDistrictId(Integer adrDistrictId) {
        this.adrDistrictId = adrDistrictId;
    }

    public String getAdrDetail() {
        return adrDetail;
    }

    public void setAdrDetail(String adrDetail) {
        this.adrDetail = adrDetail == null ? null : adrDetail.trim();
    }

    public String getAdrZip() {
        return adrZip;
    }

    public void setAdrZip(String adrZip) {
        this.adrZip = adrZip == null ? null : adrZip.trim();
    }

    public String getAdrTel() {
        return adrTel;
    }

    public void setAdrTel(String adrTel) {
        this.adrTel = adrTel == null ? null : adrTel.trim();
    }

    public Integer getScId() {
        return scId;
    }

    public void setScId(Integer scId) {
        this.scId = scId;
    }

    public Integer getMainGcId() {
        return mainGcId;
    }

    public void setMainGcId(Integer mainGcId) {
        this.mainGcId = mainGcId;
    }

    public Integer getSgId() {
        return sgId;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public Byte getStsId() {
        return stsId;
    }

    public void setStsId(Byte stsId) {
        this.stsId = stsId;
    }

    public String getStsDesc() {
        return stsDesc;
    }

    public void setStsDesc(String stsDesc) {
        this.stsDesc = stsDesc == null ? null : stsDesc.trim();
    }

    public Date getOpenedDt() {
        return openedDt;
    }

    public void setOpenedDt(Date openedDt) {
        this.openedDt = openedDt;
    }

    public Date getClosedDt() {
        return closedDt;
    }

    public void setClosedDt(Date closedDt) {
        this.closedDt = closedDt;
    }

    public Byte getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId(Byte authTypeId) {
        this.authTypeId = authTypeId;
    }

    public Byte getAuthIsSucc() {
        return authIsSucc;
    }

    public void setAuthIsSucc(Byte authIsSucc) {
        this.authIsSucc = authIsSucc;
    }

    public Long getAuthImgCert1() {
        return authImgCert1;
    }

    public void setAuthImgCert1(Long authImgCert1) {
        this.authImgCert1 = authImgCert1;
    }

    public Long getAuthImgCert2() {
        return authImgCert2;
    }

    public void setAuthImgCert2(Long authImgCert2) {
        this.authImgCert2 = authImgCert2;
    }

    public Long getAuthImgCert3() {
        return authImgCert3;
    }

    public void setAuthImgCert3(Long authImgCert3) {
        this.authImgCert3 = authImgCert3;
    }

    public Long getAuthImgCert4() {
        return authImgCert4;
    }

    public void setAuthImgCert4(Long authImgCert4) {
        this.authImgCert4 = authImgCert4;
    }

    public Long getAuthImgCert5() {
        return authImgCert5;
    }

    public void setAuthImgCert5(Long authImgCert5) {
        this.authImgCert5 = authImgCert5;
    }

    public Long getImgLogoPic() {
        return imgLogoPic;
    }

    public void setImgLogoPic(Long imgLogoPic) {
        this.imgLogoPic = imgLogoPic;
    }

    public Long getImgLogoLabel() {
        return imgLogoLabel;
    }

    public void setImgLogoLabel(Long imgLogoLabel) {
        this.imgLogoLabel = imgLogoLabel;
    }

    public Long getImgBanner() {
        return imgBanner;
    }

    public void setImgBanner(Long imgBanner) {
        this.imgBanner = imgBanner;
    }

    public Long getImgWatermark() {
        return imgWatermark;
    }

    public void setImgWatermark(Long imgWatermark) {
        this.imgWatermark = imgWatermark;
    }

    public Long getImg2dBarcode() {
        return img2dBarcode;
    }

    public void setImg2dBarcode(Long img2dBarcode) {
        this.img2dBarcode = img2dBarcode;
    }

    public String getDescPageCaption() {
        return descPageCaption;
    }

    public void setDescPageCaption(String descPageCaption) {
        this.descPageCaption = descPageCaption == null ? null : descPageCaption.trim();
    }

    public String getSeoTag() {
        return seoTag;
    }

    public void setSeoTag(String seoTag) {
        this.seoTag = seoTag == null ? null : seoTag.trim();
    }

    public Short getSumSalesCnt() {
        return sumSalesCnt;
    }

    public void setSumSalesCnt(Short sumSalesCnt) {
        this.sumSalesCnt = sumSalesCnt;
    }

    public Short getSumSalesQty() {
        return sumSalesQty;
    }

    public void setSumSalesQty(Short sumSalesQty) {
        this.sumSalesQty = sumSalesQty;
    }

    public BigDecimal getSumSalesAmt() {
        return sumSalesAmt;
    }

    public void setSumSalesAmt(BigDecimal sumSalesAmt) {
        this.sumSalesAmt = sumSalesAmt;
    }

    public Short getSumCollectedQty() {
        return sumCollectedQty;
    }

    public void setSumCollectedQty(Short sumCollectedQty) {
        this.sumCollectedQty = sumCollectedQty;
    }

    public Byte getAvgPraiseRate() {
        return avgPraiseRate;
    }

    public void setAvgPraiseRate(Byte avgPraiseRate) {
        this.avgPraiseRate = avgPraiseRate;
    }

    public Byte getAvgGoodsDescScore() {
        return avgGoodsDescScore;
    }

    public void setAvgGoodsDescScore(Byte avgGoodsDescScore) {
        this.avgGoodsDescScore = avgGoodsDescScore;
    }

    public Byte getAvgServiceScore() {
        return avgServiceScore;
    }

    public void setAvgServiceScore(Byte avgServiceScore) {
        this.avgServiceScore = avgServiceScore;
    }

    public Byte getAvgDeliveryRate() {
        return avgDeliveryRate;
    }

    public void setAvgDeliveryRate(Byte avgDeliveryRate) {
        this.avgDeliveryRate = avgDeliveryRate;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId == null ? null : createdUserId.trim();
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

	public String getImgLogoPicFullURL() {
		return imgLogoPicFullURL;
	}

	public void setImgLogoPicFullURL(String imgLogoPicFullURL) {
		this.imgLogoPicFullURL = imgLogoPicFullURL;
	}
}
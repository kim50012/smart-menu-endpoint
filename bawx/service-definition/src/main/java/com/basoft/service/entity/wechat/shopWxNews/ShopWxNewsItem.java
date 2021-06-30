package com.basoft.service.entity.wechat.shopWxNews;

import java.util.Date;

public class ShopWxNewsItem extends ShopWxNewsItemKey {
    private Long mfileId;

    private String mauthor;

    private String mtitle;

    private Byte mshowCoverPic;

    private Integer isDelete;

    private String createdId;

    private String modifiedId;

    private Date createdDt;

    private Date modifiedDt;

    private Integer readCnt;

    private Integer listImg;

    private Integer newsType;

    public Long getMfileId() {
        return mfileId;
    }

    public void setMfileId(Long mfileId) {
        this.mfileId = mfileId;
    }

    public String getMauthor() {
        return mauthor;
    }

    public void setMauthor(String mauthor) {
        this.mauthor = mauthor == null ? null : mauthor.trim();
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle == null ? null : mtitle.trim();
    }

    public Byte getMshowCoverPic() {
        return mshowCoverPic;
    }

    public void setMshowCoverPic(Byte mshowCoverPic) {
        this.mshowCoverPic = mshowCoverPic;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    public String getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(String modifiedId) {
        this.modifiedId = modifiedId == null ? null : modifiedId.trim();
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Integer getReadCnt() {
        return readCnt;
    }

    public void setReadCnt(Integer readCnt) {
        this.readCnt = readCnt;
    }

    public Integer getListImg() {
        return listImg;
    }

    public void setListImg(Integer listImg) {
        this.listImg = listImg;
    }

    public Integer getNewsType() {
        return newsType;
    }

    public void setNewsType(Integer newsType) {
        this.newsType = newsType;
    }
}
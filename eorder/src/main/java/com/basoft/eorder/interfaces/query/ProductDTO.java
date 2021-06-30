package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.interfaces.query.retail.cms.ProductAloneStandardDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {

    private Long id;		//
    private String nameKor;		//
    private String nameChn;
    private BigDecimal weight;		//
    private String detailDesc;
    private String detailChnDesc;
    private Long categoryId;		//
    private String categoryName;		//
//    private Long productGroupId;		//
//    private String prodctGroupName;		//
//    private String prodctGroupNameChn;
    private int status;		//0:不使用;1：使用中;2：删除
    private Long storeId;		//
    private String storeName;		//
    private String areaNm;		//
    private BigDecimal defaultPrice;
    private BigDecimal minPrice;
    private int isStandard;
    private int isInventory;
    private String created;		//
    private String updateTime;
    private String desChn;
    private String desKor;
    private int showIndex;
    private Integer recommend;
    private String fileId;
    private String fileName;
    private String fileSysName;
    private String fileType;
    private int fileSize;
    private String fileUrl;
    private String fileOriginalName;

    private Long prdGroupId;

    private String mainUrl;
    private List<ProductImageDTO> imageList = new LinkedList<>();
    private List<String> subImageUrl;

    private List<ProductAloneStandardDTO>  aloneStandardList;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSysName() {
        return fileSysName;
    }

    public void setFileSysName(String fileSysName) {
        this.fileSysName = fileSysName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    private List<ProductSkuDTO> psdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getDetailChnDesc() {
        return detailChnDesc;
    }

    public void setDetailChnDesc(String detailChnDesc) {
        this.detailChnDesc = detailChnDesc;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {

        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public int getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(int isStandard) {
        this.isStandard = isStandard;
    }

    public int getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(int isInventory) {
        this.isInventory = isInventory;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<ProductSkuDTO> getPsdList() {
        return psdList;
    }

    public void setPsdList(List<ProductSkuDTO> psdList) {
        this.psdList = psdList;
    }


    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getDesChn() {
        return desChn;
    }

    public void setDesChn(String desChn) {
        this.desChn = desChn;
    }

    public String getDesKor() {
        return desKor;
    }

    public void setDesKor(String desKor) {
        this.desKor = desKor;
    }

    public Long getPrdGroupId() { return prdGroupId; }

    public void setPrdGroupId(Long prdGroupId) { this.prdGroupId = prdGroupId; }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMainUrl() {
        if (imageList != null && imageList.size() > 0) {
            return imageList.stream().filter((img) -> img.getImageType() == 1).findFirst().map(ProductImageDTO::getImageUrl).get();
        }
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public List<ProductImageDTO> getImageList() {
        return imageList;
    }

    public void setImageList(List<ProductImageDTO> imageList) {
        this.imageList = imageList;
    }

    public List<String> getSubImageUrl() {
        if (imageList != null && imageList.size() > 0) {
            return imageList.stream().filter((img) -> img.getImageType() == 0).map(ProductImageDTO::getImageUrl).collect(Collectors.toList());
        }
        return subImageUrl;
    }

    public void setSubImageUrl(List<String> subImageUrl) {
        this.subImageUrl = subImageUrl;
    }

    public List<ProductAloneStandardDTO> getAloneStandardList() {
        return aloneStandardList;
    }

    public void setAloneStandardList(List<ProductAloneStandardDTO> aloneStandardList) {
        this.aloneStandardList = aloneStandardList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void addImage(ProductImageDTO productImageDTO) {
        this.imageList.add(productImageDTO);
    }
}

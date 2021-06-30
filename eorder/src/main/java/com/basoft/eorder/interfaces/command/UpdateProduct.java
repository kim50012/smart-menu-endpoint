package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.StoreOption;
import com.basoft.eorder.interfaces.query.StoreOptionDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.basoft.eorder.domain.model.Product.SImage;
import static com.basoft.eorder.domain.model.Product.update;

public class UpdateProduct implements Command {


    private static Logger logger = LoggerFactory.getLogger(UpdateProduct.class);

    static final String NAME ="updateProduct";

    private Long id;
   // private Long groupId;
    private String name;
    private String chnName;
    private String imageUrl;

    private int showIndex;
    private String desKor;//韩文说明
    private String desChn;//中文说明
    private int recommend;
    private Product.Status status;

    private Long categoryId;

    private String detailDesc; //韩文详细产品描述
    private String detailChnDesc;//中文详细产品描述


    private List<SaveProduct.SaveProductSku> skuList;
    private List<String> subImageList = new LinkedList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public String getDesKor() {
        return desKor;
    }

    public void setDesKor(String desKor) {
        this.desKor = desKor;
    }

    public String getDesChn() {
        return desChn;
    }

    public void setDesChn(String desChn) {
        this.desChn = desChn;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public List<SaveProduct.SaveProductSku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SaveProduct.SaveProductSku> skuList) {
        this.skuList = skuList;
    }

    public List<String> getSubImageList() {
        return subImageList;
    }

    public void setSubImageList(List<String> subImageList) {
        this.subImageList = subImageList;
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

    public Product.Status getStatus() {
        return status;
    }

    public void setStatus(Product.Status status) {
        this.status = status;
    }

    /*
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
*/

    public List<ProductSku> buildSku(Product product, StoreOptionDTO rootOption) {

        return this.getSkuList()
                    .stream()
                    .map(new Function<SaveProduct.SaveProductSku, ProductSku>() {
                        @Override
                        public ProductSku apply(SaveProduct.SaveProductSku saveProductSku) {
                            return saveProductSku.build(product, rootOption);
                        }
                    })
                    .collect(Collectors.toList());
    }

    public Product build(Product product, Category category) {



        if(StringUtils.isBlank(this.name)){
            throw new BizException(ErrorCode.PARAM_INVALID,"name is null");
        }


        final List<SImage> collect =
                subImageList.stream()
                        .map(new Function<String, SImage>() {
                            @Override
                            public SImage apply(String s) {
                                return SImage.subOf(s);
                            }
                        })
                        .collect(Collectors.toList());

        collect.add(SImage.mainOf(this.imageUrl));


        long count = collect.stream().filter((s) -> s.isMain()).count();

        if(count != 1){
            this.logger.warn("Main images size:"+count);
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return update(product)
                .clearImage()  //删除掉从原先从数据库里查出来的 图片list
                .imageList(collect)
                .name(this.name)
                .detailDescChn(this.detailChnDesc)
                .detailDescKor(this.detailDesc)
                .chnName(this.chnName)
                .showIndex(this.showIndex)
                .desChn(this.desChn)
                .desKor(this.desKor)
                .recommend(this.recommend)
                .status(this.status)
                .category(category)
                .build();
    }
}

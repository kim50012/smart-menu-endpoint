package com.basoft.eorder.domain.model;

import com.basoft.eorder.application.base.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Author:Woonill
 * @Description:
 * @Date Created in 下午3:46 2018/12/4
 * @Modify 18:01 2018/12/5
 **/
public class Product {
    public enum Status {
        NORMAL(0), OPEN(1), CLOSED(2), DELETED(3);
        private int code;

        Status(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }

        public static Status valueOf(Integer value) {
            for (Status e : Status.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    private Long id; //for db
    private Store store;
    private Category category;
    private String name;
    private String chnName;
    private BigDecimal weight;
    //    private String imageUrl;//产品图片
    private Status status;
    private int isDeposit;
    private List<SImage> imagess = new ArrayList<>();


    public enum Recommend {
        NORMAL(0), OPEN(1);
        private int code;

        private Recommend(int code) {
            code = code;
        }

        public int code() {
            return code;
        }
    }


    public Long id() {
        return this.id;
    }

    public Store store() {
        return this.store;
    }

    public Category category() {
        return category;
    }

    public String name() {
        return this.name;
    }

    public String chnName() {
        return this.chnName;
    }

    public BigDecimal weight() {
        return this.weight;
    }

    public Product.Status status() {
        return status;
    }

    public int isDeposit() {
        return isDeposit;
    }

    public List<SImage> images() {
        return this.imagess;
    }


    private ShowOption showOption;

    public ShowOption showOption() {
        return this.showOption;
    }


    /**
     * 现阶段没有体现出 Menu的概念，所以附加到Product
     * 但以下功能却是不是领域模型中的概念，所以用 ShowOption ValueObject 去包以下
     */
    public static final class ShowOption {

        private int showIndex;
        private String desKor;//韩文说明
        private String desChn;//中文说明
        private int recommend;

        private String detailDescKor;
        private String detailDescChn;

        private int isStandard; //该产品是否设置规格，0-否 1-是
        private int isInventory; //该产品是否设置库存，0-否 1-是

        private ShowOption(int showIndex, String desChn, String desKor, int recommend) {
            this.showIndex = showIndex;
            this.desChn = desChn;
            this.desKor = desKor;
            this.recommend = recommend;
        }

        public ShowOption(int showIndex, String desChn, String desKor, int recommend
                , String detailDescKor2, String detailDescChn2,int isStandard2,int isInventory2) {
            this(showIndex, desChn, desKor, recommend);
            this.detailDescChn = detailDescChn2;
            this.detailDescKor = detailDescKor2;
            this.isStandard = isStandard2;
            this.isInventory = isInventory2;
        }


        public int getShowIndex() {
            return showIndex;
        }

        public String getDesKor() {
            return desKor;
        }

        public String getDesChn() {
            return desChn;
        }

        public int getRecommend() {
            return recommend;
        }


        public String getDetailDescKor() {
            return detailDescKor;
        }

        public String getDetailDescChn() {
            return detailDescChn;
        }

        public int getIsStandard() {
            return isStandard;
        }

        public int getIsInventory() {
            return isInventory;
        }
    }


    public static class SImage {

        private static final int MAIN_TYPE = 1;
        private static final int SUB_TYPE = 0;

        private String url;
        private int type; // 0 为 subimage 1 为 main image


        private SImage(String pImg, int iType) {
            this.url = pImg;
            this.type = iType;
        }

        public static SImage of(String imageUrl, int imageType) {

/*            if(MAIN_TYPE != imageType ||  SUB_TYPE != imageType){
                throw new IllegalArgumentException("not found image type:"+imageType);
            }*/

            return new SImage(imageUrl, imageType);
        }

        public String getUrl() {
            return url;
        }

        public int getType() {
            return type;
        }

        public static final SImage mainOf(String url) {
            return new SImage(url, MAIN_TYPE);
        }

        public static final SImage subOf(String url) {
            return new SImage(url, SUB_TYPE);
        }

        public boolean isMain() {
            return this.type == MAIN_TYPE;
        }

        public boolean isSub() {
            return this.type == SUB_TYPE;
        }
    }


    public static final class Builder {

        private Long id; //for db
        private String name;
        private String chnName;
        private BigDecimal weight;
        private Store store;
        private Category category;
        private Product.Status status = Status.OPEN;

        private int isDeposit;
        private int showIndex;
        private String desKor;//韩文说明
        private String desChn;//中文说明
        private int recommend;

        private String detailDescKor;
        private String detailDescChn;

        private int isStandard; //该产品是否设置规格，0-否 1-是
        private int isInventory; //该产品是否设置库存，0-否 1-是

        private List<SImage> images = new LinkedList<>();


        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder store(Store store) {
            this.store = store;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder img(SImage image) {
            this.images.add(image);
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder chnName(String chnName) {
            this.chnName = chnName;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder desKor(String desKor) {
            this.desKor = desKor;
            return this;
        }

        public Builder desChn(String chn) {
            this.desChn = chn;
            return this;
        }

        public Builder showIndex(int showIndex) {
            this.showIndex = showIndex;
            return this;
        }


        public Builder recommend(int state) {
            this.recommend = state;
            return this;
        }

        public Builder isDeposit(int isDeposit) {
            this.isDeposit = isDeposit;
            return this;
        }

        public Builder status(Status status1) {
            this.status = status1;
            return this;
        }


        public Builder detailDescKor(String ddk) {
            this.detailDescKor = ddk;
            return this;
        }


        public Builder detailDescChn(String chn) {
            this.detailDescChn = chn;
            return this;
        }

        public Builder clearImage() {
            this.images.clear();
            return this;

        }

        public Builder isStandard(int isStandard) {
            this.isStandard = isStandard;
            return this;
        }

        public Builder isInventory(int isInventory) {
            this.isInventory = isInventory;
            return this;
        }

        public Builder imageList(List<SImage> imagesList) {
/*
            if(subImageList != null && !subImageList.isEmpty()){
                final List<SImage> collect = subImageList.stream().map((inSImags) -> new SImage(inSImags, SImage.SUB_TYPE)).collect(Collectors.toList());
                this.subImages.addAll(collect);
            }
*/
            this.images.addAll(imagesList);
            return this;
        }


        public Product build() {


            if (this.images.isEmpty()) {
                throw new IllegalArgumentException("Images is null");
            }


/*            final long mainCount = this.images.stream().filter((p) -> p.isMain()).count();
            if(mainCount > 1){

            } */

            Product product = new Product();
            product.id = this.id;
            product.name = this.name;
            product.chnName = this.chnName;
            product.weight = this.weight;
            product.store = this.store;
            product.category = this.category;
            product.status = this.status == null ? Status.OPEN : this.status;
            product.isDeposit = this.isDeposit;
            product.showOption = new ShowOption(this.showIndex, this.desChn, this.desKor, this.recommend, this.detailDescKor, this.detailDescChn,this.isStandard,this.isInventory);

            product.imagess.addAll(this.images);


            return product;
        }


    }

    public static Builder update(Product product) {

        return new Builder()
                .id(product.id)
                .name(product.name)
                .imageList(product.imagess)
                .store(product.store)
                .category(product.category)
                .desChn(product.showOption.desChn)
                .desKor(product.showOption.desKor)
                .showIndex(product.showOption.showIndex)
                .status(product.status)
                .recommend(product.showOption.recommend);
    }
}

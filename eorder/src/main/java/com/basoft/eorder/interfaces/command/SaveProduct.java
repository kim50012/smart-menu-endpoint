package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.Standard;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.interfaces.query.StoreOptionDTO;
import com.basoft.eorder.util.UidGenerator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.basoft.eorder.domain.model.Product.*;


/**
 * @author woonill
 */

public class SaveProduct implements Command {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String NAME = "saveProduct";
    private Long id;
//    private Long storeId;
    private Long categoryId;
    private String name;
    private String chnName;
    private BigDecimal weight;
    private String imageUrl;
    private int showIndex;

    private String desKor;//韩文说明
    private String desChn;//中文说明

    private String recommend;
    private int isDeposit;


    private String detailDesc; //韩文详细产品描述
    private String detailChnDesc;//中文详细产品描述


    private List<String> subImageList = new LinkedList<>();
    private List<SaveProductSku> skuList = new LinkedList<>();

    private int isStandard; //该产品是否设置规格，0-否 1-是
    private int isInventory; //该产品是否设置库存，0-否 1-是

//    public void setId(Long id) {
//        this.id = id;
//    }
//    public void setStoreId(Long storeId) {
//        this.storeId = storeId;
//    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSkuList(List<SaveProductSku> skuList) {
        this.skuList = skuList;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    //   public Long getId() {
//        return id;
//    }

/*
    public Long getStoreId() {
        return storeId;
    }
*/

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<SaveProductSku> getSkuList() {
        return skuList;
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

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public int getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(int isDeposit) {
        this.isDeposit = isDeposit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SaveProduct addSku(SaveProductSku sku) {
        skuList.add(sku);
        return this;

    }

    public Product build(
            Long id,
            Store store,
            Category category){


        int recommend = StringUtils.isEmpty(this.recommend) ? Recommend.NORMAL.code() : Integer.parseInt(this.recommend);

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


        return new Builder()
                .id(id)
                .store(store)
                .category(category)
                .imageList(collect)
                .name(this.name)
                .chnName(this.chnName)
                .weight(this.weight)
                .showIndex(this.showIndex)
                .isDeposit(this.isDeposit)
                .desChn(this.desChn)
                .desKor(this.desKor)
                .detailDescChn(this.detailChnDesc)
                .detailDescKor(this.detailDesc)
                .recommend(recommend)
                .isStandard(isStandard)
                .isInventory(isInventory)
                .build();
    }




    @Data
    public static class SaveItemName {
        public SaveItemName(){

        }
        public int disOrder;
        public String itemNameChn;
        public String itemNameKor;
        public String itemNameEng;
        public ProductSku.ItemName build(){
            return new ProductSku.ItemName.Builder()
                    .disOrder(disOrder)
                    .itemNameChn(itemNameChn)
                    .itemNameKor(itemNameKor)
                    .itemNameEng(itemNameEng)
                    .build();
        }
    }

    public static  class SaveProductSku {

        private Long id;//
        private String standardNames;  //前台需要的字段，后端不处理此字段
        private List<Long> optionIds;
        private List<SaveSkuOptions> options;
        private BigDecimal weight;
        private BigDecimal salesPrice;//标准售价(原价)
        private BigDecimal priceWeekend;//酒店周末售价
        private BigDecimal priceSettle;
        private String name;
        private String chnName;
        private Boolean useDefault;

        private int isInventory;
        private Long invTotal;
        private int disOrder;
        private String mainImageUrl;
        private List<SaveItemName> standardItemList;//retail专属
        private ProductSku.Status status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public BigDecimal getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(BigDecimal salesPrice) {
            this.salesPrice = salesPrice;
        }

        public BigDecimal getPriceWeekend() {
            return priceWeekend;
        }

        public void setPriceWeekend(BigDecimal priceWeekend) {
            this.priceWeekend = priceWeekend;
        }

        public BigDecimal getPriceSettle() {
            return priceSettle;
        }

        public void setPriceSettle(BigDecimal priceSettle) {
            this.priceSettle = priceSettle;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getChnName() {
            return chnName;
        }
        public void setChnName(String chnName) {
            this.chnName = chnName;
        }

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public String getStandardNames() {
            return standardNames;
        }

        public void setStandardNames(String standardNames) {
            this.standardNames = standardNames;
        }

        public void setOptionIds(List<Long> optionIds) {
            this.optionIds = optionIds;
        }

        public Boolean getUseDefault() {
            return useDefault;
        }

        public void setUseDefault(Boolean useDefault) {
            this.useDefault = useDefault;
        }

        public List<SaveSkuOptions> getOptions() {
            return options;
        }

        public void setOptions(List<SaveSkuOptions> options) {
            this.options = options;
        }

        public int getIsInventory() {
            return isInventory;
        }

        public void setIsInventory(int isInventory) {
            this.isInventory = isInventory;
        }

        public Long getInvTotal() {
            return invTotal;
        }

        public void setInvTotal(Long invTotal) {
            this.invTotal = invTotal;
        }

        public int getDisOrder() {
            return disOrder;
        }

        public void setDisOrder(int disOrder) {
            this.disOrder = disOrder;
        }

        public String getMainImageUrl() {
            return mainImageUrl;
        }

        public void setMainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
        }

        public List<SaveItemName> getStandardItemList() {
            return standardItemList;
        }

        public void setStandardItemList(List<SaveItemName> standardItemList) {
            this.standardItemList = standardItemList;
        }

        public ProductSku.Status getStatus() {
            return status;
        }

        public void setStatus(ProductSku.Status status) {
            this.status = status;
        }

        /*        Standard.Standars toStandards(String pro){

            String[] codeNames = pro.split(",");

            final List<Standard> standards = Arrays.asList(codeNames)
                    .stream()
                    .map((codeName) -> {
                        String[] stdStr = standardVal.split("=");
                        return new Standard(new Long(stdStr[0]), stdStr[1]);
                    })
                    .collect(Collectors.toList());
            return Standard.Standars.geNew(standards);

        }*/

       public ProductSku build(Product product, StoreOptionDTO rootOption) {
           List<Standard> standardList = new LinkedList<>();
           if (this.optionIds != null && this.optionIds.size() > 0) {
               for (SaveSkuOptions ops : this.options) {
                   StoreOptionDTO o = rootOption.find(ops.getId());

                   if (o == null) {
//                    System.err.println("error:"+oid+" 找不到Option");
                       throw new BizException(ErrorCode.PARAM_INVALID);
                   }

                   if (!o.getChildren().isEmpty()) {
//                    System.err.println("有子选项的Option:"+oid+" :"+o.children().size());
                       throw new BizException(ErrorCode.PARAM_INVALID);
                   }
               }

            /*final List<Standard> standards = this.optionIds.stream().map((opId) -> {
                StoreOptionDTO option = rootOption.find(opId);
                return new Standard(option.getPcId(), option.getName(),option.getChnName(),1);
            }).collect(Collectors.toList());*/

                standardList = this.options.stream().map((op) -> {
                   StoreOptionDTO option = rootOption.find(op.id);
                   return new Standard(option.getPcId(), option.getName(), option.getChnName(), op.baseExtend);
               }).collect(Collectors.toList());
           }

           List<ProductSku.ItemName> itemNames = new LinkedList<>();
           if (this.standardItemList != null && this.standardItemList.size() > 0) {
               itemNames = this.standardItemList.stream().map((item) -> {
                   return  item.build();
               }).collect(Collectors.toList());
           }


           List<SaveSkuOptions> options;
           return new ProductSku.Builder()
                   .setId(this.id)
                   .setChnName(this.chnName)
                   .setName(this.name)
                   .setWeight(this.weight)
                   .setPrice(this.getSalesPrice())
                   .setHotelPriceWeekend(this.priceWeekend)
                   .setPriceSettle(this.priceSettle)
                   .setStandards(Standard.Standars.geNew(standardList))
                   .setUseDefault(this.useDefault)
                   .isInventory(this.isInventory)
                   .invTotal(this.invTotal)
                   .disOrder(this.disOrder)
                   .mainImageUrl(this.mainImageUrl)
                   .itemNames(itemNames)
                   .setStatus(this.status)
                   //.setStatus(this.getStatus().equals("0")?ProductSku.Status.CLOSE: ProductSku.Status.OPEN)
                   .build(product);
           }

        /**
         * 构建retail库存
         *
         * @param product
         * @param invSold
         * @param us
         * @param uidGenerator
         * @return
         */
        public InventoryRetail buildInventory(Product product, Long invSold, UserSession us, UidGenerator uidGenerator) {
            return InventoryRetail.builder()
                    .invId(uidGenerator.generate(BusinessTypeEnum.INVENTORY_RETAIL))
                    .storeId(us.getStoreId())
                    .prodId(product.id())
                    .prodSkuId(this.id)
                    .invTotal(invTotal)
                    .invSold(invSold)
                    .invBalance(this.invTotal)
                    .createUser(us.getAccount())
                    .updateUser(us.getAccount())
                    .build();
        }


    }



    /**
     * @author DongXifu
     * 新增规格
     */
    public static final class SaveSkuOptions {
        private Long id;//
        private int baseExtend;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getBaseExtend() {
            return baseExtend;
        }

        public void setBaseExtend(int baseExtend) {
            this.baseExtend = baseExtend;
        }
    }
}

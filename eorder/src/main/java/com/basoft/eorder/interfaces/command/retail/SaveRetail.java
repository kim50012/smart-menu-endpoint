package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductAloneStandardItem;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.interfaces.command.SaveProduct;
import com.basoft.eorder.util.UidGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class SaveRetail implements Command {

    public final static int IS_TEMPLATE_YES = 1;//生成模版
    public final static int IS_STANDARD_YES = 1;//设置规格
    public final static int IS_INVENTORY_YES = 1;//设置库存

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String NAME = "saveRetail";
    public static final String UPNAME = "updateRetail";

    private Long productId;
    private Long categoryId;
    private String name;
    private String chnName;
    private String imageUrl;
    private int showIndex;

    private String desKor;//韩文说明
    private String desChn;//中文说明
    private String detailDesc; //韩文详细产品描述
    private String detailChnDesc;//中文详细产品描述

    private String recommend;
    private int isDeposit;
    private int isStandard; //该产品是否设置规格，0-否 1-是
    private int isTemplate;//是否保存模版
    private BigDecimal weight;
    @JsonProperty("tId")
    private Long tId;//模版id
    @JsonProperty("tNameChn")
    private String tNameChn;//模版名称中文
    @JsonProperty("tNameKor")
    private String tNameKor;//模版名称韩文
    //private int isInventory; //该产品是否设置库存，0-否 1-是

    private List<SaveProduct.SaveProductSku> skuList = new LinkedList<>();

    private List<String> subImageList = new LinkedList<>();

    private List<SaveAloneStandard> aloneStandardList;

    private List<InventoryRetail> inventoryRetails;


    @Data
    public static class SaveAloneStandard {

        private Long tId;//模版id

        private Long stdId;//零售产品规格ID

        private Long storeId;//零售商户ID

        private Long prodId;//零售商户产品ID

        private String name;

        private String stdNameChn;//产品规格中文名称

        private String stdNameKor;//产品规格韩文名称

        private String stdNameEng;//产品规格英文名称

        private int disOrder;//显示顺序 显示顺序

        private String stdImage;//规格图片

        private String stdStatus;//使用状态 0-停用 1-在用

        private List<SaveAloneStandardItem> standardItemList;

        private List<InventoryRetail> inventoryRetails;

        public String getStdStatus() {
            return stdStatus;
        }

        public void setStdStatus(String stdStatus) {
            if (StringUtils.isBlank(stdStatus)) {
                this.stdStatus = "1";
            } else {
                this.stdStatus = stdStatus;
            }
        }

        public ProductAloneStandard build(Store store
                ,Long tId
                , Product product
                , UserSession user, UidGenerator uidGenerator) {

            List<ProductAloneStandardItem> standardItemListFinal = standardItemList.stream().map((SaveAloneStandardItem standardItem) -> {
                if (standardItem.getItemId() == null) {
                    standardItem.setItemId(uidGenerator.generate(BusinessTypeEnum.PRO_ALONE_STAND_ITEM));
                }
                return new SaveAloneStandardItem().build(standardItem,tId,stdId,product.id(),user);
            }).collect(Collectors.toList());

            return ProductAloneStandard.builder()
                    .standardItemList(standardItemListFinal)
                    .tId(tId)
                    .stdId(stdId)
                    .storeId(store.id())
                    .prodId(product.id())
                    .stdNameChn(stdNameChn)
                    .stdNameKor(stdNameKor)
                    .stdNameEng(stdNameEng)
                    .disOrder(disOrder)
                    .stdImage(stdImage)
                    .stdStatus(this.stdStatus==null?1:Integer.valueOf(this.stdStatus))
                    .createUser(user.getAccount())
                    .build();
        }

    }


    @Data
    public static  class SaveAloneStandardItem {

        private Long tId;//模版id;

        private Long itemId;//零售产品规格项目ID

        private Long stdId;//零售产品规格ID

        private String name;

        private String itemNameChn;//产品规格项目中文名称

        private String itemNameKor;//产品规格项目韩文名称

        private String itemNameEng;//产品规格项目英文名称

        private int disOrder;//显示顺序

        private String itemImage;//规格项目图片

        private BigDecimal price;

        private Long invTotal;

        private Boolean useDefaut;

        private int isInventory;

        private String itemStatus;//使用状态 0-停用 1-在用


        public ProductAloneStandardItem build(SaveAloneStandardItem item,Long tId, Long stdId,Long prodId, UserSession user) {
            return ProductAloneStandardItem.builder()
                    .tId(tId)
                    .itemId(item.itemId)
                    .prodId(prodId)
                    .stdId(stdId)
                    .itemNameChn(item.itemNameChn)
                    .itemNameKor(item.itemNameKor)
                    .itemNameEng(item.itemNameEng)
                    .disOrder(item.disOrder)
                    .itemImage(item.itemImage)
                    .itemStatus(item.getItemStatus()==null?1:Integer.valueOf(item.getItemStatus()))
                    .createUser(user.getAccount())
                    .build();
        }
    }


    /**
     * 构建产品
     *
     * @param productId
     * @param store
     * @param c
     * @param saveRetail
     * @return
     */
    public Product buildProduct(Long productId,
                                Store store,
                                Category c, SaveRetail saveRetail) {
        SaveProduct saveProduct = new SaveProduct();
        try {
            BeanUtils.copyProperties(saveProduct,saveRetail);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return  saveProduct.build(productId, store, c);
    }

    /**
     * 构建模版
     *
     * @param store
     * @return
     */
    public ProductAloneStandardTemplate buildTemplate(Store store,UserSession us) {
        return ProductAloneStandardTemplate.builder()
                .tId(this.tId)
                .storeId(store.id())
                .tNameChn(this.tNameChn)
                .tNameKor(this.tNameKor)
                .tStatus(1)
                .createUser(us.getAccount())
                .updateUser(us.getAccount())
                .build();
    }

}

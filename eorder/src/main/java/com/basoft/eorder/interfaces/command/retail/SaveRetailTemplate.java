package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductAloneStandardItem;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.util.UidGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author DongXifu
 * @since 2020-04-16 13:30:41
 */
@Data
public class SaveRetailTemplate implements Command {

    public static final String NAME = "saveRetailTemplate";
    public static final String UPDATE_STATUS = "updateRetailTemplateStatus";
    public static final String DEL_TEMPLATE = "deleteTemplate";

    @JsonProperty("tId")
    private Long tId;//模版id

    @JsonProperty("tNameChn")
    private String tNameChn;//模版名称中文

    @JsonProperty("tNameKor")
    private String tNameKor;//模版名称韩文

    @JsonProperty("tNameEng")
    private String tNameEng;//模版名称英文

    @JsonProperty("tStatus")
    private String tStatus; //0停用 1 正常 2删除

    private String desKor; //模版描述

    private String desChn;

    private List<ProductAloneStandardTemplate> retailTemplates;

    private List<SaveAloneStandardT> aloneStandardList;

    private List<SaveRetail.SaveAloneStandardItem> standardItemList;



    @Data
    public static class SaveAloneStandardT {

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

        private List<SaveRetail.SaveAloneStandardItem> standardItemList;

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
                , UserSession user
                , UidGenerator uidGenerator) {

            List<ProductAloneStandardItem> standardItemListFinal = standardItemList.stream().map((SaveRetail.SaveAloneStandardItem standardItem) -> {
                if (standardItem.getItemId() == null) {
                    standardItem.setItemId(uidGenerator.generate(BusinessTypeEnum.PRODUCT_ALONE_STANDARD__T_S_I));
                }
                return new SaveRetail.SaveAloneStandardItem().build(standardItem,tId,stdId,prodId,user);
            }).collect(Collectors.toList());


            return ProductAloneStandard.builder()
                    .standardItemList(standardItemListFinal)
                    .tId(tId)
                    .stdId(stdId)
                    .storeId(store.id())
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


    public ProductAloneStandardTemplate build(Long tId, UserSession us) {
        return ProductAloneStandardTemplate.builder()
                .tId(tId)
                .storeId(us.getStoreId())
                .tNameChn(tNameChn)
                .tNameKor(tNameKor)
                .tNameEng(tNameEng)
                .tStatus(this.tStatus==null?1:Integer.valueOf(this.tStatus))
                .desKor(this.desKor)
                .desChn(this.desChn)
                .createUser(us.getAccount())
                .updateUser(us.getAccount())
                .build();
    }


}
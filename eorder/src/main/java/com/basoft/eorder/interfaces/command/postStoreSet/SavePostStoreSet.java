package com.basoft.eorder.interfaces.command.postStoreSet;

import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.postStoreSet.PostStoreSet;
import com.basoft.eorder.domain.model.postStoreSet.PostStoreSetDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 商户配送设置表(PostStoreSet)入参
 *
 * @author DongXifu
 * @since 2020-04-29 10:55:58
 */
@Data
public class SavePostStoreSet implements Command {

    public static final String NAME = "savePostStoreSet";
    public static final String upPostStoreSetStatus = "upPostStoreSetStatus";

    private Long pssId; //配送设置编号
    
    private Long storeId; //商户ID
    
    private String setNameChn; //配送设置名称
    
    private String setNameKor; //配送设置名称
    
    private String setNameEng; //配送设置名称
    
    private String targetCountryName; //配送目标国家名称
    
    private String targetCountryCode; //配送目标国家编码
    
    private int isFree; //是否存在免费配送 1-是，0-否
    
    private BigDecimal freeAmount; //免费配送起始购物金额
    
    private String postComName; //对应的快递公司
    
    private Long postComCode; //对应的快递公司编码
    
    private String setRule; //用于H5端显示的该配送设置的规则说明
    
    private int status; //配送设置状态 0-禁用，1-在用，2-删除
    
    private Date statusTime; //状态时间

    List<SaveDetail> detailList;

    List<PostStoreSet> postStoreSetList;


    @Data
    public static class SaveDetail {

        private Long pssId; //配送设置ID

        private Integer detailNo; //收费明细编号

        private BigDecimal lowerLimit; //该配送明细设置重量下限

        private BigDecimal upperLimit; //该配送明细设置重量上限，包含

        private BigDecimal chargeFee; //该重量区间收费金额，韩币

        public static PostStoreSetDetail build(Long pssId, SaveDetail saveDetail, UserSession us) {
            return PostStoreSetDetail.builder()
                    .pssId(pssId)
                    .detailNo(saveDetail.getDetailNo())
                    .lowerLimit(saveDetail.getLowerLimit())
                    .upperLimit(saveDetail.getUpperLimit())
                    .chargeFee(saveDetail.getChargeFee())
                    .createUser(us.getAccount())
                    .updateUser(us.getAccount())
                    .build();
        }
    }


     public PostStoreSet build(UserSession us) {
         List<PostStoreSetDetail> details = new LinkedList<>();
         if (this.detailList != null && this.detailList.size() > 0) {
              details = this.detailList.stream().map(p -> {
                 return SaveDetail.build(this.pssId, p, us);
             }).collect(Collectors.toList());
         }

         return PostStoreSet.builder()
                 .pssId(pssId)
                 .storeId(storeId)
                 .setNameChn(setNameChn)
                 .setNameKor(setNameKor)
                 .setNameEng(setNameEng)
                 .targetCountryName(targetCountryName)
                 .targetCountryCode(targetCountryCode)
                 .isFree(isFree)
                 .freeAmount(freeAmount)
                 .postComName(postComName)
                 .postComCode(postComCode)
                 .setRule(setRule)
                 .status(status)
                 .statusTime(statusTime)
                 .postStoreSetDetailList(details)
                 .createUser(us.getAccount())
                 .updateUser(us.getAccount())
                 .build();
     }
        
}
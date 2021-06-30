package com.basoft.eorder.domain.model.postStoreSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 商户配送设置表(PostStoreSet)实体类
 *
 * @author DongXifu
 * @since 2020-04-29 10:49:03
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostStoreSet {

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
    
    private Date createTime; //创建时间생성시간
    
    private String createUser; //创建人
    
    private Date updateTime; //修改时间수정시간
    
    private String updateUser; //修改人

    List<PostStoreSetDetail> postStoreSetDetailList;
    


}
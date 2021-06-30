package com.basoft.eorder.interfaces.query.topic;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:18 PM 1/15/20
 **/
@Data
public class StoreTopicDTO {
    private Long storeId;  //商户ID

    private Long tpId;  //主题ID

    private String tpName;//主题名称

    private String tpNameForei;//主题名称韩语

    private Integer tpBizType;//主题业务类型 基于平台的业务类型定义

    private String tpFuncType;//主题功能类型 在业务类型基础上，进行功能分类，如餐厅类主题可以分为 1-餐厅类型 2-菜类
}

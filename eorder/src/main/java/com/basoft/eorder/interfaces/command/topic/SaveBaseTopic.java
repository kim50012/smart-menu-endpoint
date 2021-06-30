package com.basoft.eorder.interfaces.command.topic;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.topic.BaseTopic;
import lombok.Data;

import java.util.Date;

@Data
public class SaveBaseTopic implements Command {

    public static final String NAME = "saveBaseTopic";
    public static final String UPSTATUS = "upTopicStatus";

    //columns START
    private Long tpId;  //主题编号  db_column: TP_ID

    private Integer tpBizType;  //主题业务类型 基于平台的业务类型定义  db_column: TP_BIZ_TYPE

    private String tpFuncType;  //主题功能类型 在业务类型基础上，进行功能分类，如餐厅类主题可以分为 1-餐厅类型 2-菜类

    private String tpName;  //主题名称  db_column: TP_NAME

    private String tpNameForei;  //主题国外名称  db_column: TP_NAME_FOREI

    private String tpLogoSid;  //logo存储实例ID  db_column: TP_LOGO_SID

    private String tpLogoUrl;  //log URL  db_column: TP_LOGO_URL

    private Integer tpUrlType;  //主题链接类型 1-查询条件 2-功能或业务链接  db_column: TP_URL_TYPE

    private String tpUrl;  //主题链接  db_column: TP_URL

    private Integer tpDisType;  //显示类型 1-主显示 2-筛选显示

    private Integer tpOrder;  //排序

    private Integer tpStatus;  //状态 1-可用 0-不可用  db_column: TP_STATUS

    private Date createTime;  //创建时间  db_column: CREATE_TIME

    private String createUser;  //创建人  db_column: CREATE_USER

    private Date updateTime;  //修改时间  db_column: UPDATE_TIME

    private String updateUser;  //修改人  db_column: UPDATE_USER

    //columns END


    public BaseTopic build(Long tpId){
         BaseTopic topic = BaseTopic.builder()
                .tpId(tpId)
                .tpBizType(tpBizType)
                .tpFuncType(tpFuncType)
                .tpName(tpName)
                .tpNameForei(tpNameForei)
                .tpLogoSid(tpLogoSid)
                .tpLogoUrl(tpLogoUrl)
                .tpUrlType(tpUrlType)
                .tpUrl(tpUrl)
                .tpDisType(tpDisType)
                .tpOrder(tpOrder)
                .tpStatus(tpStatus)
                .createTime(createTime)
                .createUser(createUser)
                .updateTime(updateTime)
                .updateUser(updateUser)
                .build();
        return topic;
    }
}


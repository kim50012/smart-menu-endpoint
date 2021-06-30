package com.basoft.eorder.interfaces.query.topic;

import lombok.Data;

import java.util.Date;

@Data
public class BaseTopicDTO {
	//columns START
	private Long tpId;  //主题编号
	private Integer tpBizType;  //主题业务类型 基于平台的业务类型定义
	private String tpFuncType;  //主题功能类型 在业务类型基础上，进行功能分类，如餐厅类主题可以分为 1-餐厅类型 2-菜类
	private String tpName;  //主题名称
	private String tpNameForei;  //主题国外名称
	private String tpLogoSid;  //logo存储实例ID
	private String tpLogoUrl;  //log URL
	private Integer tpUrlType;  //主题链接类型 1-查询条件 2-功能或业务链接
	private String tpUrl;  //主题链接
	private int tpDisType;  //显示类型 1-主显示 2-筛选显示
	private int tpOrder;  //序号
	private Integer tpStatus;  //状态 1-可用 0-不可用 3-已删除
	private Date createTime;  //创建时间
	private String createUser;  //创建人
	private Date updateTime;  //修改时间
	private String updateUser;  //修改人
	//columns END

}


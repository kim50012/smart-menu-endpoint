package com.basoft.service.dao.wechat.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoExample;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;

@Repository
public interface AppInfoMapper {
	long countByExample(AppInfoExample example);

	int deleteByExample(AppInfoExample example);

	int deleteByPrimaryKey(String sysId);

	int insert(AppInfoWithBLOBs record);

	int insertSelective(AppInfoWithBLOBs record);

	List<AppInfoWithBLOBs> selectByExampleWithBLOBs(AppInfoExample example);

	List<AppInfo> selectByExample(AppInfoExample example);

	AppInfoWithBLOBs selectByPrimaryKey(String sysId);

	int updateByExampleSelective(@Param("record") AppInfoWithBLOBs record, @Param("example") AppInfoExample example);

	int updateByExampleWithBLOBs(@Param("record") AppInfoWithBLOBs record, @Param("example") AppInfoExample example);

	int updateByExample(@Param("record") AppInfo record, @Param("example") AppInfoExample example);

	int updateByPrimaryKeySelective(AppInfoWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(AppInfoWithBLOBs record);

	int updateByPrimaryKey(AppInfo record);

	/**
	 * 获取全部公众账号信息
	 * 
	 * @return List<AppInfo> 公众账号信息List
	 */
	List<AppInfoWithBLOBs> selectAllAppInfoList();
	
	/**
	 * 根据ID获取公众账号信息
	 * 
	 * @param key ID
	 * @return AppInfo 公众账号信息
	 */
	AppInfoWithBLOBs selectAppInfoByKey(String key);

	/**
	 * 根据shopId获取公众账号信息
	 * 
	 * @param shopId
	 * @return AppInfo 公众账号信息
	 */
	AppInfoWithBLOBs selectAppInfoByShopId(Long shopId);

	/**
	 * 根据公众账号原始ID获取公众账号信息
	 * 
	 * @param originalAppId
	 * @return AppInfo 公众账号信息
	 */
	AppInfoWithBLOBs selectAppInfoByOriginalAppId(String originalAppId);

	/**
	 * 根据公众账号appID获取公众账号信息
	 * 
	 * @param appId 公众账号ID
	 * @return AppInfo 公众账号信息
	 */
	AppInfoWithBLOBs selectAppInfoByAppId(String appId);
	
	/**
	 * 查询开启定时任务的AppInfo列表
	 * 
	 * @return
	 */
	public List<AppInfoWithBLOBs> selectBatchAppInfoList();
}
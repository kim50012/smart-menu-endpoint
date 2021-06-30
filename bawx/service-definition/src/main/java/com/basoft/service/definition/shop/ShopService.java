package com.basoft.service.definition.shop;

import java.util.List;

import com.basoft.core.ware.wechat.domain.dkf.CheckDkf;
import com.basoft.service.entity.shop.ShopWithBLOBs;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;

public interface ShopService {

	/**
	 * 根据公司ID和用户ID获取自己负责的公众号列表，并且状态正常的
	 * 
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<ShopWithBLOBs> getResponsibleShopList(String userId, Integer companyId);
	
	/**
	 * 根据公司ID获取全部的公众号列表，即使店铺状态不正常
	 * 
	 * @param companyId
	 * @return
	 */
	public List<ShopWithBLOBs> getShopList(Integer companyId);

	/**
	 * 根据id查询商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public ShopWithBLOBs getShopById(Long shopId);

	/**
	 * 新增商铺（即公众号） 同步新增对应的AppInfo
	 * 
	 * @param shop
	 * @return
	 */
	public int addShop(ShopWithBLOBs shop, AppInfoWithBLOBs appInfo);

	/**
	 * 修改商铺（即公众号） 同步修改对应的AppInfo
	 * 
	 * @param shop
	 * @return
	 */
	public int updateShopById(ShopWithBLOBs shop, AppInfoWithBLOBs appInfo);

	/**
	 * 删除商铺（即公众号） 同步删除对应的AppInfo
	 * 
	 * @param shopId
	 * @return
	 */
	public int deleteShopById(Long shopId, String sysId);
	
	/**
	 * 关闭（禁用）商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public int closeShopById(long shopId);
	
	/**
	 * 开启商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public int openShopById(long parseLong);

	/**
	 * 根据公司ID获取公众号列表，并且状态正常的
	 * 
	 * @param groupId
	 * @return
	 */
	public List<ShopWithBLOBs> findAllShopList(Integer groupId);

	/**
	 * 根据shopId获取对应的AppInfo
	 * 
	 * @param shopId
	 * @return
	 */
	public AppInfoWithBLOBs getAppInfoByShopId(Long shopId);
	
	/**
	 *
	 * 判断是否使用多客服系统
	 * 
	 * @param appInfo
	 * @param keywork 关键字
	 * @return
	 */
	public CheckDkf checkIsUseDkf(AppInfo appInfo, String keywork);

	/**
	 *
	 * 判断是否使用多客服系统
	 * 
	 * @param shopId
	 * @param keywork 关键字
	 * @return
	 */
	public CheckDkf checkIsUseDkf(Long shopId, String keywork);
}
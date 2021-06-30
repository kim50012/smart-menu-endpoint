package com.basoft.service.impl.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basoft.core.ware.wechat.domain.dkf.CheckDkf;
import com.basoft.service.dao.shop.ShopMapper;
import com.basoft.service.dao.wechat.appinfo.AppInfoMapper;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.entity.shop.ShopWithBLOBs;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopMapper shopMapper;
	
	@Autowired
	private AppInfoMapper appInfoMapper;

	/**
	 * 根据公司ID和用户ID获取自己负责的公众号列表，并且状态正常的
	 * 
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<ShopWithBLOBs> getResponsibleShopList(String userId, Integer companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USER_ID", userId);
		map.put("COMP_ID", companyId);
		return shopMapper.getResponsibleShopList(map);
	}
	
	/**
	 * 根据公司ID获取全部的公众号列表，即使店铺状态不正常
	 * 
	 * @param userId
	 * @return
	 */
	public List<ShopWithBLOBs> getShopList(Integer companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("COMP_ID", companyId);
		return shopMapper.getShopList(map);
	}

	/**
	 * 根据id查询商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	@Deprecated
	public ShopWithBLOBs getShopById1(Long shopId) {
		return shopMapper.selectByPrimaryKey(shopId);
	}
	
	/**
	 * 根据id查询商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public ShopWithBLOBs getShopById(Long shopId) {
		return shopMapper.selectByPrimaryKey1(shopId);
	}

	/**
	 * 新增商铺（即公众号）
	 * 同步新增对应的AppInfo
	 * 
	 * @param shop
	 * @return
	 */
	//@Transactional(value = "primaryTransactionManager")
	@Transactional
	public int addShop(ShopWithBLOBs shop, AppInfoWithBLOBs appInfo) {
		appInfoMapper.insertSelective(appInfo);
		return shopMapper.insert(shop);
	}

	/**
	 * 修改商铺（即公众号）
	 * 同步修改对应的AppInfo
	 * 
	 * @param shop
	 * @return
	 */
	//@Transactional(value = "primaryTransactionManager")
	@Transactional
	public int updateShopById(ShopWithBLOBs shop, AppInfoWithBLOBs appInfo) {
		appInfoMapper.updateByPrimaryKeySelective(appInfo);
		return shopMapper.updateByPrimaryKeySelective(shop);
	}

	/**
	 * 删除商铺（即公众号）
	 * 同步删除对应的AppInfo
	 * 
	 * @param shopId
	 * @return
	 */
	//@Transactional(value = "primaryTransactionManager")
	@Transactional
	public int deleteShopById(Long shopId, String sysId) {
		appInfoMapper.deleteByPrimaryKey(sysId);
		return shopMapper.deleteByPrimaryKey(shopId);
	}
	
	/**
	 * 关闭（禁用）商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public int closeShopById(long shopId) {
		return shopMapper.closeShopById(shopId);
	}
	
	/**
	 * 开启商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	public int openShopById(long shopId) {
		return shopMapper.openShopById(shopId);
	}
	
	/**
	 * 根据公司ID获取公众号列表，并且状态正常的
	 * 
	 * @param groupId
	 * @return
	 */
	public List<ShopWithBLOBs> findAllShopList(Integer groupId) {
		return shopMapper.findAllShopList(groupId);
	}
	
	/**
	 * 根据shopId获取对应的AppInfo
	 * 
	 * @param shopId
	 * @return
	 */
	public AppInfoWithBLOBs getAppInfoByShopId(Long shopId) {
		return appInfoMapper.selectAppInfoByShopId(shopId);
	}
	
	/**
	 * 判断是否使用多客服系统
	 * 
	 * @param appInfo
	 * @param keywork 关键字
	 * @return
	 */
	public CheckDkf checkIsUseDkf(AppInfo appInfo, String keywork) {
		return checkIsUseDkf(appInfo.getShopId(), keywork);
	}

	/**
	 *
	 * 判断是否使用多客服系统
	 * 
	 * @param appInfo
	 * @param keywork 关键字
	 * @return
	 */
	public CheckDkf checkIsUseDkf(Long shopId, String keywork) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SHOP_ID", shopId);
		map.put("KEYWORD", keywork);
		//Map<String, Object> returnMap = (Map<String, Object>) getSqlMapClientTemplate().queryForObject("WeixinCustom.selectDkfByKeywork", map);
		// WX_DKF_KEYWORD_SELECT
		Map<String, Object> returnMap = (Map<String, Object>) shopMapper.selectDkfByKeywork(map);
		
		CheckDkf dkf = new CheckDkf();
		dkf.setDkf((Integer)returnMap.get("IS_DKF"));
		dkf.setAuto((Integer)returnMap.get("IS_AUTO"));
		dkf.setKf_account((String)returnMap.get("KF_ACCOUNT"));
		return dkf;
	}
}
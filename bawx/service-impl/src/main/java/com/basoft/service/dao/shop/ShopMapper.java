package com.basoft.service.dao.shop;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.basoft.service.entity.shop.Shop;
import com.basoft.service.entity.shop.ShopExample;
import com.basoft.service.entity.shop.ShopWithBLOBs;

public interface ShopMapper {
    long countByExample(ShopExample example);

    int deleteByExample(ShopExample example);

    int deleteByPrimaryKey(Long shopId);

    int insert(ShopWithBLOBs record);

    int insertSelective(ShopWithBLOBs record);

    List<ShopWithBLOBs> selectByExampleWithBLOBs(ShopExample example);

    List<Shop> selectByExample(ShopExample example);

    ShopWithBLOBs selectByPrimaryKey(Long shopId);

    int updateByExampleSelective(@Param("record") ShopWithBLOBs record, @Param("example") ShopExample example);

    int updateByExampleWithBLOBs(@Param("record") ShopWithBLOBs record, @Param("example") ShopExample example);

    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    int updateByPrimaryKeySelective(ShopWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ShopWithBLOBs record);

    int updateByPrimaryKey(Shop record);
    
	/**
	 * 禁用公众号
	 * 
	 * @param shopId
	 * @return
	 */
	int closeShopById(@Param("shopId") long shopId);
	
	/**
	 * 开启商铺（即公众号）
	 * 
	 * @param shopId
	 * @return
	 */
	int openShopById(long shopId);
    
	/**
	 * 根据公司ID和用户ID获取自己负责的公众号列表，并且状态正常的
	 * 
	 * @param userId Map Item
	 * @param companyId Map Item
	 * @return
	 */
	List<ShopWithBLOBs> getResponsibleShopList(Map<String, Object> map);
    
	/**
	 * 根据公司ID获取全部的公众号列表，即使店铺状态不正常
	 * 
	 * @param userId Map Item
	 * @param companyId Map Item
	 * @return
	 */
	List<ShopWithBLOBs> getShopList(Map<String, Object> map);

	/**
	 * 根据公司ID获取公众号列表，并且状态正常的
	 * 
	 * @param groupId
	 * @return
	 */
	List<ShopWithBLOBs> findAllShopList(Integer groupId);

	/**
	 * 判断是否使用多客服系统
	 * 
	 * @param appInfo
	 * @param keywork 关键字
	 * @return
	 */
	Map<String, Object> selectDkfByKeywork(Map<String, Object> map);

	ShopWithBLOBs selectByPrimaryKey1(Long shopId);
}
package com.basoft.service.definition.shop;

import com.basoft.service.dto.shop.ShopFileDto;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.param.shopFile.ShopFileQueryParam;
import com.github.pagehelper.PageInfo;

public interface ShopFileService {
	/**
	 * 新增公众号文件
	 * 
	 * @param shopFile
	 * @return
	 */
	public int addShopFile(ShopFile shopFile);

	public PageInfo<ShopFileDto> findAllByParam(ShopFileQueryParam param);

	/**
	 * 添加文件
	 * 
	 * @param shopFile
	 * @return
	 */
	public int insertShopFile(ShopFile shopFile);

	/**
	 * 根据ShopId和fileId查询ShopFile
	 * 
	 * @param ShopId
	 * @param fileId
	 * @return
	 */
	public ShopFile getShopFileBykey(Long ShopId, Long fileId);

	/**
	 * 修改文件
	 * 
	 * @param shopFile
	 * @return
	 */
	public int updateShopFile(ShopFile shopFile);

	/**
	 * 刪除文件
	 * 
	 * @param shopId
	 * @param fileId
	 * @return
	 */
	public int deleteShopFile(Long shopId, Long fileId);
}
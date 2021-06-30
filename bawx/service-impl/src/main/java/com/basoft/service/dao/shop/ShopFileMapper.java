package com.basoft.service.dao.shop;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.basoft.service.dto.shop.ShopFileDto;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.shop.ShopFileExample;
import com.basoft.service.entity.shop.ShopFileKey;
import com.basoft.service.param.shopFile.ShopFileQueryParam;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopFileMapper {
    long countByExample(ShopFileExample example);

    int deleteByExample(ShopFileExample example);

    int deleteByPrimaryKey(ShopFileKey key);

    int insert(ShopFile record);

    int insertSelective(ShopFile record);

    List<ShopFile> selectByExample(ShopFileExample example);

    ShopFile selectByPrimaryKey(ShopFileKey key);

    int updateByExampleSelective(@Param("record") ShopFile record, @Param("example") ShopFileExample example);

    int updateByExample(@Param("record") ShopFile record, @Param("example") ShopFileExample example);

    int updateByPrimaryKeySelective(ShopFile record);

    int updateByPrimaryKey(ShopFile record);

	int addShopFile(ShopFile shopFile);
	
    List<ShopFileDto> findAllByParam(ShopFileQueryParam param);
}
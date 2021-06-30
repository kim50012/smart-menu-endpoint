package com.basoft.service.dao.customer.custShop;

import com.basoft.service.entity.customer.custShop.CustShop;
import com.basoft.service.entity.customer.custShop.CustShopExample;
import com.basoft.service.entity.customer.custShop.CustShopKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustShopMapper {
    int countByExample(CustShopExample example);

    int deleteByExample(CustShopExample example);

    int deleteByPrimaryKey(CustShopKey key);

    int insert(CustShop record);

    int insertSelective(CustShop record);

    List<CustShop> selectByExample(CustShopExample example);

    CustShop selectByPrimaryKey(CustShopKey key);

    int updateByExampleSelective(@Param("record") CustShop record, @Param("example") CustShopExample example);

    int updateByExample(@Param("record") CustShop record, @Param("example") CustShopExample example);

    int updateByPrimaryKeySelective(CustShop record);

    int updateByPrimaryKey(CustShop record);

}
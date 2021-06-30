package com.basoft.service.impl.customer.custShop;

import com.basoft.service.dao.customer.custShop.CustShopMapper;
import com.basoft.service.definition.customer.cust.custShop.CustShopService;
import com.basoft.service.entity.customer.custShop.CustShop;
import com.basoft.service.entity.customer.custShop.CustShopKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:47 2018/4/28
 **/
@Service
public class CustShopServiceImpl implements CustShopService {
    @Autowired
    private CustShopMapper custShopMapper;
    @Override
    public CustShop getCustShop(Long shopId, Long custSysId) {
        CustShopKey key = new CustShopKey();
        key.setShopId(shopId);
        key.setCustSysId(custSysId);
        return custShopMapper.selectByPrimaryKey(key);
    }
}

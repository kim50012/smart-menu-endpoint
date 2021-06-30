package com.basoft.service.definition.customer.cust.custShop;

import com.basoft.service.entity.customer.custShop.CustShop;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:43 2018/4/28
 **/

public interface CustShopService {

    public CustShop getCustShop(Long shopId,Long custSysId);
}

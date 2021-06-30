package com.basoft.eorder.interfaces.query.post;

import com.basoft.eorder.domain.model.post.CustPostAddress;

import java.util.List;

public interface CustPostAddressQuery {
    /**
     * 获取配送地址列表
     *
     * @param wxOpenId
     * @param queryType a-查询所有状态的配送地址 p-查询可用的配送地址 d-查询默认的配送地址
     * @return
     */
    List<CustPostAddress> getPostAddressList(String wxOpenId, String queryType);
}

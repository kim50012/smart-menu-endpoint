package com.basoft.eorder.domain.post;

import com.basoft.eorder.domain.model.post.CustPostAddress;

/**
 * Desc:CustPostAddressRepository
 *
 * @author Mentor
 * @version 1.0
 * @date 20200429
 */
public interface CustPostAddressRepository {
    /**
     * 平台客户新增配送地址
     *
     * @param custPostAddress
     * @return
     */
    int saveCustPostAddress(CustPostAddress custPostAddress);

    /**
     * 平台客户修改配送地址
     *
     * @param custPostAddress
     * @return
     */
    int updateCustPostAddress(CustPostAddress custPostAddress);

    /**
     * 删除配送地址
     *
     * @param custPostAddress
     * @return
     */
    int deleteCustPostAddress(CustPostAddress custPostAddress);
}

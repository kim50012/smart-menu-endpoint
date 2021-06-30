package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.WxAppInfoTable;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:24 2018/12/13
 **/
public interface WxAppInfoTableRepository {

    /**
     * @param  sysId
     * @return com.basoft.eorder.domain.model.StoreTable
     * @describe 一条餐桌信息
     * @author Dong Xifu
     * @date 2018/12/13 下午2:55
     */
	WxAppInfoTable getWxAppInfoTable(String sysId);
}

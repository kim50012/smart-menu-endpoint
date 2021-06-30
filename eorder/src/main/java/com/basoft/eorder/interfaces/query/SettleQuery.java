package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:51 2019/8/29
 **/
public interface SettleQuery {

    SettleDTO getAdminSettle(Map<String, Object> param);//总结算

    int getAdminSettleCount(Map<String, Object> param);

    List<SettleDTO> getAdminSettleList(Map<String, Object> param);//结算列表

    int getSettleDtlCount(Map<String, Object> param);

    List<SettleDTO> getSettleDtlListByMap(Map<String, Object> param);//详情列表*/

    SettleDTO getStoreSettleSum(Map<String, Object> param);//本月店铺总结算

    int getStoreSettleCount(Map<String, Object> param);

    List<SettleDTO> getStoreSettleList(Map<String, Object> param);//




}

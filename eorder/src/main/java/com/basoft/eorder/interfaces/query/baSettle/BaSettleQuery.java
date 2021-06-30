package com.basoft.eorder.interfaces.query.baSettle;

import java.util.List;
import java.util.Map;

public interface BaSettleQuery {

     StoreSettlementBaDTO getStoreSettlementBaDto(Map<String, Object> param);

     StoreSettlementBa getStoreSettlementBa(Map<String, Object> param);

     StoreSettlementBaDTO getAdminSettleSum(Map<String, Object> param);//本月店铺总结算

     int getAdminSettleBaCount(Map<String, Object> param);

     List<StoreSettlementBaDTO> getAdminSettleBaList(Map<String, Object> param);

     StoreSettlementDayBaDTO getSettleSumDay(Map<String, Object> param);

     int getSettlesDayCount(Map<String, Object> param);

     List<StoreSettlementDayBaDTO> getSettleDayListByMap(Map<String, Object> param);

     BaSettleDtlDTO getBaSettleDtl(Map<String, Object> param);

     int getBaSettleDtlCount(Map<String, Object> param);

     List<BaSettleDtlDTO> getBaSettleDtlListByMap(Map<String, Object> param);
}
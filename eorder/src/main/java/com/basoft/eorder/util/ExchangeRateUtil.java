package com.basoft.eorder.util;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExchangeRateUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductQuery productQuery;

    /**
     * 获取当前汇率（韩元兑换人民币汇率）
     *
     * @return
     */
    public BigDecimal getNowKrwCnyRate() {
        // 从缓存中获取，key值see AlliexTransExchangeThread
        BigDecimal krw_cny_rate = (BigDecimal) redisUtil.get("EXCHANGE_RATE_KRW_CNY_CACHE");
        if (krw_cny_rate != null) {
            return krw_cny_rate;
        }
        // 缓存中不存在，则从数据库中获取汇率
        else {
            ExchangeRateDTO erd = productQuery.getNowExchangeRate();
            if (erd != null) {
                return erd.getKrwcnyRate();
            } else {
                return null;
            }
        }
    }

    /**
     * 获取当前汇率（韩元兑换人民币汇率）
     *
     * @return
     */
    public BigDecimal getNowKrwUsdRate() {
        // 从缓存中获取，key值see AlliexTransExchangeThread
        BigDecimal krw_usd_rate = (BigDecimal) redisUtil.get("EXCHANGE_RATE_KRW_USD_CACHE");
        if (krw_usd_rate != null) {
            return krw_usd_rate;
        }
        // 缓存中不存在，则从数据库中获取汇率
        else {
            ExchangeRateDTO erd = productQuery.getNowExchangeRate();
            if (erd != null) {
                return erd.getKrwusdRate();
            } else {
                return null;
            }
        }
    }
}
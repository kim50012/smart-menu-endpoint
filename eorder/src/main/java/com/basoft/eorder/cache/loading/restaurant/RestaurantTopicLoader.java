package com.basoft.eorder.cache.loading.restaurant;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.topic.BaseTopicDTO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicQuery;
import com.basoft.eorder.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务启动执行：加载餐厅商户的主题数据
 *
 * @author Mentor
 * @version 1.0
 * @since 20200115
 */
@Slf4j
@Component
@Order(value = 1)
public class RestaurantTopicLoader implements CommandLineRunner {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BaseTopicQuery baseTopicQuery;

    @Autowired
    private ProductQuery productQuery;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>>服务启动，加载餐厅商户的主题数据 <<<<<<<<<<<<<");
        // 查询出餐厅主题列表-状态可用，业务类型为餐厅
        List<BaseTopicDTO> restaurantBaseTopicList = baseTopicQuery.getRestaurantBaseTopicList();
        redisUtil.lSet("restaurantBaseTopicList", restaurantBaseTopicList);
        log.info(">>>>>>>>>>>>>>>服务启动，加载餐厅商户的主题数据完毕 <<<<<<<<<<<<<");

        log.info("\n");
        log.info("\n");

        log.info(">>>>>>>>>>>>>>>服务启动，加载汇率数据 <<<<<<<<<<<<<");
        try {
            // 查询汇率：韩币和人民币之间汇率
            ExchangeRateDTO erd = productQuery.getNowExchangeRate();
            if (erd != null) {
                log.info(">>>>>>>>>>>>>>>汇率信息：{}<<<<<<<<<<<<<", erd.getKrwcnyRate());
                // 将汇率放入缓存：新增或者更新汇率（韩币和人民币之间）
                redisUtil.set("EXCHANGE_RATE_KRW_CNY_CACHE", erd.getKrwcnyRate());
                // 将汇率放入缓存：新增或者更新汇率（韩币和美元之间）
                redisUtil.set("EXCHANGE_RATE_KRW_USD_CACHE", erd.getKrwusdRate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>>>>>>>>>>>>服务启动，加载汇率数据完毕 <<<<<<<<<<<<<");
    }
}
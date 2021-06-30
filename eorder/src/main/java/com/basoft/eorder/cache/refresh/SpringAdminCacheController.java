package com.basoft.eorder.cache.refresh;

import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.topic.BaseTopicDTO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicQuery;
import com.basoft.eorder.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author BA
 */
@Slf4j
@Controller
@RequestMapping("/admin/api/v2/cache")
@ResponseBody
public class SpringAdminCacheController extends CQRSAbstractController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BaseTopicQuery baseTopicQuery;

    @GetMapping("/refreshRestaurantTopic")
    @ResponseBody
    public String refreshRestaurantTopic() {
        log.info(">>>>>>>>>>>>>>>【ADMIN】加载餐厅商户的主题数据 <<<<<<<<<<<<<");
        // 清理缓存
        redisUtil.del("restaurantBaseTopicList");

        // 查询出餐厅主题列表-状态可用，业务类型为餐厅
        List<BaseTopicDTO> restaurantBaseTopicList = baseTopicQuery.getRestaurantBaseTopicList();
        redisUtil.lSet("restaurantBaseTopicList", restaurantBaseTopicList);

        log.info(">>>>>>>>>>>>>>>【ADMIN】加载餐厅商户的主题数据完毕 <<<<<<<<<<<<<");
        return "success";
    }
}
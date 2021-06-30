package com.basoft.eorder.interfaces.query.topic;

import com.basoft.eorder.domain.model.topic.BaseTopic;

import java.util.List;
import java.util.Map;

public interface BaseTopicQuery {
    BaseTopicDTO getBaseTopicDto(Map<String, Object> param);

    BaseTopic getBaseTopic(Map<String, Object> param);

    int getBaseTopicCount(Map<String, Object> param);

    List<BaseTopicDTO> getBaseTopicListByMap(Map<String, Object> param);

    /**
     * 获取餐厅的主题列表
     *
     * @return
     */
    List<BaseTopicDTO> getRestaurantBaseTopicList();
}
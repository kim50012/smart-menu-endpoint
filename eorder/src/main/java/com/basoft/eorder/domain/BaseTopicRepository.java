package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.topic.BaseTopic;
import com.basoft.eorder.interfaces.command.topic.BatchUpTopic;

public interface  BaseTopicRepository{

	//新增
	Long insertBaseTopic(BaseTopic baseTopic);

	//修改
	Long updateBaseTopic(BaseTopic baseTopic);

	//修改状态
	int updateBaseTopicStatus(BatchUpTopic batchUpTopic);

}

package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.domain.BaseTopicRepository;
import com.basoft.eorder.domain.model.topic.BaseTopic;
import com.basoft.eorder.interfaces.command.topic.BatchUpTopic;
import com.basoft.eorder.interfaces.command.topic.SaveBaseTopic;
import com.basoft.eorder.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:DongXifu
 * @Description:
 * @Date
 **/
@CommandHandler.AutoCommandHandler("BaseTopicCommandHandler")
@Transactional
public class BaseTopicCommandHandler  {
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private UidGenerator uidGenerator;
	
	@Autowired
	private BaseTopicRepository baseTopicRepository;



	/**
	 * 新增或修改BaseTopic
	 *
	 * @Param saveBaseTopic
	 * @return int
	 * @author Dong Xifu
	 * @date
	 */
	@CommandHandler.AutoCommandHandler(SaveBaseTopic.NAME)
	public Long saveBaseTopic(SaveBaseTopic saveBaseTopic){
		logger.debug("Start saveBaseTopic -------");

		Long baseTopicId =  uidGenerator.generate(BusinessTypeEnum.BASE_TOPIC);

		if(saveBaseTopic.getTpId()!=null&&saveBaseTopic.getTpId()>0){
			BaseTopic baseTopic = saveBaseTopic.build(saveBaseTopic.getTpId());
			return  baseTopicRepository.updateBaseTopic(baseTopic);
		}else{
			BaseTopic baseTopic = saveBaseTopic.build(baseTopicId);
			baseTopicRepository.insertBaseTopic(baseTopic);
		}

		return baseTopicId;
	}

	/**
	 * 修改主题状态
	 *
	 * @Param
	 * @return java.lang.Long
	 * @author Dong Xifu
	 * @date 1/14/20 10:44 AM
	 */
	@CommandHandler.AutoCommandHandler(BatchUpTopic.NAME)
	public int batchUpTopicStatus(BatchUpTopic batchUpTopic){
	    return baseTopicRepository.updateBaseTopicStatus(batchUpTopic);
    }

}


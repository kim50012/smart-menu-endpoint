package com.basoft.eorder.interfaces.command.postStoreSet;

import com.basoft.eorder.application.*;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.model.postStoreSet.PostStoreSet;
import com.basoft.eorder.domain.postStoreSet.PostStoreSetRepository;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 *商户配送设置表控制层
 *
 * @author DongXifu
 * @since 2020-04-29 11:12:08
 */
@CommandHandler.AutoCommandHandler("PostStoreSet")
public class PostStoreSetCommandHandler  {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
      
    @Autowired
	private UidGenerator uidGenerator;

	@Autowired
	private PostStoreSetRepository postStoreSetRepository;


	/**
	 * 新增或修改PostStoreSet
	 *
	 * @Param postStoreSet
	 * @return Long
	 */
	@CommandHandler.AutoCommandHandler(SavePostStoreSet.NAME)
	@Transactional
	public Long savePostStoreSet(SavePostStoreSet savePostStoreSet, CommandHandlerContext context) {
		logger.debug("Start savePostStoreSet -------");
		UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
		savePostStoreSet.setStoreId(us.getStoreId());

		if(StringUtils.isBlank(savePostStoreSet.getSetNameKor())){
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		Long pssId = uidGenerator.generate(BusinessTypeEnum.POST_STORE_SET);
		if (savePostStoreSet.getPssId() != null && savePostStoreSet.getPssId() > 0) {
			PostStoreSet postStoreSet = savePostStoreSet.build(us);
			return postStoreSetRepository.updatePostStoreSet(postStoreSet);
		} else {
			savePostStoreSet.setPssId(pssId);
			PostStoreSet postStoreSet = savePostStoreSet.build(us);
			return postStoreSetRepository.insertPostStoreSet(postStoreSet);
		}
	}
		
	/**
	 * 批量修改PostStoreSet状态
	 
	 * @Param postStoreSet
	 * @return Long
	 */
	@CommandHandler.AutoCommandHandler(SavePostStoreSet.upPostStoreSetStatus)
	public int upPostStoreSetStatus(SavePostStoreSet savePostStoreSet) {
		return postStoreSetRepository.updatePostStoreSetStatus(savePostStoreSet.getPostStoreSetList(),savePostStoreSet.getStatus());
	}
    
}
package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.interfaces.command.BatchUpAdviceStatus;
import com.basoft.eorder.interfaces.command.advice.BatchUpAdvImgStatus;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:58 2019/5/20
 **/
public interface AdviceRepository {

    int saveAdvice(Advice advice);

    int updateAdvice(Advice advice);

    int bachUpAdviceStatus(BatchUpAdviceStatus batchUpAdviceStatus);

    int updateAdviceImages(BatchUpAdvImgStatus imgStatus);

}

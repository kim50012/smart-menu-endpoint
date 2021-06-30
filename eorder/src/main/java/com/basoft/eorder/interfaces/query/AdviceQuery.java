package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.Advice;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:24 2019/5/20
 **/
public interface AdviceQuery {
    Advice getAdvice(Map<String, Object> param);

    AdviceDTO getAdviceDto(Map<String, Object> param);

    int getAdviceCount(Map<String, Object> param);

    List<AdviceDTO> getAdviceList(Map<String, Object> param);


}

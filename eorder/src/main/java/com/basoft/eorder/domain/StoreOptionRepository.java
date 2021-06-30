package com.basoft.eorder.domain;

import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.domain.model.StoreOption;
import com.basoft.eorder.interfaces.query.StoreOptionDTO;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:04 2019/7/8
 **/
public interface StoreOptionRepository {


    StoreOption getRootOption();

    StoreOptionDTO getOptionDto(Long storeId,int type);

    StoreOption getOption(Long id,Long storeId);

    StoreOption saveOption(StoreOption option);

    StoreOption updateOption(StoreOption option);
}



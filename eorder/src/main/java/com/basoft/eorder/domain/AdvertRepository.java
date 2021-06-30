package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.Advertisement;
import com.basoft.eorder.interfaces.command.advert.BatchUpStatusAdvert;
/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:29 2019/6/1
 **/
public interface AdvertRepository {

    public int saveAdvert(Advertisement advert);

    public Long updateAdvert(Advertisement advert);

    public int upStatusAdvert(BatchUpStatusAdvert upStatusAdvert);
}

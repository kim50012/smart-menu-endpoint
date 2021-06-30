package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.Advertisement;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:29 2019/6/1
 **/
public interface AdvertQuery {

    Advertisement getAdvert(Long advertId);

    Advertisement getAdvert(Map<String,Object> map);

    AdvertDTO getAdvertDto(Map<String,Object> map);

    List<AdvertDTO> getAdvertList(Map<String, Object> map);

    int getAdvertCount(Map<String, Object> param);
}

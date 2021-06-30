package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

public interface BannerQuery {


    BannerDTO getBannerById(Long id);

    List<BannerDTO> getBannerListByMap(Map<String, Object> map);

    int getBannerCount(Map<String, Object> map);
}

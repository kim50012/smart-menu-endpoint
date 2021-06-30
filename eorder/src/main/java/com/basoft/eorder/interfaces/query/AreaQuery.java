package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

public interface AreaQuery {
    List<AreaDTO> getAreaListByMap(Map<String, Object> param);
}

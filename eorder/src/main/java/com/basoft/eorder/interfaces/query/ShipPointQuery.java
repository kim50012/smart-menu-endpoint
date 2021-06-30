package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

public interface ShipPointQuery {
    ShipPointDTO getShipPointById(long id);

    int getShipPointCount(Map<String, Object> param);

    List<ShipPointDTO> getShipPointListByMap(Map<String, Object> param);
}

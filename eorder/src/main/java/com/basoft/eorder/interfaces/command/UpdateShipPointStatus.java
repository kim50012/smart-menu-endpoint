package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.ShipPoint;
import com.basoft.eorder.domain.model.Store;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:24 2019/1/7
 **/
public class UpdateShipPointStatus implements Command {
    public static final String NAME = "updateShipPointStatus";
    private List<Long> shipPointIds;

    private int status;


    public List<Long> getShipPointIds() {
        return shipPointIds;
    }

    public void setShipPointIds(List<Long> shipPointIds) {
        this.shipPointIds = shipPointIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ShipPoint build(ShipPoint shipPoint) {
        return new ShipPoint.Builder()
            .shipPointid(shipPoint.shipPointid())
            .shipPointnm(shipPoint.shipPointnm())
            .areaId(shipPoint.areaId())
            .addr(shipPoint.addr())
            .addrCn(shipPoint.addrCn())
            .lat(shipPoint.lat())
            .lon(shipPoint.lon())
            .phoneNo(shipPoint.phoneNo())
            .cmt(shipPoint.cmt())
            .cmtCn(shipPoint.cmtCn())
            .status(this.status)
            .build();
    }


}

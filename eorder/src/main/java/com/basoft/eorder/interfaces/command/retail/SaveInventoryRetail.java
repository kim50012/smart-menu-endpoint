package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.framework.Command;
import lombok.Data;

@Data
public class SaveInventoryRetail implements Command {

    public static final String  NAME ="saveStockRetail";
    private Long invId;
    private Long storeId;
    private Long prodId;
    private Long prodSkuId;
    private Long invTotal;
    private Long invSold;
    private Long invBalance;
    private int isInventory;
    private int num;
    private String acoount;

}

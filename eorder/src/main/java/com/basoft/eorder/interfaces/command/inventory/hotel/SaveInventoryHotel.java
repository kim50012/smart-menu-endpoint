
package com.basoft.eorder.interfaces.command.inventory.hotel;


import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class SaveInventoryHotel implements Command {

    public static final String NAME = "SaveInventoryHotel";

    public static final String STATUS_NAME = "BatchUpInventoryHotelStatus";

    public static final String EXPORT_EXCEL_NAME = "ExportExcel";

    //columns START
    private int specDate[];

    private Long invId;  //invId  db_column: INV_ID

    private Long storeId;  //酒店ID  db_column: STORE_ID

    private Long prodId;  //酒店库存对应的产品  db_column: PROD_ID

    private Long prodSkuId;  //酒店库存对应的产品SKU  db_column: PROD_SKU_ID

    private BigDecimal price;//价格

    private BigDecimal disPrice;//折扣价格

    private BigDecimal priceSettle;//产品库存的到手价格

    private BigDecimal disPriceSettle;//产品库存的到手折扣价格

    private Integer invYear;  //库存年  db_column: INV_YEAR

    private Integer invMonth;  //库存月  db_column: INV_MONTH

    private Integer invDay;  //库存日  db_column: INV_DAY

    private Integer isOpening;  //当前日期是否开放该房间，默认值为1 1-开放 0-关闭  db_column: IS_OPENING

    private Integer invTotal;  //当前日期的库存总量，可修改，设定值要大于已订购数量。  db_column: INV_TOTAL

    private Integer invUsed;  //当前日期的被订购总量，小于等于库存总量。  db_column: INV_USED

    private Integer invBalance;  //当前日期的库存余量。由于库存总量可以随时被修改，所以此字段无参考意义  db_column: INV_BALANCE

    private Date createTime;  //创建时间생성시간  db_column: CREATE_TIME

    private String createUser;  //创建人  db_column: CREATE_USER

    private Date updateTime;  //修改时间수정시간  db_column: UPDATE_TIME

    private String updateUser;  //修改人  db_column: UPDATE_USER

    private String startTime;

    private String endTime;

    private List<ProductAndSku> productAndSkuList = new LinkedList<>();
    //columns END


    public static final class ProductAndSku {
        private Long prodId;  //酒店库存对应的产品  db_column: PROD_ID

        private Long prodSkuId;  //酒店库存对应的产品SKU  db_column: PROD_SKU_ID

        public Long getProdId() {
            return prodId;
        }

        public void setProdId(Long prodId) {
            this.prodId = prodId;
        }

        public Long getProdSkuId() {
            return prodSkuId;
        }

        public void setProdSkuId(Long prodSkuId) {
            this.prodSkuId = prodSkuId;
        }
    }


    public InventoryHotel build(Long prodId, Long prodSkuId, Long invId, String date, UserSession us) {
        return new InventoryHotel.Builder()
            .invId(invId)
            .storeId(us.getStoreId())
            .prodId(prodId)
            .prodSkuId(prodSkuId)
            .price(price)
            .disPrice(disPrice)
            .priceSettle(priceSettle)
            .disPriceSettle(disPriceSettle)
            .invYear(Integer.valueOf(date.substring(0, 4)))
            .invMonth(Integer.valueOf(date.substring(5, 7)))
            .invDay(Integer.valueOf(date.substring(8, 10)))
            .invDate(date)
            .isOpening(isOpening)
            .invTotal(invTotal)
            .invUsed(invUsed)
            .invBalance(invBalance)
            .createTime(createTime)
            .createUser(us.getAccount())
            .updateTime(updateTime)
            .updateUser(updateUser)
            .build();
    }

}


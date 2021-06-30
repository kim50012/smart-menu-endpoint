package com.basoft.eorder.foundation.jdbc.query.inventory.hotel;

import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import com.basoft.eorder.domain.model.inventory.hotel.StoreDayPrice;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.hotel.HotelProductSkuDatePriceDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("InventoryHotelQuery")
public class JdbcInventoryHotelQueryImpl extends BaseRepository implements InventoryHotelQuery {
    /*private final static String SQL_GROUP_CONCAT_SELECT = "select " +
        " PROD_ID,p.name_kor as productNmKor," +
        " p.name_chn as productNmChn,"+
        " ps.id as prodSkuId,"+
        " ps.name_chn as skuNmChn,"+
        " ps.name_chn as skuNmChn,"+
        " ps.name_kor as skuNmKor,"+
        " group_concat(INV_DATE) as invDate," +
        " group_concat(INV_TOTAL) as invTotal ";*/
    private final static String SQL_GROUP_COUNT = " select count(1)";
    private final static String SQL_GROUP_CONCAT_COLUMN = "select p.id as prodId,p.name_kor as productNmKor\n" +
            ",p.name_chn as productNmChn, ps.id as prodSkuId\n" +
            " ,ps.name_chn as skuNmChn\n" +
            " ,ps.name_kor as skuNmKor\n" +
            " ,t1.price,t1.disPrice,t1.priceSettle,t1.disPriceSettle,ps.price as priceDefault,ps.PRICE_SETTLE as priceSettleDefault\n"+
            " ,invDate,invTotal,invUsed,invBalance,isOpening\n";

    private final static String SQL_GROUP_CONCAT_FROM = "" +
            " from (\n" +
            " SELECT  PROD_ID as productId,p.name_kor as productNmKor,\n" +
            " p.name_chn as productNmChn,\n" +
            " ps.id as prodSkuId,\n" +
            " ps.name_chn as skuNmChn,\n" +
            " ps.name_kor as skuNmKor,\n" +
            " group_concat(IFNULL(ih.price,0)) as price,\n" +
            " group_concat(IFNULL(ih.dis_price,0)) as disPrice,\n" +
            " group_concat(IFNULL(ih.PRICE_SETTLE,0)) as priceSettle,\n" +
            " group_concat(IFNULL(ih.DIS_PRICE_SETTLE,0)) as disPriceSettle,\n" +
            " group_concat(IFNULL(INV_DATE,0)) as invDate,\n" +
            " group_concat(IFNULL(INV_TOTAL,0)) as invTotal,\n" +
            " group_concat(IFNULL(INV_USED,0)) as invUsed,\n" +
            " group_concat(IFNULL(INV_BALANCE,0)) as invBalance,\n" +
            " group_concat(IFNULL(IS_OPENING,0)) as isOpening\n" +
            " from inventory_hotel ih left JOIN product p on p.id=ih.PROD_ID\n" +
            " left JOIN product_sku ps on ps.product_id=p.id\n" +
            " where 1=1\n";

    private final static String GROUP_CONCAT_FROM = " GROUP BY PROD_ID,ih.PROD_SKU_ID\n" +
            " )t1\n" +
            " right JOIN product p on t1.productId=p.id\n" +
            " right JOIN product_sku ps on p.id=ps.product_id\n" +
            " inner  JOIN store s on p.store_id=s.id and s.store_type=4 and  p.`status` not in(2,3) where 1=1\n";


    private final static String SQL_SELECT = "select\n" +
            " ih.INV_ID as invId,\n" +
            " ih.STORE_ID as storeId,\n" +
            " ih.PROD_ID as prodId,\n" +
            " ih.PROD_SKU_ID as prodSkuId,\n" +
            " ifnull(ih.price,0)price ,ifnull(ih.dis_price,0) as disPrice,\n"+
            " ifnull(ih.PRICE_SETTLE,0)priceSettle ,ifnull(ih.DIS_PRICE_SETTLE,0) as disDriceSettle,\n"+
            " ih.INV_YEAR as invYear,\n" +
            " ih.INV_MONTH as invMonth,\n" +
            " ih.INV_DAY as invDay,\n" +
            " ih.INV_DATE as invDate,\n" +
            " ih.IS_OPENING as isOpening,\n" +
            " ih.INV_TOTAL as invTotal,\n" +
            " ih.INV_USED as invUsed,\n" +
            " ih.INV_BALANCE as invBalance,\n" +
            " ih.CREATE_TIME as createTime,\n" +
            " ih.CREATE_USER as createUser,\n" +
            " ih.UPDATE_TIME as updateTime,\n" +
            " ih.UPDATE_USER as updateUser,\n" +
            " p.name_kor  as productNmKor, \n" +
            " p.name_chn  as productNmChn \n";

    private final static String INVENTORY_FROM = " from inventory_hotel ih left join product p on ih.PROD_ID=p.id\n" +
            " where 1=1 \n";

    @Override
    public InventoryHotelDTO getInventoryHotelDto(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(SQL_SELECT + GROUP_CONCAT_FROM);
        productCondition(sql, param, false);
        InventoryHotelDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotelDTO.class));
        return dto;
    }

    @Override
    public InventoryHotel getInventoryHotel(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(SQL_SELECT + GROUP_CONCAT_FROM);

        productCondition(sql, param, false);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotel.class));
    }

    @Override
    public int getInventoryHotelCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();

        sql.append(SQL_GROUP_COUNT + SQL_GROUP_CONCAT_FROM);
        conditionInventHotel(sql, param);
        sql.append(GROUP_CONCAT_FROM);
        productCondition(sql, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<InventoryHotelDTO> getIventHotelGroupList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(SQL_GROUP_CONCAT_COLUMN + SQL_GROUP_CONCAT_FROM);
        conditionInventHotel(sql, param);
        sql.append(GROUP_CONCAT_FROM);
        productCondition(sql, param, false);

        List<InventoryHotelDTO> inventoryHotelDTOList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotelDTO.class));
        for (InventoryHotelDTO dto : inventoryHotelDTOList) {
            if (dto.getInvDate() != null && dto.getInvTotal() != null && dto.getInvBalance() != null
                    && dto.getInvUsed() != null && dto.getIsOpening() != null) {
                String invPrice[] = dto.getPrice().split(",");
                String invdisPrice[] = dto.getDisPrice().split(",");
                String priceSettle[] = dto.getPriceSettle().split(",");
                String disPriceSettle[] = dto.getDisPriceSettle().split(",");
                String invDate[] = dto.getInvDate().split(",");
                String invTotal[] = dto.getInvTotal().split(",");
                String invUsed[] = dto.getInvUsed().split(",");
                String invBalance[] = dto.getInvBalance().split(",");
                String isOpening[] = dto.getIsOpening().split(",");
                for (int i = 0; i < invDate.length; i++) {
                    InventoryHotelDTO.InvDateAndInv dateAndInvTotal = new InventoryHotelDTO.InvDateAndInv();
                    dateAndInvTotal.setProdId(dto.getProdId());
                    dateAndInvTotal.setPrice(new BigDecimal(invPrice[i]));
                    dateAndInvTotal.setDisPrice(new BigDecimal(invdisPrice[i]));
                    dateAndInvTotal.setPriceSettle(new BigDecimal(priceSettle[i]));
                    dateAndInvTotal.setDisPriceSettle(new BigDecimal(disPriceSettle[i]));
                    dateAndInvTotal.setInvDate(invDate[i]);
                    dateAndInvTotal.setInvTotal(invTotal[i]);
                    dateAndInvTotal.setInvUsed(invUsed[i]);
                    dateAndInvTotal.setInvBalance(invBalance[i]);
                    dateAndInvTotal.setIsOpening(isOpening[i]);
                    dto.getInvDateAndTotalList().add(dateAndInvTotal);
                }
            }
        }
        return inventoryHotelDTOList;
    }

    @Override
    public List<InventoryHotelDTO> getInventoryHotelList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(SQL_SELECT + INVENTORY_FROM);
        conditionInventHotel(sql, param);
        //productCondition(sql, param, false);
        List<InventoryHotelDTO> resultList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotelDTO.class));
        return resultList;
    }

    @Override
    public List<StoreDayPrice> getfuturePriceMinList(Map<String, Object> param) {
        String sql = "select :storeId as storeId, c.dt as invDate\n" +
            ",(select max(price)\n" +
            "              from product p\n" +
            "                     inner join product_sku ps on p.id = ps.product_id\n" +
            "                and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "              where store_id = :storeId)skuMaxPrice"+
            ",(select min(price)\n" +
            "              from product p\n" +
            "                     inner join product_sku ps on p.id = ps.product_id\n" +
            "                and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "              where store_id = :storeId)skuMinPrice"+
            "       ,case\n" +
            "         when t.minPrice is not null then t.minPrice\n" +
            "         else (select min(price)\n" +
            "               from product p\n" +
            "                      inner join product_sku ps on p.id = ps.product_id\n" +
            "                 and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "               where store_id = :storeId) end minPrice\n" +
            "       ,case\n" +
            "        when t.maxPrice is not null then t.maxPrice\n" +
            "        else (select max(price)\n" +
            "              from product p\n" +
            "                     inner join product_sku ps on p.id = ps.product_id\n" +
            "                and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "              where store_id = :storeId) end maxPrice"+
            "       ,case\n" +
            "         when t.PROD_ID is not null then t.PROD_ID\n" +
            "         else (select min(p.id)\n" +
            "               from product p\n" +
            "                      inner join product_sku ps on p.id = ps.product_id\n" +
            "                 and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "               where store_id = :storeId\n" +
            "               having min(price) = minPrice) end prodId\n" +
            "       ,case\n" +
            "         when t.PROD_ID is not null then t.PROD_ID\n" +
            "         else (select min(ps.id)\n" +
            "               from product p\n" +
            "                      inner join product_sku ps on p.id = ps.product_id\n" +
            "                 and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "               where store_id = :storeId\n" +
            "               having min(price) = minPrice) end prodSkuId\n" +
            "from calendar c\n" +
            "       left join\n" +
            "     (\n" +
            "       select PROD_ID\n" +
            "            ,p.name_chn\n" +
            "            ,PROD_SKU_ID\n" +
            "            ,ih.INV_DATE\n" +
            "            ,min(case\n" +
            "                    when DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                      and ih.PRICE is not null and ih.PRICE != -1 and ih.DIS_PRICE < ih.PRICE\n" +
            "                      then ih.DIS_PRICE\n" +
            "                    when DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                      and ih.PRICE is not null and ih.PRICE != -1 and ih.DIS_PRICE > ih.PRICE\n" +
            "                      then ih.PRICE\n" +
            "                    when DIS_PRICE is null\n" +
            "                      and ih.price is not null and ih.price != -1\n" +
            "                      then ih.PRICE\n" +
            "                    when ih.price is null\n" +
            "                      and DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                      then ih.DIS_PRICE\n" +
            "                    when ih.PRICE is null and DIS_PRICE is null\n" +
            "                      then ps.price\n" +
            "                    when DIS_PRICE = -1 and ih.PRICE != -1\n" +
            "                      then ih.PRICE\n" +
            "                    when ih.PRICE = -1 and DIS_PRICE != -1 and DIS_PRICE is not null\n" +
            "                      then DIS_PRICE\n" +
            "                    else ps.price\n" +
            "         end) minPrice\n" +
            "              ,max(case\n" +
            "                   when DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                     and ih.PRICE is not null and ih.PRICE != -1 and ih.DIS_PRICE < ih.PRICE\n" +
            "                     then ih.PRICE\n" +
            "                   when DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                     and ih.PRICE is not null and ih.PRICE != -1 and ih.DIS_PRICE > ih.PRICE\n" +
            "                     then ih.DIS_PRICE\n" +
            "                   when DIS_PRICE is null\n" +
            "                     and ih.price is not null and ih.price != -1\n" +
            "                     then ih.PRICE\n" +
            "                   when ih.price is null\n" +
            "                     and DIS_PRICE is not null and DIS_PRICE != -1\n" +
            "                     then ih.DIS_PRICE\n" +
            "                   when ih.PRICE is null and DIS_PRICE is null\n" +
            "                     then ps.price\n" +
            "                   when DIS_PRICE = -1 and ih.PRICE != -1\n" +
            "                     then ih.PRICE\n" +
            "                   when ih.PRICE = -1 and DIS_PRICE != -1 and DIS_PRICE is not null\n" +
            "                     then DIS_PRICE\n" +
            "                   else ps.price\n" +
            "               end) maxPrice"+
            "            ,ps.price\n" +
            "       from inventory_hotel ih\n" +
            "              inner join product_sku ps on ih.PROD_ID = ps.product_id and ih.PROD_SKU_ID = ps.id\n" +
            "              inner join product p on p.id = ps.product_id and p.is_deposit = 0 and p.status not in (2, 3)\n" +
            "       where ih.STORE_ID = :storeId \n"+
            "       group by ih.INV_DATE\n" +
            "     ) t on c.dt = t.INV_DATE\n" +
            "where c.dt >= date_format(now(), '%Y-%m-%d')\n" +
            "  and c.dt <= date_sub(now(),INTERVAL  - "+ StoreDayPrice.fatureNum +" day)\n" +
            "order by c.dt asc ";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreDayPrice.class));
    }

    @Override
    public InventoryHotelDTO.InvDateAndInv getUnSetHotelProds(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id as prodId,t1.name_chn\n" +
            " from (\n" +
            " SELECT  PROD_ID as productId,p.name_chn\n" +
            " from inventory_hotel ih left JOIN product p on p.id=ih.PROD_ID\n" +
            " left JOIN product_sku ps on ps.product_id=p.id\n" +
            " where 1=1\n" +
            " and ih.inv_date = :dateTime and ih.STORE_ID = :storeId  GROUP BY PROD_ID,ih.PROD_SKU_ID\n" +
            " )t1\n" +
            " right JOIN product p on t1.productId=p.id\n" +
            " right JOIN product_sku ps on p.id=ps.product_id\n" +
            " inner  JOIN store s on p.store_id=s.id and s.store_type=4 and  p.`status` not in(2,3) where 1=1\n" +
            " and s.id = :storeId \n" +
            " and p.is_deposit=0 and productId not in ( select ih.PROD_ID\n" +
            "  from inventory_hotel ih where STORE_ID=:storeId and ih.DIS_PRICE !=-1 and ih.INV_DATE in (:dateTime))");

        return  this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotelDTO.InvDateAndInv.class));

    }

    @Override
    public List<InventoryHotel> getfutureProdAll(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.dt as invDate, p.id as prodId,ps.id as prodSkuId,p.name_chn,ps.price_weekend,ps.price,ps.price as disPrice\n" +
            "from product p\n" +
            "       inner join product_sku ps on ps.product_id = p.id\n" +
            "  and p.is_deposit = 0 and p.store_id = :storeId and p.status not in (2, 3)\n" +
            "left join calendar c on 1=1\n" +
            "where c.dt >= date_format(now(), '%Y-%m-%d')\n" +
            "  and c.dt <= date_sub(now(), INTERVAL - "+StoreDayPrice.fatureNum+" day)");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotel.class));
    }

    @Override
    public List<InventoryHotel> getfutureSetProDays(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ih.INV_DATE,ih.PRICE,ih.DIS_PRICE,ih.PROD_ID,ih.PROD_SKU_ID\n" +
            "from inventory_hotel ih\n" +
            "where STORE_ID = :storeId\n" +
            "  and (ih.price is not null and ih.PRICE != '-1'\n" +
            "  or ih.DIS_PRICE is not null and ih.DIS_PRICE != '-1')\n" +
            "  and ih.INV_DATE >= date_format(now(), '%Y-%m-%d')\n" +
            "  and ih.INV_DATE <= date_sub(now(), INTERVAL - "+StoreDayPrice.fatureNum+" day)\n" +
            "order by ih.INV_DATE asc");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryHotel.class));
    }

    private StringBuilder conditionInventHotel(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String prodId = Objects.toString(param.get("prodId"), null);
        String prodSkuId = Objects.toString(param.get("prodSkuId"), null);
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and ih.inv_date >= :startTime and ih.inv_date<=:endTime ");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and ih.STORE_ID=:storeId ");
        }
        if (StringUtils.isNotBlank(prodId)) {
            sql.append(" and ih.PROD_ID=:prodId ");
        }
        if (StringUtils.isNotBlank(prodSkuId)) {
            sql.append("and ih.PROD_SKU_ID=:prodSkuId ");
        }
        return sql;
    }

    private StringBuilder productCondition(StringBuilder sql, Map<String, Object> param, boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        if (storeId > 0) {
            sql.append(" and s.id = :storeId \n");
        }
        sql.append(" and p.is_deposit=0");
        appendPage(page, size, sql, param, isLimit);
        return sql;
    }

    /**
     * 查询指定酒店商户和查询日期期间的库存信息
     *
     * @param param
     *              storeId 商户id 必选
     *              skuId skuId 可选
     *              startDate 必选
     *              endDate 必选
     * @return
     */
    public List<InventoryHotelDTO> getHotelInventoryListByConditions(Map<String, Object> param) {
        String skuId = (String) param.get("skuId");
        StringBuilder qrySQL = new StringBuilder("SELECT ");
        qrySQL.append("ih.INV_ID AS invId, ih.STORE_ID AS storeId, ")
                .append("ih.PROD_ID AS prodId, ih.PROD_SKU_ID AS prodSkuId, ")
                .append("ih.PRICE AS price, ih.DIS_PRICE AS disPrice, ")
                .append("ih.INV_YEAR AS invYear, ih.INV_MONTH AS invMonth,ih.INV_DAY AS invDay, ")
                .append("ih.INV_DATE AS invDate, ih.IS_OPENING AS isOpening, ")
                .append("ih.INV_TOTAL AS invTotal, ih.INV_USED AS invUsed, ")
                .append("ih.INV_BALANCE AS invBalance, ih.CREATE_TIME AS createTime, ")
                .append("ih.CREATE_USER AS createUser, ih.UPDATE_TIME AS updateTime, ih.UPDATE_USER AS updateUser ")
                .append("FROM INVENTORY_HOTEL ih ")
                .append("WHERE ih.STORE_ID = :storeId ");
        if (skuId != null && !"".equals(skuId.trim())) {
            qrySQL.append("AND ih.PROD_SKU_ID = :skuId ");
        }
        qrySQL.append("AND str_to_date(INV_DATE,'%Y-%m-%d') >= STR_TO_DATE(:startDate,'%Y-%m-%d') ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') < STR_TO_DATE(:endDate,'%Y-%m-%d')")
                .append("AND ih.INV_TOTAL != -1");
        logger.info("查询指定酒店商户和查询日期期间的库存信息SQL:::" + qrySQL.toString());
        List<InventoryHotelDTO> resultList = this.getNamedParameterJdbcTemplate().query(qrySQL.toString(), param, new BeanPropertyRowMapper<>(InventoryHotelDTO.class));
        return resultList;
    }


    /**
     * 查询指定酒店的所有产品的所有sku的日期期间内的价格
     *
     * 产品在线的所有sku，即产品状态不为2和3。
     * 库存日期值无论0和1都查询
     *
     * @param storeId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<HotelProductSkuDatePriceDTO> getProductSkuDatePriceList(long storeId, String startDate, String endDate) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        StringBuilder qrySQL = new StringBuilder("SELECT ");
        /**qrySQL.append("t.STORE_ID AS storeId,t.PROD_ID AS prodId,t.PROD_SKU_ID AS prodSkuId,t.INV_DATE AS priceDate, t.IS_OPENING AS isOpening,")
                .append("ps.price AS prodSkuPriceKor,t.PRICE as inventPriceKor,t.DIS_PRICE AS inventDisPriceKor, ")
                .append("case ")
                .append("when t.PRICE IS NULL AND t.DIS_PRICE IS null then ps.price ")
                .append("when t.PRICE IS NULL AND t.DIS_PRICE IS not null then ps.price ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS null then t.PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS not NULL AND t.PRICE >= t.DIS_PRICE then t.DIS_PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS not NULL AND t.PRICE < t.DIS_PRICE then t.PRICE ")
                .append("ELSE ps.price ")
                .append("END AS effectivePriceKor, ")
                .append("case ")
                .append("when t.PRICE IS NULL AND t.DIS_PRICE IS null then 1 ")
                .append("when t.PRICE IS NULL AND t.DIS_PRICE IS not null then 1 ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS null then 2 ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS not NULL AND t.PRICE >= t.DIS_PRICE then 3 ")
                .append("when t.PRICE IS NOT NULL AND t.DIS_PRICE IS not NULL AND t.PRICE < t.DIS_PRICE then 2 ")
                .append("ELSE 1 ")
                .append("END AS priceType ")
                .append("FROM inventory_hotel t INNER JOIN product_sku ps ON t.PROD_SKU_ID = ps.id ")
                .append("INNER JOIN product p ON ps.product_id = p.id ")
                .append("WHERE t.STORE_ID = :storeId ")
                //.append("AND T.IS_OPENING = 1 ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') >= STR_TO_DATE(:startDate,'%Y-%m-%d') ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') < STR_TO_DATE(:endDate,'%Y-%m-%d') ")
                .append("AND p.`status` !=2  ")
                .append("AND p.`status` !=3  ");*/

        /**
         * 1-产品SKU价格为有效价格。此时库存价格为空，无论库存折扣价是否为空
         * 2-库存原价为有效价格。库存折扣价为空或者比库存价还高，即库存折扣价无效
         * 3-库存折扣价为有效价格。
         */
        qrySQL.append("t.STORE_ID AS storeId,t.PROD_ID AS prodId,t.PROD_SKU_ID AS prodSkuId,t.INV_DATE AS priceDate, t.IS_OPENING AS isOpening,")
                .append("ps.price AS prodSkuPriceKor,t.PRICE as inventPriceKor,t.DIS_PRICE AS inventDisPriceKor, ")
                .append("case ")
                .append("when t.PRICE IS NULL then ps.price ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE <= 0 then ps.price ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND (t.DIS_PRICE IS NULL OR t.DIS_PRICE <= 0) then t.PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE >= t.DIS_PRICE then t.DIS_PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE < t.DIS_PRICE then t.PRICE ")
                .append("ELSE ps.price ")
                .append("END AS effectivePriceKor, ")
                .append("case ")
                .append("when t.PRICE IS NULL then 1 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE <= 0 then 1 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND (t.DIS_PRICE IS NULL OR t.DIS_PRICE <= 0) then 2 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE >= t.DIS_PRICE then 3 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE < t.DIS_PRICE then 2 ")
                .append("ELSE 1 ")
                .append("END AS priceType ")
                .append("FROM inventory_hotel t INNER JOIN product_sku ps ON t.PROD_SKU_ID = ps.id ")
                .append("INNER JOIN product p ON ps.product_id = p.id ")
                .append("WHERE t.STORE_ID = :storeId ")
                //.append("AND T.IS_OPENING = 1 ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') >= STR_TO_DATE(:startDate,'%Y-%m-%d') ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') < STR_TO_DATE(:endDate,'%Y-%m-%d') ")
                .append("AND p.`status` !=2  ")
                .append("AND p.`status` !=3  ");
        logger.info("查询指定酒店的所有产品的所有sku的日期期间内的价格SQL:::" + qrySQL.toString());
        List<HotelProductSkuDatePriceDTO> resultList = this.getNamedParameterJdbcTemplate().query(qrySQL.toString(),
                param, new BeanPropertyRowMapper<>(HotelProductSkuDatePriceDTO.class));
        return resultList;
    }

    /**
     * 从指定酒店房间库存中查询指定sku的日期期间内的价格
     *
     *
     * @param prodSkuId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<HotelProductSkuDatePriceDTO> getProductSkuDatePriceBySkuIdList(long prodSkuId, String startDate, String endDate) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("prodSkuId", prodSkuId);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        StringBuilder qrySQL = new StringBuilder("SELECT ");
        qrySQL.append("t.STORE_ID AS storeId,t.PROD_ID AS prodId,t.PROD_SKU_ID AS prodSkuId,t.INV_DATE AS priceDate, t.IS_OPENING AS isOpening,")
                .append("ps.price AS prodSkuPriceKor,t.PRICE as inventPriceKor,t.DIS_PRICE AS inventDisPriceKor, ")

                .append("case ")
                .append("when t.PRICE IS NULL then ps.price ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE <= 0 then ps.price ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND (t.DIS_PRICE IS NULL OR t.DIS_PRICE <= 0) then t.PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE >= t.DIS_PRICE then t.DIS_PRICE ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE < t.DIS_PRICE then t.PRICE ")
                .append("ELSE ps.price ")
                .append("END AS effectivePriceKor, ")// sku有效价格

                .append("case ")
                .append("when t.PRICE IS NULL then 1 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE <= 0 then 1 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND (t.DIS_PRICE IS NULL OR t.DIS_PRICE <= 0) then 2 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE >= t.DIS_PRICE then 3 ")
                .append("when t.PRICE IS NOT NULL AND t.PRICE > 0 AND t.PRICE < t.DIS_PRICE then 2 ")
                .append("ELSE 1 ")
                .append("END AS priceType, ")// sku价格类型

                .append("ps.PRICE_SETTLE AS prodPriceSettle, ")// 产品到手价

                .append("t.PRICE_SETTLE AS priceSettle, t.DIS_PRICE_SETTLE AS disPriceSettle, ")// 库存到手价

                .append("case ")
                .append("when t.PRICE_SETTLE IS NULL then ps.PRICE_SETTLE ")
                .append("when t.PRICE_SETTLE IS NOT NULL AND t.PRICE_SETTLE <= 0 then ps.PRICE_SETTLE ")
                .append("when t.PRICE_SETTLE IS NOT NULL AND t.PRICE_SETTLE > 0 then t.PRICE_SETTLE ")
                .append("ELSE ps.PRICE_SETTLE ")
                .append("END AS effectivePriceSettle ")// 有效到手价

                .append("FROM inventory_hotel t INNER JOIN product_sku ps ON t.PROD_SKU_ID = ps.id ")
                .append("WHERE t.PROD_SKU_ID = :prodSkuId ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') >= STR_TO_DATE(:startDate,'%Y-%m-%d') ")
                .append("AND str_to_date(INV_DATE,'%Y-%m-%d') < STR_TO_DATE(:endDate,'%Y-%m-%d') ");
        logger.info("查询指定酒店的指定sku的日期期间内的价格SQL:::" + qrySQL.toString());
        List<HotelProductSkuDatePriceDTO> resultList = this.getNamedParameterJdbcTemplate().query(qrySQL.toString(),
                param, new BeanPropertyRowMapper<>(HotelProductSkuDatePriceDTO.class));
        return resultList;
    }
}
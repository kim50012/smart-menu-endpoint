package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.batch.job.model.agent.AgentSettlementOrder;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component("OrderQuery")
public class JdbcOrderQueryImpl extends BaseRepository implements OrderQuery {

    private final static String ORDER_LIST_SELECT = "select o.id as id, \n" +
            "o.store_id as storeId, \n" +
            "o.table_id as tableId, \n" +
            "o.open_id as openId, \n" +
            "o.amount as amount, \n" +
            "o.payment_amount as paymentAmount, \n" +
            "o.discount_amount as discountAmount, \n" +
            "o.pay_amt_cny  as payAmtCny, \n" +
            "pay.cash_fee/100 as payAmtRmb,\n" +
            "o.pay_amt_usd as payAmtUsd, \n" +
            "o.krw_usd_rate as krwUsdRate, \n" +
            "o.usd_cny_rate as usdCnyRate, \n" +
            "o.status as status, \n" +
            "o.CHANGE_STATUS as changeStatus, \n" +
            "o.buyer_memo as buyerMemo, \n" +
            "o.customer_id as customerId, \n" +
            "o.pay_dt as payDt, \n" +
            "o.ORDER_TYPE as orderType, \n" +
            "o.created as created, \n" +
            "o.updated as updated, \n" +
            "(select max(p.create_dts) from eorder.order_pay p where p.order_id = o.id and p.pay_status = 1) pay_dt, \n" +
            "(select case when s.IS_PAY_SET = 0 then o.cancel_dt else max(c.createdt) end from eorder.order_pay_cancel c where c.order_id = o.id and c.return_code = 'SUCCESS' and c.result_code = 'SUCCESS') as cancel_dt, \n" +
            "s.name as storeNm, \n" +
            "s.store_type as storeType, \n" +
            "s.manager_id as managerId, \n" +
            "s.logo_url as logoUrl, \n" +
            "st.number as tableNum, \n" +
            "st.tag as tableTag, \n " +
            "CONCAT(tag,'-',number)as numTag ,\n " +
            " a.area_nm as areaNm,a.area_cd as areaCd\n " +
            ", inf.cust_nm as custNm\n" +
            ", ifnull(inf.country_no, 0) as countryNo\n" +
            ", inf.mobile as mobile\n" +
            ", inf.reseve_dt_from as reseveDtfrom\n" +
            ", inf.reseve_dt_to as reseveDtto\n" +
            ", inf.reseve_confirm_time as reseveConfirmtime\n" +
            ", ifnull(inf.reseve_time, 0) as reseveTime\n" +
            ", inf.confirm_dt_from as confirmDtfrom\n" +
            ", inf.confirm_dt_to as confirmDtto\n" +
            ", ifnull(inf.confirm_time, 0) as confirmTime\n" +
            ", ifnull(inf.num_persons, 0) as numPersons\n" +
            ", ifnull(inf.shipping_type, 0) as shippingType\n" +
            ", ifnull(inf.shipping_addr, 0) as shippingAddr\n" +
            ", case when inf.shipping_type = 1 then '现场实时自提' "
            + "		when inf.shipping_type = 2 then '现场预约时间自提' "
            + "		when inf.shipping_type = 3 then concat(sp.ship_point_nm,' ',sp.addr) "
            + "		when inf.shipping_type = 4 then '' "
            + "		else '' end as shippingAddrNm\n" +
            ", inf.shipping_dt as shippingDt\n" +
            ", ifnull(inf.shipping_time, 0) as shippingTime\n" +

            ", inf.shipping_mode as shippingMode\n" +
            ", inf.shipping_mode_name_chn as shippingModeNameChn\n" +
            ", inf.shipping_mode_name_kor as shippingModeNameKor\n" +
            ", inf.shipping_mode_name_eng as shippingModeNameEng\n" +
            ", inf.shipping_addr_detail as shippingAddrDetail\n" +
            ", inf.shipping_addr_country as shippingAddrCountry\n" +
            ", inf.shipping_weight as shippingWeight\n" +
            ", inf.shipping_cost as shippingCost\n" +
            ", inf.shipping_cost_rule as shippingCostRule\n" +

            ", inf.shipping_cmt as shippingCmt\n" +
            ", inf.DINING_PLACE as diningPlace " +
            ", inf.DINING_TIME as diningTime " +
            ", inf.cmt as cmt " +
            ", sre.REV_CONTENT as revContent,ifnull(sre.REV_ID,0)as revId "
            + ", inf.cust_no as custNo \n"
            + ", inf.cust_nm_en as custNmEn \n"
            + ", inf.nm_last as nmLast \n"
            + ", inf.nm_first as nmFirst \n"
            + ", inf.nm_en_last as nmLastEn \n"
            + ", inf.nm_en_first as nmFirstEn \n";


    private final static String ORDER_FROM = " from `order` o join store s on o.store_id = s.id" +
            " join area a on a.area_cd=s.city " +
            " join store_table st on o.table_id = st.id \n " +
            "  left join order_pay pay on o.id=pay.order_id \n " +
            "  left join order_info inf on o.id=inf.order_id " +
            "  left join ship_point sp on inf.shipping_addr=sp.ship_point_id " +
            "  LEFT JOIN str_review sre on sre.order_id=o.id and sre.REV_STATUS !=4 ";

    private final static String ORDER_ITEM_LIST_SELECT = "select oi.id as id, oi.order_id as orderId, oi.sku_id as skuId, oi.sku_nm_kor as skuNmKor, oi.sku_nm_chn as skuNmChn, oi.price as price, oi.price_cny as priceCny, oi.qty as qty, oi.created as created, oi.updated as updated, p.name_kor as prodNmKor, p.name_chn as prodNmChn, pi.image_url as prodUrl \n";

    private final static String ORDER_ITEM_FROM = " from order_item oi join product_sku ps on oi.sku_id = ps.id join product p on ps.product_id = p.id left join product_image pi on p.id = pi.product_id and pi.image_type = 1\n where 1 = 1 ";

    private final static String MY_CONTACT_LIST_SELECT = ""
            + "select \n" +
            "        a.cust_nm as custNm \n" +
            "        , a.country_no as countryNo \n" +
            "        , a.mobile as mobile  \n" +
            "        , a.nm_en_last as nmLastEn  \n" +
            "        , a.nm_en_first as nmFirstEn\n" +
            "        , max(b.created) as created" +
            "";

    private final static String MY_CONTACT_FROM = ""
            + "  from    eorder.order_info a\n" +
            "        inner join eorder.order b \n" +
            "                on a.order_id = b.id"
            + " where 1 = 1 ";


    @Override
    public int getOrderCount(Map<String, Object> param) {
        StringBuilder sb = new StringBuilder(SELECT_COUNT + ORDER_FROM);
        sb.append(getOrderQueryConditions(param, false));
        logger.debug("订单查询：订单数量查询SQL::" + sb.toString());
        return this.getNamedParameterJdbcTemplate().queryForObject(sb.toString(), param, Integer.class);
    }

    @Override
    public OrderDTO getOrderByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select * from (" + ORDER_LIST_SELECT + ORDER_FROM);
        sql.append(getOrderQueryConditions(param, false));
        logger.debug("订单查询：getOrderByMap：订单列表查询SQL::" + sql.toString());
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));
    }


    @Override
    public List<OrderDTO> getOrderListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(ORDER_LIST_SELECT + ORDER_FROM);
        query.append(getOrderQueryConditions(param, " order by o.created desc, o.id \n", true));
        //log.info(query.toString());
        logger.debug("订单查询：getOrderListByMap：：订单列表查询SQL::" + query.toString());
        List<OrderDTO> orderList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));
        return getOrderListWithItem(orderList);
    }

    @Override
    public List<OrderDTO> orderCompleteList(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(ORDER_LIST_SELECT + ORDER_FROM);
        String dayNum = Objects.toString(param.get("dayNum"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        query.append(" where o.status not in(1,2,3,4,7,8,9) and s.IS_PAY_SET = 1 and s.store_type in(1,4) \n");

        if (StringUtils.isNotBlank(storeId)) {
            query.append(" and o.store_id = :storeId \n");
        }

        if (StringUtils.isNotBlank(dayNum) && storeType.equals("4")) {//酒店
            query.append(" and  TIMESTAMPDIFF(day,inf.reseve_dt_to,DATE_FORMAT(now(),'%Y-%m-%d')) = :dayNum\n");
        } else if (StringUtils.isNotBlank(dayNum) && storeType.equals("1")) {//餐厅
            query.append(" and TIMESTAMPDIFF(day,DATE_FORMAT(o.created,'%Y-%m-%d'),DATE_FORMAT(now(),'%Y-%m-%d'))=:dayNum\n");
        }

        List<OrderDTO> orderList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));
        return orderList;
    }

    @Override
    public List<OrderDTO> retailOrderCompleteList(Map<String, Object> param) {
        StringBuilder query = new StringBuilder("SELECT o.id from `order` o \n" +
                " inner join store s on o.store_id = s.id\n" +
                " inner join order_pay pay on o.id=pay.order_id\n" +
                " inner join order_info inf on o.id=inf.order_id\n" +
                " inner JOIN retail_order_event roe on o.id = roe.ORDER_ID and roe.event_type= 11\n");
        String dayNum = Objects.toString(param.get("dayNum"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        query.append(" where o.status = 10 and s.IS_PAY_SET = 1 and CHANGE_STATUS = 0 and s.store_type = 3 \n");
        query.append(" and shipping_type in(4,5)\n");

        if (StringUtils.isNotBlank(storeId)) {
            query.append(" and o.store_id = :storeId \n");
        }
        if (StringUtils.isNotBlank(dayNum)) {
            query.append(" and  TIMESTAMPDIFF(day,DATE_FORMAT(roe.EVENT_TIME,'%Y-%m-%d'),DATE_FORMAT(now(),'%Y-%m-%d')) >= :dayNum\n");
        }


        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));
    }

    @Override
    public int getMyContactCount(Map<String, Object> param) {
        StringBuilder sb = new StringBuilder(SELECT_COUNT + " from (" + MY_CONTACT_LIST_SELECT + MY_CONTACT_FROM);
        sb.append(getMyContactQueryConditions(param, null, false));
        sb.append(") t");
//        log.info("订单联系人信息数量查询SQL>>>" + sb.toString());
        return this.getNamedParameterJdbcTemplate().queryForObject(sb.toString(), param, Integer.class);
    }


    @Override
    public List<OrderDTO> getMyContactListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder("select * from (" + MY_CONTACT_LIST_SELECT + MY_CONTACT_FROM);
        query.append(getMyContactQueryConditions(param, " order by created desc \n", true));
        query.append(") t");
//        log.info("订单联系人信息列表查询SQL>>>" + query.toString());
        List<OrderDTO> orderList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));


        return getOrderListWithItem(orderList);
    }

    protected List<OrderDTO> getOrderListWithItem(List<OrderDTO> orderList) {
        Map param = Maps.newHashMap();
        List<Long> orderIdList = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
        param.put("orderIds", orderIdList);
        List<OrderItemDTO> itemList = getOrderItemListByMap(param);//根据orderId数组查出来所有orderItem

        if (itemList != null && itemList.size() > 0) {
            Map<Long, List<OrderItemDTO>> groups = itemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));

            List<OrderDTO> resultList = orderList.stream().map(new Function<OrderDTO, OrderDTO>() {
                public OrderDTO apply(OrderDTO order) {

                    Long id = order.getId();

                    List<OrderItemDTO> oiList = groups.get(id);
                    if (oiList != null && oiList.size() > 0) {
                        order.setItemList(oiList);
                        order.setQty(oiList.stream().mapToInt(OrderItemDTO::getQty).sum());

                    }
                    return order;
                }

            }).collect(Collectors.toList());

            return resultList;
        } else {
            Map<Long, List<OrderItemDTO>> groups = itemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
            List<OrderDTO> resultList = orderList.stream().map(new Function<OrderDTO, OrderDTO>() {
                public OrderDTO apply(OrderDTO order) {
                    order.setItemList(new ArrayList<>());
                    order.setQty(0);

                    return order;
                }
            }).collect(Collectors.toList());
        }
        return orderList;
    }

    @Override
    public int getOrderItemCount(Map<String, Object> param) {
        StringBuilder sb = new StringBuilder(SELECT_COUNT + ORDER_ITEM_FROM);
        sb.append(getOrderItemQueryConditions(param, false));

        return this.getNamedParameterJdbcTemplate().queryForObject(sb.toString(), param, Integer.class);
    }

    @Override
    public List<OrderItemDTO> getOrderItemListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(ORDER_ITEM_LIST_SELECT + ORDER_ITEM_FROM);
        query.append(getOrderItemQueryConditions(param, " order by oi.updated desc, oi.id \n", true));
//        log.info(query);
        List<OrderItemDTO> orderItemDTOList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderItemDTO.class));
        return orderItemDTOList;
    }

    @Override
    public int getUserOrderCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from (  "
                + " select \n" +
                "        max(wu.nickname) as nickname \n" +
                "        , max(wu.headimgurl) as headimgurl\n" +
                "        , o.open_id\n" +
                "        , sum(payment_amount) as paysumamount\n" +
                "        , count(1) as qty  \n" +
                "from    bawechat.wx_user wu \n" +
                "        inner join eorder.`order` o \n" +
                "                on wu.openid = o.open_id  \n" +
                "where 1=1 ");
        sql.append(getUserOrderConditions(param));

        sql.append(" )t ");
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<UserOrderDTO> getUserOrderListByMap(Map<String, Object> param) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String sumAmount = Objects.toString(param.get("sumAmount"), null);
        String qty = Objects.toString(param.get("qty"), null);
        String laterTrainDate = Objects.toString(param.get("laterTrainDate"), null);

        StringBuilder sql = new StringBuilder();
        sql.append(" "
                + "select \n" +
                "        max(wu.nickname) as nickname \n" +
                "        , max(wu.headimgurl) as headimgurl\n" +
                "        , o.open_id\n" +
                "        , sum(payment_amount) as sumAmount\n" +
                "        , count(1) as qty  \n" +
                "        , DATE_FORMAT(max(o.created),'%Y-%m-%d %H:%i:%s')as laterTrainDate\n" +
                "        , (SELECT name from store WHERE id=(SELECT min(store_id) from `order` WHERE created=max(o.created)) )as laterStore \n" +
                "from    bawechat.wx_user wu \n" +
                "        inner join eorder.`order` o \n" +
                "                on wu.openid = o.open_id  \n" +
                "where 1=1  ");
        sql.append(getUserOrderConditions(param));

        if (StringUtils.isNotBlank(sumAmount)) {
            sql.append("order by sum(payment_amount) ").append(sumAmount);
        } else if (StringUtils.isNotBlank(qty)) {
            sql.append("order by count(1) ").append(qty);
        } else if (StringUtils.isNotBlank(laterTrainDate)) {
            sql.append("order by DATE_FORMAT(max(o.created),'%Y-%m-%d %H:%i:%s') ").append(laterTrainDate);
        } else {
            sql.append("order by DATE_FORMAT(max(o.created),'%Y-%m-%d %H:%i:%s') desc");
        }
        if (page >= 0 && size > 0) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sql.append(LIMIT);
        }

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(UserOrderDTO.class));
    }

    private String getUserOrderConditions(Map<String, Object> param) {
        String nickName = Objects.toString(param.get("nickName"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        StringBuilder condition = new StringBuilder();

        appendOrderStatusSql(condition);

        if (StringUtils.isNotBlank(nickName)) {
            condition.append(" and wu.NICK_UNEMOJI like '%' :nickName '%'");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            condition.append(" and o.created >= :startTime and o.created<=:endTime\n");
        }
        condition.append(" group by o.open_id ");

        return condition.toString();
    }

    private String getMyContactQueryConditions(Map<String, Object> param, String orderBy, boolean isLimit) {
        String openId = Objects.toString(param.get("openId"), null);
        String custNm = Objects.toString(param.get("custNm"), null);
        String countryNo = Objects.toString(param.get("countryNo"), null);
        String mobile = Objects.toString(param.get("mobile"), null);
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(openId)) {
            sb.append(" and b.open_id=:openId ");
        }
        if (StringUtils.isNotBlank(custNm)) {
            sb.append(" and (a.cust_nm like :custNm or a.cust_nm_en like :custNm) ");
        }
        if (StringUtils.isNotBlank(countryNo)) {
            sb.append(" and a.country_no=:countryNo ");
        }
        if (StringUtils.isNotBlank(mobile)) {
            sb.append(" and a.mobile=:mobile ");
        }

        sb.append(" and a.cust_nm is not null ");

        sb.append(" "
                + " group by\n" +
                "        a.cust_nm\n" +
                "        , a.country_no\n" +
                "        , a.mobile\n" +
                "        , a.nm_en_last\n" +
                "        , a.nm_en_first "
                + " ");

        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }

        if (page >= 0 && size > 0 && isLimit) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sb.append(LIMIT);

        }

        return sb.toString();
    }

    private String getOrderItemQueryConditions(Map<String, Object> param, boolean isLimit) {
        return getOrderItemQueryConditions(param, null, isLimit);
    }

    private String getOrderItemQueryConditions(Map<String, Object> param, String orderBy, boolean isLimit) {
        long orderId = NumberUtils.toLong(Objects.toString(param.get("orderId"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        long id = NumberUtils.toLong(Objects.toString(param.get("id"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        long skuId = NumberUtils.toInt(Objects.toString(param.get("skuId"), null));

        List<Long> orderIds = param.get("orderIds") != null ? (List<Long>) param.get("orderIds") : null;

        StringBuilder sb = new StringBuilder();

        if (orderId > 0) {
            sb.append(" and oi.order_id = :orderId \n");
        }

        if (skuId > 0) {
            sb.append(" and oi.sku_id = :skuId \n");
        }

        if (id > 0) {
            sb.append(" and oi.id = :id \n");
        }

        if (orderIds != null && orderIds.size() > 0) {
            sb.append(" and oi.order_id in (:orderIds) \n");
        }

        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }

        if (page >= 0 && size > 0 && isLimit) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sb.append(LIMIT);
        }

        return sb.toString();
    }


    protected String getOrderQueryConditions(Map<String, Object> param, boolean isLimit) {
        return getOrderQueryConditions(param, null, isLimit);
    }

    protected String getOrderQueryConditions(Map<String, Object> param, String orderBy, boolean isLimit) {
        String customerId = Objects.toString(param.get("customerId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String openId = Objects.toString(param.get("openId"), null);
        String areaNm = Objects.toString(param.get("areaNm"), null);
        String numTag = Objects.toString(param.get("numTag"), null);
        String city = Objects.toString(param.get("city"), null);
        String storeNm = Objects.toString(param.get("storeNm"), null);
        String storeType = Objects.toString(param.get("storeType"), null);

        String custNm = Objects.toString(param.get("custNm"), null);
        String mobile = Objects.toString(param.get("mobile"), null);
        String shippingType = Objects.toString(param.get("shippingType"), null);
        String shippingDt = Objects.toString(param.get("shippingDt"), null);

        String storeId = Objects.toString(param.get("storeId"), null);
        String id = Objects.toString(param.get("id"), null);
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String status = Objects.toString(param.get("status"), null);

        String reseveDtfrom = Objects.toString(param.get("reseveDtfrom"), null);
        String reseveDtto = Objects.toString(param.get("reseveDtto"), null);

        String custNo = Objects.toString(param.get("custNo"), null);
        String custNmEn = Objects.toString(param.get("custNmEn"), null);
        String isDeposit = Objects.toString(param.get("isDeposit"), "");

        StringBuilder sb = new StringBuilder();

        sb.append("where 1=1");
        if (StringUtils.isNotBlank(customerId)) {
            sb.append(" and o.customer_id = :customerId \n");
        }

        if (StringUtils.isNotBlank(storeId)) {
            sb.append(" and o.store_id = :storeId \n");
        }

        if (StringUtils.isNotBlank(id)) {
            sb.append(" and o.id like '%' :id '%'\n");
        }

        if (StringUtils.isNotBlank(isDeposit)) {
            sb.append(" and o.ORDER_TYPE = :isDeposit \n");
        }

        if (StringUtils.isNotBlank(status)) {
            sb.append(" and o.status = :status \n");
        }
        if (StringUtils.isNotBlank(openId)) {
            sb.append(" and o.open_id = :openId \n");
        }
        if (StringUtils.isNotBlank(areaNm)) {
            sb.append(" and a.area_nm = :areaNm \n");
        }
        if (StringUtils.isNotBlank(city)) {
            sb.append(" and a.area_cd = :city \n");
        }
        if (StringUtils.isNotBlank(storeNm)) {
            sb.append(" and s.name like '%' :storeNm '%'\n");
        }
        if (StringUtils.isNotBlank(numTag)) {
            sb.append(" and CONCAT(st.tag,'-',st.number)= :numTag \n");
        }
        if (StringUtils.isNotBlank(storeType)) {
            sb.append(" and s.store_type = :storeType \n");

            Date d = new Date();
            SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
            String dt = today.format(d);

            if ("2".equals(storeType) || "4".equals(storeType)) {
                if (StringUtils.isNotBlank(shippingDt)) {
                    if (shippingDt.equals(dt)) {
                        sb.append(" and inf.reseve_dt_from = :shippingDt \n");
                    } else {
                        sb.append(" and inf.reseve_dt_from >= :shippingDt \n");
                    }
                }
                if (StringUtils.isNotBlank(reseveDtfrom) && StringUtils.isNotBlank(reseveDtto)) {
                    sb.append(" and inf.reseve_dt_from >= :reseveDtfrom \n");
                    sb.append(" and inf.reseve_dt_from <= :reseveDtto \n");
                }
            } else if ("3".equals(storeType)) {
                if (StringUtils.isNotBlank(shippingDt)) {
                    if (shippingDt.equals(dt)) {
                        sb.append(" and inf.shipping_dt = :shippingDt \n");
                    } else {
                        sb.append(" and inf.shipping_dt >= :shippingDt \n");
                    }
                }
                if (StringUtils.isNotBlank(reseveDtfrom) && StringUtils.isNotBlank(reseveDtto)) {
                    sb.append(" and inf.shipping_dt >= :reseveDtfrom \n");
                    sb.append(" and inf.shipping_dt <= :reseveDtto \n");
                }
            } else {
                if (StringUtils.isNotBlank(shippingDt)) {
                    sb.append(" and inf.reseve_dt_from >= :shippingDt \n");
                }
            }
        }

        if (StringUtils.isNotBlank(custNm)) {
            sb.append(" and (inf.cust_nm like :custNm or inf.cust_nm_en like :custNm) \n");
        }

        if (StringUtils.isNotBlank(custNo)) {
            sb.append(" and inf.cust_no = :custNo \n");
        }

        if (StringUtils.isNotBlank(mobile)) {
            sb.append(" and inf.mobile like :mobile \n");
        }

        if (StringUtils.isNotBlank(shippingType)) {
            sb.append(" and inf.shipping_type = :shippingType \n");
        }

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sb.append(" and o.created >= :startTime and o.created<=:endTime\n");
        }

        sb.append(" and o.status !=0 and  o.status !=1 and o.status !=2 ");

        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }


        if (page >= 0 && size > 0 && isLimit) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sb.append(LIMIT);

        }

        return sb.toString();
    }

    @Override
    public List<OrderSumStatsDTO> getOrderSumStatisList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        String storeId = Objects.toString(param.get("storeId"), null);

        sql.append("  SELECT 'today' as date,  ifnull(sum(payment_amount),0)paySumAmount,ifnull(count(1),0) qty  FROM `order` o where to_days(created) = to_days(now())\n ");
        if (StringUtils.isNotBlank(storeId)) {
            sql.append("  and o.store_id = :storeId ");
        }
        appendOrderStatusSql(sql);

        sql.append("  UNION SELECT 'tswk' as date, ifnull(sum(payment_amount),0)paySumAmount,ifnull(count(1),0) qty  FROM `order` o WHERE YEARWEEK(date_format(created,'%Y-%m-%d')) = YEARWEEK(now())  \n");
        if (StringUtils.isNotBlank(storeId)) {
            sql.append("  and o.store_id = :storeId ");
        }
        appendOrderStatusSql(sql);
        sql.append("  UNION SELECT 'tsmt' as date, ifnull(sum(payment_amount),0)paySumAmount,ifnull(count(1),0) qty FROM `order` o WHERE DATE_FORMAT( created, '%Y%m' ) = DATE_FORMAT( CURDATE( ),'%Y%m')   \n");
        if (StringUtils.isNotBlank(storeId)) {
            sql.append("  and o.store_id = :storeId ");
        }
        appendOrderStatusSql(sql);
        sql.append("  UNION  SELECT 'tsyear' as date, ifnull(sum(payment_amount),0)paySumAmount,ifnull(count(1),0) qty  FROM `order` o where YEAR(created)=YEAR(NOW())\n");
        if (StringUtils.isNotBlank(storeId)) {
            sql.append("  and o.store_id = :storeId ");
        }
        appendOrderStatusSql(sql);
        sql.append("union\n");
        sql.append(" select 'chargeFee' as date,sum(paySumAmount),sum(ORDER_COUNT)qty  from (\n" +
                "                select sum(PL_FINAL_FEE)paySumAmount,sum(ORDER_COUNT)ORDER_COUNT\n" +
                "                from store_settlement\n" +
                "                where YEAR(START_DT)=YEAR(NOW()) and \n" +
                "                   START_DT < date_add(curdate(), interval - day(curdate()) + 1 day)\n" +
                "                union\n" +
                "                select sum(PL_FINAL_FEE)paySumAmount,sum(ORDER_COUNT)ORDER_COUNT\n" +
                "                from store_settlement_day\n" +
                "                where SETTLE_TYPE != 3 and  YEAR(SETTLE_DATE)=YEAR(NOW()) and SETTLE_DATE>=date_add(curdate(), interval - day(curdate()) + 1 day)\n" +
                "              )t");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(OrderSumStatsDTO.class));
    }


    //店铺销量前十名
    @Override
    public List<OrderSumRankStoreStatsDTO> getOrderSumRankStoreStatisList(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT store_id,s.name as storeNm, IFNULL(sum(payment_amount),0)paySumAmount, count(1)as qty  from `order` o ");
        sql.append(" left join store s on o.store_id=s.id where 1=1 ");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime ");
        }
        appendOrderStatusSql(sql);
        sql.append("  GROUP BY store_id  ");

        sql.append("  order by paySumAmount desc limit 0,10  ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(OrderSumRankStoreStatsDTO.class));
    }

    //产品销量前十名
    @Override
    public List<ProductSaleRankDTO> getProductSaleRankList(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id as productId,p.name_chn as nameChn,p.name_kor as nameKor,oi.price,\n");
        sql.append(" oi.price*sum(qty) paySumAmount,sum(qty)as qty \n");
        sql.append(" FROM `order` o \n");
        sql.append(" inner join order_item oi on o.id=oi.order_id \n");
        sql.append(" inner join product_sku ps on oi.sku_id=ps.id \n");
        sql.append(" inner join product p on p.id=ps.product_id where 1=1\n");

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id = :storeId \n");
        }

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime\n");
        }
        appendOrderStatusSql(sql);

        sql.append(" group by p.id ");
        sql.append(" order by paySumAmount desc limit 0,10 ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(ProductSaleRankDTO.class));

    }

    //分类排行前十名
    @Override
    public List<CategorySaleRankDTO> getCategorySaleRankList(Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id as productId,");
        sql.append(" p.category_id,bc.chn_name as nameChn,bc.`name` as nameKor,");
        sql.append(" oi.price ,oi.price*sum(qty) paySumAmount,sum(qty)as qty");
        sql.append(" FROM `order` o ");
        sql.append(" inner join order_item oi on o.id=oi.order_id");
        sql.append(" inner join product_sku ps on oi.sku_id=ps.id");
        sql.append(" inner join product p on p.id=ps.product_id");
        sql.append(" INNER JOIN base_category bc on p.category_id=bc.id where 1=1 ");

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id = :storeId ");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime ");
        }
        appendOrderStatusSql(sql);
        sql.append(" group by p.category_id ");
        sql.append(" order by paySumAmount desc limit 0,10 ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(CategorySaleRankDTO.class));
    }

    private void appendOrderStatusSql(StringBuilder sql) {
        sql.append(" and (o.status in (4,5,6,8,9,10,11,12)) ");
    }

    @Override
    public List<QtyAndDateDTO> getQtyAndDate(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String dateType = Objects.toString(param.get("dateType"), null);

       /* select DATE_FORMAT(created,'%Y%u') weeks,count(1)qty,IFNULL(sum(payment_amount),0)paySumAmount from `order`
        WHERE DATE_FORMAT(created,'%Y-%m-%d')='2019-02-19'
        group by weeks;*/

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.id ");
        if ("1".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y-%m-%d') as date ");
        } else if ("2".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y%u') as date ");
        } else if ("3".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y-%m') as date ");
        }
        sql.append(" ,store_id");
        sql.append(" ,count(1)as qty  from `order` o ");
        sql.append(" left join store s on o.store_id=s.id where 1=1 ");

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime ");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id=:storeId ");
        }
        appendOrderStatusSql(sql);
        sql.append(" GROUP BY date");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(QtyAndDateDTO.class));

    }

    @Override
    public List<PayAmountAndDateDTO> getPayAmountAndDate(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String dateType = Objects.toString(param.get("dateType"), null);


        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.id ");
        if ("1".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y-%m-%d') as date ");
        } else if ("2".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y%u') as date ");
        } else if ("3".equals(dateType)) {
            sql.append(" ,DATE_FORMAT(o.created,'%Y-%m') as date ");
        }
        sql.append(" ,store_id,sum(payment_amount)payAmount ");
        sql.append(" from `order` o");
        sql.append(" left join store s on o.store_id=s.id where 1=1 ");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime ");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id=:storeId ");
        }
        appendOrderStatusSql(sql);
        sql.append(" GROUP BY date");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(PayAmountAndDateDTO.class));

    }

    public BigDecimal getSumPayAmountForDate(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(payment_amount) as payAmountSum from `order` o where 1=1 ");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created<=:endTime ");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id=:storeId ");
        }
        appendOrderStatusSql(sql);
        BigDecimal amoutSum = this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, BigDecimal.class);
        return amoutSum == null ? new BigDecimal(0) : amoutSum;
    }


    /**
     * Admin CMS商户结算-查询指定商户指定日期之间的结算信息
     *
     * @param param
     * @return
     */
    @Override
    public SettleDTO getSettleByMap(Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        StringBuilder sql = new StringBuilder();
        String receiveMonth = startTime.substring(0, 7);
        String receiveDt = startTime.substring(0, 7) + "-14";
        logger.info("【getSettleByMap】receiveMonth和receiveDt分别为::" + receiveMonth + "和" + receiveDt);
        param.put("storeId", storeId);
        param.put("receiveMonth", receiveMonth);
        param.put("receiveDt", receiveDt);
        sql.append(" select ");
        sql.append(" date_format(min(t3.pay_dts), '%Y-%m') as closing_months");
        sql.append(" , date_format(min(t3.pay_dts), '%Y-%m-%d') as pay_fr_dt");
        sql.append(" , date_format(max(t3.pay_dts), '%Y-%m-%d') as pay_to_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(t3.pay_dts), '%Y-%m-%d')) as pg_fr_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(t3.pay_dts), '%Y-%m-%d')) as pg_to_dt");
        sql.append(" , sum(t2.amount) * 0.98 as pg_amount");
        sql.append(" , count(*) payCnt");
        sql.append(" , sum(t2.amount) as amount");
        sql.append(" , sum(t2.amount) * 0.02 as pg_fee");
        sql.append(" , round(sum(t2.amount) * 0.03) as service_fee");
        sql.append(" , (select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt    ");
        sql.append(" , (SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= :storeId and closing_months=:receiveMonth and status=1)as baSureReciveDt    ");
        sql.append(" , ifnull((select a.status from    eorder.order_closing a where   a.store_id = t1.id \n" +
                "  and a.closing_months = date_format(min(t3.pay_dts), '%Y-%m') ), 0) as status");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where   1 = 1");

        getSettleCondition(sql, param);

        sql.append(" HAVING amount>0 ");

        logger.info("【getSettleByMap】查询SQL为::" + sql.toString());

        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));
    }


    @Override
    public int getSettleCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from (");
        sql.append(" select");
        sql.append(" count(1) ");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where 1=1  ");
        String groupBySql = getSettleCondition(sql, param);
        sql.append(" group by ");
        sql.append(groupBySql);
        sql.append(")t");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<SettleDTO> getSettleListByMap(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String receiveMonth = startTime.substring(0, 7);
        String receiveDt = startTime.substring(0, 7) + "-04";
        param.put("receiveMonth", receiveMonth);
        param.put("receiveDt", receiveDt);
        StringBuilder sql = new StringBuilder();
        String storeId = Objects.toString(param.get("storeId"), null);
        if (StringUtils.isBlank(storeId)) {//admin
            sql.append("   select");
            sql.append("   t1.id as storeId");
            sql.append("       , t1.name as storeNm");
            sql.append("   , (select a.area_nm from eorder.area a where a.area_cd = t1.city) as areaNm");
            sql.append("   , date_format(min(t3.pay_dts), '%Y-%m') as closingMonths");
            sql.append("   , date_format(min(t3.pay_dts), '%Y-%m-%d') as payFrDt");
            sql.append("   , date_format(max(t3.pay_dts), '%Y-%m-%d') as payToDt");
            sql.append("   , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(t3.pay_dts), '%Y-%m-%d')) as pgFrDt");
            sql.append("   , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(t3.pay_dts), '%Y-%m-%d')) as pgToDt");
            sql.append("   , sum(t2.amount) * 0.98 as pgAmount");
            sql.append("   , sum(t2.amount) as amount");
            sql.append("   , sum(t2.amount) * 0.02 as pgFee");
            sql.append("   , round(sum(t2.amount) * 0.03) as serviceFee");
            sql.append("   , (select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt    ");
            sql.append("   ,	(SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= t1.id and closing_months=:receiveMonth and status=1)as baSureReciveDt ");
            sql.append("   , ifnull((");
            sql.append("           select");
            sql.append("       a.status");
            sql.append("       from    eorder.order_closing a");
            sql.append("       where   a.store_id = t1.id");
            sql.append("       and     a.closing_months = date_format(min(t3.pay_dts), '%Y-%m')");
            sql.append("   ), 0) as status");
            sql.append("   from    eorder.store t1");
            sql.append("    inner join eorder.`order` t2");
            sql.append("    on t1.id = t2.store_id");
            sql.append("    and t2.status in (4,5,6,8,9,10,11)");
            sql.append("    inner join eorder.order_pay t3");
            sql.append("    on t2.id = t3.order_id");
            sql.append("    and t3.pay_status = 1");
        } else {//mananger
            sql.append(" select");
            sql.append(" date_format(t3.pay_dts, '%Y-%m-%d') as payDt");
            sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(t3.pay_dts, '%Y-%m-%d')) as pgPlanDt");
            sql.append(" , count(*) payCnt");
            sql.append(" ,sum(t2.amount) * 0.98 as pgAmount");
            sql.append(" , sum(t2.amount) as amount");
            sql.append(" , sum(t2.amount) * 0.02 as pgFee");
            sql.append(" , round(sum(t2.amount) * 0.03) as serviceFee");
            sql.append(" from    eorder.store t1");
            sql.append(" inner join eorder.`order` t2");
            sql.append(" on t1.id = t2.store_id");
            sql.append(" and t2.status in (4,5,6,8,9,10,11)");
            sql.append(" inner join eorder.order_pay t3");
            sql.append(" on t2.id = t3.order_id");
            sql.append(" and t3.pay_status = 1 ");
        }
        sql.append(" where 1=1  ");
        String groubySql = getSettleCondition(sql, param);
        sql.append(" group by");
        sql.append(groubySql);

//        log.info(sql.toString());

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));

    }

    private String getSettleCondition(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeNm = Objects.toString(param.get("storeNm"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeType = Objects.toString(param.get("storeType"), null);

        if (StringUtils.isNotBlank(storeId)) { //t1 store
            sql.append(" and t1.id=:storeId ");
        }
        if (StringUtils.isNotBlank(storeType)) {
            sql.append(" and t1.store_type=:storeType ");
        }
        if (StringUtils.isNotBlank(storeNm)) {
            sql.append(" and     t1.name like '%' :storeNm '%' ");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and     DATE_FORMAT(t3.pay_dts,'%Y-%m-%d') >= :startTime ");
            sql.append(" and     DATE_FORMAT(t3.pay_dts,'%Y-%m-%d') <= :endTime");
        }

        String groupSql = StringUtils.isBlank(storeId) ? " t1.id,t1.name,t1.city" : " date_format(t3.pay_dts, '%Y-%m-%d')";
        return groupSql;
    }


    @Override
    public int getSettleDtlCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" count(1)");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where   1 = 1");
        getSettleCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);

    }

    @Override
    public List<SettleDTO> getSettleDtlListByMap(Map<String, Object> param) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String startTime = Objects.toString(param.get("startTime"), null);
        String receiveDt = startTime.substring(0, 7) + "-14";
        param.put("receiveDt", receiveDt);
        StringBuilder sql = new StringBuilder();
        sql.append("select"); //admin端/manager端
        sql.append(" t2.id as orderId,");
        sql.append("     t1.id");
        sql.append("     , t1.name as store_nm");
        sql.append(" , (select a.area_nm from eorder.area a where a.area_cd = t1.city) as area_nm");
        sql.append(" , date_format(t3.pay_dts, '%Y-%m') as closing_months");
        sql.append(" , date_format(t3.pay_dts, '%Y-%m-%d') as pay_fr_dt");
        sql.append(" , date_format(t3.pay_dts, '%Y-%m-%d') as pay_to_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(t3.pay_dts, '%Y-%m-%d')) as pg_fr_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(t3.pay_dts, '%Y-%m-%d')) as pg_to_dt");
        sql.append(" , t2.amount * 0.98 as pg_amount");
        sql.append(" , t2.amount as amount");
        sql.append("     , t2.amount * 0.02 as pg_fee");
        sql.append(" , round(t2.amount * 0.03) as service_fee");
        sql.append(" 		,(select a.after_one_working_days from eorder.calendar a where a.dt = date_add(:receiveDt, interval 1 month) ) as baReciveDt");
        sql.append(" , ifnull((");
        sql.append("         select");
        sql.append("     a.status");
        sql.append("     from    eorder.order_closing a");
        sql.append("     where   a.store_id = t1.id");
        sql.append("     and     a.closing_months = date_format(t3.pay_dts, '%Y-%m')");
        sql.append(" ), 0) as status");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where   1 = 1");
        getSettleCondition(sql, param);

        if (page >= 0 && size > 0) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sql.append(LIMIT);
        }


        List<SettleDTO> settleDTOList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));
        Map<String, Object> itemParam = Maps.newHashMap();

        settleDTOList.stream().map(s -> {
//            if (s != null && s.getOrderId() != null) {
//                itemParam.put("orderId", s.getOrderId());
//                List<OrderItemDTO> itemList = getOrderItemListByMap(itemParam);
//                if (itemList != null && itemList.size() > 0) {
//                    s.setItemList(itemList);
//                } else {
//                    s.setItemList(new LinkedList<>());
//                }
//            }
            return s;
        }).collect(Collectors.toList());
        return settleDTOList;
    }

    /**
     * 查询酒店类待恢复库存的临时订单（1天内，且20分钟前下单未支付的订单）
     * <p>
     * TODO 如果用户拉起支付，长时间（超过20分钟）不支付，库存恢复程序会恢复掉该订单所占库存，
     * TODO 这样会造成该订单再次支付不能再扣除库存，导致多卖
     * <p>
     * TODO 解决方案：订单下单时指定prepay_id的有效时间，建议设置为20分钟
     *
     * @return
     */
    public List<Map<String, Object>> queryTempOrdersToRecover() {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        /*
        SELECT distinct ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty,oift.reseve_dt_from AS reseveDtFrom,oift.reseve_dt_to AS reseveDtTo
         FROM order_temp ot,order_item_temp oit,order_info_temp oift
        WHERE ot.created >= date_sub(NOW(), INTERVAL 1 DAY)
        AND ot.created <= date_sub(NOW(), INTERVAL 1 HOUR)
        AND ot.ORDER_TYPE = 1
        AND ot.IS_RECOVER = 0
        AND ot.store_id IN (SELECT id FROM store s WHERE s.`status` != 3 AND s.store_type = 4)
        AND ot.trans_id NOT IN (SELECT o.id FROM `order` o WHERE o.created >= date_sub(NOW(), INTERVAL 1 DAY))
        AND oit.order_id = ot.id
        AND oift.pay_id = ot.id;
         */
        StringBuilder querySQL = new StringBuilder("SELECT distinct ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty,oift.reseve_dt_from AS reseveDtFrom,oift.reseve_dt_to AS reseveDtTo ");
        querySQL.append("FROM order_temp ot,order_item_temp oit,order_info_temp oift ")
                .append("WHERE ot.created >= date_sub(NOW(), INTERVAL 1 DAY) ")
                //.append("AND ot.created <= date_sub(NOW(), INTERVAL 1 HOUR) ")
                .append("AND ot.created <= date_sub(NOW(), INTERVAL 20 MINUTE) ")
                .append("AND ot.ORDER_TYPE = 1 ")
                .append("AND ot.IS_RECOVER = 0 ")
                .append("AND ot.store_id IN (SELECT id FROM store s WHERE s.`status` != 3 AND s.store_type = 4) ")
                .append("AND ot.trans_id NOT IN (SELECT o.id FROM `order` o WHERE o.created >= date_sub(NOW(), INTERVAL 1 DAY)) ")
                .append("AND oit.order_id = ot.id ")
                .append("AND oift.pay_id = ot.id");
        return this.getNamedParameterJdbcTemplate().queryForList(querySQL.toString(), param);
    }

    /**
     * 根据订单ID查询酒店的临时订单
     *
     * @return
     */
    public List<Map<String, Object>> queryTempOrderByIdToRecover(String tempOrderId) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("tempOrderId", tempOrderId);
        StringBuilder querySQL = new StringBuilder("SELECT distinct ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty,oift.reseve_dt_from AS reseveDtFrom,oift.reseve_dt_to AS reseveDtTo ");
        querySQL.append("FROM order_temp ot,order_item_temp oit,order_info_temp oift ")
                .append("WHERE ot.ORDER_TYPE = 1 ")
                .append("AND ot.IS_RECOVER = 0 ")
                .append("AND oit.order_id = ot.id ")
                .append("AND oift.pay_id = ot.id ")
                .append("AND ot.id = :tempOrderId");
        return this.getNamedParameterJdbcTemplate().queryForList(querySQL.toString(), param);
    }

    /**
     * 零售业务：查询零售业务类待恢复库存的临时订单（1天内，且20分钟前下单未支付的订单）
     * <p>
     * TODO 如果用户拉起支付，长时间（超过20分钟）不支付，库存恢复程序会恢复掉该订单所占库存，
     * TODO 这样会造成该订单再次支付不能再扣除库存，导致多卖
     * TODO 解决方案：订单下单时指定prepay_id的有效时间，建议设置为20分钟
     *
     * @return
     */
    public List<RetailToDoRecoverTempOrder> queryRetailTempOrdersToRecover() {
        /*
        查询逻辑：
        1、查询一天内，20分钟前的临时订单；
        2、订单类型为正常（非押金产品），并且库存没有恢复的临时订单
        3、零售业务商户的临时订单
        4、未进行支付的临时订单（即正式订单表中没有数据的临时订单）
        SELECT ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty
         FROM order_temp ot,order_item_temp oit
        WHERE ot.created >= date_sub(NOW(), INTERVAL 1 DAY)
        AND ot.created <= date_sub(NOW(), INTERVAL 20 MINUTE)
        AND ot.ORDER_TYPE = 1
        AND ot.IS_RECOVER = 0
        AND ot.store_id IN (SELECT id FROM store s WHERE s.`status` != 3 AND s.store_type = 3)
        AND ot.trans_id NOT IN (SELECT o.id FROM `order` o WHERE o.BIZ_TYPE = 3 AND o.created >= date_sub(NOW(), INTERVAL 1 DAY))
        AND oit.order_id = ot.id;
         */
        StringBuilder querySQL = new StringBuilder("SELECT ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty");
        querySQL.append(" FROM order_temp ot,order_item_temp oit")
                .append(" WHERE ot.created >= date_sub(NOW(), INTERVAL 1 DAY)")
                //.append(" AND ot.created <= date_sub(NOW(), INTERVAL 1 HOUR)")
                .append(" AND ot.created <= date_sub(NOW(), INTERVAL 20 MINUTE)")
                .append(" AND ot.ORDER_TYPE = 1")
                .append(" AND ot.IS_RECOVER = 0")
                .append(" AND ot.store_id IN (SELECT id FROM store s WHERE s.`status` != 3 AND s.store_type = 3)")
                .append(" AND ot.trans_id NOT IN (SELECT o.id FROM `order` o WHERE o.BIZ_TYPE = 3 AND o.created >= date_sub(NOW(), INTERVAL 1 DAY))")
                .append(" AND oit.order_id = ot.id");

        // 空的查询参数
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), Maps.newHashMap(), new BeanPropertyRowMapper<>(RetailToDoRecoverTempOrder.class));
    }

    /**
     * 零售业务：
     * 库存恢复：
     * 根据订单ID查询酒店的临时订单
     *
     * @param tempOrderId
     * @param queryType
     * @return
     */
    public List<RetailToDoRecoverTempOrder> queryRetailTempOrderByIdToRecover(String tempOrderId, String queryType) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("tempOrderId", tempOrderId);
        StringBuilder querySQL = new StringBuilder("SELECT ot.trans_id AS transId,ot.id AS id,ot.store_id AS storeId,oit.sku_id AS skuId,oit.qty AS qty");
        querySQL.append(" FROM order_temp ot,order_item_temp oit ")
                .append(" WHERE oit.order_id = ot.id AND ot.IS_RECOVER = 0");
        // 退款处的查询利用trans_id
        if ("refund".equals(queryType)) {
            querySQL.append(" AND ot.trans_id = :tempOrderId");
        }
        // 未支付接口处查询利用id
        else if ("auto".equals(queryType)) {
            querySQL.append(" AND ot.id = :tempOrderId");
        }

        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(RetailToDoRecoverTempOrder.class));
    }


    /**********************************************PG交易类商户月度结算-start**************************************************/
    /**
     * 查询商户指定结算月有效交易订单总金额
     * 有效交易订单总金额=非退款订单总金额 + 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * <p>
     * 对
     * 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * 的解释：是指结算进行月，即结算进行月开始第一秒，到在该结算进行月进行的商户月度结算任务运行起始那一秒，这期间发生的结算月交易订单的退款
     *
     * @param storeId
     * @param monthStartTime 结算月起始时间
     * @param monthEndTime   结算月截止时间
     * @return
     */
    public Map<String, Object> getPGStoreEffectiveOrderAmountSum(Long storeId
            , String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询正常订单（非退款订单）SQL：订单状态为4或5，即支付成功或订单接收。另外6,8,9,10,11也都要计算。
        // 订单状态定义见com.basoft.eorder.domain.model.Order中。
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` IN (4,5,6,8,9,10,11) AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";

        // 20191231:系统只结算完成状态的订单。补充说明：对于各个业务线订单状态完成置9的定时任务，一般都在次日凌晨运行。具体设置逻辑详见StoreOrderStatusJob
        // 20200102：PG结算类商户的月度服务费结算还是结算所有状态的订单。
        // String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` = 9 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";


        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.debug("【PG交易类商户月度结算】【{}】计算有效交易订单总金额>>>非退款订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 查询非结算月退款订单SQL：订单状态为7，且退款日期非结算月内
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.debug("【PG交易类商户月度结算】【{}】计算有效交易订单总金额>>>结算月内成交的退款订单且不是在结算月退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 汇总计算-start
        Map resultMap = Maps.newHashMap();
        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        resultMap.put("orderCount", orderCount + refundOrderCount);

        BigDecimal orderTotalAmount = (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");
        resultMap.put("orderTotalAmount", orderTotalAmount.add(refundOrderTotalAmount));
//        log.debug("【PG交易类商户月度结算】【{}】有效交易订单总金额（非退款订单总金额 + 结算月内成交的退款订单且不是在结算月退款的订单总金额）信息如下：{}", storeId, resultMap);
        // 汇总计算-end

        return resultMap;
    }

    /**
     * 查询商户指定日期期间的有效订单总金额（含退款订单）
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @return
     */
    @Deprecated
    public Map<String, Object> getPGStoreOrderAllAmountSum(Long storeId, String monthStartTime, String monthEndTime) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);

        // 查询SQL：订单状态为4或5，即支付成功或订单接收。另外6,8,9,10,11也都要计算。
        // String sql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` IN (4,5,6,8,9,10,11) AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')";

        // 查询SQL：订单状态为4或5，即支付成功或订单接收。另外6,8,9,10,11也都要计算。状态为7的退款成功订单也计算
        // String sql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` IN (4,5,6,7,8,9,10,11) AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')";
        String sql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }

    /**
     * 查询指定商户当前结算月和当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * <p>
     * 假定如下：
     * （1）假定当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
     * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
     * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
     * 2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
     * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
     * （5）特别说明：结算月的退款成功订单还可能存在2019.09.01-2019.09.05结算时间点，此
     * 部分订单将会在2019.09结算月进行计算哦，也就是将在2019.10.05号进行结算。
     *
     * @param storeId
     * @param monthStartTime  2019-08-01 00:00:00
     * @param monthEndTime    2019-08-31 23:59:59
     * @param refundYearMonth 201908
     * @return
     */
    @Deprecated
    public Map<String, Object> getPGStoreRefundOrderAmountSum(Long storeId,
                                                              String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询SQL:查询结算月和结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
        // 特别说明，此SQL并没有查询出结算月在当前月退款的订单，这部分订单将在结算对当前月结算时计算。
        String sql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }

    /**
     * 查询指定商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内的任何退款订单。
     * <p>
     * 假定如下：
     * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
     * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
     * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
     * 2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
     * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
     * <p>
     * 查询2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
     * 2019.01，2018.12，2018.11，2018.10，2018.09，2018.08这12个月内的退款订单，并且退款日期为2019.08.01-2019.08.31
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public Map<String, Object> getPGStoreRefundBeforeOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询SQL:结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
        String sql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL)";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }

    /**
     * 查询指定商户指定年月PG结算信息
     *
     * @param param
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[getSettleByMap]
     * 去掉注释和see参考方法完全相同
     */
    public SettleDTO getPGSettleByMap(Map<String, Object> param) {
        //String storeId = Objects.toString(param.get("storeId"), null);
        //String startTime = Objects.toString(param.get("startTime"), null);
        StringBuilder sql = new StringBuilder();
        //String receiveMonth = startTime.substring(0,7);
        //String receiveDt = startTime.substring(0,7)+"-14";
        //param.put("storeId", storeId);
        //param.put("receiveMonth", receiveMonth);
        //param.put("receiveDt", receiveDt);
        sql.append(" select ");
        sql.append(" date_format(min(t3.pay_dts), '%Y-%m') as closing_months");
        sql.append(" , date_format(min(t3.pay_dts), '%Y-%m-%d') as pay_fr_dt");
        sql.append(" , date_format(max(t3.pay_dts), '%Y-%m-%d') as pay_to_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(t3.pay_dts), '%Y-%m-%d')) as pg_fr_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(t3.pay_dts), '%Y-%m-%d')) as pg_to_dt");
        sql.append(" , sum(t2.amount) * 0.98 as pg_amount");
        sql.append(" , count(*) payCnt");
        sql.append(" , sum(t2.amount) as amount");
        sql.append(" , sum(t2.amount) * 0.02 as pg_fee");
        sql.append(" , round(sum(t2.amount) * 0.03) as service_fee");
        //sql.append(" , (select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt    ");
        //sql.append(" , (SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= :storeId and closing_months=:receiveMonth and status=1)as baSureReciveDt    ");
        sql.append(" , ifnull((select a.status from    eorder.order_closing a where   a.store_id = t1.id \n" +
                "  and a.closing_months = date_format(min(t3.pay_dts), '%Y-%m') ), 0) as status");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where   1 = 1");

        getPGSettleCondition(sql, param);
        //sql.append(" HAVING amount>0 ");
        logger.debug("【PG交易类或BA交易类商户月度结算】【" + Objects.toString(param.get("storeId"), "STORE_ID") + "】【getPGSettleByMap】查询SQL为::" + sql.toString());

        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));
    }

    /**
     * @param sql
     * @param param
     * @see com.basoft.eorder.interfaces.query.OrderQuery[getSettleCondition]
     */
    private void getPGSettleCondition(StringBuilder sql, Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);

        //t1 store
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and t1.id=:storeId ");
        }

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and t3.pay_dts >= STR_TO_DATE(:startTime, '%Y-%m-%d %H:%i:%s')");
            sql.append(" and t3.pay_dts <= STR_TO_DATE(:endTime, '%Y-%m-%d %H:%i:%s')");
        }
    }
    /**********************************************PG交易类商户月度结算-end**************************************************/


    /**********************************************PG交易类商户每日结算-start**************************************************/
    /**
     * PG交易类商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=非退款订单 + 非昨日退款订单
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getPGStoreDailyEffectiveOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 查询结算日正常订单（非退款订单）SQL：订单状态为4或5，即支付成功或订单接收。另外6,8,9,10,11也都要计算。
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` IN (4,5,6,8,9,10,11) AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.info("【PG交易类商户每日结算】【{}】计算有效交易订单总金额>>>非退款订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 查询结算日交易但是非结算日退款的订单SQL：订单状态为7，且退款日期非结算日
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.info("【PG交易类商户每日结算】【{}】计算有效交易订单总金额>>>结算日内成交的退款订单且不是在结算日退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 汇总计算-start
        Map resultMap = Maps.newHashMap();
        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        resultMap.put("dailyOrderCount", orderCount + refundOrderCount);

        BigDecimal orderTotalAmount = (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");
        resultMap.put("dailyOrderTotalAmount", orderTotalAmount.add(refundOrderTotalAmount));
//        log.info("【PG交易类商户每日结算】【{}】有效交易订单总金额（非退款订单总金额 + 结算日内成交的退款订单且不是在结算日退款的订单总金额）信息如下：{}", storeId, resultMap);
        // 汇总计算-end

        return resultMap;
    }

    /**
     * 商户每日结算：查询指定商户结算日往前推360天内成交的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getPGStoreDailyRefundBeforeOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 查询SQL:结算月往前360内的退款成功订单的总金额，并且退款日期为结算日。
        String sql = "SELECT COUNT(*) AS dailyRefundOrderCount, IFNULL(SUM(o.amount), 0) AS dailyRefundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY) AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday AND o.amount >= CAST(50 AS DECIMAL)";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }

    /**
     * 查询指定商户指定年月日PG结算信息
     *
     * @param param
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[getSettleByMap]
     * 去掉注释和see参考方法完全相同
     */
    public SettleDTO getPGDailySettleByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" date_format(min(t3.pay_dts), '%Y-%m') as closing_months");
        sql.append(" , date_format(min(t3.pay_dts), '%Y-%m-%d') as pay_fr_dt");
        sql.append(" , date_format(max(t3.pay_dts), '%Y-%m-%d') as pay_to_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(t3.pay_dts), '%Y-%m-%d')) as pg_fr_dt");
        sql.append(" , (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(t3.pay_dts), '%Y-%m-%d')) as pg_to_dt");
        sql.append(" , sum(t2.amount) * 0.98 as pg_amount");
        sql.append(" , count(*) payCnt");
        sql.append(" , sum(t2.amount) as amount");
        sql.append(" , sum(t2.amount) * 0.02 as pg_fee");
        sql.append(" , round(sum(t2.amount) * 0.03) as service_fee");
        sql.append(" , ifnull((select a.status from    eorder.order_closing a where   a.store_id = t1.id \n" +
                "  and a.closing_months = date_format(min(t3.pay_dts), '%Y-%m') ), 0) as status");
        sql.append(" from    eorder.store t1");
        sql.append(" inner join eorder.`order` t2");
        sql.append(" on t1.id = t2.store_id");
        sql.append(" and t2.status in (4,5,6,8,9,10,11)");
        sql.append(" inner join eorder.order_pay t3");
        sql.append(" on t2.id = t3.order_id");
        sql.append(" and t3.pay_status = 1");
        sql.append(" where   1 = 1");

        getPGSettleCondition(sql, param);
        logger.info("【商户每日结算】【" + Objects.toString(param.get("storeId"), "STORE_ID") + "】【getPGDailySettleByMap】查询SQL为::" + sql.toString());

        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));
    }
    /**********************************************PG交易类商户每日结算-end**************************************************/


    /**********************************************BA交易类商户月度结算-start**************************************************/
    /**
     * 查询商户指定结算月有效交易订单总金额
     * 有效交易订单总金额=非退款订单总金额(完成状态订单，状态为9) + 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * <p>
     * 对
     * 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * 的解释：是指结算进行月，即结算进行月开始第一秒，到在该结算进行月进行的商户月度结算任务运行起始那一秒，这期间发生的结算月交易订单的退款
     *
     * @param storeId
     * @param monthStartTime 结算月起始时间
     * @param monthEndTime   结算月截止时间
     * @return
     */
    public Map<String, Object> getBAStoreEffectiveOrderAmountSum(Long storeId
            , String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询完成订单SQL：订单状态为9
        // 订单状态定义见com.basoft.eorder.domain.model.Order中。
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` = 9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";


        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.debug("【BA交易类商户月度结算】【商户{}】计算有效交易订单总金额>>>非退款完成状态的订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 查询非结算月退款订单SQL：订单状态为7，且退款日期非结算月内
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.debug("【BA交易类商户月度结算】【商户{}】计算有效交易订单总金额>>>结算月内成交的退款订单且不是在结算月退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 汇总计算-start
        Map resultMap = Maps.newHashMap();
        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        resultMap.put("orderCount", orderCount + refundOrderCount);

        BigDecimal orderTotalAmount = (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");
        resultMap.put("orderTotalAmount", orderTotalAmount.add(refundOrderTotalAmount));
//        log.debug("【BA交易类商户月度结算】【商户{}】有效交易订单总金额（非退款完成状态订单总金额 + 结算月内成交的退款订单且不是在结算月退款的订单总金额）和订单数量信息如下：{}", storeId, resultMap);
        // 汇总计算-end

        return resultMap;
    }

    /**
     * 查询指定商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内交易但退款的订单。
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public Map<String, Object> getBAStoreRefundBeforeOrderAmountSum(Long storeId, String monthStartTime,
                                                                    String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询SQL:结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
        String sql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL)";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }
    /**********************************************BA交易类商户月度结算-END**************************************************/
    /**********************************************BA交易类商户每日结算-start**************************************************/
    /**
     * BA交易类商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=完成订单 + 非昨日退款订单
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getBAStoreDailyEffectiveOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 查询结算日完成订单（完成日期在结算日，有可能结算日交易的，也有可能结算之前交易的）
        // String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status`=9 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status`=9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.info("【BA交易类商户每日结算】【{}】计算有效交易订单总金额>>>非退款订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 查询结算日交易但是非结算日退款的订单SQL：订单状态为7，且退款日期非结算日
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday AND o.amount >= CAST(50 AS DECIMAL)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.info("【BA交易类商户每日结算】【{}】计算有效交易订单总金额>>>结算日内成交的退款订单且不是在结算日退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 汇总计算-start
        Map resultMap = Maps.newHashMap();
        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        resultMap.put("dailyOrderCount", orderCount + refundOrderCount);

        BigDecimal orderTotalAmount = (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");
        resultMap.put("dailyOrderTotalAmount", orderTotalAmount.add(refundOrderTotalAmount));
//        log.info("【BA交易类商户每日结算】【{}】有效交易订单总金额（非退款订单总金额 + 结算日内成交的退款订单且不是在结算日退款的订单总金额）信息如下：{}", storeId, resultMap);
        // 汇总计算-end

        return resultMap;
    }

    /**
     * BA交易类商户每日结算：查询指定商户结算日往前360天的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getBAStoreDailyRefundBeforeOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 查询SQL:结算月往前360内的退款成功订单的总金额，并且退款日期为结算日。
        String sql = "SELECT COUNT(*) AS dailyRefundOrderCount, IFNULL(SUM(o.amount), 0) AS dailyRefundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY) AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday AND o.amount >= CAST(50 AS DECIMAL)";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }
    /**********************************************BA交易类商户每日结算-end**************************************************/


    /**********************************************BA交易类酒店商户月度结算-start**************************************************/
    /**
     * 查询酒店商户指定结算月有效交易订单总金额
     * 有效交易订单总金额=非退款订单总金额(完成状态订单，状态为9) + 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * <p>
     * 20200224正常订单和到手价订单分别处理！！！
     *
     * <p>
     * 对
     * 结算月内成交的退款订单且不是在结算月退款的订单总金额
     * 的解释：是指结算进行月，即结算进行月开始第一秒，到在该结算进行月进行的商户月度结算任务运行起始那一秒，这期间发生的结算月交易订单的退款
     *
     * @param storeId
     * @param monthStartTime 结算月起始时间
     * @param monthEndTime   结算月截止时间
     * @return
     */
    public Map<String, Object> getBAStoreEffectiveOrderAmountSum4Hotel(Long storeId
            , String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 1-1、正常订单（非到手价类订单）-查询完成订单SQL：订单状态为9
        // 订单状态定义见com.basoft.eorder.domain.model.Order中。 非到手价类订单o.AMOUNT_SETTLE为空或者小于0（值为-1）
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status` = 9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0)";
        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.debug("【BA交易类酒店商户月度结算】【商户{}】计算有效非到手价类交易订单总金额>>>非退款完成状态的订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 1-2、到手价类订单-查询完成订单SQL：订单状态为9
        // 订单状态定义见com.basoft.eorder.domain.model.Order中。 到手价类订单o.AMOUNT_SETTLE大于0
        String normalSqlDaoshou = "SELECT COUNT(*) AS orderCountDaoshou, IFNULL(SUM(o.amount), 0) AS orderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS orderTotalAmountSettle  FROM `order` o WHERE o.`status` = 9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> normalOrderInfoDaoshou = this.getNamedParameterJdbcTemplate().queryForMap(normalSqlDaoshou, param);
//        log.debug("【BA交易类酒店商户月度结算】【商户{}】计算有效到手价交易订单总金额>>>非退款完成状态的订单总金额信息如下：{}", storeId, normalOrderInfoDaoshou);

        // 2-1、正常订单（非到手价类订单）-查询非结算月退款订单SQL：订单状态为7，且退款日期非结算月内
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.debug("【BA交易类酒店商户月度结算】【商户{}】计算有效非到手价交易订单总金额>>>结算月内成交的退款订单且不是在结算月退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 2-2、到手价类订单-查询非结算月退款订单SQL：订单状态为7，且退款日期非结算月内
        String refundSqlDaoshou = "SELECT COUNT(*) AS refundOrderCountDaoshou, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS refundOrderTotalAmountSettle FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> refundOrderInfoDaoshou = this.getNamedParameterJdbcTemplate().queryForMap(refundSqlDaoshou, param);
//        log.debug("【BA交易类酒店商户月度结算】【商户{}】计算有效到手价交易订单总金额>>>结算月内成交的退款订单且不是在结算月退款的订单总金额信息如下：{}", storeId, refundOrderInfoDaoshou);

        // 3、汇总计算-start
        Map resultMap = Maps.newHashMap();

        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long orderCountDaoshou = normalOrderInfoDaoshou.get("orderCountDaoshou") == null ? 0L : (Long) normalOrderInfoDaoshou.get("orderCountDaoshou");

        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        Long refundOrderCountDaoshou = refundOrderInfoDaoshou.get("refundOrderCountDaoshou") == null ? 0L : (Long) refundOrderInfoDaoshou.get("refundOrderCountDaoshou");

        // 3-1-1、有效订单总数量(正常订单和到手价订单)
        // resultMap.put("orderCount", orderCount + orderCountDaoshou + refundOrderCount + refundOrderCountDaoshou);

        // 3-1-2、正常订单数量-状态9和状态7
        resultMap.put("orderCountNormal", orderCount + refundOrderCount);

        // 3-1-3、到手价订单数量-状态9和状态7
        resultMap.put("orderCountDaoshou", orderCountDaoshou + refundOrderCountDaoshou);

        // 3-2-1、订单金额-正常订单（状态9和非结算月退款订单状态7）
        BigDecimal orderTotalAmount = normalOrderInfo == null || normalOrderInfo.get("orderTotalAmount") == null ? BigDecimal.ZERO :
                (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = refundOrderInfo == null || refundOrderInfo.get("refundOrderTotalAmount") == null ? BigDecimal.ZERO :
                (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");

        // 3-2-2、订单金额-到手价订单（状态9和非结算月退款订单状态7）-平台总金额
        BigDecimal orderTotalAmountDaoshou = normalOrderInfoDaoshou == null || normalOrderInfoDaoshou.get("orderTotalAmountDaoshou") == null ? BigDecimal.ZERO :
                (BigDecimal) normalOrderInfoDaoshou.get("orderTotalAmountDaoshou");
        BigDecimal refundOrderTotalAmountDaoshou = refundOrderInfoDaoshou == null || refundOrderInfoDaoshou.get("refundOrderTotalAmountDaoshou") == null ? BigDecimal.ZERO :
                (BigDecimal) refundOrderInfoDaoshou.get("refundOrderTotalAmountDaoshou");

        //3-3-3、订单金额-到手价订单（状态9和非结算月退款订单状态7）-到手总金额
        BigDecimal orderTotalAmountSettle = normalOrderInfoDaoshou == null || normalOrderInfoDaoshou.get("orderTotalAmountSettle") == null ? BigDecimal.ZERO :
                (BigDecimal) normalOrderInfoDaoshou.get("orderTotalAmountSettle");
        BigDecimal refundOrderTotalAmountSettle = refundOrderInfoDaoshou == null || refundOrderInfoDaoshou.get("refundOrderTotalAmountSettle") == null ? BigDecimal.ZERO :
                (BigDecimal) refundOrderInfoDaoshou.get("refundOrderTotalAmountSettle");

        // 订单金额-正常订单总金额（状态9和非结算月退款订单状态7）
        resultMap.put("orderTotalAmountNormal", orderTotalAmount.add(refundOrderTotalAmount));
        // 订单金额-到手价订单-平台总金额（状态9和非结算月退款订单状态7）
        resultMap.put("orderTotalAmountDaoshou", orderTotalAmountDaoshou.add(refundOrderTotalAmountDaoshou));
        // 订单金额-到手价订单-到手总金额（状态9和非结算月退款订单状态7）
        resultMap.put("orderTotalAmountDaoshouSettle", orderTotalAmountSettle.add(refundOrderTotalAmountSettle));

//        log.debug("【BA交易类酒店商户月度结算】【酒店商户{}】有效交易订单总金额（非退款完成状态订单总金额 + 结算月内成交的退款订单且不是在结算月退款的订单总金额）和订单数量信息如下：{}", storeId, resultMap);
        // 3、汇总计算-end

        return resultMap;
    }

    /**
     * 查询指定酒店商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内交易但退款的订单。
     * <p>
     * 20200224正常订单和到手价订单分别处理！！！
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public Map<String, Object> getBAStoreRefundBeforeOrderAmountSum4Hotel(Long storeId, String monthStartTime,
                                                                          String monthEndTime, String refundYearMonth) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);

        // 查询SQL:结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。-正常订单-订单没有记录即时费率
        String sqlNoraml = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0) AND (o.RATE_SETTLE is null OR o.RATE_SETTLE < 0)";
        Map<String, Object> refundOrderNormalInfo = this.getNamedParameterJdbcTemplate().queryForMap(sqlNoraml, param);

        // 查询SQL:结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。-正常订单-订单记录了即时费率
        String sqlNoramlWithRate = "SELECT COUNT(*) AS refundOrderCountWithRate, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmountWithRate, IFNULL(SUM(o.amount*o.RATE_SETTLE/100), 0) AS refundOrderServiceFeeWithRate FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0) AND (o.RATE_SETTLE > 0)";
        Map<String, Object> refundOrderNormalInfoWithRate = this.getNamedParameterJdbcTemplate().queryForMap(sqlNoramlWithRate, param);

        // 查询SQL:结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。-到手价订单
        String sqlDaoshou = "SELECT COUNT(*) AS refundOrderCountDaoshou, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS refundOrderTotalAmountSettle FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH) AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> refundOrderDaoshouInfo = this.getNamedParameterJdbcTemplate().queryForMap(sqlDaoshou, param);

        // 汇总计算
        Map resultMap = Maps.newHashMap();

        // 退款正常订单数量
        Long refundOrderCount = (Long) refundOrderNormalInfo.get("refundOrderCount");
        Long refundOrderCountWithRate = (Long) refundOrderNormalInfoWithRate.get("refundOrderCountWithRate");
        // 退款到手价订单数量
        Long refundOrderCountDaoshou = (Long) refundOrderDaoshouInfo.get("refundOrderCountDaoshou");
        resultMap.put("refundOrderCountNormal", refundOrderCount);
        resultMap.put("refundOrderCountWithRateNormal", refundOrderCountWithRate);

        resultMap.put("refundOrderCountDaoshou", refundOrderCountDaoshou);


        // 退款正常订单总金额
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderNormalInfo.get("refundOrderTotalAmount");
        BigDecimal refundOrderTotalAmountWithRate = (BigDecimal) refundOrderNormalInfoWithRate.get("refundOrderTotalAmountWithRate");
        BigDecimal refundOrderServiceFeeWithRate = (BigDecimal) refundOrderNormalInfoWithRate.get("refundOrderServiceFeeWithRate");
        // 退款到手价订单平台总金额
        BigDecimal refundOrderTotalAmountDaoshou = (BigDecimal) refundOrderDaoshouInfo.get("refundOrderTotalAmountDaoshou");
        // 退款到手价订单到手总金额
        BigDecimal refundOrderTotalAmountSettle = (BigDecimal) refundOrderDaoshouInfo.get("refundOrderTotalAmountSettle");
        resultMap.put("refundOrderTotalAmountNormal", refundOrderTotalAmount);
        resultMap.put("refundOrderTotalAmountWithRateNormal", refundOrderTotalAmountWithRate);
        resultMap.put("refundOrderServiceFeeWithRateNormal", refundOrderServiceFeeWithRate);

        resultMap.put("refundOrderTotalAmountDaoshou", refundOrderTotalAmountDaoshou);
        resultMap.put("refundOrderTotalAmountDaoshouSettle", refundOrderTotalAmountSettle);

        return resultMap;
    }
    /**********************************************BA交易类酒店商户月度结算-end**************************************************/


    /**********************************************BA交易类酒店商户每日结算-start**************************************************/
    /**
     * BA交易类酒店商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=完成订单 + 非昨日退款订单
     * <p>
     * 20200224正常订单和到手价订单分别处理！！！
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getBAStoreDailyEffectiveOrderAmountSum4Hotel(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 1-1、查询结算日完成订单（完成日期在结算日，有可能结算日交易的，也有可能结算之前交易的）-正常订单
        String normalSql = "SELECT COUNT(*) AS orderCount, IFNULL(SUM(o.amount), 0) AS orderTotalAmount FROM `order` o WHERE o.`status`=9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0)";
        Map<String, Object> normalOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(normalSql, param);
//        log.info("【BA交易类酒店商户每日结算】【{}】计算有效交易订单总金额>>>非退款正常订单总金额信息如下：{}", storeId, normalOrderInfo);

        // 1-2、查询结算日完成订单（完成日期在结算日，有可能结算日交易的，也有可能结算之前交易的）-到手价订单
        String normalSqlDaoshou = "SELECT COUNT(*) AS orderCountDaoshou, IFNULL(SUM(o.amount), 0) AS orderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS orderTotalAmountSettle FROM `order` o WHERE o.`status`=9 AND o.store_id=:storeId AND o.updated >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.updated <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> normalOrderInfoDaoshou = this.getNamedParameterJdbcTemplate().queryForMap(normalSqlDaoshou, param);
//        log.info("【BA交易类酒店商户每日结算】【{}】计算有效交易订单总金额>>>非退款到手价订单总金额信息如下：{}", storeId, normalOrderInfoDaoshou);

        // 2-1、查询结算日交易但是非结算日退款的订单SQL：订单状态为7，且退款日期非结算日-正常订单
        String refundSql = "SELECT COUNT(*) AS refundOrderCount, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0)";
        Map<String, Object> refundOrderInfo = this.getNamedParameterJdbcTemplate().queryForMap(refundSql, param);
//        log.info("【BA交易类酒店商户每日结算】【{}】计算有效交易订单总金额>>>结算日内成交的退款正常订单且不是在结算日退款的订单总金额信息如下：{}", storeId, refundOrderInfo);

        // 2-2、查询结算日交易但是非结算日退款的订单SQL：订单状态为7，且退款日期非结算日-到手价订单
        String refundSqlDaoshou = "SELECT COUNT(*) AS refundOrderCountDaoshou, IFNULL(SUM(o.amount), 0) AS refundOrderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS refundOrderTotalAmountSettle FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s') AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> refundOrderInfoDaoshou = this.getNamedParameterJdbcTemplate().queryForMap(refundSqlDaoshou, param);
//        log.info("【BA交易类酒店商户每日结算】【{}】计算有效交易订单总金额>>>结算日内成交的退款到手价订单且不是在结算日退款的订单总金额信息如下：{}", storeId, refundOrderInfoDaoshou);

        // 3、汇总计算-start
        Map resultMap = Maps.newHashMap();
        Long orderCount = normalOrderInfo.get("orderCount") == null ? 0L : (Long) normalOrderInfo.get("orderCount");
        Long orderCountDaoshou = normalOrderInfoDaoshou.get("orderCountDaoshou") == null ? 0L : (Long) normalOrderInfoDaoshou.get("orderCountDaoshou");

        Long refundOrderCount = refundOrderInfo.get("refundOrderCount") == null ? 0L : (Long) refundOrderInfo.get("refundOrderCount");
        Long refundOrderCountDaoshou = refundOrderInfoDaoshou.get("refundOrderCountDaoshou") == null ? 0L : (Long) refundOrderInfoDaoshou.get("refundOrderCountDaoshou");

        // 3-1-1、正常订单数量-状态9和状态7
        resultMap.put("dailyOrderCountNormal", orderCount + refundOrderCount);
        // 3-1-2、到手价订单数量-状态9和状态7
        resultMap.put("dailyOrderCountDaoshou", orderCountDaoshou + refundOrderCountDaoshou);


        // 3-2-1、订单金额-正常订单（状态9和非结算月退款订单状态7）
        BigDecimal orderTotalAmount = (BigDecimal) normalOrderInfo.get("orderTotalAmount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) refundOrderInfo.get("refundOrderTotalAmount");

        // 3-2-2、订单金额-到手价订单（状态9和非结算月退款订单状态7）-平台总金额
        BigDecimal orderTotalAmountDaoshou = (BigDecimal) normalOrderInfoDaoshou.get("orderTotalAmountDaoshou");
        BigDecimal refundOrderTotalAmountDaoshou = (BigDecimal) refundOrderInfoDaoshou.get("refundOrderTotalAmountDaoshou");

        //3-3-3、订单金额-到手价订单（状态9和非结算月退款订单状态7）-到手总金额
        BigDecimal orderTotalAmountSettle = (BigDecimal) normalOrderInfoDaoshou.get("orderTotalAmountSettle");
        BigDecimal refundOrderTotalAmountSettle = (BigDecimal) refundOrderInfoDaoshou.get("refundOrderTotalAmountSettle");

        // 订单金额-正常订单总金额（状态9和非结算月退款订单状态7）
        resultMap.put("dailyOrderTotalAmountNormal", orderTotalAmount.add(refundOrderTotalAmount));
        // 订单金额-到手价订单-平台总金额（状态9和非结算月退款订单状态7）
        resultMap.put("dailyOrderTotalAmountDaoshou", orderTotalAmountDaoshou.add(refundOrderTotalAmountDaoshou));
        // 订单金额-到手价订单-到手总金额（状态9和非结算月退款订单状态7）
        resultMap.put("dailyOrderTotalAmountDaoshouSettle", orderTotalAmountSettle.add(refundOrderTotalAmountSettle));
//        log.info("【BA交易类酒店商户每日结算】【{}】有效交易订单总金额（非退款订单总金额 + 结算日内成交的退款订单且不是在结算日退款的订单总金额）信息如下：{}", storeId, resultMap);
        // 汇总计算-end

        return resultMap;
    }

    /**
     * BA交易类酒店商户每日结算：查询指定商户结算日往前360天的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public Map<String, Object> getBAStoreDailyRefundBeforeOrderAmountSum4Hotel(Long storeId, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);

        // 查询SQL:结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。-正常订单-订单没有记录即时费率
        String sqlNoraml = "SELECT COUNT(*) AS dailyRefundOrderCount, IFNULL(SUM(o.amount), 0) AS dailyRefundOrderTotalAmount FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY) AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0) AND (o.RATE_SETTLE is null OR o.RATE_SETTLE < 0)";
        Map<String, Object> refundOrderNormalInfo = this.getNamedParameterJdbcTemplate().queryForMap(sqlNoraml, param);

        // 查询SQL:结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。-正常订单-订单记录了即时费率
        String sqlNoramlWithRate = "SELECT COUNT(*) AS dailyRefundOrderCountWithRate, IFNULL(SUM(o.amount), 0) AS dailyRefundOrderTotalAmountWithRate,IFNULL(SUM(o.amount*o.RATE_SETTLE/100), 0) AS dailyRefundOrderServiceFeeWithRate FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY) AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday AND o.amount >= CAST(50 AS DECIMAL) AND (o.AMOUNT_SETTLE is NULL OR o.AMOUNT_SETTLE < 0) AND o.RATE_SETTLE > 0";
        Map<String, Object> refundOrderNormalInfoWithRate = this.getNamedParameterJdbcTemplate().queryForMap(sqlNoramlWithRate, param);

        // 查询SQL:结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。-到手价订单
        String sqlDaoshou = "SELECT COUNT(*) AS dailyRefundOrderCountDaoshou, IFNULL(SUM(o.amount), 0) AS dailyRefundOrderTotalAmountDaoshou, IFNULL(SUM(o.AMOUNT_SETTLE), 0) AS dailyRefundOrderTotalAmountSettle FROM `order` o WHERE o.`status`=7 AND o.store_id=:storeId AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY) AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY) AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday AND o.amount >= CAST(50 AS DECIMAL) AND o.AMOUNT_SETTLE > 0";
        Map<String, Object> refundOrderDaoshouInfo = this.getNamedParameterJdbcTemplate().queryForMap(sqlDaoshou, param);


        // 汇总计算
        Map resultMap = Maps.newHashMap();
        // 退款正常订单数量
        Long dailyRefundOrderCount = (Long) refundOrderNormalInfo.get("dailyRefundOrderCount");
        Long dailyRefundOrderCountWithRate = (Long) refundOrderNormalInfoWithRate.get("dailyRefundOrderCountWithRate");
        // 退款到手价订单数量
        Long dailyRefundOrderCountDaoshou = (Long) refundOrderDaoshouInfo.get("dailyRefundOrderCountDaoshou");
        resultMap.put("dailyRefundOrderCountNormal", dailyRefundOrderCount);
        resultMap.put("dailyRefundOrderCountWithRateNormal", dailyRefundOrderCountWithRate);

        resultMap.put("dailyRefundOrderCountDaoshou", dailyRefundOrderCountDaoshou);


        // 退款正常订单总金额
        BigDecimal dailyRefundOrderTotalAmount = (BigDecimal) refundOrderNormalInfo.get("dailyRefundOrderTotalAmount");
        BigDecimal dailyRefundOrderTotalAmountWithRate = (BigDecimal) refundOrderNormalInfoWithRate.get("dailyRefundOrderTotalAmountWithRate");
        BigDecimal dailyRefundOrderServiceFeeWithRate = (BigDecimal) refundOrderNormalInfoWithRate.get("dailyRefundOrderServiceFeeWithRate");
        // 退款到手价订单平台总金额
        BigDecimal dailyRefundOrderTotalAmountDaoshou = (BigDecimal) refundOrderDaoshouInfo.get("dailyRefundOrderTotalAmountDaoshou");
        // 退款到手价订单到手总金额
        BigDecimal dailyRefundOrderTotalAmountSettle = (BigDecimal) refundOrderDaoshouInfo.get("dailyRefundOrderTotalAmountSettle");
        resultMap.put("dailyRefundOrderTotalAmountNormal", dailyRefundOrderTotalAmount);
        resultMap.put("dailyRefundOrderTotalAmountWithRateNormal", dailyRefundOrderTotalAmountWithRate);
        resultMap.put("dailyRefundOrderServiceFeeWithRateNormal", dailyRefundOrderServiceFeeWithRate);

        resultMap.put("dailyRefundOrderTotalAmountDaoshou", dailyRefundOrderTotalAmountDaoshou);
        resultMap.put("dailyRefundOrderTotalAmountDaoshouSettle", dailyRefundOrderTotalAmountSettle);

        return resultMap;
    }
    /**********************************************BA交易类酒店商户每日结算-end**************************************************/


    /**********************************************代理商月度结算-start**************************************************/
    /**
     * SA代理商：查询代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    /*
        SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund
        FROM `order` o
        WHERE o.`status` IN (4,5,6,7,8,9,10,11)
        AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 733756776677970944)
        AND o.created >= STR_TO_DATE('2019-09-01 00:00:00', '%Y-%m-%d %H:%i:%s')
        AND o.created <= STR_TO_DATE('2019-09-30 23:59:59', '%Y-%m-%d %H:%i:%s')
        AND o.cancel_dt IS NOT NULL
        AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != '2019-09'
     */
    @Deprecated
    public List<AgentSettlementOrder> querySagentOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        // 完整版SQL
        /*querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` IN (4,5,6,8,9,10,11)")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" UNION ALL")
                .append(" SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate")
                .append(" FROM `order` o")
                .append(" WHERE o.`status`=7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth");*/
        // 优化版SQL-错误的
        /*querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth");*/

        // 优化版SQL-正确的
        querySQL.append(" FROM `order` o ")
                // 该条件可有可无了
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(4,5,6,8,9,10,11)，并且订单时间在结算区间
                .append(" (o.`status` IN (4,5,6,8,9,10,11) AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算月
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth)")
                .append(" )");
//        log.info("【CA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
//        log.info("【SA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /*
    20200107完成订单代理商结算重算-版本
    基于方法querySagentOrderList改造
    */

    /**
     * SA代理商：查询代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public List<AgentSettlementOrder> querySagentOrderList1(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(9)，并且完成订单时间在结算区间
                .append(" (o.`status` = 9 AND o.updated >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.updated <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算月
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth)")
                .append(" )");
//        log.info("【SA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /**
     * SA代理商：查询出结算月内退款但交易日期在之前12个月内的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    /*
        SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate,
        o.status as orderStatus, o.cancel_dt as cancelDt, 1 as isRefund
        FROM `order` o
        WHERE o.`status` = 7
        AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 733756776677970944)
        AND o.created >= DATE_SUB(STR_TO_DATE('2019-09-01 00:00:00', '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH)
        AND o.created <= DATE_SUB(STR_TO_DATE('2019-09-30 23:59:59', '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH)
        AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = '2019-09'
     */
    public List<AgentSettlementOrder> querySagentRefundBeforeOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 1 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH)")
                .append(" AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH)")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth");
//        log.info("【SA代理商月度结算】查询结算月退款的之前11个月的订单SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /**
     * SA代理商：查询出结算月交易并且结算月退款的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public List<AgentSettlementOrder> querySagentRefundOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth");
//        log.info("【SA代理商月度结算】结算月交易结算月退款的订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
    }

    /**
     * SA代理商：查询代理商所有结算订单
     * 1、查询代理商结算月内的有效订单
     * 2、查询出结算月内退款但交易日期在之前12个月内的订单
     * <p>
     * 该方法合并queryAgentOrderList和queryAgentRefundBeforeOrderList两个方法，即合并查询SQL，充分利用数据库服务器的运算能力
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    @Deprecated
    public List<AgentSettlementOrder> queryAgentAllOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt");
        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth")
                // 查询出结算月内退款但交易日期在之前12个月内的订单
                .append(" UNION ALL")
                .append(" SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt")
                .append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH)")
                .append(" AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH)")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth");
//        log.debug("【SA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
    }


    /**
     * CA代理商：查询代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    /*
        SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount,
        o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 0 AS isRefund
        FROM `order` o
        WHERE o.`status` IN (4,5,6,7,8,9,10,11)
        AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
        AND
        (
        (o.`status` IN (4,5,6,8,9,10,11) AND o.created >= STR_TO_DATE('2019-09-01 00:00:00', '%Y-%m-%d %H:%i:%s')
        AND o.created <= STR_TO_DATE('2019-09-30 23:59:59', '%Y-%m-%d %H:%i:%s'))
        OR
        (o.`status` = 7 AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != '2019-09')
        )
     */
    @Deprecated
    public List<AgentSettlementOrder> queryCagentOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        // 查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
        // 错误的
        /*StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund");
        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth");*/
        // 正确的
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                // 该条件可有可无了
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(4,5,6,8,9,10,11)，并且订单时间在结算区间
                .append(" (o.`status` IN (4,5,6,8,9,10,11) AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算月
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth)")
                .append(" )");
//        log.info("【CA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /*
    20200107完成订单代理商结算重算-版本
    基于方法queryCagentOrderList改造
    */

    /**
     * CA代理商：查询CA代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[queryAgentAllOrderList]
     */
    public List<AgentSettlementOrder> queryCagentOrderList1(Agent agent, String monthStartTime,
                                                            String monthEndTime, String refundYearMonth) {
        // 查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(9)，并且订单完成时间在结算区间
                .append(" (o.`status` = 9 AND o.updated >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.updated <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算月
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') != :refundYearMonth)")
                .append(" )");
//        log.info("【CA代理商月度结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }


    /**
     * CA代理商：查询出结算月内退款但交易日期在之前12个月内的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    // 示例SQL
    /*
        SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount,
        o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 1 AS isRefund
        FROM `order` o
        WHERE o.`status` = 7
        AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
        AND o.created >= DATE_SUB(STR_TO_DATE('2019-09-01 00:00:00', '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH)
        AND o.created <= DATE_SUB(STR_TO_DATE('2019-09-30 23:59:59', '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH)
        AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = '2019-09'
     */
    public List<AgentSettlementOrder> queryCagentRefundBeforeOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 1 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= DATE_SUB(STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 MONTH)")
                .append(" AND o.created <= DATE_SUB(STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 MONTH)")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth");
//        log.info("【CA代理商月度结算】查询结算月退款的之前11个月的订单SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /**
     * CA代理商：查询出结算月交易并且结算月退款的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    // 示例SQL
    /*
        SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId,
        o.amount AS orderAmount, o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 0 AS isRefund
        FROM `order` o
        WHERE o.`status` = 7 AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
        AND o.created >= STR_TO_DATE('2019-09-01 00:00:00', '%Y-%m-%d %H:%i:%s')
        AND o.created <= STR_TO_DATE('2019-09-30 23:59:59', '%Y-%m-%d %H:%i:%s')
        AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = '2019-09'
     */
    public List<AgentSettlementOrder> queryCagentRefundOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户的结算费率表查询：类型1和2，状态useYn为2或3并且finalYn为1，且年月为结算月（取值refundYearMonth即可）
        querySQL.append(" IFNULL((SELECT IFNULL(scir.CHARGE_RATE,0) FROM store_charge_info_record scir")
                .append(" WHERE scir.STORE_ID=o.store_id AND scir.USE_YN IN (2,3) AND scir.FINAL_YN=1")
                .append(" AND scir.CHARGE_TYPE IN (1,2) AND scir.CHARGE_YEAR_MONTH=REPLACE(:refundYearMonth,'-','')),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:monthStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:monthEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m') = :refundYearMonth");
//        log.info("【CA代理商月度结算】结算月交易结算月退款的订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("monthStartTime", monthStartTime);
        param.put("monthEndTime", monthEndTime);
        param.put("refundYearMonth", refundYearMonth);
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
    }
    /**********************************************代理商月度结算-end**************************************************/


    /**********************************************代理商每日结算-start**************************************************/
    /**
     * SA代理商：查询代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status AS orderStatus,
    o.cancel_dt AS cancelDt, 0 AS isRefund,
    IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate

    FROM `order` o
    WHERE o.`status` IN (4,5,6,7,8,9,10,11)
    AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730814577153033)
    AND
    (
    (o.`status` IN (4,5,6,8,9,10,11)
    AND o.created >= STR_TO_DATE('2019-11-06 00:00:00', '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE('2019-11-06 23:59:59', '%Y-%m-%d %H:%i:%s'))
    OR
    (o.`status` = 7
    AND o.created >= STR_TO_DATE('2019-11-06 00:00:00', '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE('2019-11-06 23:59:59', '%Y-%m-%d %H:%i:%s')
    AND o.cancel_dt IS NOT NULL
    AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != '2019-11-06')
    );
     */
    @Deprecated
    public List<AgentSettlementOrder> querySagentDailyOrderList(Agent agent, String dayStartTime,
                                                                String dayEndTime, String yesterday) {
        // 查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        // 优化版SQL-正确的
        querySQL.append(" FROM `order` o ")
                // 该条件可有可无了
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(4,5,6,8,9,10,11)，并且订单时间在结算区间
                .append(" (o.`status` IN (4,5,6,8,9,10,11) AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算日
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday)")
                .append(")");
//        log.info("【CA代理商每日结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /*
    20200107完成订单代理商结算重算-版本
    基于方法querySagentDailyOrderList改造
    */

    /**
     * SA代理商：查询代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public List<AgentSettlementOrder> querySagentDailyOrderList1(Agent agent, String dayStartTime,
                                                                 String dayEndTime, String yesterday) {
        // 查询出结算日有效交易订单（有效交易订单=完成订单 + 结算日交易但非结算日退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");
        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(9)，并且订单时间在结算区间
                .append(" (o.`status` = 9 AND o.updated >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.updated <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算日
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday)")
                .append(")");
//        log.info("【CA代理商每日结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }


    /**
     * SA代理商：查询出结算日内退款但交易日期在之前360天内的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate,
    o.status AS orderStatus, o.cancel_dt AS cancelDt, 1 AS isRefund,
    IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate
    FROM `order` o
    WHERE o.`status` = 7
    AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730814577153033)
    AND o.created >= DATE_SUB(STR_TO_DATE('2019-11-06 00:00:00', '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY)
    AND o.created <= DATE_SUB(STR_TO_DATE('2019-11-06 23:59:59', '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY)
    AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = '2019-11-06';
     */
    public List<AgentSettlementOrder> querySagentDailyRefundBeforeOrderList(Agent agent, String dayStartTime,
                                                                            String dayEndTime, String yesterday) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 1 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY)")
                .append(" AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY)")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday");
//        log.info("【SA代理商每日结算】查询结算日退款的之前360天的订单SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /**
     * SA代理商：查询出结算日交易并且结算日退款的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate,
    o.status AS orderStatus, o.cancel_dt AS cancelDt, 0 AS isRefund,
    IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate
    FROM `order` o
    WHERE o.`status` = 7
    AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730814577153033)
    AND o.created >= STR_TO_DATE('2019-11-06 00:00:00', '%Y-%m-%d %H:%i:%s')
    AND o.created <= STR_TO_DATE('2019-11-06 23:59:59', '%Y-%m-%d %H:%i:%s')
    AND o.cancel_dt IS NOT NULL
    AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = '2019-11-06';
     */
    public List<AgentSettlementOrder> querySagentDailyRefundOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.store_id IN (SELECT a.STORE_ID FROM agent_store_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday");
//        log.info("【SA代理商月度结算】结算月交易结算月退款的订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
    }

    /**
     * CA代理商：查询CA代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount,
    o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 0 AS isRefund,
    IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate
    FROM `order` o
    WHERE o.`status` IN (4,5,6,7,8,9,10,11)
    AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
    AND
    (
    (o.`status` IN (4,5,6,8,9,10,11)
    AND o.created >= STR_TO_DATE('2019-11-07 00:00:00', '%Y-%m-%d %H:%i:%s')
    AND o.created <= STR_TO_DATE('2019-11-07 23:59:59', '%Y-%m-%d %H:%i:%s'))
    OR
    (o.`status` = 7
    AND o.created >= STR_TO_DATE('2019-11-07 00:00:00', '%Y-%m-%d %H:%i:%s')
    AND o.created <= STR_TO_DATE('2019-11-07 23:59:59', '%Y-%m-%d %H:%i:%s')
    AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != '2019-11-07')
    );
     */
    @Deprecated
    public List<AgentSettlementOrder> queryCagentDailyOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday) {
        // 查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                // 该条件可有可无了
                .append(" WHERE o.`status` IN (4,5,6,7,8,9,10,11)")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(4,5,6,8,9,10,11)，并且订单时间在结算区间
                .append(" (o.`status` IN (4,5,6,8,9,10,11) AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算日
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday)")
                .append(")");
//        log.info("【CA代理商每日结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /*
    20200107完成订单代理商结算重算-版本
    基于方法queryCagentDailyOrderList改造
    */

    /**
     * CA代理商：查询CA代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    public List<AgentSettlementOrder> queryCagentDailyOrderList1(Agent agent, String dayStartTime,
                                                                 String dayEndTime, String yesterday) {
        // 查询出结算日有效交易订单（有效交易订单=完成订单 + 结算日交易但非结算日退款订单）
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND")
                .append(" (")
                // 条件一状态为(9)，并且订单时间在结算区间
                .append(" (o.`status` = 9 AND o.updated >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') ")
                .append(" AND o.updated <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'))")
                .append(" OR")
                // 条件二状态为(7)，并且退款时间不在结算日
                .append(" (o.`status` = 7")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') != :yesterday)")
                .append(")");
//        log.info("【CA代理商每日结算】有效订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }


    /**
     * CA代理商：查询出结算日内退款但交易日期在之前360天内的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount,
    o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 1 AS isRefund,
    IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate
    FROM `order` o
    WHERE o.`status` = 7
    AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
    AND o.created >= DATE_SUB(STR_TO_DATE('2019-11-07 00:00:00', '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY)
    AND o.created <= DATE_SUB(STR_TO_DATE('2019-11-07 23:59:59', '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY)
    AND o.cancel_dt IS NOT NULL
    AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = '2019-11-07';
     */
    public List<AgentSettlementOrder> queryCagentDailyRefundBeforeOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 1 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= DATE_SUB(STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 360 DAY)")
                .append(" AND o.created <= DATE_SUB(STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s'), INTERVAL 1 DAY)")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday");
//        log.info("【CA代理商每日结算】查询结算日退款的之前360天的订单SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        List<AgentSettlementOrder> agentSettlementOrderList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
        return agentSettlementOrderList;
    }

    /**
     * CA代理商：查询出结算日交易并且结算日退款的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    /*
    SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount,
     o.created AS orderDate, o.status AS orderStatus, o.cancel_dt AS cancelDt, 0 AS isRefund,
     IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate
    FROM `order` o
    WHERE o.`status` = 7
    AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = 725730898496787463)
    AND o.created >= STR_TO_DATE('2019-11-07 00:00:00', '%Y-%m-%d %H:%i:%s')
    AND o.created <= STR_TO_DATE('2019-11-07 23:59:59', '%Y-%m-%d %H:%i:%s')
    AND o.cancel_dt IS NOT NULL
    AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = '2019-11-07';
    */
    public List<AgentSettlementOrder> queryCagentDailyRefundOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday) {
        StringBuilder querySQL = new StringBuilder("SELECT o.id AS orderId, o.store_id AS storeId, o.open_id AS openId, o.amount AS orderAmount, o.created AS orderDate, o.status as orderStatus, o.cancel_dt as cancelDt, 0 as isRefund,");

        // 查询订单对应商户的平台结算费率storePlatRate，用于计算代理商的默认结算费用
        // 从商户主表store查询。因为当前月的结算费率信息查询store主表即可。
        querySQL.append(" IFNULL((SELECT IFNULL(s.CHARGE_RATE,0) FROM store s WHERE s.id=o.store_id),0)/100 AS storePlatRate");

        querySQL.append(" FROM `order` o ")
                .append(" WHERE o.`status` = 7")
                .append(" AND o.open_id IN (SELECT a.OPEN_ID FROM agent_wx_map a WHERE a.`STATUS` = 1 AND a.AGT_ID = :agtId)")
                .append(" AND o.created >= STR_TO_DATE(:dayStartTime, '%Y-%m-%d %H:%i:%s') AND o.created <= STR_TO_DATE(:dayEndTime, '%Y-%m-%d %H:%i:%s')")
                .append(" AND o.cancel_dt IS NOT NULL")
                .append(" AND DATE_FORMAT(o.cancel_dt,'%Y-%m-%d') = :yesterday");
//        log.info("【CA代理商每日结算】结算日交易结算日退款的订单查询SQL>>>>>>{}", querySQL.toString());
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        param.put("dayStartTime", dayStartTime);
        param.put("dayEndTime", dayEndTime);
        param.put("yesterday", yesterday);
        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementOrder.class));
    }
    /**********************************************代理商每日结算-end**************************************************/
}

package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.OrderPay;
import com.basoft.eorder.domain.model.OrderPayCancel;
import com.basoft.eorder.domain.model.order.StoreDailySettlement;
import com.basoft.eorder.domain.model.order.StoreDailySettlementBA;
import com.basoft.eorder.domain.model.order.StoreSettlement;
import com.basoft.eorder.domain.model.order.StoreSettlementBA;
import com.basoft.eorder.interfaces.command.AcceptOrder;
import com.basoft.eorder.interfaces.command.ConfirmOrder;
import com.basoft.eorder.interfaces.command.SaveOrder;
import com.basoft.eorder.interfaces.command.UpSettleStatus;
import com.basoft.eorder.interfaces.query.order.HotelOrderDTO;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Transactional
public class JdbcOrderRepoImpl extends BaseRepository implements OrderRepository {
    @Autowired
    private UidGenerator uidGenerator;

    /*************************************Wechat H5用户下单全过程-START*******************************************/
    /**
     * 1、保存订单信息到订单临时表(非酒店业务)
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public Order saveOrderTemp(Order order) {
        // insert order to temp
        insertOrderTemp(order);

        // insert order info to temp
        insertOrderInfoTemp(order);

        // insert order items to temp
        List<OrderItem> itemList = order.getItemList();
        if (itemList != null && itemList.size() > 0) {
            Long finalOrderId = order.getId();
            List<OrderItem> newItemList = itemList.stream().map(item -> {
                OrderItem orderItem = item.createNewOrderItem(uidGenerator.generate(BusinessTypeEnum.ORDER_ITEM), finalOrderId, item).build();
                return orderItem;
            }).collect(Collectors.toList());
            insertOrderItemTemp(newItemList);
        }

        return order;
    }

    /**
     * 1、酒店保存订单信息到订单临时表,同时扣减酒店库存
     *
     * @param order
     * @return
     * @see public Order saveOrderTemp(Order order)
     */
    @Transactional
    public Order saveOrderTempWithInventory(Order order) {
        // insert order to temp
        insertOrderTemp(order);

        // insert order info to temp
        insertOrderInfoTemp(order);

        // insert order items to temp
        List<OrderItem> itemList = order.getItemList();
        if (itemList != null && itemList.size() > 0) {
            Long finalOrderId = order.getId();
            List<OrderItem> newItemList = itemList.stream().map(item -> {
                OrderItem orderItem = item.createNewOrderItem(uidGenerator.generate(BusinessTypeEnum.ORDER_ITEM), finalOrderId, item).build();
                return orderItem;
            }).collect(Collectors.toList());
            insertOrderItemTemp(newItemList);
        }

        // 处理库存
        Long prodSkuId = order.getItemList().get(0).getSkuId();
        reduceHotelInventory(order.getStoreId(), prodSkuId, order.getReseveDtfrom(), order.getReseveDtto());

        return order;
    }

    /**
     * 1-1、insert order to temp table
     *
     * @param order
     */
    private void insertOrderTemp(Order order) {
//        this.getNamedParameterJdbcTemplate().update("insert into order_temp (id, store_id, table_id, open_id, amount, payment_amount, discount_amount, status, buyer_memo, customer_id, spbill_create_ip, time_start, time_expire, pay_amt_usd, krw_usd_rate, total_amount) values (:id, :storeId, :tableId, :customerId, :amount, :paymentAmount, :discountAmount, :status, :buyerMemo, :customerId, :spbillCreateIp, :timeStart, :timeExpire, :payAmtUsd, :krwUsdRate, :totalAmount)", new BeanPropertySqlParameterSource(order));
        this.getNamedParameterJdbcTemplate().update(""
            + "insert into order_temp \n" +
            "(\n" +
            "    id\n" +
            "    , store_id\n" +
            "    , table_id\n" +
            "    , open_id\n" +
            "    , amount\n" +
            "    , payment_amount\n" +
            "    , discount_amount\n" +
            "    , AMOUNT_SETTLE\n" +
            "    , RATE_SETTLE\n" +
            "    , status\n" +
            "    , buyer_memo\n" +
            "    , customer_id\n" +
            "    , spbill_create_ip\n" +
            "    , time_start\n" +
            "    , time_expire\n" +
            "    , pay_amt_usd\n" +
            "    , krw_usd_rate\n" +
            "    , total_amount\n" +
            "    , BIZ_TYPE\n" +
            "    , ORDER_TYPE\n" +
            ") \n" +
            "values \n" +
            "(\n" +
            "    :id\n" +
            "    , :storeId\n" +
            "    , :tableId\n" +
            "    , :customerId\n" +
            "    , :amount\n" +
            "    , :paymentAmount\n" +
            "    , :discountAmount\n" +
            "    , :amountSettle\n" +
            "    , :rateSettle\n" +
            "    , :status\n" +
            "    , :buyerMemo\n" +
            "    , :customerId\n" +
            "    , :spbillCreateIp\n" +
            "    , :timeStart\n" +
            "    , :timeExpire\n" +
            "    , :payAmtUsd\n" +
            "    , :krwUsdRate\n" +
            "    , :totalAmount\n" +
            "    , :bizType\n" +
            "    , :orderType\n" +
            ")"
            + "", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 1-2、insert order info to temp info table
     *
     * @param order
     */
    private void insertOrderInfoTemp(Order order) {
//        this.getNamedParameterJdbcTemplate().update("insert into order_info_temp (pay_id, cust_nm, country_no, mobile, reseve_dt_from, reseve_dt_to, reseve_time, confirm_dt_from, confirm_dt_to, confirm_time, num_persons, shipping_type, shipping_addr, shipping_dt, shipping_time, shipping_cmt, cmt, DINING_PLACE, DINING_TIME) values (:id, :custNm, :countryNo, :mobile, :reseveDtfrom, :reseveDtto, :reseveTime, :confirmDtfrom, :confirmDtto, :confirmTime, :numPersons, :shippingType, :shippingAddr, :shippingDt, :shippingTime, :shippingCmt, :cmt ,:diningPlace ,:diningTime)", new BeanPropertySqlParameterSource(order));
        this.getNamedParameterJdbcTemplate().update(""
            + "insert into order_info_temp \n" +
            "(\n" +
            "    pay_id\n" +
            "    , cust_nm\n" +
            "    , country_no\n" +
            "    , mobile\n" +
            "    , reseve_dt_from\n" +
            "    , reseve_dt_to\n" +
            "    , reseve_time\n" +
            "    , confirm_dt_from\n" +
            "    , confirm_dt_to\n" +
            "    , confirm_time\n" +
            "    , num_persons\n" +
            "    , shipping_type\n" +
            "    , shipping_addr\n" +
            "    , shipping_dt\n" +
            "    , shipping_time\n" +
            "    , shipping_cmt\n" +
            "    , cmt\n" +
            "    , DINING_PLACE\n" +
            "    , DINING_TIME\n" +
            "    , cust_no\n" +
            "    , cust_nm_en\n" +
            "    , nm_last\n" +
            "    , nm_first\n" +
            "    , nm_en_last\n" +
            "    , nm_en_first\n" +
            ") \n" +
            "values \n" +
            "(\n" +
            "    :id\n" +
            "    , :custNm \n" +
            "    , :countryNo\n" +
            "    , :mobile\n" +
            "    , :reseveDtfrom\n" +
            "    , :reseveDtto\n" +
            "    , :reseveTime\n" +
            "    , :confirmDtfrom\n" +
            "    , :confirmDtto\n" +
            "    , :confirmTime\n" +
            "    , :numPersons\n" +
            "    , :shippingType\n" +
            "    , :shippingAddr\n" +
            "    , :shippingDt\n" +
            "    , :shippingTime\n" +
            "    , :shippingCmt\n" +
            "    , :cmt \n" +
            "    , :diningPlace \n" +
            "    , :diningTime\n" +
            "    , :custNo\n" +
            "    , concat(:nmFirstEn, ' ', :nmLastEn)\n" +
            "    , :nmLast\n" +
            "    , :nmFirst\n" +
            "    , :nmLastEn\n" +
            "    , :nmFirstEn\n" +
            ")"
            + "", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 1-3、batch insert items to temp
     *
     * @param itemList
     */
    private void insertOrderItemTemp(List<OrderItem> itemList) {
        this.getNamedParameterJdbcTemplate().batchUpdate("insert into order_item_temp (id, order_id, sku_id, sku_nm_kor, sku_nm_chn, price, PRICE_SETTLE, qty) values (:id, :orderId, :skuId, :skuNmKor, :skuNmChn, :price, :priceSettle, :qty)", SqlParameterSourceUtils.createBatch(itemList.toArray()));
    }

    /**
     * 1-4、更新酒店房间库存
     *
     * @param storeId
     * @param prodSkuId
     * @param fromDate
     * @param toDate
     */
    private void reduceHotelInventory(Long storeId, Long prodSkuId, String fromDate, String toDate) {
        String sql = "UPDATE INVENTORY_HOTEL SET INV_USED = INV_USED + 1, UPDATE_TIME = NOW(), UPDATE_USER = 'H5Order' WHERE STORE_ID = ? AND PROD_SKU_ID = ? AND DATE_FORMAT(INV_DATE,'%Y-%m-%d') = ?";
        try {
            // findDataAll抛出转换异常
            List<String> dateList = DateUtil.findDataAll(fromDate, toDate, 1);

            if (dateList == null || dateList.size() == 0) {
                log.info("【酒店下单】无扣减库存的日期##############################@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            } else {
                // 库存削减不削减最后一天toDate的。
                dateList.remove(dateList.size()-1);

                log.info("【酒店下单】需要扣减库存的日期>>> " + "【" + dateList.size() + "】" + dateList);
                this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, storeId);
                        preparedStatement.setLong(2, prodSkuId);
                        preparedStatement.setString(3, dateList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return dateList.size();
                    }
                });
            }
        } catch (ParseException e) {
            log.error("【酒店下单】扣减库存时对日期转换异常>>> " + e.getMessage(), e);
            throw new BizException(ErrorCode.INVENTHOTEL_UPDATE_FAILURE);
        }
    }

    /**
     * 2、调用微信支付后，更新临时订单状态
     *
     * @param order
     * @return
     */
    public int updateOrderTemp(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order_temp` set status = :status, prepay_id = :prepayId, updated = current_timestamp() where id = :id ", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 3-1、生成正式订单（支付成功后的订单）时查询临时订单信息
     *
     * @param id
     * @return
     */
    @Override
    public Order getOrderPay(Long id) {
        final List<Order> order = this.getJdbcTemplate().query(
            "select \n" +
                "        a.id\n" +
                "		, a.store_id\n" +
                "        , b.number as table_id\n" +
                "        , ifnull(c.pay_id,0) as pay_id\n" +
                "        , ifnull(d.id,0) as order_id\n" +
                "        , a.trans_id as trans_id\n" +
                "        , a.biz_type as biz_type\n" +
                "from	eorder.order_temp a\n" +
                "        left outer join eorder.store_table b\n" +
                "                on a.table_id = b.id\n" +
                "                and a.store_id = b.store_id\n" +
                "        left outer join eorder.order_pay c\n" +
                "                on a.id = c.order_id\n" +
                "        left outer join eorder.`order` d\n" +
                "                on a.id = d.id\n" +
                "where   a.id = ? \n" +
                "limit 1",
            new Object[]{id},
            new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .storeId(resultSet.getLong("store_id"))
                        .tableId(resultSet.getLong("table_id"))
                        .payId(resultSet.getLong("pay_id"))
                        .orderId(resultSet.getLong("order_id"))
                        .transId(resultSet.getLong("trans_id"))
                        .bizType(resultSet.getInt("biz_type"))
                        .build();
                }
            });
        return order.isEmpty() ? null : order.get(0);
    }

    /**
     * 3-2、插入微信支付成功后的OrderPay信息
     *
     * @param pay
     */
    @Override
    public void insertOrderPay(OrderPay pay) {
        // this.getNamedParameterJdbcTemplate().update("insert into order_pay(pay_id, order_id, device_info, nonce_str, sign, sign_type, result_code, err_code, err_code_des, open_id, is_subscribe, trade_type, bank_type, total_fee, fee_type, cash_fee, cash_fee_type, transaction_id, out_trade_no, attach, pay_dts, pay_status, rate_value, pay_desc) values ( :payId, :orderId, :deviceInfo, :nonceStr, :sign, :signType, :resultCode, :errCode, :errCodeDes, :openId, :isSubscribe, :tradeType, :bankType, :totalFee, :feeType, :cashFee, :cashFeeType, :transactionId, :outTradeNo, :attach, :payDts, :payStatus, :rateValue, :payDesc)", new BeanPropertySqlParameterSource(pay));
        String sql = "insert into eorder.order_pay\n" +
            "(\n" +
            "    pay_id\n" +
            "    , order_id\n" +
            "    , device_info\n" +
            "    , nonce_str\n" +
            "    , sign\n" +
            "    , sign_type\n" +
            "    , result_code\n" +
            "    , err_code\n" +
            "    , err_code_des\n" +
            "    , open_id\n" +
            "    , is_subscribe\n" +
            "    , sub_open_id\n" +
            "    , sub_is_subscribe\n" +
            "    , trade_type\n" +
            "    , trade_state\n" +
            "    , bank_type\n" +
            "    , total_fee\n" +
            "    , fee_type\n" +
            "    , cash_fee\n" +
            "    , cash_fee_type\n" +
            "    , transaction_id\n" +
            "    , out_trade_no\n" +
            "    , attach\n" +
            "    , pay_dts\n" +
            "    , pay_status\n" +
            "    , rate_value\n" +
            "    , pay_desc\n" +
            ") \n" +
            "values \n" +
            "( \n" +
            "    :payId\n" +
            "    , :orderId\n" +
            "    , :deviceInfo\n" +
            "    , :nonceStr\n" +
            "    , :sign\n" +
            "    , :signType\n" +
            "    , :resultCode\n" +
            "    , :errCode\n" +
            "    , :errCodeDes\n" +
            "    , :openId\n" +
            "    , :isSubscribe\n" +
            "    , :subOpenId\n" +
            "    , :subIsSubscribe\n" +
            "    , :tradeType\n" +
            "    , :tradeState\n" +
            "    , :bankType\n" +
            "    , :totalFee\n" +
            "    , :feeType\n" +
            "    , :cashFee\n" +
            "    , :cashFeeType\n" +
            "    , :transactionId\n" +
            "    , :outTradeNo\n" +
            "    , :attach\n" +
            "    , :payDts\n" +
            "    , :payStatus\n" +
            "    , :rateValue\n" +
            "    , :payDesc\n" +
            ")\n" +
            "on duplicate key update\n" +
            "    order_id = :orderId\n" +
            "    , device_info = :deviceInfo\n" +
            "    , nonce_str = :nonceStr\n" +
            "    , sign = :sign\n" +
            "    , sign_type = :signType\n" +
            "    , result_code = :resultCode\n" +
            "    , err_code = :errCode\n" +
            "    , err_code_des = :errCodeDes\n" +
            "    , open_id = :openId\n" +
            "    , is_subscribe = :isSubscribe\n" +
            "    , sub_open_id = :subOpenId\n" +
            "    , sub_is_subscribe = :subIsSubscribe\n" +
            "    , trade_type = :tradeType\n" +
            "    , trade_state = :tradeState\n" +
            "    , bank_type = :bankType\n" +
            "    , total_fee = :totalFee\n" +
            "    , fee_type = :feeType\n" +
            "    , cash_fee = :cashFee\n" +
            "    , cash_fee_type = :cashFeeType\n" +
            "    , transaction_id = :transactionId\n" +
            "    , out_trade_no = :outTradeNo\n" +
            "    , attach = :attach\n" +
            "    , pay_dts = :payDts\n" +
            "    , pay_status = :payStatus\n" +
            "    , rate_value = :rateValue\n" +
            "    , pay_desc = :payDesc\n" +
            "    , update_dts = now()";
        this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(pay));
    }

    /**
     * 3-3、支付成功并响应微信支付平台后生成正式的订单
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public Order saveOrder(Order order) {
        // 3-3-1 由临时订单生成正式订单
        insertOrderFromTemp(order);

        // 3-3-2 由临时订单信息生成正式订单信息
        insertOrderInfoFromTemp(order);

        // 3-3-3 由临时订单项生成正式订单项
        insertOrderItemFromTemp(order);

        // 20200512 如果为零售业务则记录订单事件
        if(CommonConstants.BIZ_SHOPPING_INT == order.getBizType()){
            // 3-3-4 记录订单事件-支付成功
            insertRetailOrderEvent(order);
        }

        /*insertOrder(order);
        List<OrderItem> itemList = order.getItemList();

        if (itemList != null && itemList.size() > 0) {
            Long finalOrderId = order.getId();
            List<OrderItem> newItemList = itemList.stream().map(item -> {
                OrderItem orderItem = item.createNewOrderItem(uidGenerator.generate(BusinessTypeEnum.ORDER_ITEM), finalOrderId, item).build();
                return orderItem;
            }).collect(Collectors.toList());

            insertOrderItem(newItemList);
        }*/
        return order;
    }

    /**
     * 3-3-1、由临时订单生成正式订单
     *
     * @param order
     */
    private void insertOrderFromTemp(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into `order`(id, store_id, table_id, open_id, amount, payment_amount, discount_amount, AMOUNT_SETTLE, RATE_SETTLE, status, buyer_memo, customer_id, spbill_create_ip, time_start, time_expire, pay_amt_usd, krw_usd_rate, prepay_id, trans_id, pay_amt_cny, total_amount, BIZ_TYPE, ORDER_TYPE) select trans_id as id, store_id, table_id, open_id, amount, payment_amount, discount_amount, AMOUNT_SETTLE, RATE_SETTLE,:status as status, buyer_memo, customer_id, spbill_create_ip, time_start, time_expire, pay_amt_usd, krw_usd_rate, prepay_id, id as trans_id, :payAmtCny as pay_amt_cny, total_amount, BIZ_TYPE, ORDER_TYPE from order_temp where id = :id", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 3-3-2、由临时订单信息生成正式订单信息
     *
     * @param order
     */
    private void insertOrderInfoFromTemp(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into order_info \n" +
            "(order_id"
            + ", pay_id"
            + ", cust_nm"
            + ", country_no"
            + ", mobile"
            + ", reseve_dt_from"
            + ", reseve_dt_to"
            + ", reseve_time"
            + ", confirm_dt_from"
            + ", confirm_dt_to"
            + ", confirm_time"
            + ", num_persons"
            + ", shipping_type"
            + ", shipping_addr"
            + ", shipping_dt"
            + ", shipping_time"

            + ", shipping_mode"
            + ", shipping_mode_name_chn"
            + ", shipping_mode_name_kor"
            + ", shipping_mode_name_eng"
            + ", shipping_addr_detail"
            + ", shipping_addr_country"
            + ", shipping_weight"
            + ", shipping_cost"
            + ", shipping_cost_rule"

            + ", shipping_cmt"
            + ", DINING_PLACE"
            + ", DINING_TIME"
            + ", cmt\n"
            + ", cust_no\n"
            + ", cust_nm_en\n"
            + ", nm_last\n"
            + ", nm_first\n"
            + ", nm_en_last\n"
            + ", nm_en_first\n"
            + ") \n" +
            "select\n" +
            "	a.trans_id\n" +
            "	, b.pay_id\n" +
            "	, b.cust_nm \n" +
            "	, b.country_no\n" +
            "	, b.mobile\n" +
            "	, b.reseve_dt_from\n" +
            "	, b.reseve_dt_to\n" +
            "	, b.reseve_time\n" +
            "	, b.confirm_dt_from\n" +
            "	, b.confirm_dt_to\n" +
            "	, b.confirm_time\n" +
            "	, b.num_persons\n" +
            "	, b.shipping_type\n" +
            "	, b.shipping_addr\n" +
            "	, b.shipping_dt\n" +
            "	, b.shipping_time\n" +

            "   , b.shipping_mode\n" +
            "   , b.shipping_mode_name_chn\n" +
            "   , b.shipping_mode_name_kor\n" +
            "   , b.shipping_mode_name_eng\n" +
            "   , b.shipping_addr_detail\n" +
            "   , b.shipping_addr_country\n" +
            "   , b.shipping_weight\n" +
            "   , b.shipping_cost\n" +
            "   , b.shipping_cost_rule\n" +

            "	, b.shipping_cmt\n" +
            "	, b.DINING_PLACE\n" +
            "	, b.DINING_TIME\n" +
            "	, b.cmt \n" +
            "   , b.cust_no \n" +
            "   , concat(b.nm_en_first, ' ', b.nm_en_last) \n" +
            "   , b.nm_last \n" +
            "   , b.nm_first \n" +
            "   , b.nm_en_last \n" +
            "   , b.nm_en_first \n" +
            "from	order_temp a\n" +
            "	left join order_info_temp b\n" +
            "		on a.id = b.pay_id\n" +
            "where	a.id = :id", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 3-3-3、由临时订单项生成正式订单项
     *
     * @param order
     */
    private void insertOrderItemFromTemp(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into order_item (id, order_id, sku_id, sku_nm_kor, sku_nm_chn, price, PRICE_SETTLE, qty) select id, :transId as order_id, sku_id, sku_nm_kor, sku_nm_chn, price, PRICE_SETTLE, qty from order_item_temp where order_id = :id", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 3-3-4、记录订单事件-支付成功
     *
     * @param order
     */
    private void insertRetailOrderEvent(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into RETAIL_ORDER_EVENT \n" +
                "(\n" +
                "    ORDER_ID\n" +
                "    , EVENT_TYPE\n" +
                "    , EVENT_INITIATOR\n" +
                "    , IS_MAIN\n" +
                "    , EVENT_TIME\n" +
                "    , EVENT_NAME\n" +
                "    , EVENT_TARGET\n" +
                "    , EVENT_RESULT\n" +
                "    , EVENT_RESULT_DESC\n" +
                "    , SERV_ID\n" +
                ") \n" +
                "values \n" +
                "(\n" +
                "    :transId\n" +
                "    , 1\n" +
                "    , 1\n" +
                "    , 1\n" +
                "    , now()\n" +
                "    , '支付成功'\n" +
                "    , ''\n" +
                "    , 1\n" +
                "    , ''\n" +
                "    , 0\n" +
                ")", new BeanPropertySqlParameterSource(order));
    }
    /*************************************Wechat H5用户下单全过程-END*******************************************/

    /*************************************WECHAT H5用户不支付下单全过程-START*******************************************/
    /**
     * 保存下单不支付的临时订单
     *
     * @param order
     */
    public int saveNoPayTempOrder(Order order){
        return this.getNamedParameterJdbcTemplate().update(""
            + "insert into order_temp \n" +
            "(\n" +
            "    id\n" +
            "    , store_id\n" +
            "    , table_id\n" +
            "    , open_id\n" +
            "    , amount\n" +
            "    , payment_amount\n" +
            "    , discount_amount\n" +
            "    , status\n" +
            "    , buyer_memo\n" +
            "    , customer_id\n" +
            "    , spbill_create_ip\n" +
            "    , time_start\n" +
            "    , time_expire\n" +
            "    , pay_amt_usd\n" +
            "    , krw_usd_rate\n" +
            "    , total_amount\n" +
            "    , BIZ_TYPE\n" +
            "    , ORDER_TYPE\n" +
            ") \n" +
            "values \n" +
            "(\n" +
            "    :id\n" +
            "    , :storeId\n" +
            "    , :tableId\n" +
            "    , :customerId\n" +
            "    , :amount\n" +
            "    , :paymentAmount\n" +
            "    , :discountAmount\n" +
            "    , :status\n" +
            "    , :buyerMemo\n" +
            "    , :customerId\n" +
            "    , :spbillCreateIp\n" +
            "    , :timeStart\n" +
            "    , :timeExpire\n" +
            "    , :payAmtUsd\n" +
            "    , :krwUsdRate\n" +
            "    , :totalAmount\n" +
            "    , :bizType\n" +
            "    , :orderType\n" +
            ")"
            + "", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 保存下单不支付的订单
     *
     * @param order
     */
    @Override
    @Transactional
    public void saveNoPayOrder(Order order) {
        // insert no pay order to order table
        insertNoPayOrder(order);

        // insert no pay order info to order info table
        insertNoPayOrderInfo(order);

        // insert order items to temp
        List<OrderItem> itemList = order.getItemList();
        if (itemList != null && itemList.size() > 0) {
            Long finalOrderId = order.getTransId();
            List<OrderItem> newItemList = itemList.stream().map(item -> {
                OrderItem orderItem = item.createNewOrderItem(uidGenerator.generate(BusinessTypeEnum.ORDER_ITEM), finalOrderId, item).build();
                return orderItem;
            }).collect(Collectors.toList());
            // batch insert items to order item table
            insertNoPayOrderItem(newItemList);
        }
    }

    /**
     * insert no pay order to order table
     *
     * @param order
     */
    private void insertNoPayOrder(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into `order` (id, store_id, table_id, open_id, amount, payment_amount, discount_amount, status, buyer_memo, customer_id, spbill_create_ip, time_start, time_expire, pay_amt_usd, krw_usd_rate, total_amount, trans_id) values (:transId, :storeId, :tableId, :customerId, :amount, :paymentAmount, :discountAmount, :status, :buyerMemo, :customerId, :spbillCreateIp, :timeStart, :timeExpire, :payAmtUsd, :krwUsdRate, :totalAmount, :id)", new BeanPropertySqlParameterSource(order));
    }

    /**
     * insert no pay order info to order info table
     *
     * @param order
     */
    private void insertNoPayOrderInfo(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into order_info (order_id, pay_id, cust_nm, country_no, mobile, reseve_dt_from, reseve_dt_to, reseve_time, confirm_dt_from, confirm_dt_to, confirm_time, num_persons, shipping_type, shipping_addr, shipping_dt, shipping_time, shipping_cmt, cmt) values (:transId, :id, :custNm, :countryNo, :mobile, :reseveDtfrom, :reseveDtto, :reseveTime, :confirmDtfrom, :confirmDtto, :confirmTime, :numPersons, :shippingType, :shippingAddr, :shippingDt, :shippingTime, :shippingCmt, :cmt)", new BeanPropertySqlParameterSource(order));
    }

    /**
     * batch insert items to order item table
     *
     * @param itemList
     */
    private void insertNoPayOrderItem(List<OrderItem> itemList) {
        this.getNamedParameterJdbcTemplate().batchUpdate("insert into order_item (id, order_id, sku_id, sku_nm_kor, sku_nm_chn, price, qty) values (:id, :orderId, :skuId, :skuNmKor, :skuNmChn, :price, :qty)", SqlParameterSourceUtils.createBatch(itemList.toArray()));
    }
    /*************************************WECHAT H5用户不支付下单全过程-END*******************************************/


    private void insertOrder(Order order) {
        this.getNamedParameterJdbcTemplate().update("insert into `order`(id, store_id, table_id, open_id, amount, payment_amount, discount_amount, status, buyer_memo, customer_id, spbill_create_ip, time_start, time_expire, pay_amt_usd, krw_usd_rate, prepay_id, pay_amt_cny) values (:id, :storeId, :tableId, :customerId, :amount, :paymentAmount, :discountAmount, :status, :buyerMemo, :customerId, :spbillCreateIp, :timeStart, :timeExpire, :payAmtUsd, :krwUsdRate, :prepayId, :payAmtCny)", new BeanPropertySqlParameterSource(order));
    }

    public int updateOrder(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp(), cancel_dt = case when :status = 7 then current_timestamp() else cancel_dt end where id = :id ", new BeanPropertySqlParameterSource(order));
    }

    @Override
    public int updateOrderStatusAndChange(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, CHANGE_STATUS = :changeStatus, updated = current_timestamp(), cancel_dt = case when :status = 7 then current_timestamp() else cancel_dt end where id = :id ", new BeanPropertySqlParameterSource(order));

    }

    public int updateOrder_success(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp() where id = :id and status in (0,1,2,3,4) ", new BeanPropertySqlParameterSource(order));
    }

    public int updateSuccessOrder(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, prepay_id = :prepayId, updated = current_timestamp() where id = :id ", new BeanPropertySqlParameterSource(order));
    }

    private void insertOrderItem(List<OrderItem> itemList) {
        this.getNamedParameterJdbcTemplate().batchUpdate("insert into order_item (id, order_id, sku_id, sku_nm_kor, sku_nm_chn, price, qty) values (:id, :orderId, :skuId, :skuNmKor, :skuNmChn, :price, :qty)", SqlParameterSourceUtils.createBatch(itemList.toArray()));
    }

    private void updateOrderItem(List<OrderItem> itemList) {
        this.getNamedParameterJdbcTemplate().batchUpdate("update order_item set qty = :qty, updated = current_timestamp() where id = :id", SqlParameterSourceUtils.createBatch(itemList.toArray()));
    }

    public int updateOrderPay_fail(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update order_pay set pay_status = 0, update_dts = current_timestamp() where order_id = :id ", new BeanPropertySqlParameterSource(order));
    }

    @Override
    @Transactional
    public Order saveOrderFromTemp(Order order) {
        insertOrderFromTemp(order);
        insertOrderInfoFromTemp(order);
        insertOrderItemFromTemp(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        final List<Order> order = this.getJdbcTemplate().query(
            "select\n" +
                "        t1.id\n" +
                "        , t1.store_id\n" +
                "        , t1.table_id\n" +
                "        , t1.open_id\n" +
                "        , t1.amount\n" +
                "        , t1.payment_amount\n" +
                "        , t1.discount_amount\n" +
                "        , t1.pay_amt_cny\n" +
                "        , t1.pay_amt_usd\n" +
                "        , t1.krw_usd_rate\n" +
                "        , t1.usd_cny_rate\n" +
                "        , t1.status\n" +
                "        , t1.CHANGE_STATUS\n" +
                "        , t1.buyer_memo\n" +
                "        , t1.customer_id\n" +
                "        , t1.time_start\n" +
                "        , t1.time_expire\n" +
                "        , t1.spbill_create_ip\n" +
                "        , t1.prepay_id\n" +
                "        , t1.created\n" +
                "        , t1.updated\n" +
                "        , t1.trans_id\n" +
                "        , (select max(a.refund_id) from eorder.order_pay_cancel a where a.out_trade_no = t1.id) as refund_id\n" +
                "	     , t2.cust_nm\n" +
                "	     , ifnull(t2.country_no, 0) as country_no\n" +
                "	     , t2.mobile\n" +
                "	     , t2.reseve_dt_from\n" +
                "	     , t2.reseve_dt_to\n" +
                "	     , ifnull(t2.reseve_time, 0) as reseve_time\n" +
                "	     , t2.confirm_dt_from\n" +
                "	     , t2.confirm_dt_to\n" +
                "	     , ifnull(t2.confirm_time, 0) as confirm_time\n" +
                "	     , ifnull(t2.num_persons, 0) as num_persons\n" +
                "	     , ifnull(t2.shipping_type, 0) as shipping_type\n" +
                "	     , ifnull(t2.shipping_addr, 0) as shipping_addr\n" +
                "	     , t2.shipping_dt\n" +
                "	     , ifnull(t2.shipping_time, 0) as shipping_time\n" +
                "	     , t2.shipping_cmt \n" +
                "        , t2.cmt \n" +
                "		 , (\n" +
                "        		select\n" +
                "                	max(c.name_chn)\n" +
                "        		from    order_item a\n" +
                "                		inner join product_sku b\n" +
                "                        		on a.sku_id = b.id\n" +
                "                		inner join product c\n" +
                "                        		on b.product_id = c.id\n" +
                "        		where   a.order_id = t1.id\n" +
                "    	   ) as productNm \n" +
                "		 , (select a.ship_point_nm from ship_point a where a.ship_point_id = t2.shipping_addr) shippingAddrNm \n" +
                "        , t1.ORDER_TYPE as orderType \n" +
                "from    eorder.`order` t1\n" +
                "        left join eorder.order_info t2 \n" +
                "               on t1.id = t2.order_id\n" +
                "where   t1.id = ?",
            new Object[]{id},
            new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .storeId(resultSet.getLong("store_id"))
                        .tableId(resultSet.getLong("table_id"))
                        .amount(resultSet.getBigDecimal("amount"))
                        .paymentAmount(resultSet.getBigDecimal("payment_amount"))
                        .payAmtUsd(resultSet.getBigDecimal("pay_amt_usd"))
                        .discountAmount(resultSet.getBigDecimal("discount_amount"))
                        .status(Order.Status.get(resultSet.getInt("status")))
                        .buyerMemo(resultSet.getString("buyer_memo"))
                        .customerId(resultSet.getString("customer_id"))
                        .spbillCreateIp(resultSet.getString("spbill_create_ip"))
                        .timeStart(resultSet.getString("time_start"))
                        .timeExpire(resultSet.getString("time_expire"))
                        .transId(resultSet.getLong("trans_id"))
                        .custNm(resultSet.getString("cust_nm"))
                        .countryNo(resultSet.getInt("country_no"))
                        .mobile(resultSet.getString("mobile"))
                        .reseveDtfrom(resultSet.getString("reseve_dt_from"))
                        .reseveDtto(resultSet.getString("reseve_dt_to"))
                        .reseveTime(resultSet.getInt("reseve_time"))
                        .confirmDtfrom(resultSet.getString("confirm_dt_from"))
                        .confirmDtto(resultSet.getString("confirm_dt_to"))
                        .confirmTime(resultSet.getInt("confirm_time"))
                        .numPersons(resultSet.getInt("num_persons"))
                        .shippingType(resultSet.getInt("shipping_type"))
                        .shippingAddr(resultSet.getLong("shipping_addr"))
                        .shippingDt(resultSet.getString("shipping_dt"))
                        .shippingTime(resultSet.getInt("shipping_time"))
                        .shippingCmt(resultSet.getString("shipping_cmt"))
                        .cmt(resultSet.getString("cmt"))
                        .productNm(resultSet.getString("productNm"))
                        .payAmtCny(resultSet.getBigDecimal("pay_amt_cny"))
                        .shippingAddrNm(resultSet.getString("shippingAddrNm"))
                        .orderType(resultSet.getInt("orderType"))
                        .changeStatus(resultSet.getInt("CHANGE_STATUS"))
                        .build();
                }
            });
        return order.isEmpty() ? null : order.get(0);
    }

    @Override
    public Order getOrderTemp(Long id) {
        final List<Order> order = this.getJdbcTemplate().query(
            "select\n" +
                "        t1.id\n" +
                "        , t1.store_id\n" +
                "        , t1.table_id\n" +
                "        , t1.open_id\n" +
                "        , t1.amount\n" +
                "        , t1.payment_amount\n" +
                "        , t1.discount_amount\n" +
                "        , t1.pay_amt_cny\n" +
                "        , t1.pay_amt_usd\n" +
                "        , t1.krw_usd_rate\n" +
                "        , t1.usd_cny_rate\n" +
                "        , t1.status\n" +
                "        , t1.buyer_memo\n" +
                "        , t1.customer_id\n" +
                "        , t1.time_start\n" +
                "        , t1.time_expire\n" +
                "        , t1.spbill_create_ip\n" +
                "        , t1.prepay_id\n" +
                "        , t1.created\n" +
                "        , t1.updated\n" +
                "        , (select max(a.refund_id) from eorder.order_pay_cancel a where a.out_trade_no = t1.id) as refund_id\n" +
                "from    eorder.`order_temp` t1\n" +
                "where   t1.id = ?",
            new Object[]{id},
            new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .storeId(resultSet.getLong("store_id"))
                        .tableId(resultSet.getLong("table_id"))
                        .amount(resultSet.getBigDecimal("amount"))
                        .paymentAmount(resultSet.getBigDecimal("payment_amount"))
                        .payAmtUsd(resultSet.getBigDecimal("pay_amt_usd"))
                        .discountAmount(resultSet.getBigDecimal("discount_amount"))
                        .status(Order.Status.get(resultSet.getInt("status")))
                        .buyerMemo(resultSet.getString("buyer_memo"))
                        .customerId(resultSet.getString("customer_id"))
                        .spbillCreateIp(resultSet.getString("spbill_create_ip"))
                        .timeStart(resultSet.getString("time_start"))
                        .timeExpire(resultSet.getString("time_expire"))
                        .build();
                }
            });
        return order.isEmpty() ? null : order.get(0);
    }

    @Override
    public void insertOrderPayCancel(OrderPayCancel payCalcel) {
        String sql = "insert into eorder.order_pay_cancel\n" +
            "(\n" +
            "    cancel_id\n" +
            "    , order_id\n" +
            "    , return_code\n" +
            "    , return_msg\n" +
            "    , appid\n" +
            "    , mch_id\n" +
            "    , sub_mch_id\n" +
            "    , nonce_str\n" +
            "    , sign\n" +
            "    , result_code\n" +
            "    , transaction_id\n" +
            "    , out_trade_no\n" +
            "    , out_refund_no\n" +
            "    , refund_id\n" +
            "    , refund_channel\n" +
            "    , refund_fee\n" +
            "    , coupon_refund_fee\n" +
            "    , total_fee\n" +
            "    , cash_fee\n" +
            "    , fee_type\n" +
            "    , coupon_refund_count\n" +
            "    , cash_refund_fee\n" +
            "    , refund_fee_type\n" +
            "    , cash_fee_type\n" +
            "    , cash_refund_fee_type" +
            ")\n" +
            "values\n" +
            "(\n" +
            "    :cancelId\n" +
            "    , :orderId\n" +
            "    , :returnCode\n" +
            "    , :returnMsg\n" +
            "    , :appId\n" +
            "    , :mchId\n" +
            "    , :subMchId\n" +
            "    , :nonceStr\n" +
            "    , :sign\n" +
            "    , :resultCode\n" +
            "    , :transactionId\n" +
            "    , :outTradeNo\n" +
            "    , :outRefundNo\n" +
            "    , :refundId\n" +
            "    , :refundChannel\n" +
            "    , :refundFee\n" +
            "    , :couponRefund_fee\n" +
            "    , :totalFee\n" +
            "    , :cashFee\n" +
            "    , :feeType\n" +
            "    , :couponRefundCount\n" +
            "    , :cashRefundFee\n" +
            "    , :refundFeeType\n" +
            "    , :cashFeeType\n" +
            "    , :cashRefundFeeType\n" +
            ")";
        this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(payCalcel));
    }

    @Override
    public int acceptOrder(AcceptOrder acceptOrder) {
        String sql = "update `order` set status=:status, updated=now() where id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(acceptOrder));
    }

    @Override
    public int batchInsertOrderBackupCompled(List<Long> orderIds) {
        String sql = "replace into `order_completed_backup`\n" +
                "    (\n" +
                "        id,store_id,table_id,open_id\n" +
                "        ,amount,payment_amount,discount_amount,pay_amt_cny,pay_amt_usd,krw_usd_rate,usd_cny_rate,AMOUNT_SETTLE,RATE_SETTLE\n" +
                "        ,status,STATUS_8_FROM,buyer_memo,customer_id,time_start,time_expire\n" +
                "        ,spbill_create_ip\n" +
                "        ,prepay_id,created,updated,pay_dt,cancel_dt,trans_id\n" +
                "        ,total_amount,BIZ_TYPE,ORDER_TYPE,CHANGE_STATUS\n" +
                "    )\n" +
                "select\n" +
                "    id,store_id,table_id,open_id\n" +
                "    ,amount,payment_amount,discount_amount,pay_amt_cny,pay_amt_usd,krw_usd_rate,usd_cny_rate,AMOUNT_SETTLE,RATE_SETTLE \n" +
                "    ,status,STATUS_8_FROM,buyer_memo,customer_id,time_start,time_expire\n" +
                "    ,spbill_create_ip" +
                "    ,prepay_id,created,updated,pay_dt,cancel_dt,trans_id\n" +
                "    ,total_amount,BIZ_TYPE,ORDER_TYPE,CHANGE_STATUS\n" +
                "from    eorder.`order` o\n" +
                "where   o.id = ?\n" ;
        this.getJdbcTemplate().batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        Long id = orderIds.get(i);
                        preparedStatement.setLong(1,id);
                    }

                    @Override
                    public int getBatchSize() {
                        return orderIds.size();
                    }
                });
        return orderIds.size();
    }

    @Override
    public int batchInsertRetailOrderBackupCompled(List<Long> orderIds) {
        String sql = "replace into `order_retail_completed_backup`\n" +
                "    (\n" +
                "        id,store_id,table_id,open_id\n" +
                "        ,amount,payment_amount,discount_amount,pay_amt_cny,pay_amt_usd,krw_usd_rate,usd_cny_rate,AMOUNT_SETTLE,RATE_SETTLE\n" +
                "        ,status,STATUS_8_FROM,buyer_memo,customer_id,time_start,time_expire\n" +
                "        ,spbill_create_ip\n" +
                "        ,prepay_id,created,updated,pay_dt,cancel_dt,trans_id\n" +
                "        ,total_amount,BIZ_TYPE,ORDER_TYPE,CHANGE_STATUS\n" +
                "    )\n" +
                "select\n" +
                "    id,store_id,table_id,open_id\n" +
                "    ,amount,payment_amount,discount_amount,pay_amt_cny,pay_amt_usd,krw_usd_rate,usd_cny_rate,AMOUNT_SETTLE,RATE_SETTLE \n" +
                "    ,status,STATUS_8_FROM,buyer_memo,customer_id,time_start,time_expire\n" +
                "    ,spbill_create_ip" +
                "    ,prepay_id,created,updated,pay_dt,cancel_dt,trans_id\n" +
                "    ,total_amount,BIZ_TYPE,ORDER_TYPE,CHANGE_STATUS\n" +
                "from    eorder.`order` o\n" +
                "where   o.id = ?\n" ;
                /*"on duplicate key\n" +
                "    update\n" +
                "        store_id = o.store_id\n";*/
        this.getJdbcTemplate().batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        Long id = orderIds.get(i);
                        preparedStatement.setLong(1, id);
                    }

                    @Override
                    public int getBatchSize() {
                        return orderIds.size();
                    }
                });
        return orderIds.size();
    }



    @Override
    public int batchUpOrderStatus(List<Long> orderIds, int status) {
        this.getJdbcTemplate().batchUpdate("update `order` set status = ?,updated=now() where id=?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Long id = orderIds.get(i);
                preparedStatement.setInt(1,status);
                preparedStatement.setLong(2,id);
            }

            @Override
            public int getBatchSize() {
                return orderIds.size();
            }
        });
        return orderIds.size();
    }

    public int saveStoreCustNo(SaveOrder saveOrder) {
        String sql = ""
            + "update  eorder.order_info\n" +
            "set     cust_no = :custNo\n" +
            "where   order_id = :orderId\n" +
            ";"
            + "";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(saveOrder));
    }

    @Override
    public int upShippingCmt(SaveOrder saveOrder) {
        String sql = "update  eorder.order_info set shipping_cmt=:shippingCmt where order_id = :orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(saveOrder));

    }

    @Override
    public int acceptOrderClinic(ConfirmOrder confirmOrder) {
        String sql = "update `order` set status=:status, updated=now() where id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public int confirmOrderClinic(ConfirmOrder confirmOrder) {
        String sql = "update order_info set reseve_dt_from = :reseveDtfrom, reseve_time = :reseveTime, reseve_confirm_time = :reseveConfirmtime, shipping_cmt = :shippingCmt where order_id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public int confirmOrderShopping(ConfirmOrder confirmOrder) {
        String sql = "update order_info set shipping_no = :shippingNo, shipping_cmt = :shippingCmt where order_id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public int acceptOrderHotel(ConfirmOrder confirmOrder) {
        String sql = "update `order` set status=:status, updated=now() where id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public int confirmOrderHotel(ConfirmOrder confirmOrder) {
        String sql = "update order_info set shipping_cmt = :shippingCmt where order_id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public int cancelOrderReserve(ConfirmOrder confirmOrder) {
        String sql = "update order_info set cancel_reason = :cancelReason where order_id=:orderId";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(confirmOrder));
    }

    @Override
    public Long getOrderPayId(Long orderId) {
        Map<String, Object> param = new HashedMap();
        param.put("orderId", orderId);
        String sql = "select ifnull(max(pay_id),0) from eorder.order_pay where order_id=:orderId limit 1";
        Long payId = this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Long.class);
        return payId;
    }

    @Override
    public Long getOrderTransId(Long orderId) {
        Map<String, Object> param = new HashedMap();
        param.put("orderId", orderId);
        String sql = "select ifnull(max(trans_id),0) as trans_id from eorder.order_temp where id=:orderId limit 1";
        Long transId = this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Long.class);
        return transId;
    }

    @Override
    public int upSettleStatus(UpSettleStatus upSettleStatus) {
        String sql = "UPDATE order_closing set cash_settle_type=:cashSettleType , amount = :amount ,service_fee=:serviceFee,status=:status ,updated=now()  where store_id=:storeId and closing_months=:closingMonths";
        return this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(upSettleStatus));
    }

    public int insertClosing(UpSettleStatus upSettle) {
        String sql = "insert into order_closing(store_id,closing_months,cash_settle_type,amount,service_fee,created) values (?,?,?,?,?,now())";
        return this.getJdbcTemplate().update(sql, new Object[]{
            upSettle.getStoreId()
            ,upSettle.getClosingMonths()
            ,upSettle.getCashSettleType()
            ,upSettle.getAmount()
            ,upSettle.getServiceFee()
        });
    }

    public int getClosing(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) from order_closing where 1=1 ");
        query.append(" and store_id = :storeId");
        query.append(" and closing_months=:closingMonths");
        return this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
    }

    /**
     * 酒店库存恢复，查询酒店订单的sku
     *
     * @param orderId
     * @return
     */
    public Long queryHotelOrderItem(Long orderId) {
        Map<String, Object> param = new HashedMap();
        param.put("orderId", orderId);
        String sql = "SELECT sku_id AS skuId FROM eorder.order_item WHERE order_id=:orderId LIMIT 1";
        Long prodSkuId = this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Long.class);
        return prodSkuId;
    }

    /**
     * 删除商户的月度结算结果
     *
     * @param storeId
     * @param year
     * @param month
     */
    public Long deleteStoreSettlement(Long storeId, int year, int month) {
        this.getJdbcTemplate().update("delete from store_settlement where STORE_ID = ? AND SETTLE_YEAR = ? AND SETTLE_MONTH = ?",
            new Object[]{
                storeId,
                year,
                month
            });
        return storeId;
    }

    /**
     * 存储商户的月度结算结果
     *
     * @param storeSettlement
     */
    public void saveStoreSettlement(StoreSettlement storeSettlement) {
        this.getJdbcTemplate().update("insert into store_settlement (SID,STORE_ID,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,START_DT,END_DT,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
            "   values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,now(),'ba',now(),'ba')", new Object[]{
            storeSettlement.getSid(),
            storeSettlement.getStoreID(),
            storeSettlement.getSettleYearMonth(),
            storeSettlement.getSettleYear(),
            storeSettlement.getSettleMonth(),
            storeSettlement.getStartDT(),
            storeSettlement.getEndDT(),
            storeSettlement.getSettleType(),
            storeSettlement.getSettleRate(),
            storeSettlement.getSettleFee(),
            storeSettlement.getOrderCount(),
            storeSettlement.getSettleSum(),
            storeSettlement.getPgDate(),
            storeSettlement.getPgSum(),
            storeSettlement.getPgServiceFee(),
            //storeSettlement.getPlDate(),
            storeSettlement.getPlMinFee(),
            storeSettlement.getPlServiceFee(),
            storeSettlement.getPlFinalFee()
        });
    }

    /**
     * 存储商户的月度结算结果(手工)
     *
     * @param storeId
     * @param year
     * @param month
     * @param storeSettlement
     */
    @Transactional
    public void saveStoreSettlementManual(Long storeId, int year, int month, StoreSettlement storeSettlement) {
        this.getJdbcTemplate().update("delete from store_settlement_manual where STORE_ID = ? AND SETTLE_YEAR = ? AND SETTLE_MONTH = ?",
            new Object[]{
                storeId,
                year,
                month
            });

        this.getJdbcTemplate().update("insert into store_settlement_manual (SID,STORE_ID,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,START_DT,END_DT,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
            "   values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,now(),'ba',now(),'ba')", new Object[]{
            storeSettlement.getSid(),
            storeSettlement.getStoreID(),
            storeSettlement.getSettleYearMonth(),
            storeSettlement.getSettleYear(),
            storeSettlement.getSettleMonth(),
            storeSettlement.getStartDT(),
            storeSettlement.getEndDT(),
            storeSettlement.getSettleType(),
            storeSettlement.getSettleRate(),
            storeSettlement.getSettleFee(),
            storeSettlement.getOrderCount(),
            storeSettlement.getSettleSum(),
            storeSettlement.getPgDate(),
            storeSettlement.getPgSum(),
            storeSettlement.getPgServiceFee(),
            //storeSettlement.getPlDate(),
            storeSettlement.getPlMinFee(),
            storeSettlement.getPlServiceFee(),
            storeSettlement.getPlFinalFee()
        });
    }

    /**
     *插入更新（存在则先删除）商户结算日的结算结果
     *
     * @param storeDailySettlement
     */
    @Transactional
    public void saveStoreDailySettlement(StoreDailySettlement storeDailySettlement) {
        this.getJdbcTemplate().update("delete from store_settlement_day where STORE_ID = ? AND DATE_FORMAT(SETTLE_DATE,'%Y-%m-%d') = ?",
                new Object[]{
                        storeDailySettlement.getStoreID(),
                        storeDailySettlement.getSettleDate()
                });

        this.getJdbcTemplate().update("insert into store_settlement_day (SID,STORE_ID,SETTLE_DATE,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                " values (?,?,STR_TO_DATE(?,'%Y-%m-%d'),?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,now(),'ba',now(),'ba')", new Object[]{
                storeDailySettlement.getSid(),
                storeDailySettlement.getStoreID(),
                storeDailySettlement.getSettleDate(),
                storeDailySettlement.getSettleType(),
                storeDailySettlement.getSettleRate(),
                storeDailySettlement.getSettleFee(),
                storeDailySettlement.getOrderCount(),
                storeDailySettlement.getSettleSum(),
                storeDailySettlement.getPgDate(),
                storeDailySettlement.getPgSum(),
                storeDailySettlement.getPgServiceFee(),
                //storeSettlement.getPlDate(),
                storeDailySettlement.getPlMinFee(),
                storeDailySettlement.getPlServiceFee(),
                storeDailySettlement.getPlFinalFee()
        });
    }


    /**
     * 删除BA交易类商户的月度结算结果
     *
     * @param storeId
     * @param year
     * @param month
     */
    public Long deleteBAStoreSettlement(Long storeId, int year, int month) {
        this.getJdbcTemplate().update("delete from store_settlement_ba where STORE_ID = ? AND SETTLE_YEAR = ? AND SETTLE_MONTH = ?",
                new Object[]{
                        storeId,
                        year,
                        month
                });
        return storeId;
    }

    /**
     * 存储BA交易类商户的月度结算结果
     *
     * @param storeSettlementBA
     */
    public void saveBAStoreSettlement(StoreSettlementBA storeSettlementBA) {
        this.getJdbcTemplate().update("insert into store_settlement_ba (SID,STORE_ID,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,START_DT,END_DT,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,ORDER_COUNT_DAOSHOU,ORDER_PlAT_AMOUNT_SUM,ORDER_DAOSHOU_AMOUNT_SUM,ORDER_COUNT_DAOSHOU_REFUND,ORDER_PlAT_AMOUNT_SUM_REFUND,ORDER_DAOSHOU_AMOUNT_SUM_REFUND,CASH_SETTLE_SUM,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                "   values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,?,?,?,?,?,?,?,now(),'ba',now(),'ba')", new Object[]{
                storeSettlementBA.getSid(),
                storeSettlementBA.getStoreID(),
                storeSettlementBA.getSettleYearMonth(),
                storeSettlementBA.getSettleYear(),
                storeSettlementBA.getSettleMonth(),
                storeSettlementBA.getStartDT(),
                storeSettlementBA.getEndDT(),
                storeSettlementBA.getSettleType(),
                storeSettlementBA.getSettleRate(),
                storeSettlementBA.getSettleFee(),
                storeSettlementBA.getOrderCount(),
                storeSettlementBA.getSettleSum(),
                storeSettlementBA.getPgDate(),
                storeSettlementBA.getPgSum(),
                storeSettlementBA.getPgServiceFee(),
                //storeSettlement.getPlDate(),
                storeSettlementBA.getPlMinFee(),
                storeSettlementBA.getPlServiceFee(),
                storeSettlementBA.getPlFinalFee(),

                storeSettlementBA.getOrderCountDaoshou(),
                storeSettlementBA.getOrderPlatAmountSum(),
                storeSettlementBA.getOrderDaoshouAmountSum(),
                storeSettlementBA.getOrderCountDaoshouRefund(),
                storeSettlementBA.getOrderPlatAmountSumRefund(),
                storeSettlementBA.getOrderDaoshouAmountSumRefund(),

                storeSettlementBA.getCashSettleSum()
        });
    }

    /**
     * 存储BA交易类商户的月度结算结果(手工)
     *
     * @param storeId
     * @param year
     * @param month
     * @param storeSettlementBA
     */
    public void saveBAStoreSettlementManual(Long storeId, int year, int month, StoreSettlementBA storeSettlementBA) {
        this.getJdbcTemplate().update("delete from store_settlement_manual_ba where STORE_ID = ? AND SETTLE_YEAR = ? AND SETTLE_MONTH = ?",
                new Object[]{
                        storeId,
                        year,
                        month
                });

        this.getJdbcTemplate().update("insert into store_settlement_manual_ba (SID,STORE_ID,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,START_DT,END_DT,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,CASH_SETTLE_SUM,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                "   values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,?,now(),'ba',now(),'ba')", new Object[]{
                storeSettlementBA.getSid(),
                storeSettlementBA.getStoreID(),
                storeSettlementBA.getSettleYearMonth(),
                storeSettlementBA.getSettleYear(),
                storeSettlementBA.getSettleMonth(),
                storeSettlementBA.getStartDT(),
                storeSettlementBA.getEndDT(),
                storeSettlementBA.getSettleType(),
                storeSettlementBA.getSettleRate(),
                storeSettlementBA.getSettleFee(),
                storeSettlementBA.getOrderCount(),
                storeSettlementBA.getSettleSum(),
                storeSettlementBA.getPgDate(),
                storeSettlementBA.getPgSum(),
                storeSettlementBA.getPgServiceFee(),
                //storeSettlement.getPlDate(),
                storeSettlementBA.getPlMinFee(),
                storeSettlementBA.getPlServiceFee(),
                storeSettlementBA.getPlFinalFee(),
                storeSettlementBA.getCashSettleSum()
        });
    }

    /**
     *插入更新（存在则先删除）PG交易类商户结算日的结算结果
     *
     * @param storeDailySettlementBA
     */
    public void saveBAStoreDailySettlement(StoreDailySettlementBA storeDailySettlementBA) {
        this.getJdbcTemplate().update("delete from store_settlement_day_ba where STORE_ID = ? AND DATE_FORMAT(SETTLE_DATE,'%Y-%m-%d') = ?",
                new Object[]{
                        storeDailySettlementBA.getStoreID(),
                        storeDailySettlementBA.getSettleDate()
                });

        this.getJdbcTemplate().update("insert into store_settlement_day_ba (SID,STORE_ID,SETTLE_DATE,SETTLE_TYPE,SETTLE_RATE,SETTLE_FEE,ORDER_COUNT,SETTLE_SUM,PG_DATE,PG_SUM,PG_SERVICE_FEE,PL_DATE,PL_MIN_FEE,PL_SERVICE_FEE,PL_FINAL_FEE,ORDER_COUNT_DAOSHOU,ORDER_PlAT_AMOUNT_SUM,ORDER_DAOSHOU_AMOUNT_SUM,ORDER_COUNT_DAOSHOU_REFUND,ORDER_PlAT_AMOUNT_SUM_REFUND,ORDER_DAOSHOU_AMOUNT_SUM_REFUND,CASH_SETTLE_SUM,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                " values (?,?,STR_TO_DATE(?,'%Y-%m-%d'),?,?,?,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%d'),?,?,?,?,?,?,?,?,?,?,now(),'ba',now(),'ba')", new Object[]{
                storeDailySettlementBA.getSid(),
                storeDailySettlementBA.getStoreID(),
                storeDailySettlementBA.getSettleDate(),
                storeDailySettlementBA.getSettleType(),
                storeDailySettlementBA.getSettleRate(),
                storeDailySettlementBA.getSettleFee(),
                storeDailySettlementBA.getOrderCount(),
                storeDailySettlementBA.getSettleSum(),
                storeDailySettlementBA.getPgDate(),
                storeDailySettlementBA.getPgSum(),
                storeDailySettlementBA.getPgServiceFee(),
                //storeSettlement.getPlDate(),
                storeDailySettlementBA.getPlMinFee(),
                storeDailySettlementBA.getPlServiceFee(),
                storeDailySettlementBA.getPlFinalFee(),

                storeDailySettlementBA.getOrderCountDaoshou(),
                storeDailySettlementBA.getOrderPlatAmountSum(),
                storeDailySettlementBA.getOrderDaoshouAmountSum(),
                storeDailySettlementBA.getOrderCountDaoshouRefund(),
                storeDailySettlementBA.getOrderPlatAmountSumRefund(),
                storeDailySettlementBA.getOrderDaoshouAmountSumRefund(),

                storeDailySettlementBA.getCashSettleSum()
        });
    }

    /**
     * 查询指定ID的酒店订单信息
     *
     * @param orderId
     * @return
     */
    public HotelOrderDTO getHotelOrderInfo(Long orderId) {
        String querySQL = "select oi.order_id as id,oi.reseve_dt_from,oi.reseve_dt_to,(SELECT distinct oit.sku_id FROM order_item oit where oit.order_id = oi.order_id) AS sku_id from order_info oi where oi.order_id = ?";
        final List<HotelOrderDTO> orderList = this.getJdbcTemplate().query(
                querySQL,
                new Object[]{orderId},
                new RowMapper<HotelOrderDTO>() {
                    @Override
                    public HotelOrderDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                        return HotelOrderDTO.builder()
                                .id(resultSet.getLong("id"))
                                .reseveDtfrom(resultSet.getString("reseve_dt_from"))
                                .reseveDtto(resultSet.getString("reseve_dt_to"))
                                .prodSkuId(resultSet.getLong("sku_id"))
                                .build();
                    }
                });
        return orderList.isEmpty() ? null : orderList.get(0);
    }

    /**
     * 查询指定酒店商户、指定房间prodSKU、指定日期期间的已经被订单确认或订单完成的明细
     *
     * @param params
     * @return
     */
    /*
        SELECT
        o.id,o.store_id,oit.sku_id,oit.sku_nm_chn FROM `order` o, order_info oi,order_item oit
        WHERE
        o.id = oi.order_id
        AND o.id = oit.order_id
        AND o.store_id=635790301684634630
        AND o.`status` IN (5,9)
        AND oit.sku_id = 706904780187702277
        AND ((oi.reseve_dt_from <= '2020-01-08' AND oi.reseve_dt_to >= '2020-01-08') OR (oi.reseve_dt_from <= '2020-01-11' AND oi.reseve_dt_to >= '2020-01-11'))
        AND oit.sku_nm_chn >= '2020-01-08'
        AND oit.sku_nm_chn < '2020-01-11';
     */
    public List<HotelOrderDTO> getHotelOrderDetailList(Map<String, Object> params) {
        StringBuilder qrySQL = new StringBuilder("SELECT ");
        qrySQL.append("o.id,o.store_id,oit.sku_id as prodSkuId,oit.sku_nm_chn as reserveDate FROM `order` o, order_info oi,order_item oit ")
                .append(" where ")
                .append("o.id = oi.order_id ")
                .append("AND o.id = oit.order_id ")
                .append("AND o.store_id=:storeId ")
                .append("AND o.`status` IN (5,9) ")
                .append("AND oit.sku_id = :skuId ")
                .append("AND ((oi.reseve_dt_from <= :startDate AND oi.reseve_dt_to >= :startDate) OR (oi.reseve_dt_from <= :endDate AND oi.reseve_dt_to >= :endDate)) ")
                .append("AND oit.sku_nm_chn >= :startDate ")
                .append("AND oit.sku_nm_chn < :endDate ");
        logger.info("查询指定商户、指定prodSKU、指定日期期间的预订明细SQL:::" + qrySQL.toString());
        List<HotelOrderDTO> resultList = this.getNamedParameterJdbcTemplate().query(qrySQL.toString(), params, new BeanPropertyRowMapper<>(HotelOrderDTO.class));
        return resultList;
    }
}
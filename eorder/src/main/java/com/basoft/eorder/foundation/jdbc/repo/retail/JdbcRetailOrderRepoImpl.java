package com.basoft.eorder.foundation.jdbc.repo.retail;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.domain.retail.RetailOrderServiceRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.command.order.retail.DealRetailOrder;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Transactional
public class JdbcRetailOrderRepoImpl extends BaseRepository implements RetailOrderRepository {
    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private RetailOrderEventRepository retailOrderEventRepository;

    @Autowired
    private RetailOrderServiceRepository retailOrderServiceRepository;

    /*************************************Wechat H5用户下单全过程-START*******************************************/
    /**
     * 1、保存订单信息到订单临时表
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public Order saveRetailOrderTemp(Order order) {
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
     * 1、保存零售业务订单信息到订单临时表,同时扣减零售产品库存
     *
     * @param order
     * @param toCheckInvProdSkuIdList
     * @param toUpdateInvMap
     * @return
     * @see public Order saveOrderTemp(Order order)
     */
    @Transactional
    public Order saveRetailOrderTempWithInventory(Order order
            , List<Long> toCheckInvProdSkuIdList, Map<Long, Long> toUpdateInvMap) {
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
        reduceRetailInventory(order.getStoreId(), toCheckInvProdSkuIdList, toUpdateInvMap);

        return order;
    }

    /**
     * 1-1、insert order to temp table
     *
     * @param order
     */
    private void insertOrderTemp(Order order) {
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
                "    , shipping_mode\n" +
                "    , shipping_mode_name_chn\n" +
                "    , shipping_mode_name_kor\n" +
                "    , shipping_mode_name_eng\n" +
                "    , shipping_addr_detail\n" +
                "    , shipping_addr_country\n" +
                "    , shipping_weight\n" +
                "    , shipping_cost\n" +
                "    , shipping_cost_rule\n" +
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
                "    , :shippingMode\n" +
                "    , :shippingModeNameChn\n" +
                "    , :shippingModeNameKor\n" +
                "    , :shippingModeNameEng\n" +
                "    , :shippingAddrDetail\n" +
                "    , :shippingAddrCountry\n" +
                "    , :shippingWeight\n" +
                "    , :shippingCost\n" +
                "    , :shippingCostRule\n" +
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
     * 1-4 零售业务产品库存削减
     *
     * @param storeId
     * @param toCheckInvProdSkuIdList
     * @param toUpdateInvMap
     */
    private void reduceRetailInventory(Long storeId, List<Long> toCheckInvProdSkuIdList
            , Map<Long, Long> toUpdateInvMap) {
        this.getJdbcTemplate().batchUpdate("UPDATE INVENTORY_RETAIL SET INV_BALANCE = INV_BALANCE - ?, INV_SOLD = INV_SOLD + ? WHERE STORE_ID = ? AND PROD_SKU_ID = ?", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        Long prodSkuId = toCheckInvProdSkuIdList.get(i);
                        preparedStatement.setLong(1, toUpdateInvMap.get(prodSkuId));
                        preparedStatement.setLong(2, toUpdateInvMap.get(prodSkuId));
                        preparedStatement.setLong(3, storeId);
                        preparedStatement.setLong(4, prodSkuId);
                    }

                    @Override
                    public int getBatchSize() {
                        return toCheckInvProdSkuIdList.size();
                    }
                }
        );
    }


    /**
     * 2、【回调】调用微信支付后，更新临时订单状态
     *
     * @param order
     * @return
     * @see JdbcOrderRepoImpl
     */
    /*public int updateOrderTemp(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order_temp` set status = :status, prepay_id = :prepayId, updated = current_timestamp() where id = :id ", new BeanPropertySqlParameterSource(order));
    }*/

    /**
     * 3-1、【回调】生成正式订单（支付成功后的订单）时查询临时订单信息
     *
     * @param id
     * @return
     * @see JdbcOrderRepoImpl
     */
    //public Order getOrderPay(Long id) {

    /**
     * 3-2、【回调】插入微信支付成功后的OrderPay信息
     *
     * @param pay
     * @see JdbcOrderRepoImpl
     */
    // public void insertOrderPay(OrderPay pay) {

    /**
     * 3-3、【回调】支付成功并响应微信支付平台后生成正式的订单
     *
     * @param order
     * @return
     * @see JdbcOrderRepoImpl
     */
    //@Transactional
    //public Order saveOrder(Order order) {

    /**
     * 存储退款原因并且将订单状态变为6（退款进行中）
     *
     * @param dealRetailOrder
     * @return
     */
    @Transactional
    @Override
    public int updateRetailOrderCancelReason(DealRetailOrder dealRetailOrder) {
        // 更新订单状态为6
        int o = this.getNamedParameterJdbcTemplate()
                .update("update `order` set status = 6, updated = current_timestamp() where id = :orderId "
                        , new BeanPropertySqlParameterSource(dealRetailOrder));

        // 存储退款原因
        int oi = this.getNamedParameterJdbcTemplate()
                .update("update order_info set cancel_reason = :cancelReason where order_id=:orderId"
                        , new BeanPropertySqlParameterSource(dealRetailOrder));

        return o + oi;
    }

    /**
     * 更新零售业务订单退款失败
     *
     * @param order
     */
    public int updateRetailOrderTo8(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, STATUS_8_FROM = :status8From , updated = current_timestamp() where id = :id ", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 根据订单编号更新订单状态
     *
     * @param order
     * @return
     */
    public int updateOrderStatus(Order order) {
        return this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp() where id = :id ", new BeanPropertySqlParameterSource(order));
    }

    /**
     * 零售业务接单：更新订单状态为5并记录接单事件6
     *
     * @param saveRetailOrderEvent
     * @return 1-正常 0-失败 异常-失败
     */
    @Transactional
    public int acceptRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent) {
        // 接单：订单状态为4，并且订单变更状态为0
        int o = 0;
        try{
            Order order = new Order.Builder()
                    .id(saveRetailOrderEvent.getOrderId())
                    .status(Order.Status.ACEEPT)
                    .build();

            o = this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp() where id = :id and status = 4 and CHANGE_STATUS = 0", new BeanPropertySqlParameterSource(order));
        } catch (Exception e){
            log.error("【零售业务接单】更新订单状态异常，异常信息：：：：{}", e.getMessage());

            e.printStackTrace();

            // 异常回滚
            throw new RuntimeException();
        }

        // 记录接单事件6
        if(o > 0){
            try{
                saveRetailOrderEvent.setEventType(CommonConstants.RETAIL_ORDER_EVENT_TYPE_6);
                int e = retailOrderEventRepository.saveRetailOrderEvent(saveRetailOrderEvent.build());
                if(e <= 0){
                    // 事件未记录回滚
                    throw new RuntimeException();
                }
            } catch (Exception e){
                log.error("【零售业务接单】记录接单事件异常，异常信息：：：：{}", e.getMessage());
                e.printStackTrace();
                // 事件记录异常回滚
                throw new RuntimeException();
            }
        }

        return o;
    }

    /**
     * 零售业务订单发货：更新订单状态为10并记录发货事件11
     *
     * @param saveRetailOrderEvent
     * @return 1-正常 0-失败 异常-失败
     */
    @Transactional
    public int sendRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent) {
        // 发货：订单状态为5，并且订单变更状态为0
        int o = 0;
        try{
            Order order = new Order.Builder()
                    .id(saveRetailOrderEvent.getOrderId())
                    .status(Order.Status.SHIPPING_REDY)
                    .build();

            o = this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp() where id = :id and status = 5 and CHANGE_STATUS = 0", new BeanPropertySqlParameterSource(order));
        } catch (Exception e){
            log.error("【零售业务订单发货】更新订单状态异常，异常信息：：：：{}", e.getMessage());

            e.printStackTrace();

            // 异常回滚
            throw new RuntimeException();
        }

        // 记录发货事件11
        if(o > 0){
            try{
                saveRetailOrderEvent.setEventType(CommonConstants.RETAIL_ORDER_EVENT_TYPE_11);
                int e = retailOrderEventRepository.saveRetailOrderEvent(saveRetailOrderEvent.build());
                if(e <= 0){
                    // 事件未记录回滚
                    throw new RuntimeException();
                }
            } catch (Exception e){
                log.error("【零售业务订单发货】记录发货事件异常，异常信息：：：：{}", e.getMessage());
                e.printStackTrace();
                // 事件记录异常回滚
                throw new RuntimeException();
            }
        }

        return o;
    }

    /**
     * 零售业务订单-确认收货：更新订单状态为9并记录确认收货事件
     *
     * @param saveRetailOrderEvent
     * @return 1-正常 0-失败 异常-失败
     */
    @Transactional
    public int endRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent) {
        // 接单：订单状态为10，并且订单变更状态为0
        int o = 0;
        try{
            Order order = new Order.Builder()
                    .id(saveRetailOrderEvent.getOrderId())
                    .status(Order.Status.COMPLETE)
                    .build();

            o = this.getNamedParameterJdbcTemplate().update("update `order` set status = :status, updated = current_timestamp() where id = :id and status = 10 and CHANGE_STATUS = 0", new BeanPropertySqlParameterSource(order));
        } catch (Exception e){
            log.error("【零售业务订单确认收货】更新订单状态异常，异常信息：：：：{}", e.getMessage());
            e.printStackTrace();
            // 异常回滚
            throw new RuntimeException();
        }

        // 记录确认收货事件12
        if(o > 0){
            try{
                saveRetailOrderEvent.setEventType(CommonConstants.RETAIL_ORDER_EVENT_TYPE_12);
                int e = retailOrderEventRepository.saveRetailOrderEvent(saveRetailOrderEvent.build());
                if(e <= 0){
                    // 事件未记录回滚
                    throw new RuntimeException();
                }
            } catch (Exception e){
                log.error("【零售业务订单确认收货】记录确认收货事件异常，异常信息：：：：{}", e.getMessage());
                e.printStackTrace();
                // 事件记录异常回滚
                throw new RuntimeException();
            }
        }

        return o;
    }



    @Transactional
    public int endRetailOrderbatch(List<SaveRetailOrderEvent> saveRetailOrderEvents) {
        int o = 0;//订单修改成功条数
        try {
            String upOrderSql = "update `order` set status = ?, updated = current_timestamp() " +
                    "where id = ? and status = 10 and CHANGE_STATUS = 0";

            int arr[] = this.getJdbcTemplate().batchUpdate(upOrderSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    SaveRetailOrderEvent event = saveRetailOrderEvents.get(i);
                    preparedStatement.setInt(1, Order.Status.COMPLETE.code());
                    preparedStatement.setLong(2, event.getOrderId());
                }

                @Override
                public int getBatchSize() {
                    return saveRetailOrderEvents.size();
                }
            });

            for (int i = arr.length - 1; i >=0; i--) {
                o += arr[i];
                if (arr[i] == 0) {
                    saveRetailOrderEvents.remove(i);//没修改成功的删掉 避免插入事件表
                }
            }
        } catch (Exception e) {
            log.error("【零售业务订单确认收货】更新订单状态异常，异常信息：：：：{}", e.getMessage());
            e.printStackTrace();
            // 异常回滚
            throw new RuntimeException();
        }

        // 记录确认收货事件12
        if (o > 0) {
            List<RetailOrderEvent> events = saveRetailOrderEvents.stream().map(saveRetailOrderEvent -> {
                saveRetailOrderEvent.setEventType(CommonConstants.RETAIL_ORDER_EVENT_TYPE_12);
                return saveRetailOrderEvent.build();
            }).collect(Collectors.toList());
            try {
                int e = retailOrderEventRepository.batchSaveRetailOrderEvent(events);
                if (e <= 0) {
                    // 事件未记录回滚
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                log.error("【零售业务订单确认收货】记录确认收货事件异常，异常信息：：：：{}", e.getMessage());
                e.printStackTrace();
                // 事件记录异常回滚
                throw new RuntimeException();
            }
        }


        return 0;
    }




    /**
     * 零售业务订单-退换货/售后
     *
     * @param retailOrderService
     * @return
     */
    @Transactional
    public int retailOrderService(RetailOrderService retailOrderService, Order order) {
        // 退换货/售后：订单状态为9，即完成状态，并且订单变更状态为0
        int o = 0;
        try{
            o = this.getNamedParameterJdbcTemplate().update("update `order` set CHANGE_STATUS = 2, updated = current_timestamp() where id = :id and status = 9 and CHANGE_STATUS = 0", new BeanPropertySqlParameterSource(order));
        } catch (Exception e){
            log.error("【零售业务订单退换货/售后】更新订单变更状态异常，异常信息：：：：{}", e.getMessage());
            e.printStackTrace();
            // 异常回滚
            throw new RuntimeException();
        }

        // 记录售后申请信息
        if(o > 0){
            try{
                // 完善售后申请信息
                retailOrderService.setServCode(uidGenerator.generate(BusinessTypeEnum.RETAIL_ORDER_SERVICE));
                retailOrderService.setStoreId(order.getStoreId());
                retailOrderService.setApplyAmount(order.getPaymentAmount());
                retailOrderService.setServStatus(1);
                retailOrderService.setApplyDeliveryMode(2);
                int e = retailOrderServiceRepository.saveRetailOrderService(retailOrderService);
                if(e <= 0){
                    // 事件未记录回滚
                    throw new RuntimeException();
                }
            } catch (Exception e){
                log.error("【零售业务订单退换货/售后】记录售后记录异常，异常信息：：：：{}", e.getMessage());
                e.printStackTrace();
                // 事件记录异常回滚
                throw new RuntimeException();
            }
        }

        return o;
    }
}
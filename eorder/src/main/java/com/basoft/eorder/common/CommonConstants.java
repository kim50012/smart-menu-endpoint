package com.basoft.eorder.common;

public class CommonConstants{
    // 1-餐厅 2-医院 3-购物 4-酒店
    public static final String BIZ_ORDERING_STRING = "1";
    public static final String BIZ_HOSPITAL_STRING = "2";
    public static final String BIZ_SHOPPING_STRING = "3";
    public static final String BIZ_HOTEL_STRING = "4";

    public static final int BIZ_ORDERING_INT = 1;
    public static final int BIZ_HOSPITAL_INT = 2;
    public static final int BIZ_SHOPPING_INT = 3;
    public static final int BIZ_HOTEL_INT = 4;

    //retail订单类型
    public static final String SHIPPINGTYPE_SELF = "2"; //自提
    public static final String SHIPPINGTYPE_POST_KOREA = "4"; //邮寄到韩国
    public static final String SHIPPINGTYPE_POST_CHINA = "5";  //邮寄到中国

    //Category Function Type
    public static final int CATEGORY_FUNCTION_TYPE_PRODUCT = 1;
    public static final int CATEGORY_FUNCTION_TYPE_TAG = 2;

    //Category Manage Type
    public static final int CATEGORY_MANAGE_TYPE_ADMIN = 1;
    public static final int CATEGORY_MANAGE_TYPE_MANAGER = 2;

    // 标签的管理类型
    public static final String TAG_MANAGE_ADMIN_CMS = "1";
    public static final String TAG_MANAGE_MANAGER_CMS = "2";
    public static final String TAG_MANAGE_ALL_CMS = "3";

    // 商户的收费类型
    // 百分比
    public static final int STORE_CHARGE_TYPE_RATE = 1;
    // 百分比或最小服务费
    public static final int STORE_CHARGE_TYPE_RATE_MIN_FEE = 2;
    // 按月
    public static final int STORE_CHARGE_TYPE_MONTHLY = 3;

    // 商户月度结算操作类型 zhengshi-存到正式的商户结算表 ceshi-存到手工的商户结算表
    public static final String STORE_SETTLE_MANUAL_ZHENGSHI = "ZS";
    // 按月
    public static final String STORE_SETTLE_MANUAL_CESHI = "CS";


    /**
     * 酒店库存操作锁
     * （1）具体到酒店商户，后续并发需要可以加锁到酒店商户的sku上，锁粒度越小，能更好的提供并发。
     *      完整锁名称为HIL_4_storeId
     * （2）下单和恢复采用同一把锁
     * （3）HOTEL_INVENTORY_LOCK_4_，简称HIL_4_
     */
    public static final String HOTEL_INVENTORY_LOCK = "HIL_4_";

    // 酒店业务：库存恢复类型 auto-定时任务，下单不支付类临时订单  refund-退款库存恢复
    public static final String HOTEL_INVENTORY_RECOVER_AUTO = "auto";
    public static final String HOTEL_INVENTORY_RECOVER_REFUND = "refund";

    // 零售业务：库存恢复类型 auto-定时任务，下单不支付类临时订单  refund-退款库存恢复
    public static final String RETAIL_INVENTORY_RECOVER_AUTO = "auto";
    public static final String RETAIL_INVENTORY_RECOVER_REFUND = "refund";

    // 订单类型-正常商品订单
    public static final int ORDER_TYPE_NORMAL = 1;
    // 订单类型-押金商品订单
    public static final int ORDER_TYPE_PLEDGE = 2;

    // 代理商结算默认费率
    // public static final double AGENT_DEFAULT_RATE= 0.33;
    public static final String AGENT_DEFAULT_RATE= "0.33";
    public static final int AGENT_DEFAULT_PERSENT= 33;

    // 代理商结算税后比率
    public static final String AGENT_VAT_RATE= "0.9";

    // 交易金结算类型 PG-韩亚银行直接结算交易金给商户，BA-韩亚银行结算交易金给BA，BA结算交易金给商户
    public static final String CASH_SETTLE_TYPE_PG = "PG";
    public static final String CASH_SETTLE_TYPE_BA = "BA";


    /**
     * Retail零售业务库存操作锁
     * （1）具体到零售商户，后续并发需要可以加锁到零售商户的sku上，锁粒度越小，能更好的提供并发。
     *      完整锁名称为RIL_3_storeId
     * （2）下单和恢复采用同一把锁
     * （3）RETAIL_INVENTORY_LOCK，简称RIL_3_
     */
    public static final String RETAIL_INVENTORY_LOCK = "RIL_3_";

    /**
     * 零售业务订单事件定义
     * 1-支付成功
     *
     * 2-已退款
     * 说明：商户发起退款，该订单完结
     *
     * 3-申请退款
     * 说明：支付成功后立即申请退款，此时必须对申请退款做出审核，拒绝后可以接单，同意后该订单完结
     * 4-商户审核退款申请
     * 说明：通过-平台退款；不通过，继续接单
     * 5-已退款
     *
     * 6-商户已接单，正在为您准备商品
     *
     * 7-已退款
     * 说明：商户发起退款，该订单完结
     *
     * 8-申请退款
     * 说明：商户接单后用户发起申请退款，此时必须对申请退款做出审核，拒绝后可以发货，同意后该订单完结
     * 9-商户审核退款申请
     * 10-已退款
     *
     * 11-已发货
     * 12-确认收货
     * 13-退货换货/售后
     */
    public static final int RETAIL_ORDER_EVENT_TYPE_1 = 1;
    public static final int RETAIL_ORDER_EVENT_TYPE_2 = 2;
    public static final int RETAIL_ORDER_EVENT_TYPE_3 = 3;
    public static final int RETAIL_ORDER_EVENT_TYPE_4 = 4;
    public static final int RETAIL_ORDER_EVENT_TYPE_5 = 5;
    public static final int RETAIL_ORDER_EVENT_TYPE_6 = 6;
    public static final int RETAIL_ORDER_EVENT_TYPE_7 = 7;
    public static final int RETAIL_ORDER_EVENT_TYPE_8 = 8;
    public static final int RETAIL_ORDER_EVENT_TYPE_9 = 9;
    public static final int RETAIL_ORDER_EVENT_TYPE_10 = 10;
    public static final int RETAIL_ORDER_EVENT_TYPE_11 = 11;
    public static final int RETAIL_ORDER_EVENT_TYPE_12 = 12;
    public static final int RETAIL_ORDER_EVENT_TYPE_13 = 13;

}

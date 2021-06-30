package com.basoft.eorder.interfaces.query.ghraphql;

import graphql.Scalars;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLUnionType;
import graphql.schema.TypeResolver;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

class GraphQLObjects {


    final static GraphQLObjectType ORDER_ITEM_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("OrderItem")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodNmKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodNmChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("skuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("skuNmKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("skuNmChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceCny").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updated").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("weight").type(Scalars.GraphQLBigDecimal))//重量
            /*.field(newFieldDefinition().name("prodNmKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodNmChn").type(Scalars.GraphQLString))*/
            .build();

    final static GraphQLObjectType ORDER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Order")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("tableId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("amount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("paymentAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("discountAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("payAmtCny").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("payAmtRmb").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("payAmtUsd").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("krwUsdRate").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("usdCnyRate").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("status8From").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("changeStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("buyerMemo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("customerId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updated").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("payDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("cancelDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("payDts").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("orderType").type(Scalars.GraphQLInt))
            
            .field(newFieldDefinition().name("retailType").type(Scalars.GraphQLInt))
            
            .field(newFieldDefinition().name("storeNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("managerId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("logoUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("tableNum").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("tableTag").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("numTag").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("openId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("itemList").type(GraphQLList.list(ORDER_ITEM_GRAPHQL_TYPE)))

            .field(newFieldDefinition().name("custNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("countryNo").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("reseveDtfrom").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("reseveDtto").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("reseveTime").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("confirmDtfrom").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("confirmDtto").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("confirmTime").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("numPersons").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("shippingType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("shippingAddr").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("shippingAddrNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingMode").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("shippingModeNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingModeNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingModeNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingAddrDetail").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingAddrCountry").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingWeight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("shippingCost").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("shippingCostRule").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingCmt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("diningPlace").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("diningTime").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("cmt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("reseveConfirmtime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custNo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custNmEn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmLast").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmFirst").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmLastEn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmFirstEn").type(Scalars.GraphQLString))
            
            .build();

    final static GraphQLObjectType ORDER_PAGING_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("OrderPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(ORDER_GRAPHQL_TYPE)))
            .build();


    final static GraphQLObjectType ORDER_SUM_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("OrderAmountSumStats")
            .field(newFieldDefinition().name("date").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("paySumAmount").type(Scalars.GraphQLBigDecimal))
            .build();

    final static GraphQLObjectType ORDER_SUM_STORE_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("OrderSumStoreRankStats")
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("paySumAmount").type(Scalars.GraphQLBigDecimal))
            .build();

    final static GraphQLObjectType RETAIL_ORDER_STATUS_COUNT_GRAPHQL = GraphQLObjectType.newObject()
            .name("RetailOrderStatusCount")
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("servStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("count").type(Scalars.GraphQLInt))
            .build();

    final static GraphQLObjectType RETAIL_ORDER_SERVICE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("RetailOrderService")
            .field(newFieldDefinition().name("servId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("servCode").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("servType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("applyCount").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("applyAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("applyDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("applyImages").type(GraphQLList.list(Scalars.GraphQLString)))
            .field(newFieldDefinition().name("applyDeliveryMode").type(Scalars.GraphQLInt))//1-上门取件 2-自助快递到商户，默认为自助快递到商户，地址通过客服获取
            .field(newFieldDefinition().name("applyLinker").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("applyMobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("applyAddress").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("applyTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("acceptor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("acceptTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("auditResult").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("auditDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("auditRefundType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("auditRefundAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("amount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("paymentAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("auditor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("auditTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingAddrDetail").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingAddrCountry").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("countryNo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingWeight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("shippingCost").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("shippingCostRule").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("shippingCmt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("servStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("itemList").type(GraphQLList.list(ORDER_ITEM_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType RETAIL_ORDER_SERVICE_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("RetailOrderServicePagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(RETAIL_ORDER_SERVICE_GRAPHQL_TYPE)))
            .build();



    final static GraphQLObjectType PRODUCT_SALE_RANK_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductSaleRankStats")
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("paySumAmount").type(Scalars.GraphQLBigDecimal))
            .build();

    final static GraphQLObjectType CATEGORY_SALE_RANK_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("CategorySaleRankStats")
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("paySumAmount").type(Scalars.GraphQLBigDecimal))
            .build();

    final static GraphQLObjectType PAYAMOUNT_DATE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("PayAmountAndDate")
            .field(newFieldDefinition().name("payAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("agtSumAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("date").type(Scalars.GraphQLString))
            .build();
    final static GraphQLObjectType QTY_DATE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("QtyAndDate")
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("date").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType ORDER_BY_DATE_SALE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("OrderByDateSaleStats")
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("payAmountSum").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("qtyList").type(GraphQLList.list(QTY_DATE_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("payAmountList").type(GraphQLList.list(PAYAMOUNT_DATE_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("agtAmountList").type(GraphQLList.list(PAYAMOUNT_DATE_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("dateList").type(GraphQLList.list(Scalars.GraphQLString)))
            .build();


    final static GraphQLObjectType USER_ORDER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("UserOrder")
            .field(newFieldDefinition().name("openId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nickName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("headImgUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("custNo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("laterTrainDate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("laterStore").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("bindTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("subscribeTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType USER_ORDER_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("UserOrderPagingList")
            .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("vatFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(USER_ORDER_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType SETTLE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("SettleSum")
            .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("closingMonths").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("payDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("pgPlanDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("payFrDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("payToDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("pgFrDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("pgToDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("baReciveDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("baSureReciveDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("pgAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("payCnt").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("amount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("settleType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("settleTypeStr").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isPay").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("pgFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("plMinFee").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("serviceFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("finalFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("serviceFeeSum").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("cloStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("chargeRate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chargeFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("chargeType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("itemList").type(GraphQLList.list(ORDER_ITEM_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType SETTLES_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("SettlesPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(SETTLE_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType BA_SETTLE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("BaSettle")
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("closingMonths").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("settleDate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgPlanDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payFrDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payToDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgFrDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgToDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("orderCount").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("amount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("settleType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("isPay").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("plFinalFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("settleSum").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("serviceFeeSum").type(Scalars.GraphQLBigDecimal))//pg加平台
        .field(newFieldDefinition().name("cashSettleSum").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("cloStatus").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("settleRate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("stBankName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("stBankAcc").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("stBankAccName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("chargeFee").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("chargeType").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType BA_SETTLE_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("BaSettlesPagingList")
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(BA_SETTLE_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType BA_SETTLE_DTL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("BaSettleDtl")
        .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("amount").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("completeDate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgPlanDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("pgAmount").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("pgFee").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("plFinalFee").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("serviceFeeSum").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("settleAmount").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("orderCount").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("reseveDtFrom").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("settleRate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("itemList").type(GraphQLList.list(ORDER_ITEM_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType BA_SETTLE_DTL_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("BaSettleDtlPagingList")
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(BA_SETTLE_DTL_GRAPHQL_TYPE)))
        .build();

    /**
     * 产品规格（产品独立规格）的项目的定义表
     * 对应表product_alone_standard_item
     */
    final static GraphQLObjectType PRODUCT_ALONE_STANDARD_ITEM_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductAloneStandardItem")
            .field(newFieldDefinition().name("itemId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("stdId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("itemNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("disOrder").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("itemImage").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemStatus").type(Scalars.GraphQLInt))
            .build();

    /**
     * 产品规格（产品独立规格）的定义表，该定义基于商户的产品，属于某个产品的规格定义。
     * 对应表product_alone_standard
     */
    final static GraphQLObjectType PRODUCT_ALONE_STANDARD_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductAloneStandard")
            .field(newFieldDefinition().name("stdId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("stdNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("disOrder").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("stdImage").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("standardItemList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_ITEM_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType PRODUCT_ALONE_STANDARD_TEMPLATE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductAloneStandardTemplate")
            .field(newFieldDefinition().name("tId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("tNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("tNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("tNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("tStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("desKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("aloneStandardList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType PRODUCT_ALONE_STANDARD_TEMPLATE_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("ProductAloneStandardTemplatePagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_TEMPLATE_GRAPHQL_TYPE)))
            .build();


    /**
     * TODO
     */
    final static GraphQLObjectType STANDARD_ITEM_NAME_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("StandardItemName")
            .field(newFieldDefinition().name("disOrder").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("itemNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameEng").type(Scalars.GraphQLString))
            .build();

    /**
     * TODO
     */
    final static GraphQLObjectType SKU_STANDARD_MAP_TYPE = GraphQLObjectType.newObject()
            .name("SkuStandardMap")
            .field(newFieldDefinition().name("skuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("baseExtend").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("prdStandardId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("chnName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prdStandardNameVal").type(Scalars.GraphQLString))
            .build();

    /**
     * 产品SKU GraphQLObjectType
     */
    final static GraphQLObjectType PRODUCT_SKU_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductSku")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("weight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceSettle").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceWeekend").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("disOrder").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("mainImageUrl").type(Scalars.GraphQLString))

            // 经查证前后端都无用，暂留
            .field(newFieldDefinition().name("optionList").type(GraphQLList.list(SKU_STANDARD_MAP_TYPE)))

            // 该sku的规格组合明细（以规格项列表形式呈现：STANDARD_ITEM_TYPE）
            .field(newFieldDefinition().name("standardItemList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_ITEM_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType RETAIL_PRODUCT_SKU_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("RetailProductSku")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("priceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceSettle").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("itemNames").type(GraphQLList.list(STANDARD_ITEM_NAME_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType RETAIL_PRODUCT_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("RetailProduct")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("weight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("desKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailChnDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("categoryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("recommend").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("defaultPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prdGroupId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("fileId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSysName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSize").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("fileUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileOriginalName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mainUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isStandard").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("subImageUrl").type(GraphQLList.list(Scalars.GraphQLString)))
            .field(newFieldDefinition().name("aloneStandardList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("psdList").type(GraphQLList.list(PRODUCT_SKU_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType STAND_AND_ITEM_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("standAndItem")
            .field(newFieldDefinition().name("invId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("invBalance").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("stdItemNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdItemNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdItemNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdItemNameEng").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType INVENTORY_RETAIL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("InventoryRetail")
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mainUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isStandard").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("standardAndItems").type(GraphQLList.list(STAND_AND_ITEM_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType INVENTORY_RETAIL_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("InventoryRetailPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(INVENTORY_RETAIL_GRAPHQL_TYPE)))
            .build();

    /**
     * @author woonill
     * ProducGroupDTO -> GraphQLObject
     */
    final static GraphQLObjectType PRODUCT_GROUP_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductGroup")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .build();

    /**
     * @author liminzhe
     * ProductGroupMapDTO -> GraphQLObject
     */
    final static GraphQLObjectType PRD_GRP_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductGroupMap")
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prdGroupId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailChnDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("categoryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("recommend").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("defaultPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSysName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSize").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("fileUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileOriginalName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mainUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("subImageUrl").type(GraphQLList.list(Scalars.GraphQLString)))
            .field(newFieldDefinition().name("psdList").type(GraphQLList.list(PRODUCT_SKU_GRAPHQL_TYPE)))
            .build();

    /**
     * 产品图片 GraphQLObjectType
     * ProductImageDTO -> GraphQLObject
     */
    final static GraphQLObjectType PRODUCT_IMAGE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("productImage")
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("imageUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("imageType").type(Scalars.GraphQLInt))
            .build();

    /**
     * 零售商户产品列表查询：产品sku的规格组合明细
     * Retail改造；用户端查询
     * 20200415
     */
    final static GraphQLObjectType PRODUCT_SKU_ALONE_STANDARD_MAP_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ProductSkuAloneStandardMap")
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))

            .field(newFieldDefinition().name("productSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("standardId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("standardItemId").type(Scalars.GraphQLLong))

            .field(newFieldDefinition().name("stdNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stdStatus").type(Scalars.GraphQLInt))

            .field(newFieldDefinition().name("itemNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("itemStatus").type(Scalars.GraphQLInt))
            .build();

    /**
     * 零售商户产品列表查询：产品sku信息
     * Retail改造；用户端查询
     * 20200415
     */
    final static GraphQLObjectType H5_RETAIL_PRODUCT_SKU_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("H5RetailProductSku")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("priceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceSettle").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceWeekend").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invCount").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            // 该sku的规格组合明细（产品SKU的规格组合明细表product_sku_alone_standard）
            .field(newFieldDefinition().name("combinationDetails").type(GraphQLList.list(PRODUCT_SKU_ALONE_STANDARD_MAP_GRAPHQL_TYPE)))
            .build();

    /**
     * 零售商户产品列表查询（产品信息，产品sku信息，产品sku库存信息，产品规格信息）
     * Retail改造；用户端查询
     * 20200415
     */
    final static GraphQLObjectType RETAIL_PRD_GRP_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("RetailProductGroupMap")
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prdGroupId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("weight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("detailDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailChnDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("categoryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("recommend").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isStandard").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isInventory").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("defaultSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("defaultSkuPriceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("defaultSkuPriceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("defaultSkuDiscPriceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("defaultSkuDiscPriceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("defaultSkuIsInv").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("defaultSkuInv").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("skuCount").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSysName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSize").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("fileUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileOriginalName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mainImageUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("subImageUrl").type(Scalars.GraphQLString))

            // 该产品的图片列表
            .field(newFieldDefinition().name("imageList").type(GraphQLList.list(PRODUCT_IMAGE_GRAPHQL_TYPE)))

            // 该产品的sku列表（产品sku信息，产品sku库存信息）
            .field(newFieldDefinition().name("skuList").type(GraphQLList.list(H5_RETAIL_PRODUCT_SKU_GRAPHQL_TYPE)))

            // 该产品的规格定义列表
            .field(newFieldDefinition().name("stdList").type(GraphQLList.list(PRODUCT_ALONE_STANDARD_GRAPHQL_TYPE)))

            .build();

    /**
     * @author woonill
     * ProducDTO -> GraphQLObject
     */
    final static GraphQLObjectType PRODUCT_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Product")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("weight").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("desChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailChnDesc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("categoryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useDefault").type(Scalars.GraphQLBoolean))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("recommend").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("defaultPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("minPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prdGroupId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("fileId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSysName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileSize").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("fileUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("fileOriginalName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mainUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("updateTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("subImageUrl").type(GraphQLList.list(Scalars.GraphQLString)))
            .field(newFieldDefinition().name("psdList").type(GraphQLList.list(PRODUCT_SKU_GRAPHQL_TYPE)))
            .build();


    /**
     * @author liminzhe
     * UserDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType USER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("User")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("account").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("password").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("email").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType USER_IN_MANAGER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("UserInManager")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("account").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("password").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("email").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("accountType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("bizType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("accountRole").type(Scalars.GraphQLInt))
            .build();

    final static GraphQLObjectType USER_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("UserPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(USER_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType USER_IN_MANAGER_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("UserInManagerPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(USER_IN_MANAGER_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType PRODUCT_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("productPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(PRODUCT_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType PRODUCT_GROUP_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("productGroupPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(PRODUCT_GROUP_GRAPHQL_TYPE)))
            .build();


    /**
     * @author liminzhe
     * BannerDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType BANNER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Banner")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("imagePath").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .build();

    /**
     * @author liminzhe
     * BannerDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType STORE_TABLE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("StoreTable")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("number").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("tag").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("numberStr").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("qrCodeImagePath").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("maxSeat").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("qrcodeImageUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isSilent").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .build();


    final static GraphQLObjectType CATEGORY_ID_TYPE = GraphQLObjectType.newObject()
            .name("Category")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            // 20190807添加标签其他属性
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chnName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("type").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("categoryType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("manageType").type(Scalars.GraphQLInt))
            .build();

    final static GraphQLObjectType STORE_EXTEND_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("StoreExtend")
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("fdGroupNm").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("fdGroupId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("fdName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("fdId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("fdValName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("fdValCode").type(Scalars.GraphQLString))
        .build();

    final static GraphQLObjectType STORE_ATTACH_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("StoreAttach")
        .field(newFieldDefinition().name("contentUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("displayOrder").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("isDisplay").type(Scalars.GraphQLBoolean))
        .field(newFieldDefinition().name("attachType").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType STORE_TOPIC_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("StoreTopic")
        .field(newFieldDefinition().name("tpId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpNameForei").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpBizType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tpFuncType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("isDisplay").type(Scalars.GraphQLBoolean))
        .field(newFieldDefinition().name("attachType").type(Scalars.GraphQLInt))
        .build();


    /**
     * @author liminzhe
     * BannerDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType STORE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Store")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("retailType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("city").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailAddr").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailAddrChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("description").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("descriptionChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("longitude").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("latitude").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("disdance").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("shopHour").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("email").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("ceoName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("bizScope").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("managerId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("managerPhone").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("managerAccount").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("managerName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("managerEmail").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("categoryList").type(GraphQLList.list(CATEGORY_ID_TYPE)))
            .field(newFieldDefinition().name("categoryTagList").type(GraphQLList.list(CATEGORY_ID_TYPE)))
            .field(newFieldDefinition().name("categoryManagerTagList").type(GraphQLList.list(CATEGORY_ID_TYPE)))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("logoUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("merchantId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("merchantNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("gatewayPw").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("transidType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isSelfservice").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isDelivery").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("selfserviceUseyn").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("deliveryUseyn").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isPaySet").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isOpening").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isSegmented").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("morningSt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("morningEt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("noonSt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("noonEt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("eveningSt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("eveningEt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("afternoonSt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("afternoonEt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("midnightSt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("midnightEt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chargeType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("chargeRate").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("chargeFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("nextChargeType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("nextChargeRate").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("nextChargeFee").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("tableCount").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("productList").type(GraphQLList.list(PRD_GRP_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("storeTableList").type(GraphQLList.list(STORE_TABLE_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("minPriceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("minPriceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("minDiscountPriceKor").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("minDiscountPriceChn").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("isBind").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("storeExtendList").type(GraphQLList.list(STORE_EXTEND_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("attachList").type(GraphQLList.list(STORE_ATTACH_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("topicList").type(GraphQLList.list(STORE_TOPIC_GRAPHQL_TYPE)))
            .field(newFieldDefinition().name("cashSettleType").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodPriceType").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("stBankName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stBankAcc").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("stBankAccName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isJoin").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("chargeRatePriceOn").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType STORE_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("StorePagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(STORE_GRAPHQL_TYPE)))
            .build();

    /**
     * @author liminzhe
     * CategoryDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType CATEGORY_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Category")
            .field(newFieldDefinition().name("id").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("nameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("createdDt").type(Scalars.GraphQLString))
            .build();

    final static GraphQLUnionType PAGE_UNION_TYPE = GraphQLUnionType.newUnionType()
            .name("Page")
            // .possibleType(GraphQLTypeReference.typeRef("ProductGroup"))
            .possibleTypes(PRODUCT_GROUP_GRAPHQL_TYPE)
            .typeResolver(new TypeResolver() {
                @Override
                public GraphQLObjectType getType(TypeResolutionEnvironment tre) {
                    System.out.println("\n\n\n starting------------");
                    System.out.println(tre.getObject().getClass());
                    System.out.println(tre.getField() + "  the field");
                    System.out.println("Field Type:" + tre.getFieldType());

                    return PRODUCT_GROUP_GRAPHQL_TYPE;
                }
            })
            .build();

    /**
     * @author liminzhe
     * CategoryDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType AREA_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("area")
            .field(newFieldDefinition().name("areaCd").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("parentCd").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaLvl").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("areaStatus").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("useYn").type(Scalars.GraphQLChar))
            .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
            .build();


    final static GraphQLObjectType MenuItemDTO_TYPE = GraphQLObjectType.newObject()
            .name("MenuItem")
            .field(newFieldDefinition().name("productGroupId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("productId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("showIndex").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .build();

    final static GraphQLObjectType REVIEW_IMG_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("RevImg")
            .field(newFieldDefinition().name("revAttachId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("revId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("contentUrl").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("displayOrder").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("isDisplay").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType REVIEW_TYPE = GraphQLObjectType.newObject()
            .name("Review")
            .field(newFieldDefinition().name("revId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("platformId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nickname").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chathead").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revClass").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("envClass").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodClass").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("serviceClass").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revContent").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revContentCopie").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isAnonymity").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revStatus").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revReplyContent").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revReplyContentCopie").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revReplier").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("revReplyTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("imgList").type(GraphQLList.list(REVIEW_IMG_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType ADVICE_IMG_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("AdviceImg")
        .field(newFieldDefinition().name("adviAttachId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("adviId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("contentUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("displayOrder").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("isDisplay").type(Scalars.GraphQLString))
        .build();

    final static GraphQLObjectType ADVICE_TYPE = GraphQLObjectType.newObject()
        .name("Advice")
        .field(newFieldDefinition().name("adviId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("platformId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("nickname").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("chathead").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("revPlatform").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviTos").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("custId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviContent").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviTime").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("linker").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("linkPhone").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("linkEmail").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviStatus").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviReplyContent").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviReplier").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("adviReplyTime").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("modifiedDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("modifiedUserId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("imgList").type(GraphQLList.list(ADVICE_IMG_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType ADVERT_DETAIL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("AdvertDtl")
        .field(newFieldDefinition().name("advDetId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("advId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("contentName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("contentUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("targetUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advDetOrder").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("useYn").type(Scalars.GraphQLString))
        .build();


    final static GraphQLObjectType ADVERT_TYPE = GraphQLObjectType.newObject()
        .name("Advert")
        .field(newFieldDefinition().name("advId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("advName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advLocation").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advContent").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advImageUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advSeconds").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advHeight").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advWidth").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advTop").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advLeft").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advDesc").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("advStatus").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("useYn").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("createdDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("modifiedDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("detailList").type(GraphQLList.list(ADVERT_DETAIL_GRAPHQL_TYPE)))
        .build();



    final static GraphQLObjectType REVIEW_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("ReviewPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(REVIEW_TYPE)))
            .build();

    final static GraphQLObjectType ADVICE_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("AdvicePagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(ADVICE_TYPE)))
            .build();
    final static GraphQLObjectType ADVERT_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("advertPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(ADVERT_TYPE)))
            .build();

    // 折扣活动的GraphQL Object
    final static GraphQLObjectType DISCOUNT_TYPE = GraphQLObjectType.newObject()
            .name("Discount")
            .field(newFieldDefinition().name("discId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discChannel").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discRate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("startTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("endTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discStatus").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useYn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdUserId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("modifiedDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("modifiedUserId").type(Scalars.GraphQLString))
            .build();

    // 折扣活动的明细GraphQL Object
    final static GraphQLObjectType DISCOUNT_DETAIL_TYPE = GraphQLObjectType.newObject()
            .name("DiscountDetail")
            .field(newFieldDefinition().name("discDetId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("custId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discPrice").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdUserId").type(Scalars.GraphQLString))
            .build();

    // 折扣详情GraphQL Object
    final static GraphQLObjectType DISCOUNT_INFO_TYPE = GraphQLObjectType.newObject()
            .name("DiscountDTO")
            .field(newFieldDefinition().name("discId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discChannel").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discRate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("startTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("endTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discStatus").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("useYn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("createdUserId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("modifiedDt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("modifiedUserId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailList").type(GraphQLList.list(DISCOUNT_DETAIL_TYPE)))
            .build();

    // 折扣活动的分页列表的GraphQL Object
    final static GraphQLObjectType DISCOUNT_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("DiscountPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(DISCOUNT_TYPE)))
            .build();

    // 折扣活动的展示列表的GraphQL Object
    final static GraphQLObjectType DISCOUNT_DISPLAY_TYPE = GraphQLObjectType.newObject()
            .name("DiscountDisplayList")
            .field(newFieldDefinition().name("discId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discChannel").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discRate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("startTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("endTime").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discDetId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("discPrice").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("discPriceChn").type(Scalars.GraphQLString))
            .build();

    /**
     * @author dikim
     * ShipPointDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType SHIP_POINT_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("ShipPoint")
            .field(newFieldDefinition().name("shipPointid").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("shipPointnm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("areaName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("addr").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("addrCn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("lat").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("lon").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("phoneNo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("cmt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("cmtCn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .build();


    final static GraphQLObjectType SHIP_POINT_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("ShipPointPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(SHIP_POINT_GRAPHQL_TYPE)))
            .build();   

    /**
     * @author dikim
     * OrderDTO -> GraphQLObjectType
     */
    final static GraphQLObjectType MY_CONTACT_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("MyContact")
            .field(newFieldDefinition().name("openId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("countryNo").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("mobile").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("custNm").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmLastEn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("nmFirstEn").type(Scalars.GraphQLString))
            .build();


    final static GraphQLObjectType MY_CONTACT_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("MyContactPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(MY_CONTACT_GRAPHQL_TYPE)))
            .build();   

    final static GraphQLObjectType HOLIDAY_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("Holiday")
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("dt").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("holiday").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType INVDATEANDINVTOTAL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("InvDateAndInvTotal")
            .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("disPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceSettle").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("disPriceSettle").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invUsed").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invBalance").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invDate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isOpening").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType INVENTORYHOTEL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("InventoryHotel")
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("productNmKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("productNmChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("skuNmKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("skuNmChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("priceDefault").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("priceSettleDefault").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("invDate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isOpening").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invDateAndTotalList").type(GraphQLList.list(INVDATEANDINVTOTAL_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType INVENTORYHOTEL_GRAPHQL_TYPE_BASE = GraphQLObjectType.newObject()
            .name("InventoryHotelBase")
            .field(newFieldDefinition().name("invId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("prodId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("prodSkuId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("disPrice").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("invYear").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invMonth").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invDay").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invDate").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isOpening").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("invTotal").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invUsed").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("invBalance").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType AGENT_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("Agent")
        .field(newFieldDefinition().name("agtId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("agtType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("agtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtAccount").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("caChargeRate").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtPassword").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtMobile").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtEmail").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtBankCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtQrcodeUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtTicket").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtQrcodeId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("contractSt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("contractEd").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("renewal").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType AGT_STORE_MAP = GraphQLObjectType.newObject()
        .name("agtStoreMap")
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("agtType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("agtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtRate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtPercent").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeChargeRate").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType AGENT_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("AgentPagingList")
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(AGENT_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType AGENT_STORE_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("AgentStore")
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("agtId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("agtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("agtBankCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("chargeType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("city").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeChargeRate").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtRate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtPercent").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("contractSt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("contractEd").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("isBind").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("isValid").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("vatFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("laterTrainDate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("saAgtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("caAgtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("aimMapList").type(GraphQLList.list(AGT_STORE_MAP)))
        .build();

    final static GraphQLObjectType AGENT_STORE_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("AgentStorePagingList")
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("vatFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(AGENT_STORE_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType AGENT_ORDER_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("AgentOrder")
        .field(newFieldDefinition().name("agtName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("agtCode").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("orderId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("tableId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("orderAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("vatFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("price").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("paymentAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("discountAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("isRefund").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("buyerMemo").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("customerId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("created").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("orderDate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("cancelDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("updated").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("cancelDt").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("payDts").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("completeDate").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("storeType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("areaNm").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("logoUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tableNum").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tableTag").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("numTag").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("openId").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("nickName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("headImgUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("subscribeTime").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("itemList").type(GraphQLList.list(ORDER_ITEM_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType AGT_ORDERS_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("AgtOrderPagingList")
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("agtFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("vatFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(AGENT_ORDER_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType AGT_ORDER_SUM_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("AgtSumAmountQtyStats")
        .field(newFieldDefinition().name("date").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("sumAmount").type(Scalars.GraphQLBigDecimal))
        .build();

    final static GraphQLObjectType PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("plAgtAmountSumStats")
        .field(newFieldDefinition().name("date").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("dateType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("aimType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("amount").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("fee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("plFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("saFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("caFee").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("profit").type(Scalars.GraphQLBigDecimal))
        .field(newFieldDefinition().name("qty").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType PL_AGT_AMOUNT_STATS_LIST_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("plAgtAmountList")
        .field(newFieldDefinition().name("plFeeList").type(GraphQLList.list(PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE)))
        .field(newFieldDefinition().name("saFeeList").type(GraphQLList.list(PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE)))
        .field(newFieldDefinition().name("caFeeList").type(GraphQLList.list(PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE)))
        .field(newFieldDefinition().name("profitList").type(GraphQLList.list(PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE)))
        .field(newFieldDefinition().name("dateList").type(GraphQLList.list(Scalars.GraphQLString)))
        .build();



    final static GraphQLObjectType BASETOPIC_GRAPHQL_TYPE = GraphQLObjectType.newObject()
        .name("BaseTopic")
        .field(newFieldDefinition().name("tpId").type(Scalars.GraphQLLong))
        .field(newFieldDefinition().name("tpBizType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tpFuncType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tpName").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpNameForei").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpLogoSid").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpLogoUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpUrlType").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tpUrl").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpDisType").type(Scalars.GraphQLString))
        .field(newFieldDefinition().name("tpOrder").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("tpStatus").type(Scalars.GraphQLInt))
        .build();

    final static GraphQLObjectType BASE_TOPIC_PAGING_TYPE = GraphQLObjectType.newObject()
        .name("BaseTopicPagingList")
        .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
        .field(newFieldDefinition().name("dataList").type(GraphQLList.list(BASETOPIC_GRAPHQL_TYPE)))
        .build();

    final static GraphQLObjectType POST_STORE_SET_DETAIL_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("PostStoreSetDetail")
            .field(newFieldDefinition().name("pssId").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("detailNo").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("lowerLimit").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("upperLimit").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chargeFee").type(Scalars.GraphQLString))
            .build();

    final static GraphQLObjectType POST_STORE_SET_GRAPHQL_TYPE = GraphQLObjectType.newObject()
            .name("PostStoreSet")
            .field(newFieldDefinition().name("pssId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("storeId").type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("setNameChn").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("setNameKor").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("setNameEng").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("targetCountryName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("targetCountryCode").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("isFree").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("freeAmount").type(Scalars.GraphQLBigDecimal))
            .field(newFieldDefinition().name("postComName").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("postComCode").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("setRule").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("status").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("detailList").type(GraphQLList.list(POST_STORE_SET_DETAIL_GRAPHQL_TYPE)))
            .build();

    final static GraphQLObjectType POST_STORE_SET_PAGING_TYPE = GraphQLObjectType.newObject()
            .name("PostStoreSetPagingList")
            .field(newFieldDefinition().name("total").type(Scalars.GraphQLInt))
            .field(newFieldDefinition().name("dataList").type(GraphQLList.list(POST_STORE_SET_GRAPHQL_TYPE)))
            .build();



}

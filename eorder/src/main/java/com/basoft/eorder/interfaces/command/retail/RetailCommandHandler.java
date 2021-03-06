package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
//import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.ProductRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductSkuAloneStandard;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.domain.retail.RetailOrderServiceRepository;
import com.basoft.eorder.domain.retail.RetailRepository;
import com.basoft.eorder.domain.retail.RetailTemplateRepository;
import com.basoft.eorder.interfaces.command.SaveProduct;
import com.basoft.eorder.interfaces.command.UpdateProSkuStatus;
import com.basoft.eorder.interfaces.query.retailOrderService.RetailOrderServiceQuery;
import com.basoft.eorder.util.UidGenerator;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * ?????????????????????
 * @author DongXifu
 * @since  2020/04/02
 */
@Slf4j
@CommandHandler.AutoCommandHandler("RetailCommandHandler")
public class RetailCommandHandler {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RetailRepository retailRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryRetailRepository inventoryRetailRepository;

    @Autowired
    private RetailTemplateRepository retailTemplateRepository;

    @Autowired
    private UidGenerator uidGenerator;

//    @Autowired
//    private RedissonUtil redissonUtil;

    @Autowired
    private RetailOrderServiceRepository retailOrderServiceRepository;

    @Autowired
    private RetailOrderServiceQuery retailServiceQuery;

    @Autowired
    private OrderService orderService;

    /**
     * ????????????
     *
     * @param saveRetail
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveRetail.NAME)
    @Transactional
    public Long saveRetail(SaveRetail saveRetail, CommandHandlerContext context) {
        log.debug("Start saveRetail -------");
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = storeRepository.getStore(us.getStoreId());
        Category c = vaildCategory(store,saveRetail);
        Product product = buildProduct(store,c,saveRetail);

        List<ProductAloneStandard> standards = new LinkedList<>();
        if (saveRetail.getIsStandard() == 1) {
            standards = buildStandards(saveRetail, store, product, us);
        }
        RetailSkuStandard poting = buildProductPoting(saveRetail,store,product,us,standards);//????????????,skuStandardItem??????

        Long productId = saveRetail(store,saveRetail,poting); //??????,??????,standard??????

        saveRetailTemplate(saveRetail,store,poting,us); //????????????

        if(poting.getInventoryRetails()!=null&&poting.getInventoryRetails().size()>0)
            inventoryRetailRepository.saveInventoryRetails(poting.getInventoryRetails());//???????????? ??????

        return  productId;
    }

    /**
     * ??????????????????
     *
     * @param updateProSkuStatus
     * @return
     */
    @CommandHandler.AutoCommandHandler(UpdateProSkuStatus.UP_STATUS_NAME)
    public Long updateProSKuStatus(UpdateProSkuStatus updateProSkuStatus) {
       return productRepository.updateProSkuStatus(updateProSkuStatus);
    }

    /**
     * ????????????
     *
     * @param poting
     * @return
     */
    private Long saveRetailTemplate(SaveRetail saveRetail,Store store,RetailSkuStandard poting,UserSession us) {
        ProductAloneStandardTemplate template = saveRetail.buildTemplate(store,us);
        if (saveRetail.getIsTemplate() == 1) {
            return   retailTemplateRepository.saveRetailTemplate(poting.getStandards(),template);
        }
        return 0L;
    }


    /**
     * ???????????? ??????,??????,standard ??????
     *
     * @param saveRetail
     * @param poting
     * @return
     */
    private Long saveRetail(Store store,SaveRetail saveRetail ,RetailSkuStandard poting) {
        Long prodId = 0L;
        if (saveRetail.getProductId() != null) {//??????
            Product product =  this.productRepository.getProduct(store,saveRetail.getProductId());
            if (product == null) {
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
            prodId = this.retailRepository.updateProduct(poting.getProduct(), poting.getProductSkus());
            this.productRepository.saveProductSkuImage(poting.getProductSkus());
        } else {//??????
            Product product = this.productRepository.saveProduct(poting.getProduct(), poting.getProductSkus());
            this.productRepository.saveProductSkuImage(poting.getProductSkus());
            prodId = product.id();
        }
        if (saveRetail.getIsStandard() == 1)
            this.retailRepository.saveProductStandards(poting);

        return prodId;
    }

    /**
     * ??????standard???item
     *
     * @param saveRetail
     * @param store
     * @param product
     * @param us
     * @return
     */
    private List<ProductAloneStandard> buildStandards(SaveRetail saveRetail, Store store, Product product, UserSession us) {
        Long tId = saveRetail.getTId();
        if (saveRetail.getIsTemplate() == SaveRetail.IS_TEMPLATE_YES) {
            if (tId == null) {
                tId= uidGenerator.generate(BusinessTypeEnum.PRODUCT_ALONE_STANDARD_TEMPLATE);
                saveRetail.setTId(tId);
            }
        }
        Long finalTId = tId;
        return saveRetail.getAloneStandardList()
                .stream().map((SaveRetail.SaveAloneStandard stand) -> {
                    Long standId = stand.getStdId();
                    if (standId == null) {
                        standId = uidGenerator.generate(BusinessTypeEnum.PRO_ALONE_STAND);
                        stand.setStdId(standId);
                    }
                    return stand.build(store, finalTId, product, us, uidGenerator);
                }).collect(toList());
    }

    /**
     * ????????????
     * ???????????????standar??????
     * ??????????????????
     *
     * @param saveRetail
     * @param
     * @return
     */
    private RetailSkuStandard  buildProductPoting(SaveRetail saveRetail,Store store,Product product, UserSession us,List<ProductAloneStandard> standardList) {
        Category c = vaildCategory(store,saveRetail);
        List<ProductSkuAloneStandard> skuStandardList = new LinkedList<>();//?????????sku????????????
        List<InventoryRetail> inventoryRetails = new LinkedList<>();//??????list
        Product finalProduct = product;
        List<ProductSku> skuList =
                saveRetail
                        .getSkuList()
                        .stream()
                        .map((SaveProduct.SaveProductSku builder) -> {
                            Long skuId = builder.getId();
                            if (skuId == null) {
                                builder.setId(uidGenerator.generate(BusinessTypeEnum.PRODUCT_SKU));
                            }
                            //????????????
                            if (builder.getIsInventory() == saveRetail.IS_INVENTORY_YES) {
                                inventoryRetails.add(builder.buildInventory(product,0L,us,uidGenerator));
                            }
                            if(saveRetail.getIsStandard()==SaveRetail.IS_STANDARD_YES) {
                                //???????????????standard??????
                                for (SaveRetail.SaveAloneStandard standard : saveRetail.getAloneStandardList()) {
                                    ProductSkuAloneStandard proSkuStandard = new ProductSkuAloneStandard();
                                    Long standId = standard.getStdId();
                                    proSkuStandard.setProductSkuId(builder.getId());
                                    proSkuStandard.setStandardId(standId);
                                    for (SaveRetail.SaveAloneStandardItem item : standard.getStandardItemList()) {
                                        for (SaveProduct.SaveItemName saveItemName : builder.getStandardItemList()) {
                                            if (item.getItemNameChn().trim().equals(saveItemName.getItemNameChn().trim())) {
                                                proSkuStandard.setStandardItemId(item.getItemId());
                                                proSkuStandard.setStatus(item.getItemStatus() == null ? 1 : Integer.valueOf(item.getItemStatus()));
                                                skuStandardList.add(proSkuStandard);
                                            }
                                        }
                                    }
                                }
                            }
                            return builder.build(finalProduct, null);
                        })
                        .collect(toList());

        return  new RetailSkuStandard(c,product,skuList,standardList,skuStandardList,inventoryRetails);
    }


    /**
     * ??????retail??????
     *
     * @param store
     * @param c
     * @param saveRetail
     * @return
     */
    private Product buildProduct(Store store, Category c, SaveRetail saveRetail) {
        Long productId = 0L;
        if (saveRetail.getProductId() == null) {
            productId = uidGenerator.generate(BusinessTypeEnum.PRODUCT);
        }else {
            productId = saveRetail.getProductId();
        }
        return saveRetail.buildProduct(productId, store, c, saveRetail);
    }

    /**
     * ????????????????????????
     *
     * @param store
     * @param saveRetail
     */
    private Category vaildCategory(Store store, SaveRetail saveRetail) {
        Category c =
                storeRepository
                        .getStoreCategory(store)
                        .stream()
                        .filter((cc) -> cc.id().equals(saveRetail.getCategoryId()))
                        .findFirst()
                        .orElseThrow(() -> {
                            log.warn("??????????????????ID:" + saveRetail.getCategoryId());
                            return new BizException(ErrorCode.BIZ_EXCEPTION);
                        });
        return c;
    }


    /**
     * ?????????????????????
     *
     * @param saveInventory
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveInventoryRetail.NAME)
    @Transactional
    public Long saveStockRetail(SaveInventoryRetail saveInventory, CommandHandlerContext context) {
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = storeRepository.getStore(us.getStoreId());
        saveInventory.setAcoount(us.getAccount());
        saveInventory.setStoreId(us.getStoreId());

        log.info("<><><><><><><><><>><><><><>???cms??????id->"+store.id()+"????????????????????????->{}???<><><><><><>><><><><>", saveInventory.getNum());
        // 3func-2.?????????????????????????????????
        StringBuilder retailLockKey = new StringBuilder(CommonConstants.RETAIL_INVENTORY_LOCK).append(store.id());
//        RLock lock = redissonUtil.getFairLock(retailLockKey.toString());
//        // ?????????????????????????????????60?????????????????????
//        lock.lock(60, TimeUnit.SECONDS);

        try {
            log.info("<><><><><><><><><>><><><><>???cms??????id->"+store.id()+"????????????????????????->{}???<><><><><><><><><>><><><><>", saveInventory.getNum());
           return inventoryRetailRepository.saveInventoryRetail(saveInventory);
        } finally {
//            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.info("<><><><><><><><><>><><><><>???cms??????id->"+store.id()+"??????????????????????????????->{}???<><><><><><><><><>><><><><>",saveInventory.getNum());
//            }
        }
    }

    /**
     * command?????????
     * ???1??????????????????????????????????????????????????????????????????????????????????????????saveRetailOrderService???accept?????????1
     * ???2??????????????????????????????saveRetailOrderService???accept?????????0
     *
     * @param saveRetailOrderService
     * @param context
     * @return
     */
    /*
        {
            "servId": 6,
            "servCode":895390198328005638,
            "accept":1
        }
     */
    @CommandHandler.AutoCommandHandler(SaveRetailOrderService.ACCEPTANCE)
    public RetailOrderService retailOrderServiceAcceptance(SaveRetailOrderService saveRetailOrderService, CommandHandlerContext context) {
        RetailOrderService retailOrderService = retailServiceQuery.getRetailOrderService(saveRetailOrderService.getServId(), saveRetailOrderService.getServCode());

        // ????????????
        if (retailOrderService != null && saveRetailOrderService.getAccept() == 1) {
            if (retailOrderService.getServStatus() == 1) {
                //??????ACCEPTOR ??? ACCEPT_TIME
                retailOrderServiceRepository.acceptance(
                        saveRetailOrderService.build(
                                (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP)
                        )
                );
            }
        }

        // ??????????????????
        retailOrderService = retailServiceQuery.getRetailOrderService(saveRetailOrderService.getServId(), saveRetailOrderService.getServCode());

        return retailOrderService;
    }

    /**
     * ???????????????????????????
     * ???1????????????????????????????????????????????????????????????????????????????????????????????????
     * ???2?????????????????????????????????????????????
     *
     * @param saveRetailOrderService
     * @param context
     * @return
     */
    /*
        {
            "servId": 6,
            "servCode":895390198328005638,
            "auditResult":0,
            "auditDesc":"???????????????"
        }

        {
            "servId": 6,
            "servCode":895390198328005638,
            "auditResult":1,
            "auditRefundType":1,
            "auditRefundAmount":200,
            "auditDesc":"???????????????"
        }
     */
    @CommandHandler.AutoCommandHandler(SaveRetailOrderService.AUDIT)
    public Map<String, Object> retailOrderServiceAudit(SaveRetailOrderService saveRetailOrderService, CommandHandlerContext context) {
        // 1???????????????
        if (!saveRetailOrderService.checkParameter()) {
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 2?????????????????????
        RetailOrderService retailOrderService = retailServiceQuery.getRetailOrderService(saveRetailOrderService.getServId(), saveRetailOrderService.getServCode());
        if(retailOrderService == null || retailOrderService.getServStatus() != 2){
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 3?????????????????????
        int auditResult = saveRetailOrderService.getAuditResult();
        int auditRefundType = saveRetailOrderService.getAuditRefundType();
        boolean isRefund = false;

        // ?????????????????????????????????1???2??????????????????????????????????????????
        if (auditResult == 1) {

            // ????????????
            BigDecimal auditRefundAmount = saveRetailOrderService.getAuditRefundAmount();

            // ????????????
            BigDecimal paymentAmount = retailOrderService.getApplyAmount();

            // ????????????
            if(auditRefundType == 1){
                // ????????????????????????????????????
                if (auditRefundAmount.compareTo(paymentAmount) == 0) {
                    // ??????????????????
                    isRefund = true;
                } else {
                    // ?????????????????????
                    throw new BizException(ErrorCode.AUDIT_REFUND_AMOUNT_INVALID);
                }
            }

            // ????????????
            if(auditRefundType == 2){
                // ??????????????????0???????????????????????????
                if (auditRefundAmount.compareTo(BigDecimal.ZERO) == 1 && auditRefundAmount.compareTo(paymentAmount) == -1) {
                    // ??????????????????
                    isRefund = true;
                } else {
                    // ?????????????????????
                    throw new BizException(ErrorCode.AUDIT_REFUND_AMOUNT_INVALID);
                }
            }

            saveRetailOrderService.setOrderId(retailOrderService.getOrderId());
        }

        // 4??????????????????????????????
        saveRetailOrderService.setServStatus();
        int a = retailOrderServiceRepository.audit(
                saveRetailOrderService.build((UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP))
        );


        // 5?????????????????????????????????????????????????????????
        WxPayRefundResult refundResult = null;
        if (a > 0) {
            // ????????????????????????????????????
            if (isRefund) {
                refundResult = orderService.retailOrderRefundFromOrderService(saveRetailOrderService, context);
            }
        }

        // 6?????????
        Map<String, Object> r = Maps.newHashMap();
        r.put("result", a);
        r.put("refundResult", refundResult);
        return r;
    }

    /**
     * ???????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     * ???1????????????????????????6
     *
     * @param saveRetailOrderService
     * @param context
     * @return
     */
    /*
        {
            "servId": 6,
            "servCode":895390198328005638,
        }
     */
    @CommandHandler.AutoCommandHandler(SaveRetailOrderService.RETRY_REFUND)
    public Map<String, Object> retailOrderServiceRetryRefund(SaveRetailOrderService saveRetailOrderService, CommandHandlerContext context) {
        // 1???????????????
        if(saveRetailOrderService.getServId() == null
                || saveRetailOrderService.getServId().longValue() == 0
                || saveRetailOrderService.getServCode() == null
                || saveRetailOrderService.getServCode().longValue() == 0){
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 2?????????????????????
        RetailOrderService retailOrderService = retailServiceQuery.getRetailOrderService(saveRetailOrderService.getServId(), saveRetailOrderService.getServCode());
        if(retailOrderService == null || retailOrderService.getServStatus() != 6){
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 3?????????????????????????????????????????????????????????
        saveRetailOrderService.setOrderId(retailOrderService.getOrderId());
        WxPayRefundResult refundResult = orderService.retailOrderRefundFromOrderService(saveRetailOrderService, context);

        // 6?????????
        Map<String, Object> r = Maps.newHashMap();
        r.put("result", 1);
        r.put("refundResult", refundResult);
        return r;
    }
}

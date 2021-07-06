package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.*;
import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
//import com.basoft.eorder.batch.job.threads.HotelPriceThread;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.ProductRepository;
import com.basoft.eorder.domain.StoreOptionRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.*;
import com.basoft.eorder.interfaces.query.StoreOptionDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;


/**
 * @author woonill
 * @since  2018/12/14
 * 产品的保存和更新操作处理
 */
@CommandHandler.AutoCommandHandler("ProductCommandHandler")
@Transactional
public class ProductCommandHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ProductRepository productRepository;
    private StoreRepository storeRepository;
    private UidGenerator uidGenerator;
    private BaseService baseService;
    private StoreOptionRepository storeOptionRepository;
    private InventoryHotelQuery inventoryHotelQuery;


    ProductCommandHandler(){}

    @Autowired
    public ProductCommandHandler(
            ProductRepository productRepository,
            StoreRepository storeRepository,
            StoreOptionRepository storeOptionRepository,
            InventoryHotelQuery inventoryHotelQuery,
            UidGenerator uidg,
            BaseService bas
    ){

        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.storeOptionRepository = storeOptionRepository;
        this.inventoryHotelQuery = inventoryHotelQuery;
        this.uidGenerator = uidg;
        this.baseService = bas;
    }

    @CommandHandler.AutoCommandHandler(SaveProduct.NAME)
    @Transactional
    public Long saveProduct(SaveProduct saveProduct, CommandHandlerContext context) {


        logger.debug("Start saveProduct -------");

        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = storeRepository.getStore(us.getStoreId());
        Category c =
                storeRepository
                    .getStoreCategory(store)
                    .stream()
                    .filter((cc) -> cc.id().equals(saveProduct.getCategoryId()))
                    .findFirst()
                    .orElseThrow(()-> {
                        logger.warn("无法找到分类ID:"+saveProduct.getCategoryId());
                        return new BizException(ErrorCode.BIZ_EXCEPTION);
                    });


        Long productId = uidGenerator.generate(BusinessTypeEnum.PRODUCT);
        Product product = saveProduct.build(productId, store, c);


        final StoreOptionDTO storeOption = storeOptionRepository.getOptionDto(store.id(),us.getStoreType());

       // Option rootOption = baseService.getRootOption();

        final List<ProductSku> productSkus =
                saveProduct
                        .getSkuList()
                        .stream()
                        .map((SaveProduct.SaveProductSku builder) -> {
                            builder.setId(uidGenerator.generate(BusinessTypeEnum.PRODUCT_SKU));
                            return builder.build(product,storeOption);

                        })
                        .collect(toList());
        this.productRepository.saveProduct(product, productSkus);
        if (us.getStoreType() == CommonConstants.BIZ_HOTEL_INT) {
//            Thread thread = new Thread(new HotelPriceThread(us,storeRepository,inventoryHotelQuery));
//            thread.start();
        }
        return product.id();
    }


    @CommandHandler.AutoCommandHandler(UpdateProduct.NAME)
    @Transactional
    public Long updateProduct(UpdateProduct upProduct,CommandHandlerContext cch) {

        UserSession us = (UserSession) cch.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = storeRepository.getStore(us.getStoreId());

        upProduct.getSkuList().forEach(new Consumer<SaveProduct.SaveProductSku>() {
            @Override
            public void accept(SaveProduct.SaveProductSku spSku) {
                if(spSku.getId() == null){
                    spSku.setId(uidGenerator.generate(BusinessTypeEnum.PRODUCT_SKU));
                }
            }
        });


        Product product = productRepository.getProduct(store,upProduct.getId());

        //Option rootOption = this.baseService.getRootOption();//待选规格列表

        final StoreOptionDTO storeOption = storeOptionRepository.getOptionDto(store.id(),us.getStoreType());

        verifyProStatus(product,upProduct);

        Category c =
            storeRepository
                .getStoreCategory(store)
                .stream()
                .filter((cc) -> cc.id().equals(upProduct.getCategoryId()))
                .findFirst()
                .orElseThrow(()-> {
                    logger.warn("无法找到分类ID:"+upProduct.getCategoryId());
                    return new BizException(ErrorCode.BIZ_EXCEPTION);
                });

        Product newProduct = upProduct.build(product,c);

        List<ProductSku> inSkus = upProduct.buildSku(product,storeOption);
        List<ProductSku> skus = productRepository.getProductSkuList(product);

//        System.out.println(skus.size());

        List<ProductSku> newSkuList = new LinkedList<>(inSkus);

        skus.forEach(new Consumer<ProductSku>() {
            @Override
            public void accept(ProductSku productSku) {
                ProductSku fps = inSkus.stream()
                        .filter((pp) -> pp.isSame(productSku))
                        .findFirst()
                        .orElseGet(() -> null);

                /*if(fps == null){
                    final ProductSku sku = productSku.close();
                    newSkuList.add(sku);
                }*/
            }
        });
        productRepository.updateProduct(newProduct, newSkuList);
//        Thread thread = new Thread(new HotelPriceThread(us,storeRepository,inventoryHotelQuery));
//        thread.start();


        return product.id();
    }

    private UpdateProduct verifyProStatus(Product p, UpdateProduct u) {
        String detaildesc = StringUtils.isBlank(u.getDetailDesc())==true?"":u.getDetailChnDesc();
        String detaildescChn = StringUtils.isBlank(u.getDetailChnDesc())==true?"":u.getDetailChnDesc();
        if(!u.getChnName().equals(p.chnName())||!u.getName().equals(p.name())
            ||!u.getDesChn().equals(p.showOption().getDesChn())
            ||!u.getDesKor().equals(p.showOption().getDesKor())
            ||!detaildescChn.equals(p.showOption().getDetailDescChn())
            ||!detaildesc.equals(p.showOption().getDetailDescKor())
            ){
            u.setStatus(Product.Status.NORMAL);
            return  u;
        }
        return u;
    }

    /**
     * @param  productStatus, cch
     * @return int
     * @describe 删除或启用禁用 产品
     * @author Dong Xifu
     * @date 2019/1/7 下午2:23
     */
    @CommandHandler.AutoCommandHandler(UpdateProductStatus.NAME)
    @Transactional
    public int updateProductStatus(UpdateProductStatus productStatus,CommandHandlerContext cch){
        UserSession us = (UserSession) cch.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Long storeId = us.getStoreId();
        int status = productRepository.updateProductStatus(productStatus,storeId);

//        Thread thread = new Thread(new HotelPriceThread(us,storeRepository,inventoryHotelQuery));
//        thread.start();

        return status;
    }



/*    @CommandHandler.AutoCommandHandler("activeProductGroup")
    @Transactional
    public Long activeProductGroup(OpenStoreProductGroup openProduct, CommandHandlerContext context) {
        Long id = (Long) context.props().get("store_id");
        Store store = storeRepository.getStore(id);
        List<ProductGroup> groups = this.productRepository.getProductGroup(store).stream().filter((pgr) -> pgr.);
        ProductGroup newGroup = openProduct.build(group);
        return this.productRepository.updateProductGroup(newGroup);
    }*/

    /**
     * @param  updateProductGroup, context
     * @return java.lang.Long
     * @describe
     * @author Woonill
     * @date 2018/12/27 下午2:02
     */
    @CommandHandler.AutoCommandHandler(SaveProductGroup.NAME)
    @Transactional
    public Long saveProductGroup(SaveProductGroup updateProductGroup, CommandHandlerContext context) {

        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Long storeId = us.getStoreId();
        Store store = storeRepository.getStore(storeId);

        if(store == null){
            throw new BizException(ErrorCode.SYS_ERROR);

        }

        List<Product> productList = productRepository.getProductListOfStore(store);
/*
        if(productList.size() != updateProductGroup.getItems().size()){
            logger.warn("miss match product list");
            throw new BizException(ErrorCode.SYS_ERROR);
        }*/

        //更新
        if(updateProductGroup.getId() != null){
            ProductGroup newGroup = updateProductGroup.build(store,updateProductGroup.getId(),productList);
            return this.productRepository.updateProductGroup(newGroup);
        }

        //此部分业务逻辑上暂时封闭
        Long pgId = uidGenerator.generate(BusinessTypeEnum.PRODUCT_GROUP);
        ProductGroup newGroup = updateProductGroup.build(store,pgId,productList);
        return this.productRepository.saveProductGroup(newGroup);
    }

    /**
     * @param  proGroupStatus, context
     * @return int
     * @describe 删除或禁用分类
     * @author Dong Xifu
     * @date 2019/1/7 下午3:27
     */
    @CommandHandler.AutoCommandHandler(UpdateProGroupStatus.NAME)
    @Transactional
    public int updateProGroupStatus(UpdateProGroupStatus proGroupStatus,CommandHandlerContext context){

        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Long storeId = us.getStoreId();

        Store store = storeRepository.getStore(storeId);
        List<ProductGroup> groups = this.productRepository.getGroupOfStore(store);//店铺所有的分类
        Map<Long,ProductGroup> groupMap =new HashMap<>();

        groups.stream().forEach(new Consumer<ProductGroup>() {
            @Override
            public void accept(ProductGroup productGroup) {
                groupMap.put(productGroup.id(),productGroup);
            }
        });

        List<Long> findIds = proGroupStatus.getProductGroupIds().stream().filter((gid) -> {
            ProductGroup group = groupMap.get(gid);
            if(group != null && !group.items().isEmpty()){
                return true;
            }
            return false;

        }).collect(toList());

        if(!findIds.isEmpty() ){
            ProductGroup fGroup = groupMap.get(findIds.get(0));
            System.out.println(fGroup.items().iterator().next().productId());
            throw new BizException(ErrorCode.PRODUCT_IS_USE,fGroup.id()+":有产品在使用此分类");
        }
        return this.productRepository.updateProductGroupStatus(proGroupStatus,storeId);

    }



    /**
     * @Param 新增或修改店铺规格
     *
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/7/8 上午11:38
     */
    @Transactional
    @CommandHandler.AutoCommandHandler(SaveStoreOption.NAME)
    public Long saveStoreOption(SaveStoreOption saveOption,CommandHandlerContext context){
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Long storeId = us.getStoreId();

        Long optionId = saveOption.getPcId();
        StoreOption parentOption = storeOptionRepository.getOption(saveOption.getParentId(),storeId);



        if(parentOption == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if(optionId == null){
            optionId = uidGenerator.generate(BusinessTypeEnum.STORE_OPTION);
            StoreOption option = saveOption.build(parentOption,optionId,storeId);
            return storeOptionRepository.saveOption(option).pcId();
        }
        StoreOption option = saveOption.build(parentOption,optionId,storeId);
        return storeOptionRepository.updateOption(option).pcId();
    }






}

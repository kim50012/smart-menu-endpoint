package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.domain.ProductRepository;
import com.basoft.eorder.domain.model.ProductGroup;
import com.basoft.eorder.interfaces.command.UpProductGroupAdm;
import com.basoft.eorder.interfaces.command.UpadateProductName;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@CommandHandler.AutoCommandHandler("AdminCommandHandler")
public class AdminCommandHandler {
    @Autowired
    private BaseService baseService;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QueryHandler queryHandler;

    AdminCommandHandler() {
    }

    public AdminCommandHandler(BaseService serivce, UidGenerator uidGenerator1) {
        this.baseService = serivce;
        this.uidGenerator = uidGenerator1;
    }

    /**
     * 商品分类的新增和修改
     * 商品标签的新增和修改
     * 产品标签的新增和修改
     *
     * @param saveCategory
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveCategory.NAME)
    public Long saveCategory(SaveCategory saveCategory) {
        Category parent = baseService.getCategory(saveCategory.getParentId());
        if (parent == null) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        Long id = saveCategory.getId();
        // 新增
        if (id == null) {
            id = uidGenerator.generate(BusinessTypeEnum.CATEGORY);
            Category category = saveCategory.build(parent, id);
            baseService.saveCategory(category);
        }
        // 修改
        else {
            Category newCategory = saveCategory.build(parent, id);
            baseService.updateCategory(newCategory);
            return newCategory.id();
        }
        return id;
    }

    /**
     * 删除分类
     * 状态改为3
     *
     * @param updateCategory
     * @return
     */
    @CommandHandler.AutoCommandHandler(UpdateCategory.DEL_NAME)
    public Long delCategory(UpdateCategory updateCategory) {
        Category category = updateCategory.build();
        Category parent = baseService.getCategory(updateCategory.getId());
        if (parent.getChildren() != null && parent.getChildren().size() > 0) {
            throw new BizException(ErrorCode.CATEGORY_HAS_CHILD);
        }
        Long id = baseService.upCategoryStatus(category);
        return id;
    }

    /* @Transactional
    @CommandHandler.AutoCommandHandler(StopCategory.NAME)
    public Long stopCategoryStatus(StopCategory stopCategory){
        return null;
    }*/

    /* @Transactional
    public String batchUpdateProductName(BachUpdateProductName bup){
        List<UpdateProudctEvent> proudctEventList = bup.build();
        baseService.batchUpdateProductName(proudctEventList);
        return "ok";
    }*/

    /* @Transactional
    @CommandHandler.AutoCommandHandler(UpdateCategory.NAME)
    public Long updateCategory(UpdateCategory updateCategory){
        Category parent = baseService.getCategory(updateCategory.getParentId());
        Category category = baseService.getCategory(updateCategory.getId());
        Category newCategory = updateCategory.build(parent,category.id());
        baseService.updateCategory(newCategory);
        return newCategory.id();
    }*/

    @Transactional
    @CommandHandler.AutoCommandHandler(SaveOption.NAME)
    public Long saveOption(SaveOption option) {

        Long optionId = option.getId();
        Option parentOption = this.baseService.getOption(option.getParentId());

        if (parentOption == null) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (optionId == null) {
            optionId = uidGenerator.generate(BusinessTypeEnum.OPTION);
            Option option1 = option.build(parentOption, optionId);
            return baseService.saveOption(option1).id();
        }
        Option option1 = option.build(parentOption, optionId);
        return baseService.updateOption(option1).id();
    }

    /**
     * 删除规格
     *
     * @param updateOption
     * @return
     */
    @Transactional
    @CommandHandler.AutoCommandHandler(UpdateOption.DEL_OPTION)
    public Long delOption(UpdateOption updateOption) {
        Option option = updateOption.build();
        Option parent = baseService.getOption(updateOption.getId());
        if (parent.children() != null && parent.children().size() > 0) {
            throw new BizException(ErrorCode.OPTION_HAS_CHILD);
        }
        return baseService.delOption(option);

    }



    /**
     * @param upProductGroupAdm
     * @return java.lang.Long
     * @describe 修改产品分类多语言
     * @author Dong Xifu
     * @date 2019/1/4 上午9:27
     */
    @Transactional
    @CommandHandler.AutoCommandHandler(UpProductGroupAdm.NAME)
    public Long upProdctGroup(UpProductGroupAdm upProductGroupAdm) {
        ProductGroup newGroup = upProductGroupAdm.buildAdmin(upProductGroupAdm, upProductGroupAdm.getId());
        return this.productRepository.updateProductGroupName(newGroup);
    }

    /**
     * @param upadateProductName
     * @return java.lang.Long
     * @describe 修改产品多语言
     * @author Dong Xifu
     * @date 2019/1/4 上午9:28
     */
    @Transactional
    @CommandHandler.AutoCommandHandler(UpadateProductName.NAME)
    public Long updateProductName(UpadateProductName upadateProductName) {
        /*String id="";
        int size = 10;
        int page = 1;
        String longitude = "";
        String latitude = "";
        String storeType = "1";
        String far = "";
        String query = "{ Stores(id:\""+id+"\",storeType:\""+storeType+"\",size:"+size+",page:"+page+",longitude:\""+longitude+"\",latitude:\""+latitude+"\",far:\""+far+"\" ){total,dataList{id,name,city,areaNm,detailAddr,detailAddrChn,description,descriptionChn,shopHour,mobile,logoUrl,status,managerPhone,managerAccount,managerName,merchantId,longitude,latitude,disdance,categoryList{id} }}}";

        String proQuery = "{ProductGroupMaps(recommend:\"1\",storeId:"+id+"){ nameChn,nameKor,mainUrl,psdList{nameKor,nameChn, priceChn}  } }";
       // QueryHandler queryHandler = context.getBean(QueryHandler.class);

        Map map = (Map) queryHandler.handle(query);
        if(StringUtils.isNotBlank(id)){
            String bannerQuery = "{ Banners(storeId:"+id+", size:"+size+", page:"+page+"){id,name, imagePath, created, storeId,showIndex,status}}";

            Map bannerMap = (Map) queryHandler.handle(bannerQuery);
            List<Map> listBanner = new LinkedList();
            if(bannerMap.size()>0)
                listBanner = (List<Map>)bannerMap.get("Banners");
            map.put("bannerList",listBanner);

            HashMap map1 = new HashMap();
            Object stores =  map.get("Stores"); Map storeMap =  (Map)stores;
            List<Map> storeList = (List<Map>) storeMap.get("dataList");
            Map proMap = (Map) queryHandler.handle(proQuery);
            storeList.get(0).put("productList",proMap.get("ProductGroupMaps"));
        }*/
        return this.productRepository.updateProductNAME(upadateProductName);
    }
}

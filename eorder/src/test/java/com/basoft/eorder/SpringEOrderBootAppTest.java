package com.basoft.eorder;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.CommandHandleException;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.application.wx.model.WxPayQueryResp;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.interfaces.command.*;
import com.basoft.eorder.interfaces.query.UserDTO;
import com.basoft.eorder.interfaces.query.UserQuery;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.UidGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.basoft.eorder.foundation.jdbc.service.CacheLoginService.REDIS_CMS_KEY;


public class SpringEOrderBootAppTest {




    private static ConfigurableApplicationContext context;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    @BeforeClass
    public static void setupClass() {

        final File file = Paths.get("./images-test").toFile();
        if(file.exists()){
            System.out.println("Delete file");
            file.delete();
        }

        //重新创建文件夹
        file.mkdir();


        String rootPath = "--"+ AppConfigure.BASOFT_UPLOAD_PATH+"=./images-test";
        context = new InnerSpringEOrderApplicationBoot().run(rootPath);
    }

//    @AfterClass
    public static void tearDownClass() {
        context.stop();
    }

    public void testSaveProduct(){



        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);


        UserRepository ur = context.getBean(UserRepository.class);
        StoreRepository sr = context.getBean(StoreRepository.class);
        UidGenerator uidGenerator = context.getBean(UidGenerator.class);

        Long userId = uidGenerator.generate(BusinessTypeEnum.USER);
        CreateUser cUser = getDefaultCreatUser();
        User newUser = cUser.buildForNew(userId);
        Long newStoreId=null;

        try {

            ur.insertUser(newUser);

            newStoreId = uidGenerator.generate(BusinessTypeEnum.STORE);

            SaveStore saveStore = newSaveStore();
            saveStore.setId(newStoreId);
            saveStore.setManagerId(userId);
            Store store = saveStore.build(new Store(),newUser, newStoreId);
            sr.saveStore(store);


            SaveProduct.SaveProductSku sku = new SaveProduct.SaveProductSku();
            sku.setName("test-sku");
            sku.setSalesPrice(new BigDecimal("12.23"));
//            sku.setStandardVal("100=DA,200=putong");
            final List<Long> options = Arrays.asList(new Long(100));
            sku.setOptionIds(options);

            SaveProduct sp = new SaveProduct();
//            sp.setCategoryId(new Long(1));
            sp.setName("火锅");


            sp.addSku(sku);

            Map<String,Object> props = new HashMap<>();
            props.put("store_id","1");

            CommandHandlerContext context = handleEngine.newCommandHandlerContext(SaveProduct.NAME,props);
            final Object productObject = handleEngine.getCommandHandler().exec(sp,context);

            TestCase.assertNotNull(productObject);
//            TestCase.assertEquals(Product.class,productObject.getClass());

//            Product product = (Product) productObject;
//            ProductRepository pr = context.getBean(ProductRepository.class);
//            Product newProduct = pr.getProduct(product.id());

//            TestCase.assertEquals(product.);

        } catch (CommandHandleException e) {
//            TestCase.assertEquals();
            e.printStackTrace();
        }
        finally {
            ur.deleteUser(userId);
            if(newStoreId != null){
                sr.deleteStore(newStoreId);
            }


        }
    }


    /**
     * @author woonill
     * 测试创建商店桌子的信息和更新桌子信息
     */
    public void testSaveStoreTable(){

        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);
        UserRepository uRepo = context.getBean(UserRepository.class);
        UidGenerator uidGenerator = context.getBean(UidGenerator.class);
        final StoreRepository storeRepo = context.getBean(StoreRepository.class);

        Long newUserId  = uidGenerator.generate(BusinessTypeEnum.USER);
        Long newStoreId = null;
        Long newStoreTableId = null;

        try{

            //新建用户信息
            CreateUser cu = getDefaultCreatUser();
            User user = cu.buildForNew(newUserId);
            uRepo.insertUser(user);

            newStoreId = uidGenerator.generate(BusinessTypeEnum.STORE);
            SaveStore saveStore = newSaveStore();
            saveStore.setId(newStoreId);
            saveStore.setManagerId(newUserId);
            Store store = saveStore.build(new Store(), user, newStoreId);
            storeRepo.saveStore(store);


            SaveStoreTable sst = new SaveStoreTable();
            sst.setId(newStoreTableId);
            sst.setNumber(1);
            sst.setOrder(1);
            sst.setStoreId(newStoreId);

            final CommandHandleEngine engine = context.getBean(CommandHandleEngine.class);
            engine.getCommandHandler().exec(sst,engine.newCommandHandlerContext(SaveStoreTable.NAME));

        }
        catch (Throwable te){
            te.printStackTrace();

        }
        finally {
            uRepo.deleteUser(newUserId);
            if(newStoreId != null){
                storeRepo.deleteStore(newStoreId);
            }
            if(newStoreTableId != null){
                storeRepo.deleteStoreTable(newStoreTableId);
            }
        }
    }



    /**
     * @author woonill
     *
     * 创建 Banner 和 更新 Banner Test
     */

    public void testSaveBanner(){

        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);
        UserRepository uRepo = context.getBean(UserRepository.class);
        UidGenerator uidGenerator = context.getBean(UidGenerator.class);
        final StoreRepository storeRepo = context.getBean(StoreRepository.class);

        Long userId = uidGenerator.generate(BusinessTypeEnum.USER);
        Long storeId= uidGenerator.generate(BusinessTypeEnum.STORE);

        CreateUser createUser = getDefaultCreatUser();
        User user  = createUser.buildForNew(userId);

        try{
            uRepo.insertUserNamedPkValue(user);

            SaveStore saveStore = newSaveStore();
            saveStore.setManagerId(userId);
            saveStore.setId(storeId);

            storeRepo.saveStore(saveStore.build(new Store(),user, storeId));

            SaveBanner sb = new SaveBanner();
            sb.setImageUrl("http://baidu.com/a-image.png");
            sb.setName("aaa");


            final CommandHandleEngine engine = context.getBean(CommandHandleEngine.class);
            CommandHandlerContext context = engine.newCommandHandlerContext(SaveBanner.NAME);
            handleEngine.getCommandHandler().exec(sb,context);




        }
        catch (Throwable te){
            te.printStackTrace();
        }
        finally {
            uRepo.deleteUser(userId);
            storeRepo.deleteStore(storeId);
        }
    }

    /**
     * @author woonill
     * 测试保存商店信息
     */
    public void testSaveStore(){


        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);
        UserRepository uRepo = context.getBean(UserRepository.class);
        UidGenerator uidGenerator = context.getBean(UidGenerator.class);

        CreateUser createUser = getDefaultCreatUser();
        User user  = createUser.buildForNew(uidGenerator.generate(BusinessTypeEnum.USER));
        Long id= uRepo.insertUserNamedPkValue(user);

        try {

            Long storeId= uidGenerator.generate(BusinessTypeEnum.STORE);
            SaveStore saveStore = newSaveStore();
            saveStore.setManagerId(id);
            saveStore.setId(storeId);

            CommandHandlerContext context = handleEngine.newCommandHandlerContext(SaveStore.NAME);
            handleEngine.getCommandHandler().exec(saveStore,context);
        } catch (CommandHandleException e) {
            e.printStackTrace();
        }

    }

    /**
     * @author woonill
     * 测试更新商店信息
     *
     */
    public void testUpdateStore(){

        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);
        UserRepository uRepo = context.getBean(UserRepository.class);

        UidGenerator uidGenerator = context.getBean(UidGenerator.class);

        CreateUser createUser = getDefaultCreatUser();
        Long newUserId = uidGenerator.generate(BusinessTypeEnum.USER);
        User user  = createUser.buildForNew(newUserId);
        Long storeId = uidGenerator.generate(BusinessTypeEnum.STORE);

        try {
            uRepo.insertUserNamedPkValue(user);
            SaveStore saveStore = newSaveStore();
            saveStore.setId(storeId);
            saveStore.setManagerId(newUserId);

            saveStore.setManagerId(newUserId);


            CommandHandlerContext context = handleEngine.newCommandHandlerContext(SaveStore.NAME);
            Long storeId2 = (Long) handleEngine.getCommandHandler().exec(saveStore,context);
            TestCase.assertEquals(storeId,storeId2);



        } catch (CommandHandleException e) {
            e.printStackTrace();
        }
        finally {
            uRepo.deleteUser(newUserId);


        }

    }

    private SaveStore newSaveStore() {

        SaveStore saveStore = new SaveStore();
        saveStore.setName("test store");
        saveStore.setEmail("a@email.com");
        saveStore.setCity("jeju");
        saveStore.setCeo("woonill");
        saveStore.setPhone("122-331-3333");
        saveStore.setScope("sales");
        return saveStore;
    }


    private CreateUser getDefaultCreatUser(){
        CreateUser createUser = new CreateUser();
        createUser.setName("liminzhe");
        createUser.setAccount("testtesttest01");
        createUser.setPassword("123456");
        createUser.setMobile("18600457009");
        return createUser;
    }

    public void testSaveUser(){
        final CommandHandleEngine handleEngine = context.getBean(CommandHandleEngine.class);
        try {
            CreateUser createUser = new CreateUser();
            createUser.setName("liminzhe");
            createUser.setAccount("testtesttest01");
            createUser.setPassword("123456");
            createUser.setMobile("18600457009");
            createUser.setEmail("wonill@aliyun.com");

            final CommandHandlerContext context = handleEngine.newCommandHandlerContext(CreateUser.NAME);
            final Object exec = handleEngine.getCommandHandler().exec(createUser,context);
            TestCase.assertEquals("ok",exec);
        } catch (CommandHandleException e) {
//            TestCase.assertEquals();
            e.printStackTrace();
        }
    }

    public void testUserById() {
        UserQuery uq = context.getBean(UserQuery.class);

        UserDTO user = uq.getUserById(new Long(123456789));

        System.out.println(user.toString());
    }

    public void testUser() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        final Object handle = bean.handle("{User(id:123456789){id, name, account, mobile, created}, Users(name:\"liminzhe\", size:10, page:0){id, name, account, mobile, created}}");

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;
        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(handle);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testWeMain() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        String query = "{ProductGroups(storeId:1){id, nameKor, nameChn, storeId, status, showIndex, created}, Products(storeId:1){id, storeId, categoryId, nameKor, nameChn, status, created, categoryName, psdList{id, nameKor, nameChn, productId, price, created}}}";
        final Object handle = bean.handle(query);

        ObjectMapper om = new ObjectMapper();

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;

        List<Map> groups = (List<Map>) map.get("ProductGroups");
        List<Map> products = (List<Map>) map.get("Products");

        System.out.println(groups.get(0).getClass().getName());
        System.out.println(products.getClass().getName());


        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product -> {
            return NumberUtils.toLong(Objects.toString(product.get("categoryId"), null));
        }));

        try {
            System.out.println(om.writeValueAsString(groupMap));
        } catch (Exception e) {

        }

        List<Map> newgroups =

        groups.stream().map(new Function<Map, Map>() {
            public Map apply(Map group){
                Long id = NumberUtils.toLong(Objects.toString( group.get("id"), null));
                List<Map> mapList = groupMap.get(id);
                  if (mapList != null && mapList.size() > 0) {
                    group.put("products", mapList);
                }
                return group;
            }
        }).collect(Collectors.toList());


//        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(newgroups);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testBanner() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        final Object handle = bean.handle("{Banner(id:514665313360221185){id, name, imagePath, storeId, created, showIndex}, Banners(storeId:515407223930557443){id, name, imagePath, storeId, created, showIndex}}");

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;
        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(handle);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testStore() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        final Object handle = bean.handle("{Store(id:1){id, name, city, email, mobile, ceoName, bizScope, managerId, created, storeTableList{id, storeId, number, showIndex, qrcodeImageUrl, created}}}");

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;
        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(handle);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testStoreTable() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        final Object handle = bean.handle("{StoreTable(id:514680459755852809){id, storeId, number, showIndex, qrcodeImageUrl, created}, StoreTables(storeId:515477001613415428){id, storeId, number, showIndex, qrcodeImageUrl, created}}");

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;
        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(handle);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testGetCategory() {
        final QueryHandler bean = context.getBean(QueryHandler.class);
        final Object handle = bean.handle(" {RootCategory {id,name,children}}");

        System.out.println(handle);
    }


//    @Test
    public void testProductGroup(){

        String query = "{ProductGroups(storeId:1){id, nameKor, nameChn, storeId, status, showIndex}, Products(storeId:1){id, storeId, categoryId, nameKor, nameChn, status, created, categoryName,psdList{id, nameKor, nameChn, productId, price, created}}}";
        final QueryHandler bean = context.getBean(QueryHandler.class);
        Object result = bean.handle(query);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

//    @Test
    public void testProductGroupQuery(){

        String query = "{ProductGroups(storeId:1){... on ProductGroup{id, nameKor, nameChn, storeId, status, showIndex}}}";
        final QueryHandler bean = context.getBean(QueryHandler.class);
        Object result = bean.handle(query);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

//    @Test
    public void testCase(){
        System.out.println("\n\n\n\n");
        final QueryHandler bean = context.getBean(QueryHandler.class);
//        System.out.println(bean.handle("\"query\": \"{ productList { id, text, comments { id, text} } }\""  ));
//        System.out.println(bean.handle("{hello}"  ));
        final Object handle = bean.handle(" {Products{id}}");

        System.out.println(handle.getClass().getName());

        Map map = (Map)handle;

        map.put("name","woonill-test");

        List productList = (List) map.get("Products");
        System.out.println(productList.size());
        System.out.println(productList.get(0).getClass().getName());

//        Set<String> keys = map.keySet();
//        final Iterator<String> iterator = keys.iterator();
//        while (iterator.hasNext()){
//
//            String key = iterator.next();
//            Object value = map.get(key);
//            System.out.println("Key:"+key+" value:"+value+"  valueType:"+value.getClass().getName());
//            System.out.println(((Map)value).get("id"));
//
//        }
        ObjectMapper om = new ObjectMapper();
        try {
            String str = om.writeValueAsString(handle);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
//    @Test
    public void testLoop(){



        ArrayList<String> ss = new ArrayList<>();
        ss.add("A");
        ss.add("B");
        ss.add("C");
        ss.add("D");

        for(int i = 0;i<ss.size();i++){
            System.out.println(ss.get(i));
        }
        ss.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);

            }
        });

    }

//    @Test
    public void BAWX_CMS_TOKEN(){
        RedisUtil redisUtil = context.getBean(RedisUtil.class);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InJvb3QifQ.s20zlMcmydArPzU03LBiS3On0yFA_Gm2ZIfY7K0EQmE";
        Object o =  redisUtil.hget(REDIS_CMS_KEY,token);
        System.out.println(REDIS_CMS_KEY+"="+o);
    }



//        @Test
        public void TestMain(){

            Long sid =  535049498704811014L;
            String query = "{ProductGroups(storeId:" + sid + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + sid + ",status:\""+1+"\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
            QueryHandler qh =  context.getBean(QueryHandler.class);
            Map map = (Map) qh.handle(query);
            List<Map> groups = (List<Map>) ((Map)map.get("ProductGroups")).get("dataList");
            List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

            Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
                NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
            ));

            List<Map> groupList = groups
                .stream()
                .peek(group -> {
                    Long id = NumberUtils.toLong(Objects.toString( group.get("id"), null));
                    List<Map> mapList = groupMap.get(id);
                    if (mapList != null && mapList.size() > 0) {
                        if(mapList.size()>0)
                            group.put("products", mapList);
                    }
                })
                .collect(Collectors.toList());


            List<Map> hotProducts = products.stream().filter((hotMap) -> {
                Integer recommend = (Integer) hotMap.get("recommend");
                return recommend != null && recommend == 1;
            }).collect(Collectors.toList());


            if (0 < hotProducts.size()) {
                Map hotGroup = hotOfGroupMap(sid);
                hotGroup.put("products", hotProducts);
                groupList.add(0,hotGroup);
            }

            for (int i=0;i<groupList.size();i++) {

             System.out.println(groupList.get(i));
            }
        }




    private  Map hotOfGroupMap(Long storeId) {

        Map groupDTO = new LinkedHashMap();
        groupDTO.put("id", (long) -1);
        groupDTO.put("nameKor", "추천");
        groupDTO.put("nameChn", "火热");
        groupDTO.put("storeId", storeId);
        groupDTO.put("status", 1);
        groupDTO.put("showIndex", 0);
        groupDTO.put("created", java.util.Calendar.getInstance().getTime().toString());
        return groupDTO;
    }


//    @Test
    public void mainDataList() {
        String id="535049498704811014";
        int page=1;int size = 10;
        String recommend = "1";
        String query = "{ Stores(id:\""+id+"\",size:"+size+",page:"+page+"){total,dataList{id,name,city,areaNm,detailAddr,detailAddrChn,description,descriptionChn,shopHour,mobile,logoUrl,status,managerPhone,managerAccount,managerName,merchantId,longitude,latitude,categoryList{id} }}}";
        String proQuery = "{ProductGroupMaps(recommend:\""+recommend+"\",storeId:"+id+"){ nameChn,nameKor,mainUrl,psdList{nameKor,nameChn, priceChn}  } }";
        QueryHandler qh =  context.getBean(QueryHandler.class);
        Map map = (Map) qh.handle(query);

        if(StringUtils.isNotBlank(id)){
            String bannerQuery = "{ Banners(storeId:"+id+", size:"+size+", page:"+page+"){id,name, imagePath, created, storeId,showIndex,status}}";

            Map bannerMap = (Map) qh.handle(bannerQuery);
            List<Map> listBanner = new LinkedList();
            if(bannerMap.size()>0)
                listBanner = (List<Map>)bannerMap.get("Banners");
            map.put("dataList",listBanner);

            Object stores =  map.get("Stores"); Map storeMap =  (Map)stores;
            List<Map> storeList = (List<Map>) storeMap.get("dataList");
            Map proMap = (Map) qh.handle(proQuery);
            storeList.get(0).put("productList",proMap.get("ProductGroupMaps"));
        }

    }


    @Autowired
    private  OrderService orderService;

    @Test
    public void myorder() {
//        StoreRepository storeRepository = context.getBean(StoreRepository.class);
//
//
//        final QueryHandler qh = context.getBean(QueryHandler.class);
//        String customerId = "o2dki5tPJdb_nfjTefCxr7cZCn1I";
//        int page = 0;
//        int size = 10;
//        if (StringUtils.isBlank(customerId)) {
//            logger.error("<<<<<<< error : customerId is null");
//            throw new BizException(ErrorCode.PARAM_INVALID);
//        }
//
//        String query = "{Orders(customerId:\""+ customerId + "\",size:" + size + ",page:" + page + "){total, dataList{id, storeId, tableId, amount, paymentAmount,payAmtRmb, discountAmount, status, buyerMemo, customerId, created, payDts, updated, storeNm, logoUrl, tableNum, tableTag, itemList{id, orderId, skuId, skuNmKor, skuNmChn, price, qty, created}}}}";
//
//        Map map = (Map) qh.handle(query);
//        if (map == null) {
//            logger.info("========== my order is empty =========");
//        }
//
//        int total = NumberUtils.toInt(Objects.toString(((Map)map.get("Orders")).get("total")));
//        if (total == 0) {
//            logger.info("========== search my order result is 0 =========");
//        }
//        List<Map> dataList = (List<Map>) ((Map)map.get("Orders")).get("dataList");
//
//        if (dataList != null && dataList.size() > 0) {
//            dataList.stream().filter(p -> 1 == NumberUtils.toInt(Objects.toString(p.get("status"))) || 2 == NumberUtils.toInt(Objects.toString(p.get("status"))))
//                .forEach(m -> {
//                    try {
//                        Long storeId = NumberUtils.toLong(Objects.toString(m.get("storeId")));
//
//                        Store store = storeRepository.getStore(storeId);
//                        if (store != null ) {
//                            WxPayQueryResp queryResp = orderService.queryWechatOrder(store, m);
//                            if ("SUCCESS".equals(queryResp.getReturn_code())) {
//                                if ("SUCCESS".equals(queryResp.getResult_code())) {
//                                    if ("SUCCESS".equals(queryResp.getTrade_state())) {
//                                        m.put("status", "4");
//                                    } else if ("NOTPAY".equals(queryResp.getTrade_state())) {
//                                        m.put("status", "0");
//                                    } else if ("USERPAYING".equals(queryResp.getTrade_state())) {
//                                        m.put("status", "2");
//                                    } else if ("PAYERROR".equals(queryResp.getTrade_state())) {
//                                        m.put("status", "3");
//                                    }
//                                } else {
//                                    if ("ORDERNOTEXIST".equals(queryResp.getErr_code())) {
//                                        m.put("status", "3");
//                                    }
//                                }
//                            }
//                        }
//                        else {
//                            m.put("status", "3");
//                        }
//
//                    } catch (Exception e) {
//                        logger.error("<<<<< query order pay is error >>>>>", e);
//                    }
//                });
//        }else{
//            dataList = new LinkedList<>();
//            System.out.println(dataList);
//        }

        //return dataList;
    }
}

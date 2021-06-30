package com.basoft.eorder;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.framework.QueryHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:58 2019/3/14
 **/
public class productTest {
    @Autowired
    private static ConfigurableApplicationContext context;
    @Autowired
    private QueryHandler queryHandler;


    @BeforeClass
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

    @AfterClass
    public static void tearDownClass() {
        context.stop();
    }



//    @Test
    public void  productStore(){

        //QueryHandler queryHandler = context.getBean(QueryHandler.class);
        long sid = 581363347573511175L;
        String query = "{ProductGroups(storeId:" + sid + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + sid + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        Map map = (Map) queryHandler.handle(query);


        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
            NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
        ));

        List<Map> groupList = groups
            .stream()
            .peek(group -> {
                Long id = NumberUtils.toLong(Objects.toString(group.get("id"), null));
                List<Map> mapList = groupMap.get(id);
                if (mapList != null && mapList.size() > 0) {
                    if (mapList.size() > 0)
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
            groupList.add(0, hotGroup);
        }
    }





//    @Test
    public void  maindiscTest(){
        // 门店ID
        long storeId = 581363347573511175L;
        QueryHandler queryHandler = context.getBean(QueryHandler.class);

        // 查询折扣活动的产品列表
        List<LinkedHashMap<String,Object>> discountDisplayList = null;
        String queryDiscount = "{DiscountsDisplay(storeId:" + storeId + "){discId, discName, discChannel, custId, storeId, discRate, startTime,endTime,discDetId,prodId,prodSkuId,discPrice,discPriceChn}}";
        Map disCountMap = (Map) queryHandler.handle(queryDiscount);
        if (disCountMap == null) {

        } else {
            discountDisplayList = (List<LinkedHashMap<String,Object>>) disCountMap.get("DiscountsDisplay");
        }

        String query = "{ProductGroups(storeId:" + storeId + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + storeId + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        Map map = (Map) queryHandler.handle(query);
        if (map == null) {

        }

        // 产品组列表
        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        // 产品列表
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));
        System.out.println(products);
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
    public void  t1(){
       String str =  "qrscene_agent_736657847805088769";
       if(str.contains("_agent")){
           System.out.println(1111);
       }
        str.substring(14, str.length());
        System.out.println(str.substring(14, str.length()));
    }


//    @Test
    public void storesQuery() {
        String id="";
        int size = 10;
        int page = 1;
        String longitude = "";
        String latitude = "";
        String storeType = "1";
        String far = "";
        String query = "{ Stores(id:\""+id+"\",storeType:\""+storeType+"\",size:"+size+",page:"+page+",longitude:\""+longitude+"\",latitude:\""+latitude+"\",far:\""+far+"\" ){total,dataList{id,name,city,areaNm,detailAddr,detailAddrChn,description,descriptionChn,shopHour,mobile,logoUrl,status,managerPhone,managerAccount,managerName,merchantId,longitude,latitude,disdance,categoryList{id} }}}";

        String proQuery = "{ProductGroupMaps(recommend:\"1\",storeId:"+id+"){ nameChn,nameKor,mainUrl,psdList{nameKor,nameChn, priceChn}  } }";
        QueryHandler queryHandler = context.getBean(QueryHandler.class);
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

        }

    }


//    @Test
    public void testAnd(){
        int first=12;//-----------------00001100   值为 12
        int second=22;//----------------00010100   值为 22
        int three=first^second;//-------00000100   值为 4
        System.out.println(three);//---4
    }
}

package com.basoft.eorder;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.base.query.CategoryDTO;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.Standard;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.foundation.jdbc.base.JbcBaseServiceImpl;
import com.basoft.eorder.foundation.jdbc.base.JdbcBaseQueryFacade;
import com.basoft.eorder.foundation.jdbc.query.JdbcCategoryQueryImpl;
import com.basoft.eorder.foundation.jdbc.query.JdbcProductQueryImpl;
import com.basoft.eorder.foundation.jdbc.repo.JdbcProductRepoImpl;
import com.basoft.eorder.foundation.jdbc.repo.JdbcStoreRepository;
import com.basoft.eorder.interfaces.query.OptionDTO;
import com.basoft.eorder.interfaces.query.ProductGroupDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.StoreCategoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import junit.framework.TestCase;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JdbcRepoTest {



    DataSource getDataSource() {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setUrl("jdbc:mysql://127.0.0.1:3306/eorder?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;useSSL=true&amp;failOverReadOnly=false");
        mds.setUser("root");
        mds.setPassword("kdi5006");
        mds.setUseSSL(true);
//        mds.setDatabaseName("");
//        mds.setPortNumber(3307);
//        mds.setUser("bawechat");
//        mds.setPassword("1q2w3e4r!");
        return mds;
    }


//    @Test
    public void testStoreCategoryDTO(){


        BaseService pq = new JbcBaseServiceImpl(this.getDataSource());

        final Category category = pq.getCategory(new Long("526347856779219976"));
        System.out.println(category);

        JdbcCategoryQueryImpl jcqi = new JdbcCategoryQueryImpl(this.getDataSource(),pq);

//        final List<StoreCategoryDTO> categoryOfStore = jcqi.getCategoryOfStore(new Long(1));
//        System.out.println(categoryOfStore.size());


        final List<StoreCategoryDTO> categoryOfStore1 = jcqi.getCategoryOfStore(new Long("526353746579231753"));
        System.out.println(categoryOfStore1.size());
        System.out.println(categoryOfStore1.get(0).getName());
        System.out.println(categoryOfStore1.get(0).getChnName());

    }

//    @Test
    public void testOption(){

        BaseService pq = new JbcBaseServiceImpl(this.getDataSource());
        Option option = pq.getRootOption();
        printOption(option);
        System.out.println(option);
        System.out.println(option.children().size());

//        final Option option1 = option.get(new Long(526346075576275970));

    }


    void printOption(Option option){

        System.out.println(option.id());
        for(Option c:option.children()){
            printOption(c);
        }
    }



//    @Test
    public void testCategoryBuild(){

        Category.Builder sDepth = new Category.Builder()
                .id(new Long(2))
                .name("Root")
                .depth(0);



        Category.Builder fDepth = new Category.Builder()
                .id(new Long(1))
                .name("Root")
                .depth(0)
                .child(sDepth);

        Category root = new Category.Builder()
                .id(new Long(0))
                .name("Root")
                .depth(0)
                .child(fDepth)
                .build();

        

    }



//    @Test
    public void testProductGroupQuery(){

        ProductQuery pq = new JdbcProductQueryImpl(this.getDataSource());
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("storeId",new Long(1));

        final List<ProductGroupDTO> productGroupListByMap = pq.getProductGroupListByMap(param);
        System.out.println(productGroupListByMap.size());

        final ProductGroupDTO dto = productGroupListByMap.stream().findFirst().get();
        if(dto != null){
            try {
                System.out.println(new ObjectMapper().writeValueAsString(dto));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }


    }

//    @Test
    public void testQueryFacade(){

        JdbcBaseQueryFacade facade = new JdbcBaseQueryFacade(this.getDataSource());
        JbcBaseServiceImpl jbsi = new JbcBaseServiceImpl(this.getDataSource());
        String type = "";

        Category rootCategory = jbsi.getCategory(new Long(0));
        TestCase.assertNotNull(rootCategory);
        Long newId = new Long(1);
        Long newId2 = new Long(2);

        Category newCategory = new Category.Builder()
                .id(newId)
                .type(1)
                .name("korea-food")
                .chnName("韩餐馆")
                .parent(rootCategory)
                .build();

        try{
            jbsi.saveCategory(newCategory);

            final CategoryDTO rootCategory1 = facade.getRootCategory(type);
//            TestCase.assertEquals(rootCategory1.getChildren().size(),1);


            Category newCategory2 = new Category.Builder()
                    .id(newId2)
                    .name("korea-food")
                    .chnName("韩餐馆")
                    .parent(newCategory)
                    .build();
            jbsi.saveCategory(newCategory2);


            final CategoryDTO rootCategory2 = facade.getRootCategory(type);
  //          TestCase.assertEquals(rootCategory2.getChildren().get(0).getChildren().size(),1);


        }
        finally {
            jbsi.delete(newId);
            jbsi.delete(newId2);
        }

    }


//    @Test
    public void testQueryOption(){

        String type = "";
        JdbcBaseQueryFacade facade = new JdbcBaseQueryFacade(this.getDataSource());
        final OptionDTO rootOption = facade.getRootOption(type);

        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println(om.writeValueAsString(rootOption));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


//    @Test
    public void testSaveOption(){

        JbcBaseServiceImpl jbsi = new JbcBaseServiceImpl(this.getDataSource());


        Option parentId = jbsi.getOption(new Long("519741999551943688"));
        System.out.println("ss");


        Option option = new Option.Builder()
                .id(new Long(2))
                .name("test-name")
                .chnName("张三")
                .parent(
                        new Option.Builder()
                        .id(new Long(0))
                        .name("Root")
                        .chnName("Root")
                        .build()
                )
                .build();

        try {

            jbsi.saveOption(option);
        }
        catch (Throwable te){
            te.printStackTrace();
        }
        finally {
            jbsi.deleteOption(option.id());

        }
    }

//    @Test
    public void testGetProduct(){


        BaseService bs = new JbcBaseServiceImpl(this.getDataSource());
        StoreRepository sr = new JdbcStoreRepository(this.getDataSource(),bs);
        JdbcProductRepoImpl repo = new JdbcProductRepoImpl(this.getDataSource(),bs);

        Store store =sr.getStore(new Long("535049498704811014"));
        final List<Product> productListOfStore = repo.getProductListOfStore(store);
        System.out.println(productListOfStore.size());


        final Product product = repo.getProduct(store, new Long("535169855827284996"));
        final List<ProductSku> productSkuList = repo.getProductSkuList(product);
        System.out.println(productSkuList);

/*
        final Store store = sr.getStore(new Long("534247730072851464"));
        final Product product = repo.getProduct(store, new Long("534353011600069639"));
        System.out.println(product);
*/
    }


//    @Test
    public void testSaveProduct(){


        BaseService bs = new JbcBaseServiceImpl(this.getDataSource());
        JdbcProductRepoImpl repo = new JdbcProductRepoImpl(this.getDataSource(),bs);

        Product product = new Product.Builder()
                .id(new Long(1))
                .name("test-product")
                .desChn("好")
                .desKor("sss")
                .category(new Category.Builder().id(new Long(1)).build())
                .store(new Store.Builder().id(new Long(1)).build())
                .img(Product.SImage.mainOf("http://a.png"))
                .recommend(1)
                .showIndex(1)
                .build();


        Standard standard = new Standard(new Long(1),"1=a,2=b");
        ProductSku sku = new ProductSku.Builder().setId(new Long(1)).setPrice(new BigDecimal("123")).setName("test-name").setStandard(standard).build(product);
        List<ProductSku> skuList =new LinkedList<>();
        skuList.add(sku);
        repo.saveProduct(product,skuList);

    }

//    @Test
    public void testSaveCategory(){



        JbcBaseServiceImpl jbsi = new JbcBaseServiceImpl(this.getDataSource());

        Category rootCategory = jbsi.getCategory(new Long(0));
        TestCase.assertNotNull(rootCategory);
        Long newId = new Long(1);

        Category newCategory = new Category.Builder()
                .id(new Long(newId))
                .name("korea-food")
                .chnName("韩餐馆")
                .parent(rootCategory)
                .build();

        try{
            jbsi.saveCategory(newCategory);

            Category rCategory = jbsi.getCategory(newId);
            System.out.println("Category id:"+rCategory.id());


            Long parentId = rCategory.getParent().id();
            TestCase.assertEquals(new Long(0),parentId);


        }
        finally {
            jbsi.delete(newId);
        }
    }
}

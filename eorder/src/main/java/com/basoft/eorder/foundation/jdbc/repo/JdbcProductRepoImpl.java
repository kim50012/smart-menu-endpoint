package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.domain.ProductRepository;
import com.basoft.eorder.domain.model.*;
import com.basoft.eorder.interfaces.command.UpadateProductName;
import com.basoft.eorder.interfaces.command.UpdateProGroupStatus;
import com.basoft.eorder.interfaces.command.UpdateProSkuStatus;
import com.basoft.eorder.interfaces.command.UpdateProductStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author woonill
 * @since 2018/12/14
 */
@Repository
@Transactional
public class JdbcProductRepoImpl extends BaseRepository implements ProductRepository {
    private JdbcTemplate jdbcTemplate;
    private BaseService bs;

    JdbcProductRepoImpl() {
    }

    @Autowired
    public JdbcProductRepoImpl(DataSource ds,BaseService bs){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.bs = bs;
    }


    /**
     *
     * @param product
     */
    @Override
    @Transactional
    public Product saveProduct(Product product,List<ProductSku> skus) {


        this.jdbcTemplate.update("insert into product (id,store_id,category_id,name_kor,name_chn,weight,status,is_deposit,show_index,des_kor,des_chn,recommend,detail_desc_kor,detail_desc_chn,IS_STANDARD,IS_INVENTORY) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
            new Object[]{
                product.id(),
                    product.store().id(),
                    product.category().id(),
                    product.name(),
                    product.chnName(),
                    product.weight(),
                    product.status().code(),
                    product.isDeposit(),
                    product.showOption().getShowIndex(),
                    product.showOption().getDesKor(),
                    product.showOption().getDesChn(),
                    product.showOption().getRecommend(),
                    product.showOption().getDetailDescKor(),
                    product.showOption().getDetailDescChn(),
                    product.showOption().getIsStandard(),
                    product.showOption().getIsInventory()
            });

        this.updateProductImages(product);
        this.updateProductSku(product,skus);
        return product;
    }


    @Transactional
    public void updateProductImages(Product product) {
        List<Product.SImage> images = product.images();

        this.jdbcTemplate.update("delete from product_image where product_id = ?",product.id());
        this.jdbcTemplate.batchUpdate("insert into product_image (product_id,image_url,image_type) values(?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Product.SImage image = images.get(i);
                ps.setLong(1,product.id());
                ps.setString(2,image.getUrl());
                ps.setInt(3,image.getType());

            }

            @Override
            public int getBatchSize() {
                return images.size();
            }
        });
    }

    @Override
    @Transactional
    public void updateProduct(Product product,List<ProductSku> skus) {

       this.jdbcTemplate.update(
               "update product set category_id=?,  name_kor=?,name_chn=?,status=?,show_index=?,des_kor=?,des_chn=?,recommend=?,detail_desc_kor=?,detail_desc_chn=?,update_time=now() where id = ?",
                new Object[]{
                        product.category().id(),
                        product.name(),
                        product.chnName(),
                        product.status().code(),
                        product.showOption().getShowIndex(),
                        product.showOption().getDesKor(),
                        product.showOption().getDesChn(),
                        product.showOption().getRecommend(),
                        product.showOption().getDetailDescKor(),
                        product.showOption().getDetailDescChn(),
                        product.id(),
                });

       this.updateProductImages(product);
       this.updateProductSku(product,skus);
    }

    /**
     * @param  product
     * @return java.lang.Long
     * @describe 修改产品名称
     * @author Dong Xifu
     * @date 2018/12/31 上午10:51
     */
    @Override
    public Long updateProductNAME(UpadateProductName product) {
        this.jdbcTemplate.update(
            "update product set name_kor=?,name_chn=?,des_kor=?,des_chn=?,detail_desc_kor=?," +
                "detail_desc_chn=?,status=?,update_time=now() where id = ? and store_id = ?",
            new Object[]{
                product.getNameKor(),
                product.getNameChn(),
                product.getDesKor(),
                product.getDesChn(),
                product.getDetailDesc(),
                product.getDetailChnDesc(),
                product.getStatus(),
                product.getId(),
                product.getStoreId()
            });
        return product.getId();
    }

    @Override
    public int updateProductStatus(UpdateProductStatus productStatus, Long storeId) {
        this.jdbcTemplate.batchUpdate("update product set status = ? where id=? and store_id= ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Long id = productStatus.getProductIds().get(i);
                preparedStatement.setInt(1,productStatus.getStatus());
                preparedStatement.setLong(2,id);
                preparedStatement.setLong(3,storeId);
            }

            @Override
            public int getBatchSize() {
                return productStatus.getProductIds().size();
            }
        });
        return productStatus.getProductIds().size();
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        this.jdbcTemplate.update("update product set status=? where id = ? ",Product.Status.DELETED.code(),id);
    }


    @Override
    public List<ProductSku> getProductSkuList(Product product) {

        logger.debug("ProductID:"+product.id());

        final List<ProductSkuWrapper> wrappers = this.jdbcTemplate.query(
                SELECT_PRODUCT_SKU_SQL,
                new Object[]{product.id()},
                getProductSkuWrapper());


        return toProductSku(wrappers,product);

    }

/*    @Override
    public List<ProductDTO> getProductCountByGroup(Map<String,Object> param,UpdateProGroupStatus proGroupStatus) {

        StringBuilder buf = new StringBuilder("SELECT p.id,p.name_kor as nameKor,p.name_chn as nameChn from  product_prdgroup_map prm join product p on prm.product_id=p.id WHERE prd_group_id in (");

        for(int i = 0; i < proGroupStatus.getProductGroupIds().size(); i++) {
            if (i > 0)
                buf.append(",");
            buf.append(proGroupStatus.getProductGroupIds().get(i));
        }
        buf.append(")");

        List<ProductDTO> productDTOList =  this.getNamedParameterJdbcTemplate().query(buf.toString(), param, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
       return productDTOList;
    }*/


    @Override
    @Transactional
    public Long saveProductGroup(ProductGroup newGroup) {

        updateMenuItem(newGroup);
        this.jdbcTemplate.update(
                "insert into product_group (id,name_kor,name_chn,store_id,status,show_index) values(?,?,?,?,?,?)",
                newGroup.id(),newGroup.name(),newGroup.chnName(),newGroup.storeId(),newGroup.status().getCode(),newGroup.showIndex());

        return newGroup.id();
    }

    @Transactional
    public void updateMenuItem(ProductGroup newGroup) {

        final List<MenuItem> items = newGroup.items();
        this.jdbcTemplate.update("delete from product_prdgroup_map where prd_group_id =?",newGroup.id());
        this.jdbcTemplate.batchUpdate(
                "insert into product_prdgroup_map (product_id,prd_group_id,show_index) values(?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MenuItem item = items.get(i);
                        ps.setLong(1,item.productId());
                        ps.setLong(2,newGroup.id());
                        ps.setInt(3,item.showIndex());
                    }
                    @Override
                    public int getBatchSize() {
                        return items.size();
                    }
                });
    }

    @Override
    @Transactional
    public Long updateProductGroup(ProductGroup newGroup) {
        updateMenuItem(newGroup);
        this.jdbcTemplate.update(
                "update product_group set name_kor = ? ,name_chn = ?,show_index = ?,status = ?,update_time=now() where id = ? and store_id = ?",
                newGroup.name(),newGroup.chnName(),newGroup.showIndex(),newGroup.status().getCode(),newGroup.id(),newGroup.storeId()
        );
        return newGroup.id();
    }

    @Override
    public Long updateProductGroupName(ProductGroup newGroup) {
        this.jdbcTemplate.update(
            "update product_group set name_kor = ? ,name_chn = ?,status = ?  where id = ? and store_id = ?",
            newGroup.name(),newGroup.chnName(),newGroup.status().getCode(),newGroup.id(),newGroup.storeId()
        );
        return newGroup.id();
    }

    final String GET_PRODUCT_GROUP_SQL ="select" +
            "   pp.id," +
            "   pp.name_kor," +
            "   pp.name_chn," +
            "   pp.status," +
            "   pp.show_index," +
            "   ppm.product_id  as productId," +
            "   ppm.category_id as categoryId," +
            "   ppm.show_index as menuShowIndex" +
            " from product_group pp" +
            " left join (" +
            "        select pmap.product_id,p.category_id,pmap.prd_group_id,pmap.show_index" +
            "          from product_prdgroup_map pmap" +
            "          join product p on pmap.product_id = p.id" +
            "    ) as ppm on ppm.prd_group_id = pp.id" +
            " where pp.store_id=? and pp.status != 2 and pp.status != 3";

    final ProductGroupWrapperMapper productGroupRowMapper = new ProductGroupWrapperMapper();


    @Override
    public int updateProductGroupStatus(UpdateProGroupStatus proGroupStatus, Long storeId) {
        this.jdbcTemplate.batchUpdate("update product_group set status =? where id=? and store_id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Long id = proGroupStatus.getProductGroupIds().get(i);
                preparedStatement.setInt(1,proGroupStatus.getStatus());
                preparedStatement.setLong(2,id);
                preparedStatement.setLong(3,storeId);
            }

            @Override
            public int getBatchSize() {
                return proGroupStatus.getProductGroupIds().size();
            }
        });
        return proGroupStatus.getProductGroupIds().size();
    }

    @Override
    public List<ProductGroup> getGroupOfStore(Store store) {
        final Map<Long,ProductGroup.Builder> builderMap = new HashMap<>();
        this.jdbcTemplate.query(GET_PRODUCT_GROUP_SQL, new Object[]{store.id()}, productGroupRowMapper)
                .stream()
                .forEach(new Consumer<ProductGroupWrapper>() {
                    @Override
                    public void accept(ProductGroupWrapper pgw) {

                        ProductGroup.Builder builder = builderMap.get(pgw.id);
                        if(builder == null){
                            builder = pgw.build();
                            builderMap.put(pgw.id,builder);
                        }
                        final MenuItem menuItem = pgw.newItem();
                        if(menuItem != null){
                            builder.item(menuItem);
                        }
                    }
                });

        if(builderMap.isEmpty()){
            return Collections.emptyList();
        }
        return builderMap.values()
                .stream()
                .map((s) -> s.storeId(store.id()).build())
                .collect(Collectors.toList());
    }

    private List<ProductSku> toProductSku(List<ProductSkuWrapper> wrappers, Product product) {
        Map<Long,ProductSku.Builder> builderMap = new HashMap<>();
        wrappers.stream().forEach(new Consumer<ProductSkuWrapper>() {
            @Override
            public void accept(ProductSkuWrapper psw) {
                ProductSku.Builder builder = builderMap.get(psw.skuId);
                if(builder == null){
                    builder = new ProductSku.Builder()
                            .setId(psw.skuId)
                            .setName(psw.skuName)
                            .setChnName(psw.skuNameChn)
                            .setUseDefault(psw.useDefault)
                            .setStatus(ProductSku.Status.get(psw.status))
                            .setPrice(psw.skuPrice);
                    builderMap.put(psw.skuId,builder);
                }
                builder.setStandard(new Standard(psw.standardId,psw.standardNameProp));
            }
        });

        return builderMap.values()
                .stream()
                .map(new Function<ProductSku.Builder, ProductSku>() {
                    @Override
                    public ProductSku apply(ProductSku.Builder builder) {
                        return builder.build(product);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param product
     * @param skus
     * @return
     *
     */
    public Long[] updateProductSku(Product product, List<ProductSku> skus) {
        final List<SkuStandardMap> collect = skus.stream()
                .flatMap(new Function<ProductSku, Stream<SkuStandardMap>>() {
                    @Override
                    public Stream<SkuStandardMap> apply(ProductSku productSku) {
                        return productSku.standarCollection()
                                .stream()
                                .map(new Function<Standard, SkuStandardMap>() {
                                    @Override
                                    public SkuStandardMap apply(Standard standard) {
                                        SkuStandardMap map = new SkuStandardMap();
                                        map.productId = product.id();
                                        map.standardId = standard.code();
                                        map.skuId = productSku.id();
                                        map.name_kor = standard.name();
                                        map.name_chn = standard.chnName();
                                        map.standardType = standard.standardType();
                                        return map;
                                    }
                                });
                    }
                })
                .collect(Collectors.toList());


        this.jdbcTemplate.update("delete from product_sku where product_id =?",product.id());
        this.jdbcTemplate.update("delete from sku_standard_map where product_id = ?",product.id());

        this.jdbcTemplate.batchUpdate(
                "insert into product_sku (id,name_kor,name_chn,price,price_weekend,PRICE_SETTLE,product_id,WEIGHT,use_default,IS_INVENTORY,DIS_ORDER,status) values(?,?,?,?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ProductSku sku = skus.get(i);
                        logger.info("id:"+sku.id()+"  name:"+sku.name());
                        int num = 1;
                        ps.setLong(num++,sku.id());
                        ps.setString(num++,sku.name());
                        ps.setString(num++,sku.chnName());
                        ps.setBigDecimal(num++,sku.price());
                        ps.setBigDecimal(num++,sku.priceWeekend());
                        ps.setBigDecimal(num++,sku.priceSettle());
                        // ps.setString(4,sku.standards().toContentsString());
                        ps.setLong(num++,product.id());
                        ps.setBigDecimal(num++,sku.weight());
                        ps.setBoolean(num++,sku.useDefault());
                        ps.setInt(num++,sku.isInventory());
                        ps.setInt(num++,sku.disOrder());
                        ps.setInt(num++,sku.status().getCode());
                    }
                    @Override
                    public int getBatchSize() {
                        return skus.size();
                    }
        });

        this.jdbcTemplate.batchUpdate(
                "insert into sku_standard_map (sku_id,prd_standard_id,prd_standard_name_val,name_chn,product_id,STANDARD_TYPE) values(?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        SkuStandardMap ssm = collect.get(i);
                        ps.setLong(1,ssm.skuId);
                        ps.setLong(2,ssm.standardId);
                        ps.setString(3,ssm.name_kor);
                        ps.setString(4,ssm.name_chn);
                        ps.setLong(5,ssm.productId);
                        ps.setInt(6,ssm.standardType);
                    }

                    @Override
                    public int getBatchSize() {
                        return collect.size();
                    }
        });

        return skus.stream()
                .map((ProductSku sku) -> {return sku.id();})
                .collect(Collectors.toList())
                .toArray(new Long[skus.size()]);
    }


    final String sql =
            " select p.*,"+
                    " pi.image_url as imageUrl,"+
                    " pi.image_type as imageType"+
                    " from product p"+
                    " left outer join product_image pi on p.id = pi.product_id"+
                    " where p.id = ?";

    @Override
    @Transactional
    public Product getProduct(Store store,Long id) {
        Category c = this.bs.getRootCategory();
        final Map<Long,Product.Builder> listMap  = new HashMap<>();
         this.jdbcTemplate.query(
                    sql,
                    prodMapper,
                    new Object[]{id}
                )
                .stream()
                .forEach(new Consumer<ProductColumnWrapper>() {
                    @Override
                    public void accept(ProductColumnWrapper pcw) {
                        Product.Builder builder = listMap.get(pcw.id);
                        if(builder == null){
                            builder = pcw.newProductBuilder();
                            builder.store(store).category(c.getCategory(pcw.categoryId));
                            listMap.put(pcw.id,builder);

                        }
                        Product.SImage image = pcw.newImage();
                        builder.img(image);
                    }
                });

         if(listMap.isEmpty()){
             return null;
         }

         return listMap.values().stream().map((pbuilder) -> pbuilder.build()).findFirst().orElseGet(new Supplier<Product>() {
             @Override
             public Product get() {
                 return null;
             }
         });

    }

    @Override
    public List<Product> getProductListOfStore(Store store) {
        String sql =
                " select p.*," +
                        " pi.image_url as imageUrl," +
                        " pi.image_type as imageType" +
                        " from product p" +
                        " left outer join product_image pi on p.id = pi.product_id" +
                        " where p.store_id = ?";

        Category c = this.bs.getRootCategory();
        final Map<Long, Product.Builder> listMap = new HashMap<>();
        this.jdbcTemplate.query(
                sql,
                prodMapper,
                new Object[]{store.id()}
        )
                .stream()
                .forEach(new Consumer<ProductColumnWrapper>() {
                    @Override
                    public void accept(ProductColumnWrapper pcw) {
                        Product.Builder builder = listMap.get(pcw.id);
                        if (builder == null) {
                            builder = pcw.newProductBuilder();
                            builder.store(store).category(c.getCategory(pcw.categoryId));
                            listMap.put(pcw.id, builder);

                        }
                        Product.SImage image = pcw.newImage();
                        builder.img(image);
                    }
                });

        if (listMap.isEmpty()) {
            return Collections.emptyList();
        }
        return listMap.values().stream().map((pbuilder) -> pbuilder.build()).collect(Collectors.toList());
    }

    static final ProductColumnMapper prodMapper = new ProductColumnMapper();

    static final class SkuStandardMap {
        private Long skuId;
        private Long standardId;
        private String name_kor;
        private String name_chn;
        private Long productId;
        private int standardType;
    }

    static final String SELECT_PRODUCT_SKU_SQL = "select" +
            "   ps.id as skuId," +
            "   ps.price as skuPrice," +
            "   ps.name_kor as skuName," +
            "   ps.name_chn as skuNameChn," +
            "   ps.use_default as useDefault," +
            "   ps.status as status," +
            "   p.id as productId," +
            "   p.name_kor as productName," +
            "   p.status as productStatus," +
            "   ssm.prd_standard_id as standardId," +
            "   ssm.prd_standard_name_val as standardNameProp" +
            " from product_sku ps" +
            "  join product p on ps.product_id = p.id" +
            "  left join sku_standard_map ssm on ssm.sku_id = ps.id" +
            // " where ps.product_id = ? and p.status != "+Product.Status.CLOSED.code();
            " where ps.product_id = ? ";


    static final RowMapper<ProductSkuWrapper> getProductSkuWrapper(){
        return new RowMapper<ProductSkuWrapper>(){
            @Override
            public ProductSkuWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                ProductSkuWrapper psw = new ProductSkuWrapper();
                psw.skuId = resultSet.getLong("skuId");
                psw.skuPrice = resultSet.getBigDecimal("skuPrice");
                psw.skuName = resultSet.getString("skuName");
                psw.skuNameChn = resultSet.getString("skuNameChn");
                psw.productId = resultSet.getLong("productId");
                psw.productName = resultSet.getString("productName");
                psw.productStatus= resultSet.getInt("productStatus");
                psw.useDefault= resultSet.getBoolean("useDefault");
                psw.standardId = resultSet.getLong("standardId");
                psw.standardNameProp = resultSet.getString("standardNameProp");

                return psw;
            }
        };
    }

    static final class ProductGroupWrapperMapper implements RowMapper<ProductGroupWrapper>{
        @Override
        public ProductGroupWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductGroupWrapper pgw = new ProductGroupWrapper();
            pgw.id = resultSet.getLong("id");
            pgw.nameKor = resultSet.getString("name_kor");
            pgw.nameChn = resultSet.getString("name_chn");
            pgw.showIndex = resultSet.getInt("show_index");
            pgw.status = resultSet.getInt("status");
            pgw.productId = resultSet.getLong("productId");
            pgw.categoryId = resultSet.getLong("categoryId");
            pgw.menuItemShowIndex = resultSet.getInt("menuShowIndex");
            return pgw;
        }
    }

    static final class ProductGroupWrapper {
        private Long id;
        private String nameKor;
        private String nameChn;
        private Integer status;
        private Integer showIndex;
        private Long productId;
        private Long categoryId;
        private Integer menuItemShowIndex;

        public ProductGroup.Builder build() {
            return new ProductGroup.Builder().id(id).name(nameKor).chnName(this.nameChn).showIndex(this.showIndex).status(ProductGroup.Status.get(this.status));
        }

        public MenuItem newItem() {
            if(this.productId == 0){
                return null;
            }
            return new MenuItem(this.productId,categoryId,menuItemShowIndex);
        }
    }

    static final class ProductSkuWrapper{
        private Long skuId;
        private BigDecimal skuPrice;
        private String skuName;
        private String skuNameChn;
        private Long productId;
        private String productName;
        private int productStatus;
        private int status;
        private Boolean useDefault;
        private Long standardId;
        private String standardNameProp;
    }

    static final class ProductColumnWrapper {
        private Long id;
        private Long categoryId;
        private String name;
        private String chnName;
        private int showIndex;
        private int recommend;
        private String desKor;
        private String desChn;
        private String detail_desc_kor;
        private String detail_desc_chn;
        private int status;
        private String imageUrl;
        private int imageType;

        public Product.Builder newProductBuilder() {
            return new Product.Builder()
                    .id(this.id)
                    .name(this.name)
                    .chnName(this.chnName)
                    .showIndex(this.showIndex)
                    .recommend(this.recommend)
                    .detailDescKor(this.detail_desc_kor)
                    .detailDescChn(this.detail_desc_chn)
                    .desKor(this.desKor)
                    .desChn(this.desChn);
        }
        public Product.SImage newImage() {
            return Product.SImage.of(this.imageUrl,this.imageType);
        }

    }

    static final class ProductColumnMapper implements RowMapper<ProductColumnWrapper>{
        @Override
        public ProductColumnWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductColumnWrapper pcw = new ProductColumnWrapper();
            pcw.id=resultSet.getLong("id");
            pcw.categoryId = resultSet.getLong("category_id");
            pcw.name=resultSet.getString("name_kor");
            pcw.chnName=resultSet.getString("name_chn");
            pcw.showIndex=resultSet.getInt("show_index");
            pcw.recommend=resultSet.getInt("recommend");
            pcw.desKor=resultSet.getString("des_kor");
            pcw.desChn=resultSet.getString("des_chn");
            pcw.detail_desc_chn = resultSet.getString("detail_desc_chn");
            pcw.detail_desc_kor = resultSet.getString("detail_desc_kor");
            pcw.status = resultSet.getInt("status");

            return pcw;
        }
    }

    static final class ProductMapper implements RowMapper<Product.Builder>{
        @Override
        public Product.Builder mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Product.Builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name_kor"))
                    .chnName(resultSet.getString("name_chn"))
                    .showIndex(resultSet.getInt("show_index"))
                    .recommend(resultSet.getInt("recommend"))
                    .desKor(resultSet.getString("des_kor"))
                    .desChn(resultSet.getString("des_chn"));
        }
    }

    @Override
    public int saveProductSkuImage(List<ProductSku> skus) {
        skus = skus.stream().filter(s->StringUtils.isNotBlank(s.mainImageUrl())).collect(Collectors.toList());
        List<ProductSku> finalSkus = skus;
        String delSql = "delete from product_sku_image where PRODUCT_SKU_ID=?";
        this.jdbcTemplate.batchUpdate(
                delSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ProductSku sku = finalSkus.get(i);
                        int num = 1;
                        ps.setLong(num++,sku.id());
                    }
                    @Override
                    public int getBatchSize() {
                        return finalSkus.size();
                    }
                });


        String skuImgsql = "insert into product_sku_image" +
                "(PRODUCT_SKU_ID,IMAGE_URL)" +
                "values" +
                "(?, ?)\n";
        this.jdbcTemplate.batchUpdate(
                skuImgsql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ProductSku sku = finalSkus.get(i);
                        int num = 1;
                        ps.setLong(num++,sku.id());
                        ps.setString(num++,sku.mainImageUrl());
                    }
                    @Override
                    public int getBatchSize() {
                        return finalSkus.size();
                    }
                });
        return skus.size();
    }

    @Override
    public Long updateProSkuStatus(UpdateProSkuStatus proSkuStatus) {

        String sql = "update product_sku set status=? where id = ?";
        this.getJdbcTemplate().update(sql, new Object[]{proSkuStatus.getStatus(),proSkuStatus.getId()});

        return proSkuStatus.getId();
    }
}

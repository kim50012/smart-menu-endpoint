package com.basoft.eorder.foundation.jdbc.repo.retail;


import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductAloneStandardItem;
import com.basoft.eorder.domain.model.retail.ProductSkuAloneStandard;
import com.basoft.eorder.domain.retail.RetailRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.command.retail.RetailSkuStandard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 商户零售产品规格表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-03 09:24:11
 */
@Repository
public class JdbcRetailRepoImpl extends BaseRepository implements RetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Long updateProduct(Product product,List<ProductSku> productSkus) {
        this.jdbcTemplate.update(
                "update product set category_id=?,weight=?,name_kor=?,name_chn=?,status=?,show_index=?,des_kor=?,des_chn=?,recommend=?,detail_desc_kor=?,detail_desc_chn=?,update_time=now() where id = ?",
                new Object[]{
                        product.category().id(),
                        product.weight(),
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
        updateProductImages(product);//修改产品图片
        updateProsku(productSkus);
        return product.id();
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
    public int saveProductStandards(RetailSkuStandard poting) {
        List<ProductAloneStandard> standards = poting.getStandards();
        String saveProductAloneStandardSql = "insert  into product_alone_standard" +
                "(STD_ID,STORE_ID,PROD_ID,STD_NAME_CHN,STD_NAME_KOR,STD_NAME_ENG,DIS_ORDER,STD_IMAGE,STD_STATUS,CREATE_TIME,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "STD_NAME_CHN=?,STD_NAME_KOR=?,STD_NAME_ENG=?,DIS_ORDER=?,STD_IMAGE=?,STD_STATUS=?";
        this.jdbcTemplate.batchUpdate(saveProductAloneStandardSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductAloneStandard standard = standards.get(i);
                int num = 1;
                ps.setLong(num++,standard.getStdId());
                ps.setLong(num++, standard.getStoreId());
                ps.setLong(num++, standard.getProdId());
                ps.setString(num++,standard.getStdNameChn());
                ps.setString(num++,standard.getStdNameKor());
                ps.setString(num++,standard.getStdNameEng());
                ps.setInt(num++,standard.getDisOrder());
                ps.setString(num++,standard.getStdImage());
                ps.setInt(num++,standard.getStdStatus());
                ps.setString(num++,standard.getCreateUser());

                ps.setString(num++,standard.getStdNameChn());
                ps.setString(num++,standard.getStdNameKor());
                ps.setString(num++,standard.getStdNameEng());
                ps.setInt(num++,standard.getDisOrder());
                ps.setString(num++,standard.getStdImage());
                ps.setInt(num++,standard.getStdStatus());
            }

            @Override
            public int getBatchSize() {
                return standards.size();
            }
        });
        logger.info("新增或修改standard条数="+standards.size());

        saveStandardItems(standards);

        saveSkuStandards(poting.getProductSkuStandards());//规格standard关系
        return standards.size();
    }

    //新增或修改规格小项
    public int  saveStandardItems(List<ProductAloneStandard> standards) {

        List<ProductAloneStandardItem> standardItems = new LinkedList<>();
        for (ProductAloneStandard standard : standards) {
            standardItems.addAll(standard.getStandardItemList());
        }

        String saveStandardItemSql = "insert into product_alone_standard_item" +
                "(ITEM_ID,PROD_ID,STD_ID,ITEM_NAME_CHN,ITEM_NAME_KOR,ITEM_NAME_ENG,DIS_ORDER,ITEM_IMAGE,ITEM_STATUS,CREATE_TIME,CREATE_USER)" +
                "values" +
                "(?, ? ,? , ?, ?, ?, ?, ?, ?,now(), ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "PROD_ID=?,ITEM_NAME_CHN=?,ITEM_NAME_KOR=?,ITEM_NAME_ENG=?,DIS_ORDER=?,ITEM_IMAGE=?,ITEM_STATUS=?";

        final int[] ints = this.getJdbcTemplate().batchUpdate(saveStandardItemSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        int k = 1;

                        ProductAloneStandardItem standardItem  = standardItems.get(i);
                        preparedStatement.setLong(k++, standardItem.getItemId());
                        preparedStatement.setLong(k++, standardItem.getProdId());
                        preparedStatement.setLong(k++, standardItem.getStdId());
                        preparedStatement.setString(k++, standardItem.getItemNameChn()==null?"":standardItem.getItemNameChn());
                        preparedStatement.setString(k++, standardItem.getItemNameKor()==null?"":standardItem.getItemNameKor());
                        preparedStatement.setString(k++, standardItem.getItemNameEng()==null?"":standardItem.getItemNameEng());
                        preparedStatement.setInt(k++, standardItem.getDisOrder());
                        preparedStatement.setString(k++, standardItem.getItemImage());
                        preparedStatement.setInt(k++, standardItem.getItemStatus());
                        preparedStatement.setString(k++, standardItem.getCreateUser());

                        preparedStatement.setLong(k++, standardItem.getProdId());
                        preparedStatement.setString(k++, standardItem.getItemNameChn()==null?"":standardItem.getItemNameChn());
                        preparedStatement.setString(k++, standardItem.getItemNameKor()==null?"":standardItem.getItemNameKor());
                        preparedStatement.setString(k++, standardItem.getItemNameEng()==null?"":standardItem.getItemNameEng());;
                        preparedStatement.setInt(k++, standardItem.getDisOrder());
                        preparedStatement.setString(k++, standardItem.getItemImage());
                        preparedStatement.setInt(k++, standardItem.getItemStatus());
                    }
                    @Override
                    public int getBatchSize() {
                        return standardItems.size();
                    }
        });
        logger.info("新增或修改standardItem条数="+standardItems.size());
        return standardItems.size();
    }


    /**
     * 保存规格和standard关系
     *
     * @param productSkuStandards
     * @return
     */
    private int saveSkuStandards(List<ProductSkuAloneStandard> productSkuStandards) {

        String saveStandardItemSql = "insert into product_sku_alone_standard" +
                "(PRODUCT_SKU_ID,STANDARD_ID,STANDARD_ITEM_ID,STATUS,CREATED)" +
                "values" +
                "(?, ?, ?, ?, now())\n"+
                "on duplicate key\n"+
                "update \n"+
                "status=?";

        final int[] ints = this.getJdbcTemplate().batchUpdate(saveStandardItemSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement,int i) throws SQLException {
                        int k = 1;
                        ProductSkuAloneStandard skuAloneStandard = productSkuStandards.get(i);
                        preparedStatement.setLong(k++, skuAloneStandard.getProductSkuId());
                        preparedStatement.setLong(k++, skuAloneStandard.getStandardId());
                        preparedStatement.setLong(k++, skuAloneStandard.getStandardItemId());
                        preparedStatement.setInt(k++, skuAloneStandard.getStatus());
                        preparedStatement.setInt(k++, skuAloneStandard.getStatus());
                    }
                    @Override
                    public int getBatchSize() {
                        return productSkuStandards.size();
                    }
                });
        logger.info("新增规格standard关联关系条数="+productSkuStandards.size());
        return productSkuStandards.size();
    }

    private int updateProsku(List<ProductSku> skus) {
                String skusql = "insert into product_sku" +
                "(id,name_kor,name_chn,weight,price,PRICE_SETTLE,product_id,use_default,IS_INVENTORY,DIS_ORDER,status)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "name_kor=?,name_chn=?,weight=?,price=?,use_default=?,DIS_ORDER=?,status=?";
        this.jdbcTemplate.batchUpdate(
                skusql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ProductSku sku = skus.get(i);
                        int num = 1;
                        ps.setLong(num++,sku.id());
                        ps.setString(num++,sku.name());
                        ps.setString(num++,sku.chnName());
                        ps.setBigDecimal(num++,sku.weight());
                        ps.setBigDecimal(num++,sku.price());
                        ps.setBigDecimal(num++,sku.priceSettle());
                        ps.setLong(num++,sku.product().id());
                        ps.setBoolean(num++,sku.useDefault());
                        ps.setInt(num++,sku.isInventory());
                        ps.setInt(num++,sku.disOrder());
                        ps.setInt(num++,sku.status().getCode());

                        ps.setString(num++,sku.name());
                        ps.setString(num++,sku.chnName());
                        ps.setBigDecimal(num++,sku.weight());
                        ps.setBigDecimal(num++,sku.price());
                        ps.setBoolean(num++,sku.useDefault());
                        ps.setInt(num++,sku.disOrder());
                        ps.setInt(num++,sku.status().getCode());
                    }
                    @Override
                    public int getBatchSize() {
                        return skus.size();
                    }
                });

        logger.info("新增或修改规格条数="+skus.size());

        return skus.size();
    }

    @Override
    public Long  updateProductAloneStandardStatus(ProductAloneStandard productAloneStandard) {
        String upStaSql = "update product_alone_standard set " +
                "where STD_ID = ?";
        this.jdbcTemplate.update(upStaSql,
                new Object[]{
                        productAloneStandard.getStdId(),
                });

        return productAloneStandard.getStdId();
    }

















    @Override
    public Long  updateProductAloneStandardItem(ProductAloneStandardItem productAloneStandardItem) {
        String upProductAloneStandardItemSql = "update product_alone_standard_item set " +
                "STD_ID = ?," +
                "ITEM_NAME_CHN = ?," +
                "ITEM_NAME_KOR = ?," +
                "ITEM_NAME_ENG = ?," +
                "DIS_ORDER = ?," +
                "ITEM_IMAGE = ?," +
                "ITEM_STATUS = ?," +
                "CREATE_TIME = ?," +
                "CREATE_USER = ?," +
                "UPDATE_TIME = ?," +
                "UPDATE_USER = ?," +
                "where ITEM_ID = itemId";

        this.jdbcTemplate.update(upProductAloneStandardItemSql,
                new Object[]{
                        productAloneStandardItem.getStdId(),
                        productAloneStandardItem.getItemNameChn(),
                        productAloneStandardItem.getItemNameKor(),
                        productAloneStandardItem.getItemNameEng(),
                        productAloneStandardItem.getDisOrder(),
                        productAloneStandardItem.getItemImage(),
                        productAloneStandardItem.getItemStatus(),
                        productAloneStandardItem.getCreateTime(),
                        productAloneStandardItem.getCreateUser(),
                        productAloneStandardItem.getUpdateTime(),
                        productAloneStandardItem.getUpdateUser()
                });

        return productAloneStandardItem.getItemId();
    }

    @Override
    public Long  updateProductAloneStandardItemStatus(ProductAloneStandardItem productAloneStandardItem) {
        String upStaSql = "update product_alone_standard_item set " +
                "where ITEM_ID = ?";
        this.jdbcTemplate.update(upStaSql,
                new Object[]{
                        productAloneStandardItem.getItemId(),
                });

        return productAloneStandardItem.getItemId();
    }

}
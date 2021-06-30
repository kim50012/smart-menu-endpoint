package com.basoft.eorder.foundation.jdbc.repo.retail;


import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductAloneStandardItem;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.domain.retail.RetailTemplateRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-16 13:37:32
 */
@Repository
public class JdbcRetailTemplateRepoImpl extends BaseRepository implements RetailTemplateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long saveRetailTemplate(List<ProductAloneStandard> standards,ProductAloneStandardTemplate template) {
        saveProductAloneStandardTemplate(template);//新增模版主表
        saveProductStandardsTemplate(standards,template.getTId());

        return template.getTId();
    }

    @Override
    public Long saveProductAloneStandardTemplate(ProductAloneStandardTemplate productAloneStandardTemplate) {
        String saveProductAloneStandardTemplateSql = "insert  into product_alone_standard_template" +
                "(T_ID,STORE_ID,T_NAME_CHN,T_NAME_KOR,T_NAME_ENG,T_STATUS,DES_KOR,DES_CHN,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)"+
                "on duplicate key\n"+
                        "update \n"+
                        "T_NAME_CHN = ?,T_NAME_KOR = ?,T_STATUS = ?,DES_KOR = ?,DES_CHN = ?";

        this.jdbcTemplate.update(saveProductAloneStandardTemplateSql,
                new Object[]{
                        productAloneStandardTemplate.getTId(),
                        productAloneStandardTemplate.getStoreId(),
                        productAloneStandardTemplate.getTNameChn(),
                        productAloneStandardTemplate.getTNameKor(),
                        productAloneStandardTemplate.getTNameEng(),
                        productAloneStandardTemplate.getTStatus(),
                        productAloneStandardTemplate.getDesKor(),
                        productAloneStandardTemplate.getDesChn(),
                        productAloneStandardTemplate.getCreateUser(),

                        productAloneStandardTemplate.getTNameChn(),
                        productAloneStandardTemplate.getTNameKor(),
                        productAloneStandardTemplate.getTStatus(),
                        productAloneStandardTemplate.getDesKor(),
                        productAloneStandardTemplate.getDesChn()
                });

        return productAloneStandardTemplate.getTId();
    }


    //新增或修改规格模版大项
    public int saveProductStandardsTemplate(List<ProductAloneStandard> standards,Long tId) {
        this.getJdbcTemplate().update("delete from product_alone_standard_t_s where T_ID=?",new Object[]{tId});


        String saveProductAloneStandardSql = "insert  into product_alone_standard_t_s" +
                "(STD_ID,T_ID,STD_NAME_CHN,STD_NAME_KOR,STD_NAME_ENG,DIS_ORDER,STD_IMAGE,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "STD_NAME_CHN=?,STD_NAME_KOR=?,STD_NAME_ENG=?,DIS_ORDER=?,STD_IMAGE=?";
        this.jdbcTemplate.batchUpdate(saveProductAloneStandardSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductAloneStandard standard = standards.get(i);
                int num = 1;
                ps.setLong(num++,standard.getStdId());
                ps.setLong(num++,standard.getTId());
                ps.setString(num++,standard.getStdNameChn());
                ps.setString(num++,standard.getStdNameKor());
                ps.setString(num++,standard.getStdNameEng());
                ps.setInt(num++,standard.getDisOrder());
                ps.setString(num++,standard.getStdImage());
                ps.setString(num++,standard.getCreateUser());

                ps.setString(num++,standard.getStdNameChn());
                ps.setString(num++,standard.getStdNameKor());
                ps.setString(num++,standard.getStdNameEng());
                ps.setInt(num++,standard.getDisOrder());
                ps.setString(num++,standard.getStdImage());
            }

            @Override
            public int getBatchSize() {
                return standards.size();
            }
        });
        logger.info("新增或修改模版standard条数="+standards.size());

        saveStandardItemsTemplate(standards,tId);

        return standards.size();
    }


    //新增或修改规格模版小项
    public int  saveStandardItemsTemplate(List<ProductAloneStandard> standards,Long tId) {
        this.getJdbcTemplate().update("delete from product_alone_standard_t_s_i where T_ID=?",new Object[]{tId});

        List<ProductAloneStandardItem> standardItems = new LinkedList<>();
        for (ProductAloneStandard standard : standards) {
            standardItems.addAll(standard.getStandardItemList());
        }

        String saveStandardItemSql = "insert into product_alone_standard_t_s_i" +
                "(ITEM_ID,T_ID,STD_ID,ITEM_NAME_CHN,ITEM_NAME_KOR,ITEM_NAME_ENG,DIS_ORDER,ITEM_IMAGE,CREATE_USER)" +
                "values" +
                "(?, ?, ?,  ?, ?, ?,  ?, ?, ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "ITEM_NAME_CHN=?,ITEM_NAME_KOR=?,ITEM_NAME_ENG=?,DIS_ORDER=?,ITEM_IMAGE=?";

        final int[] ints = this.getJdbcTemplate().batchUpdate(saveStandardItemSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        int k = 1;

                        ProductAloneStandardItem standardItem  = standardItems.get(i);
                        preparedStatement.setLong(k++, standardItem.getItemId());
                        preparedStatement.setLong(k++, standardItem.getTId());
                        preparedStatement.setLong(k++, standardItem.getStdId());
                        preparedStatement.setString(k++, standardItem.getItemNameChn()==null?"":standardItem.getItemNameChn());
                        preparedStatement.setString(k++, standardItem.getItemNameKor()==null?"":standardItem.getItemNameKor());
                        preparedStatement.setString(k++, standardItem.getItemNameEng()==null?"":standardItem.getItemNameEng());
                        preparedStatement.setInt(k++, standardItem.getDisOrder());
                        preparedStatement.setString(k++, standardItem.getItemImage());
                        preparedStatement.setString(k++, standardItem.getCreateUser());

                        preparedStatement.setString(k++, standardItem.getItemNameChn()==null?"":standardItem.getItemNameChn());
                        preparedStatement.setString(k++, standardItem.getItemNameKor()==null?"":standardItem.getItemNameKor());
                        preparedStatement.setString(k++, standardItem.getItemNameEng()==null?"":standardItem.getItemNameEng());;
                        preparedStatement.setInt(k++, standardItem.getDisOrder());
                        preparedStatement.setString(k++, standardItem.getItemImage());
                    }
                    @Override
                    public int getBatchSize() {
                        return standardItems.size();
                    }
                });
        logger.info("新增或修改模版standardItem条数="+standardItems.size());
        return standardItems.size();
    }


    /**
     * 修改模版状态
     * @param templates
     * @return
     */
    public int updateRetailTemplateStatus(List<ProductAloneStandardTemplate> templates,String tStatus) {
        String saveProductAloneStandardSql = "update  product_alone_standard_template\n" +
                "set T_STATUS = ? where T_ID = ?";
        this.jdbcTemplate.batchUpdate(saveProductAloneStandardSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductAloneStandardTemplate template = templates.get(i);
                int num = 1;
                ps.setInt(num++, Integer.valueOf(tStatus));
                ps.setLong(num++,template.getTId());
            }

            @Override
            public int getBatchSize() {
                return templates.size();
            }
        });
        return templates.size();
    }

    @Override
    public int deleteTemplate(List<ProductAloneStandardTemplate> templates, UserSession us) {
        templates.stream().forEach(template -> {template.setStoreId(us.getStoreId());});

        String delTem_s_Sql = "delete from product_alone_standard_t_s where T_ID = ?";
        this.jdbcTemplate.batchUpdate(delTem_s_Sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductAloneStandardTemplate template = templates.get(i);
                int num = 1;
                ps.setLong(num++,template.getTId());
            }

            @Override
            public int getBatchSize() {
                return templates.size();
            }
        });

        String delTem_s_i_Sql = "delete from product_alone_standard_t_s_i where T_ID = ?";
        this.jdbcTemplate.batchUpdate(delTem_s_i_Sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductAloneStandardTemplate template = templates.get(i);
                int num = 1;
                ps.setLong(num++,template.getTId());
            }

            @Override
            public int getBatchSize() {
                return templates.size();
            }
        });

        if(templates!=null&&templates.size()>0) {
            String delTem_Sql = "delete from product_alone_standard_template where T_ID=? and STORE_ID=?";
            this.jdbcTemplate.batchUpdate(delTem_Sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ProductAloneStandardTemplate template = templates.get(i);
                    int num = 1;
                    ps.setLong(num++, template.getTId());
                    ps.setLong(num++,template.getStoreId());
                }

                @Override
                public int getBatchSize() {
                    return templates.size();
                }
            });
        }
        return templates.size();
    }
}
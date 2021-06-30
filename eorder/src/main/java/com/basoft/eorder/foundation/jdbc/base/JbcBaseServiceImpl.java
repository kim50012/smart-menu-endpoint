package com.basoft.eorder.foundation.jdbc.base;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.base.query.CategoryDTO4H5;
import com.basoft.eorder.domain.events.UpdateProudctEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class JbcBaseServiceImpl implements BaseService {
    static final Long ROOT_ID = new Long(0);

    private JdbcTemplate jdbcTemplate;

    JbcBaseServiceImpl() {
    }

    @Autowired
    public JbcBaseServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JbcBaseServiceImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Category getCategory(Long id) {
        Map<Long, JdbcCategoryWrapper> childMap = new HashMap<>();
        this.jdbcTemplate.query(
                " SELECT * FROM base_category ca WHERE ca.id = ? and ca.status !=3" +
                        "  UNION ALL" +
                        "  SELECT cca.* FROM base_category cca WHERE cca.parent_id = ? and cca.status !=3",
                new Object[]{id, id},
                getCategoryLineMapper())
                .stream()
                .forEach(new Consumer<JdbcCategoryWrapper>() {
                    @Override
                    public void accept(JdbcCategoryWrapper cateWrapper) {
                        childMap.put(cateWrapper.id, cateWrapper);
                    }
                });

        final JdbcCategoryWrapper jdbcCategoryWrapper = childMap.get(id);
        Category.Builder currentBuilder = jdbcCategoryWrapper.build();

        childMap.values()
                .stream()
                .filter(new Predicate<JdbcCategoryWrapper>() {
                    @Override
                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                        return !id.equals(jdbcCategoryWrapper.id);
                    }
                })
                .map(new Function<JdbcCategoryWrapper, Category.Builder>() {
                    @Override
                    public Category.Builder apply(JdbcCategoryWrapper jdbcCategoryWrapper) {
                        return jdbcCategoryWrapper.build();
                    }
                })
                .forEach(new Consumer<Category.Builder>() {
                    @Override
                    public void accept(Category.Builder builder) {
                        currentBuilder.child(builder);
                    }
                });
        return currentBuilder.build();
    }

    @Override
    public Category getRootCategory() {
        final List<JdbcCategoryWrapper> resultList = this.jdbcTemplate.query(
                " SELECT * FROM base_category ",
                getCategoryLineMapper());

        final Category.Builder rootBuilder =
                resultList.stream()
                        .filter((wrapper) -> wrapper.isRoot)
                        .map((swrapper) -> swrapper.build())
                        .findFirst()
                        .get();

        resultList.stream()
                .filter((jcw) -> !jcw.isRoot)
                .map((jcw) -> jcw.build())
                .forEach(new Consumer<Category.Builder>() {
                    @Override
                    public void accept(Category.Builder builder) {
                        rootBuilder.child(builder);
                    }
                });
        return rootBuilder.build();
    }

    /**
     * 基础商品分类RowMapper
     *
     * @return
     */
    static RowMapper<JdbcCategoryWrapper> getCategoryLineMapper() {
        return new RowMapper<JdbcCategoryWrapper>() {
            @Override
            public JdbcCategoryWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                JdbcCategoryWrapper cateWrapper = new JdbcCategoryWrapper();
                cateWrapper.id = resultSet.getLong("id");
                cateWrapper.name = resultSet.getString("name");
                cateWrapper.type = resultSet.getInt("type");
                //cateWrapper.status = resultSet.getInt("status");
                cateWrapper.parentId = resultSet.getLong("parent_id");
                cateWrapper.depth = resultSet.getInt("depth");
                cateWrapper.chnName = resultSet.getString("chn_name");
                cateWrapper.status = resultSet.getInt("status");
                cateWrapper.isRoot = resultSet.getBoolean("is_root");
                cateWrapper.functionType = resultSet.getInt("FUNCTION_TYPE");
                cateWrapper.categoryType = resultSet.getInt("CATEGORY_TYPE");
                cateWrapper.manageType = resultSet.getInt("MANAGE_TYPE");
                return cateWrapper;
            }
        };
    }

    /**
     * @return
     */
    static RowMapper<JdbcCategoryWrapper> getCategoryLineMapper4RootOption() {
        return new RowMapper<JdbcCategoryWrapper>() {
            @Override
            public JdbcCategoryWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                JdbcCategoryWrapper cateWrapper = new JdbcCategoryWrapper();
                cateWrapper.id = resultSet.getLong("id");
                cateWrapper.name = resultSet.getString("name");
                cateWrapper.type = resultSet.getInt("type");
                //cateWrapper.status = resultSet.getInt("status");
                cateWrapper.parentId = resultSet.getLong("parent_id");
                cateWrapper.depth = resultSet.getInt("depth");
                cateWrapper.chnName = resultSet.getString("chn_name");
                cateWrapper.status = resultSet.getInt("status");
                cateWrapper.isRoot = resultSet.getBoolean("is_root");
                return cateWrapper;
            }
        };
    }

    public void delete(Long newId) {
        this.jdbcTemplate.update("delete from base_category where id = ?", newId);
    }

    public void deleteOption(Long id) {
        this.jdbcTemplate.update("delete from base_product_standard where id = ?", id);
    }

    static final class JdbcCategoryWrapper {
        private Long id;
        private int type;
        private String name;
        private int status;
        private Long parentId;
        private int depth;
        private String chnName;
        private boolean isRoot;
        private int functionType;
        private int categoryType;
        private int manageType;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getChnName() {
            return chnName;
        }

        public void setChnName(String chnName) {
            this.chnName = chnName;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getFunctionType() {
            return functionType;
        }

        public void setFunctionType(int functionType) {
            this.functionType = functionType;
        }

        public int getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(int categoryType) {
            this.categoryType = categoryType;
        }

        public int getManageType() {
            return manageType;
        }

        public void setManageType(int manageType) {
            this.manageType = manageType;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Long getParentId() {
            return parentId;
        }

        public int getDepth() {
            return depth;
        }

        public Category.Builder build() {
            return new Category.Builder()
                    .id(id)
                    .type(type)
                    .name(name)
                    .chnName(this.chnName)
                    .depth(this.depth)
                    .statusInt(status)
                    .functionType(this.functionType)
                    .categoryType(this.categoryType)
                    .manageType(manageType);
        }
    }

    static final class OptionWrapper {
        private Long id;
        private String name;
        private int type;
        private Long parentId;
        private int depth;
        private String chnName;
        private boolean isRoot;

        public OptionWrapper(Long id, String name, Long parentId, String chnName, boolean isRoot, int type) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
            this.chnName = chnName;
            this.isRoot = isRoot;
            this.type = type;
        }

        public Option.Builder build(List<OptionWrapper> childrenBuilder) {
            Option.Builder builder = new Option.Builder()
                    .id(this.id)
                    .name(this.name)
                    .chnName(this.chnName)
                    .type(this.type);

            List<Option.Builder> childOptionArray = new LinkedList<>();

            for (OptionWrapper wrapper : childrenBuilder) {
                if (wrapper.parentId.equals(this.id)) {
                    final Option.Builder builder1 = wrapper.build(childrenBuilder);
                    childOptionArray.add(builder1);
                }
            }
            return builder.children(childOptionArray);
        }
    }

    /**
     * 新增Category
     *
     * @param category
     * @return
     */
    @Override
    public Category saveCategory(Category category) {
        this.jdbcTemplate.update("insert into base_category (id,name,parent_id,depth,chn_name,type,FUNCTION_TYPE,CATEGORY_TYPE,MANAGE_TYPE) values(?,?,?,?,?,?,?,?,?)",
                new Object[]{category.id(),
                        category.name(),
                        category.getParent().id(),
                        category.depth(),
                        category.chnName(),
                        category.type(),
                        category.functionType(),
                        category.categoryType(),
                        category.manageType()
                });
        return category;
    }

    /**
     * 修改Category
     *
     * @param category
     * @return
     */
    @Override
    public Category updateCategory(Category category) {
        this.jdbcTemplate.update(
                "update base_category set chn_name=?, CATEGORY_TYPE=?, MANAGE_TYPE=? where id = ?",
                new Object[]{category.chnName(), category.categoryType(), category.manageType(), category.id()});
        return category;
    }

    @Override
    public Long upCategoryStatus(Category category) {
        this.jdbcTemplate.update("update base_category set status=? where id=?",
                new Object[]{category.status().code(), category.id()});
        return category.id();
    }

    @Override
    public Option getRootOption() {
        List<OptionWrapper> rootArray = new LinkedList<>();
        List<OptionWrapper> childrenBuilder = this.jdbcTemplate.query(
                " SELECT * FROM base_product_standard ca where ca.status !=3 ",
                new RowMapper<OptionWrapper>() {
                    @Override
                    public OptionWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                        Long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        Long parentId = resultSet.getLong("parent_id");
                        String chnName = resultSet.getString("chn_name");
                        boolean isRoot = resultSet.getBoolean("is_root");
                        int type = resultSet.getInt("type");
                        return new OptionWrapper(id, name, parentId, chnName, isRoot, type);
                    }
                })
                .stream()
                .filter(new Predicate<OptionWrapper>() {
                    @Override
                    public boolean test(OptionWrapper optionWrapper) {
                        if (optionWrapper.isRoot) {
                            rootArray.add(optionWrapper);
                            return false;
                        }
                        return true;
                    }
                })
                .collect(Collectors.toList());

        return rootArray.get(0)
                .build(childrenBuilder)
                .build();
    }


    @Override
    @Transactional
    public Option getOption(Long id) {
//        Map<Long,OptionWrapper> childMap = new HashMap<>();
        List<OptionWrapper> rootArray = new LinkedList<>();
        List<OptionWrapper> childrenBuilder = this.jdbcTemplate.query(
                " SELECT * FROM base_product_standard ca WHERE ca.id = ? and ca.status !=3 " +
                        "  UNION ALL" +
                        "  SELECT cca.* FROM base_product_standard cca WHERE cca.parent_id = ? and cca.status !=3 ",
                new Object[]{id, id},
                new RowMapper<OptionWrapper>() {
                    @Override
                    public OptionWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                        Long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        Long parentId = resultSet.getLong("parent_id");
                        String chnName = resultSet.getString("chn_name");
                        boolean isRoot = resultSet.getBoolean("is_root");
                        int type = resultSet.getInt("type");
                        return new OptionWrapper(id, name, parentId, chnName, isRoot, type);
                    }
                })
                .stream()
                .filter(new Predicate<OptionWrapper>() {
                    @Override
                    public boolean test(OptionWrapper optionWrapper) {
                        if (optionWrapper.id.equals(id)) {
                            rootArray.add(optionWrapper);
                            return false;
                        }
                        return true;
                    }
                })
                .collect(Collectors.toList());
        return rootArray.get(0)
                .build(childrenBuilder)
                .build();
    }

    @Override
    @Transactional
    public Option saveOption(Option option1) {
        this.jdbcTemplate.update(
                "insert into base_product_standard (id,name,chn_name,parent_id,type) values(?,?,?,?,?)",
                option1.id(), option1.name(), option1.chnName(), option1.parent().id(), option1.type());
        return option1;
    }

    @Override
    @Transactional
    public Option updateOption(Option option1) {
        this.jdbcTemplate.update(
                "update base_product_standard set chn_name=? where id = ?",
                new Object[]{option1.chnName(), option1.id()});
        return option1;
    }

    @Override
    public Long delOption(Option option) {
        this.jdbcTemplate.update(
                "update base_product_standard set status=? where id = ?",
                new Object[]{option.status(), option.id()});
        return option.id();
    }

    @Override
    public void batchUpdateProductName(List<UpdateProudctEvent> proudctEventList) {
        this.jdbcTemplate.batchUpdate(
                "update product set chn_name where store_id = ? and id = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UpdateProudctEvent event = proudctEventList.get(i);
                        ps.setString(1, event.getChnName());
                        ps.setLong(2, event.getStoreId());
                        ps.setLong(3, event.getProductId());
                    }

                    @Override
                    public int getBatchSize() {
                        return proudctEventList.size();
                    }
                });
    }

/*    static Option.Builder newOption(OptionWrapper currentWrapper, List<OptionWrapper> builders) {
        Option.Builder currentBuilder = currentWrapper.build(childrenBuilder);
        for(OptionWrapper war :builders){
            if(war.parentId.longValue() == currentWrapper.id.longValue()){
                currentBuilder.child(newOption(war,builders));
            }
        }
        return currentBuilder;
    }*/

    /***********************************20191211-酒店大改造重写标签查询方式***********************************/
    /**
     * 查询category列表为了H5商户列表
     *
     * @return
     */
    public List<CategoryDTO4H5> getAllCategory() {
        final List<CategoryDTO4H5> resultList = this.jdbcTemplate.query(
                " SELECT * FROM base_category ",
                getCategory4H5());
        return resultList;
    }

    /**
     * Category RowMapper
     *
     * @return
     */
    private RowMapper<CategoryDTO4H5> getCategory4H5() {
        return new RowMapper<CategoryDTO4H5>() {
            @Override
            public CategoryDTO4H5 mapRow(ResultSet resultSet, int i) throws SQLException {
                CategoryDTO4H5 categoryDTO4H5 = new CategoryDTO4H5();
                categoryDTO4H5.setId(resultSet.getLong("id"));
                categoryDTO4H5.setName(resultSet.getString("name"));
                categoryDTO4H5.setType(resultSet.getInt("type"));
                categoryDTO4H5.setChnName(resultSet.getString("chn_name"));
                categoryDTO4H5.setStatus(resultSet.getInt("status"));
                categoryDTO4H5.setFunctionType(resultSet.getInt("FUNCTION_TYPE"));
                categoryDTO4H5.setCategoryType(resultSet.getInt("CATEGORY_TYPE"));
                categoryDTO4H5.setManageType(resultSet.getInt("MANAGE_TYPE"));
                return categoryDTO4H5;
            }
        };
    }
}

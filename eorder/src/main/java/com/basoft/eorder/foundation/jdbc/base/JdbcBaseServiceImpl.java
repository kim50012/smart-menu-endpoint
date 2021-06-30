/*
package com.basoft.eorder.foundation.jdbc.base;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JdbcBaseServiceImpl implements BaseService {


    static final Long ROOT_ID =new Long(0);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBaseServiceImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Category getCategory(Long id) {

        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();


        final List<JdbcCategoryWrapper> builders =
                this.jdbcTemplate
                        .query(
                            " SELECT * FROM base_category",
                            getCategoryLineMapper())
                                .stream()
                                .filter(new Predicate<JdbcCategoryWrapper>() {
                                    @Override
                                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {

                                        System.out.println(jdbcCategoryWrapper.id);

                                        if (ROOT_ID.longValue() == jdbcCategoryWrapper.id.longValue()) {
                                            childMap.put(ROOT_ID, jdbcCategoryWrapper);
                                            return false;
                                        }
                                        return true;
                                    }
                                })
                                .collect(Collectors.toList());

        return buildCategory(childMap.get(ROOT_ID),builders)
                .build()
                .getCategory(id);

    }

    static Category.Builder buildCategory(JdbcCategoryWrapper parent,List<JdbcCategoryWrapper> allList){

        Category.Builder parentBuilder = parent.build();

        for(JdbcCategoryWrapper child : allList){
            if(!parent.id.equals(child.parentId)){
                return buildCategory(child,allList);
            }

            Category.Builder childBuilder = child.build();
            parentBuilder.child(childBuilder);
        }
        return parentBuilder;
    }



    static RowMapper<JdbcCategoryWrapper> getCategoryLineMapper(){

        return new RowMapper<JdbcCategoryWrapper>() {
            @Override
            public JdbcCategoryWrapper mapRow(ResultSet resultSet, int i) throws SQLException {

                JdbcCategoryWrapper cateWrapper = new JdbcCategoryWrapper();
                cateWrapper.id = resultSet.getLong("id");
                cateWrapper.name = resultSet.getString("name");
                cateWrapper.parentId = resultSet.getLong("parent_id");
                cateWrapper.depth = resultSet.getInt("depth");
               // Category.Status.values() = resultSet.getInt("status");
                return cateWrapper;
            }
        };
    }

    public void delete(Long newId) {
        this.jdbcTemplate.update("delete from base_category where id = ?",newId);
    }

    static final class JdbcCategoryWrapper {
        private Long id;
        private String name;
        private Long parentId;
        private int depth;
        private Category.Status status;

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

        public void setStatus(Category.Status status) {
            this.status = status;
        }


        public Category.Builder build() {
            return new Category.Builder()
                    .id(id)
                    .name(name)
                    .status(status)
                    .depth(this.depth);

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
    }



    @Override
    public Category saveCategory(Category category) {

        this.jdbcTemplate.update("insert into base_category (id,name,parent_id,depth,status) values(?,?,?,?,?)",
                new Object[]{
                    category.id(),
                    category.name(),
                    category.getParent().id(),
                    category.depth(),
                    category.status().code()});
        return category;
    }

    @Override
    public Category updateCategory(Category newCategory) {
        this.jdbcTemplate.update("update base_category set name=?,parent_id=?,depth=?,status=? where id = ?",
                new Object[]{
                    newCategory.name(),
                    newCategory.getParent().id(),
                    newCategory.depth(),
                    newCategory.status().code(),
                    newCategory.id()
                });
        return newCategory;
    }

    @Override
    public Option getOption(Long parentId) {
        return null;
    }

    @Override
    public Option saveOption(Option option1) {
        return null;
    }

    @Override
    public Option updateOption(Option option1) {
        return null;
    }
}
*/

package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.CategoryDTO;
import com.basoft.eorder.interfaces.query.CategoryQuery;
import com.basoft.eorder.interfaces.query.StoreCategoryDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcCategoryQueryImpl
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-17 10:58
 * @Version: 1.0
 */
@Component("CategoryQuery")
public class JdbcCategoryQueryImpl extends BaseRepository implements CategoryQuery {

    private BaseService baseService;

    private final static String CATEGORY_BY_ID = "select * from base_category c where c.id = :id";

    private final static String CATEGORY_BY_PID = "select c.id as id, c.name_kor as nameKor, c.name_chn as nameChn, c.show_index as showIndex, c.status as status, c.store_id as storeId, c.created_dt as createdDt from base_category c where c.parent_id = :parentId";

    private static final String CATEGORY_LIST_SELECT = "select c.id as id, c.name_kor as nameKor, c.name_chn as nameChn, c.show_index as showIndex, c.status as status, c.store_id as storeId, c.created_dt as createdDt \n";

    private static final String CATEGORY_FORM = "from category c \n where 1 = 1 \n";


    JdbcCategoryQueryImpl(){}


    @Autowired
    public JdbcCategoryQueryImpl(BaseService bs){
        this.baseService = bs;
    }

    public JdbcCategoryQueryImpl(DataSource dataSource, BaseService pq) {
        super(dataSource);
        this.baseService = pq;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return this.queryForObject(CATEGORY_BY_ID, map, new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class));
    }


    @Override
    public int getCategoryCountByMap(Map<String, Object> param) {
        String name = Objects.toString(param.get("name"), null);
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));

        StringBuilder query = new StringBuilder(SELECT_COUNT + CATEGORY_FORM);

        if (StringUtils.isNotBlank(name)) {
            query.append(" and b.name = :name \n");
        }

        if (storeId > 0) {
            query.append(" and b.store_id = :storeId \n");
        }

        Integer count = this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
        return count.intValue();
    }

    @Override
    public List<CategoryDTO> getCategoryListByMap(Map<String, Object> param) {
        String name = Objects.toString(param.get("name"), null);
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        StringBuilder query = new StringBuilder(CATEGORY_LIST_SELECT + CATEGORY_FORM);

        if (StringUtils.isNotBlank(name)) {
            query.append(" and b.name = :name \n");
        }

        if (storeId > 0) {
            query.append(" and b.store_id = :storeId \n");
        }
//                .append(this.getQueryConditions(param))
        query.append("order by b.show_index, b.id \n");

        if (page >= 0 && size > 0) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*10;
                param.put("page",resPage);
            }
            query.append(LIMIT);
        }

        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class));
    }


    @Override
    public List<StoreCategoryDTO> getCategoryOfStore(Long storeId) {

        Category rootCategory = this.baseService.getRootCategory();
        return  this.getJdbcTemplate().queryForList(
                        "select category_id from store_category_map where store_id = ?",
                        new Object[]{storeId},
                        Long.class
                )
                .stream()
                .map((categoryId) -> {
                        Category category = rootCategory.getCategory(categoryId);
                        return toStoreCategory(category);
                    }
                ).collect(Collectors.toList());
    }

    static StoreCategoryDTO toStoreCategory(Category c){

        if(c == null){
            throw new IllegalStateException("Category is null");
        }


        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setId(c.id());
        categoryDTO.setName(c.name());
        categoryDTO.setChnName(c.chnName());
        categoryDTO.setStatus(c.status().code());

        final Collection<Category> children = c.getChildren();
        List<StoreCategoryDTO> categoryDTOS = new ArrayList<>(children.size());
        for(Category cc :children){
            StoreCategoryDTO child = toStoreCategory(cc);
            if(child != null){
                categoryDTOS.add(child);
            }
        }
        categoryDTO.setChildren(categoryDTOS);
        return categoryDTO;
    }

}

package com.basoft.eorder.application.base;

import com.basoft.eorder.application.base.query.CategoryDTO4H5;
import com.basoft.eorder.domain.events.UpdateProudctEvent;

import java.util.List;

public interface BaseService {
    Category getCategory(Long parentId);

    /**
     * 新增Category
     *
     * @param category
     * @return
     */
    Category saveCategory(Category category);

    /**
     * 修改Category
     *
     * @param newCategory
     * @return
     */
    Category updateCategory(Category newCategory);

    Long upCategoryStatus(Category category);

    Option getRootOption();

    Option getOption(Long id);

    Option saveOption(Option option1);

    Option updateOption(Option option1);

    Long delOption(Option option1);

    void batchUpdateProductName(List<UpdateProudctEvent> proudctEventList);

    Category getRootCategory();

    /**
     * 查询category列表
     *
     * @return
     */
    List<CategoryDTO4H5> getAllCategory();
}

package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

public interface CategoryQuery {


    List<StoreCategoryDTO> getCategoryOfStore(Long storeId);
    CategoryDTO getCategoryById(Long id);
    int getCategoryCountByMap(Map<String, Object> map);
    List<CategoryDTO> getCategoryListByMap(Map<String, Object> map);

}

package com.basoft.eorder.application.base.query;

import com.basoft.eorder.interfaces.query.OptionDTO;

public interface AdminQueryFacade {
    /**
     * ADMIN-商品分类查询|店铺标签查询|产品标签查询
     *
     * @param type         业务类型 1-餐厅 2-医院 3-购物 4-酒店
     * @param functionType 用途和功能类型 1-商品分类 2-店铺标签 3-产品标签
     * @return
     */
    CategoryDTO getRootCategory(String type, String functionType);

    /**
     * ADMIN-店铺标签查询|产品标签查询
     *
     * @param type         业务类型 1-餐厅 2-医院 3-购物 4-酒店
     * @param functionType 用途和功能类型 2-店铺标签 3-产品标签
     * @param manageType   标签管理方 1-Admin CMS 2-Manager CMS 3-查询Admin CMS和Manager CMS的和
     * @return
     */
    CategoryDTO getTags(String type, String functionType, String manageType);

    CategoryDTO getStoreCategory(Long storeId);

    OptionDTO getRootOption(String type);
}

package com.basoft.eorder.interfaces.query.postStoreSet;

import java.util.List;
import java.util.Map;

/**
 * 商户配送设置表(PostStoreSet)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-29 10:49:08
 */
public interface PostStoreSetQuery {

    PostStoreSetDTO getPostStoreSetDto(Map<String, Object> param);

    int getPostStoreSetCount(Map<String, Object> param);

    List<PostStoreSetDTO> getPostStoreSetListByMap(Map<String, Object> param);

}
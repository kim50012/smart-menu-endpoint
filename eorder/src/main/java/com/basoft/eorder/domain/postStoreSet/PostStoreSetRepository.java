package com.basoft.eorder.domain.postStoreSet;

import com.basoft.eorder.domain.model.postStoreSet.PostStoreSet;

import java.util.List;

/**
 * 商户配送设置表(PostStoreSet)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-29 10:49:03
 */
public interface  PostStoreSetRepository{

	//新增商户配送设置表
	Long  insertPostStoreSet(PostStoreSet postStoreSet);

	//修改商户配送设置表
	Long  updatePostStoreSet (PostStoreSet postStoreSet);

	//批量修改商户配送设置表状态
	int  updatePostStoreSetStatus(List<PostStoreSet> postStoreSetList,int status);

}
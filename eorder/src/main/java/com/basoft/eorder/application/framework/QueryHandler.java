package com.basoft.eorder.application.framework;

/**
 * QueryHandler interface
 * 查询处理器接口定义
 * 
 * @author BA
 */
public interface QueryHandler {
	/**
	 * @param query
	 * @return
	 */
	Object handle(String query);

	/**
	 * @param query
	 * @param context
	 * @return
	 */
	Object handle(String query, Object context);
}
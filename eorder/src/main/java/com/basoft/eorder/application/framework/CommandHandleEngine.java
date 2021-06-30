package com.basoft.eorder.application.framework;

import java.util.Map;

/**
 * 非查询（增删改）处理器接口定义
 * 
 * @author BA
 */
public interface CommandHandleEngine {
	/**
	 * @param cmdName
	 * @return
	 */
	Class<? extends Command> getCommandClass(String cmdName);

	/**
	 * @param cmdName
	 * @return
	 */
	CommandHandlerContext newCommandHandlerContext(String cmdName);

	/**
	 * @param cmdName
	 * @param props
	 * @return
	 */
	CommandHandlerContext newCommandHandlerContext(String cmdName, Map<String, Object> props);

	/**
	 * @return
	 */
	CommandHandler getCommandHandler();
}
package com.basoft.eorder.interfaces.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.framework.QueryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description:
 * 
 * @author woonill
 * @Date Created in 下午6:47 2018/12/7
 */
public abstract class CQRSAbstractController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private QueryHandler queryHandler;
    
    private CommandHandleEngine commandHandleEngine;


	protected CQRSAbstractController() {
	}
    
	public CQRSAbstractController(QueryHandler queryHandler, CommandHandleEngine commandHandleEngine) {
		this.queryHandler = queryHandler;
		this.commandHandleEngine = commandHandleEngine;
	}

	/********************************************查询业务核心-START*********************************************************************/
	/**
	 * 查询业务核心
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	@GetMapping("/query")
	public Object queryExec(@RequestParam(name = "param", required = true) String param, HttpServletRequest request) {
		return queryHandler.handle(param, newQueryHandlerContext(request));
	}
	
    /**
     * 查询业务上下文初始化
     * 
     * @param request
     * @return
     */
    protected Object newQueryHandlerContext(HttpServletRequest request){
        return Collections.emptyMap();
    }
    /********************************************查询业务核心-END*********************************************************************/
    
    
    
    /********************************************非查询业务核心-START*********************************************************************/
	/**
	 * 非查询业务核心（新增、修改和删除）
	 * 
	 * @param name
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/command/{name}", method = RequestMethod.POST)
	@ResponseBody
	public Object commandExec(@PathVariable("name") String name, HttpServletRequest request) throws Exception {
		final Class<? extends Command> cmdClazz = commandHandleEngine.getCommandClass(name);
		
		final CommandHandlerContext context = initCommandHandleContext(name, request);
		
		final Command command = toCommand(cmdClazz, request);
		
		return commandHandleEngine.getCommandHandler().exec(command, context);
	}

	/**
	 * 非查询业务（新增、修改和删除）上下文初始化
	 * 
	 * @param commandName
	 * @param request
	 * @return
	 */
	protected CommandHandlerContext initCommandHandleContext(String commandName, HttpServletRequest request) {
		return commandHandleEngine.newCommandHandlerContext(commandName, newCommandHandlerContextMap(request));
	}

    /**
     * 非查询业务（新增、修改和删除）上下文初始化
     * 
     * @param request
     * @return
     */
    protected Map<String,Object> newCommandHandlerContextMap(HttpServletRequest request){
        return Collections.emptyMap();
    }

	/**
	 * 非查询业务参数转化
	 * 
	 * @param commandClazz
	 * @param request
	 * @return
	 */
	protected Command toCommand(Class<? extends Command> commandClazz, HttpServletRequest request) {
		try {
			return new ObjectMapper().readValue(request.getInputStream(), commandClazz);
		} catch (IOException e) {
			logger.error("Command Handle Error<<::>>" +  e.getMessage());
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}
	/********************************************非查询业务核心-END*********************************************************************/

	protected String toJson(Object o) {
		try {
			return new ObjectMapper().writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
    protected QueryHandler getQueryHandler(){
        return this.queryHandler;
    }

    protected CommandHandleEngine getCommandHandleEngine(){
        return this.commandHandleEngine;
    }


	/**
	 * 工具方法，获取ip地址
	 *
	 * @param req
	 * @return
	 */
	public String getRealIP(HttpServletRequest req) {
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		ip = ip.split(",")[0];
		return ip;
	}
}
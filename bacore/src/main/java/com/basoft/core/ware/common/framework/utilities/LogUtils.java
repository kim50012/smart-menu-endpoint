package com.basoft.core.ware.common.framework.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtils {
	public static final Boolean LOGGING_ABLE = true;

	private static final transient Log logger = LogFactory.getLog(LogUtils.class);

	public static String format(String src) {
		return format(src, 20);
	}

	public static String format(String src, int length) {
		return format(src, length, "=");
	}

	public static String format(String src, String pad) {
		return format(src, 20, pad);
	}

	public static String format(String src, int length, String pad) {
		if (StringUtils.isBlank(src)) {
			src = "";
		}
		if (src.length() > length) {
			return src + pad;
		}
		return StringUtil.rightPadding(src, length, pad);
	}

	public static void getActionInfo() {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>[Called Action Method]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("className = " + className);
		logger.info("methodName = " + methodName);
		logger.info("lineNumber = " + lineNumber);
		// logger.info("calledTime = " + DateUtil.toString(new Date(),true));
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<[Called Action Method]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		// logger.info(Thread.currentThread().getStackTrace()[2].getMethodName());
		// logger.info(Thread.currentThread().getStackTrace()[2].getClassName());
		// logger.info(Thread.currentThread().getStackTrace()[2].getLineNumber());
	}

	public static void getMethodExecuteTime(Long startTime) {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		Long endTime = System.currentTimeMillis();
		String simpleClassName = className;
		if (className.lastIndexOf(".") > -1) {
			simpleClassName = className.substring(className.lastIndexOf(".") + 1);
		}
		long executionTime = endTime - startTime;
		logger.info("*************[Method Execution Millis]**************");
		logger.info(simpleClassName + "." + methodName + "()执行时间为 " + executionTime + "ms");
		if (executionTime > 10000) {
			logger.error(simpleClassName + "." + methodName + "()执行时间大于10秒!!!!!!");
		} else if (executionTime > 5000) {
			logger.error(simpleClassName + "." + methodName + "()执行时间大于5秒!!!");
		}
		logger.info("**************[Method Execution Millis]**************");
	}

	public static void main(String[] args) {
		logger.info(format("aaa"));
		logger.info(format("a"));
		logger.info(format("dfsfdsdfs"));
		logger.info(format("ddddddddddssssssssssssssssssssssssssssssssssdd"));
	}
}

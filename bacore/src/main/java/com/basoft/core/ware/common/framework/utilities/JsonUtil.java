package com.basoft.core.ware.common.framework.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonUtil {
	// 用于自定义处理Date类型的字段，让他显示的格式是yyyy-MM-dd HH:mm:ss
	private static JsonConfig jsonConfig = new JsonConfig();

	private static final transient Log logger = LogFactory.getLog(JsonUtil.class);
	static {
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateValueProcessor());
	}

	/**
	 * 根据原来的Page对象，取出结果总数和list返回给extjs
	 * 
	 * @param page
	 * @return
	 */
	/*@SuppressWarnings("static-access")
	public static String getJsonString(Page page) {
		String s = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalCount());
		map.put("list", page.getData());
		s = new JSONArray().fromObject(map, jsonConfig).toString();
		s = s.substring(1, s.length() - 1);
		if (logger.isDebugEnabled()) {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug(s);
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		return s;
	}*/

	public static String getJsonString(Object object) {
		String s = JSONObject.fromObject(object, jsonConfig).toString();
		if (logger.isDebugEnabled()) {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug(s);
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		return s;
	}

	@SuppressWarnings("rawtypes")
	public static String getJsonString(List list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", JSONArray.fromObject(list, jsonConfig));
		if (logger.isDebugEnabled()) {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug(jsonObject.toString());
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		return jsonObject.toString();
	}

	public static JSONObject parseGridParamJOSN(String jsonString) {
		JSONObject JSON = null;
		JSON = (JSONObject) JSONSerializer.toJSON(jsonString);
		return JSON;
	}

	@SuppressWarnings("rawtypes")
	public static String getNoTitleJsonString(List list) {
		try {
			String s = JSONArray.fromObject(list, jsonConfig).toString();
			if (logger.isDebugEnabled()) {
				logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.debug(s);
				logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public static String toJSONString(List list) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		return JSONArray.fromObject(list, jsonConfig).toString();
	}

	public static Integer getInt(JSONObject object, String key) {
		Integer returns = null;
		try {
			returns = object.getInt(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static Long getLong(JSONObject object, String key) {
		Long returns = null;
		try {
			returns = object.getLong(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static Boolean getBoolean(JSONObject object, String key) {
		Boolean returns = null;
		try {
			returns = object.getBoolean(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}
	
	public static Double getDouble(JSONObject object, String key) {
		Double returns = null;
		try {
			returns = object.getDouble(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static String getString(JSONObject object, String key) {
		String returns = null;
		try {
			returns = object.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static JSONArray getJSONArray(JSONObject object, String key) {
		JSONArray returns = null;
		try {
			returns = object.getJSONArray(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static JSONObject getJSONObject(JSONObject object, String key) {
		JSONObject returns = null;
		try {
			returns = object.getJSONObject(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returns;
	}

	public static void main(String[] args) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("1", new Date());
		map1.put("2", 2);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("2", new Date());
		list.add(map1);
		list.add(map2);
		if (logger.isDebugEnabled()) {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug(getJsonString(list));
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		getJsonString(list);
	}
}

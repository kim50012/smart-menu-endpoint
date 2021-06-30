package com.basoft.core.ware.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class SsgSSOSignUtils {
	private static final String SIGN = "sign";

	private static final String KEY = "AqsJixN1Zh8BeXnfrRe47JfTD6PGC1Ng";

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("shopId", "11");
		map.put("mobile", "18853118887");
		map.put("content", "中国你好");
		map.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		String sign = sign(map);
		map.put(SIGN, sign);
		System.out.println(verify(map));
	}

	/**
	 * 加密
	 * 
	 * @return
	 */
	public static String sign(Map<String, String> map) {
		String string1 = "";
		Map<String, Object> treemap = new TreeMap<String, Object>();
		Set<Entry<String, String>> sets = map.entrySet();
		for (Entry<String, String> entry : sets) {
			treemap.put(entry.getKey(), entry.getValue());
		}
		Iterator<Entry<String, Object>> titer = treemap.entrySet().iterator();
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String k = ent.getKey().toString();
			String v = ent.getValue().toString();
			if (StringUtils.isBlank(v)) {
				continue;
			}
			if (k.equals(SIGN)) {
				continue;
			}
			string1 += "&" + ent.getKey().toString() + "=" + ent.getValue().toString();
		}
		string1 = string1.substring(1);
		String stringSignTemp = string1 + "&key=" + KEY;
		// String newSign = MD5(stringSignTemp);
		String newSign = sha256(stringSignTemp);
		System.out.println("string1=\n" + string1);
		System.out.println("stringSignTemp=\n" + stringSignTemp);
		System.out.println("newSign=" + newSign);
		return newSign;
	}

	public static boolean verify(Map<String, String> map) {
		String string1 = "";
		String sign = map.get(SIGN);
		Map<String, Object> treemap = new TreeMap<String, Object>();
		Set<Entry<String, String>> sets = map.entrySet();
		for (Entry<String, String> entry : sets) {
			treemap.put(entry.getKey(), entry.getValue());
		}
		Iterator<Entry<String, Object>> titer = treemap.entrySet().iterator();
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String k = ent.getKey().toString();
			String v = ent.getValue().toString();
			if (StringUtils.isBlank(v)) {
				continue;
			}
			if (k.equals(SIGN)) {
				continue;
			}
			string1 += "&" + ent.getKey().toString() + "=" + ent.getValue().toString();
		}
		string1 = string1.substring(1);
		String stringSignTemp = string1 + "&key=" + KEY;
		// String newSign = MD5(stringSignTemp);
		String newSign = sha256(stringSignTemp);
		System.out.println("oldSign=" + sign);
		System.out.println("newSign=" + newSign);
		return newSign.equals(sign);
	}

	private static String MD5(String original) {
		return DigestUtils.md5Hex(original).toUpperCase();
	}

	private static String sha256(String original) {
		return DigestUtils.sha256Hex(original).toUpperCase();
	}
}

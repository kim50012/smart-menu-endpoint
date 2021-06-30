package com.basoft.core.ware.wechat.util;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtils {
	public static String MD5(String original) {
		return DigestUtils.md5Hex(original).toUpperCase();
	}

	public static String sha256(String original) {
		return DigestUtils.sha256Hex(original).toUpperCase();
	}

	public static String sha384(String original) {
		return DigestUtils.sha384Hex(original).toUpperCase();
	}

	public static String sha512(String original) {
		return DigestUtils.sha512Hex(original).toUpperCase();
	}

	public static void main(String[] args) {
		updatePwd("123456");
		updatePwd("64132601");
		updatePwd("demo");
		updatePwd("29102964");
		updatePwd("kdi1234");
	}
	
	public static void updatePwd(String org){
		System.out.println(sha256(org).length());
		System.out.println(DigestUtils.sha256Hex(org));
//		String dist = sha256(org);
//		String sql = "UPDATE [USER] \nSET PWD = '" + dist + "' \nWHERE PWD = '" + org + "' \n";
//		System.out.println(sql);
	}
}
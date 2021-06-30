package com.basoft.core.ware.wechat.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SQLDateTimeUtils {
	public static Date parseToDate(java.util.Date date) {
		Date sqlDate = null;
		try {
			sqlDate = new Date(date.getTime());
		} catch (Exception e) {
			sqlDate = null;
		}
		return sqlDate;
	}

	public static Date parseToDate(String date, String format) {
		Date sqlDate = null;
		try {
			SimpleDateFormat fDate = new SimpleDateFormat(format);
			sqlDate = parseToDate(fDate.parse(date));
		} catch (Exception e) {
			sqlDate = null;
		}
		return sqlDate;
	}

	public static Date parseToDate(String date) {
		return parseToDate(date, "yyyy-MM-dd");
	}

	public static Time parseToTime(java.util.Date date) {
		Time sqlTime = null;
		try {
			sqlTime = new Time(date.getTime());
		} catch (Exception e) {
			sqlTime = null;
		}
		return sqlTime;
	}

	public static Time parseToTime(String time) {
		Time sqlTime = null;
		try {
			sqlTime = Time.valueOf(time);
		} catch (Exception e) {
			sqlTime = null;
		}
		return sqlTime;
	}

	public static Timestamp parseToTimestamp(java.util.Date date) {
		Timestamp sqlTimestamp = null;
		try {
			sqlTimestamp = new Timestamp(date.getTime());
		} catch (Exception e) {
			sqlTimestamp = null;
		}
		return sqlTimestamp;
	}

	public static Timestamp parseToTimestamp(String dateTime, String format) {
		Timestamp sqlTimestamp = null;
		try {
			SimpleDateFormat fDateTime = new SimpleDateFormat(format);
			sqlTimestamp = parseToTimestamp(fDateTime.parse(dateTime));
		} catch (Exception e) {
			sqlTimestamp = null;
		}
		return sqlTimestamp;
	}

	public static Timestamp parseToTimestamp(String dateTime) {
		return parseToTimestamp(dateTime, "yyyy-MM-dd HH:mm:ss");
	}

	public static String parseToYear(java.util.Date date) {
		Date sqlDate = null;
		try {
			sqlDate = new Date(date.getTime());
		} catch (Exception e) {
			sqlDate = null;
		}
		return sqlDate.toString().substring(0, 4);
	}
}

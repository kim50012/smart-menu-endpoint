package com.basoft.core.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.ocpsoft.prettytime.PrettyTime;

import com.basoft.core.constants.CoreConstants;
import com.google.common.collect.Lists;

public class MyDateTimeUtils {

	/**
	 * 1天的时间86400000L=24小时*60分钟*60秒*1000毫秒
	 */
	private static final long ONE_DAY_NUM = 86400000L;

	/**
	 * 格林尼治时间
	 */
	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");

	/*************************************************************************************
	 * 日期格式化操作
	 ************************************************************************************/

	/**
	 * getGMTDateTime: 获取当前格林威治时间 <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static Date getGMTDateTime() {
		GregorianCalendar rightNow = new GregorianCalendar(TIME_ZONE_GMT);
		return rightNow.getTime();
	}

	/**
	 * getGMTDateTime: 获取格林威治时间 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static Date getGMTDateTime(Date date) {
		Calendar calendar = Calendar.getInstance(TIME_ZONE_GMT);
		calendar.setTime(date);
		return calendar.getTime();
	}

	/**
	 * getGMTDateTime: 把时间戳转为格林威治时间 <br/>
	 * 
	 * @author zhiyong.li
	 * @param longDate
	 * @return
	 */
	public static Date getGMTDateTime(Long longDate) {
		Calendar calendar = Calendar.getInstance(TIME_ZONE_GMT);
		calendar.setTimeInMillis(longDate);
		return calendar.getTime();
	}

	/**
	 * formatDateForString:(格式化日期). <br/>
	 * 
	 * @author zhiyong.li
	 * @param datetime
	 *            日期字符串
	 * @param pattern
	 *            格式类型
	 * @return Date
	 */
	public static Date formatDateForString(String datetime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getMinute: 获取两个时间之间的分钟数 <br/>
	 * 
	 * @author zhiyong.li
	 * @param endDate
	 *            结束时间
	 * @param beginDate
	 *            开始时间
	 * @return
	 */
	public static int getMinute(Date endDate, Date beginDate) {
		long between = (endDate.getTime() - beginDate.getTime()) / 1000;// 除以1000是为了转换成秒
		long min = between / 60;

		return Integer.parseInt(Long.toString(min));
	}

	/**
	 * formatTimeStrForStamp:(格式化时间戳). <br/>
	 * 
	 * @author zhiyong.li
	 * @param ts
	 *            Timestamp时间戳
	 * @param pattern
	 *            格式化类型
	 * @return string
	 */
	public static String formatTimeStrForStamp(Timestamp ts, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(ts.getTime()));
	}

	/**
	 * getBeforeAndAfterDays:(根据当前日期获取另外的日期). <br/>
	 * 
	 * @author zhiyong.li
	 * @param dayDate
	 *            日期字符串
	 * @param num
	 *            天数
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getBeforeAndAfterDays(String dayDate, int num, String pattern) {
		return DateFormatUtils.format(DateUtils.addDays(formatDateForString(dayDate, pattern), num), pattern);
	}

	/**
	 * pretty:(根据日期A获取当前时间距离日期A的间隔). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 *            Date类型日期
	 * @return
	 */
	public static String pretty(Date date) {
		PrettyTime pretty = new PrettyTime();
		pretty.setLocale(Locale.CHINA);
		return pretty.format(date);
	}

	/**
	 * pretty:(根据日期A获取当前时间距离日期A的间隔). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 *            String类型
	 * @return
	 */
	public static String pretty(String date) {
		return pretty(formatDateForString(date, CoreConstants.DATE_FORMAT_YYYY_MM_DD));
	}

	/**
	 * getNextDate:(获取当前日期的下一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getNextDate(String pattern) {
		Timestamp ts = new Timestamp(System.currentTimeMillis() + ONE_DAY_NUM);
		return new SimpleDateFormat(pattern).format(ts);
	}

	/**************************************************************************
	 * 对日期的操作
	 *************************************************************************/

	/**
	 * isLastDayOfMonth:(判断当前日期是否是当月最后一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static boolean isLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		// 获取前月的最后一天
		calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		String lastday = DateFormatUtils.format(calendar.getTime(), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		String currentDay = DateFormatUtils.format(new Date(), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		if (lastday.equals(currentDay)) {
			return true;
		}
		return false;
	}

	/**
	 * getLastDayOfMonth:(获取某年某月的最后一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param year
	 *            年数
	 * @param month
	 *            月数
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month, String pattern) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);

		return DateFormatUtils.format(cal.getTime(), pattern);
	}

	/**
	 * getLastDayOfMonth:(获取当月的最后一天).
	 * 
	 * @param pattern
	 *            格式
	 * @author zhiyong.li
	 * @return
	 */
	public static String getLastDayOfMonth(String pattern) {
		Calendar cal = Calendar.getInstance();
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);

		return DateFormatUtils.format(cal.getTime(), pattern);
	}

	/**
	 * getWorkDaysOfMonth:(获取当月的工作日,只按照日历中的来，不按照中国的节假日来计算). <br/>
	 * 
	 * @author zhiyong.li
	 * @param currentDate
	 * @return
	 */
	public static List<Date> getWorkDaysOfMonth(Date currentDate) {
		List<Date> dates = Lists.newArrayList();
		Calendar cal = Calendar.getInstance();
		int year = getDateYear(currentDate);
		int month = getDateMonth(currentDate);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);

		while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
			int day = cal.get(Calendar.DAY_OF_WEEK);

			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				dates.add((Date) cal.getTime().clone());
			}
			cal.add(Calendar.DATE, 1);
		}
		return dates;
	}

	/**
	 * getWorkDateStrOfMonth:(获取当月的工作日,只按照日历中的来，不按照中国的节假日来计算). <br/>
	 * 
	 * @author zhiyong.li
	 * @param currentDate
	 * @return
	 */
	public static List<String> getWorkDayStrOfMonth(Date currentDate) {
		List<String> dateList = Lists.newArrayList();
		Calendar cal = Calendar.getInstance();
		int year = getDateYear(currentDate);
		int month = getDateMonth(currentDate);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);

		while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
			int day = cal.get(Calendar.DAY_OF_WEEK);

			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				dateList.add(
						DateFormatUtils.format((Date) cal.getTime().clone(), CoreConstants.DATE_FORMAT_YYYY_MM_DD));
			}
			cal.add(Calendar.DATE, 1);
		}
		return dateList;
	}

	/**
	 * getWorkDaysOfMonth:(获取当月前7个工作日). <br/>
	 * 
	 * @author zhiyong.li
	 */
	public static List<String> getWorkDaysOfMonth() {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int year_Now = now.get(Calendar.YEAR);// 当前年
		int month_Now = now.get(Calendar.MONTH) + 1;// 当前月
		int day_Now = now.get(Calendar.DATE);// 当前日
		System.out.println(year_Now);
		System.out.println(month_Now);
		System.out.println(day_Now);
		now.set(Calendar.DATE, 1);// 设置日期为1
		int num = 0;
		List<String> dataList = Lists.newArrayList();
		while (now.get(Calendar.YEAR) == year_Now && now.get(Calendar.MONTH) < month_Now && num < 7) {
			int day = now.get(Calendar.DAY_OF_WEEK);
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				String date = now.get(Calendar.YEAR) + "年" + now.get(Calendar.MONTH) + 1 + "月" + now.get(Calendar.DATE)
						+ "日";
				dataList.add(date);
				num++;
			}
			now.add(Calendar.DATE, 1);// 日期加1
		}
		return dataList;
	}

	/**
	 * isFirstDayOfMonth:(判断当天是否为本月第一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static boolean isFirstDayOfMonth() {
		boolean flag = false;
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		if (1 == today) {
			flag = true;
		}
		return flag;
	}

	/**
	 * getFirstDayOfNextMonth:(获取下一个月的第一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getFirstDayOfNextMonth(String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return DateFormatUtils.format(calendar, pattern);
	}

	/**
	 * getLastDateOfMonth:(获取上个月的最后一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getLastDateOfMonth(String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return DateFormatUtils.format(calendar, pattern);
	}

	/**
	 * getLastDayOfMonth:(获取任意时间的月的最后一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getLastDayOfMonth(String repeatDate, String pattern) {
		Calendar calendar = Calendar.getInstance();
		if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
			calendar.setTime(formatDateForString(repeatDate, pattern));
		}
		// 当月最大的一天
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxDay);
		return DateFormatUtils.format(calendar, pattern);
	}

	/**
	 * getFirstDayOfMonth:(获取任意时间的月第一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getFirstDayOfMonth(String repeatDate, String pattern) {
		Calendar calendar = Calendar.getInstance();
		if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
			calendar.setTime(formatDateForString(repeatDate, pattern));
		}
		// 本月最小的一天
		int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, minDay);
		return DateFormatUtils.format(calendar, pattern);
	}

	/**
	 * getModify2DaysAgo:(不论是当前时间，还是历史时间 皆是时间点的前天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 * @param style
	 * @return
	 */
	public static String getModify2DaysAgo(Date repeatDate, String style) {
		Calendar cal = Calendar.getInstance();
		String daysAgo = "";
		SimpleDateFormat dft = new SimpleDateFormat(style);
		if (repeatDate == null || "".equals(repeatDate)) {
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 2);

		} else {
			int year = getDateYear(repeatDate);
			String monthsString = String.valueOf(getDateMonth(repeatDate));
			int month;
			if ("0".equals(monthsString.substring(0, 1))) {
				month = Integer.parseInt(monthsString.substring(1, 2));
			} else {
				month = Integer.parseInt(monthsString.substring(0, 2));
			}
			String dateString = String.valueOf(getDateDay(repeatDate));
			int date;
			if ("0".equals(dateString.subSequence(0, 1))) {
				date = Integer.parseInt(dateString.substring(1, 2));
			} else {
				date = Integer.parseInt(dateString.substring(0, 2));
			}
			cal.set(year, month - 1, date - 1);
		}
		daysAgo = dft.format(cal.getTime());
		return daysAgo;
	}

	/**
	 * getModifyNumDaysAgo:(不论是当前时间，还是历史时间 皆是时间点的T-N天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 * @param param
	 * @param style
	 * @return
	 */
	public static String getModifyNumDaysAgo(Date repeatDate, int param, String style) {
		Calendar cal = Calendar.getInstance();
		String daysAgo = "";
		SimpleDateFormat dft = new SimpleDateFormat(style);
		if (repeatDate == null || "".equals(repeatDate)) {
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - param);

		} else {
			int year = getDateYear(repeatDate);
			String monthsString = String.valueOf(getDateMonth(repeatDate));
			int month;
			if ("0".equals(monthsString.substring(0, 1))) {
				month = Integer.parseInt(monthsString.substring(1, 2));
			} else {
				month = Integer.parseInt(monthsString.substring(0, 2));
			}
			String dateString = String.valueOf(getDateDay(repeatDate));
			int date;
			if ("0".equals(dateString.subSequence(0, 1))) {
				date = Integer.parseInt(dateString.substring(1, 2));
			} else {
				date = Integer.parseInt(dateString.substring(0, 2));
			}
			cal.set(year, month - 1, date - param + 1);
		}
		daysAgo = dft.format(cal.getTime());
		return daysAgo;
	}

	/**
	 * getMaxDayOfMonth:(获得某年某月最大的一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param year
	 * @param mon
	 * @return
	 */
	public static int getMaxDayOfMonth(int year, int mon) {
		int dqnd = year;
		int dqyd = mon;
		if (dqyd == 12) {
			++dqnd;
			dqyd = 1;
		} else {
			++dqyd;
		}
		java.sql.Date xydyt = java.sql.Date.valueOf(dqnd + "-" + dqyd + "-1");
		Timestamp ts = new Timestamp(xydyt.getTime() - ONE_DAY_NUM);
		String day = new SimpleDateFormat(CoreConstants.DATE_FORMAT_DD).format(ts);
		return Integer.parseInt(day);
	}

	/**
	 * getMaxDayOfMonth:(获得当月最大的一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static int getMaxDayOfMonth() {
		int dqnd = Integer.parseInt(getCurrentYear());
		int dqyd = Integer.parseInt(getCurrentMonth());
		return getMaxDayOfMonth(dqnd, dqyd);
	}

	/**
	 * getBackDate:(传入一个日期型参数date，和想要获取几天前或几天后日期的参数day 返回一个类型为Date的日期). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static final Date getBackDate(Date date, int day) throws ParseException {
		Date backDate = new Date();
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, day);
		backDate = ca.getTime();
		return backDate;
	}

	/**
	 * getBackMonths:(传入一个日期型参数date，和想要获取几月前或几月后日期的参数month 返回一个类型为Date的日期).
	 * <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static final Date getBackMonths(Date date, int month) throws ParseException {
		Date backDate = new Date();
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(date);
		ca.add(Calendar.MONTH, month);
		backDate = ca.getTime();
		return backDate;
	}

	/*******************************************************************
	 * 对周的操作
	 *******************************************************************/
	/**
	 * getWeekDay:(获得当天是周几---从周日开始为第一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * getWeekDay:(获得当天是周几---从周日开始为第一天). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static int getWeekDay(String date) {
		return getWeekDay(formatDateForString(date, CoreConstants.DATE_FORMAT_YYYY_MM_DD));
	}

	/**
	 * getWeekDayCN:(根据日期获取那天是星期几，java.util.Date日期和String日期必须有一个为空). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 *            java.util.Date日期 可以为空
	 * @param dateStr
	 *            String日期 可以为空
	 * @return
	 */
	public static String getWeekDayCN(Date date, String dateStr) {
		int dayOfWeek = 0;
		if (StringUtils.isBlank(dateStr)) {
			dayOfWeek = getWeekDay(date);
		} else {
			dayOfWeek = getWeekDay(dateStr);
		}
		String weekDay = "";
		switch (dayOfWeek) {
		case 1:
			weekDay = "星期日";
			break;
		case 2:
			weekDay = "星期一";
			break;
		case 3:
			weekDay = "星期二";
			break;
		case 4:
			weekDay = "星期三";
			break;
		case 5:
			weekDay = "星期四";
			break;
		case 6:
			weekDay = "星期五";
			break;
		case 7:
			weekDay = "星期六";
			break;
		}
		return weekDay;
	}

	/**
	 * getCurrentWeek:(获得当前的周数-----西方模式). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static int getCurrentWeek() {
		Calendar cd = Calendar.getInstance();
		// setFirstDayOfWeek的方法意思只对WEEK_OF_MONTH 与WEEK_OF_YEAR 有作用.
		cd.setFirstDayOfWeek(Calendar.MONTH);
		cd.setMinimalDaysInFirstWeek(Calendar.DAY_OF_WEEK);
		cd.setTime(new java.util.Date());
		return cd.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * getDayOfWeek:(判断某年某月某日是周几). <br/>
	 * 
	 * @author zhiyong.li
	 * @param year
	 *            年数
	 * @param mon
	 *            月数
	 * @param day
	 *            天数
	 * @return
	 */
	public static int getDayOfWeek(int year, int mon, int day) {
		Calendar cal = new GregorianCalendar(year, mon - 1, day);
		int dayOfWeek = cal.get(7);
		if (dayOfWeek - 1 == 0) {
			return 7;
		}
		return (dayOfWeek - 1);
	}

	/**
	 * getDayOfWeek:(根据传入的参数判断是周几). <br/>
	 * 
	 * @author zhiyong.li
	 * @param text
	 * @return
	 */
	public final static String getDayOfWeek(int text) {
		String map[] = { "零", "日", "一", "二", "三", "四", "五", "六" };
		if (text > 0 && text < map.length)
			return map[text];
		else
			return "";
	}

	/**
	 * getMaxWeekOfYear:(获得当年中最大周数----西方). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static int getMaxWeekOfYear() {
		Calendar cd = Calendar.getInstance();
		cd.setFirstDayOfWeek(2);
		cd.setMinimalDaysInFirstWeek(7);
		cd.setTime(new java.util.Date());
		if ((cd.get(2) == 0) && (cd.get(5) < 7)) {
			return cd.get(3);
		}
		cd.set(cd.get(1), 11, 31);
		return cd.get(3);
	}

	/************************************************************************
	 * 对月份的操作
	 ***********************************************************************/

	/**
	 * getCurrentMonth:(获得当前月份). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static String getCurrentMonth() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return new SimpleDateFormat(CoreConstants.DATE_FORMAT_MM).format(ts);
	}

	/**
	 * getNextMonth:(根据当前月份获取别的月份). <br/>
	 * 
	 * @author zhiyong.li
	 * @param currentDate
	 *            当前日期
	 * @param num
	 * @return
	 */
	public static String getBeforeAndAfterMonth(String currentDate, int num) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(CoreConstants.DATE_FORMAT_YYYY_MM);
			SimpleDateFormat sdf2 = new SimpleDateFormat(CoreConstants.DATE_FORMAT_MM);
			Date date = sdf.parse(currentDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, num);
			return sdf2.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * getLastMonth:(获取上一个月). <br/>
	 * 
	 * @author zhiyong.li
	 * @param style
	 *            格式
	 * @return
	 */
	public static String getLastMonth(String style) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat dft = new SimpleDateFormat(style);
		String lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * getPreMonth:(获取下一个月.). <br/>
	 * 
	 * @author zhiyong.li
	 * @param style
	 * @return
	 */
	public static String getPreMonth(String style) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		SimpleDateFormat dft = new SimpleDateFormat(style);
		String preMonth = dft.format(cal.getTime());
		return preMonth;
	}

	/**
	 * 
	 * getPreMonth:(获取任意时间的下一个月). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 *            日期
	 * @param style
	 * @return
	 */
	public static String getPreMonth(Date repeatDate, String style) {
		String lastMonth = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat(style);
		int year = getDateYear(repeatDate);
		String monthsString = String.valueOf(getDateMonth(repeatDate));
		int month;
		if ("0".equals(monthsString.substring(0, 1))) {
			month = Integer.parseInt(monthsString.substring(1, 2));
		} else {
			month = Integer.parseInt(monthsString.substring(0, 2));
		}
		cal.set(year, month, Calendar.DATE);
		lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * getLastMonth:(获取任意时间的上一个月). <br/>
	 * 
	 * @author zhiyong.li
	 * @param repeatDate
	 * @param style
	 * @return
	 */
	public static String getLastMonth(Date repeatDate, String style) {
		String lastMonth = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat(style);
		int year = getDateYear(repeatDate);
		String monthsString = String.valueOf(getDateMonth(repeatDate));
		int month;
		if ("0".equals(monthsString.substring(0, 1))) {
			month = Integer.parseInt(monthsString.substring(1, 2));
		} else {
			month = Integer.parseInt(monthsString.substring(0, 2));
		}
		cal.set(year, month - 2, Calendar.DATE);
		lastMonth = dft.format(cal.getTime());
		return lastMonth;
	}

	/**
	 * getNext12Month:(根据某年某月获取之后的12个月的数组). <br/>
	 * 
	 * @author zhiyong.li
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param style
	 *            年和日之间的连接符
	 * @return
	 */
	public static String[] getNext12Month(int year, int month, String style) {
		String[] ret = new String[12];
		int nextYear = year;
		int nextMonth = month;
		for (int i = 0; i < 12; ++i) {
			if (++nextMonth > 12) {
				++nextYear;
				nextMonth = 1;
			}
			ret[i] = new String(String.valueOf(nextYear) + style + String.valueOf(nextMonth));
		}
		return ret;
	}

	/**
	 * getNext12Month:(根绝当年当月获取之后的12个月的数组). <br/>
	 * 
	 * @author zhiyong.li
	 * @param style
	 *            年和日之间的连接符
	 * @return
	 */
	public static String[] getNext12Month(String style) {
		int year = Integer.parseInt(getCurrentYear());
		int month = Integer.parseInt(getCurrentMonth());
		return getNext12Month(year, month, style);
	}

	/***************************************************************************
	 * 获取年份
	 **************************************************************************/

	/**
	 * parseDateTime:(获得年月日时分秒的map集合). <br/>
	 * 
	 * @author zhiyong.li
	 * @param rightNow
	 *            Calendar类型日期
	 * @return
	 */
	public final static HashMap<String, String> parseDateTime(Calendar rightNow) {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("date",
				String.valueOf(rightNow.get(Calendar.YEAR)) + "-" + String.valueOf(rightNow.get(Calendar.MONTH) + 1)
						+ "-" + String.valueOf(rightNow.get(Calendar.DAY_OF_MONTH)));
		result.put("time", String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.valueOf(rightNow.get(Calendar.MINUTE)) + ":" + String.valueOf(rightNow.get(Calendar.SECOND)));
		result.put("hour", String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)));
		result.put("minute", String.valueOf(rightNow.get(Calendar.MINUTE)));
		result.put("second", String.valueOf(rightNow.get(Calendar.SECOND)));
		return result;
	}

	/**
	 * getCurrentYear:(获得当前年份). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static String getCurrentYear() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return new SimpleDateFormat(CoreConstants.DATE_FORMAT_YYYY).format(ts);
	}

	/**
	 * getWorkYears:(根据当前日期获得与当前日期间隔的年数带小数). <br/>
	 * 
	 * @author zhiyong.li
	 * @param time
	 * @return
	 */
	public static String getWorkYears(String time) {
		SimpleDateFormat format = new SimpleDateFormat(CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		Date date1 = new Date();
		Date date2;
		double year = 0.00;
		try {
			date2 = format.parse(time);
			long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
			double aa = (double) day;
			aa = aa / 365;
			BigDecimal bigDecimal = new BigDecimal(aa);
			year = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(year);
	}

	public final static int[] getDateElements(Date date) {
		if (date == null)
			return null;
		Calendar cal = getStaticCalendars(date);
		int ymd[] = new int[7];
		ymd[0] = cal.get(Calendar.YEAR);
		ymd[1] = cal.get(Calendar.MONTH) + 1;
		ymd[2] = cal.get(Calendar.DATE);
		ymd[3] = cal.get(Calendar.HOUR_OF_DAY);
		ymd[4] = cal.get(Calendar.MINUTE);
		ymd[5] = cal.get(Calendar.SECOND);
		return ymd;
	}

	/**
	 * getStaticCalendars:(得到一个静态的 Calendar 临时对象). <br/>
	 * 
	 * @author zhiyong.li
	 * @return
	 */
	public static Calendar getStaticCalendars() {
		return getStaticCalendars(null);
	}

	/**
	 * getStaticCalendars:(从一个 <code>java.util.Date</code> 对象得到一个表示该日期的临时
	 * <code>Calendar</code> 对象. 该对象只作为临时使用). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static Calendar getStaticCalendars(java.util.Date date) {
		Calendar staticCal = new GregorianCalendar();
		if (date != null)
			staticCal.setTime(date);
		return staticCal;
	}

	/**
	 * getStaticCalendars:(得到一个静态的 给定日期和时间的 Calendar 临时对象,). <br/>
	 * 
	 * @author zhiyong.li
	 * @param time
	 *            给定Calendar 临时对象表示的日期和时间
	 * @return 一个静态的给定日期和时间(long time) Calendar 临时对象
	 */
	public static Calendar getStaticCalendars(long time) {
		Calendar cal = getStaticCalendars(null);
		if (cal != null)
			cal.setTime(new java.util.Date(time));
		return cal;
	}

	static public int getDefaultHolidays(int year, int month) {
		GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
		int x = 0;
		for (int d = 0;; d++) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
				x |= (1 << d);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			if (cal.get(Calendar.MONTH) + 1 != month)
				break;
		}
		return x;
	}

	/**
	 * 从一个 <code>java.util.Date</code> 对象得到一个表示该日期的日
	 * 
	 * @param date
	 *            <code>java.util.Date</code> 对象,从中取日
	 * @return 日期 date 表示的日
	 */
	public static int getDateDay(Date date) {
		if (date == null)
			return 0;
		return getStaticCalendars(date).get(Calendar.DATE);
	}

	/**
	 * 从一个 <code>java.util.Date</code> 对象得到一个表示该日期的月份
	 * 
	 * @param date
	 *            <code>java.util.Date</code> 对象,从中取月份
	 * @return 日期 date 表示的月份
	 */
	public static int getDateMonth(Date date) {
		if (date == null)
			return 0;
		return getStaticCalendars(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 从一个 <code>java.util.Date</code> 对象得到一个表示该日期的年份
	 * 
	 * @param date
	 *            <code>java.util.Date</code> 对象,从中取年份
	 * @return 日期 date 表示的年份
	 */
	public static int getDateYear(Date date) {
		if (date == null)
			return 0;
		return getStaticCalendars(date).get(Calendar.YEAR);
	}

	/**
	 * getBirthdayByCard:(根据身份证号码获取生日，格式yyyy-MM-dd). <br/>
	 * 
	 * @author zhiyong.li
	 * @param card
	 * @return
	 */
	public static String getBirthdayByCard(String card) {
		if (StringUtils.isNotEmpty(card)) {
			String year = StringUtils.substring(card, 6, 10);
			String month = StringUtils.substring(card, 10, 12);
			String day = StringUtils.substring(card, 12, 14);
			return StringUtils.join(year, "-", month, "-", day);
		}
		return StringUtils.EMPTY;
	}

	/****************************************************************************
	 * 日期比较方法
	 ***************************************************************************/

	/**
	 * getDifday:(比较相同月份中的两个日期相差的天数). <br/>
	 * 
	 * @author zhiyong.li
	 * @param year
	 *            年份
	 * @param mon
	 *            月份
	 * @param oldday
	 *            老天数
	 * @param newday
	 *            新天数
	 * @return
	 */
	public static long getDifday(int year, int mon, int oldday, int newday) {
		String strri = String.valueOf(oldday);
		String stryd = String.valueOf(mon);
		String strnd = String.valueOf(year);
		java.sql.Date dolddate = java.sql.Date.valueOf(strnd + "-" + stryd + "-" + strri);
		strri = String.valueOf(newday);
		stryd = String.valueOf(mon);
		strnd = String.valueOf(year);
		java.sql.Date dnewdate = java.sql.Date.valueOf(strnd + "-" + stryd + "-" + strri);
		long a = (dnewdate.getTime() - dolddate.getTime()) / ONE_DAY_NUM;
		return a;
	}

	/**
	 * getDiffDate:(求两个日期之间相差的天数(日期相减)). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDate(Calendar date1, Calendar date2) {
		return getDiffDate(date1.getTime(), date2.getTime());
	}

	/**
	 * getDiffDate:(求两个日期之间相差的天数(日期相减)). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDate(Date date1, Date date2) {
		return (int) ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
	}

	/**
	 * getDiffYear:(根据传入的日期计算与当前年份相差多少年). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static int getDiffYear(Date date) {
		Calendar rightNow = Calendar.getInstance();
		Calendar cal = getStaticCalendars(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int nowyear = rightNow.get(Calendar.YEAR);
		int nowmonth = rightNow.get(Calendar.MONTH);
		int diffyear = nowmonth >= month ? nowyear - year : nowyear - year - 1;
		return diffyear;
	}

	/**
	 * incDate:(求某一日期加或减(day为负数)后的日期). <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @param day
	 * @return
	 */
	static public Date incDate(Date date, int day) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * getSecond: 获取某个时间至今有多少秒 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		Date date1 = new Date();
		return getTwoDateSecond(date1, date);
	}

	/**
	 * getTwoDateSecond: 获得两个日期间的秒差 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 *            新时间
	 * @param date2
	 *            老时间
	 * @return
	 */
	public static int getTwoDateSecond(Date date1, Date date2) {
		long a = date1.getTime();
		long b = date2.getTime();
		int c = (int) ((a - b) / 1000);
		return c;
	}

	/**
	 * compare_date: 比较两个日期大小 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare_date(String date1, String date2) {
		Date dt1 = formatDateForString(date1, CoreConstants.DATE_FORMAT_YYYY_MM_DD_HH24MM);
		Date dt2 = formatDateForString(date2, CoreConstants.DATE_FORMAT_YYYY_MM_DD_HH24MM);
		return compare_date(dt1, dt2);
	}

	/**
	 * compare_date: 比较两个日期大小 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare_date(Date date1, Date date2) {
		try {
			if (date1.getTime() > date2.getTime()) {
				return 1;
			} else if (date1.getTime() < date2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * formatDateToDate: 把Date转成相应格式(pattern)的Date <br/>
	 * 
	 * @author zhiyong.li
	 * @param date
	 *            java.util.Date
	 * @param pattern
	 *            转换格式
	 * @return
	 */
	public static Date formatDateToDate(Date date, String pattern) {
		String dateStr = DateFormatUtils.format(date, pattern);
		return formatDateForString(dateStr, pattern);
	}

	/**
	 * getDateMillis: 把两个日期相减得到毫秒数 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 *            新时间
	 * @param date2
	 *            老时间
	 * @return
	 */
	public static long getDateMillis(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	/**
	 * formatDateToString: 把两个日期相减得到毫秒数,并格式化 <br/>
	 * 
	 * @author zhiyong.li
	 * @param date1
	 * @param date2
	 * @param pattern
	 * @return
	 */
	public static String formatDateToString(Date date1, Date date2, String pattern) {
		long number = getDateMillis(date1, date2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(number);
		return DateFormatUtils.format(calendar, pattern);
	}

	/**
	 * GTM转本地时间
	 * 
	 * @param GTMDate
	 * @return
	 */
	@SuppressWarnings("unused")
	public String GTMToLocal(String GTMDate) {
		int tIndex = GTMDate.indexOf("T");
		String dateTemp = GTMDate.substring(0, tIndex);
		String timeTemp = GTMDate.substring(tIndex + 1, GTMDate.length() - 6);
		String convertString = dateTemp + " " + timeTemp;
		SimpleDateFormat format;
		format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date result_date;
		long result_time = 0;
		if (null == GTMDate) {
			return GTMDate;
		} else {
			try {
				format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
				result_date = format.parse(convertString);
				result_time = result_date.getTime();
				format.setTimeZone(TimeZone.getDefault());
				return format.format(result_time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return GTMDate;
	}

	/***
	 * 转成格林威治时间 感觉用不到
	 */
	public String LocalToGTM(String LocalDate) {
		SimpleDateFormat format;
		format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date result_date;
		long result_time = 0;
		if (null == LocalDate) {
			return LocalDate;
		} else {
			try {
				format.setTimeZone(TimeZone.getDefault());
				result_date = format.parse(LocalDate);
				result_time = result_date.getTime();
				format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
				return format.format(result_time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return LocalDate;
	}
}

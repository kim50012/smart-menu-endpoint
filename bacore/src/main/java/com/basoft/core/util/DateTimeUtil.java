package com.basoft.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    public static final String TO_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String TO_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String Only_HHMMSS_FORMAT = "HH:mm:ss";

    public static final String Only_HHMM_FORMAT = "HH:mm";

    //-------------各种日期的格式----------
    public static final String PATTERN_YYY_MM_DD = "yyyy-MM-dd";

    public static final String PATTERN_YY_MM_DD_H_M_S = "yy-MM-dd HH:mm:ss";

    public static final String PATTERN_YYMMDDHHMM = "yyMMddHHmm";

    public static final String PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public static final String PATTERN_YYMMDD_CN = "yy年MM月dd日";


    //--------------time zone, 默认为 中国时区(东八区)-----------
    public static final TimeZone TIME_ZONE_CN = TimeZone.getTimeZone("GMT+8");

    //--------------locale, 默认为 China----------
    public static final Locale LOCALE_CN = Locale.CHINA;

    /**
     * 增加月数
     *
     * @param time 时间
     * @param n    月数
     * @return
     */
    public static long addMonth(long time, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTimeInMillis() / 1000;
    }

    public static Date getDate(Integer timeInSeconds) {
        timeInSeconds = timeInSeconds == null ? 0 : timeInSeconds;
        return getDate(timeInSeconds, TIME_ZONE_CN);
    }

    public static Date getDate(int timeInSeconds, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(timeInSeconds * 1000l);

        return calendar.getTime();
    }

    public static int getCurrentTimeInSeconds() {
        long milliseconds = new Date().getTime();

        return (int) (milliseconds / 1000l);
    }

    public static String getCurrentTimeInDays() {
        long milliseconds = new Date().getTime();
        return format((int) (milliseconds / 1000l), PATTERN_YYY_MM_DD);
    }

    public static int getTimeInSeconds(Date date) {
        long milliseconds = date.getTime();

        return (int) (milliseconds / 1000l);
    }

    public static int getTimeInSeconds(String date) {
        try {
            date = getDayStartTimeStr(date);
            return getTimeInSeconds(new SimpleDateFormat(TO_SECOND_FORMAT).parse(date));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static boolean isExpire(Date start, Date end) {
        Date now = DateTimeUtil.getNow();

        return now.before(start) || now.after(end);
    }

    public static String getDayStartTimeStr(String date) {
        if(StringUtil.isNotBlank(date)) {
            int length = date.length();
            switch (length) {
                case 10:
                    date += " 00:00:00";
                    break;
                case 13:
                    date += ":00:00";
                    break;
                case 16:
                    date += ":00";
                    break;
                default:
                    break;
            }

            return date;
        }

        return date;
    }

    public static String getDayEndTimeStr(String date) {
        if(StringUtil.isNotBlank(date)) {
            int length = date.length();
            switch (length) {
                case 10:
                    date += " 23:59:59";
                    break;
                case 13:
                    date += ":59:59";
                    break;
                case 16:
                    date += ":59";
                    break;
                default:
                    break;
            }

            return date;
        }

        return date;
    }

    public static int getTimeInSeconds(String date, String pattern) {
        try {
            return getTimeInSeconds(new SimpleDateFormat(pattern + " HH:mm:ss").parse(date + " 00:00:00"));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String format(long timestamp) {
        Date date = new Date(timestamp * 1000);
        return format(date);
    }

    public static String formatToSecond(Date date) {
        return date != null ? format(date, TO_SECOND_FORMAT) : "";
    }

    public static String formatNow() {
        return format(new Date(), TO_SECOND_FORMAT);
    }

    public static String format(long time, String pattern) {
        long timeInMills = time * 1000l;

        return DateTimeUtil.format(new Date(timeInMills), pattern);
    }
    
    public static String formatMills(long timeInMills, String pattern) {
        return DateTimeUtil.format(new Date(timeInMills), pattern);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern, TIME_ZONE_CN, LOCALE_CN);
    }

    public static String format(Date date, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone, LOCALE_CN);
    }

    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(date, pattern, timeZone, locale);
    }

    public static Date parse(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static Date parse(String date) {
        return parse(date, DEFAULT_FORMAT);
    }

    /**
     * 获取当前时间，以后可以加TimeZone.
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Date d = getDate(1510646400);
        d = getDate(1510646400, TimeZone.getTimeZone("EST"));
        System.out.println(d);
    }

	/**
	 * 得到两个日期之间的所有日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayNum
	 * @return
	 * @throws ParseException
	 */
	public static ArrayList<String> findDataAll(String startDate, String endDate, int dayNum) throws ParseException {
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date data = df.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DATE, -1); // 减1天
		Date start = cal.getTime();
		startCalendar.setTime(start);
		Date end = df.parse(endDate);
		endCalendar.setTime(end);
		ArrayList<String> arr = new ArrayList<String>();
		for (;;) {
			startCalendar.add(Calendar.DAY_OF_MONTH, dayNum);
			if (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
				arr.add(df.format(startCalendar.getTime()));
			} else {
				break;
			}
		}
		return arr;
	}

	/**
	 * 查询一天之间的所有小时（小时）
	 * 
	 * @return
	 */
	public static ArrayList<String> findHourAll() {
		String hourArr[] = { "000", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100",
				"1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900",
				"2000", "2100", "2200", "2300" };
		ArrayList<String> hourList = new ArrayList<String>();
		for (String hour : hourArr) {
			hourList.add(hour);
		}
		return hourList;
	}

    /**
     * 获取当前时间的分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }
}

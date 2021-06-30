package com.basoft.eorder.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class DateUtil {
    public static String getGMTDateStr(long time) {
        Date d = new Date(System.currentTimeMillis() + time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        return dateFormat.format(d);
    }

    public static LocalDateTime getWxPayDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    public static LocalDateTime plusWxPayDateTime(long value, TemporalUnit unit) {
        return getWxPayDateTimeNow().plus(value, unit);
    }

    public static LocalDateTime getDateTimeNow() {
        return LocalDateTime.now();
    }

    public static LocalDateTime plusDateTime(LocalDateTime dateTime, long value, TemporalUnit unit) {
        return dateTime.plus(value, unit);
    }

    /**
     * 微信使用的当前时间（北京时间）
     *
     * @return
     */
    public static String getWxPayNowStr() {
        return getFormatStr(getWxPayDateTimeNow(), "yyyyMMddHHmmss");
    }

    /**
     * 字符串时间戳 默认为 yyyyMMddHHmmss
     *
     * @param dateTime
     * @return
     */

    public static String getFormatStr(LocalDateTime dateTime) {
        return getFormatStr(dateTime, "yyyyMMddHHmmss");
    }

    /**
     * 字符串时间戳
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static String getFormatStr(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }


    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowStr() {
        return getFormatStr(LocalDateTime.now(), "yyyyMMddHHmmss");
    }

    /**
     * 得到两个日期之间的所有日期
     *
     * @param startDate
     * @param endDate
     * @param dayNum    返回值之间的间隔天数，一般设置为1即可，即返回每一天
     * @return
     * @throws ParseException
     * @author DongXifu
     */
    public static ArrayList<String> findDataAll(String startDate, String endDate, int dayNum) throws ParseException {
        // 获取当前日历，一起获取，减少各个值的间隔
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        // 格式化参数为时间对象
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = df.parse(startDate);
        Date eDate = df.parse(endDate);

        // 起始日历日期减一天
        cal.setTime(sDate);
        cal.add(Calendar.DATE, -1); // 减1天
        Date start = cal.getTime();
        startCalendar.setTime(start);

        // 截止日历日期
        endCalendar.setTime(eDate);

        ArrayList<String> dateList = new ArrayList<String>();
        for (; ; ) {
            startCalendar.add(Calendar.DAY_OF_MONTH, dayNum);
            if (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
                dateList.add(df.format(startCalendar.getTime()));
            } else {
                break;
            }
        }
        return dateList;
    }

    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * secondsToDate
     *
     * @param seconds
     * @return
     */
    public static Date secondsToDate(long seconds) {
        Date date = new Date();
        try {
            date.setTime(seconds * 1000);
        } catch (Exception e) {
            date = null;
        }
        return date;
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

    /**
     * 获取指定年月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /***
     * 日期月份减一个月
     *
     * @param datetime
     *            日期(2014-11)
     * @return 2014-10
     */
    public static String dateFormat(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, -1);
        date = cl.getTime();
        return sdf.format(date);
    }

    //获取某月最后一天
    public static String getLastDayOfMonth(String datetime){
        int year = Integer.valueOf(datetime.substring(0,4));
        int month = Integer.valueOf(datetime.substring(5,7));
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }



    /**
     * @param  startTime, endTime
     * @return java.util.List<java.lang.String>
     * @describe 获得每周第一天
     * @author Dong Xifu
     * @date 2019/2/20 上午11:15
     */
    public static ArrayList<String> getFirstDayOfWeekList(String startTime, String endTime) {
        int weekStart = getHowWeek(startTime);//第几周 2018-08-09  2019-04-03
        int weekEnd = getHowWeek(endTime);

        int yearStart = Integer.valueOf(startTime.substring(0,4));
        int yearEnd = Integer.valueOf(endTime.substring(0,4));

        List<Integer> listWeek = new LinkedList<>();
        if(yearEnd>yearStart) {
            weekEnd = getHowWeek(startTime.substring(0,5)+"12-30");
        }

        for (int i = 0; i <= weekEnd - weekStart; i++) {
            listWeek.add(weekStart + i);
        }

        ArrayList<String> listDate = new ArrayList<>();//每周第一天

        listWeek.stream().forEach(s->listDate.add(getFirstDayOfWeek(yearStart,s)));

        if(yearEnd>yearStart){
            weekStart = getHowWeek(endTime.substring(0,5)+"01-01");
            weekEnd = getHowWeek(endTime);
            listWeek.clear();
            for (int i = 0; i <= weekEnd - weekStart; i++) {
                listWeek.add(weekStart + i);
            }

            listWeek.stream().forEach(s->listDate.add(getFirstDayOfWeek(yearEnd,s)));
        }

        return listDate;
    }

    /**
     * @param  year(年), week(第几周)
     * @return java.lang.String
     * @describe每周第一天
     * @author Dong Xifu
     * @date 2019/2/20 上午10:21
     */
    public static String getFirstDayOfWeek(int year,int week){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        //设置该周第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfWeek = sdf.format(cal.getTime());

        return firstDayOfWeek;
    }

    /**
     * @param  time
     * @return int
     * @describe 查询第几周
     * @author Dong Xifu
     * @date 2019/2/20 下午2:10
     */
    public static int getHowWeek(String time){
        String today = time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(today);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取昨天，格式yyyy-MM-dd
     *
     * @return
     */
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 获取今天，格式yyyy-MM-dd
     *
     * @return
     */
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 获取N天后的日期
     * @return
     */
    public static String getFatureDate(int dayNum ) {
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        calendar2.add(Calendar.DATE, dayNum);
        return sdf2.format(calendar2.getTime());
    }

    /**
     * 获取当前时间的年月日时分秒偏移量
     *
     * @param offsetType
     * @param offsetCount
     * @return
     */
    public static String getNowTimeOffset(String offsetType, int offsetCount, String format) {
        Calendar nowTime = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat df = new SimpleDateFormat(format);
        if ("Y".equals(offsetType)) {
            nowTime.add(Calendar.YEAR, offsetCount);
        } else if ("MO".equals(offsetType)) {
            nowTime.add(Calendar.MONTH, offsetCount);
        } else if ("D".equals(offsetType)) {
            nowTime.add(Calendar.DATE, offsetCount);
        } else if ("H".equals(offsetType)) {
            nowTime.add(Calendar.HOUR, offsetCount);
        } else if ("MI".equals(offsetType)) {
            nowTime.add(Calendar.MINUTE, offsetCount);
        } else if ("S".equals(offsetType)) {
            nowTime.add(Calendar.SECOND, offsetCount);
        }
        return df.format(nowTime.getTime());
    }

    public static void main(String[] args) {
        BigDecimal b = BigDecimal.ZERO;
        b.add(new BigDecimal("250"));

        System.out.println("getWxPayNowStr :" + getWxPayNowStr());
        System.out.println("getNowStr :" + getNowStr());
        String yearMonth = DateUtil.getFormatStr(LocalDateTime.now(),"yyyyMM");
        System.out.println("yearMonth :" + yearMonth);
        yearMonth = DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS),"yyyyMM");
        System.out.println("yearMonth :" + yearMonth);

        // 指定年月的第一天和最后一天
        System.out.println(getFirstDayOfMonth(2019,9)+"--------"+getLastDayOfMonth(2019,9));

        try {
            List dateList = DateUtil.findDataAll("2019-08-30","2019-09-07",1);
            System.out.println(dateList.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(getNowTimeOffset("MI",5,"yyyyMMddHHmmss"));
    }


    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

}

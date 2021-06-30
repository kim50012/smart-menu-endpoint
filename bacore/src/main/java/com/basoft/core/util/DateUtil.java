package com.basoft.core.util;

import java.util.Date;

public class DateUtil {
    public enum Unit {
        SECOND(1000L),
        MINUTE(60000L),
        HOUR(3600000L),
        DAY(86400000L),
        WEEK(604800000L),
        MONTH(2592000000L),
        YEAR(31536000000L);

        private long interval;

        Unit(long interval) {
            this.interval = interval;
        }

        public long getInterval() {
            return this.interval;
        }
    }

    public static Date add(long amount, Unit unit) {
        return new Date(System.currentTimeMillis() + amount * unit.getInterval());
    }

    public static Date minus(long amount, Unit unit) {
        return new Date(System.currentTimeMillis() - amount * unit.getInterval());
    }
    
	/**
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
	 * @param seconds
	 * @return
	 */
	public static Date secondsToDate(int seconds) {
		Date date = new Date();
		try {
			date.setTime(seconds * 1000l);
		} catch (Exception e) {
			date = null;
		}
		return date;
	}
}

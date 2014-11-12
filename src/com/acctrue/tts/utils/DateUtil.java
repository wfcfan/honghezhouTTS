package com.acctrue.tts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static String DEFAULT_SIMPLE_TIME_FORMAT = "HH:mm";
	// public final static String START_DATE1 = "2013-12-26 00:00:00";
	// public final static String END_DATE1 = "2013-12-27 23:59:59";
	public static String DEFAULT_SIMPLE_DATE_FORMAT = "MM-dd";
	public final static String START_DATE1 = "2013-12-31 00:00:00";
	public final static String END_DATE1 = "2014-01-01 23:59:59";

	public final static String START_DATE2 = "2014-01-20 00:00:00";
	public final static String END_DATE2 = "2014-02-08 23:59:59";

	public static String getDatetime() {
		return getDatetime(new Date());
	}

	public static String getDatetime(long ms) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return sdf.format(new Date(ms));
	}

	public static String getDatetime(Date d) {
		return getDatetime(d.getTime());
	}

	public static long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return sdf.format(new Date());
	}

	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return sdf.format(date);
	}

	public static String getSimpleDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_SIMPLE_DATE_FORMAT);
		return sdf.format(date);
	}

	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String getSimpleTime(String datetime) {
		if (TextUtils.isEmpty(datetime)) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_SIMPLE_TIME_FORMAT);
		return sdf.format(parseDatetimeToDate(datetime));
	}

	public static Date parseDate(String d) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			return sdf.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long parseDatetimeToTime(String datetime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
			Date d = sdf.parse(datetime);
			return d.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String parseDatetimeToJsonDate() {
		long l = getTimeStamp();
		return parseDatetimeToJsonDate(l);
	}
	
	public static String parseDatetimeToJsonDate2(){
		long l = getTimeStamp();
		String s = "/Date(" + l + ")/";
		Log.i("parseDatetimeToJsonDate2", s);
		return s;
	}

	public static String parseDatetimeToJsonDate(long l) {
		return String.format("/Date(%d)/", l);
	}

	public static String parseDatetimeToJsonDate(Date dt) {
		long l = dt.getTime();
		return parseDatetimeToJsonDate(l);
	}

	public static String parseDatetimeToJsonDate(String d) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
			Date dt = sdf.parse(d);
			return parseDatetimeToJsonDate(dt.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Date parseDatetimeToDate(String datetime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
			return sdf.parse(datetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date add(Date date, int type, int time) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(type, time);
			return new Date(cal.getTime().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int diffDate(Date date1, Date date2) throws ParseException {
		if (date1 == null || date2 == null) {
			return 0;
		}

		long ca = date1.getTime() - date2.getTime();
		if (ca <= 0) {
			return 0;
		}

		return (int) (ca / 1000 / 60 / 60 / 24);
	}

	public static int diffHour(Date date1, Date date2) throws ParseException {
		if (date1 == null || date2 == null) {
			return 0;
		}

		long ca = date1.getTime() - date2.getTime();
		if (ca <= 0) {
			return 0;
		}

		return (int) (ca / 1000 / 60 / 60);
	}

	public static int diffMinute(Date date1, Date date2) throws ParseException {
		if (date1 == null || date2 == null) {
			return 0;
		}

		long ca = date1.getTime() - date2.getTime();
		if (ca <= 0) {
			return 0;
		}

		return (int) (ca / 1000 / 60);
	}

	public static String transDate(String storeDate){
		Long t = replaceJsonDate(storeDate);
		Date d = new Date(t);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return dateFormat.format(d);
	}
	
	static private Long replaceJsonDate(String s){
		s = s.replaceAll("\\+0800", "");
		s = s.replaceAll("/", "");
		s = s.replaceAll("Date", "");
		s = s.replaceAll("\\(", "");
		s = s.replaceAll("\\)", "");
		System.out.println(s);
		return Long.valueOf(s);
	}

}

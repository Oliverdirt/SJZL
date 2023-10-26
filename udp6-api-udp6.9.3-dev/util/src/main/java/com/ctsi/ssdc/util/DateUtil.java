package com.ctsi.ssdc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期工具类
 * @author ctsi
 *
 */
public class DateUtil {
	/**
	 * yyyyMMdd
	 */
	public static String FORMAT_ZERO = "yyyyMMdd";
	/**
	 * yyyy-MM-dd
	 */
	public static String FORMAT_ONE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd hh:mm:ss
	 */
	public static String FORMAT_TWO = "yyyy-MM-dd hh:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String FORMAT_THREE = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmss
	 */
	public static String FORMAT_FOUR = "yyyyMMddHHmmss";
	/**
	 * yyyyMMddHHmm
	 */
	public static String FORMAT_FIVE = "yyyyMMddHHmm";
	/**
	 * yyyy-MM
	 */
	public static String FORMAT_SIX = "yyyy-MM";
	/**
	 * HHmm
	 */
	public static String FORMAT_SEVEN = "HHmm";
	/**
	 * yyyy
	 */
	public static String FORMAT_EIGHT = "yyyy";
	/**
	 * yyyy-MM-dd HH:mm:ss.SS
	 */
	public static String FORMAT_NINE = "yyyy-MM-dd HH:mm:ss.SS";
	/**
	 * yyyy年MM月dd日
	 */
	public static String FORMAT_CHN = "yyyy年MM月dd日";
	/**
	 * yyyyMM
	 */
	public static String FORMAT_MON = "yyyyMM";
	/**
	 * yyyy年MM月dd日 HH时mm分ss秒
	 */
	public static String FORMAT_CHN_TWO = "yyyy年MM月dd日 HH时mm分ss秒";
//	/**
//	 * "yyyyMMdd"
//	 */
//	public static SimpleDateFormat DATE_FORMAT_ZERO = new SimpleDateFormat(FORMAT_ZERO);
//	/**
//	 * yyyy-MM-dd
//	 */
//	public static SimpleDateFormat DATE_FORMAT_ONE = new SimpleDateFormat(FORMAT_ONE);
//	/**
//	 * yyyy-MM-dd hh:mm:ss
//	 */
//	public static SimpleDateFormat DATE_FORMAT_TWO = new SimpleDateFormat(FORMAT_TWO);
//	/**
//	 * yyyy-MM-dd HH:mm:ss
//	 */
//	public static SimpleDateFormat DATE_FORMAT_THREE = new SimpleDateFormat(FORMAT_THREE);
//	/**
//	 * yyyyMMddHHmmss
//	 */
//	public static SimpleDateFormat DATE_FORMAT_FOUR = new SimpleDateFormat(FORMAT_FOUR);
//	/**
//	 * yyyyMMddHHmm
//	 */
//	public static SimpleDateFormat DATE_FORMAT_FIVE = new SimpleDateFormat(FORMAT_FIVE);
//	/**
//	 * yyyy-MM
//	 */
//	public static SimpleDateFormat DATE_FORMAT_SIX = new SimpleDateFormat(FORMAT_SIX);
//	/**
//	 * HHmm
//	 */
//	public static SimpleDateFormat DATE_FORMAT_SEVEN = new SimpleDateFormat(FORMAT_SEVEN);
//	/**
//	 * yyyy
//	 */
//	public static SimpleDateFormat DATE_FORMAT_EIGHT = new SimpleDateFormat(FORMAT_EIGHT);
//	/**
//	 * yyyy-MM-dd HH:mm:ss.SS
//	 */
//	public static SimpleDateFormat DATE_FORMAT_NINE = new SimpleDateFormat(FORMAT_NINE);

	/**
	 * 日期转换 为字符串 “年月日”
	 * @param value 日期
	 * @return 年月日
	 */
	

	public static String formatDateObject0(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_ZERO).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 日期转换 为字符串 “年-月-日”
	 * @param value 日期
	 * @return 年-月-日
	 */
	

	public static String formatDateObject1(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_ONE).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 日期转换 为字符串 “年-月”
	 * 
	 * @param value 日期
	 * @return 年-月
	 */
	

	public static String formatDateObject6(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_SIX).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 日期转换 为字符串 “年-月-日 时:分:秒” 12小时制
	 * 
	 * @param value 日期
	 * @return 年-月-日 时:分:秒，12小时制
	 */
	

	public static String formatDateObject2(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_TWO).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 日期转换字符串 为 “年-月-日 时:分:秒” 24小时制
	 * 
	 * @param value 日期
	 * @return 年-月-日 时:分:秒，24小时制
	 */
	

	public static String formatDateObject3(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_THREE).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 日期转换 为字符串 “HHmm” 时分
	 * 
	 * @param value 日期
	 * @return 时分
	 */
	

	public static String formatDateObject7(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_SEVEN).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 日期转换字符串 为 “年月日时分秒” 24小时制
	 * 
	 * @param value  日期
	 * @return 年月日时分秒，24小时制
	 */
	

	public static String formatDateObject5(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_FIVE).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 日期转换字符串 为 "yyyy"
	 * 
	 * @param value 日期
	 * @return 年
	 */
	

	public static String formatDateObject8(Object value) {
		try {
			if (value instanceof Date) {
				return new SimpleDateFormat(FORMAT_EIGHT).format(value);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 日期转换 为字符串 “yyyy年MM月dd日”
	 * 
	 * @param value 日期
	 * @return yyyy年MM月dd日
	 */
	

	public static String formatDateObjectChn(Object value) {
		try {
			if (value instanceof Date) {
				return format((Date) value, FORMAT_CHN);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 日期转换 为字符串 “yyyy年MM月dd日 HH时mm分ss秒”
	 * 
	 * @param value 日期
	 * @return yyyy年MM月dd日 HH时mm分ss秒
	 */
	

	public static String formatDateObjectChnTwo(Object value) {
		try {
			if (value instanceof Date) {
				return format((Date) value, FORMAT_CHN_TWO);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}


	/**
	 * 日期转换 为字符串 “yyyyMM”
	 * 
	 * @param value 日期
	 * @return yyyyMM
	 */
	

	public static String formatDateObjectMon(Object value) {
		try {
			if (value instanceof Date) {
				return format((Date) value, FORMAT_MON);
			}
			return value == null ? "" : value.toString();

		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 将日期[value]转化为指定格式[format]的字符串
	 * 
	 * @param value 日期
	 * @param format 指定格式
	 * @return 日期字符串
	 */
	

	public static String format(Date value, String format) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		LocalDateTime time = changeLocalDateTime(value);
		return df.format(time);

	}
	
	/**
	 * 将时间戳格式化为自定义的日期格式
	 * @param millitime 时间戳
	 * @param formatString 自定义的格式
	 * @return 格式后的日期字符串
	 */
	

	public static String formatDate(long millitime, String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		Date date = new Date(millitime);
		return sdf.format(date);
	}

	/**
	 * 将时间戳格式化为自定义的日期格式（可设置时区）
	 * @param millitime 时间戳
	 * @param formatString 时间戳
	 * @param tz 时区
	 * @return 格式后的日期字符串
	 */
	

	public static String formatDate(long millitime, String formatString, TimeZone tz) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		sdf.setTimeZone(tz);
		Date d = new Date(millitime);
		return sdf.format(d);
	}
	
	/**
	 * 字符串"yyyy-MM-dd"格式化为日期
	 * 
	 * @param value “年-月-日”
	 * @return 日期
	 */
	

	public static Date parseDate1(String value) {
		try {
			return new SimpleDateFormat(FORMAT_ONE).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyy-MM-dd hh:mm:ss"格式化为日期
	 * 
	 * @param value
	 *            “年-月-日 时:分:秒” 12小时制
	 * @return 日期
	 */
	

	public static Date parseDate2(String value) {
		try {
			return new SimpleDateFormat(FORMAT_TWO).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyy-MM-dd HH:mm:ss"格式化为日期
	 * 
	 * @param value
	 *            “年-月-日 时:分:秒” 24小时制
	 * @return 日期
	 */
	

	public static Date parseDate3(String value) {
		try {
			return new SimpleDateFormat(FORMAT_THREE).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyyMMddHHmmss"格式化为日期
	 * 
	 * @param value
	 *            “20141010153234” 24小时制
	 * @return 日期
	 */
	

	public static Date parseDate4(String value) {
		try {
			return new SimpleDateFormat(FORMAT_FOUR).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyyMMddHHmm"格式化为日期
	 * 
	 * @param value
	 *            yyyymmddHHmi “201410101532” 24小时制
	 * @return yyyymmddHHmi，24小时制
	 */
	

	public static Date parseDate5(String value) {
		try {
			return new SimpleDateFormat(FORMAT_FIVE).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyy-MM"格式化为日期
	 * 
	 * @param value
	 *            “年-月”
	 * @return 日期
	 */
	

	public static Date parseDate6(String value) {
		try {
			return new SimpleDateFormat(FORMAT_SIX).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"HHmm"格式化为日期
	 * 
	 * @param value
	 *            “HHmm”
	 * @return 日期
	 */
	

	public static Date parseDate7(String value) {
		try {
			return new SimpleDateFormat(FORMAT_SEVEN).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyy"格式化为日期
	 * 
	 * @param value
	 *            “yyyy”
	 * @return 日期
	 */
	

	public static Date parseDate8(String value) {
		try {
			return new SimpleDateFormat(FORMAT_EIGHT).parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字符串"yyyyMMdd"格式化为日期
	 * 
	 * @param value 年月日
	 * @return 日期
	 */
	

	public static Date parseDate0(String value) {
		try {
			return parse(value, FORMAT_ZERO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串"yyyy年MM月dd日"格式化为日期
	 * 
	 * @param value “yyyy年MM月dd日”
	 * @return 日期
	 */
	

	public static Date parseDateChn(String value) {
		try {
			return parse(value, FORMAT_CHN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字符串"yyyy年MM月dd日 HH时mm分ss秒"格式化为日期
	 * 
	 * @param value“yyyy年MM月dd日 HH时mm分ss秒”
	 * @return 日期
	 */
	

	public static Date parseDateChnTwo(String value) {
		try {
			return parse(value, FORMAT_CHN_TWO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 字符串"yyyyMM"格式化为日期
	 * 
	 * @param value
	 *            “yyyyMM”
	 * @return 日期
	 */
	

	public static Date parseDateMon(String value) {
		try {
			return parse(value, FORMAT_MON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 将符合格式[format]的日期字符串[value]转化为日期对象
	 * 
	 * @param value 日期字符串
	 * @param format 指定格式
	 * @return 日期
	 */
	

	public static Date parse(String value, String format) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		LocalDateTime time = LocalDateTime.parse(value, df);
		return changeDate(time);

	}

	/**
	 * 获取所有的日期格式化
	 * 
	 * @return 日期格式
	 */
	

	public static String[] getDateFormats() {
		String[] formats = { FORMAT_ONE, FORMAT_TWO, FORMAT_THREE, FORMAT_FOUR, FORMAT_FIVE, 
				FORMAT_SIX, FORMAT_SEVEN, FORMAT_EIGHT, FORMAT_NINE, FORMAT_CHN, FORMAT_MON, FORMAT_CHN_TWO};
		return formats;
	}
	
	/**
	 * 给定日期是星期几
	 * @param c 日期
	 * @return 星期几
	 */
	

	public static String getWeekDay(Calendar c) {
		int weekDay = c.get(7);
		if (weekDay > 1) {
			return (new StringBuilder()).append("").append(weekDay - 1).toString();
		} else {
			return "7";
		}
	}

	/**
	 * 转化格式
	 * @param localDateTime LocalDateTime
	 * @return Date
	 */
	

	public static Date changeDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	/**
	 * 转化格式
	 * @param date Date
	 * @return LocalDateTime
	 */
	

	public static LocalDateTime changeLocalDateTime(Date date) {

		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}

	/**
	 * 获取前num月的时间
	 * @param num 几月前
	 * @return 日期
	 */
	

	public static Date preMonths(int num) {
		LocalDateTime ldt = LocalDateTime.now();
		return changeDate(ldt.minusMonths(num));
	}

	/**
	 * 获取前num年时间
	 * @param num 几年前
	 * @return 日期
	 */
	

	public static Date preYears(int num) {
		LocalDateTime ldt = LocalDateTime.now();
		return changeDate(ldt.minusYears(num));
	}

	/**
	 * 日期加时操作
	 * @param date 日期
	 * @param hour 增加的小时
	 * @return 加时日期
	 */
	

	public static Date addHourToDate(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);

		return calendar.getTime();
	}

	

	/**
	 * 根据日期生成指定个数的随机数
	 * @param length 随机数个数
	 * @return 随机数
	 */
	

	public static String generateRamdomString(int length) {
		Date d = new Date();
		long lseed = d.getTime();
		java.util.Random rx = new java.util.Random(lseed);
		java.util.Random r = new java.util.Random(rx.nextLong()); // 设置随机种子
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length; i++) {
			str.append(r.nextInt(9)); // 生成随机数字
		}
		return str.toString();
	}

	/**
	 * 毫秒按天小时分钟秒的格式转换
	 * @param milions 时间戳
	 * @return 转换后的时间
	 */
	

	public static String millitimeToRead(long milions) {

		if (milions == 0L) {
			return "";
		}

		long s = milions % 1000; // 毫秒
		long ss = (milions - s) / 1000;
		long s1 = ss % 60; // 秒
		long s2 = (ss - s1) / 60;
		long s3 = s2 % 60; // 分
		long s4 = (s2 - s3) / 60;
		long s5 = s4 % 24; // 小时
		long s6 = (s4 - s5) / 24;

		System.out.println(s6);

		return (s6 <= 0 ? "" : String.valueOf(s6) + "天") + (s5 <= 0 ? "" : String.valueOf(s5) + "小时")
				+ (s3 <= 0 ? "" : String.valueOf(s3) + "分钟")
				+ (s1 <= 0 ? "" : String.valueOf(s1) + "." + String.valueOf(s) + "秒");
	}

	/**
	 * 获取当前时间
	 * @return 当前时间（yyyy-MM-dd HH:mm:ss.SS）
	 */
	

    public static String getCurrentTime(){
        Date currentTime = new Date();
		String dateString = new SimpleDateFormat(FORMAT_NINE).format(currentTime);
		return dateString;
    }
    
	/**
	 * 得到上月的第一天或得到下月的第一天
	 * @param y 年，-1：减一年，0：本年，1：加一年
	 * @param month 月数，-1：上一月，0：本月，1：下月
	 * @param date 处理的日期
	 * @return 第一天
	 * @throws ParseException
	 */
	

	public static Date getMonthFirstDay(int y, int month, Date date)throws ParseException{
		Calendar c = Calendar.getInstance();
		
		c.setTime(date);
		c.add(Calendar.YEAR, y);
		c.add(Calendar.MONTH, month);
		
		int minDay=c.getActualMinimum(Calendar.DAY_OF_MONTH);
		
		c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), minDay);
		
		return c.getTime();
	}
		
	/**
	 * <p>得到上月/下月的最后一天</p>
	 * @param y 年，-1：减一年，0：本年，1：加一年
	 * @param month 月数，-1：上一月，0：本月，1：下月
	 * @param date 处理的日期
	 * @return 处理后的日期
	 * @throws ParseException
	 */
	

	public static Date getMonthLastDay(int y,int month,Date date)throws ParseException{
		Calendar c = Calendar.getInstance();
		
		c.setTime(date);
		c.add(Calendar.YEAR, y);
		c.add(Calendar.MONTH, month);
		
		int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), maxDay);//设置月最后一天的最大时间

		return c.getTime();
	}
	
	/**
	 * GMT时间转UTC时间
	 * @param gmtTime GMT时间
	 * @return UTC时间
	 */
	

	public static String gmtToUtc(Date gmtTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		String utcTime = sdf.format(gmtTime);
		return utcTime;
		
	}

	/**
	 * 得到二个日期间的间隔天数
	 * @param sj1 日期1（yyyy-MM-dd）
	 * @param sj2 日期2（yyyy-MM-dd）
	 * @return 间隔天数
	 */
	

	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date date = myFormatter.parse(sj1);
			Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}
	
	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	/**
	 * 时间前推或后推
	 * @param sj1 时间（yyyy-MM-dd HH:mm:ss）
	 * @param jj 分钟数
	 * @return 运算后的时间
	 */
	

	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	/**
	 * 一个时间延后或前移几天的时间
	 * @param nowdate 时间（yyyy-MM-dd）
	 * @param delay 前移或后延的天数
	 * @return 运算后的时间
	 */
	

	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = parseDate3(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 判断是否闰年
	 * @param ddate 日期
	 * @return  闰年返回true,否则返回false
	 */
	

	public static boolean isLeapYear(String ddate) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = parseDate3(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0) {
			return true;
		}else if ((year % 4) == 0) {
			if ((year % 100) == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 获取一个月的最后一天
	 * @param dat 日期（格式：yyyy-MM-dd）
	 * @return 最后一天的日期值（28，29,30或31）
	 */
	

	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
				|| mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}
	
	/**
	 * 判断二个时间是否在同一个周
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 同一周返回true,否则返回false
	 */
	

	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR)) {
				return true;
			}
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR)) {
				return true;
			}
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 * @return 第几周
	 */
	

	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1) {
			week = "0" + week;
		}
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}
	
}

package com.cockpit.commons.utils;


import com.mujun.mng.commons.exception.BaseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*************************************************
为适应不同显示风格的日期格式，
DateUtil 类对日期类进行不同类型的处理
*************************************************/
public class DateUtil {
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATE_LONG_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat DATE_SHORT_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat DATE_SHORT_FORMAT2 = new SimpleDateFormat("yyMMdd");
	public static final SimpleDateFormat DATE_MINUTE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
	public static final SimpleDateFormat DATE_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	/**
	 * SimpleDateFormat线程不安全，需要用线程安全方法
	 */
	private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		}
	};

	/**
	 * 
	 * 函数名称：nowDate <br>
	 * 函数功能：获取当前日期 格式为:2008年12月22日
	 * @return String
	 */
	public static String nowDate() {
		
		DateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(System.currentTimeMillis());
		return dFormat.format(cd.getTime());
	}

	/**
	 * 
	 * 函数名称：now <br>
	 * 函数功能：获取当前日期 格式为:2008年12月22日 8时52分30秒
	 * @return String
	 */
	public static String now() {
		DateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日   HH时mm分ss秒");
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(System.currentTimeMillis());
		return dFormat.format(cd.getTime());
	}

	/**
	 * 
	 * 函数名称：nowDateNDay <br>
	 * 函数功能：获取当前日期 格式为：2008年12月22日  星期一
	 * @return String
	 */
	public static String nowDateNDay() {
		DateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日  E");
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(System.currentTimeMillis());
		return dFormat.format(cd.getTime());
	}

	/**
	 * 
	 * 函数名称：dateToString<br>
	 * 函数功能：根据日期参数 输出日期格式为:2008年12月22日<br>
	 * 例：DateUtil.dateToString(new Date()) 
	 * 输出：2008年12月22日
	 * @param date - Date
	 * @return String
	 */
	public static String dateToString(Date date) {
		DateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return dFormat.format(cd.getTime());
	}
	/**
	 * 
	 * 函数名称：regularize<br>
	 * 函数功能：将日期格式为2008-1-2 替换为20080102<br>
	 * 例：DateUtil.regularize("2008-1-2") 输出：20080102
	 * @param input - String
	 * @return String
	 */
	public static String regularize(String input) {
		String out = "";
		String[] arr = input.split("-");
		if (arr[1].length() < 2) {
			arr[1] = "0" + arr[1];
		}
		if (arr[2].length() < 2) {
			arr[2] = "0" + arr[2];
		}
		out = arr[0] + arr[1] + arr[2];
		return out;
	}

	/**
	 * 
	 * 函数名称：yesterday <br>
	 * 函数功能：获取昨天日期 格式为：2008-12-21
	 * @return String
	 */
	public static String yesterday() {
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		return dFormat.format(cd.getTime());
	}

	/**
	 * 
	 * 函数名称：tomorrow <br>
	 * 函数功能：获取明天日期 格式为：2008-12-23
	 * @return String
	 */
	public static String tomorrow() {
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, 1);
		return dFormat.format(cd.getTime());
	}

	/**
	 * 
	 * 函数名称：dateToDateCN <br>
	 * 函数功能：将日期格式为2008-12-1 转化为 2008年12月1日 <br>
	 * 例：DateUtil.dateToDateCN("2008-12-1") 输出：2008年12月1日
	 * @param in - String
	 * @return String
	 */
	public static String dateToDateCN(String in) {
		String[] tmp = in.split("-");
		return tmp[0] + "年" + tmp[1] + "月" + tmp[2] + "日";
	}

	/**
	 * 
	 * 函数名称：dateToDateEN <br>
	 * 函数功能：将日期格式为2008年12月1日 转化为 2008-12-1<br>
	 * 例：DateUtil.dateToDateCN("2008年12月1日") 输出：2008-12-1
	 * @param in - String
	 * @return String
	 */
	public static String dateToDateEN(String in) {
		return in.replace("年", "-").replace("月", "-").replace("日", "");
	}

	/**
	 * 程序中主要的日期分隔符为"-"和"/"，且日期序列为“年/月/日”型，其内容缺一不可 例如:09/02/02或2009-02-02
	 */
	public static final String DATE_SEPARATOR = "-/";

	/** 作日期分析之用 */
	static StringTokenizer sToken;

	/**
	 * 
	 * 函数名称：GregorianCalendar<br>
	 * 函数功能：将字符串格式的日期转换为Calender<br>
	 * 例：parse2Cal("2008-12-1") 
	 * @param pDateStr
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar parse2Cal(String pDateStr) {
		sToken = new StringTokenizer(pDateStr, DATE_SEPARATOR);
		int vYear = Integer.parseInt(sToken.nextToken());
		// GregorianCalendar的月份是从0开始算起的
		int vMonth = Integer.parseInt(sToken.nextToken()) - 1;
		int vDayOfMonth = Integer.parseInt(sToken.nextToken());
		return new GregorianCalendar(vYear, vMonth, vDayOfMonth);
	}

	/**
	 * 
	 * 函数名称：monthsBetween<br>
	 * 函数功能：给定两个时间相差的月数,String版<br>
	 * 例：DateUtil.monthsBetween("2008-12-1","2009-1-1")输出：1
	 * @param pFormerStr
	 * @param pLatterStr
	 * @return int
	 */
	public static int daysBetween(String pFormerStr, String pLatterStr) {
		GregorianCalendar vFormer = DateUtil.parse2Cal(pFormerStr);
		GregorianCalendar vLatter = DateUtil.parse2Cal(pLatterStr);
		return monthsBetween(vFormer, vLatter);
	}

	/**
	 * 
	 * 函数名称：monthsBetween<br>
	 * 函数功能：给定两个时间相差的月数<br>
	 * 例：DateUtil.monthsBetween(parse2Cal("2008-12-1"),parse2Cal("2009-1-1"))输出：1
	 * @param pFormer GregorianCalendar
	 * @param pLatter GregorianCalendar
	 * @return int
	 */
	public static int monthsBetween(GregorianCalendar pFormer,
			GregorianCalendar pLatter) {
		GregorianCalendar vFormer = pFormer, vLatter = pLatter;
		boolean vPositive = true;
		if (pFormer.before(pLatter)) {
			vFormer = pFormer;
			vLatter = pLatter;
		} else {
			vFormer = pLatter;
			vLatter = pFormer;
			vPositive = false;
		}

		int vCounter = 0;
		while (vFormer.get(vFormer.YEAR) != vLatter.get(vLatter.YEAR)
				|| vFormer.get(vFormer.MONTH) != vLatter.get(vLatter.MONTH) ||  vFormer.get(vFormer.DATE) != vLatter.get(vLatter.DATE)) {
			vFormer.add(Calendar.DAY_OF_MONTH, 1);
			vCounter++;
		}
		if (vPositive)
			return vCounter;
		else
			return -vCounter;
	}

	/**
	 * 
	 * 函数名称：getMonth<br>
	 * 函数功能：返回给定日期的月份<br>
	 * 例：DateUtil.getMonth("2008-12-2")输出：12
	 * @param pFormattedDate 格式为：2008-12-2
	 * @return int 结果：12
	 */
	public static int getMonth(String pFormattedDate) {
		StringTokenizer vSt = new StringTokenizer(pFormattedDate, "-");
		vSt.nextToken();//跳过年份
		int val = Integer.parseInt(vSt.nextToken());
		return val;
	}

	/**
	 * 
	 * 函数名称：getYear<br>
	 * 函数功能：返回给定日期的年份<br>
	 * 例：DateUtil.getYear("2008-12-2")输出：2008
	 * @param pFormattedDate 格式为：2008-12-2
	 * @return int 结果：2008
	 */
	public static int getYear(String pFormattedDate) {
		StringTokenizer vSt = new StringTokenizer(pFormattedDate, "-");
		int val = Integer.parseInt(vSt.nextToken());
		return val;
	}

	/**
	 * 
	 * 函数名称：dayOfWeek<br>
	 * 函数功能：返回给定日期的周数<br>
	 * 例：DateUtil.dayOfWeek("2008-12-2")输出：3
	 * @param pFormerStr 字符型 格式为：2008-12-2
	 * @return int 结果：3
	 */
	public static int dayOfWeek(String pFormerStr) {
		GregorianCalendar vTodayCal = parse2Cal(pFormerStr);
		return vTodayCal.get(vTodayCal.DAY_OF_WEEK);
	}

	/**
	 * 
	 * 函数名称：dayOfWeek<br>
	 * 函数功能：返回给定日期的周数<br>
	 * 例：DateUtil.dayOfWeek(new Date("2008-12-2"))输出：3
	 * @param date -日期型
	 * @return int 
	 */
	public static int dayOfWeek(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(gc.DAY_OF_WEEK);
	}

	/**
	 * 
	 * 函数名称：stringToDate<br>
	 * 函数功能：字符串转日期<br>
	 * 简要说明：注意：这里的pattern参数要与date参数格式相一致，如举例<br>
	 * 显示格式如：yyyy-MM-dd hh:mm:ss,
	 *           MM-dd-yyyy hh:mm:ss,
	 *           yy-MM-dd HH:mm,
	 *           yyyy-MM-dd,
	 *           yyyy/MM/dd,
	 *           MM/dd/yyyy,
	 *           yyyyMMdd等<br>
	 * 例：正确：DateUtil.stringToDate("2008-9-2","yy-MM-dd");<br>
	 *         DateUtil.stringToDate("2008-9-2 12:30","yy-MM-dd");<br>
	 *    错误：DateUtil.stringToDate("2008-9-2 12:30","yyyy/MM/dd");<br>
	 *         DateUtil.stringToDate("2008/9/2 12:30","yyyyMMdd HH:mm");
	 * @param date - String
	 * @param pattern
	 * @return Date
	 */
	public final static Date stringToDate(String date, String pattern)
			throws ParseException {
		if (date == null || pattern == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}

	/**
	 * 
	 * 函数名称：convDateToString<br>
	 * 函数功能：日期转字符串<br>
	 * 显示格式如：yyyy-MM-dd hh:mm:ss,
	 *           MM-dd-yyyy hh:mm:ss,
	 *           yy-MM-dd HH:mm,
	 *           yyyy-MM-dd,
	 *           yyyy/MM/dd,
	 *           MM/dd/yyyy,
	 *           yyyyMMdd等<br>
	 * 例：DateUtil.convDateToString(new Date(),"yy-MM-dd HH:mm")<br>
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public final static String convDateToString(Date date, String pattern) {
		if (date == null || pattern == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 
	 * 函数名称：calcMonday<br>
	 * 函数功能：根据日期计算这个星期的星期一是多少，并且星期一以00:00:00开头 <br>
	 * 例：DateUtil.calcMonday("2008-12-2")输出：2008-12-01 00:00:00<br>
	 * @param queryDate
	 * @return String
	 */
	public static String calcMonday(String queryDate) {
		String result = null;
		if (queryDate != null) {
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				gc.setTime(df.parse(queryDate));
				gc.add(5, -1);
				while (gc.get(7) != 1) {
					gc.add(5, -1);
				}
				gc.add(5, 1);
				result = df.format(gc.getTime()) + " 00:00:00";
			} catch (ParseException e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * 函数名称：calcSunday<br>
	 * 函数功能：根据日期计算这个星期的星期日是多少，并且星期日以23:59:59开头<br>
	 * 例：DateUtil.calcSunday("2008-12-2")输出：2008-12-07 23:59:59
	 * @param queryDate
	 * @return String
	 */
	public static String calcSunday(String queryDate) {
		String result = null;
		if (queryDate != null) {
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				gc.setTime(df.parse(queryDate));
				gc.add(5, -1);
				while (gc.get(7) != 7) {
					gc.add(5, 1);
				}
				gc.add(5, 1);
				result = df.format(gc.getTime()) + " 23:59:59";
			} catch (ParseException e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * 函数名称：calcBeginMonth<br>
	 * 函数功能：根据日期计算这个月的第一天<br>
	 * 例：DateUtil.calcBeginMonth("2008-12-2")输出：Mon Dec 01 00:00:00 GMT 2008
	 * @param queryDate
	 * @return Date
	 */
	public static Date calcBeginMonth(String queryDate) {
		Date result = null;
		if (queryDate != null) {
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				gc.setTime(df.parse(queryDate));
				int i = gc.get(Calendar.MONTH);
				// 11表示第12月
				while (gc.get(Calendar.MONTH) != (i == 0 ? 11 : i - 1)) {
					gc.add(5, -1);
				}
				gc.add(5, 1);
				result = gc.getTime();
			} catch (ParseException e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * 函数名称：calcEndMonth<br>
	 * 函数功能：根据日期计算这个月的最后一天<br>
	 * 例：DateUtil.calcEndMonth("2008-12-2")输出：Wed Dec 31 00:00:00 GMT 2008
	 * @param queryDate
	 * @return Date
	 */
	public static Date calcEndMonth(String queryDate) {
		Date result = null;
		if (queryDate != null) {
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				gc.setTime(df.parse(queryDate));
				int i = gc.get(Calendar.MONTH);
				// 11表示第12月
				while (gc.get(Calendar.MONTH) != (i == 11 ? 0 : i + 1)) {
					gc.add(5, 1);
				}
				gc.add(5, -1);
				result = gc.getTime();
			} catch (ParseException e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * 函数名称：getWeekString<br>
	 * 函数功能：根据数值给出中文的星期表示<br>
	 * 例：DateUtil.getWeekString(1)输出：星期日
	 * @param week
	 * @return String
	 */
	private static String getWeekString(int week) {
		String weeks[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		return weeks[week - 1];
	}

	/**
	 * 
	 * 函数名称：isDateBefore<br>
	 * 函数功能：判断时间date1是否在时间date2之前<br>
	 * 使用说明：时间格式 2005-4-21 16:16:34<br>
	 * 例:DateUtil.isDateBefore("2008-12-2 16:16:34","2008-12-1 16:16:34")输出：false
	 * 
	 * @param date1 字符型
	 * @param date2 字符型
	 * @return boolean
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			return stringToDate(date1, "yyyy-MM-dd HH:mm:ss").before(
					stringToDate(date2, "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 
	 * 函数名称：isDateBefore<br>
	 * 函数功能：判断时间date1是否在时间date2之前<br>
	 * 例:DateUtil.isDateBefore(DateUtil.getDate(),DateUtil.getDate())输出：false
	 * @param date1 日期型
	 * @param date2 日期型
	 * @return boolean 是否相等
	 */
	public static boolean isDateBefore(Date date1, Date date2) {
		try {
			return date1.before(date2);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * 函数名称：getDateBefore<br>
	 * 函数功能：得到几天后的时间<br>
	 * 例:DateUtil.getDateBefore(DateUtil.getDate(),2)输出：Sat Feb 21 07:14:57 GMT 2009<br>
	 * DateUtil.getDate()为Thu Feb 19 07:14:57 GMT 2009
	 * @param d - 指定日期
	 * @param day - 指定天数
	 * @return Date 指定日期几天后的时间
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 
	 * 函数名称：getDateBeforeHour<br>
	 * 函数功能：指定日期小时后的时间<br>
	 * 例:DateUtil.getDateBeforeHour(DateUtil.getDate(),2)输出：Thu Feb 19 09:17:36 GMT 2009<br>
	 * DateUtil.getDate()为Thu Feb 19 07:17:36 GMT 2009
	 * @param d
	 * @param hour
	 * @return
	 * Date
	 */
	public static Date getDateBeforeHour(Date d, int hour) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hour);
		return now.getTime();
	}

	/**
	 *
	 * 函数名称：getDateBeforeMin<br>
	 * 函数功能：指定日期分钟后的时间<br>
	 * 例:DateUtil.getDateBeforeHour(DateUtil.getDate(),2)输出：Thu Feb 19 09:17:36 GMT 2009<br>
	 * DateUtil.getDate()为Thu Feb 19 09:19:36 GMT 2009<br>
	 *   DateUtil.getDateBeforeHour(DateUtil.getDate(),-2)输出：Thu Feb 19 09:15:36 GMT 2009<br>
	 * 	 DateUtil.getDate()为Thu Feb 19 09:17:36 GMT 2009<br>
	 * @param d 当前时间
	 * @param minute
	 * @return minute正数-获取多久之后的时间、负数获取多久之前的时间
	 *
	 */
	public static Date getDateBeforeMin(Date d, int minute) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.add(Calendar.MINUTE, minute);
		return now.getTime();
	}

	/**
	 * 函数名称：getNow<br>
	 * 函数功能：获取当前时间 -年<br>
	 * 简要说明：
	 * @return String 
	 *
	 */
	public static String getYear() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "yyyy");
		return sDate;
	}
	
	/**
	 * 函数名称：getMon<br>
	 * 函数功能：获取当前时间 -月<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getMon() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "MM");
		return sDate;
	}
	/**
	 * 函数名称：getDaily<br>
	 * 函数功能：获取当前时间 -日<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getDaily() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "dd");
		return sDate;
	}
	
	/**
	 * 函数名称：getHour<br>
	 * 函数功能：获取当前时间 -小时<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getHour() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "HH");
		return sDate;
	}
	
	/**
	 * 函数名称：getMin<br>
	 * 函数功能：获取当前时间 -分<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getMin() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "mm");
		return sDate;
	}
	
	/**
	 * 函数名称：getSec<br>
	 * 函数功能：获取当前时间 -秒<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getSec() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "ss");
		return sDate;
	}
	
	/**
	 * 函数名称：getNow<br>
	 * 函数功能：获取当前日期 格式为:2008-12-22<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getNow() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "yyyy-MM-dd");
		return sDate;
	}
	
	/**
	 * 函数名称：getNowTime<br>
	 * 函数功能：获取当前日期 格式为:2008-12-22 00:00:00<br>
	 * 简要说明：
	 * @return String
	 *
	 */
	public static String getNowTime() {
		Date date = Calendar.getInstance().getTime();
		String sDate = convDateToString(date, "yyyy-MM-dd HH:mm:ss");
		return sDate;
	}
	
	/**
	 * 函数名称：getDate<br>
	 * 函数功能：获取当前日期 格式为:Thu Feb 19 02:01:37 GMT 2009<br>
	 * 简要说明：
	 * @return Date
	 *
	 */
	public static Date getDate() {
		Date date = Calendar.getInstance().getTime();
		return date;
	}

	/**
	 * 格式化日期
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDate(Date date) {
		return DATE_TIME_FORMAT.format(date);
	}

	/**
	 * 格式化日期
	 * @return yyyy-MM-dd
	 */
	public static String formatDateDay(Date date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 日期格式化为UTC时间
	 * @param date 入参日期
	 * @return 返回格式化字符串
	 */
	public static String formatUTCDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return THREAD_LOCAL.get().format(calendar.getTime());
	}

	/**
	 * 根据日期串解释出日期对象
	 * @param dateStr yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException 
	 */
	public static Date parseDate(String dateStr) throws Exception {
		try {
			return DATE_TIME_FORMAT.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 根据日期串解释出日期对象
	 * @param dateStr yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateDay(String dateStr) throws Exception {
		try {
			return DATE_FORMAT.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 格式化日期
	 */
	public static String formatDate(Date date, SimpleDateFormat dateFormat) {
		return dateFormat.format(date);
	}

	/**
	 * 根据日期串解释出日期对象
	 * @return
	 * @throws ParseException 
	 */
	public static Date parseDate(String dateStr, SimpleDateFormat dateFormat) {
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new  BaseException("根据日期串解释出日期对象出现异常"+e);
		}
	}
	
	/**
	 * 得到长格式系统当前时间
	 * 
	 * @return 格式化好的系统时间
	 */
	public static String getCurMinuteDate()
	{
		return DATE_MINUTE_FORMAT.format(new Date());
	}
	
	/**
	 * 得到长格式系统当前时间
	 * 
	 * @return 格式化好的系统时间
	 */
	public static String getCurLongDate()
	{
		return DATE_LONG_FORMAT.format(new Date());
	}
	
	/**
	 * 得到短格式时间
	 * 
	 * @return 格式化好的时间
	 */
	public static String getCurShortDate()
	{
		return DATE_SHORT_FORMAT.format(new Date());
	}

	/**
	 * 得到段格式时间，格式yyMMdd
	 * @return
	 */
	public static String getCurShortDate2()
	{
		return DATE_SHORT_FORMAT2.format(new Date());
	}

	/**
	 * 得到长格式时间
	 *
	 * @return 格式化好的时间
	 */
	public static String getCurLongDate(Date date)
	{
		return DATE_LONG_FORMAT.format(date);
	}
	
	static int counter = 0;
	
	public static void main(String[] args) throws ParseException {
//		System.out.println(localToUTC(new Date()));
//		System.out.println(localToUTCString(new Date()));
//		System.out.println(utcToLocalString(new Date()));
//		System.out.println(utcToLocal(new Date()));
//		System.out.println(utcToLocal(DATE_TIME_FORMAT.format(new Date())));
//		System.out.println(DATE_TIME_FORMAT.format(getUTCTime()));
		DateUtil dateUtil = new DateUtil();
		System.out.println(dateUtil.daysBetween("2020-01-01","2020-02-01"));
	}
	
	private static int stringNumbers(String str, String separator) {
		if (str.indexOf(separator) == -1) {
			return 0;
		} else if (str.indexOf(separator) != -1) {
			counter++;
			stringNumbers(str.substring(str.indexOf(separator) + separator.length()), separator);
			return counter;
		}
		return 0;
	}

	public static Date getLocalTime() {
		return new Date();
	}

	public static Date getUTCTime() {
		return localToUTC(new Date());
	}

	/**
	 * local时间转换成UTC时间
	 * @param localTime
	 * @return UTC时间
	 */
	public static Date localToUTC(String localTime) {
		Date date;
		try {
			date = parseDate(localTime);
		} catch (Exception e) {
			throw new BaseException("时间解析异常", e);
		}
		return localToUTC(date);
	}

	/**
	 * local时间转换成UTC时间
	 * @param localTime
	 * @return UTC时间
	 */
	public static Date localToUTC(Date localTime) {

		long localTimeInMillis=localTime.getTime();
		/** long时间转换成Calendar */
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(localTimeInMillis);
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		/** 从本地时间里扣除这些差量，即可以取得UTC时间*/
		calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		/** 取得的时间就是UTC标准时间 */
		Date utcDate=new Date(calendar.getTimeInMillis());
		return utcDate;
	}

	/**
	 * local时间转换成UTC时间
	 * @param localTime
	 * @return UTC时间
	 */
	public static String localToUTCString(Date localTime) {

		long localTimeInMillis=localTime.getTime();
		/** long时间转换成Calendar */
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(localTimeInMillis);
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		/** 从本地时间里扣除这些差量，即可以取得UTC时间*/
		calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		/** 取得的时间就是UTC标准时间 */
		Date utcDate=new Date(calendar.getTimeInMillis());
		return DATE_UTC_FORMAT.format(utcDate);
	}

	/**
	 * utc时间转成local时间
	 * @param utcTime
	 * @return
	 */
	public static Date utcToLocal(String utcTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date utcDate = null;
		try {
			utcDate = sdf.parse(utcTime);
		} catch (ParseException e) {
			throw new BaseException("时间解析异常", e);
		}
		sdf.setTimeZone(TimeZone.getDefault());
		Date locatlDate = null;
		String localTime = sdf.format(utcDate.getTime());
		try {
			locatlDate = sdf.parse(localTime);
		} catch (ParseException e) {
			throw new BaseException("时间解析异常", e);
		}
		return locatlDate;
	}

	/**
	 * utc时间转成local时间
	 * @param utcTime
	 * @return
	 */
	public static Date utcToLocal(Date utcTime){
		/** long时间转换成Calendar */
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(utcTime.getTime());
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		/** 从本地时间里加上这些差量，即可以取得本地时间*/
		calendar.add(Calendar.MILLISECOND, zoneOffset + dstOffset);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * utc时间转成local时间
	 * @param utcTime
	 * @return
	 */
	public static String utcToLocalString(Date utcTime){
		/** long时间转换成Calendar */
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(utcTime.getTime());
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		/** 从本地时间里加上这些差量，即可以取得本地时间*/
		calendar.add(Calendar.MILLISECOND, zoneOffset + dstOffset);
		Date d = new Date(calendar.getTimeInMillis());

		return DATE_TIME_FORMAT.format(d);
	}

}

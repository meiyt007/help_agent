package com.zfsoft.superwindow.util;

import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil extends PropertyEditorSupport {

	// 各种时间格式
	public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
	// 各种时间格式
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	// 各种时间格式
	public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
	public static final SimpleDateFormat date_sdf_wzs = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
	public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat yyyymmddhhmmsss = new SimpleDateFormat("yyyyMMddHHmmsss");
	public static final SimpleDateFormat hhmmsss = new SimpleDateFormat("HHmmsss");
	public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 以毫秒表示的时间
	private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
	private static final long HOUR_IN_MILLIS = 3600 * 1000;
	private static final long MINUTE_IN_MILLIS = 60 * 1000;
	private static final long SECOND_IN_MILLIS = 1000;

	// 指定模式的时间格式
	private static SimpleDateFormat getSDFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 当前日历，这里用中国时间表示
	 *
	 * @return 以当地时区表示的系统当前日历
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * 指定毫秒数表示的日历
	 *
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数表示的日历
	 */
	public static Calendar getCalendar(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(millis));
		return cal;
	}

	/**
	 * 当前日期
	 *
	 * @return 系统当前时间
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 指定毫秒数表示的日期
	 *
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数表示的日期
	 */
	public static Date getDate(long millis) {
		return new Date(millis);
	}

	/**
	 * 字符串转换时间戳
	 *
	 * @param str
	 * @return
	 */
	public static Timestamp str2Timestamp(String str) {
		Date date = str2Date(str, date_sdf);
		return new Timestamp(date.getTime());
	}

	/**
	 * 字符串转换日期
	 *
	 * @param str
	 * @return
	 */
	public static Date str2Date(String str) {
		Date date = str2Date(str, date_sdf);
		return date;
	}

	/**
	 * 字符串转换成日期
	 *
	 * @param str
	 * @param sdf
	 * @return
	 */
	public static Date str2Date(String str, SimpleDateFormat sdf) {
		if (null == str || "".equals(str)) {
			return null;
		}
		Date date = null;
		try {
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转换为字符串
	 *
	 * @param
	 *
	 * @param
	 *
	 * @return 字符串
	 */
	public static String date2Str(SimpleDateFormat date_sdf) {
		Date date = getDate();
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}

	/**
	 * 格式化时间
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateformat(String date, String format) {
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		Date _date = null;
		try {
			_date = sformat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sformat.format(_date);
	}

	/**
	 * 返回指定格式的系统当前日期时间
	 *
	 * @param
	 *            fomat 指定格式
	 * @return String yyyymmdd
	 */
	public static String getDateAndTime(String fomat) {
		SimpleDateFormat today;
		today = new SimpleDateFormat(fomat);
		return today.format(new Date());
	}

	/**
	 * 日期转换为字符串
	 *
	 * @param date
	 *            日期
	 * @param
	 *
	 * @return 字符串
	 */
	public static String date2Str(Date date, SimpleDateFormat date_sdf) {
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}

	public static String date2Str(Date date, String date_sdf) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(date_sdf);
		return sdf.format(date);
	}

	/**
	 * 日期转换为字符串
	 *
	 * @param
	 *
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String getNowDate(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 指定毫秒数的时间戳
	 *
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数的时间戳
	 */
	public static Timestamp getTimestamp(long millis) {
		return new Timestamp(millis);
	}

	/**
	 * 以字符形式表示的时间戳
	 *
	 * @param time
	 *            毫秒数
	 * @return 以字符形式表示的时间戳
	 */
	public static Timestamp getTimestamp(String time) {
		return new Timestamp(Long.parseLong(time));
	}

	/**
	 * 系统当前的时间戳
	 *
	 * @return 系统当前的时间戳
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 指定日期的时间戳
	 *
	 * @param date
	 *            指定日期
	 * @return 指定日期的时间戳
	 */
	public static Timestamp getTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 指定日历的时间戳
	 *
	 * @param cal
	 *            指定日历
	 * @return 指定日历的时间戳
	 */
	public static Timestamp getCalendarTimestamp(Calendar cal) {
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 系统当前的时间戳
	 *
	 * @return 系统当前的时间戳
	 */
	public static Timestamp getSqlTimestamp() {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = df.format(dt);
		Timestamp buydate = Timestamp.valueOf(nowTime);
		return buydate;
	}

	/**
	 * 系统时间的毫秒数
	 *
	 * @return 系统时间的毫秒数
	 */
	public static long getMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 指定日历的毫秒数
	 *
	 * @param cal
	 *            指定日历
	 * @return 指定日历的毫秒数
	 */
	public static long getMillis(Calendar cal) {
		return cal.getTime().getTime();
	}

	/**
	 * 指定日期的毫秒数
	 *
	 * @param date
	 *            指定日期
	 * @return 指定日期的毫秒数
	 */
	public static long getMillis(Date date) {
		return date.getTime();
	}

	/**
	 * 指定时间戳的毫秒数
	 *
	 * @param ts
	 *            指定时间戳
	 * @return 指定时间戳的毫秒数
	 */
	public static long getMillis(Timestamp ts) {
		return ts.getTime();
	}

	/**
	 * 将日期按照一定的格式转化为字符串 默认方式表示的系统当前日期，具体格式：年-月-日
	 *
	 * @return 默认日期按“年-月-日“格式显示
	 */
	public static String formatDate() {
		return date_sdf.format(getCalendar().getTime());
	}

	/**
	 * 获取时间字符串
	 */
	public static String getDataString(SimpleDateFormat formatstr) {
		return formatstr.format(getCalendar().getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日
	 *
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“年-月-日“格式显示
	 */
	public static String formatDate(Calendar cal) {
		return date_sdf.format(cal.getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日
	 *
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“年-月-日“格式显示
	 */
	public static String formatDate(Date date) {
		return date_sdf.format(date);
	}

	/**
	 * 指定毫秒数表示日期的默认显示，具体格式：年-月-日
	 *
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“年-月-日“格式显示
	 */
	public static String formatDate(long millis) {
		return date_sdf.format(new Date(millis));
	}

	/**
	 * 默认日期按指定格式显示
	 *
	 * @param pattern
	 *            指定的格式
	 * @return 默认日期按指定格式显示
	 */
	public static String formatDate(String pattern) {
		return getSDFormat(pattern).format(getCalendar().getTime());
	}

	/**
	 * 指定日期按指定格式显示
	 *
	 * @param cal
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @return 指定日期按指定格式显示
	 */
	public static String formatDate(Calendar cal, String pattern) {
		return getSDFormat(pattern).format(cal.getTime());
	}

	/**
	 * 指定日期按指定格式显示
	 *
	 * @param date
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @return 指定日期按指定格式显示
	 */
	public static String formatDate(Date date, String pattern) {
		return getSDFormat(pattern).format(date);
	}

	/**
	 * 将日期按照一定的格式转化为字符串 默认方式表示的系统当前日期，具体格式：年-月-日 时：分
	 *
	 * @return 默认日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime() {
		return time_sdf.format(getCalendar().getTime());
	}

	/**
	 * 将日期按照一定的格式转化为字符串 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
	 *
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(long millis) {
		return time_sdf.format(new Date(millis));
	}

	/**
	 * 将日期按照一定的格式转化为字符串 指定日期的默认显示，具体格式：年-月-日 时：分
	 *
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(Calendar cal) {
		return time_sdf.format(cal.getTime());
	}

	/**
	 * 将日期按照一定的格式转化为字符串 指定日期的默认显示，具体格式：年-月-日 时：分
	 *
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(Date date) {
		return time_sdf.format(date);
	}

	/**
	 * 将日期按照一定的格式转化为字符串 默认方式表示的系统当前日期，具体格式：时：分
	 *
	 * @return 默认日期按“时：分“格式显示
	 */
	public static String formatShortTime() {
		return short_time_sdf.format(getCalendar().getTime());
	}

	/**
	 * 指定毫秒数表示日期的默认显示，具体格式：时：分
	 *
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“时：分“格式显示
	 */
	public static String formatShortTime(long millis) {
		return short_time_sdf.format(new Date(millis));
	}

	/**
	 * 指定日期的默认显示，具体格式：时：分
	 *
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“时：分“格式显示
	 */
	public static String formatShortTime(Calendar cal) {
		return short_time_sdf.format(cal.getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：时：分
	 *
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“时：分“格式显示
	 */
	public static String formatShortTime(Date date) {
		return short_time_sdf.format(date);
	}

	/**
	 * 将字符串按照一定的格式转化为日期或时间 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 *
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的日期
	 * @throws ParseException
	 * @throws
	 */
	public static Date parseDate(String src, String pattern) throws ParseException {
		return getSDFormat(pattern).parse(src);

	}

	/**
	 * 将字符串按照一定的格式转化为日期或时间 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 *
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的日期
	 * @throws ParseException
	 * @throws
	 */
	public static Calendar parseCalendar(String src, String pattern) throws ParseException {

		Date date = parseDate(src, pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * 为字符串日期添加天数
	 *
	 * @author gaolh
	 * @date 2016-7-11
	 *
	 * @param src
	 *            字符串日期
	 * @param pattern
	 *            字符串日期格式
	 * @param amount
	 *            添加的天数
	 * @return 计算后的结果
	 * @throws ParseException
	 *             格式化异常
	 */
	public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
		Calendar cal;
		cal = parseCalendar(src, pattern);
		cal.add(Calendar.DATE, amount);
		return formatDate(cal);
	}

	/**
     * 为日期添加天数(工作日)
     * @param date
     * @param timeLimit
     * @return
     * @author wangxl
     * @date 2018-08-15
     */
    public static Date addDateForWork(Date date, Integer timeLimit) {
       // Date calDate = new LimitDateCalc().dateCalcWork(date,timeLimit);
        //return calDate;
		return null;
    }

	/**
	 * 为日期添加天数
	 *
	 * @author gaolh
	 * @date 2016-7-11
	 *
	 * @param date
	 *            日期
	 * @param amount
	 *            添加的天数
	 * @return 计算后的结果
	 */
	public static Date addDate(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);
		return cal.getTime();
	}

	/**
	 * 为日期添加年数
	 *
	 * @author sunch
	 * @date 2017年9月2日
	 * @param date
	 *            日期
	 * @param amount
	 *            添加年数
	 * @return
	 */
	public static Date addDateYear(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, amount);
		return cal.getTime();
	}

	/**
	 * 将字符串按照一定的格式转化为日期或时间 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 *
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的时间戳
	 * @throws ParseException
	 * @throws
	 */
	public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
		Date date = parseDate(src, pattern);
		return new Timestamp(date.getTime());
	}

	/**
	 * 计算两个时间之间的差值，根据标志的不同而不同
	 *
	 * @param flag
	 *            计算标志，表示按照年/月/日/时/分/秒等计算
	 * @param calSrc
	 *            减数
	 * @param calDes
	 *            被减数
	 * @return 两个日期之间的差值
	 */
	public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

		long millisDiff = getMillis(calSrc) - getMillis(calDes);

		if (flag == 'y') {
			return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
		}

		if (flag == 'd') {
			return (int) (millisDiff / DAY_IN_MILLIS);
		}

		if (flag == 'h') {
			return (int) (millisDiff / HOUR_IN_MILLIS);
		}

		if (flag == 'm') {
			return (int) (millisDiff / MINUTE_IN_MILLIS);
		}

		if (flag == 's') {
			return (int) (millisDiff / SECOND_IN_MILLIS);
		}

		return 0;
	}

	/**
	 * 计算两个时间之间的差值，根据标志的不同而不同
	 *
	 * @param flag
	 *            计算标志，表示按照年/月/日/时/分/秒等计算
	 * @param dateSrc
	 *            减数
	 * @param dateDes
	 *            被减数
	 * @return 两个日期之间的差值
	 */
	public static int dateDiff(char flag, Date dateSrc, Date dateDes) {
		if (dateSrc == null || dateDes == null) {
			return 0;
		}
		Calendar calSrc = Calendar.getInstance();
		calSrc.setTime(dateSrc);

		Calendar calDes = Calendar.getInstance();
		calDes.setTime(dateDes);

		long millisDiff = getMillis(calSrc) - getMillis(calDes);

		if (flag == 'y') {
			return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
		}

		if (flag == 'd') {
			return (int) (millisDiff / DAY_IN_MILLIS);
		}

		if (flag == 'h') {
			return (int) (millisDiff / HOUR_IN_MILLIS);
		}

		if (flag == 'm') {
			return (int) (millisDiff / MINUTE_IN_MILLIS);
		}

		if (flag == 's') {
			return (int) (millisDiff / SECOND_IN_MILLIS);
		}

		return 0;
	}

	/**
	 * String类型 转换为Date, 如果参数长度为10 转换格式”yyyy-MM-dd“ 如果参数长度为19 转换格式”yyyy-MM-dd
	 * HH:mm:ss“ * @param text String类型的时间值
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			try {
				if (text.indexOf(":") == -1 && text.length() == 10) {
					setValue(date_sdf.parse(text));
				} else if (text.indexOf(":") > 0 && text.length() == 19) {
					setValue(datetimeFormat.parse(text));
				} else {
					throw new IllegalArgumentException("Could not parse date, date format is error ");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			setValue(null);
		}
	}

	/**
	 * 获取当前时间的年份
	 *
	 * @return
	 */
	public static int getYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getDate());
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取当前时间的月份
	 *
	 * @return
	 */
	public static int getMonth() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getDate());
		return calendar.get(Calendar.MONTH)+1;
	}

	/**
	 * 获取当前时间的天数
	 *
	 * @return
	 */
	public static int getDay() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getDate());
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *
	 * @param flag
	 *            增加时间类型，1代表天数，2代表月数，3代表年数
	 * @param initDate
	 *            初始时间
	 * @param addTime
	 *            增加时间数目
	 * @param subDays
	 *            需要减去的天数，此处单位默认为天
	 * @return
	 */
	public static Date calcuDate(Integer flag, Date initDate, Integer addTime, Integer subDays) {
		Calendar cal = Calendar.getInstance();
		Date deadDateLine;
		switch (flag) {
		case 1:// 天
			int addDays = addTime - subDays;
			deadDateLine = DateUtil.addDate(initDate, addDays);
			break;
		case 2:// 月
			cal.setTime(initDate);
			cal.add(Calendar.MONTH, addTime);
			deadDateLine = DateUtil.addDate(cal.getTime(), 0 - subDays);
			break;
		case 3:// 年
			cal.setTime(initDate);
			cal.add(Calendar.YEAR, addTime);
			deadDateLine = DateUtil.addDate(cal.getTime(), subDays);
			break;
		default:
			return null;
		}
		return deadDateLine;
	}

	/**
	 * 比较两个时间大小
	 *
	 * @param dateOne
	 *            日期1
	 * @param dateTwo
	 *            日期2
	 * @return
	 */
	public static boolean compareDate(Date dateOne, Date dateTwo) {
		if (dateOne != null && dateTwo != null) {
			if (dateOne.getTime() > dateTwo.getTime()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当天开始时间
	 *
	 * @return
	 */
	public static Date getBeginADay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// 今天开始时间
		Date start = calendar.getTime();
		return start;

	}

	/**
	 * 获取当天结束时间
	 *
	 * @return
	 */
	public static Date getEndADay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		// 今天结束时间
		Date end = calendar.getTime();
		return end;

	}
	/**
	 * 当天结束时间
	* @author dongxl
	* @param
	* @return Date
	* @date 2018年7月14日
	* @throws
	 */
	public static Date getEndADay(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		// 今天结束时间
		Date end = calendar.getTime();
		return end;

	}

	/**
	 * 本月开始时间
	 *
	 * @return
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();

	}
	/**
	 * 本月结束时间
	 *
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();

	}

	// 获得本月最后一天24点时间
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	/**
	 * 根据当前时间获取上周星期一
	 *
	 * @author ZLY
	 */
	public static Date getWorkFirst(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 00:00:00", DateUtil.datetimeFormat);
		date = DateUtil.addDate(date, -7);
		// 获取date是星期几
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int wokdays = cal.get(Calendar.DAY_OF_WEEK);
		switch (wokdays) {
		case 1:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 6);
			return cal.getTime();
		case 2:
			return cal.getTime();
		case 3:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
			return cal.getTime();
		case 4:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 2);
			return cal.getTime();
		case 5:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 3);
			return cal.getTime();
		case 6:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 4);
			return cal.getTime();
		case 7:
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 5);
			return cal.getTime();
		default:
			return date;
		}
	}


	public static Date getThisWorkFirst(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 00:00:00", DateUtil.datetimeFormat);
		// 获取date是星期几
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int wokdays = cal.get(Calendar.DAY_OF_WEEK);
		switch (wokdays) {
			case 1:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 6);
				return cal.getTime();
			case 2:
				return cal.getTime();
			case 3:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
				return cal.getTime();
			case 4:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 2);
				return cal.getTime();
			case 5:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 3);
				return cal.getTime();
			case 6:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 4);
				return cal.getTime();
			case 7:
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 5);
				return cal.getTime();
			default:
				return date;
		}
	}

	/**
	 * 获取上周星期日
	 *
	 * @author ZLY
	 */
	public static Date getWorkEnd(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 23:59:59", DateUtil.datetimeFormat);
		return DateUtil.addDate(DateUtil.getWorkFirst(date), 6);
	}

	/**
	 * 获取上月第一天
	 *
	 * @author ZLY
	 */
	public static Date getMonthFirst(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 00:00:00", DateUtil.datetimeFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 获取上月最后一天
	 *
	 * @author ZLY
	 */
	public static Date getMonthEnd(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 23:59:59", DateUtil.datetimeFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// 日期减一,取得上月最后一天时间对象
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 获取去年第一天
	 *
	 * @author ZLY
	 */
	public static Date getYearFirst(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 00:00:00", DateUtil.datetimeFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 获取去年最后一天
	 *
	 * @author ZLY
	 */
	public static Date getYearEnd(Date date) {
		if (date == null) {
			date = new Date();
		}
		date = DateUtil.str2Date(DateUtil.formatDate(date) + " 23:59:59", DateUtil.datetimeFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.getTime();
	}

	/**
	 * 根据day天以前的日期
	 *
	 * @author zly
	 * @date 2017-8-3
	 * @param day
	 *            天数
	 * @return
	 */
	public static Date getTimeBeforeDay(int day) {
		Date d = new Date();
		return (new Date(d.getTime() - (long) day * 24 * 60 * 60 * 1000));
	}

	/**
	 * 当前时间所在季度的开始时间
	 *
	 * @author sunch
	 * @date 2017年8月1日
	 * @return
	 */
	public static Date getCurrentQuarterStartTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = null;
		calendar.setTime(new Date());
		int month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1, true);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		try {
			now = datetimeFormat.parse(date_sdf.format(calendar.getTime()) + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前时间所在季度的结束时间
	 *
	 * @author sunch
	 * @date 2017年8月1日
	 * @return
	 */
	public static Date getCurrentQuarterEndTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Date now = null;
		int month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1, false);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		try {
			now = datetimeFormat.parse(date_sdf.format(calendar.getTime()) + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 返回第几个月份， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
	 *
	 * @author sunch
	 * @date 2017年8月2日
	 * @param month
	 *            月份
	 * @param isQuarterStart
	 *            true：月首 false：月未
	 * @return
	 */
	private static int getQuarterInMonth(int month, boolean isQuarterStart) {
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart) {
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 2 && month <= 4){
			return months[0];
		}
		else if (month >= 5 && month <= 7){
			return months[1];
		}else if(month >= 8 && month <= 10){
			return months[2];
		} else{
			return months[3];
		}
	}
	/**
     * 获取本月第一天
     *
     */
    public static Date getBenYueDiYiTian(Date date) {
        if (date == null) {
            date = new Date();
        }
        date = DateUtil.str2Date(DateUtil.formatDate(date) + " 00:00:00", DateUtil.datetimeFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 验证预约时间是否过期 true 表示过期
     * @author shimh
     * @date 2018年7月13日
     * @param
     *
     * @param appointmentTime
     *            预约日期
     * @return
     */
    public static boolean invalidAppointmentTime(String appointmentSectionNo, Date appointmentTime) throws Exception {
        if (StringUtil.isNotEmpty(appointmentSectionNo) && null != appointmentTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            appointmentSectionNo = appointmentSectionNo.substring(appointmentSectionNo.lastIndexOf('-')+1, appointmentSectionNo.length());
            // 拼接预约时间 字符串
            String dateStr = sdf.format(appointmentTime) + " " + appointmentSectionNo + ":00";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date(); // 当前时间
            Date date = df.parse(dateStr); // 预约时间
            long timeValue = date.getTime() - now.getTime(); // 获取当前时间距离预约时间的差值
            if (timeValue < 0) {
                return true; // 表示已过期
            }
        }
        return false;
    }
    /**
     * 一个日期加上几分钟后的时间
    * @author dongxl
    * @param
    * @return String
    * @date 2018年7月18日
    * @throws
     */
    public static String addDateMinut(String day, int x)//返回的是字符串型的时间，输入的
   {
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 24小时制
  //量day格式一致
          Date date = null;
          try {
              date = format.parse(day);
          } catch (Exception ex) {
              ex.printStackTrace();
          }
          if(date == null){
			  return "";
		  }

          //System.out.println("front:" + format.format(date)); //显示输入的日期
          Calendar cal = Calendar.getInstance();
          cal.setTime(date);
          cal.add(Calendar.MINUTE, x);// 24小时制
          date = cal.getTime();
          //System.out.println("after:" + format.format(date));  //显示更新后的日期
          cal = null;
          return format.format(date);
      }
    /**
     * 获取一个月的工作日数
    * @author dongxl
    * @param
    * @return int
    * @date 2018年7月19日
    * @throws
     */
    public static int countMonthWorkDay(int year,int month){
    	int i=0;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,  month - 1);
        cal.set(Calendar.DATE, 1);
        while(cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) < month){
            Date calcDate = cal.getTime();
            /*if(LimitDateCalc.checkWorkDayForNowDate(calcDate)){
            	i++;
            }*/
            cal.add(Calendar.DATE, 1);
        }
        return i;
    }

    /**
     * 获取两个时间的差值（工作日）
     * @param
     * @param dateSrc
     * @param dateDes
     * @return
     * @author wangxl
     * @date 2018-08-15
     */
    public static Integer dateDiffWork(Date dateSrc, Date dateDes) {
		if (dateSrc == null || dateDes == null) {
			return 0;
		}
		long limt = 0;
		int dateDif = DateUtil.dateDiff('d', dateSrc, dateDes);
		for(int i=0;i<dateDif;i++){
			/*if(LimitDateCalc.checkWorkDayForNowDate(dateSrc)){ //是工作日
				limt++;
			} */
		}
		return (int) limt;
	}

    /**
	 * 获取当年的第一天
	 * @param
	 * @return
	 */
	public static Date getCurrYearFirst(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 * @param
	 * @return
	 */
	public static Date getCurrYearLast(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}
}

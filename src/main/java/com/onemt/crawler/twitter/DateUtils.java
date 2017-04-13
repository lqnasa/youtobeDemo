package com.onemt.crawler.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.StringUtils;

/**
 * 日期工具类
 *
 * @see org.apache.commons.lang.time.DateUtils
 */
public final class DateUtils extends org.apache.commons.lang.time.DateUtils {

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private DateUtils() {
    }

    static {
//        TimeZone.setDefault(Constant.DEFAULT_TIMEZONE);
    }

    /**
     * 默认的格式
     */
    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_DATEFORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_DATEFORMAT = "HH:mm:ss";

    /**
     * <p>
     * 根据默认的格式格式化当前时间
     * </p>
     *
     * @return 格式化后的日期字符串
     */
    public static String format() {
        return format(new Date(), DEFAULT_DATEFORMAT);
    }

    /**
     * <p>
     * 根据默认的格式格式化时间
     * </p>
     *
     * @param date 要格式化的日期/时间
     * @return 格式化后的日期字符串
     */
    public static String format(Date date) {
        return format(date, DEFAULT_DATEFORMAT);
    }
    
    /**
     * 
    * @Title: getCurrentByFormat
    * @Description: 根据固定格式获取当前时间字符串
    * @param @param format
    * @param @return   
    * @return String    
    * @throws
     */
    public static String getCurrentByFormat(String format) {
        return format(new Date(), format);
    }

    /**
     * <p>
     * 根据格式格式化日期/时间
     * </p>
     *
     * @param date    要格式化的日期/时间
     * @param pattern 要使用的规则
     * @return 格式化后的日期字符串
     */
    public static String format(Date date, String pattern) {
        if (!StringUtils.hasText(pattern)) {
            pattern = DEFAULT_DATEFORMAT;
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 根据默认格式把文本转换成日期/时间，
     * 转换出错，抛出异常
     *
     * @param text 要转换的文本
     * @return 转换后的日期
     */
    public static Date parse(String text) {
        return parse(text, DEFAULT_DATEFORMAT);
    }

    /**
     * 根据格式把文本转换成日期/时间，
     * 转换出错，抛出异常
     *
     * @param text    要转换的文本
     * @param pattern 要使用的规则
     * @return 转换后的日期
     */
    public static Date parse(String text, String pattern) {
        if (!StringUtils.hasText(pattern)) {
            pattern = DEFAULT_DATEFORMAT;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(text);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Date parse happen error![" + text + "," + pattern + "]");
        }
    }

	public static Long currentTimeSecs() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static Long getTimestamp(Date date){
		return date.getTime()/1000;
	}
	
	
	public static Long getTimeFromStr(String dateStr,String formate){
		Long dateLong = currentTimeSecs();
		try {
		 	SimpleDateFormat sdf = new SimpleDateFormat(formate);  
			Date date = sdf.parse(dateStr);
			dateLong = date.getTime()/1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateLong;
	}
	
	public static Long getTimeFromStr(String dateStr,String formate,Locale locale){
		Long dateLong = currentTimeSecs();
		try {
		 	SimpleDateFormat sdf = new SimpleDateFormat(formate,locale);  
			Date date = sdf.parse(dateStr);
			dateLong = date.getTime()/1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dateLong;
	}
	
	
	/**
	 * 
	* @Title: getTimeFromStrByZone
	* @Description: 
	* @param @param dateStr
	* @param @param formate
	* @param @param timeZoneStr GTM+0800
	* @param @return
	* @param @throws Exception   
	* @return Long    
	* @throws
	 */
	public static Long getTimeFromStrByZone(String dateStr,String formate,String timeZoneStr)throws Exception{
		if(StringUtils.isEmpty(dateStr)){
			return null;
		}
	 	SimpleDateFormat sdf = new SimpleDateFormat(formate);  
	 	TimeZone timeZone = TimeZone.getTimeZone(timeZoneStr);
	 	sdf.setTimeZone(timeZone);
		Date date = sdf.parse(dateStr);
		return date.getTime()/1000;
	}
	
	public static String getTimeFromLong(Long dateLong,String formate)throws Exception{
		SimpleDateFormat sdf= new SimpleDateFormat(formate);
		Date dt = new Date(dateLong*1000);  
		return sdf.format(dt);
	}
	

	/**
	 * 
	 * @Title: getUTCTime
	 * @Description: 直接将只有年月日的日期转换为UTC校准时间，数字字符串
	 * @param dateTimeStr
	 * @param pattern
	 * @return String 时间戳
	 */

	public static String getUTCTime(String dateTimeStr,String pattern){
		try {
		 	SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.ENGLISH);
		 	sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = sdf.parse(dateTimeStr);
			return date.getTime()/1000+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static String getUTCTimeByLocal(String dateTimeStr,String pattern,Locale locale){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern,locale);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = sdf.parse(dateTimeStr);
			return date.getTime()/1000+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) {
		try {
//			System.out.println(DateUtils.getTimeFromStr("", "yyyy-MM-dd HH:mm:ss z"));
//			System.out.println(DateUtils.getTimeFromStr("05-12-2016 الساعة 11:48","dd-MM-yyyy HH:mm a",new Locale("ar")));
			String a = "الاثنين 05-12-2016 الساعة 11:48";
			System.out.println(DateUtils.getTimeFromStr("السبت 07/يناير/2017 - 09:21 م".replace(" ", ""),"EEEdd/MMM/yyyy-hh:mma",new Locale("ar")));
			System.out.println(DateUtils.getTimeFromStr("الثلاثاء ٢٩ نوفمبر ٢٠١٦","EEE dd MMM yyyy",new Locale("ar")));
//			System.out.println(DateUtils.getTimeFromStr("Tue, 29 Nov 2016 2:2:2","EEE, dd MMM yyyy HH:mm:ss",Locale.ENGLISH));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package pe.mayciel.fos.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 날짜 포메팅에 관련된 utility method 를 모아놓은 클래스.
 * @author Hwang Seong-wook
 * @since 2013. 01. 11.
 * @version 1.0.0.1 (2013. 01. 11.)
 */
public class DateFormatUtil {
	/**
	 * oldPattern 으로 이루어 진 oldDateStr 을 newPattern 으로 변환하여 반환한다.
	 * @param oldDateStr 기존 date string
	 * @param oldPattern oldDateStr 의 패턴
	 * @param newPattern 새로 바꾸고자 하는 패턴
	 * @return
	 * @throws ParseException
	 */
	public static String dateToStr(String oldDateStr, String oldPattern,
		String newPattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(oldPattern);
		return dateToStr(sdf.parse(oldDateStr), newPattern);
	}

	/**
	 * Calendar 도메인을 newPattern 형식의 string 으로 변환하여 반환한다.
	 * @param cal 변환하려는 date 값
	 * @param newPattern 새로 바꾸고자 하는 패턴
	 * @return
	 */
	public static String dateToStr(Calendar cal, String newPattern) {
		return new SimpleDateFormat(newPattern).format(cal.getTime());
	}

	/**
	 * date 를 newPattern 형식의 string 으로 변환하여 반환한다.
	 * @param date 변환하려는 date 값
	 * @param newPattern 새로 바꾸고자 하는 패턴
	 * @return
	 */
	public static String dateToStr(Date date, String newPattern) {
		return new SimpleDateFormat(newPattern).format(date);
	}

	/**
	 * Timestamp 도메인을 newPattern 형식의 string 으로 변환하여 반환한다.
	 * @param timestamp 변환하려는 timestamp 값
	 * @param newPattern 새로 바꾸고자 하는 패턴
	 * @return
	 */
	public static String dateToStr(Timestamp timestamp, String newPattern) {
		return dateToStr(timestamp.getTime(), newPattern);
	}

	/**
	 * long 타입의 타임스템프를 newPattern 형식의 string 으로 변환하여 반환한다.
	 * @param time 변환하려는 timestamp 값
	 * @param newPattern 새로 바꾸고자 하는 패턴
	 * @return
	 */
	public static String dateToStr(long time, String newPattern) {
		return dateToStr(new Date(time), newPattern);
	}

	/**
	 * datePattern 형식의 dateStr 을 Calendar 타입으로 변환하여 반환한다.
	 * @param dateStr 변환하려는 date 의 string 값
	 * @param datePattern date pattern
	 * @return
	 * @throws ParseException
	 */
	public static Calendar strToCalendar(String dateStr, String datePattern) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat(datePattern).parse(dateStr));
		return cal;
	}

	/**
	 * datePattern 형식의 dateStr 을 Date 타입으로 변환하여 반환한다.
	 * @param dateStr 변환하려는 date 의 string 값
	 * @param datePattern date pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String dateStr, String datePattern) throws ParseException {
		return new SimpleDateFormat(datePattern).parse(dateStr);
	}

	/**
	 * datePattern 형식의 dateStr 을 Timestamp 타입으로 변환하여 반환한다.
	 * @param dateStr 변환하려는 date 의 string 값
	 * @param datePattern date pattern
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp strToDateTimestamp(String dateStr,
		String datePattern) throws ParseException {
		return new Timestamp(strToDateLong(dateStr, datePattern));
	}

	/**
	 * datePattern 형식의 dateStr 을 long 타입으로 변환하여 반환한다.
	 * @param dateStr 변환하려는 date 의 string 값
	 * @param datePattern date pattern
	 * @return
	 * @throws ParseException
	 */
	public static long strToDateLong(String dateStr, String datePattern) throws ParseException {
		return strToDate(dateStr, datePattern).getTime();
	}

	/**
	 * 오늘 날짜를 기본 날짜 형식 (yyyy-MM-dd) 으로 변경하여 반환한다.
	 * @return
	 */
	public static String todayToString() {
		return dateToStr(System.currentTimeMillis(), "yyyy-MM-dd");
	}

	/**
	 * 현재 시간을 {@link Timestamp} 으로 변경하여 반환한다.
	 * @return
	 */
	public static Timestamp nowToTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
}
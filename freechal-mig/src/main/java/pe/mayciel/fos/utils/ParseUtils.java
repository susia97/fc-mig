package pe.mayciel.fos.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils {
	/**
	 * input string 을 regex 에 매치한 {@link Matcher} 를 반환한다.
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static Matcher getMatcher(String regex, String input) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input);
	}

	/**
	 * obj 에 있는 모든 필드를 string 으로 변경한다.
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		StringBuilder sb = new StringBuilder();
		for (Field field : ClassUtil.getAllFields(obj.getClass())) {
			try {
				sb.append(field.getName()).append(":")
						.append(ClassUtil.getFieldValue(obj, field.getName()))
						.append(", ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * input string 을 regex 에 매치해서 몇번 일치했는지를 반환한다.
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static int getMatchCnt(String regex, String input) {
		Matcher mc = getMatcher(regex, input);
		int cnt = 0;
		while (mc.find()) {
			cnt++;
		}
		return cnt;
	}
}
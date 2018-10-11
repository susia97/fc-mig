package pe.mayciel.fos.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 이름을 원하는 notation 형식으로 변환해 주는 utility class.
 * @author Hwang Seong-wook
 * @since 2013. 01. 04.
 * @version 1.0.0.1 (2013. 01. 04.)
 */
public class NodeNameConvertor {
	/**
	 * 입력받은 name 을 camel 표기법으로 변환하여 반환한다.
	 * @param name
	 * @return camel 표기법으로 변환된 name. ex. thisIsCamel
	 */
	public static String toCamel(String name) {
		if (StringUtils.isBlank(name)) {
			return name;
		}
		if (!hasUnderscore(name)) {
			if (name.length() == 1) {
				return name.toLowerCase();
			}
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		StringBuilder result = new StringBuilder();
		String[] components = name.split("_");
		for (String component : components) {
			if (result.length() == 0) {
				result.append(toCamel(component));
			} else {
				result.append(toPascal(component));
			}
		}
		return result.toString();
	}

	/**
	 * 입력받은 name 을 pascal 표기법으로 변환하여 반환한다.
	 * @param name
	 * @return pascal 표기법으로 변환된 name. ex. ThisIsPascal
	 */
	public static String toPascal(String name) {
		if (StringUtils.isBlank(name)) {
			return name;
		}
		if (!hasUnderscore(name)) {
			if (name.length() == 1) {
				return name.toUpperCase();
			}
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		StringBuilder result = new StringBuilder();
		String[] components = name.split("_");
		for (String component : components) {
			result.append(toPascal(component));
		}
		return result.toString();
	}

	/**
	 * 입력받은 name 을 underscore 표기법으로 변환하여 반환한다.
	 * @param name
	 * @return underscore 표기법으로 변환된 name. ex. this_is_underscore
	 */
	public static String toUnderscore(String name) {
		if (StringUtils.isBlank(name)) {
			return name;
		}
		if (hasUnderscore(name)) {
			return name.toLowerCase();
		}
		StringBuilder result = new StringBuilder();
		for (char ch : name.toCharArray()) {
			if (result.length() == 0) {
				result.append(Character.toLowerCase(ch));
			} else if (Character.isUpperCase(ch)) {
				result.append("_").append(Character.toLowerCase(ch));
			} else {
				result.append(ch);
			}
		}
		return result.toString();
	}

	/**
	 * name 에 underscore(_) 가 포함되어 있는지 여부를 반환한다.
	 * @param name
	 * @return
	 */
	private static boolean hasUnderscore(String name) {
		return name.contains("_");
	}
}
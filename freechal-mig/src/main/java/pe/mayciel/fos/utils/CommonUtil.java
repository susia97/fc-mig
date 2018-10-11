package pe.mayciel.fos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 각종 utility method 를 모아놓은 클래스.
 * @author Hwang Seong-wook
 * @since 2013. 01. 11.
 * @version 1.0.0.1 (2013. 01. 11.)
 */
public class CommonUtil {
	/**
	 * str 값을 splitter 구분자로 나누어서 set 에 담고 set 을 반환한다.<br>
	 * set 에 있는 각각의 값들은 trim 처리를 하여 반환한다.
	 * @param <T> Set&lt;String&gt; 을 상속받은 객체
	 * @param set 나누어진 데이터를 담을 set 객체
	 * @param str 원본 데이터. splitter 로 데이터가 나뉘어져 있다.
	 * @param splitter 데이터들의 구분자. 정규식 형태.
	 * @return
	 */
	public static <T extends Set<String>> T str2Set(T set, String str,
		String splitter) {
		if (StringUtils.isNotBlank(str)) {
			String[] arr = str.split(splitter);
			for (String string : arr) {
				set.add(string.trim());
			}
		}
		return set;
	}

	/**
	 * str 값을 splitter 구분자로 나누어서 HashSet 으로 만들어 반환한다.<br>
	 * set 에 있는 각각의 값들은 trim 처리를 하여 반환한다.
	 * @param str 원본 데이터. splitter 로 데이터가 나뉘어져 있다.
	 * @param splitter 데이터들의 구분자. 정규식 형태.
	 * @return
	 */
	public static Set<String> str2HashSet(String str, String splitter) {
		return str2Set(new HashSet<String>(), str, splitter);
	}

	/**
	 * str 값을 splitter 구분자로 나누어서 list 에 담고 list 를 반환한다.<br>
	 * set 에 있는 각각의 값들은 trim 처리를 하여 반환한다.
	 * @param <T> List&lt;String&gt; 을 상속받은 객체
	 * @param list 나누어진 데이터를 담을 list 객체
	 * @param str 원본 데이터. splitter 로 데이터가 나뉘어져 있다.
	 * @param splitter 데이터들의 구분자. 정규식 형태.
	 * @return
	 */
	public static <T extends List<String>> T str2List(T list, String str,
		String splitter) {
		if (StringUtils.isNotBlank(str)) {
			String[] arr = str.split(splitter);
			for (String string : arr) {
				list.add(string.trim());
			}
		}
		return list;
	}

	/**
	 * str 값을 splitter 구분자로 나누어서 ArrayList 로 만들어 반환한다.<br>
	 * set 에 있는 각각의 값들은 trim 처리를 하여 반환한다.
	 * @param str 원본 데이터. splitter 로 데이터가 나뉘어져 있다.
	 * @param splitter 데이터들의 구분자. 정규식 형태.
	 * @return
	 */
	public static List<String> str2ArrayList(String str, String splitter) {
		return str2List(new ArrayList<String>(), str, splitter);
	}

	/**
	 * str 값을 splitter 구분자로 나누어서 array 로 만들어 반환한다.<br>
	 * array 에 있는 각각의 값들은 trim 처리를 하여 반환한다.
	 * @param str 원본 데이터. splitter 로 데이터가 나뉘어져 있다.
	 * @param splitter 데이터들의 구분자. 정규식 형태.
	 * @return
	 */
	public static String[] str2Array(String str, String splitter) {
		if (null == str) {
			return null;
		}
		String[] res = str.split(splitter);
		for (int i = 0; i < res.length; i++) {
			res[i] = res[i].trim();
		}
		return res;
	}

	/**
	 * str 값을 regexpSplitter 구분자로 나눈 후 enumClass 로 전환하여 리스트에 담아 반환한다.<br>
	 * enum 으로 전환 시, trim 처리를 한다.
	 * @param str 원본 데이터. regexpSplitter 로 데이터가 나뉘어져 있다.
	 * @param regexpSplitter 데이터들의 구분자. 정규식 형태.
	 * @param enmuClass 변환할 enum 의 class
	 * @return
	 */
	public static <T extends Enum<T>> List<T> str2EnumList(String str,
		String regexpSplitter, Class<T> enmuClass) {
		if (null == str || null == regexpSplitter || null == enmuClass) {
			return null;
		}
		String[] strArr = str.split(regexpSplitter);
		List<T> result = new ArrayList<T>();
		for (String string : strArr) {
			result.add(Enum.valueOf(enmuClass, string.trim()));
		}
		return result;
	}

	/**
	 * str 값을 regexpSplitter 구분자로 나눈 후 enumClass 로 전환하여 array 에 담아 반환한다.<br>
	 * enum 으로 전환 시, trim 처리를 한다.
	 * @param str 원본 데이터. regexpSplitter 로 데이터가 나뉘어져 있다.
	 * @param regexpSplitter 데이터들의 구분자. 정규식 형태.
	 * @param enmuClass 변환할 enum 의 class
	 * @return
	 */
	public static <T extends Enum<T>> T[] str2EnumArray(String str,
		String regexpSplitter, Class<T> enmuClass) {
		if (null == str || null == regexpSplitter || null == enmuClass) {
			return null;
		}
		String[] strArr = str.split(regexpSplitter);
		@SuppressWarnings("unchecked") T[] result = (T[])Array.newInstance(
			enmuClass, strArr.length);
		for (int i = 0; i < strArr.length; i++) {
			result[i] = Enum.valueOf(enmuClass, strArr[i].trim());
		}
		return result;
	}

	/**
	 * collection (List, Set) 의 모든 값을 splitter 값을 구분자로 연결하여 반환한다.<br>
	 * 객체에서 값은 toString 메소드를 이용하여 가져온다.
	 * @param col 원본 collection.
	 * @param splitter 데이터들의 구분자.
	 * @return
	 */
	public static String collection2String(Collection<? extends Object> col,
		String splitter) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends Object> it = col.iterator();
		while (it.hasNext()) {
			if (sb.length() != 0) {
				sb.append(splitter);
			}
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	/**
	 * array 안의 값을 splitter 값을 구분자로 연결하여 반환한다.<br>
	 * 객체에서 값은 toString 메소드를 이용하여 가져온다.
	 * @param arr
	 * @param splitter
	 * @return
	 */
	public static String array2String(Object[] arr, String splitter) {
		if (null == arr || null == splitter) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : arr) {
			if (sb.length() != 0) {
				sb.append(splitter);
			}
			sb.append(obj.toString());
		}
		return sb.toString();
	}

	/**
	 * ?, *, string 으로 이루어진 패턴을 정규식 표현으로 변환하여 반환한다.
	 * @param rawPattern
	 * @return
	 */
	public static String patternGenerator(String rawPattern) {
		if (StringUtils.isBlank(rawPattern)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("^");
		rawPattern = rawPattern.toUpperCase();
		int len = rawPattern.length();
		for (int i = 0; i < len; i++) {
			String ch = rawPattern.substring(i, i + 1);
			if ("?".equals(ch)) {
				sb.append("([\\w\\W]{1})");
			} else if ("*".equals(ch)) {
				sb.append("([\\w\\W]*)");
			} else {
				sb.append(ch);
			}
		}
		sb.append("$");
		return sb.toString();
	}

	/**
	 * HTML Entity code 를 encoding 하여 반환한다.<br>
	 * encoding 하는 대상 string 은 아래와 같다.<br>
	 * &, <, >, ", space
	 * @param str
	 * @return
	 */
	public static String entityCodeEncode(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		str = str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
			">", "&gt;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;");
		return str;
	}

	/**
	 * HTML Entity code 를 decoding 하여 반환한다.<br>
	 * decoding 하는 대상 code 는 아래와 같다.<br>
	 * &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot;, &amp;nbsp;
	 * @param str
	 * @return
	 */
	public static String entityCodeDecode(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		str = str.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll(
			"&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&");
		return str;
	}

	/**
	 * fileName 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.<br>
	 * UTF-8 로 인코딩 된 것으로 간주한다.
	 * @param fileName 해당 file 의 full path
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(String fileName) throws Exception {
		return loadFileByLine(fileName, "UTF-8");
	}

	/**
	 * fileName 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.
	 * @param fileName 해당 file 의 full path
	 * @param charsetName file 의 charset, null인 경우 UTF-8 로 설정됨.
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(String fileName,
		String charsetName) throws Exception {
		return loadFileByLine(new FileInputStream(fileName), charsetName);
	}

	/**
	 * file 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.<br>
	 * UTF-8 로 인코딩 된 것으로 간주한다.
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(File file) throws Exception {
		return loadFileByLine(file, "UTF-8");
	}

	/**
	 * file 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.
	 * @param file
	 * @param charsetName file 의 charset, null인 경우 UTF-8 로 설정됨.
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(File file, String charsetName) throws Exception {
		return loadFileByLine(new FileInputStream(file), charsetName);
	}

	/**
	 * fileInputStream 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.<br>
	 * UTF-8 로 인코딩 된 것으로 간주한다.
	 * @param fis
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(FileInputStream fis) throws Exception {
		return loadFileByLine(fis, "UTF-8");
	}

	/**
	 * fileInputStream 에 해당하는 text 파일을 읽어서, 줄 단위 List 로 반환한다.
	 * @param fis
	 * @param charsetName file 의 charset, null인 경우 UTF-8 로 설정됨.
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadFileByLine(FileInputStream fis,
		String charsetName) throws Exception {
		if (StringUtils.isBlank(charsetName)) {
			charsetName = "UTF-8";
		}
		InputStreamReader isr = new InputStreamReader(fis, charsetName);
		List<String> result = new ArrayList<String>();
		char[] cbuf = new char[1];
		StringBuilder sb = new StringBuilder();
		while (isr.read(cbuf) > 0) {
			if (Character.toString(cbuf[0]).equals("\r")) {
				result.add(sb.toString());
				isr.read(cbuf);
				sb = new StringBuilder();
				if (!Character.toString(cbuf[0]).equals("\n")) {
					sb.append(cbuf);
				}
			} else if (Character.toString(cbuf[0]).equals("\n")) {
				result.add(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(cbuf);
			}
		}
		result.add(sb.toString());
		return result;
	}

	/**
	 * \r\n, \r, \n 을 &lt;br&gt; 테그로 바꿔준다.
	 * @param str
	 * @return
	 */
	public static String cr2br(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		return str.replace("\r\n", "<br>").replace("\r", "<br>").replace("\n",
			"<br>");
	}

	/**
	 * &lt; &gt; 로 둘러쌓인 모든 글자를 제거한다.
	 * @param html
	 * @return
	 */
	public static String stripTags(String html) {
		String pattern = "(<[^>]*>)";
		return html.replaceAll(pattern, "");
	}

	/**
	 * 파일 명에서 확장자를 뽑아서 반환한다.<br>
	 * (즉, 마지막 . 이후의 글자를 반환한다.)<br>
	 * 반환되는 확장자는 소문자로 치환해서 반환한다.<br>
	 * 확장자가 없는 경우 빈 string("") 을 반환한다.
	 * @param fileNm
	 * @return
	 */
	public static String getFileExt(String fileNm) {
		if (StringUtils.isBlank(fileNm)) {
			return "";
		}
		int lastIdx = fileNm.lastIndexOf(".");
		if (lastIdx < 0) {
			return "";
		}
		return fileNm.substring(lastIdx + 1).toLowerCase();
	}

	/**
	 * 입력받은 arg 들 중에서 가장 처음으로 null 이 아닌 값을 반환한다.<br>
	 * 모두 다 null 이면 null 을 반환함.
	 * @param arg
	 * @param args
	 * @return
	 */
	public static Object getNotNullObj(Object arg, Object... args) {
		if (null != arg) {
			return arg;
		}
		for (Object obj : args) {
			if (null != obj) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * 입력받은 string 들 중에서 가장 처음으로 empty 가 아닌 값을 반환한다.<br>
	 * 모두 다 empty 이면 null 을 반환함.
	 * @param str
	 * @param strs
	 * @return
	 */
	public static String getNotEmptyStr(String str, String... strs) {
		if (StringUtils.isNotEmpty(str)) {
			return str;
		}
		for (String string : strs) {
			if (StringUtils.isNotEmpty(string)) {
				return string;
			}
		}
		return null;
	}

	/**
	 * 입력받은 string 들 중에서 가장 처음으로 blank 가 아닌 값을 반환한다.<br>
	 * 모두 다 blank 이면 null 을 반환함.
	 * @param str
	 * @param strs
	 * @return
	 */
	public static String getNotBlankStr(String str, String... strs) {
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		for (String string : strs) {
			if (StringUtils.isNotBlank(string)) {
				return string;
			}
		}
		return null;
	}

	/**
	 * 파일 용량을 표시하는 string 을 받아서 용량에 맞는 string 으로 변환하여 반환한다.<br>
	 * ex. 2048KB --> 2MB, 1048576 --> 1MB<br>
	 * <br>
	 * 소숫점은 표시하지 않는다.<br>
	 * 소숫점 값을 얻고 싶으면 fileSizeFormat(String value, int maxFractionDigits) 을 이용할 것.
	 * @param value 파일 용량. 숫자만으로 이루어지면 byte 단위로 취급. 뒤에 K, KB, M, MB 등의 단위를 인식한다(대소문자 구분안함).
	 * @return
	 */
	public static String fileSizeFormat(String value) {
		return fileSizeFormat(value, 0);
	}

	/**
	 * 파일 용량을 표시하는 string 을 받아서 용량에 맞는 string 으로 변환하여 반환한다.<br>
	 * ex. 2048KB --> 2MB, 1048576 --> 1MB
	 * @param value 파일 용량. 숫자만으로 이루어지면 byte 단위로 취급. 뒤에 K, KB, M, MB 등의 단위를 인식한다(대소문자 구분안함).
	 * @param maxFractionDigits 반환하는 숫자에서 표시할 소숫점 자릿수
	 * @return
	 */
	public static String fileSizeFormat(String value, int maxFractionDigits) {
		if (StringUtils.isBlank(value)) {
			return "0B";
		}
		String regex = "([0-9]+(?:\\.[0-9]+)?)([KMGT]?)";
		Pattern pt = Pattern.compile(regex);
		Matcher mc = pt.matcher(value.toUpperCase());
		if (mc.find()) {
			double val = getByteSize(mc);
			String unit = "";
			if (val < Math.pow(1024, 1)) {
				unit = "B";
			} else if (val < Math.pow(1024, 2)) {
				val /= Math.pow(1024, 1);
				unit = "KB";
			} else if (val < Math.pow(1024, 3)) {
				val /= Math.pow(1024, 2);
				unit = "MB";
			} else if (val < Math.pow(1024, 4)) {
				val /= Math.pow(1024, 3);
				unit = "GB";
			} else {
				val /= Math.pow(1024, 4);
				unit = "TB";
			}
			return formatNumber(val, maxFractionDigits) + unit;
		}
		return "0B";
	}

	/**
	 * 입력받은 string 이 숫자로만 이루어져 있는지 여부를 반환한다.
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		String numPattern = "[\\d]+";
		return Pattern.matches(numPattern, str);
	}

	/**
	 * sb 에 th 의 에러로그 값을 추가한다.
	 * @param sb
	 * @param th
	 */
	public static void appendErrorLogs(StringBuffer sb, Throwable th) {
		StringWriter sw = new StringWriter();
		th.printStackTrace(new PrintWriter(sw));
		sb.append(sw.toString());
	}

	/**
	 * ko_KR 과 같은 형식으로 되어 있는 locale 값을 {@link Locale} 도메인으로 반환한다.<br>
	 * 형식이 맞지 않으면 null 을 반환.
	 * @param localeStr ko_KR 형식
	 * @return
	 */
	public static Locale getLocale(String localeStr) {
		if (StringUtils.isBlank(localeStr)) {
			return null;
		}
		String[] split = localeStr.split("_");
		if (split.length != 2) {
			return null;
		}
		return new Locale(split[0], split[1]);
	}

	/**
	 * language 값과 country 값으로 {@link Locale} 도메인을 반환한다.<br><br>
	 * language 값이 없거나 2자리 미만이면 null 을 반환하고,<br>
	 * 2자리 이상이면 앞 2자리만 취한 후, 소문자 처리하여 작업한다.<br>
	 * country 값이 없거나 2자리 미만이면 null 을 반환하고,<br>
	 * 2자리 이상이면 앞 2자리만 취한 후, 대문자 처리하여 작업한다.
	 * @param language 언어 값. ko 형식. 앞 2자리만 인식하며, 소문자 처리하여 작업한다.
	 * @param country 국가 값. KR 형식. 앞 2자리만 인식하며, 대문자 처리하여 작업한다. 
	 * @return
	 */
	public static Locale getLocale(String language, String country) {
		if (StringUtils.isBlank(language) || language.length() < 2) {
			return null;
		}
		if (StringUtils.isBlank(country) || country.length() < 2) {
			return null;
		}
		if (language.length() != 2) {
			language = language.substring(0, 2);
		}
		language = language.toLowerCase();
		if (country.length() != 2) {
			country = country.substring(0, 2);
		}
		country = country.toUpperCase();
		return new Locale(language, country);
	}

	/**
	 * 입력받은 list 의 순서를 역순으로 조정한 새 list 를 반환한다.
	 * @param list
	 * @return list
	 */
	public static <T> List<T> createReverseList(List<T> list) {
		if (null == list) {
			return null;
		}
		int listCount = list.size();
		List<T> newList = new ArrayList<T>(listCount);
		for (int i = listCount - 1; i >= 0; i--) {
			newList.add(list.get(i));
		}
		return newList;
	}

	/**************************************************
	 * 
	 * Private Methods
	 * 
	 **************************************************/

	/**
	 * matcher 의 정보를 기반으로 용량을 byte 로 환산하여 반환한다.
	 * @param matcher
	 * @return
	 */
	private static double getByteSize(Matcher matcher) {
		double val = Double.parseDouble(matcher.group(1));
		String unit = matcher.group(2);
		if ("K".equals(unit)) {
			val *= Math.pow(1024, 1);
		} else if ("M".equals(unit)) {
			val *= Math.pow(1024, 2);
		} else if ("G".equals(unit)) {
			val *= Math.pow(1024, 3);
		} else if ("T".equals(unit)) {
			val *= Math.pow(1024, 4);
		}
		return val;
	}

	/**
	 * 숫자를 정해진 양식으로 다듬어서 반환한다.
	 * @param val
	 * @param maxFractionDigits
	 * @return
	 */
	private static String formatNumber(double val, int maxFractionDigits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(maxFractionDigits);
		return nf.format(val);
	}
}
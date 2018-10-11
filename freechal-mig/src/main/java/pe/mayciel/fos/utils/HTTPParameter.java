package pe.mayciel.fos.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * HTTP 통신에서 사용하는 parameter 값을 담고 있는 클래스.<br>
 * {@link HttpServletRequest} 에서 제공하는 parameter 관련 메소드를 제공하고,<br>
 * queryString 으로 객체 생성, 객체를 queryString 으로 변환 등의 기능을 제공한다.
 * 
 * @author Hwang Seong-wook
 * 
 */
public class HTTPParameter {
	private Map<String, List<String>> parameterMap = new HashMap<String, List<String>>();

	/**
	 * 새로운 객체를 생성한다.
	 */
	public HTTPParameter() {
		super();
	}

	/**
	 * queryString 값을 이용하여 새로운 객체를 생성한다.
	 * 
	 * @param queryString
	 * @param encoding
	 */
	public HTTPParameter(String queryString, String encoding) {
		super();
		setQueryString(queryString, encoding);
	}

	/**
	 * parameterMap 을 파싱하여 queryString 으로 만들어 반환한다
	 * 
	 * @param encoding
	 * @return
	 */
	public String getQueryString(String encoding) {
		StringBuilder queryString = new StringBuilder();
		for (String key : parameterMap.keySet()) {
			if (null == parameterMap.get(key)) {
				continue;
			}
			for (String val : parameterMap.get(key)) {
				if (queryString.length() > 0) {
					queryString.append("&");
				}
				try {
					queryString.append(URLEncoder.encode(key, encoding))
							.append("=")
							.append(URLEncoder.encode(val, encoding));
				} catch (UnsupportedEncodingException e) {
					// Cannot be. Ignore it.
				}
			}
		}
		return queryString.toString();
	}

	/**
	 * queryString 값을 파싱하여 parameterMap 에 값을 설정한다.
	 * 
	 * @param queryString
	 * @param encoding
	 */
	private void setQueryString(String queryString, String encoding) {
		if (StringUtils.isBlank(queryString)) {
			return;
		}
		for (String params : queryString.split("&")) {
			String[] args = params.split("=", 2);
			if (args.length == 2) {
				try {
					addParameter(args[0], URLDecoder.decode(args[1], encoding));
				} catch (UnsupportedEncodingException e) {
					// Cannot be. Ignore it.
				}
			}
		}
	}

	/**
	 * name 에 해당하는 파라메터 값을 반환한다.<br>
	 * 파라메터 값이 여러개인 경우, 그 중 맨 앞의 값을 반환한다.
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		List<String> values = parameterMap.get(name);
		return null == values || values.size() < 1 ? null : values.get(0);
	}

	/**
	 * 파라메터 값을 설정한다.<br>
	 * 기존에 같은 name 으로 설정되어 있는 값은 덮어쓴다.
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, String value) {
		List<String> values = new ArrayList<String>();
		values.add(value);
		parameterMap.put(name, values);
	}

	/**
	 * 파라메터 값을 추가한다.
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, String value) {
		if (!parameterMap.containsKey(name)) {
			parameterMap.put(name, new ArrayList<String>());
		}
		parameterMap.get(name).add(value);
	}

	/**
	 * 파라메터 맵을 반환한다.
	 * 
	 * @return
	 */
	public Map<String, List<String>> getParameterMap() {
		return parameterMap;
	}

	/**
	 * 파라메터 맵을 설정한다.
	 * 
	 * @param parameterMap
	 */
	public void setParameterMap(Map<String, List<String>> parameterMap) {
		this.parameterMap = parameterMap;
	}

	/**
	 * 파라메터 name 들의 set 을 반환한다.
	 * 
	 * @return
	 */
	public Set<String> getParameterNames() {
		return parameterMap.keySet();
	}

	/**
	 * name 에 해당하는 파라메터 값 목록을 반환한다.<br>
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getParameterValues(String name) {
		return parameterMap.containsKey(name) ? parameterMap.get(name)
				: new ArrayList<String>();
	}
}
package pe.mayciel.fos.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * HTTP 접속 정보를 가지고 있는 domain.<br>
 * request method (GET, POST) 및<br>
 * header, cookie, parameter 값을 설정할 수 있고,<br>
 * connectTimeout, readTimeout 타임을 설정할 수 있다.
 * 
 * @author hwang
 * @since 2012. 12. 13.
 */
public class HttpConnectionInfo implements Serializable {
	private static final long serialVersionUID = -8059153538342332622L;

	private String url = null;
	private String encoding = "euc-kr";
	private String requestMethod = "GET";
	private Map<String, String> headerMap;
	private Map<String, String> cookieMap;
	private HTTPParameter parameter = new HTTPParameter();
	private String requestBody;

	private transient int connectTimeout = 1000;
	private transient int readTimeout = 3000;

	/**
	 * 기본 생성자
	 */
	public HttpConnectionInfo() {
		super();
	}

	/**
	 * 값을 넣는 생성자<br>
	 * GET 요청이라고 생각한다.
	 * 
	 * @param url
	 *            접속하려는 URL.
	 * @param connectTimeout
	 *            연결 타임아웃 시간. 단위는 millisecond.
	 * @param readTimeout
	 *            응답 타임아웃 시간. 단위는 millisecond.
	 */
	public HttpConnectionInfo(String url, int connectTimeout, int readTimeout) {
		super();
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/**
	 * 이 도메인을 특정지을 수 있는 ID 값을 반환한다.<br>
	 * ID 는 ssoUser 가 없는 경우 URL 이며,<br>
	 * ssoUser 가 있으면 URL 과 ssoUser 값을 조합한 값이다.<br>
	 * ID 값은 폴더 명 등에 사용되기 때문에,<br>
	 * URLEncode 하여 생성한다.
	 * 
	 * @return
	 */
	public String getId() {
		if (null == url) {
			return null;
		}
		String ssoUser = getSsoUser();
		try {
			return (null == ssoUser) ? URLEncoder.encode(url, "UTF-8")
					: URLEncoder.encode(url, "UTF-8") + "==="
							+ URLEncoder.encode(ssoUser, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * HttpConnectionInfo 도메인이 같은지 확인한다.<br>
	 * url 값과 ssoUser 값이 같으면 같은 도메인이라고 본다.
	 * 
	 * @param info
	 * @return
	 */
	public boolean equals(HttpConnectionInfo info) {
		if (null == info || null == url) {
			return false;
		}
		String ssoUser = getSsoUser();
		if (null == ssoUser) {
			return url.equals(info.getUrl()) && null == info.getSsoUser();
		}
		return url.equals(info.getUrl()) && ssoUser.equals(info.getSsoUser());
	}

	/**
	 * 요청 헤더 정보를 추가한다.<br>
	 * 기존에 등록된 키인 경우, 값을 덮어쓴다.
	 * 
	 * @param key
	 * @param value
	 */
	public void addHeader(String key, String value) {
		if (null == headerMap) {
			headerMap = new HashMap<String, String>();
		}
		headerMap.put(key, value);
	}

	/**
	 * req 값에서 헤더 정보를 찾아서 설정한다.
	 * 
	 * @param req
	 */
	@SuppressWarnings("unchecked")
	public void setHeaderInfo(HttpServletRequest req) {
		Enumeration<String> headerKeys = req.getHeaderNames();
		while (headerKeys.hasMoreElements()) {
			String key = headerKeys.nextElement();
			addHeader(key, req.getHeader(key));
		}
	}

	/**
	 * 요청 쿠키 정보를 추가한다.<br>
	 * 기존에 등록된 키인 경우, 값을 덮어쓴다.
	 * 
	 * @param key
	 * @param value
	 */
	public void addCookie(String key, String value) {
		if (null == cookieMap) {
			cookieMap = new HashMap<String, String>();
		}
		cookieMap.put(key, value);
	}

	/**
	 * cookie array 값을 이용하여 쿠키 정보를 세팅한다.
	 * 
	 * @param cookies
	 */
	public void setCookie(Cookie[] cookies) {
		if (null == cookies) {
			return;
		}
		for (Cookie cookie : cookies) {
			addCookie(cookie.getName(), cookie.getValue());
		}
	}

	/**
	 * 요청 파라메터 값을 추가한다.<br>
	 * 기존에 등록된 키인 경우, 값을 추가한다.
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, String value) {
		parameter.addParameter(name, value);
	}

	/**
	 * req 값에서 파라메터 정보를 찾아서 설정한다.
	 * 
	 * @param req
	 */
	@SuppressWarnings("unchecked")
	public void setParameterInfo(HttpServletRequest req) {
		Enumeration<String> parameterKeys = req.getParameterNames();
		while (parameterKeys.hasMoreElements()) {
			String key = parameterKeys.nextElement();
			for (String value : req.getParameterValues(key)) {
				addParameter(key, value);
			}
		}
	}

	/**
	 * ssoUser 값에 해당하는 쿠키값을 반환한다.
	 * 
	 * @return
	 */
	public String getSsoUser() {
		String ssoUser = null;
		if (null != cookieMap) {
			ssoUser = cookieMap.get(HttpConnectionUtil.COOKIE_NM_SSO_USER);
		}
		return ssoUser;
	}

	/**
	 * smSession 값에 해당하는 쿠키값을 반환한다.
	 * 
	 * @return
	 */
	public String getSmSession() {
		String smSession = null;
		if (null != cookieMap) {
			smSession = cookieMap.get(HttpConnectionUtil.COOKIE_NM_SMSESSION);
		}
		return smSession;
	}

	/**
	 * parameterMap 을 파싱하여 queryString 으로 만들어 반환한다
	 * 
	 * @return
	 */
	public String getQueryString() {
		return parameter.getQueryString(getEncoding());
	}

	/**
	 * 입력된 url 값에 queryString 값을 추가해서 반환한다.
	 * 
	 * @return
	 */
	public String getFullUrl() {
		String url = this.url;
		String queryString = getQueryString();
		if (queryString.length() > 0) {
			if (url.contains("?")) {
				url += "&" + queryString;
			} else {
				url += "?" + queryString;
			}
		}
		return url;
	}

	// 이하 getter, setter

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 설정된 인코딩 값을 반환한다.
	 * 
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 인코딩 값을 설정한다. 기본값은 UTF-8 이다.
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * 요청 방식을 설정한다. GET / POST
	 * 
	 * @param requestMethod
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public Map<String, String> getCookieMap() {
		return cookieMap;
	}

	public void setCookieMap(Map<String, String> cookieMap) {
		this.cookieMap = cookieMap;
	}

	public Map<String, List<String>> getParameterMap() {
		return parameter.getParameterMap();
	}

	public void setParameterMap(Map<String, List<String>> parameterMap) {
		parameter.setParameterMap(parameterMap);
	}

	/**
	 * 설정된 requestBody 값을 반환한다.<br>
	 * requestBody 값이 null 인 경우 queryString 값을 반환한다.
	 * 
	 * @return
	 */
	public String getRequestBody() {
		return null == requestBody ? getQueryString() : requestBody;
	}

	/**
	 * POST 요청시에 사용할 requsetBody 값을 설정한다.<br>
	 * requestBody 값을 설정할 경우, body 값에 맞는 적절한 Content-Type 헤더 값을 선언할 필요가 있다.<br>
	 * requestBody 값을 설정한 경우, parameter 값에 의한 queryString 값이 무시됨을 주의할 것.
	 * 
	 * @param requestBody
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
}
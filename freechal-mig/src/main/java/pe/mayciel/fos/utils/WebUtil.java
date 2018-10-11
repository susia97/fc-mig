package pe.mayciel.fos.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Web 통신에 관련된 Utility.
 * @author Hwang Seong-wook
 * @since 2013. 01. 17.
 * @version 1.0.0.1 (2013. 01. 17.)
 */
public class WebUtil {
	/**
	 * requestURL 값을 반환한다.<br>
	 * (프로토콜을 포함한 전체 URL (queryString 제외)<br>
	 * 호스트명의 경우, apache proxy 를 통과했으면 X-Forwarded-Host 값을 반환한다.
	 * 
	 * @return
	 * @see CommonUtil#getRequestURL()
	 * @see CommonUtil#getRequestURLAndQueryString(HttpServletRequest)
	 */
	public static String getRequestURL(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append(req.getScheme()).append("://").append(getHost(req)).append(
			req.getRequestURI());
		return sb.toString();
	}

	/**
	 * requestURL 과 queryString 값을 반환한다.<br>
	 * (요청받은 전체 string)<br>
	 * 호스트명의 경우, apache proxy 를 통과했으면 X-Forwarded-Host 값을 반환한다.
	 * 
	 * @param req
	 * @return
	 * @see CommonUtil#getRequestURLAndQueryString()
	 */
	public static String getRequestURLAndQueryString(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append(getRequestURL(req));
		if (StringUtils.isNotBlank(req.getQueryString())) {
			sb.append("?").append(req.getQueryString());
		}
		return sb.toString();
	}

	/**
	 * host 값을 반환한다.<br>
	 * apache proxy 를 통과했으면 X-Forwarded-Host 값을,<br>
	 * 그 외의 경우 header 의 Host 값을 반환한다.
	 * 
	 * @param req
	 * @return
	 */
	public static String getHost(HttpServletRequest req) {
		return CommonUtil.getNotBlankStr(req.getHeader("X-Forwarded-Host"),
			req.getHeader("x-forwarded-host"), req.getHeader("Host"));
	}

	/**
	 * remoteAddr 값을 반환한다.<br>
	 * apache proxy 를 통과한 경우, X-Forwarded-For 값을 반환한다.
	 * 
	 * @param req
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest req) {
		return CommonUtil.getNotBlankStr(req.getHeader("X-Forwarded-For"),
			req.getHeader("x-forwarded-for"), req.getRemoteAddr());
	}

	/**
	 * web 의 최상위 URL 을 반환한다.<br>
	 * ex. http://somesite.com/ or http://somesite.com/Context/<br>
	 * request 가 없는 경우 등 예외 발생 시 data.xml 의 path/defaultWebRootUrl 값을 반환한다.
	 * 
	 * @param req
	 * @return
	 */
	public static String getWebRootUrl(HttpServletRequest req) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(req.getScheme()).append("://").append(getHost(req)).append(
				req.getContextPath());
			if (!"/".equals(String.valueOf(sb.charAt(sb.length() - 1)))) {
				sb.append("/");
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * cookie 공유 등에서 사용하는, 호스트에서 공통적으로 사용하는 영역을 반환한다.<br>
	 * 호스트의 마지막 자리가 2글자인 경우 뒤에서 3구절을,<br>
	 * 마지막 자리가 3글자인 경우 뒤에서 2구절을 잘라서 반환한다.
	 * 
	 * @param req
	 * @return
	 */
	public static String getPrimaryDomain(HttpServletRequest req) {
		return getPrimaryDomainByHost(getHost(req));
	}

	/**
	 * cookie 공유 등에서 사용하는, 호스트에서 공통적으로 사용하는 영역을 반환한다.<br>
	 * 호스트의 마지막 자리가 2글자인 경우 뒤에서 3구절을,<br>
	 * 마지막 자리가 3글자인 경우 뒤에서 2구절을 잘라서 반환한다.
	 * 
	 * @param host
	 * @return
	 */
	private static String getPrimaryDomainByHost(String host) {
		if (StringUtils.isBlank(host)) {
			return null;
		}
		host = host.split(":")[0];
		String[] names = host.split("\\.");
		if (names.length < 2) {
			return null;
		}
		int cutLength = 3;
		if (names.length == 2 || names[names.length - 1].length() == 3) {
			cutLength = 2;
		}
		StringBuilder domain = new StringBuilder();
		for (int i = names.length - cutLength; i < names.length; i++) {
			if (domain.length() > 0) {
				domain.append(".");
			}
			domain.append(names[i]);
		}
		return domain.toString();
	}
}
package pe.mayciel.fos.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * 쿠키 처리를 하기 위한 유틸리티.
 *
 * @author Web Platform Development Team
 * @version $Rev: 13440 $, $Date: 2011-09-01 16:59:48 +0900 (2011-09-01, 목) $
 */
public class CookieUtils {

	/** The log. */
	protected static Logger log = LoggerFactory.getLogger(CookieUtils.class);

	/** The Constant COOKIE_DEFAULT_PATH. */
	public static final String COOKIE_DEFAULT_PATH = "/";

	/** The Constant COOKIE_DEFAULT_MAX_AGE. */
	public static final int COOKIE_DEFAULT_MAX_AGE = -1;

	/** The Constant COOKIE_DEFAULT_MIN_AGE. */
	public static final int COOKIE_DEFAULT_MIN_AGE = 0;

	/**
	 * Creates the cookie.
	 *
	 * @param name 이름
	 * @param value 값
	 * @param maxAge 쿠키의 최대 수명(단위:초)
	 * @param domain 쿠키가 표시될 도메인
	 * @param path 클라이언트가 쿠키를 반환해야 할 경로
	 * @return Cookie
	 * {@link Cookie}를 생성한다.
	 */
	public static Cookie createCookie(String name, String value, int maxAge,
		String domain, String path) {
		Cookie cookie = new Cookie(name, value);

		if (domain != null) {
			cookie.setDomain(domain);
		}

		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		return cookie;
	}

	/**
	 * Creates the cookie.
	 *
	 * @param name 이름
	 * @param value 값
	 * @param maxAge 쿠키의 최대 수명(단위:초)
	 * @param domain 쿠키가 표시될 도메인
	 * @return Cookie
	 * {@link Cookie}를 생성한다.
	 */
	public static Cookie createCookie(String name, String value, int maxAge,
		String domain) {
		return createCookie(name, value, maxAge, domain, COOKIE_DEFAULT_PATH);
	}

	/**
	 * Sets the cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param name 이름
	 * @param value 값
	 * @param maxAge 쿠키의 최대 수명(단위:초)
	 * @param domain 쿠키가 표시될 도메인
	 * @param path 클라이언트가 쿠키를 반환해야 할 경로
	 * @return Cookie
	 * {@link Cookie}를 생성한 후 {@link HttpServletResponse}에 저장한다.
	 */
	public static Cookie setCookie(HttpServletResponse response, String name,
		String value, int maxAge, String domain, String path) {
		Cookie cookie = createCookie(name, value, maxAge, domain, path);
		response.addCookie(cookie);

		if (log.isDebugEnabled()) {
			log.debug("CookieUtils.setCookie " + name + ":" + value
				+ " maxAge=" + maxAge + ",domain=" + domain + ",path=" + path);
		}

		return cookie;
	}

	/**
	 * Sets the cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param name 이름
	 * @param value 값
	 * @param maxAge 쿠키의 최대 수명(단위:초)
	 * @param domain 쿠키가 표시될 도메인
	 * @return Cookie
	 * {@link Cookie}를 생성한 후 {@link HttpServletResponse}에 저장한다.
	 */
	public static Cookie setCookie(HttpServletResponse response, String name,
		String value, int maxAge, String domain) {
		return setCookie(response, name, value, maxAge, domain,
			COOKIE_DEFAULT_PATH);
	}

	/**
	 * Sets the cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookie {@link Cookie} 객체
	 * @return Cookie
	 * {@link Cookie}를 생성한 후 {@link HttpServletResponse}에 저장한다.
	 */
	public static Cookie setCookie(HttpServletResponse response, Cookie cookie) {
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * Gets the cookie.
	 *
	 * @param request {@link HttpServletRequest} 객체
	 * @param name 쿠키 이름
	 * @return Cookie
	 * {@link HttpServletResponse}에 저장된 {@link Cookie}를 반환한다.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}

		Cookie cookies[] = request.getCookies();

		if (cookies == null) {
			return null;
		}
		// linear scan for the cookie.
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}

		return null;
	}

	/**
	 * Gets the cookie value.
	 *
	 * @param request {@link HttpServletRequest} 객체
	 * @param name 쿠키 이름
	 * @return String
	 * {@link HttpServletResponse}에 해당 이름에 대한 {@link Cookie}의 값을 반환한다.
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);

		if (cookie != null) {
			return cookie.getValue();
		}

		return null;
	}

	/**
	 * Gets the cookie names.
	 *
	 * @param request {@link HttpServletRequest} 객체
	 * @return String[]
	 * {@link HttpServletResponse}에 저장된 모든 {@link Cookie}의 이름을 반환한다.
	 */
	public static String[] getCookieNames(HttpServletRequest request) {
		ArrayList<String> cookieNames = new ArrayList<String>();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieNames.add(cookie.getName());
			}
		}

		return cookieNames.toArray(new String[cookieNames.size()]);
	}

	/**
	 * Gets the cookie names.
	 *
	 * @param request {@link HttpServletRequest} 객체
	 * @param filter String
	 * @return String[]
	 * {@link HttpServletRequest}에 저장된 {@link Cookie} 중에 <code>filter</code>로 시작하는
	 * 이름을 가진 {@link Cookie}의 이름을 반환한다.
	 */
	public static String[] getCookieNames(HttpServletRequest request,
		String filter) {
		ArrayList<String> cookieNames = new ArrayList<String>();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();

				if (cookieName.indexOf(filter) != -1) {
					cookieNames.add(cookieName);
				}
			}
		}

		return cookieNames.toArray(new String[cookieNames.size()]);
	}

	/**
	 * Checks if is exist cookie.
	 *
	 * @param request HttpServletRequest
	 * @param cookieName 쿠키 이름.
	 * @return isExist boolean
	 * {@link HttpServletRequest}에 지정된 이름을 가진 {@link Cookie}가 있는지 확인한다.
	 */
	public static boolean isExistCookie(HttpServletRequest request,
		String cookieName) {
		boolean isExist = false;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					isExist = true;
					break;
				}
			}
		}

		return isExist;
	}

	/**
	 * Invalidate cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookieName 이름
	 * @param domain 쿠키가 표시될 도메인
	 * @param path 클라이언트가 쿠키를 반환해야 할 경로
	 * {@link HttpServletResponse}에 저장된 {@link Cookie}를 제거한다.
	 */
	public static void invalidateCookie(HttpServletResponse response,
		String cookieName, String domain, String path) {
		setCookie(response, cookieName, null, COOKIE_DEFAULT_MIN_AGE, domain,
			path);
	}

	/**
	 * Invalidate cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookieName 쿠키 이름
	 * @param domain 쿠키가 표시될 도메인
	 * {@link HttpServletResponse}에 저장된 {@link Cookie}를 제거한다.
	 */
	public static void invalidateCookie(HttpServletResponse response,
		String cookieName, String domain) {
		invalidateCookie(response, cookieName, domain, COOKIE_DEFAULT_PATH);
	}

	/**
	 * Invalidate cookie.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookieName 쿠키 이름
	 * {@link HttpServletResponse}에 저장된 {@link Cookie}를 제거한다.
	 */
	public static void invalidateCookie(HttpServletResponse response,
		String cookieName) {
		invalidateCookie(response, cookieName, null, COOKIE_DEFAULT_PATH);
	}

	/**
	 * Invalidate cookies.
	 *
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookieNames 쿠키 이름들
	 * {@link HttpServletResponse}에 저장된 {@link Cookie}를 제거한다.
	 */
	public static void invalidateCookies(HttpServletResponse response,
		String... cookieNames) {
		if (ArrayUtils.isEmpty(cookieNames)) {
			return;
		}

		for (String cookieName : cookieNames) {
			invalidateCookie(response, cookieName);
		}
	}

	/**
	 * Invalidate all cookies.
	 *
	 * @param request {@link HttpServletRequest} 객체
	 * @param response {@link HttpServletResponse} 객체
	 * @param cookieDomain 쿠키가 표시될 도메인
	 * {@link HttpServletResponse}에 저장된 해당 도메인의 모든 {@link Cookie}를 제거한다.
	 */
	public static void invalidateAllCookies(HttpServletRequest request,
		HttpServletResponse response, String cookieDomain) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cooky : cookies) {
				invalidateCookie(response, cooky.getName(), cookieDomain);
			}
		}
	}
}
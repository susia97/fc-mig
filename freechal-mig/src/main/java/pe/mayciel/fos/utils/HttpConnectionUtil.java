package pe.mayciel.fos.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.UUID;

import org.apache.log4j.Logger;

import pe.mayciel.freechal.service.FreechalMigServiceLauncher;

/**
 * HTTP 통신을 통하여 text 혹은 XML Document 를 가져오는 util.
 * 
 * @author hwang
 * @since 2012. 12. 13.
 */
public class HttpConnectionUtil {
	/**
	 * sso_user 정보를 저장할 쿠키명
	 */
	public static final String COOKIE_NM_SSO_USER = "SSO_USER";
	/**
	 * smsession 정보를 저장할 쿠키명
	 */
	public static final String COOKIE_NM_SMSESSION = "SMSESSION";

	private static Logger logger = Logger.getLogger(HttpConnectionUtil.class);

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이며, 기본 타임아웃은 접속 10초, 응답 10초이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url) throws SocketTimeoutException,
			Exception {
		return getText(url, null, null);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이며, 기본 타임아웃은 접속 10초, 응답 10초이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @param ssoUser
	 *            SSO_USER 쿠키값. 보통 사번이다. 사용하지 않으면 null.
	 * @param smSession
	 *            SMSESSION 쿠키값. SSO 인증을 위한 값. 사용하지 않으면 null.
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url, String ssoUser, String smSession)
			throws SocketTimeoutException, Exception {
		return getText(url, ssoUser, smSession, 10000, 10000);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @param ssoUser
	 *            SSO_USER 쿠키값. 보통 사번이다. 사용하지 않으면 null.
	 * @param smSession
	 *            SMSESSION 쿠키값. SSO 인증을 위한 값. 사용하지 않으면 null.
	 * @param connectTimeout
	 *            연결 타임아웃 시간. 단위는 millisecond.
	 * @param readTimeout
	 *            응답 타임아웃 시간. 단위는 millisecond.
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url, String ssoUser, String smSession,
			int connectTimeout, int readTimeout) throws SocketTimeoutException,
			Exception {
		HttpConnectionInfo info = new HttpConnectionInfo(url, connectTimeout,
				readTimeout);
		info.addCookie(COOKIE_NM_SSO_USER, ssoUser);
		info.addCookie(COOKIE_NM_SMSESSION, smSession);
		return getText(info);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.
	 * 
	 * @param info
	 *            접속에 필요한 정보를 가지고 있는 도메인
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(HttpConnectionInfo info)
			throws SocketTimeoutException, Exception {
		if (null == info) {
			return null;
		}
		InputStream is = getInputStream(info, 0);
		if (is != null) {
			String result = is2str(is, info);
			if (result.contains("현재 커뮤니티 접속이 불안정합니다.<BR><BR>이용에 불편을 드려 죄송합니다.")) {
				System.out.print(".");
				return getText(info);
			}
			return result;
		}
		logger.warn("No data on this URL...");
		return null;
	}

	/**
	 * url 에 있는 파일을 로컬에 저장한다.
	 * 
	 * @param url
	 * @param saveDir
	 * @param fileNm
	 */
	public static String saveFile(String url, String saveDir, String fileNm)
			throws Exception {
		if (null == fileNm) {
			fileNm = UUID.randomUUID().toString();
		}
		File dir = new File(saveDir);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileNm);
		HttpConnectionInfo info = new HttpConnectionInfo();
		info.setUrl(url);
		info.addCookie("FUSR", FreechalMigServiceLauncher.fusr);
		info.addHeader("Referer", "http://community.freechal.com");
		InputStream is = getInputStream(info, 0);
		if (null == is) {
			return fileNm;
		}
		FileOutputStream fos = new FileOutputStream(file);

		byte[] by = new byte[1];
		while (is.read(by) > 0) {
			fos.write(by);
		}
		is.close();
		fos.close();
		return fileNm;
	}

	/**
	 * 접속 정보를 기반으로 접속하여 connection 을 반환한다.
	 * 
	 * @param info
	 * @return
	 * @throws MalformedURLException
	 *             URL 이 잘못된 경우
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	public static HttpURLConnection getConnection(HttpConnectionInfo info)
			throws MalformedURLException, SocketTimeoutException, IOException {
		if (null == info) {
			return null;
		}
		String url = info.getUrl();
		if ("GET".equals(info.getRequestMethod())) {
			url = info.getFullUrl();
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(info.getConnectTimeout());
		conn.setReadTimeout(info.getReadTimeout());
		conn.setRequestMethod(info.getRequestMethod());
		conn.setRequestProperty("Content-Language", info.getEncoding());
		if (null != info.getHeaderMap()) {
			for (String key : info.getHeaderMap().keySet()) {
				conn.setRequestProperty(key, info.getHeaderMap().get(key));
			}
		}
		if (null != info.getCookieMap()) {
			StringBuilder cookie = new StringBuilder();
			for (String key : info.getCookieMap().keySet()) {
				cookie.append(key).append("=")
						.append(info.getCookieMap().get(key)).append(";");
			}
			conn.setRequestProperty("Cookie", cookie.toString());
		}
		String requestBody = info.getRequestBody();
		if ("POST".equals(info.getRequestMethod()) && requestBody.length() > 0) {
			conn.setFixedLengthStreamingMode(requestBody.getBytes(info
					.getEncoding()).length);
			conn.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(
					conn.getOutputStream(), info.getEncoding());
			osw.write(requestBody);
			osw.flush();
			osw.close();
		}
		conn.connect();
		return conn;
	}

	/**
	 * 접속 정보를 기반으로 접속하여 inputStream 을 반환한다.<br>
	 * 접속이 실패하면 null 을 반환.<br>
	 * 반환된 inputStream 은 꼭 close 를 해주어야 한다.
	 * 
	 * @param info
	 * @return
	 * @throws MalformedURLException
	 *             URL 이 잘못된 경우
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	public static InputStream getInputStream(HttpConnectionInfo info,
			int retryCnt) throws MalformedURLException, SocketTimeoutException,
			IOException {
		if (null == info) {
			return null;
		}
		try {
			HttpURLConnection conn = getConnection(info);
			if (conn != null) {
				return conn.getInputStream();
			}
		} catch (Exception e) {
			// retry forever.
			if (retryCnt == 10) {
				logger.warn("retry conn for 10 time... url : " + info.getUrl());
			}
			if (retryCnt == 20) {
				logger.error("retry conn for 20 time. return null. url : "
						+ info.getUrl());
				return null;
			}
			retryCnt++;
			return getInputStream(info, retryCnt);
		}
		return null;
	}

	/**************************************************
	 * 
	 * Private Methods
	 * 
	 **************************************************/

	/**
	 * InputStream 을 string 으로 변환하여 반환한다.
	 * 
	 * @param is
	 * @param info
	 * @return
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	private static String is2str(InputStream is, HttpConnectionInfo info)
			throws IOException {
		if (null == is) {
			return null;
		}
		InputStreamReader isr = new InputStreamReader(is, info.getEncoding());
		char[] cbuf = new char[1];
		StringBuilder sb = new StringBuilder();
		while (isr.read(cbuf) > 0) {
			sb.append(cbuf);
		}
		isr.close();
		is.close();
		return sb.toString();
	}
}
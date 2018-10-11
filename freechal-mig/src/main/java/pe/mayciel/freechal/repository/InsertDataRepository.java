package pe.mayciel.freechal.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import pe.mayciel.fos.utils.ClassUtil;
import pe.mayciel.fos.utils.DateFormatUtil;
import pe.mayciel.fos.utils.HttpConnectionInfo;
import pe.mayciel.fos.utils.HttpConnectionUtil;
import pe.mayciel.fos.utils.NodeNameConvertor;
import pe.mayciel.fos.utils.NotColumn;
import pe.mayciel.fos.utils.ParseUtils;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.FreechalArtcl;
import pe.mayciel.freechal.domain.FreechalAtchfile;
import pe.mayciel.freechal.domain.FreechalCment;
import pe.mayciel.freechal.domain.SiteInfo;

/**
 * @author Hwang Seong-wook
 * @since 2013. 01. 25.
 */
@Repository
public class InsertDataRepository {
	private Logger logger = LoggerFactory.getLogger("mig");

	public void insertSiteInfo(SiteInfo info) throws Exception {
		executeQuery(getInsertQuery("freechal_site", info));
	}

	public void insertContentInfo(ContentInfo info) throws Exception {
		executeQuery(getInsertQuery("freechal_board_meta", info));
	}

	public void insertArtcl(FreechalArtcl artcl) throws Exception {
		replceBdyContImg(artcl);
		executeQuery(getInsertQuery("freechal_artcl", artcl));
	}

	public void insertCment(FreechalCment cment) throws Exception {
		executeQuery(getInsertQuery("freechal_cment", cment));
	}

	public void insertAtchfile(FreechalArtcl artcl, FreechalAtchfile atchFile)
			throws Exception {
		String pathSuffix = getFilePath(artcl);
		String filePath = "c:" + pathSuffix;
		String fileNm;
		if (ContentType.ALB.equals(artcl.getContentType())) {
			fileNm = "img-";
		} else {
			fileNm = "atch-";
		}
		fileNm += atchFile.getDocId() + "-" + atchFile.getSeq();
		HttpConnectionUtil.saveFile(atchFile.getSourceUrl(), filePath, fileNm);
		String newUrl = pathSuffix + "/" + fileNm;
		atchFile.setUrl(newUrl);
		executeQuery(getInsertQuery("freechal_atchfile", atchFile));
	}

	/**
	 * 본문 내용 중 이미지를 뽑아서 치환하고, 이미지를 저장한다.
	 * 
	 * @param artcl
	 * @throws Exception
	 */
	private void replceBdyContImg(FreechalArtcl artcl) throws Exception {
		String imgPattern = "<img ([^>]*)src=\"([^\"]*)\"([^>]*)>";
		Matcher mc = ParseUtils.getMatcher(imgPattern, artcl.getBdyCont());
		StringBuffer sb = new StringBuffer();
		while (mc.find()) {
			String url = mc.group(2);
			if (!url.contains(".freechal.com")) {
				continue;
			}
			String pathSuffix = getFilePath(artcl);
			String filePath = "c:" + pathSuffix;
			String fileNm = HttpConnectionUtil.saveFile(url, filePath, null);
			String newUrl = pathSuffix + "/" + fileNm.replace("%", "%25");
			mc.appendReplacement(sb, "<img src=\"" + newUrl + "\">");
		}
		mc.appendTail(sb);
		artcl.setBdyCont(sb.toString());
	}

	/**
	 * 파일을 저장할 경로를 반환한다.<br>
	 * 
	 * @param artcl
	 * @return /freechal/attach/grpId/contType-objSeq/docId
	 */
	private String getFilePath(FreechalArtcl artcl) {
		return "/freechal/attach/" + artcl.getInfo().getGrpId() + "/"
				+ artcl.getContentType() + artcl.getObjSeq() + "/"
				+ artcl.getDocId();
	}

	/**
	 * insert 구문을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tableName
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 */
	private String getInsertQuery(String tableName, Object obj)
			throws Exception {
		StringBuffer map = new StringBuffer();
		map.append("INSERT INTO ").append(tableName).append(" (\n\t");
		int i = 0;
		List<Field> fields = ClassUtil.getAllFields(obj.getClass());
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append(NodeNameConvertor.toUnderscore(field.getName()));
			i++;
		}
		map.append("\n) VALUES (\n\t");

		i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			addValue(map, obj, field);
			i++;
		}
		map.append("\n)\n");
		return map.toString();
	}

	private void addValue(StringBuffer map, Object obj, Field field)
			throws Exception {
		Class<?> valueClass = field.getType();
		Object fieldValue = ClassUtil.getFieldValue(obj, field.getName());
		if (null == fieldValue || valueClass.isPrimitive()) {
			map.append(fieldValue);
			return;
		}
		String val;
		if (Date.class.isInstance(fieldValue)) {
			val = DateFormatUtil.dateToStr(Date.class.cast(fieldValue),
					"yyyy-MM-dd HH:mm:ss");
		} else {
			val = fieldValue.toString().replace("\\", "\\\\")
					.replace("'", "\\'");
		}
		map.append("'").append(val).append("'");
	}

	/**
	 * field 가 query 생성에서 제외될 field 인지 여부를 반환한다.<br>
	 * {@link NotColumn} 이 선언되어 있거나, final 필드이면 제외한다.
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isSkipField(Field field) {
		return null != field.getAnnotation(NotColumn.class)
				|| Modifier.isFinal(field.getModifiers());
	}

	/**
	 * idx 에 따라 쉼표와 띄어쓰기, 줄바꿈 처리를 한다.
	 * 
	 * @param map
	 * @param idx
	 */
	private static void addSeparator(StringBuffer map, int idx) {
		if (idx == 0) {
			return;
		}
		map.append(",");
		if (idx % 5 == 0) {
			map.append("\n\t");
		} else {
			map.append(" ");
		}
	}

	/**
	 * 쿼리를 실행한다.
	 * 
	 * @param query
	 * @throws Exception
	 */
	private void executeQuery(String query) throws Exception {
		HttpConnectionInfo ci = new HttpConnectionInfo();
		ci.setUrl("http://snuaaa.net/freechal/insertSql.php");
		ci.addParameter("sqlStr", query);
		ci.setRequestMethod("POST");
		ci.setReadTimeout(10000);
		String result = HttpConnectionUtil.getText(ci);
		if (!"ok".equals(result)) {
			String resMsg;
			int start = result.indexOf("중복된 입력 값");
			if (start > 0) {
				resMsg = result.substring(start, result.indexOf("<br>", start));
				logger.error(resMsg + ", thread : {}", Thread.currentThread());
			} else {
				throw new Exception("Error on insert data! query : " + query
						+ ", resMsg : " + result);
			}
		}
	}
}
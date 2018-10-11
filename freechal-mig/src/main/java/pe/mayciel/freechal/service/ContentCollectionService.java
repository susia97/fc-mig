package pe.mayciel.freechal.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pe.mayciel.fos.utils.DateFormatUtil;
import pe.mayciel.fos.utils.HTTPParameter;
import pe.mayciel.fos.utils.HttpConnectionInfo;
import pe.mayciel.fos.utils.HttpConnectionUtil;
import pe.mayciel.fos.utils.ParseUtils;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.DocIdObj;
import pe.mayciel.freechal.domain.FreechalArtcl;
import pe.mayciel.freechal.domain.FreechalAtchfile;
import pe.mayciel.freechal.domain.FreechalCment;

@Service
public class ContentCollectionService {
	private Logger logger = LoggerFactory.getLogger("mig");

	/**
	 * 해당 페이지의 답글을 제외한 게시글의 docId 목록을 반환한다.
	 * 
	 * @param info
	 * @param page
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public List<DocIdObj> getDocIdListByPage(ContentInfo info, int page)
			throws SocketTimeoutException, Exception {
		Source source = getSource(getListUrl(info, page));
		switch (info.getContentType()) {
		case NTC:
		case BBS:
		case EBS:
		case ABS:
		case PDS:
			return getDocIdListForBbs(source, page);
		case ALB:
			return getDocIdListForAlbum(source, page);
		}
		return null;
	}

	/**
	 * 게시판 목록 url 을 반환한다.
	 * 
	 * @param info
	 * @param page
	 * @return
	 */
	private String getListUrl(ContentInfo info, int page) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://community.freechal.com/ComService/Activity/")
				.append(info.getContentType().getListUrl()).append("?GrpId=")
				.append(info.getGrpId()).append("&ObjSeq=")
				.append(info.getObjSeq()).append("&PageNo=").append(page);
		return sb.toString();
	}

	/**
	 * 게시글 타입인 경우의 목록 html 에서 개별 글들의 docId 값을 찾아서 list 로 반환한다.
	 * 
	 * @param source
	 * @param page
	 * @return
	 */
	private List<DocIdObj> getDocIdListForBbs(Source source, int page) {
		List<DocIdObj> result = new ArrayList<DocIdObj>();
		List<Element> tables = source.getElementById("BoardTdList")
				.getAllElements("table");
		Element table = null;
		for (Element ele : tables) {
			if (null == ele.getAttributeValue("class")) {
				table = ele;
				break;
			}
		}

		for (Element tr : table.getAllElements("tr")) {
			Element atag = tr.getFirstElement("a");
			if (null == atag) {
				continue;
			}
			String href = atag.getAttributeValue("href");
			HTTPParameter param = new HTTPParameter(href.split("\\?")[1],
					"euc-kr");
			long docId = Long.parseLong(param.getParameter("DocId"));
			DocIdObj obj = new DocIdObj();
			obj.setDocId(docId);
			for (Element img : tr.getAllElements("img")) {
				if (img.getAttributeValue("src").contains("ico_re.gif")) {
					obj.setReply(true);
					break;
				}
			}
			result.add(obj);
		}
		if (result.size() != 50) {
			logger.warn("id list size isn't 50. size : {}, page : {}",
					result.size(), page);
		}
		return result;
	}

	/**
	 * 앨범 타입인 경우의 목록 html 에서 개별 글들의 docId 값을 찾아서 list 로 반환한다.
	 * 
	 * @param source
	 * @param page
	 * @return
	 */
	private List<DocIdObj> getDocIdListForAlbum(Source source, int page) {
		List<DocIdObj> result = new ArrayList<DocIdObj>();
		Element table = source.getElementById("AlbumTdList").getFirstElement(
				"table");

		for (Element td : table.getAllElementsByClass("img")) {
			Element atag = td.getFirstElement("a");
			if (null == atag) {
				continue;
			}
			String href = atag.getAttributeValue("href");
			HTTPParameter param = new HTTPParameter(href.split("\\?")[1],
					"euc-kr");
			long docId = Long.parseLong(param.getParameter("SeqNo"));
			DocIdObj obj = new DocIdObj();
			obj.setDocId(docId);
			result.add(obj);
		}
		if (result.size() != 40) {
			logger.warn("id list size isn't 40. size : {}, page : {}",
					result.size(), page);
		}
		return result;
	}

	/**
	 * 게시글 정보를 가져온다.
	 * 
	 * @param info
	 * @param docId
	 * @param parentArtcl
	 * @return
	 * @throws Exception
	 */
	public FreechalArtcl getArtclData(ContentInfo info, long docId,
			FreechalArtcl parentArtcl) throws Exception {
		String url = getArtclUrl(info, docId);
		FreechalArtcl artcl = new FreechalArtcl();
		artcl.setInfo(info);
		if (ContentType.ALB.equals(info.getContentType())) {
			docId = 1000000l * info.getGrpId() + 10000 * info.getObjSeq()
					+ docId;
		}
		artcl.setDocId(docId);
		if (null == parentArtcl) {
			artcl.setSprrDocId(docId);
			artcl.setDocIdList(String.valueOf(docId));
		} else {
			artcl.setSprrDocId(parentArtcl.getSprrDocId());
			artcl.setParentDocId(parentArtcl.getDocId());
			artcl.setDocIdList(parentArtcl.getDocIdList() + ";" + docId);
			artcl.setLevel(parentArtcl.getLevel() + 1);
		}

		Source source = getSource(url);
		switch (info.getContentType()) {
		case NTC:
		case BBS:
		case EBS:
		case ABS:
			setContentsForBbs(artcl, source);
			setCmentForBbs(artcl, source);
			setChildArtclForBbs(artcl, source);
			break;
		case ALB:
			setAtchForAlb(artcl, source);
			setContentsForAlb(artcl, source);
			setCmentForBbs(artcl, source);
			break;
		case PDS:
			setAtchForPds(artcl, source);
			setContentsForBbs(artcl, source);
			setCmentForBbs(artcl, source);
			setChildArtclForBbs(artcl, source);
			break;
		}
		artcl.setCmentCnt(artcl.getCmentList().size());
		artcl.setAtchCnt(artcl.getAtchFileList().size());
		return artcl;
	}

	private Source getSource(String url) throws MalformedURLException,
			IOException {
		HttpConnectionInfo info = new HttpConnectionInfo();
		info.setUrl(url);
		info.addCookie("FUSR", FreechalMigServiceLauncher.fusr);
		info.addHeader("Referer", "http://community.freechal.com");
		InputStream is = HttpConnectionUtil.getInputStream(info, 0);
		Source source = new Source(is);
		if (source.toString().contains(
				"현재 커뮤니티 접속이 불안정합니다.<BR><BR>이용에 불편을 드려 죄송합니다.")) {
			System.out.print(".");
			return getSource(url);
		}
		return source;
	}

	/**
	 * 게시글 url 을 반환한다.
	 * 
	 * @param info
	 * @param docId
	 * @param page
	 * @return
	 */
	private String getArtclUrl(ContentInfo info, long docId) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://community.freechal.com/ComService/Activity/")
				.append(info.getContentType().getArtclUrl()).append("?GrpId=")
				.append(info.getGrpId()).append("&ObjSeq=")
				.append(info.getObjSeq());
		if (ContentType.ALB.equals(info.getContentType())) {
			sb.append("&SeqNo=");
		} else {
			sb.append("&DocId=");
		}
		sb.append(docId);
		return sb.toString();
	}

	private void setCmentForBbs(FreechalArtcl artcl, Source source)
			throws Exception {
		Element cmentTable = source.getFirstElementByClass("CommentList");
		if (null == cmentTable) {
			return;
		}
		int seq = 1;
		for (Element tr : cmentTable.getAllElements("tr")) {
			FreechalCment cment = new FreechalCment();
			cment.setInfo(artcl.getInfo());
			cment.setDocId(artcl.getDocId());

			Element writerTd = tr.getFirstElementByClass("nicname");
			Element writerSpan = writerTd.getFirstElement("span");
			if (null == writerSpan) {
				cment.setWriterId("Guest");
				cment.setWriterNm(writerTd.getTextExtractor().toString().trim());
			} else {
				String mbrInfoStr = writerSpan.getAttributeValue("onclick");
				String span = "initMenu\\('([^']*)','([^']*)','([^']*)','([^']*)'([^;]*);";
				Matcher mc = ParseUtils.getMatcher(span, mbrInfoStr);
				if (mc.find()) {
					cment.setWriterId(mc.group(3));
					cment.setWriterNm(mc.group(4));
				}
			}

			Element cmtxt = tr.getFirstElementByClass("cmtxt");
			String cm = cmtxt.getContent().toString();
			cment.setCont(cm.substring(0, cm.indexOf("<span")).trim());
			Element daySpan = cmtxt.getFirstElementByClass("day");

			String datePattern;
			switch (artcl.getContentType()) {
			case BBS:
			case ABS:
				datePattern = "yyyy-MM-dd HH:mm:ss";
				break;
			default:
				datePattern = "yyyy/MM/dd HH:mm";
			}

			cment.setDate(DateFormatUtil.strToDate(daySpan.getTextExtractor()
					.toString().trim(), datePattern));

			cment.setSeq(seq++);
			artcl.getCmentList().add(cment);
		}
	}

	private void setChildArtclForBbs(FreechalArtcl artcl, Source source)
			throws Exception {
		Element div = source.getElementById("prev-next");
		if (null == div) {
			return;
		}
		boolean findMe = false;
		for (Element tr : div.getAllElements("tr")) {
			String blankImg = "<img src=\"([^\"]*)blank\\.gif\"([^>]*)>";
			String reImg = "<img src=\"([^\"]*)ico_re\\.gif\"([^>]*)>";
			int level = ParseUtils.getMatchCnt(blankImg, tr.toString())
					+ ParseUtils.getMatchCnt(reImg, tr.toString());
			if (level < artcl.getLevel() || level > artcl.getLevel() + 1) {
				continue;
			}
			if (findMe && level == artcl.getLevel()) {
				break;
			}
			String href = tr.getFirstStartTag("a").getAttributeValue("href");
			HTTPParameter param = new HTTPParameter(href.split("\\?")[1],
					"euc-kr");
			long docId = Long.parseLong(param.getParameter("DocId"));
			if (docId == artcl.getDocId()) {
				findMe = true;
				continue;
			}
			if (findMe && level == artcl.getLevel() + 1) {
				artcl.getChildArtcl().add(
						getArtclData(artcl.getInfo(), docId, artcl));
			}
		}
	}

	private void setAtchForAlb(FreechalArtcl artcl, Source source) {
		Element table = source.getElementById("view-content");
		int seq = 1;
		for (Element td : table.getAllElementsByClass("cont-img")) {
			FreechalAtchfile atch = new FreechalAtchfile();
			atch.setInfo(artcl.getInfo());
			atch.setDocId(artcl.getDocId());
			atch.setSeq(seq++);
			atch.setAtchType("IMG");
			Element img = td.getFirstElement("img");
			String src = img.getAttributeValue("src");
			atch.setSourceUrl(src);
			HTTPParameter param = new HTTPParameter(src.split("\\?")[1],
					"euc-kr");
			atch.setFileNm(param.getParameter("file"));
			artcl.getAtchFileList().add(atch);
		}
	}

	private void setContentsForAlb(FreechalArtcl artcl, Source source)
			throws ParseException {
		setCommonContentInfo(artcl, source);
		Element docCont = source.getElementById("container");
		artcl.setBdyCont(docCont.getContent().toString().trim());
	}

	private void setAtchForPds(FreechalArtcl artcl, Source source) {
		Element div = source.getFirstElementByClass("attachments_file");
		if (null == div) {
			return;
		}
		List<Element> liList = div.getAllElements("li");
		int seq = 1;
		for (Element li : liList) {
			FreechalAtchfile atch = new FreechalAtchfile();
			atch.setInfo(artcl.getInfo());
			atch.setDocId(artcl.getDocId());
			atch.setSeq(seq++);
			atch.setAtchType("AT");
			Element atag = li.getFirstElement("a");
			String href = atag.getAttributeValue("href");
			atch.setSourceUrl(href);
			atch.setFileNm(atag.getTextExtractor().toString());
			TextExtractor ex = new TextExtractor(li) {
				@Override
				public boolean excludeElement(StartTag startTag) {
					return startTag.getName().equals("a");
				}
			};
			atch.setFileSzTxt(ex.toString());

			artcl.getAtchFileList().add(atch);
		}
	}

	private void setContentsForBbs(FreechalArtcl artcl, Source source)
			throws ParseException {
		setCommonContentInfo(artcl, source);
		Element docCont = source.getElementById("DocContent");
		artcl.setBdyCont(docCont.getContent().toString().trim());
	}

	private void setCommonContentInfo(FreechalArtcl artcl, Source source)
			throws ParseException {
		Element title = source.getFirstElementByClass("td_title");
		artcl.setTitle(title.getTextExtractor().toString().trim());

		Element date = source.getFirstElementByClass("td_date");
		String dateStr = date.getTextExtractor().toString().trim();
		artcl.setDate(DateFormatUtil
				.strToDate(dateStr, "yyyy-MM-dd a hh:mm:ss"));

		Element writerTd = source.getFirstElementByClass("td_writer");
		Element writerSpan = writerTd.getFirstElement("span");
		if (null == writerSpan) {
			artcl.setWriterId("Guest");
			if (ContentType.ABS.equals(artcl.getContentType())) {
				artcl.setWriterNm(writerTd.getFirstElement("div")
						.getTextExtractor().toString().trim());
			} else {
				Element mInfo = source.getElementById("MemberInfoBox")
						.getFirstElementByClass("Minfo");
				String writerNm = mInfo.getTextExtractor().toString().trim()
						.replace("&nbsp;", "");
				writerNm = writerNm.substring(0, writerNm.length() - 1);
				artcl.setWriterNm(writerNm);
			}
		} else {
			String mbrInfoStr = writerSpan.getAttributeValue("onclick");
			String span = "initMenu\\('([^']*)','([^']*)','([^']*)','([^']*)'([^;]*);";
			Matcher mc = ParseUtils.getMatcher(span, mbrInfoStr);
			if (mc.find()) {
				artcl.setWriterId(mc.group(3));
				artcl.setWriterNm(mc.group(4));
			}
		}

		Element hitSpan = source.getElementById("td_hit").getFirstElement(
				"span");
		artcl.setReadCnt(Integer.parseInt(hitSpan.getTextExtractor().toString()
				.trim()));
	}
}
package pe.mayciel.freechal.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.mayciel.fos.junit.AbstractTestCaseRunWithSpring;
import pe.mayciel.fos.utils.ParseUtils;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.DocIdObj;
import pe.mayciel.freechal.domain.FreechalArtcl;

public class ContentCollectionServiceTest extends AbstractTestCaseRunWithSpring {
	@Autowired
	private ContentCollectionService service;

	private ContentInfo info = new ContentInfo();

	@Before
	public void before() {
		info.setGrpId(406762);
		info.setContentType(ContentType.BBS);
		info.setObjSeq(1);
	}

	@Test
	public void getDocIdListForBbs() throws Exception {
		List<DocIdObj> list = service.getDocIdListByPage(info, 85);
		assertTrue(list.size() > 0);
		System.out.println(list.size());
	}

	@Test
	public void getDocIdListForAlbum() throws Exception {
		info.setContentType(ContentType.ALB);
		List<DocIdObj> list = service.getDocIdListByPage(info, 2);
		for (DocIdObj obj : list) {
			System.out.println(obj.getDocId() + ", " + obj.isReply());
		}
		assertTrue(list.size() > 0);
	}

	@Test
	public void getDocIdListForPds() throws Exception {
		info.setContentType(ContentType.PDS);
		List<DocIdObj> list = service.getDocIdListByPage(info, 5);
		for (DocIdObj obj : list) {
			System.out.println(obj.getDocId() + ", " + obj.isReply());
		}
		assertTrue(list.size() > 0);
	}

	@Test
	public void getArtclData() throws Exception {
		info.setGrpId(167165);
		info.setObjSeq(1);
		int docId = 133716648;
		FreechalArtcl artcl = service.getArtclData(info, docId, null);
		System.out.println("ttl : " + artcl.getTitle());
		System.out.println("wi : " + artcl.getWriterId());
		System.out.println("wn : " + artcl.getWriterNm());
		System.out.println(artcl.getCmentCnt());
		System.out.println(artcl.getCmentList().size());
	}

	@Test
	public void getPdsData() throws Exception {
		info.setGrpId(406762);
		info.setObjSeq(1);
		info.setContentType(ContentType.PDS);
		int docId = 22678906;
		FreechalArtcl artcl = service.getArtclData(info, docId, null);
		System.out.println("ttl : " + artcl.getTitle());
		System.out.println(artcl.getCmentCnt());
		System.out.println(artcl.getCmentList().size());
	}

	@Test
	public void getAlbData() throws Exception {
		info.setGrpId(406762);
		info.setObjSeq(1);
		info.setContentType(ContentType.ALB);
		int docId = 244;
		FreechalArtcl artcl = service.getArtclData(info, docId, null);
		System.out.println("ttl : " + artcl.getTitle());
		System.out.println("getDate : " + artcl.getDate());
		System.out.println("writerId : " + artcl.getWriterId());
		System.out.println("writerId : " + artcl.getWriterNm());
		System.out.println("getReadCnt : " + artcl.getReadCnt());
		System.out.println(artcl.getCmentCnt());
		System.out.println(artcl.getCmentList().size());
	}

	@Test
	public void t() throws Exception {
		String h = "<a href=\"javascript:DeleteReply(9,'cyanblue');\">";
		String cmentIdxPt = "<a ([^>]*)javascript([^0-9]*)([0-9]+)([^>]*)>";
		Matcher mc = ParseUtils.getMatcher(cmentIdxPt, h);
		System.out.println(mc.matches());
		if (mc.matches()) {
			System.out.println(Integer.parseInt(mc.group(3)));
		}
	}
}
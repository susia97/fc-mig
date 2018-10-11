package pe.mayciel.freechal.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.mayciel.fos.junit.AbstractTestCaseRunWithSpring;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.FreechalArtcl;

public class ContentInsertServiceTest extends AbstractTestCaseRunWithSpring {
	@Autowired
	private ContentInsertService service;
	@Autowired
	private ContentCollectionService collectionService;

	@Test
	public void insertArtcl() throws Exception {
		ContentInfo info = new ContentInfo();
		info.setGrpId(544357);
		info.setContentType(ContentType.BBS);
		info.setObjSeq(6);
		int docId = 81900012;
		FreechalArtcl artcl = collectionService.getArtclData(info, docId, null);
		service.insertArtcl(artcl);
	}

}

package pe.mayciel.freechal.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.mayciel.fos.junit.AbstractTestCaseRunWithSpring;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.FreechalArtcl;
import pe.mayciel.freechal.service.ContentCollectionService;

public class InsertDataRepositoryTest extends AbstractTestCaseRunWithSpring {
	@Autowired
	private InsertDataRepository repository;
	@Autowired
	private ContentCollectionService service;

	// @Test
	public void insertContentInfo() throws Exception {
		ContentInfo info = new ContentInfo(406762, 1, ContentType.BBS, 1,
				"천기누설 III", 1, 100);
		repository.insertContentInfo(info);
	}

	@Test
	public void insertArtcl() throws Exception {
		ContentInfo info = new ContentInfo();
		info.setGrpId(406762);
		info.setContentType(ContentType.BBS);
		info.setObjSeq(1);
		int docId = 124367173;
		FreechalArtcl artcl = service.getArtclData(info, docId, null);
		repository.insertArtcl(artcl);
	}
}
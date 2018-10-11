package pe.mayciel.freechal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.FreechalArtcl;
import pe.mayciel.freechal.domain.FreechalAtchfile;
import pe.mayciel.freechal.domain.FreechalCment;
import pe.mayciel.freechal.domain.SiteInfo;
import pe.mayciel.freechal.repository.InsertDataRepository;

@Service
public class ContentInsertService {
	@Autowired
	private InsertDataRepository repository;

	/**
	 * 사이트 정보를 저장한다.
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void insertSiteInfo(SiteInfo info) throws Exception {
		repository.insertSiteInfo(info);
	}

	/**
	 * 게시판 정보를 저장한다.
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void insertContentInfo(ContentInfo info) throws Exception {
		repository.insertContentInfo(info);
	}

	/**
	 * 게시글 정보를 저장한다.
	 * 
	 * @param artcl
	 * @throws Exception
	 */
	public void insertArtcl(FreechalArtcl artcl) throws Exception {
		repository.insertArtcl(artcl);
		for (FreechalAtchfile atchFile : artcl.getAtchFileList()) {
			repository.insertAtchfile(artcl, atchFile);
		}
		for (FreechalCment cment : artcl.getCmentList()) {
			repository.insertCment(cment);
		}
		for (FreechalArtcl childArtcl : artcl.getChildArtcl()) {
			insertArtcl(childArtcl);
		}
	}
}
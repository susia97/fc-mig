package pe.mayciel.freechal.domain;

public enum ContentType {
	/** 공지사항 */
	NTC("Notice/CsNoticeList.asp", "Notice/CsNoticeContent.asp"),
	/** 게시판 */
	BBS("BBS/CsBBSList.asp", "BBS/CsBBSContent.asp"),
	/** 추천게시판 */
	EBS("EstimBBS/CsBBSList.asp", "EstimBBS/CsBBSContent.asp"),
	/** 익명 게시판 */
	ABS("ABBS/CsBBSList.asp", "ABBS/CsBBSContent.asp"),
	/** 자료실 */
	PDS("PDS/CsPDSList.asp", "PDS/CsPDSContent.asp"),
	/** 앨범 */
	ALB("Album/CsPhotoList.asp", "Album/CsPhotoView.asp");

	private String listUrl;
	private String artclUrl;

	ContentType(String listUrl, String artclUrl) {
		this.listUrl = listUrl;
		this.artclUrl = artclUrl;
	}

	public String getListUrl() {
		return listUrl;
	}

	public String getArtclUrl() {
		return artclUrl;
	}
}
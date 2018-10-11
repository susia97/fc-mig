package pe.mayciel.freechal.domain;

import pe.mayciel.fos.utils.NotColumn;

public class ContentInfo {
	private int grpId;
	private int objSeq;
	private ContentType contentType;
	private int dispSeq;
	private String boardName;

	@NotColumn
	private int startPage;
	@NotColumn
	private int endPage;

	public ContentInfo() {
	}

	public ContentInfo(int grpId, int objSeq, ContentType contentType,
			int dispSeq, String boardName, int startPage, int endPage) {
		this.grpId = grpId;
		this.objSeq = objSeq;
		this.contentType = contentType;
		this.dispSeq = dispSeq;
		this.boardName = boardName;
		this.startPage = startPage;
		this.endPage = endPage;
	}

	public int getGrpId() {
		return grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	public int getObjSeq() {
		return objSeq;
	}

	public void setObjSeq(int objSeq) {
		this.objSeq = objSeq;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public int getDispSeq() {
		return dispSeq;
	}

	public void setDispSeq(int dispSeq) {
		this.dispSeq = dispSeq;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
}
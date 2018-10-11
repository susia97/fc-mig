package pe.mayciel.freechal.domain;

import pe.mayciel.fos.utils.NotColumn;

public class FreechalAtchfile {
	@NotColumn
	private ContentInfo info;
	private int grpId;
	private int objSeq;
	private ContentType contentType;

	private long docId;
	private int seq;
	/**
	 * 기존 프리챌의 url
	 */
	@NotColumn
	private String sourceUrl;
	private String url;
	private String fileNm;
	private String fileSzTxt;
	// AT / IMG
	private String atchType;

	public ContentInfo getInfo() {
		return info;
	}

	public void setInfo(ContentInfo info) {
		this.info = info;
		this.grpId = info.getGrpId();
		this.objSeq = info.getObjSeq();
		this.contentType = info.getContentType();
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

	public long getDocId() {
		return docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getFileSzTxt() {
		return fileSzTxt;
	}

	public void setFileSzTxt(String fileSzTxt) {
		this.fileSzTxt = fileSzTxt;
	}

	public String getAtchType() {
		return atchType;
	}

	public void setAtchType(String atchType) {
		this.atchType = atchType;
	}
}
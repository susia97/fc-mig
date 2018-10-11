package pe.mayciel.freechal.domain;

import java.util.Date;

import pe.mayciel.fos.utils.NotColumn;

public class FreechalCment {
	@NotColumn
	private ContentInfo info;
	private int grpId;
	private int objSeq;
	private ContentType contentType;

	private long docId;
	private int seq;
	private String writerId;
	private String writerNm;
	private String cont;
	private Date date;

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

	public String getWriterId() {
		return writerId;
	}

	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	public String getWriterNm() {
		return writerNm;
	}

	public void setWriterNm(String writerNm) {
		this.writerNm = writerNm;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
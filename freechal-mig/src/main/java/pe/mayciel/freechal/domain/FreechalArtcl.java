package pe.mayciel.freechal.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.mayciel.fos.utils.NotColumn;

public class FreechalArtcl {
	@NotColumn
	private ContentInfo info;
	private int grpId;
	private int objSeq;
	private ContentType contentType;

	private long docId;
	private long sprrDocId;
	private long parentDocId;
	private String docIdList;
	private int level = 0;

	private String title;
	private Date date;
	private String writerId;
	private String writerNm;
	private int readCnt;
	private String bdyCont;
	private int cmentCnt = 0;
	private int atchCnt = 0;

	@NotColumn
	private List<FreechalArtcl> childArtcl = new ArrayList<FreechalArtcl>();
	@NotColumn
	private List<FreechalCment> cmentList = new ArrayList<FreechalCment>();
	@NotColumn
	private List<FreechalAtchfile> atchFileList = new ArrayList<FreechalAtchfile>();

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

	public long getSprrDocId() {
		return sprrDocId;
	}

	public void setSprrDocId(long sprrDocId) {
		this.sprrDocId = sprrDocId;
	}

	public long getParentDocId() {
		return parentDocId;
	}

	public void setParentDocId(long parentDocId) {
		this.parentDocId = parentDocId;
	}

	public String getDocIdList() {
		return docIdList;
	}

	public void setDocIdList(String docIdList) {
		this.docIdList = docIdList;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public int getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}

	public String getBdyCont() {
		return bdyCont;
	}

	public void setBdyCont(String bdyCont) {
		this.bdyCont = bdyCont;
	}

	public int getCmentCnt() {
		return cmentCnt;
	}

	public void setCmentCnt(int cmentCnt) {
		this.cmentCnt = cmentCnt;
	}

	public int getAtchCnt() {
		return atchCnt;
	}

	public void setAtchCnt(int atchCnt) {
		this.atchCnt = atchCnt;
	}

	public List<FreechalArtcl> getChildArtcl() {
		return childArtcl;
	}

	public void setChildArtcl(List<FreechalArtcl> childArtcl) {
		this.childArtcl = childArtcl;
	}

	public List<FreechalCment> getCmentList() {
		return cmentList;
	}

	public void setCmentList(List<FreechalCment> cmentList) {
		this.cmentList = cmentList;
	}

	public List<FreechalAtchfile> getAtchFileList() {
		return atchFileList;
	}

	public void setAtchFileList(List<FreechalAtchfile> atchFileList) {
		this.atchFileList = atchFileList;
	}
}
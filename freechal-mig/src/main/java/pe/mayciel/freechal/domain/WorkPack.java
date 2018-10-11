package pe.mayciel.freechal.domain;

import java.util.List;

public class WorkPack {
	private ContentInfo info;
	private int page;
	private List<DocIdObj> idList;
	private boolean readOnly = false;
	
	public ContentInfo getInfo() {
		return info;
	}
	public void setInfo(ContentInfo info) {
		this.info = info;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<DocIdObj> getIdList() {
		return idList;
	}
	public void setIdList(List<DocIdObj> idList) {
		this.idList = idList;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
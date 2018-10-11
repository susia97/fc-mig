package pe.mayciel.freechal.domain;

public class DocIdObj {
	private long docId;
	private boolean isReply = false;

	public long getDocId() {
		return docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}

	public boolean isReply() {
		return isReply;
	}

	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}
}
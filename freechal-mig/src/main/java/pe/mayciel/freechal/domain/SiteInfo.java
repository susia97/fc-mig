package pe.mayciel.freechal.domain;

public class SiteInfo {
	private int grpId;
	private String grpName;
	private String grpAlias;
	private String sinceStr;

	public SiteInfo() {
	}

	public SiteInfo(int grpId, String grpName, String grpAlias, String sinceStr) {
		this.grpId = grpId;
		this.grpName = grpName;
		this.grpAlias = grpAlias;
		this.sinceStr = sinceStr;
	}

	public int getGrpId() {
		return grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	public String getGrpName() {
		return grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public String getGrpAlias() {
		return grpAlias;
	}

	public void setGrpAlias(String grpAlias) {
		this.grpAlias = grpAlias;
	}

	public String getSinceStr() {
		return sinceStr;
	}

	public void setSinceStr(String sinceStr) {
		this.sinceStr = sinceStr;
	}
}
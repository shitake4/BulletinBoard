package BulletinBoard;

public class ResData {
	String threadId = null;
	String resId = null;
	String deleteId = null;
	String contributorName = null;
	String resContent = null;
	String cookieId =  null;
	String date = null;

	public String getContributorName() {
		return contributorName;
	}
	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}
	public String getResContent() {
		return resContent;
	}
	public void setResContent(String resContent) {
		this.resContent = resContent;
	}
	public String getDeleteId() {
		return deleteId;
	}
	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}
	public String getCookieId() {
		return cookieId;
	}
	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

}

package BulletinBoard;

public class ThreadData {
	String threadId      = null;
	String threadTitle   = null;
	String threadCreater = null;
	String threadContent = null;
	String deleteId      = null;
	String cookieId      = null;
	String date          = null;
	java.sql.Date sqlDate= null;
	
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getThreadTitle() {
		return threadTitle;
	}
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	public String getThreadCreater() {
		return threadCreater;
	}
	public void setThreadCreater(String threadCreater) {
		this.threadCreater = threadCreater;
	}
	public String getThreadContent() {
		return threadContent;
	}
	public void setThreadContent(String threadContent) {
		this.threadContent = threadContent;
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
	public java.sql.Date getSqlDate() {
		return sqlDate;
	}
	public void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}
	
	
}

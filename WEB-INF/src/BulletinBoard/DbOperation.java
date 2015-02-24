package BulletinBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbOperation {
	static Connection conn = null;
	static{
		//ドライバーのダウンロード
			 try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void getConnection (){
		//データベースへの接続
		String url = "jdbc:mysql://localhost/bulletinbord";
		String user = "root";
		String pass = "root";
			try {
				conn = DriverManager.getConnection(url,user,pass);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void insertThread(String thCreater,String thTitle,String thContent,int deleteId,String cookieId,java.sql.Date sqlDate) throws SQLException{
		getConnection();
		String sqlStr = "INSERT INTO thread (thread_title,thread_creater,thread_content,delete_id,cookie_id,date) VALUES(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setString(1, thCreater);
		pstmt.setString(2, thTitle);
		pstmt.setString(3, thContent);
		pstmt.setInt(4, deleteId);
		pstmt.setString(5, cookieId);
		pstmt.setDate(6, sqlDate);
		pstmt.executeUpdate();
		conn.close();
		return;
	}
	
	public static void insertRes(int threadId,String contributorName,java.sql.Date sqlDate,String cookieId,int deleteId,String resContent) throws SQLException{
		getConnection();
		String sqlStr = "INSERT INTO res (thread_id,contributor_name,date,cookie_id,delete_id,res_content) VALUES(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, threadId);
		pstmt.setString(2, contributorName);
		pstmt.setDate(3, sqlDate);
		pstmt.setString(4, cookieId);
		pstmt.setInt(5, deleteId);
		pstmt.setString(6, resContent);
		pstmt.executeUpdate();
		pstmt.close();
		return;
	}

	public static ArrayList<ThreadData> selectAllThread() throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM thread";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.executeQuery();
		ResultSet rs = pstmt.getResultSet();
		
		ArrayList<ThreadData> ThreadList = new ArrayList<ThreadData>();
			while(rs.next()){
				ThreadData Thread = new ThreadData();
				Thread.setThreadId(rs.getString("thread_id"));
				Thread.setThreadTitle(rs.getString("thread_title"));
				Thread.setThreadCreater(rs.getString("thread_creater"));
				Thread.setThreadContent(rs.getString("thread_content"));
				ThreadList.add(Thread);
			}
		rs.close();
		conn.close();
		return ThreadList;
	}
	
	public static ThreadData selectThread(int threadId) throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM thread WHERE thread_id =?";
			PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, threadId);
		ResultSet rs = pstmt.executeQuery();

		ThreadData Thread = new ThreadData();
		while(rs.next()){
			Thread.setThreadId(rs.getString("thread_id"));
			Thread.setThreadTitle(rs.getString("thread_title"));
			Thread.setThreadCreater(rs.getString("thread_creater"));
			Thread.setThreadContent(rs.getString("thread_content"));
		}
		rs.close();
		return Thread;
	}
	
	public static ArrayList<ResData> selectAllRes(int threadId) throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM res WHERE thread_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, threadId);
		ResultSet rs = pstmt.executeQuery();

		ArrayList<ResData> ResAllList = new ArrayList<ResData>();
		while(rs.next()){
			ResData Res = new ResData();
			Res.setThreadId(rs.getString("thread_id"));
			Res.setResId(rs.getString("res_id"));
			Res.setContributorName(rs.getString("contributor_name"));
			Res.setDate(rs.getString("date"));
			Res.setCookieId(rs.getString("cookie_id"));
			Res.setDeleteId(rs.getString("delete_id"));
			Res.setResContent(rs.getString("res_content"));
			ResAllList.add(Res);
		}
		rs.close();
		conn.close();
		return ResAllList;
	}
	
	public static ResData selectRes(String resId,String contributorName,String resContent) throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM res WHERE res_id =?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setString(1, resId);
		ResultSet rs = pstmt.executeQuery();

		ResData ResData = new ResData();
		while(rs.next()){
			ResData.setThreadId(rs.getString("thread_id"));
			ResData.setResId(rs.getString("res_id"));
			ResData.setContributorName(rs.getString("contributor_name"));
			ResData.setResContent(rs.getString("res_content"));
		}
		rs.close();
		return ResData;
	}
	
	public static String checkDeleteId(String deleteId,String resId) throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM res WHERE delete_id=? AND res_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setString(1, deleteId);
		pstmt.setString(2, resId);
		ResultSet rs = pstmt.executeQuery();
		String selectResult = null;
		
		while(rs.next()){
			selectResult = rs.getString("delete_id");
		}
		return selectResult;
	}
	
	public static boolean checkDeleteThread(int deleteId,int ThreadId) throws SQLException{
		getConnection();
		String sqlStr = "SELECT * FROM thread WHERE delete_id=? AND thread_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, deleteId);
		pstmt.setInt(2, ThreadId);
		ResultSet rs = pstmt.executeQuery();
		String selectResult = null;
		boolean result =false;
		
		while(rs.next()){
			selectResult = rs.getString("delete_id");
		}
		if(selectResult == null || selectResult.equals("")){
			System.out.println("照合できませんでした");
		}else{
			 result = true;
		}
		return result;
	}
	
	public static void deleteRes(int resId) throws SQLException{
		getConnection();
		String sqlStr = "DELETE FROM res WHERE res_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, resId);
		pstmt.executeUpdate();
		return;
	}
	
	public static void deleteThread(int threadId) throws SQLException{
		getConnection();
		String sqlStr = "DELETE FROM thread WHERE thread_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		pstmt.setInt(1, threadId);
		pstmt.executeUpdate();
		return;
	}
	public static int serchThreadCount() throws SQLException{
		getConnection();
		String sqlStr = "SELECT MAX(thread_id) as maxno FROM thread";
		PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int threadIdCount = rs.getInt("maxno");
		return threadIdCount;
	}
	
	
	
	
}
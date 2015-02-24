package BulletinBoard.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BulletinBoard.Confirm;
import BulletinBoard.DbOperation;
import BulletinBoard.ThreadData;

public class ThreadController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			DbOperation.getConnection();
			ArrayList<ThreadData> ThreadList = new ArrayList<ThreadData>();
			try {
				ThreadList = DbOperation.selectAllThread();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 String reflesh = "done";
			 req.setAttribute("reflesh", reflesh);
			 req.setAttribute("ThreadList", ThreadList);
			 RequestDispatcher dispatch = req.getRequestDispatcher("/thread.jsp");
			 dispatch.forward(req, resp);
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		String tempThreadId =req.getParameter("threadId");
		String status = req.getParameter("status");
		String thTitle = req.getParameter("threadTitle");
		String thCreater = req.getParameter("threadCreater");
		String thContent = req.getParameter("threadContent");
		String tempDeleteId = req.getParameter("deleteId");
			//tempDeleteIdをintに変換
			int deleteId = 0;
			if(tempDeleteId.equals("")){
				System.out.println("deleteIdがありません");
				tempDeleteId = "1111";
			}else{
				deleteId = Integer.parseInt(tempDeleteId);
			}
			//tempThreadIdをintに変換
			int threadId = 0;
			if(tempThreadId == null ||tempThreadId.equals("")){
				System.out.println("threadIdがありません");
			}else{
				threadId = Integer.parseInt(tempThreadId);
			}
			
		if(status == null || status.equals("")){
			System.out.println("想定外のパターンです");
		}else if(status.equals("insert")){
			if(thTitle == null || thTitle.equals("")){
				thTitle = "タイトルは名無しさんです";
			}
			if(thCreater == null || thCreater.equals("")){
				thCreater = "こちらは名無しでお送りします";
			}
			ThreadData ThreadData = insertThread(req, resp, thTitle, thCreater, thContent, tempDeleteId);
			ArrayList<Confirm> ConfirmList = confirmContents("insertThread", thTitle, thCreater, thContent, tempDeleteId);

			req.setAttribute("transition", "home");
			req.setAttribute("status", "insertOK");
			req.setAttribute("ConfirmList", ConfirmList);
			req.setAttribute("ThreadData", ThreadData);
			RequestDispatcher dispatch = req.getRequestDispatcher("confirm.jsp");
			dispatch.forward(req, resp);

		}else if(status.equals("insertOK")){
			ThreadData ThreadData = insertThread(req, resp, thTitle, thCreater, thContent, tempDeleteId);
			java.sql.Date sqlDate =ThreadData.getSqlDate();
			String cookieId = ThreadData.getCookieId();
			try {
					DbOperation.insertThread(thTitle, thCreater, thContent,deleteId,cookieId,sqlDate);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			RequestDispatcher dispatch = req.getRequestDispatcher("thread.jsp");
			dispatch.forward(req, resp);
		}else if(status.equals("delete")){
			//初期化処理
			boolean result = false;
			ThreadData ThreadData = new ThreadData();
			try {
				result = DbOperation.checkDeleteThread(deleteId, threadId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result){
				try {
					ThreadData   = DbOperation.selectThread(threadId);
					thTitle      = ThreadData.getThreadTitle();
					thCreater    = ThreadData.getThreadCreater();
					thContent    = ThreadData.getThreadContent();
					tempDeleteId = ThreadData.getDeleteId();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList<Confirm> ConfirmList = confirmContents("deleteThread",thTitle,thCreater,thContent,tempDeleteId);
				req.setAttribute("transition", "home");
				req.setAttribute("status", "deleteOK");
				req.setAttribute("ThreadData", ThreadData);
				req.setAttribute("ConfirmList", ConfirmList);
				RequestDispatcher dispatch = req.getRequestDispatcher("confirm.jsp");
				dispatch.forward(req, resp);
			}else{
				System.out.println("チェック結果：NGです");
				req.setAttribute("ThreadData", ThreadData);
			}
		}else if(status.equals("deleteOK")){
			try {
				DbOperation.deleteThread(threadId);
				RequestDispatcher dispatch = req.getRequestDispatcher("thread.jsp");
				dispatch.forward(req, resp);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("確認画面通ってないです");
			RequestDispatcher dispatch = req.getRequestDispatcher("thread.jsp");
			dispatch.forward(req, resp);
		}
	}
	
	private ThreadData insertThread(HttpServletRequest req,HttpServletResponse resp,String thTitle,String thCreater,
			String thContent,String tempDeleteId){
		//初期化処理
			ThreadData ThreadData = new ThreadData();
	    	//現在時刻を取得
			java.util.Date utilDate = new java.util.Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String today = dateFormat.format(utilDate);
			try {
				utilDate = dateFormat.parse(today);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			//IPアドレス 
			String cookieId = req.getRemoteAddr();
			
		//データのセット
			ThreadData.setThreadTitle(thTitle);
			ThreadData.setThreadCreater(thCreater);
			ThreadData.setThreadContent(thContent);
			ThreadData.setDeleteId(tempDeleteId);
			ThreadData.setCookieId(cookieId);
			ThreadData.setSqlDate(sqlDate);
		return ThreadData;
	}
	public static ArrayList<Confirm> confirmContents(String confirmStatus,String thTitle,String thCreater,String thContent,String tempDeleteId){
		ArrayList<Confirm> ConfirmList = new ArrayList<Confirm>();
		
		if(confirmStatus == null || confirmStatus.equals("")){
			System.out.println("確認エラー");
		}else if(confirmStatus.equals("insertThread")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("スレッドを作成します",null);
			Confirm Confirm2 = new Confirm("スレッドタイトル：",thTitle);
			Confirm Confirm3 = new Confirm("名前：",thCreater);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			Confirm Confirm5 = new Confirm("削除キー：",tempDeleteId);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm2);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);
			ConfirmList.add(Confirm5);
		}else if(confirmStatus.equals("deleteThread")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("スレッドを削除します",null);
			Confirm Confirm2 = new Confirm("スレッドタイトル：",thTitle);
			Confirm Confirm3 = new Confirm("名前：",thCreater);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm2);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);

		}else if(confirmStatus.equals("insertRes")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("スレッドに対して返信します",null);
			Confirm Confirm3 = new Confirm("名前：",thCreater);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			Confirm Confirm5 = new Confirm("削除キー：",tempDeleteId);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);
			ConfirmList.add(Confirm5);

		}else if(confirmStatus.equals("deleteRes")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("返信した内容を削除します",null);
			Confirm Confirm3 = new Confirm("名前：",thCreater);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);
		}else{
			
		}
		return ConfirmList;
	}
}

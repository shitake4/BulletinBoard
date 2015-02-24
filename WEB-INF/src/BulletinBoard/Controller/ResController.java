package BulletinBoard.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Dispatch;

import BulletinBoard.Confirm;
import BulletinBoard.DbOperation;
import BulletinBoard.ResData;
import BulletinBoard.ThreadData;

public class ResController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			
		req.setCharacterEncoding("UTF-8");
		//スレッドIDの取得
		String tempId = req.getParameter("threadId");
		int threadId = 0;
		  //ThreadIDをintegerに変換
			if(tempId == null){
				System.out.println("error");
			}else{
				threadId = Integer.parseInt(tempId);
			}
			DbOperation.getConnection();
			ArrayList<ResData> ResList = new ArrayList<ResData>();
			ThreadData ThreadData = new ThreadData();

			try {
				ResList = DbOperation.selectAllRes(threadId);
				ThreadData = DbOperation.selectThread(threadId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			req.setAttribute("ThreadData", ThreadData);
			req.setAttribute("ResList", ResList);
			req.setAttribute("reflesh", "done");
			RequestDispatcher dispach = req.getRequestDispatcher("/contents.jsp?threadId="+threadId);
			dispach.forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		String contributorName = req.getParameter("contributorName");
		String resContent = req.getParameter("resContent");
		String loginStatus = req.getParameter("status");

		//スレッドIDの取得
		String tempId = req.getParameter("threadId");
		int threadId = 0;
		  //ThreadIDをintegerに変換
			if(tempId == null ||tempId.equals("")){
				System.out.println("error");
			}else{
				threadId = Integer.parseInt(tempId);
			}
			//deleteIDの取得
			String tempDeleteId = req.getParameter("deleteId");
			if(tempDeleteId == null || tempDeleteId.equals("")){
				tempDeleteId ="1111";
			}
			int deleteId = Integer.parseInt(tempDeleteId);
	    if(loginStatus.equals("insert")){
			//投稿名の設定
			if(contributorName.equals("")){
				contributorName = "こちらは名無しのゴンベイです";
			}
			//投稿内容の設定
			if(resContent.equals("")){
				resContent = "私は荒らしです!暇なんです。かまってください。助けてください…";
			}
				ResData ResData = new ResData();
				ResData.setThreadId(tempId);
				ResData.setContributorName(contributorName);
				ResData.setResContent(resContent);
				ResData.setDeleteId(tempDeleteId);

			ArrayList<Confirm> ConfirmList = confirmContents("insertRes",contributorName,resContent,tempDeleteId);
			req.setAttribute("transition", "thread");
			req.setAttribute("status", "insertOK");
			req.setAttribute("ResData", ResData);
			req.setAttribute("ConfirmList", ConfirmList);
			RequestDispatcher dispacher = req.getRequestDispatcher("confirmRes.jsp");
			dispacher.forward(req, resp);
			
	    }else if(loginStatus.equals("insertOK")){
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

	    	try {
				DbOperation.insertRes(threadId, contributorName, sqlDate, cookieId, deleteId, resContent);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestDispatcher dispacher = req.getRequestDispatcher("contents.jsp");
			dispacher.forward(req, resp);
	    	
	    }else if(loginStatus.equals("delete")){
			String tempResId = req.getParameter("resId");
			//deleteIdチェック
				String checkResult = null;
				try {
					checkResult = DbOperation.checkDeleteId(tempDeleteId, tempResId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(checkResult == null){
					System.out.println("照合に失敗しました");
					RequestDispatcher dispacher = req.getRequestDispatcher("thread.jsp");
					dispacher.forward(req, resp);

				}else{
					ResData ResData = new ResData();
					try {
						ResData = DbOperation.selectRes(tempResId, contributorName, resContent);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					contributorName = ResData.getContributorName();
					resContent = ResData.getResContent();
					tempDeleteId = ResData.getDeleteId();
					
				ArrayList<Confirm> ConfirmList = confirmContents("deleteRes",contributorName,resContent,tempDeleteId);
				req.setAttribute("transition", "thread");
				req.setAttribute("status", "deleteOK");
				req.setAttribute("ResData", ResData);
				req.setAttribute("ConfirmList", ConfirmList);
				RequestDispatcher dispacher = req.getRequestDispatcher("confirmRes.jsp");
				dispacher.forward(req, resp);
				}
				
		}else if(loginStatus.equals("deleteOK")){
			String tempResId = req.getParameter("resId");
			//resIdをinteger
			int resId = 0;
			if(tempResId ==null){
				System.out.println("ありえないことが起こってますよ");
			}else{
				resId = Integer.parseInt(tempResId);
			}
			try {
				DbOperation.deleteRes(resId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestDispatcher dispacher = req.getRequestDispatcher("contents.jsp");
			dispacher.forward(req, resp);
		}
	}

	private  ArrayList<Confirm> confirmContents(String confirmStatus,String contributorName,String thContent,String tempDeleteId){
		ArrayList<Confirm> ConfirmList = new ArrayList<Confirm>();
		
		if(confirmStatus == null || confirmStatus.equals("")){
			System.out.println("確認エラー");
		}else if(confirmStatus.equals("insertRes")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("スレッドに対して返信します",null);
			Confirm Confirm3 = new Confirm("名前：",contributorName);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			Confirm Confirm5 = new Confirm("削除キー：",tempDeleteId);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);
			ConfirmList.add(Confirm5);

		}else if(confirmStatus.equals("deleteRes")){
			//確認用リスト作成
			Confirm Confirm1 = new Confirm("返信した内容を削除します",null);
			Confirm Confirm3 = new Confirm("名前：",contributorName);
			Confirm Confirm4 = new Confirm("内容：",thContent);
			
			ConfirmList.add(Confirm1);
			ConfirmList.add(Confirm3);
			ConfirmList.add(Confirm4);
		}else{
			
		}
		return ConfirmList;
	}

}

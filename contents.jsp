<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String tempThreadId = request.getParameter("threadId");
int threadId = 0;
//ThreadIDをintegerに変換
	if(tempThreadId == null){
		System.out.print("getできてないよ");
	}else{
		threadId = Integer.parseInt(tempThreadId);
		session.setAttribute("threadId",threadId);
	}

String reflesh = (String)request.getAttribute("reflesh");
if(reflesh == null){
		response.sendRedirect("thread?threadId="+threadId);
}else if(reflesh.equals("done")){
	System.out.println("レスロード完了");	
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${ThreadData.threadTitle}</title>
</head>
<body>

<h1>${ThreadData.threadTitle}</h1>
<table>
	<tr>
		<td>番号</td>
		<td><a href="">${ThreadData.threadCreater}</a>日付：ID：</td>
	</tr>
	<tr>
		<td></td>
		<td>${ThreadData.threadContent}</td>
	</tr>
</table>

<hr />

<c:forEach var='resList' items='${ResList}'>
	<form action="thread" method="post">
		<table>
			<tr>
				<td>${resList.resId}</td>
				<td><a href="">名前：${resList.contributorName}</a>日付：${resList.date}ID：${resList.cookieId}</td>
			</tr>
			<tr>
				<td></td>
				<td>${resList.resContent}</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="hidden" name="threadId" value="${threadId}">
					<input type="hidden" name="resId" value="${resList.resId}">
					<input type="hidden" name="status" value="delete">
					<input type="text" name="deleteId" />
					<input type="submit" value="削除" />
				</td>
			</tr>
		</table>
	</form>
	<hr />
</c:forEach>


	<form action="thread" method="post">
		<table>
			<tr>
				<th>名前:</th>
				<td>
					<input type="text" name="contributorName" size="40">
				</td>
			</tr>
			<tr>
				<th>内容:</th>
				<td>
					<textarea name="resContent" rows="3" cols="40"></textarea>
				</td>
			</tr>
			<tr>
				<th>削除パス</th>
				<td>
					<input type="text" name="deleteId" size="40">
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="hidden" name="threadId" value="${threadId}">
					<input type="hidden" name="status" value="insert">
					<input type="submit" value="投稿する">
					<input type="reset" value="リセット">
				</td>
			</tr>
		</table>
	</form>
	<h2><a href="thread.jsp">ホーム画面に戻る</a></h2>
</body>
</html>
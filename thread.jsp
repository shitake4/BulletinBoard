<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String reflesh = (String)request.getAttribute("reflesh");
	if(reflesh == null){
 		response.sendRedirect("home");
 	}else if(reflesh.equals("done")){
 		System.out.println("スレッドのロードが完了しました！");
 	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>スレッドの作成、一覧</title>
</head>
<body>
	
<h2>スレッドの作成</h2>

	<form action="home" method="post">
		<table>
			<caption>※下記にすべて入力してください</caption>
			<tr>
				<th>作成者名</th>
				<td><input type="text" name="threadCreater" size="40"></td>
			</tr>
			<tr>
				<th>スレッドタイトル</th>
				<td><input type="text" name="threadTitle" size="40"></td>
			</tr>
			<tr>
				<th>内容</th>
				<td>
					<textarea name="threadContent" rows="15" cols="41"></textarea>
				</td>
			</tr>
			<tr>
				<th>削除パス</th>
				<td>
					<input type="text" name="deleteId" size="40">
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<input type="hidden" name="status" value="insert">
					<input type="submit" value="作成">
					<input type="reset" value="リセット">
				</td>
			</tr>
		</table>
	</form>

<h2>スレッドの一覧</h2>
    <form action="home" method="post">
	<table border="2">
		<tr>
			<th>番号</th>
			<td>タイトル名</td>
			<td>削除するスレッドを選択</td>
		</tr>
		<c:forEach var="ThreadList" items="${ThreadList}">
		<tr>
			<th>${ThreadList.threadId}</th>
			<td>
				<a href="/BulletinBoard/thread?threadId=${ThreadList.threadId}">${ThreadList.threadTitle}</a>
			</td>
			<td>
				<input type="radio" name="threadId" value="${ThreadList.threadId}">
			</td>
		</c:forEach>
		</table>
		<table border="1">
			<tr>
				<td>
					<input type="text" name="deleteId">
				</td>
				<td>
					<input type="hidden" name="status" value="delete">
					<input type="submit" value="削除する">
				</td>
			</tr>
		</table>
  	</form>
</body>
</html>
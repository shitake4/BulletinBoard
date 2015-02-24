<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>確認画面</title>
</head>
<body>

<h1>以下を行ってよろしいでしょうか</h1>

<c:forEach var="ConfirmList" items="${ConfirmList}">
	<p>${ConfirmList.item}${ConfirmList.content}</p>
</c:forEach>

<form action="${transition}" method="post">
	<input type="hidden" name="threadId" value="${ThreadData.threadId}">
	<input type="hidden" name="threadTitle" value="${ThreadData.threadTitle}">
	<input type="hidden" name="threadCreater" value="${ThreadData.threadCreater}">
	<input type="hidden" name="threadContent" value="${ThreadData.threadContent}">
	<input type="hidden" name="deleteId" value="${ThreadData.deleteId}">
	<input type="hidden" name="status" value="${status}">
	<p><input type="submit" value="送信"></p>
</form>

</body>
</html>
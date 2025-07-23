<%--
  Created by IntelliJ IDEA.
  User: choejinho
  Date: 2025. 7. 22.
  Time: 오후 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>알람 등록</title>
</head>
<body>
<h1>알람 등록</h1>
<form action="${pageContext.request.contextPath}/alarm/register" method="post">
    <label for="title">제목:</label>
    <input type="text" id="title" name="title" required/><br/><br/>

    <label for="content">내용:</label>
    <textarea id="content" name="content"></textarea><br/><br/>

    <label for="time">시간 (yyyy-MM-ddTHH:mm):</label>
    <input type="datetime-local" id="time" name="time" required/><br/><br/>

    <button type="submit">등록</button>
</form>
</body>
</html>

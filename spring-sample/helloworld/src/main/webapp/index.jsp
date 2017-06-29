<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<html>
<body>
<h2>Hello World!</h2>

<c:redirect url="/greeting">
    <c:param name="message" value="안녕하세요" />
</c:redirect>

</body>
</html>

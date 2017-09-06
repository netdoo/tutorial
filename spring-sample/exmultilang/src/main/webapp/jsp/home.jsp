<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><spring:message code="site.title" text="스프링 테스트 사이트 - default" /></title>
</head>
<body>
<h2>Home</h2>
<p>site.count : <spring:message code="site.count" arguments="첫번째" text="default text" /></p>
<p>site.count using EL : <spring:message code="site.count" arguments="${siteCount}" text="default text" /></p>
</body>
</html>

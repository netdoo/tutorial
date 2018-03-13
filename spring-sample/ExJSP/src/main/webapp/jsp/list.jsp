<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>mybatis, jstl, bootstrap, pagination</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
    </style>

</head>

<body>

<div class="container">
    <table class="table table-bordered table-hover">
        <thead>
        <tr>
            <th class="col-md-2">No</th>
            <th class="col-md-2">Name</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.no}    </td>
                <td>${user.name}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="container" align="center">
    <div class="row">
        <div class="col-xs-12">
            <ul class="pagination pagination-sm">

                <c:choose>
                    <c:when test="${paging.pageNo == 1}">
                        <li class="disabled"><span>이전</span></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/list?page=${paging.doublePrevPageNo}"><span>이전</span></a></li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="${paging.startPageNo}" end="${paging.endPageNo}" step="1">
                    <c:choose>
                        <c:when test="${i==paging.pageNo}">
                            <li class="active"><span>${i}</span></li>
                        </c:when>
                        <c:otherwise>
                            <li><span><a href="/list?page=${i}">${i}</a></span></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${paging.doubleNextPageNo == paging.finalPageNo}">
                        <li class="disabled"><span>다음</span></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/list?page=${paging.doubleNextPageNo}">다음</a></li>
                    </c:otherwise>
                </c:choose>

            </ul>
        </div>
    </div>
</div>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>HS Communication - MtoMCash
        <c:if test="${not empty param.title}"> :: ${param.title}</c:if>
    </title>

    <meta http-equiv='cache-control' content='no-cache'>
    <meta http-equiv='expires' content='0'>
    <meta http-equiv='pragma' content='no-cache'>

    <link rel="shortcut icon" href="/back/static/images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="/back/static/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/back/static/css/main.css">
    <link rel="stylesheet" type="text/css" href="/back/static/themes/icon.css">

    <script type="text/javascript" src="/back/static/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="/back/static/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/back/static/js/main.js"></script>
    
	<script src="http://jquery.bassistance.de/validate/jquery.validate.js"></script>
	<script src="http://jquery.bassistance.de/validate/additional-methods.js"></script>	

</head>

<body class="easyui-layout" id="mainLayout">
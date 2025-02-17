<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Heads Up Report</title>
<style>
	tr { outline: thin solid; }
	tr.compliant td { background-color: lightgreen; }
	tr.noncompliant td { background-color: yellow; }
</style>
</head>
<body>
<H1>HeadsUp Report</H1>

Repeat Test: <a href="${requestUrl}">${requestUrl}</a><br/><br/>

<%@ include file="jspf/showReport.jspf" %>

</body>
</html>
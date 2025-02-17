<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.websecuritylab.tools.headers.util.PolicyHandler" %>

<% request.setAttribute("pageType", "test"); %>
<% request.setAttribute("policyList", PolicyHandler.getPolicyNames()); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
Testing JSP
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Heads Up Tester</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/headsUp.css">
	<link rel="stylesheet" href="css/tabs.css">
	<script src="js/tabs.js"></script>
	<script src="js/showHide.js"></script>
</head>
<body>

<h2>HeadsUp</h2>
<div class="tab">
  <button class="tablinks ${pageType == 'test' ? 'active' : ''}" onclick="window.location.href='index.jsp'">Run Test</button>
  <button class="tablinks ${pageType == 'view' ? 'active' : ''}" onclick="window.location.href='MaintainRules?pageType=view'">View Rules</button>
  <button class="tablinks ${pageType == 'edit' ? 'active' : ''}" onclick="window.location.href='MaintainRules?pageType=edit'">Edit Rules</button>
  <button class="tablinks ${pageType == 'about' ? 'active' : ''}" onclick="window.location.href='about.jsp'">About</button>
</div>

<div id="Run" class="tabcontent ${pageType == 'test' ? '' : 'hidden'}">
	<%@ include file="jspf/runTest.jspf" %>
</div>

<div id="View" class="tabcontent ${pageType == 'view' ? '' : 'hidden'}">
	<%@ include file="jspf/viewRules.jspf" %>
</div>

<div id="Edit" class="tabcontent ${pageType == 'edit' ? '' : 'hidden'}">
	<%@ include file="jspf/editRules.jspf" %>
</div>

<div id="Edit" class="tabcontent ${pageType == 'report' ? '' : 'hidden'}">
	<%@ include file="jspf/showReport.jspf" %>
</div>

<div id="About" class="tabcontent ${pageType == 'about' ? '' : 'hidden'}">
  <h3>About</h3>
  <p>HeadsUp is a tool to check your HTTP Security Headers for best practice compliance.</p>
  <p>See: <a href="https://securityheaders.com/">securityheaders.com</a> for information on HTTP Security Header best practices.</p>
  
</div>

  
</body>
</html> 

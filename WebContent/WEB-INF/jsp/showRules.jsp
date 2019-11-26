<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8f59-1">
<title>Heads Up Rules</title>
</head>
<body>
<h1>Heads Up Rules</h1>
<fieldset>
	<legend>Active Policy: ${policy.name}</legend>
	<%@ include file="showPolicy.jspf" %>
</fieldset>

<fieldset>
	<legend>Saved Policies</legend>
	<form action="MaintainRules" method=POST>
		<button type="submit">Save Rules</button>
	</form>
</fieldset>

	
<fieldset>
	<legend>Saved Rules</legend>
</fieldset>



</body>
</html>
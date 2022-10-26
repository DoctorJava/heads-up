<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8f59-1">
<title>Heads Up Rules</title>
</head>
<body>


<h1>View Rules</h1>
<h2>Selected Policy: ${policy.name}</h2>

<fieldset>
	<legend>
		<form action="MaintainRules" method=GET>
			Policy:
				<select name=policyName onchange="this.form.submit()">
					<option ${ policy.name == "Default" ? "selected" : "" }>Default</option>				
					<c:forEach var="policyName" items="${policyList}">
						<option ${ policy.name == policy ? "selected" : "" }>${policyName}</option>
					</c:forEach>
				</select>
		</form>	
	</legend>
	<%@ include file="jspf/showPolicy.jspf" %>
	<form action="MaintainRules" method=GET>
		<input type=hidden name=method value=edit>
		<input type=hidden name=policyName value="${policy.name}">
		<button type="submit">Edit Policy</button>
	</form>	
</fieldset>

</body>
</html>
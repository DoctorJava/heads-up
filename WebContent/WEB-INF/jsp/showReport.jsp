<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Heads Up Report</title>
<link rel="stylesheet" type="text/css" href="main.css">
</head>
<body>
<fieldset>
	<legend>Heads Up Report: ${report.name}</legend>
	Site: <a href="${report.url}">${report.url}</a>
	<table
>
		<tr>
			<th></th><th>Header</th><th>Actual</th><th>Suggested</th><th>Reference</th>
		</tr>
		<c:forEach var="item" items="${report.items}">
			<tr class=${item.rule.isRequired()? (item.isCompliant()? "compliant" : "noncompliant") : "" }>
				<td>
						<%-- ${ item.compliant ... only works with boolean, not Boolean } --%>
						<c:if test="${item.rule.isRequired()}">
							${ item.isCompliant() ? "<img src='images/check.png' border=0/>" : "<img src='images/cross.png' border=0/>"}				
						</c:if>
				</td>
				<td>${item.headerName}</td>
				<td>
					<c:if test="${ !item.isPresent() }">(Not Present)</c:if>
					<c:forEach var="value" items="${item.headerValues}">
						${value}</br>
					</c:forEach>					
				</td>
				<td>
					<c:if test="${ ! item.isCompliant() }">
						<%-- ${item.rule.containsType} --%>
						<c:forEach var="values" items="${item.rule.contains}">
							${values}</br>
						</c:forEach>					
					</c:if>			
				</td>
				<td>
					<c:if test="${ ! item.isCompliant() }">
						<c:forEach var="ref" items="${item.rule.references}">
							<a href="${ref.url}" target="_blank">${ref.title}</a> | 
						</c:forEach>
					</c:if>			
				</td>
			</tr>
		</c:forEach>
	</table>
</fieldset>
<fieldset>
	<legend>Raw Response Headers</legend>
	<pre>${report.rawHeaders}</pre>
</fieldset>
<fieldset>
<legend>Rules</legend>
	<%@ include file="showPolicy.jspf" %>
</fieldset>
</body>
</html>
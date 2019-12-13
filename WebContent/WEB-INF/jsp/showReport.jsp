<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<fieldset>
	<legend>Heads Up Report: ${report.name}</legend>
	Site: <a href="${report.url}">${report.url}</a>
	<table>
		<tr>
			<th></th><th>Header</th><th>Actual</th><th>Suggested</th><th>Reference</th>
		</tr>
		<c:forEach var="item" items="${report.items}">
			<tr class=${item.rule.isRequired()? (item.isCompliant()? "compliant" : "noncompliant") : "" }>
				<td>
						<%-- ${ item.compliant ... only works with boolean, not Boolean } --%>
						<c:if test="${item.rule.isRequired()}">
						<!-- &#9989; is Green Checkbox -->
						<!--   -->
							${ item.isCompliant() ? "&#9989; " : "&#10060;"}				
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
	<hr/>
	<table>
		<tr>
			<th></th><th>Cookie</th><th>Actual</th><th>Suggested</th><th>Reference</th><th>Value</th>
		</tr>
		<c:forEach var="item" items="${report.cookies}">   <!--  var="cookie" didn't work because evidently ${cookie} picks up the cookie from HeadsUp -->
			<tr class=${ item.isCompliant() != null ? ( item.isCompliant() ? "compliant" : "noncompliant" ) : "" }>
				<td width=25>
					<c:if test="${item.isCompliant() != null}">
						${ item.isCompliant() ? "&#9989; " : "&#10060;"}	
					</c:if>			
				</td>
				<td>${item.name}</td>
				<td>
					<c:forEach var="value" items="${item.directives}">
						${value}</br>
					</c:forEach>					
				</td>
				<td>
					<c:if test="${ item.isCompliant() != null }">
						<c:if test="${ item.isCompliant() != null }">
							<c:forEach var="rule" items="${item.rules}">
								<c:forEach var="values" items="${rule.contains}">
									${values}</br>
								</c:forEach>
							</c:forEach>	
						</c:if>					
					</c:if>			
				</td>
				<td>
					<c:forEach var="rule" items="${item.rules}">
						<c:forEach var="ref" items="${rule.references}">
							<a href="${ref.url}" target="_blank">${ref.title}</a> | 
						</c:forEach>
					</c:forEach>	
				</td>
				<td>${item.value}
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
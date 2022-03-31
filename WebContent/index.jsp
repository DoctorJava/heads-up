<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Heads Up HTTP Security Tool</title>
</head>
<body>
<h1>Heads Up</h1>	
<fieldset style="border: 2px solid #AA0000;">
	<legend>HTTP Security Header Test</legend>
	<form action="CheckHeaders" method=GET >
		<h3>Report Name: <input type=text name=reportName /></h3>
		<%@ include file="formTarget.jspf" %>  
	
		<h3>Rules:</h3>
		<div style="margin-left:50px">
			Standard: <input name=rulesType type=radio checked disabled />
			<select name="policySelect">
				<option value="default">Default Rules</option>
				<option value="caseSentiveValues.json">Case Sensitive Values (more strict)</option>
			</select>
			</br>
			Custom : <input name=rulesType type=radio onclick="window.location.href='index2.jsp'"/><br/>
			
		</div>
		
		<button type="submit" >Check Headers</button>
		
	</form>	
</fieldset>
	
	<p><a href="MaintainRules">Maintain Rules</a>
	
	<p>See: <a href="https://securityheaders.com/">securityheaders.com</a> for information on HTTP Security Header best practices.

</body>
</html>
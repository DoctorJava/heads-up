<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.websecuritylab.tools.headers.util.PolicyHandler" %>

<% request.setAttribute("pageType", "test"); %>
<% request.setAttribute("policyList", PolicyHandler.getPolicyNames()); %>

<%-- <jsp:include page="WEB-INF/jsp/page.jsp" /> --%>

<jsp:forward page="WEB-INF/jsp/page.jsp" />
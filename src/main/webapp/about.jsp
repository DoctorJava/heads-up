<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<% request.setAttribute("pageType", "about"); %>
<%-- <jsp:include page="WEB-INF/jsp/page.jsp" /> --%>

<jsp:forward page="WEB-INF/jsp/page.jsp" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.ResourceBundle"%>
<%@page import="org.mxhero.engine.plugin.attachmentlink.fileserver.servlet.FileService"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%ResourceBundle bundle = ResourceBundle.getBundle("i18n/Messages",request.getLocale()); %>
	<div style="width: 100%; border-bottom-width: 2px; border-bottom-style: solid; padding-bottom: 10px;">
	<img style="width: 200px; height: 50px; padding-bottom: 5px;" src="http://www.mxhero.com/wp-content/uploads/2011/09/logo3.png" width="200" height="50" border="0" /></div>
	<span style="font-size: 2em; font-weight: bold;">
	<%=bundle.getString("error.general.title") %>
	</span>
	<p style="color: red; font-weight: bold;">
	<%=bundle.getString("error.general.message") %>
	</p>

</body>
</html>
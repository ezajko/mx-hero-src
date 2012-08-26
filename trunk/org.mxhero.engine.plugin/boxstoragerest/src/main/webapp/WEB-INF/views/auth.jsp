<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tbody>
			<tr>
				<td>&nbsp;</td>
				<td>
					<table border="0" cellpadding="0" cellspacing="0" width="100%"
						align="left">
						<tbody>
							<tr>
								<td align="left" width="270"><img
									src='<c:url value='/static/images/head1.png'/>' alt="" width="270"
									height="91"></td>
								<td align="center"><p
										style="font-family: Arial, Helvetica, sans-serif; font-size: 16px; font-weight: normal; line-height: 20px; color: #000000; text-align: center; display: block; margin: 0px; padding: 30px 0px 0px 0px;">
										<strong
											style="display: block; clear: both; width: 100%; font-size: 28px; line-height: 28px; font-weight: bold;"><spring:message code="title" /></strong>
									</p></td>
								<td align="right" width="270"><img
									src='<c:url value='/static/images/head3.gif'/>' alt="" width="270"
									height="91"></td>
							</tr>
						</tbody>
					</table>
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="left" width="65" style="background: repeat-x;"
					background='<c:url value='/static/images/h_full.gif'/>'><img
					src='<c:url value='/static/images/h_1.gif'/>' alt="" width="65" height="29"></td>
				<td align="center" style="background-repeat: repeat-x;"
					background='<c:url value='/static/images/h_full.gif'/>'><img
					src='<c:url value='/static/images/h_3.gif'/>' alt="" width="65" height="29"></td>
				<td align="right" width="65" style="background: repeat-x;"
					background='<c:url value='/static/images/h_full.gif'/>'><img
					src='<c:url value='/static/images/h_2.gif'/>' alt="" width="65" height="29"></td>
			</tr>
			<tr>
				<td align="left" valign="top"
					style="background-image: #f2f2f2 top left repeat-y;"
					background='<c:url value='/static/images/left.gif'/>'><img
					src='<c:url value='/static/images/left.gif'/>' alt="" width="65" height="100%"></td>
				<td align="center" bgcolor="#f2f2f2" style="background: #f2f2f2;"><span
					style="font-size: 16px; font-family: Arial, Helvetica, sans-serif; color: #000;">&nbsp;<br /><spring:message code="user.message"/>
				</span></td>
				<td align="right" valign="top"
					style="background-image: #f2f2f2 top right repeat-y;"
					background='<c:url value='/static/images/right.gif'/>'><img
					src='<c:url value='/static/images/right.gif'/>' alt="" width="65"
					height="100%"></td>
			</tr>
			<tr>
				<td align="left" valign="bottom" style="background: repeat-x;"
					background='<c:url value='/static/images/f_full.gif'/>'><img
					src='<c:url value='/static/images/f_1.gif'/>' alt="" width="65" height="48"></td>
				<td align="center" valign="bottom"
					style="background-repeat: repeat-x;"
					background='<c:url value='/static/images/f_full.gif'/>'>&nbsp;</td>
				<td align="right" valign="bottom" style="background: repeat-x;"
					background='<c:url value='/static/images/f_full.gif'/>'><img
					src='<c:url value='/static/images/f_2.gif'/>' alt="" width="65" height="48"></td>
			</tr>
		</tbody>
	</table>

</body>
</html>
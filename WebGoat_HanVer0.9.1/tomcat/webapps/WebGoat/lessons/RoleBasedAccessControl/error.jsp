<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.RoleBasedAccessControl.*"
	errorPage="" %>
<%
	Employee employee = (Employee) session.getAttribute("RoleBasedAccessControl." + RoleBasedAccessControl.EMPLOYEE_ATTRIBUTE_KEY);
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
//	int myUserId = getIntSessionAttribute(webSession, "RoleBasedAccessControl." + RoleBasedAccessControl.USER_ID);
%>
<br><br><br>An error has occurred.
<br><br><br>
<form method="POST" action="attack?menu=<%=webSession.getCurrentMenu()%>">

	<input type="submit" name="action" value="<%=RoleBasedAccessControl.LOGIN_ACTION%>"/>
</form>
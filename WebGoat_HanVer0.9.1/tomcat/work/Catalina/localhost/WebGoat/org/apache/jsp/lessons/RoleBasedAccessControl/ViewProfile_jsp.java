package org.apache.jsp.lessons.RoleBasedAccessControl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.*;

public final class ViewProfile_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');

	Employee employee = (Employee) session.getAttribute("RoleBasedAccessControl." + RoleBasedAccessControl.EMPLOYEE_ATTRIBUTE_KEY);
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
//	int myUserId = getIntSessionAttribute(webSession, "RoleBasedAccessControl." + RoleBasedAccessControl.USER_ID);

      out.write("\r\n");
      out.write("\t\t<div class=\"lesson_title_box\"><strong>Welcome Back </strong><span class=\"lesson_text_db\">");
      out.print(webSession.getUserNameInLesson());
      out.write("</span> - View Profile Page</div>\r\n");
      out.write("\t\t<div class=\"lesson_text\">\r\n");
      out.write("\t\t\t\t<Table>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tFirst Name:\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getFirstName());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tLast Name:\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t \t<span class=\"lesson_text_db\">");
      out.print(employee.getLastName());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tStreet: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getAddress1());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tCity/State: \r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getAddress2());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tPhone: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getPhoneNumber());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tStart Date: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getStartDate());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t    \t\tSSN: \r\n");
      out.write("\t\t\t    \t</TD>\r\n");
      out.write("\t\t\t    \t<TD>\r\n");
      out.write("\t\t\t    \t\t<span class=\"lesson_text_db\">");
      out.print(employee.getSsn());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tSalary: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getSalary());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tCredit Card: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getCcn());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tCredit Card Limit: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getCcnLimit());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tComments: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"3\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getPersonalDescription());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\t\t\t\t\r\n");
      out.write("\t\t\t\t<TR>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"2\">\t\r\n");
      out.write("\t\t\t\t\t\tDisciplinary Explanation: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tDisc. Dates: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getDisciplinaryActionDate());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t<TR>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"4\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getDisciplinaryActionNotes());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR>\r\n");
      out.write("\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tManager: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<span class=\"lesson_text_db\">");
      out.print(employee.getManager());
      out.write("</span>\r\n");
      out.write("\t\t\t\t\t</TD>\t\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t</Table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div class=\"lesson_buttons_bottom\">\r\n");
      out.write("\t\t    <table width=\"460\" height=\"20\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("                 <tr>\r\n");
      out.write("                 \t<td width=\"50\">\r\n");
      out.write("\t\t\t\t\t ");
					
					 if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.LISTSTAFF_ACTION))
					 {
					 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<form method=\"POST\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"");
      out.print(RoleBasedAccessControl.EMPLOYEE_ID);
      out.write("\" value=\"");
      out.print(employee.getId());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.LISTSTAFF_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t ");

					 }
      out.write("\r\n");
      out.write("\t\t\t\t\t </td>\r\n");
      out.write("\t\t             <td width=\"50\">\r\n");
      out.write("\t\t\t\t\t ");
					
					 if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.EDITPROFILE_ACTION))
					 {
					 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<form method=\"POST\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"");
      out.print(RoleBasedAccessControl.EMPLOYEE_ID);
      out.write("\" value=\"");
      out.print(employee.getId());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.EDITPROFILE_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t");

					}
					
      out.write("\r\n");
      out.write("\t\t\t\t\t</td>\t\t\t\t\t\r\n");
      out.write("                    <td width=\"60\">\r\n");
      out.write("\t\t\t\t\t");
					
					if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.DELETEPROFILE_ACTION))
					{
					
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<form method=\"POST\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"hidden\" name=\"");
      out.print(RoleBasedAccessControl.EMPLOYEE_ID);
      out.write("\" value=\"");
      out.print(employee.getId());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.DELETEPROFILE_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t");

					}
					
      out.write("\r\n");
      out.write("\t\t\t\t\t</td>\r\n");
      out.write("                      <td width=\"190\">&nbsp;</td>\r\n");
      out.write("                      <td width=\"76\">\r\n");
      out.write("\t\t\t\t\t\t<form method=\"POST\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.LOGOUT_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</form>\r\n");
      out.write("\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("         \t</table>\r\n");
      out.write("\t\t</div>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

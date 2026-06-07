package org.apache.jsp.lessons.RoleBasedAccessControl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.*;

public final class EditProfile_jsp extends org.apache.jasper.runtime.HttpJspBase
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

	WebSession webSession = ((WebSession)session.getAttribute("websession"));
	Employee employee = (Employee) session.getAttribute("RoleBasedAccessControl.Employee");

      out.write("\r\n");
      out.write("\t\t<div class=\"lesson_title_box\"><strong>Welcome Back </strong><span class=\"lesson_text_db\">");
      out.print(webSession.getUserNameInLesson());
      out.write("</span> - Edit Profile Page</div>\r\n");
      out.write("\t\t<div class=\"lesson_text\">\r\n");
      out.write("\t\t\t<form id=\"form1\" name=\"form1\" method=\"post\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("<Table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t\t\t\t<TR><TD width=\"110\">\r\n");
      out.write("\t\t\t\t\t\tFirst Name:\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD width=\"193\">\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.FIRST_NAME);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getFirstName());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t  </TD>\r\n");
      out.write("\t\t\t\t\t<TD width=\"110\">\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tLast Name:\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD width=\"196\">\r\n");
      out.write("\t\t\t\t\t \t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.LAST_NAME);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getLastName());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t  </TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tStreet: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.ADDRESS1);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getAddress1());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tCity/State: \r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.ADDRESS2);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getAddress2());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tPhone: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.PHONE_NUMBER);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getPhoneNumber());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tStart Date: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.START_DATE);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getStartDate());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t    \t\tSSN: \r\n");
      out.write("\t\t\t    \t</TD>\r\n");
      out.write("\t\t\t    \t<TD>\r\n");
      out.write("\t\t\t    \t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.SSN);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getSsn());
      out.write("\"/> \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tSalary: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.SALARY);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getSalary());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tCredit Card: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.CCN);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getCcn());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tCredit Card Limit: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.CCN_LIMIT);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getCcnLimit());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR><TD>\r\n");
      out.write("\t\t\t\t\t\tComments: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"3\">\r\n");
      out.write("\t\t\t\t\t\t<input name=\"");
      out.print(RoleBasedAccessControl.DESCRIPTION);
      out.write("\" type=\"text\" class=\"lesson_text_db\" value=\"");
      out.print(employee.getPersonalDescription());
      out.write("\" size=\"62\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t<TR>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"2\">\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tDisciplinary Explanation:  \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\r\n");
      out.write("\t\t\t\t\t\tDisc. Date:\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\t\t\t\r\n");
      out.write("\t\t\t\t\t\t<input class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.DISCIPLINARY_DATE);
      out.write("\" type=\"text\" value=\"");
      out.print(employee.getDisciplinaryActionDate());
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR>\r\n");
      out.write("\t\t\t\t\t<TD colspan=\"4\">\r\n");
      out.write("\t\t\t\t\t\t<textarea name=\"");
      out.print(RoleBasedAccessControl.DISCIPLINARY_NOTES);
      out.write("\" cols=\"52\" rows=\"2\" wrap=\"off\" class=\"lesson_text_db\" >");
      out.print(employee.getDisciplinaryActionNotes());
      out.write("</textarea>\r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t<TR>\t\r\n");
      out.write("\t\t\t\t<TD>\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\tManager: \r\n");
      out.write("\t\t\t\t\t</TD>\r\n");
      out.write("\t\t\t\t\t<TD>\r\n");
      out.write("\t\t\t\t\t\t<select class=\"lesson_text_db\" name=\"");
      out.print(RoleBasedAccessControl.MANAGER);
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t");

				      	List employees = (List) session.getAttribute("RoleBasedAccessControl.Staff");
				      	Iterator i = employees.iterator();
						while (i.hasNext())
						{
							EmployeeStub stub = (EmployeeStub) i.next();
								
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<option value=\"");
      out.print(Integer.toString(stub.getId()));
      out.write('"');
      out.write('>');
      out.print(stub.getFirstName() + " " + stub.getLastName());
      out.write("</option>\r\n");
      out.write("\t\t\t\t\t\t");
}
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t</TD>\t\r\n");
      out.write("\t\t\t\t</TR>\r\n");
      out.write("\t\t\t\t</Table>\r\n");
      out.write("\t\t\t\t<BR>\r\n");
      out.write("\t\t\t\t<div class=\"lesson_buttons_bottom\">\r\n");
      out.write("\t\t\t\t<table width=\"460\" height=\"20\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("               \t\t<tr>\r\n");
      out.write("                     \t\t<td width=\"57\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.VIEWPROFILE_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t  \t\t</td>\r\n");
      out.write("\t\t\t\t  \t\t\r\n");
      out.write("                       \t<td width=\"81\">\r\n");
      out.write(" \t\t\t\t\t\t\t<input name=\"");
      out.print(RoleBasedAccessControl.EMPLOYEE_ID);
      out.write("\" type=\"hidden\" value=\"");
      out.print(employee.getId());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input name=\"");
      out.print(RoleBasedAccessControl.TITLE);
      out.write("\" type=\"hidden\" value=\"");
      out.print(employee.getTitle());
      out.write("\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.UPDATEPROFILE_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("                        \t<td width=\"211\"></td>\r\n");
      out.write("                        \t<td width=\"83\">\r\n");
      out.write("\t \t\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.LOGOUT_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("                 \t</tr>\r\n");
      out.write("              \t</table>\t\t\t\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\t");
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

package org.apache.jsp.lessons.RoleBasedAccessControl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.*;

public final class ListStaff_jsp extends org.apache.jasper.runtime.HttpJspBase
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
	int myUserId = webSession.getUserIdInLesson();

      out.write("\r\n");
      out.write("\t<div class=\"lesson_title_box\"><strong>Welcome Back </strong><span class=\"lesson_text_db\">");
      out.print(webSession.getUserNameInLesson());
      out.write("</span> - Staff Listing Page</div>\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<p>Select from the list below\t</p>\r\n");
      out.write("\r\n");
      out.write("\t\t<form id=\"form1\" name=\"form1\" method=\"post\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("  <table width=\"60%\" border=\"0\" cellpadding=\"3\">\r\n");
      out.write("    <tr>\r\n");
      out.write("      <td>  <label>\r\n");
      out.write("  <select name=\"");
      out.print(RoleBasedAccessControl.EMPLOYEE_ID);
      out.write("\" size=\"11\">\r\n");
      out.write("\t\t\t      \t");

			      	List employees = (List) session.getAttribute("RoleBasedAccessControl." + RoleBasedAccessControl.STAFF_ATTRIBUTE_KEY);
			      	Iterator i = employees.iterator();
					while (i.hasNext())
					{
						EmployeeStub stub = (EmployeeStub) i.next();
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print(Integer.toString(stub.getId()));
      out.write('"');
      out.write('>');
      out.print(stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")");
      out.write("</option>");

					}
      out.write("\r\n");
      out.write("  </select>\r\n");
      out.write("  </label></td>\r\n");
      out.write("      <td>\r\n");
      out.write("\t        \t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.SEARCHSTAFF_ACTION);
      out.write("\"/><br>\r\n");
      out.write("\t        \t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.VIEWPROFILE_ACTION);
      out.write("\"/><br>\r\n");
      out.write("            \t\t");
 
				if (webSession.isAuthorizedInLesson(myUserId, RoleBasedAccessControl.CREATEPROFILE_ACTION))
				{
				
      out.write("\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.CREATEPROFILE_ACTION);
      out.write("\"/><br>\r\n");
      out.write("\t\t\t\t");
 
				}
				
      out.write("\r\n");
      out.write("            \t\t");
 
				if (webSession.isAuthorizedInLesson(myUserId, RoleBasedAccessControl.DELETEPROFILE_ACTION))
				{
				
      out.write("\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.DELETEPROFILE_ACTION);
      out.write("\"/><br>\r\n");
      out.write("\t\t\t\t");
 
				}
				
      out.write("\r\n");
      out.write("\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.LOGOUT_ACTION);
      out.write("\"/>\r\n");
      out.write("\t  </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("  </table>\r\n");
      out.write("\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\t\t");
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

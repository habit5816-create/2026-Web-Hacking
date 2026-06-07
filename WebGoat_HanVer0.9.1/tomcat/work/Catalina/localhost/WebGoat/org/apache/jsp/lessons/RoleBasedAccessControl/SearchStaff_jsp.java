package org.apache.jsp.lessons.RoleBasedAccessControl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.*;

public final class SearchStaff_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n");
      out.write("\t<div id=\"lesson_search\">\r\n");
      out.write("\t\t\t");
 
			WebSession webSession = ((WebSession)session.getAttribute("websession"));
			String searchedName = request.getParameter(RoleBasedAccessControl.SEARCHNAME);
			if (searchedName != null)
			{
			
      out.write("\r\n");
      out.write("\t\t\t\tEmployee ");
      out.print(searchedName);
      out.write(" not found.\r\n");
      out.write("\t\t\t");

			}
			
      out.write("\r\n");
      out.write("\t\t\t\t<form id=\"form1\" name=\"form1\" method=\"post\" action=\"attack?menu=");
      out.print(webSession.getCurrentMenu());
      out.write("\">\r\n");
      out.write("\t\t\t    \t<label>Name\r\n");
      out.write("\t\t\t\t\t<input class=\"lesson_text_db\" type=\"text\" name=\"");
      out.print(RoleBasedAccessControl.SEARCHNAME);
      out.write("\"/>\r\n");
      out.write("\t\t        </label>\r\n");
      out.write("\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t<input type=\"submit\" name=\"action\" value=\"");
      out.print(RoleBasedAccessControl.FINDPROFILE_ACTION);
      out.write("\"/>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t</div>");
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

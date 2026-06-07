package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.lessons.*;
import java.util.*;

public final class webgoat_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html;charset=euc-kr");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("  \r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=euc-kr\" />\r\n");
      out.write("<title>WebGoat V4</title>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css/webgoat.css\" type=\"text/css\" />\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<div id=\"wrap\">\r\n");
      out.write("\t\t<div id=\"top\"></div>\r\n");
      out.write("\t\t<div id=\"start\">\r\n");
      out.write("\t\t  <p>WebGoat를 이용해 주셔서 감사합니다.!</p>\r\n");
      out.write("\t\t  <p>이 프로그램은 일반적인 웹 어플리케이션 결함들을 보여줍니다.\r\n");
      out.write("\t\t  이 예제들은 어플리케이션 침투 테스트 기술들의 실전을 제공할 것입니다.</p>\r\n");
      out.write("\t\t  <div id=\"team\">\r\n");
      out.write("\t\t\t<table width=\"460\" border=\"0\" align=\"center\" class=\"lessonText\">\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td width=\"50%\"><div align=\"center\"><span class=\"style1\">WebGoat Design Team </span></div></td>\r\n");
      out.write("                  <td width=\"50%\"><div align=\"center\"><span class=\"style1\">Lesson Contributers </span></div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td rowspan=\"6\" valign=\"top\">\r\n");
      out.write("                  \t<div align=\"center\" class=\"style2\">Jeff Williams</div>                    \r\n");
      out.write("\t                 <div align=\"center\" class=\"style2\">Bruce Mayhew</div>                    \r\n");
      out.write("\t                 <div align=\"center\" class=\"style2\">Laurence Casey</div>                    \r\n");
      out.write("\t                 <div align=\"center\" class=\"style2\">David Anderson</div>                    \r\n");
      out.write("\t                 <div align=\"center\" class=\"style2\">Eric Sheridan</div>\r\n");
      out.write("\t              </td>\r\n");
      out.write("                  <td><div align=\"center\" class=\"style2\">Aspect Security <br />\r\n");
      out.write("                  (http://www.aspectsecurity.com) </div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td><div align=\"center\" class=\"style2\">Alex Smolen <br />\r\n");
      out.write("                  (http://www.parasoft.com) </div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td><div align=\"center\" class=\"style2\">Rogan Dawes <br />\r\n");
      out.write("                  (http://dawes.za.net/rogan) </div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td><div align=\"center\" class=\"style2\">Chuck Willis<br />\r\n");
      out.write("                  (http://www.securityfoundry.com) </div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td height=\"25\" valign=\"bottom\"><div align=\"center\"><span class=\"style1\">Documentation Contributer</span></div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("                <tr>\r\n");
      out.write("                  <td><div align=\"center\" class=\"style2\">Robert Sullivan<br />\r\n");
      out.write("                  (http://www.unitedhealthgroup.com/) </div></td>\r\n");
      out.write("                </tr>\r\n");
      out.write("            </table>\r\n");
      out.write("\t\t\t<form id=\"form\" name=\"form\" method=\"post\" action=\"attack\">\r\n");
      out.write("\t    \t\t\t<div align=\"center\">  \r\n");
      out.write("    \t\t\t        <input type=\"submit\" name=\"start\" value=\"Start\" />\r\n");
      out.write("\t    \t\t\t</div>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t  </div>\r\n");
      out.write("\t  </div>\r\n");
      out.write("\t\t<div id=\"warning\"> 경고!<br /> \r\n");
      out.write("\t      이 프로그램을 실행하는 동안 당신의 컴퓨터는 공격에 아주 취약해집니다. \r\n");
      out.write("\t\t  이 프로그램을 사용하는 동안에는 네트워크의 연결을 끊어주시는게 좋습니다.\r\n");
      out.write("\t\t  <br>\r\n");
      out.write("\t\t  <br>\r\n");
      out.write("\t\t  이 프로그램은 오직 교육적인 목적을 위한 것입니다.\r\n");
      out.write("\t\t  허가 없이 이 기술들을 사용한다면 해고, 손해배상 및 형사처벌을 당할 수 있습니다.\r\n");
      out.write("\t  </div>\r\n");
      out.write("\t\t<div id=\"bottom\">\r\n");
      out.write("\t\t\t<div align=\"center\"><img src=\"images/logos/owasp.jpg\" alt=\"OWASP Foundation\" width=\"238\" height=\"43\" longdesc=\"http://www.owasp.org\" /><a href=\"http://www.owasp.org\"><br />\r\n");
      out.write("Project WebGoat </div>\r\n");
      out.write("\t  \t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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

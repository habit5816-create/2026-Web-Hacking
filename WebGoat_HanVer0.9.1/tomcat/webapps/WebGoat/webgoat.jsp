  <%@ page contentType="text/html;charset=euc-kr"  language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.*, java.util.*" 
	errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=euc-kr" />
<title>WebGoat V4</title>
<link rel="stylesheet" href="css/webgoat.css" type="text/css" />
</head>

<body>


	<div id="wrap">
		<div id="top"></div>
		<div id="start">
		  <p>WebGoat를 이용해 주셔서 감사합니다.!</p>
		  <p>이 프로그램은 일반적인 웹 어플리케이션 결함들을 보여줍니다.
		  이 예제들은 어플리케이션 침투 테스트 기술들의 실전을 제공할 것입니다.</p>
		  <div id="team">
			<table width="460" border="0" align="center" class="lessonText">
                <tr>
                  <td width="50%"><div align="center"><span class="style1">WebGoat Design Team </span></div></td>
                  <td width="50%"><div align="center"><span class="style1">Lesson Contributers </span></div></td>
                </tr>
                <tr>
                  <td rowspan="6" valign="top">
                  	<div align="center" class="style2">Jeff Williams</div>                    
	                 <div align="center" class="style2">Bruce Mayhew</div>                    
	                 <div align="center" class="style2">Laurence Casey</div>                    
	                 <div align="center" class="style2">David Anderson</div>                    
	                 <div align="center" class="style2">Eric Sheridan</div>
	              </td>
                  <td><div align="center" class="style2">Aspect Security <br />
                  (http://www.aspectsecurity.com) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2">Alex Smolen <br />
                  (http://www.parasoft.com) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2">Rogan Dawes <br />
                  (http://dawes.za.net/rogan) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2">Chuck Willis<br />
                  (http://www.securityfoundry.com) </div></td>
                </tr>
                <tr>
                  <td height="25" valign="bottom"><div align="center"><span class="style1">Documentation Contributer</span></div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2">Robert Sullivan<br />
                  (http://www.unitedhealthgroup.com/) </div></td>
                </tr>
            </table>
			<form id="form" name="form" method="post" action="attack">
	    			<div align="center">  
    			        <input type="submit" name="start" value="Start" />
	    			</div>
			</form>
		  </div>
	  </div>
		<div id="warning"> 경고!<br /> 
	      이 프로그램을 실행하는 동안 당신의 컴퓨터는 공격에 아주 취약해집니다. 
		  이 프로그램을 사용하는 동안에는 네트워크의 연결을 끊어주시는게 좋습니다.
		  <br>
		  <br>
		  이 프로그램은 오직 교육적인 목적을 위한 것입니다.
		  허가 없이 이 기술들을 사용한다면 해고, 손해배상 및 형사처벌을 당할 수 있습니다.
	  </div>
		<div id="bottom">
			<div align="center"><img src="images/logos/owasp.jpg" alt="OWASP Foundation" width="238" height="43" longdesc="http://www.owasp.org" /><a href="http://www.owasp.org"><br />
Project WebGoat </div>
	  	</div>
	</div>
</body>
</html>

<%@ page contentType="text/html; charset=euc-kr" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.*, java.util.*" 
	errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>WebGoat V4</title>
<link rel="stylesheet" href="css/webgoat_challenge.css" type="text/css" />
</head>

<body>
	<div id="wrap_ch">
		<div id="top_ch"><img src="images/header/header.jpg" width="500" height="86" /></div>
		<div id="start_ch">
		  <p>WebGoat¸¦ ÀÌ¿ëÇØ ÁÖ¼Å¼­ °¨»çÇÕ´Ï´Ù.!</p>
		  <p>ÀÌ ÇÁ·Î±×·¥Àº ÀÏ¹ÝÀûÀÎ À¥ ¾îÇÃ¸®ÄÉÀÌ¼Ç °áÇÔµéÀ» º¸¿©ÁÝ´Ï´Ù.</p>
		  <p>¿¹Á¦µéÀº ¾îÇÃ¸®ÄÉÀÌ¼Ç Ä§Åõ Å×½ºÆ® ±â¼úµéÀÇ ½ÇÀüÀ» Á¦°øÇÒ °ÍÀÔ´Ï´Ù.</p>
		  <div id="team_ch">
		  	<table width="400" border="0" align="center" class="lessonText">
                <tr>
                  <td width="50%"><div align="center"><span class="style1_ch">WebGoat Design Team </span></div></td>
                  <td width="50%"><div align="center"><span class="style1_ch">Lesson Contributers </span></div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2_ch">Jeff Williams</div></td>
                  <td><div align="center" class="style2_ch">Aspect Security <br />
                  (http://www.aspectsecurity.com) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2_ch">Bruce Mayhew</div></td>
                  <td><div align="center" class="style2_ch">Alex Smolen <br />
                  (http://www.parasoft.com) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2_ch">Laurence Casey</div></td>
                  <td><div align="center" class="style2">Rogan Dawes <br />
                  (http://dawes.za.net/rogan) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style2_ch">David Anderson</div></td>
                  <td><div align="center" class="style2_ch">Chuck Willis<br />
                  (http://www.securityfoundry.com) </div></td>
                </tr>
                <tr>
                  <td height="18"><div align="center" class="style2_ch">Eric Sheridan</div></td>
                  <td><div align="center"><span class="style2_ch"></span></div></td>
                </tr>
            </table>
			<form id="form" name="form" method="post" action="attack">
	    			<div align="center">  
    			        <input type="submit" name="start" value="Start" />
	    			</div>
			</form>
		  </div>
	  </div>
		<div id="warning"> °æ°í<br /> 
	      ÀÌ ÇÁ·Î±×·¥À» ½ÇÇàÇÏ´Â µ¿¾È ´ç½ÅÀÇ ÄÄÇ»ÅÍ´Â °ø°Ý¿¡ ¾ÆÁÖ Ãë¾àÇØÁý´Ï´Ù. 
		  ÀÌ ÇÁ·Î±×·¥À» »ç¿ëÇÏ´Â µ¿¾È¿¡´Â ³×Æ®¿öÅ©ÀÇ ¿¬°áÀ» ²÷¾îÁÖ½Ã´Â°Ô ÁÁ½À´Ï´Ù.
		  <br>
		  <br>
		  ÀÌ ÇÁ·Î±×·¥Àº ¿ÀÁ÷ ±³À°ÀûÀÎ ¸ñÀûÀ» À§ÇÑ °ÍÀÔ´Ï´Ù.
		  Çã°¡ ¾øÀÌ ÀÌ ±â¼úµéÀ» »ç¿ëÇÑ´Ù¸é ÇØ°í, ¼ÕÇØ¹è»ó ¹× Çü»çÃ³¹úÀ» ´çÇÒ ¼ö ÀÖ½À´Ï´Ù.
	  </div>
		<div id="bottom_ch">
			<div align="center"><img src="images/logos/owasp.jpg" alt="OWASP Foundation" width="166" height="29" longdesc="http://www.owasp.org" /><a href="http://www.owasp.org"><br />
&copy; 2006 Project WebGoat </div>
	  	</div>
	</div>
</body>
</html>

package euc_kr.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class BasicAuthentication extends LessonAdapter
{

	private static final String EMPTY_STRING = "";
	private static final String WEBGOAT_BASIC = "webgoat_basic(webgoat기본)";
	private static final String AUTHORIZATION = "Authorization(인증)";
	private static final String ORIGINAL_AUTH = "Original_Auth(원본 인증)";
	private static final String ORIGINAL_USER = "Original.user(고유의 유저)";
	private static final String BASIC = "basic(기본)";
	private static final String JSESSIONID = "JSESSIONID(임의의 세션)";
	private final static String HEADER_NAME =  "header(헤더)";
	private final static String HEADER_VALUE = "value(값)";
	
	/**
	 * Save the session so it can be used in a staged lesson
	 */
	private WebSession session = null;
	
	
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		session = s;
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		String headerName = null;
		String headerValue = null;
		try
		{
			headerName = new String( s.getParser().getStringParameter( HEADER_NAME, EMPTY_STRING ) );
			headerValue = new String( s.getParser().getStringParameter( HEADER_VALUE, EMPTY_STRING ) );
			
			//<START_OMIT_SOURCE>
			// FIXME: This won;t work for CBT, we need to use the UserTracker
			//Authorization: Basic Z3Vlc3Q6Z3Vlc3Q=
			if ( headerName.equals(AUTHORIZATION) && 
				( headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
			{
				getLessonTracker(s).setStage(2);
				return doStage2( s );
			}
			else
			{
				if ( headerName.length() > 0 && !headerName.equals(AUTHORIZATION))
				{
					s.setMessage("Basic Authentication header name is incorrect.(기본 인증 헤더의 이름이 올바르지 않습니다.)");
				}
				if( headerValue.length() > 0 && !(headerValue.equals("guest:guest") || headerValue.equals("webgoat:webgoat")))
				{
					s.setMessage("Basic Authentication header value is incorrect.(기본 인증 헤더 값이 올바르지 않습니다.)");
					
				}
			}
			//<END_OMIT_SOURCE>

			Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}

			TR row1 = new TR();
			TR row2 = new TR();
			row1.addElement( new TD( new StringElement( "What is the name of the authentication header(인증 헤더의 이름은): " ) ) );
			row2.addElement( new TD( new StringElement( "What is the decoded value of the authentication header(인증 헤더의 값은 어떻게 디코딩되었는가?): " ) ) );
			
			row1.addElement( new TD( new Input( Input.TEXT, HEADER_NAME, headerName.toString() )));
			row2.addElement( new TD( new Input( Input.TEXT, HEADER_VALUE, headerValue.toString() )));
			
			t.addElement( row1 );
			t.addElement( row2 );
			
			ec.addElement( t );
			ec.addElement( new P() );
			
			Element b = ECSFactory.makeButton( "Submit" );
			ec.addElement( b );
			

		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating (오류 발생) " + this.getClass().getName() );
			e.printStackTrace();
		}


		return ( ec );
	}

	protected Element doStage2( WebSession s ) throws Exception
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if ( s.getRequest().isUserInRole(WEBGOAT_BASIC) )
			{
				String originalUser = getLessonTracker(s).getLessonProperties().getProperty(ORIGINAL_USER,EMPTY_STRING);
				getLessonTracker(s, originalUser).setCompleted(true);
				getLessonTracker(s, originalUser).setStage(1);
				getLessonTracker(s, originalUser).store(s, this);
				makeSuccess(s);
				s.setMessage("Close your browser and login as " + originalUser + " to get your green stars back.");
				return ec;
			}
			else
			{
				// If we are still in the ORIGINAL_USER role see if the Basic Auth header has been manipulated
				String originalAuth = getLessonTracker(s).getLessonProperties().getProperty(ORIGINAL_AUTH, EMPTY_STRING);
				String originalSessionId = getLessonTracker(s).getLessonProperties().getProperty(JSESSIONID,s.getCookie(JSESSIONID));

				// store the original user info in the BASIC properties files
				if ( originalSessionId.equals(s.getCookie(JSESSIONID)) )
			    {
					// Store the original user name in the "basic" user properties file.  We need to use
					// the original user to access the correct properties file to update status.
					// store the initial auth header
					getLessonTracker(s).getLessonProperties().setProperty(JSESSIONID, originalSessionId);
					getLessonTracker(s).getLessonProperties().setProperty(ORIGINAL_AUTH, s.getHeader(AUTHORIZATION) );
					getLessonTracker(s, BASIC).getLessonProperties().setProperty(ORIGINAL_USER, s.getUserName() );
					getLessonTracker(s, BASIC).setStage(2);
					getLessonTracker(s, BASIC).store(s, this, BASIC);
			    }

				s.setMessage("Congratulations, you have figured out the mechanics of basic authentication.(축하합니다, 당신은 기본인증의 매커니즘을 알아냈습니다.)" );
				s.setMessage("&nbsp;&nbsp;- Now you must try to make WebGoat reauthenticate you as:(WebGoat에서 당신을 다시 인증하기 위해 시도해야 합니다.)  ");
				s.setMessage("&nbsp;&nbsp;&nbsp;&nbsp;- username:  basic");
				s.setMessage("&nbsp;&nbsp;&nbsp;&nbsp;- password:  basic");

				// If the auth header is different but still the original user - tell the user
				// that the original cookie was posted bak and basic auth uses the cookie before the
				// authorization token
				if ( !originalAuth.equals("") && !originalAuth.equals( s.getHeader(AUTHORIZATION) ))
				{
					ec.addElement("You're almost there!  You've modified the(거의 다 왔습니다! 수정하십시오) " + AUTHORIZATION + " header but you are(하지만 당신은) " +
							"still logged in as " + s.getUserName() + ".  Look at the request after you typed in the 'basic'(보고 요청에 당신의 'basic'를 입력한 후) " +
							"user credentials and submitted the request.(사용자의 자격 증명 요청과 실행을 요구합니다.)  Remember the order of events that occur during Basic Authentication.(기본인증이 되는 동안, 이벤트를 기억 하십시오.)");
				}
				else if (!originalSessionId.equals(s.getCookie(JSESSIONID)))
				{
					ec.addElement("You're really close!  Changing the session cookie caused the server to create a new session for you.  This did not cause the server to reauthenticate you.(당신은 실제로 잠급니다! 당신이 새로운 세션과 쿠키를 생성하여 변경합니다. 이것은 믿을만한 원인이 아닙니다.)  " +
							"When you figure out how to force the server to perform an authentication request, you have to authenticate as(서버 인증 요청을 강제로 수행하는 방법을 알아 내고 당신을 증명할 수 있습니다.) :<br><br>" +
							"&nbsp;&nbsp;&nbsp;&nbsp;user name: basic<br> " +
							"&nbsp;&nbsp;&nbsp;&nbsp;password: basic<br>");
				} 
				else
				{
					ec.addElement("Use the hints!  One at a time... (한번에 하나씩 힌트를 사용하십시오.)");
				}

			}

		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating(에러 발생) " + this.getClass().getName() );
			e.printStackTrace();
		}


		return ( ec );
	}

	
	/**
	 *  Gets the category attribute of the ForgotPassword object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{

		return AbstractLesson.A3;
	}


	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List hints = new ArrayList();
//		int stage = getLessonTracker(session, BASIC).getStage();

//		switch ( stage )
//		{
//				case 1:
					hints.add( "Basic authentication uses a cookie to pass the credentials.(쿠키를 사용하여 기본 인증 자격 증명을 전달합니다.) " +
					   	"Use a proxy to intercept the request.  Look at the cookies.(프록시를 사용하여 응답을 방해합니다. 쿠키를 조사 합니다.)");
					hints.add( "Basic authentication uses Base64 encoding to 'scramble' the('scramble'에 Base64를 사용하여 기본 인증을 인코딩 하고) " +
			   		   	"user's login credentials.(사용자의 로그인 자격을 증명합니다.)");
					hints.add( "Basic authentication uses 'Authorization' as the cookie name to ('승인'된 쿠키 이름의 기본인증을 사용)" +
					   	"store the user's credentials.(저장하는 사용자의 자격을 증명합니다.)");
					hints.add( "Use WebScarab -> Tools -> Transcoder to Base64 decode the(Webscrab을 사용 -> 툴 사용 ->Base64로 디코딩하여 코드 변환)  " +
					   	"the value in the Authorization cookie.(인증된 쿠키의 값을 사용)");
//					break;
//				case 2:
					hints.add( "Basic authentication uses a cookie to pass the credentials.(쿠키를 사용하여 기본인증 자격 증명을 전달합니다.) " +
						"Use a proxy to intercept the request.  Look at the cookies.(프록시를 사용하여 응답을 방해합니다. 쿠키를 조사 합니다.)");
					hints.add( "Before the WebServer requests credentials from the client, the current(클라이언트에서 웹서버 자격 증명을 요청하기 전에, 현재의) " +
						"session is checked for validitity.(정당성을 위해 세션을 체크)");
					hints.add( "If the session is invalid the webserver will use the basic authentication credentials(세션이 유효하지 않은 경우에는 기본 인증 자격 증명을 웹 서버에 사용합니다)");
					hints.add( "If the session is invalid and the basic authentication credentials are invalid,(세션이 유효하지 않은 경우, 기본 인증 자격 증명이 무효,) " +
						"new credentials will be requested from the client.(새 자격 증명을 클라이언트로부터 요청합니다.)");
					hints.add( "Intercept the request and corrupt the JSESSIONID and the Authorization header.(응답을 가로채고 JSESSIONID를 손상시켜서 해더를 인증함.");
//					break;
//		}

		return hints;
	}


	private final static Integer DEFAULT_RANKING = new Integer(100);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
	

	/**
	 *  Gets the title attribute of the HelloScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Basic Authentication" );
	}
	

}


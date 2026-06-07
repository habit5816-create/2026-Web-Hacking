package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
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
public class FailOpenAuthentication extends WeakAuthenticationCookie
{
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		boolean logout = s.getParser().getBooleanParameter( LOGOUT, false );

		if ( logout )
		{
			s.setMessage( "Goodbye!" );
			s.eatCookies();

			return ( makeLogin( s ) );
		}

		try
		{
			String username = "";
			String password = "";

			try
			{
				username = s.getParser().getRawParameter( USERNAME );
				password = s.getParser().getRawParameter( PASSWORD );

				// if credentials are bad, send the login page
				if ( !"webgoat".equals( username ) || !password.equals( "webgoat" ) )
				{
					s.setMessage( "사용자 이름과 비밀 번호를 입력이 잘못되었습니다." );

					return ( makeLogin( s ) );
				}
			}
			catch ( Exception e )
			{
				// The parameter was omitted. set fail open status complete
				if ( username.length() > 0 && e.getMessage().indexOf( "not found") != -1 )
				{	
					if ( ( username != null ) && ( username.length() > 0 ) )
					{
						makeSuccess( s );
						return ( makeUser( s, username, "열기 실패 오류" ) );
					}
				}
			}

			// Don't let the fail open pass with a blank password.
			if ( password.length() == 0 )
			{
				// We make sure the username was submitted to avoid telling the user an invalid
				// username/password was entered when they first enter the lesson via the side menu.
				// This also suppresses the error if they just hit the login and both fields are empty.
				if ( username.length() != 0)
				{	
					s.setMessage( "사용자 이름과 비밀 번호를 입력이 잘못되었습니다." );
				}

				return ( makeLogin( s ) );
				
			} 
			
			// otherwise authentication is good, show the content
			if ( ( username != null ) && ( username.length() > 0 ) )
			{
				return ( makeUser( s, username, "Parameters.  파일을 열수가 없습니다.." ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
		}

		return ( makeLogin( s ) );
	}


	/**
	 *  Gets the category attribute of the FailOpenAuthentication object
	 *
	 * @return    The category value
	 */
	public Category getDefaultCategory()
	{
		return AbstractLesson.A7;
	}


	/**
	 *  Gets the hints attribute of the AuthenticateScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "당신은 잘못된 인증과정중 오류를 없앨 수 있습니다. ." );
		hints.add( "당신은 인증 파라미터의 값, 길이, 존재를 변경할 수 있습니다." );
		hints.add( "다음의 파라미터 값을 완저닣 없애 보십시요 <A href=\"http://www.owasp.org/development/webscarab\">WebScarab</A>." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the FailOpenAuthentication object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		return "문제 처리 매커님증의 오류를 이용하여 인증 할 수 있습니다. " +
			"'으로 webgoat'사용자가 비밀 번호를 입력하지 않고있습니다. 사용자가 webgoat의 비밀번호를 입력하지 않고 " +
			"로그인 하십시요.";
	}




	private final static Integer DEFAULT_RANKING = new Integer(20);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
	/**
	 *  Gets the title attribute of the AuthenticateScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Bypass a Fail Open Authentication Scheme" );
	}
}


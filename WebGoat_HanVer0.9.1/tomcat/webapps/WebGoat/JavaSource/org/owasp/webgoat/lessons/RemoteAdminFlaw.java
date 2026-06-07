package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
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
public class RemoteAdminFlaw extends LessonAdapter
{

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		if ( s.completedHackableAdmin() )
		{
			makeSuccess( s );		
		}
		else 
		{
			ec.addElement( "WebGoat은 관리자 인터페이스를 가지고있다.  To 'complete' 이과는 당신이 학습해야 한다. "
					+ "WebGoat를 위한 관리 영역에 접근하는 방법.");
		}
		return ec;

	}


	/**
	 *  Gets the category attribute of the ForgotPassword object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{

		return AbstractLesson.A2;
	}

	/**
	 *  Gets the hints attribute of the HelloScreen object
	 *
	 * @return    The hints value
	 */
	public List getHints()
	{
		List hints = new ArrayList();
		hints.add( "WebGoat은 2개의 관리자 인터페이스가 있다." );
		hints.add( "WebGoat은  'heckable'이라는 URL 파라미터를 통해 조정되어지는 하나의 admin을 가지고 있다" );
		hints.add( "WebGoat은 서버를 통해서 제어되어지는 관리자 인터페이스를 가지고있다." );
		hints.add( "소스를 따르시오!" );

		return hints;
	}



	private final static Integer DEFAULT_RANKING = new Integer(15);

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
		return ( "Remote Admin Access(Admin 접근)" );
	}
	


}


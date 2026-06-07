package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;

import org.owasp.webgoat.session.*;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class ThreadSafetyProblem extends LessonAdapter
{
	private final static String USER_NAME = "username";
	private Connection connection = null;
	private static String currentUser;
	private String originalUser;


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( new StringElement( "사용자 이름 입력: " ) );
			ec.addElement( new Input( Input.TEXT, USER_NAME, "" ) );
			currentUser = s.getParser().getRawParameter( USER_NAME, "" );
			originalUser = currentUser;
			
			// Store the user name
			String user1 = new String( currentUser );
			
			Element b = ECSFactory.makeButton( "입력" );
			ec.addElement( b );
			ec.addElement( new P() );

			if ( !"".equals( currentUser ) )
			{
				Thread.sleep( 1500 );

				// Get the users info from the DB
				String query = "SELECT * FROM user_system_data WHERE user_name = '" + currentUser + "'";
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ec.addElement("Account information for user: " + originalUser + "<br><br>");
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
				}
				else
				{
					s.setMessage("'" + currentUser + "' is not a user in the WebGoat database.");
				}
			}
			if ( !user1.equals( currentUser ) )
			{
				makeSuccess( s );
			}

		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}


	/**
	 *  Gets the hints attribute of the ConcurrencyScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "웹 어플리케이션은 동시에 많은 HTTP 요청을 다룹니다." );
		hints.add( "개발자는 thread-safe 변수를 사용하지 않았습니다." );
		hints.add( "자바소스코드나 'currentUser' 변수의 흔적을 보십시오" );
		hints.add( "두개의 브라우저를 열고 하나엔 'jeff' 다른 하나엔 'dave'를 입력하시오." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the ThreadSafetyProblem object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		
		String instructions = "사용자는 이 웹 어플리케이션에서 concurrency error를 이용하여" + 
							  "동시에 같은 시도를 하여 다른 사용자의 로그인 정보를 보아야 합니다. " +
							  "<b>또, 두개의 브라우저를 이용하여야 합니다.</b>. 정확한 사용자 " +
							  "의 이름은 'jeff' 와 'dave' 입니다." +
							  "<p>사용자의 정보에 접근하기 위하여 사용자 이름을 입력하시오.";

		return (instructions );
	}


	private final static Integer DEFAULT_RANKING = new Integer(80);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	protected Category getDefaultCategory()
	{
		return AbstractLesson.GENERAL;
	}
	
	/**
	 *  Gets the title attribute of the ConcurrencyScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Exploit Thread Safety Problems" );
	}


	/**
	 *  Constructor for the ConcurrencyScreen object
	 *
	 * @param  s  Description of the Parameter
	 */
	public void handleRequest( WebSession s )
	{
		try
		{
			super.handleRequest( s );

			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}
		}
		catch ( Exception e )
		{
			System.out.println( "Exception caught: " + e );
			e.printStackTrace( System.out );
		}
	}
}


package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.DatabaseUtilities;
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
public class SqlStringInjection extends LessonAdapter
{
	private final static String ACCT_NAME = "account_name";
	private static Connection connection = null;
	private static String STAGE = "stage";
	private String accountName;

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		return injectableQuery( s );
	}
	
	protected Element doStage2( WebSession s ) throws Exception
	{
		return parameterizedQuery( s);
	}

	
	protected Element injectableQuery( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeAccountLine( s ) );

			String query = "SELECT * FROM user_data WHERE last_name = '" + accountName +"'";
			ec.addElement( new PRE( query ) );

			try
			{
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one user they succeeded
					if ( results.getRow() >= 6 )
					{
						makeSuccess( s );
						getLessonTracker(s).getLessonProperties().setProperty(STAGE,"2");
						s.setMessage("이 연습은 쿼리변수를 초과하여 공격이 시작된다.");
					}
				}
				else 
				{
					ec.addElement( "결과가 맞지않다.  다시 시도하시요." );
				}
			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement( sqle.getMessage() ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "에러 발생 " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}
	

	protected Element parameterizedQuery( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		ec.addElement("현재 당신은 SQL injection 실행에 성공하였다, " +
				" 같은 변수쿼리 공격의 형태로 시도해보아라. " +
				" 입력창에 restart타입은 만약 당신이 injectable query를 되돌려주는것을 하는것이다 ");
		if ( s.getParser().getRawParameter( ACCT_NAME, "YOUR_NAME" ).equals("restart"))
		{
			getLessonTracker(s).getLessonProperties().setProperty(STAGE,"1");
			return( injectableQuery(s));
		}
		
		ec.addElement( new BR() );
		
		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeAccountLine( s ) );

			String query = "SELECT * FROM user_data WHERE last_name = ?";
			ec.addElement( new PRE( query ) );

			try
			{
				PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.setString(1, accountName);
				ResultSet results = statement.executeQuery();

				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					
					// If they get back more than one user they succeeded
					if ( results.getRow() >= 6 )
					{
						makeSuccess( s );
					}
				}
				else 
				{
					ec.addElement( "결과가 맞지않다.  다시 시도하시요." );
				}
			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement( sqle.getMessage() ) );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "에러 발생 " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}

	protected Element makeAccountLine( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement( new P().addElement( "당식의 마지막 이름을 넣으시요: " ) );

		accountName = s.getParser().getRawParameter( ACCT_NAME, "Your Name" );
		Input input = new Input( Input.TEXT, ACCT_NAME, accountName.toString() );
		ec.addElement( input );

		Element b = ECSFactory.makeButton( "Go!" );
		ec.addElement( b );

		return ec;

	}
	
	
	/**
	 *  Gets the category attribute of the SqNumericInjection object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A6;
	}


	/**
	 *  Gets the hints attribute of the DatabaseFieldScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "이 응용프로그램은 SQL명령 형식의 끝에 입력그리고 삽입으로 취득한다." );
		hints.add( "WebGoat Quere 코드는 본질은 이렇게만들어졌다:<br><br> " +
					"\"SELECT * FROM user_data WHERE last_name = \" + accountName " );
		hints.add( "SQL명령문에 합성하여 AND 그리고 OR함께 접합할수있는 다양한 테스트를 만든다." +
					"SQL명령문에 항상 정당한 풀이방법을 추가하여 시도한다.");
		hints.add( "[ smith' OR '1' = '1 ]넣어 시도해보아라." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the SqNumericInjection object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "이 표에서 한유저가 사람들의 개인카드 번호 보는것을 허가한다.   " +
			" SQL string 삽입으로 존재하는 모든 개인카드 번호를 결과를 보여주어라. " +
			"유저 이름은 'Smith'.";

		return ( instructions );
	}



	private final static Integer DEFAULT_RANKING = new Integer(75);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the DatabaseFieldScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "어떻게 String SQL Injection 실행하는가? " );
	}


	/**
	 *  Constructor for the DatabaseFieldScreen object
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
			System.out.println( "예외를 받다: " + e );
			e.printStackTrace( System.out );
		}
	}
}


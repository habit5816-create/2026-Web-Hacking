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
public class SqlNumericInjection extends LessonAdapter
{
	private final static String ACCT_NUM = "account_number";
	private static Connection connection = null;
	private String accountNumber;

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

			ec.addElement( makeAccountLine(s) );

			String query = "SELECT * FROM user_data WHERE userid = " + accountNumber ;
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
						getLessonTracker(s).setStage(2);
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

		ec.addElement("현재 당신은 SQL 인젝션에 성공적으로 이루어졌다 , 같은 방법의 " 
				" 타입으로 파라미터 쿼리 공격하여라.  Type 'restart' in the input field if you wish to " +
				" to return to the 주입가능한 퀴리에.");
		if ( s.getParser().getRawParameter( ACCT_NUM, "101" ).equals("restart"))
		{
			getLessonTracker(s).setStage(1);
			return( injectableQuery(s));
		}
		
		ec.addElement( new BR() );

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			ec.addElement( makeAccountLine(s) );

			String query = "SELECT * FROM user_data WHERE userid = ?" ;
			ec.addElement( new PRE( query ) );

			try
			{
				PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				statement.setString(1, accountNumber);
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
		
		ec.addElement( new P().addElement( "당신의 계정 숫자를 넣어라: " ) );

		accountNumber = s.getParser().getRawParameter( ACCT_NUM, "101" );
		Input input = new Input( Input.TEXT, ACCT_NUM, accountNumber.toString() );
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
					"\"SELECT * FROM user_data WHERE userid = \" + accountNumber " );
		hints.add( "SQL명령문에 합성하여 AND 그리고 OR함께 접합할수있는 다양한 테스트를 만든다." +
					"SQL명령문에 항상 정당한 풀이방법을 추가하여 시도한다.");
		hints.add( "[ 101 OR 1 = 1 ] 넣어 시도해보아라." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the SqNumericInjection object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "이 표에서 한유저가 사람들의 개인카드 번호 보는것을 허가한다. " +
			" SQL string 삽입으로 존재하는 모든 개인카드 번호를 결과를 보여주어라." ;

		return ( instructions );
	}




	private final static Integer DEFAULT_RANKING = new Integer(70);

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
		return ( "Numeric SQL Injection은 어떻게 실행되는가?" );
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


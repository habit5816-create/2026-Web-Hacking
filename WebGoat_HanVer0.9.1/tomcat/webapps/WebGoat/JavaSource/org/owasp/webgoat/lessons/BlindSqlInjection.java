package euc_kr.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;


/**
 *  Copyright (c) 2005 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Chuck Willis <a href="http://www.securityfoundry.com">Chuck's web site</a> (this lesson is heavily based on Jeff Williams' SQL Injection lesson
 * @created    January 14, 2005
 */
public class BlindSqlInjection extends LessonAdapter
{
	private final static String ACCT_NUM = "account_number(계좌 번호)";
	private final static int TARGET_ACCT_NUM = 15613;
	private static Connection connection = null;


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

			ec.addElement( new P().addElement( "Enter your Account Number(당신의 계좌 번호를 입력하시오.): " ) );

			String accountNumber = s.getParser().getRawParameter( ACCT_NUM, "101" );
			Input input = new Input( Input.TEXT, ACCT_NUM, accountNumber.toString() );
			ec.addElement( input );

			Element b = ECSFactory.makeButton( "Go!" );
			ec.addElement( b );

			String query = "SELECT * FROM user_data WHERE userid = " + accountNumber ;
			String answer_query;
			if(runningOnWindows()) {
				answer_query = "SELECT TOP 1 first_name FROM user_data WHERE userid = " + TARGET_ACCT_NUM; 
			} else {
				answer_query = "SELECT first_name FROM user_data WHERE userid = " + TARGET_ACCT_NUM; 
			}
			
			try
			{
				Statement answer_statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet answer_results = answer_statement.executeQuery( answer_query );
				answer_results.first();
				if( accountNumber.toString().equals(answer_results.getString(1))) {
					makeSuccess( s );
				} else {
				
					Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
					ResultSet results = statement.executeQuery( query );
					
					if ( ( results != null ) && ( results.first() == true ) )
					{
						ec.addElement( new P().addElement("Account number is valid (계좌 번호 값)"));
					} else {
						ec.addElement( new P().addElement("Invalid account number(계좌 번호가 잘못되었습니다.)"));
					}
				}
			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement("An error occurred, please try again.(오류가 발생했습니다 다시 시도해 보시기 바랍니다.)"));	
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
	 *  Gets the category attribute of the SqlInjection object
	 *
	 * @return    The category value
	 */
	public Category getCategory()
	{
		return AbstractLesson.A6;
	}


	/**
	 *  Gets the credits attribute of the AbstractLesson object
	 *
	 * @return    The credits value
	 */
	public Element getCredits()
	{
		return new StringElement("By Chuck Willis (edited 14 Dec 05 - Hints need to updated for non-Windows systems) 척 윌리스 (수정 2005년 12월 14일 - 윈도우 시스템이 아닌 것을 위하여 업데이트 해야 합니다.) ");
	}

	/**
	 * 
	 * Determines the OS that WebGoat is running on.  Needed because different DB backends
	 * are used on the different OSes (Access on Windows, InstantDB on others)
	 * 
	 * @return true if running on Windows, false otherwise
	 */
	private boolean runningOnWindows() {
		String os = System.getProperty("os.name","Windows");
		if ( os.toLowerCase().indexOf("window") != -1  )
		{			
			return true; 
		}
		else 
		{		
            return false;
		}
 	}
	
	
	/**
	 *  Gets the hints attribute of the DatabaseFieldScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		if (runningOnWindows()) {
			hints.add( "Compound SQL statements can be made by joining multiple tests with keywords like AND and OR.(AND와 OR를 사용하여 다수의 SQL 혼합 구문을 만들고 여러가지를 시험해 볼 수 있습니다.) " +
					"Create a SQL statement that you can use as a true/false test and then (SQL 구문 만들기로 당신은 참/거짓 을 시험해보고 )" +
					"select the first character of the target element and do a start narrowing (이 문서가 적용되는 요소를 선택한 다음 첫번째 문자의 범위를 축소하도록 ) " +
					"down the character using > and < (다음 문자를 >,<를 사용하여 비교함) " + 
					"<br><br>The backend database is Microsoft Access.  Keep that in mind if you research SQL functions (Microsoft Access의 최종 데이터 베이스 입니다. 명심하십시오 SQL 기능을 연구하는 경우)" +
					"on the Internet since different databases use some different functions and syntax. (인터넷의 다른 데이터베이스들은 서로 조금 다른 기능과 구문을 사용합니다.)");
			hints.add( "This is the code for the query being built and issued by WebGoat(이 것은 WebGoat에서 제공하는 질문으로 이슈가 되었던 코드들입니다.):<br><br> " +
			"\"SELECT * FROM user_data WHERE userid = \" + accountNumber " );
			hints.add( "The application is taking your input and inserting it at the end of a pre-formed SQL command. (이 응용프로그램은 당신의 입력과 그것의 끝에 끼워넣기 위한 SQL 명령을 형성합니다.) "+
					"You will need to make use of the following SQL functions(당신은 다음과 같은 sql 함수를 사용해야합니다.): " + 
					"<br><br>SELECT - query for your target data and get a string (당신의 타겟 자료와 문자열을 얻는 질문입니다.) "+
					"<br><br>mid(string, start, length) - returns a (중반 (문자열, 시작, 길이) 를 반환)"
					+ "substring of string starting at the start character and going for length characters (시작 문자열의 시작 문자와 같은 문자를 찾기위해) "+
					"<br><br>asc(string) will return the ascii value of the first character in string (첫 문자의 ascii값을 사용하여 오름차순으로 (문자열)의 값을 반환합니다.) " +
					"<br><br>&gt and &lt - once you have a character's value, compare it to a choosen one(일단 하나의 선택된 자의 성질 값을 비교하여)");
			hints.add( "Example: is the first character of the first_name of userid (예: first_name의 회원 아이디의 첫번째문자는) " + TARGET_ACCT_NUM + " less than 'M' (ascii 77)?( 'M(ascii 77)'미만이다. " + 
					"<br><br>101 AND (asc( mid((SELECT first_name FROM user_data WHERE userid=" + TARGET_ACCT_NUM + ") , 1 , 1) ) < 77 ); " +
					"<br><br>If you get back that account number is valid, then yes.  If get back that the number is(만약 당신이 얻은 계좌번호가 정당한지 확인하여 맞다면, 다음 번호를 )" +
					"invalid then answer is no.(잘못된 응답이 나오지 않는다.");
			hints.add( "Another example: is the second character of the first_name of userid (다른 예 :first_name의 회원아이디 두번째 문자를) " + TARGET_ACCT_NUM + " greater than 'm' (ascii 109)? ('m (asscii 109)' 보다 크다) " + 
					"<br><br>101 AND (asc( mid((SELECT first_name FROM user_data WHERE userid=" + TARGET_ACCT_NUM + ") , 2 , 1) ) > 109 ); " +
					"<br><br>If you get back that account number is valid, then yes.  If get back that the number is (만약 당신이 얻은 계좌번호가 정당한지 확인하여 맞다면, 다음 번호를 )" +
					"invalid then answer is no.(잘못된 응답이 나오지 않는다.)");
		} else {
			hints.add("Compound SQL statements can be made by joining multiple tests with keywords like AND and OR. (AND와 OR를 사용하여 다수의 SQL 혼합 구문을 만들고 여러가지를 시험해 볼 수 있습니다.) " +
					"Create a SQL statement that you can use as a true/false test and then (SQL 구문 만들기로 당신은 참/거짓 을 시험해보고 )" +
					"select the first character of the target element and do a start narrowing (이 문서가 적용되는 요소를 선택한 다음 첫번째 문자의 범위를 축소하도록 )" +
					"down the character using > and < (다음 문자를 >,<를 사용하여 비교함)" );

			hints.add("The database backend is InstantDB.(데이터 베이스의 최종 완료는 InstantDB이다.) Here is a reference guide (이것에 대한 참조 가이드는 ) : <a href=\"http://www.instantdb.com/doc/syntax.html\" target=\"_blank\">http://www.instantdb.com/doc/syntax.html</a>");
			hints.add( "This is the code for the query being built and issued by WebGoat(이 것은 WebGoat에서 제공하는 질문으로 이슈가 되었던 코드들입니다.):<br><br> " +
				"\"SELECT * FROM user_data WHERE userid = \" + accountNumber " );
			hints.add( "THIS HINT IS FOR THE MS ACCESS DB.(이 힌트는 MS ACCESS DB에서 나왔습니다.)  IT NEEDS TO BE ALTERED FOR THE INSTANTDB BACKEND.(최종적인 INSTANDDB를 변경해야 합니다.) <br><br>The application is taking your input and inserting it at the end of a pre-formed SQL command.(이 응용프로그램은 당신의 입력과 그것의 끝에 끼워넣기 위한 SQL 명령을 형성합니다.) "+
					"You will need to make use of the following SQL functions(당신은 다음과 같은 sql 함수를 사용해야합니다): " + 
					"<br><br>SELECT - query for your target data and get a string (당신의 타겟 자료와 문자열을 얻는 질문입니다.)"+
					"<br><br>mid(string, start, length) - returns a (중반 (문자열, 시작, 길이) 를 반환)"
					+ "substring of string starting at the start character and going for length characters(시작 문자열의 시작 문자와 같은 문자를 찾기위해) "+
					"<br><br>asc(string) will return the ascii value of the first character in string (첫 문자의 ascii값을 사용하여 오름차순으로 (문자열)의 값을 반환합니다.)" +
					"<br><br>&gt and &lt - once you have a character's value, compare it to a choosen one(일단 하나의 선택된 자의 성질 값을 비교하여)");
			hints.add( "THIS HINT IS FOR THE MS ACCESS DB.(이 힌트는 MS ACCESS DB에서 나왔습니다.)  IT NEEDS TO BE ALTERED FOR THE INSTANTDB BACKEND.(최종적인 INSTANDDB를 변경해야 합니다.) <br><br>Example: is the first character of the first_name of userid(예 : first_name의 회원 아이디의 첫 번째 문자는) " + TARGET_ACCT_NUM + " less than 'M' (ascii 77)? ( 'M(ascii 77)'미만이다.)" + 
					"<br><br>101 AND (asc( mid((SELECT first_name FROM user_data WHERE userid=" + TARGET_ACCT_NUM + ") , 1 , 1) ) < 77 ); " +
					"<br><br>If you get back that account number is valid, then yes.  If get back that the number is(만약 당신이 얻은 계좌번호가 정당한지 확인하여 맞다면, 다음 번호를 )" +
					"invalid then answer is no.(잘못된 응답이 나오지 않는다.)");
			hints.add( "THIS HINT IS FOR THE MS ACCESS DB.(이 힌트는 MS ACCESS DB에서 나왔습니다.)  IT NEEDS TO BE ALTERED FOR THE INSTANTDB BACKEND.(최종적인 INSTANDDB를 변경해야 합니다.) <br><br> example: is the second character of the first_name of userid(예 :first_name의 회원아이디 두번째 문자를) " + TARGET_ACCT_NUM + " greater than 'm' (ascii 109)? ('m (asscii 109)' 보다 크다)" + 
					"<br><br>101 AND (asc( mid((SELECT first_name FROM user_data WHERE userid=" + TARGET_ACCT_NUM + ") , 2 , 1) ) > 109 ); " +
					"<br><br>If you get back that account number is valid, then yes.  If get back that the number is (만약 당신이 얻은 계좌번호가 정당한지 확인하여 맞다면, 다음 번호를 )" +
					"invalid then answer is no.(잘못된 응답이 나오지 않는다.)");
		}
		return hints;
	}


	/**
	 *  Gets the instructions attribute of the SqlInjection object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "The form below allows a user to enter an account number and determine if(아래의 양식을 통해 사용자가 입력하는 계좌 번호를 사용하고 있는지 확인) "+
		"it is valid or not.  Use this form to develop a true / false test check other entries in the database.(그것은 유효 여부를 확인합니다.테스트 데이베이스에 있는 양식을 확인하여 다른항목을 체크하여 참 / 거짓을 지닌 구문을 개발합니다.  "+
		"<br><br>Reference Ascii Values: 'A' = 65   'Z' = 90   'a' = 97   'z' = 122 (Ascii 값을 조회 하면 'A' = 65   'Z' = 90   'a' = 97   'z' = 122 입니다.)" + 
		"<br><br>The goal is to find the value of(정확한 값을 찾아내어) "+
		"the first_name in table user_data for userid (user_data의 회원 아이디와  first_name의 표를) " + TARGET_ACCT_NUM + ".  Put that name in the form to pass the lesson.(그 이름 학습의 형태를 통과합니다.)";

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
		return ( "How to Perform Blind SQL Injection" );
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
			System.out.println( "Exception caught(예외): " + e );
			e.printStackTrace( System.out );
		}
	}
}



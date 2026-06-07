/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;

import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

/**
 * @author asmolen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WsSqlInjection extends LessonAdapter {
	public final static String ccNumber = "신용카드번호";
	private final static String ACCT_NUM = "계정 번호";
	private String accountNumber;
	final static IMG CREDITS_LOGO = new IMG( "images/logos/parasoft.jpg" ).setAlt( "Parasoft" ).setBorder( 0 ).setHspace( 0 ).setVspace( 0 );
	private static Connection connection = null;
	/* (non-Javadoc)
	 * @see lessons.AbstractLesson#getMenuItem()
	 */
	static boolean completed;
	
	protected Category getDefaultCategory()
	{
		return AbstractLesson.WEB_SERVICES;
	}
	
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "브라우저나 웹서비스툴을 가지고 웹서비스정의언어(WSDL)에 연결을 시도하시오." );
		hints.add( "때때로 서버 측 코드는 웹 서비스 작동에 있어서 해당 요청을 실행하기 전에  " +
				"입력을 검증할 것이다. 웹 서비스에 직접 접근해서  " +
				" 체크를 우회하시오");
		hints.add( "웹 서비스를 위한 URL이다: http://localhost/WebGoat/services/WsSqlInjection?WSDL <br>" +
				" WSDL은 일반적으로 요청의 끝에 a ?WSDL 추가하여 보여줄수 있다.");
		hints.add( "getCreditCard (string id) 실행을 위한 새로운 soap 요청을 만드시오.");
		hints.add("soap 요청는 뒤에 오는 HTTP header를 이용한다: <br> " +
        "SOAPAction: some action header, can be &quot;&quot;<br><br>" +
	    "The soap message body has the following format:<br>" +
	    "&lt;?xml version='1.0' encoding='UTF-8'?&gt; <br>" +
	    "&nbsp;&nbsp;&lt;SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'&gt; <br>" +
	    "&nbsp;&nbsp;&nbsp;&nbsp;&lt;SOAP-ENV:Body&gt; <br>" +
	    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ns1:getCreditCard SOAP-ENV:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns1='http://lessons'&gt; <br>" +
	    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;id xsi:type='xsd:string'&gt;101&lt;/id&gt; <br>" +
	    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ns1:getCreditCard&gt; <br>" +
	    "&nbsp;&nbsp;&nbsp;&nbsp;&lt;/SOAP-ENV:Body&gt; <br>" +
	    "&nbsp;&nbsp;&lt;/SOAP-ENV:Envelope&gt; <br>" +
		"");
/*		"&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt; <br>" +
		"  &lt;SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" <br>" +
		"	                  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" <br>" +
		"	                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&gt; <br>" +
		"     &lt;SOAP-ENV:Body&gt; <br>" +
		"		  &lt;ns1:getCreditCard SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://lessons\"&gt; <br>" +
		"		   &lt;id xsi:type=\"xsd:string\"&gt;101&lt;/id&gt; <br>"+
		"		  &lt;/ns1:getCreditCard&gt; <br>" +
		"     &lt;/SOAP-ENV:Body&gt; <br>" +
		"  &lt;/SOAP-ENV:Envelope&gt; <br><br>" +
		"HTTP 요청을 가로채고 soap 요청을 만들어 보시오.");	*/	
		return hints;
	}

	public String getInstructions(WebSession s)
	{
		String instructions = "웹 서비스 기술 언어 (WSDL)를 검사하고 다수 고객의 신용 카드 번호를 획득하시오  " +
			"이 화면에서는 반환된 결과들을 볼 수 없을 것이다.  성공했다고 생각되면, 페이지를 " +
			"새로 고침하고 'green star'를 찾으시오";
		return ( instructions );
	}

	private final static Integer DEFAULT_RANKING = new Integer(150);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	public String getTitle()
	{
		return "웹 서비스 SQL 삽입";
	}
	protected Element makeAccountLine( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		
		ec.addElement( new P().addElement( "당신의 계정 번호를 입력하세요: " ) );

		accountNumber = s.getParser().getRawParameter( ACCT_NUM, "101" );
		Input input = new Input( Input.TEXT, ACCT_NUM, accountNumber.toString() );
		ec.addElement( input );

		Element b = ECSFactory.makeButton( "Go!" );
		ec.addElement( b );

		return ec;
	}
	protected Element createContent(WebSession s)
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
			for (int i=0; i<accountNumber.length(); i++) {
				char c = accountNumber.charAt(i);
				if (c < '0' || c > '9') {
					ec.addElement("유효하지않는 계정 번호. ");
                    accountNumber = "0";
				}
			}
			try
			{
				ResultSet results = getResults(accountNumber);
				if ( ( results != null ) && ( results.first() == true ) )
				{
					ResultSetMetaData resultsMetaData = results.getMetaData();
					ec.addElement( DatabaseUtilities.writeTable( results, resultsMetaData ) );
					results.last();
					if ( results.getRow() >= 6 )
					{
                        //this should never happen
					}
				}
				else 
				{
					ec.addElement( "일치하는 결과 없음.  다시 시도하세요." );
				}
			}
			catch ( SQLException sqle )
			{
				ec.addElement( new P().addElement( sqle.getMessage() ) );
			}
			A a = new A("services/WsSqlInjection?WSDL","WebGoat WSDL");
			ec.addElement(new P().addElement("중요한 자료에 접근하기 위하여 웹서비스기술언어(WSDL)를 활용하시오:"));
			ec.addElement(new BR());   
			ec.addElement(a);
			getLessonTracker( s ).setCompleted( completed );
		}
		catch (Exception e)
		{
			s.setMessage(" 오류 발생 " + this.getClass().getName());
			e.printStackTrace();
		}
		return (ec);
	}	
	public ResultSet getResults (String id) {
		try
		{				
			Connection connection = DatabaseUtilities.makeConnection();
			if (connection == null) {
				return null;
			}
			String query = "SELECT * FROM user_data WHERE userid = " + id ;
			try
			{
				Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				ResultSet results = statement.executeQuery( query );
				return results;
			}
			catch ( SQLException sqle )
			{
			}
		}
		catch ( Exception e )
		{
		}
	return null;		
	}
	public String[] getCreditCard(String id) {
		ResultSet results = getResults(id);
		if ((results != null)) {
			try {
				results.last();
				String[] users = new String[results.getRow()];
				if (users.length > 4) {
					completed = true;
				}
				results.beforeFirst();
				while (results.next() == true) {
					int i = results.getRow();
					users[i-1] = results.getString(ccNumber);
				}
				return users;
			} catch (SQLException sqle) {
			}
		}
		return null;
	}
	
    public Element getCredits()
    {
    	return super.getCustomCredits("By Alex Smolen", CREDITS_LOGO);
    }
}

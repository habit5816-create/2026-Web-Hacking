/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.WebSession;

/**
 * @author asmolen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SoapRequest extends LessonAdapter {
	/* TEST CODE
	private final static String ACCT_NUM = "account_number";
	private static Connection connection = null;
	private String accountNumber;
	*/
	
	//static boolean completed;
	public static Connection connection = null;
    public final static String firstName = "getFirstName";
    public final static String lastName = "getLastName";
    public final static String loginCount = "getLoginCount";
    public final static String ccNumber = "getCreditCard";
    
    //int instead of boolean to keep track of method invocation count
    static int accessFirstName;
    static int accessLastName;
    static int accessCreditCard;
    static int accessLoginCount;
    
    protected Category getDefaultCategory()
	{
		return AbstractLesson.WEB_SERVICES;
	}

	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add("접근하기 쉬운 연산은 WSDL의 &lt;portType&gt;부분 범위안에 &lt;operation&gt; 태그가 포암함되어 범위를 정해진다. <BR> 전형적인 작동(getFirstName): 예는 밑에 있다  <br><br>" +
				"&lt;wsdl:portType name=\"SoapRequest\"&gt; <br>" +
				"&lt;wsdl:<strong>operation name=\"getFirstName\"</strong>&gt;<br>" +
				"&lt;wsdl:input message=\"impl:getFirstNameRequest\" name=\"getFirstNameRequest\" /&gt;<br>" +
				"&lt;wsdl:output message=\"impl:getFirstNameResponse\" name=\"getFirstNameResponse\" /&gt;<br>" +
				"&lt;wsdlsoap:operation soapAction=\"\" /&gt;" +
				"&lt;/wsdl:portType&gt;<br><br>" +
				"방법들을 불러내는것은 입/출력 메세지 속성에 의해 정의된다. " +
				"예: <strong>\"getFirstNameRequest\"</strong>");
		hints.add("SOAP envelop 범이안에  몇몇의 태그가있다. " + 
				"각각의 이름공간은 WSDL의 &lt;definitions&gt; 부분 정의되어지고, 그리고 (xmlns:namespace_name_here=\"namespace_reference_location_here\") 형식은 사용되는것이 선언되어진다.<br><br>" + 
				"다음의 예제는 태그는 정의된다. \"&lt;xsd:\" 정의된다., 구조의특성은 참조하여 이름공간의 위치를 선어하여 분배한다:<br>" +
				"<strong>xmlns:xsd=\"http://www.w3.org/2001/XMLSchema</strong>");
		hints.add("어떤 매개변수 또는 타입은 정의된 메세지의 통신의 요구작동 방법에 의해 규정된다. " + 
				"이 예는  이름공간에(xsd) 매개변수(id)타입(int) 정의하는 방법이다. (getFirstNameRequest):<br>" +
				"&lt;wsdl:message name=\"getFirstNameRequest\"<br><br>" +
				"&lt;wsdl:<strong>part name=\"id\" type=\"xsd:int\"</strong> /&gt;<br>" +
				"&lt;/wsdl:message&gt;<br><br>" +
				"다른 타입의 예:<br>" + 
				"{boolean, byte, base64Binary, double, float, int, long, short, unsignedInt, unsignedLong, unsignedShort, string}.<br>");
		String soapEnv = " SOAP의 요구는 HTTP헤더 사용한다: <br><br> " +
		        "다음 형식은 SOAP 메세지 body부분의 이다:<br>" +
				"&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt; <br>" +
				"&lt;SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" <br>" +
				"	                  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" <br>" +
				"	                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&gt; <br>" +
				"&nbsp;&nbsp;&lt;SOAP-ENV:Body&gt; <br>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&lt;ns1:getFirstName SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://lessons\"&gt; <br>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&lt;id xsi:type=\"xsd:int\"&gt;101&lt;/id&gt; <br>"+
				"&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ns1:getFirstName&gt; <br>" +
				"&nbsp;&nbsp;&lt;/SOAP-ENV:Body&gt; <br>" +
				"&lt;/SOAP-ENV:Envelope&gt; <br><br>" +
				"HTTP요구 그리고 새로 만들어진 SOAP요구를 가로채다.";
		soapEnv.replaceAll("(?s) ","&nbsp;");
		hints.add(soapEnv);
		
		return hints;
	}
	
	public String getInstructions(WebSession s)
	{
		String instructions = "WSDL파일을 배워보아라. WebGoat 웹서비스 언어기술 WSDL파일을 확인해보았라 . <br><br>" +
			"브라우저 또는 웹서비스 툴과함께 WSDL에 연결하여라."+
			"웹서비스 URL은: http://localhost/WebGoat/services/SoapRequest 이다." +
			"WSDL은 보통 웹서비스 요청 맨 끝에 ?WSDL을 더해서 보여진다.";
		return ( instructions );
	}


	private final static Integer DEFAULT_RANKING = new Integer(100);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	public String getTitle()
	{
		return "어떻게 SOAP요구는 만들어지는가?";
	}
	
	protected Element makeOperationsLine( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		
		Table t1 = new Table().setCellSpacing( 0 ).setCellPadding( 2 );
		
		if ( s.isColor() )
		{
			t1.setBorder( 1 );
		}
			
		TR tr = new TR();
		tr.addElement(new TD().addElement( "WSDL는 얼마나 많은 작동을 정의되는가? : " ));
		tr.addElement(new TD( new Input( Input.TEXT, "count", "")));
		Element b = ECSFactory.makeButton( "입력" ); 
		tr.addElement( new TD(b).setAlign("LEFT") );
		t1.addElement(tr);
		
		ec.addElement(t1);

		return ec;
	}
	
	protected Element makeTypeLine( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		
		Table t1 = new Table().setCellSpacing( 0 ).setCellPadding( 2 );
		
		if ( s.isColor() )
		{
			t1.setBorder( 1 );
		}
			
		TR tr = new TR();
		tr.addElement(new TD().addElement( "현재,  \"getFirstNameRequest\" method: 의 매개 변수가 무언인가?" ));
		tr.addElement(new TD( new Input( Input.TEXT, "type", "")));
		Element b = ECSFactory.makeButton( "입력" ); 
		tr.addElement( new TD(b).setAlign("LEFT") );
		t1.addElement(tr);
		
		ec.addElement(t1);

		return ec;
	}

	protected Element createContent( WebSession s )
	{
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		return viewWsdl( s );
	}
	
	protected Element doStage2( WebSession s ) throws Exception
	{
		return determineType( s);
	}
	
	protected Element doStage3( WebSession s ) throws Exception
	{
		return createSoapEnvelope( s);
	}
	
	protected Element viewWsdl(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		
		//DEVNOTE: Test for stage completion.
	   	try
		{
			int operationCount = 0;
			operationCount = s.getParser().getIntParameter( "count" );
			
			if (operationCount == 4)
			{
				getLessonTracker(s).setStage(2);
				s.setMessage("1단계 성공.");
				
				// Redirect user to Stage2 content.
				ec.addElement(doStage2(s));
			}
			else
			{
				s.setMessage( "미안, 올바르지않은 계산입니다.. 다시 시도하세요." );
			}
		}
	   	catch (NumberFormatException nfe)
		{
			//DEVNOTE: Eat the exception.
	   		//ec.addElement( new P().addElement( nfe.getMessage() ) );
	   		s.setMessage("미안, 무효한 대답입니다. 다시 시도하세요.");
		}
		catch (ParameterNotFoundException pnfe)
		{
			//DEVNOTE: Eat the exception.
			// ec.addElement( new P().addElement( pnfe.getMessage() ) );
		}
		catch ( Exception e )
		{
			s.setMessage( "에러 발생 " + this.getClass().getName() );
			e.printStackTrace();
		}
		
		//DEVNOTE: Conditionally display Stage1 content depending on whether stage is completed or not
		if (getLessonTracker(s).getStage() == 1)
		//if ( null == (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)) ||
		//	(getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)).equals("1") )
		{
			ec.addElement( makeOperationsLine(s) );
			
			A a = new A("services/SoapRequest?WSDL","WebGoat WSDL");
			ec.addElement(new P().addElement("다음의 WSDL은 유용한 연산 계산을 보여진다:"));
			ec.addElement(new BR());
			ec.addElement(a);
		}
		
		//getLessonTracker( s ).setCompleted( SoapRequest.completed );
		
		return (ec);
	}
	
	protected Element determineType(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		
		//DEVNOTE: Test for stage completion.
	   	try
		{
			String paramType = "";
			paramType = s.getParser().getStringParameter( "type" );
			
			//if (paramType.equalsIgnoreCase("int"))
			if (paramType.equals("int"))
			{
				getLessonTracker(s).setStage(3);
				s.setMessage("2단계성공. ");
				//s.setMessage("Now, you'll craft a SOAP envelope for invoking a web service directly.");
				
				// Redirect user to Stage2 content.
				ec.addElement(doStage3(s));
			}
			else
			{
				s.setMessage( "미안, 올바르지 않은 타입 입니다. 다시 시도하세요." );
			}
		}
		catch (ParameterNotFoundException pnfe)
		{
			//DEVNOTE: Eat the exception.
			// ec.addElement( new P().addElement( pnfe.getMessage() ) );
		}
		catch ( Exception e )
		{
			s.setMessage( "에러 발생" + this.getClass().getName() );
			e.printStackTrace();
		}
		
		//DEVNOTE: Conditionally display Stage2 content depending on whether stage is completed or not
		if (getLessonTracker(s).getStage() == 2)
		//if ( null == (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)) ||
		//	(getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)).equals("2") )
		{
			ec.addElement( makeTypeLine(s) );
			
			A a = new A("services/SoapRequest?WSDL","WebGoat WSDL");
			ec.addElement(new P().addElement("다음의 WSDL은 유용한 연산 계산을 보여진다:"));
			ec.addElement(new BR());
			ec.addElement(a);
		}
		
		//getLessonTracker( s ).setCompleted( SoapRequest.completed );
		
		return (ec);
	}
	
	protected Element createSoapEnvelope (WebSession s)
	{
		ElementContainer ec = new ElementContainer();
        
        // Determine how many methods have been accessed. User needs to check at least two methods
        // before completing the lesson.
        if ((accessFirstName + accessLastName + accessCreditCard + accessLoginCount) >= 2)
        {
        	//SoapRequest.completed = true;
        	makeSuccess(s);
        }
        else
        {

        	// display Stage2 content
            ec.addElement(new P().addElement( "request를 가로거나 그리고 불러내는 방법으로 정당한 SOAP요구에의한 정당한 계정을 보낸다 <br>" ));
    		Element b = ECSFactory.makeButton( "Press to generate an HTTP request" );        
            ec.addElement( b );
            
        	// conditionally display invoked methods
            if ((accessFirstName + accessLastName + accessCreditCard + accessLoginCount) > 0)
            {
            	ec.addElement("<br><br>방법 을 끌어내다.:<br>");
            	ec.addElement("<ul>");
            	if ( accessFirstName > 0 )
            	{
            		ec.addElement("<li>getFirstName</li>");
            	}
            	if ( accessLastName > 0 )
            	{
            		ec.addElement("<li>getLastName</li>");
            	}
            	if ( accessCreditCard > 0 )
            	{
            		ec.addElement("<li>getCreditCard</li>");
            	}
            	if ( accessLoginCount > 0 )
            	{
            		ec.addElement("<li>getLoginCount</li>");
            	}
            	ec.addElement("</ul>");
            }
        }
        
        //getLessonTracker( s ).setCompleted( SoapRequest.completed );
		return (ec);
	}
	
	public String getResults(int id, String field) {
		try
		{				
			Connection connection = DatabaseUtilities.makeConnection();
			if (connection == null) {
				return null;
			}
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM user_data WHERE userid = ?");
			ps.setInt(1, id);
			try
			{
				ResultSet results = ps.executeQuery();
				if ( ( results != null ) && ( results.next() == true ) )
				{						
					return results.getString(field);
				}				
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
	
	public String getCreditCard(int id) {
		String result = getResults(id, "cc_number");
		//SoapRequest.completed = true;
		
		if (result != null)
		{
			//DEVNOTE: Always set method access counter to (1) no matter how many times it is accessed.
			// This is intended to be used to determine how many methods have been accessed, not how often.
			accessCreditCard = 1;
			return result;
		}
		return null;
	}
	
	public String getFirstName(int id) {
		String result = getResults(id, "first_name");
		if (result != null)
		{
			//DEVNOTE: Always set method access counter to (1) no matter how many times it is accessed.
			// This is intended to be used to determine how many methods have been accessed, not how often.
			accessFirstName = 1;
			return result;
		}
		return null;
	}
	
    public String getLastName(int id) {
        String result = getResults(id, "last_name");
        if (result != null)
        {
			//DEVNOTE: Always set method access counter to (1) no matter how many times it is accessed.
			// This is intended to be used to determine how many methods have been accessed, not how often.
        	accessLastName = 1;
            return result;
        }
        return null;
    }
    
    public String getLoginCount(int id) {
        String result = getResults(id, "login_count");        
        if (result != null)
        {
			//DEVNOTE: Always set method access counter to (1) no matter how many times it is accessed.
			// This is intended to be used to determine how many methods have been accessed, not how often.
        	accessLoginCount = 1;
            return result;
        }
        return null;
    }    
}

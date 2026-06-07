/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.owasp.webgoat.lessons;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.ServiceFactory;
import org.apache.axis.encoding.XMLType;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

/**
 * @author asmolen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WSDLScanning extends LessonAdapter {
	static boolean completed = false;
	static boolean beenRestartedYet = false;
	public static Connection connection = null;
    public final static String firstName = "getFirstName";
    public final static String lastName = "getLastName";
    public final static String loginCount = "getLoginCount";
    public final static String ccNumber = "getCreditCard";
	final static IMG CREDITS_LOGO = new IMG( "images/logos/parasoft.jpg" ).setAlt( "Parasoft" ).setBorder( 0 ).setHspace( 0 ).setVspace( 0 );

    protected Category getDefaultCategory()
	{
		return AbstractLesson.WEB_SERVICES;
	}

	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "브라우저나 웹서비스툴을 가지고 웹서비스정의언어(WSDL)에 연결을 시도하시오." );
		hints.add( "때때로 WSDL는 웹 API를 통해 유효하지 않은 방법을 정의할 것이다. " +
				"WSDL에 있는 연산을 찾아보십시오, 그러나 이 API의 부분 아닙니다");
		hints.add( "웹 서비스를 위한 URL이다: http://localost/WebGoat/services/WSDLScanning <br>" +
				" WSDL은 일반적으로 요청의 끝에 a ?WSDL 추가하여 보여줄수 있다.");
		hints.add( "WSDL에서 getCreditCard을 위한 연산을 찾아보고 차단된 요청 값을 삽입하시오.");
		return hints; 
	}
	public String getInstructions(WebSession s)
	{
		String instructions = "이 화면은 웹서비스를 위한 API이다. " +
				"웹서비스를 위한 웹서비스정의언어(WSDL)를 체크하고 몇몇의 고객 신용 카드 번호를 획득하시오." ;
		return ( instructions );
	}


	private final static Integer DEFAULT_RANKING = new Integer(120);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	public String getTitle()
	{
		return "WSDL Scanning";
	}
    public Object accessWGService(String serv, String proc, String parameterName, Object parameterValue) {
        String targetNamespace = "WebGoat";
        try {
            ServiceFactory factory = (ServiceFactory) ServiceFactory.newInstance();
            QName serviceName = new QName(targetNamespace, serv);
            QName operationName = new QName(targetNamespace, proc);
            Service service = new Service();
            Call     call   = (Call) service.createCall();
            call.setOperationName(operationName);
            call.addParameter( parameterName, serviceName, ParameterMode.INOUT );
            call.setReturnType( XMLType.XSD_STRING );
            call.setUsername("guest");
            call.setPassword("guest");
            call.setTargetEndpointAddress(
             "http://localhost/WebGoat/services/" + serv);
            Object result = call.invoke( new Object[] { parameterValue } );
            return result;     
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
        

        Table t1 = new Table().setCellSpacing( 0 ).setCellPadding( 2 );

		if ( s.isColor() )
		{
			t1.setBorder( 1 );
		}
		TR tr = new TR();
        tr.addElement(new TD( "Enter your account number: " ));
        tr.addElement(new TD( new Input( Input.TEXT, "id", "101")));
        t1.addElement(tr);
        
        tr = new TR();
        tr.addElement( new TD( "Select the fields to return: " ));
        tr.addElement(new TD( new Select("field").setMultiple(true)
                .addElement(new Option(firstName).addElement("  이름  "))
                .addElement(new Option(lastName).addElement("   성   "))
                .addElement(new Option(loginCount).addElement("로그인 횟수"))));
        t1.addElement(tr);

        tr = new TR();
        Element b = ECSFactory.makeButton( "Submit" );        
        tr.addElement( new TD(b).setAlign("CENTER").setColSpan(2) );
        t1.addElement(tr);
        
        ec.addElement(t1);
        
        try {
            String[] fields = s.getParser().getParameterValues( "field" );
            int id =  s.getParser().getIntParameter( "id" );
            if ( connection == null )
            {
                connection = DatabaseUtilities.makeConnection( s );
            }

            Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 1 );

			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}
            TR header = new TR();
            TR results = new TR();
            for (int i=0; i<fields.length;i++) {
                header.addElement(new TD().addElement(fields[i]));
                results.addElement(new TD().addElement((String) accessWGService("WSDLScanning",  fields[i], "acct_num", new Integer(id))));
            }
            if ( fields.length == 0 )
            {
            	s.setMessage("Please select a value to return.");
            }
            t.addElement(header);
            t.addElement(results);
            ec.addElement(new P().addElement(t));                        
        } catch (Exception e) {
            
        }
        try
		{
			A a = new A("services/WSDLScanning?WSDL","WebGoat WSDL");
			ec.addElement(new P().addElement("완전한 API를 보기 위하여 웹 서비스 정의 언어 (WSDL)를 이용하시오:"));
			ec.addElement(new BR());
			ec.addElement(a);
			//getLessonTracker( s ).setCompleted( completed );
			
			if ( completed && ! getLessonTracker( s ).getCompleted() && ! beenRestartedYet ) {
				makeSuccess(s);
				beenRestartedYet = true;
			} else if ( completed && ! getLessonTracker(s).getCompleted() && beenRestartedYet) {
				completed = false;
				beenRestartedYet = false;
			}

//            accessWGService("WSDLScanning", "getCreditCard", "acct_num", new Integer(101));
		}
		catch (Exception e)
		{
			s.setMessage("Error generating " + this.getClass().getName());
			e.printStackTrace();
		}
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
		if (result != null)
		{
			completed = true;
			return result;
		}
		return null;
	}
	public String getFirstName(int id) {
		String result = getResults(id, "  이름  ");
		if (result != null)
		{
			return result;
		}
		return null;
	}
    public String getLastName(int id) {
        String result = getResults(id, "   성   ");
        if (result != null)
        {
            return result;
        }
        return null;
    }
    public String getLoginCount(int id) {
        String result = getResults(id, "로그인 횟수");        
        if (result != null)
        {
            return result;
        }
        return null;
    }
    
    public Element getCredits()
    {
    	return super.getCustomCredits("By Alex Smolen", CREDITS_LOGO);
    }
	

}

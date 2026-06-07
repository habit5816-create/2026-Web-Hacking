package org.owasp.webgoat.lessons;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;

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

public class UncheckedEmail extends LessonAdapter
{

	private final static String MESSAGE = "msg";
	private final static String TO = "to";


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
			String to = s.getParser().getRawParameter( TO, "" );

			Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 0 ).setWidth("90%").setAlign("center");

			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}
			
			TR tr = new TR();
			tr.addElement( new TH().addElement("OWASP 비평<BR>").setAlign("left").setColSpan(3));
			t.addElement( tr );
			
			tr = new TR();
			tr.addElement( new TD().addElement( "&nbsp;").setColSpan(3));
			t.addElement( tr );
			
			tr = new TR();
			tr.addElement( new TH().addElement(new H1("전체 접속")).setAlign("left").setWidth("55%").setVAlign("BOTTOM"));	
			tr.addElement( new TH().addElement( "&nbsp;"));	
			tr.addElement( new TH().addElement(new H3("Contact Information:")).setAlign("left").setVAlign("BOTTOM"));	
			t.addElement( tr );
			
			
			tr = new TR();
			tr.addElement( new TD().addElement("저희는 여러분의 의견을 높이 평가합니다. WebGoat tools에 대한 질문사항이나 불편한 점이 있으면 아래 창에 입력하여" +
					"OWASP에 보내주십시오. 입력하신 정보는 <U>사생활 보호 정책</U>에 의거하여 " +
					"보호될 것입니다."));
			tr.addElement( new TD().addElement( "&nbsp;"));	
			tr.addElement( new TD().addElement("<b>OWASP</B><BR>" +
											"9175 Guilford Rd <BR> Suite 300 <BR>" +
											"Columbia, MD.  21046").setVAlign("top"));
			t.addElement( tr );
			
			

			tr = new TR();
			tr.addElement( new TD().addElement( "&nbsp;").setColSpan(3));
			t.addElement( tr );
			
			Input input = new Input( Input.HIDDEN, TO, "webgoat.admin@owasp.org" );
			tr = new TR();
			tr.addElement( new TD().addElement( "질문사항이나 불편사항:"));
			tr.addElement( new TD().addElement( "&nbsp;"));	
			tr.addElement( new TD().setAlign( "LEFT" ).addElement( input ));
			t.addElement( tr );
			
			
			tr = new TR();
			String message = s.getParser().getRawParameter( MESSAGE, "" );
			TextArea ta = new TextArea(  MESSAGE, 5, 40 );
			ta.addElement( new StringElement( convertMetachars(message) ));
			tr.addElement( new TD().setAlign( "LEFT" ).addElement( ta ));
			tr.addElement( new TD().setAlign( "LEFT" ).setVAlign( "MIDDLE" ).addElement( ECSFactory.makeButton( "보내기!" ) ) );
			tr.addElement( new TD().addElement( "&nbsp;"));	
			t.addElement( tr );
			ec.addElement( t );

			// Eventually we could send the actually mail, but the point should already be made
			//ec.addElement(exec( use java mail here + to));

			if ( to.length() > 0 )
			{
				Format formatter;
				// Get today's date
				Date date = new Date();
				formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
				String today = formatter.format(date);
				// Tue, 09 Jan 2002 22:14:02 -0500

				ec.addElement( new HR() );
				ec.addElement( new Center().addElement( new B().addElement( "보낸 메시지: " + to ) ) );
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>Return-Path:</b> &lt;webgoat@owasp.org&gt;"));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>Delivered-To:</b> " + to));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>Received:</b> (qmail 614458 invoked by uid 239); " + today));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("for &lt;" + to+"&gt;; " + today ));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>보내는 이:</b> " + to));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>받는 이:</b> Blame it on the Goat &lt;webgoat@owasp.org&gt;"));
				ec.addElement( new BR() );
				ec.addElement( new StringElement("<b>제목:</b> OWASP security issues"));				
				ec.addElement( new BR() );
				ec.addElement( new BR() );
				ec.addElement( new StringElement( splitMessage(message, 70, true) ) );
			}
			
			// only complete the lesson if they changed the "to" hidden field
			if ( to.length() > 0 && ! "webgoat.admin@owasp.org".equals( to ) )
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
	 *  DOCUMENT ME!
	 *
	 * @return    DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A1;
	}


	/**
	 *  Gets the hints attribute of the EmailScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "자신에게 익명의 메시지를 보내십시오." );
		hints.add( "html이나 자바스크립트 코드를 메시지 폴더에 삽입하여 보내십시오." );
		hints.add( "HTML안에 숨겨진 폴더를 보십시오.");
		hints.add( "폴더 메시지에 &lt;A href=\"http://www.aspectsecurity.com/webgoat.html\"&gt;Click here for Aspect&lt;/A&gt 삽입" );
		hints.add( "폴더 메시지에 &lt;script&gt;alert(\"Bad Stuff\");&lt;/script&gt; 삽입" );
		return hints;
	}


	/**
	 *  Gets the instructions attribute of the UncheckedEmail object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "이 폼은 고객 유지 페이지의 예시입니다. 폼을 이용하여 아래 항목을 시도하여야 합니다:<br>"
							+ "1) 웹 사이트 사용자에게 악의적인 스크립트 보내기<br>"
							+ "2) OWASP으로 'friend'에게 악의적인 스크립트 보내기<br>";
		return ( instructions );
	}




	private final static Integer DEFAULT_RANKING = new Integer(55);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the EmailScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Exploit Unchecked Email" );
	}
}



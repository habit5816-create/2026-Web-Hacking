package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;



/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */

public class ReflectedXSS extends LessonAdapter
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
		String regex1 = "^[0-9]{3}$";// any three digits
		Pattern pattern1 = Pattern.compile( regex1 );

		try
		{
			String param1 = s.getParser().getRawParameter( "field1", "111" );
			String param2 = HtmlEncoder.encode( s.getParser().getRawParameter( "field2", "4128 3214 0002 1999" ) );
			float quantity = 1.0f;
			float total = 0.0f;
			float runningTotal = 0.0f;
			
			// test input field1
			if ( !pattern1.matcher( param1 ).matches() )
			{
				if ( param1.toLowerCase().indexOf( "script" ) != -1 )
				{
					makeSuccess( s );
				}

				s.setMessage( "Whoops! You entered " + param1 + " instead of your three digit code.  Please try again." );
			}

			// FIXME: encode output of field2, then s.setMessage( field2 );

			ec.addElement( new HR().setWidth("90%") );
			ec.addElement( new Center().addElement( new H1().addElement( "Shopping Cart " )));
			Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 1 ).setWidth("90%").setAlign("center");

			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}
			
			TR tr = new TR();
			tr.addElement( new TH().addElement("쇼핑 카트 품목 -- 지금사는것").setWidth("80%"));
			tr.addElement( new TH().addElement("Price:").setWidth("10%"));
			tr.addElement( new TH().addElement("Quantity:").setWidth("3%"));
			tr.addElement( new TH().addElement("Total").setWidth("7%"));
			t.addElement( tr );
			
			tr = new TR();
			tr.addElement( new TD().addElement("Studio RTA - Laptop/Reading Cart with Tilting Surface - Cherry "));
			tr.addElement( new TD().addElement("69.99").setAlign("right"));
			tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY1", s.getParser().getStringParameter("QTY1", "1") )).setAlign( "right" ));
			quantity = s.getParser().getFloatParameter("QTY1", 1.0f);
			total = quantity * 69.99f;
			runningTotal += total;
			tr.addElement( new TD().addElement("$" +total));
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement("Dynex - Traditional Notebook Case"));
			tr.addElement( new TD().addElement("27.99").setAlign("right"));
			tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY2", s.getParser().getStringParameter("QTY2", "1") )).setAlign( "right" ));
			quantity = s.getParser().getFloatParameter("QTY2", 1.0f);
			total = quantity * 27.99f;
			runningTotal += total;
			tr.addElement( new TD().addElement("$" +total));
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement("Hewlett-Packard - Pavilion Notebook with Intel?Centrino?));
			tr.addElement( new TD().addElement("1599.99").setAlign("right"));
			tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY3", s.getParser().getStringParameter("QTY3", "1") )).setAlign( "right" ));
			quantity = s.getParser().getFloatParameter("QTY3", 1.0f);
			total = quantity * 1599.99f;
			runningTotal += total;
			tr.addElement( new TD().addElement("$" +total));
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement("3 - Year Performance Service Plan $1000 and Over "));
			tr.addElement( new TD().addElement("299.99").setAlign("right"));
			
			tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY4", s.getParser().getStringParameter("QTY4", "1") )).setAlign( "right" ));
			quantity = s.getParser().getFloatParameter("QTY4", 1.0f);
			total = quantity * 299.99f;
			runningTotal += total;
			tr.addElement( new TD().addElement("$" +total));
			t.addElement( tr );
			
			ec.addElement(t);
			
			t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 0 ).setWidth("90%").setAlign("center");

			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}
			
			ec.addElement( new BR() );
			
			tr = new TR();
			tr.addElement( new TD().addElement( "총계는 당신의 신용카드에 청구한다.:" ) );
			tr.addElement( new TD().addElement( "$" + runningTotal ));
			tr.addElement( new TD().addElement( ECSFactory.makeButton( "Update Cart" )));
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement( "&nbsp;" ).setColSpan(2) );
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement( "당신의 신용카드 번호를 입력하세요:" ) );
			tr.addElement( new TD().addElement( new Input( Input.TEXT, "field2", param2 )));
			t.addElement( tr );
			tr = new TR();
			tr.addElement( new TD().addElement( "당신의 접근코드를 입력하세요:" ) );
			tr.addElement( new TD().addElement( new Input( Input.TEXT, "field1", param1 )));
			t.addElement( tr );
			
			Element b = ECSFactory.makeButton( "구입" );
			tr = new TR();
			tr.addElement( new TD().addElement( b ).setColSpan(2).setAlign("center"));
			t.addElement( tr );
			
			ec.addElement( t );
			ec.addElement( new BR() );
			ec.addElement( new HR().setWidth("90%") );
		}
		catch ( Exception e )
		{
			s.setMessage( "에서 생성 " + this.getClass().getName() );
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
		return AbstractLesson.A4;
	}



	/**
	 *  Gets the hints attribute of the AccessControlScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "A simple script is &lt;SCRIPT&gt;alert('bang!');&lt;/SCRIPT&gt;." );
		hints.add( "JSESSIONID cookie 를 밝혀 스크립트를 가질수 있는가?" );
		hints.add( "당신은 사용할수있습니다. &lt;SCRIPT&gt;alert(document.cookie);&lt;/SCRIPT&gt; 이 쿠기 ID에 접근하기위해서" );
		hints.add( "당신은 신용 카드 모양 분야에 접근하기 위하여 원본을 얻을 수 있는가??" );
		hints.add( "크로스사이트 (XST) 명령을 시도하십시오" +
				"&lt;script type=\"text/javascript\"&gt;if ( navigator.appName.indexOf(\"Microsoft\") !=-1)" +
				" {var xmlHttp = new ActiveXObject(\"Microsoft.XMLHTTP\");xmlHttp.open(\"TRACE\", \"./\", false);" +
				" xmlHttp.send();str1=xmlHttp.responseText; while (str1.indexOf(\"\\n\") > -1) str1 = str1.replace(\"\\n\",\"&lt;br&gt;\"); " +
				"document.write(str1);}&lt;/script&gt;");
		return hints;
	}
//	<script type="text/javascript">if ( navigator.appName.indexOf("Microsoft") !=-1) {var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");xmlHttp.open("TRACE", "./", false); xmlHttp.send();str1=xmlHttp.responseText;document.write(str1);}</script>
	/**
	 *  Gets the instructions attribute of the WeakAccessControl object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "이 예제는, 당신의 임무는 원본에 약간을 입력을통해 약간을 포함하는것이다. 당신은 이 것을 수행하고 나쁜 무언가를 할 당신의 브라우저 등을 입력해 페이지를 얻는 것을 시도해야 한다.";
		return ( instructions );
	}



	private final static Integer DEFAULT_RANKING = new Integer(120);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the AccessControlScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return "방법은 크로스사이트 스크립트(XSS)공격을 반영했다.";
	}
}



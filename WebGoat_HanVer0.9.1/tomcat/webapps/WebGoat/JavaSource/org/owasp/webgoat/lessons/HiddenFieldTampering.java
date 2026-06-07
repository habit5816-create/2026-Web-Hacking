package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
public class HiddenFieldTampering extends LessonAdapter
{
	private final static String PRICE = "Price";

	private final static String PRICE_TV = "2999.99";
	
	private final static String PRICE_TV_HACKED = "9.99";	

	/**
	 *  Constructor for the HiddenFieldScreen object
	 */
	public HiddenFieldTampering() { }

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
			String price = s.getParser().getRawParameter( PRICE, PRICE_TV );
			float quantity = s.getParser().getFloatParameter("QTY", 1.0f);
			float total = quantity * Float.parseFloat(price);
			
			if ( price.equals(PRICE_TV)  )
			{
				ec.addElement( new Center().addElement( new H1().addElement( "Shopping Cart " )));
                 ec.addElement( new BR() );
				Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 1 ).setWidth("90%").setAlign("center");

				if ( s.isColor() )
				{
					t.setBorder( 1 );
				}
				
				TR tr = new TR();
				tr.addElement( new TH().addElement("장바구니 Items -- 현재물품").setWidth("80%"));
				tr.addElement( new TH().addElement("가격:").setWidth("10%"));
				tr.addElement( new TH().addElement("수량:").setWidth("3%"));
				tr.addElement( new TH().addElement("금액").setWidth("7%"));
				t.addElement( tr );
				
				tr = new TR();
				tr.addElement( new TD().addElement("56 inch HDTV (model KTV-551)"));
				tr.addElement( new TD().addElement(PRICE_TV).setAlign("right"));
				tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY", 1 )).setAlign( "right" ));
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
				tr.addElement( new TD().addElement( "신용카드 결제 금액:" ) );
				tr.addElement( new TD().addElement( "$" + total ));
				tr.addElement( new TD().addElement( ECSFactory.makeButton( "장바구니" )));
				tr.addElement( new TD().addElement( ECSFactory.makeButton( "구입" )));
				t.addElement( tr );
				
				ec.addElement(t);

				Input input = new Input( Input.HIDDEN, PRICE, PRICE_TV );
				ec.addElement( input );
				ec.addElement( new BR() );

			}
			else
			{
				if ( !price.toString().equals( PRICE_TV ) )
				{
					makeSuccess( s );
				}

				ec.addElement( new P().addElement( "총 구입금액은:" ) );
				ec.addElement( new B( "$" + total ) );
				ec.addElement( new BR() );
				ec.addElement( new P().addElement( "총구입액은 당신의 신용카드로 즉시 결제 될 것입니다." ) );
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
	 *  Gets the hints attribute of the HiddenFieldScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "서버에 값 정보를 전송할때 숨겨진 fields를 사용하여 적용하십시오." );
		hints.add( "프로그램을 가로채고 숨겨진 field의 값을 바꾸십시오." );
		hints.add( "" + PRICE_TV + "를 " + PRICE_TV_HACKED + "로 <A href=\"http://www.owasp.org/development/webscarab\">WebScarab</A>를 사용하여 TV의 가격을 바꾸십시오." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the HiddenFieldTampering object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "HDTV를 구입할때 보다 적은 가격으로 구입하시오, if you have not done so already.";

		return ( instructions );
	}




	private final static Integer DEFAULT_RANKING = new Integer(50);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the HiddenFieldScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "숨겨진 Fields 공격 방법" );
	}
}


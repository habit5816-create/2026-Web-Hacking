package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.P;
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

public class AccessControlMatrix extends LessonAdapter
{

	private final static String RESOURCE = "Resource";
	private final static String USER = "User";
	private final static String[] resources = {"Public Share", "Time Card Entry", "Performance Review", "Time Card Approval", "Site Manager", "Account Manager"};
	private final static String[] roles = {"Public", "User", "Manager", "Admin"};
	private final static String[] users = { "Moe", "Larry", "Curly", "Shemp"};


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
			String user = s.getParser().getRawParameter( USER, users[0] );
			String resource = s.getParser().getRawParameter( RESOURCE, resources[0] );
			String credentials = getRoles( user ).toString();
			ec.addElement( new P().addElement( "사용자 변경:" ) );
			ec.addElement( ECSFactory.makePulldown( USER, users, user, 1 ) );
			ec.addElement( new P() );

			// These two lines would allow the user to select the resource from a  list
			// Didn't seem right to me so I made them type it in.
			//	ec.addElement( new P().addElement( "Choose a resource:" ) );
			//	ec.addElement( ECSFactory.makePulldown( RESOURCE, resources, resource, 1 ) );
			ec.addElement( new P().addElement( "자원 선택: " ) );
			ec.addElement( ECSFactory.makePulldown( RESOURCE, resources, resource, 1 ) );

			ec.addElement( new P() );
			ec.addElement( ECSFactory.makeButton( "접근 확인" ) );

			if (  isAllowed( user, resource ) )
			{
				if ( !getRoles( user ).contains( "Admin") && resource.equals("Account Manager")) 
				{
					makeSuccess( s );
				}
				s.setMessage( "사용자 " + user + " " + credentials + " 리소스에 엑세스 할 수 있습니다. " + resource );
			}
			else
			{
				s.setMessage( "사용자 " + user + " " + credentials + " 이 리소스에 엑세스할 권한이 없습니다. " + resource );
			}
		}
		catch ( Exception e )
		{
			s.setMessage( " 에러가 발생하였습니다. " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}



	/**
	 *  Gets the category attribute of the RoleBasedAccessControl object
	 *
	 * @return    The category value
	 */

	protected Category getDefaultCategory()
	{
		return AbstractLesson.A2;
	}



	/**
	 *  Gets the hints attribute of the RoleBasedAccessControl object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "대부분의 사이트에 제한된 접근을 시도." );
		hints.add( "개발자들이 자주 구현하는 실수." );
		hints.add( "사용자의 조합을 시도, 역할, 자원." );
		return hints;
	}


	/**
	 *  Gets the instructions attribute of the WeakAccessControl object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "각각의 사용자가 구성원으로의 역할은 특정 리소스에 액세스할 수 있는것이 유일합니다. 귀하의 탐험의 목표는 이 사이트에 대한 액세스 제어 규칙이 적용. 관리 전용의 [Admin] 그룹으로 들어가서 '계정 관리'자원에 접근합니다.";
		return ( instructions );
	}



	private final static Integer DEFAULT_RANKING = new Integer(120);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the resources attribute of the RoleBasedAccessControl object
	 *
	 * @param  rl  Description of the Parameter
	 * @return     The resources value
	 */
	private List getResources( List rl )
	{
		// return the resources allowed for these roles
		ArrayList list = new ArrayList();

		if ( rl.contains( roles[0] ) )
		{
			list.add( resources[0] );
		}

		if ( rl.contains( roles[1] ) )
		{
			list.add( resources[1] );
			list.add( resources[5] );
		}

		if ( rl.contains( roles[2] ) )
		{
			list.add( resources[2] );
			list.add( resources[3] );
		}

		if ( rl.contains( roles[3] ) )
		{
			list.add( resources[4] );
			list.add( resources[5] );
		}

		return list;
	}



	/**
	 *  Gets the role attribute of the RoleBasedAccessControl object
	 *
	 * @param  user  Description of the Parameter
	 * @return       The role value
	 */

	private List getRoles( String user )
	{
		ArrayList list = new ArrayList();

		if ( user.equals( users[0] ) )
		{
			list.add( roles[0] );
		}
		else if ( user.equals( users[1] ) )
		{
			list.add( roles[1] );
			list.add( roles[2] );
		}
		else if ( user.equals( users[2] ) )
		{
			list.add( roles[0] );
			list.add( roles[2] );
		}
		else if ( user.equals( users[3] ) )
		{
			list.add( roles[3] );
		}

		return list;
	}


	/**
	 *  Gets the title attribute of the AccessControlScreen object
	 *
	 * @return    The title value
	 */

	public String getTitle()
	{
		return ( "접근 제어 매트릭스를 사용" );
	}

	// private final static ArrayList userList = new ArrayList(Arrays.asList(users));
	// private final static ArrayList resourceList = new ArrayList(Arrays.asList(resources));
	// private final static ArrayList roleList = new ArrayList(Arrays.asList(roles));


	/**
	 *  Please do not ever implement an access control scheme this way! But it's not the worst I've
	 *  seen.
	 *
	 * @param  user      Description of the Parameter
	 * @param  resource  Description of the Parameter
	 * @return           The allowed value
	 */

	private boolean isAllowed( String user, String resource )
	{
		List roles = getRoles( user );
		List resources = getResources( roles );
		return ( resources.contains( resource ) );
	}
}



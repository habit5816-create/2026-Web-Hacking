package org.owasp.webgoat.lessons.CrossSiteScripting;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.ecs.ElementContainer;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.lessons.LessonAction;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.DeleteProfile;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.ListStaff;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.Login;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.Logout;
import org.owasp.webgoat.lessons.RoleBasedAccessControl.SearchStaff;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ParameterNotFoundException;
import org.owasp.webgoat.session.UnauthenticatedException;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.ValidationException;
import org.owasp.webgoat.session.WebSession;

/**
 *  Copyright (c) 2006 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 */
public class CrossSiteScripting extends LessonAdapter
{
	public final static String DESCRIPTION = "description";
	public final static String DISCIPLINARY_DATE = "disciplinaryDate";
	public final static String DISCIPLINARY_NOTES = "disciplinaryNotes";
	public final static String CCN_LIMIT = "ccnLimit";
	public final static String CCN = "ccn";
	public final static String SALARY = "salary";
	public final static String START_DATE = "startDate";
	public final static String MANAGER = "manager";
	public final static String ADDRESS1 = "address1";
	public final static String ADDRESS2 = "address2";
	public final static String PHONE_NUMBER = "phoneNumber";
	public final static String TITLE = "title";
	public final static String SSN = "ssn";
	public final static String LAST_NAME = "lastName";
	public final static String FIRST_NAME = "firstName";
	public final static String PASSWORD = "password";

	public final static String EMPLOYEE_ID = "employee_id";
	public final static String USER_ID = "user_id";
	public final static String SEARCHNAME = "search_name";
	public final static String SEARCHRESULT_ATTRIBUTE_KEY = "SearchResult";
	public final static String EMPLOYEE_ATTRIBUTE_KEY = "Employee";
	public final static String STAFF_ATTRIBUTE_KEY = "Staff";
	
	public final static String LOGIN_ACTION = "Login";
	public final static String LOGOUT_ACTION = "Logout";
	public final static String LISTSTAFF_ACTION = "ListStaff";
	public final static String SEARCHSTAFF_ACTION = "SearchStaff";
	public final static String FINDPROFILE_ACTION = "FindProfile";
	public final static String VIEWPROFILE_ACTION = "ViewProfile";
	public final static String EDITPROFILE_ACTION = "EditProfile";
	public final static String UPDATEPROFILE_ACTION = "UpdateProfile";
	public final static String CREATEPROFILE_ACTION = "CreateProfile";
	public final static String DELETEPROFILE_ACTION = "DeleteProfile";
	public final static String ERROR_ACTION = "error";

	private final static String LESSON_NAME = "CrossSiteScripting";
	private final static String JSP_PATH = "/lessons/" + LESSON_NAME + "/";
	
	private final static Integer DEFAULT_RANKING = new Integer(100);

	private static Connection connection = null;

	private Map lessonFunctions = new Hashtable();
		
	public static synchronized Connection getConnection(WebSession s) 
			throws SQLException, ClassNotFoundException
	{
		if ( connection == null )
		{
			connection = DatabaseUtilities.makeConnection( s );
		}
		
		return connection;
	}
	
	public CrossSiteScripting()
	{
		String myClassName = parseClassName(this.getClass().getName());
		registerAction(new ListStaff(this, myClassName, LISTSTAFF_ACTION));
		registerAction(new SearchStaff(this, myClassName, SEARCHSTAFF_ACTION));
		registerAction(new ViewProfile(this, myClassName, VIEWPROFILE_ACTION));
		registerAction(new EditProfile(this, myClassName, EDITPROFILE_ACTION));
		registerAction(new EditProfile(this, myClassName, CREATEPROFILE_ACTION));
		
		// These actions are special in that they chain to other actions.
		registerAction(new Login(this, myClassName, LOGIN_ACTION, getAction(LISTSTAFF_ACTION)));
		registerAction(new Logout(this, myClassName, LOGOUT_ACTION, getAction(LOGIN_ACTION)));
		registerAction(new FindProfile(this, myClassName, FINDPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
		registerAction(new UpdateProfile(this, myClassName, UPDATEPROFILE_ACTION, getAction(VIEWPROFILE_ACTION)));
		registerAction(new DeleteProfile(this, myClassName, DELETEPROFILE_ACTION, getAction(LISTSTAFF_ACTION)));
	}
	
	protected static String parseClassName(String fqcn)
	{
		String className = fqcn;
		
		int lastDotIndex = fqcn.lastIndexOf('.');
		if (lastDotIndex > -1)
			className = fqcn.substring(lastDotIndex + 1);
		
		return className;
	}
	
	protected void registerAction(LessonAction action)
	{
		lessonFunctions.put(action.getActionName(), action);		
	}
	
	/**
	 *  Gets the category attribute of the CrossSiteScripting object
	 *
	 * @return    The category value
	 */
	public Category getDefaultCategory()
	{
		return AbstractLesson.A4;
	}


	/**
	 *  Gets the hints attribute of the DirectoryScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		
		// Stage 1
		hints.add( "당신은 폼 입력 필드에 HTML 태그를 넣을 수 있다.You can put HTML tags in form input fields." );
		hints.add( "스크립트 태크를 읽는 누군가를 공격하기 위해서 스크립트 태그를 필드에 숨긴다. Bury a SCRIPT tag in the field to attack anyone who reads it." );
		hints.add( "입력: &lt;script language=\"javascript\" type=\"text/javascript\"&gt;alert(\"Ha Ha Ha\");&lt;/script&gt; 메세지 필드에 입력.in message fields." );
		hints.add( "입력: &lt;script&gtalert(\"document.cookie\");&lt;/script&gt; 메세지 필드에 입력.in message fields." );
		
		// Stage 2
		hints.add( "대부분의 스크립트들은 이와 같은 특수문자를 사용한다.: Many scripts rely on the use of special characters such as: &lt;" );
		hints.add( "특정 문자세트를 허락하는 것(positive filtering)은 문자 세트를 차단하는 것(negative filtering)보다 낫다. Allowing only a certain set of characters (positive filtering) is preferred to blocking a set of characters (negative filtering)." );
		hints.add( "java.util.regex 패키지는 문자열 필터링을 하기에 유용하다. The java.util.regex package is useful for filtering string values." );

		// Stage 3
		hints.add( "브라우저를 인식하고 구문분석 후 인코딩된 HTML 엔티티 컨텐츠를 디코딩하고 HTML 태그를 인터프리트한다.Browsers recognize and decode HTML entity encoded content after parsing and interpretting HTML tags." );
		hints.add( "html 엔티티 인코더는 parameterparser 클래스에 있다.An HTML entity encoder is provided in the ParameterParser class." );

		// Stage 4
		hints.add( "컨텐츠를 검사는 form submissions이 폼에서 데이터를 찾은 응답을 제공해준다.Examine content served in response to form submissions looking for data taken from the form." );

		// Stage 5
		hints.add( "Validate early.  Consider: out.println(\"Order for \" + request.getParameter(\"product\") + \" being processed...\");" );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the ParameterInjection object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "";
		
		if (!getLessonTracker(s).getCompleted())
		{
			switch (getStage(s))
			{
			case 1:
				instructions = "Stage " + getStage(s) + ": 저장되어 있는 XXS 공격을 실행한다.Execute a Stored Cross Site Scripting (XSS) attack.<br>" +
					"이번 훈련에서의 목표는 어플리케이션이 당신이 다른 사용자를 위해 만든 스크립트를 실행시키도록 하는 것이다.For this exercise, your mission is to cause the application to serve a script of your making " + 
					" to some other user.";
				break;
			case 2:
				instructions = "Stage " + getStage(s) + ": 입력 유효성을 이용하여 저장되어 있는 XSS를 막는다.Block Stored XSS using Input Validation.<br>" +
					"어플리케이션이 방금 익스플로잇한 취약점이 있는 입력 필드에 입력 유효성 검사를 실행하도록 수정할 수 있다.You will modify the application to perform input validation on the vulnerable input field " + 
					"you just exploited.";
				break;
			case 3:
				instructions = "Stage " + getStage(s) + ": 이전에 저장된 XSS 공격을 실행한다.Execute a previously Stored Cross Site Scripting (XSS) attack.<br>" + 
					"이 어플리케이션은 여전히 테이터베이스에 있는 스크립트에 취약하다. The application is still vulnerable to scripts in the database.  Trigger a pre-stored " +
					"이미 저장되어 있는 스크립트를 실행해서 직원 'David'로 로그인하고 Bruce의 프로필을 볼 수 있다.script by logging in as employee 'David' and viewing Bruce's profile.";
				break;
			case 4:
				instructions = "Stage " + getStage(s) + ": 출력 인코딩을 이용하여 저장된 XSS를 막는다.Block Stored XSS using Output Encoding.<br>" + 
					"데이터 인코딩은 데이터베이스에서 클라이언트가 어떤 스크립트가 피해가 없도록 하기 위해서 제공된다. Encode data served from the database to the client so that any scripts are rendered harmless.";
				break;
			case 5:
				instructions = "Stage " + getStage(s) + ": Reflect XSS 공격을 실행한다.Execute a Reflected XSS attack.<br>" + 
					"여기서의 당신의 목표는 Your goal here is to craft a link containing a script which the application will " + 
					"어플리케이션이 링크를 실행시킨 어떤 클라이언트로 바로 제공될 링크가 포함된 스크립트를 만는 것이다.serve right back to any client that activates the link.";
				break;
			case 6:
				instructions = "Stage " + getStage(s) + ": 입력 유효성을 이용하여 Reflected XSS를 막는다. Block Reflected XSS using Input Validation.<br>" +
					"방금 익스플로잇한 취약점을 끝내기 위해 얼마전 배운 입력유효성 검사를 이번 수업에 이용한다.Use the input validation techniques learned ealier in this lesson to close the vulnerability " +
					"you just exploited.";
				break;
			default:
				// Illegal stage value
				break;
			}
		}
		
		return instructions;

	}

	
	protected LessonAction getAction(String actionName)
	{
		return (LessonAction) lessonFunctions.get(actionName);
	}
	
	public void handleRequest(WebSession s)
	{
		if (s.getLessonSession(this) == null)
			s.openLessonSession(this);
		
		String requestedActionName = null;
		try
		{
			requestedActionName = s.getParser().getStringParameter("action");
		}
		catch (ParameterNotFoundException pnfe) 
		{
			// Let them eat login page.
			requestedActionName = LOGIN_ACTION;
		}

		if (requestedActionName != null)
		{
			try
			{
				LessonAction action = getAction(requestedActionName);
				
				if (action != null)
				{
					if (!action.requiresAuthentication() || action.isAuthenticated(s))
					{
						action.handleRequest(s);
						//setCurrentAction(s, action.getNextPage(s));
					}		
				}
				else
				{
					setCurrentAction(s, ERROR_ACTION);
				}
			}
			catch (ParameterNotFoundException pnfe)
			{
				System.out.println("Missing parameter");
				pnfe.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);												
			}
			catch (ValidationException ve)
			{
				System.out.println("Validation failed");
				ve.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);												
			}
			catch (UnauthenticatedException ue)
			{
				s.setMessage("Login failed");
				System.out.println("Authentication failure");
				ue.printStackTrace();
			}
			catch (UnauthorizedException ue2)
			{
				s.setMessage("You are not authorized to perform this function");
				System.out.println("Authorization failure");
				ue2.printStackTrace();
			}
			catch (Exception e)
			{
				// All other errors send the user to the generic error page
				System.out.println("handleRequest() error");
				e.printStackTrace();
				setCurrentAction(s, ERROR_ACTION);
			}
		}
		
		// All this does for this lesson is ensure that a non-null content exists.
		setContent(new ElementContainer());
	}
	
	public boolean isAuthorized(WebSession s, int userId, String functionId)
	{
		//System.out.println("Checking authorization from " + getCurrentAction(s));
		LessonAction action = (LessonAction) lessonFunctions.get(getCurrentAction(s));
		return action.isAuthorized(s, userId, functionId);
	}
	
	public int getUserId(WebSession s) throws ParameterNotFoundException
	{
		LessonAction action = (LessonAction) lessonFunctions.get(getCurrentAction(s));
		return action.getUserId(s);
	}
	
	public String getUserName(WebSession s) throws ParameterNotFoundException
	{
		LessonAction action = (LessonAction) lessonFunctions.get(getCurrentAction(s));
		return action.getUserName(s);
	}
	
	public String getTemplatePage(WebSession s)
	{
		return JSP_PATH + LESSON_NAME + ".jsp";
	}

	public String getPage(WebSession s)
	{
		String page = JSP_PATH + getCurrentAction(s) + ".jsp";
		//System.out.println("Retrieved sub-view page for " + this.getClass().getName() + " of " + page);
		
		return page;
	}
	
	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the CrossSiteScripting object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return "LAB: Cross Site Scripting (XSS)";
	}
	
    public String getSourceFileName()
    {
    		// FIXME: Need to generalize findSourceResource() and use it on the currently active 
    		// LessonAction delegate to get its source file.
        //return findSourceResource(getCurrentLessonScreen()....);
    		return super.getSourceFileName();
    }

}

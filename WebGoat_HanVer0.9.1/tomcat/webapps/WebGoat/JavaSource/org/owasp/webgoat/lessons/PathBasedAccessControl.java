package org.owasp.webgoat.lessons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.TD;
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
public class PathBasedAccessControl extends LessonAdapter
{
	private static final int _80 = 80;
	private final static String FILE = "File";


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
			String dir = s.getContext().getRealPath( "/lesson_plans" );
			File d = new File( dir );

			Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setWidth("90%").setAlign("center");

			if ( s.isColor() )
			{
				t.setBorder( 1 );
			}

			String[] list = d.list();
			String listing = splitMessage(" <p><B>Current Directory is:</B> " + Encoding.urlDecode( dir ), _80, true) + "<br><br> Choose the file to view:</p>";
			
			TR tr = new TR();
			tr.addElement(new TD().setColSpan(2).addElement( new StringElement(listing) ));
			t.addElement(tr);
			
			tr = new TR();
			tr.addElement( new TD().setWidth("35%").addElement( ECSFactory.makePulldown( FILE, list, "", 15 )));
			tr.addElement( new TD().addElement( ECSFactory.makeButton( "View File" )));
			t.addElement(tr);
			
			ec.addElement( t );


			// FIXME: would be cool to allow encodings here -- hex, percent, url, etc...
			String file = s.getParser().getRawParameter( FILE, "" );

			// defuse file searching
			boolean illegalCommand = s.isDefuseOSCommands();
			if ( s.isDefuseOSCommands() )
			{
				// allow them to look at any file in the webgoat hierachy.  Don't allow them
				// to look about the webgoat root, except to see the LICENSE file
				if( upDirCount( file ) == 3 && !file.endsWith("LICENSE")) 
				{
					s.setMessage( "잘못된 접근" );
					s.setMessage( "올바른 접근이다..  " +
								  "명령은 운영체제를 손상할지도 모른다.  " +
								  "이 디렉토리에서는 1개의 파일을 보는것이 허용된다. ");;
				} 
				else if ( upDirCount( file ) > 2 )
				{
					s.setMessage( "잘못된 접근" );
					s.setMessage( "올바른접근이다..  " +
								  "명령은 운영체제를 손상할지도 모른다.  " +
								  "이 디렉토리에서는 1개의 파일을 보는것이 허용된다. ");
				} 
				else 
				{
					illegalCommand = false;
				}
			}
			
			// Using the URI supports encoding of the data.  
			// We could force the user to use encoded '/'s == %2f to make the lesson more difficult.
			// We url Encode our dir name to avoid problems with special characters in our own path.
			//File f = new File( new URI("file:///" + Encoding.urlEncode(dir).replaceAll("\\\\","/") + "/" + file.replaceAll("\\\\","/")) );
			File f = new File( (dir + "\\" + file).replaceAll("\\\\","/"));

			if( s.isDebug() )
			{
				 
				s.setMessage("File: " + file );
				s.setMessage("Dir: " + dir );
				//s.setMessage("File URI: " + "file:///" + (Encoding.urlEncode(dir) + "\\" + Encoding.urlEncode(file)).replaceAll("\\\\","/"));
				s.setMessage("  - isFile(): " + f.isFile() );
				s.setMessage("  - exists(): " + f.exists() );
			}
			if ( !illegalCommand ) 
			{	
				if ( f.isFile() && f.exists() )
				{
					// Don't set completion if they are listing files in the 
					// directory listing we gave them.
					if ( upDirCount( file ) >= 1 )
					{	
						s.setMessage( "Congratulations! Access to file allowed" );
						s.setMessage( " ==> " + Encoding.urlDecode( f.getCanonicalPath() ));
						makeSuccess( s );
					} 
					else
					{
						s.setMessage( "파일은 디렉토리 안에 주어진다. - 다시시도해라!" );
						s.setMessage( " ==> " + Encoding.urlDecode( f.getCanonicalPath() ));
					}
				}
				else if ( file != null && file.length() != 0 )
				{
					s.setMessage( "파일에 접근/디렉토리 \"" + Encoding.urlDecode( f.getCanonicalPath() ) + "\" denied" );
 				}
				else
				{
					// do nothing, probably entry screen
				}

				try 
				{
					// Show them the file
					// Strip out some of the extra html from the "help" file
					ec.addElement( new BR() );
					ec.addElement( new BR() );
					ec.addElement( new HR().setWidth("100%") );
					ec.addElement( splitMessage("보기 파일: " + f.getCanonicalPath(), _80, true) );
					ec.addElement( new HR().setWidth("100%") );
					if ( f.length() > 8000 )
					{
						throw new Exception("파일은 크다.");
					}
					String fileData= getFileText( new BufferedReader( new FileReader( f ) ), false );
					if ( fileData.indexOf(0x00) != -1)
					{
						throw new Exception("파일은이진수다.");
					}
					ec.addElement( new StringElement( fileData.replaceAll(System.getProperty("line.separator"),"<br>")
								.replaceAll("(?s)<!DOCTYPE.*/head>","")
								.replaceAll("<br><br>","<br>")
							.replaceAll("<br>\\s<br>","<br>")
							.replaceAll("<\\?", "&lt;")
							.replaceAll("<(r|u|t)", "&lt;$1")));
				}
				catch (Exception e)
				{
					ec.addElement( new BR() );
					ec.addElement("The following error occurred while accessing the file: <");
					ec.addElement( splitMessage(e.getMessage(), _80, true) );
				}			
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}

	private int upDirCount( String fileName )
	{
		int count = 0;
		int startIndex = fileName.indexOf("..");
		while ( startIndex != -1 )
		{
			count++;
			startIndex = fileName.indexOf("..", startIndex+1);
		}
		return count;
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @return    DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A2;
	}


	/**
	 *  Gets the hints attribute of the AccessControlScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List hints = new ArrayList();
		hints.add( "대부분의 운영체제는 경로에서 특수문자를 허락한다." );
		hints.add( "이 디렉토리 ( tomcat\webapps\WebGoat\lesson_plans ) 를 찾기위해서파일 익스플로어를 사용해라" );
		hints.add( "..경로로시도해봐라" );
		hints.add( "..\..\..\ 경로처럼 시도해봐라" );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the WeakAccessControl object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "The '" + s.getUserName() + "' user has access to all the files in the " +
		                      "lesson_plans directory.  Try to break the access control mechanism and access a " +
							  "resource that is not in the listed directory.  After selecting a file to view, WebGoat " +
							  "will report if access to the file was granted.  An interesting file to try and obtain might " +
							  "be a file like tomcat/conf/tomcat-users.xml";

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
		return ( "경로를 우회하는 방법에 기초를 두었다." );
	}
}


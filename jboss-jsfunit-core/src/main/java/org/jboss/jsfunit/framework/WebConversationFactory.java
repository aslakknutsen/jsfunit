/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jsfunit.framework;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.parsing.HTMLParserFactory;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The WebConversationFactory dispenses a clean WebConversation for use with
 * JSFUnit.  The WebConversation returned will be set up as if it is the
 * first request to JSF.
 *
 * @author Stan Silvert
 */
public class WebConversationFactory
{
   public static String JSF_UNIT_CONVERSATION_FLAG = WebConversationFactory.class.getName() + ".testing_flag";
   public static String WAR_URL = WebConversationFactory.class.getName() + ".WARURL";
   
   private static ThreadLocal tlsession = new ThreadLocal()
   {
      protected Object initialValue()
      {
         return null;
      }
   };
   
   private static ThreadLocal warURL = new ThreadLocal()
   {
      protected Object initialValue()
      {
         return null;
      }
   };
   
   public static void setThreadLocals(HttpServletRequest req)
   {
      tlsession.set(req.getSession());
      String stringWARURL = makeWARURL(req);
      warURL.set(stringWARURL);
   }
   
   protected static HttpSession getSessionFromThreadLocal()
   {
      return (HttpSession)tlsession.get();
   }
   
   public static String makeWARURL(HttpServletRequest req)
   {
     return req.getScheme() + "://" + req.getServerName() + ":" + 
            req.getServerPort() + req.getContextPath();
   }
   
   public static void removeThreadLocals()
   {
      tlsession.remove();
      warURL.remove();
   }
   
   /**
    * Don't allow a new instance of WebConversationFactory
    */
   private WebConversationFactory()
   {
   }
   
   /**
    * Return a clean WebConversation to start sending JSF requests.
    *
    * @return A clean WebConverstaion for JSFUnit.
    */
   public static WebConversation makeWebConversation()
   {
      HTMLParserFactory.useJTidyParser();
      HttpUnitOptions.setScriptingEnabled(false);
      WebConversation wc = new WebConversation();
      HttpSession session = getSessionFromThreadLocal();
      
      if (session == null)
      {
         throw new IllegalStateException("Can not find HttpSession.  Perhaps JSFUnitFilter has not run?");
      }
      
      clearSession(session);
      wc.putCookie("JSESSIONID", session.getId());
      wc.putCookie(JSF_UNIT_CONVERSATION_FLAG, JSF_UNIT_CONVERSATION_FLAG);
      return wc;
   }
   
   /**
    * Return a clean WebClient to start sending JSF requests.
    *
    * @param browserVersion The browser version to emulate. (a typical default is BrowserVersion.getDefault() )
    * @param proxyHost The server that will act as proxy (a typical default is null)
    * @param proxyPort The port to use for the proxy server (a typical default is 0 )
    *
    * @return A clean WebClient for JSFUnit.
    */
   public static WebClient makeWebClient(BrowserVersion browserVersion, String proxyHost, int proxyPort)
   {
      WebClient wc = null;
      if (proxyHost != null) wc = new WebClient(browserVersion, proxyHost, proxyPort);
      if (proxyHost == null) wc = new WebClient(browserVersion);
      
      HttpSession session = getSessionFromThreadLocal();
      
      if (session == null)
      {
         throw new IllegalStateException("Can not find HttpSession.  Perhaps JSFUnitFilter has not run?");
      }
      
      clearSession(session);
      wc.addRequestHeader("Cookie", "JSESSIONID=" + session.getId() + "; " + JSF_UNIT_CONVERSATION_FLAG + "=" + JSF_UNIT_CONVERSATION_FLAG);
      return wc;
   }
   
   /**
    * Determine if the WebConversation was created with this factory.
    *
    * @return <code>true</code> if the WebConversation was created with this
    *         factory.  <code>false</code> otherwise.
    */
   public static boolean isJSFUnitWebConversation(WebConversation webConversation)
   {
      if (webConversation == null) return false;
      String jsessionid = webConversation.getCookieValue("JSESSIONID");
      String testingFlag = webConversation.getCookieValue(JSF_UNIT_CONVERSATION_FLAG);
      if ( (jsessionid == null) || (testingFlag == null) )
      {
          return false;
      }
      
      return true;
   }
   
   // We need to start with a clean (but not new) session at the beginning of each
   // WebConversation.
   protected static void clearSession(HttpSession session)
   {
      for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();)
      {
         session.removeAttribute((String)e.nextElement());
      }
   }
   
   /**
    * Get the base URL for this web app.  This will return a String in
    * the form scheme://servername:port/contextpath.
    *
    * @return The base URL for this web app.
    */
   public static String getWARURL()
   {
      return (String)warURL.get();
   }
   
}

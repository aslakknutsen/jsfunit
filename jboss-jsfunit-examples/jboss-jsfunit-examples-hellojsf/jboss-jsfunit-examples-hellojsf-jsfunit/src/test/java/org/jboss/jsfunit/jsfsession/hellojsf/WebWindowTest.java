/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.jsfsession.hellojsf;

import com.gargoylesoftware.htmlunit.WebWindow;
import java.io.IOException;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * This class tests all of the API's in the JSFClientSession and JSFServerSession.
 * 
 * 
 * 
 * @author Stan Silvert
 */
public class WebWindowTest extends ServletTestCase
{
   private JSFSession jsfSession;
   private JSFClientSession client;
   private JSFServerSession server;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException
   {
      // Initial JSF request
      this.jsfSession = new JSFSession("/parentwindow.faces");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( WebWindowTest.class  );
   }
   
   public void testDoNotFollowNewWindow() throws IOException
   {
      System.out.println("********** Start testDoNotFollowNewWindow");
      client.click("child1");
      assertFalse(client.getPageAsText().contains("Hello from Window #1"));
      System.out.println("********** End testDoNotFollowNewWindow");
   }
   
   public void testSetCurrentWindow() throws IOException
   {
      System.out.println("*********** Start testSetCurrentWindow");
      client.click("child1");
      //System.out.println("current page=" + client.getPageAsText());
      client.click("child2");
      client.setCurrentWindow("child1");
      assertTrue(client.getPageAsText().contains("Hello from Window #1"));
      client.setCurrentWindow("child2");
      assertTrue(client.getPageAsText().contains("Hello from Window #2"));
      System.out.println("*********** End testSetCurrentWindow");
   }
   
   public void testCloseWindow() throws IOException
   {
      System.out.println("******** Start testCloseWindow");
      WebWindow parentWindow = client.getCurrentWindow();
      client.click("child1");
      client.setCurrentWindow("child1");
      System.out.println("Closing window");
      client.click("close");
      System.out.println("window closed");
      assertNotSame("child1", client.getCurrentWindow().getName());
      System.out.println("asserting parent window is current");
      assertEquals(parentWindow, client.getCurrentWindow());
      System.out.println("******** End testCloseWindow");
   }
   
   public void testThreeOpenWindows() throws IOException
   {
      client.click("child1");
      client.click("child2");
      int count = 0;
      for (Iterator i = jsfSession.getWebClient().getWebWindows().iterator(); i.hasNext(); i.next()) count++;
      assertEquals(3, count);
   }
}

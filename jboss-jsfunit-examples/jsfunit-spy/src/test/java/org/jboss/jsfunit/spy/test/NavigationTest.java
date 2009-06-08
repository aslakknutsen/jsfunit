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

package org.jboss.jsfunit.spy.test;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.SpyManager;

/**
 *
 * @author Stan Silvert
 */
public class NavigationTest extends ServletTestCase 
{
   private JSFServerSession server;
   private JSFClientSession client;
   private SpyManager spyManager;
   private RequestData request;
   
   @Override
   public void setUp() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/jsf-spy-ui/index.jsf");
      this.server = jsfSession.getJSFServerSession();
      this.client = jsfSession.getJSFClientSession();
      this.spyManager = SpyManager.getInstance();
      this.request = spyManager.getCurrentRequest();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( NavigationTest.class  );
   }

   public void testGotoScopesView() throws IOException
   {
      client.click(":0:selectSession");
      assertEquals("/jsf-spy-ui/sessionview.xhtml", server.getCurrentViewID());
      
      client.click(":0:selectScopes");
      assertEquals("/jsf-spy-ui/scopesview.xhtml", server.getCurrentViewID());
   }
   
   public void testGotoHttpRequestView() throws IOException
   {
      client.click(":0:selectSession");
      assertEquals("/jsf-spy-ui/sessionview.xhtml", server.getCurrentViewID());
      
      client.click(":0:selectHttpServletRequest");
      assertEquals("/jsf-spy-ui/httprequestview.xhtml", server.getCurrentViewID());
   }
   
   public void testGotoPerfView() throws IOException
   {
      client.click(":0:selectSession");
      assertEquals("/jsf-spy-ui/sessionview.xhtml", server.getCurrentViewID());
      
      client.click(":0:selectPerfView");
      assertEquals("/jsf-spy-ui/perfview.xhtml", server.getCurrentViewID());
   }
   
   public void testTopmenuNavigation() throws IOException
   {
      client.click(":0:selectSession");
      client.click(":0:selectScopes");
      
      client.click("topmenuform:allsessions");
      assertEquals("/jsf-spy-ui/index.xhtml", server.getCurrentViewID());
      
      client.click("topmenuform:sessionview");
      assertEquals("/jsf-spy-ui/sessionview.xhtml", server.getCurrentViewID());
      
      client.click("topmenuform:scopesview");
      assertEquals("/jsf-spy-ui/scopesview.xhtml", server.getCurrentViewID());
      
      client.click("topmenuform:perfview");
      assertEquals("/jsf-spy-ui/perfview.xhtml", server.getCurrentViewID());
   }
   
}

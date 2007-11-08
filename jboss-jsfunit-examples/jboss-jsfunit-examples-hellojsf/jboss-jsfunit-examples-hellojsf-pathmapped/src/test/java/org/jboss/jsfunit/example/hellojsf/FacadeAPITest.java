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

package org.jboss.jsfunit.example.hellojsf;

import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.xml.sax.SAXException;

/**
 * This class tests all of the API's in the JSFClientSession and JSFServerSession.
 * 
 * 
 * 
 * @author Stan Silvert
 */
public class FacadeAPITest extends ServletTestCase
{
   private JSFClientSession client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.
    * Also, set the current naming container to form1 so that any API
    * call on the client side will refer to the parameters of "form1".
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = new JSFClientSession("/jsf/index.jsp");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FacadeAPITest.class );
   }
   
   /**
    */
   public void testGetCurrentViewId() throws IOException, SAXException
   {
      JSFServerSession server = new JSFServerSession(client);
      
      // Test navigation to initial viewID
      assertEquals("/index.jsp", server.getCurrentViewID());
      assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }
   
   public void testSetParamAndSubmit() throws IOException, SAXException
   {
      client.setParameter("input_foo_text", "Stan"); 
      client.submit("submit_button");
      
      JSFServerSession server = new JSFServerSession(client);
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   /**
    * Tests JSFClientSession.submit().  This can only be called if there is
    * only one submit button on the form.
    */
   public void testNoArgSubmit() throws IOException, SAXException
   {
      client.submit("goodbye_button");  // go to finalgreeting page
      client.submit(); // only one submit button on finalgreeting page
      JSFServerSession server = new JSFServerSession(client);
      assertEquals("/index.jsp", server.getCurrentViewID());  // test that we are back on the first page
   }
   
   public void testServerSideComponentValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      JSFServerSession server = new JSFServerSession(client);
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   /**
    * This demonstrates how to test managed beans.
    */
   public void testManagedBeanValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      JSFServerSession server = new JSFServerSession(client);
      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
}

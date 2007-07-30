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

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ComponentIDNotFoundException;
import org.jboss.jsfunit.facade.FormNotFoundException;
import org.jboss.jsfunit.facade.ServerFacade;
import org.xml.sax.SAXException;

/**
 * This class tests all of the API's in the ClientFacade and ServerFacade.
 *
 * @author Stan Silvert
 */
public class FacadeAPITest extends ServletTestCase
{
   private ClientFacade client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = new ClientFacade("/index.faces");
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
      ServerFacade server = new ServerFacade(client);
      
      // Test navigation to initial viewID
      assertEquals("/index.jsp", server.getCurrentViewId());
      assertEquals(server.getCurrentViewId(), server.getFacesContext().getViewRoot().getViewId());
   }
   
   public void testSetParamAndSubmit() throws IOException, SAXException
   {
      client.setParameter("input_foo_text", "Stan"); 
      client.submit("submit_button");
      
      ServerFacade server = new ServerFacade(client);
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   public void testSetCheckbox() throws IOException, SAXException
   {
      ServerFacade server = new ServerFacade(client);
      client.setCheckbox("funcheck", false);
      client.submit("submit_button");
      assertFalse((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
      
      client.setCheckbox("funcheck", true);
      client.submit("submit_button");
      assertTrue((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
   }
   
   /**
    * Tests ClientFacade.submit().  This can only be called if there is
    * only one submit button on the form.
    */
   public void testNoArgSubmit() throws IOException, SAXException
   {
      client.submit("goodbye_button");  // go to finalgreeting page
      client.submit(); // only one submit button on finalgreeting page
      ServerFacade server = new ServerFacade(client);
      assertEquals("/index.jsp", server.getCurrentViewId());  // test that we are back on the first page
   }
   
   public void testServerSideComponentValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      ServerFacade server = new ServerFacade(client);
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   /**
    * This demonstrates how to test managed beans.
    */
   public void testManagedBeanValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      ServerFacade server = new ServerFacade(client);
      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testClickALink() throws IOException, SAXException
   {
      client.clickLink("SourceSimplifiedHelloJSFIntegrationTest");
      WebResponse response = client.getWebResponse();
      assertTrue(response.getText().contains("public class SimplifiedHelloJSFIntegrationTest"));
   }
   
   public void testGetForm() throws IOException, SAXException
   {
      // test passing a form param
      WebForm form = client.getForm("input_foo_text");
      assertEquals("form1", form.getID());
      
      // test passing a component in the form that is not a param
      form = client.getForm("prompt");
      assertEquals("form1", form.getID());
      
      // test passing the form ID itself
      form = client.getForm("form1");
      assertEquals("form1", form.getID());
      
      // test ComponentIDNotFoundException
      try
      {
         form = client.getForm("bogus");
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
      
      // test FormNotFoundException
      // "title" is on the page (a valid component) but it's not inside the form
      try
      {
         form = client.getForm("title");
         fail("Expected FormNotFoundException");
      }
      catch (FormNotFoundException e)
      {
         // OK
      }
   }
}

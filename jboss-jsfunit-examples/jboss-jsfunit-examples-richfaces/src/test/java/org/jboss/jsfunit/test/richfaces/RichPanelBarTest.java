/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichPanelBarTest extends ServletTestCase
{
   public void testRichPanelBar() throws IOException
   { 
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/panelBar.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      RichFacesClient richClient = new RichFacesClient(client);

      HtmlDivision leverageTag = (HtmlDivision)client.getElement("leverage");
      assertTrue(isDisplayed(leverageTag));

      richClient.clickPanelBarItem("addajax");
      assertFalse(isDisplayed(leverageTag));

      HtmlDivision addajaxTag = (HtmlDivision)client.getElement("addajax");
      assertTrue(isDisplayed(addajaxTag));

      richClient.clickPanelBarItem("testcomponents");
      assertFalse(isDisplayed(addajaxTag));

      HtmlDivision testcomponentsTag = (HtmlDivision)client.getElement("testcomponents");
      assertTrue(isDisplayed(testcomponentsTag));
   }

   private boolean isDisplayed(HtmlDivision divTag)
   {
      return divTag.asXml().contains("style=\"display: block");
   }
   
   public static Test suite()
   {
      return new TestSuite( RichPanelBarTest.class );
   }
}

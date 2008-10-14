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
package org.jboss.jsfunit.example.richfaces;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichInputNumberSpinnerTest extends ServletTestCase
{
   public void testNumberSpinners() throws IOException, SAXException
   {
      /* disable this test.  It will be converted when we get rid of HttpUnit
      JSFClientSession client = new JSFClientSession("/richfaces/inputNumberSpinner.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      ajaxClient.setInputNumberSpinner("spinner1", "45");
      ajaxClient.setInputNumberSpinner("spinner2", "90");
      ajaxClient.ajaxSubmit("form1");
      
      String value = (String)server.getComponentValue("spinner1");
      assertEquals("45", value);
      value = (String)server.getComponentValue("spinner2");
      assertEquals("90", value); */
   }
   
   public static Test suite()
   {
      return new TestSuite( RichInputNumberSpinnerTest.class );
   }
}

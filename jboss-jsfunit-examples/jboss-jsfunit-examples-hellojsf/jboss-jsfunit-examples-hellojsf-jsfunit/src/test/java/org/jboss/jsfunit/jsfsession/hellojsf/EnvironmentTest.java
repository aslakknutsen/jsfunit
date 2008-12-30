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

package org.jboss.jsfunit.jsfsession.hellojsf;

import java.io.IOException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.Environment;

/**
 * This class relies on setting two system properties to test the Environment
 * class against.
 * 
 * @author Stan Silvert
 */
public class EnvironmentTest extends ServletTestCase
{
   private int jsfMajorVersionProp = 1;
   private int jsfMinorVersionProp = 1;
   
   public void setUp() throws IOException
   {
      Properties props = new Properties();
      props.load(getClass().getResourceAsStream("/jsfversion.properties"));
      
      try
      {
         jsfMajorVersionProp = Integer.parseInt(props.getProperty("jsfMajorVersion"));
         jsfMinorVersionProp = Integer.parseInt(props.getProperty("jsfMinorVersion"));
      }
      catch (NumberFormatException e)
      {
         // ignore
      }
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( EnvironmentTest.class  );
   }
   
   public void testJSFMajorVersion() 
   {
      assertEquals(this.jsfMajorVersionProp, Environment.getJSFMajorVersion());
   }
   
   public void testJSFMinorVersion()
   {
      assertEquals(this.jsfMinorVersionProp, Environment.getJSFMinorVersion());
   }
   
   public void testJSF12Compatible()
   {
      boolean compatible = Environment.is12Compatible();
      
      // JSF 1.2
      if (jsfMinorVersionProp == 2) assertTrue(compatible);
      
      // JSF 2.0
      if (jsfMajorVersionProp == 2) assertTrue(compatible); 
      
      // JSF 1.1
      if ((jsfMajorVersionProp == 1) && (jsfMinorVersionProp == 1))
      {
         assertFalse(compatible);
      }
   }
   
   public void testJSF20Compatible()
   {
      boolean compatible = Environment.is20Compatible();
      
      // JSF 1.2
      if (jsfMinorVersionProp == 2) assertFalse(compatible);
      
      // JSF 2.0
      if (jsfMajorVersionProp == 2) assertTrue(compatible); 
      
      // JSF 1.1
      if ((jsfMajorVersionProp == 1) && (jsfMinorVersionProp == 1))
      {
         assertFalse(compatible);
      }
   }
      
}

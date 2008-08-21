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

/**
 * Contains methods to determine the test environment.
 *
 * @author Stan Silvert
 */
public class Environment
{
   
   // don't allow an instance of this static utility class
   private Environment()
   {
   }
   
   /**
    * Until JSF 2.0 is released, this will always return 1.
    *
    * @return 1
    */
   public static int getJSFMajorVersion()
   {
      return 1;
   }
   
   /**
    * Returns the JSF minor version.  The method will not detect JSF 1.0.
    *
    * @return 2 for JSF 1.2, otherwise returns 1
    *
    * @throws IllegalStateException if minor version can not be determined.
    */
   public static int getJSFMinorVersion()
   {
      try
      {
         Class appClass = loadClass("javax.faces.application.Application");
      
         if (appClass == null) return 1;
         
         if ((appClass.getMethod("getELResolver") != null) &&
             (appClass.getMethod("getExpressionFactory") != null))
         {
            return 2;
         }
      }
      catch (NoSuchMethodException e)
      {
         // do nothing
      }
      catch (NoClassDefFoundError e)
      {
         // do nothing
      }
      catch (Exception e)
      {
         throw new IllegalStateException(e);
      }
      
      return 1;
   }
   
   public static boolean isSeamInitialized()
   {
      Class lifecycle = loadClass("org.jboss.seam.contexts.Lifecycle");
      if (lifecycle == null) return false;
      
      try
      {
         Boolean returnVal = (Boolean)lifecycle.getMethod("isApplicationInitialized", null)
                                               .invoke(null, null);
         return returnVal.booleanValue();
      }
      catch (Exception e)
      {
         return false;
      }
   }
   
   /**
    * Load a class using the context class loader.
    *
    * @param clazz The class name to load.
    *
    * @return The Class or <code>null</code> if not found.
    */
   public static Class loadClass(String clazz)
   {
      try
      {
         return Thread.currentThread()
                      .getContextClassLoader()
                      .loadClass(clazz);
      }
      catch (NoClassDefFoundError e)
      {
         return null;
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
      catch (Exception e)
      {
         throw new IllegalStateException(e);
      }
   }
   
}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * This exception is thrown if the JSFSession encounters a Facelets
 * error page.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class FaceletsErrorPageException extends RuntimeException
{
   private static final String HEADER = "\r\n----- EXTRACTED FROM FACELETS ERROR PAGE -------\r\n";
   private static final String FOOTER = "\r\n------END FACELETS ERROR PAGE ------------------\r\n";
   
   private static final String STACK_TRACE_BEGIN = "<div id=\"trace\" class=\"grayBox\"><pre><code>";
   private static final String STACK_TRACE_END = "</code></pre></div>";
   
   private static final String TRACE_OFF = "span id=\"traceOff\"";
   private static final String TRACE_ON = "span id=\"traceOn\"";
   private static final String TREE_OFF = "span id=\"treeOff\"";
   private static final String TREE_ON = "span id=\"treeOn\"";
   
   /**
    * Static utility method to determine if the current page is a
    * Facelets error page.
    * 
    * @param response The WebResponse from HtmlUnit
    */
   public static boolean isFaceletsErrorPage(WebResponse response)
   {
      byte[] responseBytes = response.getContentAsBytes();
      if ((responseBytes == null) || (responseBytes.length == 0)) return false;
      
      String responseBody = new String(responseBytes);
      return response.getContentType().equals("text/html") &&
             responseBody.contains(TRACE_OFF) &&
             responseBody.contains(TRACE_ON) &&
             responseBody.contains(TREE_OFF) &&
             responseBody.contains(TREE_ON) &&
             responseBody.contains("Generated by Facelets");
   }
   
   FaceletsErrorPageException(WebResponse response)
   {
      super(extractStackTrace(response));
   }
   
   private static String extractStackTrace(WebResponse response)
   {
      String responseBody = new String(response.getContentAsBytes());
      int beginning = responseBody.indexOf(STACK_TRACE_BEGIN) + STACK_TRACE_BEGIN.length();
      int ending = responseBody.indexOf(STACK_TRACE_END, beginning);
      return HEADER + responseBody.substring(beginning, ending) + FOOTER;
   }
}

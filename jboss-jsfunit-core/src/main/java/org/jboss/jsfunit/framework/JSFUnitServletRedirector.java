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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.cactus.server.ServletTestRedirector;
import org.jboss.jsfunit.seam.SeamUtil;

/**
 * The JSFUnitServletRedirector does JSFUnit cleanup at the end of each
 * redirector request.
 *
 * @author Stan Silvert
 */
public class JSFUnitServletRedirector extends ServletTestRedirector
{
   public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException
   {
      super.doPost(httpServletRequest, httpServletResponse);
      cleanUp(httpServletRequest);
   }

   public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException
   {
      super.doGet(httpServletRequest, httpServletResponse);
      cleanUp(httpServletRequest);
   }
   
   private void cleanUp(HttpServletRequest httpServletRequest)
   {
      if (SeamUtil.isSeamInitialized())
      {
         SeamUtil.invalidateSeamSession(httpServletRequest);
      }
      
      HttpSession session = httpServletRequest.getSession(false);
      if (session != null)
      {
         session.invalidate();
      }
   }
   
}

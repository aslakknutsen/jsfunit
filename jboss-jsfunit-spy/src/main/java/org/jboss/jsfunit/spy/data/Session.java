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

package org.jboss.jsfunit.spy.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class Session 
{
   private List<RequestData> requestList = new ArrayList<RequestData>();

   private String sessionId;
   private long creationTime;
   private long maxInactiveInterval;
   
   Session (FacesContext facesContext)
   {
      HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
      this.sessionId = session.getId();
      this.creationTime = session.getCreationTime();
      this.maxInactiveInterval = session.getMaxInactiveInterval();
   }

   // returns its position in the request list
   synchronized int addNewRequestData(RequestData requestData, FacesContext facesContext)
   {
      requestList.add(requestData);
      return requestList.indexOf(requestData);
   }
   
   public synchronized List<RequestData> getRequests()
   {
      return Collections.unmodifiableList(requestList);
   }
   
   public String getId()
   {
      return this.sessionId;
   }
   
   public long getCreationTime()
   {
      return this.creationTime;
   }
   
   public long getMaxInactiveInterval()
   {
      return this.maxInactiveInterval;
   }
   
   @Override
   public String toString()
   {
      return this.sessionId;
   }
   
}

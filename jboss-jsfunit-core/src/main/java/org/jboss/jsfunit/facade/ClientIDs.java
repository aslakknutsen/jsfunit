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

package org.jboss.jsfunit.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.framework.FacesContextBridge;

/**
 * This immutable helper class gathers all the client IDs from the current 
 * component tree.  It then allows finding a full client ID given a suffix of 
 * that ID.  This suffix is usually just the component ID, but for specificity 
 * it can also include one or more of a component's naming containers such as in 
 * "mysubview:myform:mycomponentID".
 *
 * @author Stan Silvert
 */
public class ClientIDs
{
   private List<String> allClientIDs = new ArrayList<String>();
   private Map<String, UIComponent> allComponents = new HashMap<String, UIComponent>();
   
   // key = clientID; value = set of ancestor clientID's including my own
   private Map<String, Set> ancestors = new HashMap<String, Set>();
   
   /**
    * Create a new instance of ClientIDs.
    */
   ClientIDs()
   {
      FacesContext facesContext = FacesContextBridge.getCurrentInstance();
      UIComponent component = facesContext.getViewRoot();
      addAllIDs(component, facesContext);
   }

   // recursively walk the component tree and add all the client IDs to the list
   private void addAllIDs(UIComponent component, FacesContext facesContext)
   {
      if (component == null) return;
      
      addClientID(component, facesContext);
      newAncestorEntry(component, facesContext);
      
      if (component instanceof UIData)
      {
         addUIData((UIData)component, facesContext);
         return;
      }
      
      for (Iterator facetsAndChildren = component.getFacetsAndChildren(); facetsAndChildren.hasNext();)
      {
         UIComponent facetOrChild = (UIComponent)facetsAndChildren.next();
         addAncestors(component, facetOrChild, facesContext);
         addAllIDs(facetOrChild, facesContext);
      }
   }
   
   private void newAncestorEntry(UIComponent component, FacesContext facesContext)
   {
      String clientID = component.getClientId(facesContext);
      if (ancestors.get(clientID) == null)
      {
         HashSet myAncestors = new HashSet();
         myAncestors.add(clientID); // I am my own ancestor
         ancestors.put(clientID, myAncestors);
      }
   }
   
   private void addAncestors(UIComponent parent, UIComponent child, FacesContext facesContext)
   {
      if (child == null) return;
      addAncestors(parent.getClientId(facesContext), child.getClientId(facesContext));
   }
      
   private void addAncestors(String parentClientID, String childClientID)
   {
      Set myAncestors = new HashSet(ancestors.get(parentClientID));
      myAncestors.add(childClientID); // I am my own ancestor
      //System.out.println("ancestors: " + childClientID + " = " + myAncestors);
      ancestors.put(childClientID, myAncestors);
   }
   
   private void addUIData(UIData component, FacesContext facesContext)
   {
      String parentClientID = component.getClientId(facesContext);
      // TODO: find out if headers and footers are found
      for (int i=0; i < component.getRowCount(); i++)
      {
         component.setRowIndex(i);
         addAncestors(parentClientID, component.getClientId(facesContext));
         for (Iterator facetsAndChildren = component.getFacetsAndChildren(); facetsAndChildren.hasNext();)
         {
            UIComponent facetOrChild = (UIComponent)facetsAndChildren.next();
            addAncestors(component, facetOrChild, facesContext);
            addAllIDs(facetOrChild, facesContext);
         } 
      }
      
      component.setRowIndex(-1);
   }
   
   private void addClientID(UIComponent component, FacesContext facesContext)
   {
      if (component instanceof UIViewRoot) return;
      
      String clientId = component.getClientId(facesContext);
      if (clientId == null) return;

      //System.out.println("adding clientID=" + clientId + "/ className=" + component.getClass().getName() + " / identity=" + component.hashCode());
      
      allClientIDs.add(clientId);
      allComponents.put(clientId, component);
   }
   
   /**
    * Given a client ID or client ID suffix, find the fully-qualified client ID.
    * 
    * @param suffix The full client ID or a suffix of the client ID.
    *
    * @return The fully-qualified client ID.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    */
   public String findClientID(String suffix)
   {
      if (suffix == null) throw new NullPointerException();
      
      List<String> matches = new ArrayList<String>();
      for (Iterator<String> ids = allClientIDs.iterator(); ids.hasNext();)
      {
         String id = ids.next();
         if (id.endsWith(suffix)) matches.add(id);
      }
      
      if (matches.size() == 1) return matches.get(0);
      
      if (matches.isEmpty()) throw new ComponentIDNotFoundException(suffix);
      
      throw new DuplicateClientIDException(suffix, matches);
   }
   
   /**
    * Given a client ID suffix, find the matching UIComponent.
    * 
    * @param suffix The full client ID or a suffix of the client ID.
    *
    * @return The UIComponent.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    */
   public UIComponent findComponent(String suffix)
   {
      return allComponents.get(findClientID(suffix));
   }
   
   /**
    * Determines if a component with a given clientID has an ancestor with a
    * given ancestorClientID.
    */
   public boolean isAncestor(String clientID, String ancestorClientID)
   {
      Set ancestorSet = ancestors.get(clientID);
      if (ancestorSet == null) return false;
      return ancestorSet.contains(ancestorClientID);
   }
}
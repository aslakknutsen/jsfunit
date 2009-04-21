/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.jsfunit.jsf2.beans;

import java.util.Map;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author ssilvert
 */
public class ScopeAwareBean {

   public String getScope()
   {
      FacesContext facesCtx = FacesContext.getCurrentInstance();
      ExternalContext extCtx = facesCtx.getExternalContext();
      
      Map scopeMap = extCtx.getRequestMap();
      if (scopeMap.containsValue(this)) return "request";
       
      scopeMap = facesCtx.getViewRoot().getViewMap();
      if (scopeMap.containsValue(this)) return "view";
      
      scopeMap = extCtx.getSessionMap();
      if (scopeMap.containsValue(this)) return "session";
       
      scopeMap = extCtx.getApplicationMap();
      if (scopeMap.containsValue(this)) return "application";
       
      return "unknown";
   }
}

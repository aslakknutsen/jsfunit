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

package org.jboss.jsfunit.a4jsupport;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.jboss.jsfunit.facade.DOMUtil;
import org.jboss.jsfunit.framework.JSFUnitFilter;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains static methods similar to those in the RichFaces
 * JSFAJAX javascript library.
 *
 * @author ssilvert
 */
public class JSFAJAX
{
   
   // don't allow a new instance
   private JSFAJAX()
   {
   }
   
   /**
    * Create the web request needed to deal with the AJAX response.  If this
    * returns null then just keep the newResponse as the response.
    */
   static WebRequest processResponse(Document oldDoc, 
                                     WebResponse newResponse,
                                     Map options)
         throws SAXException, ParserConfigurationException, IOException, TransformerException
   {
      String ajaxResponse = newResponse.getHeaderField("Ajax-Response");
      if (!ajaxResponse.equals("true")) 
      {
         //TODO: handle expired message
         //TODO: handle redirect
         //TODO: hanlde reload
         return null;
      }
      
      String[] idsToReplace = null;
      Set affected = (Set)options.get("affected");
      if (affected != null)
      {
         idsToReplace = (String[])affected.toArray(new String[0]);
      }
      
      String idsFromResponse = newResponse.getHeaderField("Ajax-Update-Ids");
      if (idsFromResponse != null)
      {
         idsToReplace = idsFromResponse.split(",");
      }
      
      Element metaTag = DOMUtil.findElementWithAttribValue("name", 
                                                           "Ajax-Update-Ids", 
                                                           newResponse.getDOM().getDocumentElement());
      if (metaTag != null)
      {
         idsToReplace = metaTag.getAttribute("content").split(",");
      }
      
      return updatePage(oldDoc, newResponse.getDOM(), idsToReplace);
   }
   
   
   private static WebRequest updatePage(Document oldDoc, 
                                        Document newDoc,
                                        String[] idsToReplace)
         throws SAXException, ParserConfigurationException, IOException, TransformerException
   {
      oldDoc = DOMUtil.convertToDomLevel2(oldDoc);
      newDoc = DOMUtil.convertToDomLevel2(newDoc);
      for (int i=0; i < idsToReplace.length; i++)
      {
         Element oldElement = DOMUtil.findElementWithID(idsToReplace[i], oldDoc);
         Element newElement = DOMUtil.findElementWithID(idsToReplace[i], newDoc);
         oldElement.getParentNode().replaceChild(oldDoc.importNode(newElement, true), oldElement);
      }
      
      replaceAjaxViewState(oldDoc, newDoc);
      
      addResponseStringToSession(DOMUtil.docToHTMLString(oldDoc));
      
      return makeJSFUnitFilterRequest();
   }
   
   // TODO heed this from JSFAJAX.js
   // "For a portal case, replace content in the current window
   // var namespace = options.parameters['org.ajax4jsf.portlet.NAMESPACE'];"
   private static void replaceAjaxViewState(Document oldDoc, Document newDoc)
   {
      Element idsSpan = DOMUtil.findElementWithID("ajax-view-state", newDoc); // RichFaces 3.1
      if (idsSpan == null) idsSpan = DOMUtil.findElementWithID("ajax-update-ids", newDoc); // old A4J
      if (idsSpan == null) return;
      
      NodeList inputs = oldDoc.getElementsByTagName("input");
      NodeList newInputs = idsSpan.getElementsByTagName("input");
      replaceViewState(inputs, newInputs);
      newInputs = idsSpan.getElementsByTagName("INPUT");
      replaceViewState(inputs, newInputs);
   }
   
   private static void replaceViewState(NodeList inputs, NodeList newInputs)
   {
      if( (newInputs.getLength() == 0) || (inputs.getLength() == 0) ) return;

      for(int i = 0 ; i < newInputs.getLength(); i++)
      {
         Element newInput = (Element)newInputs.item(i);
         String newInputName = newInput.getAttribute("name");
         for(int j = 0 ; j < inputs.getLength(); j++)
         {
            Element input = (Element)inputs.item(j);
            String inputName = input.getAttribute("name");
            if(inputName.equals(newInputName))
            {
               input.setAttribute("value", newInput.getAttribute("value"));
             }
         }
      }
   }
   
   private static void addResponseStringToSession(String responseString)
   {
      getSession().setAttribute(JSFUnitFilter.ALT_RESPONSE, responseString);
   }
   
   private static WebRequest makeJSFUnitFilterRequest() throws MalformedURLException
   {
      String urlString = "/ServletRedirector";
      GetMethodWebRequest request = new GetMethodWebRequest(getWarURL() + urlString);
      request.setParameter("JSESSIONID", getSession().getId());
      return request;
   }
   
   private static HttpSession getSession()
   {
      return (HttpSession)FacesContext.getCurrentInstance()
                                      .getExternalContext()
                                      .getSession(true);  
   }
   
   private static String getWarURL()
   {
      Map appMap = FacesContext.getCurrentInstance()
                               .getExternalContext()
                               .getApplicationMap();
      return (String)appMap.get(WebConversationFactory.WAR_URL);
   }
   
   private static HttpServletRequest getServletRequest()
   {
      return (HttpServletRequest)FacesContext.getCurrentInstance()
                                             .getExternalContext()
                                             .getRequest();
   }
   
}

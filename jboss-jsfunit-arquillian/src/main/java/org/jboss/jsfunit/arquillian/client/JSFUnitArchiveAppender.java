/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsfunit.arquillian.client;

import java.net.URL;

import org.jboss.arquillian.spi.TestEnricher;
import org.jboss.arquillian.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.jsfunit.arquillian.container.JSFUnitCleanupTestTreadFilter;
import org.jboss.jsfunit.arquillian.container.JSFUnitTestEnricher;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;


/**
 * JSFUnitArchiveAppender
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @author Stan Silvert
 * @version $Revision: $
 */
public class JSFUnitArchiveAppender implements AuxiliaryArchiveAppender
{
   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.AuxiliaryArchiveAppender#createAuxiliaryArchive()
    */
   public Archive<?> createAuxiliaryArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "arquillian-jsfunit.jar")
                  .addClass(JSFUnitCleanupTestTreadFilter.class)
                  .addPackages(
                        true, 
                        org.jboss.jsfunit.jsfsession.JSFSession.class.getPackage(),
                        org.jboss.jsfunit.framework.WebClientSpec.class.getPackage(),
                        org.jboss.jsfunit.context.JSFUnitFacesContext.class.getPackage(),
                        org.jboss.jsfunit.seam.SeamUtil.class.getPackage(),
                        org.jboss.jsfunit.api.JSFUnitResource.class.getPackage(), // Arquillian JSFunit API
                        org.jboss.jsfunit.arquillian.container.JSFUnitTestEnricher.class.getPackage(), // Support package for incontainer enrichment 
                        org.apache.http.HttpEntity.class.getPackage(), // HTTPClient
                        org.apache.james.mime4j.MimeException.class.getPackage(), // Apache Mime4j, used by HTTP client
                        com.gargoylesoftware.htmlunit.BrowserVersion.class.getPackage(),
                        org.apache.commons.codec.Decoder.class.getPackage(),
                        org.apache.commons.io.IOUtils.class.getPackage(),
                        org.apache.commons.lang.StringUtils.class.getPackage(),
                        net.sourceforge.htmlunit.corejs.javascript.EvaluatorException.class.getPackage(),
                        org.w3c.css.sac.CSSException.class.getPackage(),
                        com.steadystate.css.dom.CSSOMObject.class.getPackage(),
                        org.cyberneko.html.HTMLComponent.class.getPackage())
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/FF2.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/FF3.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/FF3.6.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/IE6.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/IE7.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/IE8.properties")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/JavaScriptConfiguration.xml")
                  .addAsResource("com/gargoylesoftware/htmlunit/javascript/configuration/JavaScriptConfiguration.xsd")
                  .addAsManifestResource(jsfunitFacesConfigXml(), "faces-config.xml")
                  .addAsManifestResource("arquillian/web-fragment.xml", "web-fragment.xml")
                  .addAsServiceProvider(TestEnricher.class, JSFUnitTestEnricher.class);
   }

   private URL jsfunitFacesConfigXml()
   {
      return org.jboss.jsfunit.context.JSFUnitFacesContext.class.getResource("/META-INF/faces-config.xml");
   }
}

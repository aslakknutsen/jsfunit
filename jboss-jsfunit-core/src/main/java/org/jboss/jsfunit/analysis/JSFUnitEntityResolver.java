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
 * 
 */

package org.jboss.jsfunit.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Dennis Byrne
 */

public class JSFUnitEntityResolver implements EntityResolver {

	private static final Map<String, String> RESOURCES_BY_SYSTEM_ID = new HashMap<String, String>() {{
		put("http://java.sun.com/dtd/web-facesconfig_1_0.dtd", "web-facesconfig_1_0.dtd");
		put("http://java.sun.com/dtd/web-facesconfig_1_1.dtd", "web-facesconfig_1_1.dtd");
		put("http://java.sun.com/xml/ns/javaee/web-facesconfig_1_2.xsd", "web-facesconfig_1_2.xsd");
		put("http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd", "web-jsptaglibrary_1_2.dtd");
		put("http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd", "web-jsptaglibrary_2_0.xsd");
		put("http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd", "web-jsptaglibrary_2_1.xsd");
	}};
	
	public InputSource resolveEntity(String pid, String sid) throws SAXException, IOException {
		
		if( ! RESOURCES_BY_SYSTEM_ID.containsKey(sid))
			return null;
		
		InputStream stream = new DefaultStreamProvider().getInputStream(RESOURCES_BY_SYSTEM_ID.get(sid));
		InputSource inputSource = null;
		
		if(stream != null) {
			
			inputSource = new InputSource(stream);
        	inputSource.setPublicId(pid);
        	inputSource.setSystemId(sid);
        	inputSource.setEncoding("ISO-8859-1");
		}
		
        return inputSource;
	}
	
}

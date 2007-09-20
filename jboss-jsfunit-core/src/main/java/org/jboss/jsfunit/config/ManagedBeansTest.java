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

package org.jboss.jsfunit.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

public class ManagedBeansTest {

	private final Map<String, Document> documentsByPath;
	
	private static final List<String> SCOPES = new ArrayList<String>(){{
		add("none");
		add("request");
		add("session");
		add("application");
		}};
	
	public ManagedBeansTest(Map<String, Document> documentsByPath) {
		this.documentsByPath = documentsByPath;
	}
		
	public void scrutinize() {
		
		Iterator<String> facesConfigPaths = documentsByPath.keySet().iterator();
		Map<String, String> managedBeanNames = new HashMap<String, String>();
		
		for( ; facesConfigPaths.hasNext() ; ) {
			String facesConfigPath = facesConfigPaths.next();
			managedBeans(documentsByPath.get(facesConfigPath), facesConfigPath, managedBeanNames);
		}
		
	}
		
	private void managedBeans(Node node, String facesConfigPath, final Map<String, String> managedBeanNames) {
		
		String nodeName = node.getNodeName();
		NodeList children = node.getChildNodes();
		
		if("managed-bean".equals(nodeName)) { 
			doManagedBean(node, facesConfigPath, children, managedBeanNames);
		}else { 
			for(int i = 0; i < children.getLength(); i++)
				managedBeans(children.item(i), facesConfigPath, managedBeanNames);
		}
	}

	private void doManagedBean(Node parent, String facesConfigPath, 
			NodeList children, final Map<String, String> managedBeanNames) {
		
		String name = null;
		String clazz = null;
		String scope = null;
		List<String> propertyNames = new LinkedList<String>();
		
		for(int i = 0 ; i < children.getLength(); i++) {
			Node child = children.item(i);
			String nodeName = child.getNodeName();
			String text = child.getTextContent();
			if("managed-bean-name".equals(nodeName)) 
				name = text;
			else if("managed-bean-scope".equals(nodeName)) 
				scope = text;
			else if("managed-bean-class".equals(nodeName)) 
				clazz = text;
			else if("managed-property".equals(nodeName))
				doManagedBeanProperty(child, propertyNames, name, facesConfigPath);
		}
		
		if(scope == null || "".equals(scope))
			throw new RuntimeException("could not determine scope of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		if(clazz == null || "".equals(clazz))
			throw new RuntimeException("could not determine class of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		if(name == null || "".equals(name))
			throw new RuntimeException("could not determine name of " + parent.getNodeName() + 
					" in " + facesConfigPath);
		
		if(managedBeanNames.keySet().contains(name))
			throw new RuntimeException("managed bean '" + name + "' in '" + facesConfigPath 
					+ "' is duplicated. Look for a managed bean w/ the same name in '" + managedBeanNames.get(name) + "'");
		
		managedBeanNames.put(name, facesConfigPath);
		
		if ( ! SCOPES.contains(scope) )
			throw new RuntimeException("managed bean '" + name + "' in " 
					+ facesConfigPath + " has an invalid scope '" + scope + "'");
		
		if(( "session".equals(scope) || "application".equals(scope) ) ) {
			Class managedBeanClass = new ClassUtils().loadClass(clazz, "managed-bean-class"); 
			if( ! Serializable.class.isAssignableFrom(managedBeanClass))
				throw new RuntimeException("Managed bean '" + parent.getNodeValue() + "' is in " 
						+ scope + " scope, so it needs to implement " + Serializable.class);
		}
	}

	private void doManagedBeanProperty(Node parent, final List<String> propertyNames, 
			String managedBeanName, String facesConfig) {

		NodeList children = parent.getChildNodes();
		
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if("property-name".equals(child.getNodeName())) {
				String name = child.getTextContent();
				if(propertyNames.contains(name))
					throw new RuntimeException("managed bean '" + managedBeanName 
							+ "' in " + facesConfig + " has a duplicate property named " + name);
				propertyNames.add(name);
			}
		}
	}
	
}

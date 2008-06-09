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

import static junit.framework.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.jsfunit.analysis.util.ClassUtils;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * @author Dennis Byrne
 */

class ManagedBeansImpl {

	private final Map<String, Document> documentsByPath;
	
	private static final List<String> SCOPES = new ArrayList<String>(){{
		add("none");
		add("request");
		add("session");
		add("application");
		}};
	
	public ManagedBeansImpl(Map<String, Document> documentsByPath) {
		this.documentsByPath = documentsByPath;
	}
		
	public void test() {
		
		final Map<String, String> managedBeanNames = new HashMap<String, String>();
		String xpath = "//managed-bean";
		
		for( String facesConfigPath : documentsByPath.keySet() ) {
			NodeList managedBeans = new ParserUtils().query(documentsByPath.get(facesConfigPath), xpath, facesConfigPath);
			for(int i = 0; i < managedBeans.getLength(); i++) {
				Node managedBean = managedBeans.item(i);
				doManagedBean(managedBean, facesConfigPath, managedBeanNames);
			}
		}
	}
		
	private void doManagedBean(Node parent, String facesConfigPath, final Map<String, String> managedBeanNames) {
		
		// should've used jaxb or digestor
		String name = new ParserUtils().querySingle(parent, "./managed-bean-name/text()", facesConfigPath);
		if(name == null || "".equals(name))
			throw new RuntimeException("could not determine name of " + parent.getNodeName() + 
					" in " + facesConfigPath);
		
		String clazz = new ParserUtils().querySingle(parent, "./managed-bean-class/text()", facesConfigPath);
		if(clazz == null || "".equals(clazz))
			throw new RuntimeException("could not determine class of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		
		String scope = new ParserUtils().querySingle(parent, "./managed-bean-scope/text()", facesConfigPath);
		if(scope == null || "".equals(scope))
			throw new RuntimeException("could not determine scope '" + scope + "' of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		
		failIfMapHasDuplicateKeys(parent, facesConfigPath, name);
		
		doManagedBean(facesConfigPath, parent.getChildNodes(), managedBeanNames, name, clazz, scope);
	}

	private void doManagedBean(String facesConfigPath, NodeList children, 
			final Map<String, String> managedBeanNames, String name, String clazz, String scope) {
		
		List<String> propertyNames = new LinkedList<String>();
		
		for(int i = 0 ; i < children.getLength(); i++) {
			Node child = children.item(i);
			if("managed-property".equals(child.getNodeName()))
				doManagedBeanProperty(child, propertyNames, name, facesConfigPath, clazz);
		}
		
		if(managedBeanNames.keySet().contains(name))
			fail("The managed bean '" + name + "' in '" + facesConfigPath 
					+ "' is duplicated. Look for a managed bean w/ the same name in '" + managedBeanNames.get(name) + "'");
		
		managedBeanNames.put(name, facesConfigPath);
		
		if ( ! SCOPES.contains(scope) )
			fail("Managed bean '" + name + "' in " 
					+ facesConfigPath + " has an invalid scope '" + scope + "'");
		
		if(( "session".equals(scope) || "application".equals(scope) ) ) {
			Class managedBeanClass = new ClassUtils().loadClass(clazz, "managed-bean-class"); 
			if( ! Serializable.class.isAssignableFrom(managedBeanClass))
				fail("Managed bean '" + name + "' is in " 
						+ scope + " scope, so it needs to implement " + Serializable.class);
		}
		
	}

	private void failIfMapHasDuplicateKeys(Node managedBean, String facesConfigPath, String name) {
		String query = "./managed-property/map-entries";
		String subQuery = "./map-entry/key";
		NodeList nodeList = new ParserUtils().query(managedBean, query, facesConfigPath);
		
		for(int i = 0; i < nodeList.getLength(); i++) {
			NodeList keys = new ParserUtils().query(nodeList.item(i), subQuery, facesConfigPath);
			Set<String> keyNames = new HashSet<String>();
			for(int j = 0; j < keys.getLength(); j++) {
				Node firstChild = keys.item(j).getFirstChild();
				if(firstChild != null){
					String textContent = firstChild.getTextContent();
					if(keyNames.contains(textContent)) 
						fail("Managed Bean '" + name + "' has a managed Map property w/ a duplicate key '" 
								+ textContent + "'");
					keyNames.add(textContent);
				}
			}
		}
	}

	private void doManagedBeanProperty(Node parent, final List<String> propertyNames, 
			String managedBeanName, String facesConfig, String managedBeanClass) {

		NodeList children = parent.getChildNodes();
		
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if("property-name".equals(child.getNodeName())) {
				String name = child.getTextContent();
				
				Class clazz = new ClassUtils().loadClass(managedBeanClass, "managed-bean-class");
				
				String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
				if( ! hasMethod(setter, clazz))
					fail("The managed bean '" + managedBeanName 
							+ "' has a managed property called '" + name + "', but " 
							+ clazz.getName() + " has no method " + setter + "(?)");
				
				if(propertyNames.contains(name))
					fail("managed bean '" + managedBeanName 
							+ "' in " + facesConfig + " has a duplicate property named " + name);
				propertyNames.add(name);
			}
		}
	}
	
	private boolean hasMethod(String methodName, Class clazz) {
		
		if(clazz == null)
			return false;
		
		Method[] methods = clazz.getMethods();
		
		for(int i = 0; i < methods.length; i++) 
			if(methodName.equals(methods[i].getName())
					& methods[i].getParameterTypes().length == 1)
				return true;
		
		return hasMethod(methodName, clazz.getSuperclass());
	}
}
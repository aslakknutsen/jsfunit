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

package org.jboss.jsfunit.analysis;

import java.util.HashSet;
import java.util.Set;

import org.jboss.jsfunit.analysis.AbstractFacesConfigTestCase;
import org.jboss.jsfunit.analysis.StreamProvider;

import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class ManagedBeanScope_JSFUNIT_25_TestCase extends TestCase {

	public void testDuplicateManagedBean() {
		
		String manageBean = TestUtils.getManagedBean("mirror", Pojo.class, "none");
		String facesConfig = TestUtils.getFacesConfig(manageBean + manageBean);
		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
			
			fail("should have failed");
			
		}catch(Exception e) { }
		
	}

	public void testDuplicateManagedBeansDifferentConfigSource() {

		String manageBean = TestUtils.getManagedBean("mirror", Pojo.class, "none");
		String facesConfig = TestUtils.getFacesConfig(manageBean);
		StreamProvider streamProvider = new StringStreamProvider(facesConfig);

		Set<String> facesConfigPaths = new HashSet<String>() {{
			add("one path");
			add("two paths");
		}};

		try {

			new AbstractFacesConfigTestCase(facesConfigPaths, streamProvider) {}.testManagedBeans();

			fail("should have failed");

		}catch(Exception e) { }		
	}

}
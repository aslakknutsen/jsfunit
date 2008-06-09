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

import org.jboss.jsfunit.analysis.AbstractFacesConfigTestCase;
import org.jboss.jsfunit.analysis.StreamProvider;
import org.jboss.jsfunit.analysis.model.Pojo;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class ManagedBean_JSFUNIT_32_TestCase extends TestCase {
	
	public void testDuplicateProperty() {
		
		String managedProperty = TestUtils.getManagedProperty("setter", "value");
		String manageBean = TestUtils.getManagedBean("bad", Pojo.class, "none", managedProperty);
		String facesConfig = TestUtils.getFacesConfig(manageBean);
		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
			
			throw new RuntimeException("should have failed");
			
		}catch(AssertionFailedError e) { 
			
			String msg = "The managed bean 'bad' has a managed property called 'setter', " +
					"but org.jboss.jsfunit.analysis.model.Pojo has no method setSetter(?)";
			
			assertEquals(msg, e.getMessage());
			
		}
		
	}

}
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

package org.jboss.jsfunit.config;

import java.util.List;
import java.util.Map;

import net.sf.maventaglib.checker.Tld;

import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public abstract class AbstractTldTestCase extends TestCase {

	private Map<String, Tld> tldsByPath;
	
	public AbstractTldTestCase(List<String> tldPaths) {
		
		if(tldPaths == null || tldPaths.size() == 0)
			throw new IllegalArgumentException("tldPaths was null or empty. At least one TLD needed");
		
	}
	
}

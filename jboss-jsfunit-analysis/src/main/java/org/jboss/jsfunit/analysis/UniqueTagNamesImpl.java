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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.fail;
import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.Tld;

/**
 * @author Dennis Byrne
 * @since 1.0
 */

class UniqueTagNamesImpl {

	private Map<String, Tld> tldsByPath = new HashMap<String, Tld>();
	
	public UniqueTagNamesImpl(Map<String, Tld> tldsByPath) {
		this.tldsByPath = tldsByPath;
	}
	
	public void test() {
		
		Map<String, List<String>> tagNamesByTldName = new HashMap<String, List<String>>();
		
		for(String tldPath : tldsByPath.keySet()) {
			Tld tld = tldsByPath.get(tldPath);
			for(Tag tag : tld.getTags()) {
				List<String> tagNames = tagNamesByTldName.get(tld.getName());
				
				if(tagNames == null) {
					tagNames = new LinkedList<String>();
					tagNamesByTldName.put(tld.getName(), tagNames);
				}
				
				if(tagNames.contains(tag.getName()))
					fail("Tag '" + tag.getName() + "' occurs in tag library '"
							+ tld.getName() + "' more than once");
				tagNames.add(tag.getName());
			}
		}
	}
	
}

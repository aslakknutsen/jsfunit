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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;

class UniqueTagAttributesImpl {

	private Collection<Tld> tlds;
	
	public UniqueTagAttributesImpl(Collection<Tld> tlds) {
		this.tlds = tlds;
	}
	
	public void test(){
		
		for(Tld tld : tlds) 
			for(Tag tag : tld.getTags()) 
				scrutinizeAttributes(tld, tag);
	}

	private void scrutinizeAttributes(Tld tld, Tag tag) {

		final List<String> attributeNames = new LinkedList<String>();
		
		for (TagAttribute attribute : tag.getAttributes()) {

			String name = attribute.getAttributeName();
			
			if (name == null || "".equals(name.trim())) {
				throw new RuntimeException(tld.getName() + ":" + tag.getName() 
						+ " has an empty attribute name");
			} else if (attributeNames.contains(name)) {
				throw new RuntimeException(tld.getName() + ":" + tag.getName() 
						+ "@" + name + " is a duplicated attribute.");
			} 
			
			//path + ":" + tag.getName() + "@" + name + " exists, but " + tag.getName() + " has no setter for " + name

			attributeNames.add(name);
		}
	}
	
}

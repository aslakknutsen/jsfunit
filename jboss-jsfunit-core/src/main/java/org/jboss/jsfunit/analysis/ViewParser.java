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

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

public class ViewParser {

	private Map<String, List<String>> actionListeners = new HashMap<String, List<String>>();
	private Map<String, List<String>> actions = new HashMap<String, List<String>>();
	
	public Map<String, List<String>> getActionListeners() {
		return actionListeners;
	}
	
	public Map<String, List<String>> getActions() {
		return actions;
	}

	private void addActionListener(String path, String el) {
		List<String> list = actionListeners.get(path);
		if(list == null) 
			actionListeners.put(path, (list = new LinkedList<String>()));
		list.add(el);
	}
	
	private void addAction(String path, String el) {
		List<String> list = actions.get(path);
		if(list == null) 
			actions.put(path, (list = new LinkedList<String>()));
		list.add(el);
	}
	
	public void parse(Node node, String path) {
		
		NodeList bindings = new ParserUtils().query(node, "//actionListener/@binding", path);
		for(int i = 0; i < bindings.getLength(); i++)
			addActionListener(path, bindings.item(i).getNodeValue());
		
		NodeList actionListeners = new ParserUtils().query(node, "*//@actionListener", path);
		for(int i = 0; i < actionListeners.getLength(); i++)
			addActionListener(path, actionListeners.item(i).getNodeValue());

		NodeList actions = new ParserUtils().query(node, "*//@action", path);
		for(int i = 0; i < actions.getLength(); i++)
			addAction(path, actions.item(i).getNodeValue());
	}

}
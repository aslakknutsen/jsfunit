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

package org.jboss.jsfunit.stub;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;

/**
 * <p>Stub implementation of <code>PortletContext</code>.</p>
 *
 * $Id: PortletContextStub.java 516091 2007-03-08 16:25:17Z greddin $
 */
public class PortletContextStub implements PortletContext {

    // ----------------------------------------------------- Stub Object Methods

    /**
     * <p>Add a context initialization parameter to the set of parameters
     * recognized by this instance.</p>
     *
     * @param name Parameter name
     * @param value Parameter value
     */
    public void addInitParameter(String name, String value) {

        parameters.put(name, value);

    }


    /**
     * <p>Add a new MIME type mapping to the set of mappings recognized by this
     * instance.</p>
     * 
     * @param extension Extension to check for (without the period)
     * @param contentType Corresponding content type
     */
    public void addMimeType(String extension, String contentType) {

        mimeTypes.put(extension, contentType);

    }


    /**
     * <p>Set the document root for <code>getRealPath()</code> resolution.
     * This parameter <strong>MUST</strong> represent a directory.</p>
     *
     * @param documentRoot The new base directory
     */
    public void setDocumentRoot(File documentRoot) {

        this.documentRoot = documentRoot;

    }

    // ------------------------------------------------------ Instance Variables

    private Hashtable attributes = new Hashtable();
    private File documentRoot = null;
    private Hashtable mimeTypes = new Hashtable();
    private Hashtable parameters = new Hashtable();


    // -------------------------------------------------- PortletContext Methods


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return attributes.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return attributes.keys();

    }


    /** {@inheritDoc} */
    public String getInitParameter(String name) {

        return (String) parameters.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getInitParameterNames() {

        return parameters.keys();

    }


    /** {@inheritDoc} */
    public int getMajorVersion() {

        return 1;

    }


    /** {@inheritDoc} */
    public String getMimeType(String path) {

        int period = path.lastIndexOf('.');
        if (period < 0) {
            return null;
        }
        String extension = path.substring(period + 1);
        return (String) mimeTypes.get(extension);

    }


    public int getMinorVersion() {

        // TODO Auto-generated method stub
        return 0;
    }


    public PortletRequestDispatcher getNamedDispatcher(String arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getPortletContextName() {

        return "PortletContextStub";

    }


    /** {@inheritDoc} */
    public String getRealPath(String path) {

        if (documentRoot != null) {
            if (!path.startsWith("/")) {
                throw new IllegalArgumentException("The specified path ('"
                        + path + "') does not start with a '/' character");
            }
            File resolved = new File(documentRoot, path.substring(1));
            try {
                return resolved.getCanonicalPath();
            } catch (IOException e) {
                return resolved.getAbsolutePath();
            }
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public PortletRequestDispatcher getRequestDispatcher(String arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public URL getResource(String path) throws MalformedURLException {

        if (documentRoot != null) {
            if (!path.startsWith("/")) {
                throw new MalformedURLException("The specified path ('" + path
                        + "') does not start with a '/' character");
            }
            File resolved = new File(documentRoot, path.substring(1));
            if (resolved.exists()) {
                return resolved.toURL();
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public InputStream getResourceAsStream(String path) {

        try {
            URL url = getResource(path);
            if (url != null) {
                return url.openStream();
            }
        } catch (Exception e) {
            ;
        }
        return null;

    }


    /** {@inheritDoc} */
    public Set getResourcePaths(String path) {

        if (documentRoot == null) {
            return null;
        }

        // Enforce the leading slash restriction
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The specified path ('" + path
                    + "') does not start with a '/' character");
        }

        // Locate the File node for this path's directory (if it exists)
        File node = new File(documentRoot, path.substring(1));
        if (!node.exists()) {
            return null;
        }
        if (!node.isDirectory()) {
            return null;
        }

        // Construct a Set containing the paths to the contents of this
        // directory
        Set set = new HashSet();
        String[] files = node.list();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            String subfile = path + files[i];
            File subnode = new File(node, files[i]);
            if (subnode.isDirectory()) {
                subfile += "/";
            }
            set.add(subfile);
        }

        // Return the completed set
        return set;

    }


    /** {@inheritDoc} */
    public String getServerInfo() {

        return "PortletContextStub";
    }


    /** {@inheritDoc} */
    public void log(String message) {

        System.out.println(message);

    }


    /** {@inheritDoc} */
    public void log(String message, Throwable exception) {

        System.out.println(message);
        exception.printStackTrace();

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name) {

        if (attributes.containsKey(name)) {
            attributes.remove(name);
        }

    }


    /** {@inheritDoc} */
    public void setAttribute(String name, Object value) {

        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null");
        }
        if (value == null) {
            removeAttribute(name);
            return;
        }
        attributes.put(name, value);

    }

}

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

import java.security.Principal;

/**
 * <p>Stub implementation of <code>Principal</code>.</p>
 * @author Apache Software Foundation
 */
public class PrincipalStub implements Principal {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default Principal instance.</p>
     */
    public PrincipalStub() {
        this(null);
    }


    /**
     * <p>Construct a Principal with the specified name.</p>
     *
     * @param name Name for this Principal
     */
    public PrincipalStub(String name) {
        this.name = name;
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The name for this Principal intance.</p>
     */
    private String name = null;


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Set the name for this Principal.</p>
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }


    // ------------------------------------------------------- Principal Methods


    /** {@inheritDoc} */
    public String getName() {
        return this.name;
    }


}

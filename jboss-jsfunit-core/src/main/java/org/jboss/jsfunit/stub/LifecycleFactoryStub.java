/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.jsfunit.stub;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * <p>Mock implementation of <code>LifecycleFactory</code>.</p>
 *
 * $Id$
 */

public class LifecycleFactoryStub extends LifecycleFactory {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     */
    public LifecycleFactoryStub() {

        lifecycles = new HashMap();
        lifecycles.put(LifecycleFactory.DEFAULT_LIFECYCLE, new LifecycleStub());

    }


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The set of Lifecycle instances registered with us.</p>
     */
    private Map lifecycles = null;


    // ------------------------------------------------ LifecycleFactory Methods


    /** {@inheritDoc} */
    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {

        lifecycles.put(lifecycleId, lifecycle);

    }


    /** {@inheritDoc} */
    public Lifecycle getLifecycle(String lifecycleId) {

        return (Lifecycle) lifecycles.get(lifecycleId);

    }


    /** {@inheritDoc} */
    public Iterator getLifecycleIds() {

        return lifecycles.keySet().iterator();

    }


}

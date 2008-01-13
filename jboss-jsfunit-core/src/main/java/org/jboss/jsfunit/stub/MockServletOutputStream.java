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

import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;

/**
 * <p>Mock implementation of <code>ServletOutputStream</code>.</p>
 *
 * $Id: MockServletOutputStream.java 464373 2006-10-16 04:21:54Z rahul $
 */

public class MockServletOutputStream extends ServletOutputStream {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     *
     * @param stream The stream we will use to buffer output
     */
    public MockServletOutputStream(ByteArrayOutputStream stream) {
        this.baos = stream;
    }


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Return the content that has been written to this output stream.</p>
     */
    public byte[] content() {
        return baos.toByteArray();
    }


    /**
     * <p>Reset this output stream so that it appears no content has been
     * written.</p>
     */
    public void reset() {
        baos.reset();
    }


    /**
     * <p>Return the number of bytes that have been written to this output stream.</p>
     */
    public int size() {
        return baos.size();
    }



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The internal buffer we use to capture output.</p>
     */
    private ByteArrayOutputStream baos = null;


    // --------------------------------------------- ServletOutputStream Methods


    /**
     * <p>Write the specified content to our internal cache.</p>
     *
     * @param content Content to be written
     */
    public void write(int content) {
        baos.write(content);
    }


}

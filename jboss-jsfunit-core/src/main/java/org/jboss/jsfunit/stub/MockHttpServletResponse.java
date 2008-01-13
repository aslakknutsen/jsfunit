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
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Mock implementation of <code>HttpServletResponse</code>.</p>
 *
 * $Id$
 */

public class MockHttpServletResponse implements HttpServletResponse {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     */
    public MockHttpServletResponse() { }


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Retrieve the first value that was set for the specified header,
     * if any.  Otherwise, return <code>null</code>.</p>
     *
     * @param name Header name to look up
     */
    public String getHeader(String name) {
        String match = name + ":";
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                return header.substring(match.length() + 1).trim();
            }
        }
        return null;
    }


    /**
     * <p>Return the text message for the HTTP status that was set.</p>
     */
    public String getMessage() {
        return this.message;
    }


    /**
     * <p>Return the HTTP status code that was set.</p>
     */
    public int getStatus() {
        return this.status;
    }


    /**
     * <p>Set the <code>ServletOutputStream</code> to be returned by a call to
     * <code>getOutputStream()</code>.</p>
     *
     * @param stream The <code>ServletOutputStream</code> instance to use
     *
     * @deprecated Let the <code>getOutputStream()</code> method create and
     *  return an instance of <code>MockServletOutputStream</code> for you
     */
    public void setOutputStream(ServletOutputStream stream) {
        this.stream = stream;
    }


    /**
     * <p>Set the <code>PrintWriter</code> to be returned by a call to
     * <code>getWriter()</code>.</p>
     *
     * @param writer The <code>PrintWriter</code> instance to use
     *
     * @deprecated Let the <code>getWriter()</code> method create and return
     *  an instance of <code>MockPrintWriter</code> for you
     */
    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }


    // ------------------------------------------------------ Instance Variables


    private String encoding = "ISO-8859-1";
    private String contentType = "text/html";
    private List headers = new ArrayList();
    private String message = null;
    private int status = HttpServletResponse.SC_OK;
    private ServletOutputStream stream = null;
    private PrintWriter writer = null;


    // -------------------------------------------- HttpServletResponse Methods


    /** {@inheritDoc} */
    public void addCookie(Cookie cookie) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void addDateHeader(String name, long value) {

        headers.add(name + ": " + formatDate(value));

    }


    /** {@inheritDoc} */
    public void addHeader(String name, String value) {

        headers.add(name + ": " + value);

    }


    /** {@inheritDoc} */
    public void addIntHeader(String name, int value) {

        headers.add(name + ": " + value);

    }


    /** {@inheritDoc} */
    public boolean containsHeader(String name) {

        return getHeader(name) != null;

    }


    /** {@inheritDoc} */
    public String encodeRedirectUrl(String url) {

        return encodeRedirectURL(url);

    }


    /** {@inheritDoc} */
    public String encodeRedirectURL(String url) {

        return url;

    }


    /** {@inheritDoc} */
    public String encodeUrl(String url) {

        return encodeURL(url);

    }


    /** {@inheritDoc} */
    public String encodeURL(String url) {

        return url;

    }


    /** {@inheritDoc} */
    public void sendError(int status) {

        this.status = status;

    }


    /** {@inheritDoc} */
    public void sendError(int status, String message) {

        this.status = status;
        this.message = message;

    }


    /** {@inheritDoc} */
    public void sendRedirect(String location) {

        this.status = HttpServletResponse.SC_MOVED_TEMPORARILY;
        this.message = location;

    }


    /** {@inheritDoc} */
    public void setDateHeader(String name, long value) {

        removeHeader(name);
        addDateHeader(name, value);

    }


    /** {@inheritDoc} */
    public void setHeader(String name, String value) {

        removeHeader(name);
        addHeader(name, value);

    }


    /** {@inheritDoc} */
    public void setIntHeader(String name, int value) {

        removeHeader(name);
        addIntHeader(name, value);

    }


    /** {@inheritDoc} */
    public void setStatus(int status) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void setStatus(int status, String message) {

        throw new UnsupportedOperationException();

    }


    // ------------------------------------------------ ServletResponse Methods


    /** {@inheritDoc} */
    public void flushBuffer() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public int getBufferSize() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getCharacterEncoding() {

        return this.encoding;

    }


    /** {@inheritDoc} */
    public String getContentType() {

        return this.contentType;

    }


    /** {@inheritDoc} */
    public Locale getLocale() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public ServletOutputStream getOutputStream() throws IOException {

        if (stream == null) {
            if (writer != null) {
                throw new IllegalStateException("Cannot call getOutputStream() after getWriter() has been called");
            }
            stream = new MockServletOutputStream(new ByteArrayOutputStream());
        }
        return stream;

    }


    /** {@inheritDoc} */
    public PrintWriter getWriter() throws IOException {

        if (writer == null) {
            if (stream != null) {
                throw new IllegalStateException("Cannot call getWriter() after getOutputStream() was called");
            }
            writer = new MockPrintWriter(new CharArrayWriter());
        }
        return writer;

    }


    /** {@inheritDoc} */
    public boolean isCommitted() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void reset() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void resetBuffer() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void setBufferSize(int size) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void setCharacterEncoding(String charset) {

        this.encoding = charset;

    }


    /** {@inheritDoc} */
    public void setContentLength(int length) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void setContentType(String type) {

        contentType = type;

    }


    /** {@inheritDoc} */
    public void setLocale(Locale locale) {

        throw new UnsupportedOperationException();

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>The date formatting helper we will use in <code>httpTimestamp()</code>.
     * Note that usage of this helper must be synchronized.</p>
     */
    private static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * <p>Return a properly formatted String version of the specified
     * date/time, formatted as required by the HTTP specification.</p>
     *
     * @param date Date/time, expressed as milliseconds since the epoch
     */
    private String formatDate(long date) {
        return format.format(new Date(date));
    }


    /**
     * <p>Remove any header that has been set with the specific name.</p>
     *
     * @param name Header name to look up
     */
    private void removeHeader(String name) {
        String match = name + ":";
        Iterator headers = this.headers.iterator();
        while (headers.hasNext()) {
            String header = (String) headers.next();
            if (header.startsWith(match)) {
                headers.remove();
                return;
            }
        }
    }


}

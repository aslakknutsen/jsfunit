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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


/**
 * <p>Stub implementation of <code>FacesContext</code>.</p>
 *
 * @author Apache Software Foundation
 */

public class FacesContextStub extends FacesContext {


    // ------------------------------------------------------------ Constructors


    public FacesContextStub() {
        super();
        setCurrentInstance(this);
    }


    public FacesContextStub(ExternalContext externalContext) {
        setExternalContext(externalContext);
        setCurrentInstance(this);
    }


    public FacesContextStub(ExternalContext externalContext, Lifecycle lifecycle) {
        this(externalContext);
        this.lifecycle = lifecycle;
    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Set the <code>Application</code> instance for this instance.</p>
     *
     * @param application The new Application
     */
    public void setApplication(Application application) {

        this.application = application;

    }


    /**
     * <p>Set the <code>ExternalContext</code> instance for this instance.</p>
     *
     * @param externalContext The new ExternalContext
     */
    public void setExternalContext(ExternalContext externalContext) {

        this.externalContext = externalContext;

    }


    /**
     * <p>Set the <code>FacesContext</code> instance for this instance.</p>
     *
     * @param facesContext The new FacesContext
     */
    public static void setCurrentInstance(FacesContext facesContext) {

        FacesContext.setCurrentInstance(facesContext);

    }


    // ------------------------------------------------------ Instance Variables


    private Application application = null;
    private ExternalContext externalContext = null;
    private Lifecycle lifecycle = null;
    private Map messages = new HashMap();
    private boolean renderResponse = false;
    private boolean responseComplete = false;
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private UIViewRoot viewRoot = null;


    // ---------------------------------------------------- FacesContext Methods


    /** {@inheritDoc} */
    public Application getApplication() {

        return this.application;

    }


    /** {@inheritDoc} */
    public Iterator getClientIdsWithMessages() {

        return messages.keySet().iterator();

    }


    /** {@inheritDoc} */
    public ExternalContext getExternalContext() {

        return this.externalContext;

    }


    /** {@inheritDoc} */
    public Severity getMaximumSeverity() {

        Severity severity = null;
        Iterator messages = getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            if (severity == null) {
                severity = message.getSeverity();
            } else if (message.getSeverity().getOrdinal() > severity.getOrdinal()) {
                severity = message.getSeverity();
            }
        }
        return severity;

    }


    /** {@inheritDoc} */
    public Iterator getMessages() {

        ArrayList results = new ArrayList();
        Iterator clientIds = messages.keySet().iterator();
        while (clientIds.hasNext()) {
            String clientId = (String) clientIds.next();
            results.addAll((List) messages.get(clientId));
        }
        return results.iterator();

    }


    /** {@inheritDoc} */
    public Iterator getMessages(String clientId) {

        List list = (List) messages.get(clientId);
        if (list == null) {
            list = new ArrayList();
        }
        return list.iterator();

    }


    /** {@inheritDoc} */
    public RenderKit getRenderKit() {

        UIViewRoot vr = getViewRoot();
        if (vr == null) {
            return null;
        }
        String renderKitId = vr.getRenderKitId();
        if (renderKitId == null) {
            return null;
        }
        RenderKitFactory rkFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        return rkFactory.getRenderKit(this, renderKitId);

    }


    /** {@inheritDoc} */
    public boolean getRenderResponse() {

        return this.renderResponse;

    }


    /** {@inheritDoc} */
    public boolean getResponseComplete() {

        return this.responseComplete;

    }


    /** {@inheritDoc} */
    public ResponseStream getResponseStream() {

        return this.responseStream;

    }


    /** {@inheritDoc} */
    public void setResponseStream(ResponseStream responseStream) {

        this.responseStream = responseStream;

    }


    /** {@inheritDoc} */
    public ResponseWriter getResponseWriter() {

        return this.responseWriter;

    }


    /** {@inheritDoc} */
    public void setResponseWriter(ResponseWriter responseWriter) {

        this.responseWriter = responseWriter;

    }


    /** {@inheritDoc} */
    public UIViewRoot getViewRoot() {

        return this.viewRoot;

    }


    /** {@inheritDoc} */
    public void setViewRoot(UIViewRoot viewRoot) {

        this.viewRoot = viewRoot;

    }


    /** {@inheritDoc} */
    public void addMessage(String clientId, FacesMessage message) {

        if (message == null) {
            throw new NullPointerException();
        }
        List list = (List) messages.get(clientId);
        if (list == null) {
            list = new ArrayList();
            messages.put(clientId, list);
        }
        list.add(message);

    }


    /** {@inheritDoc} */
    public void release() {

        application = null;
        externalContext = null;
        messages.clear();
        renderResponse = false;
        responseComplete = false;
        responseStream = null;
        responseWriter = null;
        viewRoot = null;
        setCurrentInstance(null);

    }


    /** {@inheritDoc} */
    public void renderResponse() {

        this.renderResponse = true;

    }


    /** {@inheritDoc} */
    public void responseComplete() {

        this.responseComplete = true;

    }


}

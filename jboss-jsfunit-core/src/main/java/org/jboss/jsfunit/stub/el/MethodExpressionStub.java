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

package org.jboss.jsfunit.stub.el;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.context.FacesContext;

/**
 * <p>Stub implementation of <code>MethodExpression</code>.</p>
 * @author Apache Software Foundation
 */
public class MethodExpressionStub extends MethodExpression {
    

    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5694105394290316715L;


    /**
     * <p>Construct a new expression for the specified expression string.</p>
     *
     * @param expression Expression string to be evaluated
     * @param signature Parameter signature of the method to be called
     * @param expectedType Expected type of the result
     */
    public MethodExpressionStub(String expression, Class[] signature, Class expectedType) {

        if (expression == null) {
            throw new NullPointerException("Expression string cannot be null");
        }
        this.expression = expression;
        this.signature = signature;
        this.expectedType = expectedType;
        parse();

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The parsed elements of this expression.</p>
     */
    private String[] elements = null;


    /**
     * <p>The expected result type for <code>getValue()</code> calls.</p>
     */
    private Class expectedType = null;


    /**
     * <p>The original expression string used to create this expression.</p>
     */
    private String expression = null;


    /**
     * <p>The method signature of the method to be called.</p>
     */
    private Class[] signature = null;


    // ------------------------------------------------------ Expression Methods


    /**
     * <p>Return <code>true</code> if this expression is equal to the
     * specified expression.</p>
     *
     * @param obj Object to be compared
     */
    public boolean equals(Object obj) {

        if ((obj != null) & (obj instanceof MethodExpression)) {
            return expression.equals(((MethodExpression) obj).getExpressionString());
        } else {
            return false;
        }

    }


    /**
     * <p>Return the original String used to create this expression,
     * unmodified.</p>
     */
    public String getExpressionString() {

        return this.expression;

    }


    /**
     * <p>Return the hash code for this expression.</p>
     */
    public int hashCode() {

        return this.expression.hashCode();

    }


    /**
     * <p>Return <code>true</code> if the expression string for this expression
     * contains only literal text.</p>
     */
    public boolean isLiteralText() {

        return (expression.indexOf("${") < 0) && (expression.indexOf("#{") < 0);

    }


    // ------------------------------------------------ MethodExpression Methods


    /**
     * <p>Evaluate the expression relative to the specified context,
     * and return information about the actual implementation method.</p>
     *
     * @param context ELContext for this evaluation
     */
    public MethodInfo getMethodInfo(ELContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return new MethodInfo(elements[elements.length - 1], expectedType, signature);

    }


    /**
     * <p>Evaluate the expression relative to the specified ocntext,
     * and return the result after coercion to the expected result type.</p>
     *
     * @param context ELContext for this evaluation
     * @param params Parameters for this method call
     */
    public Object invoke(ELContext context, Object[] params) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (isLiteralText()) {
            return expression;
        }

        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ELResolver resolver = fcontext.getApplication().getELResolver();
        Object base = null;
        for (int i = 0; i < elements.length - 1; i++) {
            base = resolver.getValue(context, base, elements[i]);
        }

        try {
            Method method = base.getClass().getMethod(elements[elements.length - 1], signature);
            Object result = method.invoke(base, params);
            return fcontext.getApplication().getExpressionFactory().coerceToType(result, expectedType);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ELException(e);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Parse the expression string into its constituent elemetns.</p>
     */
    private void parse() {

        if (isLiteralText()) {
            elements = new String[0];
            return;
        }

        if (expression.startsWith("${") || expression.startsWith("#{")) {
            if (expression.endsWith("}")) {
                String temp = expression.substring(2, expression.length() - 1).replaceAll(" ", "");
                List names = new ArrayList();
                while (temp.length() > 0) {
                    int period= temp.indexOf(".");
                    if (period >= 0) {
                        names.add(temp.substring(0, period));
                        temp = temp.substring(period + 1);
                    } else {
                        names.add(temp);
                        temp = "";
                    }
                }
                elements = (String[]) names.toArray(new String[names.size()]);
            } else {
                throw new IllegalArgumentException(expression);
            }
        } else {
            throw new IllegalArgumentException(expression);
        }

    }


}

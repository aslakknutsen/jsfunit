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

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * <p>Stub implementation of <code>ExpressionFactory</code>.</p>
 *
 * @since 1.0.4
 */
public class ExpressionFactoryStub extends ExpressionFactory {
    

    // ------------------------------------------------------------ Constructors


    /** Creates a new instance of ExpressionFactoryStub */
    public ExpressionFactoryStub() {
    }
    

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Literal numeric value for zero.</p>
     */
    private static final Integer ZERO = new Integer(0);


    // ----------------------------------------------------- Stub Object Methods



    // ----------------------------------------------- ExpressionFactory Methods


    /** {@inheritDoc} */
    public Object coerceToType(Object object, Class targetType) {

        // Check for no conversion necessary
        if ((targetType == null) || Object.class.equals(targetType)) {
            return object;
        }

        // Coerce to String if appropriate
        if (String.class.equals(targetType)) {
            if (object == null) {
                return "";
            } else if (object instanceof String) {
                return (String) object;
            } else {
                return object.toString();
            }
        }

        // Coerce to Number (or a subclass of Number) if appropriate
        if (isNumeric(targetType)) {
            if (object == null) {
                return coerce(ZERO, targetType);
            } else if ("".equals(object)) {
                return coerce(ZERO, targetType);
            } else if (object instanceof String) {
                return coerce((String) object, targetType);
            } else if (isNumeric(object.getClass())) {
                return coerce((Number) object, targetType);
            }
            throw new IllegalArgumentException("Cannot convert " + object + " to Number");
        }

        // Coerce to Boolean if appropriate
        if (Boolean.class.equals(targetType) || (Boolean.TYPE == targetType)) {
            if (object == null) {
                return Boolean.FALSE;
            } else if ("".equals(object)) {
                return Boolean.FALSE;
            } else if ((object instanceof Boolean) || (object.getClass() == Boolean.TYPE)) {
                return (Boolean) object;
            } else if (object instanceof String) {
                return Boolean.valueOf((String) object);
            }
            throw new IllegalArgumentException("Cannot convert " + object + " to Boolean");
        }

        // Coerce to Character if appropriate
        if (Character.class.equals(targetType) || (Character.TYPE == targetType)) {
            if (object == null) {
                return new Character((char) 0);
            } else if ("".equals(object)) {
                return new Character((char) 0);
            } else if (object instanceof String) {
                return new Character(((String) object).charAt(0));
            } else if (isNumeric(object.getClass())) {
                return new Character((char) ((Number) object).shortValue());
            } else if ((object instanceof Character) || (object.getClass() == Character.TYPE)) {
                return (Character) object;
            }
            throw new IllegalArgumentException("Cannot convert " + object + " to Character");
        }

        // Is the specified value type-compatible already?
        if ((object != null) && targetType.isAssignableFrom(object.getClass())) {
            return object;
        }

        // We do not know how to perform this conversion
        throw new IllegalArgumentException("Cannot convert " + object + " to " + targetType.getName());

    }


    /** {@inheritDoc} */
    public MethodExpression createMethodExpression(ELContext context,
                                                   String expression,
                                                   Class expectedType,
                                                   Class[] signature) {

        return new MethodExpressionStub(expression, signature, expectedType);

    }


    /** {@inheritDoc} */
    public ValueExpression createValueExpression(ELContext context,
                                                 String expression,
                                                 Class expectedType) {

        return new ValueExpressionStub(expression, expectedType);

    }


    /** {@inheritDoc} */
    public ValueExpression createValueExpression(Object instance,
                                                 Class expectedType) {

        return new VariableValueExpressionStub(instance, expectedType);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Coerce the specified value to the specified Number subclass.</p>
     *
     * @param value Value to be coerced
     * @param type Destination type
     */
    private Number coerce(Number value, Class type) {

        if ((type == Byte.TYPE) || (type == Byte.class)) {
            return new Byte(value.byteValue());
        } else if ((type == Double.TYPE) || (type == Double.class)) {
            return new Double(value.doubleValue());
        } else if ((type == Float.TYPE) || (type == Float.class)) {
            return new Float(value.floatValue());
        } else if ((type == Integer.TYPE) || (type == Integer.class)) {
            return new Integer(value.intValue());
        } else if ((type == Long.TYPE) || (type == Long.class)) {
            return new Long(value.longValue());
        } else if ((type == Short.TYPE) || (type == Short.class)) {
            return new Short(value.shortValue());
        } else if (type == BigDecimal.class) {
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            } else if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger) value);
            } else {
                return new BigDecimal(((Number) value).doubleValue());
            }
        } else if (type == BigInteger.class) {
            if (value instanceof BigInteger) {
                return (BigInteger) value;
            } else if (value instanceof BigDecimal) {
                return ((BigDecimal) value).toBigInteger();
            } else {
                return BigInteger.valueOf(((Number) value).longValue());
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getName());

    }


    /**
     * <p>Coerce the specified value to the specified Number subclass.</p>
     *
     * @param value Value to be coerced
     * @param type Destination type
     */
    private Number coerce(String value, Class type) {

        if ((type == Byte.TYPE) || (type == Byte.class)) {
            return Byte.valueOf(value);
        } else if ((type == Double.TYPE) || (type == Double.class)) {
            return Double.valueOf(value);
        } else if ((type == Float.TYPE) || (type == Float.class)) {
            return Float.valueOf(value);
        } else if ((type == Integer.TYPE) || (type == Integer.class)) {
            return Integer.valueOf(value);
        } else if ((type == Long.TYPE) || (type == Long.class)) {
            return Long.valueOf(value);
        } else if ((type == Short.TYPE) || (type == Short.class)) {
            return Short.valueOf(value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (type == BigInteger.class) {
            return new BigInteger(value);
        }
        throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getName());

    }


    /**
     * <p>Return <code>true</code> if the specified type is numeric.</p>
     *
     * @param type Type to check
     */
    private boolean isNumeric(Class type) {

        return
               (type == Byte.TYPE) || (type == Byte.class)
            || (type == Double.TYPE) || (type == Double.class)
            || (type == Float.TYPE) || (type == Float.class)
            || (type == Integer.TYPE) || (type == Integer.class)
            || (type == Long.TYPE) || (type == Long.class)
            || (type == Short.TYPE) || (type == Short.class)
            || (type == BigDecimal.class) || (type == BigInteger.class);

    }


}

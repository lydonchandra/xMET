/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.profiles.model;

/**
 * A simple attribute is simply a attribute name / attribute value tuple.
 * @author Nahid Akbar
 */
public final class SimpleAttribute
    extends ElementAttribute {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new simple attribute.
     * @param name the name
     * @param value the value
     * @param namespace the namespace
     * @param required TODO
     */
    public SimpleAttribute(
        final String name,
        final String value,
        final String namespace,
        final boolean required) {
        super(name, value, namespace, required);
        setQualifiedName(name);
        this.setValue(value);
        this.setNamespace(namespace);
    }

    /**
     * Instantiates a new simple attribute.
     * @param parent the parent
     * @param name the name
     * @param value the value
     * @param namespace the namespace
     * @param required TODO
     */
    public SimpleAttribute(
        final Entity parent,
        final String name,
        final String value,
        final String namespace,
        final boolean required) {
        super(parent, name, value, namespace, required);
        setQualifiedName(name);
        this.setValue(value);
        this.setNamespace(namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "@%1$s=%2$s",
            getQualifiedName(),
            getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
        final Object obj) {
        if (obj instanceof ElementAttribute) {
            return getQualifiedName().equals(
                ((ElementAttribute) obj).getQualifiedName())
                && getNamespace().equals(
                    ((ElementAttribute) obj).getNamespace());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int varI = 0;
        if (getValue() != null) {
            varI = getValue().hashCode();
        } else {
            varI = 0;
        }
        return getQualifiedName().hashCode()
            + varI;
    }

}

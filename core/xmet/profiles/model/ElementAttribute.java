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
 * Represents an Attribute in an element.
 * @author Nahid Akbar
 */
public abstract class ElementAttribute
    extends Entity
    implements
    Settable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private String value;

    /** The required. */
    private boolean required;

    /**
     * Instantiates a new element attribute.
     * @param name the name
     * @param aValue the value
     * @param namespace the namespace
     * @param aRequired TODO
     */
    public ElementAttribute(
        final String name,
        final String aValue,
        final String namespace,
        final boolean aRequired) {
        super(null, name, namespace);
        this.setRequired(aRequired);
    }

    /**
     * Instantiates a new element attribute.
     * @param parent the parent
     * @param name the name
     * @param aValue the value
     * @param namespace the namespace
     * @param aRequired TODO
     */
    public ElementAttribute(
        final Entity parent,
        final String name,
        final String aValue,
        final String namespace,
        final boolean aRequired) {
        super(parent, name, namespace);
        this.setRequired(aRequired);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueType() {
        return "string";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String aValue) {
        this.value = aValue;
        if (aValue != null) {
            notifyObservers(this);
        }
    }

    /**
     * Checks if is required.
     * @return true, if is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets the required.
     * @param aRequired the new required
     */
    public void setRequired(
        final boolean aRequired) {
        required = aRequired;
    }

}

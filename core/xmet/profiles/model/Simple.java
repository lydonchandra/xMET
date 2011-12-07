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

import java.util.ArrayList;

/**
 * Represents a simple type end-point.
 * @author Nahid Akbar
 */
public class Simple
    extends Entity
    implements
    Settable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Type == */
    /**
     * Gets the type.
     * @return the type
     */
    public String getType() {
        return getQualifiedName();
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(
        final String type) {
        setQualifiedName(type);
    }

    /* == Value == */

    /** The value. */
    private String value;

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

    /* == Restrictions == */

    /** The restrictions. */
    private ArrayList<Restriction> restrictions = null;

    /**
     * Sets the restrictions.
     * @param group the new restrictions
     */
    public void setRestrictions(
        final ArrayList<Restriction> group) {
        restrictions = group;
    }

    /**
     * Gets the restrictions.
     * @return the restrictions
     */
    public ArrayList<Restriction> getRestrictions() {
        if (restrictions == null) {
            restrictions = new ArrayList<Restriction>();
        }
        return restrictions;
    }

    /* == Constructors == */

    /**
     * Instantiates a new simple.
     */
    public Simple() {
        this(null, null, null);
    }

    /**
     * Instantiates a new simple.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    public Simple(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueType() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "%1$s=\"%2$s\"",
            getQualifiedName(),
            value);
    }

    /* == MultiplicityConstraints == */
    /** The constraints. */
    private MultiplicityConstraints constraints = null;

    /**
     * Gets the constraints.
     * @return the constraints
     */
    public MultiplicityConstraints getConstraints() {
        return constraints;
    }

    /**
     * Sets the constraints.
     * @param muc the new constraints
     */
    public void setConstraints(
        final MultiplicityConstraints muc) {
        constraints = muc;
    }

    /**
     * Checks for constraints.
     * @return true, if successful
     */
    public boolean hasConstraints() {
        return constraints != null;
    }

}

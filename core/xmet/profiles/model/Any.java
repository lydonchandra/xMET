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
 * Any type implements xsd schema wildcards - more or less what Extra is.
 * @author Nahid Akbar
 */
public class Any
    extends Entity
    implements
    Settable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /**
     * The content.
     */
    private String value = null;

    /**
     * Instantiates a new any.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    public Any(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "any data";
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueType() {
        return "any";
    }

    /** The constraints. */
    private MultiplicityConstraints constraints = null;

    /**
     * Checks for constraints.
     * @return true, if successful
     */
    public boolean hasConstraints() {
        return getConstraints() != null;
    }

    /**
     * Gets the constraints.
     * @return the constraints
     */
    public MultiplicityConstraints getConstraints() {
        return constraints;
    }

    /**
     * Sets the constraints.
     * @param aConstraints the new constraints
     */
    public void setConstraints(
        final MultiplicityConstraints aConstraints) {
        this.constraints = aConstraints;
    }

}

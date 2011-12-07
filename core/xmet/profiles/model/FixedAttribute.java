/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.profiles.model;

import n.reporting.Reporting;

/**
 * Attributes that are fixed and can not be set to anything else.
 * @author Nahid Akbar
 */
public class FixedAttribute
    extends ElementAttribute {

    /**
     * attribute value may be fixed but it may not be required.
     */
    private boolean present;

    /**
     * Instantiates a new fixed attribute.
     * @param aParent the parent
     * @param aName the name
     * @param aValue the value
     * @param aNamespace the namespace
     * @param aRequired the required
     */
    public FixedAttribute(
        final Entity aParent,
        final String aName,
        final String aValue,
        final String aNamespace,
        final boolean aRequired) {
        super(aParent, aName, aValue, aNamespace, aRequired);
        super.setValue(aValue);
        present = aRequired;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "@%1$s=%2$s[[fixed]]",
            getQualifiedName(),
            getValue());
    }

    /**
     * {@inheritDoc}
     */
    /*
     * Set the present flag via setting value
     */
    @Override
    public void setValue(
        final String aValue) {
        Reporting.logUnexpected();
        if (aValue != null
            && aValue.trim().length() > 0) {
            present = true;
        } else {
            if (!isRequired()) {
                present = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (present) {
            return super.getValue();
        } else {
            return null;
        }
    }

}

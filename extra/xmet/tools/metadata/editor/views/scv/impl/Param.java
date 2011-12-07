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
package xmet.tools.metadata.editor.views.scv.impl;

import java.io.Serializable;

import n.io.CS;
import n.io.CSC;

/**
 * Represents a parameter to feed into the GUIObject controls Note: for every
 * parameterName or ParameterName there must be a setParameterName method in the
 * corresponding GUIObject.
 * @author Nahid Akbar
 */
@CSC("param")
public class Param
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The name. */
    @CS
    private String name;

    /** The value. */
    @CS
    private String value;

    /**
     * Instantiates a new param.
     */
    public Param() {
        super();
    }

    /**
     * Instantiates a new param.
     * @param aName the name
     * @param aValue the value
     */
    public Param(
        final String aName,
        final String aValue) {
        this.setName(aName);
        this.setValue(aValue);
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Gets the value.
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param aValue the new value
     */
    public void setValue(
        final String aValue) {
        value = aValue;
    }
}

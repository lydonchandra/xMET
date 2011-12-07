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
 * Class contains default value of an item.
 * @author Nahid Akbar
 */
@CSC("default")
public class Default
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The type of value. @see DefaultType */
    @CS
    private DefaultType type = DefaultType.text;

    /** The value. */
    @CS
    private String value;

    /**
     * gets the value after automatically trims the value before returning.
     * @return the valuetrim
     */
    public String getValuetrim() {
        if (getValue() == null) {
            return null;
        }
        setValue(getValue().trim());
        return getValue();
    }

    /**
     * Enumeration of the types of values.
     */
    public static enum DefaultType {

        /** plain text value. */
        text,

        /** value is an keyword that needs to be evaluated. */
        eval
    }

    /**
     * Gets the type of value.
     * @return the type of value
     */
    public DefaultType getType() {
        return type;
    }

    /**
     * Sets the type of value.
     * @param aType the new type of value
     */
    public void setType(
        final DefaultType aType) {
        type = aType;
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

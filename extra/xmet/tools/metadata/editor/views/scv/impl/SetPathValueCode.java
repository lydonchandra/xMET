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

import n.io.CS;
import n.io.CSC;
import xmet.tools.metadata.editor.views.scv.utils.SetableIC;

/**
 * Code sets value at an xpath.
 * @author Nahid Akbar
 */
@CSC("setxpath")
public class SetPathValueCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** path to the model element. */
    @CS
    private String dest;

    /** value to set. */
    @CS
    private String value;

    /** The ic. */
    private transient SetableIC ic;

    /**
     * Gets the path to the model element.
     * @return the path to the model element
     */
    public String getDest() {
        return dest;
    }

    /**
     * Sets the path to the model element.
     * @param aDest the new path to the model element
     */
    public void setDest(
        final String aDest) {
        dest = aDest;
    }

    /**
     * Gets the value to set.
     * @return the value to set
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value to set.
     * @param aValue the new value to set
     */
    public void setValue(
        final String aValue) {
        value = aValue;
    }

    /**
     * Gets the ic.
     * @return the ic
     */
    public SetableIC getIc() {
        return ic;
    }

    /**
     * Sets the ic.
     * @param aIc the new ic
     */
    public void setIc(
        final SetableIC aIc) {
        ic = aIc;
    }

}

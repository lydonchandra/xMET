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

/**
 * This code sets the valid property of a named item.
 * @author Nahid Akbar
 */
@CSC("setValid")
public class SetItemValidCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** name of the item. */
    @CS
    private String name;

    /** value to set. */
    @CS
    private String validationError;

    /**
     * Gets the name of the item.
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     * @param aName the new name of the item
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Gets the value to set.
     * @return the value to set
     */
    public String getValidationError() {
        return validationError;
    }

    /**
     * Sets the value to set.
     * @param aValidationError the new value to set
     */
    public void setValidationError(
        final String aValidationError) {
        validationError = aValidationError;
    }

}

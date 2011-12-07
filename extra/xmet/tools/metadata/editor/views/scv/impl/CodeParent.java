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

import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;

/**
 * Parent interface of all Code Items.
 * @author Nahid Akbar
 */
public interface CodeParent
    extends
    ModelItem {

    /**
     * Gets the iC.
     * @return the iC
     */
    InitializationContext getIC();

    /**
     * Gets the value.
     * @return the value
     */
    String getValue();

    /**
     * Sets the value.
     * @param value the new value
     */
    void setValue(
        String value);

    /**
     * Gets the xpath.
     * @return the xpath
     */
    String getXpath();

    /**
     * Sets the mandatory.
     * @param mandatory the new mandatory
     */
    void setMandatory(
        boolean mandatory);

    /**
     * Sets the visible.
     * @param visible the new visible
     */
    void setVisible(
        boolean visible);

    /**
     * Sets the validation error message.
     * @param validationError the new validation error
     */
    void setValidationError(
        String validationError);
}

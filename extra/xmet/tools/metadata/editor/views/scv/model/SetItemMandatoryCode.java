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
package xmet.tools.metadata.editor.views.scv.model;

import n.io.CS;
import n.io.CSC;

/**
 * This code modifies the mandatory property of items.
 * @author Nahid Akbar
 */
@CSC("makeMandatory")
public class SetItemMandatoryCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The name. */
    @CS
    private String name;

    /** The mandatory. */
    @CS
    private boolean mandatory = true;

    /**
     * Gets the serialversionuid.
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if is the mandatory.
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
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
     * Sets the mandatory.
     * @param aMandatory the new mandatory
     */
    public void setMandatory(
        final boolean aMandatory) {
        mandatory = aMandatory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor aVisitor) {
        aVisitor.visitSetItemMandatoryCode(this);
    }

}

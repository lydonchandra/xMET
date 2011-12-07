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
 * A composite item is like an item which, instead of its control values being
 * mapped to a single element, maps to a repeated set of items.
 * @author Nahid Akbar
 */
@CSC("compositeItem")
public class CompositeItem
    extends Item {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The base xpath of the repeated item. */
    @CS
    private String base;

    /** The relative path (excluding the usual "$" sign). */
    @CS
    private String relative;

    /**
     * Gets the base xpath of the repeated item.
     * @return the base xpath of the repeated item
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the base xpath of the repeated item.
     * @param aBase the new base xpath of the repeated item
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

    /**
     * Gets the relative path (excluding the usual "$" sign).
     * @return the relative path (excluding the usual "$" sign)
     */
    public String getRelative() {
        return relative;
    }

    /**
     * Sets the relative path (excluding the usual "$" sign).
     * @param aRelative the new relative path (excluding the usual "$" sign)
     */
    public void setRelative(
        final String aRelative) {
        relative = aRelative;
    }
}

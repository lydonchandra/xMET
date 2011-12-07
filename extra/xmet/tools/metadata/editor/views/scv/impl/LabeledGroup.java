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
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;

/**
 * Represents an user visible label and its sub group of elements.
 * @author Nahid Akbar
 */
@CSC("labeledGroup")
public class LabeledGroup
    extends Group {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** The base xpath. */
    @CS
    private String label;

    /* == Runtime helper stuff == */
    /**
     * The display context.
     */
    private transient DisplayContext dc;

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return dc;
    }

    /**
     * Gets the base xpath.
     * @return the base xpath
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the base xpath.
     * @param aLabel the new base xpath
     */
    public void setLabel(
        final String aLabel) {
        label = aLabel;
    }

    /**
     * Sets the display context.
     * @param aDc the new display context
     */
    public void setDc(
        final DisplayContext aDc) {
        dc = aDc;
    }

}

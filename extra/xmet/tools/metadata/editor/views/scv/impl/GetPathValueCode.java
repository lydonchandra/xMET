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
 * Code gets the value at specified xpath and sets it to named item.
 * @author Nahid Akbar
 */
@CSC("getxpath")
public class GetPathValueCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** path to the model element. */
    @CS
    private String src;

    /** item name to set value to. */
    @CS
    private String name;

    /** The initialization context. */
    private transient SetableIC ic;

    /**
     * Gets the path to the model element.
     * @return the path to the model element
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the path to the model element.
     * @param aSrc the new path to the model element
     */
    public void setSrc(
        final String aSrc) {
        src = aSrc;
    }

    /**
     * Gets the item name to set value to.
     * @return the item name to set value to
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the item name to set value to.
     * @param aName the new item name to set value to
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Gets the initialization context.
     * @return the initialization context
     */
    public SetableIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context.
     * @param aIc the new initialization context
     */
    public void setIc(
        final SetableIC aIc) {
        ic = aIc;
    }

}

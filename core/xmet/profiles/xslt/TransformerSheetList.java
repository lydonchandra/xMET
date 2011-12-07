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
package xmet.profiles.xslt;

import java.util.ArrayList;
import java.util.List;

import n.io.CSC;
import n.io.CSL;

/**
 * Represents a list of XSL sheets.
 * @author Nahid Akbar
 */
@CSC("XSLTStyleSheetList")
public class TransformerSheetList {

    /** The list. */
    @CSC
    @CSL(listMode = CSL.LIST_LINIENT_MODE)
    private List<TransformerSheet> list = new ArrayList<TransformerSheet>();

    /**
     * Instantiates a new transformer sheet list.
     */
    public TransformerSheetList() {

    }

    /**
     * Gets the sheets.
     * @return the sheets
     */
    public List<TransformerSheet> getSheets() {
        return getList();
    }

    /**
     * Gets the list.
     * @return the list
     */
    public List<TransformerSheet> getList() {
        return list;
    }

    /**
     * Sets the list.
     * @param aList the new list
     */
    public void setList(
        final List<TransformerSheet> aList) {
        list = aList;
    }
}

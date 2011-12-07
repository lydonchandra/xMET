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
import xmet.tools.metadata.editor.views.scv.utils.EntityIC;

/**
 * Represents a choice inside a choices list which is signified by a xpath to
 * test to check whether the choice was set, a label for the use to select this
 * choice, and an item.
 * @author Nahid Akbar
 */
@CSC("choice")
public class ChoiceItem
    implements
    ModelItem {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label of the choice. */
    @CS
    private String label;

    /** The test xpath of the choice. */
    @CS
    private String testXpath;

    /** The item of the choice. */
    @CSC
    private GroupSubitem item;

    /** The initialization context to say this is ready for use. */
    private transient EntityIC ic;

    /**
     * Gets the label of the choice.
     * @return the label of the choice
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the choice.
     * @param aLabel the new label of the choice
     */
    public void setLabel(
        final String aLabel) {
        label = aLabel;
    }

    /**
     * Gets the test xpath of the choice.
     * @return the test xpath of the choice
     */
    public String getTestXpath() {
        return testXpath;
    }

    /**
     * Sets the test xpath of the choice.
     * @param aTestXpath the new test xpath of the choice
     */
    public void setTestXpath(
        final String aTestXpath) {
        testXpath = aTestXpath;
    }

    /**
     * Gets the item of the choice.
     * @return the item of the choice
     */
    public GroupSubitem getItem() {
        return item;
    }

    /**
     * Sets the item of the choice.
     * @param aItem the new item of the choice
     */
    public void setItem(
        final GroupSubitem aItem) {
        item = aItem;
    }

    /**
     * Gets the initialization context to say this is ready for use.
     * @return the initialization context to say this is ready for use
     */
    public EntityIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context to say this is ready for use.
     * @param aIc the new initialization context to say this is ready for use
     */
    public void setIc(
        final EntityIC aIc) {
        ic = aIc;
    }

}

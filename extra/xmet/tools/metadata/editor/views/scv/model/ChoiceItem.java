/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.scv.model;

import n.io.CS;
import n.io.CSC;

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

    /**
     * Gets the item.
     * @return the item
     */
    public GroupSubitem getItem() {
        return item;
    }

    /**
     * Sets the item.
     * @param aItem the new item
     */
    public void setItem(
        final GroupSubitem aItem) {
        this.item = aItem;
    }

    /**
     * Gets the test xpath.
     * @return the test xpath
     */
    public String getTestXpath() {
        return testXpath;
    }

    /**
     * Sets the test xpath.
     * @param aTestXpath the new test xpath
     */
    public void setTestXpath(
        final String aTestXpath) {
        this.testXpath = aTestXpath;
    }

    /**
     * Gets the label.
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * @param aLabel the new label
     */
    public void setLabel(
        final String aLabel) {
        this.label = aLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor aVisitor) {
        if (item != null) {
            item.accept(aVisitor);
        }
    }

}

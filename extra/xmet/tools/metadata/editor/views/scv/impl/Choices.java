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
import n.io.CSL;
import n.utils.TreeChildrenArrayList;
import xmet.tools.metadata.editor.views.scv.utils.ChoiceIC;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;
import xmet.tools.metadata.editor.views.scv.view.ChoiceDC;

/**
 * Represents a situation where an item is substitutable with a set of other
 * items or there is a choice sequence group and the resulting structure in the
 * metadata file (i.e. our model) differs depending on what is set. Represents a
 * list of choices for both editing and also for user to select different
 * options.
 * @author Nahid Akbar
 */
@CSC("choices")
public class Choices
    implements
    GroupSubitem,
    PageSubitem {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label (displayed). */
    @CS
    private String label;

    /** The choice items. */
    @CSC
    @CSL(listClass = TreeChildrenArrayList.class, listMode = CSL.DEFAULT_MODE)
    private TreeChildrenArrayList<ChoiceItem> items =
        new TreeChildrenArrayList<ChoiceItem>();

    /** The initialization context to say this is ready for use. */
    private transient ChoiceIC ic;

    /** The display context to say this is ready for displaying. */
    private transient ChoiceDC dc;

    /**
     * {@inheritDoc}
     */
    @Override
    public ChoiceDC getDC() {
        return dc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationContext getIC() {
        return getIc();
    }

    /**
     * Gets the label (displayed).
     * @return the label (displayed)
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label (displayed).
     * @param aLabel the new label (displayed)
     */
    public void setLabel(
        final String aLabel) {
        label = aLabel;
    }

    /**
     * Gets the choice items.
     * @return the choice items
     */
    public TreeChildrenArrayList<ChoiceItem> getItems() {
        return items;
    }

    /**
     * Sets the choice items.
     * @param aItems the new choice items
     */
    public void setItems(
        final TreeChildrenArrayList<ChoiceItem> aItems) {
        items = aItems;
    }

    /**
     * Gets the initialization context to say this is ready for use.
     * @return the initialization context to say this is ready for use
     */
    public ChoiceIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context to say this is ready for use.
     * @param aIc the new initialization context to say this is ready for use
     */
    public void setIc(
        final ChoiceIC aIc) {
        ic = aIc;
    }

    /**
     * Sets the display context to say this is ready for displaying.
     * @param aDc the new display context to say this is ready for displaying
     */
    public void setDc(
        final ChoiceDC aDc) {
        dc = aDc;
    }
}

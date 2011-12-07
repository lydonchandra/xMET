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
import n.io.CSL;
import n.utils.TreeChildrenArrayList;

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
     * Gets the items.
     * @return the items
     */
    public TreeChildrenArrayList<ChoiceItem> getItems() {
        return items;
    }

    /**
     * Sets the items.
     * @param aItems the new items
     */
    public void setItems(
        final TreeChildrenArrayList<ChoiceItem> aItems) {
        this.items = aItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitChoices(this);
        if (this.items != null) {
            int i = 0;
            for (final ChoiceItem item : items) {
                if (item != null) {
                    visitor.preVisitChoiceItem(
                        item,
                        this,
                        i);
                    item.accept(visitor);
                    visitor.postVisitChoiceItem(
                        item,
                        this,
                        i++);
                }
            }

        }
        visitor.postVisitChoices(this);
    }
}

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
package xmet.tools.metadata.editor.views.scv.utils;

import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Initialization context for choice items - contains the current selection.
 * @author Nahid Akbar
 */
public class ChoiceIC
    extends InitializationContext {

    /** The selected choice item. */
    private int selected = 0;

    /** the choice item this IC belongs to. */
    private final Choices choices;

    /**
     * Gets the selected choice item.
     * @return the selected
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Sets the selected choice item.
     * @param aSelected the new selected
     */
    public void setSelected(
        final int aSelected) {
        this.selected = aSelected;
    }

    /**
     * Instantiates a new choice Initialization context.
     * @param sheet the sheet
     * @param parent the parent
     * @param aChoices the choices
     */
    public ChoiceIC(
        final Sheet sheet,
        final ParentItem parent,
        final Choices aChoices) {
        super(sheet, parent);
        this.choices = aChoices;
    }

    /**
     * This helper method updates the value of selected based on what it sees in
     * the model.
     * @return the int
     */
    public int updateSelected() {
        final int currentSelected = selected;
        for (int i = 0; i < choices.getItems().size(); i++) {
            final ChoiceItem ci = choices.getItems().get(
                i);
            if ((ci.getIc() != null)
                && ci.getIc().isPresent()) {
                /* Reporting.log("updateSelected %1$d", i); */
                selected = i;
                if (selected != currentSelected) {
                    SCVUtils.initializeItem(
                        getSheet(),
                        getParent(),
                        choices.getItems().get(
                            selected).getItem(),
                        choices.getItems().get(
                            currentSelected).getIc().getLastParentPath());
                }
                return selected;

            }
        }
        /* Reporting.log("updateSelected %1$d - ", 0); */
        selected = 0;
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return choices;
    }

}

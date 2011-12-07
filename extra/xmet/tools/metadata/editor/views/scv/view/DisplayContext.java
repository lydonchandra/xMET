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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.Component;

import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;

/**
 * Display Context - all items in display have one and contains the necessary
 * information for displaying that item.
 * @author Nahid Akbar
 */
public abstract class DisplayContext {

    /**
     * Checklist for display context - make sure setModified is set on any
     * modification.
     * @return the display panel
     */

    public abstract Component getDisplayPanel();

    /**
     * Rebuild display panel.
     */
    public abstract void rebuildDisplayPanel();

    /**
     * soft - rebuild.
     */
    public abstract void refreshPanel();

    /* == Validation Implementation == */
    /** The valid. */
    private boolean valid = true;

    /**
     * Checks if is valid.
     * @return true, if is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the valid.
     * @param aValid the new valid
     */
    public void setValid(
        final boolean aValid) {
        this.valid = aValid;
    }

    /** The validation error message. */
    private String validationErrorMessage;

    /**
     * Gets the validation error message.
     * @return the validation error message
     */
    public String getValidationErrorMessage() {
        return validationErrorMessage;
    }

    /**
     * Sets the validation error message.
     * @param aValidationErrorMessage the new validation error message
     */
    public void setValidationErrorMessage(
        final String aValidationErrorMessage) {
        this.validationErrorMessage = aValidationErrorMessage;
    }

    /* == Misc helper methods == */
    /**
     * Rebuild parent.
     * @param ic the ic
     */
    public static void rebuildParent(
        final InitializationContext ic) {
        ParentItem parent = ic.getParent();
        while ((parent != null)
            && (parent.getDC() == null)
            && (parent.getIC() != null)) {
            parent = parent.getIC().getParent();
        }
        if (parent != null) {
            parent.getDC().rebuildDisplayPanel();
        } else {
            ic.getSheet().getDC().rebuildDisplayPanel();
        }
    }

}

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
package xmet.tools.screen;

import n.io.CS;
import n.io.CSC;
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseCustomPSE;
import xmet.tools.ToolSelectionPropertySheetEditor;

/**
 * A SSI whick invokes a tool.
 * @author Nahid Akbar
 */
@CSC("tool")
public class ToolLinkStartScreenItem
    extends NamedStartScreenItem {

    /** The tool name. */
    @PSELabel("Tool:")
    @UseCustomPSE(ToolSelectionPropertySheetEditor.class)
    @CS
    private String toolName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Tool Link: ");
        if (getToolName() == null
            || getToolName().trim().length() == 0) {
            sb.append("Unset");
        } else {
            sb.append(getToolName());
        }
        sb.append(' ');
        return sb.toString()
            + super.toString();
    }

    /**
     * Gets the tool name.
     * @return the tool name
     */
    public String getToolName() {
        return toolName;
    }

    /**
     * Sets the tool name.
     * @param aToolName the new tool name
     */
    public void setToolName(
        final String aToolName) {
        toolName = aToolName;
    }

}

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
import n.ui.patterns.propertySheet.StringPSE;
import n.ui.patterns.propertySheet.UseCustomPSE;

/**
 * A SSI whick links to another SS.
 * @author Nahid Akbar
 */
@CSC("link")
public class StartScreenLinkItem
    extends ToolLinkStartScreenItem {

    {
        setToolName(StartScreenTool.APPLICATION_START);
    }

    /** The screen file name. */
    @PSELabel("Screen File Path")
    @UseCustomPSE(StringPSE.class)
    @CS
    private String screenFileName;

    /**
     * Gets the tool name.
     * @return the tool name
     */
    public String getToolName() {
        return StartScreenTool.APPLICATION_START;
    }

    /**
     * Sets the tool name.
     * @param toolName the new tool name
     */
    public void setToolName(
        final String toolName) {

    }

    /**
     * Gets the screen file name.
     * @return the screen file name
     */
    public String getScreenFileName() {
        return screenFileName;
    }

    /**
     * Sets the screen file name.
     * @param aScreenFileName the new screen file name
     */
    public void setScreenFileName(
        final String aScreenFileName) {
        screenFileName = aScreenFileName;
    }
}

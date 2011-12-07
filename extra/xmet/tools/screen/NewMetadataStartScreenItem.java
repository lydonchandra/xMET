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
import xmet.ui.ProfileSelectionPSE;

/**
 * A SSI which creates a new metadata entry screen.
 * @author Nahid Akbar
 */
@CSC("new")
public class NewMetadataStartScreenItem
    extends ToolLinkStartScreenItem {

    {
        setToolName("metadata.editor");
    }

    /** The profile name. */
    @PSELabel("Profile")
    @UseCustomPSE(ProfileSelectionPSE.class)
    @CS
    private String profileName;

    /** The profile sheet. */
    @PSELabel("Editor Sheet")
    @UseCustomPSE(EditorSheetPSE.class)
    @CS
    private String profileSheet;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("New Metadata Item: ");
        if (getProfileName() == null
            || getProfileName().trim().length() == 0) {
            sb.append("Unset Profile");
        } else {
            sb.append(getProfileName());
            sb.append('\\');
            sb.append(getProfileSheet());
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
        return "metadata.editor";
    }

    /**
     * Sets the tool name.
     * @param toolName the new tool name
     */
    public void setToolName(
        final String toolName) {

    }

    /**
     * Gets the profile name.
     * @return the profile name
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Sets the profile name.
     * @param aProfileName the new profile name
     */
    public void setProfileName(
        final String aProfileName) {
        profileName = aProfileName;
    }

    /**
     * Gets the profile sheet.
     * @return the profile sheet
     */
    public String getProfileSheet() {
        return profileSheet;
    }

    /**
     * Sets the profile sheet.
     * @param aProfileSheet the new profile sheet
     */
    public void setProfileSheet(
        final String aProfileSheet) {
        profileSheet = aProfileSheet;
    }

}

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
package xmet.tools.metadata.manager;

/**
 * Toolbar extension interface for Metadata Management Panel.
 * @author Nahid Akbar
 */
public interface MetadataManagerToolbarExtension {

    /**
     * Gets the icon for button.
     * @return the icon
     */
    String getIconPath();

    /**
     * Gets the label for button.
     * @return the label
     */
    String getLabel();

    /**
     * Checks to see if extension is supported by specified plugin.
     * @param plugin the plugin
     * @return true, if successful
     */
    boolean supportsPlugin(
        MMGMTPlugin plugin);

    /**
     * Callback for button press.
     * @param viewingPanel the viewing panel
     */
    void onCallback(
        MetadataManagementViewingPanel viewingPanel);
}

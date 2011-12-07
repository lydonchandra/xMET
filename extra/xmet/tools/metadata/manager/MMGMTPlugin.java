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
 * The most basic version of the management plugin. All the extensions are
 * bridged. For more info, see interfaces with "MMGMTPlugin" suffix
 * @author Nahid AKbar
 */
public interface MMGMTPlugin {

    /**
     * Return the label of the plugin.
     * @return the name
     */
    String getName();

    /**
     * return the root metadata file node of the plugin.
     * @return the root
     */
    MetadataFile getRoot();

    /**
     * called before closing plugin.
     */
    void onCloseCallback();

    /**
     * Called to get an unique ID that uniquely identifies this plugin.
     * @return the iD
     */
    String getId();
}

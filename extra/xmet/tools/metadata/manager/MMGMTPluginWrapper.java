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

import xmet.ClientContext;

/**
 * This object contains metadata on MMGMTPlugins and methods and information for
 * instantiating MMGMTPlugins.
 * @author Nahid AKbar
 */
public interface MMGMTPluginWrapper {

    /**
     * Return the label of the plugin.
     * @return the name
     */
    String getNameLabel();

    /**
     * Instantiate new plugin - asks the user appropriate questions as
     * necessary.
     * @param client the client
     * @return the mMGMT plugin
     */
    MMGMTPlugin instantiateNewPlugin(
        ClientContext client);

    /**
     * Instantiate from setting.
     * @param settings the settings
     * @param client the client
     * @return the mMGMT plugin
     */
    MMGMTPlugin instantiateFromConfig(
        String settings,
        ClientContext client);

    /**
     * Called to get a string representing the configuration of this plugin for
     * storage so that it may be restored later.
     * @param plugin the plugin
     * @return the settings
     */
    String getPluginConfig(
        MMGMTPlugin plugin);

    /**
     * Called to get an unique ID that uniquely identifies this plugin. Must be
     * the same as the MMGMTPlugin instances
     * @return the iD
     */
    String getID();

    /**
     * flag used to determine the first plugin which never gets closed. Do not
     * override this unless you are writing a replacement for the FileSystem
     * plugin and can take a null string as an iniial paramater
     * @return true if it is the default plugin
     */
    boolean isDefaultPlugin();

}

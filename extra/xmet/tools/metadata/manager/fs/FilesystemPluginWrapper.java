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
package xmet.tools.metadata.manager.fs;

import java.io.File;

import n.ui.JFileChooserUtils;
import xmet.ClientContext;
import xmet.services.ServiceProvider;
import xmet.tools.metadata.manager.MMGMTPlugin;
import xmet.tools.metadata.manager.MMGMTPluginWrapper;

/**
 * Wrapper service provider for the Filesystem plugin.
 * @author Nahid Akbar
 */
public class FilesystemPluginWrapper
    implements
    MMGMTPluginWrapper,
    ServiceProvider<MMGMTPluginWrapper> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNameLabel() {
        return "Local Folder";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MMGMTPlugin instantiateNewPlugin(
        final ClientContext client) {
        final File folder = JFileChooserUtils.getSingleOpenFolder(null);
        if (folder != null) {
            return new FilesystemPlugin(
                client,
                folder.getAbsolutePath());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MMGMTPlugin instantiateFromConfig(
        final String settings,
        final ClientContext client) {
        try {
            return new FilesystemPlugin(
                client,
                settings);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPluginConfig(
        final MMGMTPlugin plugin) {
        return ((FilesystemPlugin) plugin).getSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID() {
        return FilesystemPlugin.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefaultPlugin() {
        return true;
    }

}

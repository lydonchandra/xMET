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
package xmet.tools.metadata.manager.gn;

import n.ui.patterns.propertySheet.ReflectionPSEditorDialog;
import xmet.ClientContext;
import xmet.services.ServiceProvider;
import xmet.tools.metadata.manager.MMGMTPlugin;
import xmet.tools.metadata.manager.MMGMTPluginWrapper;

/**
 * Wrapper service provider for the Geonetwork CSW plugin.
 * @author Nahid Akbar
 */
public class GeonetowrkPluginWrapper
    implements
    MMGMTPluginWrapper,
    ServiceProvider<MMGMTPluginWrapper> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNameLabel() {
        return "Geonetwork CSW";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MMGMTPlugin instantiateNewPlugin(
        final ClientContext client) {
        // CHECKSTYLE OFF: MagicNumber
        GeonetworkDetails gnd = new GeonetworkDetails(
            "admin",
            "admin",
            "http://localhost:8080/geonetwork/");
        gnd =
            (GeonetworkDetails) (new ReflectionPSEditorDialog())
                .showEditorDialog(
                    "Connect to GeoNetwork",
                    gnd,
                    300,
                    200);
        if (gnd != null) {
            return new GeonetworkPlugin(
                client,
                gnd);
        }
        return null;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MMGMTPlugin instantiateFromConfig(
        final String settings,
        final ClientContext client) {
        try {
            return new GeonetworkPlugin(
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
        return ((GeonetworkPlugin) plugin).getSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID() {
        return GeonetworkPlugin.ID;
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
        return false;
    }

}

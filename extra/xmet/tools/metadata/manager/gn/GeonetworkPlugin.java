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

import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.manager.FilterableMMGMTPlugin;
import xmet.tools.metadata.manager.MMGMTPlugin;
import xmet.tools.metadata.manager.MetadataEditableMMGMTPlugin;
import xmet.tools.metadata.manager.MetadataFile;

/**
 * MMGMT Plugin for Geonetwork CSW.
 * @author Nahid Akbar
 */
public class GeonetworkPlugin
    implements
    MMGMTPlugin,
    FilterableMMGMTPlugin,
    MetadataEditableMMGMTPlugin {

    /** The Constant ID. */
    public static final String ID = "gncsw";
    /** The g n node. */
    private Geonetwork gNNode;

    /**
     * Instantiates a new geonetwork plugin.
     * @param client the client
     * @param gnDetails the gn details
     */
    public GeonetworkPlugin(
        final ClientContext client,
        final GeonetworkDetails gnDetails) {
        gNNode = new Geonetwork(
            gnDetails,
            client);
    }

    /**
     * Instantiates a new geonetwork plugin.
     * @param client the client
     * @param setting the setting
     * @throws Exception the exception
     */
    public GeonetworkPlugin(
        final ClientContext client,
        final String setting)
        throws Exception {
        try {
            final GeonetworkDetails gnDetails =
                (GeonetworkDetails) CSXMLSerializationCodec.decodeClassesS(
                    setting,
                    GeonetworkDetails.class);
            gNNode = new Geonetwork(
                gnDetails,
                client);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new Exception(
                e);
        }
    }

    /**
     * Gets the settings.
     * @return the settings
     */
    public String getSettings() {
        return CSXMLSerializationCodec.encode(gNNode.getDetails());
    }

    /* == MMGMTPlugin Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "GeoNetwork: "
            + gNNode.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile getRoot() {
        return gNNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCloseCallback() {
        gNNode.disconnect();
    }

    /* == FilterableMMGMTPlugin Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean filterMetadata(
        final String filterText,
        final boolean caseSensitive) {
        gNNode.search(filterText);
        return (gNNode.getChildren() != null)
            && (gNNode.getChildren().length > 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetMetadataFilter() {
        gNNode.search("");
    }

    /* == MetadataEditableMMGMTPlugin Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getEditableMetadataFile(
        final MetadataFile file) {
        if (file instanceof GeonetworkMetadataFile) {
            return (GeonetworkMetadataFile) file;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getEditableMetadataFile(
        final MetadataFile parent,
        final String fileName) {
        return GeonetworkMetadataFile.getNewFromName(
            gNNode,
            fileName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMetadataFile(
        final MetadataFile metadata) {
        if (metadata instanceof GeonetworkMetadataFile) {
            return ((GeonetworkMetadataFile) metadata).delete();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameMetadataFile(
        final MetadataFile metadata,
        final String newName) {
        Reporting
            .reportUnexpected("This Module Does Not Support File Renaming");
    }

}

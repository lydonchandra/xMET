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

import java.nio.ByteBuffer;

import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;

import xmet.profiles.codecs.DataCodec;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.utils.MetadataMetadataUtils;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.manager.MetadataFile;

/**
 * Metadata record on Geonetwork.
 * @author Nahid Akbar
 */
public class GeonetworkMetadataFile
    implements
    MetadataFile,
    EditableFile {

    /** The g n node. */
    private final Geonetwork gNNode;

    /** The metadata title. */
    private String metadataTitle;

    /** The metadat uuid. */
    private String metadatUUID;

    /** The metadata abstract. */
    private String metadataAbstract;

    /**
     * Instantiates a new geonetwork metadata file.
     * @param geonetNode the geonet node
     * @param title the title
     * @param uuid the uuid
     * @param abstractE the abstract_
     */
    public GeonetworkMetadataFile(
        final Geonetwork geonetNode,
        final String title,
        final String uuid,
        final String abstractE) {
        gNNode = geonetNode;
        metadataTitle = title;
        metadatUUID = uuid;
        metadataAbstract = abstractE;
    }

    /* == MetadataFile implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        if (metadataTitle != null) {
            return metadataTitle;
        } else {
            return metadatUUID;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFolder() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile[] getChildren() {
        return new MetadataFile[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile getParent() {
        return gNNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return metadataTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbstract() {
        return metadataAbstract;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUUID() {
        return metadatUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getPreviewContents(
        final DataCodec codec) {
        return gNNode.getFileContentsRequest(metadatUUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileName() {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEditorSheetName() {

        return null;
    }

    /* == Editable file implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists() {
        return getPreviewContents(null) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContents(
        final ByteBuffer xmlBuffer,
        final ProfileEditorSheet editorSheet,
        final DataCodec codec) {
        final Element root = JDOMXmlUtils.elementFromXml(new String(
            xmlBuffer.array()));
        if (editorSheet != null) {
            metadataTitle = MetadataMetadataUtils.extractTitle(
                root,
                editorSheet.getProfile());
            metadataAbstract = MetadataMetadataUtils.extractAbstract(
                root,
                editorSheet.getProfile());
        }
        if (exists()) {
            gNNode.setFileContentsRequest(
                metadatUUID,
                xmlBuffer);
        } else {
            metadatUUID = MetadataMetadataUtils.extractUUID(
                root,
                editorSheet.getProfile());
            gNNode.newFileRequest(xmlBuffer);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getContents(
        final DataCodec codec) {
        return getPreviewContents(null);
    }

    /* == misc == */
    /**
     * Gets the new from uuid.
     * @param geonetNode the geonet node
     * @param uuid the uuid
     * @param title the title
     * @param abstractT the abstract_
     * @return the new from uuid
     */
    public static GeonetworkMetadataFile getNewFromUUID(
        final Geonetwork geonetNode,
        final String uuid,
        final String title,
        final String abstractT) {
        return new GeonetworkMetadataFile(
            geonetNode,
            title,
            uuid,
            abstractT);
    }

    /**
     * Gets the new from name.
     * @param geonetNode the geonet node
     * @param fileName the file name
     * @return the new from name
     */
    public static GeonetworkMetadataFile getNewFromName(
        final Geonetwork geonetNode,
        final String fileName) {
        return new GeonetworkMetadataFile(
            geonetNode,
            null,
            null,
            null);
    }

    /**
     * Delete.
     * @return true, if successful
     */
    public boolean delete() {
        Reporting.logUnexpected();
        return false;
    }

}

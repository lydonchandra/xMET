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

import java.nio.ByteBuffer;

import xmet.profiles.codecs.DataCodec;

/**
 * The basic MetadataFile interface the MetadataManager works with. Note that
 * the Metadata Editor uses a different interface called EditableFile and deals
 * with a different set of concerns than this interface used by Metadata
 * Manager.
 * @author Nahid Akbar
 */
public interface MetadataFile {

    /* == basic functionalities == */
    /**
     * Gets the file label.
     * @return the name
     */
    String getName();

    /* travasal */
    /**
     * Checks if its a folder node.
     * @return true, if is folder
     */
    boolean isFolder();

    /**
     * Gets the children nodes if it is a folder node.
     * @return the children
     */
    MetadataFile[] getChildren();

    /**
     * Gets the parent folder node.
     * @return the parent
     */
    MetadataFile getParent();

    /* contents */

    /**
     * Gets the contents of file for previewing - in xml.
     * @param codec the data codec to use to decode from file storage format to
     *            xml
     * @return the preview contents
     */
    ByteBuffer getPreviewContents(
        DataCodec codec);

    /* metametadata */

    /**
     * Gets the profile name associated with this metadata file.
     * @return the profile name
     */
    String getProfileName();

    /**
     * Gets the name of the editor sheet last used with this metadata file.
     * @return the editor sheet name
     */
    String getEditorSheetName();

    /**
     * Gets the title of the metadata record.
     * @return the title
     */
    String getTitle();

    /**
     * Gets the abstract section in the metadata record.
     * @return the abstract
     */
    String getAbstract();

    /**
     * Gets the uUID of the metadata record.
     * @return the uUID
     */
    String getUUID();

}

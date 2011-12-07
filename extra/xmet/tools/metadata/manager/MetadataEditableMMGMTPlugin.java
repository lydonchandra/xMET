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

import xmet.tools.metadata.editor.EditableFile;

/**
 * A plugin which allows its metadata to be edited.
 * @author Nahid Akbar
 */
public interface MetadataEditableMMGMTPlugin
    extends
    MMGMTPlugin {

    /**
     * Returns an EditableFile structure (used by metadata editor) for the
     * specified MetadataFile.
     * @param file the file
     * @return the editable metadata file
     */
    EditableFile getEditableMetadataFile(
        MetadataFile file);

    /**
     * Returns an EditableFile structure (used by metadata editor) for the
     * specified MetadataFile parent node and a fileName. This method must
     * create a new file if one does not exists.
     * @param parent the parent
     * @param fileName the file name
     * @return the editable metadata file
     */
    EditableFile getEditableMetadataFile(
        MetadataFile parent,
        String fileName);

    /**
     * Deletes the specified metadata file.
     * @param metadata the metadata
     * @return true, if successful
     */
    boolean deleteMetadataFile(
        MetadataFile metadata);

    /**
     * Rename metadata file.
     * @param metadata the metadata
     * @param newName the new name
     */
    void renameMetadataFile(
        MetadataFile metadata,
        String newName);
}

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
package xmet.tools.metadata.editor;

import xmet.profiles.editorSheet.EditorSheetFilenameExtension;
import xmet.services.ServiceProvider;

/**
 * An implementation of the EditorSheetFilenameExtension interface which is used
 * for listing a list of Editor View configuration file extension names.
 * @author Nahid Akbar
 */
public class EditorSheetFilenameExtensionImpl
    implements
    EditorSheetFilenameExtension,
    ServiceProvider<EditorSheetFilenameExtension> {

    /** The filename extension. */
    private final String filenameExtension;

    /**
     * Instantiates a new editor sheet filename extension impl.
     * @param aFilenameExtension the filename extension
     */
    public EditorSheetFilenameExtensionImpl(
        final String aFilenameExtension) {
        this.filenameExtension = aFilenameExtension;
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
    public String getFilenameExtension() {
        return filenameExtension;
    }

}

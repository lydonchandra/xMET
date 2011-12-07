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

import java.io.File;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

import n.io.bin.Files;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.editorSheet.ProfileEditorSheet;

/**
 * EditableFile wrapper for plain file objects.
 * @author Nahid Akbar
 */
public class LocalEditableFile
    implements
    EditableFile {

    /** The file. */
    private final File file;

    /**
     * Instantiates a new local editable file.
     * @param pFile the file
     */
    public LocalEditableFile(
        final File pFile) {
        this.file = pFile;
        if (file == null) {
            throw new InvalidParameterException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists() {
        return file.exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return file.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContents(
        final ByteBuffer contents,
        final ProfileEditorSheet editorSheet,
        final DataCodec codec) {
        Files.write(
            file,
            codec.encodeFileContents(contents));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getContents(
        final DataCodec codec) {
        return codec.decodeFileContents(Files.read(file));
    }
}

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

import java.nio.ByteBuffer;

import xmet.profiles.codecs.DataCodec;
import xmet.profiles.editorSheet.ProfileEditorSheet;

/**
 * Way of hiding whether we are editing local or remote files. Unit of file
 * Metadata Editor tool works with.
 * @author Nahid Akbar
 */
public interface EditableFile {

    /**
     * returns whether file exists - called when checking if to replace file or
     * not. e.g. for local file it might be as simple as a file.exists
     * call...for a remote portal, there might be duplicate uuid checks
     * @return true, if exists
     */
    boolean exists();

    /**
     * Gets the name. just must return at least "." + file extension e.g. ".xml"
     * for normal metadata files there might be ".mef" or ".xml.gz" or
     * whatever...will depend on the data codecs coded
     * @return the name
     */
    String getName();

    /**
     * Sets the contents it must do a codec.encodeFileFormat(xmlBuffer) before
     * storing.
     * @param xmlBuffer the xml buffer
     * @param editorSheet TODO
     * @param codec the codec
     */
    void setContents(
        ByteBuffer xmlBuffer,
        ProfileEditorSheet editorSheet,
        DataCodec codec);

    /**
     * Gets the contents.
     * @param codec the codec
     * @return the contents
     */
    ByteBuffer getContents(
        DataCodec codec);

}

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
package xmet.profiles.codecs;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import xmet.profiles.ProfileSchema;
import xmet.profiles.model.Entity;

/**
 * A codec for data files.
 * @author Nahid Akbar
 */
public interface DataCodec
    extends
    Codec {

    /**
     * Checks if is file format allowed.
     * @param fileName the file name
     * @return true, if is file format allowed
     */
    boolean isFileFormatAllowed(
        String fileName);

    /**
     * Gets the file format filters list.
     * @return the file format filters list
     */
    List<FileFilter> getFileFormatFiltersList();

    /*
     * part one: encoding and decoding to and from xml and model this should be
     * the re-usable part
     */
    /**
     * Encode model contents to xml.
     * @param profileModelRoot the profile model node
     * @param profileSchemas the profile schemas
     * @return the byte buffer
     */
    ByteBuffer encodeModelContentsToXML(
        Entity profileModelRoot,
        Map<String, ProfileSchema> profileSchemas);

    /**
     * Decode model contents from xml.
     * @param profileModelRoot the profile model node
     * @param dataContents the data contents
     * @throws ContentsCouldNotBeDecodedException the document could not be
     *             loaded exception
     */
    void decodeModelContentsFromXML(
        Entity profileModelRoot,
        ByteBuffer dataContents)
        throws ContentsCouldNotBeDecodedException;

    /*
     * part two: encoding and decoding to and from xml and storage file format
     * this part should vary for different data codecs
     */
    /**
     * Encode file contents.
     * @param xmlContents the xml contents
     * @return the byte buffer
     */
    ByteBuffer encodeFileContents(
        ByteBuffer xmlContents);

    /**
     * Decode file contents.
     * @param dataContents the data contents
     * @return the byte buffer
     */
    ByteBuffer decodeFileContents(
        ByteBuffer dataContents);

}

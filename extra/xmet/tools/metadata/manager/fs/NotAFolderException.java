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

/**
 * This denotes that the specified file is not a folder.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class NotAFolderException
    extends Exception {

    /**
     * Instantiates a new not a folder exception.
     * @param localMetadataDirectory the local metadata directory
     */
    public NotAFolderException(
        final String localMetadataDirectory) {
        super(localMetadataDirectory);
    }

}

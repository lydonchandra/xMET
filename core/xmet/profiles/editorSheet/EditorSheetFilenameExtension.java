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
package xmet.profiles.editorSheet;

/**
 * A filename extension for an editor view. Double as an id for that editor
 * view. These needs to be provided as services by editor editor view plugins.
 * @author Nahid Akbar
 */
public interface EditorSheetFilenameExtension {

    /**
     * Gets the filename extension. e.g. "xml" or "jpg"
     * @return the filename extension
     */
    String getFilenameExtension();
}

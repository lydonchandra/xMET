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

import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * Interface used by Metadata Editor to show file save/load etc dialogs.
 * @author Nahid Akbar
 */
public interface FilesSelectionUtil {

    /**
     * Gets the single selected save file.
     * @param filters the filters
     * @return the single selected save file
     */
    EditableFile getSingleSelectedSaveFile(
        List<FileFilter> filters);

    /**
     * Gets the single selected open file.
     * @return the single selected open file
     */
    EditableFile getSingleSelectedOpenFile();

    /**
     * Gets the file replace confirmation.
     * @param file the file
     * @return the file replace confirmation
     */
    boolean getFileReplaceConfirmation(
        EditableFile file);

    //
    /**
     * Gets the cancellable save confirmation.
     * @param file the file
     * @return -1 for cancel, 1 for yes, 0 for no
     */
    int getCancellableSaveConfirmation(
        EditableFile file);

}

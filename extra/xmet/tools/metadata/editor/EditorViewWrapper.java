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

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.EditorSheetFilenameExtension;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;

/**
 * A wrapper service for Editor View which must be implemented for integrating
 * the Editor View into Metadata Editor.
 * @author Nahid Akbar
 */
public interface EditorViewWrapper
    extends
    EditorSheetFilenameExtension {

    /**
     * Gets the name.
     * @return the name
     */
    String getName();

    /**
     * {@inheritDoc}
     */
    @Override
    String getFilenameExtension();

    /**
     * Instantiate new.
     * @param model the model
     * @param profile the profile
     * @param client the client
     * @param editorSheet the editor sheet
     * @return the editor view
     */
    EditorView instantiateNew(
        final Entity model,
        final Profile profile,
        final ClientContext client,
        final ProfileEditorSheet editorSheet);

    /**
     * Returns true if the specified EV is a Default EV to be used in absence of
     * a configured EV.
     * @return true, if is default
     */
    boolean isDefault();
}

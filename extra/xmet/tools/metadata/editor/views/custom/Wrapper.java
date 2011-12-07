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
package xmet.tools.metadata.editor.views.custom;

import xmet.Client;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;
import xmet.services.ServiceProvider;
import xmet.tools.metadata.editor.EditorSheetFilenameExtensionImpl;
import xmet.tools.metadata.editor.EditorView;
import xmet.tools.metadata.editor.EditorViewWrapper;

/**
 * Wrapper for Custom Editor View.
 * @author Nahid Akbar
 */
public class Wrapper
    implements
    EditorViewWrapper,
    ServiceProvider<EditorViewWrapper> {

    /**
     * Instantiates a new wrapper.
     */
    public Wrapper() {
        Client.getContext().getServices().registerServiceProviders(
            new EditorSheetFilenameExtensionImpl(
                getFilenameExtension()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Custom View";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilenameExtension() {
        return CustomView.FILENAME_EXTENSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditorView instantiateNew(
        final Entity model,
        final Profile profile,
        final ClientContext client,
        final ProfileEditorSheet editorSheet) {
        return new CustomView(
            model,
            profile,
            client,
            editorSheet);
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
    public boolean isDefault() {
        return false;
    }

}

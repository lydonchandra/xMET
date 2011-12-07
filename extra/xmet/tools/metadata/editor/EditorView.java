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
import java.util.Observable;

import javax.swing.JComponent;

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.Entity;

/**
 * Abstract representation of a view plugable into the
 * MetadataEditorToolInstance for editing metadata.
 * @author Nahid Akbar
 */
public abstract class EditorView
    extends Observable {

    /** profile the model belongs to. */
    private Profile profile;

    /** root of the data model. */
    private Entity root;

    /** The client. */
    private ClientContext client;

    /**
     * Gets the client.
     * @return the client
     */
    public ClientContext getClient() {
        return client;
    }

    /**
     * Sets the client.
     * @param aClient the new client
     */
    public void setClient(
        final ClientContext aClient) {
        this.client = aClient;
    }

    /**
     * Instantiates a new editor view.
     * @param model the model
     * @param aProfile the profile
     * @param aClient the client
     */
    public EditorView(
        final Entity model,
        final Profile aProfile,
        final ClientContext aClient) {

        this.profile = aProfile;
        root = model;
        this.client = aClient;
    }

    /**
     * Gets the editor panel.
     * @return the editor panel
     */
    public abstract JComponent getEditorPanel();

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     * @param aProfile the profile to set
     */
    public void setProfile(
        final Profile aProfile) {
        this.profile = aProfile;
    }

    /**
     * Gets the root.
     * @return the root
     */
    public Entity getRoot() {
        return root;
    }

    /**
     * Sets the root.
     * @param aRoot the root to set
     */
    public void setRoot(
        final Entity aRoot) {
        this.root = aRoot;
    }

    /**
     * method will help views to refresh on data model change.
     */
    public void postLoadCallback() {

    }

    /**
     * Pre load callback.
     */
    public void preLoadCallback() {

    }

    /**
     * method will help view to commit data to model before data save.
     */
    public void preSaveCallback() {

    }

    /**
     * method will help view update things like uuids before saving things under
     * different filenames Note: will be followed by a preSaveCallback.
     */
    public void preSaveAsCallback() {

    }

    /**
     * Post save callback.
     */
    public void postSaveCallback() {

    }

    /**
     * Next page callback.
     */
    public void nextPageCallback() {

    }

    /**
     * Previous page callback.
     */
    public void previousPageCallback() {

    }

    /**
     * Checks if is model modified.
     * @return true, if is model modified
     */
    public boolean isModelModified() {
        return true;
    }

    /**
     * Close callback.
     */
    public void closeCallback() {

    }

    /**
     * Post load template callback.
     */
    public void postLoadTemplateCallback() {
        postLoadCallback();
    }

    /**
     * Validate raw metadata.
     * @param metadataContents the bb
     */
    public void validateRawMetadata(
        final ByteBuffer metadataContents) {

    }

}

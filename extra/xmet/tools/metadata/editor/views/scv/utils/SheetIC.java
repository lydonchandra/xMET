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
package xmet.tools.metadata.editor.views.scv.utils;

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Initialization context for the Sheet.
 * @author Nahid Akbar
 */
public class SheetIC
    extends InitializationContext {

    /** The root data model entity. */
    private final Entity root;

    /** The profile. */
    private final Profile profile;

    /** The client. */
    private final ClientContext client;

    /* == Helper getters and setters == */
    /**
     * {@inheritDoc}
     */
    @Override
    public ClientContext getContext() {
        return getClient();
    }

    //
    // /** The named items. */
    /* public Map<String, ManipulableItem> namedItems = new HashMap<String, */
    /* ManipulableItem>(); */

    /**
     * Instantiates a new sheet initialization context.
     * @param aRoot the root
     * @param aProfile the profile
     * @param aClient the client
     * @param sheet the sheet
     */
    public SheetIC(
        final Entity aRoot,
        final Profile aProfile,
        final ClientContext aClient,
        final Sheet sheet) {
        super(sheet, null);
        this.root = aRoot;
        this.profile = aProfile;
        this.client = aClient;
        /* new SheetDebugUtil(sheet); */
    }

    /** The on load executed. */
    private boolean onLoadExecuted = false;

    /**
     * Checks if is on load executed.
     * @return true, if is on load executed
     */
    public boolean isOnLoadExecuted() {
        return onLoadExecuted;
    }

    /**
     * Sets the on load executed.
     * @param aOnLoadExecuted the new on load executed
     */
    public void setOnLoadExecuted(
        final boolean aOnLoadExecuted) {
        this.onLoadExecuted = aOnLoadExecuted;
    }

    /** The modified flag. */
    private boolean modified = true;

    /**
     * Checks if is modified flag is set.
     * @return true, if is modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModified(
        final boolean b) {
        modified = b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return getSheet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reValidate() {
        if (getSheet().getDc() != null) {
            getSheet().getDc().reValidate(
                getSheet());
        }
    }

    /**
     * Gets the client.
     * @return the client
     */
    public ClientContext getClient() {
        return client;
    }

    @Override
    public Entity getRoot() {
        return root;
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

}

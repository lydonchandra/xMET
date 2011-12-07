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
package xmet.tools.screen;

import java.awt.Component;
import java.util.List;
import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PSEExtraParams;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.ProfileEditorSheet;

/**
 * This PSE is for editing the Editor Sheet property of
 * NewMetadataStartScreenItem.
 * @author Nahid Akbar
 */
@PSEExtraParams({
"client",
"item"
})
public class EditorSheetPSE
    extends Observable
    implements
    PropertySheetEditor {

    /* == Parameter client == */
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

    /** The item. */
    private NewMetadataStartScreenItem item;

    /**
     * Gets the item.
     * @return the item
     */
    public NewMetadataStartScreenItem getItem() {
        return item;
    }

    /**
     * Sets the item.
     * @param aItem the new item
     */
    public void setItem(
        final NewMetadataStartScreenItem aItem) {
        this.item = aItem;
    }

    /* == build panel == */
    /** The panel. */
    private JPanel panel;

    /** The selection list. */
    private JComboBox selectionList;

    /**
     * Instantiates a new editor sheet pse.
     */
    public EditorSheetPSE() {
    }

    /** The profile not set. */
    private boolean profileNotSet;

    /* == PSE Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem aItem) {

        profileNotSet = false;
        String[] labels;

        final Profile profile = getClient().getProfiles().getProfileByName(
            this.item.getProfileName());
        if (profile == null) {
            labels = new String[] {
                "Select a profile first"
            };
            profileNotSet = true;
        } else {
            final List<ProfileEditorSheet> options = profile.getEditorSheets();
            // {
            labels = new String[options.size() + 1];
            labels[0] = "";
            for (int i = 0; i < options.size(); i++) {
                labels[i + 1] = options.get(
                    i).getName();
            }
            // }
        }
        selectionList = new JComboBox(
            labels);
        panel = SwingUtils.BorderLayouts.getNew();
        panel.add(selectionList);

        if (!profileNotSet) {
            selectionList.setSelectedItem(value);
            selectionList.setEnabled(true);
        } else {
            selectionList.setSelectedIndex(0);
            selectionList.setEnabled(false);
        }
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        if (profileNotSet) {
            return null;
        } else {
            return selectionList.getSelectedItem();
        }
    }

}

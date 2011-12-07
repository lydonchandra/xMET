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
package xmet.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PSEExtraParams;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.ClientContext;
import xmet.profiles.Profile;

/**
 * A PSE to select a profile.
 * @author Nahid Akbar
 */
@PSEExtraParams({
"client",
"actionListener"
})
public class ProfileSelectionPSE
    implements

    PropertySheetEditor,
    ActionListener {

    /* == Parameter client == */
    /** The client. */
    private ClientContext context;

    /**
     * Gets the client.
     * @return the client
     */
    public ClientContext getClient() {
        return context;
    }

    /**
     * Sets the client.
     * @param aClient the new client
     */
    public void setClient(
        final ClientContext aClient) {
        this.context = aClient;
    }

    /** The action listener. */
    private ActionListener actionListener;

    /**
     * Gets the action listener.
     * @return the action listener
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Sets the action listener.
     * @param aActionListener the new action listener
     */
    public void setActionListener(
        final ActionListener aActionListener) {
        this.actionListener = aActionListener;
    }

    /* == build panel == */
    /** The editor. */
    private JComboBox editor = null;

    /** The panel. */
    private JPanel panel;

    /**
     * Instantiates a new profile selection pse.
     */
    public ProfileSelectionPSE() {

    }

    /* == PSE Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        if (editor == null) {
            editor = new JComboBox();
            panel = SwingUtils.BorderLayouts.getNew();
            panel.add(editor);
            for (final Profile p : getClient().getProfiles().getProfiles()) {
                editor.addItem(p.getName());
            }
        }
        editor.removeActionListener(this);
        editor.setSelectedItem(value);
        editor.addActionListener(this);
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return editor.getSelectedItem();
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent e) {
        if (actionListener != null) {
            actionListener.actionPerformed(e);
        }
    }

}

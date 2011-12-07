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
package xmet.ui.profileHelp;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.patterns.callback.RunnableCallback;
import n.ui.patterns.propertySheet.PSEExtraParams;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.ClientContext;
import xmet.profiles.Profile;

/**
 * Property sheet editor that edits profile help context string.
 * @author Nahid Akbar
 */
@PSEExtraParams({
    "profile"
})
public class ProfileHelpContextSelectionPSE
    implements
    PropertySheetEditor {

    /** The profile. */
    private Profile profile;

    /** The client context. */
    private ClientContext client;

    /** The editor panel. */
    private JPanel panel;

    /** The value field. */
    private JTextField valueField;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        panel = SwingUtils.BorderLayouts.getNew();
        valueField = new JTextField(
            (String) value);
        panel.add(valueField);
        panel.add(
            SwingUtils.BUTTON.getNew(
                "set",
                new RunnableCallback(
                    new Runnable() {

                        @Override
                        public void run() {
                            final ProfileHelpContextSelectionDialog dlg =
                                new ProfileHelpContextSelectionDialog(
                                    getProfile(),
                                    getClient().getResources());
                            final String value = dlg.showSelectionWindow();
                            if (value != null) {
                                valueField.setText(value);
                            }
                        }
                    })),
            BorderLayout.EAST);
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return valueField.getText().trim();
    }

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     * @param aProfile the new profile
     */
    public void setProfile(
        final Profile aProfile) {
        profile = aProfile;
    }

    /**
     * Gets the client context.
     * @return the client context
     */
    public ClientContext getClient() {
        return client;
    }

    /**
     * Sets the client context.
     * @param aClient the new client context
     */
    public void setClient(
        final ClientContext aClient) {
        client = aClient;
    }

}

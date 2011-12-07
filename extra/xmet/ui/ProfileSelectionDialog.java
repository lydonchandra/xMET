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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.profiles.Profile;
import xmet.profiles.ProfileManager;

/**
 * A selection dialog for profiles.
 * @author Nahid Akbar
 */
public class ProfileSelectionDialog
    implements
    ActionListener {

    /** The dialog. */
    private JDialog dialog;
    /**
     * The profile manager.
     */
    private ProfileManager profileManager;

    /**
     * The ok button.
     */
    private JButton okButton;

    /**
     * The cancel button.
     */
    private JButton cancelButton;

    /**
     * The selected profile.
     */
    private Profile selectedProfile = null;

    /**
     * The list.
     */
    private JList list;

    /**
     * Instantiates a new profile selection dialog.
     * @param aProfileManager the profile manager
     */
    public ProfileSelectionDialog(
        final ProfileManager aProfileManager) {
        // CHECKSTYLE OFF: MagicNumber

        this.profileManager = aProfileManager;

        final JPanel buttonsPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        buttonsPanel.add(Box.createHorizontalGlue());
        okButton = new JButton(
            "Ok");
        buttonsPanel.add(okButton);
        okButton.addActionListener(this);
        cancelButton = new JButton(
            "Cancel");
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener(this);

        final JPanel panel = GridBag.getNew();

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Please select a profile"),
            "w=rem;f=h;wx=1;");
        list = new JList();
        final Component component = new JScrollPane(
            list);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(
                final ListSelectionEvent arg0) {
                selectedProfile = (Profile) list.getSelectedValue();
            }
        });

        list.setModel(new ListModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener l) {
            }

            @Override
            public int getSize() {
                return ProfileSelectionDialog.this.profileManager
                    .getProfiles()
                    .size();
            }

            @Override
            public Object getElementAt(
                final int index) {
                return ProfileSelectionDialog.this.profileManager
                    .getProfiles()
                    .get(
                        index);
            }

            @Override
            public void addListDataListener(
                final ListDataListener l) {
            }
        });
        SwingUtils.GridBag.add(
            panel,
            component,
            "w=rem;f=b;wx=1;wy=1;");
        SwingUtils.GridBag.add(
            panel,
            buttonsPanel,
            "w=rem;a=r;wx=0;f=h;");

        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));

        dialog = SwingUtils.DIALOG.createDialog(
            panel,
            "Select a profile...",
            640,
            480,
            true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show selection window.
     */
    public void showSelectionWindow() {
        selectedProfile = null;
        dialog.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == okButton) {
            if (selectedProfile != null) {
                dialog.setVisible(false);
            }
        } else { /* cancel button */
            selectedProfile = null;
            dialog.setVisible(false);
        }
    }

    /**
     * Gets the selected profile.
     * @return the selected profile
     */
    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    /**
     * Gets the selected profile.
     * @param profileManager2 the profile manager2
     * @return the selected profile
     */
    public static Profile getSelectedProfile(
        final ProfileManager profileManager2) {
        final ProfileSelectionDialog psd = new ProfileSelectionDialog(
            profileManager2);
        psd.showSelectionWindow();
        return psd.getSelectedProfile();
    }

}

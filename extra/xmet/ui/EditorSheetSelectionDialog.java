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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import n.ui.ListHelperListModel;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.Profile;
import xmet.profiles.ProfileManager;
import xmet.profiles.editorSheet.ProfileEditorSheet;

/**
 * A selection dialog for choosing between a list of editor sheers.
 * @author Nahid Akbar
 */
public class EditorSheetSelectionDialog {

    /** Maintain this as the currently selected sheet. */
    private ProfileEditorSheet selectedSheet;

    /** The profiles. */
    private ProfileManager profiles;

    /** The profile. */
    private Profile profile;

    /**
     * Instantiates a new editor sheet selection dialog.
     * @param aProfiles the profiles
     */
    public EditorSheetSelectionDialog(
        final ProfileManager aProfiles) {
        this.profiles = aProfiles;
    }

    /**
     * Instantiates a new editor sheet selection dialog.
     * @param aProfile the profile
     */
    public EditorSheetSelectionDialog(
        final Profile aProfile) {
        this.profile = aProfile;
    }

    /** The dialog. */
    private JDialog dialog = null;

    /**
     * Show selection dialog.
     * @param panel the panel
     * @param title the title
     * @param width the width
     * @param height the height
     */
    private void showSelectionDialog(
        final JPanel panel,
        final String title,
        final int width,
        final int height) {
        if (dialog != null) {
            hideDialog();
        }
        dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(
            width,
            height);
        dialog.add(panel);
        SwingUtils.WINDOW.centreWindow(dialog);
        dialog.setVisible(true);
    }

    /**
     * Hide dialog.
     */
    private void hideDialog() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }

    /**
     * Gets the selected sheet.
     * @return the selected sheet
     */
    public ProfileEditorSheet getSelectedSheet() {
        return selectedSheet;
    }

    /**
     * shows selection dialog of the selected profile.
     * @return the profile editor sheet
     */
    public ProfileEditorSheet showProfileSheetSelectionDialog() {
        // CHECKSTYLE OFF: MagicNumber
        selectedSheet = null;
        final JPanel panel = buildSimplePanel(new Profile[] {
            profile
        });
        if (panel != null) {
            showSelectionDialog(
                panel,
                "Please select an editor sheet",
                640,
                480);
        }
        return selectedSheet;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show sheet selection dialog.
     * @return the profile editor sheet
     */
    public ProfileEditorSheet showSheetSelectionDialog() {
        // CHECKSTYLE OFF: MagicNumber
        selectedSheet = null;
        final JPanel panel = buildSimplePanel(profiles.getProfiles().toArray(
            new Profile[0]));
        if (panel != null) {
            showSelectionDialog(
                panel,
                "Please select an editor sheet",
                640,
                480);
        }
        return selectedSheet;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Builds the simple panel.
     * @param aProfiles the profiles
     * @return the j panel
     */
    private JPanel buildSimplePanel(
        final Profile[] aProfiles) {
        // CHECKSTYLE OFF: MagicNumber

        final ArrayList<ProfileEditorSheet> editorSheets =
            new ArrayList<ProfileEditorSheet>();
        for (final Profile aProfile : aProfiles) {
            editorSheets.addAll(aProfile.getEditorSheets());
        }

        if (editorSheets.size() > 0) {
            if (editorSheets.size() == 1) {
                selectedSheet = editorSheets.get(0);
                return null;
            } else {

                final JPanel panel = SwingUtils.BorderLayouts.getNew();

                final JPanel internalPanel = SwingUtils.GridBag.getNew();

                // {
                final JList list = new JList();
                // {
                list.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(
                        final ListSelectionEvent arg0) {
                        selectedSheet =
                            (ProfileEditorSheet) ((JList) arg0.getSource())
                                .getSelectedValue();
                    }
                });

                list.setModel(new ListHelperListModel<ProfileEditorSheet>(
                    editorSheets));
                // }

                final Component listScrollPane = new JScrollPane(
                    list);

                final JLabel selectionLabel = new JLabel(
                    "Please select an editor sheet");

                final JPanel buttonsPanel =
                    SwingUtils.BoxLayouts.getHorizontalPanel();
                // {
                buttonsPanel.add(Box.createHorizontalGlue());
                final Object[] params = {};
                buttonsPanel.add(SwingUtils.BUTTON.getNew(
                    "Select",
                    new ClassMethodCallback(
                        this,
                        "simpleOkButtonCallback",
                        params)));
                final Object[] params1 = {};
                buttonsPanel.add(SwingUtils.BUTTON.getNew(
                    "Cancel",
                    new ClassMethodCallback(
                        this,
                        "simpleCancelButtonCallback",
                        params1)));
                if (aProfiles.length > 1) {
                    final Object[] params2 = {
                        aProfiles
                    };
                    buttonsPanel.add(SwingUtils.BUTTON.getNew(
                        "Advanced",
                        new ClassMethodCallback(
                            this,
                            "simpleAdvancedButtonCallback",
                            params2)));
                }
                // }
                SwingUtils.GridBag.add(
                    internalPanel,
                    selectionLabel,
                    "w=rem;f=h;wx=1;");
                SwingUtils.GridBag.add(
                    internalPanel,
                    listScrollPane,
                    "w=rem;f=b;wx=1;wy=1;");
                SwingUtils.GridBag.add(
                    internalPanel,
                    buttonsPanel,
                    "w=rem;a=r;wx=0;f=h;");

                internalPanel.setBorder(BorderFactory.createEmptyBorder(
                    10,
                    10,
                    10,
                    10));
                // }

                panel.add(internalPanel);

                return panel;
            }
        } else {
            if (editorSheets.size() == 1) {
                selectedSheet = editorSheets.get(0);
            }
            return null;
        }
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Simple ok button callback.
     */
    public void simpleOkButtonCallback() {
        if (selectedSheet != null) {
            dialog.setVisible(false);
        }
    }

    /**
     * Simple cancel button callback.
     */
    public void simpleCancelButtonCallback() {
        selectedSheet = null;
        dialog.setVisible(false);
    }

    /**
     * Simple advanced button callback.
     * @param aProfiles the profiles
     */
    public void simpleAdvancedButtonCallback(
        final Profile[] aProfiles) {
        if (dialog != null) {
            final JPanel panel = buildAdvancedPanel(aProfiles);
            if (panel != null) {
                dialog.setContentPane(panel);
                dialog.validate();
            }
        }
    }

    /** The advanced editor sheers list. */
    private JList advancedEditorSheersList;

    /**
     * Builds the advanced panel.
     * @param aProfiles the profiles
     * @return the j panel
     */
    private JPanel buildAdvancedPanel(
        final Profile[] aProfiles) {
        // CHECKSTYLE OFF: MagicNumber
        final JPanel panel = SwingUtils.BorderLayouts.getNew();

        final JPanel internalPanel = SwingUtils.GridBag.getNew();

        // {
        final JList profilesList = new JList();
        // {
        profilesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(
                final ListSelectionEvent arg0) {
                final Profile aProfile =
                    (Profile) ((JList) arg0.getSource()).getSelectedValue();
                final ArrayList<ProfileEditorSheet> editorSheets =
                    new ArrayList<ProfileEditorSheet>();
                editorSheets.addAll(aProfile.getEditorSheets());
                advancedEditorSheersList
                    .setModel(new ListHelperListModel<ProfileEditorSheet>(
                        editorSheets));
            }
        });
        profilesList.setModel(new ListHelperListModel<Profile>(
            Arrays.asList(aProfiles)));
        selectedSheet = null;
        // }

        advancedEditorSheersList = new JList();
        // {
        advancedEditorSheersList
            .addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(
                    final ListSelectionEvent arg0) {
                    selectedSheet =
                        (ProfileEditorSheet) ((JList) arg0.getSource())
                            .getSelectedValue();
                }
            });
        // }

        final Component profilesListScrollPane = new JScrollPane(
            profilesList);
        final Component editorSheetListScrollPane = new JScrollPane(
            advancedEditorSheersList);

        final JLabel profileSelectionLabel = new JLabel(
            "Please select a profile");
        final JLabel editorSheetSelectionLabel = new JLabel(
            "Please select an editor sheet");

        final JPanel buttonsPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        // {
        buttonsPanel.add(Box.createHorizontalGlue());
        final Object[] params = {};
        buttonsPanel.add(SwingUtils.BUTTON.getNew(
            "Select",
            new ClassMethodCallback(
                this,
                "simpleOkButtonCallback",
                params)));
        final Object[] params1 = {};
        buttonsPanel.add(SwingUtils.BUTTON.getNew(
            "Cancel",
            new ClassMethodCallback(
                this,
                "simpleCancelButtonCallback",
                params1)));
        if (aProfiles.length > 1) {
            final Object[] params2 = {
                aProfiles
            };
            buttonsPanel.add(SwingUtils.BUTTON.getNew(
                "Simple",
                new ClassMethodCallback(
                    this,
                    "advancedSimpleButtonCallback",
                    params2)));
        }
        // }

        SwingUtils.GridBag.add(
            internalPanel,
            profileSelectionLabel,
            "w=rem;f=h;wx=1;");
        SwingUtils.GridBag.add(
            internalPanel,
            profilesListScrollPane,
            "w=rem;f=b;wx=1;wy=1;");
        SwingUtils.GridBag.add(
            internalPanel,
            editorSheetSelectionLabel,
            "w=rem;f=h;wx=1;");
        SwingUtils.GridBag.add(
            internalPanel,
            editorSheetListScrollPane,
            "w=rem;f=b;wx=1;wy=1;");
        SwingUtils.GridBag.add(
            internalPanel,
            buttonsPanel,
            "w=rem;a=r;wx=0;f=h;");

        internalPanel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));
        // }

        panel.add(internalPanel);

        return panel;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Advanced simple button callback.
     * @param aProfiles the profiles
     */
    public void advancedSimpleButtonCallback(
        final Profile[] aProfiles) {
        if (dialog != null) {
            final JPanel panel = buildSimplePanel(aProfiles);
            if (panel != null) {
                dialog.setContentPane(panel);
                dialog.validate();
            }
        }
    }

}

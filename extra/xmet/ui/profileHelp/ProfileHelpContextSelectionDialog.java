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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import n.ui.GenericSingleItemSelectionListener;
import n.ui.SwingUtils;
import xmet.profiles.Profile;
import xmet.resources.ResourceManager;

/**
 * Wrapper dialog for ProfileHelpContextSelectionPane which lets people select
 * help context id given a profile.
 * @author Nahid Akbar
 */
public class ProfileHelpContextSelectionDialog
    implements
    GenericSingleItemSelectionListener,
    ActionListener {

    /** The selection pane. */
    private ProfileHelpContextSelectionPane pchp;

    /** The ok button. */
    private final JButton okButton;

    /** The cancel button. */
    private final JButton cancelButton;

    /** The selected context. */
    private String selectedContext = null;

    /** The dialog. */
    private final JDialog dialog;

    /**
     * Instantiates a new profile help context selection dialog.
     * @param profile the profile
     * @param resources the resources
     */
    public ProfileHelpContextSelectionDialog(
        final Profile profile,
        final ResourceManager resources) {
        // CHECKSTYLE OFF: MagicNumber

        setPchp(new ProfileHelpContextSelectionPane(
            profile,
            resources));

        getPchp().addSelectionListener(
            this);

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

        final JPanel panel = SwingUtils.BorderLayouts.getNew();

        panel.add(
            new JLabel(
                "Please select a help file or section..."),
            BorderLayout.NORTH);
        panel.add(getPchp().getPanel());
        panel.add(
            buttonsPanel,
            BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));

        dialog = SwingUtils.DIALOG.createDialog(
            panel,
            "Select a help file or section...",
            640,
            480,
            true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show selection window.
     * @return the string
     */
    public String showSelectionWindow() {
        dialog.setVisible(true);
        return getSelectedContext();
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == okButton) {
            if (getSelectedContext() != null) {
                dialog.setVisible(false);
            }
        } else { /* cancel button */
            setSelectedContext(null);
            dialog.setVisible(false);
        }

    }

    /*
     * == GenericSingleItemSelectionListener Interface Implementation
     * ================
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionChanged(
        final Object source,
        final Object selectedItem,
        final Object selectedItemIndex) {
        setSelectedContext(getPchp().getSelectedPath());
    }

    /**
     * Gets the selected template.
     * @return the selected template
     */
    public String getSelectedTemplate() {
        return getSelectedContext();
    }

    /**
     * Gets the selection pane.
     * @return the selection pane
     */
    public ProfileHelpContextSelectionPane getPchp() {
        return pchp;
    }

    /**
     * Sets the selection pane.
     * @param aPchp the new selection pane
     */
    public void setPchp(
        final ProfileHelpContextSelectionPane aPchp) {
        pchp = aPchp;
    }

    /**
     * Gets the selected context.
     * @return the selected context
     */
    public String getSelectedContext() {
        return selectedContext;
    }

    /**
     * Sets the selected context.
     * @param aSelectedContext the new selected context
     */
    public void setSelectedContext(
        final String aSelectedContext) {
        selectedContext = aSelectedContext;
    }

}

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
package xmet.ui.controls.contacts;

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
import xmet.ClientContext;
import xmet.profiles.contacts.ContactsInformation;

/**
 * Contacts list selection dialog.
 * @author Nahid Akbar
 */
public class ContactSelectionDialog
    implements
    GenericSingleItemSelectionListener,
    ActionListener {

    /** The cmp. */
    private final ContactsManagementPane cmp;
    /**
     * The ok button.
     */
    private final JButton okButton;

    /**
     * The cancel button.
     */
    private final JButton cancelButton;

    /** The selected contact. */
    private ContactsInformation selectedContact = null;

    /** The dialog. */
    private final JDialog dialog;

    /**
     * Instantiates a new contact selection dialog.
     * @param context the context
     */
    public ContactSelectionDialog(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        cmp = new ContactsManagementPane(
            context);
        cmp.addSelectionListener(this);

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
                "Please select a Contact"),
            BorderLayout.NORTH);
        panel.add(cmp);
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
            "Select a contact...",
            640,
            480,
            true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show selection window.
     * @return the contacts information
     */
    public ContactsInformation showSelectionWindow() {
        dialog.setVisible(true);
        return selectedContact;
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == okButton) {
            if (selectedContact != null) {
                dialog.setVisible(false);
            }
        } else { /* cancel button */
            selectedContact = null;
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
        selectedContact = (ContactsInformation) selectedItem;
    }

    /**
     * Gets the selected contact.
     * @return the selected contact
     */
    public ContactsInformation getSelectedContact() {
        return selectedContact;
    }

}

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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import n.ui.GenericSingleItemSelectionListener;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;

import org.jdesktop.swingx.JXTable;

import xmet.ClientContext;
import xmet.profiles.contacts.AddressBook;
import xmet.profiles.contacts.ContactsInformation;

/**
 * A pane that allows editing of contacts information.
 * @author Nahid Akbar
 */
public class ContactsManagementPane
    extends JPanel
    implements
    TableModel,
    ListSelectionListener {

    /** The Constant COL_EMAIL. */
    private static final int COL_EMAIL = 4;

    /** The Constant COL_PHONE. */
    private static final int COL_PHONE = 3;

    /** The Constant COL_POS_NAME. */
    private static final int COL_POS_NAME = 2;

    /** The Constant COL_ORG_NAME. */
    private static final int COL_ORG_NAME = 1;

    /** The Constant COL_NAME. */
    private static final int COL_NAME = 0;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The contacts. */
    private final AddressBook contacts;

    /** The contacts table. */
    private final JXTable contactsTable;

    /** The edit button. */
    private final JButton editButton;

    /** The delete button. */
    private final JButton deleteButton;

    /** The selection listeners. */
    private final ArrayList<GenericSingleItemSelectionListener> selListeners =
        new ArrayList<GenericSingleItemSelectionListener>();

    /**
     * Instantiates a new contacts management pane.
     * @param context the context
     */
    ContactsManagementPane(
        final ClientContext context) {
        contacts = context.getProfiles().getContacts();
        setLayout(new BorderLayout());
        contactsTable = new JXTable(
            this);
        contactsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsTable.getSelectionModel().addListSelectionListener(
            this);
        final JToolBar editingToolbar = new JToolBar(
            "Contact Management",
            SwingConstants.VERTICAL);
        final Object[] params = {};
        editingToolbar.add(SwingUtils.BUTTON.getNewV(
            "New Contact",
            context.getResources().getImageIconResource(
                "images/cm.icon.newContact.png"),
            new ClassMethodCallback(
                this,
                "newContact",
                params)));
        final Object[] params1 = {};
        editButton = SwingUtils.BUTTON.getNewV(
            "Edit Contact",
            context.getResources().getImageIconResource(
                "images/cm.icon.editContact.png"),
            new ClassMethodCallback(
                this,
                "editContact",
                params1));
        editingToolbar.add(editButton);
        final Object[] params2 = {};
        deleteButton = SwingUtils.BUTTON.getNewV(
            "Delete Contact",
            context.getResources().getImageIconResource(
                "images/cm.icon.deleteContact.png"),
            new ClassMethodCallback(
                this,
                "deleteContact",
                params2));
        editingToolbar.add(deleteButton);
        editingToolbar.setFloatable(false);
        add(new JScrollPane(
            contactsTable));
        add(
            editingToolbar,
            BorderLayout.EAST);
        valueChanged(null);
    }

    /* == TableModel Interface Implementation == */
    /** The columns. */
    private final String[] columns = {
    "Name",
    "Organization",
    "Position",
    "Phone",
    "Email",
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return contacts.getContacts().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(
        final int columnIndex) {
        return columns[columnIndex];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(
        final int columnIndex) {
        return String.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(
        final int rowIndex,
        final int columnIndex) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(
        final int rowIndex,
        final int columnIndex) {
        if ((rowIndex >= 0)
            && (rowIndex < getRowCount())) {
            final ContactsInformation contact = contacts.getContacts().get(
                rowIndex);
            if (contact != null) {
                switch (columnIndex) {
                case COL_NAME:
                    return contact.getIndividualName();
                case COL_ORG_NAME:
                    return contact.getOrganizationName();
                case COL_POS_NAME:
                    return contact.getPositionName();
                case COL_PHONE:
                    return contact.getPhone();
                case COL_EMAIL:
                    return contact.getEmail();
                default:
                    break;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAt(
        final Object aValue,
        final int rowIndex,
        final int columnIndex) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTableModelListener(
        final TableModelListener l) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTableModelListener(
        final TableModelListener l) {

    }

    /* == Buttion Callback Implementations == */
    /**
     * New contact.
     */
    public void newContact() {
        final ContactsInformation contact =
            (new ContactInformationEditorDialog()).showNewDialog(null);
        if ((contact != null)
            && !contact.isEmpty()) {
            contacts.getContacts().add(
                contact);
            contacts.saveContacts();
            contactsTable.setModel(this);
        }
    }

    /**
     * Edits the contact.
     */
    public void editContact() {
        final int rowIndex = contactsTable.getSelectedRow();
        if ((rowIndex >= 0)
            && (rowIndex < getRowCount())) {
            ContactsInformation contact = contacts.getContacts().get(
                rowIndex);
            contact =
                (new ContactInformationEditorDialog())
                    .showEditorDialog(contact);
            contacts.getContacts().set(
                rowIndex,
                contact);
            contacts.saveContacts();
            contactsTable.setModel(this);
        }
    }

    /**
     * Delete contact.
     */
    public void deleteContact() {
        final int rowIndex = contactsTable.getSelectedRow();
        if ((rowIndex >= 0)
            && (rowIndex < getRowCount())) {
            final ContactsInformation contact = contacts.getContacts().get(
                rowIndex);
            if (JOptionPaneUtils.getYesNoConfirmation("Delete Contact?")) {
                contacts.getContacts().remove(
                    contact);
                contacts.saveContacts();
                contactsTable.setModel(this);
            }
        }
    }

    /**
     * Adds the selection listener.
     * @param e the e
     * @return true, if successful
     */
    public boolean addSelectionListener(
        final GenericSingleItemSelectionListener e) {
        return selListeners.add(e);
    }

    /**
     * Removes the selection listener.
     * @param o the o
     * @return true, if successful
     */
    public boolean removeSelectionListener(
        final Object o) {
        return selListeners.remove(o);
    }

    /* == ListSelectionListener Interface Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final ListSelectionEvent e) {
        int rowIndex = contactsTable.getSelectedRow();
        ContactsInformation contact = null;
        if ((rowIndex >= 0)
            && (rowIndex < getRowCount())) {
            contact = contacts.getContacts().get(
                rowIndex);
        } else {
            rowIndex = -1;
        }
        editButton.setEnabled(rowIndex != -1);
        deleteButton.setEnabled(rowIndex != -1);
        for (final GenericSingleItemSelectionListener l : selListeners) {
            l.selectionChanged(
                this,
                contact,
                rowIndex);
        }
    }

}

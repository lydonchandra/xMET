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
package xmet.ui.controls;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.codecs.impl.StringContactsInformationCodec;
import xmet.profiles.contacts.ContactsInformation;
import xmet.ui.controls.contacts.ContactInformationEditorDialog;
import xmet.ui.controls.contacts.ContactSelectionDialog;

/**
 * Interface for editing Contacts information.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class ContactInfoEditor
    extends GUIObject
    implements
    ContactInformationGUIObject {

    /** The contact. */
    private ContactsInformation contact;

    /**
     * Instantiates a new contact info editor.
     */
    public ContactInfoEditor() {
        rebuild();
    }

    /**
     * Rebuild.
     */
    void rebuild() {
        // CHECKSTYLE OFF: MagicNumber
        removeAll();
        setLayout(new BorderLayout());
        final JPanel panel = SwingUtils.GridBag.getNew();

        String name;
        String position;
        String organization;
        if (contact != null) {
            name = contact.getIndividualName();
            position = contact.getPositionName();
            organization = contact.getOrganizationName();
            if (name == null
                || name.trim().length() == 0) {
                name = null;
            } else {
                if (name.length() > 50) {
                    name = name.substring(
                        0,
                        47)
                        + "...";
                }
            }
            if (position == null
                || position.trim().length() == 0) {
                position = null;
            } else {
                if (position.length() > 50) {
                    position = position.substring(
                        0,
                        47)
                        + "...";
                }
            }
            if (organization == null
                || organization.trim().length() == 0) {
                organization = null;
            } else {
                if (organization.length() > 50) {
                    organization = organization.substring(
                        0,
                        47)
                        + "...";
                }
            }
        } else {
            name = null;
            position = null;
            organization = null;
        }

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Name: ",
                SwingConstants.RIGHT),
            "x=0;y=0;f=b;");
        String varString3 = null;
        if (name == null) {
            varString3 = "not set";
        } else {
            varString3 = name;
        }
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                varString3),
            "x=1;y=0;f=b;wx=1;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Position: ",
                SwingConstants.RIGHT),
            "x=0;y=1;f=b;");
        String varString = null;
        if (position == null) {
            varString = "not set";
        } else {
            varString = position;
        }
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                varString),
            "x=1;y=1;f=b;wx=1;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Organization: ",
                SwingConstants.RIGHT),
            "x=0;y=2;f=b;");
        String varString2 = null;
        if (organization == null) {
            varString2 = "not set";
        } else {
            varString2 = organization;
        }
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                varString2),
            "x=1;y=2;f=b;wx=1;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Edit",
                new ClassMethodCallback(
                    this,
                    "editContact",
                    params)),
            "x=3;y=0;f=b;");
        final Object[] params1 = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Save to List",
                new ClassMethodCallback(
                    this,
                    "saveToList",
                    params1)),
            "x=3;y=1;f=b;");
        final Object[] params2 = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Select from List",
                new ClassMethodCallback(
                    this,
                    "selectFromList",
                    params2)),
            "x=3;y=2;f=b;");
        add(panel);
        revalidate();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Edits the contact.
     */
    public void editContact() {
        contact =
            (new ContactInformationEditorDialog()).showEditorDialog(contact);
        rebuild();
        notifyObserversIfChanged();
    }

    /**
     * Save to list.
     */
    public void saveToList() {
        if (contact != null) {
            getContext().getProfiles().getContacts().getContacts().add(
                contact);
            rebuild();
            notifyObserversIfChanged();
        }
    }

    /**
     * Select from list.
     */
    public void selectFromList() {
        final ContactSelectionDialog csd = new ContactSelectionDialog(
            getContext());
        final ContactsInformation newContact = csd.showSelectionWindow();
        if (newContact != null) {
            contact = newContact;
            rebuild();
            notifyObserversIfChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return StringContactsInformationCodec
            .encodeConstactsInformation(getContactInformation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {

        if ((value != null)
            && (value.trim().length() > 0)) {
            setContactInformation(StringContactsInformationCodec
                .decodeContactsInformation(value));
        } else {
            setContactInformation(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContactInformation(
        final ContactsInformation aContact) {
        super.disableNotifications();
        this.contact = aContact;
        rebuild();
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContactsInformation getContactInformation() {
        return contact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent() {
        return super.isPresent()
            && !getValue().equals(
                "<ContactsInformation />");
    }
}

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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PropertySheet;
import n.ui.patterns.propertySheet.ReflectionPropertySheet;
import xmet.profiles.contacts.ContactsInformation;

/**
 * Contacts information editor dialog.
 * @author Nahid Akbar
 */
public class ContactInformationEditorDialog {

    /** The contact. */
    private ContactsInformation contact;

    /** The contact editor. */
    private PropertySheet contactEditor;

    /** The postal address editor. */
    private PropertySheet postalAddressEditor;

    /** The online address editor. */
    private PropertySheet onlineAddressEditor;

    /**
     * Gets the contact.
     * @return the contact
     */
    public ContactsInformation getContact() {
        return contact;
    }

    /**
     * Sets the contact.
     * @param aContact the new contact
     */
    public void setContact(
        final ContactsInformation aContact) {
        this.contact = aContact;
    }

    /**
     * Show editor dialog.
     * @param aContact the contact
     * @return the contacts information
     */
    public ContactsInformation showEditorDialog(
        final ContactsInformation aContact) {
        // CHECKSTYLE OFF: MagicNumber
        if (aContact != null) {
            setContact(aContact);
        } else {
            if (getContact() == null) {
                setContact(new ContactsInformation());
            }
        }
        if (getContact() != null) {
            SwingUtils.DIALOG.wrapContainerAndShow(
                rebuildEditorPanel(),
                "Edit Contact",
                600,
                700,
                true);
            contactEditor.commit();
            postalAddressEditor.commit();
            onlineAddressEditor.commit();
        }
        return getContact();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show add dialog.
     * @param aContact the contact
     * @return the contacts information
     */
    public ContactsInformation showNewDialog(
        final ContactsInformation aContact) {
        // CHECKSTYLE OFF: MagicNumber
        if (aContact != null) {
            setContact(aContact);
        } else {
            if (getContact() == null) {
                setContact(new ContactsInformation());
            }
        }
        if (getContact() != null) {
            SwingUtils.DIALOG.wrapContainerAndShow(
                rebuildEditorPanel(),
                "Add Contact",
                600,
                700,
                true);
            contactEditor.commit();
            postalAddressEditor.commit();
            onlineAddressEditor.commit();
        }
        return getContact();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Rebuild editor panel.
     * @return the j component
     */
    private JComponent rebuildEditorPanel() {
        // CHECKSTYLE OFF: MagicNumber
        final JPanel panel = SwingUtils.GridBag.getNew();
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Contact Person"),
            "w=rem;a=ml;");
        contactEditor = new ReflectionPropertySheet(
            contact);
        SwingUtils.GridBag.add(
            panel,
            contactEditor,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Postal Address"),
            "w=rem;a=ml;");
        if (contact.getPostalAddresses() == null) {
            contact.setPostalAddresses(new ContactsInformation.PostalAddress());
        }
        postalAddressEditor = new ReflectionPropertySheet(
            contact.getPostalAddresses());
        SwingUtils.GridBag.add(
            panel,
            postalAddressEditor,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Online Address"),
            "w=rem;a=ml;");
        if (contact.getOnlineAddresses() == null) {
            contact.setOnlineAddresses(new ContactsInformation.OnlineAddress());
        }
        onlineAddressEditor = new ReflectionPropertySheet(
            contact.getOnlineAddresses());
        SwingUtils.GridBag.add(
            panel,
            onlineAddressEditor,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            panel,
            Box.createGlue(),
            "w=rem;f=v;wy=1;");
        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));
        return new JScrollPane(
            panel);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * The main method.
     * @param args the arguments
     */
    public static void main(
        final String[] args) {
        final ContactsInformation ci =
            (new ContactInformationEditorDialog()).showEditorDialog(null);
        Reporting.logExpected(
            "%1$s",
            CSXMLSerializationCodec.encode(ci));
    }

}

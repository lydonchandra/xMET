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

package xmet.profiles.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import n.io.CSC;
import n.io.CSL;
import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;

/**
 * Represents an address book and contains a list of contacts.
 * @author Nahid Akbar
 */
@CSC("AddressBook")
public class AddressBook {

    /** The contacts of the address book. */
    @CSL(listClass = ArrayList.class)
    @CSC
    private List<ContactsInformation> contacts =
        new ArrayList<ContactsInformation>();

    /**
     * This method loads contents from preferences.
     * @return the address book
     */
    public static AddressBook loadContacts() {
        final Preferences prefs = Preferences.userRoot().node(
            "/xmet");
        final String data = prefs.get(
            "contacts",
            "");
        AddressBook book;
        if ((data == null)
            || (data.trim().length() == 0)) {
            book = new AddressBook();
            saveContacts(book);
        } else {
            book = (AddressBook) CSXMLSerializationCodec.decode(data);
            if (book == null) {
                book = new AddressBook();
            }
        }
        return book;
    }

    /**
     * This method saves contacts to preferences.
     * @param book the book
     */
    public static void saveContacts(
        final AddressBook book) {
        try {
            final String encoded = CSXMLSerializationCodec.encode(book);
            final Preferences prefs = Preferences.userRoot().node(
                "/xmet");
            prefs.put(
                "contacts",
                encoded);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /**
     * Gets the contacts in this address book instances.
     * @return the contacts
     */
    public List<ContactsInformation> getContacts() {
        return contacts;
    }

    /**
     * sets the contacts in this address book instances.
     * @param aContacts the new contacts
     */
    public void setContacts(
        final List<ContactsInformation> aContacts) {
        this.contacts = aContacts;
    }

    /**
     * saves contacts.
     */
    public void saveContacts() {
        saveContacts(this);
    }
}

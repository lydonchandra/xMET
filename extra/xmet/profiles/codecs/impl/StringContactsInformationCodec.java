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
package xmet.profiles.codecs.impl;

import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;
import xmet.profiles.codecs.ContactsInformationCodec;
import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;

/**
 * This Contacts Codec is for converting contacts info to and from a single
 * string value.
 * @author Nahid Akbar
 */
public class StringContactsInformationCodec
    implements
    ContactsInformationCodec,
    DefaultCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public ContactsInformation extractContactInformation(
        final Entity profileModelBode) {

        final Settable setable = ModelUtils.getSetable(profileModelBode);
        if (setable != null) {
            final String encoded = setable.getValue();
            if (encoded != null
                && encoded.trim().length() > 0) {
                return decodeContactsInformation(encoded.trim());
            } else {
                return new ContactsInformation();
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertContactInformation(
        final ContactsInformation contactsInfo,
        final Entity profileModelNode) {
        try {
            final Settable setable = ModelUtils.getSetable(profileModelNode);
            if (setable != null) {
                final String encoded = encodeConstactsInformation(contactsInfo);
                setable.setValue(encoded);
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(
                e,
                "error while decoding address");
        }
    }

    /**
     * Decode contacts information.
     * @param encodedString the encoded string
     * @return the contacts information
     */
    public static ContactsInformation decodeContactsInformation(
        final String encodedString) {
        try {
            final CSXMLSerializationCodec xc = new CSXMLSerializationCodec();
            xc.includeClasses(
                ContactsInformation.class,
                ContactsInformation.OnlineAddress.class,
                ContactsInformation.PostalAddress.class);
            xc.setPrintClasses(false);
            return (ContactsInformation) xc.decodeObject(encodedString);
        } catch (final Exception e) {
            Reporting.reportUnexpected(
                e,
                "error while decoding address");
        }
        return null;
    }

    /**
     * Encode constacts information.
     * @param contactsInfo the contacts info
     * @return the string
     */
    public static String encodeConstactsInformation(
        final ContactsInformation contactsInfo) {
        final CSXMLSerializationCodec xc = new CSXMLSerializationCodec();
        xc.includeClasses(
            ContactsInformation.class,
            ContactsInformation.OnlineAddress.class,
            ContactsInformation.PostalAddress.class);
        xc.setPrintClasses(false);
        final String encoded = xc.encodeObject(contactsInfo);
        if ((encoded != null)
            && encoded.equals("<ContactsInformation />")) {
            return "";
        } else if (encoded == null) {
            return "";
        } else {
            return encoded;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "string";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}

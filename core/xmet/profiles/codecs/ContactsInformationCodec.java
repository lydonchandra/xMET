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
package xmet.profiles.codecs;

import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.model.Entity;

/**
 * Contacts information Codec for decoding and encoding contacts information
 * from and to profile model.
 * @author Nahid Akbar
 */
public interface ContactsInformationCodec
    extends
    Codec {

    /**
     * Extracts contact info from supplied Model Node.
     * @param profileModelNode the profile model node
     * @return the contacts information
     */
    ContactsInformation extractContactInformation(
        Entity profileModelNode);

    /**
     * Encode contact info into the supplied model node.
     * @param contactsInfo the contacts info
     * @param profileModelNode the profile model node
     */
    void insertContactInformation(
        ContactsInformation contactsInfo,
        Entity profileModelNode);

    /**
     * Gets the supported node names. Preferably the name of an element
     * declaration. This is for codecs to maintain a list of names they support.
     * Supply multiple names by splitting names via the unix pipe '|'
     * @return the supported node names
     */
    String getSupportedNodeNames();
}

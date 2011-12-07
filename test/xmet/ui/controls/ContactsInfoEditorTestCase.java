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

import java.math.BigInteger;
import java.security.SecureRandom;

import n.math.MathUtils;
import xmet.profiles.codecs.impl.StringContactsInformationCodec;
import xmet.profiles.contacts.ContactsInformation;

/**
 * Test case for control.
 * @author Nahid Akbar
 */
public class ContactsInfoEditorTestCase
    extends AbstractGUIObjectTestCase {

    /**
     * Instantiates a new contacts info editor test case.
     */
    public ContactsInfoEditorTestCase() {
        super(ContactInfoEditor.class);
    }

    /** The random. */
    private static SecureRandom random = new SecureRandom();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExampleValue() {
        // CHECKSTYLE OFF: MagicNumber
        final ContactsInformation ci = new ContactsInformation();
        ci.setIndividualName((new BigInteger(
            MathUtils.getRandomInt(
                0,
                1024),
            random).toString(32)));
        ci.setOrganizationName((new BigInteger(
            MathUtils.getRandomInt(
                0,
                1024),
            random).toString(32)));
        ci.setPositionName(((new BigInteger(
            MathUtils.getRandomInt(
                0,
                1024),
            random).toString(32))));
        return StringContactsInformationCodec.encodeConstactsInformation(ci);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getChangeValueMethods() {
        return new String[] {
            "Enter \"asdf\" in individual name|"
                + "<ContactsInformation><individualName>"
                + "asdf</individualName></ContactsInformation>"
        };
    }
}

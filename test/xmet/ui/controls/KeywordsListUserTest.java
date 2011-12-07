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

import java.awt.EventQueue;

import n.ui.SwingUtils;
import xmet.Client;
import xmet.ClientContext;
import xmet.profiles.Profile;

/**
 * The Class KeywordsListUserTest.
 */
public final class KeywordsListUserTest {

    /**
     * Instantiates a new keywords list user test.
     */
    private KeywordsListUserTest() {

    }

    /**
     * The main method.
     * @param args the arguments
     */
    public static void main(
        final String[] args) {
        Client.preInitialize(args);
        Client.postInitialize();
        EventQueue.invokeLater(new Runnable() {

            // CHECKSTYLE OFF: MagicNumber
            @Override
            public void run() {
                final ClientContext context = Client.getContext();
                final KeywordsListEditor editor = new KeywordsListEditor(
                    context);
                final Profile anzlic =
                    context.getProfiles().getProfileByKeyword(
                        "anzlic");
                editor.setCodeList(anzlic.getCodelistByURL(
                    "http://asdd.ga.gov.au/"
                        + "asdd/profileinfo/"
                        + "anzlic-jurisdic.xml#"
                        + "anzlic-jurisdic",
                    context));
                editor.setCodeList(anzlic.getCodelistByURL(
                    "http://asdd.ga.gov.au/"
                        + "asdd/"
                        + "profileinfo/"
                        + "anzlic-theme.xml#"
                        + "anzlic-theme",
                    context));
                editor.setCodeList(anzlic.getCodelistByURL(
                    "http://asdd.ga.gov.au/"
                        + "asdd/"
                        + "profileinfo/"
                        + "osdm-schedule.xml#"
                        + "osdm-schedule",
                    context));
                SwingUtils.COMPONENT.show(
                    editor,
                    800,
                    600,
                    true);

            }
            // CHECKSTYLE ON: MagicNumber
        });
    }
}

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
package xmet.tools.substanceImpl;

import xmet.Client;

/**
 * User testing class for About Dialog.
 * @author Nahid Akbar
 */
public final class AboutDialogUserTest {

    /**
     * Instantiates a new about dialog user test.
     */
    private AboutDialogUserTest() {

    }

    /**
     * The main method.
     * @param args the arguments
     */
    public static void main(
        final String[] args) {
        Client.preInitialize(args);
        Client.postInitialize();
        (new SubstanceToolInstanceManagerAboutDialog()).showAboutDialog(Client
            .getContext());
    }
}

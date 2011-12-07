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

import javax.swing.JPanel;

import n.ui.SwingUtils;
import xmet.Client;

/**
 * The Class SpatialExtentUserTester.
 */
public final class SpatialExtentUserTest {

    /** Default constructor made private. */
    private SpatialExtentUserTest() {
    }

    /* == User testing code == */
    /**
     * The main method.
     * @param args the arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(
        // CHECKSTYLE OFF: MagicNumber
        final String[] args)
        throws InterruptedException {
        Client.preInitialize(args);
        Client.postInitialize();
        final JPanel panel = SwingUtils.BorderLayouts.getNew();
        final SpatialExtent dtp = new SpatialExtent(
            Client.getContext());
        panel.add(dtp);
        SwingUtils.COMPONENT.show(
            panel,
            800,
            600,
            true);
        // CHECKSTYLE ON: MagicNumber
    }
}

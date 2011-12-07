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
package xmet.utils;

import java.awt.Dimension;

import javax.swing.JFrame;

import n.reporting.Reporting;
import n.ui.SwingUtils;

import org.jdesktop.swingx.JXBusyLabel;

/**
 * user testing script for BusyScreenUtil.
 * @author Nahid Akbar
 */
public class BusyScreenUtilUserTest
    extends JFrame {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new busy screen util user tester.
     * @param title the title
     * @throws InterruptedException the interrupted exception
     */
    public BusyScreenUtilUserTest(
        final String title)
        throws InterruptedException {
        super(title);
        // CHECKSTYLE OFF: MagicNumber
        SwingUtils.WINDOW.centreSizeWindow(
            this,
            50);
        setVisible(true);
        BusyScreenUtil.startBusy("Doing this and that....");
        for (int i = 0; i < 200; i++) {
            Reporting.logExpected("aa");
            Thread.sleep(20);
            BusyScreenUtil.tickBusy(String.format(
                "Doing this and that.... %1$d",
                i));
        }
        BusyScreenUtil.endBusy();

        final JXBusyLabel busyLabel = new JXBusyLabel();
        busyLabel.setSize(
            400,
            400);
        busyLabel.setPreferredSize(new Dimension(
            400,
            400));
        busyLabel.setToolTipText("sbl");
        add(busyLabel);
        pack();
        repaint();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * The main method.
     * @param args the arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(
        final String[] args)
        throws InterruptedException {
        new BusyScreenUtilUserTest(
            "test");
    }
}

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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel;

/**
 * Class for containing utils swing helper functions.
 * @author Nahid Akbar
 */
public final class LookAndFeelUtil {

    /**
     * Instantiates a new look and feel util.
     */
    private LookAndFeelUtil() {

    }

    /**
     * Initialize gui fixes.
     */
    public static void initializeGUIFixes() {
        /* OS X fixes */
        System.setProperty(
            "com.apple.mrj.application.apple.menu.about.name",
            "xMET");
        System.setProperty(
            "apple.laf.useScreenMenuBar",
            "true");
        System.setProperty(
            "apple.awt.fileDialogForDirectories",
            "true");
        LookAndFeelUtil.setSubstanceLookAndFeel();
    }

    /**
     * Sets the substance look and feel.
     */
    private static void setSubstanceLookAndFeel() {
        try {
            /* My Choice */
            /* UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel()); */
            /* John W's choices */
            UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel());
            /* UIManager.setLookAndFeel(new SubstanceDustLookAndFeel()); */
        } catch (final UnsupportedLookAndFeelException e) {
            e.printStackTrace(); /* handle exception */
        }
    }

}

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import n.ui.SwingUtils;

import org.jdesktop.swingx.JXImagePanel;

import xmet.ClientContext;

/**
 * Shows "About" Dialog. Hardcoded
 * @author Nahid Akbar
 */
public class SubstanceToolInstanceManagerAboutDialog {

    /** The dialog. */
    private JDialog dialog;

    /**
     * Show about dialog.
     * @param context the context
     */
    public void showAboutDialog(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        /* create and show */
        final String title = String.format(
            "About %1$s",
            context.getConfig().getApplicationName());
        dialog = SwingUtils.DIALOG.createDialog(
            getAboutPanel(context),
            title,
            380,
            440,
            true);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        SwingUtils.WINDOW.centreWindow(dialog);
        dialog.setVisible(true);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * creates the about panel.
     * @param context the context
     * @return the about panel
     */
    private JPanel getAboutPanel(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        final JPanel panel = SwingUtils.GridBag.getNew();

        final String version = String.format(
            "<html><body bgcolor=\"ffffff\"><p>Version %1$s",
            context.getConfig().getApplicationVersion());

        final Image osdmLogo = context.getResources().getImageResource(
            "images/osdm_logo.png");
        final Icon xmetLogo = context.getResources().getImageIconResource(
            "images/xmet_logo.png");

        final String text = "<html>"
            + "<body bgcolor=\"ffffff\">"
            + "<p style=\"padding-top: 10px;\">"
            + "The extensible Metadata Editing Tool is a Java application that"
            + "<p style=\"padding-top: 10px;\">"
            + "allows users to manage ANZLIC Metadata Profile version 1.1"
            + "<p style=\"padding-top: 10px;\">"
            + "XML metadata. xMET is distributed using the LGPL version 3"
            + "<p style=\"padding-top: 10px;\">"
            + "licence agreement."
            + "<p style=\"padding-top: 10px;\">"
            + "More information about xMET is available at:"
            + "<p style=\"padding-top: 10px;\">"
            + "http://www.mymaps.gov.au/xmet/"
            + "<p style=\"padding-top: 10px;\">"
            + "In the future xMET will be distributed as Open Source Software.";

        final JXImagePanel osdmImagePanel = new JXImagePanel();
        osdmImagePanel.setImage(osdmLogo);
        osdmImagePanel.setStyle(JXImagePanel.Style.SCALED_KEEP_ASPECT_RATIO);
        osdmImagePanel.setPreferredSize(new Dimension(
            370,
            90));
        osdmImagePanel.setBackground(Color.WHITE);

        JLabel label;

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                xmetLogo),
            "x=1;y=1;w=3;a=c;");
        SwingUtils.GridBag.add(
            panel,
            osdmImagePanel,
            "x=1;y=4;w=3;a=c;wx=1;wy=0.245");
        label = new JLabel(
            version);
        SwingUtils.GridBag.add(
            panel,
            label,
            "x=1;y=2;w=3;a=c;");
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label = new JLabel(
            text);
        SwingUtils.GridBag.add(
            panel,
            label,
            "x=1;y=3;w=3;f=b;wx=1;wy=1;");
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(
            5,
            5,
            5,
            5));
        return panel;
        // CHECKSTYLE ON: MagicNumber
    }
}

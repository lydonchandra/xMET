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
package xmet.tools.metadata.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import n.reporting.Reporting;
import n.ui.SwingUtils;

import org.jdesktop.swingx.JXImagePanel;

import xmet.ClientContext;

/**
 * Default Implementation of metadata management viewing panel tree cell
 * renderer.
 * @author Nahid Akbar
 */
public class DefaultMMVPTCR
    extends MMVPTCR
    implements
    TreeCellRenderer {

    /** The client. */
    private final ClientContext context;

    /**
     * Instantiates a new default mmvptcr.
     * @param aClient the client
     */
    public DefaultMMVPTCR(
        final ClientContext aClient) {
        super();
        this.context = aClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installCellRenderer(
        final MetadataManagementViewingPanel mmvp) {
        try {
            mmvp.getFilesTree().setCellRenderer(
                this);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /* == TreeCellRenderer Interface Implementation == */
    /** The renderer panel. */
    private static JPanel rendererPanel = null;

    /** The icon panel. */
    private static JXImagePanel iconPanel;

    /** The name label. */
    private static JLabel nameLabel;

    /** The desc label. */
    private static JLabel descLabel;

    /** The file icon. */
    private static Image fileIcon = null;

    /** The closed folder icon. */
    private static Image closedFolderIcon = null;

    /** The open folder icon. */
    private static Image openFolderIcon = null;

    /** The selected color. */
    private static Color selectedColor;

    /** The normal color. */
    private static Color normalColor;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {
        // CHECKSTYLE OFF: MagicNumber
        if (rendererPanel == null) { /* initialize */
            try {
                init(context);
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        try {
            if (value instanceof MetadataFile) {
                final MetadataFile file = (MetadataFile) value;

                nameLabel.setText("<html><b>"
                    + file.getName());
                String abstract1 = file.getProfileName();
                if (abstract1 == null) {
                    abstract1 = "";
                }
                if (abstract1.length() > 35) {
                    abstract1 = abstract1.substring(
                        0,
                        35)
                        + "...";
                }
                descLabel.setText("<html><p>"
                    + abstract1);

                if (file.isFolder()) {
                    if (expanded) {
                        iconPanel.setImage(openFolderIcon);
                    } else {
                        iconPanel.setImage(closedFolderIcon);
                    }
                } else {
                    iconPanel.setImage(fileIcon);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (selected) {
            rendererPanel.setBackground(selectedColor);
        } else {
            rendererPanel.setBackground(normalColor);
        }
        return rendererPanel;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Inits the.
     * @param context the context
     */
    private static void init(
        final ClientContext context) {
        iconPanel = new JXImagePanel();
        iconPanel.setOpaque(false);
        nameLabel = new JLabel();
        descLabel = new JLabel();
        fileIcon = context.getResources().getImageResource(
            "images/mm.icon.file.png");
        closedFolderIcon = context.getResources().getImageResource(
            "images/mm.icon.closedFolder.png");
        openFolderIcon = context.getResources().getImageResource(
            "images/mm.icon.openFolder.png");

        rendererPanel = SwingUtils.GridBag.getNew();
        SwingUtils.GridBag.add(
            rendererPanel,
            iconPanel,
            "x=0;y=0;h=2;w=1;f=b;");
        SwingUtils.GridBag.add(
            rendererPanel,
            nameLabel,
            "x=1;y=0;h=1;w=1;wx=1;f=b;");
        SwingUtils.GridBag.add(
            rendererPanel,
            descLabel,
            "x=1;y=1;h=1;w=1;wx=1;f=b;");
        selectedColor = UIManager.getColor("Tree.selectionBackground");
        normalColor = UIManager.getColor("Tree.nackground");
    }

}

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

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;

/**
 * This dialog is for selecting a folder using Metadata Management Viewing
 * Panel.
 * @author Nahid Akbar
 */
public class MetadataFolderSelectionDialog {

    /** The mmvp. */
    private MetadataManagementViewingPanel mmvp;

    /** The client. */
    // ClientContext client;

    /** The selected file. */
    private MetadataFile selectedFile;

    /** The selected plugin. */
    private MMGMTPlugin selectedPlugin;

    /**
     * Gets the selected file.
     * @return the selected file
     */
    public MetadataFile getSelectedFile() {
        return selectedFile;
    }

    /**
     * Gets the selected plugin.
     * @return the selected plugin
     */
    public MMGMTPlugin getSelectedPlugin() {
        return selectedPlugin;
    }

    /**
     * Creates the editor pane.
     * @param client the client
     * @return the container
     */
    private Container createEditorPane(
        final ClientContext client) {
        // CHECKSTYLE OFF: MagicNumber
        // this.client = client;
        final JPanel panel = SwingUtils.GridBag.getNew();
        mmvp = new MetadataManagementViewingPanel(
            client);
        SwingUtils.GridBag.add(
            panel,
            mmvp.getPanel(),
            "w=rem;f=b;wx=1;wy=1;");
        final JPanel btnPanel = SwingUtils.GridBag.getNew();
        SwingUtils.GridBag.add(
            btnPanel,
            Box.createGlue(),
            "f=b;wx=0.5;x=0;y=0;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            btnPanel,
            SwingUtils.BUTTON.getNew(
                "Select",
                new ClassMethodCallback(
                    this,
                    "selectButtonCallback",
                    params)),
            "f=b;x=1;y=0;");
        final Object[] params1 = {};
        SwingUtils.GridBag.add(
            btnPanel,
            SwingUtils.BUTTON.getNew(
                "Cancel",
                new ClassMethodCallback(
                    this,
                    "cancelButtonCallback",
                    params1)),
            "f=b;x=2;y=0;");
        SwingUtils.GridBag.add(
            panel,
            btnPanel,
            "w=rem;f=b;wx=1;");
        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));
        return panel;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show selection dialog.
     * @param client the client
     */
    public void showSelectionDialog(
        final ClientContext client) {
        // CHECKSTYLE OFF: MagicNumber
        dialog = SwingUtils.DIALOG.createDialog(
            createEditorPane(client),
            "Select the Destination Folder...",
            800,
            600,
            true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        SwingUtils.WINDOW.centreSizeWindow(
            dialog,
            150);
        dialog.setVisible(true);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Select button callback.
     */
    public void selectButtonCallback() {
        selectedPlugin = mmvp.getSelectedPlugin();
        selectedFile = mmvp.getSelectedFile();
        if ((selectedFile != null)
            && selectedFile.isFolder()) {
            hideDialog();
        } else {
            Reporting.reportUnexpected("Please select a folder node");
        }
    }

    /**
     * Cancel button callback.
     */
    public void cancelButtonCallback() {
        selectedFile = null;
        selectedPlugin = null;
        hideDialog();
    }

    /** The dialog. */
    private JDialog dialog = null;

    /**
     * Hide & close dialog.
     */
    private void hideDialog() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }

}

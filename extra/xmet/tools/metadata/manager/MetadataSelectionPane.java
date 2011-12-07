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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.reporting.Reporting;
import n.ui.GenericSelectionPaneListener;
import n.ui.JFileChooserUtils;
import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import n.ui.patterns.callback.ClassMethodCallback;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTextField;

import xmet.ClientContext;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.manager.fs.LocalMetadataFile;

/**
 * Represents a selection pane for selecting metadata records and can also show
 * a popup dialog.
 * @author Nahid Akbar
 */
public class MetadataSelectionPane
    implements
    ActionListener,
    GenericSelectionPaneListener,
    DocumentListener {

    /** The external button. */
    private final JXButton externalButton = new JXButton(
        "External");

    /** The open button. */
    private final JXButton openButton = new JXButton(
        "Open");

    /** The save button. */
    private final JXButton saveButton = new JXButton(
        "Save");

    /** The cancel button. */
    private final JXButton cancelButton = new JXButton(
        "Cancel");

    /** The file text field. */
    private final JXTextField fileTextField = new JXTextField();

    /** The last selected is file. */
    private boolean lastSelectedIsFile = true;

    {
        externalButton.addActionListener(this);
        cancelButton.addActionListener(this);
        openButton.addActionListener(this);
        saveButton.addActionListener(this);
        fileTextField.addActionListener(this);
        fileTextField.getDocument().addDocumentListener(
            this);
    }

    /** The management viewing panel. */
    private final MetadataManagementViewingPanel mmvp;

    /** The selected file in EditableFile format. */
    private EditableFile selectedFile = null;

    /** The dialog used for displaying. */
    private JDialog dialog;

    /**
     * Instantiates a new metadata selection pane.
     * @param client the client
     */
    public MetadataSelectionPane(
        final ClientContext client) {
        mmvp = new MetadataManagementViewingPanel(
            client);
        mmvp.setSelectionListener(this);
    }

    /**
     * The save mode or load mode - some things are different depending on
     * whether this flag is true or not.
     */
    private boolean saveMode = false;

    /**
     * Show open file dialog.
     * @return the editable file
     */
    public EditableFile showOpenDialog() {
        saveMode = false;
        return showDialog(
            openButton,
            "Open Metadata");
    }

    /**
     * Show save file dialog.
     * @return the editable file
     */
    public EditableFile showSaveDialog() {
        saveMode = true;
        return showDialog(
            saveButton,
            "Save Metadata");
    }

    /**
     * Helper method for showing dialog.
     * @param button the button
     * @param title the title
     * @return the editable file
     */
    private EditableFile showDialog(
        final JXButton button,
        final String title) {
        // CHECKSTYLE OFF: MagicNumber
        dialog = new JDialog();

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(
                final WindowEvent e) {
                selectedFile = null;
            }
        });

        SwingUtils.WINDOW.centreSizeWindow(
            dialog,
            150);

        final JPanel panel = GridBag.getNew();
        SwingUtils.GridBag.add(
            panel,
            mmvp.getPanel(),
            "x=0;y=0;w=2;f=b;wx=1;wy=1;i=0,0,10,0;");

        SwingUtils.MOUSE.onDoubleClick(
            mmvp.getFilesTree(),
            new ClassMethodCallback(
                this,
                "actionPerformed",
                new ActionEvent(
                    openButton,
                    1,
                    "")));

        SwingUtils.GridBag.add(
            panel,
            new JXLabel(
                "File Name:"),
            "x=0;y=1;");
        SwingUtils.GridBag.add(
            panel,
            fileTextField,
            "x=1;y=1;f=h;wx=0.5;");

        final JPanel buttonsPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(externalButton);
        buttonsPanel.add(button);
        buttonsPanel.add(cancelButton);
        SwingUtils.GridBag.add(
            panel,
            buttonsPanel,
            "x=1;y=2;i=10,0,0,0;f=h;wx=1;");

        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));
        dialog.setContentPane(panel);
        dialog.setModal(true);
        dialog.setTitle(title);
        dialog.setVisible(true);
        return selectedFile;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * helper method that exits and cleans up the dialog.
     */
    private void exitDialog() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
    }

    /* == ActionListener Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == externalButton) {
            File newFile = JFileChooserUtils.getSingleOpenFile();
            if (newFile != null) {
                try {
                    if (!newFile.getName().endsWith(
                        ".xml")) {
                        newFile = new File(
                            newFile.getPath()
                                + ".xml");
                    }
                    /* if (saveMode && ! newFile.exists()) { */
                    /* newFile.createNewFile(); */
                    // }
                    setSelection(new LocalMetadataFile(
                        null,
                        newFile,
                        true,
                        mmvp.getContext()));
                } catch (final Exception e) {
                    Reporting.reportUnexpected(e);
                }
            }
        } else if ((arg0.getSource() == openButton)
            || (arg0.getSource() == saveButton)
            || (arg0.getSource() == fileTextField)) {
            if (selectedFile != null
                && lastSelectedIsFile) {
                exitDialog();
            }
        } else if (arg0.getSource() == cancelButton) {
            selectedFile = null;
            exitDialog();
        } else {
            Reporting.logUnexpected();
        }
    }

    /* == TreeSelectionListener implementation == */

    /**
     * Helper method that processes selection change event.
     * @param newSelected the new selected
     */
    private void setSelection(
        final EditableFile newSelected) {
        selectedFile = newSelected;
        if (selectedFile != null) {
            fileTextField.setText(selectedFile.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionChanged(
        final Object source) {
        /* Reporting.log("xxx"); */
        final MetadataFile selected = mmvp.getSelectedFile();
        if (selected != null) {
            lastSelectedIsFile = !selected.isFolder();
            if (lastSelectedIsFile) {
                final MMGMTPlugin plugin = mmvp.getSelectedPlugin();
                /* selectionChanged(selected); */
                if (plugin instanceof MetadataEditableMMGMTPlugin) {
                    setSelection(((MetadataEditableMMGMTPlugin) plugin)
                        .getEditableMetadataFile(selected));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionMade(
        final Object source) {
        actionPerformed(new ActionEvent(
            openButton,
            1,
            "asdf"));
    }

    /* == SelectedFile == */

    /**
     * Gets the selected file.
     * @return the selected file
     */
    public EditableFile getSelectedFile() {
        return selectedFile;
    }

    // --------------------------------------
    // -- DocumentListener Implementation
    /* for when user types in filenames */
    // --------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(
        final DocumentEvent arg0) {
        removeUpdate(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(
        final DocumentEvent arg0) {
        removeUpdate(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(
        final DocumentEvent arg0) {
        // CHECKSTYLE OFF: MagicNumber
        /* Reporting.log("removeUpdate"); */
        if (saveMode) {
            final MMGMTPlugin plugin = mmvp.getSelectedPlugin();
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                if (selectedFile == null
                    || selectedFile.getName() == null
                    || !selectedFile.getName().equals(
                        fileTextField.getText())) {
                    String fileName = fileTextField.getText();
                    fileName = fileName.replaceAll(
                        "[^\\p{Alnum}\\p{Blank}]",
                        "_").trim();
                    if (!fileName.endsWith(".xml")) {
                        fileName = fileName
                            + ".xml";
                    }
                    if (fileName.length() > 4) {
                        selectedFile =
                            ((MetadataEditableMMGMTPlugin) plugin)
                                .getEditableMetadataFile(
                                    mmvp.getSelectedOrParentFolder(),
                                    fileName);
                    }
                }
            }
        }
        // CHECKSTYLE ON: MagicNumber
    }
}

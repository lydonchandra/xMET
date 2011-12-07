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
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;

import n.io.bin.Files;
import n.io.net.CharacterSetUtils;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;
import n.ui.GenericSelectionListener;
import n.ui.JFileChooserUtils;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.xslt.TransformerSheet;
import xmet.tools.metadata.editor.EditableFile;
import xmet.utils.BusyScreenUtil;

/**
 * This dialog is for exporting metadata.
 * @author Nahid Akbar
 */
public class MetadataExportDialog {

    /** The Constant DESTINATION_MANAGEMENT_MODULE. */
    private static final String DESTINATION_MANAGEMENT_MODULE =
        "Internal Location";

    /** The Constant DESTINATION_EXTERNAL_FOLDER. */
    private static final String DESTINATION_EXTERNAL_FOLDER = "External Folder";

    /** The Constant SELECTED_SHEET_NONE. */
    private static final String SELECTED_SHEET_NONE = "None";

    /** The selected sheet. */
    private String selectedSheet = SELECTED_SHEET_NONE;

    /** The export files list mmvptcr. */
    private ExportFilesListMMVPTCR exportFilesListMMVPTCR;

    /** The mmvp. */
    private MetadataManagementViewingPanel mmvp;

    /** The client. */
    private ClientContext client;

    /** The selection count label. */
    private JLabel selectionCountLabel;

    /** The dest select button. */
    private JButton destSelectButton;

    /** The destination. */
    private ExportDesitnation destination;

    /** The preserve directory structure check box. */
    private JCheckBox preserveDirectoryStructureCheckBox;

    /**
     * Creates the editor pane.
     * @param aClient the client
     * @return the container
     */
    private Container createEditorPane(
        final ClientContext aClient) {
        // CHECKSTYLE OFF: MagicNumber
        this.client = aClient;
        final JPanel panel = SwingUtils.GridBag.getNew();
        try {
            mmvp = new MetadataManagementViewingPanel(
                aClient);
            exportFilesListMMVPTCR = new ExportFilesListMMVPTCR(
                aClient);
            mmvp.setRenderer(exportFilesListMMVPTCR);
            exportFilesListMMVPTCR
                .setSelectionListener(new GenericSelectionListener() {

                    @Override
                    public void selectionChanged(
                        final Object source) {
                        int filesCount = 0;
                        int folderCount = 0;
                        for (final MetadataFile mf : exportFilesListMMVPTCR
                            .getSelectedList()) {
                            if (mf.isFolder()) {
                                folderCount++;
                            } else {
                                filesCount++;
                            }
                        }
                        selectionCountLabel.setText(String.format(
                            "%1$d files in %2$d folders selected",
                            filesCount,
                            folderCount));
                    }
                });
            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "Files:"),
                "x=0;y=0;f=b;h=2;");
            SwingUtils.GridBag.add(
                panel,
                mmvp.getPanel(),
                "x=1;y=0;f=b;wx=1;wy=1;w=2;");
            selectionCountLabel = new JLabel(
                "0 files selected");
            SwingUtils.GridBag.add(
                panel,
                selectionCountLabel,
                "x=1;y=1;f=b;wx=0.5;w=2;");

            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "Destination:"),
                "x=0;y=2;f=b;");
            final Object[] params = {};
            destSelectButton = SwingUtils.BUTTON.getNew(
                "Select",
                new ClassMethodCallback(
                    this,
                    "selectDest",
                    params));
            SwingUtils.GridBag.add(
                panel,
                destSelectButton,
                "x=1;y=2;f=b;wx=1;w=2;");

            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "Transform:"),
                "x=0;y=3;f=b;");
            SwingUtils.GridBag.add(
                panel,
                new JComboBox(
                    getTransformersComboBoxModel()),
                "x=1;y=3;f=b;wx=1;w=2;");
            preserveDirectoryStructureCheckBox = new JCheckBox(
                "Preserve Directory Structure");
            SwingUtils.GridBag.add(
                panel,
                preserveDirectoryStructureCheckBox,
                "x=0;y=4;f=b;wx=1;w=3;");
            final Object[] params1 = {};

            SwingUtils.GridBag.add(
                panel,
                SwingUtils.BUTTON.getNew(
                    "Export",
                    new ClassMethodCallback(
                        this,
                        "doExport",
                        params1)),
                "x=2;y=5;");
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));
        return panel;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Do export.
     */
    public void doExport() {
        // {
        if (destination == null) {
            Reporting.reportUnexpected("Export destination not selected");
            return;
        }
        // }
        final ArrayList<MetadataFile> selectedList =
            exportFilesListMMVPTCR.getSelectedList();
        final ArrayList<MMGMTPlugin> selectedPlugins =
            exportFilesListMMVPTCR.getSelectedPlugins();
        // {
        int filesCount = 0;
        for (final MetadataFile mf : selectedList) {
            if (!mf.isFolder()) {
                filesCount++;
            }
        }
        if (filesCount == 0) {
            Reporting.reportUnexpected("No Files Selected");
            return;
        }
        // }
        final boolean preserveDirectoryStructure =
            preserveDirectoryStructureCheckBox.isSelected();
        final ArrayList<ByteBuffer> fileContents = new ArrayList<ByteBuffer>();
        final ArrayList<String> filePath = new ArrayList<String>();
        BusyScreenUtil.startBusy("Loading Files");
        try {
            for (int i = 0; i < selectedList.size(); i++) {
                final MetadataFile file = selectedList.get(i);
                final MMGMTPlugin plugin = selectedPlugins.get(i);
                if (!file.isFolder()) {
                    filePath.add(buildFilePath(
                        file,
                        selectedList,
                        preserveDirectoryStructure));
                    BusyScreenUtil.tickBusy();
                    fileContents.add(getFileContents(
                        file,
                        plugin));
                    BusyScreenUtil.tickBusy();
                }
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
        if (fileContents.size() > 0) {
            TransformerSheet transformationSheet = null;
            // {
            if (selectedSheet != SELECTED_SHEET_NONE) {
                for (final TransformerSheet sheet : client
                    .getProfiles()
                    .getTransformerSheetList()
                    .getSheets()) {
                    if (sheet.getName().equals(
                        selectedSheet)) {
                        transformationSheet = sheet;
                        break;
                    }
                }
            }
            // }

            if (transformationSheet != null) {
                BusyScreenUtil.startBusy("Transforming Files");
                try {
                    for (int i = 0; i < fileContents.size(); i++) {
                        try {
                            BusyScreenUtil.tickBusy();
                            Reporting.logExpected(
                                "Loading %1$s",
                                filePath.get(i));
                            String contents = CharacterSetUtils.decodeBuffer(
                                fileContents.get(i),
                                "UTF-8");
                            /* Reporting.log("Contents: %1$s", contents); */
                            BusyScreenUtil.tickBusy();
                            Reporting.logExpected(
                                "Transforming %1$s",
                                filePath.get(i));
                            try {
                                contents = transformationSheet.transformString(
                                    contents,
                                    client.getResources());
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            BusyScreenUtil.tickBusy();
                            Reporting.logExpected(
                                "Indenting %1$s",
                                filePath.get(i));
                            try {
                                contents =
                                    JDOMXmlUtils
                                        .indentXMLDocument(CharacterSetUtils
                                            .decodeString(
                                                contents,
                                                "UTF-8"));
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            fileContents.set(
                                i,
                                ByteBuffer.wrap(contents.getBytes()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (final Exception e) {
                    Reporting.reportUnexpected(e);
                } finally {
                    BusyScreenUtil.endBusy();
                }
            }

            // {
            BusyScreenUtil.startBusy("Writing Files");
            try {
                for (int i = 0; i < fileContents.size(); i++) {
                    destination.exportFile(
                        filePath.get(i),
                        fileContents.get(i),
                        client.getProfiles().getDataCodecByFileName(
                            filePath.get(i)));
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            } finally {
                BusyScreenUtil.endBusy();
            }
            hideDialog();
            // }
        } else {
            Reporting.reportUnexpected("No File Could Be Extracted");
        }
    }

    /**
     * Gets the file contents.
     * @param file the file
     * @param plugin the plugin
     * @return the file contents
     */
    private ByteBuffer getFileContents(
        final MetadataFile file,
        final MMGMTPlugin plugin) {
        if (plugin instanceof MetadataEditableMMGMTPlugin) {
            final EditableFile ef =
                ((MetadataEditableMMGMTPlugin) plugin)
                    .getEditableMetadataFile(file);
            if (ef != null) {
                return ef.getContents(client
                    .getProfiles()
                    .getDataCodecByFileName(
                        file.getName()));
            }
        }
        return file.getPreviewContents(client
            .getProfiles()
            .getDataCodecByFileName(
                file.getName()));
    }

    /**
     * Builds the file path.
     * @param file the file
     * @param selectedList the selected list
     * @param preserveDirectoryStructure the preserve directory structure
     * @return the string
     */
    private String buildFilePath(
        final MetadataFile file,
        final ArrayList<MetadataFile> selectedList,
        final boolean preserveDirectoryStructure) {
        final MetadataFile parent = file.getParent();
        String parentPart = "";
        if ((parent != null)
            && preserveDirectoryStructure) {
            if (selectedList.indexOf(parent) != -1) {
                parentPart = buildFilePath(
                    parent,
                    selectedList,
                    preserveDirectoryStructure);
            }
        }
        if (file.isFolder()) {
            return parentPart
                + file.getName()
                + "/";
        } else {
            return parentPart
                + file.getName();
        }
    }

    /**
     * Gets the transformers combo box model.
     * @return the transformers combo box model
     */
    private ComboBoxModel getTransformersComboBoxModel() {
        return new ComboBoxModel() {

            private final List<TransformerSheet> sheets = client
                .getProfiles()
                .getTransformerSheetList()
                .getSheets();

            @Override
            public void removeListDataListener(
                final ListDataListener arg0) {

            }

            @Override
            public int getSize() {
                return 1 + sheets.size();
            }

            @Override
            public Object getElementAt(
                final int arg0) {
                if (arg0 == 0) {
                    return SELECTED_SHEET_NONE;
                } else {
                    return sheets.get(
                        arg0 - 1).getName();
                }
            }

            @Override
            public void addListDataListener(
                final ListDataListener arg0) {

            }

            @Override
            public void setSelectedItem(
                final Object arg0) {
                selectedSheet = (String) arg0;
            }

            @Override
            public Object getSelectedItem() {
                return selectedSheet;
            }
        };
    }

    /**
     * Show export dialog.
     * @param aClient the client
     */
    public void showExportDialog(
        final ClientContext aClient) {
        // CHECKSTYLE OFF: MagicNumber
        dialog = SwingUtils.DIALOG.createDialog(
            createEditorPane(aClient),
            "Export",
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

    /**
     * Select dest.
     */
    public void selectDest() {
        final Object[] exportLocations = new Object[] {
        DESTINATION_EXTERNAL_FOLDER,
        DESTINATION_MANAGEMENT_MODULE
        };
        final String option = (String) JOptionPaneUtils.getUserChoice(
            "Please Select Where to Export",
            exportLocations);
        if (option != null) {
            if (option.equals(DESTINATION_EXTERNAL_FOLDER)) {
                final File file =
                    JFileChooserUtils.getSingleOpenFolder(Files
                        .getMyDocumentsPath());
                if ((file != null)
                    && file.isDirectory()) {
                    destSelectButton.setText(String.format(
                        "%1$s (Click to Change)",
                        file.getPath()));
                    destination = new ExternalExportDestination(
                        file);
                }
            } else if (option.equals(DESTINATION_MANAGEMENT_MODULE)) {
                final MetadataFolderSelectionDialog mfsd =
                    new MetadataFolderSelectionDialog();
                mfsd.showSelectionDialog(client);
                if ((mfsd.getSelectedFile() != null)
                    && (mfsd.getSelectedPlugin() != null)
                    && mfsd.getSelectedFile().isFolder()) {
                    destination = new InternalExportDestination(
                        mfsd.getSelectedFile(),
                        mfsd.getSelectedPlugin());
                    destSelectButton.setText(String.format(
                        "%1$s \\ %2$s (Click to Change)",
                        mfsd.getSelectedPlugin().getName(),
                        mfsd.getSelectedFile().getName()));
                }
            } else {
                Reporting.logUnexpected();
            }
        }
    }

    /**
     * Representation of export designation.
     */
    private abstract static class ExportDesitnation {

        /**
         * Export file.
         * @param path the path
         * @param contents the contents
         * @param dataCodec the data codec
         */
        public abstract void exportFile(
            String path,
            ByteBuffer contents,
            DataCodec dataCodec);
    }

    /**
     * Represents an external (local folder) export destination.
     */
    private static class ExternalExportDestination
        extends ExportDesitnation {

        /**
         * Instantiates a new external export destination.
         * @param aFolder the folder
         */
        public ExternalExportDestination(
            final File aFolder) {
            this.folder = aFolder;
        }

        /** The folder. */
        private final File folder;

        /**
         * {@inheritDoc}
         */
        @Override
        public void exportFile(
            final String path,
            final ByteBuffer contents,
            final DataCodec dataCodec) {
            final String fullPath = String.format(
                "%1$s/%2$s",
                folder.getAbsolutePath(),
                path);
            final File fullPathFile = new File(
                fullPath);
            Files.ensureParentExists(fullPathFile);
            Files.write(
                fullPathFile,
                contents);
        }
    }

    /**
     * Represents an internal (Metadata manager plugin) export destination.
     */
    private static class InternalExportDestination
        extends ExportDesitnation {

        /**
         * Instantiates a new internal export destination.
         * @param selectedFile the selected file
         * @param selectedPlugin the selected plugin
         */
        public InternalExportDestination(
            final MetadataFile selectedFile,
            final MMGMTPlugin selectedPlugin) {
            folder = selectedFile;
            plugin = selectedPlugin;
        }

        /** The plugin. */
        private final MMGMTPlugin plugin;

        /** The folder. */
        private final MetadataFile folder;

        /**
         * {@inheritDoc}
         */
        @Override
        public void exportFile(
            final String path,
            final ByteBuffer contents,
            final DataCodec dataCodec) {
            final Stack<String> pathStack = new Stack<String>();
            // {
            String varPath = path;
            varPath = varPath.replaceAll(
                "\\\\",
                "/");
            final String[] parts = (varPath).split("/");
            for (int i = parts.length - 1; i >= 0; i--) {
                pathStack.add(parts[i]);
            }
            // }
            if ((pathStack.size() > 1)
                && (plugin instanceof FoldersEditableMMGMTPlugin)) {
                MetadataFile parentFolder = folder;
                while ((pathStack.size() > 1)
                    && (parentFolder != null)) {
                    final String part = pathStack.pop();
                    parentFolder = getChildFolder(
                        parentFolder,
                        part,
                        plugin);
                }

                MetadataFile varParentFolder = null;
                if (parentFolder != null) {
                    varParentFolder = parentFolder;
                } else {
                    varParentFolder = folder;
                }
                setFileContents(
                    varParentFolder,
                    pathStack.get(0),
                    plugin,
                    contents,
                    dataCodec);
            } else {
                setFileContents(
                    folder,
                    pathStack.get(0),
                    plugin,
                    contents,
                    dataCodec);
            }
        }

        /**
         * Gets the child folder.
         * @param parentFolder the parent folder
         * @param part the part
         * @param aPlugin the plugin
         * @return the child folder
         */
        private MetadataFile getChildFolder(
            final MetadataFile parentFolder,
            final String part,
            final MMGMTPlugin aPlugin) {
            MetadataFile[] c = parentFolder.getChildren();
            for (final MetadataFile metadataFile : c) {
                if (metadataFile.getName().toLowerCase().equals(
                    part.toLowerCase())) {
                    return metadataFile;
                }
            }
            if (aPlugin instanceof FoldersEditableMMGMTPlugin) {
                ((FoldersEditableMMGMTPlugin) aPlugin).addNewFolder(
                    parentFolder,
                    part);
                c = parentFolder.getChildren();
                for (final MetadataFile metadataFile : c) {
                    if (metadataFile.getName().toLowerCase().equals(
                        part.toLowerCase())) {
                        return metadataFile;
                    }
                }
                Reporting.logUnexpected();
            }
            return null;
        }

        /**
         * Sets the file contents.
         * @param parentFolder the parent folder
         * @param name the name
         * @param aPlugin the plugin
         * @param contents the contents
         * @param dataCodec the data codec
         */
        private void setFileContents(
            final MetadataFile parentFolder,
            final String name,
            final MMGMTPlugin aPlugin,
            final ByteBuffer contents,
            final DataCodec dataCodec) {
            if (aPlugin instanceof MetadataEditableMMGMTPlugin) {
                final EditableFile ef =
                    ((MetadataEditableMMGMTPlugin) aPlugin)
                        .getEditableMetadataFile(
                            parentFolder,
                            name);
                ef.setContents(
                    contents,
                    null,
                    dataCodec);
            } else {
                Reporting.logUnexpected();
            }
        }

    }

}

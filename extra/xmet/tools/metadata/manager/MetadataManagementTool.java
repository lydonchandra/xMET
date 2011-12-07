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

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import n.io.bin.Files;
import n.reporting.Reporting;
import n.ui.GenericSelectionPaneListener;
import n.ui.JFileChooserUtils;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.Profile;
import xmet.profiles.ProfileManager;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.services.ServiceProvider;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.editor.FilesSelectionUtil;
import xmet.ui.EditorSheetSelectionDialog;
import xmet.ui.ToolbarBuilder;
import xmet.ui.ToolbarItem;

/**
 * Tool for managing metadata.
 * @author Nahid Akbar
 */
public class MetadataManagementTool
    extends DefaultTool
    implements
    FilesSelectionUtil,
    ServiceProvider<FilesSelectionUtil> {

    /** The Constant TOOL_NAME. */
    public static final String TOOL_NAME = "metadata.manage";

    /* == Tool interface == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize() {
        super.onInitialize();
        plugins = new ArrayList<MMGMTPlugin>();

        final MMGMTPluginWrapper[] pluginWrappers =
            getContext()
                .getServices()
                .<MMGMTPluginWrapper>getServiceProviderList(
                    MMGMTPluginWrapper.class);

        if (pluginWrappers != null) {

            /* default plugin */
            for (final MMGMTPluginWrapper mpw : pluginWrappers) {
                if (mpw.isDefaultPlugin()) {
                    plugins.add(mpw.instantiateFromConfig(
                        null,
                        getContext()));
                }
            }
            setSelectedPlugin(plugins.get(0));

            /* load saved metadata management plugins */
            final Preferences prefs = Preferences.userRoot().node(
                "/xmet");

            final int count = prefs.getInt(
                "mgmt_plugins_count",
                0);
            for (int x = 0; x < count; x++) {
                try {
                    final String id = prefs.get(
                        "mgmt_plugins_id_"
                            + (x + 1),
                        "");
                    final String setting = prefs.get(
                        "mgmt_plugins_setting_"
                            + (x + 1),
                        "");
                    if ((id != null)
                        && (setting != null)
                        && (id.trim().length() > 0)) {
                        final int size = plugins.size();
                        for (final MMGMTPluginWrapper mpw : pluginWrappers) {
                            if (mpw.getID().equals(
                                id)) {
                                final MMGMTPlugin instantiation =
                                    mpw.instantiateFromConfig(
                                        setting,
                                        getContext());
                                if (instantiation != null) {
                                    plugins.add(instantiation);
                                }
                                break;
                            }
                        }
                        if (size == plugins.size()) {
                            Reporting.reportUnexpected(
                                "Unknown plugin ID %1$s",
                                id);
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        getContext().getServices().registerServiceProviders(
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisposal() {
        /* save existing metadata management plugins */
        final MMGMTPluginWrapper[] pluginWrappers =
            getContext()
                .getServices()
                .<MMGMTPluginWrapper>getServiceProviderList(
                    MMGMTPluginWrapper.class);
        if (pluginWrappers != null) {
            int count = 0;
            final Preferences prefs = Preferences.userRoot().node(
                "/xmet");
            for (int x = 1; x < plugins.size(); x++) {
                final MMGMTPlugin plugin = plugins.get(x);
                try {
                    for (final MMGMTPluginWrapper mpw : pluginWrappers) {
                        if (mpw.getID().equals(
                            plugin.getId())) {
                            final String pluginConfig =
                                mpw.getPluginConfig(plugin);
                            count++;
                            prefs.put(
                                "mgmt_plugins_id_"
                                    + (count),
                                plugin.getId());
                            prefs.put(
                                "mgmt_plugins_setting_"
                                    + (count),
                                pluginConfig);

                            break;
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            prefs.putInt(
                "mgmt_plugins_count",
                count);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return TOOL_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params) {
        return getInstance();
    }

    /** The instance. */
    private static Instance instance = null;

    /* getinstance singleton */
    /**
     * Gets the single instance of MetadataManagementTool.
     * @return single instance of MetadataManagementTool
     */
    private ToolInstance getInstance() {
        if (instance == null) {
            instance = new Instance();
        }
        return instance;
    }

    /* == Extensions == */

    /** The metadata manager title. */
    private static String metadataManagerTitle = "Metadata Manager";
    /* == Plugins == */

    /** The plugins. */
    private ArrayList<MMGMTPlugin> plugins = null;

    /**
     * Gets the plugins.
     * @return the plugins
     */
    public ArrayList<MMGMTPlugin> getPlugins() {
        return plugins;
    }

    /** The selected. */
    private MMGMTPlugin selected;

    /**
     * Gets the selected plugin.
     * @return the selected plugin
     */
    public MMGMTPlugin getSelectedPlugin() {
        return selected;
    }

    /**
     * Sets the selected plugin.
     * @param plugin the new selected plugin
     */
    public void setSelectedPlugin(
        final MMGMTPlugin plugin) {
        selected = plugin;
    }

    //
    /* public int getSelectedPluginIndex() { */
    /* return 0; */
    // }

    /* == Tool Instance interface == */

    /**
     * The tool instance.
     */
    public class Instance
        implements
        ToolInstance {

        /** The Constant MENU_NEW_FOLDER. */
        private static final String MENU_NEW_FOLDER = "New Folder";

        /** The Constant MENU_NEW_METADATA. */
        private static final String MENU_NEW_METADATA = "New Metadata";
        /* == ToolInstance Interface Implementation == */
        /** The panel. */
        private JPanel panel = null;

        /** The button panel. */
        private JPanel buttonPanel = null;

        /** The view panel. */
        private MetadataManagementViewingPanel viewPanel = null;

        /**
         * {@inheritDoc}
         */
        @Override
        public Tool getTool() {
            return MetadataManagementTool.this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle() {
            return getMetadataManagerTitle();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getDisplayPanel() {
            return panel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onInstantiation() {

            /* make the basic layout */
            panel = SwingUtils.GridBag.getNew();
            buttonPanel = SwingUtils.BorderLayouts.getNew();
            viewPanel = new MetadataManagementViewingPanel(
                getContext()) {

                @Override
                public void refresh() {
                    super.refresh();
                    rebuildSubPanels(false);
                };
            };

            viewPanel.setSelectionListener(new GenericSelectionPaneListener() {

                @Override
                public void selectionChanged(
                    final Object source) {
                }

                @Override
                public void selectionMade(
                    final Object source) {
                    editButtonCallback();
                }
            });

            /* build the subpanels */
            rebuildSubPanels(true);

            /* integrate */
            SwingUtils.GridBag.add(
                panel,
                viewPanel.getPanel(),
                "wx=1;wy=1;f=b;w=rel;i=0,5,5,5;");
            SwingUtils.GridBag.add(
                panel,
                buttonPanel,
                "wy=1;f=v;w=rem;i=0,5,5,0;");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisposal() {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onClose(
            final boolean force) {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onRefocus(
            final Map<String, Object> params) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onEvent(
            final int applicationEvent) {

        }

        /**
         * method rebuilds panel based on the current mgmt plugin.
         * @param refreshVP flag to say whether or not to refresh the viewing
         *            panel
         */
        public void rebuildSubPanels(
            final boolean refreshVP) {
            if (viewPanel == null) {
                return;
            }
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            // {
            /* rebuild buttons panel */
            buttonPanel.removeAll();
            final ToolbarBuilder tBuilder = new ToolbarBuilder(
                getContext(),
                "Metadata Management Toolbar",
                true,
                true);
            boolean newButton = false;
            boolean editButton = false;
            boolean renameButton = false;
            boolean deleteButton = false;
            boolean importButton = false;
            boolean exportButton = false;
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                newButton = true;
                editButton = true;
                renameButton = true;
                deleteButton = true;
                importButton = true;
                exportButton = true;
            }
            if (plugin instanceof FoldersEditableMMGMTPlugin) {
                newButton = true;
                renameButton = true;
                deleteButton = true;
            }
            if (newButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "New",
                        "images/toolbar.common.newDocument.png",
                        new ClassMethodCallback(
                            this,
                            "newButtonCallback")));
            }
            if (editButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Edit",
                        "images/toolbar.common.editDocument.png",
                        new ClassMethodCallback(
                            this,
                            "editButtonCallback")));
            }
            if (renameButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Rename",
                        "images/toolbar.common.renameDocument.png",
                        new ClassMethodCallback(
                            this,
                            "renameButtonCallback")));
            }
            if (deleteButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Delete",
                        "images/toolbar.common.deleteDocument.png",
                        new ClassMethodCallback(
                            this,
                            "deleteButtonCallback")));
            }
            if (importButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Import",
                        "images/toolbar.common.importDocument.png",
                        new ClassMethodCallback(
                            this,
                            "importButtonCallback")));
            }
            if (exportButton) {
                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Export",
                        "images/toolbar.common.exportDocument.png",
                        new ClassMethodCallback(
                            this,
                            "exportButtonCallback")));
            }

            tBuilder.getItems().add(
                null);
            final MetadataManagerToolbarExtension[] spl = getExtenstions();
            if (spl != null) {
                for (final MetadataManagerToolbarExtension extension : spl) {
                    if (extension.supportsPlugin(plugin)) {
                        tBuilder.getItems().add(
                            new ToolbarItem(
                                extension.getLabel(),
                                extension.getIconPath(),
                                new ClassMethodCallback(
                                    this,
                                    "executeExtension",
                                    extension)));
                    }
                }
            }

            final JToolBar toolbar = tBuilder.buildToolbar();
            buttonPanel.add(toolbar);
            buttonPanel.validate();

            if (refreshVP) {
                /* rebuild files list panel - just the JTree */
                viewPanel.refresh();
            }
            // }
        }

        /**
         * Gets the extenstions.
         * @return the extenstions
         */
        private MetadataManagerToolbarExtension[] getExtenstions() {
            return getContext()
                .getServices()
                .<MetadataManagerToolbarExtension>getServiceProviderList(
                    MetadataManagerToolbarExtension.class);
        }

        /* == Different operation button callbacks == */
        /**
         * Execute extension.
         * @param extension the extension
         */
        public void executeExtension(
            final MetadataManagerToolbarExtension extension) {
            extension.onCallback(viewPanel);
        }

        /**
         * New button callback.
         */
        public void newButtonCallback() {
            final ArrayList<String> options = new ArrayList<String>();

            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                options.add(MENU_NEW_METADATA);
                if (plugin instanceof FoldersEditableMMGMTPlugin) {
                    options.add(MENU_NEW_FOLDER);
                }
                final Object choice = JOptionPaneUtils.getUserChoice(
                    "New ...",
                    options.toArray());
                if (choice != null) {
                    if (choice == MENU_NEW_FOLDER) {
                        newFolderCallback();
                    } else if (choice == MENU_NEW_METADATA) {
                        newMetadataCallback();
                    } else {
                        Reporting.logUnexpected();
                    }
                }
            }
        }

        /**
         * Rename button callback.
         */
        public void renameButtonCallback() {
            final MetadataFile aSelected = viewPanel.getSelectedFile();
            if (aSelected != null) {
                if (aSelected.isFolder()) {
                    renameFolderCallback();
                } else {
                    renameMetadataCallback();
                }
            }
        }

        /**
         * Delete button callback.
         */
        public void deleteButtonCallback() {
            final MetadataFile aSelected = viewPanel.getSelectedFile();
            if (aSelected != null) {
                if (aSelected.isFolder()) {
                    deleteFolderCallback();
                } else {
                    deleteMetadataCallback();
                }
            }
        }

        /**
         * Import button callback.
         */
        public void importButtonCallback() {
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                /* get selected metadata */
                MetadataFile aSelected = viewPanel.getSelectedFile();

                if (aSelected == null) {
                    return;
                }
                if (!aSelected.isFolder()) {
                    aSelected = aSelected.getParent();
                }
                if (aSelected.isFolder()) {
                    /* get file to import */
                    final File file =
                        JFileChooserUtils.getSingleOpenFileWithExtension(
                            "Metadata Files",
                            "xml");
                    if ((file != null)
                        && !file.isDirectory()) {
                        final ByteBuffer contents = Files.read(file);
                        if (contents != null) {
                            final EditableFile ef =
                                ((MetadataEditableMMGMTPlugin) plugin)
                                    .getEditableMetadataFile(
                                        aSelected,
                                        file.getName());
                            ef.setContents(
                                contents,
                                null,
                                getContext()
                                    .getProfiles()
                                    .getDataCodecByFileName(
                                        file.getName()));
                            rebuildSubPanels(true);
                        }
                    }
                }
            }
        }

        /**
         * Export button callback.
         */
        public void exportButtonCallback() {
            new MetadataExportDialog().showExportDialog(getContext());
        }

        /**
         * New metadata callback.
         */
        public void newMetadataCallback() {
            final EditorSheetSelectionDialog essd =
                new EditorSheetSelectionDialog(
                    getContext().getProfiles());
            final ProfileEditorSheet sheet = essd.showSheetSelectionDialog();
            if (sheet != null) {
                final Map<String, Object> newParams =
                    new HashMap<String, Object>();
                newParams.put(
                    "editorSheet",
                    sheet);
                getContext().getTools().invokeToolByName(
                    "metadata.editor",
                    newParams);
            }
        }

        /**
         * Edits the button callback.
         */
        public void editButtonCallback() {

            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();

            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                /* get selected metadata */
                final MetadataFile aSelected = viewPanel.getSelectedFile();

                if ((aSelected == null)
                    || aSelected.isFolder()) {
                    return;
                }

                /* get stylesheet */
                ProfileEditorSheet editorSheet = null;
                try {
                    editorSheet = selectStylesheet(
                        aSelected.getProfileName(),
                        aSelected.getEditorSheetName());
                } catch (final Exception e) {
                    Reporting.reportUnexpected(e);
                    return;
                }

                final EditableFile file =
                    ((MetadataEditableMMGMTPlugin) plugin)
                        .getEditableMetadataFile(aSelected);

                if (file != null) {
                    /* invoke metadata editor with appropriate callback */
                    /* parameters */
                    getContext().getTools().invokeToolByName(
                        "metadata.editor",
                        "editorSheet",
                        editorSheet,
                        "file",
                        file);
                } else {
                    Reporting.reportUnexpected("error getting file");
                }
            }
        }

        /**
         * Delete metadata callback.
         */
        public void deleteMetadataCallback() {
            /* get selected */
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                /* get selected metadata */
                final MetadataFile aSelected = viewPanel.getSelectedFile();

                if ((aSelected == null)
                    || aSelected.isFolder()) {
                    return;
                }
                /* call delete */
                /* TODO Check return value */
                ((MetadataEditableMMGMTPlugin) plugin)
                    .deleteMetadataFile(aSelected);
                rebuildSubPanels(true);
            }
        }

        /**
         * Rename metadata callback.
         */
        private void renameMetadataCallback() {
            /* get selected */
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof MetadataEditableMMGMTPlugin) {
                /* get selected metadata */
                final MetadataFile aSelected = viewPanel.getSelectedFile();

                if ((aSelected == null)
                    || aSelected.isFolder()) {
                    return;
                }
                String suffix = "";
                if (aSelected.getName().lastIndexOf(
                    '.') != -1) {
                    suffix = aSelected.getName().substring(
                        aSelected.getName().lastIndexOf(
                            '.'));
                }
                String newName = JOptionPaneUtils.getUserInputString(
                    "Please specify the new name of the file",
                    aSelected.getName());
                if (newName != null
                    && newName.trim().length() != 0) {
                    newName = newName.trim();
                    if (!newName.endsWith(suffix)) {
                        newName = newName
                            + suffix;
                    }
                    /* call rename */
                    ((MetadataEditableMMGMTPlugin) plugin).renameMetadataFile(
                        aSelected,
                        newName);
                    viewPanel.valueChanged(null);
                }
                rebuildSubPanels(true);
            }
        }

        /**
         * New folder callback.
         */
        public void newFolderCallback() {
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof FoldersEditableMMGMTPlugin) {
                final FoldersEditableMMGMTPlugin folderPlugin =
                    (FoldersEditableMMGMTPlugin) plugin;
                final MetadataFile parentFolder =
                    viewPanel.getSelectedOrParentFolder();
                if ((parentFolder != null)
                    && parentFolder.isFolder()) {
                    String newName = JOptionPaneUtils.getUserInputString(
                        "Please enter a folder name",
                        MENU_NEW_FOLDER);
                    if (newName != null
                        && newName.trim().length() != 0) {
                        newName = newName.trim();
                        for (final MetadataFile file : parentFolder
                            .getChildren()) {
                            if (file.isFolder()
                                && file.getName().equals(
                                    newName)) {
                                Reporting.reportUnexpected("File with name "
                                    + "already exists");
                                return;
                            }
                        }
                        folderPlugin.addNewFolder(
                            parentFolder,
                            newName);
                        rebuildSubPanels(true);
                    }
                } else {
                    Reporting.reportUnexpected("select a parent folder");
                }
            }
        }

        /**
         * Rename folder callback.
         */
        public void renameFolderCallback() {
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof FoldersEditableMMGMTPlugin) {
                final FoldersEditableMMGMTPlugin folderPlugin =
                    (FoldersEditableMMGMTPlugin) plugin;
                final MetadataFile folder = viewPanel.getSelectedFile();
                if ((folder != null)
                    && folder.isFolder()) {
                    String newName = JOptionPaneUtils.getUserInputString(
                        "Please enter a new name",
                        folder.getName());
                    if (newName != null
                        && newName.trim().length() != 0) {
                        newName = newName.trim();
                        folderPlugin.renameFolder(
                            folder,
                            newName);
                        viewPanel.valueChanged(null);
                        rebuildSubPanels(true);
                    }
                } else {
                    Reporting.reportUnexpected("select a folder");
                }
            }
        }

        /**
         * Delete folder callback.
         */
        public void deleteFolderCallback() {
            final MMGMTPlugin plugin = viewPanel.getSelectedPlugin();
            if (plugin instanceof FoldersEditableMMGMTPlugin) {
                final FoldersEditableMMGMTPlugin folderPlugin =
                    (FoldersEditableMMGMTPlugin) plugin;
                final MetadataFile folder = viewPanel.getSelectedFile();
                if ((folder != null)
                    && folder.isFolder()) {
                    if (JOptionPaneUtils
                        .getYesNoConfirmation("Do you really want"
                            + " to delete the selected folder?")) {
                        folderPlugin.deleteFolder(folder);
                        rebuildSubPanels(true);
                    }
                } else {
                    Reporting.reportUnexpected("select a folder");
                }
            }
        }

        /* == Misc helper methods == */
        /**
         * Select stylesheet.
         * @param profileName the profile name
         * @param sheetName the sheet name
         * @return the profile editor sheet
         * @throws NoEditorSheetsFoundException the no editor sheets found
         *             exception
         */
        private ProfileEditorSheet selectStylesheet(
            final String profileName,
            final String sheetName)
            throws NoEditorSheetsFoundException {
            ProfileEditorSheet sheet = null;
            Profile profile = null;
            /* try to automatically determine */
            final ProfileManager profileManager = getContext().getProfiles();
            final ArrayList<Profile> profiles = profileManager.getProfiles();
            String varSheetName = sheetName;
            String varProfileName = profileName;
            if ((varProfileName != null && varProfileName.trim().length() > 0)
                || (varSheetName != null && varSheetName.trim().length() > 0)) {
                if (varProfileName != null
                    && varProfileName.trim().length() != 0) {
                    varProfileName = varProfileName.trim();
                    profile =
                        profileManager.getProfileByKeyword(varProfileName);
                }
                if ((profile == null)
                    && (profiles.size() == 0)) {
                    profile = profiles.get(0);
                }
                if ((profile != null)
                    && (varSheetName != null)
                    && (varSheetName.length() > 0)) {
                    sheet = profile.getEditorSheetByName(varSheetName);
                } else if (varSheetName != null
                    && varSheetName.trim().length() != 0) {
                    varSheetName = varSheetName.trim();
                    for (final Profile p : profiles) {
                        sheet = p.getEditorSheetByName(varSheetName);
                        if (sheet != null) {
                            break;
                        }
                    }
                }
            }
            /* when determined profile but not sheet */
            if ((profile != null)
                && (sheet == null)) {
                if (profile.getEditorSheets().size() == 1) {
                    sheet = profile.getEditorSheets().get(
                        0);
                } else {
                    final EditorSheetSelectionDialog essd =
                        new EditorSheetSelectionDialog(
                            profile);
                    essd.showProfileSheetSelectionDialog();
                }
            }
            /* when neither profile or sheet could be determined */
            if ((profile == null)
                && (sheet == null)) {
                final EditorSheetSelectionDialog essd =
                    new EditorSheetSelectionDialog(
                        profileManager);
                sheet = essd.showSheetSelectionDialog();
            }
            if (sheet == null) {
                throw new NoEditorSheetsFoundException();
            }
            return sheet;
        }

    }

    /* == FileSelectionUtil implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getSingleSelectedSaveFile(
        final List<FileFilter> filters) {
        final MetadataSelectionPane pane = new MetadataSelectionPane(
            getContext());
        return pane.showSaveDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getSingleSelectedOpenFile() {
        final MetadataSelectionPane pane = new MetadataSelectionPane(
            getContext());
        return pane.showOpenDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFileReplaceConfirmation(
        final EditableFile file) {
        return JOptionPaneUtils.getYesNoConfirmation("File Already exists. "
            + "Do you want to replace the file?");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCancellableSaveConfirmation(
        final EditableFile file) {
        switch (JOptionPaneUtils.getYesNoCancelConfirmation(
            "Save",
            "Do you want to save metadata?")) {
        case JOptionPane.YES_OPTION:
            return 1;
        case JOptionPane.NO_OPTION:
            return 0;
        default:
            return -1;
        }
    }

    /* == ServiceProvider<FilesSelectionUtil> implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 1;
    }

    /**
     * Sets the metadata manager title.
     * @param aMetadataManagerTitle the new metadata manager title
     */
    public static void setMetadataManagerTitle(
        final String aMetadataManagerTitle) {
        MetadataManagementTool.metadataManagerTitle = aMetadataManagerTitle;
    }

    /**
     * Gets the metadata manager title.
     * @return the metadata manager title
     */
    public static String getMetadataManagerTitle() {
        return metadataManagerTitle;
    }

}

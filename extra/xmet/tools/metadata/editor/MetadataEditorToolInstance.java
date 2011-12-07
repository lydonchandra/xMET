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
package xmet.tools.metadata.editor;

import java.awt.BorderLayout;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import n.reporting.Reporting;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.SwingUtils.COMPONENT;
import n.ui.patterns.callback.ClassMethodCallback;
import n.ui.patterns.sdm.SimpleSingleDocumentManagedUnit;
import n.ui.patterns.sdm.SimpleSingleDocumentManager;
import xmet.profiles.Profile;
import xmet.profiles.codecs.ContentsCouldNotBeDecodedException;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.codecs.impl.XMLDataCodec;
import xmet.profiles.editorSheet.EditorSheetTemplate;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;
import xmet.profiles.utils.MetadataModelMetadataUtils;
import xmet.tools.DocumentEditingTI;
import xmet.tools.MultipageTI;
import xmet.tools.Tool;
import xmet.tools.ToolCallback;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceEvents;
import xmet.tools.TransparentToolException;
import xmet.ui.ToolbarBuilder;
import xmet.ui.ToolbarItem;
import xmet.ui.templates.TemplatesSelectionDialog;
import xmet.utils.BusyScreenUtil;

/**
 * Metadata editor view class. document bug - Handle DocumentCouldNotBeLoaded
 * exception
 * @author Nahid Akbar
 */

public class MetadataEditorToolInstance
    extends Observable
    implements
    ToolInstance,
    Observer,
    SimpleSingleDocumentManagedUnit<EditableFile>,
    DocumentEditingTI,
    MultipageTI {

    /** The callback. */
    private final ToolCallback callback;

    /** The tool. */
    private final MetadataEditorTool tool;

    /** The panel. */
    private JPanel panel;

    /** The view panel. */
    private JComponent viewPanel;

    /** The codec. */
    private DataCodec codec;

    /** Containing view. */
    private EditorView view;

    /** link to profile. */
    private Profile profile;

    /** The editor sheet. */
    private final ProfileEditorSheet editorSheet;

    /** The template. */
    private EditorSheetTemplate template;

    /** The fm. */
    private final SimpleSingleDocumentManager<EditableFile> fm;

    /** The file selection. */
    private final FilesSelectionUtil fileSelection;

    /**
     * Instantiates a new metadata editor tool instance.
     * @param metadataEditorToolI the metadata editor tool i
     * @param aProfile the profile
     * @param aEditorSheet the editor sheet
     * @param editableFile the editable file
     * @param aCallback the callback
     * @param aFileSelection the file selection
     * @param aTemplate the template
     * @throws Exception the exception
     */
    public MetadataEditorToolInstance(
        final MetadataEditorTool metadataEditorToolI,
        final Profile aProfile,
        final ProfileEditorSheet aEditorSheet,
        final EditableFile editableFile,
        final ToolCallback aCallback,
        final FilesSelectionUtil aFileSelection,
        final EditorSheetTemplate aTemplate)
        throws Exception {

        /* init variables */
        this.fileSelection = aFileSelection;
        tool = metadataEditorToolI;
        this.profile = aProfile;
        EditableFile varFile = null;
        if (editableFile == null) {
            varFile = null;
        } else {
            varFile = editableFile;
        }
        fm = new SimpleSingleDocumentManager<EditableFile>(
            varFile,
            this);
        this.callback = aCallback;

        this.editorSheet = aEditorSheet;
        this.template = aTemplate;

        fm.onLoad();
    }

    /* CHECKSTYLE OFF: MethodLength */
    /**
     * rebuilds the whole panel.
     * @throws TransparentToolException the transparent tool exception
     */
    private void rebuildPanel()
        throws TransparentToolException {

        Reporting.logExpected("Re/Building Metadata Editor UI ...");

        replaceView(null);

        /* sort out which view it is */
        final Entity root = profile.getModel(getTool().getContext());

        final EditorViewWrapper[] wrappers =
            getTool()
                .getContext()
                .getServices()
                .<EditorViewWrapper>getServiceProviderList(
                    EditorViewWrapper.class);
        if (wrappers != null) {
            if (editorSheet != null) {
                for (final EditorViewWrapper editorViewWrapper : wrappers) {
                    if (editorSheet.getFileName().endsWith(
                        "."
                            + editorViewWrapper.getFilenameExtension())) {
                        replaceView(editorViewWrapper.instantiateNew(
                            root,
                            profile,
                            getTool().getContext(),
                            editorSheet));
                        break;
                    }
                }
                if (view == null) {
                    Reporting.reportUnexpected(
                        "Did not find appropriate Editor View for %1$s",
                        editorSheet.getFileName());
                    throw new TransparentToolException();
                }
            } else {
                for (final EditorViewWrapper editorViewWrapper : wrappers) {
                    if (editorViewWrapper.isDefault()) {
                        replaceView(editorViewWrapper.instantiateNew(
                            root,
                            profile,
                            getTool().getContext(),
                            editorSheet));
                        break;
                    }
                }
                if (view == null) {
                    Reporting
                        .reportUnexpected("No Default Editor View Installed");
                    throw new TransparentToolException();
                }
            }

            loadTemplate();

            /* load file */
            loadFile();

            view.addObserver(this);

            /* make ui */
            panel = SwingUtils.BorderLayouts.getNew();

            /* get view stuff sorted. */
            viewPanel = view.getEditorPanel();
            panel.add(viewPanel);

            final ToolbarBuilder tBuilder = new ToolbarBuilder(
                getTool().getContext(),
                "Metadata Editing Toolbar",
                false,
                true);
            // {
            tBuilder.getItems().add(
                new ToolbarItem(
                    "New Editor",
                    "images/toolbar.common.newEditor.png",
                    new ClassMethodCallback(
                        this,
                        "onNewEditor")));

            tBuilder.getItems().add(
                new ToolbarItem(
                    "New File",
                    "images/toolbar.common.newDocument.png",
                    new ClassMethodCallback(
                        fm,
                        "onNewFile")));

            tBuilder.getItems().add(
                new ToolbarItem(
                    "Open File",
                    "images/toolbar.common.openDocument.png",
                    new ClassMethodCallback(
                        fm,
                        "onOpenFile")));

            tBuilder.getItems().add(
                new ToolbarItem(
                    "Save File",
                    "images/toolbar.common.saveDocument.png",
                    new ClassMethodCallback(
                        fm,
                        "onSaveFile")));

            tBuilder.getItems().add(
                new ToolbarItem(
                    "Save File As",
                    "images/toolbar.common.saveDocumentAs.png",
                    new ClassMethodCallback(
                        fm,
                        "onSaveFileAs")));

            if (editorSheet != null) {
                tBuilder.getItems().add(
                    null);

                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Load Template",
                        "images/toolbar.common.loadTemplate.png",
                        new ClassMethodCallback(
                            this,
                            "onLoadTemplate")));

                tBuilder.getItems().add(
                    new ToolbarItem(
                        "Save Template",
                        "images/toolbar.common.saveTemplate.png",
                        new ClassMethodCallback(
                            this,
                            "onSaveFileAsTemplate")));
            }
            tBuilder.getItems().add(
                null);
            tBuilder.getItems().add(
                new ToolbarItem(
                    "Validate File",
                    "images/toolbar.common.validateDocument.png",
                    new ClassMethodCallback(
                        this,
                        "validate")));
            tBuilder.getItems().add(
                null);
            tBuilder.getItems().add(
                new ToolbarItem(
                    "Close Editor",
                    "images/toolbar.common.closeEditor.png",
                    new ClassMethodCallback(
                        this,
                        "closeWindow")));
            // }

            final JToolBar toolbar = tBuilder.buildToolbar();

            panel.add(
                toolbar,
                BorderLayout.EAST);

            setChanged();
            notifyObservers();

            Reporting.logExpected("Done!");
        } else {
            Reporting.reportUnexpected("No Editor View Installed");
            throw new TransparentToolException();
        }
    }

    /* CHECKSTYLE ON: MethodLength */

    /**
     * Load template.
     */
    private void loadTemplate() {
        if (template != null) {
            final ByteBuffer contents =
                template.getFileContents(getTool().getContext().getResources());
            if (contents != null) {
                final XMLDataCodec xdc = new XMLDataCodec();
                try {
                    xdc.decodeModelContentsFromXML(
                        view.getRoot(),
                        contents);
                    view.postLoadTemplateCallback();
                } catch (final ContentsCouldNotBeDecodedException e) {
                    Reporting.reportUnexpected(e);
                }
            }
        }
        template = null;
    }

    /**
     * Load file.
     */
    private void loadFile() {
        if (view != null) {
            BusyScreenUtil.startBusy("Loading Document");
            try {
                if (fm.getFile() != null) {
                    Reporting.logExpected("Loading Document");
                    try {
                        codec =
                            getTool()
                                .getContext()
                                .getProfiles()
                                .getDataCodecByFileName(
                                    fm.getFile().getName());
                        try {

                            view.preLoadCallback();

                            final ByteBuffer content =
                                fm.getFile().getContents(
                                    codec);
                            codec.decodeModelContentsFromXML(
                                view.getRoot(),
                                content);
                        } catch (final ContentsCouldNotBeDecodedException e) {
                            Reporting.reportUnexpected(e);
                        }
                        view.postLoadCallback();
                    } finally {
                        Reporting.logExpected("Finished Loading Document");
                    }
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(
                    e,
                    "Error loading document");
            } finally {
                BusyScreenUtil.endBusy();
            }
        }
    }

    /**
     * Replace view.
     * @param newView the new view
     */
    private void replaceView(
        final EditorView newView) {
        if (view != null) {
            view.closeCallback();
        }
        view = newView;
    }

    /**
     * Validate. Called when asked to validate.
     */
    public void validate() {
        BusyScreenUtil.startBusy("Validating Against Schema Constraints");
        try {
            if (getProfile().verifyModelConstraints(
                view.getRoot())) {
                JOptionPaneUtils.showInfoMessageBox(
                    "Schema Constraints Verification",
                    "Passed");
            } else {
                Reporting
                    .reportUnexpected("Schema Constraints Verification Failed");
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
        ByteBuffer metadataContents = null;
        String oldUUid = "";
        // {
        BusyScreenUtil.startBusy("Producing XML Metadata");
        try {
            oldUUid = MetadataModelMetadataUtils.extractUUID(
                view.getRoot(),
                profile);
            MetadataModelMetadataUtils.updateUUID(
                view.getRoot(),
                profile,
                UUID.randomUUID().toString());
            final DataCodec aCodec = new XMLDataCodec();
            metadataContents = aCodec.encodeModelContentsToXML(
                view.getRoot(),
                getProfile().getProfileSchemas());
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
        // }
        // {
        BusyScreenUtil.startBusy("Performing Schematron Validation");
        try {

            String result = getProfile().verifyModelSchematron(
                metadataContents,
                getTool().getContext());
            if (result != null
                && result.trim().length() != 0) {
                result = result.trim();
                JOptionPaneUtils.showInfoMessageBox(
                    "Schematron Verification Results",
                    result);
            } else {
                JOptionPaneUtils.showInfoMessageBox(
                    "Schematron Verification",
                    "Passed");
            }

        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }

        // }
        // {
        BusyScreenUtil.startBusy("Performing UI Validation");
        try {

            view.validateRawMetadata(metadataContents);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
        // }
        // {
        String varUuid = null;
        if (oldUUid != null) {
            varUuid = oldUUid;
        } else {
            varUuid = "";
        }
        MetadataModelMetadataUtils.updateUUID(
            view.getRoot(),
            profile,
            varUuid);
        // }
    }

    /**
     * Called when the close button is pressed.
     */
    public void closeWindow() {
        getTool().getContext().getTools().getInstances().removeToolInstance(
            this);
    }

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     * @param aProfile the profile to set
     */
    public void setProfile(
        final Profile aProfile) {
        this.profile = aProfile;
    }

    /**
     * Gets the view.
     * @return the view
     */
    public EditorView getView() {
        return view;
    }

    /**
     * Sets the view.
     * @param aView the view to set
     */
    public void setView(
        final EditorView aView) {
        this.view = aView;
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
    public String getTitle() {
        if (fm.getFile() == null) {
            return "New";
        } else {
            return fm.getFile().getName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tool getTool() {
        return tool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisposal() {
        if (callback != null) {
            callback.callback(null);
        }
        replaceView(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClose(
        final boolean force) {
        try {
            return fm.onCloseFile(force);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
            return true;
        }
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
    public void onInstantiation() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        final JComponent varEditorPanel = view.getEditorPanel();
        COMPONENT.replace(
            viewPanel,
            varEditorPanel);
        viewPanel = varEditorPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEvent(
        final int event) {
        try {
            switch (event) {
            case ToolInstanceEvents.NEXT_PAGE:
                if (view != null) {
                    view.nextPageCallback();
                }
                break;
            case ToolInstanceEvents.PREVIOUS_PAGE:
                if (view != null) {
                    view.previousPageCallback();
                }
                break;
            case ToolInstanceEvents.SAVE_DOCUMENT:
                fm.onSaveFile();
                break;
            case ToolInstanceEvents.SAVE_DOCUMENT_AS:
                fm.onSaveFileAs();
                break;
            case ToolInstanceEvents.NEW_DOCUMENT:
                fm.onNewFile();
                break;
            case ToolInstanceEvents.OPEN_DOCUMENT:
                fm.onOpenFile();
                break;
            default:
                Reporting.logUnexpected("unhandled event "
                    + event);
                break;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /* == FileEditManagementUnit implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void newFileCallback()
        throws TransparentToolException {
        rebuildPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFileCallback(
        final EditableFile file) {
        Reporting.logExpected("Writing File "
            + file);
        if (view != null) {
            view.preSaveCallback();

            /* new file */
            final String uuid = MetadataModelMetadataUtils.extractUUID(
                view.getRoot(),
                profile);
            if ((uuid == null)
                || (uuid.trim().length() == 0)) {
                Reporting.logExpected("new file...updating UUID");
                MetadataModelMetadataUtils.updateUUID(
                    view.getRoot(),
                    profile,
                    UUID.randomUUID().toString());
            }

            if (codec == null) {
                codec =
                    getTool()
                        .getContext()
                        .getProfiles()
                        .getDataCodecByFileName(
                            file.getName());
            }
            final ByteBuffer xmlBuffer = codec.encodeModelContentsToXML(
                view.getRoot(),
                profile.getProfileSchemas());
            file.setContents(
                xmlBuffer,
                editorSheet,
                codec);
            Reporting.logExpected("Finished Writing File "
                + file);

            view.postSaveCallback();

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAsFileCallback(
        final EditableFile file) {
        codec = null;
        if (view != null) {
            view.preSaveAsCallback();

            Reporting
                .logExpected("saving file as something new ... updating UUID");
            MetadataModelMetadataUtils.updateUUID(
                view.getRoot(),
                profile,
                UUID.randomUUID().toString());

        }
        saveFileCallback(file);
        setChanged();
        notifyObservers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFileCallback(
        final EditableFile file)
        throws TransparentToolException {
        rebuildPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeFileCallback(
        final EditableFile file) {
        /* nothing needs doing in our case */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getSaveFileConfirmation() {
        return JOptionPaneUtils
            .getYesNoConfirmation("Do you want to save metadata?");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCancellableSaveFileConirmation(
        final EditableFile file) {
        return fileSelection.getCancellableSaveConfirmation(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getNewSaveFileCallback() {
        final List<FileFilter> filters = new ArrayList<FileFilter>();
        for (int i = 0; i < getTool()
            .getContext()
            .getProfiles()
            .getDataCodecs()
            .size(); i++) {
            final DataCodec aCodec =
                getTool().getContext().getProfiles().getDataCodecs().get(
                    i);
            filters.addAll(aCodec.getFileFormatFiltersList());
        }
        return fileSelection.getSingleSelectedSaveFile(filters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getReplaceFileCallback(
        final EditableFile file) {
        return fileSelection.getFileReplaceConfirmation(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getNewOpenFileCallback() {
        return fileSelection.getSingleSelectedOpenFile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasModified() {
        return (view != null)
            && view.isModelModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean fileExists(
        final EditableFile file) {
        return file.exists();
    }

    /**
     * On save file as template.
     */
    public void onSaveFileAsTemplate() {
        if (editorSheet != null) {
            String name = JOptionPaneUtils.getUserInputString(
                "<html><b>Please enter a template name"
                    + "<p>Note that if there is only one template,"
                    + " the load template function will load it"
                    + " automatically.",
                "default");
            if (name != null
                && name.trim().length() > 0) {
                name = name.trim().replaceAll(
                    "[^\\p{Alnum}\\p{Blank}]",
                    "_").trim();
                final XMLDataCodec xdc = new XMLDataCodec();
                editorSheet.addProfileTemplateByName(
                    name,
                    xdc.encodeModelContentsToXML(
                        view.getRoot(),
                        profile.getProfileSchemas()),
                    getTool().getContext().getResources());
            }
        }
    }

    /**
     * On new editor.
     */
    public void onNewEditor() {
        if (editorSheet != null) {
            getTool().getContext().getTools().invokeToolByName(
                getTool().getName(),
                "profile",
                getProfile(),
                "editorSheet",
                editorSheet);
        }
    }

    /**
     * On load template.
     */
    public void onLoadTemplate() {
        if (editorSheet != null) {
            template = (new TemplatesSelectionDialog(
                editorSheet,
                getTool().getContext())).showSelectionWindow();
            loadTemplate();
        }
    }

}

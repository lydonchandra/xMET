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
package xmet.tools.metadata.manager.fs;

import java.io.File;

import n.io.bin.Files;
import n.patterns.search.SimpleSearchPatternCompiler;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.manager.FilterableMMGMTPlugin;
import xmet.tools.metadata.manager.FoldersEditableMMGMTPlugin;
import xmet.tools.metadata.manager.MMGMTPlugin;
import xmet.tools.metadata.manager.MetadataEditableMMGMTPlugin;
import xmet.tools.metadata.manager.MetadataFile;

/**
 * MMGMTPlugin for local files and folders.
 * @author Nahid Akbar
 */
public class FilesystemPlugin
    implements
    MMGMTPlugin,
    MetadataEditableMMGMTPlugin,
    FoldersEditableMMGMTPlugin,
    FilterableMMGMTPlugin {

    /** The Constant ID. */
    public static final String ID = "lfs";

    /** The client. */
    private final ClientContext context;

    /** The root. */
    private LocalMetadataFileInterface root = null;

    /** The filtered root. */
    private MetadataFile filteredRoot = null;

    /** The root absolute path. */
    private String rootAbsolutePath;

    /**
     * Gets the settings.
     * @return the settings
     */
    public String getSettings() {
        return rootAbsolutePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Local: "
            + rootAbsolutePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile getRoot() {
        if (filteredRoot != null) {
            return filteredRoot;
        } else {
            if (root != null) {
                return root;
            }
            try {
                Files.ensureParentExists(new File(
                    rootAbsolutePath
                        + "\\test.xml"));
                root = new LocalMetadataFolder(
                    rootAbsolutePath,
                    context);
                return root;
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCloseCallback() {

    }

    /**
     * Instantiates a new filesystem plugin.
     * @param client the client
     * @param absoluteFolderPath the absolute folder path
     */
    public FilesystemPlugin(
        final ClientContext client,
        final String absoluteFolderPath) {
        this.context = client;
        rootAbsolutePath = absoluteFolderPath;
        if (rootAbsolutePath == null) {
            rootAbsolutePath = client.getConfig().getLocalMetadataDirectory();
        }
        Files.ensureParentExists(new File(
            rootAbsolutePath
                + "/blob"));
    }

    /* == MetadataEditableMMGMTPlugin implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getEditableMetadataFile(
        final MetadataFile file) {
        return (LocalMetadataFile) file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditableFile getEditableMetadataFile(
        final MetadataFile parent,
        final String fileName) {
        if (parent instanceof LocalMetadataFolder) {
            ((LocalMetadataFolder) parent).recacheChildren();
            try {
                return new LocalMetadataFile(
                    (LocalMetadataFolder) parent,
                    new File(
                        ((LocalMetadataFolder) parent)
                            .getFolder()
                            .getAbsolutePath()
                            + File.separator
                            + fileName),
                    true,
                    context);
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMetadataFile(
        final MetadataFile metadata) {
        if (metadata instanceof LocalMetadataFile) {
            return ((LocalMetadataFile) metadata).delete();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameMetadataFile(
        final MetadataFile metadata,
        final String newName) {
        if (metadata instanceof LocalMetadataFile) {
            ((LocalMetadataFile) metadata).rename(newName);
        }
    }

    /* == Misc == */

    /* == FoldersEditableMMGMTPlugin implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFolder(
        final MetadataFile folder) {
        if ((folder instanceof LocalMetadataFolder)
            && (folder != root)) {
            ((LocalMetadataFolder) folder).deleteFolder();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameFolder(
        final MetadataFile folder,
        final String newName) {
        if ((folder instanceof LocalMetadataFolder)
            && (folder != root)) {
            ((LocalMetadataFolder) folder).renameFolder(newName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewFolder(
        final MetadataFile parent,
        final String newFolderName) {
        if (parent instanceof LocalMetadataFolder) {
            ((LocalMetadataFolder) parent).createSubfolder(newFolderName);
            /* root = null; */
            /* filteredRoot = null; */
        }
    }

    /* == FilterableMMGMTPlugin implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean filterMetadata(
        final String filterText,
        final boolean caseSensitive) {
        resetMetadataFilter();
        String varFilterText = filterText;
        if (varFilterText == null
            || varFilterText.trim().length() == 0) {
            return false;
        } else {
            varFilterText = varFilterText.trim();
            /* clone root */
            filteredRoot = ((LocalMetadataFileInterface) getRoot()).copy();

            /* compile filtering patterns */
            final SimpleSearchPatternCompiler sspc =
                new SimpleSearchPatternCompiler(
                    varFilterText,
                    !caseSensitive,
                    true);

            /* filter */
            filteredRoot =
                ((LocalMetadataFileInterface) filteredRoot).filter(sspc);
            return filteredRoot != null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetMetadataFilter() {
        filteredRoot = null;
    }

}

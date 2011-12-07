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
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import n.algorithm.ArrayUtil;
import n.io.bin.Files;
import n.patterns.search.SimpleSearchPatternCompiler;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.codecs.DataCodec;
import xmet.tools.metadata.manager.MetadataFile;
import xmet.utils.BusyScreenUtil;

/**
 * Representa a local metadata folder.
 * @author Nahid Akbar
 */
public class LocalMetadataFolder
    implements
    LocalMetadataFileInterface {

    /** The parent. */
    private LocalMetadataFolder parent;

    /** The folder. */
    private File folder;

    /** The children. */
    private MetadataFile[] children = null;

    /** The context. */
    private ClientContext context;

    /* == Constructors == */

    /**
     * Instantiates a new local metadata folder.
     * @param localMetadataDirectory the local metadata directory
     * @param aContext TODO
     * @throws FileNotFoundException the file not found exception
     * @throws NotAFolderException the not a folder exception
     */
    public LocalMetadataFolder(
        final String localMetadataDirectory,
        final ClientContext aContext)
        throws FileNotFoundException, NotAFolderException {
        this(null, new File(
            localMetadataDirectory), aContext);
    }

    /**
     * Instantiates a new local metadata folder.
     * @param aParent the parent
     * @param aFolder the folder
     * @param aContext TODO
     * @throws FileNotFoundException the file not found exception
     * @throws NotAFolderException the not a folder exception
     */
    public LocalMetadataFolder(
        final LocalMetadataFolder aParent,
        final File aFolder,
        final ClientContext aContext)
        throws FileNotFoundException, NotAFolderException {
        this.folder = aFolder;
        this.parent = aParent;
        this.context = aContext;
        if (aFolder == null) {
            throw new FileNotFoundException();
        }
        if (!aFolder.exists()) {
            throw new FileNotFoundException(
                aFolder.getAbsolutePath());
        }
        if (!aFolder.isDirectory()) {
            throw new NotAFolderException(
                aFolder.getAbsolutePath());
        }
    }

    /* == Folder == */

    /**
     * Gets the folder.
     * @return the folder
     */
    public File getFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     * @param aFolder the new folder
     */
    public void setFolder(
        final File aFolder) {
        this.folder = aFolder;
    }

    /**
     * Creates the subfolder.
     * @param subfolderName the subfolder name
     * @return true, if successful
     */
    public boolean createSubfolder(
        final String subfolderName) {
        boolean success = false;
        final File file = new File(
            folder.getAbsolutePath()
                + File.separator
                + subfolderName);
        success = file.mkdir();
        if (success) {
            try {
                children = ArrayUtil.extend(
                    children,
                    new LocalMetadataFolder(
                        this,
                        file,
                        context));
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        return success;
    }

    /**
     * Rename folder.
     * @param newName the new name
     * @return true, if successful
     */
    public boolean renameFolder(
        final String newName) {
        boolean success = false;
        try {
            final String newName2 = folder.getParent()
                + File.separator
                + newName;
            success = folder.renameTo(new File(
                newName2));
            if (success) {
                folder = new File(
                    newName2);
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        return success;
    }

    /**
     * Delete folder.
     */
    public void deleteFolder() {
        Files.recursivelyDeleteFolder(folder);
        if (parent != null) {
            parent.childrenRemoved(this);
        }
    }

    /**
     * Children removed.
     * @param localMetadataFolder the local metadata folder
     */
    private void childrenRemoved(
        final LocalMetadataFolder localMetadataFolder) {
        if (ArrayUtil.indexOf(
            children,
            localMetadataFolder) != -1) {
            children = ArrayUtil.removeItem(
                children,
                localMetadataFolder);
        }
    }

    /* == Children == */
    /**
     * Recache children.
     */
    public void recacheChildren() {
        children = null;
    }

    /**
     * Cache children.
     */
    private void cacheChildren() {
        BusyScreenUtil.startBusy("Caching Files Tree");
        try {
            if (children == null) {
                final File[] list = folder.listFiles();
                if (list != null) {
                    final ArrayList<MetadataFile> cached =
                        new ArrayList<MetadataFile>();
                    for (final File file : list) {
                        BusyScreenUtil.tickBusy();
                        try {
                            if (file.isFile()
                                && context.getProfiles().isSupportedDataFormat(
                                    file.getName())) {
                                cached.add(new LocalMetadataFile(
                                    this,
                                    file,
                                    context));
                            } else if (file.isDirectory()
                                && !file.getName().startsWith(
                                    ".")) {
                                cached.add(new LocalMetadataFolder(
                                    this,
                                    file,
                                    context));
                            }
                        } catch (final RuntimeException e) {
                            Reporting.reportUnexpected(e);
                        } catch (FileNotFoundException e) {
                            Reporting.reportUnexpected(e);
                        } catch (NotAFileException e) {
                            Reporting.reportUnexpected(e);
                        } catch (NotAFolderException e) {
                            Reporting.reportUnexpected(e);
                        }
                    }
                    children = cached.toArray(new MetadataFile[cached.size()]);
                }
            }
        } catch (final RuntimeException e) {
            e.printStackTrace();
        } finally {
            BusyScreenUtil.endBusy();
        }
    }

    /* == MetadataFile Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return folder.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFolder() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile[] getChildren() {
        cacheChildren();
        return children.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getPreviewContents(
        final DataCodec dodec) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEditorSheetName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbstract() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUUID() {
        return null;
    }

    /* == LocalMetadataFileInterface implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public LocalMetadataFileInterface copy() {
        return new LocalMetadataFolder(
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(
        final LocalMetadataFolder aParent) {
        this.parent = aParent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalMetadataFileInterface filter(
        final SimpleSearchPatternCompiler patterns) {
        final ArrayList<LocalMetadataFileInterface> childrenList =
            new ArrayList<LocalMetadataFileInterface>();
        for (int i = 0; i < children.length; i++) {
            children[i] =
                ((LocalMetadataFileInterface) children[i]).filter(patterns);
            if (children[i] != null) {
                childrenList.add((LocalMetadataFileInterface) children[i]);
            }
        }
        if (childrenList.size() > 0) {
            try {
                children =
                    childrenList
                        .toArray(new LocalMetadataFileInterface[childrenList
                            .size()]);
            } catch (final Exception e) {
                children = ArrayUtil.arrayFromList(childrenList);
                Reporting.reportUnexpected();
            }
            return this;
        }
        return null;
    }

    /**
     * Clone constructor...use others.
     * @param lmf the lmf
     */
    protected LocalMetadataFolder(
        final LocalMetadataFolder lmf) {
        super();
        parent = lmf.parent;
        folder = lmf.folder;
        children = lmf.getChildren().clone();
        for (int i = 0; i < children.length; i++) {
            children[i] = ((LocalMetadataFileInterface) children[i]).copy();
            ((LocalMetadataFileInterface) children[i]).setParent(this);
        }
    }

}

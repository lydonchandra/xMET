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
import java.util.List;

import n.io.bin.Files;
import n.io.net.CharacterSetUtils;
import n.io.xml.JDOMXmlUtils;
import n.patterns.search.SimpleSearchPatternCompiler;
import n.reporting.Reporting;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ContentFilter;

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.utils.MetadataMetadataUtils;
import xmet.tools.metadata.editor.EditableFile;
import xmet.tools.metadata.manager.MetadataFile;

/**
 * Represents a local metadata file.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class LocalMetadataFile
    implements
    LocalMetadataFileInterface,
    EditableFile {

    /** The file. */
    private File file;

    /** The parent. */
    private LocalMetadataFolder parent;

    /** The metadata. */
    private LocalMetadataFileMetadata metadata = null;

    /** The context. */
    private ClientContext context;

    /**
     * Instantiates a new local metadata file.
     * @param aParent the parent
     * @param aFile the file
     * @param aContext TODO
     * @throws NotAFileException the not a file exception
     * @throws FileNotFoundException the file not found exception
     */
    public LocalMetadataFile(
        final LocalMetadataFolder aParent,
        final File aFile,
        final ClientContext aContext)
        throws NotAFileException, FileNotFoundException {
        this(aParent, aFile, false, aContext);
    }

    /**
     * Instantiates a new local metadata file.
     * @param aParent the parent
     * @param aFile the file
     * @param possiblyAbstract the possibly abstract
     * @param aContext TODO
     * @throws NotAFileException the not a file exception
     * @throws FileNotFoundException the file not found exception
     */
    public LocalMetadataFile(
        final LocalMetadataFolder aParent,
        final File aFile,
        final boolean possiblyAbstract,
        final ClientContext aContext)
        throws NotAFileException, FileNotFoundException {
        this.context = aContext;
        this.file = aFile;
        this.parent = aParent;
        if (aFile == null) {
            throw new FileNotFoundException();
        }
        if (!aFile.exists()
            && !possiblyAbstract) {
            throw new FileNotFoundException(
                aFile.getAbsolutePath());
        }
        if (aFile.isDirectory()) {
            throw new NotAFileException(
                aFile.getAbsolutePath());
        }
        /* cacheMetadata(file, possiblyAbstract); */
    }

    /* == MetadataFile Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return file.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFolder() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile[] getChildren() {
        return new MetadataFile[0];
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
        final DataCodec codec) {
        final ByteBuffer contents = getContents(codec);
        extractMetadata(contents);
        return contents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileName() {
        if (metadata == null) {
            extractMetadata(getPreviewContents(context
                .getProfiles()
                .getDataCodecByFileName(
                    file.getName())));
        }
        if (metadata != null) {
            return metadata.getProfileName();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEditorSheetName() {
        if (metadata == null) {
            extractMetadata(getPreviewContents(context
                .getProfiles()
                .getDataCodecByFileName(
                    file.getName())));
        }
        if (metadata != null) {
            return metadata.getEditorSheetName();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        /* cacheMetadata(file, false); */
        if (metadata != null) {
            return metadata.getTitleString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbstract() {
        /* cacheMetadata(file, false); */
        if (metadata != null) {
            return metadata.getAbstractString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUUID() {
        if (metadata == null) {
            extractMetadata(getPreviewContents(context
                .getProfiles()
                .getDataCodecByFileName(
                    file.getName())));
        }
        if (metadata != null) {
            return metadata.getUuidString();
        }
        return null;
    }

    /* == EditableFile Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getContents(
        final DataCodec codec) {
        return codec.decodeFileContents(Files.read(file));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContents(
        final ByteBuffer xmlContent,
        final ProfileEditorSheet editorSheet,
        final DataCodec codec) {
        if (editorSheet != null) {
            final ByteBuffer newBuffer = insertMetaMetadata(
                xmlContent,
                editorSheet,
                codec);
            if (newBuffer != null) {
                Files.write(
                    file,
                    codec.encodeFileContents(newBuffer));
            } else {
                Reporting.reportUnexpected("Failed at inserting metadata");
                Files.write(
                    file,
                    codec.encodeFileContents(xmlContent));
            }
        } else {
            Files.write(
                file,
                codec.encodeFileContents(xmlContent));
        }
    }

    /**
     * Delete.
     * @return True if deletion was successful.
     */
    public boolean delete() {
        final boolean success = file.delete();
        if (success) {
            parent.recacheChildren();
        }
        return success;
    }

    /**
     * Rename.
     * @param newName the new name
     */
    public void rename(
        final String newName) {
        try {
            final String newName2 = file.getParent()
                + File.separator
                + newName;
            if (file.renameTo(new File(
                newName2))) {
                /* dsfsdf */
                Reporting.logExpected(
                    "File renamed to %1$s",
                    getName());
            }
            file = new File(
                newName2);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists() {
        return file.exists();
    }

    /* == LocalMetadataFileInterface Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public LocalMetadataFileInterface copy() {
        return new LocalMetadataFile(
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
        if ((getName() != null)
            && patterns.anyMatches(getName())) {
            return this;
        }
        // }
        return null;
    }

    /**
     * Instantiates a new local metadata file.
     * @param lmf the lmf
     */
    protected LocalMetadataFile(
        final LocalMetadataFile lmf) {
        parent = lmf.parent;
        file = lmf.file;
        metadata = lmf.metadata;
    }

    /* == Metadata == */

    /** The Constant XMET_META_PREFIX. */
    private static final String XMET_META_PREFIX = "XMET-META-";

    /** The Constant XMET_META_POSTFIX. */
    private static final String XMET_META_POSTFIX = ": ";

    /** The Constant XMET_META_PROFILE. */
    private static final String XMET_META_PROFILE = XMET_META_PREFIX
        + "PROFILE"
        + XMET_META_POSTFIX;

    /** The Constant XMET_META_EDITOR. */
    private static final String XMET_META_EDITOR = XMET_META_PREFIX
        + "EDITOR"
        + XMET_META_POSTFIX;

    /** The Constant XMET_META_TITLE. */
    private static final String XMET_META_TITLE = XMET_META_PREFIX
        + "TITLE"
        + XMET_META_POSTFIX;

    /** The Constant XMET_META_ABSTRACT. */
    private static final String XMET_META_ABSTRACT = XMET_META_PREFIX
        + "ABSTRACT"
        + XMET_META_POSTFIX;

    /** The Constant XMET_META_UUID. */
    private static final String XMET_META_UUID = XMET_META_PREFIX
        + "UUID"
        + XMET_META_POSTFIX;

    /**
     * Extract metadata.
     * @param xmlBuffer the xml buffer
     */
    private synchronized void extractMetadata(
        final ByteBuffer xmlBuffer) {
        try {
            if ((metadata == null)
                && (xmlBuffer != null)) {
                String title = null;
                String abstractE = null;
                String uuid = null;
                String profile = null;
                String sheet = null;
                final Element element =
                    JDOMXmlUtils.elementFromXml(CharacterSetUtils.decodeBuffer(
                        xmlBuffer,
                        "UTF-8"));
                if (element != null) {
                    for (final Object item : element
                        .getContent(new ContentFilter(
                            ContentFilter.COMMENT))) {
                        final Comment comment = (Comment) item;
                        String text;
                        text = comment.getText().trim();
                        if ((text).startsWith(XMET_META_PREFIX)) {
                            if (text.startsWith(XMET_META_PROFILE)) {
                                profile = text.substring(
                                    XMET_META_PROFILE.length()).trim();
                            } else if (text.startsWith(XMET_META_EDITOR)) {
                                sheet = text.substring(
                                    XMET_META_EDITOR.length()).trim();
                            } else if (text.startsWith(XMET_META_TITLE)) {
                                title = text.substring(
                                    XMET_META_TITLE.length()).trim();
                            } else if (text.startsWith(XMET_META_ABSTRACT)) {
                                abstractE = text.substring(
                                    XMET_META_ABSTRACT.length()).trim();
                            } else if (text.startsWith(XMET_META_UUID)) {
                                uuid = text.substring(
                                    XMET_META_UUID.length()).trim();
                            } else {
                                Reporting.logExpected(
                                    "UNHANDLED META-METADATA %1$s",
                                    text);
                            }
                        }
                    }
                } else {
                    Reporting.logExpected(
                        "%1$s",
                        new String(
                            xmlBuffer.array()));
                }
                metadata = new LocalMetadataFileMetadata(
                    title,
                    abstractE,
                    uuid,
                    profile,
                    sheet);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert meta metadata.
     * @param buffer the buffer
     * @param editorSheet the editor sheet
     * @param codec the codec
     * @return the byte buffer
     */
    private synchronized ByteBuffer insertMetaMetadata(
        final ByteBuffer buffer,
        final ProfileEditorSheet editorSheet,
        final DataCodec codec) {
        if (editorSheet != null) {
            final Profile profile = editorSheet.getProfile();
            final Element element = JDOMXmlUtils.elementFromXml(new String(
                buffer.array()));
            if (element != null) {
                /* remove all xmet meta tags */
                final List contents = element.getContent(new ContentFilter(
                    ContentFilter.COMMENT));
                for (int i = 0; i < contents.size(); i++) {
                    final Comment comment = (Comment) contents.get(i);
                    if (comment.getText().trim().startsWith(
                        XMET_META_PREFIX)) {
                        comment.detach();
                        i--;
                    }
                }
                /* put in new ones */
                element.addContent(
                    0,
                    new Comment(
                        XMET_META_PROFILE
                            + profile.getID()));
                element.addContent(
                    1,
                    new Comment(
                        XMET_META_EDITOR
                            + editorSheet.getName()));
                final String title = MetadataMetadataUtils.extractTitle(
                    element,
                    profile);
                if (title != null) {
                    element.addContent(
                        2,
                        new Comment(
                            XMET_META_TITLE
                                + title));
                }
                final String abstractE = MetadataMetadataUtils.extractAbstract(
                    element,
                    profile);
                if (abstractE != null) {
                    element.addContent(
                        2,
                        new Comment(
                            XMET_META_ABSTRACT
                                + abstractE));
                }
                final String uuid = MetadataMetadataUtils.extractUUID(
                    element,
                    profile);
                if (uuid != null) {
                    element.addContent(
                        2,
                        new Comment(
                            XMET_META_UUID
                                + uuid));
                }
                final Document document = new Document(
                    element);
                final String encoded = JDOMXmlUtils.xmlFromDocument(document);
                metadata = new LocalMetadataFileMetadata(
                    title,
                    abstractE,
                    uuid,
                    profile.getID(),
                    editorSheet.getName());
                return ByteBuffer.wrap(encoded.getBytes());
            } else {
                return buffer;
            }
        }
        return null;
    }

}

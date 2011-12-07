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
package xmet.resources;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import n.io.bin.Files;
import n.io.patterns.IncludedFolderStack;
import n.reporting.Reporting;

/**
 * Proxy for accessing local resources through. <br />
 * All file system and inside jar resources should be accessed through this
 * interface through relative paths. This allows some extra functionalities on
 * top of basic resource finding service such as resource overriding by folder
 * priorities, image loading and resizing etc.
 * @author Nahid Akbar
 */
public final class ResourceManager {

    /* == Folder Stack Stuff == */
    /**
     * relativeIncludePaths are folders where resources are searched. For
     * example images/default.png could be located in images/default.png or in
     * xmet/client/resources/images/default.png . Provides a mechanism for
     * searching those local folders one by one for local file resources and
     * provides the flexibility of having the resource in any of the possible
     * folders, including inside the self packed jar file.<br />
     * make sure there are no leading or trailing "/".
     */
    private static String[] relativeIncludePaths = {
        "",
        "client",
        "xmet/resources",
        "extra/xmet/resources",
        "core/xmet/client/resources"
    };

    /**
     * Adds a list of paths to the relativeIncludePaths list (at the top of the
     * stack). This should be done before client initialization (i.e. before
     * calling Client.main)
     * @param pathsList the relative include paths
     */
    public static void addRelativeIncludePaths(
        final String[] pathsList) {
        final ArrayList<String> al = new ArrayList<String>(
            Arrays
                .asList(pathsList));
        al
            .addAll(Arrays
                .asList(ResourceManager.relativeIncludePaths));
        ResourceManager.relativeIncludePaths = al
            .toArray(new String[al
                .size()]);
    }

    /* == Methods to be used == */

    /**
     * Gets a list of possible folders given a relative folder path e.g.
     * getFoldersList("profiles/anzlic/presentation");
     * @param relativeFolderPath the relative folder path
     * @return list of folders that match the relative folder path
     */
    public File[] getFoldersList(
        final String relativeFolderPath) {
        return localFolders
            .getFoldersList(relativeFolderPath);
    }

    /**
     * Gets a list of files given a relative folder path e.g.
     * getFoldersList("profiles/anzlic/presentation/default.xml"); Note: does
     * not return folders
     * @param relativeFilePath relative file path
     * @return the files list
     */
    public File[] getFilesList(
        final String relativeFilePath) {
        return localFolders
            .getFilesList(relativeFilePath);
    }

    /**
     * Gets a list of files in the given relative folder path that passes
     * through the specified file filter.
     * @param relativeFolderPath relative folder path name
     * @param filter the filter
     * @return the files list
     */
    public File[] getFilesList(
        final String relativeFolderPath,
        final FileFilter filter) {
        return IncludedFolderStack
            .filterFilesListInFolders(
                localFolders,
                relativeFolderPath,
                filter);
    }

    /**
     * Gets an image resource by the relative file name.
     * @param relativeFilePath relative file path of the image resource
     * @return the image resource
     */
    public Image getImageResource(
        final String relativeFilePath) {
        Image ret = null;
        try {
            ret = ImageIO
                .read(new ByteArrayInputStream(
                    getResourceContents(
                        relativeFilePath)
                        .array()));
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(
                    e,
                    "icon not found (%1$s)",
                    relativeFilePath);
        }
        if (ret == null) {
            try {
                ret = ImageIO
                    .read(new ByteArrayInputStream(
                        getResourceContents(
                            "images/default.png")
                            .array()));
            } catch (final IOException e) {
                Reporting
                    .reportUnexpected(e);
            }
        }
        return ret;
    }

    /**
     * Gets an image icon resource by the relative file name.
     * @param relativeFilePath relative file path of the image resource
     * @return the image icon resource
     */
    public ImageIcon getImageIconResource(
        final String relativeFilePath) {
        ImageIcon ret = null;
        try {
            ret = new ImageIcon(
                getResourceContents(
                    relativeFilePath)
                    .array());
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(
                    e,
                    "icon not found (%1$s)",
                    relativeFilePath);
        }
        if (ret == null) {
            ret = new ImageIcon(
                getResourceContents(
                    "images/default.png")
                    .array());
        }
        return ret;
    }

    /**
     * Gets an image icon resource by the relative file name and also resizes it
     * to the specified size.
     * @param relativeFilePath the relative file path of the resource
     * @param newWidth the new width
     * @param newHeight the new height
     * @return the image icon resource resize
     */
    public ImageIcon getImageIconResourceResize(
        final String relativeFilePath,
        final int newWidth,
        final int newHeight) {
        final ImageIcon ii = getImageIconResource(relativeFilePath);
        /* http://www.coderanch.com/t/331731/GUI/java/Resize-ImageIcon */
        final Image img = ii
            .getImage();
        final BufferedImage bi = new BufferedImage(
            newWidth,
            newHeight,
            BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = bi
            .createGraphics();
        g
            .setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g
            .setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g
            .setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g
            .drawImage(
                img,
                0,
                0,
                newWidth,
                newHeight,
                null,
                null);
        // --------------------------------
        return new ImageIcon(
            bi);
    }

    /**
     * Gets the resource URI Given a relative path.
     * @param relativePath the relative path
     * @return the resource uri
     */
    public URI getResourceURI(
        final String relativePath) {
        try {
            try {
                /* see if it is already an URI */
                if (relativePath
                    .contains("://")) {
                    return new URI(
                        relativePath);
                }
            } catch (final URISyntaxException e) {
                e
                    .printStackTrace();
            }
            /* search and return a local file */
            return localFolders
                .getFileFromRelativePath(
                    relativePath)
                .toURI();
        } catch (final Exception e) {
            Reporting
                .logExpected(
                    "Could not get URI of %1$s",
                    relativePath);
            return null;
        }
    }

    /**
     * Gets the contents of the resource given a relative path.
     * @param relativeFilePath the relative file path of the resource
     * @return the resource contents
     */
    public ByteBuffer getResourceContents(
        final String relativeFilePath) {
        ByteBuffer returnFile = null;
        if (returnFile == null) {
            returnFile = loadLocalFile(relativeFilePath);
        }
        if (returnFile == null) {
            try {
                returnFile = getInJarFileContents(relativeFilePath);
            } catch (final Exception e) {
                Reporting
                    .reportUnexpected(e);
            }
        }
        /* if (returnFile == null) { */
        /* returnFile = loadLocalFile(relativeFilePath); */
        // }
        return returnFile;
    }

    /**
     * Sets the contents of the resource specified by relative file path.
     * @param relativeFilePath the relative file path
     * @param contents the contents
     */
    public void setResourceContents(
        final String relativeFilePath,
        final ByteBuffer contents) {
        saveLocalFile(
            relativeFilePath,
            contents);
    }

    /* == Misc == */

    /** The local folders stack. */
    private final IncludedFolderStack localFolders;

    /**
     * Constructor.
     * @param baseDir the base directory absolute path of the application
     */
    public ResourceManager(
        final String baseDir) {
        localFolders = new IncludedFolderStack();
        final String path = new File(
            baseDir)
            .getAbsolutePath();
        for (final String folder : relativeIncludePaths) {
            localFolders
                .foldersAdd(path
                    + "/"
                    + folder);
        }
    }

    /* == Helper methods for resources outside jar == */

    /**
     * helper method that loads a local physical file given a relative path.
     * @param relativeFilePath the relative file path
     * @return contents of the file
     */
    private ByteBuffer loadLocalFile(
        final String relativeFilePath) {
        ByteBuffer returnContents = null;
        if (relativeFilePath != null) {
            File f = localFolders
                .getFileFromRelativePath(relativeFilePath);
            /* Reporting.log("File %1$s", f); */
            if (f != null) {
                returnContents = Files
                    .read(f);
            }
            if (returnContents == null) { /* absolute file */
                f = new File(
                    relativeFilePath);
                if (f
                    .exists()) {
                    returnContents = Files
                        .read(f);
                }
            }
        } else {
            Reporting
                .logUnexpected(
                    "Resource %1$s not found",
                    (Object) relativeFilePath);
        }
        return returnContents;
    }

    /**
     * helper method that sets the contents of a local file.
     * @param relativeFilePath the relative file path
     * @param contents the contents to write
     */
    private void saveLocalFile(
        final String relativeFilePath,
        final ByteBuffer contents) {
        File f = localFolders
            .getFileFromRelativePath(relativeFilePath);
        Reporting
            .logExpected(
                "Writing to file %1$s",
                f);
        if (f == null) {
            String folderName = relativeFilePath;
            int i = folderName
                .lastIndexOf('\\');
            if (i == -1) {
                i = folderName
                    .lastIndexOf('/');
            }
            if (i != -1) {
                folderName = folderName
                    .substring(
                        0,
                        i);
                ensureParentsExist(folderName);
                final String fileName = relativeFilePath
                    .substring(i);
                Reporting
                    .logExpected(folderName);
                final File[] folders = getFoldersList(folderName);
                for (final File folder : folders) {
                    f = new File(
                        folder
                            .getAbsolutePath()
                            + fileName);
                    break;
                }
            }

        }
        if (f != null) {
            Files
                .write(
                    f,
                    contents);
        }
    }

    /**
     * given a relative path, this helper method ensures that all the folder
     * between the end file and the root folder exists.
     * @param relativeFilePath the relative url
     */
    private void ensureParentsExist(
        final String relativeFilePath) {
        String folderName = relativeFilePath;
        int i = folderName
            .lastIndexOf('\\');
        if (i == -1) {
            i = folderName
                .lastIndexOf('/');
        }
        if (i != -1) {
            folderName = folderName
                .substring(
                    0,
                    i);
            final String fileName = relativeFilePath
                .substring(i);
            Reporting
                .logExpected(folderName);
            File[] folders = getFoldersList(folderName);
            if (folders.length == 0) {
                ensureParentsExist(folderName);
            }
            folders = getFoldersList(folderName);
            for (final File folder : folders) {
                final File f = new File(
                    folder
                        .getAbsolutePath()
                        + fileName);
                if (!f
                    .exists()) {
                    if (!f
                        .mkdir()) {
                        Reporting
                            .reportUnexpected(
                                "Directory Creation Failed: %1$s",
                                f
                                    .getAbsoluteFile());
                    }
                }
                break;
            }
        }

    }

    /* == Helper methods for resources inside jar == */
    /**
     * This helper method gets contents of a file packed inside the jar.
     * @param relativeFilePath the relative file path of the resource
     * @return the in jar file contents
     */
    private ByteBuffer getInJarFileContents(
        final String relativeFilePath) {
        ByteBuffer contents = null;
        for (final String folder : relativeIncludePaths) {
            if (contents == null) {
                contents = loadJarResource("/"
                    + folder
                    + "/"
                    + relativeFilePath);
            }
        }
        return contents;
    }

    /**
     * Helpet method that gets the contents of the file of a resource inside a
     * jar file given its absolute path inside the jar.
     * @param jarAbsolutePath the jar relative url
     * @return contents of pointed resource if exists, null otherwise
     */
    private ByteBuffer loadJarResource(
        final String jarAbsolutePath) {
        ByteBuffer returnFile = null;
        InputStream is = (this
            .getClass()
            .getResourceAsStream(jarAbsolutePath));
        if (is != null) {
            try {
                int totalSize = 0;
                // is.mark(Integer.MAX_VALUE);
                try {
                    while (is
                        .available() > 0) {
                        totalSize += is
                            .skip(is
                                .available());
                    }
                } catch (final IOException e) {
                    Reporting
                        .reportUnexpected(e);
                } finally {
                    try {
                        is
                            .close();
                    } catch (IOException e) {
                        Reporting
                            .reportUnexpected(e);
                    }
                }
                final byte[] contents = new byte[totalSize];
                try {
                    is = (this
                        .getClass()
                        .getResourceAsStream(jarAbsolutePath));
                    totalSize = 0;
                    /*
                     * Modified code to re-use stream by marking the stream
                     * position previously
                     */
                    // is.reset();
                    while (is
                        .available() > 0) {
                        final int read = is
                            .read(
                                contents,
                                totalSize,
                                is
                                    .available());
                        totalSize += read;
                    }
                } catch (final IOException e) {
                    Reporting
                        .reportUnexpected(e);
                } finally {
                    try {
                        is
                            .close();
                    } catch (IOException e) {
                        Reporting
                            .reportUnexpected(e);
                    }
                }
                returnFile = ByteBuffer
                    .wrap(contents);
            } catch (final RuntimeException e) {
                Reporting
                    .reportUnexpected(e);
            }
        }
        return returnFile;
    }

}

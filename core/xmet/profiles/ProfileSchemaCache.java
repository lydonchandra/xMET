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
package xmet.profiles;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

import n.io.bin.Files;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;

import xmet.ClientContext;

/**
 * A cache of all the cached profile schemas.
 * <p>
 * Implemented as a map from schema URLs to a local schema files.
 * </p>
 * <h2>How to configure schemas for schema cache</h2>
 * <p>
 * Its pretty much self explanatory. The cached schemas are in
 * client/profiles/default/schemas folder. The cache.xml is the configuration
 * file. The application will look for all the cache.xml files inside all the
 * profiles/default/schemas folders it finds in its resource include paths and
 * map the schemas accorting to the cache.xml configuration file.
 * </p>
 * <h2>cache.xml format</h2>
 * <pre>
 * &lt;schemaLocations&gt;
 *  &lt;schemaLocation&gt;
 *      &lt;schameURL&gt;<b>URL</b>&lt;/schameURL&gt;
 *      &lt;schemaFile&gt;<b>PATH</b>&gt; &lt;/schemaLocation&gt;
 * &lt;/schemaLocations&gt;
 * </pre>
 * <p>
 * The xml file is just a "schemaLocations" xml element with multiple
 * schemaLocation declarations. There are two ways of implementing this.
 * Firstly, mapping a single schema file. In this option, put the url of the
 * schema file in the URL position and the relative file path to the schema file
 * in the PATH position. The second way involves mapping a schema folder to a
 * local schema folder. With this way, xMET recursively lists the local folder
 * for files and assumes that the remote location mirrors it. For this, enter
 * the relative url and the relative path of the folder in the appropriate
 * places. See the existing cache.xml for examples of both approaches.
 * </p>
 * @author Nahid Akbar
 */
public class ProfileSchemaCache
    extends
    HashMap<String, String> {

    /* == Constructor override == */

    /**
     * Instantiates a new profile schema cache.
     * @param context the context
     * @param profileDirectory the profile directory
     */
    public ProfileSchemaCache(
        final ClientContext context,
        final String profileDirectory) {
        super();
        localDefaultSchemaLocations(
            context,
            profileDirectory);
    }

    /**
     * For debugging cached schema locations only. {@inheritDoc}
     */
    @Override
    public String put(
        final String key,
        final String value) {
        Reporting.logExpected(
            "schema %1$s - %2$s",
            key,
            value);
        return super.put(
            key,
            value);
    }

    /* == Internal helper methods == */
    /**
     * Local default schema locations.
     * @param context the context
     * @param profileDirectory the profile directory
     */
    @SuppressWarnings("rawtypes")
    private void localDefaultSchemaLocations(
        final ClientContext context,
        final String profileDirectory) {
        // {
        final String schemasDirectory = profileDirectory
            + "/default/schemas";
        final File[] schemasFolders = context.getResources().getFoldersList(
            schemasDirectory);
        if (schemasFolders != null
            && schemasFolders.length > 0) {
            for (final File schemasFolder : schemasFolders) {

                try {
                    final ByteBuffer contents =
                        Files.read(schemasFolder.getPath()
                            + "/cache.xml");
                    if (contents != null) {
                        final Element rootElement =
                            JDOMXmlUtils.elementFromXml(new String(contents
                                .array()));
                        final List schemaLocations =
                            rootElement.getChildren("schemaLocation");
                        for (final Object object : schemaLocations) {
                            final Element schemaLocation = (Element) object;
                            final String specifiedURL =
                                schemaLocation.getChildTextTrim("schameURL");
                            final String specifiedPath =
                                schemaLocation.getChildTextTrim("schemaFile");
                            if (specifiedURL != null
                                && specifiedURL.length() > 0
                                && specifiedPath != null
                                && specifiedPath.length() > 0) {
                                addSchemas(
                                    schemasFolder,
                                    specifiedURL,
                                    specifiedPath);
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // }
    }

    /**
     * Adds the schemas.
     * @param schemasFolder the schemas folder
     * @param specifiedURL the specified url
     * @param specifiedPath the specified path
     */
    private void addSchemas(
        final File schemasFolder,
        final String specifiedURL,
        final String specifiedPath) {
        if (specifiedURL.endsWith("/")
            && specifiedPath.endsWith("/")) {
            resursiveAddSchema(
                schemasFolder,
                specifiedURL,
                specifiedPath);
        } else {
            put(
                specifiedURL,
                schemasFolder.getAbsolutePath()
                    + "/"
                    + specifiedPath);
        }
    }

    /**
     * Resursive add schema.
     * @param schemasFolder the schemas folder
     * @param specifiedURL the specified url
     * @param specifiedPath the specified path
     */
    private void resursiveAddSchema(
        final File schemasFolder,
        final String specifiedURL,
        final String specifiedPath) {
        final File folder = new File(schemasFolder.getAbsolutePath()
            + "/"
            + specifiedPath);
        if (folder.exists()
            && folder.isDirectory()) {
            final String[] files = folder.list();
            for (final String file : files) {
                if (!file.startsWith(".")) {
                    final File fileFile = new File(folder.getPath()
                        + "/"
                        + file);
                    if (fileFile.isFile()) {
                        put(
                            specifiedURL
                                + file,
                            fileFile.getAbsolutePath());
                    } else {
                        resursiveAddSchema(
                            schemasFolder,
                            specifiedURL
                                + file
                                + "/",
                            specifiedPath
                                + file
                                + "/");
                    }
                }
            }

        }
    }

    /* == Serializable == */

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

}

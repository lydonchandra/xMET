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
package xmet.profiles.codecs.impl;

import java.io.File;
import java.io.IOException;

import n.reporting.Reporting;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import xmet.ClientContext;
import xmet.profiles.ProfileSchemaCache;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Class Simply Loads XSDs schema.
 * @author Nahid Akbar
 */
public final class SchemaParser {

    /**
     * Instantiates a new schema parser.
     */
    private SchemaParser() {

    }

    /**
     * Parses the schema.
     * @param file the file
     * @param context the context
     * @return the xS schema set
     */
    public static XSSchemaSet parseSchema(
        final File file,
        final ClientContext context) {
        try {
            XSSchemaSet schemaSet;
            final XSOMParser parser = new XSOMParser();
            if (context != null
                && context.getProfiles() != null
                && context.getProfiles().getSchemaCache() != null) {
                parser.setEntityResolver(new EntityResolver() {

                    @Override
                    public InputSource resolveEntity(
                        final String publicId,
                        final String systemId)
                        throws SAXException,
                        IOException {
                        final ProfileSchemaCache schemaCache =
                            context.getProfiles().getSchemaCache();
                        if (systemId != null) {

                            if (!systemId.startsWith("file")) {

                                String localPath = schemaCache.get(systemId);
                                if (localPath != null) {
                                    // Reporting.log(
                                    // "schema cache success %1$s", systemId);
                                    localPath = "file:///"
                                        + localPath.replace(
                                            '\\',
                                            '/');
                                    localPath = localPath.replaceAll(
                                        "/\\./",
                                        "/");
                                    localPath = localPath.replaceAll(
                                        " ",
                                        "%20");
                                    // Reporting.log(
                                    // "schema path = %1$s", localPath);
                                    return new InputSource(
                                        localPath);
                                } else {
                                    Reporting.logExpected(
                                        "schema cache fail %1$s",
                                        systemId);
                                }
                            } else {
                                // Reporting.log(
                                // "Rogue SystemID %1$s", systemId);
                                String localPath = systemId;

                                localPath = "file:///"
                                    + localPath.replaceAll(
                                        "file:/+",
                                        "");
                                localPath = localPath.replaceAll(
                                    "/\\./",
                                    "/");
                                localPath = localPath.replaceAll(
                                    " ",
                                    "%20");
                                // Reporting.log(
                                // "schema path = %1$s", localPath);
                                return new InputSource(
                                    localPath);
                            }
                        }
                        return null;
                    }
                });
            }
            parser.setErrorHandler(new ErrorHandler() {

                @Override
                public void warning(
                    final SAXParseException arg0)
                    throws SAXException {
                    System.err.println(arg0.getLocalizedMessage());
                    System.err.format(
                        "warning:%1$s:%2$s:%3$d\n",
                        arg0.getPublicId(),
                        arg0.getSystemId(),
                        arg0.getLineNumber());
                }

                @Override
                public void fatalError(
                    final SAXParseException arg0)
                    throws SAXException {
                    System.err.println(arg0.getLocalizedMessage());
                    System.err.format(
                        "fatal:%1$s:%2$s:%3$d\n",
                        arg0.getPublicId(),
                        arg0.getSystemId(),
                        arg0.getLineNumber());
                }

                @Override
                public void error(
                    final SAXParseException arg0)
                    throws SAXException {
                    System.err.println(arg0.getLocalizedMessage());
                    System.err.format(
                        "error:%1$s:%2$s:%3$d\n",
                        arg0.getPublicId(),
                        arg0.getSystemId(),
                        arg0.getLineNumber());
                }
            });
            parser.parse(file);
            schemaSet = parser.getResult();
            return schemaSet;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

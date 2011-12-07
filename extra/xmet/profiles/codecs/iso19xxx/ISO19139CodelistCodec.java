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
package xmet.profiles.codecs.iso19xxx;

import java.nio.ByteBuffer;

import org.jdom.Namespace;

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.codecs.CodelistCatalogCodec;

/**
 * ISO19139 implementation of CodelistCodec.
 * @author Nahid Akbar
 */
public class ISO19139CodelistCodec
    implements
    CodelistCatalogCodec {

    /** The gml namespace. */
    private static Namespace gmlNamespace = null;

    /** The gco namespace. */
    private static Namespace gcoNamespace = null;

    /** The gmx namespace. */
    private static Namespace gmxNamespace = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public CodelistCatalog decode(
        final ByteBuffer contents,
        final String uri,
        final ClientContext context) {
        if (getGmlNamespace() == null) {
            final Profile profile = context.getProfiles().getProfileByKeyword(
                "iso19115");
            if (profile != null) {
                setGmlNamespace(Namespace.getNamespace(profile
                    .getNamespaceURI("gml")));
                setGcoNamespace(Namespace.getNamespace(profile
                    .getNamespaceURI("gco")));
                setGmxNamespace(Namespace.getNamespace(profile
                    .getNamespaceURI("gmx")));
            } else {
                setGmlNamespace(Namespace
                    .getNamespace("http://www.opengis.net/gml/3.2"));
                setGcoNamespace(Namespace
                    .getNamespace("http://www.isotc211.org/2005/gco"));
                setGmxNamespace(Namespace
                    .getNamespace("http://www.isotc211.org/2005/gmx"));
            }
        }
        return CtCodelistCatalogue.load(
            contents,
            uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "iso19139";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedExtension() {
        return "xml";
    }

    /**
     * Gets the gml namespace.
     * @return the gmlNamespace
     */
    public static Namespace getGmlNamespace() {
        return gmlNamespace;
    }

    /**
     * Sets the gml namespace.
     * @param aGmlNamespace the gmlNamespace to set
     */
    public static synchronized void setGmlNamespace(
        final Namespace aGmlNamespace) {
        ISO19139CodelistCodec.gmlNamespace = aGmlNamespace;
    }

    /**
     * Gets the gco namespace.
     * @return the gcoNamespace
     */
    public static Namespace getGcoNamespace() {
        return gcoNamespace;
    }

    /**
     * Sets the gco namespace.
     * @param aGcoNamespace the gcoNamespace to set
     */
    public static synchronized void setGcoNamespace(
        final Namespace aGcoNamespace) {
        ISO19139CodelistCodec.gcoNamespace = aGcoNamespace;
    }

    /**
     * Gets the gmx namespace.
     * @return the gmxNamespace
     */
    public static Namespace getGmxNamespace() {
        return gmxNamespace;
    }

    /**
     * Sets the gmx namespace.
     * @param aGmxNamespace the gmxNamespace to set
     */
    public static synchronized void setGmxNamespace(
        final Namespace aGmxNamespace) {
        ISO19139CodelistCodec.gmxNamespace = aGmxNamespace;
    }

}

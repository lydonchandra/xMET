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
package xmet.profiles.catalogs;

import n.io.CS;
import n.io.CSC;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.codecs.CodelistCatalogCodec;

/**
 * A Wrapper object for a Codelist Catalog that is cached in the local file
 * system.
 * <p>
 * This class is responsible for loading a Catalog from a file name.
 * </p>
 * @author Nahid Akbar
 */
@CSC("Catalog")
public class CatalogCache {

    /* == Fields == */
    /** The name of the catalog. */
    @CS
    private String name;

    /** The url of the catalog. */
    @CS
    private String url;

    /** The local file name of the catalog. */
    @CS
    private String fileName;

    /** cached catalog. */
    private CodelistCatalog cache;

    /** The last update. */
    @CS
    private long lastUpdate;

    /* == Loading catalog == */
    /**
     * Checks if is loaded.
     * @return true, if is loaded
     */
    public boolean isLoaded() {
        return getCache() != null;
    }

    /**
     * loads the codelist codec and returns the codelist catalog.
     * @param codec the codec
     * @param context the context
     * @return the codelist catalog
     */
    public CodelistCatalog getLoadedCatalog(
        final CodelistCatalogCodec codec,
        final ClientContext context) {
        loadCatalog(
            codec,
            context);
        return getCache();
    }

    /**
     * if not loaded, loads catalog with the specified codec and caches it.
     * @param codec the codec
     * @param context the context
     * @return true, if successful
     */
    public boolean loadCatalog(
        final CodelistCatalogCodec codec,
        final ClientContext context) {
        if (!isLoaded()) {
            Reporting.logExpected("Loading "
                + getFileName());
            setCache(codec.decode(
                context.getResources().getResourceContents(
                    getFileName()),
                getUrl(),
                context));
        }
        return isLoaded();
    }

    /* == Getters and setters == */
    /**
     * Gets the url.
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     * @param aUrl the new url
     */
    public void setUrl(
        final String aUrl) {
        this.url = aUrl;
    }

    /**
     * Gets the file name.
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     * @param aFileName the new file name
     */
    public void setFileName(
        final String aFileName) {
        this.fileName = aFileName;
    }

    /**
     * Gets the cache.
     * @return the cache
     */
    public CodelistCatalog getCache() {
        return cache;
    }

    /**
     * Sets the cache.
     * @param aCache the new cache
     */
    public void setCache(
        final CodelistCatalog aCache) {
        this.cache = aCache;
    }

    /**
     * Sets the last update.
     * @param aLastUpdate the new last update
     */
    public void setLastUpdate(
        final long aLastUpdate) {
        this.lastUpdate = aLastUpdate;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Gets the name of the catalog.
     * @return the name of the catalog
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the last update.
     * @return the last update
     */
    public long getLastUpdate() {
        return lastUpdate;
    }
}

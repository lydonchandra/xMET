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

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import n.io.CSC;
import n.io.CSL;
import n.io.bin.Files;
import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;

/**
 * A class that maintains a list of Catalog objects.
 * @author Nahid Akbar
 */
public class CatalogCacheManager {

    /* == Storable Attributes == */
    /** The items. */
    @CSL
    @CSC
    private List<CatalogCache> items = new ArrayList<CatalogCache>();

    /* == Runtime Attributes == */
    /** The file name. */
    private String fileName;

    /** The folder. */
    private String folder;

    /* == Constructors == */
    /**
     * Instantiates a new catalog cache manager.
     * @param aFileName the file name
     * @param aFolder the folder
     */
    public CatalogCacheManager(
        final String aFileName,
        final String aFolder) {
        super();
        this.setFileName(aFileName);
        this.setFolder(aFolder);
    }

    /**
     * Instantiates a new catalog cache manager.
     */
    public CatalogCacheManager() {
        this(null, null);
    }

    /* == Downloading and storing == */
    /**
     * Gets the catalog.
     * @param url the url
     * @return the catalog
     */
    public CatalogCache getCatalog(
        final String url) {
        /* find in cache */
        for (final CatalogCache pcli : getItems()) {
            if (pcli.getUrl().equalsIgnoreCase(
                url)) {
                return pcli;
            }
        }
        /* if not cached */
        try {
            /* download file */
            final ByteBuffer contents = Files.downloadURLContents(new URL(
                url));
            /* put it in a random filename inside the catalogs folder */
            final CatalogCache cat = new CatalogCache();
            cat.setFileName(getFolder()
                + "/"
                + UUID.randomUUID().toString()
                + ".xml");
            cat.setName(url);
            cat.setLastUpdate((new Date()).getTime());
            cat.setUrl(url);
            /* write to that file */
            Files.write(
                new File(
                    cat.getFileName()),
                contents);
            /* add the new catalof */
            getItems().add(
                cat);
            /* save list */
            save();
            return cat;
        } catch (final Exception e) {
            Reporting.reportUnexpected(
                e,
                "Error");
        }
        return null;
    }

    /**
     * Save catalog cache.
     */
    public void save() {
        if (getFileName() != null) {
            final String code = CSXMLSerializationCodec.encode(this);
            Files.write(
                new File(
                    getFileName()),
                ByteBuffer.wrap(code.getBytes()));
        } else {
            Reporting.reportUnexpected("catalog cache list file name not set");
        }
    }

    /**
     * Load catalog cache.
     * @param catalogCacheListFile asdf
     * @param catalogCacheFolder the catalog cache folder
     * @return the catalog cache manager
     */
    public static CatalogCacheManager loadCatalogCache(
        final String catalogCacheListFile,
        final String catalogCacheFolder) {
        CatalogCacheManager cache = null;
        try {
            final ByteBuffer contents = Files.read(catalogCacheListFile);
            cache =
                (CatalogCacheManager) CSXMLSerializationCodec
                    .decode(new String(
                        contents.array()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (cache == null) {
            cache = new CatalogCacheManager();
        }
        cache.setFileName(catalogCacheListFile);
        cache.setFolder(catalogCacheFolder);
        return cache;
    }

    /**
     * Gets the items.
     * @return the items
     */
    public List<CatalogCache> getItems() {
        return items;
    }

    /**
     * Sets the items.
     * @param aItems the new items
     */
    public void setItems(
        final List<CatalogCache> aItems) {
        items = aItems;
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
        fileName = aFileName;
    }

    /**
     * Gets the folder.
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     * @param aFolder the new folder
     */
    public void setFolder(
        final String aFolder) {
        folder = aFolder;
    }
}

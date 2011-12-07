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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.codecs.CodelistCatalogCodec;
import xmet.profiles.codecs.DefaultCodec;

/**
 * Catalog Codec for decoding plain text Catalogs.
 * @author Nahid Akbar
 */
public class TextCodelistCatalogCodec
    implements
    CodelistCatalogCodec,
    DefaultCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public CodelistCatalog decode(
        final ByteBuffer catalogContents,
        final String catalogURL,
        final ClientContext context) {
        final Scanner sc = new Scanner(
            new String(
                catalogContents.array()));
        if (catalogContents.remaining() > 0) {
            final CodelistCatalogImpl catalog = new CodelistCatalogImpl(
                catalogURL);
            CodelistImpl codelist = null;
            int lc = 0;
            while (sc.hasNext()) {
                lc++;
                String line = sc.nextLine();
                if (line != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        if (line.startsWith("#")) {
                            continue;
                        } else if (line.endsWith(":")) {
                            if (codelist != null) {
                                codelist.setCatalogURL(catalogURL);
                                catalog.getItems().add(
                                    codelist);
                                codelist = null;
                            }
                            codelist = new CodelistImpl();
                            codelist.setName(line.substring(
                                0,
                                line.length() - 1));
                        } else {
                            if (codelist != null) {
                                String label;
                                String code;
                                final String[] parts = line.split("\\|");
                                if (parts.length == 2) {
                                    code = parts[0];
                                    label = parts[1];
                                    codelist.getItems().add(
                                        new CodelistItemImpl(
                                            code,
                                            label));
                                } else if (parts.length == 1) {
                                    code = parts[0];
                                    label = parts[0];
                                    codelist.getItems().add(
                                        new CodelistItemImpl(
                                            code,
                                            label));
                                } else {
                                    Reporting.logUnexpected(
                                        "Too many \"|\"s in on line %1$d",
                                        lc);
                                }
                            } else {
                                Reporting.logExpected(
                                    "Codelist item specified on "
                                        + " line %1$d without specifying name",
                                    lc);
                            }
                        }
                    }
                }
            }
            if (codelist != null) {
                catalog.getItems().add(
                    codelist);
                codelist = null;
            }
            return catalog;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "text";
    }

    /**
     * Internal CodelistCatalog Implementation.
     */
    static class CodelistCatalogImpl
        implements
        CodelistCatalog {

        /** The items. */
        private ArrayList<Codelist> items = new ArrayList<Codelist>();

        /** The uri. */
        private String uri;

        /**
         * Instantiates a new codelist catalog impl.
         * @param aUri the uri
         */
        public CodelistCatalogImpl(
            final String aUri) {
            this.setUri(aUri);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<Codelist> getCodelists() {
            return getItems();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCatalogURI() {
            return getUri();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Codelist getCodelistByIdentifier(
            final String part) {
            for (final Codelist c : getItems()) {
                if (c.hasIdentifier(part)) {
                    return c;
                }
            }
            return null;
        }

        /**
         * Gets the items.
         * @return the items
         */
        public ArrayList<Codelist> getItems() {
            return items;
        }

        /**
         * Sets the items.
         * @param aItems the new items
         */
        public void setItems(
            final ArrayList<Codelist> aItems) {
            items = aItems;
        }

        /**
         * Gets the uri.
         * @return the uri
         */
        public String getUri() {
            return uri;
        }

        /**
         * Sets the uri.
         * @param aUri the new uri
         */
        public void setUri(
            final String aUri) {
            uri = aUri;
        }

    }

    /**
     * Internal Codelist Implementation.
     */
    static class CodelistImpl
        implements
        Codelist {

        /** The items. */
        private ArrayList<CodeItem> items = new ArrayList<CodeItem>();

        /** The name. */
        private String name;

        /**
         * {@inheritDoc}
         */
        @Override
        public List<CodeItem> getItems() {
            return items;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasIdentifier(
            final String text) {
            return getName().equals(
                text);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCodelistName() {
            return getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCodelistURL() {
            return String.format(
                "%1$s#%2$s",
                getCatalogURL(),
                getName());
        }

        /** The catalog url. */
        private String catalogURL = "";

        /**
         * {@inheritDoc}
         */
        @Override
        public void setCatalogURL(
            final String uri) {
            catalogURL = uri;
        }

        /**
         * Gets the name.
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name.
         * @param aName the new name
         */
        public void setName(
            final String aName) {
            name = aName;
        }

        /**
         * Gets the catalog url.
         * @return the catalog url
         */
        public String getCatalogURL() {
            return catalogURL;
        }

        /**
         * Sets the items.
         * @param aItems the new items
         */
        public void setItems(
            final ArrayList<CodeItem> aItems) {
            items = aItems;
        }
    }

    /**
     * Internal CodelistItem Implementation.
     */
    static class CodelistItemImpl
        implements
        CodeItem {

        /** The name. */
        private String name;

        /** The description. */
        private String description;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValue() {
            return getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLabel() {
            return getDescription();
        }

        /**
         * Instantiates a new codelist item impl.
         * @param aName the name
         * @param aDescription the description
         */
        public CodelistItemImpl(
            final String aName,
            final String aDescription) {
            super();
            this.setName(aName);
            this.setDescription(aDescription);
        }

        /**
         * Gets the name.
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name.
         * @param aName the new name
         */
        public void setName(
            final String aName) {
            name = aName;
        }

        /**
         * Gets the description.
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description.
         * @param aDescription the new description
         */
        public void setDescription(
            final String aDescription) {
            description = aDescription;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedExtension() {
        return "xmettextcodelist";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}

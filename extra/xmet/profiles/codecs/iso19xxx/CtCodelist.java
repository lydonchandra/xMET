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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;

/**
 * A CT_Codelist.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CtCodelist
    extends CtItem
    implements
    Codelist {

    /** The Constant ANZLIC_SE_CODELIST_FORMAT. */
    private static final String ANZLIC_SE_CODELIST_FORMAT =
        "^[^|]+\\|([0-9\\-\\.]+)\\|([0-9\\-\\.]+)"
            + "\\|([0-9\\-\\.]+)\\|([0-9\\-\\.]+)\\|[^|]+$";
    /** The code entries. */
    private List<CodeItem> codeEntries = new ArrayList<CodeItem>();

    /**
     * Instantiates a new c t_ codelist.
     * @param codeListItem the code list item
     */
    public CtCodelist(
        final Element codeListItem) {
        super(codeListItem.getChild(
            "CodeListDictionary",
            ISO19139CodelistCodec.getGmxNamespace()));
        final Element codeListDictionary = codeListItem.getChild(
            "CodeListDictionary",
            ISO19139CodelistCodec.getGmxNamespace());
        if (codeListDictionary != null) {
            final List aCodeEntries =
                codeListDictionary.getContent(new ElementFilter(
                    "codeEntry",
                    ISO19139CodelistCodec.getGmxNamespace()));
            for (final Object codeEntityo : aCodeEntries) {
                Element codeEntity = (Element) codeEntityo;
                codeEntity = codeEntity.getChild(
                    "CodeDefinition",
                    ISO19139CodelistCodec.getGmxNamespace());
                if (codeEntity != null) {
                    final Element description = codeEntity.getChild(
                        "description",
                        ISO19139CodelistCodec.getGmlNamespace());
                    try {
                        if (description != null) {
                            final String desc = description.getText();
                            if (desc.matches(ANZLIC_SE_CODELIST_FORMAT)) {
                                final CodeDefinition cd = new SECodeDefinition(
                                    codeEntity);
                                this.codeEntries.add(cd);
                                continue;
                            }
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    final CodeDefinition cd = new CodeDefinition(
                        codeEntity);
                    this.codeEntries.add(cd);
                }
            }
        }
    }

    /**
     * Instantiates a new c t_ codelist.
     * @param string the string
     * @param cd the cd
     */
    public CtCodelist(
        final String string,
        final List<CodeItem> cd) {
        super(null);
        final ArrayList<String> names = new ArrayList<String>();
        names.add(string);
        setName(names);
        setIdentifier(string);
        codeEntries = cd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CodeItem> getItems() {
        return codeEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasIdentifier(
        final String text) {
        /* TODO: Check logic */
        for (final String s : getName()) {
            if (s.equalsIgnoreCase(text)) {
                return true;
            }
        }
        if ((getIdentifier() != null)
            && getIdentifier().equals(
                text)) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if ((getName() != null)
            && (getName().size() > 0)) {
            return getName().get(
                0);
        } else {
            if (getDescription() != null) {
                return getDescription();
            }
            return super.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodelistName() {
        if ((getName() != null)
            && (getName().size() > 0)) {
            return getName().get(
                0);
        } else {
            if (getDescription() != null) {
                return getDescription();
            }
            return super.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodelistURL() {
        return String.format(
            "%1$s#%2$s",
            catalogURL,
            getIdentifier());
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

}

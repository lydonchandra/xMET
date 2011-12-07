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
import java.util.ArrayList;
import java.util.List;

import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;

/**
 * A CT_CodelistCatalogue. See figure 10 of ISO 19139:2008
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CtCodelistCatalogue
    extends CtCatalogue
    implements
    CodelistCatalog {

    /** The code list items. */
    private final List<Codelist> codeListItems = new ArrayList<Codelist>();

    /**
     * Instantiates a new c t_ codelist catalogue.
     * @param ctCodelistCatalogueElement the c t_ codelist catalogue element
     * @param uri2 the uri2
     */
    public CtCodelistCatalogue(
        final Element ctCodelistCatalogueElement,
        final String uri2) {
        super(ctCodelistCatalogueElement);
        uri = uri2;

        List children =
            ctCodelistCatalogueElement.getContent(new ElementFilter(
                "codelistItem",
                ISO19139CodelistCodec.getGmxNamespace()));

        for (final Object childo : children) {
            final Element child = (Element) childo;
            final CtCodelist codeListItem = new CtCodelist(
                child);
            codeListItems.add(codeListItem);
        }

        children = ctCodelistCatalogueElement.getContent(new ElementFilter(
            "crs",
            ISO19139CodelistCodec.getGmxNamespace()));
        if (children.size() > 0) {
            final List<CodeItem> cd = new ArrayList<CodeItem>();
            for (final Object o : children) {
                final Element e = (Element) o;
                Element gmlCRS = e.getChild(
                    "ProjectedCRS",
                    ISO19139CodelistCodec.getGmlNamespace());
                if (gmlCRS == null) {
                    gmlCRS = e.getChild(
                        "GeodeticCRS",
                        ISO19139CodelistCodec.getGmlNamespace());
                }
                if (gmlCRS == null) {
                    gmlCRS = e.getChild(
                        "ML_VerticalCRS",
                        ISO19139CodelistCodec.getGmxNamespace());
                }
                if (gmlCRS == null) {
                    gmlCRS = e.getChild(
                        "EllipsoidalCS",
                        ISO19139CodelistCodec.getGmlNamespace());
                }

                if (gmlCRS != null) {
                    /* Reporting.log("%1$s", gmlCRS.getName()); */
                    final Element gmlIdentifier =
                        (Element) JDOMXmlUtils.traceSimpleXpathItem(
                            gmlCRS,
                            "gml:identifier");
                    final Element gmlName =
                        (Element) JDOMXmlUtils.traceSimpleXpathItem(
                            gmlCRS,
                            "gml:name");
                    cd.add(new CodeDefinition(
                        gmlIdentifier.getTextTrim(),
                        gmlName.getTextTrim()));
                }
            }
            if (cd.size() > 0) {
                codeListItems.add(new CtCodelist(
                    "crs",
                    cd));
            }
        }

        children = ctCodelistCatalogueElement.getContent(new ElementFilter(
            "uomItem",
            ISO19139CodelistCodec.getGmxNamespace()));

        if (children.size() > 0) {
            for (final Object o : children) {
                final Element e = (Element) o;
                String type = null;
                CodeItem item = null;
                final Element unit = (Element) e.getChildren().get(
                    0);
                if (unit != null) {
                    final Element gmlIdentifier =
                        (Element) JDOMXmlUtils.traceSimpleXpathItem(
                            unit,
                            "gml:identifier");
                    final Element gmlQuantityType =
                        (Element) JDOMXmlUtils.traceSimpleXpathItem(
                            unit,
                            "gml:quantityType");
                    if (gmlIdentifier != null) {
                        item = new CodeDefinition(
                            gmlIdentifier.getTextTrim(),
                            gmlIdentifier.getTextTrim());
                    }
                    if (gmlQuantityType != null) {
                        type = gmlQuantityType.getTextTrim();
                    }
                    if ((type == null)
                        || (type.length() == 0)) {
                        type = "length";
                    }
                }

                if ((type != null)
                    && (item != null)) {
                    Codelist cl = getCodelistByIdentifier(type);
                    if (cl == null) {
                        cl = new CtCodelist(
                            type,
                            new ArrayList<CodeItem>());
                        codeListItems.add(cl);
                    }
                    cl.getItems().add(
                        item);
                }
            }
        }
        for (final Codelist list : codeListItems) {
            list.setCatalogURL(getCatalogURI());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Codelist> getCodelists() {
        return codeListItems;
    }

    /**
     * Load.
     * @param contents the contents
     * @param uri the uri
     * @return the codelist catalog
     */
    static CodelistCatalog load(
        final ByteBuffer contents,
        final String uri) {
        final Element element = JDOMXmlUtils.elementFromXml(new String(
            contents.array()));

        if (element.getName().equals(
            "CT_CodelistCatalogue")) {
            return new CtCodelistCatalogue(
                element,
                uri);
        }
        if (element.getName().equals(
            "CT_CrsCatalogue")) {
            return new CtCodelistCatalogue(
                element,
                uri);
        }
        if (element.getName().equals(
            "CT_UomCatalogue")) {
            return new CtCodelistCatalogue(
                element,
                uri);
        }
        Reporting.logUnexpected(
            "Unhandelled codelist type %1$s",
            element.getName());
        return null;
    }

    /** The uri. */
    private final String uri;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCatalogURI() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Codelist getCodelistByIdentifier(
        final String part) {
        for (final Codelist c : getCodelists()) {
            if (c.hasIdentifier(part)) {
                return c;
            }
        }
        return null;
    }
}

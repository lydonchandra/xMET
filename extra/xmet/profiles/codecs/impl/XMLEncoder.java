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

import java.util.HashMap;
import java.util.Map;

import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import xmet.profiles.ProfileSchema;
import xmet.profiles.model.AllGroup;
import xmet.profiles.model.Any;
import xmet.profiles.model.AttributesList;
import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.Comment;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Extra;
import xmet.profiles.model.ImplicitGroup;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.SequenceGroup;
import xmet.profiles.model.Simple;

/**
 * Extracts xml element from model.
 * @author Nahid Akbar
 */
class XMLEncoder {

    /** The root element. */
    private Element rootElement = null;

    /** The profile schemas. */
    // private Map<String, ProfileSchema> profileSchemas = null;

    /** The name spaces. */
    private final Map<String, Namespace> nameSpaces =
        new HashMap<String, Namespace>();

    /**
     * entry point Encode data.
     * @param profileModelRootNode the profile model root node
     * @param aProfileSchemas the profile schemas
     * @return the element
     */
    public Element encodeData(
        final Entity profileModelRootNode,
        final Map<String, ProfileSchema> aProfileSchemas) {
        assert (ModelUtils.isElementDeclaration(profileModelRootNode));
        final Element e = new XMLEncoder().extractElementData(
            (ElementDeclaration) profileModelRootNode,
            aProfileSchemas);
        return e;
    }

    /**
     * Extract element data.
     * @param elementDeclarationNode the element declaration node
     * @param aProfileSchemas the profile schemas
     * @return the element
     */
    @SuppressWarnings("unchecked")
    protected Element extractElementData(
        final ElementDeclaration elementDeclarationNode,
        final Map<String, ProfileSchema> aProfileSchemas) {

        if (ModelUtils.isPresent(elementDeclarationNode)) {
            /* Reporting.log("%1$s - %2$s - %3$s", */
            /* ModelUtils.extractEntityPlainName(elem), */
            /* ModelUtils.extractEntityNamePrefix(elem), */
            /* ModelUtils.extractEntityNamespace(elem)); */

            final Element element = new Element(
                ModelUtils.extractEntityPlainName(elementDeclarationNode),
                makeNamespace(
                    ModelUtils.extractEntityNamePrefix(elementDeclarationNode),
                    ModelUtils.extractEntityNamespace(elementDeclarationNode)));

            if (rootElement == null) {
                rootElement = element;
                makeNamespace(
                    ModelUtils.extractEntityNamePrefix(elementDeclarationNode),
                    ModelUtils.extractEntityNamespace(elementDeclarationNode));
            }

            if (elementDeclarationNode.hasAttributes()) {
                final AttributesList list =
                    elementDeclarationNode.getAttributes();
                for (final ElementAttribute atr : list) {
                    if (ModelUtils.isPresent(atr)) {
                        try {
                            element
                                .getAttributes()
                                .add(
                                    new Attribute(
                                        ModelUtils.extractEntityPlainName(atr),
                                        ModelUtils.getAttributeValue(atr),
                                        makeNamespace(
                                            ModelUtils
                                                .extractEntityNamePrefix(atr),
                                            ModelUtils
                                                .extractEntityNamespace(atr))));
                        } catch (final Exception e) {
                            Reporting.reportUnexpected();
                        }
                    }
                }
            }

            if (elementDeclarationNode.hasGroup()) {
                extractData(
                    elementDeclarationNode.getGroup(),
                    element);
            }

            /* enter validation information */

            if (aProfileSchemas != null) { /* this is the root element */
                rootElement = element;
                // this.profileSchemas = aProfileSchemas;
                final StringBuilder sb = new StringBuilder();
                for (final String nsPreFix : nameSpaces.keySet()) {
                    final Namespace ns = nameSpaces.get(nsPreFix);
                    rootElement.addNamespaceDeclaration(ns);
                    if (aProfileSchemas != null) {
                        final ProfileSchema schema =
                            aProfileSchemas.get(ns.getURI());
                        if (schema != null) {
                            final String scUri = schema.getSchemaUrl();
                            if (scUri != null) {
                                sb.append(ns.getURI()
                                    + " "
                                    + scUri
                                    + " ");
                            }
                        }
                    }
                }
                final Namespace xsins = Namespace.getNamespace(
                    "xsi",
                    "http://www.w3.org/2001/XMLSchema-instance");
                rootElement.setAttribute(
                    "schemaLocation",
                    sb.toString().trim(),
                    xsins);

            }

            return element;
        } else {
            return null;
        }
    }

    /**
     * Make namespace.
     * @param namespacePrefix the namespace prefix
     * @param namespaceURI the namespace uri
     * @return the namespace
     */
    private Namespace makeNamespace(
        final String namespacePrefix,
        final String namespaceURI) {
        if ((namespacePrefix != null)
            && (namespacePrefix.length() > 0)) {
            Namespace ns = nameSpaces.get(namespacePrefix);
            if (ns == null) {
                ns = Namespace.getNamespace(
                    namespacePrefix,
                    namespaceURI);
                nameSpaces.put(
                    namespacePrefix,
                    ns);
                if (rootElement != null) {
                    rootElement.addNamespaceDeclaration(ns);
                }
            }
            return ns;
        }
        return Namespace.getNamespace(
            namespacePrefix,
            namespaceURI);
    }

    /**
     * Extract data.
     * @param modelNode the entity
     * @param dataElement the element
     */
    private void extractData(
        final Entity modelNode,
        final Element dataElement) {
        if (ModelUtils.isOptional(modelNode)) {
            try {
                final Optional optional = ModelUtils.asOptional(modelNode);
                if (ModelUtils.isPresent(optional)) {
                    extractData(
                        optional.getSetTerm(),
                        dataElement);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isRepeated(modelNode)) {
            try {
                final Repeated repeated = ModelUtils.asRepeated(modelNode);
                if (repeated.getEntities().size() > 0) {
                    for (final Entity i : repeated) {
                        if (ModelUtils.isPresent(i)) {
                            extractData(
                                i,
                                dataElement);
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isSequenceGroup(modelNode)) {
            try {
                final SequenceGroup sequence =
                    ModelUtils.asSequenceGroup(modelNode);
                for (final Entity e : sequence) {
                    if (/* (e != null) && */ModelUtils.isPresent(e)) {
                        extractData(
                            e,
                            dataElement);
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isAllGroup(modelNode)) {
            try {
                final AllGroup all = ModelUtils.asAllGroup(modelNode);
                for (final Entity e : all) {
                    /* if (e != null) { */
                    extractData(
                        e,
                        dataElement);
                    // }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isImplicitGroup(modelNode)) {
            try {
                final ImplicitGroup implicit =
                    ModelUtils.asImplicitGroup(modelNode);
                for (final Entity e : implicit) {
                    /* TODO: get rid of e!=null for task #85 */
                    if (/* (e != null) && */ModelUtils.isPresent(e)) {
                        extractData(
                            e,
                            dataElement);
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isChoiceGroup(modelNode)) {
            try {
                final ChoiceGroup choice = ModelUtils.asChoiceGroup(modelNode);
                if ((choice.getSelected() != -1)
                    && ModelUtils.isPresent(choice.getChildren().get(
                        choice.getSelected()))) {
                    extractData(
                        choice.getChildren().get(
                            choice.getSelected()),
                        dataElement);
                } else {
                    Reporting.logUnexpected();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isElementDeclaration(modelNode)) {
            final Element newElement = extractElementData(
                (ElementDeclaration) modelNode,
                null);
            if (newElement != null) {
                dataElement.addContent(newElement);
            }
        } else if (ModelUtils.isExtra(modelNode)) {
            try {
                final Extra extra = ModelUtils.asExtra(modelNode);
                dataElement.addContent(JDOMXmlUtils.documentFromXml(
                    extra.getContent()).getRootElement().detach());
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isSimple(modelNode)) {
            try {
                final Simple simple = ModelUtils.asSimple(modelNode);
                if (simple.getValue() != null) {
                    dataElement.addContent(simple.getValue());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isComment(modelNode)) {
            try {
                final Comment comment = ModelUtils.asComment(modelNode);
                dataElement.addContent(new org.jdom.Comment(
                    comment.getComment()));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isAny(modelNode)) {
            try {
                final Any any = ModelUtils.asAny(modelNode);
                Element content = null;
                try {
                    if ((any.getValue() != null)
                        && (any.getValue().indexOf(
                            '<') >= 0)) {
                        content = JDOMXmlUtils.elementFromXml(any.getValue());
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                if (content != null) {
                    dataElement.addContent(content);
                } else {
                    dataElement.setText(any.getValue());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            Reporting.logUnexpected();
        }
    }

}

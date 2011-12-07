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
import java.util.List;
import java.util.Map;

import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Attribute;
import org.jdom.Element;

import xmet.profiles.model.Comment;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Extra;
import xmet.profiles.model.Group;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;
import xmet.profiles.model.Simple;
import xmet.profiles.model.SimpleAttribute;

/**
 * Helper class that decodes xml element into model.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"unused",
"rawtypes"
})
class XMLDecoder {

    /**
     * The repeated history map. It is for maintaining a list of repeated nodes
     * and the number of elements there are so far (for easy model element
     * locating through ModelUtils element tracing)
     */
    private Map<String, Integer> repeatedHistoryMap;

    /**
     * Load data.
     * @param profileModelRoot the profile model root
     * @param dataRoot the data root
     */
    public void loadData(
        final Entity profileModelRoot,
        final Element dataRoot) {
        assert (ModelUtils.isElementDeclaration(profileModelRoot));
        repeatedHistoryMap = new HashMap<String, Integer>();
        loadElementData(
            ModelUtils.asElementDeclaration(profileModelRoot),
            dataRoot);
    }

    /**
     * Loads element data.
     * @param modelElement the model element
     * @param dataElement the data element
     */
    private void loadElementData(
        final ElementDeclaration modelElement,
        final Element dataElement) {
        /* make sure the names match */
        //
        /* Reporting.log("loadElementData \n\tmodel=%1$s\n\tdata =%2$s", */
        // (ModelUtils.getPath(modelElement)).replaceAll("\\[1\\]", ""),
        /* JDOMXmlUtils.getPath(dataElement)); */

        if (dataElement.getQualifiedName().equals(
            modelElement.getQualifiedName())) {

            loadElementAttributes(
                modelElement,
                dataElement);

            if (dataElement.getChildren().size() > 0) {

                Entity lastEntity = null;

                for (final Object content : dataElement.getContent()) {

                    if (content instanceof Element) {
                        final Element element = (Element) content;

                        String name = element.getQualifiedName();
                        /* Reporting.log("%1$s", name); */
                        // {
                        /* handle repeated instances */
                        final String path = ModelUtils.getPath(modelElement)
                            + "|"
                            + JDOMXmlUtils.getPath(element);
                        Integer i = repeatedHistoryMap.get(path);
                        if (i == null) {
                            i = Integer.valueOf(1);
                        } else {
                            i = i + 1;
                            name = name
                                + "["
                                + i
                                + "]";
                        }
                        repeatedHistoryMap.put(
                            path,
                            i);
                        // }

                        final Entity entity = ModelUtils.hardGetEntityChild(
                            modelElement,
                            name);

                        lastEntity = entity;

                        if (entity != null) { /* case 1 */
                            loadEntityData(
                                entity,
                                element);
                        } else {
                            final Group group =
                                ModelUtils.extractEntityGroup(modelElement);
                            ModelUtils.groupAdd(
                                group,
                                new Extra(
                                    group,
                                    element.getQualifiedName(),
                                    JDOMXmlUtils.xmlFromElement(element)));
                        }
                    } else if (content instanceof org.jdom.Comment) {
                        final org.jdom.Comment comment =
                            (org.jdom.Comment) content;
                        final Group group =
                            ModelUtils.extractEntityGroup(modelElement);
                        final Comment vC = new Comment(
                            group,
                            comment.getValue());
                        ModelUtils.groupAddAfter(
                            group,
                            lastEntity,
                            vC);
                        lastEntity = vC;
                    }
                    // else {
                    // /* ignore anything else */
                    // }
                }
            } else {
                Settable setAble;
                setAble = ModelUtils.getSetable(modelElement);
                if (setAble != null) {
                    final String value = dataElement.getValue();
                    if (value != null
                        && value.trim().length() > 0) {
                        setAble.setValue(value);
                        /* Reporting.log("value = %1$s",value); */
                    }
                }
            }
        }
    }

    /**
     * Load entity data.
     * @param profileModelNode the profile model node
     * @param dataNode the data node
     */
    private void loadEntityData(
        final Entity profileModelNode,
        final Element dataNode) {

        if (ModelUtils.isElementDeclaration(profileModelNode)) {
            try {
                loadElementData(
                    (ElementDeclaration) profileModelNode,
                    dataNode);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (ModelUtils.isSimple(profileModelNode)) {
            try {
                final Simple simple = ModelUtils.asSimple(profileModelNode);
                /* TODO: Investigate this */
                Reporting.logUnexpected();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            Reporting.logUnexpected();
        }
    }

    /**
     * Load element attributes.
     * @param elementDeclarationProfileModelNode the element declaration profile
     *            model node
     * @param elementNode the element node
     */
    private void loadElementAttributes(
        final ElementDeclaration elementDeclarationProfileModelNode,
        final Element elementNode) {
        final List attributesList = elementNode.getAttributes();
        for (int i = 0; i < attributesList.size(); i++) {
            final Attribute attribute = (Attribute) attributesList.get(i);
            final String name = attribute.getQualifiedName();
            if (name.startsWith("xmlns:")
                || name.equals("xsi:schemaLocation")) {
                Reporting.logExpected("Ignoring header tag "
                    + name);
            } else {
                /* find attribute and set value */
                final ElementAttribute modelAttribute =
                    (ElementAttribute) ModelUtils.hardGetEntityChild(
                        elementDeclarationProfileModelNode,
                        "@"
                            + name);

                if (modelAttribute != null) {
                    modelAttribute.setValue(attribute.getValue().trim());

                } else {
                    ModelUtils.addAttribute(
                        elementDeclarationProfileModelNode,
                        new SimpleAttribute(
                            attribute.getQualifiedName(),
                            attribute.getValue().trim(),
                            attribute.getNamespaceURI(),
                            false));
                }
            }
        }
    }

}

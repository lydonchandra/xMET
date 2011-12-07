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

/**
 * A CT_Item.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public abstract class CtItem {

    /** The identifier. */
    private String identifier;

    /** The name. */
    private ArrayList<String> name = new ArrayList<String>();

    /** The definition. */
    private String definition;

    /** The description. */
    private String description;

    /** The description reference. */
    private String descriptionReference;

    /** The remarks. */
    private String remarks;

    /**
     * Instantiates a new c t_ item.
     * @param codeListItem the code list item
     */
    public CtItem(
        final Element codeListItem) {
        if (codeListItem != null) {
            final Element identifierElement = codeListItem.getChild(
                "identifier",
                ISO19139CodelistCodec.getGmlNamespace());
            final Element descriptionElement = codeListItem.getChild(
                "description",
                ISO19139CodelistCodec.getGmlNamespace());
            final List nameElements =
                codeListItem.getContent(new ElementFilter(
                    "name",
                    ISO19139CodelistCodec.getGmlNamespace()));
            final Element descriptionReferenceElement = codeListItem.getChild(
                "descriptionReference",
                ISO19139CodelistCodec.getGmlNamespace());
            final Element remarksElement = codeListItem.getChild(
                "remarks",
                ISO19139CodelistCodec.getGmlNamespace());

            if (identifierElement != null) {
                setIdentifier(identifierElement.getTextTrim());
            }
            if (descriptionElement != null) {
                setDescription(descriptionElement.getTextTrim());
            }

            for (final Object nameElemento : nameElements) {
                final Element nameElement = (Element) nameElemento;
                name.add(nameElement.getTextTrim());
            }
            if (descriptionReferenceElement != null) {
                setDescriptionReference(descriptionReferenceElement
                    .getTextTrim());
            }
            if (remarksElement != null) {
                setRemarks(remarksElement.getTextTrim());
            }
        }
    }

    /**
     * Gets the identifier.
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier.
     * @param aIdentifier the new identifier
     */
    public void setIdentifier(
        final String aIdentifier) {
        this.identifier = aIdentifier;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public ArrayList<String> getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final ArrayList<String> aName) {
        this.name = aName;
    }

    /**
     * Gets the definition.
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     * @param aDefinition the new definition
     */
    public void setDefinition(
        final String aDefinition) {
        this.definition = aDefinition;
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
        this.description = aDescription;
    }

    /**
     * Gets the description reference.
     * @return the description reference
     */
    public String getDescriptionReference() {
        return descriptionReference;
    }

    /**
     * Sets the description reference.
     * @param aDescriptionReference the new description reference
     */
    public void setDescriptionReference(
        final String aDescriptionReference) {
        this.descriptionReference = aDescriptionReference;
    }

    /**
     * Gets the remarks.
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the remarks.
     * @param aRemarks the new remarks
     */
    public void setRemarks(
        final String aRemarks) {
        this.remarks = aRemarks;
    }

}

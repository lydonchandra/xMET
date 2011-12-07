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

import org.jdom.Element;

import xmet.profiles.catalogs.model.CodeItem;

/**
 * A single code definition.
 * @author Nahid Akbar
 */
public class CodeDefinition
    implements
    CodeItem {

    /** The description. */
    private String description;

    /** The identifier. */
    private String identifier;

    /**
     * Instantiates a new code definition.
     * @param codeDefinition the code definition
     */
    public CodeDefinition(
        final Element codeDefinition) {
        if (codeDefinition.getName().equals(
            "CodeDefinition")) {
            final Element aDescription = codeDefinition.getChild(
                "description",
                ISO19139CodelistCodec.getGmlNamespace());
            if (aDescription != null) {
                setDescription(aDescription.getTextTrim());
            }
            final Element aIdentifier = codeDefinition.getChild(
                "identifier",
                ISO19139CodelistCodec.getGmlNamespace());
            if (aIdentifier != null) {
                setIdentifier(aIdentifier.getTextTrim());
            }
        }
    }

    /**
     * Instantiates a new code definition.
     * @param aIdentifier the identifier$
     * @param aDescription the description$
     */
    public CodeDefinition(
        final String aIdentifier,
        final String aDescription) {
        setIdentifier(aIdentifier);
        setDescription(aDescription);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return getDescription();
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
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return getIdentifier();
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}

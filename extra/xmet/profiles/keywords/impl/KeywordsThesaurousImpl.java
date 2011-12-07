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
package xmet.profiles.keywords.impl;

import xmet.profiles.keywords.KeywordsThesaurus;

/**
 * Implementation of a Keywords Thesaurus.
 * @author Nahid Akbar
 */
public class KeywordsThesaurousImpl
    implements
    KeywordsThesaurus {

    /** The title. */
    private String title;

    /** The identifier. */
    private final String identifier;

    /** The type. */
    private String type;

    /**
     * Instantiates a new keywords thesaurous impl.
     * @param aIdentifier the identifier
     * @param aType the type
     * @param aTitle the title
     */
    public KeywordsThesaurousImpl(
        final String aIdentifier,
        final String aType,
        final String aTitle) {
        super();
        assert (aIdentifier != null);
        this.title = aTitle;
        this.identifier = aIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the title.
     * @param aTitle the new title
     */
    public void setTitle(
        final String aTitle) {
        this.title = aTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param aType the new type
     */
    public void setType(
        final String aType) {
        this.type = aType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
        final Object obj) {
        return obj instanceof KeywordsThesaurus
            && ((KeywordsThesaurus) obj).getIdentifier().equals(
                identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (identifier != null) {
            return identifier.hashCode();
        } else {
            return 0;
        }
    }

}

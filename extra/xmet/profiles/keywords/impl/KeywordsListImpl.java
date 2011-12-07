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

import java.util.ArrayList;

import xmet.profiles.keywords.Keyword;
import xmet.profiles.keywords.KeywordsList;

/**
 * Implementation of a Keywords List.
 * @author Nahid Akbar
 */
public class KeywordsListImpl
    implements
    KeywordsList {

    /** The keywords. */
    private final ArrayList<Keyword> keywords;

    /**
     * Instantiates a new keywords list impl.
     */
    public KeywordsListImpl() {
        this(new ArrayList<Keyword>());
    }

    /**
     * Instantiates a new keywords list impl.
     * @param aKeywords the keywords
     */
    public KeywordsListImpl(
        final ArrayList<Keyword> aKeywords) {
        super();
        assert (aKeywords != null);
        this.keywords = aKeywords;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Keyword> getKeywords() {
        return keywords;
    }

}

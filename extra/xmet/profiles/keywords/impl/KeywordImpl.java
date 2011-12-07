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

import xmet.profiles.keywords.Keyword;
import xmet.profiles.keywords.KeywordsThesaurus;

/**
 * Implementation of a Keyword.
 * @author Nahid Akbar
 */
public class KeywordImpl
    implements
    Keyword {

    /** The value. */
    private final String value;

    /** The thesaurous. */
    private KeywordsThesaurus thesaurous;

    /**
     * Instantiates a new keyword impl.
     * @param aValue the value
     * @param aThesaurous the thesaurous
     */
    public KeywordImpl(
        final String aValue,
        final KeywordsThesaurus aThesaurous) {
        super();
        assert (aValue != null);
        this.value = aValue;
        this.thesaurous = aThesaurous;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeywordsThesaurus getThesaurous() {
        return thesaurous;
    }

    /**
     * Sets the thesaurous.
     * @param aThesaurous the new thesaurous
     */
    public void setThesaurous(
        final KeywordsThesaurus aThesaurous) {
        this.thesaurous = aThesaurous;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
        final Object obj) {
        if (obj instanceof Keyword) {
            final Keyword keywordObj = (Keyword) obj;
            /*
             * compare value & type & thesaurous
             */
            if (keywordObj.getValue().equals(
                value)) {
                if (keywordObj.getThesaurous() == null
                    || thesaurous == null
                    || keywordObj.getThesaurous().equals(
                        thesaurous)) {
                    return keywordObj.getThesaurous() == null
                        || thesaurous == null
                        || keywordObj.getThesaurous().equals(
                            thesaurous);
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int code = 0;
        if (thesaurous != null) {
            code += thesaurous.hashCode();
        }
        if (value != null) {
            code += value.hashCode();
        }
        return code;
    }
}

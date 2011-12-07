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
package xmet.profiles.codecs;

import xmet.profiles.keywords.KeywordsList;
import xmet.profiles.model.Entity;

/**
 * Keywords List Codec for converting between model elements and Keywords object
 * model.
 * @author Nahid Akbar
 */
public interface KeywordsListCodec
    extends
    Codec {

    /**
     * Extract keywords list.
     * @param profileModelNode the node
     * @return the keywords list
     */
    KeywordsList extractKeywordsList(
        final Entity profileModelNode);

    /**
     * Insert keywords list.
     * @param keywordsList the extent
     * @param profileModelNode the profile model node
     */
    void insertKeywordsList(
        final KeywordsList keywordsList,
        Entity profileModelNode);

    /**
     * Gets the supported node names. Preferably the name of an element
     * declaration. This is for codecs to maintain a list of names they support.
     * Supply multiple names by splitting names via the unix pipe '|'
     * @return the supported node names
     */
    String getSupportedNodeNames();
}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.codecs.KeywordsListCodec;
import xmet.profiles.keywords.Keyword;
import xmet.profiles.keywords.KeywordsList;
import xmet.profiles.keywords.KeywordsThesaurus;
import xmet.profiles.keywords.impl.KeywordImpl;
import xmet.profiles.keywords.impl.KeywordsListImpl;
import xmet.profiles.keywords.impl.KeywordsThesaurousImpl;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;

/**
 * Codec for encoding and decoding Keywords List Codec to and from string value.
 * @author Nahid Akbar
 */
public class StringKeywordsCodec
    implements
    KeywordsListCodec,
    DefaultCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public KeywordsList extractKeywordsList(
        final Entity profileModelNode) {
        final Settable setable = ModelUtils.getSetable(profileModelNode);
        if (setable != null) {
            final String text = setable.getValue();
            if ((text != null)
                && (text.length() > 0)) {
                return extractKeywordsList(text);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertKeywordsList(
        final KeywordsList keywordsList,
        final Entity profileModelNode) {
        final Settable setable = ModelUtils.getSetable(profileModelNode);
        if (setable != null) {
            setable.setValue(encodeKeywordsList(keywordsList));
        }
    }

    /**
     * Encode keywords list.
     * @param keywordList the keyword list
     * @return the string
     */
    public static String encodeKeywordsList(
        final KeywordsList keywordList) {
        if (keywordList != null) {
            final HashMap<String, KeywordsThesaurus> thesauruses =
                new HashMap<String, KeywordsThesaurus>();
            /* build a list of thesauruses */
            for (final Keyword keyword : keywordList.getKeywords()) {
                if (keyword.getThesaurous() != null) {
                    thesauruses.put(
                        keyword.getThesaurous().getIdentifier(),
                        keyword.getThesaurous());
                }
            }
            final StringBuilder keywordsBuilder = new StringBuilder();
            String remaining = "";
            for (final Map.Entry<String, KeywordsThesaurus> thes : thesauruses
                .entrySet()) {
                final KeywordsThesaurus thesaurus = thes.getValue();
                keywordsBuilder.append(thes.getKey());
                keywordsBuilder.append("~ ");
                remaining = "";
                for (final Keyword keyword : keywordList.getKeywords()) {
                    if (keyword.getThesaurous() != null
                        && keyword.getThesaurous().equals(
                            thesaurus)) {
                        keywordsBuilder.append(remaining);
                        keywordsBuilder.append(keyword.getValue());
                        remaining = ", ";
                    }
                }
                keywordsBuilder.append(";");
            }
            remaining = "";
            for (final Keyword keyword : keywordList.getKeywords()) {
                if (keyword.getThesaurous() == null) {
                    keywordsBuilder.append(remaining);
                    keywordsBuilder.append(keyword.getValue());
                    remaining = ", ";
                }
            }
            return keywordsBuilder.toString();
        }
        return null;
    }

    /**
     * Extract keywords list.
     * @param encodedString the encoded string
     * @return the keywords list
     */
    public static KeywordsList extractKeywordsList(
        final String encodedString) {
        final ArrayList<Keyword> keywordsList = new ArrayList<Keyword>();
        if (encodedString != null
            && encodedString.length() > 0) {
            final String[] groups = encodedString.split(";");
            for (final String group : groups) {
                if (group.indexOf(':') != -1) {
                    final String thesaurusId = group.substring(
                        0,
                        group.indexOf('~')).trim();
                    final String groupStr = group.substring(
                        group.indexOf('~') + 1).trim();
                    if (thesaurusId.length() > 0
                        && groupStr.length() > 0) {
                        final KeywordsThesaurus thesaurous =
                            new KeywordsThesaurousImpl(
                                thesaurusId,
                                null,
                                null);
                        final String[] keywords = groupStr.split(",");
                        for (String keyword : keywords) {
                            keyword = keyword.trim();
                            if (keyword.length() > 0) {
                                keywordsList.add(new KeywordImpl(
                                    keyword,
                                    thesaurous));
                            }
                        }
                    }
                } else {
                    final String[] keywords = group.split(",");
                    for (String keyword : keywords) {
                        keyword = keyword.trim();
                        if (keyword.length() > 0) {
                            keywordsList.add(new KeywordImpl(
                                keyword,
                                null));
                        }
                    }
                }
            }
        }
        return new KeywordsListImpl(
            keywordsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "string";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}

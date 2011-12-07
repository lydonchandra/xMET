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
import java.util.HashMap;
import java.util.Map;

import n.reporting.Reporting;
import xmet.profiles.codecs.KeywordsListCodec;
import xmet.profiles.keywords.Keyword;
import xmet.profiles.keywords.KeywordsList;
import xmet.profiles.keywords.KeywordsThesaurus;
import xmet.profiles.keywords.impl.KeywordImpl;
import xmet.profiles.keywords.impl.KeywordsListImpl;
import xmet.profiles.keywords.impl.KeywordsThesaurousImpl;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;

/**
 * ISO19115 keywords structure encoder/decoder.
 * @author Nahid Akbar
 */
public class ISO19115KeywordsCodec
    implements
    KeywordsListCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public KeywordsList extractKeywordsList(
        final Entity profileModelNode) {
        Reporting.logExpected(
            "hash %1$d",
            profileModelNode.hashCode());
        if (profileModelNode.getQualifiedName().equals(
            "gmd:descriptiveKeywords")) {
            final Entity repeatedE = profileModelNode.getParent();
            if (ModelUtils.isRepeated(repeatedE)) {
                return extractKeywordsListHelper(ModelUtils
                    .asRepeated(repeatedE));
            } else {
                Reporting.reportUnexpected(
                    "%1$s parent repeated entity not found",
                    profileModelNode.getQualifiedName());
            }
        } else {
            Reporting
                .logUnexpected("only gmd:descriptiveKeywords is supported");
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
        if (keywordsList != null) {
            if (profileModelNode.getQualifiedName().equals(
                "gmd:descriptiveKeywords")) {
                final Entity repeatedE = profileModelNode.getParent();
                if (ModelUtils.isRepeated(repeatedE)) {
                    encodeKeywordsListHelper(
                        keywordsList,
                        ModelUtils.asRepeated(repeatedE));
                } else {
                    Reporting.reportUnexpected(
                        "%1$s parent repeated entity not found",
                        profileModelNode.getQualifiedName());
                }
            } else {
                Reporting.logUnexpected("only gmd:extent is supported");
            }
        }
    }

    /**
     * Encode keywords list helper.
     * @param keywordList the keyword list
     * @param gmdDescriptiveKeywords the gmdDescriptive keywords
     */
    public static void encodeKeywordsListHelper(
        final KeywordsList keywordList,
        final Repeated gmdDescriptiveKeywords) {
        if (keywordList != null
            && gmdDescriptiveKeywords != null) {
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

            gmdDescriptiveKeywords.getEntities().clear();
            /* StringBuilder keywordsBuilder = new StringBuilder(); */
            /* String remaining = ""; */
            int i = 0;
            for (final Map.Entry<String, KeywordsThesaurus> thes : thesauruses
                .entrySet()) {
                final KeywordsThesaurus thesaurus = thes.getValue();
                final Entity gmdDescriptiveKeyword =
                    gmdDescriptiveKeywords.addNewEntity();
                final Settable gmdThesaurusNameIdentifier =
                    ModelUtils.hardTraceSetableXpath(
                        gmdDescriptiveKeyword,
                        "gmd:MD_Keywords"
                            + "/gmd:thesaurusName"
                            + "/gmd:CI_Citation"
                            + "/gmd:identifier"
                            + "/gmd:MD_Identifier"
                            + "/gmd:code"
                            + "/gco:CharacterString");
                gmdThesaurusNameIdentifier.setValue(thesaurus.getIdentifier());
                final Settable gmdThesaurusNameName =
                    ModelUtils.hardTraceSetableXpath(
                        gmdDescriptiveKeyword,
                        "gmd:MD_Keywords"
                            + "/gmd:thesaurusName"
                            + "/gmd:CI_Citation"
                            + "/gmd:title"
                            + "/gco:CharacterString");
                gmdThesaurusNameName.setValue(thesaurus.getTitle());
                if (gmdThesaurusNameName.getValue() == null
                    || gmdThesaurusNameName.getValue().trim().length() == 0) {
                    final Settable gmdThesaurusNameNameNilReason =
                        ModelUtils.hardTraceSetableXpath(
                            gmdDescriptiveKeyword,
                            "gmd:MD_Keywords"
                                + "/gmd:thesaurusName"
                                + "/gmd:CI_Citation"
                                + "/gmd:title@gco:nilReason");
                    gmdThesaurusNameNameNilReason.setValue("missing");
                }
                final Settable gmdThesaurusNameDateNilReason =
                    ModelUtils.hardTraceSetableXpath(
                        gmdDescriptiveKeyword,
                        "gmd:MD_Keywords"
                            + "/gmd:thesaurusName"
                            + "/gmd:CI_Citation"
                            + "/gmd:date@gco:nilReason");
                gmdThesaurusNameDateNilReason.setValue("missing");
                for (final Keyword keyword : keywordList.getKeywords()) {
                    if (keyword.getThesaurous() != null
                        && keyword.getThesaurous().equals(
                            thesaurus)) {
                        ++i;
                        final Settable gcoCharacterString =
                            ModelUtils.hardTraceSetableXpath(
                                gmdDescriptiveKeyword,
                                String.format(
                                    "gmd:MD_Keywords"
                                        + "/gmd:keyword[%1$s]"
                                        + "/gco:CharacterString",
                                    i));
                        gcoCharacterString.setValue(keyword.getValue());
                    }
                }
            }

            for (final Keyword keyword : keywordList.getKeywords()) {
                if (keyword.getThesaurous() == null) {
                    final Entity gmdDescriptiveKeyword =
                        gmdDescriptiveKeywords.addNewEntity();
                    final Settable gcoCharacterString =
                        ModelUtils.hardTraceSetableXpath(
                            gmdDescriptiveKeyword,
                            "gmd:MD_Keywords"
                                + "/gmd:keyword"
                                + "/gco:CharacterString");
                    gcoCharacterString.setValue(keyword.getValue());
                }
            }
        }

    }

    /**
     * Extract keywords list helper.
     * @param gmdDescriptiveKeywords the gmdDescriptive keywords
     * @return the keywords list
     */
    public static KeywordsList extractKeywordsListHelper(
        final Repeated gmdDescriptiveKeywords) {
        final ArrayList<Keyword> keywordsList = new ArrayList<Keyword>();
        if (gmdDescriptiveKeywords != null
            && gmdDescriptiveKeywords.getEntities().size() > 0) {

            for (final Entity gmdDescriptiveKeyword : gmdDescriptiveKeywords
                .getEntities()) {

                KeywordsThesaurus thesaurous = null;
                final Settable gmdThesaurusNameCode =
                    ModelUtils.softTraceSetableXpath(
                        gmdDescriptiveKeyword,
                        "gmd:MD_Keywords"
                            + "/gmd:thesaurusName"
                            + "/gmd:CI_Citation"
                            + "/gmd:identifier"
                            + "/gmd:MD_Identifier"
                            + "/gmd:code"
                            + "/gco:CharacterString");
                final Settable gmdThesaurusNameName =
                    ModelUtils.softTraceSetableXpath(
                        gmdDescriptiveKeyword,
                        "gmd:MD_Keywords"
                            + "/gmd:thesaurusName"
                            + "/gmd:CI_Citation"
                            + "/gmd:title"
                            + "/gco:CharacterString");
                if (gmdThesaurusNameCode != null) {
                    String varTitle = null;
                    if (gmdThesaurusNameName != null) {
                        varTitle = gmdThesaurusNameName.getValue();
                    } else {
                        varTitle = null;
                    }
                    thesaurous = new KeywordsThesaurousImpl(
                        gmdThesaurusNameCode.getValue(),
                        null,
                        varTitle);
                }
                /* extract keywords */
                final Entity gmdKeyWordTemp = ModelUtils.softTraceXPath(
                    gmdDescriptiveKeyword,
                    "gmd:MD_Keywords/gmd:keyword");
                if (gmdKeyWordTemp != null) {
                    final Repeated gmdKeyWords =
                        ModelUtils.asRepeated(gmdKeyWordTemp.getParent());
                    if (gmdKeyWords != null
                        && gmdKeyWords.getEntities().size() > 0) {
                        for (final Entity gmdKeyWord : gmdKeyWords
                            .getEntities()) {
                            final Settable gcoCharacterString =
                                ModelUtils.softTraceSetableXpath(
                                    gmdKeyWord,
                                    "gco:CharacterString");
                            if (gcoCharacterString != null
                                && gcoCharacterString.getValue() != null) {
                                keywordsList.add(new KeywordImpl(
                                    gcoCharacterString.getValue(),
                                    thesaurous));
                            }
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
        return "iso19115";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return "gmd:descriptiveKeywords";
    }

}

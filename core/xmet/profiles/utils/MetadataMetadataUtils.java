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
package xmet.profiles.utils;

import n.io.xml.JDOMXmlUtils;

import org.jdom.Element;

import xmet.profiles.Profile;

/**
 * This is a variant of MetadataModelMetadataUtil that works with JDOM Metadata
 * elements instead of Model.
 * @author Nahid Akbar
 * @see MetadataModelMetadataUtils
 */
public final class MetadataMetadataUtils {

    /**
     * Instantiates a new metadata metadata utils.
     */
    private MetadataMetadataUtils() {

    }

    /**
     * Extract uuid.
     * @param root the root
     * @param profile the profile
     * @return the string
     */
    public static String extractUUID(
        final Element root,
        final Profile profile) {
        if (profile.getUuidPaths().size() > 0) {
            for (final String uuidPath : profile.getUuidPaths()) {
                final Object e = JDOMXmlUtils.traceSimpleXpathItem(
                    root,
                    uuidPath);
                if (e != null) {
                    final String value = JDOMXmlUtils.getJdomItemTextTrim(e);
                    if ((value != null)
                        && (value.length() > 0)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Update uuid.
     * @param root the root
     * @param profile the profile
     * @param uuid the uuid
     */
    public static void updateUUID(
        final Element root,
        final Profile profile,
        final String uuid) {
        if (profile.getUuidPaths().size() > 0) {
            for (final String uuidPath : profile.getUuidPaths()) {
                final Object e = JDOMXmlUtils.traceSimpleXpathItem(
                    root,
                    uuidPath);
                if (e != null) {
                    JDOMXmlUtils.setJdomItemText(
                        e,
                        uuid);
                }
            }
        }
    }

    /**
     * Extract abstract.
     * @param root the root
     * @param profile the profile
     * @return the string
     */
    public static String extractAbstract(
        final Element root,
        final Profile profile) {
        if ((profile.getAbstractPath() != null)
            && (profile.getAbstractPath().length() > 0)) {
            final Object e = JDOMXmlUtils.traceSimpleXpathItem(
                root,
                profile.getAbstractPath());
            if (e != null) {
                return JDOMXmlUtils.getJdomItemTextTrim(e);
            }
        }
        return null;
    }

    /**
     * Extract title.
     * @param root the root
     * @param profile the profile
     * @return the string
     */
    public static String extractTitle(
        final Element root,
        final Profile profile) {
        if ((profile.getTitlePath() != null)
            && (profile.getTitlePath().length() > 0)) {
            final Object e = JDOMXmlUtils.traceSimpleXpathItem(
                root,
                profile.getTitlePath());
            if (e != null) {
                return JDOMXmlUtils.getJdomItemTextTrim(e);
            }
        }
        return null;
    }
}

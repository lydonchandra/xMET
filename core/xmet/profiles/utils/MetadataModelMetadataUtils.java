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

import java.util.UUID;

import xmet.profiles.Profile;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;

/**
 * Helper utility for extracting and optionally setting metadata elements from
 * metadata model.
 * @author Nahid Akbar
 */
public final class MetadataModelMetadataUtils {

    /**
     * Instantiates a new metadata model metadata utils.
     */
    private MetadataModelMetadataUtils() {

    }

    /**
     * Extracts uuid from data model. returns first good match
     * @param root the model
     * @param profile the profile
     * @return the string
     */
    public static String extractUUID(
        final Entity root,
        final Profile profile) {
        if (profile.getUuidPaths().size() > 0) {
            for (final String uuidPath : profile.getUuidPaths()) {
                final Entity e = ModelUtils.hardTraceXPath(
                    root,
                    uuidPath);
                if (e != null) {
                    final Settable setable = ModelUtils.getSetable(e);
                    if (setable != null) {
                        if ((setable.getValue() != null)
                            && (setable.getValue().trim().length() > 0)
                            && (UUID.fromString(setable.getValue()) != null)) {
                            return setable.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Update uuid on all the uuid paths set in profile.
     * @param root the model
     * @param profile the profile
     * @param uuid the uuid
     */
    public static void updateUUID(
        final Entity root,
        final Profile profile,
        final String uuid) {
        if (profile.getUuidPaths().size() > 0) {
            for (final String uuidPath : profile.getUuidPaths()) {
                final Entity e = ModelUtils.hardTraceXPath(
                    root,
                    uuidPath);
                if (e != null) {
                    final Settable setable = ModelUtils.getSetable(e);
                    if (setable != null) {
                        setable.setValue(uuid);
                    }
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
        final Entity root,
        final Profile profile) {
        if ((profile.getAbstractPath() != null)
            && (profile.getAbstractPath().length() > 0)) {
            final Entity e = ModelUtils.hardTraceXPath(
                root,
                profile.getAbstractPath());
            if (e != null) {
                final Settable setable = ModelUtils.getSetable(e);
                if (setable != null) {
                    return setable.getValue();
                }
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
        final Entity root,
        final Profile profile) {
        if ((profile.getTitlePath() != null)
            && (profile.getTitlePath().length() > 0)) {
            final Entity e = ModelUtils.hardTraceXPath(
                root,
                profile.getTitlePath());
            if (e != null) {
                final Settable setable = ModelUtils.getSetable(e);
                if (setable != null) {
                    return setable.getValue();
                }
            }
        }
        return null;
    }
}

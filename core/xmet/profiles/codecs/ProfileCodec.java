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

import java.util.Map;

import xmet.ClientContext;
import xmet.profiles.ProfileSchema;
import xmet.profiles.model.Entity;

/**
 * A Profile Codec.
 * @author Nahid Akbar
 */
public interface ProfileCodec
    extends
    Codec {

    /**
     * Load profile data model.
     * @param rootElementName the root element name
     * @param context TODO
     * @return the entity
     * @throws ProfileDecodingFailedException the schema parsing failed
     *             exception
     */
    Entity loadProfileModel(
        String rootElementName,
        ClientContext context)
        throws ProfileDecodingFailedException;

    /**
     * Sets the profile schemas.
     * @param profileSchemas the profile schemas
     */
    void setProfileSchemas(
        Map<String, ProfileSchema> profileSchemas);

    /**
     * Sets the element substitutions.
     * @param substitutions the substitutions
     */
    void setElementSubstitutions(
        Map<String, String> substitutions);
}

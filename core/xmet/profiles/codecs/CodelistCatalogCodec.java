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

import java.nio.ByteBuffer;

import xmet.ClientContext;
import xmet.profiles.catalogs.model.CodelistCatalog;

/**
 * Codec for decoding Codelist Catalogs.
 * @author Nahid Akbar
 */
public interface CodelistCatalogCodec
    extends
    Codec {

    /**
     * Decode catalog from content buffer.
     * @param catalogContents the catalog contents
     * @param catalogURL the catalog url
     * @param context the context
     * @return the codelist catalog
     */
    CodelistCatalog decode(
        ByteBuffer catalogContents,
        String catalogURL,
        ClientContext context);

    /**
     * Gets the supported filename extension.
     * @return the supported extension
     */
    String getSupportedExtension();
}

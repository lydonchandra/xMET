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
package xmet.profiles.catalogs.model;

import java.util.List;

/**
 * A Codelist catalog which consists of a list of Codelists.
 * @author Nahid Akbar
 */
public interface CodelistCatalog {

    /**
     * Gets the catalog items.
     * @return the catalog items
     */
    List<Codelist> getCodelists();

    /**
     * Gets the uRI.
     * @return the uRI
     */
    String getCatalogURI();

    /**
     * Gets the codelist by id.
     * @param identifier the part
     * @return the codelist by id
     */
    Codelist getCodelistByIdentifier(
        String identifier);
}

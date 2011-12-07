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
 * A codelist that consists of a list of CodelistItems.
 * @author Nahid Akbar
 */
public interface Codelist {

    /**
     * Gets the items.
     * @return the items
     */
    List<CodeItem> getItems();

    /**
     * Checks for identifier.
     * @param identifier the identifier
     * @return true, if successful
     */
    boolean hasIdentifier(
        String identifier);

    /**
     * Gets the codelist name.
     * @return the codelist name
     */
    String getCodelistName();

    /**
     * Gets the codelist url.
     * @return the codelist url
     */
    String getCodelistURL();

    /**
     * Sets the catalog url.
     * @param catalogURL the new catalog url
     */
    void setCatalogURL(
        String catalogURL);
}
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
package xmet.tools.metadata.manager;

/**
 * Allows plugin to have multiple pages of metadata files rather than a single
 * page.
 * @author Nahid Akbar
 */
public interface PagedMMGMTPlugin
    extends
    MMGMTPlugin {

    /**
     * Gets the pages count.
     * @return the pages count
     */
    int getPagesCount();

    /**
     * Gets the current page index of whatever is returned by getRoot().
     * @return the current page index
     */
    int getCurrentPageIndex();

    /**
     * Sets the current page index of whatever should be returned by getRoot().
     * @param index the new current page index
     */
    void setCurrentPageIndex(
        int index);
}

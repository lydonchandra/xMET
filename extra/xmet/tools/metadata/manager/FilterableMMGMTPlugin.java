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
 * A metadata management plugin that can filter its files and folders based on
 * filter text.
 * @author Nahid Akbar
 */
public interface FilterableMMGMTPlugin
    extends
    MMGMTPlugin {

    /**
     * Note: result tree should be returned through the usual getRoot() <br />
     * Preferred if the result tree had the minimum skeleton of folders just
     * containing the results.
     * @param filterText filter words. seperated by spaces
     * @param caseSensitive whether to do a case sensiive search or not
     * @return false if no results found. true if results found
     */
    boolean filterMetadata(
        String filterText,
        boolean caseSensitive);

    /**
     * Clear filter setting. I.e. getRoot() should return the original root now
     */
    void resetMetadataFilter();
}

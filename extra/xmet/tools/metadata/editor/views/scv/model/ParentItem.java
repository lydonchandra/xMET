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
package xmet.tools.metadata.editor.views.scv.model;

import n.utils.TreeChildrenArrayList;

/**
 * The Interface implemented by Group and Page and Sheet who are more or less
 * parent elements of other elements.
 * @param <E> the element types of children
 * @author Nahid Akbar
 */
public interface ParentItem<E>
    extends
    ModelItem {

    /**
     * Gets the items.
     * @return the items
     */
    TreeChildrenArrayList<E> getItems();
}

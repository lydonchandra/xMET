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

/**
 * Represents anything that repeats for a "Repeated" item in our xml model.
 * @param <E> the element type that repeats. e.g. its Group for a RepeatedGroup
 * @author Nahid Akbar
 */
public interface RepeatedItem<E>
    extends
    ModelItem {

    /**
     * Gets the base.
     * @return the base
     */
    String getBase();

    /**
     * Gets the label.
     * @return the label
     */
    String getLabel();

}

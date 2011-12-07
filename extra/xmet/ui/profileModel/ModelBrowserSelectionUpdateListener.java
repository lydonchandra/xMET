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
package xmet.ui.profileModel;

/**
 * The listener interface for receiving modelBrowserSelectionUpdate events. The
 * class that is interested in processing a modelBrowserSelectionUpdate event
 * implements this interface, and the object created with that class is
 * registered with a component using the component's
 * <code>addModelBrowserSelectionUpdateListener</code> method. When the
 * modelBrowserSelectionUpdate event occurs, that object's appropriate method is
 * invoked.
 * @author Nahid Akbar
 */
public interface ModelBrowserSelectionUpdateListener {

    /**
     * Selected value changed.
     * @param parent the parent
     * @param newValue the new value
     */
    void selectedValueChanged(
        Object parent,
        Object newValue);

}

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
package xmet.ui.controls.spatialExtent;

import java.awt.Component;

import xmet.profiles.geometry.impl2d.SpatialExtent2d;

/**
 * Represents a particular editor interface for editing spatial extent
 * information.
 * @author Nahid Akbar
 */
public interface SpatialExtentView {

    /**
     * Update. - called before anything else - this is meant for filling the
     * editor with the latest extent information.
     * @param extent the extent
     */
    void update(
        SpatialExtent2d extent);

    /**
     * Commit - this is called to save editing changes and return the extent.
     * @return the spatial extent2d
     */
    SpatialExtent2d commit();

    /**
     * Gets the display control.
     * @return the uI
     */
    Component getUI();

    /**
     * Draw point callback.
     */
    void drawPointCallback();

    /**
     * Draw poly line callback.
     */
    void drawPolyLineCallback();

    /**
     * Draw bounding box callback.
     */
    void drawBoundingBoxCallback();

    /**
     * Draw surface callback.
     */
    void drawSurfaceCallback();

    /**
     * Draw surface hole callback.
     */
    void drawSurfaceHoleCallback();

    /**
     * Draw code callback.
     */
    void drawCodeCallback();

    /**
     * Delete item callback.
     */
    void deleteItemCallback();

    /**
     * Checks for if view has configuration dialog.
     * @return true, if successful
     */
    boolean hasConfigurationDialog();

    /**
     * Show coniguration dialog callback.
     */
    void showConigurationDialogCallback();
}

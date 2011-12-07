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
package xmet.ui.mapping;

import java.awt.event.ActionListener;

import xmet.profiles.geometry.SpatialExtent;

/**
 * The user case related to editing SpatialExtentObjects.
 * @author Nahid Akbar
 */
public interface SpatialExtentListModifierMMUC
    extends
    MappingModuleUseCase {

    /**
     * Sets the extent.
     * @param extent the new extent
     */
    void setExtent(
        SpatialExtent extent);

    /**
     * Gets the extent.
     * @return the extent
     */
    SpatialExtent getExtent();

    /**
     * Adds the point callback.
     */
    void addPointCallback();

    /**
     * Adds the poly line callback.
     */
    void addPolyLineCallback();

    /**
     * Adds the poly gon callback.
     */
    void addPolyGonCallback();

    /**
     * Adds the bounding box callback.
     */
    void addBoundingBoxCallback();

    /**
     * Adds the poly gon hole callback.
     */
    void addPolyGonHoleCallback();

    /**
     * Sets the change notification listener.
     * @param listener the new change notification listener
     */
    void setChangeNotificationListener(
        ActionListener listener);

    /**
     * Delete callback.
     */
    void deleteCallback();

    /**
     * Show configuration callback.
     */
    void showConfigurationCallback();
}

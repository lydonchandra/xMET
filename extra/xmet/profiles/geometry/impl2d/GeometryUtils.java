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
package xmet.profiles.geometry.impl2d;

import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SpatialExtent;

/**
 * A list of helper methods for creating various geometric objects.
 * @author Nahid Akbar
 */
public final class GeometryUtils {

    /**
     * Instantiates a new geometry utils.
     */
    private GeometryUtils() {

    }

    /**
     * Create2 d bounding box surface.
     * @param north the north
     * @param east the east
     * @param south the south
     * @param west the west
     * @param inclusive the inclusive
     * @return the bounding box2d
     */
    public static BoundingBox2d create2DBoundingBoxSurface(
        final double north,
        final double east,
        final double south,
        final double west,
        final boolean inclusive) {
        return new BoundingBox2d(
            new Point2d(
                west,
                north),
            new Point2d(
                east,
                south),
            inclusive);
    }

    /**
     * Checks if is 2 d polygon.
     * @param extent the extent
     * @return true, if is 2 d polygon
     */
    public static boolean is2DPolygon(
        final SpatialExtent extent) {
        if (extent != null) {
            if (extent.getShapes().size() == 1) {
                final Shape shape = extent.getShapes().get(
                    0);
                if (shape instanceof Surface2d) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the 2 d polygon.
     * @param extent the extent
     * @return the 2 d polygon
     */
    public static Surface2d get2DPolygon(
        final SpatialExtent extent) {
        if (extent != null) {
            if (extent.getShapes().size() == 1) {
                final Shape shape = extent.getShapes().get(
                    0);
                if (shape instanceof Surface2d) {
                    return (Surface2d) shape;
                }
            }
        }
        return null;
    }
}
